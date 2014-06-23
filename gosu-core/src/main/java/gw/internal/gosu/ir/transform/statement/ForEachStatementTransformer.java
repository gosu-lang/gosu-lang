/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.config.CommonServices;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRMethodCallExpressionCompiler;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.statements.ForEachStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.interval.AbstractIntIterator;
import gw.lang.reflect.interval.AbstractLongIterator;
import gw.lang.reflect.interval.IntegerInterval;
import gw.lang.reflect.interval.LongInterval;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 */
public class ForEachStatementTransformer extends AbstractStatementTransformer<ForEachStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ForEachStatement stmt )
  {
    ForEachStatementTransformer compiler = new ForEachStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private ForEachStatementTransformer( TopLevelTransformationContext cc, ForEachStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {

    // Push a scope in case foreach body not a statement list
    _cc().pushScope( false );
    try
    {
      IRForEachStatement forLoop = makeLoopImpl( _cc(), ExpressionTransformer.compile( _stmt().getInExpression(), _cc() ),
                                                 _stmt().getInExpression().getType(),
                                                 _stmt().getIdentifier(),
                                                 _stmt().getIndexIdentifier(),
                                                 _stmt().getIteratorIdentifier() );
      forLoop.setBody( _cc().compile( _stmt().getStatement() ) );
      return forLoop;
    }
    finally
    {
      _cc().popScope();
    }
  }

  /**
   * Helper for creating iterative loops.  Note that after calling this method, you should compile and call
   * gw.internal.gosu.ir.nodes.statement.IRForEachStatement#setBody(gw.internal.gosu.ir.nodes.IRStatement) on the
   * IRForEachStatement.  Since the body often depends on symbols introduced in the loop, you must usually compile it
   * after the loop has been created.  Thus it cannot be an argument to this function.
   */
  public static IRForEachStatement makeLoop( TopLevelTransformationContext cc, IRExpression rootExpression, IType type,
                                             Symbol identifier, Symbol indexSymbol )
  {
    return new ForEachStatementTransformer( cc, null )
      .makeLoopImpl( cc, rootExpression, type, identifier, indexSymbol, null );
  }

  private IRForEachStatement makeLoopImpl( TopLevelTransformationContext cc, IRExpression rootExpression, IType type,
                                           Symbol identifier, Symbol indexSymbol, Symbol iteratorIdentifier )
  {
    IRForEachStatement forLoop = new IRForEachStatement();
    if( isArrayIteration( type ) )
    {
      makeArrayLoop( cc, rootExpression, forLoop, identifier );
    }
    else if( identifier.getType() == JavaTypes.pINT() &&
             !JavaTypes.NUMBER_INTERVAL().isAssignableFrom( type ) )
    {
      makeIntLoop( cc, rootExpression, forLoop, identifier );
    }
    else
    {
      makeIteratorLoop( cc, rootExpression, forLoop, identifier, iteratorIdentifier );
    }

    if( indexSymbol != null )
    {
      // index variable init
      IRAssignmentStatement indexAssignment = initLocalVar( indexSymbol, numericLiteral( -1 ) );
      forLoop.addInitializer( indexAssignment );

      // increment index
      IRSymbol indexIRSymbol = indexAssignment.getSymbol();
      if( indexSymbol.isValueBoxed() )
      {
        IRExpression increment = buildAddition( buildArrayLoad( identifier( indexIRSymbol ), 0, getDescriptor( indexSymbol.getType() ) ), numericLiteral( 1 ) );
        forLoop.addIncrementor( buildArrayStore( identifier( indexIRSymbol ), numericLiteral( 0 ), increment, IRTypeConstants.pINT()) );
      }
      else
      {
        IRExpression increment = buildAddition( identifier( indexIRSymbol ), numericLiteral( 1 ) );
        forLoop.addIncrementor( buildAssignment( indexIRSymbol, increment ) );
      }
    }

    return forLoop;
  }

  private void makeIteratorLoop( TopLevelTransformationContext cc, IRExpression rootExpression, IRForEachStatement forLoop, Symbol identifier, Symbol iteratorIdentifier )
  {
    // iterator temporary variable init
    IRExpression iteratorInit = callStaticMethod( ForEachStatementTransformer.class, "makeIterator", new Class[]{Object.class, boolean.class}, exprList( rootExpression, pushConstant( _stmt() != null && _stmt().isStructuralIterable() ) ) );
    if( rootExpression.getType() == JavaClassIRType.get( IntegerInterval.class ) )
    {
      iteratorInit = checkCast( AbstractIntIterator.class, iteratorInit );
    }
    else if( rootExpression.getType() == JavaClassIRType.get( LongInterval.class ) )
    {
      iteratorInit = checkCast( AbstractLongIterator.class, iteratorInit );
    }

    IRSymbol irSymbol;
    if( iteratorIdentifier != null )
    {
      irSymbol = makeIRSymbol( iteratorIdentifier );
    }
    else
    {
      irSymbol = cc.makeAndIndexTempSymbol( iteratorInit.getType() );
    }
    IRAssignmentStatement iterator = buildAssignment( irSymbol, iteratorInit );

    forLoop.addInitializer( iterator );

    // null check the iterator
    forLoop.setIdentifierToNullCheck( identifier( iterator.getSymbol() ) );

    // loop variable init
    IRAssignmentStatement loopInitializer = initLocalVarWithDefault( identifier );
    forLoop.addInitializer( loopInitializer );
    IRSymbol loopIdentifier = loopInitializer.getSymbol();

    // loop test
    forLoop.setLoopTest( callMethod( Iterator.class, "hasNext", new Class[0], identifier( iterator.getSymbol() ), Collections.<IRExpression>emptyList() ) );

    // increment iterator
    IRExpression nextValue;
    if( rootExpression.getType() == JavaClassIRType.get( IntegerInterval.class ) )
    {
      // Optimize for integer intervals (no boxing)
      nextValue = callMethod( AbstractIntIterator.class, "nextInt", new Class[0], identifier( iterator.getSymbol() ), Collections.<IRExpression>emptyList() );
    }
    else if( rootExpression.getType() == JavaClassIRType.get( LongInterval.class ) )
    {
      // Optimize for long intervals (no boxing)
      nextValue = callMethod( AbstractLongIterator.class, "nextLong", new Class[0], identifier( iterator.getSymbol() ), Collections.<IRExpression>emptyList() );
    }
    else
    {
      IRExpression nextMethodCall = callMethod( Iterator.class, "next", new Class[0], identifier( iterator.getSymbol() ), Collections.<IRExpression>emptyList() );
      // checkcast to actual type
      nextValue = checkCast( identifier.getType(), nextMethodCall );
    }

    if( identifier.isValueBoxed() )
    {
      forLoop.addIncrementor( buildAssignment( loopIdentifier, buildInitializedArray( getDescriptor( identifier.getType() ), Collections.singletonList( nextValue ) ) ) );
    }
    else
    {
      forLoop.addIncrementor( buildAssignment( loopIdentifier, nextValue ) );
    }
  }

  private void makeArrayLoop( TopLevelTransformationContext cc, IRExpression rootExpression, IRForEachStatement forLoop, Symbol identifier )
  {
    // array temporary variable init
    IRAssignmentStatement array = buildAssignment( cc.makeAndIndexTempSymbol( rootExpression.getType() ), rootExpression );
    forLoop.addInitializer( array );

    // null check the array
    forLoop.setIdentifierToNullCheck( identifier( array.getSymbol() ) );

    // array length init
    IRAssignmentStatement arrayLen = buildAssignment( cc.makeAndIndexTempSymbol(IRTypeConstants.pINT()),
                                                      buildAddition( numericLiteral( -1 ), buildNullCheckTernary( identifier( array.getSymbol() ),
                                                                                                                  numericLiteral( -1 ),
                                                                                                                  buildArrayLength( identifier( array.getSymbol() ) ) ) ) );
    forLoop.addInitializer( arrayLen );

    // array position init
    IRAssignmentStatement arrayPos = buildAssignment( cc.makeAndIndexTempSymbol(IRTypeConstants.pINT()), numericLiteral( -1 ) );
    forLoop.addInitializer( arrayPos );

    // loop variable init
    IRAssignmentStatement loopInitializer = initLocalVarWithDefault( identifier );
    forLoop.addInitializer( loopInitializer );
    IRSymbol loopIdentifier = loopInitializer.getSymbol();

    // loop test
    forLoop.setLoopTest( buildNotEquals( identifier( arrayPos.getSymbol() ), identifier( arrayLen.getSymbol() ) ) );

    // increment array position
    forLoop.addIncrementor( buildAssignment( arrayPos.getSymbol(), buildAddition( identifier( arrayPos.getSymbol() ), numericLiteral( 1 ) ) ) );

    // update loop variable
    IRExpression nextValue = buildArrayLoad( identifier( array.getSymbol() ), identifier( arrayPos.getSymbol() ), getDescriptor( identifier.getType() ) );
    if( identifier.isValueBoxed() )
    {
      forLoop.addIncrementor( buildAssignment( loopIdentifier, buildInitializedArray( getDescriptor( identifier.getType() ), Collections.singletonList( nextValue ) ) ) );
    }
    else
    {
      forLoop.addIncrementor( buildAssignment( loopIdentifier, nextValue ) );
    }
  }

  private void makeIntLoop( TopLevelTransformationContext cc, IRExpression rootExpression, IRForEachStatement forLoop, Symbol identifier )
  {
    // int temporary variable
    IRAssignmentStatement intToCountTo = buildAssignment( cc.makeAndIndexTempSymbol(IRTypeConstants.pINT()), buildAddition(makeInt(rootExpression), numericLiteral( -1 ) ) );
    forLoop.addInitializer( intToCountTo );

    // loop variable init
    IRAssignmentStatement loopInitializer = initLocalVar( identifier, numericLiteral( -1 ) );
    forLoop.addInitializer( loopInitializer );
    IRSymbol loopIdentifier = loopInitializer.getSymbol();

    if( identifier.isValueBoxed() )
    {
      // loop test
      forLoop.setLoopTest( buildGreaterThan( identifier( intToCountTo.getSymbol() ), buildArrayLoad( identifier( loopIdentifier ), 0, loopIdentifier.getType().getComponentType() ) ) );
      // increment loop variable
      IRExpression incrementedValue = buildAddition( buildArrayLoad( identifier( loopIdentifier ), 0, loopIdentifier.getType().getComponentType() ), numericLiteral( 1 ) );
      forLoop.addIncrementor( buildAssignment( loopIdentifier, buildInitializedArray( getDescriptor( identifier.getType() ), Collections.singletonList( incrementedValue ) ) ) );
    }
    else
    {
      // loop test
      forLoop.setLoopTest( buildGreaterThan( identifier( intToCountTo.getSymbol() ), identifier( loopIdentifier ) ) );
      // increment loop variable
      IRExpression incrementedValue = buildAddition( identifier( loopIdentifier ), numericLiteral( 1 ) );
      forLoop.addIncrementor( buildAssignment( loopIdentifier, incrementedValue ) );
    }
  }

  private IRExpression makeInt(IRExpression rootExpression) {
    if (rootExpression.getType().isInt()) {
      return rootExpression;
    } else if (IRTypeConstants.NUMBER().isAssignableFrom(rootExpression.getType())) {
      return buildMethodCall(Number.class, "intValue", int.class, new Class[0], rootExpression, Collections.<IRExpression>emptyList());
    } else if (rootExpression.getType().isPrimitive()) {
      return numberConvert(rootExpression.getType(), IRTypeConstants.pINT(), rootExpression);
    } else {
      throw new IllegalArgumentException("Cannot create an int from value of type " + rootExpression.getType());
    }
  }

  private static boolean isArrayIteration( IType iterationType )
  {
    return isBytecodeType( iterationType ) && iterationType.isArray();
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static Iterator makeIterator( Object obj, boolean bStructuralIterable )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof Iterable )
    {
      return ((Iterable)obj).iterator();
    }

    if( bStructuralIterable )
    {
      return ((Iterable)IRMethodCallExpressionCompiler.constructProxy( obj, Iterable.class.getName() )).iterator();
    }

    if( obj instanceof Iterator )
    {
      return(Iterator)obj;
    }

    // Treat a string as a list of characters
    if( obj instanceof String )
    {
      return new StringIterator( (String)obj );
    }

    if( obj instanceof Number )
    {
      return new NumberIterator( (Number)obj );
    }

    if( TypeSystem.getFromObject( obj ).isArray() )
    {
      return new ArrayIterator( obj, TypeSystem.getFromObject( obj ) );
    }

    // Oh well. Convert to a List of length one and iterate that single element.
    return Collections.nCopies( 1, obj ).iterator();
  }

  static final class ArrayIterator implements Iterator
  {
    private int _iCsr;
    private Object _array;
    private IType _arrayType;

    ArrayIterator( Object array, IType arrayType )
    {
      _iCsr = 0;
      _arrayType = arrayType;
      _array = CommonServices.getCoercionManager().convertValue(array, _arrayType);
    }

    public boolean hasNext()
    {
      return _iCsr < _arrayType.getArrayLength( _array );
    }

    public Object next()
    {
      if( !hasNext() )
      {
        throw new NoSuchElementException( "No element at index [" + _iCsr + "] for the array." );
      }

      return _arrayType.getArrayComponent( _array, _iCsr++ );
    }

    public void remove()
    {
      throw new UnsupportedOperationException( "Sorry, ArrayIterator does not support remove()." );
    }
  }

  static final class StringIterator implements Iterator
  {
    int iCsr = 0;
    private final String _strObj;

    public StringIterator( String strObj )
    {
      _strObj = strObj;
    }

    public boolean hasNext()
    {
      return iCsr < _strObj.length();
    }

    public Object next()
    {
      if( !hasNext() )
      {
        throw new NoSuchElementException( "No element at index [" + iCsr + "] for character iterator" );
      }

      return String.valueOf( _strObj.charAt( iCsr++ ) );
    }

    public void remove()
    {
      throw new UnsupportedOperationException( "Sorry, String character iterator does not support remove()." );
    }
  }

  static final class NumberIterator implements Iterator
  {
    private int _iIndex;
    private final int _iNum;

    public NumberIterator( Number numObj )
    {
      _iNum = numObj.intValue();
    }

    public void remove()
    {
      throw new UnsupportedOperationException( "Sorry, the integer iterator does not support remove()." );
    }

    public boolean hasNext()
    {
      return _iIndex < _iNum;
    }

    public Object next()
    {
      return _iIndex++;
    }
  }
}