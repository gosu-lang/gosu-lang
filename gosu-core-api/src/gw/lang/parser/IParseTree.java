/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.statements.IStatementList;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IParseTree extends Serializable
{
  IType getEnclosingType();

  IScriptPartId getScriptPartId();

  int getOffset();

  int getLength();

  void setLength( int iLength );

  int getLineNum();

  int getColumn();

  IParsedElement getParsedElement();

  int getExtent();

  boolean isAncestorOf( IParseTree l );

  boolean contains( int iPosition );

  boolean contains( IParseTree l );

  boolean containsOrBorders( int iPosition, boolean strict );

  boolean containsOrBorders( IParseTree l, boolean strict );

  IParseTree getDeepestLocation( int iPosition, boolean strict );

  IParseTree getDeepestLocation( int iStart, int iEnd, boolean strict );

  IParseTree getDeepestLocation( boolean statementsOnly, int iStart, int iEnd, boolean strict );

  IParseTree getDeepestStatementLocation( int iPosition, boolean strict );

  IParseTree getStatementAtLine( int iLineNum, Class clsSkip );

  void addChild( IParseTree l );

  void removeChild( IParseTree l );

  List<IParseTree> getChildren();

  IParseTree getParent();
  IParseTree getParentOtherThanThis();
  void setParent( IParseTree parent );

  boolean areOffsetAndExtentEqual( IParseTree location );

  String toString();

  String getTextFromTokens();

  void clearParseTreeInformation();

  boolean areAllChildrenAfterPosition( int caret );

  List<IParseTree> getDominatingLocationList();

  boolean isSiblingOf( IParseTree deepestAtEnd );

  IParseTree getChildAfter( int point );

  IParseTree getChildBefore( int point );

  IParseTree getChildBefore( IParseTree child );

  IParseTree getChildAfter( IParseTree child );

  List<IParseTree> getChildrenBefore( IParseTree parseTree );

  IParseTree getFirstChildWithParsedElementType( Class<? extends IParsedElement> aClass );

  IParseTree getLastChildWithParsedElementType( Class<? extends IParsedElement> aClass );

  IParseTree getLastChild();

  IParseTree getNextSibling();

  IParseTree getPreviousSibling();

  IParseTree getDeepestFirstChild();

  Collection<IParseTree> findDescendantsWithParsedElementType(Class type);

  void addUnder(IParseTree parent);

  IFunctionStatement getEnclosingFunctionStatement();

  IParseTree getMatchingElement(int iStart, int iLength);

  boolean isAncestor( IParseTree after );

  class Search
  {
    public static IParseTree getDeepestLocation( boolean statementsOnly, List<IParseTree> locations, int iStart, int iEnd, boolean strict )
    {
      if( locations == null )
      {
        return null;
      }

      IParseTree deepest = null;
      for( IParseTree child : locations )
      {
        if( child != null )
        {
          IParseTree l = child.getDeepestLocation( statementsOnly, iStart, iEnd, strict );
          if( isDeeper( deepest, l ) )
          {
            deepest = l;
          }
        }
      }

      return deepest;
    }

    public static boolean isDeeper( IParseTree deepest, IParseTree potentiallyDeeper )
    {
      return (deepest == null) ||
             (potentiallyDeeper != null && potentiallyDeeper.getLength() > 0 && potentiallyDeeper.getLength() < deepest.getLength()) ||
             (deepest.isAncestorOf( potentiallyDeeper ));
    }

    /**
   * A convenience method to find the deepest location for a specified position
     * given an array of locations (typically obtained via IGosuParser.getLocations())
     *
     * @param locations An array of locations.
     * @param iPosition A position to check for.
     * @param strict    Whether to match strictly or accept white spaces to the right
     *
     * @return The deepest descendent location containing the specified location.
     */
    public static IParseTree getDeepestLocation( List<IParseTree> locations, int iPosition, boolean strict )
    {
      return getDeepestLocation( false, locations, iPosition, iPosition, strict );
    }

    /**
   * A convenience method to find the deepest location for a specified segment
     * given an array of locations (typically obtained via IGosuParser.getLocatoins())
     *
     * @param locations     A list of IParseTree to examine
     * @param iSegmentStart The start of the segment (inclusive)
     * @param iSegmentEnd   The end of the segment (inclusive)
     * @param strict        Whether to match strictly or accept white spaces to the right
     *
     * @return The deepest location found in the list containing the endpoints.
     */
    public static IParseTree getDeepestLocation( List<IParseTree> locations, int iSegmentStart, int iSegmentEnd, boolean strict )
    {
      return getDeepestLocation( false, locations, iSegmentStart, iSegmentEnd, strict );
    }

    /**
   * A convenience method to find the deepest location for a specified position
     * given an array of locations (typically obtained via IGosuParser.getLocatoins())
     *
     * @param locations A list of IParseTree to examine
     * @param iPosition The position
     * @param strict    Whether to match strictly or accept white spaces to the right
     *
     * @return The deepest location found in the list containing the position.
     */
    public static IParseTree getDeepestStatementLocation( List<IParseTree> locations, int iPosition, boolean strict )
    {
      return getDeepestLocation( true, locations, iPosition, iPosition, strict );
    }

    public static IParseTree getStatementAtLine( List<IParseTree> locations, int iLineNum, Class clsSkip )
    {
      if( locations == null )
      {
        return null;
      }

      IParseTree deepest = null;
      for( IParseTree child : locations ) {
        if (child != null) {
          IParseTree l = child.getStatementAtLine(iLineNum, clsSkip);
          if (isDeeper(deepest, l)) {
            deepest = l;
          }
        }
      }

      return deepest;
    }

    public static <E extends IParsedElement> void getContainedParsedElementsByType( List<IParseTree> locations,
                                                                                   Class<E> parsedElementClass,
                                                                                   List<E> listOut )
    {
      if( locations == null )
      {
        return;
      }

      for( IParseTree location : locations )
      {
        location.getParsedElement().getContainedParsedElementsByType( parsedElementClass, listOut );
      }
    }

    public static IParseTree getDeepestLocationSpanning( List<IParseTree> locations, int iStart, int iEnd, boolean strict )
    {
      IParseTree loc = getDeepestLocation( false, locations, iStart, iEnd, strict );
      if( loc != null &&
          loc.getParsedElement() instanceof IStatementList &&
          loc.getParsedElement().getParent() instanceof IFunctionStatement &&
          ((IFunctionStatement)loc.getParsedElement().getParent()).getDynamicFunctionSymbol() instanceof IProgramClassFunctionSymbol )
      {
        loc = loc.getParent();
      }
      return loc;
    }

    public static IStatement getHiddenStatement( IParsedElement pe )
    {
      if( pe == null )
      {
        return null;
      }

      if( pe instanceof INewExpression )
      {
        // Special case for anonymous classes

        INewExpression newExpression = (INewExpression)pe;
        IType type = newExpression.getType();
        if( type instanceof IGosuClass && newExpression.isAnonymousClass() )
        {
          return ((IGosuClass)type).getClassStatementWithoutCompile();
        }
      }

      return null;
    }

  }
}
