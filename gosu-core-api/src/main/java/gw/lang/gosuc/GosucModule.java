/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.fs.IDirectory;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.reflect.module.INativeModule;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GosucModule implements INativeModule, Serializable {
  private String _name;
  private List<String> _allSourceRoots;
  private List<String> _excludedRoots;
  private List<String> _classpath;
  private List<String> _backingSourcePath;
  private String _outputPath;
  private List<GosucDependency> _dependencies;

  public GosucModule(String name,
                     List<String> allSourceRoots,
                     List<String> classpath,
                     List<String> backingSourcePath,
                     String outputPath,
                     List<GosucDependency> dependencies,
                     List<String> excludedRoots) {
    _allSourceRoots = new ArrayList<>();
    //noinspection Convert2streamapi
    for (String sourceRoot : allSourceRoots) {
      if (!sourceRoot.endsWith(".jar")) {
        _allSourceRoots.add( convertToUri( sourceRoot ) );
      }
    }
    makeUris( excludedRoots, _excludedRoots = new ArrayList<>() );
    makeUris( classpath, _classpath = new ArrayList<>() );
    makeUris( backingSourcePath, _backingSourcePath = new ArrayList<>() );
    _outputPath = convertToUri( outputPath );
    _dependencies = dependencies;
    _name = name;
  }

  private void makeUris( List<String> from, List<String> to )
  {
    for( String path: from )
    {
      to.add( convertToUri( path ) );
    }
  }

  private String convertToUri( String filePath )
  {
    try
    {
      File file = new File( filePath );
      if( file.exists() )
      {
        return file.getAbsoluteFile().toURI().toString();
      }
    }
    catch( Exception ignore )
    {
    }

    try
    {
      URI uri = URI.create( filePath );
      if( !uri.isAbsolute() )
      {
        filePath = new File( filePath ).getAbsoluteFile().toURI().toString();
      }
    }
    catch( Exception e )
    {
      filePath = new File( filePath ).getAbsoluteFile().toURI().toString();
    }
    return filePath;
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

  public List<String> getBackingSourcePath() {
    return _backingSourcePath;
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
        writePath( getAllSourceRoots() ) +
        "  }\n" +
        "  excludedpath {\n" +
        writePath( getExcludedRoots() ) +
        "  }\n" +
        "  classpath {\n" +
        writePath( getClasspath() ) +
        "  }\n" +
        "  backingsource {\n" +
        writePath( getBackingSourcePath() ) +
        "  }\n" +
        "  outpath {\n" +
        writeOutputPath() +
        "  }\n" +
        "  deps {\n" +
        writeDependencies() +
        "  }\n" +
        "}\n";
  }

  private String writePath( List<String> paths ) {
    StringBuilder sb = new StringBuilder();
    for( String path : paths ) {
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
    List<String> backingSourcePath = parseBackingSourcePath(parser);
    String outputPath = parseOutputPath(parser);
    List<GosucDependency> deps = parseDependencies(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close module definition");
    return new GosucModule( name, sourcepaths, classpath, backingSourcePath, outputPath, deps, excludedRoots );
  }

  private static List<GosucDependency> parseDependencies(GosucProjectParser parser) {
    parser.verify(parser.matchWord("deps", false), "Expecting keyword 'deps'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin deps list");
    List<GosucDependency> deps = parseDependenciesList( parser );
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close deps list");
    return deps;
  }

  private static List<GosucDependency> parseDependenciesList(GosucProjectParser parser) {
    List<GosucDependency> deps = new ArrayList<>();
    while (parser.match(null, ISourceCodeTokenizer.TT_WORD, true)) {
      deps.add(GosucDependency.parse(parser));
    }
    return deps;
  }

  private static List<String> parseClasspath(GosucProjectParser parser) {
    parser.verify(parser.matchWord("classpath", false), "Expecting keyword 'classpath'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin classpath list");
    List<String> classpaths = parsePathList( parser );
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close classpath list");
    return classpaths;
  }

  private static List<String> parseBackingSourcePath(GosucProjectParser parser) {
    if( parser.matchWord( "backingsource", false ) )
    {
      parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin backingsource list" );
      List<String> backingSourcePath = parsePathList( parser );
      parser.verify( parser.match( null, '}', false ), "Expecting '}' to close backingsource list" );
      return backingSourcePath;
    }
    return Collections.emptyList();
  }

  private static List<String> parsePaths(String word, GosucProjectParser parser) {
    parser.verify(parser.matchWord(word, false), "Expecting keyword 'sourcepath'");
    parser.verify(parser.match(null, '{', false), "Expecting '{' to begin " + word + " list");
    List<String> sourcepaths = parsePathList(parser);
    parser.verify(parser.match(null, '}', false), "Expecting '}' to close " + word + " list");
    return sourcepaths;
  }

  private static List<String> parsePathList( GosucProjectParser parser ) {
    List<String> paths = new ArrayList<>();
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

    if (!_allSourceRoots.equals(that._allSourceRoots)) {
      return false;
    }
    if (!_excludedRoots.equals(that._excludedRoots)) {
      return false;
    }
    if (!_classpath.equals(that._classpath)) {
      return false;
    }
    if (!_backingSourcePath.equals(that._backingSourcePath)) {
      return false;
    }
    if (!_dependencies.equals(that._dependencies)) {
      return false;
    }
    if (!_name.equals(that._name)) {
      return false;
    }
    return !(_outputPath != null ? !_outputPath.equals( that._outputPath ) : that._outputPath != null);
  }

  @Override
  public int hashCode() {
    int result = _name.hashCode();
    result = 31 * result + _allSourceRoots.hashCode();
    result = 31 * result + _excludedRoots.hashCode();
    result = 31 * result + _classpath.hashCode();
    result = 31 * result + _backingSourcePath.hashCode();
    result = 31 * result + (_outputPath != null ? _outputPath.hashCode() : 0);
    result = 31 * result + _dependencies.hashCode();
    return result;
  }
}
