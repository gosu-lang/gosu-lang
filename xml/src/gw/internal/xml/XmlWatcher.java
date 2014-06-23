/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.xml.XmlElement;
import gw.xml.XmlSerializationOptions;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class XmlWatcher extends Thread {

  private final XmlElement _element;

  public XmlWatcher( XmlElement element ) {
    _element = element;
  }

  public void run() {
    JFrame f = new JFrame();
    f.addWindowListener( new WindowAdapter() {
      @Override
      public void windowClosed( WindowEvent e ) {
        interrupt();
      }
    } );
    try {
      JTextArea area = new JTextArea();
      area.setLineWrap( true );
      area.setWrapStyleWord( true );
      area.setEditable( false );
      area.setFont( area.getFont().deriveFont( Font.BOLD, 16.0f ) );
      f.add( new JScrollPane( area ), BorderLayout.CENTER );
      f.setSize( 300, 300 );
      f.setLocationByPlatform( true );
      f.setAlwaysOnTop( true );
      f.setVisible( true );
      while ( true ) {
        Thread.sleep( 250 );
        area.setText( _element.asUTFString( XmlSerializationOptions.debug() ) );
      }
    }
    catch ( InterruptedException ex ) {
      // ignore
    }
    finally {
      f.dispose();
    }
  }

}
