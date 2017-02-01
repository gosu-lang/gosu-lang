/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.internal.gosu.ir.nodes.IRPropertyFromPropertyInfo;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.GosuVarPropertyInfo;
import gw.internal.gosu.parser.JavaFieldPropertyInfo;
import gw.internal.gosu.parser.JavaPropertyInfo;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.SuperAccess;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaPropertyInfo;

/**
 */
public class MemberAssignmentStatementTransformer extends AbstractStatementTransformer<MemberAssignmentStatement> {
  public static IRStatement compile( TopLevelTransformationContext cc, MemberAssignmentStatement stmt ) {
    MemberAssignmentStatementTransformer gen = new MemberAssignmentStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private MemberAssignmentStatementTransformer( TopLevelTransformationContext cc, MemberAssignmentStatement stmt ) {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl() {
    String strMemberName = _stmt().getMemberName();
    if( strMemberName == null ) {
      // If the name is false, it's of the form foo[bar] where bar is a variable.  We have to do the access reflectively
      IRExpression memberNameExpression = ExpressionTransformer.compile( _stmt().getMemberExpression(), _cc() );
      if( _stmt().getRootExpression().getType() instanceof IMetaType ) {
        // If it's a meta type, assume it's a static property
        return reflectivelySetProperty( _stmt().getRootExpression().getType(), memberNameExpression, null, false );
      }
      else {
        return reflectivelySetProperty( _stmt().getRootExpression().getType(), memberNameExpression, ExpressionTransformer.compile( _stmt().getRootExpression(), _cc() ), true );
      }
    }
    else {
      try {
        IPropertyInfo pi = BeanAccess.getPropertyInfo( _stmt().getRootExpression().getType(), strMemberName, null, null, null );
        IRProperty irProperty = IRPropertyFactory.createIRProperty( pi );
        IRType propertyType = irProperty.getType();
        if( pi.isStatic() ) {
          return assignStaticMember( pi, irProperty, propertyType );
        }
        else {
          return assignInstanceMember( pi, irProperty );
        }
      }
      catch( ParseException e ) {
        throw new RuntimeException( e );
      }
    }
  }

  private IRStatement assignInstanceMember( IPropertyInfo pi, IRProperty irProperty ) {
    IExpression rootExpr = _stmt().getRootExpression();
    IRExpression root;
    IRSymbol tempRoot;
    IRStatement ret;

    IRAssignmentStatement tempRootAssn;
    if( _stmt().isCompoundStatement() )
    {
      IType concreteType = getConcreteType( rootExpr.getType() );
      root = pushRootExpression( concreteType, rootExpr, irProperty );
      tempRoot = _cc().makeAndIndexTempSymbol( getDescriptor(concreteType) );
      tempRootAssn = buildAssignment( tempRoot, root );
      root = identifier( tempRoot );
      ExpressionTransformer.addTempSymbolForCompoundAssignment( rootExpr, tempRoot );
    }
    else
    {
      root = pushRootExpression( getConcreteType( rootExpr.getType() ), rootExpr, irProperty );
      tempRootAssn = null;
    }

    if( irProperty.isBytecodeProperty() ) {
      IRExpression rhs = compileRhs( irProperty);
      if( irProperty.isField() ) {
        ret = setField( irProperty, root, rhs );
      }
      else if( isWriteMethodMissingAndUsingLikeNamedField( irProperty ) ) {
        ret = setField( irProperty.getOwningIType(),
                         getField( ((IRPropertyFromPropertyInfo)irProperty).getTerminalProperty() ),
                         getWritableType( irProperty ),
                         irProperty.getAccessibility(),
                         root,
                         rhs );
      }
      else {
        if( rootExpr instanceof SuperAccess ) {
          return buildMethodCall( callSpecialMethod( getDescriptor( rootExpr.getType() ), irProperty.getSetterMethod(), root, exprList( rhs ) ) );
        }
        else if( isSuperCall( _stmt().getRootExpression() ) ) {
          ret =  buildMethodCall( callSpecialMethod( getDescriptor( _cc().getSuperType() ), irProperty.getSetterMethod(), root, exprList( rhs ) ) );
        }
        else {
          IRExpression irMethodCall = callMethod( irProperty.getSetterMethod(), root, exprList( rhs ) );
          if( !(irProperty.getOwningIType() instanceof IGosuEnhancement) ) {
            assignStructuralTypeOwner( rootExpr, irMethodCall );
          }
          ret = buildMethodCall( irMethodCall );
        }
      }
    }
    else {
      ret = reflectivelySetProperty( pi.getOwnersType(), pushConstant( pi.getDisplayName() ), root, false );
    }
    if( _stmt().isCompoundStatement() )
    {
      ExpressionTransformer.clearTempSymbolForCompoundAssignment();
      return new IRStatementList( false, tempRootAssn, ret );
    }
    return ret;
  }

  private IRExpression pushRootExpression( IType rootType, IExpression rootExpr, IRProperty pi ) {
    // Push the root expression value
    IRExpression root = ExpressionTransformer.compile( rootExpr, _cc() );
    //... and make sure it's boxed for the method call
    root = boxValue( rootType, root );

    if( pi != null && !pi.isStatic() ) {
      IRType type = pi.getTargetRootIRType();
      if( !type.isAssignableFrom( root.getType() ) ) {
        root = buildCast( type, root );
      }
    }

    return root;
  }

  private boolean isWriteMethodMissingAndUsingLikeNamedField( IRProperty irPi ) {
    if( !(irPi instanceof IRPropertyFromPropertyInfo) ) {
      return false;
    }

    IPropertyInfo terminalPi = ((IRPropertyFromPropertyInfo)irPi).getTerminalProperty();
    return terminalPi instanceof IJavaPropertyInfo && isField( terminalPi );
  }

  private IRExpression compileRhs( IRProperty pi ) {
    IRExpression rhs = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );

    if( !pi.isStatic() ) {
      IRType type = getWritableType( pi );
      if( !type.isAssignableFrom( rhs.getType() ) ) {
        rhs = buildCast( type, rhs );
      }
    }
    return rhs;
  }

  private IRType getWritableType( IRProperty pi ) {
    if( !(pi instanceof IRPropertyFromPropertyInfo) ) {
      return pi.getType();
    }

    IRType type;
    IPropertyInfo terminalPi = ((IRPropertyFromPropertyInfo)pi).getTerminalProperty();
    if( terminalPi instanceof IJavaPropertyInfo && isField( terminalPi ) ) {
      type = IRTypeResolver.getDescriptor( ((IJavaPropertyInfo)terminalPi).getPublicField().getType() );
    }
    else {
      type = pi.getType();
    }
    return type;
  }

  private IRStatement reflectivelySetProperty( IType type, IRExpression propertyName, IRExpression root, boolean forceDynamic ) {
    IRExpression value = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    IRExpression setter;
    if( forceDynamic || type instanceof IPlaceholder ) {
      // Placeholder types, such as snapshot types, have to get properties dynamically.  They can't have static properties, though.
      if( root == null ) {
        throw new IllegalArgumentException( "Cannot invoke a static property reflectively on a placeholder type or via dynamic reflection" );
      }
      setter = callStaticMethod( GosuRuntimeMethods.class, "setPropertyDynamically", new Class[]{Object.class, String.class, Object.class},
                                 exprList( root, propertyName, value ) );
    }
    else {
      // Everything else should dispatch to the statically-determined property
      setter = callStaticMethod( GosuRuntimeMethods.class, "setProperty", new Class[]{Object.class, IType.class, String.class, Object.class},
                                 exprList( root, pushType( type ), propertyName, value ) );
    }
    return buildMethodCall( setter );
  }

  private IRStatement assignStaticMember( IPropertyInfo pi, IRProperty irProperty, IRType propertyType ) {
    // Unwrap the property, and use the real owner's type as the type to compile against
    while( pi instanceof IPropertyInfoDelegate ) {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }
    IType rootType = pi.getOwnersType();


    if( irProperty.isBytecodeProperty() ) {
      IRExpression rhs = compileRhs( irProperty );
      if( irProperty.isField() ) {
        return setStaticField( rootType, getField( pi ), propertyType, AccessibilityUtil.forFeatureInfo( pi ), rhs );
      }
      else {
        return buildMethodCall( callMethod( irProperty.getSetterMethod(), null, exprList( rhs ) ) );
      }
    }
    else {
      return reflectivelySetProperty( pi.getOwnersType(), pushConstant( pi.getDisplayName() ), nullLiteral(), false );
    }
  }

  private boolean isField( IPropertyInfo pi ) {
    while( pi instanceof IPropertyInfoDelegate ) {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }

    if( pi instanceof JavaPropertyInfo ) {
      JavaPropertyInfo jpi = (JavaPropertyInfo)pi;
      return jpi.getWriteMethodInfo() == null && jpi.getPublicField() != null;
    }

    return pi instanceof GosuVarPropertyInfo ||
           pi instanceof JavaFieldPropertyInfo ||
           ((pi instanceof IPropertyInfoDelegate) && isField( ((IPropertyInfoDelegate)pi).getSource() ));
  }

  private String getField( IPropertyInfo pi ) {
    if( !isField( pi ) ) {
      throw new IllegalArgumentException( pi.getName() + " is not a 'field' property" );
    }

    while( pi instanceof IPropertyInfoDelegate ) {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }

    if( pi instanceof IJavaPropertyInfo ) {
      IJavaPropertyInfo jpi = (IJavaPropertyInfo)pi;
      return jpi.getPublicField().getName();
    }

    if( pi.getClass() == JavaFieldPropertyInfo.class ) {
      return ((JavaFieldPropertyInfo)pi).getField().getName();
    }
    return pi.getName();
  }

  private boolean isSuperCall( IExpression rootExpr ) {
    return rootExpr instanceof Identifier && Keyword.KW_super.equals( ((Identifier)rootExpr).getSymbol().getName() );
  }
}