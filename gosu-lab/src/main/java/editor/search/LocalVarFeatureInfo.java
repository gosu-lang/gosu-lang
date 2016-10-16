package editor.search;

import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;

/**
 */
public class LocalVarFeatureInfo implements IFeatureInfo
{
  private final ISymbol _symbol;
  private final IFeatureInfo _container;

  public LocalVarFeatureInfo( IIdentifierExpression id )
  {
    _symbol = findRootSymbol( id.getSymbol() );
    _container = findMethodInfo( id );
  }

  public LocalVarFeatureInfo( IVarStatement decl )
  {
    _symbol = decl.getSymbol();
    _container = findMethodInfo( decl );
  }

  public LocalVarFeatureInfo( ILocalVarDeclaration decl )
  {
    _symbol = decl.getSymbol();
    _container = findMethodInfo( decl );
  }

  public ISymbol getSymbol()
  {
    return _symbol;
  }

  @Override
  public IFeatureInfo getContainer()
  {
    return _container;
  }

  @Override
  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  @Override
  public String getName()
  {
    return _symbol.getName();
  }

  @Override
  public String getDisplayName()
  {
    return _symbol.getDisplayName();
  }

  @Override
  public String getDescription()
  {
    return _symbol.getDisplayName();
  }

  private ISymbol findRootSymbol( ISymbol symbol )
  {
    while( symbol instanceof ICapturedSymbol )
    {
      symbol = ((ICapturedSymbol)symbol).getReferredSymbol();
    }
    return symbol;
  }

  private IFeatureInfo findMethodInfo( IParsedElement pe )
  {
    if( pe == null )
    {
      return null;
    }

    if( pe instanceof IFunctionStatement )
    {
      return ((IFunctionStatement)pe).getDynamicFunctionSymbol().getMethodOrConstructorInfo();
    }

    return findMethodInfo( pe.getParent() );
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    LocalVarFeatureInfo that = (LocalVarFeatureInfo)o;

    if( _symbol == that._symbol )
    {
      return true;
    }

    if( _container != null ? !_container.equals( that._container ) : that._container != null )
    {
      return false;
    }
    if( _symbol != null ? !_symbol.equals( that._symbol ) : that._symbol != null )
    {
      return false;
    }
    if( _symbol != null )
    {
      return _symbol.getIndex() == that._symbol.getIndex();
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _symbol != null ? _symbol.hashCode() : 0;
    result = 31 * result + (_container != null ? _container.hashCode() : 0);
    return result;
  }
}
