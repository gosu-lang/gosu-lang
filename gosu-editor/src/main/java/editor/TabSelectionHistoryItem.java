package editor;

import javax.swing.undo.StateEditable;
import java.awt.*;
import java.util.Hashtable;

/**
 */
public class TabSelectionHistoryItem implements StateEditable
{
  private TabSelectionHistory _tabHistory;
  private ITabHistoryContext _tabContext;
  private ITabHistoryContext _prevTabContext;
  private boolean _bUndo;

  public TabSelectionHistoryItem( TabSelectionHistory tabHistory, GosuEditor selectedTab, GosuEditor prevTab )
  {
    _tabHistory = tabHistory;
    _prevTabContext = _tabHistory.getTabHistoryHandler().makeTabContext( prevTab );
    _tabContext = _tabHistory.getTabHistoryHandler().makeTabContext( selectedTab );
    _bUndo = false;
  }

  public void storeState( Hashtable stateTable )
  {
    stateTable.put( "_undo", _bUndo ? Boolean.TRUE : Boolean.FALSE );
    _bUndo = !_bUndo;
  }

  public void restoreState( Hashtable stateTable )
  {
    _tabHistory.lock();
    try
    {
      boolean bUndo = ((Boolean)stateTable.get( "_undo" )).booleanValue();
      _tabHistory.getTabHistoryHandler().selectTab( bUndo ? _prevTabContext : _tabContext );
    }
    finally
    {
      EventQueue.invokeLater(
        new Runnable()
        {
          public void run()
          {
            _tabHistory.unlock();
          }
        } );
    }
  }
}
