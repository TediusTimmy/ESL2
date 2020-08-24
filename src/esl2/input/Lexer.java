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

package esl2.input;

import java.util.HashMap;

import esl2.types.FatalException;

public final class Lexer
{

    private final BufferedGenericInput bgi;
    private final String sourceName;
    private Token nextToken;

    public Lexer(GenericInput input, String sourceName, int startLine, int startChar) throws FatalException
    {
        bgi = new BufferedGenericInput(input);
        this.sourceName = sourceName;
        currentLine = startLine;
        currentCharacter = startChar;
        composeNextToken();
    }

    private static final HashMap<String, Lexeme> keywords;
    static
    {
        keywords = new HashMap<String, Lexeme>();
        keywords.put("function", Lexeme.FUNCTION);
        keywords.put("end",      Lexeme.END);
        keywords.put("set",      Lexeme.SET);
        keywords.put("to",       Lexeme.TO);
        keywords.put("call",     Lexeme.CALL);
        keywords.put("if",       Lexeme.IF);
        keywords.put("then",     Lexeme.THEN);
        keywords.put("else",     Lexeme.ELSE);
        keywords.put("elseif",   Lexeme.ELSEIF);
        keywords.put("while",    Lexeme.WHILE);
        keywords.put("do",       Lexeme.DO);
        keywords.put("select",   Lexeme.SELECT);
        keywords.put("from",     Lexeme.FROM);
        keywords.put("case",     Lexeme.CASE);
        keywords.put("is",       Lexeme.IS);
        keywords.put("also",     Lexeme.ALSO);
        keywords.put("above",    Lexeme.ABOVE);
        keywords.put("below",    Lexeme.BELOW);
        keywords.put("break",    Lexeme.BREAK);
        keywords.put("continue", Lexeme.CONTINUE);
        keywords.put("return",   Lexeme.RETURN);
        keywords.put("for",      Lexeme.FOR);
        keywords.put("downto",   Lexeme.DOWNTO);
        keywords.put("step",     Lexeme.STEP);
        keywords.put("in",       Lexeme.IN);
    }

    private int currentLine;
    private int currentCharacter;

    private void consume() throws FatalException
    {
        int next = bgi.consume();
        if ('\n' == next)
        {
            currentCharacter = 1;
            ++currentLine;
        }
        else if (GenericInput.ENDOFFILE != next)
        {
            ++currentCharacter;
        }
    }

    private void consumeToEndOfLine() throws FatalException
    {
        while ((GenericInput.ENDOFFILE != bgi.peek()) && ('\n' != bgi.peek()))
        {
            consume();
        }
    }

    private void consumeWhiteSpace() throws FatalException
    {
        for(;;)
        {
            switch(bgi.peek())
            {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                consume();
                break;
            case '#':
                // Shell-style to-end-of-line comment.
                consumeToEndOfLine();
                break;
            default:
                return;
            }
        }
    }

    // Return the next token of input without removing it.
    public Token peekNextToken()
    {
        return nextToken;
    }

    // Remove and return the next token of input.
    public Token getNextToken() throws FatalException
    {
        Token result = nextToken;
        composeNextToken();
        return result;
    }

    // Create next token of input.
    private void composeNextToken() throws FatalException
    {
        consumeWhiteSpace();

        int lineNo = currentLine;
        int charNo = currentCharacter;

        String text = null;

        Lexeme tokenType = Lexeme.END_OF_FILE;

        if ((true == Character.isLetter(bgi.peek())) || ('_' == bgi.peek()))
        {
            int temp = bgi.peek();

            StringBuilder buildText = new StringBuilder();

            while ((true == Character.isLetterOrDigit(temp)) || ('_' == temp))
            {
                consume();
                buildText.append((char) temp);

                temp = bgi.peek();
                if (GenericInput.ENDOFFILE == temp)
                {
                    break;
                }
            }

            text = buildText.toString();
            if (keywords.containsKey(text))
            {
                tokenType = keywords.get(text);
            }
            else
            {
                tokenType = Lexeme.IDENTIFIER;
            }
        }
        else if ((true == Character.isDigit(bgi.peek())) || ('.' == bgi.peek()))
        {
            StringBuilder buildText = new StringBuilder();

            while (true == Character.isDigit(bgi.peek()))
            {
                buildText.append((char) bgi.peek());
                consume();
            }
            if ('.' == bgi.peek())
            {
                buildText.append('.');
                consume();
            }
            while (true == Character.isDigit(bgi.peek()))
            {
                buildText.append((char) bgi.peek());
                consume();
            }

            if (0 == ".".compareTo(buildText.toString()))
            {
                text = ".";
                tokenType = Lexeme.PERIOD;
            }
            else
            {
                if (('e' == bgi.peek()) || ('E' == bgi.peek()))
                {
                    StringBuilder temp = new StringBuilder("e");
                    int advance = 1;

                    if (('-' == bgi.peek(advance)) || ('+' == bgi.peek(advance)))
                    {
                        temp.append((char) bgi.peek(advance));
                        ++advance;
                    }
                    while (true == Character.isDigit(bgi.peek(advance)))
                    {
                        temp.append((char) bgi.peek(advance));
                        ++advance;
                    }

                    String tempStr = temp.toString();
                    if ((0 != "e-".compareTo(tempStr)) && (0 != "e+".compareTo(tempStr)) && (0 != "e".compareTo(tempStr)))
                    {
                        buildText.append(tempStr);
                        while (0 < advance)
                        {
                            consume();
                            --advance;
                        }
                    }
                }

                text = buildText.toString();
                tokenType = Lexeme.NUMBER;
            }
        }
        else // DFA for all other tokens
        {
            switch (bgi.peek())
            {
            case GenericInput.ENDOFFILE:
                text = "END-OF-INPUT";
                tokenType = Lexeme.END_OF_FILE;
                break;
            case ',':
                consume();
                text = ",";
                tokenType = Lexeme.COMMA;
                break;
            case '\'':
                tokenType = Lexeme.STRING;
                StringBuilder buildText = new StringBuilder();
                consume();
                while ((GenericInput.ENDOFFILE != bgi.peek()) && ('\'' != bgi.peek()))
                {
                    buildText.append((char) bgi.peek());
                    consume();
                }
                if (GenericInput.ENDOFFILE == bgi.peek())
                {
                    tokenType = Lexeme.INVALID;
                }
                consume();
                text = buildText.toString();
                break;
            case '"':
                tokenType = Lexeme.STRING;
                buildText = new StringBuilder();
                consume();
                while ((GenericInput.ENDOFFILE != bgi.peek()) && ('"' != bgi.peek()))
                {
                    buildText.append((char) bgi.peek());
                    consume();
                }
                if (GenericInput.ENDOFFILE == bgi.peek())
                {
                    tokenType = Lexeme.INVALID;
                }
                consume();
                text = buildText.toString();
                break;
            case '(':
                consume();
                text = "(";
                tokenType = Lexeme.OPEN_PARENS;
                break;
            case ')':
                consume();
                text = ")";
                tokenType = Lexeme.CLOSE_PARENS;
                break;
            case '[':
                consume();
                text = "[";
                tokenType = Lexeme.OPEN_BRACKET;
                break;
            case ']':
                consume();
                text = "]";
                tokenType = Lexeme.CLOSE_BRACKET;
                break;
            case '{':
                consume();
                text = "{";
                tokenType = Lexeme.OPEN_BRACE;
                break;
            case '}':
                consume();
                text = "}";
                tokenType = Lexeme.CLOSE_BRACE;
                break;
            case ':':
                consume();
                text = ":";
                tokenType = Lexeme.ALTERNATIVE;
                break;
            case '=':
                consume();
                text = "=";
                tokenType = Lexeme.EQUALITY;
                break;
            case '<':
                consume();
                if ('>' == bgi.peek())
                {
                    consume();
                    text = "<>";
                    tokenType = Lexeme.INEQUALITY;
                }
                else if ('=' == bgi.peek())
                {
                    consume();
                    text = "<=";
                    tokenType = Lexeme.LESS_THAN_OR_EQUAL_TO;
                }
                else
                {
                    text = "<";
                    tokenType = Lexeme.LESS_THAN;
                }
                break;
            case '?':
                consume();
                text = "?";
                tokenType = Lexeme.CONDITIONAL;
                break;
            case '&':
                consume();
                text = "&";
                tokenType = Lexeme.AND;
                break;
            case '|':
                consume();
                text = "|";
                tokenType = Lexeme.OR;
                break;
            case '>':
                consume();
                if ('=' == bgi.peek())
                {
                    consume();
                    text = ">=";
                    tokenType = Lexeme.GREATER_THAN_OR_EQUAL_TO;
                }
                else
                {
                    text = ">";
                    tokenType = Lexeme.GREATER_THAN;
                }
                break;
            case '+':
                consume();
                text = "+";
                tokenType = Lexeme.PLUS;
                break;
            case '-':
                consume();
                text = "-";
                tokenType = Lexeme.MINUS;
                break;
            case '*':
                consume();
                text = "*";
                tokenType = Lexeme.MULTIPLY;
                break;
            case '/':
                consume();
                text = "/";
                tokenType = Lexeme.DIVIDE;
                break;
            case '%':
                consume();
                text = "%";
                tokenType = Lexeme.CROSS;
                break;
            case '^':
                consume();
                text = "^";
                tokenType = Lexeme.POWER;
                break;
            case '!':
                consume();
                text = "!";
                tokenType = Lexeme.NOT;
                break;
            default:
                // Why can't I just construct a string from a char?
                text = new String() + (char) bgi.peek();
                consume();
                tokenType = Lexeme.INVALID;
                break;
            }
        }

        nextToken = new Token(tokenType, text, sourceName, lineNo, charNo);
    }

    private static final class BufferedGenericInput
    {

        private final GenericInput input;
        private final java.util.LinkedList<Character> buffer;
        private boolean endOfFile;

        public BufferedGenericInput(GenericInput toBuffer)
        {
            input = toBuffer;
            buffer = new java.util.LinkedList<Character>();
            endOfFile = false;
        }

        private void fill (int count) throws FatalException
        {
            if (true == endOfFile)
            {
                return;
            }

            for (int i = 0; i < count; ++i)
            {
                int it = input.getNextCharacter();
                
                if (GenericInput.ENDOFFILE == it)
                {
                    endOfFile = true;
                    return;
                }
                else if (true == Character.isValidCodePoint(it))
                {
                    buffer.add(Character.valueOf( (char) it ));
                }
                else
                {
                    throw new FatalException("Invalid character returned to BufferedGenericInput");
                }
            }
        }

        public int peek () throws FatalException
        {
            return peek(0);
        }

        public int peek (int lookahead) throws FatalException
        {
            // Do we have the data already?
            if (buffer.size() > lookahead)
            {
                return buffer.get(lookahead).charValue();
            }

            // Try to add the amount of needed data
            fill(lookahead - buffer.size() + 1);

            // Did we get enough input?
            if (buffer.size() > lookahead)
            {
                return buffer.get(lookahead).charValue();
            }

            // We must be at the End of the File.
            return GenericInput.ENDOFFILE;
        }

        public int consume () throws FatalException
        {
            // Do we have the data already?
            if (0 != buffer.size())
            {
                return buffer.removeFirst().charValue();
            }

            // Try to add a character
            fill(1);

            // Did we get new input?
            if (0 != buffer.size())
            {
                return buffer.removeFirst().charValue();
            }

            // We must be at the End of the File.
            return GenericInput.ENDOFFILE;
        }

    }

}
