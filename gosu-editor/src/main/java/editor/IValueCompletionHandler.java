package editor;

/**
 */
public interface IValueCompletionHandler
{
  public void setGosuEditor( GosuEditor gsEditor );

  public GosuEditor getGosuEditor();

  /**
   * @return True if this handler handled value completion. Note it is the
   * handler's responsibility to determine whether or not it can handle
   * completing the value. If it can't, it should return false and have no
   * side effects on the system.
   */
  public boolean handleCompleteValue();
}
