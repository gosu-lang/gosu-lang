package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.lang.annotation.Order;
import gw.lang.parser.IExpression;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICanHaveAnnotationDefault;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 */
public class AnnotationConstructorGenerator {
  public static final Object STANDARD_CTOR_WITH_DEFAULT_PARAM_VALUES = new Object();
  private final IRelativeTypeInfo _owner;

  private AnnotationConstructorGenerator( IRelativeTypeInfo owner ) {
    _owner = owner;
  }

  static List<IConstructorInfo> generateAnnotationConstructors( IRelativeTypeInfo backingClass )
  {
    AnnotationConstructorGenerator gen = new AnnotationConstructorGenerator( backingClass );

    List<? extends IMethodInfo> declaredMethods = backingClass.getDeclaredMethods();
    IMethodInfo[] methods = declaredMethods.toArray( new IMethodInfo[declaredMethods.size()] );
    sortMethods( methods );

    ArrayList<IConstructorInfo> constructorInfoArrayList = new ArrayList<IConstructorInfo>();

    //## todo: Are the "legacy" ctors needed?
    gen.addLegacyConstructors( methods, constructorInfoArrayList );

    constructorInfoArrayList.add( gen.makeStandardAnnotationConstructor( methods ) );

    return constructorInfoArrayList;
  }

  private void addLegacyConstructors( IMethodInfo[] methods, ArrayList<IConstructorInfo> constructorInfoArrayList ) 
  {
    addCtor( constructorInfoArrayList, makeLegacyAnnotationConstructor(methods) );
    if( hasArrayArgs(methods) )
    {
      addCtor( constructorInfoArrayList, makeLegacyArrayAnnotationConstructor(methods) );
    }
    if( hasDefaultArgs( methods ) )
    {
      addCtor( constructorInfoArrayList, makeLegacyAllArgsAnnotationConstructor(methods) );
    }
    if( hasArrayArgs( methods ) && hasDefaultArgs( methods ) )
    {
      addCtor( constructorInfoArrayList, makeDefaultArrayAnnotationConstructor(methods) );
    }
  }

  private static void sortMethods( IMethodInfo[] methods ) {
    Arrays.sort( methods, new Comparator<IMethodInfo>() {
      @Override
      public int compare( IMethodInfo o1, IMethodInfo o2 ) {
        IAnnotationInfo order1 = o1.getAnnotation( TypeSystem.get( Order.class ) );
        if( order1 != null ) {
          IAnnotationInfo order2 = o2.getAnnotation( TypeSystem.get( Order.class ) );
          return ((Integer)order1.getFieldValue( "index" )).compareTo( (Integer)order2.getFieldValue( "index" ) );
        }
        return o1.getDisplayName().compareTo( o2.getDisplayName() );
      }
    } );
  }

  private void addCtor( ArrayList<IConstructorInfo> list, IConstructorInfo ci )
  {
    if( !list.contains( ci ) )
    {
      list.add( ci );
    }
  }

  private boolean hasArrayArgs( IMethodInfo[] methods )
  {
    for( IMethodInfo method : methods )
    {
      if( method.getReturnType().isArray() )
      {
        return true;
      }
    }
    return false;
  }

  private boolean hasDefaultArgs( IMethodInfo[] methods )
  {
    for( IMethodInfo method : methods )
    {
      if( ((ICanHaveAnnotationDefault)method).getAnnotationDefault() != null )
      {
        return true;
      }
    }
    return false;
  }

  private IConstructorInfo makeStandardAnnotationConstructor(final IMethodInfo[] methods)
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    ArrayList<ParameterInfoBuilder> paramsWDefaultValues = new ArrayList<ParameterInfoBuilder>();
    for( IMethodInfo method : methods )
    {
      ParameterInfoBuilder pib = new ParameterInfoBuilder().withName(method.getDisplayName()).withType(method.getReturnType());
      if (((ICanHaveAnnotationDefault)method).getAnnotationDefault() != null) {
        pib.withDefValue( makeDefaultValueExpression( method ) );
        paramsWDefaultValues.add(pib);
      } else {
        params.add(pib);
      }
    }
    params.addAll(paramsWDefaultValues);

    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .withUserData( STANDARD_CTOR_WITH_DEFAULT_PARAM_VALUES )
      .build( _owner );
  }

  private IConstructorInfo makeLegacyArrayAnnotationConstructor(final IMethodInfo[] methods)
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    for( IMethodInfo method : methods )
    {
      if( method.getReturnType().isArray() )
      {
        params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType().getComponentType() ) );
      }
      else
      {
        params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType() ) );
      }
    }
    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .build( _owner );
  }

  private IConstructorInfo makeLegacyAnnotationConstructor(final IMethodInfo[] methods)
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    for( IMethodInfo method : methods )
    {
      ICanHaveAnnotationDefault annoMethod = (ICanHaveAnnotationDefault)method;
      if( annoMethod.getAnnotationDefault() == null )
      {
        params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType() ) );
      }
    }
    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .build( _owner );
  }

  private IConstructorInfo makeLegacyAllArgsAnnotationConstructor(final IMethodInfo[] methods)
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    for( IMethodInfo method : methods )
    {
      ICanHaveAnnotationDefault annoMethod = (ICanHaveAnnotationDefault)method;
      if( annoMethod.getAnnotationDefault() != null ) {
        params.add( new ParameterInfoBuilder()
                      .withName( method.getDisplayName() )
                      .withType( method.getReturnType() )
                      .withDefValue( makeDefaultValueExpression( method ) ) );
      }
      else {
        params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType() ) );
      }
    }

    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .build( _owner );
  }

  private IExpression makeDefaultValueExpression( IMethodInfo method ) {
    if( method instanceof IGosuMethodInfo )
    {
      return ((IGosuMethodInfo)method).getDfs().getDefaultValueExpression();
    }
    else
    {
      String exprString = GosuClassProxyFactory.makeValueString( ((IJavaMethodInfo)method).getMethod().getDefaultValue(), method.getReturnType() );
      return CompileTimeExpressionParser.parse( exprString, ((IJavaType)_owner.getOwnersType()).getBackingClassInfo(), method.getReturnType() );
    }
  }

  private IConstructorInfo makeDefaultArrayAnnotationConstructor( final IMethodInfo[] methods )
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    for( IMethodInfo method : methods )
    {
      if( ((ICanHaveAnnotationDefault)method).getAnnotationDefault() == null )
      {
        if( method.getReturnType().isArray() )
        {
          params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType().getComponentType() ) );
        }
        else
        {
          params.add( new ParameterInfoBuilder().withName( method.getDisplayName() ).withType( method.getReturnType() ) );
        }
      }
    }
    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .build( _owner );
  }

}
