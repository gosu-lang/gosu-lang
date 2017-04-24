package gw.fs.cache;

import gw.lang.reflect.module.IModule;
import gw.util.concurrent.Cache;
import gw.util.concurrent.LocklessLazyVar;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 */
public class ModulePathCache
{
  private static final ModulePathCache INSTANCE = new ModulePathCache();

  private final Cache<IModule, LocklessLazyVar<PathCache>> _cacheByModule =
    new Cache<>( "Path Cache", 1000, module ->
      LocklessLazyVar.make( () -> makePathCache( module ) ) );

  public static ModulePathCache instance()
  {
    return INSTANCE;
  }

  private ModulePathCache()
  {
  }

  public PathCache get( IModule module )
  {
    return _cacheByModule.get( module ).get();
  }

  private PathCache makePathCache( IModule module )
  {
    return new PathCache( module,
      () ->
        module.getSourcePath().stream()
          .filter( dir -> Arrays.stream( module.getFileRepository().getExcludedPath() )
            .noneMatch( excludeDir -> excludeDir.equals( dir ) ) )
          .collect( Collectors.toList() ),
      () -> _cacheByModule.get( module ).clear() );
  }
}
