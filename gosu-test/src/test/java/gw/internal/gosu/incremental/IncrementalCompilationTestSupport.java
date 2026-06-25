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


final class IncrementalCompilationTestSupport {
  private IncrementalCompilationTestSupport() {}

  /**
   * Test-only helper: write a dependency JSON file directly, bypassing the
   * incremental compilation machinery. Use when a test only needs to seed a
   * dep file with a known producer -> consumers mapping and then load it via
   * a fresh {@link IncrementalCompilationManager}. Output format matches what
   * {@code updateDependencyFile} produces: keys and consumer lists are sorted
   * for deterministic JSON.
   */
  static void writeDependencyFile(File depFile, Map<String, List<String>> producerToConsumers) {
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    Map<String, Object> root = new LinkedHashMap<>();
    root.put("version", DEPENDENCY_VERSION);

    Map<String, List<String>> sortedConsumers = new TreeMap<>();
    for (Map.Entry<String, List<String>> entry : producerToConsumers.entrySet()) {
      List<String> consumers = new ArrayList<>(entry.getValue());
      Collections.sort(consumers);
      sortedConsumers.put(entry.getKey(), consumers);
    }
    root.put("consumers", sortedConsumers);

    File parent = depFile.getParentFile();
    if (parent != null) {
      parent.mkdirs();
    }
    try (Writer w = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(depFile), StandardCharsets.UTF_8))) {
      gson.toJson(root, w);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
