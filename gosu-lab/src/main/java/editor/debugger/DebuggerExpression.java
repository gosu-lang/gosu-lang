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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class DebuggerExpression
{
  private Value _value;
  private String _strText;
  private String _strClassContext;
  private String _strContextElementClass;
  private int _iContextLocation;
  private WeakHashMap<Debugger, RuntimeState> _runtimeStateMap;


  public DebuggerExpression( String expr,
                             String strClassContext, String strContextElementClass,
                             int iContextLocation )
  {
    _strText = expr;
    _strClassContext = strClassContext;
    _strContextElementClass = strContextElementClass;
    _iContextLocation = iContextLocation;
    _runtimeStateMap = new WeakHashMap<>();
  }

  //call evaluate before
  public Value getValue()
  {
    return _value;
  }

  // EvaluationContextImpl should be at the same stackFrame as it was in the call to EvaluatorBuilderImpl.build
  public Value evaluate( Debugger debugger )
  {
    RuntimeState runtimeState = getRuntimeState( debugger );

    Location suspendedLoc = debugger.getSuspendedLocation();
    VirtualMachine vm = suspendedLoc.virtualMachine();

    ClassType classType = runtimeState.getCodeRunnerClass( vm );
    Value thisObject = findThisObjectFromCtx( debugger.getSuspendedThread() );
    try
    {
      _value = classType.invokeMethod(
        debugger.getSuspendedThread(), runtimeState.getRunMeSomeCodeMethod(),
        Arrays.asList(
          thisObject,
          suspendedLoc.declaringType().classLoader(),
          makeExternalsSymbolsForLocals( runtimeState, vm, debugger ),
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
    return unboxIfBoxed( runtimeState, debugger.getSuspendedThread(), _value );
  }

  private RuntimeState getRuntimeState( Debugger debugger )
  {
    RuntimeState runtimeState = _runtimeStateMap.get( debugger );
    if( runtimeState == null )
    {
      runtimeState = new RuntimeState();
      _runtimeStateMap.put( debugger, runtimeState );
    }
    return runtimeState;
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

  private ArrayReference makeExternalsSymbolsForLocals( RuntimeState runtimeState, VirtualMachine vm, Debugger debugger )
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
        values.add( boxIfPrimitive( runtimeState, suspendedThread, value ) );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    ArrayType objectArrayClass = runtimeState.getArrayType( vm, "java.lang.Object[]" );
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

  public Value boxIfPrimitive( RuntimeState runtimeState, ThreadReference suspendedThread, Value value )
  {
    if( value == null || value instanceof ObjectReference )
    {
      return value;
    }

    if( value instanceof BooleanValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (BooleanValue)value, "java.lang.Boolean" );
    }
    if( value instanceof ByteValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (ByteValue)value, "java.lang.Byte" );
    }
    if( value instanceof CharValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (CharValue)value, "java.lang.Character" );
    }
    if( value instanceof ShortValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (ShortValue)value, "java.lang.Short" );
    }
    if( value instanceof IntegerValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (IntegerValue)value, "java.lang.Integer" );
    }
    if( value instanceof LongValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (LongValue)value, "java.lang.Long" );
    }
    if( value instanceof FloatValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (FloatValue)value, "java.lang.Float" );
    }
    if( value instanceof DoubleValue )
    {
      return convertToWrapper( runtimeState, suspendedThread, (DoubleValue)value, "java.lang.Double" );
    }
    throw new RuntimeException( "Cannot perform boxing conversion for a value of type " + value.type().name() );
  }

  public Value unboxIfBoxed( RuntimeState runtimeState, ThreadReference suspendedThread, Value value )
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
      return unbox( runtimeState, suspendedThread, valueRef, Boolean.class.getName(), "booleanValue" );
    }
    if( type.name().equals( Byte.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Byte.class.getName(), "byteValue" );
    }
    if( type.name().equals( Character.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Character.class.getName(), "charValue" );
    }
    if( type.name().equals( Short.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Short.class.getName(), "shortValue" );
    }
    if( type.name().equals( Integer.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Integer.class.getName(), "intValue" );
    }
    if( type.name().equals( Long.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Long.class.getName(), "longValue" );
    }
    if( type.name().equals( Float.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Float.class.getName(), "floatValue" );
    }
    if( type.name().equals( Double.class.getName() ) )
    {
      return unbox( runtimeState, suspendedThread, valueRef, Double.class.getName(), "doubleValue" );
    }
    return value;
  }

  private Value convertToWrapper( RuntimeState runtimeState, ThreadReference suspendedThread, PrimitiveValue value, String wrapperTypeName )
  {
    ClassType wrapperClass = runtimeState.getClassType( suspendedThread.virtualMachine(), wrapperTypeName );
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

  private Value unbox( RuntimeState runtimeState, ThreadReference suspendedThread, ObjectReference value, String wrapperTypeName, String strMethod )
  {
    ClassType wrapperClass = runtimeState.getClassType( suspendedThread.virtualMachine(), wrapperTypeName );
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

  private static class RuntimeState
  {
    private ClassType _codeRunnerClass;
    private Method _runMeSomeCodeMethod;
    private Map<String, ReferenceType> _classTypes = new HashMap<>();

    public ClassType getCodeRunnerClass( VirtualMachine vm )
    {
      if( _codeRunnerClass == null )
      {
        List<ReferenceType> types = vm.classesByName( "gw.internal.gosu.parser.ContextSensitiveCodeRunner" );
        _codeRunnerClass = (ClassType)types.get( 0 );
      }
      return _codeRunnerClass;
    }

    public Method getRunMeSomeCodeMethod()
    {
      if( _runMeSomeCodeMethod == null )
      {
        _runMeSomeCodeMethod = _codeRunnerClass.methodsByName( "runMeSomeCode" ).get( 0 );
      }
      return _runMeSomeCodeMethod;
    }

    public ClassType getClassType( VirtualMachine vm, String name )
    {
      return (ClassType)getType( vm, name );
    }
    public ArrayType getArrayType( VirtualMachine vm, String name )
    {
      return (ArrayType)getType( vm, name );
    }
    private ReferenceType getType( VirtualMachine vm, String name )
    {
      ReferenceType classType = _classTypes.get( name );
      if( classType == null )
      {
        classType = vm.classesByName( name ).get( 0 );
        _classTypes.put( name, classType );
      }
      return classType;
    }
  }
}

