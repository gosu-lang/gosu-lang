/*
 *
 * 2002 CaseCenter by Centrica Software
 *
 */
package editor.actions;



/**
 * For handling toggle componenets e.g., JCheckMenuItem, ToggleButton, etc.
 */
public interface ToggleConditionalActionHandler extends ConditionalActionHandler
{
  /**
   * @return True if the selected state for the action is true.
   */
  public boolean isSelected();
}
