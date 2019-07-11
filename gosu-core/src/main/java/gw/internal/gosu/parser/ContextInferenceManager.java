/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.Res;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.StaticTypeOfExpression;
import gw.internal.gosu.parser.expressions.TypeOfExpression;
import gw.internal.gosu.parser.expressions.TemplateStringLiteral;
import gw.internal.gosu.parser.expressions.AdditiveExpression;
import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Stack;
import gw.util.GosuObjectUtil;

import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ContextInferenceManager
{
  private static final boolean ENABLED = true;
  private static final TypeAsContext EMPTY_CTX = new TypeAsContext();

  private Stack<TypeAsContext> _inferenceStack = new Stack<TypeAsContext>();
  private TypeAsContext _last;
  private boolean _refCollectionSuspended;


  public ContextInferenceManager copy()
  {
    ContextInferenceManager copy = new ContextInferenceManager();
    copy._inferenceStack = new Stack<TypeAsContext>( _inferenceStack );
    copy._last = _last;
    copy._refCollectionSuspended = false;
    return copy;
  }
  
  public void pushCtx()
  {
    if( ENABLED )
    {
      _inferenceStack.push( new TypeAsContext() );
    }
  }

  public void popCtx( boolean preserveInference )
  {
    if( ENABLED )
    {
      TypeAsContext last = _inferenceStack.pop();
      if( preserveInference )
      {
        _last = last;
      }
    }
  }

  public void pushLastCtx()
  {
    if( ENABLED )
    {
      if( _last == null )
      {
        _last = EMPTY_CTX;
      }
      _inferenceStack.push( _last );
    }
  }

  public void restoreLastCtx()
  {
    if( ENABLED )
    {
      if( !_inferenceStack.isEmpty() )
      {
        _inferenceStack.peek().merge( _last );
      }
    }
  }

  public void clear()
  {
    if( ENABLED )
    {
      _inferenceStack.peek().entries.clear();
    }
  }

  public void updateType( Expression expression, IType typeIsType )
  {
    if( ENABLED )
    {
      expression = unwrapParens( expression );
      if( !isPossibleToInfer( expression ) )
      {
        return;
      }
      TypeAsEntry currentEntry = findEntry( expression );
      IType type = currentEntry == null ? expression.getType() : currentEntry.inferredType;
      type = type.isAssignableFrom( typeIsType )
             ? typeIsType
             : computeType( typeIsType, type );

      if( currentEntry == null )
      {
        _inferenceStack.peek().entries.add( new TypeAsEntry( expression, expression.getType(), type ) );
      }
      else if( _inferenceStack.peek().entries.contains( currentEntry ) )
      {
        currentEntry.inferredType = type;
      }
      else
      {
        _inferenceStack.peek().entries.add( new TypeAsEntry( currentEntry.expr, currentEntry.originalType, type ) );
      }
    }
  }

  private IType computeType( IType typeIsType, IType type )
  {
    // compound type must have at most one non-interface type
    if( typeIsType.isInterface() || type.isInterface() )
    {
      type = CompoundType.get( type, typeIsType );
    }
    return type;
  }

  private Expression unwrapParens( Expression expression )
  {
    if( expression instanceof ParenthesizedExpression )
    {
      return unwrapParens( ((ParenthesizedExpression)expression).getExpression() );
    }
    return expression;
  }

  public IType infer( Expression e )
  {
    IType inferType = null;
    if( ENABLED )
    {
      TypeAsEntry entry = findEntry( e );
      if( entry != null )
      {
        inferType = entry.inferredType;

        if( isPossibleToInfer( e ) )
        {
          if( entry.loopCompromised > 0 && !_refCollectionSuspended )
          {
            entry.refs.add( e );
          }
        }
      }
    }
    return inferType;
  }

  private boolean isPossibleToInfer( Expression e )
  {
    if( e instanceof Identifier || e instanceof MemberAccess )
    {
      return true;
    }

    // method call ok if a "getter" for a java property
    if( e instanceof BeanMethodCallExpression )
    {
      IMethodInfo mi = ((BeanMethodCallExpression)e).getMethodDescriptor();
      if( mi != null )
      {
        if( GosuClassProxyFactory.isPropertyGetter( mi ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  public void cancelInferences( Expression assignmentRoot, Expression rhs )
  {
    if( ENABLED )
    {
      for( int i = _inferenceStack.size() - 1; i >= 0; i-- )
      {
        for( Iterator<TypeAsEntry> it = _inferenceStack.get( i ).entries.iterator(); it.hasNext(); )
        {
          TypeAsEntry typeAsEntry = it.next();
          if( areExpressionsEquivalent( assignmentRoot, typeAsEntry.expr ) )
          {
            if( !typeAsEntry.inferredType.isAssignableFrom( rhs.getType() ) )
            {
              it.remove();
              handleLoopCompromisedExpressions( typeAsEntry, assignmentRoot );
              assignmentRoot.setType( typeAsEntry.originalType );
            }
          }
          else if( isStartFor( assignmentRoot, typeAsEntry.expr ) )
          {
            if( !typeAsEntry.inferredType.isAssignableFrom( rhs.getType() ) )
            {
              it.remove();
              handleLoopCompromisedExpressions( typeAsEntry, assignmentRoot );
            }
          }
        }
      }
    }
  }

  private void handleLoopCompromisedExpressions( TypeAsEntry typeAsEntry, Expression assignmentRoot )
  {
    if( typeAsEntry.loopCompromised > 0 )
    {
      for( Expression expression : typeAsEntry.refs )
      {
        reverifyExpression( typeAsEntry, assignmentRoot, expression );
      }
    }
  }

  private void reverifyExpression( TypeAsEntry typeAsEntry, Expression assignmentRoot, Expression expression )
  {
    if( expression.equals( assignmentRoot ) )
    {
      return;
    }
    IParsedElement parent = unwrapImplicitTypeAs( expression );
    expression.setType( typeAsEntry.originalType );
    if( parent instanceof MemberAccess )
    {
      MemberAccess ma = (MemberAccess)parent;
      if( typeAsEntry.originalType.getTypeInfo().getProperty( ma.getMemberName() ) == null )
      {
        expression.addParseException( Res.MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL, assignmentRoot.toString() );
      }
    }
    else if( parent instanceof BeanMethodCallExpression )
    {
      BeanMethodCallExpression methodCallExpression = (BeanMethodCallExpression)parent;
      if( methodCallExpression.getRootExpression().equals( expression ) )
      {
        if( methodCallExpression.getMethodDescriptor() != null &&
            !methodCallExpression.getMethodDescriptor().getOwnersType().isAssignableFrom( typeAsEntry.originalType ) )
        {
          expression.addParseException( Res.MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL, assignmentRoot.toString() );
        }
      }
      else
      {
        Expression[] args = methodCallExpression.getArgs();
        for( int i = 0; i < args.length; i++ )
        {
          Expression arg = args[i];
          if( arg.equals( expression ) )
          {
            if( !methodCallExpression.getMethodDescriptor().getParameters()[i].getFeatureType().isAssignableFrom( typeAsEntry.originalType ) )
            {
              expression.addParseException( Res.MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL, assignmentRoot.toString() );
            }
          }
        }
      }
    }
    else if( parent instanceof ReturnStatement )
    {
      ReturnStatement returnStmt = (ReturnStatement)parent;
      if( returnStmt.getReturnType() != null && !returnStmt.getReturnType().isAssignableFrom( typeAsEntry.originalType ) )
      {
        expression.addParseException( Res.MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL, assignmentRoot.toString() );
      }
    }
    else
    {
      if( parent instanceof StaticTypeOfExpression || parent instanceof TypeOfExpression || (parent instanceof StatementList && parent.getParent() instanceof TemplateStringLiteral) )
      {
        return;
      }
      if( parent instanceof AdditiveExpression && parent.getReturnType().equals( JavaTypes.STRING() ) )
      {
        return;
      }
      expression.addParseException( Res.MSG_LATER_ASSIGNMENT_MAKES_EXPRESSION_ILLEGAL, assignmentRoot.toString() );
    }
  }

  public static IParsedElement unwrapImplicitTypeAs( Expression expression )
  {
    IParsedElement parent = expression.getParent();
    if( parent instanceof ImplicitTypeAsExpression )
    {
      ImplicitTypeAsExpression typeas = (ImplicitTypeAsExpression)parent;
      parent = typeas.getParent();
      if( parent != null )
      {
        parent.getLocation().removeChild( typeas.getLocation() );
      }
      typeas.getLocation().removeChild( expression.getLocation() );
      if( parent != null )
      {
        parent.getLocation().addChild( expression.getLocation() );
      }
    }
    return parent;
  }

  /**
   * If the expression is wrapped in ImplicitTypeAsExpressions, this will will unwrap them back
   * down to the original expression.
   *
   * @param expression
   * @return
   */
  public static Expression getUnwrappedExpression( Expression expression )
  {
    while( expression instanceof ImplicitTypeAsExpression )
    {
      expression = ((ImplicitTypeAsExpression) expression).getLHS();
    }
    return expression;
  }

  private boolean isStartFor( Expression possibleStart, Expression expression )
  {
    if( areExpressionsEquivalent( possibleStart, expression ) )
    {
      return true;
    }
    else if( expression instanceof IMemberAccessExpression )
    {
      return isStartFor( possibleStart, (Expression) ((IMemberAccessExpression)expression).getRootExpression() );
    }
    else
    {
      return false;
    }
  }

  private TypeAsEntry findEntry( Expression e )
  {
    for( int i = _inferenceStack.size() - 1; i >= 0; i-- )
    {
      List<TypeAsEntry> entries = _inferenceStack.get(i).entries;
      for (int j = 0; j < entries.size(); j++) {
        TypeAsEntry entry = entries.get(j);
        if( areExpressionsEquivalent( e, entry.expr ) )
        {
          return entry;
        }
      }
    }
    return null;
  }

  private boolean areExpressionsEquivalent( Expression e1, Expression e2 )
  {
    if( e1.hasParseExceptions() || e2.hasParseExceptions() )
    {
      return false;
    }
    else if( e1 instanceof Identifier && e2 instanceof Identifier )
    {
      return ((Identifier)e1).getSymbol().equals( ((Identifier)e2).getSymbol() );
    }
    else if( e1 instanceof MemberAccess && e2 instanceof MemberAccess )
    {
      MemberAccess m1 = (MemberAccess)e1;
      MemberAccess m2 = (MemberAccess)e2;
      return areExpressionsEquivalent( m1.getRootExpression(), m2.getRootExpression() ) &&
             m1.getPropertyInfo() != null
             ? GosuObjectUtil.equals( m1.getPropertyInfo(), m2.getPropertyInfo() )
             : m1.getMemberExpression() != null && GosuObjectUtil.equals( m1.getMemberExpression(), m2.getMemberExpression() );
    }
    else if( e1 instanceof MemberAccess && e2 instanceof BeanMethodCallExpression )
    {
      MemberAccess m1 = (MemberAccess)e1;
      BeanMethodCallExpression m2 = (BeanMethodCallExpression)e2;
      return areExpressionsEquivalent( m1.getRootExpression(), m2.getRootExpression() ) &&
             m1.getPropertyInfo() instanceof IJavaPropertyInfo &&
             GosuObjectUtil.equals( ((IJavaPropertyInfo)m1.getPropertyInfo()).getReadMethodInfo(), m2.getMethodDescriptor() );
    }
    else if( e1 instanceof BeanMethodCallExpression && e2 instanceof MemberAccess )
    {
      BeanMethodCallExpression m1 = (BeanMethodCallExpression)e1;
      MemberAccess m2 = (MemberAccess)e2;
      return areExpressionsEquivalent( m1.getRootExpression(), m2.getRootExpression() ) &&
             m2.getPropertyInfo() instanceof IJavaPropertyInfo &&
             GosuObjectUtil.equals( ((IJavaPropertyInfo)m2.getPropertyInfo()).getReadMethodInfo(), m1.getMethodDescriptor() );
    }
    else if( e1 instanceof BeanMethodCallExpression && e2 instanceof BeanMethodCallExpression )
    {
      BeanMethodCallExpression m1 = (BeanMethodCallExpression)e1;
      BeanMethodCallExpression m2 = (BeanMethodCallExpression)e2;
      return areExpressionsEquivalent( m1.getRootExpression(), m2.getRootExpression() ) &&
             m1.getMethodDescriptor() != null &&
             GosuObjectUtil.equals( m1.getMethodDescriptor(), m2.getMethodDescriptor() );
    }
    else if (e1 instanceof ImplicitTypeAsExpression && e2 instanceof ImplicitTypeAsExpression) {
      ImplicitTypeAsExpression i1 = (ImplicitTypeAsExpression) e1;
      ImplicitTypeAsExpression i2 = (ImplicitTypeAsExpression) e2;
      return areExpressionsEquivalent(i1.getLHS(), i2.getLHS()) &&
             GosuObjectUtil.equals(i1.getType(), i2.getType());
    } else {
      return false;
    }
  }

  public void pushLoopCompromised()
  {
    for( TypeAsContext typeAsContext : _inferenceStack )
    {
      for( TypeAsEntry entry : typeAsContext.entries )
      {
        entry.loopCompromised++;
      }
    }
  }

  public void popLoopCompromised()
  {
    for( TypeAsContext typeAsContext : _inferenceStack )
    {
      for( TypeAsEntry entry : typeAsContext.entries )
      {
        entry.loopCompromised--;
        if( entry.loopCompromised == 0 )
        {
          entry.refs.clear();
        }
      }
    }
  }

  public void suspendRefCollection() {
    _refCollectionSuspended = true;
  }

  public void resumeRefCollection() {
    _refCollectionSuspended = false;
  }

  private static class TypeAsContext {
    public Statement stmt;
    public List<TypeAsEntry> entries = new ArrayList<TypeAsEntry>();

    public void merge( TypeAsContext last )
    {
      entries.addAll( last.entries );
    }
  }

  private static class TypeAsEntry {
    public Expression expr;
    public IType originalType;
    public IType inferredType;
    public int loopCompromised;
    public ArrayList<Expression> refs = new ArrayList<Expression>();

    private TypeAsEntry( Expression expr, IType originalType, IType newType )
    {
      this.expr = expr;
      this.originalType = originalType;
      this.inferredType = newType;
    }
  }
}
