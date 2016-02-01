package editor;

import java.awt.*;

public class ScriptEditorPopupHandler extends PopupHandler<IScriptEditor>
{
  public ScriptEditorPopupHandler( IScriptEditor scriptEditor, IContextMenuHandler<IScriptEditor> contextMenuHandler )
  {
    super( scriptEditor, contextMenuHandler );
  }

  @Override
  protected void handleRightClick( int iXPos, int iYPos, Component component )
  {
    Point pt = new Point( iXPos, iYPos );
    int iPosition = _owner.getEditor().viewToModel( pt );
    if( iPosition < 0 )
    {
      iPosition = _owner.getEditor().getDocument().getLength() - 1;
    }
    if( !isInSelection( iPosition ) )
    {
      _owner.getEditor().setCaretPosition( iPosition );
    }
    _owner.getEditor().requestFocus();
    super.handleRightClick( iXPos, iYPos, component );
  }

  private boolean isInSelection( int iPosition )
  {
    if( _owner.getEditor().getSelectionStart() == _owner.getEditor().getSelectionEnd() )
    {
      return false;
    }
    return iPosition >= _owner.getEditor().getSelectionStart() &&
           iPosition <= _owner.getEditor().getSelectionEnd();
  }
}