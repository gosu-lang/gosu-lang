/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;


import com.sun.source.tree.CompilationUnitTree;
import editor.util.transform.java.visitor.GosuVisitor;
import gw.lang.javac.IJavaParser;
import gw.lang.parser.GosuParserFactory;
import java.util.ArrayList;
import java.util.List;

public class JavaToGosu
{

  public static String convertString( String javaSource )
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
    boolean parsed = false;
    String output = null;
    int i = wraps.length - 1;

    while( i >= 0 && !parsed )
    {
      javaWrapStart = wraps[i].JAVA_WRAP_START;
      javaWrapEnd = wraps[i].JAVA_WRAP_END;
      gosuWrapStart = wraps[i].GOSU_WRAP_START;
      gosuWrapEnd = wraps[i].GOSU_WRAP_END;
      trees = new ArrayList<CompilationUnitTree>();
      String src = javaWrapStart + javaSource + javaWrapEnd;
      parsed = parseJava( trees, src );
      i--;
    }
    if( !parsed )
    {
      return "";
    }
    GosuVisitor visitor = new GosuVisitor( 2 );
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
    }
    return src.toString();
  }

  private static boolean parseJava( List<CompilationUnitTree> trees, String src )
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    return javaParser.parseText( src, trees, null );
  }
}


