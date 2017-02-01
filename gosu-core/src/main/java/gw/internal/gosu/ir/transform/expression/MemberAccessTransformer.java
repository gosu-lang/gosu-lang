/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ArrayExpansionPropertyInfo;
import gw.internal.gosu.parser.GosuVarPropertyInfo;
import gw.internal.gosu.parser.JavaFieldPropertyInfo;
import gw.internal.gosu.parser.LengthProperty;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.MetaTypeTypeInfo;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.Literal;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.MemberExpansionAccess;
import gw.internal.gosu.parser.expressions.SuperAccess;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.optimizer.SinglePropertyMemberAccessRuntime;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.Autocreate;
import gw.lang.ShortCircuitingProperty;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICustomExpressionRuntime;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.MemberAccessKind;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IExpando;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeInfoPropertyInfo;
import gw.lang.reflect.IUncacheableFeature;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 */
public class MemberAccessTransformer extends AbstractExpressionTransformer<MemberAccess>
{
  public static IRExpression compile( TopLevelTransformationContext cc, MemberAccess expr )
  {
    MemberAccessTransformer compiler = new MemberAccessTransformer( cc, expr );
    return compiler.compile();
  }

  private MemberAccessTransformer( TopLevelTransformationContext cc, MemberAccess expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IExpression rootExpr = _expr().getRootExpression();
    IType rootType = getConcreteType( rootExpr.getType() );
    IPropertyInfo pi = _expr().getPropertyInfo();

    if( isArrayExpansionProperty( pi ) )
    {
      MemberExpansionAccess expr = MemberExpansionAccess.wrap( _expr() );
      return MemberExpansionAccessTransformer.compile( _cc(), expr );
    }
    else if ( _expr().getExpressionRuntime() instanceof ICustomExpressionRuntime)
    {
      return handleCustomExpressionRuntime( (ICustomExpressionRuntime) _expr().getExpressionRuntime(), _expr().getType() );
    }
    else if( isTypeInfoProperty( pi ) )
    {
      ITypeInfoPropertyInfo typeInfoProp = (ITypeInfoPropertyInfo)pi;
      IPropertyInfo backingProp = typeInfoProp.getBackingPropertyInfo();
      return handleInstanceMemberAccess( rootExpr, backingProp.getOwnersType(), backingProp, IRPropertyFactory.createIRProperty( backingProp ) );
    }
    else if( isStatic( pi ) )
    {
      return handleStaticMemberAccess( rootExpr, rootType, pi, IRPropertyFactory.createIRProperty( pi ) );
    }
    else
    {
      return handleInstanceMemberAccess( rootExpr, rootType, pi, IRPropertyFactory.createIRProperty( pi ) );
    }
  }

  private boolean isArrayExpansionProperty( IPropertyInfo pi )
  {
    return pi instanceof ArrayExpansionPropertyInfo;
  }

  private boolean isTypeInfoProperty( IPropertyInfo pi )
  {
    return pi instanceof ITypeInfoPropertyInfo;
  }

  private IRExpression handleInstanceMemberAccess( IExpression rootExpr, IType rootType, IPropertyInfo pi, IRProperty irProperty )
  {
    boolean mightRequireAutoCreation = mightRequireAutoCreation( rootType, pi );
    boolean bShouldNullShortCircuitCheck = shouldNullShortCircuit( rootType, pi, mightRequireAutoCreation );

    if( mightRequireAutoCreation && !bShouldNullShortCircuitCheck )
    {
      throw new IllegalStateException( "Should never have an expression requiring auto-creation that doesn't null-shortcircuit" );
    }

    IRExpression root = pushRootExpression( rootType, rootExpr, irProperty );

    IRSymbol rootSymbol = null;
    IRExpression accessExpression;
    if( bShouldNullShortCircuitCheck )
    {
      // Both null-short-circuiting and auto-creation require that the root be stored off in a temporary symbol
      rootSymbol = _cc().makeAndIndexTempSymbol( root.getType() );
      accessExpression = buildAccessExpression( rootExpr, rootType, pi, irProperty, identifier( rootSymbol ) );
    }
    else
    {
      accessExpression = buildAccessExpression( rootExpr, rootType, pi, irProperty, root );
    }

    accessExpression = castIfTypeDerivedFromTypeVariable( irProperty, accessExpression );

    // If we need to auto-create, handle that before the outer null-shortcircuiting
    if( mightRequireAutoCreation )
    {
      accessExpression = handleAutoCreationWhenValueIsNull( pi, rootSymbol, accessExpression );
    }

    // If we need to null-short-circuit, wrap everything in a ternary expression like:
    // temp_root = root_expression
    // (temp_root == null ? null : temp_root.member)
    if( bShouldNullShortCircuitCheck )
    {
      accessExpression = buildComposite(
        buildAssignment( rootSymbol, root ),
        buildNullCheckTernary( identifier( rootSymbol ),
                               shortCircuitValue( accessExpression.getType() ),
                               accessExpression ) );
    }

    return accessExpression;
  }

  private boolean shouldNullShortCircuit( IType rootType, IPropertyInfo pi, boolean mightRequireAutoCreation )
  {
    return
      !rootType.isPrimitive() &&

      // Standard Gosu requires the ?. operator for null-safe short-circuit
      (_expr().getMemberAccessKind() == MemberAccessKind.NULL_SAFE ||

       // Backward compatibility for non-standard Gosu
       (!CommonServices.getEntityAccess().getLanguageLevel().isStandard() &&
        (!_expr().getType().isPrimitive() ||
         //## special case: boolean short-circuit to false
         _expr().getType() == JavaTypes.pBOOLEAN() ||
         //## special case: handle array.length short-circuit to 0
         _expr().getType() == JavaTypes.pINT() && isLengthProperty( pi ) ||
         // Any property with the ShortCircuitingProperty annotation will also short-circuit
         pi.hasAnnotation( JavaTypes.getGosuType( ShortCircuitingProperty.class ) ))) ||

       // If this expression in an l-value and auto-creation is required, all non-primitive expression types are considered null-safe 
       (mightRequireAutoCreation && !_expr().getType().isPrimitive()));
  }

  private IRExpression buildAccessExpression( IExpression rootExpr, IType rootType, IPropertyInfo pi, IRProperty irProperty, IRExpression root )
  {
    if( irProperty == null )
    {
      // a reflective member access.  Why is this the same type of ParsedElement?
      return callStaticMethod( ReflectUtil.class, "getProperty", new Class[]{Object.class, String.class},
                               exprList( root, pushString( _expr().getMemberExpression() ) ) );
    }

    if( irProperty.isField() )
    {
      IRExpression fieldGetter = getField( irProperty, root );
      return castIfTypeDerivedFromTypeVariable( irProperty, fieldGetter );
    }
    else if( isLengthProperty( pi ) )
    {
      return buildArrayLength( root );
    }
    else if( rootExpr instanceof SuperAccess)
    {
      return callSpecialMethod( getDescriptor( rootExpr.getType() ), irProperty.getGetterMethod(), root, exprList(  ) );
    }
    else if( isSuperCall( rootExpr ) )
    {
      return callSpecialMethod( getDescriptor( _cc().getSuperType() ), irProperty.getGetterMethod(), root, exprList() );
    }
    else if( isOuterCall( pi ) )
    {
      return pushOuter( getNextNonBlockOuter( rootType.getEnclosingType() ), rootType, root );
    }
    else if( irProperty.isBytecodeProperty() )
    {
      IRExpression irMethodCall = callMethod( irProperty.getGetterMethod(), root, exprList() );
      if( !(irProperty.getOwningIType() instanceof IGosuEnhancement) )
      {
        assignStructuralTypeOwner( rootExpr, irMethodCall );
      }
      return irMethodCall;
    }
    else
    {
      return callPropertyInfo( rootType, pi, irProperty, root );
    }
  }

  private boolean mightRequireAutoCreation(IType rootType, IPropertyInfo pi) {
    if(_expr().getExpressionRuntime() instanceof SinglePropertyMemberAccessRuntime) {
      if (((SinglePropertyMemberAccessRuntime) _expr().getExpressionRuntime()).isNestedInLhs()) {
        // The property in the path is null; attempt to dynamically set its value if @Autocreate annotation is present
        IType autocreateAnnotationType = GosuTypes.AUTOCREATE();
        List<IAnnotationInfo> list = pi.getAnnotationsOfType( autocreateAnnotationType );
        if (list != null && !list.isEmpty()) {
          return true;
        }
        else if( rootType instanceof IPlaceholder && ((IPlaceholder)rootType).isPlaceholder() ) {
          return true;
        }
        else {
          // TODO dlank - With a bit of work, the following can be replaced by adding @Autocreate annotations to the EntityTypeInfo's properties
          IEntityAccess ea = CommonServices.getEntityAccess();
          if( ea.isEntityClass( rootType ) && ea.isEntityClass(pi.getFeatureType()))
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  private IRExpression handleAutoCreationWhenValueIsNull(IPropertyInfo pi, IRSymbol rootSymbol, IRExpression accessExpression) {
    // If the access resulted in a null value, we need to auto-create it
    // To do that, we store the result in a temp variable, then generate a ternary expression like:
    // (result == null ? autoCreateValue(root) : result)
    IRSymbol resultSymbol = _cc().makeAndIndexTempSymbol( accessExpression.getType() );
    return buildComposite(
              buildAssignment( resultSymbol, accessExpression ),
              buildNullCheckTernary(
                  identifier( resultSymbol ),
                  autoCreateEntityValue(pi, rootSymbol),
                  identifier( resultSymbol ) ) );
  }


  private IRExpression autoCreateEntityValue(IPropertyInfo pi, IRSymbol rootSymbol) {
    return checkCast( _expr().getType(),
            callStaticMethod(MemberAccessTransformer.class, "autoCreateEntityInstance", new Class[]{Object.class, String.class, String.class},
                    exprList( identifier( rootSymbol ), pushConstant(pi.getOwnersType().getName()), pushConstant(pi.getName())) ) );
  }

  private IType getNextNonBlockOuter( IType type )
  {
    while( type instanceof IBlockClass )
    {
      type = type.getEnclosingType();
    }
    return type;
  }

  private boolean isEnhancementProperty( IPropertyInfo pi )
  {
    return getDelegatedEnhancementProperty( pi ) != null;
  }

  private IPropertyInfo getDelegatedEnhancementProperty( IPropertyInfo pi )
  {
    IPropertyInfo delegatePI = null;
    if( pi instanceof IPropertyInfoDelegate )
    {
      delegatePI = ((IPropertyInfoDelegate) pi).getSource();
      if( !isEnhancementType( delegatePI.getOwnersType() ) )
      {
        delegatePI = null;
      }
    }
    return delegatePI;
  }

  private IRExpression castIfTypeDerivedFromTypeVariable( IRProperty irProp, IRExpression root )
  {
    if( irProp == null )
    {
      return root;
    }

    IType type = _expr().getType();
    if( type != JavaTypes.pVOID() && !type.isPrimitive() )
    {
      if(!getDescriptor( type ).isAssignableFrom( irProp.getType() ))
      {
        return checkCast( type, root );
      }
    }

    return root;
  }

  private IRExpression handleStaticMemberAccess( IExpression rootExpr, IType rootType, IPropertyInfo pi, IRProperty irProperty )
  {
    rootType = maybeUnwrapMetaType( rootType );

    if( isTypeProperty( pi ) )
    {
      if( rootExpr instanceof TypeLiteral)
      {
        IRExpression result = checkCast( pi.getFeatureType(), pushType( rootType ) );
        return maybeEvalRoot( rootExpr, result );
      }
      else
      {
        return pushRootExpression( rootType, rootExpr, irProperty );
      }
    }
    else if( pi != null && irProperty.isBytecodeProperty() )
    {
      IRExpression result;
      if( irProperty.isField() )
      {
        result = getField( irProperty, null );
      }
      else
      {
        result = callMethod( irProperty.getGetterMethod(), null, exprList() );
      }
      return maybeEvalRoot( rootExpr, result);
    }
    else
    {
      return callPropertyInfo( rootType, pi, irProperty, pushType( rootType ) );
    }
  }

  private IRExpression maybeEvalRoot( IExpression rootExpr, IRExpression result )
  {
    //If the root is a non literal, we must evaluate it, even if we are going to ignore it.
    if( !(rootExpr instanceof Literal) )
    {
      return new IRCompositeExpression(
               new IRSyntheticStatement( ExpressionTransformer.compile( rootExpr, _cc() ) ),
               result);
    } else {
      return result;
    }
  }

  private boolean isTypeProperty( IPropertyInfo pi )
  {
    return pi instanceof MetaTypeTypeInfo.TypeProperty;
  }

  private String getField( IPropertyInfo pi )
  {
    if( !isField( pi ) )
    {
      throw new IllegalArgumentException( pi.getName() + " is not a 'field' property" );
    }

    while( pi instanceof IPropertyInfoDelegate )
    {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }

    // Note we don't want LengthProperty here...
    if( pi.getClass() == JavaFieldPropertyInfo.class )
    {
      return ((JavaFieldPropertyInfo)pi).getField().getName();
    }
    return pi.getName();
  }

  private boolean isField( IPropertyInfo pi )
  {
    return pi instanceof GosuVarPropertyInfo ||
           pi instanceof JavaFieldPropertyInfo ||
           ((pi instanceof IPropertyInfoDelegate) && isField( ((IPropertyInfoDelegate)pi).getSource() ));
  }

  private boolean isLengthProperty( IPropertyInfo pi )
  {
    return pi instanceof LengthProperty ||
           ((pi instanceof IPropertyInfoDelegate) && isLengthProperty( ((IPropertyInfoDelegate)pi).getSource() ));
  }

  private boolean isSuperCall( IExpression rootExpr )
  {
    return rootExpr instanceof Identifier && Keyword.KW_super.equals( ((Identifier)rootExpr).getSymbol().getName() );
  }

  private boolean isOuterCall( IPropertyInfo rootExpr )
  {
    return Keyword.KW_outer.equals( rootExpr.getName() );
  }

  private IRExpression callPropertyInfo( IType rootType, IPropertyInfo pi, IRProperty irProperty, IRExpression rawRoot )
  {
    IRSymbol rootSymbol = null;
    List<IRElement> preEvaluationStatements = new ArrayList<IRElement>();
    if( rawRoot != null )
    {
      rootSymbol = _cc().makeAndIndexTempSymbol( rawRoot.getType() );
      IRAssignmentStatement rootAssignment = buildAssignment( rootSymbol, rawRoot );
      preEvaluationStatements.add( rootAssignment );
    }

    IRExpression root;
    if( pi == null || pi.isStatic() )
    {
      root = nullLiteral();
    }
    else
    {
      IRStatement nullCheck = nullCheckVar( rootSymbol );
      preEvaluationStatements.add( nullCheck );
      root = identifier( rootSymbol );
    }

    IRExpression result = buildInvocation( rootType, pi, irProperty, rootSymbol, root );

    if( preEvaluationStatements.isEmpty() )
    {
      return result;
    }
    else
    {
      preEvaluationStatements.add( result );
      return new IRCompositeExpression( preEvaluationStatements );
    }
  }

  private IRExpression buildInvocation( IType rootType, IPropertyInfo pi, IRProperty irProperty, IRSymbol rootSymbol, IRExpression root )
  {
    IRExpression result;
    if( _expr().getRootExpression() != null && !(pi instanceof ITypeInfoPropertyInfo) )
    {
      // For the time being, for debugging purposes we want standard property info accesses to go through
      // a helper method
      if( rootType instanceof IPlaceholder && rootSymbol == null )
      {
        throw new IllegalArgumentException( "Cannot invoke a static property reflectively on a placeholder type" );
      }
      result = callStaticMethod( GosuRuntimeMethods.class, "getProperty", new Class[]{Object.class, IType.class, String.class},
                                 exprList( root, pushType( rootType ), pushPropertyName( pi ) ) );
    }
    else
    {
      //
      // rootType
      // .getTypeInfo()
      // .getProperty( propertyName ) or getProperty( type, propertyName )
      // .getAccessor()
      // .getValue( ctx )
      //

      IRExpression typeExpression;
      if( _expr().getRootExpression() != null && !(pi instanceof ITypeInfoPropertyInfo) )
      {
        // Must get root type from root expression when getting property dynamically
        // i.e., the property may not exist on the static root type
        typeExpression = callStaticMethod( TypeSystem.class, "getFromObject", new Class[]{Object.class},
                                           exprList( root ) );
      }
      else if( pi instanceof ITypeInfoPropertyInfo )
      {
        // We want to get the MetaType . . . but calling TypeSystem.getFromObject will return the literal version
        // of the MetaType, which doesn't have all the properties we want . . . sooooooo
        // there's this gigantic hack in here to directly invoke MetaType.get
        typeExpression = callStaticMethod( MetaType.class, "get", new Class[]{IType.class},
                                           exprList( root ) );
      }
      else
      {
        typeExpression = pushType( rootType );
      }

      IRExpression getTypeInfo = callMethod( IType.class, "getTypeInfo", new Class[0], typeExpression, exprList() );

      IRExpression getProperty;
      boolean relativeTypeInfo = pi != null && pi.getOwnersType().getTypeInfo() instanceof IRelativeTypeInfo;
      if( relativeTypeInfo )
      {
        getProperty = callMethod( IRelativeTypeInfo.class, "getProperty", new Class[]{IType.class, CharSequence.class},
                                  checkCast( IRelativeTypeInfo.class, getTypeInfo ),
                                  exprList( pushType( rootType ), pushPropertyName( pi ) ) );
      }
      else
      {
        getProperty = callMethod( ITypeInfo.class, "getProperty", new Class[]{CharSequence.class},
                                  getTypeInfo,
                                  exprList( pushPropertyName( pi ) ) );
      }

      IRExpression accessor = callMethod( IPropertyInfo.class, "getAccessor", new Class[0], getProperty, exprList() );
      result = callMethod( IPropertyAccessor.class, "getValue", new Class[]{Object.class},
                           accessor,
                           exprList( root ) );
    }

    if( pi instanceof IPropertyInfoDelegate )
    {
      IRType piClass = irProperty.getType();
      if( !piClass.isPrimitive() )
      {
        result = buildCast( piClass, result );
      }
      else
      {
        result = unboxValueToType( _expr().getType(), result );
      }
    }
    else
    {
      result = unboxValueToType( _expr().getType(), result );
    }
    return result;
  }

  private IRExpression pushPropertyName( IPropertyInfo pi )
  {
    if( pi != null )
    {
      if ( pi instanceof IUncacheableFeature ) {
        // In the case of snapshot types, the property's name is garbage, so we need to go back to
        // the name from the member access itself
        return pushConstant( _expr().getMemberName() );
      } else {
        return pushConstant( pi.getName() );
      }
    }
    else
    {
      return pushString( _expr().getMemberExpression() );
    }
  }

  private IRExpression pushRootExpression( IType rootType, IExpression rootExpr, IRProperty pi )
  {
    // Push the root expression value
    IRExpression root = ExpressionTransformer.compile( rootExpr, _cc() );
    //... and make sure it's boxed for the method call
    root = boxValue( rootType, root );

    if( pi != null && !pi.isStatic() )
    {
      IRType type = pi.getTargetRootIRType( );
      if( !type.isAssignableFrom( root.getType() ) && (!(rootExpr.getType() instanceof IGosuClass) || !((IGosuClass)rootExpr.getType()).isStructure()) )
      {
        root = buildCast( type, root );
      }
    }

    return root;
  }

  private boolean isStatic( IPropertyInfo pi )
  {
    return pi != null && pi.isStatic();
  }

  public static Object autoCreateEntityInstance(Object rootValue, String typeName, String propertyName) {
    if( rootValue instanceof IExpando ) {
      ((IExpando)rootValue).setDefaultFieldValue( propertyName );
      return ((IExpando)rootValue).getFieldValue( propertyName );
    }

    if( rootValue instanceof Bindings ) {
      Bindings value = new SimpleBindings();
      //noinspection unchecked
      ((Bindings)rootValue).put( propertyName, value );
      return value;
    }

    IType entityType = TypeSystem.getByFullName(typeName);
    IPropertyInfo property = entityType.getTypeInfo().getProperty(propertyName);
    IEntityAccess ea = CommonServices.getEntityAccess();

    List<IAnnotationInfo> annotationInfoList = property.getAnnotationsOfType( GosuTypes.AUTOCREATE() );
    Object value;
    boolean usingAutoCreateAnnotation = annotationInfoList != null && annotationInfoList.size() > 0;
    if( usingAutoCreateAnnotation )
    {
      IAnnotationInfo annotation = annotationInfoList.get( 0 );
      Autocreate o = (Autocreate)annotation.getInstance();
      Class<? extends Callable> block = o.value();
      if( block == null || block.isInterface() )
      {
        value = property.getFeatureType().getTypeInfo().getConstructor().getConstructor().newInstance();
      }
      else
      {
        try {
          value = block.newInstance().call();
        }
        catch( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    }
    else
    {
      value = ea.getEntityInstanceFrom( rootValue, property.getFeatureType() );
    }

    if( !property.isWritable() )
    {
      throw new EvaluationException( "Property, " + property.getName() + ", on class, " + TypeSystem.getFromObject( rootValue ).getRelativeName() + ", is null and immutable." );
    }

    value = CommonServices.getCoercionManager().convertValue( value, property.getFeatureType() );
    property.getAccessor().setValue( rootValue, value );
    return usingAutoCreateAnnotation ? property.getAccessor().getValue( rootValue ) : value;
  }

}
