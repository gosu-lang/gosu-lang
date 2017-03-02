package editor;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * DefaultDocument subclass that supports batching inserts.
 */
public class BatchDocument extends DefaultStyledDocument
{

  public static final char[] EOL_ARRAY = {'\n'};
  private final List<ElementSpec> _batch = new ArrayList<>();

  public void appendString( String str, AttributeSet a )
  {
    processInserts( getElementsForString( str, a ) );
  }

  public void appendBatchString( String str, AttributeSet a )
  {
    synchronized( _batch )
    {
      _batch.addAll( getElementsForString( str, a ) );
      while( _batch.size() > 2000 )
      {
        _batch.remove( 0 );
      }
    }
  }

  private List<ElementSpec> getElementsForString( String str, AttributeSet a )
  {
    if( str.length() > 0 )
    {
      boolean bBytecodeDump = str.startsWith( "// class version " ) && str.contains( "// access flags" );

      List<ElementSpec> ret = new ArrayList<>();
      SimpleAttributeSet bytecodeDumpColor = new SimpleAttributeSet();
      bytecodeDumpColor.addAttribute( StyleConstants.Foreground, new Color( 92, 225, 92 ) );

      for( StringTokenizer t = new StringTokenizer( str, "\r\n", true ); t.hasMoreTokens(); )
      {
        String s = t.nextToken();
        if( s.equals( "\n" ) )
        {
          ret.addAll( getElementsForLineFeed( a ) );
        }
        else if( bBytecodeDump )
        {
          ret.add( new ElementSpec( bytecodeDumpColor, ElementSpec.ContentType, s.toCharArray(), 0, s.length() ) );
        }
        else
        {
          ret.addAll( _getElementsForString( s, a ) );
        }
      }
      return ret;
    }
    else
    {
      return Collections.emptyList();
    }
  }

  private Collection<? extends ElementSpec> _getElementsForString( String str, AttributeSet a )
  {
    a = a.copyAttributes();
    char[] chars = str.toCharArray();
    List<ElementSpec> ret = new ArrayList<>();
    if( !handleStackTraceLink( str, ret, a ) &&
        !handleTestResults( str, ret, a ) )
    {
      ret.add( new ElementSpec( a, ElementSpec.ContentType, chars, 0, str.length() ) );
    }
    return ret;
  }

  private boolean handleStackTraceLink( String str, List<ElementSpec> ret, AttributeSet a )
  {
    if( str.trim().startsWith( "at " ) && str.contains( "(" ) && str.contains( ".gs" ) && str.contains( ":" ) )
    {
      int iParen = str.indexOf( "(" );
      int iDot = str.indexOf( '.', iParen );
      String name = str.substring( iParen + 1, iDot );
      int iAt = str.indexOf( "at" );
      int iName = str.lastIndexOf( "." + name );
      String fqn = null;
      if( iName >= 0 )
      {
        fqn = str.substring( iAt + 3, iName + name.length() + 1 );
      }
      else
      {
        String rawFqnPlusMethodName = str.substring( iAt + 3, str.indexOf( '(' ) );
        String rawFqn = rawFqnPlusMethodName.substring( 0, rawFqnPlusMethodName.lastIndexOf( '.' ) );
        IType rawType = TypeSystem.getByFullNameIfValid( rawFqn );
        if( rawType instanceof IGosuProgram )
        {
          IType contextType = ((IGosuProgram)rawType).getContextType();
          if( contextType != null )
          {
            fqn = contextType.getName();
          }
        }
        if( fqn == null )
        {
          return false;
        }
      }
      int iLine = str.indexOf( ':' );
      String strLine = "";
      for( int iDigit = iLine + 1; Character.isDigit( str.charAt( iDigit ) ); iDigit++ )
      {
        char c = str.charAt( iDigit );
        strLine += c;
      }
      int line = strLine.length() > 0 ? Integer.parseInt( strLine ) : -1;

      String firstPart = str.substring( 0, iParen + 1 );
      ret.add( new ElementSpec( a, ElementSpec.ContentType, firstPart.toCharArray(), 0, firstPart.length() ) );

      SimpleAttributeSet newAttr = new SimpleAttributeSet();
      newAttr.addAttribute( HTML.Tag.A, new SourceFileAttribute( line, fqn ) );
      newAttr.addAttribute( StyleConstants.Foreground, new Color( 150, 150, 255 ) );
      String link = str.substring( iParen + 1, str.length() - 1 );
      ret.add( new ElementSpec( newAttr, ElementSpec.ContentType, link.toCharArray(), 0, link.length() ) );

      String lastPart = ")";
      ret.add( new ElementSpec( a, ElementSpec.ContentType, lastPart.toCharArray(), 0, 1 ) );
      return true;
    }
    return false;
  }

  private boolean handleTestResults( String str, List<ElementSpec> ret, AttributeSet a )
  {
    if( str.equals( GosuPanel.SUCCESS ) )
    {
      SimpleAttributeSet newAttr = new SimpleAttributeSet();
      newAttr.addAttribute( StyleConstants.Foreground, new Color( 90, 220, 90 ) );
      ret.add( new ElementSpec( newAttr, ElementSpec.ContentType, str.toCharArray(), 0, str.length() ) );
      return true;
    }
    else if( str.startsWith( GosuPanel.FAILED ) && str.contains( " : " ) )
    {
      SimpleAttributeSet newAttr = new SimpleAttributeSet();
      newAttr.addAttribute( StyleConstants.Foreground, new Color( 240, 90, 90 ) );
      ret.add( new ElementSpec( newAttr, ElementSpec.ContentType, GosuPanel.FAILED.toCharArray(), 0, GosuPanel.FAILED.length() ) );
      String theRest = str.substring( GosuPanel.FAILED.length() );
      ret.add( new ElementSpec( a, ElementSpec.ContentType, theRest.toCharArray(), 0, theRest.length() ) );
      return true;
    }
    return false;
  }

  public List<ElementSpec> getElementsForLineFeed( AttributeSet a )
  {
    List<ElementSpec> ret = new ArrayList<>( 3 );
    ret.add( new ElementSpec( a, ElementSpec.ContentType, EOL_ARRAY, 0, 1 ) );
    Element paragraph = getParagraphElement( 0 );
    AttributeSet pattr = paragraph.getAttributes();
    ret.add( new ElementSpec( null, ElementSpec.EndTagType ) );
    ret.add( new ElementSpec( pattr, ElementSpec.StartTagType ) );
    return ret;
  }

  public void processBatchUpdates()
  {
    synchronized( _batch )
    {
      processInserts( _batch );
      _batch.clear();
    }
  }

  private void processInserts( List<ElementSpec> insertsList )
  {
    if( !insertsList.isEmpty() )
    {
      ensureEndStartTagsPrecedeInserts( insertsList );

      ElementSpec[] inserts = new ElementSpec[insertsList.size()];
      insertsList.toArray( inserts );
      try
      {
        super.insert( getLength(), inserts );
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private void ensureEndStartTagsPrecedeInserts( List<ElementSpec> insertsList )
  {
    Element paragraph = getParagraphElement( 0 );
    AttributeSet pattr = paragraph.getAttributes();
    insertsList.add( 0, new ElementSpec( pattr, ElementSpec.StartTagType ) );
    insertsList.add( 0, new ElementSpec( null, ElementSpec.EndTagType ) );
  }
}