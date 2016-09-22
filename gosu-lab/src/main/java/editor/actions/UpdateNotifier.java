///////////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2002 Centrica Software
//
///////////////////////////////////////////////////////////////////////////////////////
package editor.actions;


import javax.swing.*;
import java.util.Iterator;
import java.util.WeakHashMap;


/**
 * The primary goal for this class is to enable/disable components relieving them
 * from the chore of monitoring select events to proactively enable/disable themselves.
 * <p/>
 * The idea is that in the end all components must respond to some sort of awt event
 * to selectively enable and disable themselves.  So why not simply monitor all events
 * and notify components when they should update themselves?  This way components are
 * always in the correct visual state.  Components only need to supply the conditions
 * upon which they are enabled via the GenericAction class.
 * <p/>
 * This class also provides for centralized application context state management i.e.,
 * display or hide user interface elements based on the applications context state[s].
 * <p/>
 * Note enabling/disabling UI command components in the dispatch loop is not a new
 * concept. I used to do something very similar in a prior life doing old-time Windows
 * API programming; in a Windows message loop one could do this sort of processing during
 * "idle" time. And now in .NET there is a similar facility that more or less does what
 * I have done here.
 */
public class UpdateNotifier
{
  /**
   * This is a singleton class.
   */
  static UpdateNotifier _singleton;

  private WeakHashMap _mapItems;

  /**
   * Enforce singleton access.
   */
  private UpdateNotifier()
  {
    _mapItems = new WeakHashMap();
  }

  /**
   * @return The singleton instance.
   */
  public static UpdateNotifier instance()
  {
    if( _singleton == null )
    {
      _singleton = new UpdateNotifier();
    }

    return _singleton;
  }

  /**
   * Adds a component to the enable/disable map.
   *
   * @param item A component for which the class controls the enable state.
   */
  public void addActionComponent( JComponent item )
  {
    if( !SwingUtilities.isEventDispatchThread() )
    {
      throw new RuntimeException( "Whoops!  UpdateNotifier is restricted to the EventDispatch thread." );
    }
    _mapItems.put( item, null );
  }

  /**
   * Removes a component from the enable/disable map.
   *
   * @param item The component to remove from the map.
   */
  public void removeActionComponent( JComponent item )
  {
    if( !SwingUtilities.isEventDispatchThread() )
    {
      throw new RuntimeException( "Whoops!  UpdateNotifier is restricted to the EventDispatch thread." );
    }
    _mapItems.remove( item );
  }

  public void notifyActionComponentsNow()
  {
    _notifyActionComponentsNow();
  }

  /**
   * Enables/Disables registered components.
   */
  protected void _notifyActionComponentsNow()
  {
    for( Iterator iter = _mapItems.keySet().iterator(); iter.hasNext(); )
    {
      JComponent item = (JComponent)iter.next();
//      if( !item.isShowing() )
//      {
//        continue;
//      }

      Action action = (Action)item.getClientProperty( GenericAction.ACTION_PROPERTY );
      if( action == null )
      {
        if( item instanceof AbstractButton )
        {
          action = ((AbstractButton)item).getAction();
        }

        if( action == null )
        {
          continue;
        }
      }

      boolean bEnabled = action.isEnabled();

//      long lTime = System.currentTime();
//      boolean bEnabled = action.isEnabled();
//      long diff = System.currentTime() - lTime;
//      if( diff > 50 )
//      {
//        //!! Uncomment following line and set a break point there to debug poor performing isEnabled() impl.
//        //action.isEnabled();
//
//        ConditionalActionHandler cah = null;
//        if( action instanceof GenericAction )
//        {
//          cah = ((GenericAction)action).getConditionalActionHandler();
//        }
//        System.out.println( "!!!!!!!!!!!!!!!!!!!!!!!\n" +
//                            "An action.isEnabled() impl needs to be optimized.\nIt took " + diff + "ms to complete.\n" +
//                            (cah == null ? "" : "ConditionalActionHandler: " + cah.getClass().getName() + "\n") +
//                            "Debug UpdateNotifier.notifyActionComponentsNow() to find the naughty code." +
//                            "\n!!!!!!!!!!!!!!!!!!!!!!!" );
//      }

      if( item.isEnabled() != bEnabled )
      {
        item.setEnabled( bEnabled );
      }


      // Also handle "selected" state for togglable :) items

      if( action instanceof GenericAction )
      {
        boolean bSelected = ((GenericAction)action).isSelected();
        if( item instanceof JToggleButton )
        {
          JToggleButton tb = (JToggleButton)item;
          if( tb.isSelected() != bSelected )
          {
            tb.setSelected( bSelected );
          }
        }
        else if( item instanceof JCheckBoxMenuItem )
        {
          JCheckBoxMenuItem tb = (JCheckBoxMenuItem)item;
          if( tb.getState() != bSelected )
          {
            tb.setState( bSelected );
          }
        }
      }
    }
  }
}
