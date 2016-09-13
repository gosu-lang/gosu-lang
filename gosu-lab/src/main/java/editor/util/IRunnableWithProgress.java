package editor.util;

/**
 * A runnable task capable of indicating some form of progress feedback.
 */
public interface IRunnableWithProgress
{
  public void run( IProgressCallback cbProgress );
}

