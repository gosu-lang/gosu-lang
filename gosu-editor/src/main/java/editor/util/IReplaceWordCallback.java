package editor.util;

/**
 * A callback interface facilitating word replacement in text components.
 */
public interface IReplaceWordCallback
{
  public boolean shouldReplace( String strWord );
}