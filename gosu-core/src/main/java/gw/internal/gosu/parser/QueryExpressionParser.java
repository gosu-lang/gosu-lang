/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.Registry;
import gw.internal.gosu.parser.expressions.BooleanLiteral;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.QueryExpression;
import gw.internal.gosu.parser.expressions.QueryPathExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.expressions.WhereClauseConditionalAndExpression;
import gw.internal.gosu.parser.expressions.WhereClauseConditionalOrExpression;
import gw.internal.gosu.parser.expressions.WhereClauseEqualityExpression;
import gw.internal.gosu.parser.expressions.WhereClauseExistsExpression;
import gw.internal.gosu.parser.expressions.WhereClauseRelationalExpression;
import gw.internal.gosu.parser.expressions.WhereClauseUnaryExpression;
import gw.internal.gosu.parser.expressions.WhereClauseParenthesizedExpression;
import gw.internal.gosu.parser.expressions.EvalExpression;
import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.lang.parser.ISymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p/>
 * <i>query-expression</i>
 * <b>find</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; [<b>where</b> &lt;where-clause-expression&gt;] <b>)</b>
 * <p/>
 * <i>exists-expression</i>
 * <b>exists</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; <b>where</b> &lt;where-clause-expression&gt; <b>)</b>
 * <p/>
 * <i>where-clause-expression</i>
 * &lt;where-clause-conditional-expression&gt;
 * <p/>
 * <i>where-clause-conditional-expression</i>
 * &lt;where-clause-conditional-or-expression&gt;<BR>
 * <p/>
 * <i>where-clause-conditional-or-expression</i>
 * &lt;where-cluase-conditional-and-expression&gt;<BR>
 * &lt;where-cluase-conditional-or-expression&gt; <b>||</b> &lt;where-cluase-conditional-and-expression&gt;<BR>
 * &lt;where-cluase-conditional-or-expression&gt; <b>or</b> &lt;where-cluase-conditional-and-expression&gt;<BR>
 * <p/>
 * <i>where-clause-conditional-and-expression</i>
 * &lt;where-clause-equality-expression&gt;<BR>
 * &lt;where-clause-conditional-and-expression&gt; <b>&&</b> &lt;where-clause-equality-expression&gt;<BR>
 * &lt;where-clause-conditional-and-expression&gt; <b>and</b> &lt;where-clause-equality-expression&gt;<BR>
 * <p/>
 * <i>where-clause-equality-expression</i>
 * &lt;where-clause-relational-expression&gt;<BR>
 * &lt;where-clause-equality-expression&gt; <b>==</b> &lt;relational-expression&gt;<BR>
 * &lt;where-clause-equality-expression&gt; <b>!=</b> &lt;relational-expression&gt;<BR>
 * &lt;where-clause-equality-expression&gt; <b>&lt;&gt;</b> &lt;relational-expression&gt;<BR>
 * <p/>
 * <i>where-clause-relational-expression</i>
 * &lt;where-clause-unary-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>&lt;</b> &lt;additive-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>&gt;</b> &lt;additive-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>&lt;=</b> &lt;additive-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>&gt;=</b> &lt;additive-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>in</b> &lt;where-clause-in-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>startswith</b> &lt;where-clause-in-expression&gt;<BR>
 * &lt;where-clause-relational-expression&gt; <b>contains</b> &lt;where-clause-in-expression&gt;<BR>
 * <p/>
 * <i>where-clause-in-expression</i>
 * &lt;expression&gt;<BR>
 * <p/>
 * <i>where-clause-unary-expression</i>
 * &lt;where-clause-primary-expression&gt;<BR>
 * <b>!</b> &lt;where-clause-unary-expression&gt;<BR>
 * <b>not</b> &lt;where-clause-unary-expression&gt;<BR>
 * <p/>
 * <i>where-clause-primary-expression</i>
 * &lt;exists-expression&gt;<BR>
 * &lt;query-path-expression&gt;<BR>
 * <b>(</b> &lt;where-clause-expression&gt; <b>)</b><BR>
 * <p/>
 *
 * @deprecated
 */
class QueryExpressionParser extends ParserBase
{
  private QueryExpressionParser _parent;
  private IType _type;
  private QueryPathExpression _ein;
  private QueryPathRootSymbol _symbol;


  QueryExpressionParser( GosuParser owner )
  {
    super( owner );
    _blocks = owner.getOwner()._blocks;    
  }

  protected String getScript()
  {
    return getOwner().getScript();
  }

  QueryExpressionParser( QueryExpressionParser parent )
  {
    super( parent.getOwner() );
    _parent = parent;
  }

  IType getEntityType()
  {
    return _type;
  }

  QueryExpressionParser getParent()
  {
    return _parent;
  }

  boolean parse( Token T )
  {
    if( !match( T, getParent() != null ? Keyword.KW_exists : Keyword.KW_find ) )
    {
      return false;
    }

    QueryExpression qe = getParent() == null
                         ? new QueryExpression()
                         : new WhereClauseExistsExpression();
    List<ICapturedSymbol> captured = new ArrayList<ICapturedSymbol>();
    captureAllSymbols( null, getCurrentEnclosingGosuClass(), captured );
    qe.setCapturedSymbolsForBytecode( captured );
    qe.setCapturedTypeVars( new HashMap<String, ITypeVariableDefinition>( getOwner().getTypeVariables() ) );

    // Distinct is only allowed for the root level find clause.  I.e. no parent.
    if( getParent() == null && match( null, "distinct" ) )
    {
      qe.setDistinct( true );
    }

    verify( qe, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_FIND );
    match( null, Keyword.KW_var );
    int iNamedOffset = getTokenizer().getTokenStart();
    if( verify( qe, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_FIND ) )
    {
      qe.setNameOffset( iNamedOffset, (String)T._strValue );
    }

    String strIdentifier = T._strValue;

    verify( qe, match( null, Keyword.KW_in ), Res.MSG_EXPECTING_IN_FIND );

    parseQueryPathExpression();
    _ein = (QueryPathExpression)popExpression();
    _type = _ein.getRootType();

    getSymbolTable().pushScope();
    try
    {
      _symbol = new QueryPathRootSymbol( strIdentifier, _ein.getType(), null );
      // getSymbolTable().putSymbol( _symbol ); Don't put this symbol here, it's pushed only when we parse a lhs expression in where-clause

      Expression whereExpression = null;
      if( match( null, Keyword.KW_where ) )
      {
        parseWhereClauseExpression();
        whereExpression = popExpression();
      }

      verify( qe, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_FIND );

      qe.setIdentifier( strIdentifier );
      qe.setEntityType( _type );
      if( _type instanceof ErrorType )
      {
        // sct: if an ErrorType, set the type on the QueryExpression to prevent ClassCastExceptions
        qe.setType( _type );
      }
      // The QueryExpression type is always a List now because an array type
      // requires we load all the results into memory which could get ugly:
      // qe.setType( new ArrayType( BeanAccess.sharedInstance().getType( getEntityType() ) ) );
      qe.setInExpression( _ein );
      qe.setWhereClauseExpression( whereExpression );

      pushExpression( qe );
    }
    finally
    {
      getSymbolTable().popScope();
    }

    return true;
  }

  private void parseWhereClauseExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    parseWhereClauseConditionalExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void parseWhereClauseConditionalExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseConditionalExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseConditionalExpression()
  {
    // <conditional-or-expression>
    parseWhereClauseConditionalOrExpression();
  }

  void parseWhereClauseConditionalOrExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseConditionalOrExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseConditionalOrExpression()
  {
    parseWhereClauseConditionalAndExpression();

    // <conditional-or-expression2>
    do
    {
      Token T = new Token();

      if( match( T, "||", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( null, Keyword.KW_or ) )
      {
        parseWhereClauseConditionalAndExpression();

        WhereClauseConditionalOrExpression e = new WhereClauseConditionalOrExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        e.setLHS( lhs );
        e.setRHS( rhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
    }
    while( true );
  }

  void parseWhereClauseConditionalAndExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseConditionalAndExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseConditionalAndExpression()
  {
    parseWhereClauseEqualityExpression();

    // <conditional-and-expression2>
    do
    {
      Token T = new Token();

      if( match( T, "&&", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( null, Keyword.KW_and ) )
      {
        parseWhereClauseEqualityExpression();

        WhereClauseConditionalAndExpression e = new WhereClauseConditionalAndExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        e.setLHS( lhs );
        e.setRHS( rhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
    }
    while( true );
  }

  void parseWhereClauseEqualityExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseEqualityExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseEqualityExpression()
  {
    parseWhereClauseRelationalExpression();

    // <relational-expression2>
    do
    {
      Token T = new Token();
      if( match( T, "==", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( T, "!=", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( T, "<>", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        Expression lhs = popExpression();
        WhereClauseEqualityExpression e = new WhereClauseEqualityExpression();
        verify( e, lhs instanceof QueryPathExpression, Res.MSG_EXPECTING_QUERY_PATH );
        IType type = lhs.getType();
        getOwner().pushInferredContextTypes( new ContextType( type ) );
        try
        {
          getOwner().parseRelationalExpression();
        }
        finally
        {
          getOwner().popInferredContextTypes();
        }

        Expression rhs = popExpression();
        verifyComparable( lhs.getType(), rhs, true, true );
        verifyPropertyVisible( lhs );
        e.setLHS( lhs );
        e.setRHS( wrapInEvalExpression( rhs ) );
        e.setEquals( T._strValue.equals( "==" ) );
        pushExpression( e );
      }
      else
      {
        Expression lhs = peekExpression();
        if( lhs instanceof QueryPathExpression )
        {
          verify( lhs, lhs.getType() == JavaTypes.BOOLEAN() || lhs.getType() == JavaTypes.pBOOLEAN(), Res.MSG_QUERY_EXPECTED_BOOLEAN_EXPRESSION, lhs.toString() );
          QueryPathExpression qpe = (QueryPathExpression)lhs;
          if( !qpe.hasParseExceptions() && !isDbProperty( qpe ) )
          {
            qpe.getDelegate().addParseException( Res.MSG_PROPERTY_NOT_VISIBLE, ((MemberAccess)qpe.getDelegate()).getMemberName() );
          }
          lhs = popExpression();
          WhereClauseEqualityExpression e = new WhereClauseEqualityExpression();
          e.setLHS( lhs );
          e.setRHS( BooleanLiteral.TRUE.get() );
          e.setEquals( true );
          pushExpression( e );
        }
        // else the <null> case
        break;
      }
    }
    while( true );
  }

  private void verifyPropertyVisible( Expression lhs )
  {
    if( lhs instanceof QueryPathExpression && !lhs.hasParseExceptions() && !(lhs.getType() instanceof ErrorType) &&
        !isDbProperty( (QueryPathExpression)lhs ) ) {

      ((QueryPathExpression)lhs).getDelegate().addParseException( Res.MSG_PROPERTY_NOT_VISIBLE, ((QueryPathExpression)lhs).getMemberName() );
    }
  }

  void parseWhereClauseRelationalExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseRelationalExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseRelationalExpression()
  {
    // <unary-expression>
    parseWhereClauseUnaryExpression();

    // <relational-expression2>
    do
    {
      Token T = new Token();

      if( match( T, "<", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( T, ">", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( T, "<=", SourceCodeTokenizer.TT_OPERATOR ) ||
          match( T, Keyword.KW_in ) ||
          match( T, Keyword.KW_startswith ) ||
          match( T, Keyword.KW_contains ) )
      {
        if( T._strValue.equals( ">" ) && match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          T._strValue = ">=";
        }

        Expression lhs = popExpression();
        WhereClauseRelationalExpression e = new WhereClauseRelationalExpression();
        verify( e, lhs instanceof QueryPathExpression, Res.MSG_EXPECTING_QUERY_PATH );
        getOwner().parseAdditiveExpression();

        Expression rhs = popExpression();
        verifyPropertyVisible( lhs );
        if( T._strValue.equals( Keyword.KW_in.toString() ) )
        {
          limitInOperandToArrayOrQuery( lhs, rhs );
        }
        else
        {
          verifyComparable( lhs.getType(), rhs, true, true );
        }
        e.setLHS( lhs );
        e.setRHS( wrapInEvalExpression( rhs ) );
        e.setOperator( T._strValue );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
    }
    while( true );
  }

  private Expression wrapInEvalExpression( Expression rhs )
  {
    if( rhs instanceof ILiteralExpression )
    {
      return rhs;
    }

    EvalExpression evalExpr = new EvalExpression( getOwner().getTypeUsesMap().copy() );
    List<ICapturedSymbol> captured = new ArrayList<ICapturedSymbol>();
    captureAllSymbols( null, getCurrentEnclosingGosuClass(), captured );
    evalExpr.setCapturedSymbolsForBytecode( captured );
    evalExpr.setCapturedTypeVars( new HashMap<String, ITypeVariableDefinition>( getOwner().getTypeVariables() ) );
    evalExpr.setExpression( new StringLiteral( rhs.toString() ) );
    getOwner().findAndWrapLocation( rhs, evalExpr );
    return possiblyWrapWithCoercion( evalExpr, rhs.getType(), false );
  }

  private void limitInOperandToArrayOrQuery( Expression lhs, Expression rhs )
  {
    if( rhs instanceof QueryExpression )
    {
      verify( lhs, CommonServices.getEntityAccess().isEntityClass( lhs.getType() ),
              Res.MSG_QUERY_IN_LHS_OP_NOT_ENTITY, lhs.toString() );
    }

    IType intrLhs = lhs.getType();
    IType qrsType = JavaTypes.IQUERY_RESULT_SET();
    IType componentType = null;
    if( qrsType.isAssignableFrom( rhs.getType() ))
    {
      for (IType iType : rhs.getType().getAllTypesInHierarchy()) {
        IType genericType = iType.getGenericType();
        if (genericType != null && genericType.equals(qrsType) && iType.isParameterizedType()) {
          componentType = iType.getTypeParameters()[0];
        }
      }
      verifyTypesComparable( lhs, intrLhs, componentType == null ? ErrorType.getInstance() : componentType, false, true );
    }
    else
    {
      intrLhs = intrLhs.isArray() ? intrLhs : intrLhs.getArrayType();
      if( intrLhs == null )
      {
        verify( lhs, false, Res.MSG_QUERY_IN_LHS_OP_NOT_ARRAY, lhs.getType().toString() );
      }
      else
      {
        verifyComparable(intrLhs, rhs, true );
      }
    }
  }

  void parseWhereClauseUnaryExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseWhereClauseUnaryExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseWhereClauseUnaryExpression()
  {
    Token T = new Token();

    if( match( T, "!", SourceCodeTokenizer.TT_OPERATOR ) ||
        match( T, Keyword.KW_not ) )
    {
      parseWhereClauseUnaryExpression();

      WhereClauseUnaryExpression ue = new WhereClauseUnaryExpression();
      Expression e = popExpression();
      ue.setExpression( e );
      ue.setType( e.getType() );
      pushExpression( ue );
    }
    else
    {
      parsePrimaryExpression();
    }
  }

  void parsePrimaryExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parsePrimaryExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parsePrimaryExpression()
  {
    Token T = new Token();

    // Parenthesized Expression
    if( match( T, '(' ) )
    {
      parseWhereClauseExpression();

      Expression e = popExpression();
      WhereClauseParenthesizedExpression expr = new WhereClauseParenthesizedExpression( e );
      pushExpression( expr );

      verify( e, match( null, ')' ), Res.MSG_EXPECTING_EXPRESSION_CLOSE );

      return;
    }

    // A nested 'exists' query
    QueryExpressionParser queryParser = new QueryExpressionParser( this );
    if( !queryParser.parse( T ) )
    {
      // A query path
      parseQueryPathExpression();
    }
  }

  void parseQueryPathExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseQueryPathExpression();
    setLocation( iOffset, iLineNum, iColumn, true );
  }

  private void _parseQueryPathExpression()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    Token T = new Token();
    QueryPathExpression e = new QueryPathExpression();

    // Peek ahead to get the first element in the query path. If this is the in-epxr,
    // we're expecting an entity name. We'll create a symbol with this name and type
    // so that we can parse a MemberAccess. Otherwise, if this is not an in-expr, in
    // which case it must be a lhs expression in a where clause, we don't need to create
    // the symbol because it should be there from the in-expr case.

    if( !verify( e, match( T, null, SourceCodeTokenizer.TT_WORD, true ), Res.MSG_EXPECTING_QUERY_PATH ) )
    {
      T._strValue = null;
    }
    String strRoot = T._strValue == null ? "" : T._strValue;

    ISymbol root;
    IType type;
    TypeLiteral typeLit;
    if( _ein != null )
    {
      getSymbolTable().pushScope();
      try
      {
        getSymbolTable().putSymbol( _symbol );

        root = resolveSymbol( e, strRoot, true );
        verify( e, root == _symbol, Res.MSG_QUERYPATH_MUST_BEGIN_WITH, _symbol.getName() );
        type = root == null ? ErrorType.getInstance( strRoot ) : root.getType();
      }
      finally
      {
        getSymbolTable().popScope();
      }
    }
    else
    {
      try
      {
        IType intrType = TypeLoaderAccess.instance().getByRelativeName( strRoot, CommonServices.getEntityAccess().getDefaultTypeUses() );
        verify( e, intrType != null && CommonServices.getEntityAccess().isEntityClass( intrType ), Res.MSG_EXPECTING_ENTITY_TYPE );
        if( !Registry.instance().isAllowEntityQueires() )
        {
          verify( e, intrType != null && !intrType.isMutable(), Res.MSG_EXPECTING_READONLY_ENTITY_TYPE );
        }
        type = intrType;
        root = new QueryPathRootSymbol( strRoot, type, null );
      }
      catch( ClassNotFoundException cnfe )
      {
        //noinspection ThrowableInstanceNeverThrown
        e.addParseException( new ParseException( makeFullParserState(), Res.MSG_EXPECTING_QUERY_PATH ) );
        type = ErrorType.getInstance( strRoot );
        root = new QueryPathRootSymbol( strRoot, type, null );
      }
    }

    getSymbolTable().pushScope();
    try
    {
      // Create a temporary symbol for the root of the expression so intellisense can handle this as an entity ref and not a type literal
      getSymbolTable().putSymbol( root );

      IType exprType = type;
      getOwner().parsePrimaryExpression();
      Expression expr = popExpression();

      if( _ein == null )
      {
        // Note we must make a type-literal-expr here so that the parse tree is consistent e.g.,for the typeinfo db
        Token typeLiteralToken = new Token();
        typeLiteralToken._strValue = strRoot;
        typeLit = getOwner().resolveTypeLiteral( typeLiteralToken );
        pushExpression( typeLit );
        setLocation( iOffset, iLineNum, iColumn, true );
        typeLit.getLocation().setLength( strRoot.length() );
        popExpression();
      }

      if( expr instanceof MemberAccess  )
      {
        MemberAccess ma = (MemberAccess)expr;
        IPropertyInfo pi;
        try
        {
          pi = ma.getPropertyInfo();
          if( pi instanceof ArrayExpansionPropertyInfo )
          {
            pi = ((ArrayExpansionPropertyInfo)pi).getDelegate();
          }
          if( _ein == null )
          {
            verify( e, CommonServices.getEntityAccess().isEntityClass( pi.getFeatureType() ), Res.MSG_EXPECTING_ENTITY_TYPE );
          }
          else
          {
//            verify( e, isDbProperty( pi ), Res.MSG_PROPERTY_NOT_VISIBLE, pi.getName() );
            verify( e, ma.getRootExpression() instanceof Identifier, Res.MSG_CANNOT_DEREF_PROPERTIES_IN_WHERE );
          }
          exprType = pi.getFeatureType();
        }
        catch( RuntimeException re )
        {
          // ignore
        }
      }
      if( exprType == null )
      {
        exprType = ErrorType.getInstance();
      }

      if( _ein == null )
      {
        verify( e, (getParent() == null) || (expr != null && expr.toString().toLowerCase().startsWith( getParent()._ein.toString().toLowerCase() )),
                Res.MSG_QUERYPATH_MUST_BEGIN_WITH,
                getParent() == null ? null : getParent()._ein.toString() );
      }

      e.setType(exprType);
      e.setDelegate( expr );

      pushExpression( e );
    }
    finally
    {
      getSymbolTable().popScope();
    }
  }

  private boolean isDbProperty( QueryPathExpression qpe )
  {
    try
    {
      return CommonServices.getEntityAccess().getQueryExpressionFeatureFilter().acceptFeature( qpe.getRootType(), qpe.getPropertyInfo() );
    }
    catch( Exception e )
    {
      return false;
    }
  }
}
