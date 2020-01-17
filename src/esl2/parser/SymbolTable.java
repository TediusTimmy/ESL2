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
import java.util.Set;
import java.util.TreeMap;

import esl2.engine.ArgGetter;
import esl2.engine.ArgSetter;
import esl2.engine.Getter;
import esl2.engine.LocalGetter;
import esl2.engine.LocalSetter;
import esl2.engine.Setter;
import esl2.engine.expression.Constant;
import esl2.engine.expression.Expression;
import esl2.engine.expression.FunctionCall;
import esl2.input.Token;
import esl2.types.FunctionPointerValue;
import esl2.types.ProgrammingException;

public final class SymbolTable
{

    public static final int INVALID_INDEX = -1;

    private final ArrayList<FrameDebugInfo> frames;

    private final FunctionPairs fun;

    private final ArrayList<String> loopCounter;
    private final TreeMap<String, Integer> loops;

    public ArrayList<FrameDebugInfo> frameInfo;
    public FunctionPairs addedHere;

    public GetterSetter gs;
    public GlobalGetterSetter globalGS;
    public Set<String> forbiddenIdentifiers;
    public Set<String> constants;

    public FunctionPointerValue insertIndex;
    public FunctionPointerValue pushBackIndex;

    public SymbolTable()
    {
        frames = new ArrayList<FrameDebugInfo>();
        fun = new FunctionPairs();

        loopCounter = new ArrayList<String>();
        loops = new TreeMap<String, Integer>();

        frameInfo = null;
        addedHere = null;
        gs = null;
        globalGS = null;
        forbiddenIdentifiers = null;
        insertIndex = null;
        pushBackIndex = null;
    }

    public void pushContext()
    {
        frames.add(new FrameDebugInfo());
    }

    public void popContext()
    {
        frames.remove(frames.size() - 1);
    }

    public void commitContext(String name)
    {
        frames.get(frames.size() - 1).frameName = name;
        if (null != frameInfo)
        {
            frameInfo.add(frames.get(frames.size() - 1));
        }
    }

    public ArrayList<FrameDebugInfo> getContext()
    {
        return frames;
    }

    private void assertGetterSetter()
    {
        if (null == gs)
        {
            throw new ProgrammingException("SymbolTable lacks a master getter/setter list.");
        }
    }

    public void addArgument(String name)
    {
        frames.get(frames.size() - 1).args.put(name, Integer.valueOf(frames.get(frames.size() - 1).args.size()));
        frames.get(frames.size() - 1).argNames.add(name);

        assertGetterSetter();
        if (frames.get(frames.size() - 1).args.size() > gs.argsGetters.size())
        {
            gs.argsSetters.add(new ArgSetter(gs.argsGetters.size()));
            gs.argsGetters.add(new ArgGetter(gs.argsGetters.size()));
        }
    }

    public void addLocal(String name)
    {
        frames.get(frames.size() - 1).locals.put(name, Integer.valueOf(frames.get(frames.size() - 1).locals.size()));
        frames.get(frames.size() - 1).localNames.add(name);

        assertGetterSetter();
        if (frames.get(frames.size() - 1).locals.size() > gs.localsGetters.size())
        {
            gs.localsSetters.add(new LocalSetter(gs.localsGetters.size()));
            gs.localsGetters.add(new LocalGetter(gs.localsGetters.size()));
        }
    }

    private ProgrammingException dieName(String type, String name)
    {
        return new ProgrammingException("Request for non-existant " + type + " " + name + ".");
    }

    public Getter getVariableGetter(String name)
    {
        Integer loc = frames.get(frames.size() - 1).args.get(name);
        if (null != loc)
        {
            assertGetterSetter();
            return gs.argsGetters.get(loc.intValue());
        }

        loc = frames.get(frames.size() - 1).locals.get(name);
        if (null != loc)
        {
            assertGetterSetter();
            return gs.localsGetters.get(loc.intValue());
        }

        if (null != globalGS)
        {
            Getter get = globalGS.getters.get(name);
            if (null != get)
            {
                return get;
            }
        }

        throw dieName("variable", name);
    }

    public Setter getVariableSetter(String name)
    {
        Integer loc = frames.get(frames.size() - 1).args.get(name);
        if (null != loc)
        {
            assertGetterSetter();
            return gs.argsSetters.get(loc.intValue());
        }

        loc = frames.get(frames.size() - 1).locals.get(name);
        if (null != loc)
        {
            assertGetterSetter();
            return gs.localsSetters.get(loc.intValue());
        }

        if (null != globalGS)
        {
            Setter get = globalGS.setters.get(name);
            if (null != get)
            {
                return get;
            }
        }

        throw dieName("variable", name);
    }

    public int getFunctionLocation(String name)
    {
        Integer loc = fun.funs.get(name);
        if (null != loc)
        {
            return loc.intValue();
        }

        throw dieName("function", name);
    }

    public int getNArgs()
    {
        return frames.get(frames.size() - 1).args.size();
    }

    public void addFunction(String name, int location)
    {
        fun.funs.put(name, Integer.valueOf(location));
        if (null != addedHere)
        {
            addedHere.funs.put(name, Integer.valueOf(location));
        }
    }

    public void addAll(FunctionPairs funs)
    {
        this.fun.funs.putAll(funs.funs);
        this.fun.args.putAll(funs.args);
    }

    public int getNLocals()
    {
        return frames.get(frames.size() - 1).locals.size();
    }

    public IdentifierType lookup(String name)
    {
        if (true == frames.get(frames.size() - 1).args.containsKey(name)) return IdentifierType.VARIABLE;
        if (true == frames.get(frames.size() - 1).locals.containsKey(name)) return IdentifierType.VARIABLE;
        if ((null != globalGS) && (true == globalGS.getters.containsKey(name))) return IdentifierType.VARIABLE;
        if (true == fun.funs.containsKey(name)) return IdentifierType.FUNCTION;
        if ((null != forbiddenIdentifiers) && (true == forbiddenIdentifiers.contains(name))) return IdentifierType.FORBIDDEN;
        return IdentifierType.UNDEFINED;
    }

    public Expression buildPushBack(Token token, Expression coll, Expression elem)
    {
        ArrayList<Expression> args = new ArrayList<Expression>();
        args.add(coll);
        args.add(elem);
        if (null == pushBackIndex)
        {
            throw new ProgrammingException("Cannot resolve request for PushBack.");
        }
        return new FunctionCall(token, new Constant(token, pushBackIndex), args);
    }

    public Expression buildInsert(Token token, Expression coll, Expression key, Expression elem)
    {
        ArrayList<Expression> args = new ArrayList<Expression>();
        args.add(coll);
        args.add(key);
        args.add(elem);
        if (null == insertIndex)
        {
            throw new ProgrammingException("Cannot resolve request for Insert.");
        }
        return new FunctionCall(token, new Constant(token, insertIndex), args);
    }

    public boolean isConstant(String var)
    {
        if (null != constants)
        {
            return true == constants.contains(var);
        }
        return false;
    }

    public int newLoop()
    {
        loopCounter.add("");
        return loopCounter.size();
    }

    public int currentLoop()
    {
        if (0 == loopCounter.size())
        {
            return INVALID_INDEX;
        }
        return loopCounter.size();
    }

    public void nameLoop(String name)
    {
        loopCounter.set(loopCounter.size() - 1, name);
        loops.put(name, Integer.valueOf(loopCounter.size()));
    }

    public int getLoop(String name)
    {
        if (true == loops.containsKey(name))
        {
            return loops.get(name).intValue();
        }
        return INVALID_INDEX;
    }

    public void popLoop()
    {
        String loop = loopCounter.get(loopCounter.size() - 1);
        if (false == "".equals(loop))
        {
            loops.remove(loop);
        }
        loopCounter.remove(loopCounter.size() - 1);
    }

}
