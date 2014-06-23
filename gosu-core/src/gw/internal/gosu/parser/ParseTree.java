/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/**
 */
package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IType;
import gw.util.DynamicArray;
import gw.util.GosuObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Intended to specify the location of a parsed element within the source.
 */
public final class ParseTree implements IParseTree
{
  private static final DynamicArray<ParseTree> EMPTY_PARSE_TREE_LIST = new DynamicArray<ParseTree>(0);

  private transient ParsedElement _pe;
  private DynamicArray<ParseTree> _children;
  private int _iOffset;
  private int _iLength;
  private transient IScriptPartId _scriptPart;

  ParseTree( ParsedElement pe, int iOffset, int iLength, IScriptPartId scriptPart )
  {
    _pe = pe;
    _iOffset = iOffset;
    _iLength = iLength;
    _scriptPart = scriptPart;
    _children = EMPTY_PARSE_TREE_LIST;
  }

  public IType getEnclosingType()
  {
    return _scriptPart == null ? null : _scriptPart.getContainingType();
  }

  public IScriptPartId getScriptPartId()
  {
    return _scriptPart;
  }

  /**
   * @return The zero-based offset of the parsed element within the source.
   */
  public int getOffset()
  {
    return _iOffset;
  }

  /**
   * @return The length of the parsed element in the source.
   */
  public int getLength()
  {
    return _iLength;
  }

  public void setLength( int iLength )
  {
    _iLength = iLength;
  }

  /**
   * @return The one based line number of the beginning of the parsed element
   */
  public int getLineNum()
  {
    return _pe.getLineNum();
  }

  /**
   * @return The offset from the beginning of the line where the parsed element starts
   */
  public int getColumn()
  {
    return _pe.getColumn();
  }

  /**
   * @return The parsed element to which this location corresponds.
   */
  public ParsedElement getParsedElement()
  {
    return _pe;
  }

  /**
   * @return The most distant position this location occupies i.e., offset + length - 1.
   */
  public int getExtent()
  {
    return getOffset() + getLength() - 1;
  }

  /**
   * @param iPosition Any position within the source.
   *
   * @return True if this location contains the position.
   */
  public boolean contains( int iPosition )
  {
    return contains( iPosition, iPosition );
  }

  /**
   * @param l A location to check.
   *
   * @return True if the space occupied by this location is a superset of the
   *         space occupied by the specified location.
   */
  public boolean contains( IParseTree l )
  {
    return contains( l.getOffset(), l.getExtent() );
  }

  private boolean contains( int start, int end )
  {
    return start >= getOffset() &&
           end <= getExtent();
  }

  public boolean containsOrBorders( int iPosition, boolean strict )
  {
    return containsOrBorders( iPosition, iPosition, strict );
  }

  public boolean containsOrBorders( IParseTree l, boolean strict )
  {
    return containsOrBorders( l.getOffset(), l.getExtent(), strict );
  }

  private boolean containsOrBorders( int start, int end, boolean strict )
  {
    int rightBounds;
    if( strict )
    {
      rightBounds = getOffset() + getLength();
    }
    else
    {
      ParseTree nextSibling = getNextSibling();
      if( nextSibling != null )
      {
        rightBounds = nextSibling.getOffset() - 1;
      }
      else
      {
        rightBounds = getOffset() + getLength();
      }
    }
    return start >= getOffset() &&
           end <= rightBounds;
  }

  public ParseTree getDeepestLocation( boolean statementsOnly, int iStart, int iEnd, boolean strict )
  {
    if( !containsOrBorders( iStart, iEnd, strict ) )
    {
      return null;
    }

    ParseTree deepest = null;
    if(!statementsOnly || (_pe instanceof IStatement)) {
      deepest = this;
    }

    if( _children != null )
    {
      for(int i = 0; i < _children.size; i++)
      {
        ParseTree child = (ParseTree) _children.data[i];
        ParseTree l = child.getDeepestLocation( statementsOnly, iStart, iEnd, strict );
        if( Search.isDeeper( deepest, l ) )
        {
          deepest = l;
        }
      }
    }

    Statement embededStmt = (Statement)Search.getHiddenStatement( getParsedElement() );
    if( embededStmt != null && embededStmt.getLocation() != null )
    {
      ParseTree l = embededStmt.getLocation().getDeepestLocation( statementsOnly, iStart, iEnd, strict );
      if( Search.isDeeper( deepest, l ) )
      {
        deepest = l;
      }
    }

    return deepest;
  }

  public boolean isAncestorOf( IParseTree l )
  {
    while( l != null && l.getParent() != l )
    {
      if( (l = l.getParent()) == this )
      {
        return true;
      }
    }
    return false;
  }

  private ParseTree getDeepestLocation( boolean statementsOnly, int iPosition, boolean strict )
  {
    return getDeepestLocation( statementsOnly, iPosition, iPosition, strict );
  }

  /**
   * @param iPosition The location to check.
   * @param strict    Whether to match strictly or accept white spaces to the right
   *
   * @return The deepest descendent location containing the specified location.
   */
  public ParseTree getDeepestLocation( int iPosition, boolean strict )
  {
    return getDeepestLocation( iPosition, iPosition, strict );
  }

  /**
   * @param iStart The start of the segment (inclusive)
   * @param iEnd   The end of the segment (inclusive)
   * @param strict Whether to match strictly or accept white spaces to the right
   *
   * @return The deepest descendent location containing the segment.
   */
  public ParseTree getDeepestLocation( int iStart, int iEnd, boolean strict )
  {
    return getDeepestLocation( false, iStart, iEnd, strict );
  }

  /**
   * @param iPosition The location to check.
   * @param strict    Whether to match strictly or accept white spaces to the right
   *
   * @return The deepest descendent statement location containing the specified location.
   */
  public ParseTree getDeepestStatementLocation( int iPosition, boolean strict )
  {
    return getDeepestLocation( true, iPosition, strict );
  }

  /**
   * @param iLineNum The one based line number to check.
   * @param clsSkip  A statement sublcass to ignore. Optional.
   *
   * @return The first statement beginning at the specified line number, or null
   *         if no statements start at the line number.
   */
  public ParseTree getStatementAtLine( int iLineNum, Class clsSkip )
  {
    if( _pe instanceof NewExpression )
    {
      NewExpression newExpression = (NewExpression)_pe;
      IType type = newExpression.getType();
      if( type instanceof IGosuClassInternal && newExpression.isAnonymousClass() )
      {
        return (ParseTree)((IGosuClassInternal)type).getClassStatement().getLocation().getStatementAtLine( iLineNum, clsSkip );
      }
    }

    if( (_pe instanceof Statement) && // Note we must analyze expressions e.g., block expressions can have statements
        !(_pe instanceof FunctionStatement && ((FunctionStatement)_pe).getDynamicFunctionSymbol() instanceof ProgramClassFunctionSymbol) &&
        (getLineNum() == iLineNum) &&
        (clsSkip == null || !clsSkip.isAssignableFrom( _pe.getClass() )) )
    {
      return this;
    }

    ParseTree deepest = null;
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      ParseTree l = child.getStatementAtLine( iLineNum, clsSkip );
      if( Search.isDeeper( deepest, l ) )
      {
        deepest = l;
      }
    }

    return deepest;
  }

  /**
   * Adds a child location to this location. Note the location must cover only
   * a subset of this locations area.
   *
   * @param l The location to add.
   */
  public void addChild( IParseTree l )
  {
    addChild( -1, l );
  }
  public void addChild( int iIndex, IParseTree l )
  {
    if( l == null )
    {
      throw new NullPointerException( "Attempted to add a Null child location." );
    }

    if( !contains( l ) && l.getLength() > 0 )
    {
      throw new IllegalArgumentException( "Attempted to add a child location whose bounds extend beyond the containing location: " +
                                          "(" + getOffset() + ", " + getExtent() + ") does not contain (" + l.getOffset() + ", " + l.getExtent() + ")" );
    }
    if( _children == EMPTY_PARSE_TREE_LIST )
    {
      _children = new DynamicArray<ParseTree>( 2 );
    }

    l.setParent( this );
    if( iIndex >= 0 )
    {
      _children.add( iIndex, (ParseTree)l );
    }
    else
    {
      _children.add( (ParseTree)l );
    }
  }

  public void removeChild( IParseTree l )
  {
    if( (l != null) && (_children != null) )
    {
      _children.remove( l );
      l.setParent( null );
    }
  }

  /**
   * @return The list of child locations covered by this location.
   */
  public List<IParseTree> getChildren()
  {
    return (List<IParseTree>) (_children == null ? Collections.<IParseTree>emptyList() : Collections.unmodifiableList( _children ));
  }

  public int getChildCount()
  {
    return _children == null ? 0 : _children.size;
  }

  /**
   * Sets the parent location. Note the parent location must cover a superset of the
   * specified location's area.
   *
   * @param l The parent location.
   */
  public void setParent( IParseTree l )
  {
    if( l != null && !l.contains( this ) && getLength() > 0 )
    {
      throw new IllegalArgumentException( "Attempted set the parent location, but the parent location's area is not a superset of this location's area." );
    }
    if( _pe != null )
    {
      ParsedElement parentElement = (ParsedElement)_pe.getParent();
      if( parentElement != null )
      {
        ParseTree oldParent = parentElement.getLocation();
        if( oldParent != null )
        {
          oldParent._children.remove( this );
        }
      }
      _pe.setParent( l == null ? null : ((ParseTree)l)._pe );
    }
  }

  /**
   * @return This location's parent location. Note the parent covers a superset of the
   *         this location's area.
   */
  public IParseTree getParent()
  {
    return _pe != null && _pe.getParent() != null ? _pe.getParent().getLocation() : null;
  }

  /**
   * Like getParent, but won't infinitely recurse if the parent turns out to be equal to this, which can happen
   * when the expression in question is a program (since the outer program has the same location as the main statement).
   */
  public IParseTree getParentOtherThanThis()
  {
    IParseTree parent = getParent();
    return (parent == this ? null : parent);
  }

  /**
   * Is just the physical location equal?
   *
   * @param location Location to check
   *
   * @return True if the given physical location's offset and extents are equal to this one's
   */
  public boolean areOffsetAndExtentEqual( IParseTree location )
  {
    return location != null &&
           location.getOffset() == getOffset() &&
           location.getExtent() == getExtent();
  }

  public String toString()
  {
    return "Offset: " + getOffset() + "\n" +
           "Length: " + getLength() + "\n" +
           (_pe != null ? _pe.getClass().getSimpleName() : "ParsedElement") + ": " + _pe;
  }

  public void initLocation( ParsedElement pe, int iOffset, int iLength )
  {
    _pe = _pe == null ? pe : _pe;
    _iOffset = iOffset;
    _iLength = iLength;
  }


  public void compactParseTree() {
    if( _children != null )
    {
      for(int i = 0; i < _children.size; i++)
      {
        ParseTree child = (ParseTree) _children.data[i];
        child.compactParseTree();
      }
      _children.trimToSize();
    }
  }

  public void clearParseTreeInformation()
  {
    // Don't clear block expressions.  Yuck!!!!!
    if (getParsedElement() == null || getParsedElement().shouldClearParseInfo())
    {
      if( _children != null )
      {
        for(int i = 0; i < _children.size; i++)
        {
          ParseTree child = (ParseTree) _children.data[i];
          child.clearParseTreeInformation();
        }
      }
      _children = EMPTY_PARSE_TREE_LIST;
      if( _pe != null )
      {
        _pe.setLocation( null );
      }
      _pe = null;
    }
  }

  public boolean areAllChildrenAfterPosition( int caret )
  {
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      if( child.getOffset() < caret )
      {
        return false;
      }
    }
    return true;
  }

  public List<IParseTree> getDominatingLocationList()
  {
    ArrayList<IParseTree> dominatingLocations = new ArrayList<IParseTree>();
    if( getParent() != null )
    {
      List<IParseTree> parentDominators = getParent().getDominatingLocationList();
      dominatingLocations.addAll( parentDominators );
      dominatingLocations.add( getParent() );
      dominatingLocations.addAll( getParent().getChildrenBefore( this ) );
    }
    return dominatingLocations;
  }

  public List<IParseTree> getChildrenBefore( IParseTree parseTree )
  {
    ArrayList<IParseTree> childrenBefore = new ArrayList<IParseTree>();
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      if( child.getOffset() < parseTree.getOffset() )
      {
        childrenBefore.add( child );
      }
    }
    return childrenBefore;
  }

  public boolean isSiblingOf( IParseTree deepestAtEnd )
  {
    return getParent() != null && GosuObjectUtil.equals( getParent(), deepestAtEnd.getParent() );
  }

  public ParseTree getChildAfter( int point )
  {
    int minDistance = Integer.MAX_VALUE;
    ParseTree closestTrailingChild = null;
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      int dist = child.getOffset() - point;
      if( dist > 0 && dist < minDistance )
      {
        minDistance = dist;
        closestTrailingChild = child;
      }
    }
    return closestTrailingChild;
  }

  public ParseTree getChildBefore( int point )
  {
    int minDistance = Integer.MAX_VALUE;
    ParseTree closestTrailingChild = null;
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      int dist = point - child.getOffset();
      if( dist > 0 && dist < minDistance )
      {
        minDistance = dist;
        closestTrailingChild = child;
      }
    }
    return closestTrailingChild;
  }

  public ParseTree getChildBefore( IParseTree child )
  {
    return getChildBefore( child.getOffset() );
  }

  public ParseTree getChildAfter( IParseTree child )
  {
    return getChildAfter( child.getExtent() );
  }

  public ParseTree getFirstChildWithParsedElementType( Class<? extends IParsedElement> aClass )
  {
    ParseTree returnChild = null;
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      if( aClass.isInstance( child.getParsedElement() ) && (returnChild == null || child.getOffset() < returnChild.getOffset()) )
      {
        returnChild = child;
      }
    }
    return returnChild;
  }

  public ParseTree getLastChildWithParsedElementType( Class<? extends IParsedElement> aClass )
  {
    ParseTree returnChild = null;
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      if( aClass.isInstance( child.getParsedElement() ) && (returnChild == null || child.getOffset() > returnChild.getOffset()) )
      {
        returnChild = child;
      }
    }
    return returnChild;
  }

  public ParseTree getLastChild()
  {
    return getChildBefore( getExtent() );
  }

  public ParseTree getNextSibling()
  {
    IParseTree parent = getParent();
    if( parent == null || parent == this )
    {
      return null;
    }
    return (ParseTree)getParent().getChildAfter( this );
  }

  public ParseTree getPreviousSibling()
  {
    if( getParent() == null )
    {
      return null;
    }
    return (ParseTree)getParent().getChildBefore( this );
  }

  public ParseTree getDeepestFirstChild()
  {
    ParseTree parseTree = this;
    while( parseTree.getFirstChildWithParsedElementType( ParsedElement.class ) != null )
    {
      parseTree = parseTree.getFirstChildWithParsedElementType( ParsedElement.class );
    }
    return parseTree;
  }

  public Collection<IParseTree> findDescendantsWithParsedElementType( Class type )
  {
    ArrayList<IParseTree> matches = new ArrayList<IParseTree>();
    findDescendantsWithParsedElementType( matches, type );
    return matches;
  }

  public void addUnder( IParseTree parent )
  {
    int offset = parent.getOffset();
    int lineNumOffset = parent.getParsedElement().getLineNum() - 1;
    int columnOffset = parent.getParsedElement().getColumn();
    adjustOffset( offset, lineNumOffset, columnOffset );
    setParent( parent );
    _scriptPart = parent.getScriptPartId();
    parent.addChild( this );
  }

  void adjustOffset( int offset, int lineNumOffset, int columnOffset )
  {
    if( getLineNum() > 1 )
    {
      columnOffset = 0;
    }
    recursivelyAdjustOffset( offset, lineNumOffset, columnOffset );
    if( _pe != null )
    {
      for( IParseIssue parseIssue : _pe.getParseIssues() )
      {
        ((ParseIssue)parseIssue).adjustOffset( offset, lineNumOffset, columnOffset );
      }
    }
  }

  private void recursivelyAdjustOffset( int offset, int lineNumOffset, int columnOffset )
  {
    if( getLineNum() > 1 )
    {
      columnOffset = 0;
    }
    _iOffset += offset;
    if( _pe != null )
    {
      _pe.adjustLineNum( lineNumOffset );
      _pe.adjustColumn( columnOffset );
    }
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      child.recursivelyAdjustOffset( offset, lineNumOffset, columnOffset );
    }
    if( _pe instanceof IMemberAccessExpression )
    {
      IMemberAccessExpression mae = (IMemberAccessExpression)_pe;
      mae.setStartOffset( mae.getStartOffset() + offset );
    }
    if( _pe instanceof IParsedElementWithAtLeastOneDeclaration )
    {
      IParsedElementWithAtLeastOneDeclaration peo = (IParsedElementWithAtLeastOneDeclaration) _pe;
      for( String name : peo.getDeclarations() )
      {
        String charSeq = (String)name;
        peo.setNameOffset( peo.getNameOffset( charSeq ) + offset, charSeq );
      }
    }
  }

  private void findDescendantsWithParsedElementType( ArrayList<IParseTree> matches, Class type )
  {
    if( type.isAssignableFrom( _pe.getClass() ) )
    {
      matches.add( this );
    }
    for(int i = 0; i < _children.size; i++)
    {
      ParseTree child = (ParseTree) _children.data[i];
      child.findDescendantsWithParsedElementType( matches, type );
    }
  }

  public IFunctionStatement getEnclosingFunctionStatement()
  {
    for( ParseTree csr = this; csr != null; csr = (ParseTree)csr.getParentOtherThanThis() )
    {
      ParsedElement pe = csr.getParsedElement();
      if( pe instanceof IFunctionStatement )
      {
        return (IFunctionStatement)pe;
      }
    }
    return null;
  }

  public final IParseTree getMatchingElement(int iStart, int iLength) {
    if (_iOffset == iStart && _iLength == iLength) {
      return this;
    }

    if (_children != null) {
      for (int i = 0; i < _children.size; i++) {
        ParseTree child = (ParseTree) _children.data[i];
        IParseTree tree = child.getMatchingElement(iStart, iLength);
        if (tree != null) {
          return tree;
        }
      }
    }

    return null;
  }

  public String getTreeOutline()
  {
    return getTreeOutline( "" );
  }
  private String getTreeOutline( String strIndent )
  {
    List<IToken> tokens = getParsedElement().getTokens();
    StringBuilder source = new StringBuilder();
    source.append( "\n" + strIndent + "- <" + getParsedElement().getClass().getSimpleName() + ">  Offset: " + getOffset() + " Extent: " + getExtent() );
    strIndent = strIndent + "  ";
    StringBuilder tokenContent = new StringBuilder();
    appendTokensForOutline( null, tokens, tokenContent );
    if( tokenContent.length() > 0 )
    {
      source.append( "\n" + strIndent + "- " );
      source.append( tokenContent );
    }
    for( IParseTree child : getChildrenSorted( ) )
    {
      source.append( ((ParseTree)child).getTreeOutline( strIndent ) );
      tokenContent = new StringBuilder();
      appendTokensForOutline( (ParseTree)child, tokens, tokenContent );
      if( tokenContent.length() > 0 )
      {
        source.append( "\n" + strIndent + "- " );
        source.append( tokenContent );
      }
    }
    return source.toString();
  }

  private void appendTokensForOutline( ParseTree child, List<IToken> tokens, StringBuilder source )
  {
    StringBuilder sbTokens = new StringBuilder();
    addTokens( child, tokens, sbTokens );
    source.append( sbTokens.toString().replace( '\n', '\u014A' ).replace( '\r', '\u019D' ).replace( ' ', '\u1D13' ) );
  }

  public String getTextFromTokens()
  {
    List<IToken> tokens = getParsedElement().getTokens();
    StringBuilder source = new StringBuilder();
    addTokens( null, tokens, source );
    for( IParseTree child : getChildrenSorted( ) )
    {
      source.append( child.getTextFromTokens() );
      addTokens( (ParseTree)child, tokens, source );
    }
    return source.toString();
  }

  private void addTokens( ParseTree after, List<IToken> tokens, StringBuilder source )
  {
    for( IToken t : tokens )
    {
      if( t.getAfter() == after || (after != null && after.isAncestor( t.getAfter() )) )
      {
        if( t.getType() == ISourceCodeTokenizer.TT_EOF )
        {
          // Skip EOF
          continue;
        }
        source.append( t.getText() );
      }
    }
  }

  public List<IParseTree> getChildrenSorted( )
  {
    List<IParseTree> children = new ArrayList<IParseTree>( _children );
    Collections.sort( children,
      new Comparator<IParseTree>()
      {
        public int compare( IParseTree o1, IParseTree o2 )
        {
          return o1.getOffset() - o2.getOffset();
        }
      } );
    return children;
  }

  public boolean isAncestor(IParseTree child) {
    if (child == null) {
      return false;
    }

    if (this == child) {
      return true;
    }

    if (child == child.getParent()) {
      return false;
    }

    return isAncestor(child.getParent());
  }
}
