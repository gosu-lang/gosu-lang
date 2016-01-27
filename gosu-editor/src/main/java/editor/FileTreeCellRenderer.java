package editor;

import editor.util.EditorUtilities;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 */
public class FileTreeCellRenderer extends JLabel implements TreeCellRenderer
{
  private boolean _bSelected;
  private FileTree _node;
  private Color _color;
  private JTree _tree;

  public FileTreeCellRenderer( JTree tree )
  {
    _tree = tree;
  }

  public Component getTreeCellRendererComponent( JTree tree, Object value,
                                                 boolean bSelected, boolean bExpanded,
                                                 boolean bLeaf, int iRow, boolean bHasFocus )
  {
    if( value != null )
    {
      _node = (FileTree)value;
      _bSelected = bSelected;
      configure();
    }

    return this;
  }

  public void update()
  {
    _tree.repaint();
  }

  public void configure()
  {
    _color = UIManager.getColor( "textText" );

    if( _node == null )
    {
      return;
    }

    TypeSystem.lock();
    try
    {
      if( _node.isDirectory() || !_node.isFile() && _node.getFileOrDir().getName().indexOf( '.' ) < 0 )
      {
        setText( _node.getName() );
      }
      else
      {
        IType type = _node.getType();
        setText( type == null ? _node.getName() : type.getRelativeName() );
      }
      setIcon( _node.getIcon() );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  /** */
  public void paint( Graphics g )
  {
//    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
//                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
//    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_RENDERING,
//                                      RenderingHints.VALUE_RENDER_QUALITY );
    Color bkColor;

    boolean bFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == _tree;
    if( _bSelected )
    {
      bkColor = _tree.isEnabled() && bFocus
                ? UIManager.getColor( "textHighlight" )
                : SystemColor.control;
    }
    else
    {
      bkColor = _tree.getBackground();
      if( bkColor == null )
      {
        bkColor = getBackground();
      }
    }

    int textXOffset;
    Icon currentIcon = getIcon();
    if( currentIcon != null && getText() != null )
    {
      textXOffset = (currentIcon.getIconWidth() + getIconTextGap() - 1);
    }
    else
    {
      textXOffset = 0;
    }

    if( bkColor != null )
    {
      g.setColor( bkColor );
      if( currentIcon != null && getText() != null )
      {
        g.fillRect( textXOffset, 0, getWidth() - 1 - textXOffset, getHeight() - 1 );

        if( _bSelected && _tree.hasFocus() )
        {
          g.setColor( _tree.isEnabled() && bFocus ? UIManager.getColor( "textHighlightText" ) : SystemColor.controlLtHighlight );
          BasicGraphicsUtils.drawDashedRect( g, textXOffset, 0, getWidth() - 1 - textXOffset, getHeight() - 1 );
        }

      }
      else
      {
        g.fillRect( textXOffset, 0, getWidth() - 1, getHeight() - 1 );

        if( _bSelected && _tree.hasFocus() )
        {
          g.setColor( _tree.isEnabled() && bFocus ? UIManager.getColor( "textHighlightText" ) : SystemColor.controlLtHighlight );
          BasicGraphicsUtils.drawDashedRect( g, textXOffset, 0, getWidth() - 1, getHeight() - 1 );
        }
      }
      g.setColor( bkColor );
    }

    setForeground( _bSelected
                   ? _tree.isEnabled() && bFocus
                     ? UIManager.getColor( "textHighlightText" )
                     : _color
                   : _color );
    super.paint( g );
  }

  public Dimension getPreferredSize()
  {
    Dimension dim = super.getPreferredSize();

    if( dim != null )
    {
      dim = new Dimension( dim.width + 3, dim.height );
    }

    return dim;
  }
}
