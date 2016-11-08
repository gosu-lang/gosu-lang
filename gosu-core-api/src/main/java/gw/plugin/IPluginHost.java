package gw.plugin;

/**
 * This simple interface provides the core foundation for component architecture in Gosu.
 */
public interface IPluginHost
{
  /**
   * Provides an implementation of a specified interface.
   * @param apiInterface
   * @param <T>
   * @return The implementation of the interface or null if unsupported.
   */
  default <T> T getInterface( Class<T> apiInterface )
  {
    return null;
  }
}
