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
 * Navigation history for goto-declaration operations and tab changes
 */
public class NavigationHistory
{
  public static final int MAX_MRU_HISTORY = 30;
  public static final int MAX_NAVIGATION_HISTORY = 100;

  private TabPane _tabPane;
  private AtomicUndoManager _undoMgr;
  private GosuEditor _prevTab;
  private boolean _bLocked;
  private ITabHistoryHandler _tabHistoryHandler;
  private List<ITabHistoryContext> _tabMruList;

  public NavigationHistory( TabPane tabPane )
  {
    _tabPane = tabPane;
    _undoMgr = new AtomicUndoManager( MAX_NAVIGATION_HISTORY );
    _tabMruList = new ArrayList<>();
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

  public List<ITabHistoryContext> getTabMruList()
  {
    return _tabMruList;
  }

  public GosuEditor getPreviousEditor()
  {
    return _prevTab;
  }

  public void dispose()
  {
    _undoMgr = new AtomicUndoManager( MAX_NAVIGATION_HISTORY );
    _tabMruList.clear();
    _prevTab = null;
  }

  public void resourceDeleted( String strTypeName )
  {
    for( Iterator<ITabHistoryContext> it = _tabMruList.iterator(); it.hasNext(); )
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
        new TabSelectionHistoryItem( NavigationHistory.this, _prevTab, -1, _prevTab = editor, -1 );
      StateEdit transaction = new StateEdit( undoItem, "tab selection" );
      transaction.end();
      _undoMgr.addEdit( transaction );
    }
  }

  public void addNavigationHistory( GosuEditor editor, int prevCaretPos, int currentCaretPos )
  {
    TabSelectionHistoryItem undoItem =
      new TabSelectionHistoryItem( this, editor, prevCaretPos, editor, currentCaretPos );
    StateEdit transaction = new StateEdit( undoItem, "navigation" );
    transaction.end();
    _undoMgr.addEdit( transaction );
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
    if( _tabMruList.contains( tabHistoryContext ) )
    {
      _tabMruList.remove( tabHistoryContext );
    }
    _tabMruList.add( 0, tabHistoryContext );
    if( _tabMruList.size() > MAX_MRU_HISTORY )
    {
      _tabMruList.remove( _tabMruList.size() - 1 );
    }
  }
}
