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

package prop6.types;

import java.util.ArrayList;
import java.util.TreeSet;

import esl2.types.Vector;

public final class Model
{

    public final SimObject object;
    public final State state;
    public boolean priority;

    public final ArrayList<Model> realizedChildren;
    public final ArrayList<LogMessage> messages;

    // These are the children that we have been told to realize this step.
    // This list is an optimization to not initiate event searching.
    public final ArrayList<SimObject> realizedThisStep;
    // These are the children who need to be realized, but their condition has passed.
    // This list initiates event searching.
    public final ArrayList<SimObject> realizePassedThisStep;
    // Priority realization list.
    public final ArrayList<SimObject> pRealizedThisStep;
    // These take precedence over pRealizedThisStep in determining who gets priority.
    public final ArrayList<SimObject> pRealizePassedThisStep;

    // This is like realization, but for termination.
    public boolean terminateThisStep;
    public boolean terminatePassedThisStep;

    // This is like the previous, but for user-defined events.
    public boolean eventThisStep;
    public boolean eventPassedThisStep;
    public double eventHint;

    public boolean instantKick;
    public Vector velocityKick;
    public Vector ratesKick;

    // List of specific times that the propagator needs to hit.
    public final TreeSet<Double> stepTimeToHitList;

    // Data for the integrator.

    // Current propagation time, in hairs.
    public double currentTime;
    // Time to step, in hairs.
    public double deltaTime;
    // Last commanded adaptively-sized step, in hairs.
    public double prevDelta;
    // Was the error of this step within bounds?
    public boolean success;
    // The number of good steps for determining if we should increase the step size.
    public int numGoodSteps;
    // The cumulative number of bad steps for this object.
    public int numBadSteps;

    public Model()
    {
        object = new SimObject();
        state = new State();
        realizedChildren = new ArrayList<Model>();
        messages = new ArrayList<LogMessage>();
        realizedThisStep = new ArrayList<SimObject>();
        realizePassedThisStep = new ArrayList<SimObject>();
        pRealizedThisStep = new ArrayList<SimObject>();
        pRealizePassedThisStep = new ArrayList<SimObject>();
        terminateThisStep = false;
        terminatePassedThisStep = false;
        eventThisStep = false;
        eventPassedThisStep = false;
        eventHint = 0.0;
        instantKick = false;
        velocityKick = new Vector(0.0, 0.0, 0.0);
        ratesKick = velocityKick;
        stepTimeToHitList = new TreeSet<Double>();
        currentTime = 0.0;
        deltaTime = 0.0;
        prevDelta = 0.0;
        success = false;
        numGoodSteps = 0;
        numBadSteps = 0;
    }

    public Model(SimObject object, State state, boolean onCriticalPath, double currentTime, double firstStep)
    {
        this.object = object;
        this.state = state;
        this.priority = onCriticalPath;
        realizedChildren = new ArrayList<Model>();
        messages = new ArrayList<LogMessage>();
        realizedThisStep = new ArrayList<SimObject>();
        realizePassedThisStep = new ArrayList<SimObject>();
        pRealizedThisStep = new ArrayList<SimObject>();
        pRealizePassedThisStep = new ArrayList<SimObject>();
        terminateThisStep = false;
        terminatePassedThisStep = false;
        eventThisStep = false;
        eventPassedThisStep = false;
        eventHint = 0.0;
        instantKick = false;
        velocityKick = new Vector(0.0, 0.0, 0.0);
        ratesKick = velocityKick;
        stepTimeToHitList = new TreeSet<Double>();
        this.currentTime = currentTime;
        deltaTime = firstStep;
        prevDelta = firstStep;
        success = false;
        numGoodSteps = 0;
        numBadSteps = 0;
    }

    public Model(Model src)
    {
        object = new SimObject();
        state = new State();
        realizedChildren = new ArrayList<Model>();
        messages = new ArrayList<LogMessage>();
        realizedThisStep = new ArrayList<SimObject>();
        realizePassedThisStep = new ArrayList<SimObject>();
        pRealizedThisStep = new ArrayList<SimObject>();
        pRealizePassedThisStep = new ArrayList<SimObject>();
        stepTimeToHitList = new TreeSet<Double>();
        copyFromHere(src);
    }

    public void copyFromHere(Model src)
    {
        object.copyFromHere(src.object);
        state.copyFromHere(src.state);
        priority = src.priority;

        realizedChildren.clear();
        realizedChildren.addAll(src.realizedChildren);
        messages.clear();
        messages.addAll(src.messages);

        realizedThisStep.clear();
        realizedThisStep.addAll(src.realizedThisStep);
        realizePassedThisStep.clear();
        realizePassedThisStep.addAll(src.realizePassedThisStep);
        pRealizedThisStep.clear();
        pRealizedThisStep.addAll(src.pRealizedThisStep);
        pRealizePassedThisStep.clear();
        pRealizePassedThisStep.addAll(pRealizePassedThisStep);

        terminateThisStep = src.terminateThisStep;
        terminatePassedThisStep = src.terminatePassedThisStep;

        eventThisStep = src.eventThisStep;
        eventPassedThisStep = src.eventPassedThisStep;
        eventHint = src.eventHint;

        instantKick = src.instantKick;
        velocityKick = src.velocityKick;
        ratesKick = src.ratesKick;

        stepTimeToHitList.clear();
        stepTimeToHitList.addAll(src.stepTimeToHitList);

        currentTime = src.currentTime;
        deltaTime = src.deltaTime;
        prevDelta = src.prevDelta;
        success = src.success;
        numGoodSteps = src.numGoodSteps;
        numBadSteps = src.numBadSteps;
    }

    public Model duplicate()
    {
        return new Model(this);
    }

}
