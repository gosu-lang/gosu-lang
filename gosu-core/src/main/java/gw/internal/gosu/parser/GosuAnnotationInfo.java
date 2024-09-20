/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.internal.gosu.parser.java.classinfo.JavaSourceDefaultValue;
import gw.lang.parser.AnnotationUseSiteTarget;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ICompilationState;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.SymbolType;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class GosuAnnotationInfo implements IAnnotationInfo
{
  private static final Object NOT_FOUND = new Object() { public String toString() {return "NOT FOUND";} };

  private volatile Object _instance;
  private IFeatureInfo  _container;
  private IGosuClassInternal  _owner;
  private String _newExpressionAsString;
  private INewExpression _expr;
  private IGosuAnnotation _rawAnnotation;
  private IType _type;

  public GosuAnnotationInfo( IGosuAnnotation rawAnnotation, IFeatureInfo container, IGosuClassInternal owner )
  {
    _rawAnnotation = rawAnnotation;
    _container = container;
    _instance = null;
    _owner = owner;
    _newExpressionAsString = rawAnnotation.getNewExpressionAsString();
    _type = rawAnnotation.getType();
    IExpression e = rawAnnotation.getExpression();
    _expr = e instanceof INewExpression ? (INewExpression)e : null;
  }

  public String getName()
  {
    return _type.getName();
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IGosuClassInternal getOwnersType()
  {
    return _owner;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return getName();
  }

  @Override
  public AnnotationUseSiteTarget getTarget()
  {
    return _rawAnnotation.getTarget();
  }

  public Object getInstance()
  {
    //## todo: Should _instance be a WeakReference?
    if( _instance == null )
    {
      TypeSystem.lock();

      ensureOwnerIsFullyParsedAndValid();
      try
      {
        if( _instance == null )
        {
          if( _owner != null && _owner.isProxy() )
          {
            // Get the annotation from the proxied java class
            _instance = getFromJavaType();
          }
          else if( JavaTypes.ANNOTATION().isAssignableFrom( getType() ) )
          {
            // Return a proxy implementing Annotation using the field values of this AnnotationInfo
            _instance = makeAnnotationInfoProxy();
          }
          else if( JavaTypes.IANNOTATION().isAssignableFrom( getType() ) )
          {
            // Execute the NewExpression for the IAnnotation impl class to create a direct instance of the quasi-annotation
            getOwnersType().isValid();
            _instance = eval( _newExpressionAsString, getType() );
          }
          else
          {
            throw new IllegalStateException( "Could not create annotation instance for type: " + getType().getName() );
          }
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _instance;
  }

  private void ensureOwnerIsFullyParsedAndValid() {
    if( _owner != null )
    {
      ICompilationState state = _owner.getCompilationState();
      if( state.isCompilingHeader() || state.isCompilingDeclarations() )
      {
        throw new IllegalStateException( "You cannot request Annotation values during the declaration parsing phase." );
      }
      _owner.compileDeclarationsIfNeeded();
    }
  }

  private Annotation makeAnnotationInfoProxy()
  {
    IType annotationType = getType();

    //## todo: don't load the class if the annotationType doesn't correspond with a Class object i.e., don't load project classes during parsing/compilation of said project!
    Class annotationClass = ((IHasJavaClass)annotationType).getBackingClass();

    return (Annotation)Proxy.newProxyInstance( annotationClass.getClassLoader(), new Class[] {annotationClass},
                                               new AnnotationInfoInvocationHandler( this ) );
  }

  private Object getFromJavaType()
  {
    IJavaType javaType = _owner.getJavaType();
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo)javaType.getTypeInfo();
    if( _container instanceof IPropertyInfo )
    {
      return typeInfo.getProperty( javaType, _container.getDisplayName() ).getAnnotation( getType() ).getInstance();
    }
    return typeInfo.getMethod( javaType, _container.getDisplayName(),
                               BaseFeatureInfo.getParamTypes( ((IMethodInfo)_container).getParameters() ) ).getAnnotation( getType() ).getInstance();
  }

  @Override
  public Object getFieldValue( String field )
  {
    Object value = getValueFromCallSite( field );
    if( value == NOT_FOUND )
    {
      value = getValueFromDeclaredDefaultValueAtDeclSite( _type, field );
      if( value == NOT_FOUND )
      {
        throw new RuntimeException( "Annotation field, " + field + ", not found in " + _type.getName() );
      }
      value = CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, _container );
    }
    return value;
  }

  private Object getValueFromDeclaredDefaultValueAtDeclSite( IType type, String field )
  {
    if( type instanceof IJavaType )
    {
      IJavaClassInfo classInfo = ((IJavaType)type).getBackingClassInfo();
      try
      {
        IJavaClassMethod m = classInfo.getDeclaredMethod( field );
        Object value = m.getDefaultValue();
        if( value instanceof JavaSourceDefaultValue )
        {
          value = ((JavaSourceDefaultValue) value).evaluate();
        }
        return value;
      }
      catch( NoSuchMethodException e )
      {
        return NOT_FOUND;
      }
    }
    else if( type instanceof IGosuClass )
    {
      IGosuClass gsClass = (IGosuClass)type;
      IGosuMethodInfo method = (IGosuMethodInfo)gsClass.getTypeInfo().getMethod( gsClass, field );
      if( method != null )
      {
        IExpression annotationDefault = method.getDfs().getDefaultValueExpression();
        if( annotationDefault != null )
        {
          return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( annotationDefault.evaluate(), gsClass.getTypeInfo() );
        }
      }

      //## todo: delete this chunk of code after we kill old Gosu annotations (IAnnotation ones)
      List<? extends IConstructorInfo> ctors = gsClass.getTypeInfo().getConstructors( gsClass );
      for( IConstructorInfo ctor: ctors )
      {
        if( ctor instanceof IGosuConstructorInfo )
        {
          String[] paramNames = ((IGosuConstructorInfo)ctor).getParameterNames();
          for( int i = 0; i < paramNames.length; i++ )
          {
            String paramName = paramNames[i];
            if( paramName != null && paramName.equals( field ) )
            {
              return ((IGosuConstructorInfo)ctor).getDefaultValueExpressions()[i].evaluate();
            }
          }
        }
      }

    }
    return NOT_FOUND;
  }

  private Object getValueFromCallSite( String field )
  {
    List<IIdentifierExpression> ids = new ArrayList<IIdentifierExpression>();
    getExpr().getContainedParsedElementsByType( IIdentifierExpression.class, ids );
    boolean bTypedSymFound = false;
    if( !ids.isEmpty() )
    {
      for( IIdentifierExpression id: ids )
      {
        ISymbol sym = id.getSymbol();
        bTypedSymFound = bTypedSymFound || sym instanceof TypedSymbol;
        if( sym instanceof TypedSymbol &&
            ((TypedSymbol)sym).getSymbolType() == SymbolType.NAMED_PARAMETER &&
            sym.getName().equals( field ) )
        {
          IParseTree nextSibling = id.getLocation().getNextSibling();
          if( nextSibling != null )
          {
            Expression expr = (Expression)nextSibling.getParsedElement();
            return evaluate( expr );
          }
          return NOT_FOUND;
        }
      }
    }

    if( !bTypedSymFound )
    {
      // Assume old-style ctor invocation where params are not named, so
      // we must also assume the ctor-defined param ordering and get the
      // field names and ordering from the params.
      IExpression[] args = getExpr().getArgs();
      if( args != null && args.length > 0 )
      {
        IParameterInfo[] parameters = getExpr().getConstructor().getParameters();
        for( int i = 0; i < parameters.length; i++ )
        {
          IParameterInfo param = parameters[i];
          if( field.equalsIgnoreCase( param.getDisplayName() ) )
          {
            return evaluate( args[i] );
          }
        }
        if( getType() instanceof IJavaType ) {
          IJavaClassInfo classInfo = ((IJavaType)getType()).getBackingClassInfo();
          if( classInfo instanceof ClassJavaClassInfo ) {
            Field[] fields = classInfo.getBackingClass().getDeclaredFields();
            for( int i = 0; i < fields.length; i++ )
            {
              Field f = fields[i];
              if( field.equalsIgnoreCase( f.getName() ) )
              {
                return evaluate( args[i] );
              }
            }
          }
        }
      }
    }

    if( "value".equalsIgnoreCase( field ) )
    {
      IExpression[] args = getExpr().getArgs();
      if( args != null && args.length == 1 )
      {
        return evaluate( args[0] );
      }
    }
    return NOT_FOUND;
  }

  private Object evaluate( IExpression expr )
  {
    return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( expr.evaluate(), _container );
  }

  private INewExpression getExpr()
  {
    if( _expr == null )
    {
      _expr = parseNewExpression();
    }
    return _expr;
  }

  private INewExpression parseNewExpression()
  {
    IGosuClassInternal ownersType = (IGosuClassInternal)_container.getOwnersType();
    ITypeUsesMap usesMap;
    IType outerMostEnclosingType = TypeLord.getOuterMostEnclosingClass( ownersType );
    if( outerMostEnclosingType instanceof IGosuClass )
    {
      usesMap = ((IGosuClass)outerMostEnclosingType).getTypeUsesMap();
    }
    else
    {
      usesMap = ownersType.getTypeUsesMap();
    }
    if( usesMap != null )
    {
      usesMap = usesMap.copy();
      usesMap.addToDefaultTypeUses( "gw.lang." );
    }
    else
    {
      usesMap = new TypeUsesMap();
    }
    addEnclosingPackages( usesMap, ownersType );
    ParserOptions options = new ParserOptions().withTypeUsesMap( usesMap );
    IGosuParser parser = GosuParserFactory.createParser( _newExpressionAsString );
    options.setParserOptions( parser );

    StandardSymbolTable symTable = new StandardSymbolTable( true );
    TypeSystem.pushSymTableCtx( symTable );
    try
    {
      parser.setSymbolTable( TypeSystem.getCompiledGosuClassSymbolTable() ); // Set up the symbol table
      return (INewExpression)parser.parseExp( new TypelessScriptPartId( toString(), ownersType ), options.getExpectedType(), options.getFileContext(), false );
    }
    catch( ParseResultsException e )
    {
      if( e.hasOnlyParseWarnings() )
      {
        return (INewExpression)e.getParsedElement();
      }
      else
      {
        throw new RuntimeException( e );
      }
    }
    finally
    {
      TypeSystem.popSymTableCtx();
    }
  }

  private Object eval( String strExprSource, IType type )
  {
    IGosuClassInternal ownersType = (IGosuClassInternal)_container.getOwnersType();
    ITypeUsesMap usesMap;
    IType outerMostEnclosingType = TypeLord.getOuterMostEnclosingClass( ownersType );
    if( outerMostEnclosingType instanceof IGosuClass )
    {
      usesMap = ((IGosuClass)outerMostEnclosingType).getTypeUsesMap();
    }
    else
    {
      usesMap = ownersType.getTypeUsesMap();
    }
    if( usesMap != null )
    {
      usesMap = usesMap.copy();
      usesMap.addToDefaultTypeUses( "gw.lang." );
    }
    else
    {
      usesMap = new TypeUsesMap();
    }
    addEnclosingPackages( usesMap, ownersType );
    IType enclType = TypeLord.getPureGenericType( outerMostEnclosingType instanceof IGosuClass ? outerMostEnclosingType : ownersType );
    ParserOptions options = new ParserOptions()
    .withTypeUsesMap( usesMap )
    .withEnclosingType( enclType.getName() )
    .withExpectedType(type)
    .asThrowawayProgram()
    .asAnonymous();

    StandardSymbolTable symTable = new StandardSymbolTable( true );
    TypeSystem.pushSymTableCtx( symTable );
    try
    {
      IParseResult res = GosuParserFactory.createProgramParser().parseExpressionOnly( strExprSource, symTable, options );
      return res.evaluate();
    }
    catch( ParseResultsException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      TypeSystem.popSymTableCtx();
    }
  }

  private static void addEnclosingPackages( ITypeUsesMap map, IType type )
  {
    type = TypeLord.getPureGenericType( type );
    type = TypeLord.getOuterMostEnclosingClass( type );
    map.addToDefaultTypeUses( GosuClassUtil.getPackage( type.getName() ) + "." );
  }

  public IType getType()
  {
    if (TypeSystem.isDeleted(_type)) {
      return TypeSystem.getErrorType(_type.getName());
    } else {
      return TypeLord.getPureGenericType( _type );
    }
  }

  public String toString()
  {
    return getName();
  }

  public String getNewExpressionAsString() {
    return _newExpressionAsString;
  }

  public IGosuAnnotation getRawAnnotation()
  {
    return _rawAnnotation;
  }

  public IType getRepeatableContainer() {

    IType repeatable = JavaTypes.REPEATABLE();
    if( getType().getTypeInfo().hasAnnotation( repeatable ) )
    {
      IAnnotationInfo anno = getType().getTypeInfo().getAnnotation( repeatable );
      Object type = anno.getFieldValue( "value" );
      if( type instanceof String )
      {
        type = TypeSystem.getByFullNameIfValid( (String)type );
      }
      return (IType)type;
    }
    return null;
  }
}
