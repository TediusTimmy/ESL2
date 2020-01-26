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

package prop6.input;

import java.util.List;

import esl2.engine.Executor;
import esl2.parser.FrameDebugInfo;
import esl2.parser.FunctionPairs;
import prop6.engine.stdlib.*;
import prop6.engine.stdlib.Error;

public class ExecutorBuilder extends esl2.parser.ExecutorBuilder
{

    public static void createDefaultFunctions(Executor executor, FunctionPairs funs, List<FrameDebugInfo> frameInfo)
    {
        esl2.parser.ExecutorBuilder.createDefaultFunctions(executor, funs, frameInfo);

            // First, overwrite the default logging functions with specialized ones.
        executor.functions.set(funs.funs.get("Fatal").intValue(), new Fatal());
        executor.functions.set(funs.funs.get("Error").intValue(), new Error());
        executor.functions.set(funs.funs.get("Warn").intValue(), new Warn());
        executor.functions.set(funs.funs.get("Info").intValue(), new Info());

            // 17
        addFunction("Rand", new Rand(), 0, executor, funs, frameInfo);
        addFunction("GetCurrentState", new GetCurrentState(), 0, executor, funs, frameInfo);
        addFunction("TerminateNow", new TerminateNow(), 0, executor, funs, frameInfo);
        addFunction("TerminatePassed", new TerminatePassed(), 0, executor, funs, frameInfo);
        addFunction("EventHit", new EventHit(), 0, executor, funs, frameInfo);
        addFunction("EventPassed", new EventPassed(), 0, executor, funs, frameInfo);
        addFunction("GetRotationRate_deg_s", new GetRotationRate_deg_s(), 0, executor, funs, frameInfo);
        addFunction("Getg0_m_s2", new Getg0_m_s2(), 0, executor, funs, frameInfo);
        addFunction("Getmu_Nm2_kg", new Getmu_Nm2_kg(), 0, executor, funs, frameInfo);
        addFunction("GetPolarRadius_m", new GetPolarRadius_m(), 0, executor, funs, frameInfo);
        addFunction("GetEquatorialRadius_m", new GetEquatorialRadius_m(), 0, executor, funs, frameInfo);
        addFunction("GetFlattening", new GetFlattening(), 0, executor, funs, frameInfo);
        addFunction("GetEccentricity", new GetEccentricity(), 0, executor, funs, frameInfo);
        addFunction("ECIToECRQuat", new ECIToECRQuat(), 0, executor, funs, frameInfo);
        addFunction("ECRToECIQuat", new ECRToECIQuat(), 0, executor, funs, frameInfo);
        addFunction("GetCurrentObject", new GetCurrentObject(), 0, executor, funs, frameInfo);
        addFunction("EnterDebugger", new EnterDebugger(), 0, executor, funs, frameInfo);

            // 37
        addFunction("DebugPrint", new DebugPrint(), 1, executor, funs, frameInfo);
        addFunction("GetTime_s", new GetTime_s(), 1, executor, funs, frameInfo);
        addFunction("GetPosition_m", new GetPosition_m(), 1, executor, funs, frameInfo);
        addFunction("GetVelocity_m_s", new GetVelocity_m_s(), 1, executor, funs, frameInfo);
        addFunction("GetAcceleration_m_s2", new GetAcceleration_m_s2(), 1, executor, funs, frameInfo);
        addFunction("GetMass_kg", new GetMass_kg(), 1, executor, funs, frameInfo);
        addFunction("GetFlowRate_kg_s", new GetFlowRate_kg_s(), 1, executor, funs, frameInfo);
        addFunction("GetOrientation_quat", new GetOrientation_quat(), 1, executor, funs, frameInfo);
        addFunction("GetAngularVelocity_deg_s", new GetAngularVelocity_deg_s(), 1, executor, funs, frameInfo);
        addFunction("GetAngularAcceleration_deg_s2", new GetAngularAcceleration_deg_s2(), 1, executor, funs, frameInfo);
        addFunction("GetMOI", new GetMOI(), 1, executor, funs, frameInfo);
        addFunction("GetIdot", new GetIdot(), 1, executor, funs, frameInfo);
        addFunction("GetPositionECR_m", new GetPositionECR_m(), 1, executor, funs, frameInfo);
        addFunction("GetVelocityECR_m_s", new GetVelocityECR_m_s(), 1, executor, funs, frameInfo);
        addFunction("GetLLH_deg_deg_m", new GetLLH_deg_deg_m(), 1, executor, funs, frameInfo);
        addFunction("GetSpeedOfSound_m_s", new GetSpeedOfSound_m_s(), 1, executor, funs, frameInfo);
        addFunction("GetDensity_kg_m3", new GetDensity_kg_m3(), 1, executor, funs, frameInfo);
        addFunction("GetPressure_Pa", new GetPressure_Pa(), 1, executor, funs, frameInfo);
        addFunction("GetTemperature_K", new GetTemperature_K(), 1, executor, funs, frameInfo);
        addFunction("GetDynamicPressure_Pa", new GetDynamicPressure_Pa(), 1, executor, funs, frameInfo);
        addFunction("GetMach", new GetMach(), 1, executor, funs, frameInfo);
        addFunction("GetCurrentGravity_N", new GetCurrentGravity_N(), 1, executor, funs, frameInfo);
        addFunction("GetPositionOffsetWRTParent_m", new GetPositionOffsetWRTParent_m(), 1, executor, funs, frameInfo);
        addFunction("GetVelocityOffsetWRTParent_m_s", new GetVelocityOffsetWRTParent_m_s(), 1, executor, funs, frameInfo);
        addFunction("GetOrientationOffsetWRTParent_quat", new GetOrientationOffsetWRTParent_quat(), 1, executor, funs, frameInfo);
        addFunction("GetRatesOffsetWRTParent_deg_s", new GetRatesOffsetWRTParent_deg_s(), 1, executor, funs, frameInfo);
        addFunction("GetCGOffset_m", new GetCGOffset_m(), 1, executor, funs, frameInfo);
        addFunction("GetName", new GetName(), 1, executor, funs, frameInfo);
        addFunction("EventHint", new EventHint(), 1, executor, funs, frameInfo);
        addFunction("StopAt", new StopAt(), 1, executor, funs, frameInfo);
        addFunction("ECRToLLH", new ECRToLLH(), 1, executor, funs, frameInfo);
        addFunction("LLHToECR", new LLHToECR(), 1, executor, funs, frameInfo);
        addFunction("IsState", new IsState(), 1, executor, funs, frameInfo);
        addFunction("IsObject", new IsObject(), 1, executor, funs, frameInfo);
        addFunction("Quantize", new Quantize(), 1, executor, funs, frameInfo);
        addFunction("SimAllPriority", new SimAllPriority(), 1, executor, funs, frameInfo);

            // 14
        addFunction("CreateObject", new CreateObject(), 2, executor, funs, frameInfo);
        addFunction("RealizeNow", new RealizeNow(), 2, executor, funs, frameInfo);
        addFunction("RealizeNowFirst", new RealizeNowFirst(), 2, executor, funs, frameInfo);
        addFunction("RealizePassed", new RealizePassed(), 2, executor, funs, frameInfo);
        addFunction("RealizePassedFirst", new RealizePassedFirst(), 2, executor, funs, frameInfo);
        addFunction("ECIToECR", new ECIToECR(), 2, executor, funs, frameInfo);
        addFunction("ECIToLLH", new ECIToLLH(), 2, executor, funs, frameInfo);
        addFunction("ECRToECI", new ECRToECI(), 2, executor, funs, frameInfo);
        addFunction("LLHToECI", new LLHToECI(), 2, executor, funs, frameInfo);
        addFunction("InverseGeo", new InverseGeo(), 2, executor, funs, frameInfo);
        addFunction("CloneObject", new CloneObject(), 2, executor, funs, frameInfo);
        addFunction("SimToTerm", new SimToTerm(), 2, executor, funs, frameInfo);
        addFunction("SimPriority", new SimPriority(), 2, executor, funs, frameInfo);
        addFunction("InstantKick", new InstantKick(), 2, executor, funs, frameInfo);

            // 2
        addFunction("ECIToECRVel", new ECIToECRVel(), 3, executor, funs, frameInfo);
        addFunction("ECRToECIVel", new ECRToECIVel(), 3, executor, funs, frameInfo);

            // 2
        addFunction("ECIToECRAcc", new ECIToECRAcc(), 4, executor, funs, frameInfo);
        addFunction("ECRToECIAcc", new ECRToECIAcc(), 4, executor, funs, frameInfo);
    }

    public static void setNoThreads(Executor executor, FunctionPairs funs, List<FrameDebugInfo> frameInfo)
    {
            // Overwrite the only multi-threaded function with its single-threaded counterpart.
        executor.functions.set(funs.funs.get("SimAllPriority").intValue(), new SimAllPriorityST());
    }

}
