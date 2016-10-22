package editor.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.ClassType;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import gw.lang.GosuShop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DebuggerExpression
{
  private Value _value;
  private String _strText;
  private String _strClassContext;
  private String _strContextElementClass;
  private int _iContextLocation;

  public DebuggerExpression( String expr,
                             String strClassContext, String strContextElementClass,
                             int iContextLocation )
  {
    _strText = expr;
    _strClassContext = strClassContext;
    _strContextElementClass = strContextElementClass;
    _iContextLocation = iContextLocation;
  }

  //call evaluate before
  public Value getValue()
  {
    return _value;
  }

  // EvaluationContextImpl should be at the same stackFrame as it was in the call to EvaluatorBuilderImpl.build
  public Value evaluate( Debugger debugger )
  {
    Location suspendedLoc = debugger.getSuspendedLocation();
    VirtualMachine vm = suspendedLoc.virtualMachine();
    List<ReferenceType> types = vm.classesByName( "gw.internal.gosu.parser.ContextSensitiveCodeRunner" );
    ClassType classType = (ClassType)types.get( 0 );
    Value thisObject = findThisObjectFromCtx( debugger.getSuspendedThread() );
    try
    {
      _value = classType.invokeMethod(
        debugger.getSuspendedThread(), classType.methodsByName( "runMeSomeCode" ).get( 0 ),
        Arrays.asList(
          thisObject,
          suspendedLoc.declaringType().classLoader(),
          makeExternalsSymbolsForLocals( debugger ),
          vm.mirrorOf( _strText ),
          vm.mirrorOf( _strClassContext ),
          vm.mirrorOf( _strContextElementClass ),
          vm.mirrorOf( _iContextLocation ) ), 0 );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    // Primitive boolean value needed for conditional breakpoint expression
    return unboxIfBoxed( suspendedLoc, debugger.getSuspendedThread(), _value );
  }

  private Value findThisObjectFromCtx( ThreadReference suspendedThread )
  {
    List<LocalVariable> localVariables;
    StackFrame frame;
    try
    {
      frame = suspendedThread.frame( 0 );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    try
    {
      localVariables = frame.visibleVariables();
    }
    catch( AbsentInformationException e )
    {
      return frame.thisObject();
    }
    for( LocalVariable localVar : localVariables )
    {
      if( localVar.name().equals( "$that$" ) )
      {
        // Use the enhanced object for the 'this' ref in enhancements
        return frame.getValue( localVar );
      }
    }
    return frame.thisObject();
  }

  private ArrayReference makeExternalsSymbolsForLocals( Debugger debugger )
  {
    Location suspendedLoc = debugger.getSuspendedLocation();
    ThreadReference suspendedThread = debugger.getSuspendedThread();

    List<Value> values = new ArrayList<>();
    try
    {
      StackFrame frame = suspendedThread.frame( 0 );
      List<LocalVariable> localVariables = frame.visibleVariables();
      Map<LocalVariable, Value> localValues = frame.getValues( localVariables );
      for( LocalVariable localVar : localValues.keySet() )
      {
        values.add( suspendedLoc.virtualMachine().mirrorOf( localVar.name() ) );
        Value value = localValues.get( localVar );
        values.add( boxIfPrimitive( suspendedLoc, suspendedThread, value ) );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    ArrayType objectArrayClass = (ArrayType)suspendedLoc.virtualMachine().classesByName( "java.lang.Object[]" ).get( 0 );
    if( objectArrayClass == null )
    {
      throw new IllegalStateException();
    }

    ArrayReference argArray = objectArrayClass.newInstance( values.size() );
    debugger.retain( argArray ); // prevents ObjectCollectedException
    try
    {
      argArray.setValues( values );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return argArray;
  }

  public Value boxIfPrimitive( Location suspendedLoc, ThreadReference suspendedThread, Value value )
  {
    if( value == null || value instanceof ObjectReference )
    {
      return value;
    }

    if( value instanceof BooleanValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (BooleanValue)value, "java.lang.Boolean" );
    }
    if( value instanceof ByteValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (ByteValue)value, "java.lang.Byte" );
    }
    if( value instanceof CharValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (CharValue)value, "java.lang.Character" );
    }
    if( value instanceof ShortValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (ShortValue)value, "java.lang.Short" );
    }
    if( value instanceof IntegerValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (IntegerValue)value, "java.lang.Integer" );
    }
    if( value instanceof LongValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (LongValue)value, "java.lang.Long" );
    }
    if( value instanceof FloatValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (FloatValue)value, "java.lang.Float" );
    }
    if( value instanceof DoubleValue )
    {
      return convertToWrapper( suspendedLoc, suspendedThread, (DoubleValue)value, "java.lang.Double" );
    }
    throw new RuntimeException( "Cannot perform boxing conversion for a value of type " + value.type().name() );
  }

  public Value unboxIfBoxed( Location suspendedLoc, ThreadReference suspendedThread, Value value )
  {
    if( value == null || !(value instanceof ObjectReference) )
    {
      return value;
    }

    ObjectReference valueRef = (ObjectReference)value;
    ReferenceType type = valueRef.referenceType();
    //System.out.println( "TYPE IS: " + type.name() );
    if( type.name().equals( Boolean.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Boolean.class.getName(), "booleanValue" );
    }
    if( type.name().equals( Byte.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Byte.class.getName(), "byteValue" );
    }
    if( type.name().equals( Character.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Character.class.getName(), "charValue" );
    }
    if( type.name().equals( Short.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Short.class.getName(), "shortValue" );
    }
    if( type.name().equals( Integer.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Integer.class.getName(), "intValue" );
    }
    if( type.name().equals( Long.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Long.class.getName(), "longValue" );
    }
    if( type.name().equals( Float.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Float.class.getName(), "floatValue" );
    }
    if( type.name().equals( Double.class.getName() ) )
    {
      return unbox( suspendedLoc, suspendedThread, valueRef, Double.class.getName(), "doubleValue" );
    }
    return value;
  }

  private Value convertToWrapper( Location suspendedLoc, ThreadReference suspendedThread, PrimitiveValue value, String wrapperTypeName )
  {
    ClassType wrapperClass = (ClassType)suspendedLoc.virtualMachine().classesByName( wrapperTypeName ).get( 0 );
    String methodSignature = "(" + GosuShop.toSignature( value.type().name() ) + ")L" + wrapperTypeName.replace( '.', '/' ) + ";";

    List<Method> methods = wrapperClass.methodsByName( "valueOf", methodSignature );
    if( methods.size() == 0 )
    { // older JDK version
      methods = wrapperClass.methodsByName( "<init>", methodSignature );
    }
    if( methods.size() == 0 )
    {
      throw new RuntimeException( "Cannot construct wrapper object for value of type " + value.type() + ": Unable to find either valueOf() or constructor method" );
    }

    Method factoryMethod = methods.get( 0 );

    ArrayList<Value> args = new ArrayList<>();
    args.add( value );

    try
    {
      return wrapperClass.invokeMethod( suspendedThread, factoryMethod, args, 0 );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private Value unbox( Location suspectedLoc, ThreadReference suspendedThread, ObjectReference value, String wrapperTypeName, String strMethod )
  {
    ClassType wrapperClass = (ClassType)suspectedLoc.virtualMachine().classesByName( wrapperTypeName ).get( 0 );
    List<Method> methods = wrapperClass.methodsByName( strMethod );
    if( methods.size() == 0 )
    {
      throw new RuntimeException( "Could not find method " + strMethod + "()" );
    }

    Method factoryMethod = methods.get( 0 );
    try
    {
      return value.invokeMethod( suspendedThread, factoryMethod, Collections.emptyList(), 0 );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}

