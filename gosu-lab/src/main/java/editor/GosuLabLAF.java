package editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;

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
      UIManager.put( "scrollbar", Scheme.active().getControl() ); /* Scrollbar background (usually the "track") */
      UIManager.put( "ScrollBar.background", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.foreground", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.trackHighlight", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.track", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.trackForeground", Scheme.active().getControl() );
//      "ScrollBar.trackHighlight", black,
      UIManager.put( "ScrollBar.trackHighlightForeground", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.thumb", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.thumbHighlight", Scheme.active().getScrollbarBorderColor() );
      UIManager.put( "ScrollBar.thumbDarkShadow", Scheme.active().getControl() );
      UIManager.put( "ScrollBar.thumbShadow", Scheme.active().getScrollbarBorderColor() ); //new Color(210, 235, 251 ) ); //EditorUtilities.CONTROL );
      UIManager.put( "ScrollBar.border", null );

      UIManager.put( "Separator.foreground", Scheme.active().getSeparator1() );
      UIManager.put( "Separator.background", Scheme.active().getSeparator2() );

      // Menus
      UIManager.put( "MenuBar.border", BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );
      UIManager.put( "MenuBar.background", Scheme.active().getMenu() );

      UIManager.put( "PopupMenu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "PopupMenu.foreground", Scheme.active().getMenuText() );
      UIManager.put( "PopupMenu.background", Scheme.active().getMenu() );
      UIManager.put( "Popup.foreground", Scheme.active().getMenuText() );
      UIManager.put( "Popup.background", Scheme.active().getMenu() );

      UIManager.put( "menu", Scheme.active().getMenu() ); /* Background color for menus */
      UIManager.put( "menuText", Scheme.active().getMenuText() ); /* Text color for menus  */
      UIManager.put( "Menu.foreground", Scheme.active().getMenuText() );
      UIManager.put( "Menu.background", Scheme.active().getMenu() );
      UIManager.put( "Menu.selectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.selectionBackground", Scheme.active().getActiveCaption() );
      UIManager.put( "Menu.disabledForeground", Scheme.active().getControlShadow() );
      UIManager.put( "Menu.acceleratorForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      UIManager.put( "Menu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      UIManager.put( "Menu.margin", Scheme.active().getWindowText() );
      UIManager.put( "menuPressedItemB", Scheme.active().getTextHighlight() ); /* LightShadow of menubutton highlight */
      UIManager.put( "menuPressedItemF", Scheme.active().getTextHighlightText() ); /* Default color for foreground "text" in menu item */

      UIManager.put( "MenuItem.foreground", Scheme.active().getMenuText() );
      UIManager.put( "MenuItem.background", Scheme.active().getMenu() );
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


      UIManager.put( "List.foreground", Scheme.active().getWindowText() );
      UIManager.put( "List.background", Scheme.active().getWindow() );

      UIManager.put( "Tree.foreground", Scheme.active().getWindowText() );
      UIManager.put( "Tree.background", Scheme.active().getWindow() );

      UIManager.put( "Table.foreground", Scheme.active().getWindowText() );
      UIManager.put( "Table.background", Scheme.active().getWindow() );

      UIManager.put( "ComboBox.foreground", Scheme.active().getWindowText() );
      UIManager.put( "ComboBox.background", Scheme.active().getWindow() );
      UIManager.put( "ComboBox.disabledForeground", Scheme.active().getControlShadow() );
      UIManager.put( "ComboBox.disabledBackground", Scheme.active().getControl() );
      UIManager.put( "ComboBox.buttonBackground", Scheme.active().getControl() );
      UIManager.put( "ComboBox.buttonShadow", Scheme.active().getControlShadow() );
      UIManager.put( "ComboBox.buttonDarkShadow", Scheme.active().getControlDarkshadow() );
      UIManager.put( "ComboBox.buttonHighlight", Scheme.active().getControl() );

      UIManager.put( "TextField.foreground", Scheme.active().getWindowText() );
      UIManager.put( "TextField.background", Scheme.active().getWindow() );
      UIManager.put( "TextField.caretForeground", Scheme.active().getWindowText() );
      
      UIManager.put( "TextArea.foreground", Scheme.active().getWindowText() );
      UIManager.put( "TextArea.background", Scheme.active().getWindow() );
      UIManager.put( "TextArea.caretForeground", Scheme.active().getWindowText() );
      
      UIManager.put( "TextPane.foreground", Scheme.active().getWindowText() );
      UIManager.put( "TextPane.background", Scheme.active().getWindow() );
      UIManager.put( "TextPane.caretForeground", Scheme.active().getWindowText() );
      
      UIManager.put( "Panel.foreground", Scheme.active().getControlText() );
      UIManager.put( "Panel.background", Scheme.active().getControl() );

      UIManager.put( "ScrollPane.background", Scheme.active().getControl() );

      UIManager.put( "EditorPane.caretForeground", Scheme.active().getWindowText() );
        
      UIManager.put( "Label.foreground", Scheme.active().getControlText() );
      UIManager.put( "Label.background", Scheme.active().getControl() );

      UIManager.put( "CheckBox.foreground", Scheme.active().getControlText() );
      UIManager.put( "CheckBox.background", Scheme.active().getControl() );

      UIDefaults table = UIManager.getLookAndFeelDefaults();
      UIManager.put( "ToolBar.background", Scheme.active().getMenu() );
      UIManager.put( "ToolBar.border", BorderFactory.createEmptyBorder() );
      table.put( "ToolBar.highlight", Scheme.active().getSeparator1() );
      table.put( "ToolBar.shadow", Scheme.active().getSeparator2() );

      UIManager.put( "RadioButton.foreground", Scheme.active().getControlText() );
      UIManager.put( "RadioButton.background", Scheme.active().getControl() );
      UIManager.put( "RadioButton.interiorBackground", Scheme.active().getControl() );
      UIManager.put( "RadioButton.shadow", Scheme.active().getButtonBorderColor() );
      UIManager.put( "RadioButton.darkShadow", Scheme.active().getControl() );
      UIManager.put( "RadioButton.light", Scheme.active().getControl() );
      UIManager.put( "RadioButton.highlight", Scheme.active().getButtonBorderColor() );

      UIManager.put( "CheckBox.foreground", Scheme.active().getControlText() );
      UIManager.put( "CheckBox.background", Scheme.active().getControl() );
      UIManager.put( "CheckBox.interiorBackground", Scheme.active().getControl() );
      UIManager.put( "CheckBox.shadow", Scheme.active().getButtonBorderColor() );
      UIManager.put( "CheckBox.darkShadow", Scheme.active().getControl() );
      UIManager.put( "CheckBox.light", Scheme.active().getControl() );
      UIManager.put( "CheckBox.highlight", Scheme.active().getButtonBorderColor() );

      table.put( "Button.foreground", Scheme.active().getControlText() );
      table.put( "Button.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "Button.disabledShadow", Scheme.active().getControlShadow() );
      table.put( "Button.background", Scheme.active().getControl() );
      table.put( "Button.interiorBackground", Scheme.active().getControl() );
      table.put( "Button.shadow", Scheme.active().getControl() );
      table.put( "Button.darkShadow", Scheme.active().getControlDarkshadow() );
      table.put( "Button.light", Scheme.active().getControl() );
      table.put( "Button.highlight", Scheme.active().getControlDarkshadow() );

      UIManager.put( "desktop", Scheme.active().getControl() );
      UIManager.put( "activeCaption", Scheme.active().getActiveCaption() );
      UIManager.put( "activeCaptionText", Scheme.active().getActiveCaptionText() );
      UIManager.put( "activeCaptionBorder", Scheme.active().getControl() ); /* Border color for caption (title bar) window borders. */
      UIManager.put( "inactiveCaption", Scheme.active().getControlDarkshadow() ); /* Color for captions (title bars) when not active. */
      UIManager.put( "inactiveCaptionText", Scheme.active().getControl() ); /* Text color for text in inactive captions (title bars). */
      UIManager.put( "inactiveCaptionBorder", Scheme.active().getControl() ); /* Border color for inactive caption (title bar) window borders. */
      UIManager.put( "window", Scheme.active().getWindowBorder() ); /* Default color for the interior of windows */
      UIManager.put( "windowBorder", Scheme.active().getWindowBorder() ); /* ??? */
      UIManager.put( "windowText", Scheme.active().getWindowText() ); /* ??? */

      UIManager.put( "text", Scheme.active().getControl() ); /* Text background color */
      UIManager.put( "textText", Scheme.active().getControlText() ); /* Text foreground color */
      UIManager.put( "textHighlight", Scheme.active().getTextHighlight() ); /* Text background color when selected */
      UIManager.put( "textHighlightText", Scheme.active().getTextHighlightText() ); /* Text color when selected */
      UIManager.put( "textInactiveText", Scheme.active().getControlShadow() ); /* Text color when disabled */
      UIManager.put( "control", Scheme.active().getControl() ); /* Default color for controls (buttons, sliders, etc) */
      UIManager.put( "controlText", Scheme.active().getControlText() ); /* Default color for text in controls */
      UIManager.put( "controlHighlight", Scheme.active().getControl() );

            /*"controlHighlight", Scheme.active().getControl() );*/ /* Specular highlight (opposite of the shadow) */
      UIManager.put( "controlLtHighlight", Scheme.active().getControlLight() ); /* Highlight color for controls */
      UIManager.put( "controlShadow", Scheme.active().getControlDarkshadow() ); /* Shadow color for controls */
      UIManager.put( "controlDkShadow", Scheme.active().getControlDarkshadow() ); /* Dark shadow color for controls */
      UIManager.put( "info", Scheme.active().getTooltipBackground() ); /* ??? */
      UIManager.put( "infoText", Scheme.active().getTooltipText() );  /* ??? */
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
