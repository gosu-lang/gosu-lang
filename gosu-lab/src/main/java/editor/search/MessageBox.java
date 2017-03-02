package editor.search;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 */
public class MessageBox extends JDialog
{
  public static final int CUSTOM = -999;
  public static final String CANCEL = "_cancel";

  int _iWidestButton;
  int _iType;
  int _iButtons;
  String _strWrappedMsg;
  String[] _astrButtonLabels;
  int _iRet;

  private static Point _lastDismissedLocation;

  public MessageBox( Frame frame, String strWrappedMsg, int iButtons, int iType, String... astrButtonLabels )
  {
    super( frame == null ? JOptionPane.getRootFrame() : frame, "Gosu", true );

    if( strWrappedMsg != null )
    {
      strWrappedMsg = strWrappedMsg.trim();
    }

    _strWrappedMsg = strWrappedMsg;
    _astrButtonLabels = astrButtonLabels;
    if( _astrButtonLabels != null && _astrButtonLabels.length == 0 )
    {
      _astrButtonLabels = null;
    }
    _iType = iType;
    _iButtons = iButtons;
    _iRet = JOptionPane.CLOSED_OPTION;

    setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );

    addWindowListener( new MessageBoxListener() );

    configureUI();
  }

  public static void showMessageDialog( String strWrappedMsg, int iType )
  {
    showMessageDialog( MessageDisplay.getFrame(), strWrappedMsg, iType );
  }

  public static void showMessageDialog( Frame frame, String strWrappedMsg, int iType )
  {
    MessageBox mb = new MessageBox( frame, strWrappedMsg, JOptionPane.DEFAULT_OPTION, iType );
    mb.show();
    _lastDismissedLocation = mb.getLocation();
  }

  public static int showConfirmDialog( Frame frame, String strWrappedMsg, int iButtons, int iType, String... astrButtonLabels )
  {
    return showConfirmDialog( frame, strWrappedMsg, iButtons, iType, null, astrButtonLabels );
  }

  public static int showConfirmDialog( Frame frame, String strWrappedMsg, int iButtons, int iType, Point loc, String... astrButtonLabels )
  {
    MessageBox mb = new MessageBox( frame, strWrappedMsg, iButtons, iType, astrButtonLabels );
    mb.show( loc );
    _lastDismissedLocation = mb.getLocation();

    return mb._iRet;
  }

  public void show()
  {
    show( null );
  }

  private void show( Point loc )
  {
    pack();
    if( loc != null )
    {
      setLocation( loc );
    }
    else
    {
      EditorUtilities.centerWindowInFrame( this, getOwner() );
    }
    super.show();
  }

  void configureUI()
  {
    getContentPane().setLayout( new BorderLayout() );

    JPanel panel = new JPanel();
    panel.setLayout( new BorderLayout() );

    JLabel labelMsg = new JLabel()
    {
      public void setUI( javax.swing.plaf.LabelUI ui )
      {
        ui = (MultiLineLabelUI)MultiLineLabelUI.createUI( this );

        super.setUI( ui );
      }

      public Dimension getPreferredSize()
      {
        Insets insets = getInsets();

        String strText = getText();
        if( strText == null )
        {
          return super.getPreferredSize();
        }

        int iLines = 1;
        int iMaxLen = 0;
        StringBuilder strbBuf = new StringBuilder();

        FontMetrics fm = getFontMetrics( getFont() );

        for( int i = 0; i < strText.length(); i++ )
        {
          char c = strText.charAt( i );
          if( c != '\n' )
          {
            strbBuf.append( c );
          }
          else
          {
            iMaxLen = Math.max( fm.stringWidth( strbBuf.toString() ), iMaxLen );
            strbBuf.setLength( 0 );
            iLines++;
          }
        }
        iMaxLen = Math.max( fm.stringWidth( strbBuf.toString() ), iMaxLen );
        iMaxLen += insets.left + insets.right;

        return new Dimension( iMaxLen, (fm.getHeight() * iLines) + insets.top + insets.bottom );
      }
    };
    labelMsg.setHorizontalAlignment( SwingConstants.LEFT );
    labelMsg.setText( _strWrappedMsg );
    labelMsg.setBorder( new javax.swing.border.EmptyBorder( 15, 15, 15, 15 ) );


    JLabel labelIcon = new JLabel( loadIcon() );
    labelIcon.setBorder( new javax.swing.border.EmptyBorder( 15, 5, 5, 5 ) );

    JPanel panelCenter = new JPanel();
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    panelCenter.setLayout( gridBag );

    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    panelCenter.add( labelIcon, c );

    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.VERTICAL;
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 0;
    c.weighty = 1;
    c.insets = new Insets( 0, 0, 0, 0 );
    panelCenter.add( new JPanel(), c );

    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 0, 0, 0, 0 );
    panelCenter.add( labelMsg, c );

    getContentPane().add( BorderLayout.NORTH, panelCenter );
    getContentPane().add( BorderLayout.CENTER, new JPanel() );

    JPanel panelBtns = new JPanel();
    panelBtns.setLayout( new FlowLayout() );


    switch( _iButtons )
    {
      case JOptionPane.DEFAULT_OPTION:
      {
        JButton btnOk = new JButton( (_astrButtonLabels == null || _astrButtonLabels[0] == null) ? "OK" : _astrButtonLabels[0] );
        setPreferredSize( btnOk );
        btnOk.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.OK_OPTION;
            close();
          }
        } );
        panelBtns.add( btnOk );
        getRootPane().setDefaultButton( btnOk );

        break;
      }

      case JOptionPane.YES_NO_OPTION:
      {
        _iRet = JOptionPane.NO_OPTION;

        JButton btnYes = new JButton( (_astrButtonLabels == null || _astrButtonLabels[0] == null) ? "Yes" : _astrButtonLabels[0] );
        setPreferredSize( btnYes );
        btnYes.setMnemonic( 'Y' );
        btnYes.addActionListener(
          new ActionListener()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.YES_OPTION;
              close();
            }
          } );
        panelBtns.add( btnYes );
        getRootPane().setDefaultButton( btnYes );
        ((JComponent)getContentPane()).registerKeyboardAction(
          new AbstractAction()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.YES_OPTION;
              close();
            }
          },
          KeyStroke.getKeyStroke( KeyEvent.VK_Y, 0 ),
          JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

        JButton btnNo = new JButton( (_astrButtonLabels == null || _astrButtonLabels[1] == null) ? "No" : _astrButtonLabels[1] );
        setPreferredSize( btnNo );
        btnNo.setMnemonic( 'N' );
        btnNo.addActionListener(
          new ActionListener()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.NO_OPTION;
              close();
            }
          } );
        panelBtns.add( btnNo );
        ((JComponent)getContentPane()).registerKeyboardAction(
          new AbstractAction()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.NO_OPTION;
              close();
            }
          },
          KeyStroke.getKeyStroke( KeyEvent.VK_N, 0 ),
          JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

        break;
      }

      case JOptionPane.YES_NO_CANCEL_OPTION:
      {
        _iRet = JOptionPane.CANCEL_OPTION;

        JButton btnYes = new JButton( (_astrButtonLabels == null || _astrButtonLabels[0] == null) ? "Yes" : _astrButtonLabels[0] );
        setPreferredSize( btnYes );
        btnYes.setMnemonic( 'Y' );
        btnYes.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.YES_OPTION;
            close();
          }
        } );
        panelBtns.add( btnYes );
        getRootPane().setDefaultButton( btnYes );
        ((JComponent)getContentPane()).registerKeyboardAction(
          new AbstractAction()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.YES_OPTION;
              close();
            }
          },
          KeyStroke.getKeyStroke( KeyEvent.VK_Y, 0 ),
          JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

        JButton btnNo = new JButton( (_astrButtonLabels == null || _astrButtonLabels[1] == null) ? "No" : _astrButtonLabels[1] );
        setPreferredSize( btnNo );
        btnNo.setMnemonic( 'N' );
        btnNo.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.NO_OPTION;
            close();
          }
        } );
        panelBtns.add( btnNo );
        ((JComponent)getContentPane()).registerKeyboardAction(
          new AbstractAction()
          {
            public void actionPerformed( ActionEvent e )
            {
              _iRet = JOptionPane.NO_OPTION;
              close();
            }
          },
          KeyStroke.getKeyStroke( KeyEvent.VK_N, 0 ),
          JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

        JButton btnCancel = new JButton( (_astrButtonLabels == null || _astrButtonLabels[2] == null) ? "Cancel" : _astrButtonLabels[2] );
        setPreferredSize( btnCancel );
        btnCancel.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.CANCEL_OPTION;
            close();
          }
        } );
        panelBtns.add( btnCancel );

        break;
      }

      case JOptionPane.OK_CANCEL_OPTION:
      {
        _iRet = JOptionPane.CANCEL_OPTION;

        JButton btnOk = new JButton( (_astrButtonLabels == null || _astrButtonLabels[0] == null) ? "OK" : _astrButtonLabels[0] );
        setPreferredSize( btnOk );
        btnOk.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.OK_OPTION;
            close();
          }
        } );
        panelBtns.add( btnOk );
        getRootPane().setDefaultButton( btnOk );

        JButton btnCancel = new JButton( (_astrButtonLabels == null || _astrButtonLabels[1] == null) ? "Cancel" : _astrButtonLabels[1] );
        setPreferredSize( btnCancel );
        btnCancel.addActionListener( new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            _iRet = JOptionPane.CANCEL_OPTION;
            close();
          }
        } );
        panelBtns.add( btnCancel );

        break;
      }

      case CUSTOM:
      {
        _iRet = -1;

        for( int i = 0; i < _astrButtonLabels.length; i++ )
        {
          String strLabel = (_astrButtonLabels == null || _astrButtonLabels[0] == null) ? "Button1" : _astrButtonLabels[i];
          strLabel = strLabel == CANCEL ? "Cancel" : strLabel;
          JButton btn = new JButton( strLabel );
          final int iRet = i;
          if( strLabel != CANCEL )
          {
            char cMnemonic = strLabel.charAt( 0 );
            btn.setMnemonic( cMnemonic );
            ((JComponent)getContentPane()).registerKeyboardAction(
              new AbstractAction()
              {
                public void actionPerformed( ActionEvent e )
                {
                  _iRet = iRet;
                  close();
                }
              },
              KeyStroke.getKeyStroke( Character.toLowerCase( cMnemonic ) ),
              JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
          }
          setPreferredSize( btn );
          btn.addActionListener(
            new ActionListener()
            {
              public void actionPerformed( ActionEvent e )
              {
                _iRet = iRet;
                close();
              }
            } );
          panelBtns.add( btn );

          if( i == 0 )
          {
            getRootPane().setDefaultButton( btn );
          }
        }
        break;
      }
    }

    getContentPane().add( BorderLayout.SOUTH, panelBtns );

    ((JComponent)getContentPane()).registerKeyboardAction(
      new AbstractAction()
      {
        public void actionPerformed( ActionEvent e )
        {
          close();
        }
      },
      KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
      JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
  }

  protected void setPreferredSize( JButton btn )
  {
    btn.setPreferredSize( new Dimension( determineWidestButton(), btn.getPreferredSize().height ) );
  }

  protected int determineWidestButton()
  {
    if( _iWidestButton == 0 )
    {
      JButton btn = new JButton( "Cancel" );

      _iWidestButton = Math.max( _iWidestButton, btn.getPreferredSize().width );

      btn.setText( "Yes" );
      _iWidestButton = Math.max( _iWidestButton, btn.getPreferredSize().width );

      btn.setText( "No" );
      _iWidestButton = Math.max( _iWidestButton, btn.getPreferredSize().width );

      btn.setText( "OK" );
      _iWidestButton = Math.max( _iWidestButton, btn.getPreferredSize().width );

      if( _astrButtonLabels != null )
      {
        for( int i = 0; i < _astrButtonLabels.length; i++ )
        {
          if( _astrButtonLabels[i] != null )
          {
            btn.setText( _astrButtonLabels[i] );
            _iWidestButton = Math.max( _iWidestButton, btn.getPreferredSize().width );
          }
        }
      }
    }

    return _iWidestButton;
  }

  public void close()
  {
    setVisible( false );
    dispose();
  }

  protected Icon loadIcon()
  {
    switch( _iType )
    {
      case JOptionPane.ERROR_MESSAGE:
        return UIManager.getIcon( "OptionPane.errorIcon" );

      case JOptionPane.WARNING_MESSAGE:
        return UIManager.getIcon( "OptionPane.warningIcon" );

      case JOptionPane.QUESTION_MESSAGE:
        return UIManager.getIcon( "OptionPane.questionIcon" );
    }

    return UIManager.getIcon( "OptionPane.informationIcon" );
  }

  public static Point getLastMessageLocation()
  {
    return _lastDismissedLocation;
  }

  class MessageBoxListener extends WindowAdapter
  {
    public void windowClosing( WindowEvent e )
    {
      close();
    }
  }
}
