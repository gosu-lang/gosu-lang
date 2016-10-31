package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.Consumer;


public class GotoExceptionTypePopup extends GotoTypePopup
{
  public static void display()
  {
    GotoExceptionTypePopup valuePopup = new GotoExceptionTypePopup( "" );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        doGoTo( strQualifedType );
      } );
    Component host = LabFrame.instance().getRootPane();
    valuePopup.show( host, 0, 0 );
  }

  public static void display( JTextComponent host, String prefix, Consumer<String> consumer )
  {
    GotoExceptionTypePopup valuePopup = new GotoExceptionTypePopup( prefix );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        consumer.accept( strQualifedType );
        host.requestFocus();
      } );
    valuePopup.getNameField().setText( prefix );
    valuePopup.show( EditorUtilities.rootPaneForComponent( host ), 0, 0 );
  }

  public static void display( JComponent host, int x, int y, Consumer<String> consumer )
  {
    GotoExceptionTypePopup valuePopup = new GotoExceptionTypePopup( "" );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        consumer.accept( strQualifedType );
        host.requestFocus();
      } );
    valuePopup.show( host, x, y );
  }

  public GotoExceptionTypePopup( String strPrefix )
  {
    super( "Enter an Exception type", strPrefix );
  }
}
