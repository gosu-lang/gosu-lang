package editor;

import editor.util.SettleModalEventQueue;
import gw.util.GosuObjectUtil;

import javax.swing.undo.StateEditable;
import java.io.File;
import java.util.Hashtable;

/**
 */
public class TabSelectionHistoryItem implements StateEditable
{
  private NavigationHistory _tabHistory;
  private ITabHistoryContext _tabContext;
  private int _caretPos;
  private ITabHistoryContext _prevTabContext;
  private int _prevCaretPos;
  private boolean _bUndo;

  public TabSelectionHistoryItem( NavigationHistory tabHistory, GosuEditor prevTab, int prevCaretPos, GosuEditor selectedTab, int caretPos )
  {
    _tabHistory = tabHistory;
    _prevTabContext = _tabHistory.getTabHistoryHandler().makeTabContext( prevTab );
    _prevCaretPos = prevCaretPos;
    _tabContext = _tabHistory.getTabHistoryHandler().makeTabContext( selectedTab );
    _caretPos = caretPos;
    _bUndo = true;
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
      if( !GosuObjectUtil.equals( _prevTabContext, _tabContext ) )
      {
        assert _caretPos < 0 && _prevCaretPos < 0;
        _tabHistory.getTabHistoryHandler().selectTab( bUndo ? _prevTabContext : _tabContext );
      }
      else
      {
        if( bUndo )
        {
          GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().findTab( (File)_prevTabContext.getContentId() );
          _caretPos = editor.getEditor().getCaretPosition();
          editor.getEditor().setCaretPosition( _prevCaretPos );
        }
        else
        {
          GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().findTab( (File)_tabContext.getContentId() );
          editor.getEditor().setCaretPosition( _caretPos );
        }
      }

      SettleModalEventQueue.instance().run();
    }
    finally
    {
      _tabHistory.unlock();
    }
  }
}
