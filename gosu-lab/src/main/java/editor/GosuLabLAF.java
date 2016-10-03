package editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 */
public class GosuLabLAF extends WindowsLookAndFeel
{
  @Override
  public String getName()
  {
    return "Gosu L&F";
  }

  @Override
  public String getID()
  {
    return "GosuL&F";
  }

  @Override
  public String getDescription()
  {
    return "Gosu";
  }

  @Override
  public boolean isNativeLookAndFeel()
  {
    return false;
  }

  @Override
  public boolean isSupportedLookAndFeel()
  {
    return true;
  }

  public static void setLookAndFeel()
  {
    try
    {
      System.setProperty( "swing.noxp", "true" );

      UIManager.setLookAndFeel( GosuLabLAF.class.getName() );

      editor.util.FixupLookAndFeel.fixupFieldBorders();

      UIManager.put( "ScrollBar.width", 11 );
      UIManager.put( "ScrollBar.background", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.foreground", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.trackHighlight", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.thumb", Scheme.active().getControlLigthShadow() );
      UIManager.put( "ScrollBar.thumbHighlight", Scheme.active().getControlHighlight() );
      UIManager.put( "ScrollBar.thumbDarkShadow", Scheme.active().getControlHighlight() );
      UIManager.put( "ScrollBar.thumbShadow", Scheme.active().getControlHighlight() ); //new Color(210, 235, 251 ) ); //EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.border", null );

      UIManager.put( "Separator.foreground", Scheme.active().getControl() );
      UIManager.put( "Separator.background", Scheme.active().getControlLigthShadow() );

      // Menus
      UIManager.put( "MenuBar.border", BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );

      UIManager.put( "PopupMenu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getControlShadow() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "Popup.background", Scheme.active().getControl() );

      UIManager.put( "Menu.background", Scheme.active().getControl() );
      UIManager.put( "Menu.selectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.selectionBackground", Scheme.active().getActiveCaption() );
      UIManager.put( "Menu.disabledForeground", Scheme.active().getControlShadow() );
      UIManager.put( "Menu.acceleratorForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getControlShadow() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );

      UIManager.put( "Menu.margin", Scheme.active().getWindowText() );

      UIManager.put( "MenuItem.selectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "MenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      UIManager.put( "MenuItem.disabledForeground", Scheme.active().getControlShadow() );
      UIManager.put( "MenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      UIManager.put( "MenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "MenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "MenuItem.disabledAreNavigable", false );
      //"MenuItem.acceleratorDelimiter", menuItemAcceleratorDelimiter,
      //"MenuItem.borderPainted", Boolean.FALSE,
      //"MenuItem.margin", twoInsets,

      UIManager.put( "ToolBar.border", new EmptyBorder( 0, 0, 0, 0 ) );

      UIManager.put( "List.background", Scheme.active().getWindow() );

      UIManager.put( "Tree.background", Scheme.active().getWindow() );

      UIManager.put( "Table.background", Scheme.active().getWindow() );

      UIManager.put( "ComboBox.background", Scheme.active().getWindow() );

      UIManager.put( "TextField.background", Scheme.active().getWindow() );

      UIManager.put( "TextArea.background", Scheme.active().getWindow() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  protected void initSystemColorDefaults( UIDefaults table )
  {
    String ourLightColor = "#F1F0E8";
    String[] defaultSystemColors = {
      "desktop", "#005C5C", /* Color of the desktop background */
      "activeCaption", "#000080", /* Color for captions (title bars) when they are active. */
      "activeCaptionText", "#000000", /* Text color for text in captions (title bars). */
      "activeCaptionBorder", ourLightColor, /* Border color for caption (title bar) window borders. */
      "inactiveCaption", "#A0A0A0", /* Color for captions (title bars) when not active. */
      "inactiveCaptionText", ourLightColor, /* Text color for text in inactive captions (title bars). */
      "inactiveCaptionBorder", ourLightColor, /* Border color for inactive caption (title bar) window borders. */
      "window", "#FFFFFF", /* Default color for the interior of windows */
      "windowBorder", "#000000", /* ??? */
      "windowText", "#000000", /* ??? */
      "menu", ourLightColor, /* Background color for menus */
      "menuPressedItemB", "#000080", /* LightShadow of menubutton highlight */
      "menuPressedItemF", "#FFFFFF", /* Default color for foreground "text" in menu item */
      "menuText", "#000000", /* Text color for menus  */
      "text", ourLightColor, /* Text background color */
      "textText", "#000000", /* Text foreground color */
      "textHighlight", "#000080", /* Text background color when selected */
      "textHighlightText", "#FFFFFF", /* Text color when selected */
      "textInactiveText", "#808080", /* Text color when disabled */
      "control", ourLightColor, /* Default color for controls (buttons, sliders, etc) */
      "controlText", "#000000", /* Default color for text in controls */
      "controlHighlight", ourLightColor,

            /*"controlHighlight", ourLightColor,*/ /* Specular highlight (opposite of the shadow) */
      "controlLtHighlight", "#FFFFFF", /* Highlight color for controls */
      "controlShadow", "#A0A0A0", /* Shadow color for controls */
      "controlDkShadow", "#000000", /* Dark shadow color for controls */
      "scrollbar", ourLightColor, /* Scrollbar background (usually the "track") */
      "info", "#FFFFE1", /* ??? */
      "infoText", "#000000"  /* ??? */
    };

    loadSystemColors( table, defaultSystemColors, isNativeLookAndFeel() );
  }
}
