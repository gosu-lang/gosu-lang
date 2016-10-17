package editor.splitpane;

import editor.tabpane.TabPosition;

import javax.swing.*;
import java.awt.*;

/**
 */
public class CollapsibleSplitPane extends SplitPane
{
  private ICaptionedPanel _topMin;
  private ICaptionedPanel _bottomMin;
  private ICaptionActionListener _captionActionListener;

  public CollapsibleSplitPane( int iOrientation, JComponent comp1, JComponent comp2 )
  {
    super( iOrientation, comp1, comp2 );
  }

  public void setTop( JComponent c )
  {
    super.setTop( c );
    listenToCaptionActions( c );
  }

  public void setBottom( JComponent c )
  {
    super.setBottom( c );
    listenToCaptionActions( c );
  }

  public void restorePane()
  {
    if( isMin() )
    {
      toggleCollapseNow( getTopMin() == null ? getBottomMin() : getTopMin() );
    }
  }

  public boolean isMin()
  {
    return getTopMin() != null || getBottomMin() != null;
  }

  public ICaptionedPanel getTopMin()
  {
    return _topMin;
  }

  protected void setTopMin( ICaptionedPanel topMin )
  {
    _topMin = topMin;
  }

  public ICaptionedPanel getBottomMin()
  {
    return _bottomMin;
  }

  protected void setBottomMin( ICaptionedPanel bottomMin )
  {
    _bottomMin = bottomMin;
  }

  public void hearBothSides()
  {
    listenToCaptionActions( _comp1 );
    listenToCaptionActions( _comp2 );
  }

  private void listenToCaptionActions( JComponent c )
  {
    ICaptionedPanel captionedPanel = findCaptionedPanel( c );
    if( captionedPanel == null )
    {
      return;
    }

    if( _captionActionListener == null )
    {
      _captionActionListener = new CaptionActionListener();
    }
    captionedPanel.getCaption().addCaptionActionListener( _captionActionListener );
  }

  private ICaptionedPanel findCaptionedPanel( Component c )
  {
    if( !(c instanceof Container) )
    {
      return null;
    }

    if( c instanceof SplitPane )
    {
      return null;
    }

    if( c instanceof ICaptionedPanel )
    {
      return (ICaptionedPanel)c;
    }

    Component[] children = ((Container)c).getComponents();
    for( int i = 0; i < children.length; i++ )
    {
      ICaptionedPanel titledPane = findCaptionedPanel( children[i] );
      if( titledPane != null )
      {
        return titledPane;
      }
    }

    return null;
  }

  public void toggleCollapse( final ICaptionedPanel captionedPanel )
  {
    EventQueue.invokeLater( () -> toggleCollapseNow( captionedPanel ) );
  }

  private void toggleCollapseNow( ICaptionedPanel captionedPanel )
  {
    if( isInTop( captionedPanel ) )
    {
      toggleTopCollapse( captionedPanel );
    }
    else if( isInBottom( captionedPanel ) )
    {
      toggleBottomCollapse( captionedPanel );
    }
    EventQueue.invokeLater(
      () -> {
        revalidate();
        doLayout();
        repaint();
      }
    );
  }

  private void toggleTopCollapse( ICaptionedPanel captionedPanel )
  {
    if( getTopMin() != null )
    {
      restore();
    }
    else
    {
      collapseTop( captionedPanel );
    }
  }

  private void toggleBottomCollapse( ICaptionedPanel captionedPanel )
  {
    if( getBottomMin() != null )
    {
      restore();
    }
    else
    {
      collapseBottom( captionedPanel );
    }
  }


  private void restore()
  {
    Component minBar = getComponent( 0 );
    if( !(minBar instanceof ICaptionBar) )
    {
      return;
    }
    ICaptionBar bar = (ICaptionBar)minBar;
    removeAll();
    addMainComponents( _comp1, _comp2 );
    setTopMin( null );
    setBottomMin( null );
    bar.setCaptionType( ICaptionActionListener.ActionType.RESTORE );

    ICaptionedPanel other = findCaptionedPanel( getTop() );
    if( other != null &&
        other.getCaption().getCaptionType() != ICaptionActionListener.ActionType.RESTORE )
    {
      other.getCaption().setCaptionType( ICaptionActionListener.ActionType.RESTORE );
    }
    else
    {
      other = findCaptionedPanel( getBottom() );
      if( other != null &&
          other.getCaption().getCaptionType() != ICaptionActionListener.ActionType.RESTORE )
      {
        other.getCaption().setCaptionType( ICaptionActionListener.ActionType.RESTORE );
      }
    }
  }

  private void collapseTop( ICaptionedPanel captionedPanel )
  {
    if( getBottomMin() != null )
    {
      setBottomMin( null );
    }

    ICaptionBar minLabel = makeMinLabel( captionedPanel, true );
    setTopMin( captionedPanel );

    removeAll();
    BorderLayout bl = new BorderLayout();
    setLayout( bl );

    if( getOrientation() == HORIZONTAL )
    {
      bl.setHgap( 4 );
      bl.setVgap( 4 );
      add( (Component)minLabel, BorderLayout.WEST );
    }
    else
    {
      bl.setVgap( 4 );
      bl.setHgap( 4 );
      add( (Component)minLabel, BorderLayout.NORTH );
    }
    ICaptionedPanel bottomCaptionedPanel = findCaptionedPanel( _comp2 );
    if( bottomCaptionedPanel != null )
    {
      bottomCaptionedPanel.getCaption().setCaptionType( ICaptionActionListener.ActionType.MAXIMIZE );
    }
    add( _comp2, BorderLayout.CENTER );
  }

  public void collapseBottom( ICaptionedPanel bottomCaptionedPanel )
  {
    if( getTopMin() != null )
    {
      setTopMin( null );
    }

    ICaptionBar minLabel = makeMinLabel( bottomCaptionedPanel, false );
    setBottomMin( bottomCaptionedPanel );

    removeAll();
    BorderLayout bl = new BorderLayout();
    setLayout( bl );

    int iGap = minLabel instanceof EmptyCaptionBar ? 0 : 4;
    String location = getOrientation() == HORIZONTAL ? BorderLayout.EAST : BorderLayout.SOUTH;
    bl.setHgap( iGap );
    bl.setVgap( iGap );
    add( (Component)minLabel, location );
    ICaptionedPanel topCaptionedPanel = findCaptionedPanel( _comp1 );
    if( topCaptionedPanel != null )
    {
      topCaptionedPanel.getCaption().setCaptionType( ICaptionActionListener.ActionType.MAXIMIZE );
    }
    add( _comp1, BorderLayout.CENTER );
  }

  private ICaptionBar makeMinLabel( final ICaptionedPanel captionedPanel, boolean bTop )
  {
    captionedPanel.getCaption().setCaptionType( ICaptionActionListener.ActionType.MINIMIZE );
    ICaptionBar bar = captionedPanel.getCaption()
      .getMinimizedPanel(
        getOrientation() == HORIZONTAL
        ? bTop
          ? TabPosition.LEFT
          : TabPosition.RIGHT
        : TabPosition.TOP
      );
    bar.setCaptionType( ICaptionActionListener.ActionType.MINIMIZE );
    return bar;
  }

  private boolean isInTop( ICaptionedPanel captionedPanel )
  {
    Component c = (Component)captionedPanel;
    while( c != _comp1 && c != null )
    {
      c = c.getParent();
    }

    return c == _comp1;
  }

  private boolean isInBottom( ICaptionedPanel captionedPanel )
  {
    Component c = (Component)captionedPanel;
    while( c != _comp2 && c != null )
    {
      c = c.getParent();
    }

    return c == _comp2;
  }

  private class CaptionActionListener implements ICaptionActionListener
  {
    public CaptionActionListener()
    {
    }

    public void captionActionPerformed( ICaptionedPanel captionedPanel, ActionType actionType )
    {
      if( actionType == ICaptionActionListener.ActionType.MINIMIZE )
      {
        toggleCollapse( captionedPanel );
      }
      else if( actionType == ICaptionActionListener.ActionType.RESTORE )
      {
        restore();
      }
      else if( actionType == ICaptionActionListener.ActionType.MAXIMIZE )
      {
        if( getTop() == captionedPanel ||
            getTop().isAncestorOf( (Component)captionedPanel ) )
        {
          ICaptionedPanel bottom = findCaptionedPanel( getBottom() );
          if( bottom != null )
          {
            toggleBottomCollapse( bottom );
          }
        }
        else
        {
          ICaptionedPanel top = findCaptionedPanel( getTop() );
          if( top != null )
          {
            toggleTopCollapse( top );
          }
        }
      }
      revalidate();
      repaint();
    }
  }
}
