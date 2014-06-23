/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.module.IModule;
import gw.compiler.ij.processors.DependencyCollector;
import gw.compiler.ij.processors.DependencySink;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierExpressionImpl;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.SafeRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.StringTokenizer;

import static gw.plugin.ij.util.ExecutionUtil.*;

public class GosuClassCompilerParser implements ICompilerParser {
  protected boolean generateByteCode(VirtualFile file, CompileContext context, IGosuClass gsClass) {
    return true;
  }

  @Override
  public boolean accepts(@NotNull VirtualFile file) {
    final String name = file.getName();
    return
        name.endsWith(GosuClassTypeLoader.GOSU_CLASS_FILE_EXT) ||
            name.endsWith(GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT) ||
            name.endsWith(GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT) ||
            name.endsWith(GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT);
  }

  // ICompilerParser
  public boolean parse(@NotNull CompileContext context, VirtualFile file, @NotNull List<TranslatingCompiler.OutputItem> outputItems, DependencySink sink) {
    final Module ijModule = context.getModuleByFile(file);
    final String qualifiedName = gw.plugin.ij.util.FileUtil.getSourceQualifiedName(file, GosuModuleUtil.getModule(ijModule));

    // Parse
    ParseResult parseResult = parseImpl(context, ijModule, file, qualifiedName, outputItems);
    if (parseResult != null) {
      final ParseResultsException parseException = parseResult.parseException;
      if (parseException != null && CompilerIssues.reportIssues(context, file, parseException)) {
        return false;
      }

      // Compile to bytecode (.class files)
      try {
        VirtualFile bytecodeOutputFile = makeClassFileForOut(context, ijModule, (IGosuClass) parseResult.type, file);
        if (bytecodeOutputFile != null) {
          outputItems.add(new OutputItemImpl(FileUtil.toSystemIndependentName(bytecodeOutputFile.getPath()), file));
        }
      } catch (Exception e) {
        final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        context.addMessage(CompilerMessageCategory.ERROR, "Failed to generate bytecode\n" + e + "\n" + sw.toString(), url, 0, 0);
      }

      // Copy Source
      final File outputFile = CompilerUtils.getOutputFile(context, file);
      CompilerUtils.copySourceToOut(file, outputFile);

      // Result
      DependencyCollector.collect(parseResult.parsedElement.getLocation(), sink);
      outputItems.add(new OutputItemImpl(FileUtil.toSystemIndependentName(outputFile.getPath()), file));

      return true;
    } else {
      //final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
      //context.addMessage(CompilerMessageCategory.ERROR, "Could not resolve type for file " + file.getPresentableUrl(), url, 0, 0);
      return false;
    }
  }

  private VirtualFile makeClassFileForOut(@NotNull final CompileContext context, final Module ijModule, @NotNull final IGosuClass gsClass, final VirtualFile file) {
    final VirtualFile[] classFile = new VirtualFile[1];
    final Throwable[] throwable = new Throwable[1];
    ExecutionUtil.execute(WRITE | DISPATCH | BLOCKING, new SafeRunnable(GosuModuleUtil.getModule(ijModule)) {
      public void execute() {
        VirtualFile moduleOutputDirectory = context.getModuleOutputDirectory(ijModule);
        if (moduleOutputDirectory == null) {
          return;
        }
        try {
          final String outRelativePath = gsClass.getName().replace('.', File.separatorChar) + ".class";
          VirtualFile child = moduleOutputDirectory;
          child = createFile(outRelativePath, child);
          if (createClassFile(child, gsClass, file, context)) {
            classFile[0] = child;
          } else {
            child.delete(null);
          }
        } catch (Exception e) {
          throwable[0] = e;
        }
      }

      private VirtualFile createFile(String outRelativePath, VirtualFile child) throws IOException {
        for (StringTokenizer tokenizer = new StringTokenizer(outRelativePath, File.separator + "/"); tokenizer.hasMoreTokens(); ) {
          String token = tokenizer.nextToken();
          VirtualFile existingChild = child.findChild(token);
          if (existingChild == null) {
            if (token.endsWith(".class")) {
              child = child.createChildData(this, token);
            } else {
              child = child.createChildDirectory(this, token);
            }
          } else {
            child = existingChild;
          }
        }
        return child;
      }
    });

    if (throwable[0] != null) {
      throw new RuntimeException(throwable[0]);
    } else {
      return classFile[0];
    }
  }

  private boolean createClassFile(@NotNull final VirtualFile outputFile, @NotNull final IGosuClass gosuClass, VirtualFile file, CompileContext context) throws IOException {
    if (hasDoNotVerifyAnnotation(gosuClass)) {
      return false;
    }

    if (generateByteCode(file, context, gosuClass)) {
      final byte[] bytes = TypeSystem.getGosuClassLoader().getBytes(gosuClass);
      OutputStream out = outputFile.getOutputStream(this);
      try {
        out.write(bytes);
      } finally {
        out.close();
      }

      for (IGosuClass innerClass : gosuClass.getInnerClasses()) {
        final String innerClassName = String.format("%s$%s.class", outputFile.getNameWithoutExtension(), innerClass.getRelativeName());
        VirtualFile innerClassFile = outputFile.getParent().findChild(innerClassName);
        if (innerClassFile == null) {
          innerClassFile = outputFile.getParent().createChildData(this, innerClassName);
        }
        createClassFile(innerClassFile, innerClass, file, context);
      }

      return true;
    }
    return false;
  }

  private boolean hasDoNotVerifyAnnotation(IGosuClass gsClass) {
    for (IAnnotationInfo ai : gsClass.getTypeInfo().getAnnotations()) {
      if (ai.getType().getRelativeName().equals("DoNotVerifyResource")) {
        return true;
      }
    }
    return false;
  }

  protected IType getType(Module ijModule, String qualifiedName) {
    final IModule gsModule = GosuModuleUtil.getModule(ijModule);
    TypeSystem.pushModule(gsModule);
    try {
      final IType type = TypeSystem.getByFullNameIfValid(qualifiedName);
      return GosuIdentifierExpressionImpl.maybeUnwrapProxy(type);
    } finally {
      TypeSystem.popModule(gsModule);
    }
  }

  // Parsing
  protected static class ParseResult {
    public final IType type;
    public final ParseResultsException parseException;
    public final IParsedElement parsedElement;

    public ParseResult(IType type, IParsedElement parsedElement, ParseResultsException parseException) {
      this.type = type;
      this.parseException = parseException;
      this.parsedElement = parsedElement;
    }
  }

  @Nullable
  protected ParseResult parseImpl(CompileContext context, Module ijModule, VirtualFile file, String qualifiedName, List<TranslatingCompiler.OutputItem> result) {
    TypeSystem.lock();
    IGosuClass klass = null;
    try {
      klass = (IGosuClass) getType(ijModule, qualifiedName);
      if (klass != null) {
        klass.setCreateEditorParser(false);
        klass.isValid(); // force compilation
      }
    } finally {
      TypeSystem.unlock();
    }

    if (klass != null) {
      final boolean report = reportParseIssues(klass);
      return new ParseResult(klass, klass.getClassStatement().getClassFileStatement(), report ? klass.getParseResultsException() : null);
    }
    return null;
  }

  protected boolean reportParseIssues(@NotNull IGosuClass klass) {
    final IType dnvr = TypeSystem.getByFullNameIfValid("gw.testharness.DoNotVerifyResource");

    final IGosuClassTypeInfo typeInfo = klass.getTypeInfo();
    return dnvr == null || !typeInfo.hasAnnotation(dnvr);
  }

}
