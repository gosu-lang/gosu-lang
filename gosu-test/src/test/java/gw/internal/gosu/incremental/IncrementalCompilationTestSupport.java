package gw.internal.gosu.incremental;

import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gw.internal.gosu.incremental.IncrementalCompilationManager.DEPENDENCY_VERSION;


final class IncrementalCompilationTestSupport
{
  /** The {@code abi_hash} written for a type a test gives no hash of its own. */
  static final String PLACEHOLDER_HASH = "0000000000000000000000000000000000000000";

  private IncrementalCompilationTestSupport()
  {
  }

  /**
   * Test-only helper: write a dependency JSON file directly, bypassing the
   * incremental compilation machinery. Use when a test only needs to seed a
   * dep file with a known producer -> consumers mapping and then load it via
   * a fresh {@link IncrementalCompilationManager}. Output format matches what
   * {@code updateDependencyFile} produces: current version, one entry per type
   * with its {@code abi_hash} ({@link #PLACEHOLDER_HASH} here) and sorted
   * consumers, types sorted.
   */
  static void writeDependencyFile( File depFile, Map<String, List<String>> producerToConsumers )
  {
    writeDependencyFile( depFile, DEPENDENCY_VERSION, producerToConsumers, Collections.emptyMap() );
  }

  /**
   * As {@link #writeDependencyFile(File, Map)}, giving the listed types the given hashes;
   * every other type gets {@link #PLACEHOLDER_HASH}.
   */
  static void writeDependencyFile( File depFile, Map<String, List<String>> producerToConsumers,
                                   Map<String, String> abiHashes )
  {
    writeDependencyFile( depFile, DEPENDENCY_VERSION, producerToConsumers, abiHashes );
  }

  /**
   * Write a dependency file with an explicit {@code version}, as a gosuc reading another
   * format version would have left it. A {@code null} {@code abiHashes} omits the
   * {@code abi_hash} field from every entry, which no gosuc ever writes: it models a
   * hand-edited or otherwise malformed file.
   */
  static void writeDependencyFile( File depFile, String version, Map<String, List<String>> producerToConsumers,
                                   Map<String, String> abiHashes )
  {
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    Map<String, Object> root = new LinkedHashMap<>();
    root.put( "version", version );

    Map<String, Object> depGraph = new TreeMap<>();
    for( Map.Entry<String, List<String>> entry : producerToConsumers.entrySet() )
    {
      List<String> consumers = new ArrayList<>( entry.getValue() );
      Collections.sort( consumers );
      LinkedHashMap<Object, Object> data = new LinkedHashMap<>();
      if( abiHashes != null )
      {
        data.put( "abi_hash", abiHashes.getOrDefault( entry.getKey(), PLACEHOLDER_HASH ) );
      }
      data.put( "consumers", consumers );
      depGraph.put( entry.getKey(), data );
    }
    root.put( "dep_graph", depGraph );

    File parent = depFile.getParentFile();
    if( parent != null )
    {
      parent.mkdirs();
    }
    try (Writer w = new BufferedWriter( new OutputStreamWriter(
      new FileOutputStream( depFile ), StandardCharsets.UTF_8 ) ))
    {
      gson.toJson( root, w );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
