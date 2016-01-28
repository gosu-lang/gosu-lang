package editor.tabpane;

import editor.search.StudioUtilities;
import editor.splitpane.ICaptionActionListener;
import editor.splitpane.ICaptionBar;
import editor.util.EditorUtilities;
import editor.util.IEditableLabel;
import editor.util.ILabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;

/**
 */
public class StandardTab extends JPanel implements ITab
{
  private TabContainer _tabContainer;
  private JLabel _innerLabel;
  private JComponent _contentPanel;
  private ILabel _label;
  private boolean _bCanClose;
  private boolean _bHover;
  private StandardTab.TabPositionChangeHandler _tabPositionChangeHandler;

  public StandardTab( TabContainer tabContainer, ILabel labelAttr, JComponent contentPane )
  {
    this( tabContainer, labelAttr, contentPane, false );
  }

  public StandardTab( TabContainer tabContainer, ILabel labelAttr, JComponent contentPane, boolean bCanClose )
  {
    super();
    _label = labelAttr;
    if( _label instanceof IEditableLabel )
    {
      ((IEditableLabel)_label).addChangeListener( new LabelChangeListener() );
    }
    _bCanClose = bCanClose;
    setLayout( new BorderLayout() );
    setOpaque( false );
    setBackground( SystemColor.window );
    _tabContainer = tabContainer;
    _innerLabel = createInnerLabel( labelAttr );
    add( _innerLabel, BorderLayout.CENTER );
    _contentPanel = contentPane;
    setBorder( new StandardTabBorder() );
    _tabPositionChangeHandler = new TabPositionChangeHandler();
    tabContainer.addTabpositionListener(_tabPositionChangeHandler);
    addMouseListener();
  }

  protected JLabel createInnerLabel( ILabel labelAttr )
  {
    return new InnerLabel( this, labelAttr.getDisplayName(), labelAttr.getIcon( BeanInfo.ICON_COLOR_16x16 ), JLabel.RIGHT );
  }

  protected JLabel recreateInnerLabel()
  {
    return new InnerLabel( this, _innerLabel.getText(), _innerLabel.getIcon(), JLabel.RIGHT );
  }

  public TabPosition getTabPosition()
  {
    return _tabContainer.getTabPosition();
  }

  public ILabel getLabel()
  {
    return _label;
  }

  public JComponent getComponent()
  {
    return this;
  }

  public JComponent getContentPane()
  {
    return _contentPanel;
  }

  public Dimension getInnerSize()
  {
    return _innerLabel.getPreferredSize();
  }

  public boolean isSelected()
  {
    return _tabContainer != null && _tabContainer.getSelectedTab() == this;
  }

  public boolean isHover()
  {
    return _bHover;
  }
  public void setHover( boolean bHover )
  {
    _bHover = bHover;
  }

  public boolean canClose()
  {
    return _bCanClose;
  }

  public void dispose() {
    _tabContainer.removeTabpositionListener(_tabPositionChangeHandler);
  }

  public void refresh() {
    _innerLabel.setText( _label.getDisplayName() );
    _innerLabel.setIcon(getIcon());
    revalidate();
    repaint();
  }

  protected Icon getIcon() {
    return _label.getIcon( BeanInfo.ICON_COLOR_16x16 );
  }

  public Color getBackground()
  {
    return
      isSelected()
      ? getTabPane().isShowing() && getTabPane().isActive()
        ? EditorUtilities.ACTIVE_CAPTION
        : super.getBackground()
      : SystemColor.control;
  }

  protected void paintBorder( Graphics g )
  {
  }

  protected void paintComponent( Graphics g )
  {
    Graphics2D g2In = (Graphics2D)g;

    BufferedImage bi = g2In.getDeviceConfiguration().createCompatibleImage( getWidth(), getHeight(), Transparency.TRANSLUCENT );
    Graphics2D g2Image = getTransformedGraphics( g2In, bi );

    paintTab( g2Image );
    super.paintBorder( g2Image );

    if( g2Image != g2In )
    {
      g2Image.dispose();
      g2In.drawImage( bi, null, 0, 0 );
    }
  }

  protected Color getGradient()
  {
    return
      isSelected()
      ? getTabPane().isShowing() && getTabPane().isActive()
        ? EditorUtilities.ACTIVE_CAPTION
        : SystemColor.control //(Color)Utilities.getDesktopProperty( DesktopProperties.INACTIVE_GRADIENT )
      : isHover()
       ? SystemColor.control
       : SystemColor.controlShadow;
  }

  private Graphics2D getTransformedGraphics( Graphics2D g2, BufferedImage bi )
  {
    if( getTabPosition() == TabPosition.TOP )
    {
      return g2;
    }

    Graphics2D g2Image = bi.createGraphics();
    AffineTransform transform;
    if( getTabPosition() == TabPosition.BOTTOM )
    {
      transform = AffineTransform.getScaleInstance( 1, -1 );
      transform.translate( 0, 1 - getHeight() );
    }
    else if( getTabPosition() == TabPosition.LEFT )
    {
      transform = AffineTransform.getRotateInstance( -Math.PI / 2 );
      transform.translate( 1 - getHeight(), 0 );
    }
    else if( getTabPosition() == TabPosition.RIGHT )
    {
      transform = AffineTransform.getRotateInstance( Math.PI / 2 );
      transform.translate( 0, 1 - getWidth() );
    }
    else
    {
      throw new IllegalStateException( "Unknown TabPosition" );
    }
    g2Image.setTransform( transform );
    return g2Image;
  }

  protected void paintTab( Graphics2D g2Image )
  {
    Color grad = getGradient();
    Color backgnd = getBackground();

    float fGradientFactor;
    if( getTabPosition() == TabPosition.TOP ||
        getTabPosition() == TabPosition.BOTTOM )
    {
      fGradientFactor = (float)(getHeight() * ((isSelected() || isHover()) ? 1.0 : 1.75));
    }
    else
    {
      fGradientFactor = (float)(getWidth() * ((isSelected() || isHover()) ? 1.0 : 1.75));
    }
    GradientPaint gp = new GradientPaint( 0, 0, backgnd, 0, fGradientFactor, grad );
    g2Image.setPaint( gp );

    StandardTabBorder border = (StandardTabBorder)getBorder();
    Polygon poly = border.getBorderPoly( this );
    g2Image.fillPolygon( poly );

    super.paintComponent( g2Image );
  }

  private void addMouseListener()
  {
    addMouseListener(
      new MouseAdapter()
      {
        @Override
        public void mousePressed( MouseEvent e )
        {
          if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            ICaptionBar caption = getTabPane().getCaption();
            if (caption instanceof TabAndToolContainer) {
              ICaptionActionListener.ActionType action;
              if (caption.getCaptionType() == ICaptionActionListener.ActionType.MAXIMIZE) {
                action = ICaptionActionListener.ActionType.RESTORE;
              } else {
                action = ICaptionActionListener.ActionType.MAXIMIZE;
              }
              ((TabAndToolContainer) caption).fireCaptionActionPerformed(
                      action);
            }
          } else {
            // Set focus to the content panel. This is esp. important for the case
            // where the tab pane does not have focus and the active tab is
            // clicked.
            EventQueue.invokeLater(
              new Runnable()
              {
                public void run()
                {
                  if( !getContentPane().isShowing() )
                  {
                    return;
                  }
                  if( _tabContainer.getSelectedTab() == StandardTab.this &&
                      !StudioUtilities.containsFocus( getContentPane() ) )
                  {
                    getContentPane().transferFocus();
                  }
                }
              } );
          }
        }
      } );
  }

  TabPane getTabPane()
  {
    return (TabPane)_tabContainer.getClientProperty( TabPane.TAB_PANE );
  }

  private class TabPositionChangeHandler implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      remove( _innerLabel );
      _innerLabel = recreateInnerLabel();
      add( _innerLabel, BorderLayout.CENTER );
      revalidate();
      repaint();
    }
  }

  private class LabelChangeListener implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      refresh();
    }
  }
}
