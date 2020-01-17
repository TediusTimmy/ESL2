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

package esl2.test;

import esl2.types.DoubleValue;
import esl2.types.StringValue;
import esl2.types.ValueType;

public final class ExtendedTypeTest
{

    public static void main(String[] args)
    {
        ValueType one = new ExtendedScalarValue(12.0);
        ValueType two = new ExtendedScalarValue(25.0);
        ValueType three = new ExtendedEmptyValue();
        ValueType four = new DoubleValue(52.0);
        ValueType five = new StringValue("Hello");

        System.out.println("one   < one   : " + one.sort(one) + " 0");
        System.out.println("one   < two   : " + one.sort(two) + " -1");
        System.out.println("two   < two   : " + two.sort(two) + " 0");
        System.out.println("two   < one   : " + two.sort(one) + " 1");
        System.out.println("one   < three : " + one.sort(three) + " -1");
        System.out.println("three < two   : " + three.sort(one) + " 1");
        System.out.println("two   < three : " + two.sort(three) + " -1");
        System.out.println("three < two   : " + three.sort(two) + " 1");
        System.out.println("one   < four  : " + one.sort(four) + " 1");
        System.out.println("four  < one   : " + four.sort(one) + " -1");
        System.out.println("one   < five  : " + one.sort(five) + " 1");
        System.out.println("five  < one   : " + five.sort(one) + " -1");

        System.out.println("one   = one   : " + (one.compare(one) ? "true" : "false") + " true");
        System.out.println("one   = two   : " + (one.compare(two) ? "true" : "false") + " false");
        System.out.println("one   = three : " + (one.compare(three) ? "true" : "false") + " false");
        System.out.println("one   = four  : " + (one.compare(four) ? "true" : "false") + " false");
        System.out.println("one   = five  : " + (one.compare(five) ? "true" : "false") + " false");
        System.out.println("two   = one   : " + (two.compare(one) ? "true" : "false") + " true");
        System.out.println("two   = two   : " + (two.compare(two) ? "true" : "false") + " false");
        System.out.println("two   = three : " + (two.compare(three) ? "true" : "false") + " false");
        System.out.println("two   = four  : " + (two.compare(four) ? "true" : "false") + " false");
        System.out.println("two   = five  : " + (two.compare(five) ? "true" : "false") + " false");
        System.out.println("three = three : " + (three.compare(three) ? "true" : "false") + " true");
    }

}
