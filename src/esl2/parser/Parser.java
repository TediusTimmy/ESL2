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

package esl2.parser;

import java.util.ArrayList;

import esl2.engine.ConstantsSingleton;
import esl2.engine.Executor;
import esl2.engine.FlowControl;
import esl2.engine.Getter;
import esl2.engine.Setter;
import esl2.engine.expression.*;
import esl2.engine.statement.*;
import esl2.engine.statement.CaseContainer.Type;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.FunctionPointerValue;
import esl2.types.ProgrammingException;
import esl2.types.DoubleValue;
import esl2.types.StringValue;

public final class Parser
{

    public static Expression parseExpression(Lexer src, SymbolTable context, ParserLogger logger) throws FatalException
    {
        Expression result = null;
        try
        {
            result = expression(src, context);
        }
        catch (ParserException e)
        {
            logger.message(e.getLocalizedMessage());
        }
        return result;
    }

    public static Expression parseFullExpression(Lexer src, SymbolTable context, ParserLogger logger) throws FatalException
    {
        Expression result;
        try
        {
            result = expression(src, context);
            expect(src, Lexeme.END_OF_FILE, "End of Input");
        }
        catch (ParserException e)
        {
            logger.message(e.getLocalizedMessage());
            result = null;
        }
        return result;
    }

    private static void expect(Lexer src, Lexeme expected, String name) throws ParserException, FatalException
    {
        if (src.peekNextToken().tokenType != expected)
        {
            Token token = src.peekNextToken();
            throw new ParserException("Expected >" + name + "< but found >" + token.text +
                "<\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
        }
        src.getNextToken();
    }

    private static Expression expression(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression condition = predicate(src, context);

        if (Lexeme.CONDITIONAL == src.peekNextToken().tokenType)
        {
            Token token = src.getNextToken();

            Expression thenCase = expression(src, context);

            expect(src, Lexeme.ALTERNATIVE, ":");

            Expression elseCase = expression(src, context);

            condition = new TernaryOperation(token, condition, thenCase, elseCase);
        }
        return condition;
    }

    private static Expression predicate(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = relation(src, context);
        while ((Lexeme.OR == src.peekNextToken().tokenType) || (Lexeme.AND == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            Expression rhs = relation(src, context);

            switch(token.tokenType)
            {
            case AND:
                lhs = new ShortAnd(token, lhs, rhs);
                break;
            case OR:
                lhs = new ShortOr(token, lhs, rhs);
                break;
            default:
                throw new ProgrammingException("Only hardware failure can get us here.");
            }
        }
        return lhs;
    }

    private static Expression relation(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = simple(src, context);
        if ((Lexeme.EQUALITY == src.peekNextToken().tokenType) || (Lexeme.INEQUALITY == src.peekNextToken().tokenType) ||
            (Lexeme.GREATER_THAN == src.peekNextToken().tokenType) || (Lexeme.GREATER_THAN_OR_EQUAL_TO == src.peekNextToken().tokenType) ||
            (Lexeme.LESS_THAN == src.peekNextToken().tokenType) || (Lexeme.LESS_THAN_OR_EQUAL_TO == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            Expression rhs = simple(src, context);

            switch(token.tokenType)
            {
            case EQUALITY:
                lhs = new Equals(token, lhs, rhs);
                break;
            case INEQUALITY:
                lhs = new NotEqual(token, lhs, rhs);
                break;
            case GREATER_THAN:
                lhs = new Greater(token, lhs, rhs);
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                lhs = new GEQ(token, lhs, rhs);
                break;
            case LESS_THAN:
                lhs = new Less(token, lhs, rhs);
                break;
            case LESS_THAN_OR_EQUAL_TO:
                lhs = new LEQ(token, lhs, rhs);
                break;
            default:
                throw new ProgrammingException("Only hardware failure can get us here.");
            }
        }
        return lhs;
    }

    private static Expression simple(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = term(src, context);
        while ((Lexeme.PLUS == src.peekNextToken().tokenType) || (Lexeme.MINUS == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            Expression rhs = term(src, context);

            switch(token.tokenType)
            {
            case PLUS:
                lhs = new Plus(token, lhs, rhs);
                break;
            case MINUS:
                lhs = new Minus(token, lhs, rhs);
                break;
            default:
                throw new ProgrammingException("Only hardware failure can get us here.");
            }
        }
        return lhs;
    }

    private static Expression term(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = unary(src, context);
        while ((Lexeme.MULTIPLY == src.peekNextToken().tokenType) || (Lexeme.DIVIDE == src.peekNextToken().tokenType) ||
            (Lexeme.CROSS == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            Expression rhs = unary(src, context);

            switch(token.tokenType)
            {
            case MULTIPLY:
                lhs = new Multiply(token, lhs, rhs);
                break;
            case DIVIDE:
                lhs = new Divide(token, lhs, rhs);
                break;
            case CROSS:
                lhs = new Cross(token, lhs, rhs);
                break;
            default:
                throw new ProgrammingException("Only hardware failure can get us here.");
            }
        }
        return lhs;
    }

    private static Expression unary(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression arg;
        if ((Lexeme.NOT == src.peekNextToken().tokenType) || (Lexeme.MINUS == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            arg = factor(src, context);

            switch(token.tokenType)
            {
            case NOT:
                arg = new Not(token, arg);
                break;
            case MINUS:
                arg = new Negate(token, arg);
                break;
            default:
                throw new ProgrammingException("Only hardware failure can get us here.");
            }
        }
        else
        {
            arg = factor(src, context);
        }
        return arg;
    }

    private static Expression factor(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = referent(src, context);
        if (Lexeme.POWER == src.peekNextToken().tokenType)
        {
            Token token = src.getNextToken();

            Expression rhs = referent(src, context);

            lhs = new Power(token, lhs, rhs);
        }
        return lhs;
    }

    private static Expression referent(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = builder(src, context);
        while ((Lexeme.OPEN_BRACKET == src.peekNextToken().tokenType) ||
            (Lexeme.PERIOD == src.peekNextToken().tokenType))
        {
            Token token = src.getNextToken();

            Expression rhs;
            if (Lexeme.PERIOD == token.tokenType)
            {
                Token memberTok = src.peekNextToken();
                expect(src, Lexeme.IDENTIFIER, "Identifier");
                rhs = new Constant(memberTok, new StringValue(memberTok.text));
            }
            else
            {
                rhs = expression(src, context);
            }

            if (Lexeme.OPEN_BRACKET == token.tokenType)
            {
                expect(src, Lexeme.CLOSE_BRACKET, "]");
            }

            lhs = new DerefVar(token, lhs, rhs);
        }
        return lhs;
    }

    private static Expression builder(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression ret;

        if (Lexeme.OPEN_BRACE == src.peekNextToken().tokenType)
        {
            Token token = src.getNextToken();

            if (Lexeme.CLOSE_BRACE != src.peekNextToken().tokenType)
            {
                Expression key = expression(src, context);

                // Is it a Dictionary?
                if (Lexeme.ALTERNATIVE == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
                    Expression value = expression(src, context);
                    ret = context.buildInsert(token, new Constant(token, ConstantsSingleton.getInstance().EMPTY_DICTIONARY), key, value);
                    while (Lexeme.COMMA == src.peekNextToken().tokenType)
                    {
                        src.getNextToken();
                        key = expression(src, context);
                        expect(src, Lexeme.ALTERNATIVE, ":");
                        value = expression(src, context);
                        ret = context.buildInsert(token, ret, key, value);
                    }
                }
                else // Assume an Array
                {
                    ret = context.buildPushBack(token, new Constant(token, ConstantsSingleton.getInstance().EMPTY_ARRAY), key);
                    while (Lexeme.COMMA == src.peekNextToken().tokenType)
                    {
                        src.getNextToken();
                        key = expression(src, context);
                        ret = context.buildPushBack(token, ret, key);
                    }
                }
                expect(src, Lexeme.CLOSE_BRACE, "}");
            }
            else // Empty Array
            {
                src.getNextToken();
                ret = new Constant(token, ConstantsSingleton.getInstance().EMPTY_ARRAY);
            }
        }
        else
        {
            ret = functionCall(src, context);
        }

        return ret;
    }

    private static Expression functionCall(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression lhs = primary(src, context);
        while (Lexeme.OPEN_PARENS == src.peekNextToken().tokenType)
        {
            Token token = src.getNextToken();

            ArrayList<Expression> args = new ArrayList<Expression>();
            if (Lexeme.CLOSE_PARENS != src.peekNextToken().tokenType)
            {
                args.add(expression(src, context));

                while (Lexeme.COMMA == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
                    args.add(expression(src, context));
                }
            }

            expect(src, Lexeme.CLOSE_PARENS, ")");

            lhs = new FunctionCall(token, lhs, args);;
        }
        return lhs;
    }

    private static Expression primary(Lexer src, SymbolTable context) throws ParserException, FatalException
    {
        Expression ret;

        Token token = src.peekNextToken();
        switch (token.tokenType)
        {
        case IDENTIFIER:
            ret = null; // I would rather have the switch's warning than ret's (undefined) warning.
            switch(context.lookup(token.text))
            {
            case VARIABLE:
                src.getNextToken();
                ret = new Variable(token, context.getVariableGetter(token.text));
                break;
            case FUNCTION:
                src.getNextToken();
                ret = new Constant(token, new FunctionPointerValue(context.getFunctionLocation(token.text)));
                break;
            case FORBIDDEN:
                throw new ParserException("Identifier >" + token.text + "< is not allowed in this context." +
                    "\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
            case UNDEFINED:
                throw new ParserException("Undefined identifier >" + token.text + "< used." +
                    "\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
            }
            break;
        case NUMBER:
            src.getNextToken();
            ret = new Constant(token, new DoubleValue(Double.parseDouble(token.text)));
            break;
        case STRING:
            src.getNextToken();
            ret = new Constant(token, new StringValue(token.text));
            break;
        case OPEN_PARENS:
            src.getNextToken();
            ret = expression(src, context);
            expect(src, Lexeme.CLOSE_PARENS, ")");
            break;
        default:
            throw new ParserException("Expected >primary expression< but found >" + token.text +
                "<\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
        }

        return ret;
    }

    public static void recoverExpression(Lexer src) throws FatalException
    {
        // Recover to end of current expression
        recoverLoop: while (true)
        {
            // This is the follow set of an expression
            // As an expression can end a statement,
            // its follow set is the follow set of a statement: 
            switch (src.peekNextToken().tokenType)
            {
            case FUNCTION:
            case SET:
            case CALL:
            case IF:
            case WHILE:
            case SELECT:
            case BREAK:
            case CONTINUE:
            case RETURN:
            case FOR:

            case ELSE:
            case ELSEIF:
            case CASE:
            case ALSO:
            case END:

            case TO:
            case THEN:
            case DO:
            case FROM:
            case IS:
            case DOWNTO:
            case STEP:
            case IN:

            case END_OF_FILE:
                break recoverLoop;

            default:
                src.getNextToken();
                break;
            }
        }
        return;
    }

    public static void recoverStatement(Lexer src) throws FatalException
    {
        // Recover to end of current statement
        recoverLoop: while (true)
        {
            // This is the follow set of a statement:
            // most of this is the first set of a statement.
            switch (src.peekNextToken().tokenType)
            {
            case FUNCTION:
            case SET:
            case CALL:
            case IF:
            case WHILE:
            case SELECT:
            case BREAK:
            case CONTINUE:
            case RETURN:
            case FOR:

            case ELSE:
            case ELSEIF:
            case CASE:
            case ALSO:
            case END:

            case END_OF_FILE:
                break recoverLoop;

            default:
                src.getNextToken();
                break;
            }
        }
        return;
    }

    private static Expression expressionRecover(Lexer src, SymbolTable table, ParserLogger logger) throws FatalException
    {
        Expression result = null;
        try
        {
            result = expression(src, table);
        }
        catch (ParserException e)
        {
            logger.message(e.getLocalizedMessage());
            recoverExpression(src);
        }
        return result;
    }

    private static boolean enforceUnique(Token token, SymbolTable table, String use, boolean stackDive, ParserLogger logger) throws ParserException
    {
        boolean errorOccurred = false;
        if (IdentifierType.UNDEFINED != table.lookup(token.text))
        {
            errorOccurred = true;
            logger.message("Identifier >" + token.text + "< for " + use + " is already defined." +
                "\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
        }
        if (true == stackDive)
        {
            for (FrameDebugInfo frame : table.getContext())
            {
                if ((true == frame.args.containsKey(token.text)) || (true == frame.locals.containsKey(token.text)))
                {
                    errorOccurred = true;
                    logger.message("Identifier >" + token.text + "< for " + use + " is already defined." +
                        "\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
                }
            }
        }
        return errorOccurred;
    }

    public static boolean ParseFunctions(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws FatalException
    {
        boolean result = true;
        try
        {
            while (Lexeme.FUNCTION == src.peekNextToken().tokenType)
            {
                if (Lexeme.FUNCTION == src.peekNextToken().tokenType)
                {
                    Statement ret = statement(src, table, executor, logger);
                    if ((null == ret) || (ret != ConstantsSingleton.getInstance().ONE_TRUE_NOP))
                    {
                        result = false;
                    }
                }
                else // Lexeme.NEW_LINE
                {
                    src.getNextToken();
                }
            }
            if (Lexeme.END_OF_FILE != src.peekNextToken().tokenType)
            {
                logger.message("Something other than a function definition was found while parsing only functions.");
                result = false;
            }
        }
        catch (ParserException e)
        {
            logger.message("Could not parse functions: " + e.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    public static Statement Parse(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws FatalException
    {
        return outerStatementSeq(src, table, executor, logger);
    }

    public static Statement ParseStatement(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws ParserException, FatalException
    {
        return statement(src, table, executor, logger);
    }

    private static Statement statement(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws ParserException, FatalException
    {
        Statement ret = null;
        boolean badWrong = false;
        boolean defined = false;

        Token token = src.peekNextToken();
        switch (token.tokenType)
        {
        case SET:
            src.getNextToken();
            Token identTok = src.peekNextToken();
            expect(src, Lexeme.IDENTIFIER, "Identifier");
            switch (table.lookup(identTok.text))
            {
            case UNDEFINED: // Assignment Statement
                // Add this to the symbol table, defining it as a local.
                table.addLocal(identTok.text);
                defined = true;

                // !!! FALL THROUGH !!!
            case VARIABLE: // Assignment Statement
                RecAssignmentState base = null;
                RecAssignmentState current = null;

                if ((false == defined) && (true == table.isConstant(token.text)))
                {
                    throw new ParserException("Identifier >" + identTok.text + "< is not assignable in this context." +
                        "\n\tFrom file " + identTok.sourceFile + " on line " + identTok.lineNumber + " at " + identTok.lineLocation);
                }

                if ((true == defined) && (Lexeme.OPEN_BRACKET == src.peekNextToken().tokenType) ||
                    (Lexeme.PERIOD == src.peekNextToken().tokenType))
                {
                    throw new ParserException("Identifier >" + identTok.text + "< cannot be a Dictionary or Array in this context." +
                        "\n\tFrom file " + identTok.sourceFile + " on line " + identTok.lineNumber + " at " + identTok.lineLocation);
                }

                while ((Lexeme.OPEN_BRACKET == src.peekNextToken().tokenType) ||
                    (Lexeme.PERIOD == src.peekNextToken().tokenType))
                {
                    Token oTok = src.getNextToken();

                    Expression index;
                    if (Lexeme.PERIOD == oTok.tokenType)
                    {
                        Token memberTok = src.peekNextToken();
                        expect(src, Lexeme.IDENTIFIER, "Identifier");
                        index = new Constant(memberTok, new StringValue(memberTok.text));
                    }
                    else
                    {
                        index = expression(src, table);
                    }

                    if (Lexeme.OPEN_BRACKET == oTok.tokenType)
                    {
                        expect(src, Lexeme.CLOSE_BRACKET, "]");
                    }

                    if (null == base)
                    {
                        base = new RecAssignmentState(oTok, index);
                        current = base;
                    }
                    else
                    {
                        current.next = new RecAssignmentState(oTok, index);
                        current = current.next;
                    }
                }

                expect(src, Lexeme.TO, "to");

                Expression rhs = expression(src, table);

                ret = new Assignment(token, table.getVariableGetter(identTok.text), table.getVariableSetter(identTok.text), base, rhs);
                break;

            case FUNCTION:
            case FORBIDDEN:
                throw new ParserException("Identifier >" + identTok.text + "< is not allowed in this context." +
                    "\n\tFrom file " + identTok.sourceFile + " on line " + identTok.lineNumber + " at " + identTok.lineLocation);
            }
            break;

        case CALL: // Expression-as-statement.
            src.getNextToken();
            Expression expr = expression(src, table);
            ret = new Expr(token, expr);
            break;

        case IF:
            ret = innerIF(src, table, executor, logger);
            break;

        case WHILE:
            src.getNextToken();
            Expression condition = expressionRecover(src, table, logger);

            int id = table.newLoop();
            try
            {
                if (Lexeme.CALL == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
    
                    Token name = src.getNextToken();
    
                    if (SymbolTable.INVALID_INDEX != table.getLoop(name.text))
                    {
                        badWrong = true;
                        logger.message("Label >" + name.text + "< is already defined." +
                            "\n\tFrom file " + name.sourceFile + " on line " + name.lineNumber + " at " + name.lineLocation);
                    }
                    else
                    {
                        table.nameLoop(name.text);
                    }
                }

                expect(src, Lexeme.DO, "do");

                Statement block = innerStatementSeq(src, table, executor, logger);

                expect(src, Lexeme.END, "end");

                if ((null != condition) && (null != block) && (false == badWrong))
                {
                    ret = new WhileStatement(token, condition, block, id);
                }
            }
            finally
            {
                table.popLoop();
            }
            break;

        case FOR:
            src.getNextToken();

            Getter getter = null;
            Setter setter = null;
            boolean to = true;
            Expression upper = null;
            Expression step = null;

            identTok = src.peekNextToken();
            expect(src, Lexeme.IDENTIFIER, "Identifier");
            if ((IdentifierType.VARIABLE == table.lookup(identTok.text)) &&
                (false == table.isConstant(identTok.text)))
            {
                getter = table.getVariableGetter(identTok.text);
                setter = table.getVariableSetter(identTok.text);
            }
            else if (IdentifierType.UNDEFINED == table.lookup(identTok.text))
            {
                table.addLocal(identTok.text);
                getter = table.getVariableGetter(identTok.text);
                setter = table.getVariableSetter(identTok.text);
            }
            else
            {
                badWrong = true;
                logger.message("Identifier >" + identTok.text + "< is not allowed or not assignable in this context." +
                    "\n\tFrom file " + identTok.sourceFile + " on line " + identTok.lineNumber + " at " + identTok.lineLocation);
            }

            if (Lexeme.IN == src.peekNextToken().tokenType)
            {
                src.getNextToken();
                condition = expressionRecover(src, table, logger);
            }
            else
            {
                expect(src, Lexeme.FROM, "from");

                condition = expressionRecover(src, table, logger);

                if (Lexeme.DOWNTO == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
                    to = false;
                }
                else
                {
                    expect(src, Lexeme.TO, "to");
                }
                upper = expressionRecover(src, table, logger);
                if (null == upper)
                {
                    badWrong = true;
                }

                if (Lexeme.STEP == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
                    step = expressionRecover(src, table, logger);
                    if (null == step)
                    {
                        badWrong = true;
                    }
                }
            }

            id = table.newLoop();
            try
            {
                if (Lexeme.CALL == src.peekNextToken().tokenType)
                {
                    src.getNextToken();
    
                    Token name = src.getNextToken();
    
                    if (SymbolTable.INVALID_INDEX != table.getLoop(name.text))
                    {
                        badWrong = true;
                        logger.message("Label >" + name.text + "< is already defined." +
                            "\n\tFrom file " + name.sourceFile + " on line " + name.lineNumber + " at " + name.lineLocation);
                    }
                    else
                    {
                        table.nameLoop(name.text);
                    }
                }

                expect(src, Lexeme.DO, "do");

                Statement block = innerStatementSeq(src, table, executor, logger);

                expect(src, Lexeme.END, "end");

                if ((null != condition) && (null != block) && (false == badWrong))
                {
                    ret = new ForStatement(token, getter, setter, condition, to, upper, step, block, id);
                }
            }
            finally
            {
                table.popLoop();
            }
            break;

        case BREAK:
        case CONTINUE:
            src.getNextToken();
            id = table.currentLoop();
            if (-1 == id)
            {
                throw new ParserException("Statement " + token.text + ", but not in loop." +
                    "\n\tFrom file " + token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation);
            }

            if (Lexeme.IDENTIFIER == src.peekNextToken().tokenType)
            {
                Token name = src.getNextToken();
                id = table.getLoop(name.text);
                if (-1 == id)
                {
                    throw new ParserException("Loop label >" + name.text + "< has not been defined." +
                        "\n\tFrom file " + name.sourceFile + " on line " + name.lineNumber + " at " + name.lineLocation);
                }
            }

            ret = new FlowControlStatement(token, "break".equals(token.text) ? FlowControl.Type.BREAK : FlowControl.Type.CONTINUE, id, null);
            break;

        case RETURN:
            src.getNextToken();
            expr = expression(src, table);
            ret = new FlowControlStatement(token, FlowControl.Type.RETURN, SymbolTable.INVALID_INDEX, expr);
            break;

        case SELECT:
            src.getNextToken();
            Expression control = expressionRecover(src, table, logger);

            expect(src, Lexeme.FROM, "from");

            ArrayList<CaseContainer> cases = new ArrayList<CaseContainer>();
            boolean elseFound = false;
            while ((false == elseFound) &&
                ((Lexeme.CASE == src.peekNextToken().tokenType) || ((Lexeme.ALSO == src.peekNextToken().tokenType))))
            {
                boolean breaking = true;
                if (Lexeme.ALSO == src.peekNextToken().tokenType)
                {
                    breaking = false;
                    src.getNextToken();
                }
                Token caseTok = src.peekNextToken();
                expect(src, Lexeme.CASE, "case");

                if (Lexeme.FROM == src.peekNextToken().tokenType)
                {
                    src.getNextToken();

                    Expression lower = expressionRecover(src, table, logger);

                    expect(src, Lexeme.TO, "to");

                    upper = expressionRecover(src, table, logger);

                    expect(src, Lexeme.IS, "is");

                    Statement block = innerStatementSeq(src, table, executor, logger);

                    if ((null != lower) && (null != upper) && (null != block))
                    {
                        cases.add(new CaseContainer(caseTok, Type.AT, breaking, lower, upper, block));
                    }
                    else
                    {
                        badWrong = true;
                    }
                }
                else if (Lexeme.ELSE != src.peekNextToken().tokenType)
                {
                    Type type = Type.AT;
                    if (Lexeme.ABOVE == src.peekNextToken().tokenType)
                    {
                        src.getNextToken();
                        type = Type.ABOVE;
                    }
                    else if (Lexeme.BELOW == src.peekNextToken().tokenType)
                    {
                        src.getNextToken();
                        type = Type.BELOW;
                    }

                    Expression current = expressionRecover(src, table, logger);

                    expect(src, Lexeme.IS, "is");

                    Statement block = innerStatementSeq(src, table, executor, logger);

                    if ((null != current) && (null != block))
                    {
                        cases.add(new CaseContainer(caseTok, type, breaking, current, null, block));
                    }
                    else
                    {
                        badWrong = true;
                    }
                }
                else // ELSE! Ah ha ha!
                {
                    elseFound = true;
                    src.getNextToken();
                    expect(src, Lexeme.IS, "is");

                    Statement block = innerStatementSeq(src, table, executor, logger);

                    if (null != block)
                    {
                        cases.add(new CaseContainer(caseTok, Type.AT, breaking, null, null, block));
                    }
                    else
                    {
                        badWrong = true;
                    }
                }
            }

            expect(src, Lexeme.END, "end");

            if ((null != control) && (false == badWrong))
            {
                ret = new SelectStatement(token, control, cases);
            }
            break;

        case FUNCTION:
            src.getNextToken();

            table.pushContext();
            try
            {
                Token functionName = src.peekNextToken();
                expect(src, Lexeme.IDENTIFIER, "Function Identifier");
                badWrong |= enforceUnique(functionName, table, "function name", true, logger);
                // Get the location of this: current size of array.
                int funLoc = executor.functions.size();
                table.addFunction(functionName.text, funLoc);
                table.commitContext(functionName.text);
                // Reserve location by increasing array size.
                executor.functions.add(null);
                executor.locals.add(null);
                executor.args.add(null);
                executor.funNames.add(functionName.text);

                expect(src, Lexeme.OPEN_PARENS, "(");

                if (Lexeme.CLOSE_PARENS != src.peekNextToken().tokenType)
                {
                    Token argName = src.peekNextToken();
                    expect(src, Lexeme.IDENTIFIER, "Argument Identifier");
                    badWrong |= enforceUnique(argName, table, "function argument", false, logger);
                    table.addArgument(argName.text);

                    while (Lexeme.COMMA == src.peekNextToken().tokenType)
                    {
                        src.getNextToken();

                        argName = src.peekNextToken();
                        expect(src, Lexeme.IDENTIFIER, "Argument Identifier");
                        badWrong |= enforceUnique(argName, table, "function argument", false, logger);
                        table.addArgument(argName.text);
                    }
                }

                try
                {
                    expect(src, Lexeme.CLOSE_PARENS, ")");
                    expect(src, Lexeme.IS, "is");
                }
                catch (ParserException e)
                {
                    logger.message(e.getLocalizedMessage());
                    badWrong = true;
                    recoverStatement(src);
                }

                Statement block = innerStatementSeq(src, table, executor, logger);

                expect(src, Lexeme.END, "end");

                if ((null != block) && (false == badWrong))
                {
                    // Set the code block to execute.
                    executor.functions.set(funLoc, block);
                    // Finally, set the number of variables that this function has.
                    executor.args.set(funLoc, Integer.valueOf(table.getNArgs()));
                    executor.locals.set(funLoc, Integer.valueOf(table.getNLocals()));
                    // Flag this as a success.
                    ret = ConstantsSingleton.getInstance().ONE_TRUE_NOP;
                }
            }
            finally
            {
                table.popContext();
            }
            break;

        case ELSE:
        case ELSEIF:
        case CASE:
        case ALSO:
        case END:
            // I expected a statement, but got something in the follow set for a statement.
            // This implies a malformed construct somewhere: if it weren't malformed, it would have seen this.
            // Because this is in the follow set of a statement, consume it before continuing to prevent a loop.
            src.getNextToken();

        default:
            // The purpose of this is to throw an exception with the correct error string.
            // We also wish to invoke error recovery, so that we don't get stuck in a bad input loop.
            expect(src, Lexeme.LEXER_NEVER_RETURNS_THIS, "Statement");
            break;
        }

        return ret;
    }
    
    private static Statement innerIF(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws ParserException, FatalException
    {
        Statement ret = null;
        Token token = src.getNextToken();

        Expression condition = expressionRecover(src, table, logger);

        expect(src, Lexeme.THEN, "then");

        Statement thenStat = innerStatementSeq(src, table, executor, logger);

        Statement elseStat = ConstantsSingleton.getInstance().ONE_TRUE_NOP;
        if (Lexeme.ELSEIF == src.peekNextToken().tokenType)
        {
            elseStat = innerIF(src, table, executor, logger);
        }
        else
        {
            if (Lexeme.ELSE == src.peekNextToken().tokenType)
            {
                src.getNextToken();
                elseStat = innerStatementSeq(src, table, executor, logger);
            }

            expect(src, Lexeme.END, "end");
        }

        if ((null != condition) && (null != thenStat) && (null != elseStat))
        {
            ret = new IfStatement(token, condition, thenStat, elseStat);
        }
        return ret;
    }

    private static Statement outerStatementSeq(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws FatalException
    {
        Statement ret = null;
        boolean badWrong = false;
        Token token = src.peekNextToken();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        while (Lexeme.END_OF_FILE  != src.peekNextToken().tokenType)
        {
            try
            {
                Statement state = statement(src, table, executor, logger);
                if (null != state)
                {
                    if (state != ConstantsSingleton.getInstance().ONE_TRUE_NOP)
                    {
                        statements.add(state);
                    }
                }
                else
                {
                    badWrong = true;
                }
            }
            catch (ParserException e)
            {
                logger.message(e.getLocalizedMessage());
                badWrong = true;
                recoverStatement(src);
            }
        }
        if (false == badWrong)
        {
            if (true == statements.isEmpty())
            {
                ret = ConstantsSingleton.getInstance().ONE_TRUE_NOP;
            }
            else if (1 == statements.size())
            {
                ret = statements.get(0);
            }
            else
            {
                ret = new StatementSeq(token, statements);
            }
        }
        return ret;
    }

    private static Statement innerStatementSeq(Lexer src, SymbolTable table, Executor executor, ParserLogger logger) throws FatalException
    {
        Statement ret = null;
        boolean badWrong = false;
        Token token = src.peekNextToken();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        // This is the follow set of a statement sequence within an 'if', 'while', 'switch', or 'function' statement.
        // With the exception of end-of-file, which is an erroneous condition (that will be caught by the caller).
        while ((Lexeme.ELSE != src.peekNextToken().tokenType) && (Lexeme.CASE  != src.peekNextToken().tokenType) &&
            (Lexeme.END != src.peekNextToken().tokenType) && (Lexeme.END_OF_FILE  != src.peekNextToken().tokenType) &&
            (Lexeme.ALSO != src.peekNextToken().tokenType) && (Lexeme.ELSEIF != src.peekNextToken().tokenType))
        {
            try
            {
                Statement state = statement(src, table, executor, logger);
                if (null != state)
                {
                    if (state != ConstantsSingleton.getInstance().ONE_TRUE_NOP)
                    {
                        statements.add(state);
                    }
                }
                else
                {
                    badWrong = true;
                }
            }
            catch (ParserException e)
            {
                logger.message(e.getLocalizedMessage());
                badWrong = true;
                recoverStatement(src);
            }
        }
        if (false == badWrong)
        {
            if (true == statements.isEmpty())
            {
                ret = ConstantsSingleton.getInstance().ONE_TRUE_NOP;
            }
            else if (1 == statements.size())
            {
                ret = statements.get(0);
            }
            else
            {
                ret = new StatementSeq(token, statements);
            }
        }
        return ret;
    }

}
