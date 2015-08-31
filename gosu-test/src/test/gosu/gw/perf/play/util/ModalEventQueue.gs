package play.util

uses java.awt.*
uses java.lang.Throwable
uses java.lang.RuntimeException
uses java.lang.Runnable

/**
 * Provides for ui modality using a local event queue to dispatch events while
 * a component is visible e.g., a frame or window.
 */
class ModalEventQueue implements Runnable {
    var  _modalHandler : block():boolean

/**
 * @param visibleComponent pkg.A visible component. The modal event queue remains
 *                         operable/modal while the component is visible.
 */
   construct( final visibleComponent: Component  ) {
     _modalHandler = \-> visibleComponent.Visible
   }

  construct( modalHandler: block():boolean  ) {
    _modalHandler = modalHandler;
  }

  override function run() {
    while( Modal ) {
      try {
        var event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent()
        dispatchEvent(event)
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
    if( event typeis ActiveEvent ) {
      event.dispatch()
    }
    else if( src typeis Component ) {
      src.dispatchEvent(event);
    }
    else if( src typeis MenuComponent ) {
      src.dispatchEvent(event);
    }
  }
}
