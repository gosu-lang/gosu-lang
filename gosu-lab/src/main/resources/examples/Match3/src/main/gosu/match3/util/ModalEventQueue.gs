package match3.util

uses java.awt.*
uses java.awt.event.MouseEvent

/**
 * Provides for ui modality using a local event queue to dispatch events while
 * a component is visible e.g., a frame or window.
 */
class ModalEventQueue implements Runnable {
  var  _modalHandler(): boolean
  var _disableMouse: boolean

/**
 * @param visibleComponent pkg.A visible component. The modal event queue remains
 *                         operable/modal while the component is visible.
 */
  construct( modalHandler(): boolean, visibleComponent: Component = null, disableMouse = false ) {
    if( modalHandler != null ) {
      _modalHandler = modalHandler
    }
    else {
      if( visibleComponent == null ) {
        throw new IllegalArgumentException( "One of: modalHandler or visibleComponent, must be non-null" )
      }
    }
    _disableMouse = disableMouse
  }

  override function run() {
    while( Modal ) {
      try {
        var event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent()
        dispatchEvent( event )
      }
      catch( e: Throwable ) {
        handleUncaughtException(e)
      }
    }
  }

  function handleUncaughtException( t: Throwable ) {
    throw new RuntimeException(t);
  }

  property get Modal() : boolean {
    return _modalHandler()
  }

  function dispatchEvent( event: AWTEvent ) {
    var src = event.getSource()

    if( _disableMouse && event typeis MouseEvent ) {
      return
    }

    if( event typeis ActiveEvent ) {
      event.dispatch()
    }
    else if( src typeis Component ) {
      src.dispatchEvent( event )
    }
    else if( src typeis MenuComponent ) {
      src.dispatchEvent( event )
    }
  }
}
