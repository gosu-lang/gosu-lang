package editor.util;

/**
 * An callback interface for indicating progress during a lengthy operation.
 * <p/>
 * Copyright 2010 Guidewire Software, Inc.
 */
public interface IProgressCallback
{
  /**
   * How long is this operation in units? For a StagedProgressFeedback, this moves to the next stage
   *
   * @param iLength The number of units the operation is expected to execute.
   */
  public void setLength( int iLength );

  /**
   * Call this when a unit of progress completes.
   *
   * @param iProgress  The unit of progress completed.
   * @param strMessage A short message describing the progress.
   *
   * @return Whether or not the operation should abort.
   * True to abort operation.
   */
  public boolean updateProgress( int iProgress, String strMessage, String... args );

  /**
   * Call this to update the progress message without modifying the amount of progress completed.
   */
  public boolean updateProgress( String strMessage, String... args );

  /**
   * Should the operation abort?
   *
   * @return True if the operation should abort.
   */
  public boolean isAbort();

  /**
   * Signals that the operation is complete
   */
  public void operationComplete();

  int getProgress();
}
