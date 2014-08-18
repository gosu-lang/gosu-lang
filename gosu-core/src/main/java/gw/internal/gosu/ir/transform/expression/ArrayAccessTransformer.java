/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class ArrayAccessTransformer extends AbstractExpressionTransformer<ArrayAccess>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ArrayAccess expr )
  {
    ArrayAccessTransformer compiler = new ArrayAccessTransformer( cc, expr );
    return compiler.compile();
  }

  private ArrayAccessTransformer( TopLevelTransformationContext cc, ArrayAccess expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    // Transform the root expression
    IRExpression originalRoot = ExpressionTransformer.compile( _expr().getRootExpression(), _cc() );

    // Short-circuit null check (only on non-primitives)
    // In that case, we'll need to store the root off and use an identifier expression as the root
    IRSymbol tempRoot = null;
    IRExpression root;
    boolean needsAutoinsert = ArrayAccess.needsAutoinsert( getParsedElement() );
    boolean bStandardGosu = CommonServices.getEntityAccess().getLanguageLevel().isStandard();
    boolean bNullSafe = _expr().isNullSafe() ||
                        (!bStandardGosu && !_expr().getType().isPrimitive()) ||
                        (needsAutoinsert && !_expr().getType().isPrimitive());
    if( bNullSafe || needsAutoinsert )
    {
      tempRoot = _cc().makeAndIndexTempSymbol( originalRoot.getType() );
      root = identifier( tempRoot );
    }
    else
    {
      root = originalRoot;
    }

    // For auto-insert logic we need to reference the index without reevaluating it, so we need to store
    // it to a temp variable, much like with null-checking
    IRExpression originalIndex =  ExpressionTransformer.compile( _expr().getMemberExpression(), _cc() );
    IRSymbol tempIndex = null;
    IRExpression index;
    if( needsAutoinsert )
    {
      tempIndex = _cc().makeAndIndexTempSymbol( originalIndex.getType() );
      index = identifier( tempIndex );
    }
    else
    {
      index = originalIndex;
    }

    IRExpression arrayAccess;

    // Non-null
    IType rootType = _expr().getRootExpression().getType();
    if( rootType.isArray() && isBytecodeType( rootType ) )
    {
      // Normal array access
      arrayAccess = buildArrayLoad( root, index, getDescriptor( rootType.getComponentType() ) );
    }
    else if( JavaTypes.LIST().isAssignableFrom( rootType ) )
    {
      arrayAccess = buildMethodCall( List.class, "get", Object.class, new Class[]{int.class}, buildCast( getDescriptor( List.class ), root ), Collections.singletonList( index ) );
      arrayAccess = unboxOrCast( arrayAccess );
    }
    else if( JavaTypes.COLLECTION().isAssignableFrom( rootType ) )
    {
      IRExpression iterExpr = buildMethodCall( Collection.class, "iterator", Iterator.class, new Class[]{}, buildCast( getDescriptor( Collection.class ), root ), Collections.<IRExpression>emptyList() );
      arrayAccess = callStaticMethod( ArrayAccess.class, "getElementFromIterator", new Class[]{Iterator.class, int.class}, Arrays.asList( iterExpr, index ) );
      arrayAccess = unboxOrCast( arrayAccess );
    }
    else if( JavaTypes.ITERATOR().isAssignableFrom( rootType ) )
    {
      arrayAccess = callStaticMethod( ArrayAccess.class, "getElementFromIterator", new Class[]{Iterator.class, int.class}, Arrays.asList( buildCast( getDescriptor( Iterator.class ), root ), index ) );
      arrayAccess = unboxOrCast( arrayAccess );
    }
    else if( JavaTypes.CHAR_SEQUENCE().isAssignableFrom( rootType ) )
    {
      //## todo: this should return just the char not a String, right?
      arrayAccess = callStaticMethod( String.class, "valueOf", new Class[]{char.class}, Collections.<IRExpression>singletonList(
        buildMethodCall( CharSequence.class, "charAt", char.class, new Class[]{int.class},
                         buildCast( getDescriptor( CharSequence.class ), root ), Collections.singletonList( index ) ) ) );
      arrayAccess = unboxOrCast( arrayAccess );
    }
    else
    {
      // Custom Array Type
      arrayAccess = callStaticMethod( ArrayAccess.class, "getArrayElement", new Class[]{Object.class, int.class, boolean.class},
                                      exprList( root, index, pushConstant( bNullSafe ) ) );
      arrayAccess = unboxOrCast( arrayAccess );
    }

    if( needsAutoinsert )
    {
      IType typeToAutoInsert = ArrayAccess.getTypeToAutoInsert( getParsedElement().getRootExpression() );
      // add an maybeAutoInsert before the array access
      arrayAccess = buildComposite( buildMethodCall( callStaticMethod( ArrayAccessTransformer.class, "maybeAutoInsert",
                                                                       new Class[]{Object.class, int.class, IType.class},
                                                                       Arrays.asList( identifier( tempRoot ),
                                                                                      identifier( tempIndex ),
                                                                                      pushType( typeToAutoInsert ) ) ) ),
                                    arrayAccess );
    }

    if( tempRoot != null )
    {
      // Null short-circuit: create a composite expression that looks like:
      // temp = root
      // (temp == null ? (ExpectedType) null : temp[member])

      IRCompositeExpression composite = new IRCompositeExpression();
      composite.addElement( buildAssignment( tempRoot, originalRoot ) );
      if( tempIndex != null )
      {
        composite.addElement( buildAssignment( tempIndex, originalIndex ) );
      }
      if( _expr().getType().isPrimitive() )
      {
        composite.addElement( buildNullCheckTernary( identifier( tempRoot ),
                                                     shortCircuitValue( arrayAccess.getType() ),
                                                     arrayAccess ) );
      }
      else
      {
        composite.addElement( buildNullCheckTernary( identifier( tempRoot ),
                                                     checkCast( _expr().getType(), nullLiteral() ),
                                                     checkCast( _expr().getType(), arrayAccess ) ) );
      }
      return composite;
    }
    else
    {
      // We're not short-circuiting, so no need to wrap the access expression
      return arrayAccess;
    }
  }

  private IRExpression unboxOrCast( IRExpression arrayAccess ) {
    if( _expr().getType().isPrimitive() )
    {
      arrayAccess = unboxValueToType( _expr().getType(), arrayAccess );
    }
    else
    {
      arrayAccess = checkCast( _expr().getType(), arrayAccess );
    }
    return arrayAccess;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static void maybeAutoInsert( Object obj, int index, IType typeToAutoCreate )
  {
    List l = (List)obj;
    if( l.size() == index )
    {
      if( typeToAutoCreate.isAbstract() || typeToAutoCreate.isInterface() )
      {
        throw new IllegalStateException( "Type " + typeToAutoCreate.getName() + " is abstract or an interface and has no default implementation class to use" );
      }
      final IConstructorInfo constructor = typeToAutoCreate.getTypeInfo().getConstructor();
      if( constructor == null )
      {
        throw new IllegalStateException( "Type " + typeToAutoCreate.getName() + " has no default constructor" );
      }
      //noinspection unchecked
      l.add( constructor.getConstructor().newInstance() );
    }
  }
}
