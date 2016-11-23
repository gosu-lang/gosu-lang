package editor;

import editor.debugger.Breakpoint;
import java.awt.event.MouseEvent;

public class SimpleLineInfoManager extends AbstractLineInfoManager
{
  protected boolean isBreakpointAtLine( int iLine )
  {
    return getBreakpointAtLine( iLine ) != null;
  }

  protected Breakpoint getBreakpointAtLine( int iLine )
  {
    return getBreakpointManager().getBreakpointAtEditorLine( getEditor().getScriptPart().getContainingTypeName(), iLine );
  }

  protected boolean isExecPointAtLine( int iLine )
  {
    return getExecPointAtLine( iLine ) != null;
  }

  protected Breakpoint getExecPointAtLine( int iLine )
  {
    return getBreakpointManager().getExecPointAtEditorLine( getEditor().getScriptPart().getContainingTypeName(), iLine );
  }

  protected boolean isFramePointAtLine( int iLine )
  {
    return getFramePointAtLine( iLine ) != null;
  }

  protected Breakpoint getFramePointAtLine( int iLine )
  {
    return getBreakpointManager().getFramePointAtEditorLine( getEditor().getScriptPart().getContainingTypeName(), iLine );
  }

  public void handleLineClick( MouseEvent e, int iLine, int iX, int iY )
  {
    if( e.isPopupTrigger() )
    {
      showContextMenu( e, iLine );
    }
    else
    {
      getBreakpointManager().toggleLineBreakpoint( getEditor(), getEditor().getScriptPart().getContainingTypeName(), iLine );
    }
  }
}
