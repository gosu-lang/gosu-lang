package editor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
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

  private static final char[] EOL_ARRAY = {'\n'};
  private final List<ElementSpec> _batch = new ArrayList<ElementSpec>();

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
      List<ElementSpec> ret = new ArrayList<ElementSpec>();
      for( StringTokenizer t = new StringTokenizer( str, "\n", true ); t.hasMoreTokens(); )
      {
        String s = t.nextToken();
        if( s.equals( "\n" ) )
        {
          ret.addAll( getElementsForLineFeed( a ) );
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
    List<ElementSpec> ret = new ArrayList<ElementSpec>();
    ret.add( new ElementSpec( a, ElementSpec.ContentType, chars, 0, str.length() ) );
    return ret;
  }

  public List<ElementSpec> getElementsForLineFeed( AttributeSet a )
  {
    List<ElementSpec> ret = new ArrayList<ElementSpec>( 3 );
    Element paragraph = getParagraphElement( 0 );
    AttributeSet pattr = paragraph.getAttributes();
    ret.add( new ElementSpec( null, ElementSpec.EndTagType ) );
    ret.add( new ElementSpec( pattr, ElementSpec.StartTagType ) );
    ret.add( new ElementSpec( a, ElementSpec.ContentType, EOL_ARRAY, 0, 1 ) );
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
      if( insertsList.get( insertsList.size() - 1 ).getType() == ElementSpec.StartTagType )
      {
        insertsList.addAll( getElementsForLineFeed( null ) ); // why is this needed?
      }
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
}