/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static gw.internal.ext.org.objectweb.asm.Opcodes.*;

public class IRMethodCallExpressionCompiler extends AbstractBytecodeCompiler {

  public static final String STRUCTURAL_PROXY = "_structuralproxy_";

  public static void compile( IRMethodCallExpression expression, IRBytecodeContext context ) {
    IRType type = null;
    if (expression.getRoot() != null) {
      IRBytecodeCompiler.compileIRExpression( expression.getRoot(), context );
      type = maybeProxyStructuralCallRoot( expression, context );
    }
    for (IRExpression arg : expression.getArgs()) {
      IRBytecodeCompiler.compileIRExpression( arg, context );
    }

    int opCode;
    if( type != null ) {
      // the type is structural, the root expr is now wrapped in a proxy of the structural interface
      assert type.isStructural();
      opCode = Opcodes.INVOKEINTERFACE;
    }
    else if (expression.getRoot() == null) {
      // If the root is null, use INVOKESTATIC, regardless of the types or the "special" flag
      type = expression.getOwnersType();
      opCode = Opcodes.INVOKESTATIC;
    } else if (expression.isSpecial()) {
      type = expression.getOwnersType();
      opCode = Opcodes.INVOKESPECIAL;
    } else if (isObjectMethod(expression)) {
      // Methods on Object always need to be invoked with INVOKEVIRTUAL and Object as the root.  Why?
      // Because it's legal to invoke an Object method like toString() on a root expression that's an interface type,
      // but using INVOKEINTERFACE to invoke an Object method not defined directly on the interface will result in an
      // IncompatibleClassChangeException at runtime in the IBM VM, and using INVOKEVIRTUAL with an interface type
      // as the root will result in such an exception at runtime in both the Sun and IBM VMs
      type = IRTypeConstants.OBJECT();
      opCode = Opcodes.INVOKEVIRTUAL;
    }
    else {
      type = expression.getRoot().getType();
      if (type.isInterface()) {
        opCode = Opcodes.INVOKEINTERFACE;
      } else {
        opCode = Opcodes.INVOKEVIRTUAL;
      }
    }

    StringBuilder descriptor = new StringBuilder();
    descriptor.append("(");
    for (IRType param : expression.getParameterTypes()) {
      descriptor.append(param.getDescriptor());
    }
    descriptor.append(")");
    descriptor.append(expression.getReturnType().getDescriptor());

    context.getMv().visitMethodInsn( opCode,
                                     type.isArray() ? JavaClassIRType.get( Object.class ).getSlashName() : type.getSlashName(),
                                     expression.getName(),
                                     descriptor.toString() );
  }

  private static IRType maybeProxyStructuralCallRoot( IRMethodCallExpression expression, IRBytecodeContext context ) {
    IRType ownersType = expression.getOwnersType();
    if( ownersType.isStructural() ) {
      // Generate the following: (note rootObject is top on the stack from the caller of this method)
      //   if( rootObject instanceof <structure-iface> ) {
      //     CheckCast and return existing instance
      //   }
      //   else {
      //     Return proxy instance
      //   }

      String structureName = ownersType.getName();

      MethodVisitor mv = context.getMv();
      mv.visitInsn( Opcodes.DUP ); // dup the root value
      mv.visitTypeInsn( INSTANCEOF, ownersType.getSlashName() );
      Label labelProxy = new Label();
      mv.visitJumpInsn( IFEQ, labelProxy );
      mv.visitTypeInsn( CHECKCAST, ownersType.getSlashName() );
      Label labelEnd = new Label();
      mv.visitJumpInsn( GOTO, labelEnd );
      mv.visitLabel( labelProxy );
      mv.visitLdcInsn( structureName );
      mv.visitMethodInsn( Opcodes.INVOKESTATIC,
                          IRMethodCallExpressionCompiler.class.getName().replace( '.', '/' ),
                          "constructProxy",
                          "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;" );
      mv.visitLabel( labelEnd );
      return ownersType;
    }
    return null;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static Object constructProxy( Object root, String iface ) {
    // return findCachedProxy( root, iface ); // this is only beneficial when structural invocation happens in a loop, otherwise too costly
    return createNewProxy( root, iface );
  }

//  private static Map<String, Map<Object, Object>> PROXY_INSTANCE_CACHE = new ConcurrentHashMap<String, Map<Object, Object>>();
//  private static Object findCachedProxy( Object root, String iface ) {
//    Map<Object, Object> proxyInstanceByInstance = PROXY_INSTANCE_CACHE.get( iface );
//    if( proxyInstanceByInstance == null ) {
//      PROXY_INSTANCE_CACHE.put( iface, proxyInstanceByInstance = Collections.synchronizedMap( new WeakHashMap<Object, Object>() ) );
//    }
//    Object proxyInstance = proxyInstanceByInstance.get( root );
//    if( proxyInstance == null ) {
//      proxyInstanceByInstance.put( root, proxyInstance = createNewProxy( root, iface ) );
//    }
//    return proxyInstance;
//  }

  private static Map<String, Map<Class, Constructor>> PROXY_CACHE = new ConcurrentHashMap<String, Map<Class, Constructor>>();
  private static Object createNewProxy( Object root, String iface ) {
    Map<Class, Constructor> proxyByClass = PROXY_CACHE.get( iface );
    if( proxyByClass == null ) {
      PROXY_CACHE.put( iface, proxyByClass = new ConcurrentHashMap<Class, Constructor>() );
    }
    boolean bStaticImpl;
    Class rootClass;
    if( root instanceof IGosuClass ) {
      bStaticImpl = true;
      rootClass = ((IGosuClass) root).getBackingClass();
    }
    else if( root instanceof Class ) {
      bStaticImpl = true;
      rootClass = (Class)root;
      root = TypeSystem.get( rootClass );
    }
    else {
      bStaticImpl = false;
      rootClass = root.getClass();
    }
    Constructor proxyClassCtor = proxyByClass.get( rootClass );
    if( proxyClassCtor == null ) {
      Class proxyClass = createProxy( iface, rootClass, bStaticImpl );
      proxyByClass.put( rootClass, proxyClassCtor = proxyClass.getConstructors()[0] );
    }
    try {
      return proxyClassCtor.newInstance( root );
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static Class createProxy( String iface, Class rootClass, boolean bStaticImpl ) {
    String relativeProxyName = rootClass.getSimpleName() + STRUCTURAL_PROXY + iface.replace( '.', '_' );
    return StructuralTypeProxyGenerator.makeProxy( iface, rootClass, relativeProxyName, bStaticImpl );
  }

  /**
   * Determines whether or not the method in question is a method directly on Object, which thus
   * needs to be invoked using INVOKEVIRTUAL regardless of the expression's root type.
   */
  private static boolean isObjectMethod(IRMethodCallExpression expression) {
    return expression.getOwnersType().getName().equals("java.lang.Object");
  }
}
