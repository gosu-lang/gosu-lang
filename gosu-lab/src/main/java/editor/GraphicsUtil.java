package editor;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.io.Serializable;

class GraphicsUtil
{
  static void paintText( Graphics g, AbstractButton b, Rectangle textRect, String text, int textShiftOffset )
  {
    FontMetrics fm = SwingUtilities2.getFontMetrics( b, g );
    paintClassicText( b, g, textRect.x + textShiftOffset,
                      textRect.y + fm.getAscent() + textShiftOffset,
                      text, b.getDisplayedMnemonicIndex() );
  }

  private static void paintClassicText( AbstractButton b, Graphics g, int x, int y,
                                        String text, int mnemIndex )
  {
    ButtonModel model = b.getModel();

    if( model.isEnabled() )
    {
      if( !(b instanceof JMenuItem && model.isArmed()) &&
          !(b instanceof JMenu && (model.isSelected() || model.isRollover())) )
      {
        g.setColor( b.getForeground() );
      }
      SwingUtilities2.drawStringUnderlineCharAt( b, g, text, mnemIndex, x, y );
    }
    else
    {
      Color color = getDisabledTextColor( b );
      if( color == null )
      {
        color = UIManager.getColor( "Button.shadow" );
      }
      Color shadow = UIManager.getColor( "Button.disabledShadow" );
      if( model.isArmed() )
      {
        color = UIManager.getColor( "Button.disabledForeground" );
      }
      else
      {
        if( shadow == null )
        {
          shadow = b.getBackground().darker();
        }
        g.setColor( shadow );
        SwingUtilities2.drawStringUnderlineCharAt( b, g, text, mnemIndex,
                                                   x + 1, y + 1 );
      }
      if( color == null )
      {
        color = b.getBackground().brighter();
      }
      g.setColor( color );
      SwingUtilities2.drawStringUnderlineCharAt( b, g, text, mnemIndex, x, y );
    }
  }

  private static Color getDisabledTextColor( AbstractButton b )
  {
    if( b instanceof JCheckBox )
    {
      return UIManager.getColor( "CheckBox.disabledText" );
    }
    else if( b instanceof JRadioButton )
    {
      return UIManager.getColor( "RadioButton.disabledText" );
    }
    else if( b instanceof JToggleButton )
    {
      return UIManager.getColor( "ToggleButton.disabledText" );
    }
    else if( b instanceof JButton )
    {
      return UIManager.getColor( "Button.disabledText" );
    }
    return null;
  }

  static class CheckBoxIcon implements Icon, Serializable
  {
    static final int csize = 13;

    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      JCheckBox cb = (JCheckBox)c;
      ButtonModel model = cb.getModel();

      // outer bevel
      if( !cb.isBorderPaintedFlat() )
      {
        // Outer top/left
        g.setColor( UIManager.getColor( "CheckBox.shadow" ) );
        g.drawLine( x, y, x + 11, y );
        g.drawLine( x, y + 1, x, y + 11 );

        // Outer bottom/right
        g.setColor( UIManager.getColor( "CheckBox.highlight" ) );
        g.drawLine( x + 12, y, x + 12, y + 12 );
        g.drawLine( x, y + 12, x + 11, y + 12 );

        // Inner top.left
        g.setColor( UIManager.getColor( "CheckBox.darkShadow" ) );
        g.drawLine( x + 1, y + 1, x + 10, y + 1 );
        g.drawLine( x + 1, y + 2, x + 1, y + 10 );

        // Inner bottom/right
        g.setColor( UIManager.getColor( "CheckBox.light" ) );
        g.drawLine( x + 1, y + 11, x + 11, y + 11 );
        g.drawLine( x + 11, y + 1, x + 11, y + 10 );

        // inside box
        if( (model.isPressed() && model.isArmed()) || !model.isEnabled() )
        {
          g.setColor( UIManager.getColor( "CheckBox.background" ) );
        }
        else
        {
          g.setColor( UIManager.getColor( "CheckBox.interiorBackground" ) );
        }
        g.fillRect( x + 2, y + 2, csize - 4, csize - 4 );
      }
      else
      {
        g.setColor( UIManager.getColor( "CheckBox.shadow" ) );
        g.drawRect( x + 1, y + 1, csize - 3, csize - 3 );

        if( (model.isPressed() && model.isArmed()) || !model.isEnabled() )
        {
          g.setColor( UIManager.getColor( "CheckBox.background" ) );
        }
        else
        {
          g.setColor( UIManager.getColor( "CheckBox.interiorBackground" ) );
        }
        g.fillRect( x + 2, y + 2, csize - 4, csize - 4 );
      }

      if( model.isEnabled() )
      {
        g.setColor( UIManager.getColor( "CheckBox.foreground" ) );
      }
      else
      {
        g.setColor( UIManager.getColor( "CheckBox.shadow" ) );
      }

      // paint check
      if( model.isSelected() )
      {
        if( SwingUtilities2.isScaledGraphics( g ) )
        {
          int[] xPoints = {3, 5, 9, 9, 5, 3};
          int[] yPoints = {5, 7, 3, 5, 9, 7};
          g.translate( x, y );
          g.fillPolygon( xPoints, yPoints, 6 );
          g.drawPolygon( xPoints, yPoints, 6 );
          g.translate( -x, -y );
        }
        else
        {
          g.drawLine( x + 9, y + 3, x + 9, y + 3 );
          g.drawLine( x + 8, y + 4, x + 9, y + 4 );
          g.drawLine( x + 7, y + 5, x + 9, y + 5 );
          g.drawLine( x + 6, y + 6, x + 8, y + 6 );
          g.drawLine( x + 3, y + 7, x + 7, y + 7 );
          g.drawLine( x + 4, y + 8, x + 6, y + 8 );
          g.drawLine( x + 5, y + 9, x + 5, y + 9 );
          g.drawLine( x + 3, y + 5, x + 3, y + 5 );
          g.drawLine( x + 3, y + 6, x + 4, y + 6 );
        }
      }
    }

    public int getIconWidth()
    {
      return csize;
    }

    public int getIconHeight()
    {
      return csize;
    }
  }

  static class RadioButtonIcon implements Icon, UIResource, Serializable
  {
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      AbstractButton b = (AbstractButton)c;
      ButtonModel model = b.getModel();
      // fill interior
      if( (model.isPressed() && model.isArmed()) || !model.isEnabled() )
      {
        g.setColor( UIManager.getColor( "RadioButton.background" ) );
      }
      else
      {
        g.setColor( UIManager.getColor( "RadioButton.interiorBackground" ) );
      }
      g.fillRect( x + 2, y + 2, 8, 8 );


      boolean isScaledGraphics = SwingUtilities2.isScaledGraphics( g );

      if( isScaledGraphics )
      {

        Graphics2D g2d = (Graphics2D)g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke( new BasicStroke( 1.03f, BasicStroke.CAP_ROUND,
                                        BasicStroke.JOIN_ROUND ) );
        Object aaHint = g2d.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON );

        // outter left arc
        g.setColor( UIManager.getColor( "RadioButton.shadow" ) );
        g.drawArc( x, y, 11, 11, 45, 180 );
        // outter right arc
        g.setColor( UIManager.getColor( "RadioButton.highlight" ) );
        g.drawArc( x, y, 11, 11, 45, -180 );
        // inner left arc
        g.setColor( UIManager.getColor( "RadioButton.darkShadow" ) );
        g.drawArc( x + 1, y + 1, 9, 9, 45, 180 );
        // inner right arc
        g.setColor( UIManager.getColor( "RadioButton.light" ) );
        g.drawArc( x + 1, y + 1, 9, 9, 45, -180 );

        g2d.setStroke( oldStroke );

        if( model.isSelected() )
        {
          if( model.isEnabled() )
          {
            g.setColor( UIManager.getColor( "RadioButton.foreground" ) );
          }
          else
          {
            g.setColor( UIManager.getColor( "RadioButton.shadow" ) );
          }
          g.fillOval( x + 3, y + 3, 5, 5 );
        }
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );

      }
      else
      {

        // outter left arc
        g.setColor( UIManager.getColor( "RadioButton.shadow" ) );
        g.drawLine( x + 4, y + 0, x + 7, y + 0 );
        g.drawLine( x + 2, y + 1, x + 3, y + 1 );
        g.drawLine( x + 8, y + 1, x + 9, y + 1 );
        g.drawLine( x + 1, y + 2, x + 1, y + 3 );
        g.drawLine( x + 0, y + 4, x + 0, y + 7 );
        g.drawLine( x + 1, y + 8, x + 1, y + 9 );

        // outter right arc
        g.setColor( UIManager.getColor( "RadioButton.highlight" ) );
        g.drawLine( x + 2, y + 10, x + 3, y + 10 );
        g.drawLine( x + 4, y + 11, x + 7, y + 11 );
        g.drawLine( x + 8, y + 10, x + 9, y + 10 );
        g.drawLine( x + 10, y + 9, x + 10, y + 8 );
        g.drawLine( x + 11, y + 7, x + 11, y + 4 );
        g.drawLine( x + 10, y + 3, x + 10, y + 2 );


        // inner left arc
        g.setColor( UIManager.getColor( "RadioButton.darkShadow" ) );
        g.drawLine( x + 4, y + 1, x + 7, y + 1 );
        g.drawLine( x + 2, y + 2, x + 3, y + 2 );
        g.drawLine( x + 8, y + 2, x + 9, y + 2 );
        g.drawLine( x + 2, y + 3, x + 2, y + 3 );
        g.drawLine( x + 1, y + 4, x + 1, y + 7 );
        g.drawLine( x + 2, y + 8, x + 2, y + 8 );


        // inner right arc
        g.setColor( UIManager.getColor( "RadioButton.light" ) );
        g.drawLine( x + 2, y + 9, x + 3, y + 9 );
        g.drawLine( x + 4, y + 10, x + 7, y + 10 );
        g.drawLine( x + 8, y + 9, x + 9, y + 9 );
        g.drawLine( x + 9, y + 8, x + 9, y + 8 );
        g.drawLine( x + 10, y + 7, x + 10, y + 4 );
        g.drawLine( x + 9, y + 3, x + 9, y + 3 );


        // indicate whether selected or not
        if( model.isSelected() )
        {
          if( model.isEnabled() )
          {
            g.setColor( UIManager.getColor( "RadioButton.foreground" ) );
          }
          else
          {
            g.setColor( UIManager.getColor( "RadioButton.shadow" ) );
          }
          g.fillRect( x + 4, y + 5, 4, 2 );
          g.fillRect( x + 5, y + 4, 2, 4 );
        }
      }
    }

    public int getIconWidth()
    {
      return 13;
    }

    public int getIconHeight()
    {
      return 13;
    }
  }


  static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable
  {
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      AbstractButton b = (AbstractButton)c;
      ButtonModel model = b.getModel();
      boolean isSelected = model.isSelected();
      if( isSelected )
      {
        y = y - getIconHeight() / 2;
        g.drawLine( x + 9, y + 3, x + 9, y + 3 );
        g.drawLine( x + 8, y + 4, x + 9, y + 4 );
        g.drawLine( x + 7, y + 5, x + 9, y + 5 );
        g.drawLine( x + 6, y + 6, x + 8, y + 6 );
        g.drawLine( x + 3, y + 7, x + 7, y + 7 );
        g.drawLine( x + 4, y + 8, x + 6, y + 8 );
        g.drawLine( x + 5, y + 9, x + 5, y + 9 );
        g.drawLine( x + 3, y + 5, x + 3, y + 5 );
        g.drawLine( x + 3, y + 6, x + 4, y + 6 );
      }
    }

    public int getIconWidth()
    {
      return 9;
    }

    public int getIconHeight()
    {
      return 9;
    }
  }

  private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable
  {
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      AbstractButton b = (AbstractButton)c;
      ButtonModel model = b.getModel();
      if( b.isSelected() == true )
      {
        g.fillRoundRect( x + 3, y + 3, getIconWidth() - 6, getIconHeight() - 6,
                         4, 4 );
      }
    }

    public int getIconWidth()
    {
      return 12;
    }

    public int getIconHeight()
    {
      return 12;
    }
  }

}