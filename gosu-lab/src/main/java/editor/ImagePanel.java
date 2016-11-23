package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ImagePanel is a simple Container for displaying an arbitrary image and optionally
 * superimposing text on the image.
 */
public class ImagePanel extends JComponent
{
  ImageIcon _image;
  Dimension _minDim;
  Border _border;
  JLabel _label;


  public ImagePanel( String strImage )
  {
    super();

    setOpaque( false );

    _image = EditorUtilities.loadIcon( strImage );

    _label = new JLabel();
    setLayout( null );
  }

  public void paintComponent( Graphics g )
  {
    _image.paintIcon( this, g, getInsets().left, getInsets().top );
    g.translate( _label.getX(), _label.getY() );
    _label.paint( g );
  }

  public synchronized Dimension getMinimumSize()
  {
    if( _image == null )
    {
      return super.getMinimumSize();
    }

    Insets insets = _border == null ? new Insets( 0, 0, 0, 0 ) : _border.getBorderInsets( this );
    return _minDim == null ? new Dimension( _image.getIconWidth() + ((_border != null) ? (insets.left + insets.right) : 0),
                                            _image.getIconHeight() + ((_border != null) ? (insets.top + insets.bottom) : 0) )
           : _minDim;
  }

  public synchronized Dimension getPreferredSize()
  {
    return getMinimumSize();
  }

  public void setMinimumSize( Dimension dim )
  {
    _minDim = dim;
  }

  public synchronized Insets getInsets()
  {
    if( _border == null )
    {
      return super.getInsets();
    }

    return _border.getBorderInsets( this );
  }

  public ImageIcon getImageIcon()
  {
    return _image;
  }

  public void setImageIcon( ImageIcon image )
  {
    _image = image;
  }

  public Border getImageBorder()
  {
    return _border;
  }

  public Rectangle getClientRect()
  {
    if( _image == null )
    {
      return getBounds();
    }

    if( _border == null )
    {
      return new Rectangle( 0, 0, _image.getIconWidth(), _image.getIconHeight() );
    }

    Insets insets = _border.getBorderInsets( this );
    return new Rectangle( insets.left,
                          insets.top,
                          _image.getIconWidth(),
                          _image.getIconHeight() );
  }

  public void setTextColor( Color textColor )
  {
    _label.setForeground( textColor );
  }

  public Color getTextColor()
  {
    return _label.getForeground();
  }

  public void setTextRect( Rectangle rcText )
  {
    _label.setBounds( rcText );
  }

  public Rectangle getTextRect()
  {
    return _label.getBounds();
  }

  public void setText( String strText )
  {
    _label.setText( strText );
    repaintNow();
  }

  public void repaintNow()
  {
    Graphics g = getGraphics();
    paint( g );
    g.dispose();
  }

  public static ImagePanel createSplashImagePanel( String strImage )
  {
    ImagePanel imagePanel = new ImagePanel( strImage );
    imagePanel.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
    Window splash =
      new Window( new Frame() )
      {
        public void update( Graphics g )
        {
          this.paint( g );
        }
      };
    splash.setBackground( new Color( 0, 0, 0, 0 ) );
    splash.setLayout( new BorderLayout() );
    splash.add( BorderLayout.CENTER, imagePanel );
    splash.pack();

    EditorUtilities.centerWindowInFrame( splash, splash );
    splash.setVisible( true );
    imagePanel.repaintNow();

    return imagePanel;
  }

  public static ImagePanel createAboutImagePopup( String strImage )
  {
    ImagePanel imagePanel = new ImagePanel( strImage );
    final JPopupMenu splash = new JPopupMenu();
    splash.setOpaque( false );
    splash.setBackground( new Color( 0, 0, 0, 0 ) );
    splash.add( imagePanel );
    int iconWidth = imagePanel.getImageIcon().getIconWidth();
    int iconHeight = imagePanel.getImageIcon().getIconHeight();
    Point showDim;
    if( EditorUtilities.getActiveWindow() == null )
    {
      showDim = EditorUtilities.getXYForDialogRelativeToStudioFrame( iconWidth, iconHeight );
    }
    else
    {
      Dimension rootPaneSize = ((JFrame)EditorUtilities.getActiveWindow()).getRootPane().getSize();
      int x = (rootPaneSize.width - iconWidth) / 2;
      int y = (rootPaneSize.height - iconHeight) / 2;
      showDim = new Point( x, y );
    }

    splash.show( EditorUtilities.getActiveWindow() == null ? null : ((JFrame)EditorUtilities.getActiveWindow()).getRootPane(),
                 (int)showDim.getX(), (int)showDim.getY() );
    splash.addMouseListener(
      new MouseAdapter()
      {
        public void mouseClicked( MouseEvent event )
        {
          splash.setVisible( false );
        }
      } );

    return imagePanel;
  }
}
