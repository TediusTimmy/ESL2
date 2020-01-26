 /*
   Copyright (C) 2017 Thomas DiModica <ricinwich@yahoo.com>

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package prop6.engine;

import java.util.ArrayList;

import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.Vector;
import prop6.types.SimObject;
import prop6.types.Model;

public final class Propagator
{

    public final Stepper stepper;
    public final double lowerBounds;
    public final double upperBounds;
    public final double checkpoint;

    public Propagator(Stepper stepper, double lowerBounds, double upperBounds, double checkpoint)
    {
        this.stepper = stepper;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.checkpoint = checkpoint;
    }

    public void propagate(ArrayList<Model> output, CallingContext context, Model input, boolean pRealizeIsTerminate) throws FatalException, TypedOperationException
    {
        final double startTime = input.currentTime;
        output.add(new Model(input));
        context.pushObject(input.object);
        context.pushModel(input);
        try
        {
            while ((false == input.terminatePassedThisStep) && (false == input.terminateThisStep))
            {
                // Step it forward.
                stepper.step(context, input);

                // Handle adaptive step sizing.
                adaptStep(context, input, output.get(output.size() - 1));

                // Did we pass any events that we need to seek for.
                checkPassedEvents(context, input, output.get(output.size() - 1));
                
                // Are we doing a Priority-only run and have realized the next priority object?
                // NOTE: if pRealizeIsTerminate is true, we assume input.priority is also true.
                if ((true == pRealizeIsTerminate) &&
                    ((false == input.pRealizePassedThisStep.isEmpty()) || (false == input.pRealizedThisStep.isEmpty())))
                {
                    input.terminateThisStep = true;
                }

                // Flag if an event occurred this step. Need to do before realizations, which clears the lists.
                input.eventThisStep = (true == input.eventThisStep) || (true == input.eventPassedThisStep) ||
                    (true == input.terminateThisStep) || (true == input.terminatePassedThisStep) ||
                    (false == input.pRealizedThisStep.isEmpty()) || (false == input.pRealizePassedThisStep.isEmpty()) ||
                    (false == input.realizedThisStep.isEmpty()) || (false == input.realizePassedThisStep.isEmpty()) ||
                    (true == input.instantKick);
                // Clean up these flags.
                input.eventPassedThisStep = false;
                input.eventHint = 0.0;

                // Process realization lists.
                doRealizations(context, input);

                // Add in instantaneous kick
                if (true == input.instantKick)
                {
                    input.state.velocity = input.state.velocity.add(input.state.orientation.rotateVectorInverseAssumeUnit(input.velocityKick));
                    input.state.angularVelocity = input.state.angularVelocity.add(input.state.orientation.rotateVectorInverseAssumeUnit(input.ratesKick));
                    input.velocityKick = new Vector(0.0, 0.0, 0.0);
                    input.ratesKick = input.velocityKick;
                    input.instantKick = false;

                    Stepper.update(input.state, context.settings.earth);
                    // The model's position didn't change, so it's gravity force didn't.
                }

                // Set input.deltaTime to the appropriate time for the next integration step.
                adaptNextStepTime(input);

                // Save state
                double test = output.get(output.size() - 1).currentTime / checkpoint;
                if ((startTime == output.get(output.size() - 1).currentTime) || (true == output.get(output.size() - 1).eventThisStep) ||
                    (0.0 == (test - Math.floor(test))))
                { // Previous state is the first state, contains an event, or is a checkpoint. Save it.
                    output.add(new Model(input));
                }
                else
                {
                    output.get(output.size() - 1).copyFromHere(input);
                }

                // Check for time out of range
                if (input.currentTime > Stepper.MAX_TIME)
                {
                    throw new FatalException("Propagation exceeded max allowable time.");
                }
            }
        }
        finally
        {
            context.popModel();
            context.popObject();
        }
    }

        // Post-Execution Invariant :
        //    After the loop, "input" is the largest step
        //    that the integrator will take this iteration.
        //    In addition lowerBounds <= deltaTime <= upperBounds, or
        //    the loop never executed.
    private void adaptStep(CallingContext context, Model input, Model preStep) throws FatalException, TypedOperationException
    {
        double stepSize = input.deltaTime;
        // Assume that if deltaTime A is a successful step, then any deltaTime < A will also be successful.
        //    I know this to be false... but we sometimes need some assumptions.
        // If we cannot shrink this step any more, then break out.
        while ((false == input.success) && (input.deltaTime > lowerBounds))
        {
            // Reload previous state
            input.copyFromHere(preStep);
            // Adjust time
            stepSize = Math.max(Math.floor(stepSize * 0.25), lowerBounds);
            input.deltaTime = stepSize;
            input.prevDelta = stepSize;
            // Retake step
            stepper.step(context, input);
        }
    }

    public static boolean passedEvent(Model input)
    {
        if ((false == input.pRealizePassedThisStep.isEmpty()) ||
            (false == input.realizePassedThisStep.isEmpty()) ||
            (true == input.terminatePassedThisStep) ||
            (true == input.eventPassedThisStep))
        {
            return true;
        }
        return false;
    }

    public static boolean hitEvent(Model input)
    {
        if ((false == input.pRealizedThisStep.isEmpty()) ||
            (false == input.realizedThisStep.isEmpty()) ||
            (true == input.terminateThisStep) ||
            (true == input.eventThisStep))
        {
            return true;
        }
        return false;
    }

        // Post-Execution Invariant :
        //    Either deltaTime is 1 or
        //    a generic, terminate, or realization event is hit and no event is passed, or
        //    an event is passed, but no event is hit or passed with step deltaTime - 1.
    private void checkPassedEvents(CallingContext context, Model input, Model preStep) throws FatalException, TypedOperationException
    {
        if (true == passedEvent(input))
        {
            double bottom = 0.0, top = input.deltaTime, next = 0.0;
            boolean stopNow = false;
            Model result = new Model(input);

            // Seek for the event using binary splitting.
            while (((top - bottom) > 1.0) && (false == stopNow))
            {
                // If there is a hint, use it (we won't get stale hints, because hint will be zero in preStep)
                // Hints allow use to improve the performance of the algorithm:
                //    A binary search has an average and worst-case runtime of log(n).
                //    An interpolation search has an average of log(log(n)), but a worst-case of n.
                //    For 100Hz integration and nanosecond precision, n = 10^7.
                //    So binary search has an average and worst-case of 24,
                //    while interpolation search has an average of 5 and a worst-case of 10^7.
                if ((0.0 != input.eventHint) && (input.eventHint > (preStep.currentTime + bottom)))
                {
                    next = input.eventHint - preStep.currentTime;
                }
                else
                {
                    next = Math.floor((top + bottom) * 0.5);
                }

                // Copy the base object
                input.copyFromHere(preStep);

                // Propagate it forward by next guess
                input.deltaTime = next;
                stepper.step(context, input);

                if (true == passedEvent(input))
                {
                    // If we have passed an event, then this is the new largest step we can take,
                    // and possibly the state that will be returned.
                    result.copyFromHere(input);
                    top = input.deltaTime;
                }
                else
                {
                    if (true == hitEvent(input))
                    {
                        // No event is passed, and an event is hit. We can stop now and use this state.
                        result.copyFromHere(input);
                        stopNow = true;
                    }
                    else
                    {
                        bottom = input.deltaTime;
                    }
                }
            }

            input.copyFromHere(result);
        }
    }

    public static void wrappedCall(CallingContext context, Model model, CallWrapper function) throws FatalException, TypedOperationException
    {
        context.pushObject(model.object);
        context.pushModel(model);
        try
        {
            function.execute(context, model.object.realizeArg);
        }
        finally
        {
            context.popModel();
            context.popObject();
        }
    }

    public static void applyOffsets(Model parent, Model child) throws FatalException, TypedOperationException 
    {
        child.state.mass_kg = Stepper.getDouble(child.object.variables.get(SimObject.INITIAL_MASS_INDEX));
        parent.state.mass_kg -= child.state.mass_kg;
        if (parent.state.mass_kg < 0.0)
        {
            throw new FatalException("Object mass went negative during a realization.");
        }

        child.state.position = parent.state.position.add(parent.state.orientation.rotateVectorInverseAssumeUnit(
            Stepper.getVector(child.object.variables.get(SimObject.POSITION_OFFSET_INDEX))));
        child.state.velocity = parent.state.velocity.add(parent.state.orientation.rotateVectorInverseAssumeUnit(
            Stepper.getVector(child.object.variables.get(SimObject.VELOCITY_OFFSET_INDEX))));

        child.state.angularVelocity = parent.state.angularVelocity.add(Stepper.getVector(child.object.variables.get(SimObject.RATES_OFFSET_INDEX)));
        child.state.orientation = Stepper.getQuaternion(child.object.variables.get(SimObject.ORIENTATION_OFFSET_INDEX)).mul(parent.state.orientation);
    }

    public static void processRealizationListNP(ArrayList<SimObject> list, ArrayList<Model> realizedThisStep, CallingContext context, Model model, double upperBounds)
        throws FatalException, TypedOperationException
    {
        for (SimObject iter : list)
        {
            model.realizedChildren.add(new Model(iter, model.state.duplicate(), false, model.currentTime, upperBounds));
            realizedThisStep.add(model.realizedChildren.get(model.realizedChildren.size() - 1));
            wrappedCall(context, realizedThisStep.get(model.realizedChildren.size() - 1), realizedThisStep.get(model.realizedChildren.size() - 1).object.onModelInit);
            applyOffsets(model, realizedThisStep.get(model.realizedChildren.size() - 1));
        }
        list.clear();
    }

    private void doRealizations(CallingContext context, Model input) throws FatalException, TypedOperationException
    {
        ArrayList<Model> realizedThisStep = new ArrayList<Model>();

        // First, handle a priority child.
        if (true == input.priority)
        {
            if (false == input.pRealizePassedThisStep.isEmpty())
            {
                input.realizedChildren.add(0, new Model(input.pRealizePassedThisStep.get(0), input.state.duplicate(), true, input.currentTime, upperBounds));
                realizedThisStep.add(input.realizedChildren.get(0));
                input.pRealizePassedThisStep.remove(0);
            }
            else if (false == input.pRealizedThisStep.isEmpty())
            {
                input.realizedChildren.add(0, new Model(input.pRealizedThisStep.get(0), input.state.duplicate(), true, input.currentTime, upperBounds));
                realizedThisStep.add(input.realizedChildren.get(0));
                input.pRealizedThisStep.remove(0);
            }

            // If there was a priority realization, initialize the model.
            if (false == realizedThisStep.isEmpty())
            {
                input.priority = false;
                wrappedCall(context, realizedThisStep.get(0), realizedThisStep.get(0).object.onModelInit);
                applyOffsets(input, realizedThisStep.get(0));
            }
        }

        // Next, all non-priority children.
        processRealizationListNP(input.pRealizePassedThisStep, realizedThisStep, context, input, upperBounds);
        processRealizationListNP(input.pRealizedThisStep, realizedThisStep, context, input, upperBounds);
        processRealizationListNP(input.realizePassedThisStep, realizedThisStep, context, input, upperBounds);
        processRealizationListNP(input.realizedThisStep, realizedThisStep, context, input, upperBounds);

        // Loop through all objects and inform of realization.
        for (Model iter : realizedThisStep)
        {
            // First, ensure they have a consistent state.
            Stepper.update(iter.state, context.settings.earth);
            Stepper.updateGravity(iter, context.settings.earth);
            wrappedCall(context, iter, iter.object.onModelRealize);
        }

        // If we realized anything, flag it and clear the list.
        if (false == realizedThisStep.isEmpty())
        {
            input.eventThisStep = true;
        }
    }

        // Set input.deltaTime to either:
        //    the next integration step size (prevDelta, which may be modified before use) or
        //    the next checkPoint time or
        //    the next item in the stepTimeToHit list.
    private void adaptNextStepTime(Model input)
    {
        // If we are set up to do something in the event that we have to accept a bad step, do it here.
        if (false == input.success)
        {
            // TODO : Propagator options on a bad step.
            ++input.numBadSteps;
        }
        // Did we actually use the integrator step?
        if (input.prevDelta == input.deltaTime)
        {
            input.prevDelta = Math.min(Math.floor(input.prevDelta * 1.5), upperBounds);
        }
        final double deltaFromIntegrator = input.prevDelta;

        // Compute the time delta to the next checkpoint.
        final double deltaFromCheckpoint = (Math.floor(input.currentTime / Stepper.HAIRS_PER_SECOND) + 1.0) * Stepper.HAIRS_PER_SECOND - input.currentTime;

        // Compute the time delta to the next item on the hit list, or the integrator delta if not applicable.
        double deltaFromHitList = deltaFromIntegrator;
        // First, purge the list of passed events.
        while ((false == input.stepTimeToHitList.isEmpty()) && (input.stepTimeToHitList.first().doubleValue() <= input.currentTime))
        {
            input.stepTimeToHitList.remove(input.stepTimeToHitList.first());
        }
        if (false == input.stepTimeToHitList.isEmpty())
        {
            deltaFromHitList = input.stepTimeToHitList.first().doubleValue() - input.currentTime;
        }

        // Set deltaTime: this is an unrolled binary search
        if (deltaFromIntegrator < deltaFromCheckpoint)
        {
            if (deltaFromIntegrator < deltaFromHitList)
            {
                input.deltaTime = deltaFromIntegrator;
            }
            else
            {
                input.deltaTime = deltaFromHitList;
            }
        }
        else // use deltaFromCheckpoint
        {
            if (deltaFromCheckpoint < deltaFromHitList)
            {
                input.deltaTime = deltaFromCheckpoint;
            }
            else
            {
                input.deltaTime = deltaFromHitList;
            }
        }
    }

}
