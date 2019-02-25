package editor.util;

import editor.Scheme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.text.DefaultEditorKit;
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
    Border borderTextField = new XPFillBorder( Scheme.active().getFieldBorderColor(), 1 );

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

    UIManager.put( "FileChooser.listViewBorder", borderTextField );
  }

  public static void fixupTreeHandles()
  {
//    UIManager.put( "Tree.expandedIcon", GosuTreeUi.ExpandedIcon.createExpandedIcon() );
//    UIManager.put( "Tree.collapsedIcon", GosuTreeUi.CollapsedIcon.createCollapsedIcon() );
    UIManager.put( "Tree.expandedIcon", EditorUtilities.loadIcon( "images/tree_expanded.png" ) );
    UIManager.put( "Tree.collapsedIcon", EditorUtilities.loadIcon( "images/tree_collapsed.png" ) );
    UIManager.put( "Tree.paintLines", false );
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

    UIManager.put( "PopupMenu.border", BorderFactory.createMatteBorder( 1, 1, 1, 1, Scheme.active().getControlShadow() ) );
    UIManager.put( "Menu.border", BorderFactory.createMatteBorder( 1, 1, 1, 1, Scheme.active().getControlShadow() ) );

  }

//  private static class XPFillBorder extends RoundedMatteBorder implements UIResource
//  {
//    private int _thickness;
//
//    XPFillBorder( Color color, int thickness )
//    {
//      super( thickness, color );
//      _thickness = thickness;
//    }
//
//    public Insets getBorderInsets( Component c )
//    {
//      return getBorderInsets( c, new Insets( 0, 0, 0, 0 ) );
//    }
//
//    public Insets getBorderInsets( Component c, Insets insets )
//    {
//      Insets margin;
//      if( c instanceof AbstractButton )
//      {
//        margin = ((AbstractButton)c).getMargin();
//      }
//      else if( c instanceof JToolBar )
//      {
//        margin = ((JToolBar)c).getMargin();
//      }
//      else if( c instanceof JTextComponent )
//      {
//        margin = ((JTextComponent)c).getMargin();
//      }
//      else if( c instanceof JComboBox )
//      {
//        margin = ((JTextField)((JComboBox)c).getEditor().getEditorComponent()).getMargin();
//      }
//      else
//      {
//        margin = new Insets( 0, 0, 0, 0 );
//      }
//      Insets smargin = super.getBorderInsets( c, insets );
//
//      insets.top = margin.top + _thickness + smargin.top;
//      insets.left = margin.left + _thickness + smargin.left;
//      insets.bottom = margin.bottom + _thickness + smargin.bottom;
//      insets.right = margin.right + _thickness + smargin.right;
//
//      return insets;
//    }
//  }

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

  public static void installKeybindings( UIDefaults table )
  {
    // *** Text
    Object fieldInputMap = new UIDefaults.LazyInputMap( new Object[]{
      "control C", DefaultEditorKit.copyAction,
      "control V", DefaultEditorKit.pasteAction,
      "control X", DefaultEditorKit.cutAction,
      "COPY", DefaultEditorKit.copyAction,
      "PASTE", DefaultEditorKit.pasteAction,
      "CUT", DefaultEditorKit.cutAction,
      "control INSERT", DefaultEditorKit.copyAction,
      "shift INSERT", DefaultEditorKit.pasteAction,
      "shift DELETE", DefaultEditorKit.cutAction,
      "control A", DefaultEditorKit.selectAllAction,
      "control BACK_SLASH", "unselect"/*DefaultEditorKit.unselectAction*/,
      "shift LEFT", DefaultEditorKit.selectionBackwardAction,
      "shift RIGHT", DefaultEditorKit.selectionForwardAction,
      "control LEFT", DefaultEditorKit.previousWordAction,
      "control RIGHT", DefaultEditorKit.nextWordAction,
      "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
      "control shift RIGHT", DefaultEditorKit.selectionNextWordAction,
      "HOME", DefaultEditorKit.beginLineAction,
      "END", DefaultEditorKit.endLineAction,
      "shift HOME", DefaultEditorKit.selectionBeginLineAction,
      "shift END", DefaultEditorKit.selectionEndLineAction,
      "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "ctrl H", DefaultEditorKit.deletePrevCharAction,
      "DELETE", DefaultEditorKit.deleteNextCharAction,
      "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
      "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
      "RIGHT", DefaultEditorKit.forwardAction,
      "LEFT", DefaultEditorKit.backwardAction,
      "KP_RIGHT", DefaultEditorKit.forwardAction,
      "KP_LEFT", DefaultEditorKit.backwardAction,
      "ENTER", JTextField.notifyAction,
      "control shift O", "toggle-componentOrientation"/*DefaultEditorKit.toggleComponentOrientation*/
    } );
    Object passwordInputMap = new UIDefaults.LazyInputMap( new Object[]{
      "control C", DefaultEditorKit.copyAction,
      "control V", DefaultEditorKit.pasteAction,
      "control X", DefaultEditorKit.cutAction,
      "COPY", DefaultEditorKit.copyAction,
      "PASTE", DefaultEditorKit.pasteAction,
      "CUT", DefaultEditorKit.cutAction,
      "control INSERT", DefaultEditorKit.copyAction,
      "shift INSERT", DefaultEditorKit.pasteAction,
      "shift DELETE", DefaultEditorKit.cutAction,
      "control A", DefaultEditorKit.selectAllAction,
      "control BACK_SLASH", "unselect"/*DefaultEditorKit.unselectAction*/,
      "shift LEFT", DefaultEditorKit.selectionBackwardAction,
      "shift RIGHT", DefaultEditorKit.selectionForwardAction,
      "control LEFT", DefaultEditorKit.beginLineAction,
      "control RIGHT", DefaultEditorKit.endLineAction,
      "control shift LEFT", DefaultEditorKit.selectionBeginLineAction,
      "control shift RIGHT", DefaultEditorKit.selectionEndLineAction,
      "HOME", DefaultEditorKit.beginLineAction,
      "END", DefaultEditorKit.endLineAction,
      "shift HOME", DefaultEditorKit.selectionBeginLineAction,
      "shift END", DefaultEditorKit.selectionEndLineAction,
      "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "ctrl H", DefaultEditorKit.deletePrevCharAction,
      "DELETE", DefaultEditorKit.deleteNextCharAction,
      "RIGHT", DefaultEditorKit.forwardAction,
      "LEFT", DefaultEditorKit.backwardAction,
      "KP_RIGHT", DefaultEditorKit.forwardAction,
      "KP_LEFT", DefaultEditorKit.backwardAction,
      "ENTER", JTextField.notifyAction,
      "control shift O", "toggle-componentOrientation"/*DefaultEditorKit.toggleComponentOrientation*/
    } );
    Object multilineInputMap = new UIDefaults.LazyInputMap( new Object[]{
      "control C", DefaultEditorKit.copyAction,
      "control V", DefaultEditorKit.pasteAction,
      "control X", DefaultEditorKit.cutAction,
      "COPY", DefaultEditorKit.copyAction,
      "PASTE", DefaultEditorKit.pasteAction,
      "CUT", DefaultEditorKit.cutAction,
      "control INSERT", DefaultEditorKit.copyAction,
      "shift INSERT", DefaultEditorKit.pasteAction,
      "shift DELETE", DefaultEditorKit.cutAction,
      "shift LEFT", DefaultEditorKit.selectionBackwardAction,
      "shift RIGHT", DefaultEditorKit.selectionForwardAction,
      "control LEFT", DefaultEditorKit.previousWordAction,
      "control RIGHT", DefaultEditorKit.nextWordAction,
      "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
      "control shift RIGHT", DefaultEditorKit.selectionNextWordAction,
      "control A", DefaultEditorKit.selectAllAction,
      "control BACK_SLASH", "unselect"/*DefaultEditorKit.unselectAction*/,
      "HOME", DefaultEditorKit.beginLineAction,
      "END", DefaultEditorKit.endLineAction,
      "shift HOME", DefaultEditorKit.selectionBeginLineAction,
      "shift END", DefaultEditorKit.selectionEndLineAction,
      "control HOME", DefaultEditorKit.beginAction,
      "control END", DefaultEditorKit.endAction,
      "control shift HOME", DefaultEditorKit.selectionBeginAction,
      "control shift END", DefaultEditorKit.selectionEndAction,
      "UP", DefaultEditorKit.upAction,
      "DOWN", DefaultEditorKit.downAction,
      "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
      "ctrl H", DefaultEditorKit.deletePrevCharAction,
      "DELETE", DefaultEditorKit.deleteNextCharAction,
      "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
      "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
      "RIGHT", DefaultEditorKit.forwardAction,
      "LEFT", DefaultEditorKit.backwardAction,
      "KP_RIGHT", DefaultEditorKit.forwardAction,
      "KP_LEFT", DefaultEditorKit.backwardAction,
      "PAGE_UP", DefaultEditorKit.pageUpAction,
      "PAGE_DOWN", DefaultEditorKit.pageDownAction,
      "shift PAGE_UP", "selection-page-up",
      "shift PAGE_DOWN", "selection-page-down",
      "ctrl shift PAGE_UP", "selection-page-left",
      "ctrl shift PAGE_DOWN", "selection-page-right",
      "shift UP", DefaultEditorKit.selectionUpAction,
      "shift DOWN", DefaultEditorKit.selectionDownAction,
      "ENTER", DefaultEditorKit.insertBreakAction,
      "TAB", DefaultEditorKit.insertTabAction,
      "control T", "next-link-action",
      "control shift T", "previous-link-action",
      "control SPACE", "activate-link-action",
      "control shift O", "toggle-componentOrientation"/*DefaultEditorKit.toggleComponentOrientation*/
    } );
    Object[] defaults = {
      "TextField.focusInputMap", fieldInputMap,
      "PasswordField.focusInputMap", passwordInputMap,
      "TextArea.focusInputMap", multilineInputMap,
      "TextPane.focusInputMap", multilineInputMap,
      "EditorPane.focusInputMap", multilineInputMap,
      "Button.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "SPACE", "pressed",
        "released SPACE", "released"
      } ),
      "CheckBox.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "SPACE", "pressed",
        "released SPACE", "released"
      } ),
      "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap( new Object[]{
      "ESCAPE", "hidePopup",
      "PAGE_UP", "pageUpPassThrough",
      "PAGE_DOWN", "pageDownPassThrough",
      "HOME", "homePassThrough",
      "END", "endPassThrough",
      "DOWN", "selectNext2",
      "KP_DOWN", "selectNext2",
      "UP", "selectPrevious2",
      "KP_UP", "selectPrevious2",
      "ENTER", "enterPressed",
      "F4", "togglePopup",
      "alt DOWN", "togglePopup",
      "alt KP_DOWN", "togglePopup",
      "alt UP", "togglePopup",
      "alt KP_UP", "togglePopup"
    } ),
      "Desktop.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ctrl F5", "restore",
        "ctrl F4", "close",
        "ctrl F7", "move",
        "ctrl F8", "resize",
        "RIGHT", "right",
        "KP_RIGHT", "right",
        "LEFT", "left",
        "KP_LEFT", "left",
        "UP", "up",
        "KP_UP", "up",
        "DOWN", "down",
        "KP_DOWN", "down",
        "ESCAPE", "escape",
        "ctrl F9", "minimize",
        "ctrl F10", "maximize",
        "ctrl F6", "selectNextFrame",
        "ctrl TAB", "selectNextFrame",
        "ctrl alt F6", "selectNextFrame",
        "shift ctrl alt F6", "selectPreviousFrame",
        "ctrl F12", "navigateNext",
        "shift ctrl F12", "navigatePrevious"
      } ),
      "FileChooser.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ESCAPE", "cancelSelection",
        "F2", "editFileName",
        "F5", "refresh",
        "BACK_SPACE", "Go Up",
        "ENTER", "approveSelection",
        "ctrl ENTER", "approveSelection"
      } ),
      "InternalFrame.windowBindings", new Object[]{
      "shift ESCAPE", "showSystemMenu",
      "ctrl SPACE", "showSystemMenu",
      "ESCAPE", "hideSystemMenu"
    },
      "List.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ctrl C", "copy",
        "ctrl V", "paste",
        "ctrl X", "cut",
        "COPY", "copy",
        "PASTE", "paste",
        "CUT", "cut",
        "control INSERT", "copy",
        "shift INSERT", "paste",
        "shift DELETE", "cut",
        "UP", "selectPreviousRow",
        "KP_UP", "selectPreviousRow",
        "shift UP", "selectPreviousRowExtendSelection",
        "shift KP_UP", "selectPreviousRowExtendSelection",
        "ctrl shift UP", "selectPreviousRowExtendSelection",
        "ctrl shift KP_UP", "selectPreviousRowExtendSelection",
        "ctrl UP", "selectPreviousRowChangeLead",
        "ctrl KP_UP", "selectPreviousRowChangeLead",
        "DOWN", "selectNextRow",
        "KP_DOWN", "selectNextRow",
        "shift DOWN", "selectNextRowExtendSelection",
        "shift KP_DOWN", "selectNextRowExtendSelection",
        "ctrl shift DOWN", "selectNextRowExtendSelection",
        "ctrl shift KP_DOWN", "selectNextRowExtendSelection",
        "ctrl DOWN", "selectNextRowChangeLead",
        "ctrl KP_DOWN", "selectNextRowChangeLead",
        "LEFT", "selectPreviousColumn",
        "KP_LEFT", "selectPreviousColumn",
        "shift LEFT", "selectPreviousColumnExtendSelection",
        "shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "ctrl shift LEFT", "selectPreviousColumnExtendSelection",
        "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "ctrl LEFT", "selectPreviousColumnChangeLead",
        "ctrl KP_LEFT", "selectPreviousColumnChangeLead",
        "RIGHT", "selectNextColumn",
        "KP_RIGHT", "selectNextColumn",
        "shift RIGHT", "selectNextColumnExtendSelection",
        "shift KP_RIGHT", "selectNextColumnExtendSelection",
        "ctrl shift RIGHT", "selectNextColumnExtendSelection",
        "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection",
        "ctrl RIGHT", "selectNextColumnChangeLead",
        "ctrl KP_RIGHT", "selectNextColumnChangeLead",
        "HOME", "selectFirstRow",
        "shift HOME", "selectFirstRowExtendSelection",
        "ctrl shift HOME", "selectFirstRowExtendSelection",
        "ctrl HOME", "selectFirstRowChangeLead",
        "END", "selectLastRow",
        "shift END", "selectLastRowExtendSelection",
        "ctrl shift END", "selectLastRowExtendSelection",
        "ctrl END", "selectLastRowChangeLead",
        "PAGE_UP", "scrollUp",
        "shift PAGE_UP", "scrollUpExtendSelection",
        "ctrl shift PAGE_UP", "scrollUpExtendSelection",
        "ctrl PAGE_UP", "scrollUpChangeLead",
        "PAGE_DOWN", "scrollDown",
        "shift PAGE_DOWN", "scrollDownExtendSelection",
        "ctrl shift PAGE_DOWN", "scrollDownExtendSelection",
        "ctrl PAGE_DOWN", "scrollDownChangeLead",
        "ctrl A", "selectAll",
        "ctrl SLASH", "selectAll",
        "ctrl BACK_SLASH", "clearSelection",
        "SPACE", "addToSelection",
        "ctrl SPACE", "toggleAndAnchor",
        "shift SPACE", "extendTo",
        "ctrl shift SPACE", "moveSelectionTo"
      } ),
      "MenuBar.windowBindings", new Object[]{
      "F10", "takeFocus"
    },
      "RadioButton.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "SPACE", "pressed",
        "released SPACE", "released"
      } ),
      "OptionPane.windowBindings", new Object[]{
      "ESCAPE", "close"
    },
      "FormattedTextField.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ctrl C", DefaultEditorKit.copyAction,
        "ctrl V", DefaultEditorKit.pasteAction,
        "ctrl X", DefaultEditorKit.cutAction,
        "COPY", DefaultEditorKit.copyAction,
        "PASTE", DefaultEditorKit.pasteAction,
        "CUT", DefaultEditorKit.cutAction,
        "control INSERT", DefaultEditorKit.copyAction,
        "shift INSERT", DefaultEditorKit.pasteAction,
        "shift DELETE", DefaultEditorKit.cutAction,
        "shift LEFT", DefaultEditorKit.selectionBackwardAction,
        "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
        "shift RIGHT", DefaultEditorKit.selectionForwardAction,
        "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
        "ctrl LEFT", DefaultEditorKit.previousWordAction,
        "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
        "ctrl RIGHT", DefaultEditorKit.nextWordAction,
        "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
        "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
        "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
        "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
        "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
        "ctrl A", DefaultEditorKit.selectAllAction,
        "HOME", DefaultEditorKit.beginLineAction,
        "END", DefaultEditorKit.endLineAction,
        "shift HOME", DefaultEditorKit.selectionBeginLineAction,
        "shift END", DefaultEditorKit.selectionEndLineAction,
        "BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
        "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction,
        "ctrl H", DefaultEditorKit.deletePrevCharAction,
        "DELETE", DefaultEditorKit.deleteNextCharAction,
        "ctrl DELETE", DefaultEditorKit.deleteNextWordAction,
        "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction,
        "RIGHT", DefaultEditorKit.forwardAction,
        "LEFT", DefaultEditorKit.backwardAction,
        "KP_RIGHT", DefaultEditorKit.forwardAction,
        "KP_LEFT", DefaultEditorKit.backwardAction,
        "ENTER", JTextField.notifyAction,
        "ctrl BACK_SLASH", "unselect",
        "control shift O", "toggle-componentOrientation",
        "ESCAPE", "reset-field-edit",
        "UP", "increment",
        "KP_UP", "increment",
        "DOWN", "decrement",
        "KP_DOWN", "decrement",
      } ),
      "RootPane.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "shift F10", "postPopup",
        "CONTEXT_MENU", "postPopup"
      } ),
      // These bindings are only enabled when there is a default
      // button set on the rootpane.
      "RootPane.defaultButtonWindowKeyBindings", new Object[]{
      "ENTER", "press",
      "released ENTER", "release",
      "ctrl ENTER", "press",
      "ctrl released ENTER", "release"
    },
      "ScrollBar.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "RIGHT", "positiveUnitIncrement",
        "KP_RIGHT", "positiveUnitIncrement",
        "DOWN", "positiveUnitIncrement",
        "KP_DOWN", "positiveUnitIncrement",
        "PAGE_DOWN", "positiveBlockIncrement",
        "ctrl PAGE_DOWN", "positiveBlockIncrement",
        "LEFT", "negativeUnitIncrement",
        "KP_LEFT", "negativeUnitIncrement",
        "UP", "negativeUnitIncrement",
        "KP_UP", "negativeUnitIncrement",
        "PAGE_UP", "negativeBlockIncrement",
        "ctrl PAGE_UP", "negativeBlockIncrement",
        "HOME", "minScroll",
        "END", "maxScroll"
      } ),
      "ScrollPane.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "RIGHT", "unitScrollRight",
        "KP_RIGHT", "unitScrollRight",
        "DOWN", "unitScrollDown",
        "KP_DOWN", "unitScrollDown",
        "LEFT", "unitScrollLeft",
        "KP_LEFT", "unitScrollLeft",
        "UP", "unitScrollUp",
        "KP_UP", "unitScrollUp",
        "PAGE_UP", "scrollUp",
        "PAGE_DOWN", "scrollDown",
        "ctrl PAGE_UP", "scrollLeft",
        "ctrl PAGE_DOWN", "scrollRight",
        "ctrl HOME", "scrollHome",
        "ctrl END", "scrollEnd"
      } ),
      "Slider.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "RIGHT", "positiveUnitIncrement",
        "KP_RIGHT", "positiveUnitIncrement",
        "DOWN", "negativeUnitIncrement",
        "KP_DOWN", "negativeUnitIncrement",
        "PAGE_DOWN", "negativeBlockIncrement",
        "LEFT", "negativeUnitIncrement",
        "KP_LEFT", "negativeUnitIncrement",
        "UP", "positiveUnitIncrement",
        "KP_UP", "positiveUnitIncrement",
        "PAGE_UP", "positiveBlockIncrement",
        "HOME", "minScroll",
        "END", "maxScroll"
      } ),
      "Spinner.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "UP", "increment",
        "KP_UP", "increment",
        "DOWN", "decrement",
        "KP_DOWN", "decrement",
      } ),
      "SplitPane.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "UP", "negativeIncrement",
        "DOWN", "positiveIncrement",
        "LEFT", "negativeIncrement",
        "RIGHT", "positiveIncrement",
        "KP_UP", "negativeIncrement",
        "KP_DOWN", "positiveIncrement",
        "KP_LEFT", "negativeIncrement",
        "KP_RIGHT", "positiveIncrement",
        "HOME", "selectMin",
        "END", "selectMax",
        "F8", "startResize",
        "F6", "toggleFocus",
        "ctrl TAB", "focusOutForward",
        "ctrl shift TAB", "focusOutBackward"
      } ),
      "TabbedPane.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "RIGHT", "navigateRight",
        "KP_RIGHT", "navigateRight",
        "LEFT", "navigateLeft",
        "KP_LEFT", "navigateLeft",
        "UP", "navigateUp",
        "KP_UP", "navigateUp",
        "DOWN", "navigateDown",
        "KP_DOWN", "navigateDown",
        "ctrl DOWN", "requestFocusForVisibleComponent",
        "ctrl KP_DOWN", "requestFocusForVisibleComponent",
      } ),
      "TabbedPane.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ctrl TAB", "navigateNext",
        "ctrl shift TAB", "navigatePrevious",
        "ctrl PAGE_DOWN", "navigatePageDown",
        "ctrl PAGE_UP", "navigatePageUp",
        "ctrl UP", "requestFocus",
        "ctrl KP_UP", "requestFocus",
      } ),
      "TableHeader.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "SPACE", "toggleSortOrder",
        "LEFT", "selectColumnToLeft",
        "KP_LEFT", "selectColumnToLeft",
        "RIGHT", "selectColumnToRight",
        "KP_RIGHT", "selectColumnToRight",
        "alt LEFT", "moveColumnLeft",
        "alt KP_LEFT", "moveColumnLeft",
        "alt RIGHT", "moveColumnRight",
        "alt KP_RIGHT", "moveColumnRight",
        "alt shift LEFT", "resizeLeft",
        "alt shift KP_LEFT", "resizeLeft",
        "alt shift RIGHT", "resizeRight",
        "alt shift KP_RIGHT", "resizeRight",
        "ESCAPE", "focusTable",
      } ),
      "Table.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ctrl C", "copy",
        "ctrl V", "paste",
        "ctrl X", "cut",
        "COPY", "copy",
        "PASTE", "paste",
        "CUT", "cut",
        "control INSERT", "copy",
        "shift INSERT", "paste",
        "shift DELETE", "cut",
        "RIGHT", "selectNextColumn",
        "KP_RIGHT", "selectNextColumn",
        "shift RIGHT", "selectNextColumnExtendSelection",
        "shift KP_RIGHT", "selectNextColumnExtendSelection",
        "ctrl shift RIGHT", "selectNextColumnExtendSelection",
        "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection",
        "ctrl RIGHT", "selectNextColumnChangeLead",
        "ctrl KP_RIGHT", "selectNextColumnChangeLead",
        "LEFT", "selectPreviousColumn",
        "KP_LEFT", "selectPreviousColumn",
        "shift LEFT", "selectPreviousColumnExtendSelection",
        "shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "ctrl shift LEFT", "selectPreviousColumnExtendSelection",
        "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "ctrl LEFT", "selectPreviousColumnChangeLead",
        "ctrl KP_LEFT", "selectPreviousColumnChangeLead",
        "DOWN", "selectNextRow",
        "KP_DOWN", "selectNextRow",
        "shift DOWN", "selectNextRowExtendSelection",
        "shift KP_DOWN", "selectNextRowExtendSelection",
        "ctrl shift DOWN", "selectNextRowExtendSelection",
        "ctrl shift KP_DOWN", "selectNextRowExtendSelection",
        "ctrl DOWN", "selectNextRowChangeLead",
        "ctrl KP_DOWN", "selectNextRowChangeLead",
        "UP", "selectPreviousRow",
        "KP_UP", "selectPreviousRow",
        "shift UP", "selectPreviousRowExtendSelection",
        "shift KP_UP", "selectPreviousRowExtendSelection",
        "ctrl shift UP", "selectPreviousRowExtendSelection",
        "ctrl shift KP_UP", "selectPreviousRowExtendSelection",
        "ctrl UP", "selectPreviousRowChangeLead",
        "ctrl KP_UP", "selectPreviousRowChangeLead",
        "HOME", "selectFirstColumn",
        "shift HOME", "selectFirstColumnExtendSelection",
        "ctrl shift HOME", "selectFirstRowExtendSelection",
        "ctrl HOME", "selectFirstRow",
        "END", "selectLastColumn",
        "shift END", "selectLastColumnExtendSelection",
        "ctrl shift END", "selectLastRowExtendSelection",
        "ctrl END", "selectLastRow",
        "PAGE_UP", "scrollUpChangeSelection",
        "shift PAGE_UP", "scrollUpExtendSelection",
        "ctrl shift PAGE_UP", "scrollLeftExtendSelection",
        "ctrl PAGE_UP", "scrollLeftChangeSelection",
        "PAGE_DOWN", "scrollDownChangeSelection",
        "shift PAGE_DOWN", "scrollDownExtendSelection",
        "ctrl shift PAGE_DOWN", "scrollRightExtendSelection",
        "ctrl PAGE_DOWN", "scrollRightChangeSelection",
        "TAB", "selectNextColumnCell",
        "shift TAB", "selectPreviousColumnCell",
        "ENTER", "selectNextRowCell",
        "shift ENTER", "selectPreviousRowCell",
        "ctrl A", "selectAll",
        "ctrl SLASH", "selectAll",
        "ctrl BACK_SLASH", "clearSelection",
        "ESCAPE", "cancel",
        "F2", "startEditing",
        "SPACE", "addToSelection",
        "ctrl SPACE", "toggleAndAnchor",
        "shift SPACE", "extendTo",
        "ctrl shift SPACE", "moveSelectionTo",
        "F8", "focusHeader"
      } ),
      "ToggleButton.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "SPACE", "pressed",
        "released SPACE", "released"
      } ),
      "ToolBar.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "UP", "navigateUp",
        "KP_UP", "navigateUp",
        "DOWN", "navigateDown",
        "KP_DOWN", "navigateDown",
        "LEFT", "navigateLeft",
        "KP_LEFT", "navigateLeft",
        "RIGHT", "navigateRight",
        "KP_RIGHT", "navigateRight"
      } ),
      "Tree.focusInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ADD", "expand",
        "SUBTRACT", "collapse",
        "ctrl C", "copy",
        "ctrl V", "paste",
        "ctrl X", "cut",
        "COPY", "copy",
        "PASTE", "paste",
        "CUT", "cut",
        "control INSERT", "copy",
        "shift INSERT", "paste",
        "shift DELETE", "cut",
        "UP", "selectPrevious",
        "KP_UP", "selectPrevious",
        "shift UP", "selectPreviousExtendSelection",
        "shift KP_UP", "selectPreviousExtendSelection",
        "ctrl shift UP", "selectPreviousExtendSelection",
        "ctrl shift KP_UP", "selectPreviousExtendSelection",
        "ctrl UP", "selectPreviousChangeLead",
        "ctrl KP_UP", "selectPreviousChangeLead",
        "DOWN", "selectNext",
        "KP_DOWN", "selectNext",
        "shift DOWN", "selectNextExtendSelection",
        "shift KP_DOWN", "selectNextExtendSelection",
        "ctrl shift DOWN", "selectNextExtendSelection",
        "ctrl shift KP_DOWN", "selectNextExtendSelection",
        "ctrl DOWN", "selectNextChangeLead",
        "ctrl KP_DOWN", "selectNextChangeLead",
        "RIGHT", "selectChild",
        "KP_RIGHT", "selectChild",
        "LEFT", "selectParent",
        "KP_LEFT", "selectParent",
        "PAGE_UP", "scrollUpChangeSelection",
        "shift PAGE_UP", "scrollUpExtendSelection",
        "ctrl shift PAGE_UP", "scrollUpExtendSelection",
        "ctrl PAGE_UP", "scrollUpChangeLead",
        "PAGE_DOWN", "scrollDownChangeSelection",
        "shift PAGE_DOWN", "scrollDownExtendSelection",
        "ctrl shift PAGE_DOWN", "scrollDownExtendSelection",
        "ctrl PAGE_DOWN", "scrollDownChangeLead",
        "HOME", "selectFirst",
        "shift HOME", "selectFirstExtendSelection",
        "ctrl shift HOME", "selectFirstExtendSelection",
        "ctrl HOME", "selectFirstChangeLead",
        "END", "selectLast",
        "shift END", "selectLastExtendSelection",
        "ctrl shift END", "selectLastExtendSelection",
        "ctrl END", "selectLastChangeLead",
        "F2", "startEditing",
        "ctrl A", "selectAll",
        "ctrl SLASH", "selectAll",
        "ctrl BACK_SLASH", "clearSelection",
        "ctrl LEFT", "scrollLeft",
        "ctrl KP_LEFT", "scrollLeft",
        "ctrl RIGHT", "scrollRight",
        "ctrl KP_RIGHT", "scrollRight",
        "SPACE", "addToSelection",
        "ctrl SPACE", "toggleAndAnchor",
        "shift SPACE", "extendTo",
        "ctrl shift SPACE", "moveSelectionTo"
      } ),
      "Tree.ancestorInputMap",
      new UIDefaults.LazyInputMap( new Object[]{
        "ESCAPE", "cancel"
      } ),
    };
    table.putDefaults( defaults );
  }
}