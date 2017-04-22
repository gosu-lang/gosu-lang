/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;


import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.SourcePositions;
import editor.util.transform.java.visitor.GosuVisitor;
import gw.lang.javac.IJavaParser;
import gw.lang.parser.GosuParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class JavaToGosu
{

  public static String convertString( String javaSource, int... indent )
  {
    Wrap[] wraps = new Wrap[]{
      new Wrap( "", "", "", "" ),
      new Wrap( "class _JAVA_TO_GOSU_INTERNAL_ {",
                "\n}",
                "class _JAVA_TO_GOSU_INTERNAL_  {\n\n",
                "\n}\n" ),
      new Wrap( "class _JAVA_TO_GOSU_INTERNAL_ { void _JAVA_TO_GOSU_INTERNAL_METHOD_() {",
                "\n}}",
                "class _JAVA_TO_GOSU_INTERNAL_  {\n\n  function _JAVA_TO_GOSU_INTERNAL_METHOD_() : void {\n",
                "\n  }\n\n}\n" ),
      new Wrap( "class _JAVA_TO_GOSU_INTERNAL_ { void _JAVA_TO_GOSU_INTERNAL_METHOD_() {",
                ";\n}}",
                "class _JAVA_TO_GOSU_INTERNAL_  {\n\n  function _JAVA_TO_GOSU_INTERNAL_METHOD_() : void {\n",
                "\n  }\n\n}\n" ),
      new Wrap( "class _JAVA_TO_GOSU_INTERNAL_ { void _JAVA_TO_GOSU_INTERNAL_METHOD_() { if(",
                ");\n}}",
                "class _JAVA_TO_GOSU_INTERNAL_  {\n\n  function _JAVA_TO_GOSU_INTERNAL_METHOD_() : void {\n    if (",
                ") {\n      \n    }\n  }\n\n}\n" )
    };
    String javaWrapStart = "";
    String javaWrapEnd = "";
    String gosuWrapStart = "";
    String gosuWrapEnd = "";
    List<CompilationUnitTree> trees = null;
    final DocTrees[] docTrees = {null};
    boolean parsed = false;
    String output;
    int i = wraps.length - 1;

    String src = null;
    while( i >= 0 && !parsed )
    {
      javaWrapStart = wraps[i].JAVA_WRAP_START;
      javaWrapEnd = wraps[i].JAVA_WRAP_END;
      gosuWrapStart = wraps[i].GOSU_WRAP_START;
      gosuWrapEnd = wraps[i].GOSU_WRAP_END;
      trees = new ArrayList<>();
      src = javaWrapStart + javaSource + javaWrapEnd;
      parsed = parseJava( trees, null, dc -> docTrees[0] = dc, src );
      i--;
    }
    if( !parsed )
    {
      return "";
    }
    GosuVisitor visitor = new GosuVisitor( indent == null || indent.length == 0 ? 2 : indent[0], docTrees[0] );
    for( CompilationUnitTree tree : trees )
    {
      tree.accept( visitor, null );
    }
    output = visitor.getOutput().toString();

    if( !javaWrapStart.isEmpty() )
    {
      int b = output.indexOf( gosuWrapStart ) + gosuWrapStart.length();
      int e = output.lastIndexOf( gosuWrapEnd );
      output = unIndent( output.substring( b, e ) );
    }
    return output;
  }

  private static String unIndent( String output )
  {
    StringBuilder src = new StringBuilder();
    String[] lines = output.split( "\n" );
    boolean leadingLines = true;
    List<String> removedEmptyLeadingLines = new ArrayList<>();
    int index = 0;
    for( ; index < lines.length; index++ )
    {
      if( !leadingLines || !lines[index].trim().isEmpty() )
      {
        removedEmptyLeadingLines.add( lines[index] );
        leadingLines = false;
      }
    }
    lines = removedEmptyLeadingLines.toArray( new String[removedEmptyLeadingLines.size()] );
    int tab = 0;
    char[] chars = lines[0].toCharArray();
    while( tab < chars.length && chars[tab] == ' ' )
    {
      tab++;
    }
    for( String line : lines )
    {
      if( !line.trim().isEmpty() )
      {
        if( line.length() > tab && line.substring( 0, tab ).trim().isEmpty() )
        {
          src.append( line.substring( tab ) );
        }
        else
        {
          src.append( line );
        }
        src.append( "\n" );
      }
      else
      {
        src.append( "\n" );
      }
    }
    return src.toString();
  }

  private static boolean parseJava( List<CompilationUnitTree> trees, Consumer<SourcePositions> sourcePositions, Consumer<DocTrees> docTrees, String src )
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class ).get( 0 );
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    if( !javaParser.parseText( src, trees, sourcePositions, docTrees, errorHandler ) )
    {
      return false;
    }
    return !errorHandler.getDiagnostics().stream().anyMatch( e -> e.getKind() == Diagnostic.Kind.ERROR );
  }
}


