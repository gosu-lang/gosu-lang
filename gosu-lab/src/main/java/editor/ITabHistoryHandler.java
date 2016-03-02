package editor;

/**
 */
public interface ITabHistoryHandler
{
  public ITabHistoryContext makeTabContext( GosuEditor tab );

  public void selectTab( ITabHistoryContext tabContext );

  public void closeTab( ITabHistoryContext tabContext );
}
