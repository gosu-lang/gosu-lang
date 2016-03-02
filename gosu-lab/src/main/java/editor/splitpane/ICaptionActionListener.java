package editor.splitpane;

/**
 */
public interface ICaptionActionListener
{
  public void captionActionPerformed( ICaptionedPanel panel, ActionType actionType );

  public static class ActionType
  {
    public static final ActionType MINIMIZE = new ActionType();
    public static final ActionType RESTORE = new ActionType();
    public static final ActionType MAXIMIZE = new ActionType();

    private ActionType()
    {
    }
  }
}
