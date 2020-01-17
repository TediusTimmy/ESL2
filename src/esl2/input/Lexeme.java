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

public enum Lexeme
{
    INVALID,
    END_OF_FILE,
    LEXER_NEVER_RETURNS_THIS,

    FUNCTION,
    COMMA,
    END,

    IDENTIFIER,
    NUMBER,
    STRING,

    OPEN_PARENS,
    CLOSE_PARENS,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    OPEN_BRACE,
    CLOSE_BRACE,
    PERIOD,

    SET,
    TO,
    CALL,
    IF,
    THEN,
    ELSE,
    ELSEIF,
    WHILE,
    DO,
    SELECT,
    FROM,
    CASE,
    IS,
    ALSO,
    ABOVE,
    BELOW,
    BREAK,
    CONTINUE,
    RETURN,
    FOR,
    DOWNTO,
    STEP,
    IN,

    EQUALITY,
    INEQUALITY,
    CONDITIONAL,
    ALTERNATIVE,
    AND,
    OR,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN_OR_EQUAL_TO,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    CROSS,
    POWER,
    NOT
}
