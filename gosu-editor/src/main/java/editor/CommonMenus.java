package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

/**
 */
public class CommonMenus
{
  public static JMenuItem makeCut( Supplier<GosuEditor> editor )
  {
    JMenuItem cutItem = new JMenuItem(
      new AbstractAction( "Cut" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipCut( Toolkit.getDefaultToolkit().getSystemClipboard() );
        }
      } );
    cutItem.setMnemonic( 't' );
    cutItem.setAccelerator( KeyStroke.getKeyStroke( "control X" ) );

    return cutItem;
  }

  public static JMenuItem makeCopy( Supplier<GosuEditor> editor )
  {
    JMenuItem copyItem = new JMenuItem(
      new AbstractAction( "Copy" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipCopy( Toolkit.getDefaultToolkit().getSystemClipboard() );
        }
      } );
    copyItem.setMnemonic( 'C' );
    copyItem.setAccelerator( KeyStroke.getKeyStroke( "control C" ) );

    return copyItem;
  }

  public static JMenuItem makePaste( Supplier<GosuEditor> editor )
  {
    JMenuItem pasteItem = new JMenuItem(
      new AbstractAction( "Paste" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipPaste( Toolkit.getDefaultToolkit().getSystemClipboard() );
        }
      } );
    pasteItem.setMnemonic( 'P' );
    pasteItem.setAccelerator( KeyStroke.getKeyStroke( "control V" ) );

    return pasteItem;
  }

  public static JMenuItem makeCodeComplete( Supplier<GosuEditor> editor )
  {
    JMenuItem completeItem = new JMenuItem(
      new AbstractAction( "Complete Code" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().handleCompleteCode();
        }
      } );
    completeItem.setMnemonic( 'L' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( "control SPACE" ) );

    return completeItem;
  }

  public static JMenuItem makeParameterInfo( Supplier<GosuEditor> editor )
  {
    JMenuItem paraminfoItem = new JMenuItem(
          new AbstractAction( "Parameter Info" )
          {
            @Override
            public void actionPerformed( ActionEvent e )
            {
              if( !editor.get().isIntellisensePopupShowing() )
              {
                editor.get().displayParameterInfoPopup( editor.get().getEditor().getCaretPosition() );
              }
            }
          } );
    paraminfoItem.setMnemonic( 'P' );
    paraminfoItem.setAccelerator( KeyStroke.getKeyStroke( "control P" ) );

    return paraminfoItem;
  }

  public static JMenuItem makeExpressionType( Supplier<GosuEditor> editor )
  {
    JMenuItem typeItem = new JMenuItem(
          new AbstractAction( "Expression Type" )
          {
            @Override
            public void actionPerformed( ActionEvent e )
            {
              editor.get().displayTypeInfoAtCurrentLocation();
            }
          } );
    typeItem.setMnemonic( 'T' );
    typeItem.setAccelerator( KeyStroke.getKeyStroke( "control T" ) );

    return typeItem;
  }

  public static JMenuItem makeGotoDeclaration( Supplier<GosuEditor> editor )
  {
    JMenuItem navigate = new JMenuItem(
          new AbstractAction( "Goto Declaration" )
          {
            @Override
            public void actionPerformed( ActionEvent e )
            {
              editor.get().gotoDeclaration();
            }
          } );
        navigate.setMnemonic( 'D' );
        navigate.setAccelerator( KeyStroke.getKeyStroke( "control B" ) );
    return navigate;
  }

  public static JMenuItem makeQuickDocumentation( Supplier<GosuEditor> editor )
  {
    JMenuItem quickDoc = new JMenuItem(
          new AbstractAction( "Quick Documentation" )
          {
            @Override
            public void actionPerformed( ActionEvent e )
            {
              editor.get().displayJavadocHelp( editor.get().getDeepestLocationAtCaret() );
            }
          } );
        quickDoc.setMnemonic( 'Q' );
        quickDoc.setAccelerator( KeyStroke.getKeyStroke( "control Q" ) );
    return quickDoc;
  }
}
