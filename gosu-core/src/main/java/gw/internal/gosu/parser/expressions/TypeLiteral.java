/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.GosuClassCompilingStack;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.exceptions.ParseWarningForDeprecatedMember;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

/**
 * Represents a Type literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public class TypeLiteral extends Literal implements ITypeLiteralExpression
{
  private static final ThreadLocal<Boolean> _isComputingIsDeprecated  = new ThreadLocal<>();
  private Expression _packageExpr;
  private boolean _ignoreTypeDeprecation;

  public TypeLiteral( IType type, boolean ignoreTypeDeprecation )
  {
    _ignoreTypeDeprecation = ignoreTypeDeprecation;
    setType( type );
  }

  public TypeLiteral( IType type )
  {
    setType( type );
  }

  public TypeLiteral()
  {
  }

  /**
   * This expression is of NamespaceType. It will be either an Identifier or a MemberAccess.
   */
  public Expression getPackageExpression()
  {
    return _packageExpr;
  }
  public void setPackageExpression( Expression packageExpr )
  {
    _packageExpr = packageExpr;
  }

  @Override
  public void setType( IType type )
  {
    if( type instanceof MetaType)
    {
      if( !((MetaType)type).isLiteral() )
      {
        type = MetaType.getLiteral( ((MetaType)type).getType() );
      }

      super.setType( type );
    }
    else
    {
      super.setType( MetaType.getLiteral( type ) );
    }

    handleDeprecated();
  }

  public void handleDeprecated()
  {
    IType gosuClass = GosuClassCompilingStack.getCurrentCompilingType();

    if( !_ignoreTypeDeprecation && (!(gosuClass instanceof IGosuClass) || ((IGosuClass)gosuClass).isCompilingDefinitions()) )
    {
      Boolean isComputing = _isComputingIsDeprecated.get();
      if( isComputing != Boolean.TRUE && getType() != null && !isEnclosureDeprecated( gosuClass ) )
      {
        IType thisType = getType().getType();
        if( thisType != null )
        {
          _isComputingIsDeprecated.set( Boolean.TRUE );
          try
          {
            ITypeInfo typeInfo = thisType.getTypeInfo();
            if( typeInfo != null && typeInfo.isDeprecated() )
            {
              //noinspection ThrowableInstanceNeverThrown
              addParseWarning( new ParseWarningForDeprecatedMember(null, thisType.getDisplayName(), thisType.getDisplayName() ) );
            }
          }
          finally
          {
            _isComputingIsDeprecated.set(Boolean.FALSE);
          }
        }
      }
    }
  }

  private boolean isEnclosureDeprecated( IType type )
  {
    if( !(type instanceof IGosuClass) )
    {
      return false;
    }

    IGosuClassInternal gsClass = (IGosuClassInternal)type;
    GosuParser parser = (GosuParser)gsClass.getParser();
    return parser != null && parser.isIgnoreTypeDeprecation();
  }

  /**
   * @param types If this is a parameterized type, these are the parameter types.
   */
  public void setParameterTypes( IType[] types )
  {
    if( types.length == 0 )
    {
      return;
    }

    IType parameterizedType = getType().getType().getParameterizedType( types );
    setType( ensureLiteral( parameterizedType ) );
  }

  private IType ensureLiteral( IType type )
  {
    if( type instanceof IMetaType )
    {
      return MetaType.getLiteral( ensureLiteral( ((IMetaType)type).getType() ) );
    }
    return MetaType.getLiteral( type );
  }

  @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDoesntDeclareCloneNotSupportedException"})
  @Override
  public Object clone()
  {
    TypeLiteral clone = new TypeLiteral( getType() );
    clone.setLocation( getLocation() );
    return clone;
  }

  public boolean isCompileTimeConstant()
  {
    IType type = getType().getType();
    return (!type.isParameterizedType() || TypeLord.getDefaultParameterizedType( type ) == type) &&
           !(type instanceof TypeVariableType);
  }

  public IType evaluate()
  {
    return getType().getType();
  }

  @Override
  public String toString()
  {
    return TypeLord.getPureGenericType( getType().getType() ).getName();
  }

  public MetaType getType()
  {
    MetaType type = getTypeImpl();
    if( type != null && TypeSystem.isDeleted( type.getType() ) ) {
      type = (MetaType) TypeSystem.getErrorType().getMetaType();
    }
    return type;
  }

  @Override
  protected MetaType getTypeImpl() {
    return (MetaType)super.getTypeImpl();
  }

}
