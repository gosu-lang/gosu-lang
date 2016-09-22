package editor;

import editor.search.MessageDisplay;
import editor.util.EditorUtilities;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.statements.IFunctionStatement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Objects;

public class GosuClassLineInfoManager extends AbstractLineInfoManager
{
  private ImageIcon _iconOverride;
  private ImageIcon _iconImpl;
  private ImageIcon _iconOverrideAndImpl;

  public GosuClassLineInfoManager()
  {
    _iconOverride = EditorUtilities.loadIcon( "images/override.png" );
    _iconImpl = EditorUtilities.loadIcon( "images/impl.png" );
    _iconOverrideAndImpl = EditorUtilities.loadIcon( "images/override_and_impl.png" );
  }

  protected boolean isBreakpointAtLine( int iLine )
  {
    return getBreakpointAtLine( iLine ) != null;
  }

  protected Breakpoint getBreakpointAtLine( int iLine )
  {
    GosuEditor editor = getGosuEditor();
    if( editor == null )
    {
      return null;
    }

    return getBreakpointManager().getBreakpointAtEditorLine( editor.getScriptPart().getContainingTypeName(), iLine );
  }

  private GosuEditor getGosuEditor()
  {
    GosuEditor editor = getGosuPanel().getCurrentEditor();
    if( editor == null )
    {
      return null;
    }
    return editor;
  }

  protected boolean isExecPointAtLine( int iLine )
  {
    return getExecPointAtLine( iLine ) != null;
  }
  protected Breakpoint getExecPointAtLine( int iLine )
  {
    GosuEditor editor = getGosuEditor();
    if( editor == null )
    {
      return null;
    }
    return getBreakpointManager().getExecPointAtEditorLine( editor.getScriptPart().getContainingTypeName(), iLine );
  }

  protected boolean isFramePointAtLine( int iLine )
  {
    return getFramePointAtLine( iLine ) != null;
  }
  protected Breakpoint getFramePointAtLine( int iLine )
  {
    GosuEditor editor = getGosuEditor();
    if( editor == null )
    {
      return null;
    }
    return getBreakpointManager().getFramePointAtEditorLine( editor.getScriptPart().getContainingTypeName(), iLine );
  }

  @Override
  public Cursor getCursor( int iLine )
  {
    if( getSuperFunction( iLine ) != null || getOverridden( iLine ) != null )
    {
      return Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
    }
    return super.getCursor( iLine );
  }

  public void handleLineClick( MouseEvent e, int iLine, int iX, int iY )
  {
    IFunctionStatement overrideFunction = getSuperFunction( iLine );
    IParseTree implementedFuction = getOverridden( iLine );
    if( overrideFunction != null && (implementedFuction == null || iY < _iconOverrideAndImpl.getIconHeight() / 2) )
    {
      getGosuEditor().handleGotoFeature( overrideFunction.getDynamicFunctionSymbol().getSuperDfs().getMethodOrConstructorInfo() );
    }
    else if( implementedFuction != null )
    {
      MessageDisplay.displayInformation( "Men at work." );
      //getGosuEditor().handleGotoFeature( implementedFuction. );
    }
    else
    {
      GosuEditor editor = getGosuEditor();
      if( editor == null )
      {
        return;
      }
      if( e.isPopupTrigger() )
      {
        showContextMenu( e, iLine );
      }
      else
      {
        getBreakpointManager().toggleLineBreakpoint( editor.getScriptPart().getContainingTypeName(), iLine );
      }
    }
  }

  public void render( Graphics g, int iLine, int iLineHeight, int iX, int iY )
  {
    super.render( g, iLine, iLineHeight, iX, iY );
    boolean isOverridden = getSuperFunction( iLine ) != null;
    boolean isImplemented = getOverridden( iLine ) != null;
    if( isOverridden )
    {
      if( isImplemented )
      {
        g.drawImage( _iconOverrideAndImpl.getImage(), iX, iY + iLineHeight / 2 - _iconOverrideAndImpl.getIconHeight() / 2, _iconOverrideAndImpl.getIconWidth(), _iconOverrideAndImpl.getIconHeight(), null );
      }
      else
      {
        g.drawImage( _iconOverride.getImage(), iX, iY + iLineHeight / 2 - _iconOverride.getIconHeight() / 2, _iconOverride.getIconWidth(), _iconOverride.getIconHeight(), null );
      }
    }
    else if( isImplemented )
    {
      g.drawImage( _iconImpl.getImage(), iX, iY + iLineHeight / 2 - _iconImpl.getIconHeight() / 2, _iconImpl.getIconWidth(), _iconImpl.getIconHeight(), null );
    }
  }

  private IFunctionStatement getSuperFunction( int iLine )
  {
    GosuEditor editor = getGosuEditor();
    Map<Integer, IFunctionStatement> functionsByLine = editor.getFunctionsByLineNumber();
    IFunctionStatement functionStatement = functionsByLine.get( iLine );
    if( functionStatement != null )
    {
      IDynamicFunctionSymbol dfs = functionStatement.getDynamicFunctionSymbol();
      if( dfs != null && dfs.isOverride() )
      {
        return functionStatement;
      }
    }
    return null;
  }

  private IParseTree getOverridden( int iLine )
  {
    GosuEditor editor = getGosuEditor();
    IFunctionStatement fs = editor.getFunctionsByLineNumber().get( iLine );
    if( fs != null )
    {
      for( IDynamicFunctionSymbol dfs : editor.getOverriddenFunctions() )
      {
        if( Objects.equals( fs.getDynamicFunctionSymbol(), dfs ) && Objects.equals( fs.getDynamicFunctionSymbol().getScriptPart(), dfs.getScriptPart() ) )
        {
          return fs.getLocation();
        }
      }
    }
    return null;
  }
}
