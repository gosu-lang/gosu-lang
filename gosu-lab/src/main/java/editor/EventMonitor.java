package editor;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 */
public class EventMonitor
{
  private static final EventMonitor INSTANCE = new EventMonitor();

  private boolean _bAltDown;
  private boolean _bShiftDown;
  private boolean _bCtrlDown;

  public static EventMonitor instance()
  {
    return INSTANCE;
  }

  private EventMonitor()
  {
    installKeyMonitor();
  }

  public boolean isAltDown()
  {
    return _bAltDown;
  }

  private void setAltDown( boolean bAltDown )
  {
    _bAltDown = bAltDown;
  }

  public boolean isShiftDown()
  {
    return _bShiftDown;
  }

  private void setShiftDown( boolean bShiftDown )
  {
    _bShiftDown = bShiftDown;
  }

  public boolean isCtrlDown()
  {
    return _bCtrlDown;
  }

  private void setCtrlDown( boolean bCtrlDown )
  {
    _bCtrlDown = bCtrlDown;
  }

  private void installKeyMonitor()
  {
    Toolkit.getDefaultToolkit().addAWTEventListener(
      new AWTEventListener()
      {
        public void eventDispatched( AWTEvent event )
        {
          if( event.getID() == KeyEvent.KEY_PRESSED ||
              event.getID() == KeyEvent.KEY_RELEASED )
          {
            KeyEvent ke = (KeyEvent)event;

            setCtrlDown( ke.isControlDown() );
            setAltDown( ke.isAltDown() );
            setShiftDown( ke.isShiftDown() );
          }
        }
      }, AWTEvent.KEY_EVENT_MASK );
  }

}
