/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.IClassDeclaration;

import java.util.List;

public class GosuRefactorUtil
{
  /**
   * Finds a bounding parent of any of the possible types passed in from the list of locations, starting at the position
   * given.
   */
  public static IParsedElement boundingParent( List<IParseTree> locations, int position, Class<? extends IParsedElement>... possibleTypes )
  {
    IParseTree location = IParseTree.Search.getDeepestLocation( locations, position, true );

    IParsedElement pe = null;
    if( location != null )
    {
      pe = location.getParsedElement();
      while( pe != null && !isOneOfTypes( pe, possibleTypes ) )
      {
        pe = pe.getParent();
      }
    }
    return pe;
  }

  private static boolean isOneOfTypes( IParsedElement pe, Class<? extends IParsedElement>[] possibleTypes )
  {
    for( Class<? extends IParsedElement> possibleType : possibleTypes )
    {
      if( possibleType.isAssignableFrom( pe.getClass() ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Given two parse tree positions, find the bounding pair that captures the start and end in one logical unit
   */
  public static IParseTree[] findSpanningLogicalRange( IParseTree start, IParseTree end )
  {
    while( end != null )
    {
      IParseTree deepestAtStart = start;
      while( deepestAtStart != null )
      {
        if( deepestAtStart.isSiblingOf( end ) )
        {
          IParseTree[] returnVal = new IParseTree[2];

          if( deepestAtStart.getOffset() < end.getOffset() )
          {
            returnVal[0] = deepestAtStart;
          }
          else
          {
            returnVal[0] = end;
          }

          if( deepestAtStart.getExtent() > end.getExtent() )
          {
            returnVal[1] = deepestAtStart;
          }
          else
          {
            returnVal[1] = end;
          }

          return returnVal;
        }
        deepestAtStart = deepestAtStart.getParent();
      }
      end = end.getParent();
    }
    return null;
  }

  public static MoveInstruction getMoveUpInstruction( IParseTree elementToMoveBefore )
  {
    return findElementToMoveToBefore( elementToMoveBefore );
  }

  public static MoveInstruction getMoveDownInstruction( IParseTree elementToMoveAfter )
  {
    return findElementToMoveToAfter( elementToMoveAfter );
  }

  private static MoveInstruction findElementToMoveToBefore( IParseTree child )
  {
    IParseTree parent = child.getParent();
    IParseTree precedingChild = parent.getChildBefore( child );
    if( precedingChild == null )
    {

      //if this is a statement list, defer to the parent of this
      if( parent.getParsedElement() instanceof IStatementList )
      {
        IParseTree previousSibling = parent.getPreviousSibling();
        if( previousSibling != null && previousSibling.getParsedElement() instanceof IStatementList )
        {
          return new MoveInstruction( false, false, previousSibling.getExtent() );
        }
        else
        {
          //If there is no preceding list, we are just going to move above the parent of the statement list
          parent = parent.getParent();
        }
      }

      if( isClassElement( parent ) )
      {
        return null;
      }

      return new MoveInstruction( false, true, parent.getOffset() );
    }
    else {
      if (precedingChild.getParsedElement() instanceof IClassDeclaration) {
        return null;
      }
      IParseTree precedingStmtList = precedingChild.getLastChildWithParsedElementType(IStatementList.class);
      if (precedingStmtList != null && !isClassElement(child)) {
        return new MoveInstruction(true, false, precedingStmtList.getExtent());
      } else {
        return new MoveInstruction(false, false, precedingChild.getOffset());
      }
    }
  }

  public static boolean isClassElement( IParseTree parseTree )
  {
    return parseTree.getParsedElement() instanceof IClassStatement ||
           parseTree.getParent() != null && parseTree.getParent().getParsedElement() instanceof IClassStatement;
  }

  public static MoveInstruction findElementToMoveToAfter( IParseTree child )
  {
    IParseTree parent = child.getParent();
    IParseTree followingChild = parent.getChildAfter( child );

    if( followingChild == null )
    {

      if( parent.getParsedElement() instanceof IStatementList )
      {
        IParseTree nextSibling = parent.getNextSibling();
        if( nextSibling != null && nextSibling.getParsedElement() instanceof IStatementList )
        {
          return new MoveInstruction( false, false, nextSibling.getOffset() );
        }
        else
        {
          //If there is no preceding list, we are just going to move above the parent of the statement list
          parent = parent.getParent();
        }
      }

      if( isClassElement( parent ) )
      {
        return null;
      }

      return new MoveInstruction( false, true, parent.getExtent() );
    }
    else
    {
      IParseTree precedingStmtList = followingChild.getFirstChildWithParsedElementType( IStatementList.class );
      if( precedingStmtList != null && !isClassElement( child ) )
      {
        return new MoveInstruction( true, false, precedingStmtList.getOffset() );
      }
      else
      {
        return new MoveInstruction( false, false, followingChild.getExtent() );
      }
    }
  }

  public static IParseTree findFirstStatementAtLine( int line, int position, List<IParseTree> locations )
  {
    return findStatementAtLine( locations, position, line, true );
  }

  public static IParseTree findLastStatementAtLine( int line, int position, List<IParseTree> locations )
  {
    return findStatementAtLine( locations, position, line, false );
  }

  private static IParseTree findStatementAtLine( List<IParseTree> locations, int position, int line, boolean earliest )
  {
    for( IParseTree location : locations )
    {
      IParseTree deepestLocation = location.getDeepestLocation( position, true );
      if( deepestLocation != null )
      {

        while( deepestLocation != null &&
               !(deepestLocation.getParsedElement() instanceof IStatement) )
        {
          deepestLocation = deepestLocation.getParent();
        }

        if( deepestLocation != null )
        {

          if( deepestLocation.getLineNum() == line )
          {
            deepestLocation = deepestLocation.getParent();
          }

          List<IParseTree> list = deepestLocation.getChildren();

          IParseTree closestChildOnLine = null;
          for( IParseTree parseTree : list )
          {

            //ignore statement lists and expressions
            if( parseTree.getParsedElement() instanceof IStatementList ||
                parseTree.getParsedElement() instanceof IExpression )
            {
              continue;
            }

            if( parseTree.getLineNum() == line )
            {
              if( closestChildOnLine == null )
              {
                closestChildOnLine = parseTree;
              }
              else if( earliest && parseTree.getOffset() < closestChildOnLine.getOffset() )
              {
                closestChildOnLine = parseTree;
              }
              else if( parseTree.getOffset() > closestChildOnLine.getOffset() )
              {
                closestChildOnLine = parseTree;
              }
            }
          }

          if( closestChildOnLine != null )
          {
            return closestChildOnLine;
          }
          else
          {
            if( deepestLocation.getParsedElement() instanceof IStatementList )
            {
              return deepestLocation.getParent();
            }
            else if( deepestLocation.getParsedElement() instanceof IClassStatement )
            {
              return null;
            }
            else
            {
              return deepestLocation;
            }
          }
        }
      }
    }
    return null;
  }


  public static class MoveInstruction
  {
    public boolean indent;
    public boolean outdent;
    public int position;

    public MoveInstruction( boolean indent, boolean outdent, int position )
    {
      this.indent = indent;
      this.outdent = outdent;
      this.position = position;
    }
  }
}