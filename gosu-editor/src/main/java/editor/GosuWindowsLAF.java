package editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import editor.util.PlatformUtil;

import javax.swing.*;

/**
 */
public class GosuWindowsLAF extends WindowsLookAndFeel
{
  public static void setLookAndFeel()
  {
    try
    {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public boolean isSupportedLookAndFeel()
  {
    return true;
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
