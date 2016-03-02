/*
 *
 * 2002 CaseCenter by Centrica Software
 *
 */
package editor.actions;

import java.awt.event.ActionListener;

/**
 * Extends ActionListener facilitating enable-state detection and exposure.
 */
public interface ConditionalActionHandler extends ActionListener
{
  /**
   * Return whether or not the action associated with this listener
   * is enabled.
   * <p/>
   * Achtung! Keep in mind implementations of this method should be short and
   * sweet -- don't do anything even moderately heavy here as this method is
   * called very very often -- every time the AWT event queue goes idle.
   *
   * @return True to enable the Action associated with this component.
   */
  public boolean isEnabled();
}
