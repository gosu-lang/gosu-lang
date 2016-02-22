package editor;

import javax.swing.*;
import java.awt.*;

/**
 */
public class SplashScreen
{
  private ImagePanel _splash;

  private static final SplashScreen SPLASH = new SplashScreen();

  public static SplashScreen instance()
  {
    return SPLASH;
  }

  private SplashScreen()
  {
    if( GraphicsEnvironment.isHeadless() || ( System.getProperty( "gosu.nosplash" ) != null ) )
    {
      return;
    }
    _splash = ImagePanel.createSplashImagePanel( "images/splash.png" );
    _splash.setTextRect( new Rectangle( 155, 215, 200, 40 ) );
    _splash.setTextColor( Color.BLACK );
  }

  public void setFeedbackText( String strText )
  {
    if( _splash != null )
    {
      _splash.setText( strText );
    }
  }

  public void dispose()
  {
    if (_splash == null) {
      return;
    }
    SwingUtilities.windowForComponent( _splash ).dispose();
    _splash = null;
  }

  public void setVisible( boolean bVisible )
  {
    if (_splash == null) {
      return;
    }
    SwingUtilities.windowForComponent( _splash ).setVisible( bVisible );
  }
}
