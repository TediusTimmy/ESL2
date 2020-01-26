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

import esl2.engine.expression.Expression;

public final class TestCase
{

    public final String system;
    public final String outFile;
    public final long randomSeed;
    public final Expression input;

    public TestCase(String system, String outFile, long randomSeed, Expression input)
    {
        this.system = system;
        this.outFile = outFile;
        this.randomSeed = randomSeed;
        this.input = input;
    }

}
