/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.internal.gosu.parser.java.classinfo.JavaSourceEnumConstant;
import gw.internal.gosu.parser.java.classinfo.JavaSourceField;
import gw.lang.Deprecated;
import gw.lang.GosuShop;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IVarNode;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IExpression;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaFieldPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 */
public class JavaFieldPropertyInfo extends JavaBaseFeatureInfo implements IJavaFieldPropertyInfo
{
  private IJavaClassField _field;
  private boolean _isStatic;
  private IType _type;
  private IPropertyAccessor _accessor;
  private String _strName;
  private IDocRef<IVarNode> _docs = new IDocRef<IVarNode>() {
    @Override
    public IVarNode get() {
      if (getContainer() instanceof JavaTypeInfo) {
        IClassDocNode classDocs = ((JavaTypeInfo) getContainer()).getDocNode().get();
        @SuppressWarnings({"UnnecessaryLocalVariable"})
        IVarNode varDoc = classDocs == null ? null : classDocs.getVar( _field.getName() );
        return varDoc;
      } else {
        return null;
      }
    }
  };

  JavaFieldPropertyInfo(IFeatureInfo container, IType type, IJavaClassField field, boolean isStatic, boolean simplePropertyProcessing)
  {
    super( container );
    if (type == null) {
      throw new IllegalArgumentException("Feature type cannot be null");
    }
    type = TypeLord.replaceRawGenericTypesWithDefaultParameterizedTypes( type );
    _type = type;
    _field = field;
    if (_field instanceof FieldJavaClassField) {
      ((FieldJavaClassField)_field).setAccessible( true );
    }
    _isStatic = isStatic;
    //_strName = NewIntrospector.capitalizeFirstChar( _field.getName() ).replace( '$', '_' );
    _strName = simplePropertyProcessing ? _field.getName() : _field.getName().replace( '$', '_' );
    if( _isStatic )
    {
      _accessor = new StaticAccessor();
    }
    else
    {
      _accessor = new NonStaticAccessor();
    }
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  void changeNameForNonStaticCollision()
  {
    _strName += "_";
  }

  @Override
  public IType getFeatureType()
  {
    return _type;
  }

  @Override
  public boolean isStatic()
  {
    return _isStatic;
  }

  @Override
  public boolean isReadable()
  {
    return true;
  }

  @Override
  public boolean isWritable(IType whosAskin) {
    IRelativeTypeInfo.Accessibility accessibilityForType = ((IRelativeTypeInfo) getContainer()).getAccessibilityForType(whosAskin);
    return !Modifier.isFinal( _field.getModifiers() ) && FeatureManager.isFeatureAccessible(this, accessibilityForType);
  }

  @Override
  public boolean isWritable()
  {
    return isWritable(null);
  }

  @Override
  public boolean isDeprecated()
  {
    return super.isDeprecated() || getField().isAnnotationPresent( gw.lang.Deprecated.class );
  }

  @Override
  public String getDeprecatedReason()
  {
    String deprecated = super.getDeprecatedReason();
    if( isDeprecated() && deprecated == null )
    {
      return (String) getField().getAnnotation( Deprecated.class ).getFieldValue("value");
    }
    return deprecated;
  }

  @Override
  public boolean isPrivate()
  {
    return Modifier.isPrivate( _field.getModifiers() );
  }

  @Override
  public boolean isInternal()
  {
    return !isPrivate() && !isProtected() && !isPublic();
  }

  @Override
  public boolean isProtected()
  {
    return Modifier.isProtected( _field.getModifiers() );
  }

  @Override
  public boolean isPublic()
  {
    return Modifier.isPublic( _field.getModifiers() );
  }

  @Override
  public String getDescription()
  {
    return getVarDocs().get() == null ? null : getVarDocs().get().getDescription();
  }

  private IDocRef<IVarNode> getVarDocs() {
    return _docs;
  }

  @Override
  public IPropertyAccessor getAccessor()
  {
    return _accessor;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    List<IAnnotationInfo> annotations = super.getDeclaredAnnotations();
    if( getVarDocs().get() != null && getVarDocs().get().isDeprecated() )
    {
      annotations.add( GosuShop.getAnnotationInfoFactory().createJavaAnnotation(makeDeprecated( getVarDocs().get().getDeprecated() ), this ) );
    }
    return annotations;
  }

  @Override
  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  @Override
  public IJavaClassField getField()
  {
    return _field;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  protected IJavaAnnotatedElement getAnnotatedElement()
  {
    return _field;
  }

  @Override
  protected boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return true;
  }

  @Override
  protected boolean isHiddenViaFeatureDescriptor() {
    return false;
  }

  @Override
  protected boolean isDefaultEnumFeature()
  {
    return false;
  }

  @Override
  public String getReturnDescription() {
    return null;
  }

  @Override
  public boolean isCompileTimeConstantValue() {
    return Modifier.isStatic( getField().getModifiers() ) &&
           Modifier.isFinal( getField().getModifiers() ) && isCompileTimeConstant();
  }

  private boolean isCompileTimeConstant() {
    IJavaClassField field = getField();
    if( field instanceof JavaSourceEnumConstant ) {
      return true;
    }
    else if( field instanceof JavaSourceField ) {
      String rhs = ((JavaSourceField)field).getRhs();
      if( rhs == null ) {
        return false;
      }
      IExpression pr = CompileTimeExpressionParser.parse( rhs, field.getEnclosingClass(), getFeatureType() );
      return pr.isCompileTimeConstant();
    }
    else if( field instanceof AsmFieldJavaClassField &&
             ((AsmFieldJavaClassField)field).getStaticValue() != null ) {
      return true;
    }
    else if( field instanceof FieldJavaClassField || field instanceof AsmFieldJavaClassField ) {
      return field.isEnumConstant() ||
             field.getType().isPrimitive() ||
             field.getType().getName().equals( "java.lang.String" ) ||
             field.getType().getName().equals( "java.lang.Class" );
    }
    return false;
  }

  @Override
  public Object doCompileTimeEvaluation() {
    //((IJavaType)getOwnersType()).getBackingClassInfo().getDeclaredFields()
    IJavaClassField field = getField();
    if( field instanceof JavaSourceEnumConstant ) {
      return field.getName();
    }
    else if( field instanceof JavaSourceField ) {
      String rhs = ((JavaSourceField)field).getRhs();
      IExpression pr = CompileTimeExpressionParser.parse( rhs, ((IJavaType)getOwnersType()).getBackingClassInfo(), getFeatureType() );
      return pr.evaluate();
    }
    else if( field instanceof FieldJavaClassField ) {
      try {
        Object value = ((FieldJavaClassField)field).get( null );
        return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, getOwnersType().getTypeInfo() );
      }
      catch( IllegalAccessException e ) {
        throw new RuntimeException( e );
      }
    }
    else if( field instanceof AsmFieldJavaClassField ) {
      if( field.isEnumConstant() ) {
        return field.getName();
      }
      Object value = ((AsmFieldJavaClassField)field).getStaticValue();
      return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, getOwnersType().getTypeInfo() );
    }
    else {
      throw new IllegalStateException( "Unexpected field type: " + field );
    }
  }

  private class StaticAccessor implements IPropertyAccessor
  {
    @Override
    public Object getValue( Object ctx )
    {
      try
      {
        return ((FieldJavaClassField)_field).get( null );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }

    @Override
    public void setValue( Object ctx, Object value )
    {
      if( !isWritable( getOwnersType()) )
      {
        throw new EvaluationException( "Property, " + getName() + ", is not writable!" );
      }

      try
      {
        value = CommonServices.getCoercionManager().convertValue( value, getFeatureType() );
        ((FieldJavaClassField)_field).set( null, value );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  }

  private class NonStaticAccessor implements IPropertyAccessor
  {
    @Override
    public Object getValue( Object ctx )
    {
      try
      {
        return ((FieldJavaClassField)_field).get( ctx );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }

    @Override
    public void setValue( Object ctx, Object value )
    {
      if( !isWritable( getOwnersType()) )
      {
        throw new EvaluationException( "Property, " + getName() + ", is not writable!" );
      }

      try
      {
        value = CommonServices.getCoercionManager().convertValue( value, getFeatureType() );
        ((FieldJavaClassField)_field).set( ctx, value );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  }
}
