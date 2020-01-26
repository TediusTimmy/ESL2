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

import esl2.engine.expression.Expression;
import esl2.input.Lexer;
import esl2.input.StringInput;
import esl2.parser.Parser;
import esl2.parser.SymbolTable;
import esl2.types.FatalException;
import prop6.engine.CallingContext;
import prop6.types.TestCase;

public final class TestCasesReader
{

    public static Node getNamedAttribute(Node source, String attribute) throws FatalException
    {
        Node retNode = source.getAttributes().getNamedItem(attribute);
        if (null == retNode)
        {
            throw new FatalException("Node >" + source.getNodeName() + "< does not contain attribute >" + attribute + "<.");
        }
        return retNode;
    }

    public static ArrayList<TestCase> readTestCases(String fileName, CallingContext contextToPopulate, FunctionDefinitions funDefs) throws FatalException
    {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));

            SymbolTable table = new SymbolTable();
            table.pushContext();
            table.addAll(funDefs.statelessStdLibFunctions);
            ExecutorBuilder.finalizeTable(table);

            Node cases = doc.getFirstChild();
            if (false == "test_cases".equals(cases.getNodeName()))
            {
                throw new FatalException("Did not understand " + fileName + ".");
            }
            for (Node node = cases.getFirstChild(); null != node; node = node.getNextSibling())
            {
                switch (node.getNodeName())
                {
                case "#text":
                case "#comment":
                    break;
                case "case":
                    String systemName = getNamedAttribute(node, "system").getNodeValue();
                    String outName = getNamedAttribute(node, "output_file").getNodeValue();
                    long seed = Long.parseLong(getNamedAttribute(node, "seed").getNodeValue());

                    StringInput input = new StringInput(node.getFirstChild().getNodeValue());
                    Lexer lexer = new Lexer(input, "TestCase " + outName, 1, 1);
                    Expression expr = Parser.parseFullExpression(lexer, table, contextToPopulate.fileOut);

                    if (null != expr)
                    {
                        testCases.add(new TestCase(systemName, outName, seed, expr));
                    }
                    else
                    {
                        throw new FatalException("Error parsing test case input to " + outName);
                    }
                    break;
                default:
                    contextToPopulate.fileOut.message("WARNING: TestCase reader ignored element: " + node.getNodeName());
                    break;
                }
            }
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            throw new FatalException("Error reading " + fileName + ".");
        }

        if (0 == testCases.size())
        {
            throw new FatalException("No test cases defined.");
        }
        return testCases;
    }

}
