/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import editor.util.transform.java.visitor.GosuVisitor;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class GosuVisitorTestUtil {

  public String transform(String jsrc) throws IOException
  {
    String src = readFile(jsrc);
    if (src == null) {
      return null;
    }

    JavaCompiler compiler = JavacTool.create();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
    ArrayList<JavaStringObject> javaStringObjects = new ArrayList<>();
    javaStringObjects.add(new JavaStringObject(src));
    Iterable<? extends JavaFileObject> compilationUnits = javaStringObjects;
    StringWriter errors = new StringWriter();
    JavaCompiler.CompilationTask task = compiler.getTask(errors, fileManager, null, null, null, compilationUnits);
    JavacTaskImpl javacTask = (JavacTaskImpl) task;
    String output = "";
    Iterable<? extends CompilationUnitTree> trees = javacTask.parse();
    if (errors.toString().isEmpty()) {
      GosuVisitor visitor = new GosuVisitor(4);
      for (CompilationUnitTree tree : trees) {
        tree.accept(visitor, null);
      }
      fileManager.close();
      output = visitor.getOutput().toString();
    }
    return output;
  }

  public String readFile(String path) throws IOException
  {
    StringBuilder src = new StringBuilder();
    URL url = null;
    try
    {
      url = Thread.currentThread().getContextClassLoader().getResource( path ).toURI().toURL();
    }
    catch( URISyntaxException e )
    {
      throw new RuntimeException( e );
    }
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        src.append(line).append("\n");
      }
    }
    return src.toString();
  }
}
