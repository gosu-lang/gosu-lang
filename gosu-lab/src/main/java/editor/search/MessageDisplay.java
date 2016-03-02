package editor.search;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

public class MessageDisplay implements SwingConstants
{
  public static final String HOME_DIR = System.getProperty( "user.dir" );

  static final String STRING_DELIMITERS = " \t\n\r";
  static final Rectangle SCRATCH_RECT = new Rectangle();
  static final Rectangle SCRATCH_RECT2 = new Rectangle();
  private static String _testError;

  //--------------------------------------------------------------------------------------------------
  public static void Log( Throwable t )
  {
    System.err.println( "\r\n" + t.getMessage() + "\r\n" + new Date( System.currentTimeMillis() ) + "\r\n" );
    t.printStackTrace();

    displayError( "Error performing operation." );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayError( Throwable e )
  {
    displayMessageBox( e == null ? "Error performing operation." : e.getMessage(), JOptionPane.ERROR_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayError( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.ERROR_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayErrorNoWrap( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.ERROR_MESSAGE, false, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayWarning( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.WARNING_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayInformation( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.INFORMATION_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayMessageBox( String strMsg, int iType, boolean bNonModalMsg )
  {
    displayMessageBox( strMsg, iType, bNonModalMsg, true );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayMessageBox( String strMsg, final int iType, final boolean bNonModalMsg, boolean bWrapText )
  {
    final String strWrappedMsg = bWrapText ? StudioUtilities.wrapText( strMsg ) : strMsg;

    Runnable logMsgBox = new Runnable()
    {
      public void run()
      {
        if( !bNonModalMsg )
        {
          MessageBox.showMessageDialog( strWrappedMsg, iType );
        }
        else
        {
          showMessageNonModal( strWrappedMsg, iType );
        }
      }
    };

    if( EventQueue.isDispatchThread() )
    {
      logMsgBox.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( logMsgBox );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }
  }

  public static String getLastTestError()
  {
    return _testError;
  }

  public static void clearTestError()
  {
    _testError = null;
  }

  //------------------------------------------------------------------------
  public static void showMessageNonModal( String strMsg )
  {
    showMessageNonModal( strMsg, JOptionPane.INFORMATION_MESSAGE );
  }

  //------------------------------------------------------------------------
  public static void showMessageNonModal( String strMsg, int iType )
  {
    JOptionPane pane = new JOptionPane( StudioUtilities.wrapText( strMsg ), iType );
    JDialog dialog = pane.createDialog( null, "" );
    dialog.setModal( false );
    dialog.show();

    // Must set dialog back to modal after it displays because swing recycles the dialog
    // for ALL modal dialogs (!)
    dialog.setModal( true );
  }

  //--------------------------------------------------------------------------------------------------
  public static int displayConfirmation( String strMsg, int iButtonsType )
  {
    return displayConfirmation( strMsg, iButtonsType, null );
  }

  //--------------------------------------------------------------------------------------------------
  public static int displayConfirmation( String strMsg, int iButtonsType, String[] astrButtonLabels )
  {
    return displayConfirmation( strMsg, iButtonsType, JOptionPane.QUESTION_MESSAGE, astrButtonLabels, null );
  }

  public static int displayConfirmation( String strMsg, int iButtonsType, String[] astrButtonLabels, Point loc )
  {
    return displayConfirmation( strMsg, iButtonsType, JOptionPane.QUESTION_MESSAGE, astrButtonLabels, loc );
  }

  //--------------------------------------------------------------------------------------------------
  public static int displayConfirmation( String strMsg, final int iButtonsType, final int iType, final String[] astrButtonLabels, final Point loc )
  {
    final String strWrappedMsg = StudioUtilities.wrapText( strMsg );
    final int[] aiResult = new int[1];
    Runnable confirmationBox = new Runnable()
    {
      public void run()
      {
        aiResult[0] = MessageBox.showConfirmDialog( getFrame(), strWrappedMsg, iButtonsType, iType, loc, astrButtonLabels );
      }
    };

    if( EventQueue.isDispatchThread() )
    {
      confirmationBox.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( confirmationBox );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }

    return aiResult[0];
  }

  public static Point getLastMessageLocation()
  {
    return MessageBox.getLastMessageLocation();
  }

  //--------------------------------------------------------------------------------------------------
  public static JFrame getFrame()
  {
    if( StudioUtilities.getActiveWindow() instanceof JFrame )
    {
      return (JFrame)StudioUtilities.getActiveWindow();
    }
    else
    {
      return null;
    }
  }

  //--------------------------------------------------------------------------------------------------
  public static void centerWindowInFrame( Window window, Frame frame )
  {
    StudioUtilities.centerWindowInFrame( window, frame );
  }
}
