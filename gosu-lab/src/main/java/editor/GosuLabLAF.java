package editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import editor.util.*;

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
      UIManager.put( "ScrollBar.background", EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.foreground", EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.trackHighlight", EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.thumb", EditorUtilities.CONTROL_LIGTH_SHADOW );
      UIManager.put( "ScrollBar.thumbHighlight", EditorUtilities.CONTROL_HIGHLIGHT );
      UIManager.put( "ScrollBar.thumbDarkShadow", EditorUtilities.CONTROL_HIGHLIGHT );
      UIManager.put( "ScrollBar.thumbShadow", EditorUtilities.CONTROL_HIGHLIGHT ); //new Color(210, 235, 251 ) ); //EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.border", null );

      UIManager.put( "Separator.foreground", EditorUtilities.CONTROL );
      UIManager.put( "Separator.background", EditorUtilities.CONTROL_LIGTH_SHADOW );

      // Menus
      UIManager.put( "MenuBar.border", BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );

      UIManager.put( "PopupMenu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.CONTROL_SHADOW ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "Popup.background", EditorUtilities.CONTROL );

      UIManager.put( "Menu.background", EditorUtilities.CONTROL );
      UIManager.put( "Menu.selectionForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "Menu.selectionBackground", EditorUtilities.ACTIVE_CAPTION );
      UIManager.put( "Menu.disabledForeground", EditorUtilities.CONTROL_SHADOW );
      UIManager.put( "Menu.acceleratorForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "Menu.acceleratorSelectionForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "Menu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.CONTROL_SHADOW ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );

      UIManager.put( "Menu.margin", EditorUtilities.WINDOW_TEXT );

      UIManager.put( "MenuItem.selectionForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "MenuItem.selectionBackground", EditorUtilities.ACTIVE_CAPTION );
      UIManager.put( "MenuItem.disabledForeground", EditorUtilities.CONTROL_SHADOW );
      UIManager.put( "MenuItem.acceleratorForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "MenuItem.acceleratorSelectionForeground", EditorUtilities.WINDOW_TEXT );
      UIManager.put( "MenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "MenuItem.disabledAreNavigable", false );
      //"MenuItem.acceleratorDelimiter", menuItemAcceleratorDelimiter,
      //"MenuItem.borderPainted", Boolean.FALSE,
      //"MenuItem.margin", twoInsets,

      UIManager.put( "ToolBar.border", new EmptyBorder( 0, 0, 0, 0 ) );

      UIManager.put( "List.background", EditorUtilities.WINDOW );

      UIManager.put( "Tree.background", EditorUtilities.WINDOW );

      UIManager.put( "Table.background", EditorUtilities.WINDOW );

      UIManager.put( "ComboBox.background", EditorUtilities.WINDOW );

      UIManager.put( "TextField.background", EditorUtilities.WINDOW );
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
