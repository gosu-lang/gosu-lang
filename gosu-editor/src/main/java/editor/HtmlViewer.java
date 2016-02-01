package editor;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.net.URL;


/**
 * Extends the Java swing class JEditorPane to produce an HTML viewer.  The editor loads the Gosu class
 * as a stream.  This file also defines the CSS for the Studio Help.
 */
public class HtmlViewer extends JEditorPane
{

  public HtmlViewer()
  {
    super();

    setEditable( false );
    setContentType( "text/html" );
    setBorder( BorderFactory.createEmptyBorder() );

    addHyperlinkListener( new HyperlinkHandler() );
  }

  public void setText( String strText )
  {
    if( strText == null )
    {
      strText = "";
    }
    else if( !strText.startsWith( "<html" ) )
    {
 /*     strText = "<html><font size=3 face=Tahoma,Verdana,Arial,Helvetica>" +
        strText +
        "</font></html>"; */
      strText = "<head>\n" +
                "<style type=\"text/css\">\n" +
                "BODY {margin: 5px; font-family: Tahoma, Verdana, Arial, Helvetica; font-size: 12pt}\n" +
                "H1 {font-size: 120%;}\n" +
                "H3 {background-color: #E4E4DA; text-indent: 5px; }\n" +
                "CODE {font-size: 111%; color: #000099; }\n" +
                "DT {font-style: italic;}\n" +
                "</style>\n" +
                "</head><html>" +
                strText +
                "</html>";
    }

    super.setText( strText );
  }

  class HyperlinkHandler implements HyperlinkListener
  {
    public void hyperlinkUpdate( HyperlinkEvent e )
    {
      if( e.getEventType() != HyperlinkEvent.EventType.ACTIVATED )
      {
        return;
      }

      URL url = e.getURL();
      if( url == null )
      {
        return;
      }
      String strProtocol = url.getProtocol();
      String strUrl = url.toString();

      if( strProtocol.equals( "gosu" ) )
      {
//        String strCmd = strUrl.substring( strUrl.indexOf( ':' ) + 1 );
//        ICommandShell shell = StudioApplication.instance().getCommandShell();
//        shell.invokeCommand( strCmd, null );
      }
      else
      {
        try
        {
          editor.util.EditorUtilities.browse( strUrl );
        }
        catch( Throwable t )
        {
          editor.util.EditorUtilities.handleUncaughtException( t );
        }
      }
    }
  }

}