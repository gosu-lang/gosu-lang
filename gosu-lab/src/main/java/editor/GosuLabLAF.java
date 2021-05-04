package editor;

import editor.util.EditorUtilities;
import editor.util.FixupLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;

/**
 */
public class GosuLabLAF extends BasicLookAndFeel
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

  protected void initComponentDefaults( UIDefaults table)
  {
    super.initComponentDefaults( table );

    final String metalPackageName = "javax.swing.plaf.metal.";
    
    // *** Shared Fonts
    UIDefaults.LazyValue dialogPlain12 = t -> new FontUIResource( Font.DIALOG, Font.PLAIN, 12);

    UIDefaults.LazyValue sansSerifPlain12 = t -> new FontUIResource(Font.SANS_SERIF, Font.PLAIN, 12);
    UIDefaults.LazyValue monospacedPlain12 = t -> new FontUIResource(Font.MONOSPACED, Font.PLAIN, 12);
    UIDefaults.LazyValue dialogBold12 = t -> new FontUIResource(Font.DIALOG, Font.BOLD, 12);

    // *** Colors
    ColorUIResource red = new ColorUIResource(Color.red);
    ColorUIResource black = new ColorUIResource(Color.black);
    ColorUIResource white = new ColorUIResource(Color.white);
    ColorUIResource gray = new ColorUIResource(Color.gray);
    ColorUIResource darkGray = new ColorUIResource(Color.darkGray);
    ColorUIResource scrollBarTrackHighlight = darkGray;

    // *** Tree
    Object treeExpandedIcon = LabTreeUi.ExpandedIcon.createExpandedIcon();

    Object treeCollapsedIcon = LabTreeUi.CollapsedIcon.createCollapsedIcon();


    // *** Text
    Object fieldInputMap = new UIDefaults.LazyInputMap(new Object[] {
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
    });

    Object passwordInputMap = new UIDefaults.LazyInputMap(new Object[] {
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
    });

    Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] {
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
    });

    //The following four lines were commented out as part of bug 4991597
    //This code *is* correct, however it differs from WindowsXP and is, apparently
    //a Windows XP bug. Until Windows fixes this bug, we shall also exhibit the same
    //behavior
    //Object ReadOnlyTextBackground = new XPColorValue(Part.EP_EDITTEXT, State.READONLY, Prop.FILLCOLOR,
    //                                                 ControlBackgroundColor);
    //Object DisabledTextBackground = new XPColorValue(Part.EP_EDITTEXT, State.DISABLED, Prop.FILLCOLOR,
    //                                                 ControlBackgroundColor);
    Object MenuFont = dialogPlain12;
    Object FixedControlFont = monospacedPlain12;
    Object ControlFont = dialogPlain12;
    Object MessageFont = dialogPlain12;
    Object WindowFont = dialogBold12;
    Object ToolTipFont = sansSerifPlain12;
    Object IconFont = ControlFont;

    Object[] defaults = {
      // *** Auditory Feedback
      // this key defines which of the various cues to render
      // Overridden from BasicL&F. This L&F should play all sounds
      // all the time. The infrastructure decides what to play.
      // This is disabled until sound bugs can be resolved.
      "AuditoryCues.playList", null, // table.get("AuditoryCues.cueList"),

      "TextField.focusInputMap", fieldInputMap,
      "PasswordField.focusInputMap", passwordInputMap,
      "TextArea.focusInputMap", multilineInputMap,
      "TextPane.focusInputMap", multilineInputMap,
      "EditorPane.focusInputMap", multilineInputMap,

      // Buttons
      "Button.font", ControlFont,
      // Button.foreground, Button.shadow, Button.darkShadow,
      // Button.disabledForground, and Button.disabledShadow are only
      // used for Windows Classic. Windows XP will use colors
      // from the current visual style.
      // W2K keyboard navigation hidding.
      "Button.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "SPACE", "pressed",
        "released SPACE", "released"
      }),

      "CheckBox.font", ControlFont,
      "CheckBox.icon",(UIDefaults.LazyValue)t -> MetalIconFactory.getCheckBoxIcon(),
      "RadioButton.icon",(UIDefaults.LazyValue)t -> MetalIconFactory.getRadioButtonIcon(),
      "CheckBox.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "SPACE", "pressed",
        "released SPACE", "released"
      }),
      // margin is 2 all the way around, BasicBorders.RadioButtonBorder
      // (checkbox uses RadioButtonBorder) is 2 all the way around too.
      "CheckBox.totalInsets", new Insets(4, 4, 4, 4),

      "CheckBoxMenuItem.font", MenuFont,
      "ComboBox.font", ControlFont,
      "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] {
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
    }),

      // DesktopIcon
      "DesktopIcon.width", 160,

      "EditorPane.font", ControlFont,
      "FileChooser.listFont", IconFont,
      "FileChooser.usesSingleFilePane", Boolean.TRUE,
      "FileChooser.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "ESCAPE", "cancelSelection",
        "F2", "editFileName",
        "F5", "refresh",
        "BACK_SPACE", "Go Up"
      }),

      "FormattedTextField.font", ControlFont,
      "InternalFrame.titleFont", WindowFont,

      "InternalFrame.windowBindings", new Object[] {
      "shift ESCAPE", "showSystemMenu",
      "ctrl SPACE", "showSystemMenu",
      "ESCAPE", "hideSystemMenu"},

      "List.lockToPositionOnScroll", Boolean.TRUE,
      "List.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),

      "FormattedTextField.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),

      // *** RootPane.
      // These bindings are only enabled when there is a default
      // button set on the rootpane.
      "RootPane.defaultButtonWindowKeyBindings", new Object[] {
      "ENTER", "press",
      "released ENTER", "release",
      "ctrl ENTER", "press",
      "ctrl released ENTER", "release"
    },

      // *** ScrollBar.
      "ScrollBar.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),

      // *** ScrollPane.
      "ScrollPane.font", ControlFont,
      "ScrollPane.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),


      // *** SplitPane
      "SplitPane.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),

      // *** TabbedPane
      "TabbedPane.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),
      "TabbedPane.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "ctrl TAB", "navigateNext",
        "ctrl shift TAB", "navigatePrevious",
        "ctrl PAGE_DOWN", "navigatePageDown",
        "ctrl PAGE_UP", "navigatePageUp",
        "ctrl UP", "requestFocus",
        "ctrl KP_UP", "requestFocus",
      }),

      // *** TextArea
      "TextArea.font", FixedControlFont,

      // *** TextField
      "TextField.font", ControlFont,

      // *** TextPane
      "TextPane.font", ControlFont,

      // *** TitledBorder
      "TitledBorder.font", ControlFont,
      // *** ToggleButton
      "ToggleButton.font", ControlFont,
      "ToggleButton.textShiftOffset", 1,
      "ToggleButton.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "SPACE", "pressed",
        "released SPACE", "released"
      }),

      // *** ToolBar
      "ToolBar.font", MenuFont,
      "ToolBar.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "UP", "navigateUp",
        "KP_UP", "navigateUp",
        "DOWN", "navigateDown",
        "KP_DOWN", "navigateDown",
        "LEFT", "navigateLeft",
        "KP_LEFT", "navigateLeft",
        "RIGHT", "navigateRight",
        "KP_RIGHT", "navigateRight"
      }),
      // *** ToolTip
      "ToolTip.font", ToolTipFont,
      // *** ToolTipManager
      "ToolTipManager.enableToolTipMode", "activeApplication",

      // *** Tree
      "Tree.selectionBorderColor", black,
      "Tree.drawDashedFocusIndicator", Boolean.TRUE,
      "Tree.lineTypeDashed", Boolean.TRUE,
      "Tree.font", ControlFont,
      "Tree.hash", gray,
      "Tree.leftChildIndent", 8,
      "Tree.rightChildIndent", 11,
      "Tree.expandedIcon", treeExpandedIcon,
      "Tree.collapsedIcon", treeCollapsedIcon,
      "Tree.focusInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
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
      }),
      "Tree.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "ESCAPE", "cancel"
      }),

      // *** Viewport
      "Viewport.font", ControlFont,

      // File View
      "FileView.directoryIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getTreeFolderIcon(),
      "FileView.fileIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getTreeLeafIcon(),
      "FileView.computerIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getTreeComputerIcon(),
      "FileView.hardDriveIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getTreeHardDriveIcon(),
      "FileView.floppyDriveIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getTreeFloppyDriveIcon(),

      // File Chooser
      "FileChooserUI", metalPackageName + "MetalFileChooserUI",
      "FileChooser.detailsViewIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getFileChooserDetailViewIcon(),
      "FileChooser.homeFolderIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getFileChooserHomeFolderIcon(),
      "FileChooser.listViewIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getFileChooserListViewIcon(),
      "FileChooser.newFolderIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getFileChooserNewFolderIcon(),
      "FileChooser.upFolderIcon",(UIDefaults.LazyValue)t -> MetalIconFactory.getFileChooserUpFolderIcon(),

      "FileChooser.usesSingleFilePane", Boolean.TRUE,
      "FileChooser.ancestorInputMap",
      new UIDefaults.LazyInputMap(new Object[] {
        "ESCAPE", "cancelSelection",
        "F2", "editFileName",
        "F5", "refresh",
        "BACK_SPACE", "Go Up"
      }),

    };

    table.putDefaults(defaults);
  }

  public static void setLookAndFeel()
  {
    try
    {
      System.setProperty( "swing.noxp", "true" );

      UIManager.setLookAndFeel( GosuLabLAF.class.getName() );

      FixupLookAndFeel.fixupFieldBorders();
      FixupLookAndFeel.fixupTreeHandles();

      UIDefaults table = UIManager.getLookAndFeelDefaults();

      table.put( "ComboBoxUI", LabComboBoxUI.class.getName() );
      table.put( "MenuItemUI", BasicMenuItemUI.class.getName() );
      table.put( "ButtonUI", LabButtonUI.class.getName() );

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
      table.put( "ScrollBar.squareButtons", true );
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

      table.put( "ToolTip.background", Scheme.active().getTooltipBackground() );
      table.put( "ToolTip.foreground", Scheme.active().getTooltipText() );
      table.put( "ToolTip.border", BorderFactory.createLineBorder( Scheme.active().getFieldBorderColor() ) );

      table.put( "Separator.foreground", Scheme.active().getSeparator1() );
      table.put( "Separator.background", Scheme.active().getSeparator2() );


      table.put( "Button.foreground", Scheme.active().getControlText() );
      table.put( "Button.disabledForeground", Scheme.active().getControlDisabledText() );
      table.put( "Button.disabledShadow", Scheme.active().getControl() );
      table.put( "Button.background", Scheme.active().getControl() );
      table.put( "Button.interiorBackground", Scheme.active().getControl() );
      table.put( "Button.shadow", Scheme.active().getControl() );
      table.put( "Button.darkShadow", Scheme.active().getControlDarkshadow() );
      table.put( "Button.light", Scheme.active().getControl() );
      table.put( "Button.highlight", Scheme.active().getControlDarkshadow() );

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
      table.put( "Menu.disabledForeground", Scheme.active().getControlDisabledText() );
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
      table.put( "MenuItem.disabledForeground", Scheme.active().getControlDisabledText() );
      table.put( "MenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "MenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "MenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "MenuItem.disabledAreNavigable", false );
      
      table.put( "RadioButtonMenuItem.foreground", Scheme.active().getMenuText() );
      table.put( "RadioButtonMenuItem.background", Scheme.active().getMenu() );
      table.put( "RadioButtonMenuItem.selectionForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "RadioButtonMenuItem.disabledForeground", Scheme.active().getControlDisabledText() );
      table.put( "RadioButtonMenuItem.acceleratorForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.acceleratorSelectionForeground", Scheme.active().getWindowText() );
      table.put( "RadioButtonMenuItem.border", BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      table.put( "RadioButtonMenuItem.disabledAreNavigable", false );
      
      table.put( "CheckBoxMenuItem.foreground", Scheme.active().getMenuText() );
      table.put( "CheckBoxMenuItem.background", Scheme.active().getMenu() );
      table.put( "CheckBoxMenuItem.selectionForeground", Scheme.active().getWindowText() );
      table.put( "CheckBoxMenuItem.selectionBackground", Scheme.active().getActiveCaption() );
      table.put( "CheckBoxMenuItem.disabledForeground", Scheme.active().getControlDisabledText() );
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
      table.put( "Label.disabledForeground", Scheme.active().getControlDisabledText() );
      table.put( "Label.disabledShadow", Scheme.active().getControl() );
      table.put( "Label.background", Scheme.active().getControl() );
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
