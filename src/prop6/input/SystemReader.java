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
import java.util.Collections;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import esl2.engine.ConstantsSingleton;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.input.StringInput;
import esl2.input.Token;
import esl2.parser.FunctionPairs;
import esl2.parser.GlobalGetterSetter;
import esl2.parser.Parser;
import esl2.parser.SymbolTable;
import esl2.types.FatalException;
import prop6.engine.CallingContext;
import prop6.engine.Executor;
import prop6.engine.CallWrapper;
import prop6.types.SimObject;
import prop6.types.Simulacrum;

public final class SystemReader
{

    public static void readSystem(String fileName, CallingContext contextToPopulate, FunctionDefinitions funDefs) throws FatalException
    {        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));

            Node system = doc.getFirstChild();
            if (false == "system".equals(system.getNodeName()))
            {
                throw new FatalException("Did not understand " + fileName + ".");
            }

            Node sharedFuns = null;
            Node objects = null;
            for (Node node = system.getFirstChild(); null != node; node = node.getNextSibling())
            {
                switch (node.getNodeName())
                {
                case "#text":
                case "#comment":
                    break;
                case "shared_functions":
                    if (null != sharedFuns)
                    {
                        throw new FatalException("Multiple instances of 'shared_functions' in " + fileName + ".");
                    }
                    sharedFuns = node;
                    break;
                case "objects":
                    if (null != objects)
                    {
                        throw new FatalException("Multiple instances of 'objects' in " + fileName + ".");
                    }
                    objects = node;
                    break;
                default:
                    contextToPopulate.fileOut.message("WARNING: Simulacrum reader ignored element: " + node.getNodeName());
                    break;
                }
            }
            if ((null == sharedFuns) || (null == objects))
            {
                throw new FatalException("Simulacrum must have children 'shared_functions' and 'objects' in " + fileName + ".");
            }

            // shared_functions processing
            {
                SymbolTable table = new SymbolTable();
                table.pushContext(); // We need a base context to operate on.
                table.frameInfo = (null != contextToPopulate.debugger) ? contextToPopulate.executor.debugFrames : null;
                table.gs = funDefs.gs;
                // Add all of the stateless functions to the symbol table.
                table.addAll(funDefs.statelessStdLibFunctions);
                // Don't allow the modeler to redefine certain functions.
                table.forbiddenIdentifiers = funDefs.statefullStdLibFunctions.funs.keySet();
                ExecutorBuilder.finalizeTable(table);
                table.addedHere = funDefs.sharedFunctions;

                StringInput input = new StringInput(sharedFuns.getFirstChild().getNodeValue());
                Lexer lexer = new Lexer(input, fileName + " shared_functions", 1, 1);
                boolean result = Parser.ParseFunctions(lexer, table, contextToPopulate.executor, contextToPopulate.fileOut);
                
                if (false == result)
                {
                    throw new FatalException("The 'shared_functions' contained more than functions.");
                }

                contextToPopulate.simulacrum = new Simulacrum(buildThunk(funDefs.sharedFunctions, "initialObject", contextToPopulate));
            }

            // Initialize system variables (no null on read before write).
            SimObject systemObject = new SimObject();
            systemObject.name = Simulacrum.name;
            contextToPopulate.simulacrum.objects.put(Simulacrum.name, systemObject);

            // objects processing
            for (Node node = objects.getFirstChild(); null != node; node = node.getNextSibling())
            {
                switch (node.getNodeName())
                {
                case "#text":
                case "#comment":
                    break;
                case "object":
                    buildObject(node, contextToPopulate, funDefs);
                    break;
                default:
                    contextToPopulate.fileOut.message("WARNING: Simulacrum reader ignored element in objects list: " + node.getNodeName());
                    break;
                }
            }
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            throw new FatalException("Error reading " + fileName + ".");
        }
    }

    public static void buildFrame(CallingContext contextToPopulate, String name, ArrayList<String> variables)
    {
        if (null != contextToPopulate.debugger)
        {
            ObjectFrame frame = new ObjectFrame();

            frame.frameName = name;
            for (int i = 0; i < variables.size(); ++i)
            {
                Integer var = Integer.valueOf(i);
                frame.vars.put(variables.get(i), var);
                frame.varNames.put(var, variables.get(i));
            }

            ((Executor)contextToPopulate.executor).objectDebugData.put(name, frame);
        }
    }
    
    private static void buildObject(Node node, CallingContext contextToPopulate, FunctionDefinitions funDefs) throws DOMException, FatalException
    {
        String name = TestCasesReader.getNamedAttribute(node, "name").getNodeValue();
        if (null != contextToPopulate.simulacrum.objects.get(name))
        {
            throw new FatalException("Duplicate object " + name + " read.");
        }

        Node objFuns = null, modFuns = null;
        for (Node onode = node.getFirstChild(); null != onode; onode = onode.getNextSibling())
        {
            switch (onode.getNodeName())
            {
            case "#text":
            case "#comment":
                break;
            case "object_functions":
                if (null != objFuns)
                {
                    throw new FatalException("Multiple instances of 'object_functions' in object.");
                }
                objFuns = onode;
                break;
            case "model_functions":
                if (null != modFuns)
                {
                    throw new FatalException("Multiple instances of 'model_functions' in object.");
                }
                modFuns = onode;
                break;
            default:
                contextToPopulate.fileOut.message("WARNING: Simulacrum reader ignored element in object: " + onode.getNodeName());
                break;
            }
        }
        if ((null == objFuns) || (null == modFuns))
        {
            throw new FatalException("Object must have children 'object_functions' and 'model_functions'.");
        }

        SimObject object = new SimObject();
        object.name = name;
        contextToPopulate.simulacrum.objects.put(name, object);

        GlobalGetterSetter vars;
        {
            ArrayList<String> varNames = new ArrayList<String>();
            // The order in this list MUST match SimObject.java
            varNames.add("initial_position_wrt_parent_m");
            varNames.add("initial_velocity_wrt_parent_m_s");
            varNames.add("initial_orientation_wrt_parent_quat");
            varNames.add("initial_rates_wrt_parent_deg_s");
            varNames.add("initial_mass_kg");
            varNames.add("inertia_matrix");
            varNames.add("cg_offset_m");
            varNames.add("force_N");
            varNames.add("torque_mN");
            varNames.add("mass_flow_rate_kg_s");
            varNames.add("outputs");

            TreeSet<String> nameSet = new TreeSet<String>(varNames);
            String variables = TestCasesReader.getNamedAttribute(node, "variables").getNodeValue();
            StringInput input = new StringInput(variables);
            Lexer lexer = new Lexer(input, name + " variables", 1, 1);

            while (Lexeme.END_OF_FILE != lexer.peekNextToken().tokenType)
            {
                String varName = lexer.peekNextToken().text;

                if (Lexeme.IDENTIFIER != lexer.getNextToken().tokenType)
                {
                    throw new FatalException("Variable named >" + varName + "< is not a valid identifier.");
                }

                if (true == nameSet.contains(varName))
                {
                    throw new FatalException("Variable named " + varName + " is not unique.");
                }

                if ((null != funDefs.statefullStdLibFunctions.funs.get(varName)) ||
                    (null != funDefs.statelessStdLibFunctions.funs.get(varName)) ||
                    (null != funDefs.sharedFunctions.funs.get(varName)))
                {
                    throw new FatalException("Variable named " + varName + " cannot have the name of an intrinsic function or simulacrum function.");
                }

                nameSet.add(varName);
                varNames.add(varName);
            }

            vars = funDefs.buildGetterSetter(varNames);
            buildFrame(contextToPopulate, name, varNames);
            object.variables.addAll(Collections.nCopies(varNames.size(), ConstantsSingleton.getInstance().DOUBLE_ZERO));
        }

        FunctionPairs objectFuns = processFunctionNode(objFuns.getFirstChild().getNodeValue(), name + ".object_functions",
                contextToPopulate, funDefs, false, null, vars);
        FunctionPairs modelFuns = processFunctionNode(modFuns.getFirstChild().getNodeValue(), name + ".model_functions",
            contextToPopulate, funDefs, true, objectFuns, vars);

        object.onObjectInit = buildThunk(objectFuns, "onObjectInit", contextToPopulate);
        object.onModelInit = buildThunk(objectFuns, "onModelInit", contextToPopulate);
        object.onModelRealize = buildThunk(modelFuns, "onModelRealize", contextToPopulate);
        object.onModelUpdate = buildThunk(modelFuns, "onModelUpdate", contextToPopulate);
        object.onModelDerivs = buildThunk(modelFuns, "onModelDerivs", contextToPopulate);
    }

    private static FunctionPairs processFunctionNode(String source, String name, CallingContext contextToPopulate,
        FunctionDefinitions funDefs, boolean statefull, FunctionPairs objectFuns, GlobalGetterSetter vars) throws FatalException
    {
        FunctionPairs res = new FunctionPairs();
        SymbolTable table = new SymbolTable();
        table.pushContext(); // We need a base context to operate on.
        table.frameInfo = (null != contextToPopulate.debugger) ? contextToPopulate.executor.debugFrames : null;
        table.gs = funDefs.gs;
        // Add all of the stateless functions to the symbol table.
        table.addAll(funDefs.statelessStdLibFunctions);
        if (true == statefull)
        {
            table.addAll(funDefs.statefullStdLibFunctions);
        }
        else
        {
            table.forbiddenIdentifiers = funDefs.statefullStdLibFunctions.funs.keySet();
        }
        table.addAll(funDefs.sharedFunctions);
        if (null != objectFuns)
        {
            table.addAll(objectFuns);
        }
        table.globalGS = vars;
        ExecutorBuilder.finalizeTable(table);
        table.addedHere = res;

        StringInput input = new StringInput(source);
        Lexer lexer = new Lexer(input, name, 1, 1);
        boolean result = Parser.ParseFunctions(lexer, table, contextToPopulate.executor, contextToPopulate.fileOut);
        
        if (false == result)
        {
            throw new FatalException("The " + name + " contained more than functions.");
        }
        
        return res;
    }

    private static CallWrapper buildThunk(FunctionPairs funs, String name, CallingContext contextToPopulate) throws FatalException
    {
        if (null != funs.funs.get(name))
        {
            int outputFun = funs.funs.get(name).intValue();
            return new CallWrapper(outputFun, contextToPopulate.executor.functions.get(outputFun).token);
        }
        else
        {
            return new CallWrapper(-1, new Token(Lexeme.INVALID, "", "", -1, -1));
        }
    }

}
