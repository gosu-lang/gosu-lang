/*
 *
 * 2002 CaseCenter by Centrica Software
 *
 */
package editor.actions;


import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

/**
 */
public class GenericAction extends AbstractAction implements ChangeListener
{
  public static final String ACTION_PROPERTY = "_genericAction";

  public static final String CTRL = "CTRL";
  public static final String ALT = "ALT";
  public static final String SHIFT = "SHIFT";

  public static final String TAG_CONTEXTS = "contexts";


  private ConditionalActionHandler _actionHandler;


  public GenericAction()
  {
    super();
  }

  public GenericAction( String strId, String strName, String icon, char cMnemonic, KeyStroke accel, String strShortDesc, String strLongDesc )
  {
    super();

    putValue(NAME, strName);

    if( strId != null )
    {
      putValue( ACTION_COMMAND_KEY, strId );
    }

    if( icon != null )
    {
      setIcon( EditorUtilities.loadIcon( icon ) );
    }

    if( cMnemonic != 0 )
    {
      putValue( MNEMONIC_KEY, new Integer( cMnemonic ) );
    }

    if( accel != null )
    {
      putValue( ACCELERATOR_KEY, accel );
    }

    if( strShortDesc != null )
    {
      putValue( SHORT_DESCRIPTION, strShortDesc );
    }

    if( strLongDesc != null )
    {
      putValue( LONG_DESCRIPTION, strLongDesc );
    }
  }

  public String getId() {
    return (String) getValue(ACTION_COMMAND_KEY);
  }

  public void actionPerformed( final ActionEvent e )
  {
    SwingUtilities.invokeLater( () -> {
      if( isEnabled() )
      {
        _actionHandler.actionPerformed( e );
      }
    } );
  }

  public boolean isEnabled()
  {
    if( _actionHandler == null )
    {
      return false;
    }

    return _actionHandler.isEnabled();
  }

  public boolean isSelected()
  {
    if( _actionHandler instanceof ToggleConditionalActionHandler )
    {
      return ((ToggleConditionalActionHandler)_actionHandler).isSelected();
    }

    return false;
  }

  public String getName() {
    if (_actionHandler instanceof ConditionalNameActionHandler) {
      return ((ConditionalNameActionHandler) _actionHandler).getDisplayName();
    } else {
      return (String) getValue( Action.NAME);
    }
  }

  public Icon getIcon() {
    return (Icon) getValue( Action.SMALL_ICON);
  }

  public void setIcon( Icon icon )
  {
    putValue( Action.SMALL_ICON, icon);
  }
  
  public ConditionalActionHandler getConditionalActionHandler()
  {
    return _actionHandler;
  }

  public void setConditionalActionHandler( ConditionalActionHandler actionHandler )
  {
    _actionHandler = actionHandler;
  }

  protected KeyStroke parseAccelerator( String strAccelerator )
  {
    int iKeyCode = -1;
    int iModifier = 0;
    StringTokenizer tokenizer = new StringTokenizer( strAccelerator, " \t\n\r\f+" );
    while( tokenizer.hasMoreTokens() )
    {
      String tok = tokenizer.nextToken();
      switch( tok )
      {
        case CTRL:
          iModifier |= EditorUtilities.CONTROL_KEY_MASK;
          break;
        case ALT:
          iModifier |= InputEvent.ALT_DOWN_MASK;
          break;
        case SHIFT:
          iModifier |= InputEvent.SHIFT_DOWN_MASK;
          break;
        default:
          if( tok.length() == 1 )
          {
            iKeyCode = (int)tok.charAt( 0 );
          }
          else
          {
            try
            {
              iKeyCode = ((Integer)(KeyEvent.class.getField( "VK_" + tok.toUpperCase() )).get( null )).intValue();
            }
            catch( Exception e )
            {
              throw new RuntimeException( e );
            }
          }
          break;
      }
    }

    return iKeyCode == -1 ? null : KeyStroke.getKeyStroke( iKeyCode, iModifier );
  }

  // -- ChangeListener Impl --

  public void stateChanged( ChangeEvent e )
  {
  }
}
