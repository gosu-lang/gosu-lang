/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.statement.ForEachStatementTransformer;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRNoOpExpression;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import gw.util.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 */
public abstract class AbstractMemberExpansionTransformer<T extends IMemberAccessExpression> extends AbstractExpressionTransformer<T>
{

  protected AbstractMemberExpansionTransformer( TopLevelTransformationContext cc, T expr )
  {
    super( cc, expr );
  }

  /**
   * Subclassers need only implement this method for the iteration expression i.e., the singular form of the expansion expr.
   */
  protected abstract IRExpression createIterationExpr(IType rootComponentType, String identifierName, IType identifierType, IType compType);

  protected abstract IType getPropertyOrMethodType(IType rootComponentType, IType compType);

  protected IRExpression compile_impl()
  {
    IType arrayType = _expr().getType();
    final boolean bVoid = arrayType == JavaTypes.pVOID();
    if( !bVoid && !arrayType.isArray() )
    {
      throw new IllegalStateException( "Expecting an array type. Found: " + arrayType.getName() );
    }
    final IType compType = bVoid ? arrayType : getConcreteType( arrayType.getComponentType() );
    IType rootType = _expr().getRootType();
    final IType rootComponentType = TypeLord.getExpandableComponentType( rootType, false );

    IType propertyType = getPropertyOrMethodType(rootComponentType, compType);

    _cc().pushScope( false );
    try
    {
      if (bVoid)
      {
        // This is like calling root.each(\r -> r.foo())
        return compileExpansionWithNoReturnValue( rootType, rootComponentType, arrayType, compType );
      }
      else if(isBytecodeType( propertyType ) && isArrayOrCollection( rootType ) && !isArrayOrCollection( rootComponentType ) && !isArrayOrCollection( propertyType ))
      {
        // This is like calling root.map(\r -> r.Bar)
        return compileExpansionDirectlyToArray( rootType, rootComponentType, arrayType, compType );
      }
      else
      {
        // This is like calling root.flatMap(\r -> r.Bar)
        return compileExpansionUsingArrayList( rootType, rootComponentType, arrayType, compType, propertyType );
      }
    }
    finally
    {
      _cc().popScope();
    }
  }


  private static boolean isArrayOrCollection( IType type ) {
    return type.isArray() || JavaTypes.COLLECTION().isAssignableFrom( type );
  }

  protected IRExpression compileExpansionWithNoReturnValue( IType rootType, IType rootComponentType, IType resultType, IType resultCompType ) {
    // Evaluate the root and assign it to a temp variable
    IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( getDescriptor( rootType ) );
    IRStatement tempRootAssignment = buildAssignment( tempRoot, ExpressionTransformer.compile( _expr().getRootExpression(), _cc() ) );

    IRForEachStatement statement = createNoValueLoop( rootType, rootComponentType, resultCompType, tempRoot );

    return buildComposite(
            tempRootAssignment,
            statement,
            new IRNoOpExpression()
            );
  }

  private IRForEachStatement createNoValueLoop(IType rootType, IType rootComponentType, IType resultCompType, IRSymbol tempRoot ) {
    Symbol loopIdentifier = new Symbol(_cc().makeTempSymbolName(), rootComponentType, null);
    IRForEachStatement forLoop = ForEachStatementTransformer.makeLoop( _cc(), identifier( tempRoot ), rootType, loopIdentifier, null);
    IRSymbol irLoopIdentifier = _cc().getSymbol( loopIdentifier.getName() );
    forLoop.setBody( new IRStatementList( false, buildMethodCall( createIterationExpr( rootComponentType, irLoopIdentifier.getName(), rootComponentType, resultCompType ) ) ) );
    return forLoop;
  }

  /**
   * If this method is being called, it means we're expanding a one-dimensional array or collection, with a right hand side
   * that evaluates to a property that's not an array or collection.  In that case, we build up an array and simply store
   * values directly into it.  We also null-short-circuit in the event that the root is null.  The member expansion portion
   * ends up as a composite that looks like:
   *
   *   temp_array = new Foo[temp_root.length]
   *   for (a in temp_root index i) {
   *     temp_array[i] = a.Bar
   *   }
   *   temp_array
   *
   * And the overall expression looks like:
   *
   *   temp_root = root
   *   ( temp_root == null ? (Bar[]) null : (Bar[]) member_expansion )
   */
  protected IRExpression compileExpansionDirectlyToArray( IType rootType, IType rootComponentType, IType resultType, IType resultCompType ) {
    // Evaluate the root and assign it to a temp variable
    IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( getDescriptor( rootType ) );
    IRStatement tempRootAssignment = buildAssignment( tempRoot, ExpressionTransformer.compile( _expr().getRootExpression(), _cc() ) );

    // Create the result array and assign it to a temp variable
    IRSymbol resultArray = _cc().makeAndIndexTempSymbol( getDescriptor( resultType ) );
    IRStatement arrayCreation = buildAssignment( resultArray, makeArray( resultCompType, createArrayLengthExpression( rootType, tempRoot ) ) );

    // Create the loop that populates the array
    IRForEachStatement forLoop = createArrayStoreLoop(rootType, rootComponentType, resultCompType, tempRoot, resultArray);

    // Build the expansion out of the array creation, for loop, and identifier
    IRExpression expansion = buildComposite( arrayCreation, forLoop, identifier( resultArray ) );

    // Short-circuit if we're not dealing with primitive types
    if (!rootComponentType.isPrimitive() && !resultCompType.isPrimitive()) {
      return buildComposite(
              tempRootAssignment,
              buildNullCheckTernary( identifier( tempRoot ),
                                    checkCast( _expr().getType(), makeArray(resultCompType, numericLiteral(0)) ),
                                    checkCast( _expr().getType(), expansion ) )
      );
    } else {
       return buildComposite(
              tempRootAssignment,
              checkCast( _expr().getType(), expansion ) );
    }
  }

  private IRExpression makeArray( IType componentType, IRExpression lengthExpression ) {
    if( componentType.isPrimitive() || componentType instanceof IGosuClass || componentType instanceof IJavaType ) {
      // Gosu/Java arrays are directly represented in bytecode, so we can directly construct a Java array from the component type
      return newArray( getDescriptor( componentType ), lengthExpression );
    }
    // Custom array types may not have direct representation in bytecode e.g., whacky entities
    IRExpression array = callMethod( IType.class, "makeArrayInstance", new Class[]{int.class}, pushType( componentType ), exprList( lengthExpression ) );
    return checkCast( componentType.getArrayType(), array );
  }

  private IRExpression createArrayLengthExpression(IType rootType, IRSymbol tempRoot) {
    // We either need to do foo.length or foo.size() depending on if we've got an array or a collection
    if ( rootType.isArray() )
    {
      return buildArrayLength( identifier( tempRoot ) );
    }
    else if (JavaTypes.COLLECTION().isAssignableFrom( rootType ) )
    {
      IRMethod irMethod = IRMethodFactory.createIRMethod( rootType, "size", JavaTypes.pINT(), new IType[0], IRelativeTypeInfo.Accessibility.PUBLIC, false );
      return callMethod( irMethod, identifier( tempRoot ), exprList());
    }
    else
    {
      throw new IllegalArgumentException( "Cannot get the size of type " + rootType );
    }
  }

  private IRForEachStatement createArrayStoreLoop(IType rootType, IType rootComponentType, IType resultCompType, IRSymbol tempRoot, IRSymbol resultArray) {
    // The body of the loop looks like:
    // temp_array[i] = l.Bar
    Symbol loopIdentifier = new Symbol(_cc().makeTempSymbolName(), rootComponentType, null);
    Symbol loopIndex = new Symbol(_cc().makeTempSymbolName(), JavaTypes.pINT(), null);
    IRForEachStatement forLoop = ForEachStatementTransformer.makeLoop( _cc(), identifier( tempRoot ), rootType, loopIdentifier, loopIndex);
    IRSymbol irLoopIdentifier = _cc().getSymbol( loopIdentifier.getName() );
    IRSymbol irLoopIndex = _cc().getSymbol( loopIndex.getName() );
    forLoop.setBody( buildArrayStore( identifier( resultArray),
                                      identifier( irLoopIndex ),
                                      createIterationExpr( rootComponentType, irLoopIdentifier.getName(), rootComponentType, resultCompType ),
                                      resultArray.getType().getComponentType() ) );
    return forLoop;
  }

  /**
   * This method will compile the expansion using an ArrayList to collect temporary results.  This is appropriate if the right-hand-side
   * is a Collection or array, if the root is an Iterable or Iterator or other object whose size can't easily be determined up-front,
   * or if the root is a nested Collection or array that will require additional unwrapping.
   *
   * The overall result of the expansion thus looks like the following composite:
   *
   *   temp_arraylist = new ArrayList()
   *   for (a in temp_root) {
   *     temp_arraylist.addAll( AbstractMemberExpansionTransform.arrayToCollection( a.Bars ) )
   *   }
   *   AbstractMemberExpansionTransformer.listToArray(temp_array)
   *
   */
  protected IRExpression compileExpansionUsingArrayList( IType rootType, IType rootComponentType, IType resultType, IType resultCompType, IType propertyType ) {
    // Evaluate the root and assign it to a temp variable
    IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( getDescriptor( rootType ) );
    IRStatement tempRootAssignment = buildAssignment( tempRoot, ExpressionTransformer.compile( _expr().getRootExpression(), _cc() ) );

    // Create an ArrayList to store the results
    IRSymbol resultArrayList = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.ARRAY_LIST() ) );
    IRStatement arrayListCreation = buildAssignment(resultArrayList, buildNewExpression( ArrayList.class, new Class[0], exprList() ) );

    // Now we loop over each element in the root array or collection.  For each element, we evaluate the
    // RHS, wrap it by calling arrayToCollection, then add it to the result list via addAll
    IRForEachStatement forLoop = createArrayListAddLoop(rootType, rootComponentType, resultCompType, tempRoot, resultArrayList, propertyType );

    // Now take the ArrayList and convert it to the desired return type
    IRExpression listToArrayConversion = convertListToArray(resultType, resultCompType, resultArrayList);

    return buildComposite( tempRootAssignment, arrayListCreation, forLoop, listToArrayConversion );
  }

  private IRForEachStatement createArrayListAddLoop(IType rootType, IType rootComponentType, IType resultCompType, IRSymbol tempRoot, IRSymbol resultArrayList, IType propertyType) {
    Symbol loopIdentifier = new Symbol(_cc().makeTempSymbolName(), rootComponentType, null);
    IRForEachStatement forLoop = ForEachStatementTransformer.makeLoop( _cc(), identifier( tempRoot ), rootType, loopIdentifier, null);
    IRSymbol irLoopIdentifier = _cc().getSymbol( loopIdentifier.getName() );

    // If the right hand side is an array or collection, then wrap it up as a Collection and call addAll on the set we're building up.
    // Otherwise, box it and call the add method.
    IRStatement loopBody;
    if ( propertyType.isArray() ) {
      IRExpression resultAsCollection = callStaticMethod( AbstractMemberExpansionTransformer.class, "arrayToCollection", new Class[]{Object.class},
              exprList( createIterationExpr( rootComponentType, irLoopIdentifier.getName(), rootComponentType, resultCompType) ) );
      loopBody = buildMethodCall( callMethod( ArrayList.class, "addAll", new Class[]{Collection.class},
                      identifier( resultArrayList ),
                      exprList( resultAsCollection ) ) );
    } else {
      loopBody = buildMethodCall( callMethod( ArrayList.class, "add", new Class[]{Object.class},
                      identifier( resultArrayList ),
                      exprList( boxValueToType( propertyType, createIterationExpr( rootComponentType, irLoopIdentifier.getName(), rootComponentType, resultCompType) ) ) ) );
    }
    forLoop.setBody( new IRStatementList( false, loopBody ) );
    return forLoop;
  }

  private IRExpression convertListToArray(IType resultType, IType resultCompType, IRSymbol resultArrayList) {
    if( resultCompType.isPrimitive() )
    {
      return convertToPrimitiveArray( resultCompType, identifier( resultArrayList ) );
    }
    else
    {
      IType arrayComponentType = getMoreSpecificType( resultCompType, resultType.getComponentType() );
      IRExpression listToArrayCall;
      if( isBytecodeType( arrayComponentType ) )
      {
        listToArrayCall = callStaticMethod( AbstractMemberExpansionTransformer.class, "listToArray", new Class[]{List.class, Class.class},
                            exprList( identifier( resultArrayList ), classLiteral( getDescriptor( arrayComponentType ) ) ));
      }
      else
      {
        listToArrayCall = callStaticMethod( AbstractMemberExpansionTransformer.class, "listToArray", new Class[]{List.class, IType.class},
                            exprList( identifier( resultArrayList ), pushType( arrayComponentType ) ));
      }
      return checkCast( arrayComponentType.getArrayType(), listToArrayCall );
    }
  }

  private IType getMoreSpecificType(IType type1, IType type2) {
    if (type1.isAssignableFrom(type2)) {
      return type2;
    } else if (type2.isAssignableFrom(type1)){
      return type1;
    } else {
      return type1;
    }
  }

  private IRExpression convertToPrimitiveArray( IType compType, IRExpression listToConvert )
  {
    if( compType == JavaTypes.pBOOLEAN() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_boolean", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pBYTE() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_byte", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pCHAR() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_char", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pDOUBLE() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_double", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pFLOAT() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_float", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pINT() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_int", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pLONG() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_long", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else if( compType == JavaTypes.pSHORT() )
    {
      return callStaticMethod( AbstractMemberExpansionTransformer.class, "listToPrimitiveArray_short", new Class[]{List.class}, exprList( listToConvert ) );
    }
    else
    {
      throw new UnsupportedOperationException( "Don't know how to handle primitive type: " + compType.getName() );
    }
  }

  public static boolean[] listToPrimitiveArray_boolean( List l )
  {
    int iCount = l.size();
    boolean[] array = new boolean[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Boolean)l.get( i )).booleanValue();
    }
    return array;
  }

  public static byte[] listToPrimitiveArray_byte( List l )
  {
    int iCount = l.size();
    byte[] array = new byte[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Byte)l.get( i )).byteValue();
    }
    return array;
  }

  public static char[] listToPrimitiveArray_char( List l )
  {
    int iCount = l.size();
    char[] array = new char[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Character)l.get( i )).charValue();
    }
    return array;
  }

  public static int[] listToPrimitiveArray_int( List l )
  {
    int iCount = l.size();
    int[] array = new int[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Integer)l.get( i )).intValue();
    }
    return array;
  }

  public static short[] listToPrimitiveArray_short( List l )
  {
    int iCount = l.size();
    short[] array = new short[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Short)l.get( i )).shortValue();
    }
    return array;
  }

  public static long[] listToPrimitiveArray_long( List l )
  {
    int iCount = l.size();
    long[] array = new long[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Long)l.get( i )).longValue();
    }
    return array;
  }

  public static float[] listToPrimitiveArray_float( List l )
  {
    int iCount = l.size();
    float[] array = new float[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Float)l.get( i )).floatValue();
    }
    return array;
  }

  public static double[] listToPrimitiveArray_double( List l )
  {
    int iCount = l.size();
    double[] array = new double[iCount];
    for( int i = 0; i < iCount; i++ )
    {
      array[i] = ((Double)l.get( i )).doubleValue();
    }
    return array;
  }

  public static Object listToArray( List l, IType compType )
  {
    int iCount = l.size();
    Object array = compType.makeArrayInstance( iCount );
    for( int i = 0; i < iCount; i++ )
    {
      compType.setArrayComponent( array, i, l.get( i ) );
    }
    return array;
  }

  public static Object listToArray( List l, Class compType )
  {
    int iCount = l.size();
    Object array = Array.newInstance( compType, iCount );
    for( int i = 0; i < iCount; i++ )
    {
      Array.set( array, i, l.get( i ) );
    }
    return array;
  }

  public static Collection arrayToCollection( Object value )
  {
    if (value == null) {
      return Collections.emptyList();
    }
    else if (value instanceof Collection)
    {
      return (Collection) value;
    }
    else if (value instanceof Object[])
    {
      return Arrays.asList((Object[]) value);
    }
    else if (value instanceof IGosuObject)
    {
      List col = new ArrayList();
      IType arrayType = ((IGosuObject)value).getIntrinsicType();
      int iLength = arrayType.getArrayLength( value );
      for( int j = 0; j < iLength; j++ )
      {
        //noinspection unchecked
        col.add( arrayType.getArrayComponent( value, j ) );
      }
      return col;
    }
    else if( value.getClass().isArray() && value.getClass().getComponentType().isPrimitive() )
    {
      List col = new ArrayList();
      for( int i = 0; i < Array.getLength( value ); i++ )
      {
        //noinspection unchecked
        col.add( Array.get( value, i ) );
      }
      return col;
    }
    else
    {
      throw new IllegalArgumentException("Cannot turn value of type " + value.getClass() + " into an array");
    }
  }
}
