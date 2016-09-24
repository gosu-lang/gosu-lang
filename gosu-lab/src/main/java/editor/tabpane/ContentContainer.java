package editor.tabpane;

import editor.search.StudioUtilities;
import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 */
public class ContentContainer extends JPanel
{
  private TabPane _tabPane;

  public ContentContainer( TabPane tabPane )
  {
    super( new GridBagLayout() );
    _tabPane = tabPane;
    configUi();
  }

  private void configUi()
  {
    setBorder( new ContentInnerBorder() );
  }

  public Component add( Component c )
  {
    add( c, getContentPaneConstraints() );
    return c;
  }

  public void remove( Component c )
  {
    _tabPane.removeTabWithContent( (JComponent)c );
  }

  void removeDirect( Component c )
  {
    super.remove( c );

    // FIXME reset layout if component count goes to zero.  This clears
    // a strong reference to the last component held by the layout manager.
    // Should probably use CardLayout instead.
    if( getComponentCount() == 0 )
    {
      setLayout( new GridBagLayout() );
    }
  }

  GridBagConstraints getContentPaneConstraints()
  {
    return
      new GridBagConstraints(
        0, 0,
        GridBagConstraints.REMAINDER,
        GridBagConstraints.REMAINDER,
        1, 1,
        GridBagConstraints.CENTER,
        GridBagConstraints.BOTH,
        new Insets( 0, 0, 0, 0 ),
        0, 0
      );
  }

  private class ContentInnerBorder implements Border
  {
    private Insets _insets;

    ContentInnerBorder()
    {
    }

    public Color getColor()
    {
      if( StudioUtilities.containsFocus( ContentContainer.this ) )
      {
        return EditorUtilities.ACTIVE_CAPTION;
      }
      return EditorUtilities.CONTROL;
    }

    public boolean isBorderOpaque()
    {
      return true;
    }

    public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
    {
      Insets insets = getBorderInsets( c );
      g.translate( x, y );
      Color borderColor = getColor();
      if( borderColor != null )
      {
        g.setColor( borderColor );
        g.fillRect( 0, 0, width - insets.right, insets.top );
        g.fillRect( 0, insets.top, insets.left, height - insets.top );
        g.fillRect( insets.left, height - insets.bottom, width - insets.left, insets.bottom );
        g.fillRect( width - insets.right, 0, insets.right, height - insets.bottom );
        if( _tabPane.isTopBorderOnly() )
        {
          g.setColor( EditorUtilities.CONTROL_SHADOW );
          TabPosition tp = _tabPane.getTabContainer().getTabPosition();
          if( tp == TabPosition.TOP ||
              tp == TabPosition.BOTTOM )
          {
            g.drawLine( 0, 0, 0, 1 );
            g.drawLine( getWidth() - 1, 0, getWidth() - 1, 1 );
          }
          else
          {
            g.drawLine( 0, 0, 1, 0 );
            g.drawLine( 0, getHeight() - 1, 1, getHeight() - 1 );
          }
        }
      }
      g.translate( -x, -y );
    }

    public Insets getBorderInsets( Component c )
    {
      return _insets == null ? initInsets() : _insets;
    }

    private Insets initInsets()
    {
      TabPosition tp = _tabPane.getTabContainer().getTabPosition();
      if( tp == TabPosition.TOP )
      {
        _insets = new Insets( 2, 0, 0, 0 );
      }
      else if( tp == TabPosition.BOTTOM )
      {
        _insets = new Insets( 0, 0, 2, 0 );
      }
      else if( tp == TabPosition.LEFT )
      {
        _insets = new Insets( 0, 2, 0, 0 );
      }
      else
      {
        _insets = new Insets( 0, 0, 0, 2 );
      }
      return _insets;
    }
  }
}
