package editor.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 */
public class FixupLookAndFeel
{
  private static final Insets FIELD_INSETS = new Insets( 1, 3, 1, 1 );

  public static void fixupAll()
  {
    try
    {
      fixupColors();
      fixupFieldBorders();
      fixupFonts();
      fixupSplitPane();
      fixupTree();
      fixupTable();
      fixupPopups();
    }
    catch( Exception e )
    {
      EditorUtilities.handleUncaughtException(e);
    }
  }

  private static void fixupPopups()
  {
    //always show popups in a window
//    JPopupMenu.setDefaultLightWeightPopupEnabled( false );
  }

  private static void fixupColors()
  {
  }

  public static void fixupTable()
  {
    UIManager.put( "Table.ancestorInputMap", new UIDefaults.LazyInputMap( new Object[]{
      // Remove clipboard bindings as these override our menu accelerators
      //                           "ctrl C", "copy",
      //                           "ctrl V", "paste",
      //                           "ctrl X", "cut",
      //                             "COPY", "copy",
      //                            "PASTE", "paste",
      //                              "CUT", "cut",
      "RIGHT", "selectNextColumn",
      "KP_RIGHT", "selectNextColumn",
      "LEFT", "selectPreviousColumn",
      "KP_LEFT", "selectPreviousColumn",
      "DOWN", "selectNextRow",
      "KP_DOWN", "selectNextRow",
      "UP", "selectPreviousRow",
      "KP_UP", "selectPreviousRow",
      "shift RIGHT", "selectNextColumnExtendSelection",
      "shift KP_RIGHT", "selectNextColumnExtendSelection",
      "shift LEFT", "selectPreviousColumnExtendSelection",
      "shift KP_LEFT", "selectPreviousColumnExtendSelection",
      "shift DOWN", "selectNextRowExtendSelection",
      "shift KP_DOWN", "selectNextRowExtendSelection",
      "shift UP", "selectPreviousRowExtendSelection",
      "shift KP_UP", "selectPreviousRowExtendSelection",
      "PAGE_UP", "scrollUpChangeSelection",
      "PAGE_DOWN", "scrollDownChangeSelection",
      "HOME", "selectFirstColumn",
      "END", "selectLastColumn",
      "shift PAGE_UP", "scrollUpExtendSelection",
      "shift PAGE_DOWN", "scrollDownExtendSelection",
      "shift HOME", "selectFirstColumnExtendSelection",
      "shift END", "selectLastColumnExtendSelection",
      "ctrl PAGE_UP", "scrollLeftChangeSelection",
      "ctrl PAGE_DOWN", "scrollRightChangeSelection",
      "ctrl HOME", "selectFirstRow",
      "ctrl END", "selectLastRow",
      "ctrl shift PAGE_UP", "scrollRightExtendSelection",
      "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection",
      "ctrl shift HOME", "selectFirstRowExtendSelection",
      "ctrl shift END", "selectLastRowExtendSelection",
      "TAB", "selectNextColumnCell",
      "shift TAB", "selectPreviousColumnCell",
      // Interferes w/ OK button            "ENTER", "selectNextRowCell",
      //      "shift ENTER", "selectPreviousRowCell",
      "ctrl A", "selectAll",
      // Interferes w/ Cancel button        "ESCAPE", "cancel",
      "F2", "startEditing"
    } ) );
  }

  public static void fixupTree()
  {
    UIManager.put( "Tree.focusInputMap", new UIDefaults.LazyInputMap( new Object[]{
      // Remove clipboard bindings as these override our menu accelerators
      //                           "ctrl C", "copy",
      //                           "ctrl V", "paste",
      //                           "ctrl X", "cut",
      //                             "COPY", "copy",
      //                            "PASTE", "paste",
      //                              "CUT", "cut",
      "UP", "selectPrevious",
      "KP_UP", "selectPrevious",
      "shift UP", "selectPreviousExtendSelection",
      "shift KP_UP", "selectPreviousExtendSelection",
      "DOWN", "selectNext",
      "KP_DOWN", "selectNext",
      "shift DOWN", "selectNextExtendSelection",
      "shift KP_DOWN", "selectNextExtendSelection",
      "RIGHT", "selectChild",
      "KP_RIGHT", "selectChild",
      "LEFT", "selectParent",
      "KP_LEFT", "selectParent",
      "PAGE_UP", "scrollUpChangeSelection",
      "shift PAGE_UP", "scrollUpExtendSelection",
      "PAGE_DOWN", "scrollDownChangeSelection",
      "shift PAGE_DOWN", "scrollDownExtendSelection",
      "HOME", "selectFirst",
      "shift HOME", "selectFirstExtendSelection",
      "END", "selectLast",
      "shift END", "selectLastExtendSelection",
      // Interferes w/ editing in nvvs   "F2", "startEditing",
      "ctrl A", "selectAll",
      "ctrl SLASH", "selectAll",
      "ctrl BACK_SLASH", "clearSelection",
      "ctrl SPACE", "toggleSelectionPreserveAnchor",
      "shift SPACE", "extendSelection",
      "ctrl HOME", "selectFirstChangeLead",
      "ctrl END", "selectLastChangeLead",
      "ctrl UP", "selectPreviousChangeLead",
      "ctrl KP_UP", "selectPreviousChangeLead",
      "ctrl DOWN", "selectNextChangeLead",
      "ctrl KP_DOWN", "selectNextChangeLead",
      "ctrl PAGE_DOWN", "scrollDownChangeLead",
      "ctrl shift PAGE_DOWN", "scrollDownExtendSelection",
      "ctrl PAGE_UP", "scrollUpChangeLead",
      "ctrl shift PAGE_UP", "scrollUpExtendSelection",
      "ctrl LEFT", "scrollLeft",
      "ctrl KP_LEFT", "scrollLeft",
      "ctrl RIGHT", "scrollRight",
      "ctrl KP_RIGHT", "scrollRight",
      "SPACE", "toggleSelectionPreserveAnchor",
    } ) );
  }

  public static void fixupSplitPane()
  {
    UIManager.put( "SplitPane.border", BorderFactory.createEmptyBorder() );
    UIManager.put( "SplitPane.ancestorInputMap",
                   new UIDefaults.LazyInputMap( new Object[]{
                     "UP", "negativeIncrement",
                     "DOWN", "positiveIncrement",
                     "LEFT", "negativeIncrement",
                     "RIGHT", "positiveIncrement",
                     "KP_UP", "negativeIncrement",
                     "KP_DOWN", "positiveIncrement",
                     "KP_LEFT", "negativeIncrement",
                     "KP_RIGHT", "positiveIncrement",
//                "HOME", "selectMin",
//                 "END", "selectMax",
// Debug step over:                 "F8", "startResize",
//                  "F6", "toggleFocus",
//            "ctrl TAB", "focusOutForward",
//       "ctrl shift TAB", "focusOutBackward"
                   } ) );
  }

  public static void fixupFieldBorders()
  {
    Border borderTextField = new XPFillBorder( EditorUtilities.CONTROL_SHADOW, 1 );

    Insets insets = (Insets)UIManager.get( "TextField.margin" );
    if( insets.left < 2 )
    {
      insets = FIELD_INSETS;
    }

    UIManager.put( "ComboBox.border", borderTextField );

    UIManager.put( "FormattedTextField.border", borderTextField );
    UIManager.put( "FormattedTextField.margin", insets );

    UIManager.put( "PasswordField.border", borderTextField );
    UIManager.put( "PasswordField.margin", insets );

    UIManager.put( "TextField.border", borderTextField );
    UIManager.put( "TextField.margin", insets );

    UIManager.put( "ScrollPane.border", borderTextField );
    UIManager.put( "ScrollPane.margin", insets );
  }

  public static void fixupFonts()
  {
    Integer twelve = new Integer( 12 );
    Integer fontPlain = new Integer( Font.PLAIN );

    Object dialogPlain12 = getDesktopProperty( "win.messagebox.font" );
    dialogPlain12 = dialogPlain12 == null
                    ? new UIDefaults.ProxyLazyValue(
                          "javax.swing.plaf.FontUIResource",
                          null,
                          new Object[] {"Dialog", fontPlain, twelve} )
                    : dialogPlain12;

    Object menuFont = getDesktopProperty( "win.menu.font" );
    menuFont = menuFont == null ? dialogPlain12 : menuFont;

    Object serifPlain12 = dialogPlain12;
    Object sansSerifPlain12 =  dialogPlain12;
    Object monospacedPlain12 = new UIDefaults.ProxyLazyValue(
          "javax.swing.plaf.FontUIResource",
          null,
          new Object[] {"MonoSpaced", fontPlain, twelve} );

    UIManager.put( "Button.font", dialogPlain12 );
    UIManager.put( "ToggleButton.font", dialogPlain12 );
    UIManager.put( "RadioButton.font", dialogPlain12 );
    UIManager.put( "CheckBox.font", dialogPlain12 );
    UIManager.put( "ColorChooser.font", dialogPlain12 );
    UIManager.put( "ComboBox.font", sansSerifPlain12 );
    UIManager.put( "Label.font", dialogPlain12 );
    UIManager.put( "List.font", dialogPlain12 );
    UIManager.put( "MenuBar.font", menuFont );
    UIManager.put( "MenuItem.font", menuFont );
    UIManager.put( "MenuItem.acceleratorFont", menuFont );
    UIManager.put( "RadioButtonMenuItem.font", menuFont );
    UIManager.put( "RadioButtonMenuItem.acceleratorFont", menuFont );
    UIManager.put( "CheckBoxMenuItem.font", menuFont );
    UIManager.put( "CheckBoxMenuItem.acceleratorFont", menuFont );
    UIManager.put( "Menu.font", menuFont );
    UIManager.put( "Menu.acceleratorFont", menuFont );
    UIManager.put( "PopupMenu.font", menuFont );
    UIManager.put( "OptionPane.font", dialogPlain12 );
    UIManager.put( "Panel.font", dialogPlain12 );
    UIManager.put( "ProgressBar.font", dialogPlain12 );
    UIManager.put( "ScrollPane.font", dialogPlain12 );
    UIManager.put( "Viewport.font", dialogPlain12 );
    UIManager.put( "Spinner.font", monospacedPlain12 );
    UIManager.put( "TabbedPane.font", dialogPlain12 );
    UIManager.put( "Table.font", dialogPlain12 );
    UIManager.put( "TableHeader.font", dialogPlain12 );
    UIManager.put( "TextField.font", sansSerifPlain12 );
    UIManager.put( "FormattedTextField.font", sansSerifPlain12 );
    UIManager.put( "PasswordField.font", monospacedPlain12 );
    UIManager.put( "TextArea.font", monospacedPlain12 );
    UIManager.put( "EditorPane.font", serifPlain12 );
    UIManager.put( "TitledBorder.font", dialogPlain12 );
    UIManager.put( "ToolBar.font", dialogPlain12 );
    UIManager.put( "ToolTip.font", sansSerifPlain12 );
    UIManager.put( "Tree.font", dialogPlain12 );

    UIManager.put( "PopupMenu.border", BorderFactory.createMatteBorder( 1, 1, 1, 1, EditorUtilities.CONTROL_SHADOW ) );
    UIManager.put( "Menu.border", BorderFactory.createMatteBorder( 1, 1, 1, 1, EditorUtilities.CONTROL_SHADOW ) );

  }

  private static class XPFillBorder extends LineBorder implements UIResource
  {
    XPFillBorder( Color color, int thickness )
    {
      super( color, thickness );
    }

    public Insets getBorderInsets( Component c )
    {
      return getBorderInsets( c, new Insets( 0, 0, 0, 0 ) );
    }

    public Insets getBorderInsets( Component c, Insets insets )
    {
      Insets margin = null;
      if( c instanceof AbstractButton )
      {
        margin = ((AbstractButton)c).getMargin();
      }
      else if( c instanceof JToolBar )
      {
        margin = ((JToolBar)c).getMargin();
      }
      else if( c instanceof JTextComponent )
      {
        margin = ((JTextComponent)c).getMargin();
      }
      else if( c instanceof JComboBox )
      {
        margin = ((JTextField)((JComboBox)c).getEditor().getEditorComponent()).getMargin();
      }

      insets.top = (margin != null ? margin.top : 0) + thickness;
      insets.left = (margin != null ? margin.left : 0) + thickness;
      insets.bottom = (margin != null ? margin.bottom : 0) + thickness;
      insets.right = (margin != null ? margin.right : 0) + thickness;

      return insets;
    }
  }

  public static Object getDesktopProperty( String strProperty )
  {
    return Toolkit.getDefaultToolkit().getDesktopProperty( strProperty );
  }
}