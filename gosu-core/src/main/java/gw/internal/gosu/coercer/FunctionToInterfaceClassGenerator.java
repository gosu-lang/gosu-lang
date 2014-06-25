/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.coercer;

import gw.internal.gosu.ir.builders.SimpleCompiler;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.GosuShop;
import gw.lang.function.IBlock;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.builder.IRClassBuilder;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRMethodBuilder;
import gw.lang.parser.ICoercer;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static gw.lang.ir.builder.IRBuilderMethods.*;

public class FunctionToInterfaceClassGenerator {

  private static Map<Class, Class> _classesToBlockCoercers = new HashMap<Class, Class>();

  public static synchronized void clearCachedClasses() {
    _classesToBlockCoercers.clear();
  }

  public static synchronized Class getBlockToInterfaceConversionClass( IType typeToCoerceTo ) {
    Class coercionClass = _classesToBlockCoercers.get( ((IHasJavaClass)typeToCoerceTo).getBackingClass() );
    if (coercionClass == null) {
      coercionClass = generateBlockToInterfaceConversionClass( typeToCoerceTo );
      _classesToBlockCoercers.put( ((IHasJavaClass)typeToCoerceTo).getBackingClass(), coercionClass );
    }
    return coercionClass;
  }
  
  private static Class generateBlockToInterfaceConversionClass( IType typeToCoerceTo ) {
    typeToCoerceTo = TypeLord.getPureGenericType( typeToCoerceTo );

    IRClassBuilder classBuilder = initializeClass( typeToCoerceTo );

    addFields( classBuilder );

    addConstructor( classBuilder );

    addInterfaceMethod( classBuilder, typeToCoerceTo );
    addToStringMethod( classBuilder );

    IRClass irClass = classBuilder.build();

    byte[] bytes = SimpleCompiler.INSTANCE.compile(irClass, false);
    return TypeSystem.getGosuClassLoader().defineClass( irClass.getName(), bytes );
  }

  private static IRClassBuilder initializeClass( IType typeToCoerceTo ) {
    IRClassBuilder classBuilder = new IRClassBuilder( "__proxy.generated.blocktointerface.ProxyFor" + typeToCoerceTo.getRelativeName(), Object.class );
    classBuilder.withInterface( typeToCoerceTo );
    return classBuilder;
  }

  private static void addFields( IRClassBuilder classBuilder ) {
    classBuilder.createField().withName( "_block" ).withType( IBlock.class )._private().build();
    classBuilder.createField().withName( "_coercer" ).withType( ICoercer.class )._private().build();
    classBuilder.createField().withName( "_returnType" ).withType( IType.class )._private().build();
  }

  private static void addConstructor( IRClassBuilder classBuilder ) {
    IRMethodBuilder method = classBuilder.createConstructor();
    method._public().parameters("block", IBlock.class, "coercer", ICoercer.class, "returnType", IType.class).body(
            set("_block", var("block")),
            set("_coercer", var("coercer")),
            set("_returnType", var("returnType")),
            _superInit(),
            _return()
    );
  }

  private static void addInterfaceMethod( IRClassBuilder classBuilder, IType typeToCoerceTo ) {

    IMethodInfo proxiedMethod = getSingleMethod( typeToCoerceTo );

    IRMethodBuilder method = classBuilder.createMethod();
    method.name( proxiedMethod.getDisplayName() )
            ._public()
            .copyParameters( proxiedMethod )
            .returns( GosuShop.getIRTypeResolver().getDescriptor( proxiedMethod.getReturnType() ) );

    List<IRExpressionBuilder> arrayContents = new ArrayList<IRExpressionBuilder>();
    IParameterInfo[] parameters = proxiedMethod.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      arrayContents.add(var("arg" + i));
    }

    if (proxiedMethod.getReturnType() == JavaTypes.pVOID()) {
      method.body(
        field("_block").call("invokeWithArgs", newArray(Object.class, arrayContents)),
        _return()
      );
    } else {
      method.body(
        assign("value", IRTypeConstants.OBJECT(), field("_block").call("invokeWithArgs", newArray(Object.class, arrayContents))),
        _if(field("_coercer").isNotNull()).then(
          assign("value", field("_coercer").call("coerceValue", field("_returnType"), var("value")))
        ),
        _return(var("value"))
      );
    }
  }

  private static void addToStringMethod( IRClassBuilder classBuilder ) {
    IRMethodBuilder method = classBuilder.createMethod();
    method.name( "toString" )
      ._public()
      .returns( IRTypeConstants.STRING() );

    method.body(
      assign( "value", IRTypeConstants.OBJECT(), field( "_block" ).call( "toString" ) ),
      _return( var( "value" ) )
    );
  }

  private static IMethodInfo getSingleMethod( IType interfaceType )
  {
    if( interfaceType.isInterface() )
    {
      List<IMethodInfo> list = new ArrayList<IMethodInfo>( interfaceType.getTypeInfo().getMethods() );

      //extract all object methods since they are guaranteed to be implemented
      for( Iterator<? extends IMethodInfo> it = list.iterator(); it.hasNext(); )
      {
        IMethodInfo methodInfo = it.next();
        IParameterInfo[] parameterInfos = methodInfo.getParameters();
        IType[] paramTypes = new IType[parameterInfos.length];
        for( int i = 0; i < parameterInfos.length; i++ )
        {
          paramTypes[i] = parameterInfos[i].getFeatureType();
        }
        if( JavaTypes.OBJECT().getTypeInfo().getMethod( methodInfo.getDisplayName(), paramTypes ) != null ||
            methodInfo.getOwnersType() instanceof IGosuEnhancement)
        {
          it.remove();
        }
        else if( methodInfo.getOwnersType().getName().contains( IGosuObject.class.getName() ) )
        {
          it.remove();
        }
        else if( !methodInfo.isAbstract() )
        {
          it.remove();
        }
      }

      if( list.size() == 1 )
      {
        return list.get( 0 );
      }
    }
    return null;
  }

}
