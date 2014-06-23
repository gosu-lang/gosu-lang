/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/*
 */
package gw.plugin.ij.debugger.evaluation;

import com.intellij.debugger.DebuggerBundle;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.JVMNameUtil;
import com.intellij.debugger.engine.SuspendContextImpl;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.EvaluateExceptionUtil;
import com.intellij.debugger.engine.evaluation.EvaluationContext;
import com.intellij.debugger.engine.evaluation.expression.ExpressionEvaluator;
import com.intellij.debugger.engine.evaluation.expression.Modifier;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.jdi.LocalVariableProxyImpl;
import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.openapi.project.Project;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.ClassType;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;
import gw.internal.gosu.parser.ContextSensitiveCodeRunner;
import gw.plugin.ij.util.GosuModuleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GosuExpressionEvaluatorImpl implements ExpressionEvaluator {
  private Value _value;
  private Project _project;
  private String _strFragName;
  private String _strText;
  private String _strClassContext;
  private String _strContextElementClass;
  private int _iContextLocation;

  public GosuExpressionEvaluatorImpl( Project project, String strFragName, String strText,
                                      String strClassContext, String strContextElementClass,
                                      int iContextLocation ) {
    _project = project;
    _strFragName = strFragName;
    _strText = strText;
    _strClassContext = GosuModuleUtil.getActualClassName( strClassContext, project );
    _strContextElementClass = GosuModuleUtil.getActualClassName( strContextElementClass, project );
    _iContextLocation = iContextLocation;
  }

  //call evaluate before
  public Value getValue() {
    return _value;
  }

  //call evaluate before
  public Modifier getModifier() {
    return null; //## todo: implement me
  }

  // EvaluationContextImpl should be at the same stackFrame as it was in the call to EvaluatorBuilderImpl.build
  public Value evaluate( final EvaluationContext context ) throws EvaluateException {
    if( !context.getDebugProcess().isAttached() ) {
      throw EvaluateExceptionUtil.createEvaluateException( DebuggerBundle.message( "error.vm.disconnected" ) );
    }
    try {
      if( context.getFrameProxy() == null ) {
        throw EvaluateExceptionUtil.NULL_STACK_FRAME;
      }

      final DebuggerSession session = DebuggerManagerEx.getInstanceEx( _project ).getContext().getDebuggerSession();
      final DebugProcessImpl process = session.getProcess();
      final VirtualMachineProxyImpl vm = process.getVirtualMachineProxy();
      List<ReferenceType> types = vm.classesByName( ContextSensitiveCodeRunner.class.getName() );
      ClassType classType = (ClassType)types.get( 0 );
      Value thisObject = findThisObjectFromCtx( context );
      _value = vm.getDebugProcess().invokeMethod( context, classType, classType.methodsByName( "runMeSomeCode" ).get( 0 ),
                                                  Arrays.asList(
                                                    thisObject,
                                                    context.getClassLoader(),
                                                    makeExternalsSymbolsForLocals( process, context ),
                                                    vm.mirrorOf( _strText ),
                                                    vm.mirrorOf( _strClassContext ),
                                                    vm.mirrorOf( _strContextElementClass ),
                                                    vm.mirrorOf( _iContextLocation ) ) );
      // Primitive boolean value needed for conditional breakpoint expression
      return unboxIfBoxed( context, _value );
    }
    catch( Throwable/*IncompatibleThreadStateException*/ e ) {
      if( e instanceof EvaluateException ) {
        throw ((EvaluateException)e);
      }
      else {
        throw EvaluateExceptionUtil.createEvaluateException( e );
      }
    }
  }

  private Value findThisObjectFromCtx( EvaluationContext context ) {
    List<LocalVariable> localVariables = null;
    StackFrame frame;
    try {
      frame = context.getFrameProxy().getStackFrame();
    }
    catch( EvaluateException e ) {
      throw new RuntimeException( e );
    }
    try {
      localVariables = frame.visibleVariables();
    }
    catch( AbsentInformationException e ) {
      return context.getThisObject();
    }
    for( LocalVariable localVar : localVariables ) {
      if( localVar.name().equals( "$that$" ) ) {
        // Use the enhanced object for the 'this' ref in enhancements
        return frame.getValue( localVar );
      }
    }
    return context.getThisObject();
  }

  private ArrayReference makeExternalsSymbolsForLocals( DebugProcessImpl debugProcess, EvaluationContext context ) throws EvaluateException, AbsentInformationException, InvalidTypeException, ClassNotLoadedException {
    List<Value> values = new ArrayList<>();
    StackFrameProxyImpl frame = (StackFrameProxyImpl)context.getFrameProxy();
    try {
      for( LocalVariableProxyImpl localVar : frame.visibleVariables() ) {
        values.add( debugProcess.getVirtualMachineProxy().mirrorOf( localVar.name() ) );
        Value value = frame.getValue( localVar );
        values.add( boxIfPrimitive( context, value ) );
      }
    }
    catch( EvaluateException e ) {
      // ignore
    }
    ArrayType objectArrayClass = (ArrayType)debugProcess.findClass(
      context,
      "java.lang.Object[]",
      context.getClassLoader() );
    if( objectArrayClass == null ) {
      throw new IllegalStateException();
    }

    ArrayReference argArray = debugProcess.newInstance( objectArrayClass, values.size() );
    ((SuspendContextImpl)context.getSuspendContext()).keep( argArray ); // to avoid ObjectCollectedException
    argArray.setValues( values );
    return argArray;
  }

  public Value boxIfPrimitive( EvaluationContext context, Value value ) throws EvaluateException {
    if( value == null || value instanceof ObjectReference ) {
      return value;
    }

    if( value instanceof BooleanValue ) {
      return convertToWrapper( context, (BooleanValue)value, "java.lang.Boolean" );
    }
    if( value instanceof ByteValue ) {
      return convertToWrapper( context, (ByteValue)value, "java.lang.Byte" );
    }
    if( value instanceof CharValue ) {
      return convertToWrapper( context, (CharValue)value, "java.lang.Character" );
    }
    if( value instanceof ShortValue ) {
      return convertToWrapper( context, (ShortValue)value, "java.lang.Short" );
    }
    if( value instanceof IntegerValue ) {
      return convertToWrapper( context, (IntegerValue)value, "java.lang.Integer" );
    }
    if( value instanceof LongValue ) {
      return convertToWrapper( context, (LongValue)value, "java.lang.Long" );
    }
    if( value instanceof FloatValue ) {
      return convertToWrapper( context, (FloatValue)value, "java.lang.Float" );
    }
    if( value instanceof DoubleValue ) {
      return convertToWrapper( context, (DoubleValue)value, "java.lang.Double" );
    }
    throw new EvaluateException( "Cannot perform boxing conversion for a value of type " + value.type().name() );
  }

  public Value unboxIfBoxed( EvaluationContext context, Value value ) throws EvaluateException {
    if( value == null || !(value instanceof ObjectReference) ) {
      return value;
    }

    ObjectReference valueRef = (ObjectReference)value;
    ReferenceType type = valueRef.referenceType();
    //System.out.println( "TYPE IS: " + type.name() );
    if( type.name().equals( Boolean.class.getName() ) ) {
      return unbox( context, valueRef, Boolean.class.getName(), "booleanValue" );
    }
    if( type.name().equals( Byte.class.getName() ) ) {
      return unbox( context, valueRef, Byte.class.getName(), "byteValue" );
    }
    if( type.name().equals( Character.class.getName() ) ) {
      return unbox( context, valueRef, Character.class.getName(), "charValue" );
    }
    if( type.name().equals( Short.class.getName() ) ) {
      return unbox( context, valueRef, Short.class.getName(), "shortValue" );
    }
    if( type.name().equals( Integer.class.getName() ) ) {
      return unbox( context, valueRef, Integer.class.getName(), "intValue" );
    }
    if( type.name().equals( Long.class.getName() ) ) {
      return unbox( context, valueRef, Long.class.getName(), "longValue" );
    }
    if( type.name().equals( Float.class.getName() ) ) {
      return unbox( context, valueRef, Float.class.getName(), "floatValue" );
    }
    if( type.name().equals( Double.class.getName() ) ) {
      return unbox( context, valueRef, Double.class.getName(), "doubleValue" );
    }
    return value;
  }

  private Value convertToWrapper( EvaluationContext context, PrimitiveValue value, String wrapperTypeName ) throws EvaluateException {
    DebugProcessImpl process = (DebugProcessImpl)context.getDebugProcess();
    ClassType wrapperClass = (ClassType)process.findClass( context, wrapperTypeName, context.getClassLoader() );
    String methodSignature = "(" + JVMNameUtil.getPrimitiveSignature( value.type().name() ) + ")L" + wrapperTypeName.replace( '.', '/' ) + ";";

    List<Method> methods = wrapperClass.methodsByName( "valueOf", methodSignature );
    if( methods.size() == 0 ) { // older JDK version
      methods = wrapperClass.methodsByName( "<init>", methodSignature );
    }
    if( methods.size() == 0 ) {
      throw new EvaluateException( "Cannot construct wrapper object for value of type " + value.type() + ": Unable to find either valueOf() or constructor method" );
    }

    final Method factoryMethod = methods.get( 0 );

    final ArrayList<Value> args = new ArrayList<>();
    args.add( value );

    return process.invokeMethod( context, wrapperClass, factoryMethod, args );
  }

  private Value unbox( EvaluationContext context, ObjectReference value, String wrapperTypeName, String strMethod ) throws EvaluateException {
    DebugProcessImpl process = (DebugProcessImpl)context.getDebugProcess();
    ClassType wrapperClass = (ClassType)process.findClass( context, wrapperTypeName, context.getClassLoader() );
    List<Method> methods = wrapperClass.methodsByName( strMethod );
    if( methods.size() == 0 ) {
      throw new EvaluateException( "Could not find method " + strMethod + "()" );
    }

    Method factoryMethod = methods.get( 0 );
    return process.invokeMethod( context, value, factoryMethod, Collections.emptyList() );
  }

}
