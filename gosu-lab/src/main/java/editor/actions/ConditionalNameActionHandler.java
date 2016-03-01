package editor.actions;

public interface ConditionalNameActionHandler extends ConditionalActionHandler {
  /**
   * @return The string to display which can be dependent on the context
   */
  public String getDisplayName();
}
