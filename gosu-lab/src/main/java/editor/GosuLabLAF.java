package editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import editor.util.EditorUtilities;
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

      UIDefaults table = UIManager.getLookAndFeelDefaults();

      table.put( "desktop", Scheme.active().getControl() );

      table.put( "activeCaption", Scheme.active().getActiveCaption() );
      table.put( "activeCaptionText", Scheme.active().getActiveCaptionText() );
      table.put( "activeCaptionBorder", Scheme.active().getControl() ); /* Border color for caption (title bar) window borders. */
      table.put( "inactiveCaption", Scheme.active().getControlDarkshadow() ); /* Color for captions (title bars) when not active. */
      table.put( "inactiveCaptionText", Scheme.active().getControl() ); /* Text color for text in inactive captions (title bars). */
      table.put( "inactiveCaptionBorder", Scheme.active().getControl() ); /* Border color for inactive caption (title bar) window borders. */

      table.put( "window", Scheme.active().getWindow() ); /* Default color for the interior of windows */
      table.put( "windowBorder", Scheme.active().getWindowBorder() ); /* ??? */
      table.put( "windowText", Scheme.active().getWindowText() ); /* ??? */

      table.put( "text", Scheme.active().getControl() ); /* Text background color */
      table.put( "textText", Scheme.active().getControlText() ); /* Text foreground color */
      table.put( "textHighlight", Scheme.active().getTextHighlight() ); /* Text background color when selected */
      table.put( "textHighlightText", Scheme.active().getTextHighlightText() ); /* Text color when selected */
      table.put( "textInactiveText", Scheme.active().getControlShadow() ); /* Text color when disabled */

      table.put( "control", Scheme.active().getControl() ); /* Default color for controls (buttons, sliders, etc) */
      table.put( "controlText", Scheme.active().getControlText() ); /* Default color for text in controls */
      table.put( "controlHighlight", Scheme.active().getControl() );
      table.put( "controlLtHighlight", Scheme.active().getControlLight() ); /* Highlight color for controls */
      table.put( "controlShadow", Scheme.active().getControlDarkshadow() ); /* Shadow color for controls */
      table.put( "controlDkShadow", Scheme.active().getControlDarkshadow() ); /* Dark shadow color for controls */

      table.put( "info", Scheme.active().getTooltipBackground() ); /* ??? */
      table.put( "infoText", Scheme.active().getTooltipText() );  /* ??? */
      table.put( "ScrollBar.width", 11 );
      table.put( "scrollbar", Scheme.active().getControl() ); /* Scrollbar background (usually the "track") */
      table.put( "ScrollBar.background", Scheme.active().getControl() );
      table.put( "ScrollBar.foreground", Scheme.active().getControl() );
      table.put( "ScrollBar.trackHighlight", Scheme.active().getControl() );
      table.put( "ScrollBar.track", Scheme.active().getControl() );
      table.put( "ScrollBar.trackForeground", Scheme.active().getControl() );
      table.put( "ScrollBar.trackHighlightForeground", Scheme.active().getControl() );
      table.put( "ScrollBar.thumb", Scheme.active().getControl() );
      table.put( "ScrollBar.thumbHighlight", Scheme.active().getScrollbarBorderColor() );
      table.put( "ScrollBar.thumbDarkShadow", Scheme.active().getControl() );
      table.put( "ScrollBar.thumbShadow", Scheme.active().getScrollbarBorderColor() ); //new Color(210, 235, 251 ) ); //EditorUtilities.CONTROL );
      table.put( "ScrollBar.border", null );

      table.put( "Separator.foreground", Scheme.active().getSeparator1() );
      table.put( "Separator.background", Scheme.active().getSeparator2() );

      // Menus
      table.put( "MenuBar.border", BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );
      table.put( "MenuBar.background", Scheme.active().getMenu() );

      table.put( "PopupMenu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "PopupMenu.foreground", Scheme.active().getMenuText() );
      table.put( "PopupMenu.background", Scheme.active().getMenu() );
      table.put( "Popup.foreground", Scheme.active().getMenuText() );
      table.put( "Popup.background", Scheme.active().getMenu() );

      table.put( "menu", Scheme.active().getMenu() ); /* Background color for menus */
      table.put( "menuText", Scheme.active().getMenuText() ); /* Text color for menus  */
      table.put( "Menu.foreground", Scheme.active().getMenuText() );
      table.put( "Menu.background", Scheme.active().getMenu() );
      table.put( "Menu.selectionForeground", Scheme.active().getWindowText() );
      table.put( "Menu.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "Menu.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "Menu.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "Menu.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "Menu.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "Menu.margin", Scheme.active().getWindowText() );
      table.put( "menuPressedItemB", Scheme.active().getTextHighlight() ); /* LightShadow of menubutton highlight */
      table.put( "menuPressedItemF", Scheme.active().getTextHighlightText() ); /* Default color for foreground "text" in menu item */

      table.put( "MenuItem.foreground", Scheme.active().getMenuText() );
      table.put( "MenuItem.background", Scheme.active().getMenu() );
      table.put( "MenuItem.selectionForeground", Scheme.active().getWindowText() );
      table.put( "MenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "MenuItem.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "MenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "MenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "MenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "MenuItem.disabledAreNavigable", false );
      
      table.put( "RadioButtonMenuItem.foreground", Scheme.active().getMenuText() );
      table.put( "RadioButtonMenuItem.background", Scheme.active().getMenu() );
      table.put( "RadioButtonMenuItem.selectionForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "RadioButtonMenuItem.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "RadioButtonMenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "RadioButtonMenuItem.disabledAreNavigable", false );
      
      table.put( "CheckBoxMenuItem.foreground", Scheme.active().getMenuText() );
      table.put( "CheckBoxMenuItem.background", Scheme.active().getMenu() );
      table.put( "CheckBoxMenuItem.selectionForeground", Scheme.active().getWindowText() );
      table.put( "CheckBoxMenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "CheckBoxMenuItem.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "CheckBoxMenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "CheckBoxMenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "CheckBoxMenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "CheckBoxMenuItem.disabledAreNavigable", false );
      //"CheckBoxMenuItem.acceleratorDelimiter", menuItemAcceleratorDelimiter,
      //"MenuItem.borderPainted", Boolean.FALSE,
      //"MenuItem.margin", twoInsets,


      table.put( "List.foreground", Scheme.active().getWindowText() );
      table.put( "List.background", Scheme.active().getWindow() );

      table.put( "Tree.foreground", Scheme.active().getWindowText() );
      table.put( "Tree.background", Scheme.active().getWindow() );

      table.put( "Table.foreground", Scheme.active().getWindowText() );
      table.put( "Table.background", Scheme.active().getWindow() );

      table.put( "ComboBox.foreground", Scheme.active().getWindowText() );
      table.put( "ComboBox.background", Scheme.active().getWindow() );
      table.put( "ComboBox.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "ComboBox.disabledBackground", Scheme.active().getControl() );
      table.put( "ComboBox.buttonBackground", Scheme.active().getControl() );
      table.put( "ComboBox.buttonShadow", Scheme.active().getControlShadow() );
      table.put( "ComboBox.buttonDarkShadow", Scheme.active().getControlDarkshadow() );
      table.put( "ComboBox.buttonHighlight", Scheme.active().getControl() );
      table.put( "ComboBox.selectionBackground", Scheme.active().getTextHighlight() );

      table.put( "TextField.foreground", Scheme.active().getWindowText() );
      table.put( "TextField.background", Scheme.active().getWindow() );
      table.put( "TextField.caretForeground", Scheme.active().getWindowText() );
      table.put( "TextField.selectionBackground", Scheme.active().getTextHighlight() );

      table.put( "TextArea.foreground", Scheme.active().getWindowText() );
      table.put( "TextArea.background", Scheme.active().getWindow() );
      table.put( "TextArea.caretForeground", Scheme.active().getWindowText() );
      table.put( "TextArea.selectionBackground", Scheme.active().getTextHighlight() );

      table.put( "TextPane.foreground", Scheme.active().getWindowText() );
      table.put( "TextPane.background", Scheme.active().getWindow() );
      table.put( "TextPane.caretForeground", Scheme.active().getWindowText() );
      table.put( "TextPane.selectionBackground", Scheme.active().getTextHighlight() );

      table.put( "Panel.foreground", Scheme.active().getControlText() );
      table.put( "Panel.background", Scheme.active().getControl() );

      table.put( "ScrollPane.background", Scheme.active().getControl() );
      table.put( "ScrollPane.foreground", Scheme.active().getControlText() );

      table.put( "Viewport.background", Scheme.active().getControl() );
      table.put( "Viewport.foreground", Scheme.active().getControlText() );

      table.put( "EditorPane.caretForeground", Scheme.active().getWindowText() );
      table.put( "EditorPane.selectionBackground", Scheme.active().getTextHighlight() );

      table.put( "Label.foreground", Scheme.active().getControlText() );
      table.put( "Label.background", Scheme.active().getControl() );
      table.put( "Label.disabledForeground", Scheme.active().getControl() );
      table.put( "Label.disabledShadow", Scheme.active().getControlText().darker() );
      table.put( "Label.interiorBackground", Scheme.active().getControl() );
      table.put( "Label.shadow", Scheme.active().getControl() );
      table.put( "Label.darkShadow", Scheme.active().getControlDarkshadow() );
      table.put( "Label.light", Scheme.active().getControl() );
      table.put( "Label.highlight", Scheme.active().getControlDarkshadow() );

      table.put( "RadioButton.foreground", Scheme.active().getControlText() );
      table.put( "RadioButton.background", Scheme.active().getControl() );
      table.put( "RadioButton.interiorBackground", Scheme.active().getControl() );
      table.put( "RadioButton.shadow", Scheme.active().getButtonBorderColor() );
      table.put( "RadioButton.darkShadow", Scheme.active().getControl() );
      table.put( "RadioButton.light", Scheme.active().getControl() );
      table.put( "RadioButton.highlight", Scheme.active().getButtonBorderColor() );

      table.put( "CheckBox.foreground", Scheme.active().getControlText() );
      table.put( "CheckBox.background", Scheme.active().getControl() );
      table.put( "CheckBox.interiorBackground", Scheme.active().getControl() );
      table.put( "CheckBox.shadow", Scheme.active().getButtonBorderColor() );
      table.put( "CheckBox.darkShadow", Scheme.active().getControl() );
      table.put( "CheckBox.light", Scheme.active().getControl() );
      table.put( "CheckBox.highlight", Scheme.active().getButtonBorderColor() );

      table.put( "ToolBar.background", Scheme.active().getMenu() );
      table.put( "ToolBar.border", BorderFactory.createEmptyBorder() );
      table.put( "ToolBar.highlight", Scheme.active().getSeparator1() );
      table.put( "ToolBar.shadow", Scheme.active().getSeparator2() );
      
      table.put( "Button.foreground", Scheme.active().getControlText() );
      table.put( "Button.disabledForeground", Scheme.active().getControlShadow() );
      table.put( "Button.disabledShadow", Scheme.active().getControlShadow() );
      table.put( "Button.background", Scheme.active().getControl() );
      table.put( "Button.interiorBackground", Scheme.active().getControl() );
      table.put( "Button.shadow", Scheme.active().getControl() );
      table.put( "Button.darkShadow", Scheme.active().getControlDarkshadow() );
      table.put( "Button.light", Scheme.active().getControl() );
      table.put( "Button.highlight", Scheme.active().getControlDarkshadow() );

      table.put( "OptionPane.background", Scheme.active().getControl() );
      table.put( "OptionPane.foreground", Scheme.active().getControlText() );
      table.put( "OptionPane.messageForeground", Scheme.active().getControlText() );

      table.put( "FileChooser.listViewBackground", Scheme.active().getWindow() );
      table.put( "FileChooser.listViewIcon", EditorUtilities.loadIcon( "images/ListView.gif" ) ); // missing in some plafs e.g., linux
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
