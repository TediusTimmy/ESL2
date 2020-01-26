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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import esl2.input.Lexer;
import esl2.input.StringInput;
import esl2.parser.FunctionPairs;
import esl2.parser.Parser;
import esl2.parser.SymbolTable;
import esl2.types.ArrayValue;
import esl2.types.FatalException;
import esl2.types.ValueType;
import prop6.engine.CallWrapper;
import prop6.engine.CallingContext;
import prop6.engine.atmospheres.BrokenUSSA1976;
import prop6.engine.gravities.J2;
import prop6.engine.gravities.UniformGravity;
import prop6.types.Earth;
import prop6.types.LogMessageType;
import prop6.types.Model;
import prop6.types.ObjectValue;
import prop6.types.ReferenceEllipsoid;
import prop6.types.Settings;
import prop6.types.SimObject;
import prop6.types.StateValue;

public final class SettingsReader
{

    public static boolean peekSettings(String fileName)
    {
        boolean debugRun = false;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));

            Node scenario = doc.getFirstChild();
            if (false == "settings".equals(scenario.getNodeName()))
            {
                return false;
            }

            if (null != scenario.getAttributes().getNamedItem("debug"))
            {
                switch(scenario.getAttributes().getNamedItem("debug").getNodeValue())
                {
                case "true":
                    debugRun = true;
                    break;
                }
            }
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
        }
        return debugRun;
    }

    public static Settings readSettings(String fileName, CallingContext contextToPopulate, FunctionDefinitions funDefs) throws FatalException
    {
        Settings result;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));

            Node scenario = doc.getFirstChild();
            if (false == "settings".equals(scenario.getNodeName()))
            {
                throw new FatalException("Did not understand " + fileName + ".");
            }

            String logLevelStr = TestCasesReader.getNamedAttribute(scenario, "log_level").getNodeValue();
            LogMessageType loggingLevel;
            switch(logLevelStr)
            {
            case "INFO":
                loggingLevel = LogMessageType.LOG_INFO;
                break;
            case "WARN":
                loggingLevel = LogMessageType.LOG_WARNING;
                break;
            case "ERROR":
                loggingLevel = LogMessageType.LOG_ERROR;
                break;
            case "FATAL":
                loggingLevel = LogMessageType.LOG_FATAL;
                break;
            default:
                contextToPopulate.fileOut.message("Did not understand log_level >" + logLevelStr + "< setting to ERROR");
                loggingLevel = LogMessageType.LOG_ERROR;
                break;
            }
            boolean singleThreaded = false;
            if (null != scenario.getAttributes().getNamedItem("single_threaded"))
            {
                switch(scenario.getAttributes().getNamedItem("single_threaded").getNodeValue())
                {
                case "true":
                    singleThreaded = true;
                    break;
                case "false":
                    singleThreaded = false;
                    break;
                default:
                    contextToPopulate.fileOut.message("Did not understand single_threaded setting to false");
                    singleThreaded = false;
                    break;
                }
            }
            boolean debugRun = false;
            if (null != scenario.getAttributes().getNamedItem("debug"))
            {
                switch(scenario.getAttributes().getNamedItem("debug").getNodeValue())
                {
                case "true":
                    debugRun = true;
                    break;
                case "false":
                    debugRun = false;
                    break;
                default:
                    contextToPopulate.fileOut.message("Did not understand debug setting to false");
                    debugRun = false;
                    break;
                }
            }
            double sampleRate = Double.parseDouble(TestCasesReader.getNamedAttribute(scenario, "output_rate").getNodeValue());
            Earth earth = null;
            switch(TestCasesReader.getNamedAttribute(scenario, "earth").getNodeValue())
            {
            case "SNRE":
                earth = new Earth(ReferenceEllipsoid.SE, Earth.NON_ROTATING, new UniformGravity(), Earth.G0, Earth.EGM_96_MU, new BrokenUSSA1976());
                break;
            case "ORE":
                earth = new Earth(ReferenceEllipsoid.OE, Earth.ROTATING_IAU, new J2(), Earth.G0, Earth.EGM_96_MU, new BrokenUSSA1976());
                break;
            default:
                contextToPopulate.fileOut.message("Did not understand earth setting to SNRE");
                earth = new Earth(ReferenceEllipsoid.SE, Earth.NON_ROTATING, new UniformGravity(), Earth.G0, Earth.EGM_96_MU, new BrokenUSSA1976());
                break;
            }
            
            // output function processing
            CallWrapper fun = null;
            {
                SymbolTable table = new SymbolTable();
                table.pushContext(); // We need a base context to operate on.
                table.frameInfo = (null != contextToPopulate.debugger) ? contextToPopulate.executor.debugFrames : null;
                table.gs = funDefs.gs;
                // Add all of the stateless functions to the symbol table.
                table.addAll(funDefs.statelessStdLibFunctions);
                // Don't allow the user to redefine certain functions.
                table.forbiddenIdentifiers = funDefs.statefullStdLibFunctions.funs.keySet();
                ExecutorBuilder.finalizeTable(table);
                table.addedHere = new FunctionPairs();

                StringInput input = new StringInput(scenario.getFirstChild().getNodeValue());
                Lexer lexer = new Lexer(input, fileName + " scenario_output_functions", 1, 1);
                boolean onlyFunctions = Parser.ParseFunctions(lexer, table, contextToPopulate.executor, contextToPopulate.fileOut);
                
                if (false == onlyFunctions)
                {
                    throw new FatalException("The 'scenario_output_functions' contained more than functions.");
                }

                if (null == table.addedHere.funs.get("doOutput"))
                {
                    throw new FatalException("The 'scenario_output_functions' did not contain the 'doOutput' function.");
                }

                int outputFun = table.addedHere.funs.get("doOutput").intValue();
                fun = new CallWrapper(outputFun, contextToPopulate.executor.functions.get(outputFun).token);
            }

            result = new Settings(earth, loggingLevel, debugRun, singleThreaded, sampleRate, fun);
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            throw new FatalException("Error reading " + fileName + ".");
        }
        return result;
    }

    public static ValueType convert(ArrayList<ArrayList<Model>> list)
    {
        ArrayList<ValueType> result = new ArrayList<ValueType>();
        for (ArrayList<Model> states : list)
        {
            ArrayList<ValueType> object = new ArrayList<ValueType>(list.size());
            object.add(new ObjectValue(states.get(0).object));
            ArrayList<ValueType> objStates = new ArrayList<ValueType>(states.size());
            for (Model model : states)
            {
                ArrayList<ValueType> curState = new ArrayList<ValueType>(2);
                curState.add(new StateValue(model.state));
                curState.add(model.object.variables.get(SimObject.OUTPUTS_INDEX));
                objStates.add(new ArrayValue(curState));
            }
            object.add(new ArrayValue(objStates));
            result.add(new ArrayValue(object));
        }
        return new ArrayValue(result);
    }

}
