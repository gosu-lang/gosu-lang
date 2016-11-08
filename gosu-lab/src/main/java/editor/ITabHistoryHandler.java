package editor;

/**
 */
public interface ITabHistoryHandler
{
  public ITabHistoryContext makeTabContext( EditorHost tab );

  public void selectTab( ITabHistoryContext tabContext );

  public void closeTab( ITabHistoryContext tabContext );
}
