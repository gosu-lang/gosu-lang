package editor.util;

import editor.search.StudioUtilities;

import javax.swing.*;
import java.awt.*;

public class ProgressWindow extends JWindow
{
  private Window _window;

  public ProgressWindow( ProgressPanel panel )
  {
    super( getFrameWindow() );
    configUI( panel );
  }

  private static Frame getFrameWindow()
  {
    Window activeWindow = StudioUtilities.getActiveWindow();
    if( activeWindow == null )
    {
      return getActiveFrameFromApp();
    }
    if( activeWindow instanceof Frame )
    {
      return (Frame)activeWindow;
    }
    return (Frame)activeWindow.getOwner();
  }

  private static Frame getActiveFrameFromApp()
  {
    Frame[] frames = Frame.getFrames();
    for( int i = 0; i < frames.length; i++ )
    {
      Frame frame = frames[i];
      if( frame.isShowing() )
      {
        return frame;
      }
    }
    return null;
  }

  private void configUI( ProgressPanel panel )
  {
    ContainerMoverSizer contentPane = new ContainerMoverSizer( null );
    contentPane.setLayout( new BorderLayout() );
    contentPane.setBackground( SystemColor.window );
    setContentPane( contentPane );
    add( panel, BorderLayout.CENTER );
  }

  public void show()
  {
    pack();
    /*
    if( getWidth() < 400 )
    {
      setSize( 400, getHeight() );
    }
    */

    StudioUtilities.centerWindowInFrame( this, getOwner() );
    super.show();

    _window = StudioUtilities.getActiveWindow();
    if( _window != null )
    {
      _window.setEnabled( false );
    }
  }

  public void dispose()
  {
    if( _window != null )
    {
      _window.setEnabled( true );
    }

    setVisible( false );
    super.dispose();
  }

}
