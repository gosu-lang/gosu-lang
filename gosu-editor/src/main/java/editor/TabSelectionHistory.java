package editor;

import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import editor.undo.AtomicUndoManager;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.StateEdit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class TabSelectionHistory
{
  public static final int MAX_MRU_HISTORY = 30;
  public static final int MAX_NAVIGATION_HISTORY = 100;

  private TabPane _tabPane;
  private AtomicUndoManager _undoMgr;
  private GosuEditor _prevTab;
  private boolean _bLocked;
  private ITabHistoryHandler _tabHistoryHandler;
  private List<ITabHistoryContext> _mruList;

  public TabSelectionHistory( TabPane tabPane )
  {
    _tabPane = tabPane;
    _undoMgr = new AtomicUndoManager( MAX_NAVIGATION_HISTORY );
    _mruList = new ArrayList<>();
    _tabPane.addSelectionListener( new TabSelectionHandler() );
  }

  public void goBackward()
  {
    if( canGoBackward() )
    {
      _undoMgr.undo();
    }
  }

  public void goForward()
  {
    if( canGoForward() )
    {
      _undoMgr.redo();
    }
  }

  public boolean canGoBackward()
  {
    return _undoMgr.canUndo();
  }

  public boolean canGoForward()
  {
    return _undoMgr.canRedo();
  }

  public TabPane getTabPane()
  {
    return _tabPane;
  }

  public ITabHistoryHandler getTabHistoryHandler()
  {
    return _tabHistoryHandler;
  }

  public void setTabHistoryHandler( ITabHistoryHandler tabHistoryHandler )
  {
    _tabHistoryHandler = tabHistoryHandler;
  }

  public List<ITabHistoryContext> getMruList()
  {
    return _mruList;
  }

  public GosuEditor getPreviousEditor()
  {
    return _prevTab;
  }

  public void dispose()
  {
    _undoMgr = new AtomicUndoManager( MAX_NAVIGATION_HISTORY );
    _mruList.clear();
    _prevTab = null;
  }

  public void resourceDeleted( String strTypeName )
  {
    for( Iterator<ITabHistoryContext> it = _mruList.iterator(); it.hasNext(); )
    {
      ITabHistoryContext iTabHistoryContext = it.next();
      if( iTabHistoryContext != null && iTabHistoryContext.getContentId().equals( strTypeName ) )
      {
        it.remove();
      }
    }
  }

  private class TabSelectionHandler implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      ITab selectedTab = _tabPane.getSelectedTab();
      GosuEditor editor = selectedTab == null ? null : (GosuEditor)selectedTab.getContentPane();
      if( editor != null )
      {
        addToMruList( _tabHistoryHandler.makeTabContext( editor ) );
      }

      if( _prevTab == editor )
      {
        return;
      }

      if( isLocked() )
      {
        _prevTab = editor;
        return;
      }

      if( !_undoMgr.canUndo() && _prevTab == null )
      {
        _prevTab = editor;
        return;
      }

      if( editor == null )
      {
        return;
      }

      TabSelectionHistoryItem undoItem =
        new TabSelectionHistoryItem( TabSelectionHistory.this, _prevTab, _prevTab = editor );
      StateEdit transaction = new StateEdit( undoItem, "tab selection" );
      transaction.end();
      _undoMgr.addEdit( transaction );
    }
  }

  void unlock()
  {
    _bLocked = false;
  }

  void lock()
  {
    _bLocked = true;
  }

  boolean isLocked()
  {
    return _bLocked;
  }

  private void addToMruList( ITabHistoryContext tabHistoryContext )
  {
    if( _mruList.contains( tabHistoryContext ) )
    {
      _mruList.remove( tabHistoryContext );
    }
    _mruList.add( 0, tabHistoryContext );
    if( _mruList.size() > MAX_MRU_HISTORY )
    {
      _mruList.remove( _mruList.size() - 1 );
    }
  }
}
