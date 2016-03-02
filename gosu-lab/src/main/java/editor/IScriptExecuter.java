package editor;

/**
 */
public interface IScriptExecuter
{
  /**
   * Evaluates Gosu source.
   *
   * @param strScript A Gosu expression or scriptlet.
   *
   * @return The result of evaluating the script.
   */
  public String[] evaluate( String strScript );

  /**
   * Executes a Gosu template.
   *
   * @param strTemplate The template
   *
   * @return The resulting content.
   */
  public String executeTemplate( String strTemplate );

}