package editor;

import gw.util.StreamUtil;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 */
public class TextComponentWriter extends PrintStream implements IColorConsole
{
  private BatchDocument _document;
  private PrintStream[] _echoWriters;
  private AttributeSet _attributes;
  private final SimpleAttributeSet _defaultAttributes;
  private StringBuilder _suspendedOutput = new StringBuilder();

  public TextComponentWriter( Color color, BatchDocument document, PrintStream... echoWriters )
  {
    super( new NullOutputStream() );
    _document = document;
    _echoWriters = echoWriters;
    _defaultAttributes = new SimpleAttributeSet();
    _defaultAttributes.addAttribute( StyleConstants.Foreground, color );
    GosuEventQueue.instance().addIdleListener( new ConsoleDrainer() );
  }

  public void print( boolean b )
  {
    print( String.valueOf( b ) );
  }

  public void print( char c )
  {
    print( String.valueOf( c ) );
  }

  public void print( int i )
  {
    print( String.valueOf( i ) );
  }

  public void print( long l )
  {
    print( String.valueOf( l ) );
  }

  public void print( float f )
  {
    print( String.valueOf( f ) );
  }

  public void print( double d )
  {
    print( String.valueOf( d ) );
  }

  public void print( char[] s )
  {
    print( String.valueOf( s ) );
  }

  public synchronized void print( final String strOutput )
  {
    _suspendedOutput.append( strOutput );
    AttributeSet attributes = _attributes == null ? _defaultAttributes : _attributes;
    _document.appendBatchString( strOutput, attributes );
  }

  public void print( Object obj )
  {
    print( String.valueOf( obj ) );
  }

  public void println()
  {
    print( "\n" );
  }

  public void println( boolean b )
  {
    println( String.valueOf( b ) );
  }

  public void println( char c )
  {
    println( String.valueOf( c ) );
  }

  public void println( int i )
  {
    println( String.valueOf( i ) );
  }

  public void println( long l )
  {
    println( String.valueOf( l ) );
  }

  public void println( float f )
  {
    println( String.valueOf( f ) );
  }

  public void println( double d )
  {
    println( String.valueOf( d ) );
  }

  public void println( char[] s )
  {
    println( String.valueOf( s ) );
  }

  public void println( String strOutput )
  {
    print( strOutput + "\n" );
  }

  public void println( Object obj )
  {
    println( String.valueOf( obj ) );
  }

  public void write( byte[] buf, int off, int len )
  {
    print( StreamUtil.toString( buf, off, len ) );
  }

  public void write( int b )
  {
    print( (char)b ); // TODO - i18n
  }

  @Override
  public void setAttributes( AttributeSet attributes )
  {
    _attributes = attributes;
  }

  @Override
  public void suspendUpdates()
  {
  }

  @Override
  public void resumeUpdates()
  {
  }

  private static class NullOutputStream extends OutputStream
  {
    @Override
    public void write( int b ) throws IOException
    {

    }
  }

  private class ConsoleDrainer implements Runnable
  {
    @Override
    public synchronized void run()
    {
      if( _suspendedOutput.length() == 0 )
      {
        return;
      }

      for( PrintStream echoWriter : _echoWriters )
      {
        echoWriter.print( _suspendedOutput );
        echoWriter.flush();
      }
      _suspendedOutput = new StringBuilder();
      _document.processBatchUpdates();
    }
  }
}