/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.json.Json;
import gw.util.cache.FqnCache;
import gw.util.concurrent.LocklessLazyVar;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Makes available the standard system properties, which should be available on all JVMs
 */
public class SystemProperties
{
  private static final String FQN = "gw.lang.SystemProperties";

  public static Map<String, LocklessLazyVar<FqnCache<String>>> make()
  {
    Map<String, LocklessLazyVar<FqnCache<String>>> systemProps = new HashMap<>( 2 );
    systemProps.put( FQN,
      LocklessLazyVar.make(
      () -> {
        FqnCache<String> cache = new FqnCache<>( FQN, true, Json::makeIdentifier );
        _keys.forEach( key -> cache.add( key, System.getProperty( key ) ) );
        return cache;
      } ) );
    return systemProps;
  }

  private static final Set<String> _keys = Collections.unmodifiableSet( new TreeSet<>( Arrays.asList(
    "java.version",
    "java.vendor",
    "java.vendor.url",
    "java.home",
    "java.vm.specification.version",
    "java.vm.specification.vendor",
    "java.vm.specification.name",
    "java.vm.version",
    "java.vm.vendor",
    "java.vm.name",
    "java.specification.version",
    "java.specification.vendor",
    "java.specification.name",
    "java.class.version",
    "java.class.path",
    "java.library.path",
    "java.io.tmpdir",
    "java.compiler",
    "java.ext.dirs",
    "os.name",
    "os.arch",
    "os.version",
    "file.separator",
    "path.separator",
    "line.separator",
    "user.name",
    "user.home",
    "user.dir"
  ) ) );
}
