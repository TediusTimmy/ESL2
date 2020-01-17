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

package esl2.engine.statement;

import java.util.ArrayList;

import esl2.engine.CallingContext;
import esl2.engine.FlowControl;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;

public final class StatementSeq extends Statement
{

    final ArrayList<Statement> seq;

    public StatementSeq(Token token, ArrayList<Statement> seq)
    {
        super(token);
        this.seq = seq;
    }

    @Override
    public FlowControl execute(CallingContext context) throws TypedOperationException, FatalException
    {
        for (Statement s : seq)
        {
            FlowControl temp = s.execute(context);
            if (null != temp)
            {
                return temp;
            }
        }
        return null;
    }

}
