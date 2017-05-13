/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import manifold.api.host.RefreshKind;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.ITypeRef;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;


/**
 * The 'eval' operator as an expression:
 * <pre>
 * <i>eval-expression</i>
 *   <b>eval</b> <b>(</b> &lt;expression&gt; <b>)</b>
 * </pre>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class EvalExpression extends Expression implements IEvalExpression
{
  private ITypeUsesMap _typeUsesMap;
  private Expression _expression;
  private List<ICapturedSymbol> _capturedForBytecode;
  private Map<String, ITypeVariableDefinition> _capturedTypeVars;
  private Map<String, IGosuProgramInternal> _cacheProgramByFingerprint;
  private int _refreshChecksum = 0;


  /**
   * Constructs an 'eval' expression.
   */
  public EvalExpression( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap;
    _type = GosuParserTypes.GENERIC_BEAN_TYPE();
    // Note, not a concurrent map because the type sys lock is acquired during access/modification (see GosuProgramParse.parseEval)
    _cacheProgramByFingerprint = new ProgramCache();
  }

  public void setCapturedSymbolsForBytecode( List<ICapturedSymbol> captured )
  {
    _capturedForBytecode = captured;
  }
  public List<ICapturedSymbol> getCapturedForBytecode()
  {
    return _capturedForBytecode;
  }

  public void cacheProgram( String strTypeName, IGosuProgramInternal gsClass )
  {
    clearCacheOnChecksumChange();
    _cacheProgramByFingerprint.put( strTypeName, gsClass );
  }
  public IGosuProgramInternal getCachedProgram( String strTypeName )
  {
    clearCacheOnChecksumChange();
    return _cacheProgramByFingerprint.get( strTypeName );
  }

  private void clearCacheOnChecksumChange() {
    if (_refreshChecksum != TypeSystem.getRefreshChecksum()) {
      _cacheProgramByFingerprint.clear();
      _refreshChecksum = TypeSystem.getRefreshChecksum();
    }
  }

  public ITypeUsesMap getTypeUsesMap()
  {
    return _typeUsesMap;
  }

  /**
   * The string expression containing Gosu code to evaluate/execute.
   */
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }
  public Expression getExpression()
  {
    return _expression;
  }
  
  /**
   * Evaluates/executes the Gosu in the expression.
   *
   * @return The value of an expression or the return value of a program.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return "eval( " + _expression.toString() + " )\n";
  }

  public void setCapturedTypeVars( Map<String, ITypeVariableDefinition> typeVariables )
  {
    for( Iterator<ITypeVariableDefinition> iter = typeVariables.values().iterator(); iter.hasNext(); )
    {
      ITypeVariableDefinition tvd = iter.next();
      if( !(tvd.getEnclosingType() instanceof IFunctionType) )
      {
        iter.remove();
      }
    }
    _capturedTypeVars = typeVariables;
  }
  public Map<String, ITypeVariableDefinition> getCapturedTypeVars()
  {
    return _capturedTypeVars;
  }

  static class ProgramCache extends LinkedHashMap<String, IGosuProgramInternal>
  {
    private static final int CACHE_SIZE = 100;

    public ProgramCache()
    {
      super( CACHE_SIZE );
    }

    @Override
    protected boolean removeEldestEntry( Map.Entry<String, IGosuProgramInternal> eldest )
    {
      TypeSystem.lock();
      try
      {
        if( size() > CACHE_SIZE )
        {
          IGosuProgramInternal program = eldest.getValue();
          if( !program.getInnerClasses().isEmpty() )
          {
            // Can't remove from type system since its inner classes may be returned as part of eval's results
            return true;
          }

          ITypeRef type = (ITypeRef)program;
          // Directly invalidate so as not to wreak havoc on type system at runtime.
          // Also avoids huge perf penalty.
          type._setStale(RefreshKind.MODIFICATION);

          //!!! NEVER! refresh types at runtime EVER!
          //IModule module = type.getTypeLoader().getModule();
          //String strName = type.getName();
          //System.out.println( "Removing Type: " + strName );
          //TypeSystem.refresh( type, true );
          //TypeSystem.notifyOfTypeDeletion( new String[] {strName}, module );

          return true;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
      return false;
    }
  }
}
