/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.fs.IDirectory;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.reflect.module.INativeModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GosucModule implements INativeModule, Serializable {
  private String _name;
  private List<String> _contentRoots;
  private List<String> _allSourceRoots;
  private List<String> _excludedRoots;
  private List<String> _classpath;
  private String _outputPath;
  private List<GosucDependency> _dependencies;

  public GosucModule(String name,
                     List<String> allSourceRoots,
                     List<String> classpath,
                     String outputPath,
                     List<GosucDependency> dependencies,
                     List<String> excludedRoots) {
    this(name, Collections.EMPTY_LIST, allSourceRoots, classpath, outputPath, dependencies, excludedRoots);
  }

  public GosucModule(String name,
                     List<String> contentRoots,
                     List<String> allSourceRoots,
                     List<String> classpath,
                     String outputPath,
                     List<GosucDependency> dependencies,
                     List<String> excludedRoots) {
    _contentRoots = contentRoots;
    _allSourceRoots = new ArrayList<String>();
    for (String sourceRoot : allSourceRoots) {
      if (!sourceRoot.endsWith(".jar")) {
        _allSourceRoots.add(sourceRoot);
      }
    }
    _excludedRoots = new ArrayList<String>(excludedRoots);
    _classpath = classpath;
    _outputPath = outputPath;
    _dependencies = dependencies;
    _name = name;
  }

  public List<String> getContentRoots() {
    return _contentRoots;
  }

  public String getContentRoot() {
    return _contentRoots.get(0);
  }

  public List<String> getAllSourceRoots() {
    return _allSourceRoots;
  }

  public List<String> getExcludedRoots() {
    return _excludedRoots;
  }

  public List<String> getClasspath() {
    return _classpath;
  }

  public List<GosucDependency> getDependencies() {
    return _dependencies;
  }

  public String getName() {
    return _name;
  }

  @Override
  public Object getNativeModule() {
    return this;
  }

  @Override
  public IDirectory getOutputPath() {
    return _outputPath != null ? GosucUtil.getDirectoryForPath(_outputPath) : null;
  }

  public String write() {
    return getName() + " {\n" +
        "  sourcepath {\n" +
        writeRoots(getAllSourceRoots()) +
        "  }\n" +
        "  excludedpath {\n" +
        writeRoots(getExcludedRoots()) +
        "  }\n" +
        "  classpath {\n" +
        writeClasspath() +
        "  }\n" +
        "  outpath {\n" +
        writeOutputPath() +
        "  }\n" +
        "  deps {\n" +
        writeDependencies() +
        "  }\n" +
        "}\n";
  }

  private String writeRoots(List<String> roots) {
    StringBuilder sb = new StringBuilder();
    for (String sourceRoot : roots) {
      sb.append("    ").append("\"").append(sourceRoot).append("\",\n");
    }
    return sb.toString();
  }

  private String writeClasspath() {
    StringBuilder sb = new StringBuilder();
    for (String path : getClasspath()) {
      sb.append("    ").append("\"").append(path).append("\",\n");
    }
    return sb.toString();
  }

  private String writeOutputPath() {
    return _outputPath != null ? "    \"" + _outputPath + "\"\n" :
        "    \"\"\n";
  }

  private String writeDependencies() {
    StringBuilder sb = new StringBuilder();
    for (GosucDependency dep : getDependencies()) {
      sb.append("    ").append(dep.write()).append("\n");
    }
    return sb.toString();
  }

  public static GosucModule parse(GosucProjectParser parser) {
    IToken t = parser.getTokenizer().getCurrentToken();
    parser.verify(parser.match(null, ISourceCodeTokenizer.TT_WORD, false), "Expecting module name");
    String name = t.getStringValue();
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin module definition");
    List<String> sourcepaths = parsePaths("sourcepath", parser);
    List<String> excludedRoots = parsePaths("excludedpath", parser);
    List<String> classpath = parseClasspath(parser);
    String outputPath = parseOutputPath(parser);
    List<GosucDependency> deps = parseDependencies(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close module definition");
    return new GosucModule(name, sourcepaths, classpath, outputPath, deps, excludedRoots);
  }

  private static List<GosucDependency> parseDependencies(GosucProjectParser parser) {
    parser.verify(parser.matchWord("deps", false), "Expecting keyword 'deps'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin deps list");
    List<GosucDependency> deps = parseDependenciesList(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close deps list");
    return deps;
  }

  private static List<GosucDependency> parseDependenciesList(GosucProjectParser parser) {
    List<GosucDependency> deps = new ArrayList<GosucDependency>();
    while (parser.match(null, ISourceCodeTokenizer.TT_WORD, true)) {
      deps.add(GosucDependency.parse(parser));
    }
    return deps;
  }

  private static List<String> parseClasspath(GosucProjectParser parser) {
    parser.verify(parser.matchWord("classpath", false), "Expecting keyword 'classpath'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin classpath list");
    List<String> classpaths = parseClasspathList(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close classpath list");
    return classpaths;
  }

  private static List<String> parseClasspathList(GosucProjectParser parser) {
    List<String> paths = new ArrayList<String>();
    for (IToken t = parser.getTokenizer().getCurrentToken(); parser.match(null, '"', false); t = parser.getTokenizer().getCurrentToken()) {
      paths.add(t.getStringValue());
      if (!parser.match(null, ',', false)) {
        break;
      }
    }
    return paths;
  }

  private static List<String> parsePaths(String word, GosucProjectParser parser) {
    parser.verify(parser.matchWord(word, false), "Expecting keyword 'sourcepath'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin " + word + " list");
    List<String> sourcepaths = parseSourcePathList(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close " + word + " list");
    return sourcepaths;
  }

  private static List<String> parseSourcePathList(GosucProjectParser parser) {
    List<String> paths = new ArrayList<String>();
    for (IToken t = parser.getTokenizer().getCurrentToken(); parser.match(null, '"', false); t = parser.getTokenizer().getCurrentToken()) {
      paths.add(t.getStringValue());
      if (!parser.match(null, ',', false)) {
        break;
      }
    }
    return paths;
  }

  private static String parseOutputPath(GosucProjectParser parser) {
    parser.verify(parser.matchWord("outpath", false), "Expecting keyword 'outpath'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin outpath list");
    IToken t = parser.getTokenizer().getCurrentToken();
    parser.verify(parser.match(null, '"', false), "Expecting quoted path");
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close outpath list");
    String value = t.getStringValue();
    return value.trim().isEmpty() ? null : value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GosucModule that = (GosucModule) o;

    if (!_contentRoots.equals(that._contentRoots)) {
      return false;
    }
    if (!_allSourceRoots.equals(that._allSourceRoots)) {
      return false;
    }
    if (!_excludedRoots.equals(that._excludedRoots)) {
      return false;
    }
    if (!_classpath.equals(that._classpath)) {
      return false;
    }
    if (!_dependencies.equals(that._dependencies)) {
      return false;
    }
    if (!_name.equals(that._name)) {
      return false;
    }
    if (_outputPath != null ? !_outputPath.equals(that._outputPath) : that._outputPath != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _name.hashCode();
    result = 31 * result + _contentRoots.hashCode();
    result = 31 * result + _allSourceRoots.hashCode();
    result = 31 * result + _excludedRoots.hashCode();
    result = 31 * result + _classpath.hashCode();
    result = 31 * result + (_outputPath != null ? _outputPath.hashCode() : 0);
    result = 31 * result + _dependencies.hashCode();
    return result;
  }
}
