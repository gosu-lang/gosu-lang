/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.EvaluationContextImpl;
import com.intellij.debugger.engine.events.DebuggerContextCommandImpl;
import com.intellij.debugger.engine.managerThread.DebuggerCommand;
import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.jdi.ThreadReferenceProxyImpl;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.Chunk;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.ClassType;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import gw.compiler.ij.api.TypeFingerprint;
import gw.compiler.ij.processors.DependencySink;
import gw.config.CommonServices;
import gw.fs.IFile;
import gw.lang.GosuShop;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.compiler.parser.CompilerParser;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.TypeUtil;
import gw.util.fingerprint.FP64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class GosuCompiler implements TranslatingCompiler {
  private static final Logger LOG = Logger.getInstance(GosuCompiler.class);
  private static final String PROP_EXT = "properties";
  private static final String PCF_EXT = "pcf";
  private static Set<String> DISALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("java", "gs", "gsx", "gsp"));
  private IExternalCompiler externalCompiler;

  @NotNull
  @Override
  public String getDescription() {
    return "Gosu Compiler";
  }

  @Override
  public boolean validateConfiguration(CompileScope scope) {
    return true; // TODO:
  }

  @Override
  public boolean isCompilableFile(@NotNull VirtualFile file, CompileContext context) {
    final String path = file.getPath();
    final String name = file.getName();
    final String extension = file.getExtension();

    if (CommonServices.getPlatformHelper().isPathIgnored(path)) {
      return false;
    }

    //## todo: we chould instead check for @DoNotVerifyResource annotation and just don't report errors for those
    if ((name.startsWith("Errant_") && "pcf".equals(extension)) ||
            (name.contains("Errant") && "gs".equals(extension))) {
      return false;
    }

//    if ("pcf".equals(extension)
//        || "en".equals(extension)
//        || "gx".equals(extension)
//        || "gr".equals(extension)
//        || "xml".equals(extension)
//        ) {
//      return true;
//    } else {
//      return false;
//    }

    return CompilerParser.accepts(file);
  }

  @Override
  public void compile(@NotNull final CompileContext context, @NotNull Chunk<Module> moduleChunk,
                      @NotNull final VirtualFile[] files, @NotNull final OutputSink sink) {
    final Set<Module> nodes = moduleChunk.getNodes();
    if (nodes.size() > 1) {
      LOG.warn("Cyclic dependency during compilation: " + Joiner.on(',').join(nodes));
      return;
    }

    final boolean useExternal = files.length > CompilerSettings.getInstance().getExternalToIncrementalCompilerLimit();
    externalCompiler = useExternal ? createOrGetExternalCompiler(context.getProject()) : null;

    final GosuCompilerMonitor monitor = GosuCompilerMonitor.getInstance(context.getProject());
    final FileDependencyCache cache = monitor.getDependencyCache();
    final Module ijModule = nodes.iterator().next();
    final IModule gsModule = GosuModuleUtil.getModule(ijModule);
    TypeSystem.pushModule(gsModule);
    try {
      final List<VirtualFile> filesToCompile = Arrays.asList(files);
      if (context.isRebuild()) {
        fullCompile(context, cache, ijModule, filesToCompile, sink);
        monitor.setCacheInSync(true);
      } else {
        if (!monitor.isCacheInSync()) {
          context.requestRebuildNextTime("Dependency cache is not in sync. Rebuild is required.");
          return;
        }
        incrementalCompile(context, cache, ijModule, Lists.<VirtualFile>newArrayList(), filesToCompile, sink, true);
      }
      notifyTargetProcessOfChanges(context, files);
    } finally {
      TypeSystem.popModule(gsModule);
    }
  }

  private static long getFingerprint(final VirtualFile file, final IModule gosuModule) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Long>() {
      public Long compute() {
        final FP64 fp = new FP64();
        final String extension = file.getExtension();
        if (PROP_EXT.equals(extension)) {
          handlePropertiesFileFingerprint(file, fp);
        } else if (PCF_EXT.equals(extension)) {
          handleFileText(file, fp);
        } else {
          List<String> types = TypeUtil.getTypesForFile(gosuModule, file);
          for (String qualifiedName : Ordering.natural().sortedCopy(types)) {
            if ("java".equals(extension)) {
              final IJavaClassInfo type = TypeSystem.getJavaClassInfo(qualifiedName, gosuModule);
              if (type != null) {
                TypeFingerprint.extend(fp, type);
              } else {
                LOG.warn("Could not resolve Java type " + qualifiedName + " during taking fingerprint");
              }
            } else {
              final IType type = TypeSystem.getByFullNameIfValid(qualifiedName, gosuModule);
              if (type != null) {
                TypeFingerprint.extend(fp, type);
              } else {
                handleFileText(file, fp);
//                LOG.warn("Could not resolve type " + qualifiedName + " during taking fingerprint");
              }
            }
          }
        }
        return fp.getRawFingerprint();
      }
    });
  }

  public static VirtualFile getOutputDirectory(@NotNull CompileContext context, Module module, boolean tests) {
    return tests ? context.getModuleOutputDirectoryForTests(module) : context.getModuleOutputDirectory(module);
  }

  public static void setProgressText(CompileContext context, VirtualFile sourceFile) {
    context.getProgressIndicator().setText(String.format("Compiling '%s' [%s]", sourceFile.getName(), context.getModuleByFile(sourceFile).getName()));
  }

  public static void notifyTargetProcessOfChanges(@NotNull CompileContext context, @NotNull final VirtualFile... files) {
    final Project project = context.getProject();
    final DebuggerSession session = DebuggerManagerEx.getInstanceEx(project).getContext().getDebuggerSession();
    if (session != null) {
      if (session.isPaused()) {
        invokeDirectly(session, files);
      } else {
        final DebugProcessImpl process = session.getProcess();
        process.getManagerThread().invokeCommand(new DebuggerCommand() {
          public void action() {
            final VirtualMachineProxyImpl vm = process.getVirtualMachineProxy();
            final List<ReferenceType> types = vm.classesByName("gw.internal.gosu.parser.ReloadClassesIndicator");
            final List<String> changedTypes = TypeUtil.getTypesForFiles(TypeSystem.getGlobalModule(), Arrays.asList(files));
            vm.redefineClasses(ImmutableMap.of(types.get(0), GosuShop.updateReloadClassesIndicator(changedTypes, "")));
          }

          public void commandCancelled() {
            // Nothing to do
          }
        });
      }
    }
  }

  private static void invokeDirectly(final DebuggerSession session, final VirtualFile[] files) {
    final DebuggerContextImpl debuggerContext = DebuggerManagerEx.getInstanceEx(session.getProject()).getContext();
    final DebugProcessImpl process = session.getProcess();
    process.getManagerThread().schedule(new DebuggerContextCommandImpl(debuggerContext) {
      public Priority getPriority() {
        return Priority.HIGH;
      }

      public void threadAction() {
        final EvaluationContextImpl evaluationContext = debuggerContext.createEvaluationContext();

        try {
          final VirtualMachineProxyImpl vm = process.getVirtualMachineProxy();
          List<ReferenceType> types = vm.classesByName(TypeSystem.class.getName());
          ClassType classType = (ClassType) types.get(0);
          vm.getDebugProcess().invokeMethod(evaluationContext, classType, classType.methodsByName("refreshedFiles").get(0),
                  Arrays.asList(getFilesAsValue(evaluationContext, files)));
        } catch (Throwable e) {
          if (e instanceof EvaluateException) {
            throw new RuntimeException(e);
          }
        }
      }

      private Value getFilesAsValue(EvaluationContextImpl evaluationContext, VirtualFile[] files) throws Exception {
        List<Value> values = new ArrayList<>();
        VirtualMachineProxyImpl machineProxy = process.getVirtualMachineProxy();
        for (VirtualFile file : files) {
          values.add(machineProxy.mirrorOf(file.getPath()));
        }

        ArrayType objectArrayClass =
                (ArrayType) process.findClass(evaluationContext, "java.lang.String[]", evaluationContext.getClassLoader());
        if (objectArrayClass == null) {
          throw new IllegalStateException();
        }
        ArrayReference argArray = process.newInstance(objectArrayClass, files.length);
        evaluationContext.getSuspendContext().keep(argArray); // to avoid ObjectCollectedException
        argArray.setValues(values);

        return argArray;
      }
    });
  }

  @NotNull
  private ThreadReferenceProxyImpl findDebugThread(@NotNull VirtualMachineProxyImpl vm) {
    for (ThreadReferenceProxyImpl thread : vm.allThreads()) {
      if (thread.name().equals("Gosu class redefiner")) {
        return thread;
      }
    }
    throw new IllegalStateException("Could not find thread: " + "Gosu class redefiner");
  }

  private void refreshFiles(CompileContext context, List<VirtualFile> files) {
    sortEtiBeforeEtx(files);

    if (externalCompiler != null) {
      return; //we do not need to refresh anything in external compiler because all resources are fresh.
    }

    for (final VirtualFile file : files) {
      context.getProgressIndicator().checkCanceled();

      // The refresh needs to be run in a read action because to insure lock safe ordering.
      // First the PSI lock, then the TS lock
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        public void run() {
          TypeSystem.refreshed(FileUtil.toIResource(file));
        }
      });
    }
  }

  private void refreshModule(CompileContext context, final IModule module) {
    if (externalCompiler != null) {
      return; //we do not need to refresh anything in external compiler because all resources are fresh.
    }
    context.getProgressIndicator().checkCanceled();

    // The refresh needs to be run in a read action because to insure lock safe ordering.
    // First the PSI lock, then the TS lock
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      public void run() {
        TypeSystem.refresh(module);
      }
    });
  }

  private void sortEtiBeforeEtx(List<VirtualFile> files) {
    Collections.sort(files,
            new Comparator<VirtualFile>() {
              public int compare(VirtualFile o1, VirtualFile o2) {
                int iRes = o1.getParent().getPath().compareToIgnoreCase(o2.getParent().getPath());
                if (iRes == 0) {
                  // .eti comes before .etx
                  return o1.getName().compareToIgnoreCase(o2.getName());
                }
                // Ensure ../metadata/ comes before ../extensions/
                return -iRes;
              }
            });
  }

  private FileDependencyInfo internalCompileFile(@NotNull CompileContext context, @NotNull Module ijModule, @NotNull VirtualFile file, List<OutputItem> outputItems) {
    final DependencySink sink = new DependencySink();

    final boolean successfully;
    final long start = System.currentTimeMillis();

    final IModule module = GosuModuleUtil.getModule(ijModule);
    TypeSystem.pushModule(module);
    try {
      successfully = CompilerParser.parse(context, file, outputItems, sink);
    } catch (ProcessCanceledException e) {
      throw e;
    } catch (Throwable e) {
      final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
      addInternalCompilerError(context, e, url);
      return null;
    } finally {
      TypeSystem.popModule(module);
    }

    if (successfully) {
      try {
        final long fingerprint = getFingerprint(file, module);
        final int duration = (int) (System.currentTimeMillis() - start);
        return new FileDependencyInfo(file, getFiles(sink), sink.getDisplayKeys(), fingerprint, duration);
      } catch (Throwable e) {
        final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
        addInternalCompilerError(context, e, url);
        return null;
      }
    } else {
      return null;
    }
  }

  private void addInternalCompilerError(CompileContext context, Throwable e, String url) {
    context.addMessage(CompilerMessageCategory.ERROR, "Internal compiler error\n" + e, url, 0, 0);
    LOG.error("Internal compiler error", e);
  }

  private Set<VirtualFile> getFiles(DependencySink sink) {
    Set<VirtualFile> result = new HashSet<>();
    for (IFile file : sink.getFiles()) {
      result.add(((IDEAFile) file).getVirtualFile());
    }
    return result;
  }

  @Nullable
  private FileDependencyInfo compileFile(@NotNull CompileContext context, @NotNull Module ijModule, @NotNull VirtualFile file, List<OutputItem> outputItems) {
    CommonServices.getMemoryMonitor().reclaimMemory(null);
    setProgressText(context, file);

    String extension = file.getExtension();
    if (isInConfigFolder(file, context.getProject()) && extension != null && DISALLOWED_EXTENSIONS.contains(extension)) {
      final OpenFileDescriptor descriptor = new OpenFileDescriptor(context.getProject(), file, 0);
      final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
      context.addMessage(CompilerMessageCategory.ERROR, "Only configuration files are allowed in the config folder.", url, 0, 0, descriptor);
      return null;
    } else if (externalCompiler != null && canUseExternalCompiler(file)) {
      return externalCompiler.compileFile(context, ijModule, file, outputItems);
    } else {
      return internalCompileFile(context, ijModule, file, outputItems);
    }
  }

  private boolean canUseExternalCompiler(VirtualFile file) {
    final String extension = file.getExtension();
    if ("gx".equals(extension) || ("xml".equals(extension) && file.getPath().contains("/config/resources/productmodel/"))) {
      return false;
    }
    return true;
  }

  private boolean isInConfigFolder(VirtualFile file, Project project) {
    ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
    return "config".equals(projectFileIndex.getSourceRootForFile(file).getName());
  }

  private void fullCompile(@NotNull final CompileContext context, FileDependencyCache cache, @NotNull Module ijModule, @NotNull List<VirtualFile> filesToCompile, @NotNull final OutputSink sink) {
    // Refresh
    sortEtiBeforeEtx(filesToCompile);
    refreshModule(context, GosuModuleUtil.getModule(ijModule));

    final List<OutputItem> outputItems = Lists.newArrayList();
    for (VirtualFile file : filesToCompile) {
      context.getProgressIndicator().checkCanceled();

      final FileDependencyInfo info = compileFile(context, ijModule, file, outputItems);
      if (info != null) {
        cache.put(info);
      }
    }

    // Result
    sink.add(getOutputDirectory(context, ijModule, false).getPath(), outputItems, VirtualFile.EMPTY_ARRAY);
  }

  private void incrementalCompile(
          @NotNull final CompileContext context, FileDependencyCache cache, @NotNull Module ijModule,
          @NotNull List<VirtualFile> processedFiles, @NotNull List<VirtualFile> filesToCompile,
          @NotNull final OutputSink sink, boolean considerDependents) {

    if (filesToCompile.size() > CompilerSettings.getInstance().getExternalToIncrementalCompilerLimit()) {
      externalCompiler = createOrGetExternalCompiler(context.getProject());
    }

    // Cache read
    final Map<VirtualFile, FileDependencyInfo> fileToFingerprint = Maps.newHashMap();
    final Multimap<VirtualFile, VirtualFile> fileToDependents = HashMultimap.create();
    for (VirtualFile file : filesToCompile) {
      // Fingerprint
      final FileDependencyInfo fileDependency = cache.get(file);
      if (fileDependency != null) {
        fileToFingerprint.put(file, fileDependency);
      }
      cache.remove(file);

      // Dependencies
      if (considerDependents) {
        Set<VirtualFile> dependents;
        if ("display.properties".equals(file.getName())) {
          dependents = getDisplayKeysDependents(file, cache);
        } else {
          dependents = cache.getDependentsOn(file);
        }
        if (!dependents.isEmpty()) {
          fileToDependents.putAll(file, dependents);
        }
      }
    }

    // Preparing for compilation
    refreshFiles(context, filesToCompile);

    // Compile
    final List<OutputItem> outputItems = Lists.newArrayList();
    for (VirtualFile file : filesToCompile) {
      context.getProgressIndicator().checkCanceled();
      if (!file.exists()) {
        continue;
      }
      Module fileModule = context.getModuleByFile(file);
      if (fileModule == null) {
        //this will happen when some deleted files are still in dependency cache
        //but parent directory for such files were deleted
        //and index does not have module for this folder/package any more.
        continue;
      }
      final FileDependencyInfo info = compileFile(context, fileModule, file, outputItems);
      final FileDependencyInfo oldFileDependencyInfo = fileToFingerprint.get(file);
      if (info != null) {
        cache.put(info);
        if (oldFileDependencyInfo != null && oldFileDependencyInfo.getFingerprint() == info.getFingerprint()) {
          fileToDependents.removeAll(file);
        }
      } else {
        if (oldFileDependencyInfo != null) {
          //put old info back to avoid second layer of dependencies.
          cache.put(oldFileDependencyInfo);

          //do not recompile dependents until dependecy is compiled
          fileToDependents.removeAll(file);
        }
      }

      processedFiles.add(file);
    }

    // Dependencies
    final Set<VirtualFile> dependents = Sets.newLinkedHashSet(fileToDependents.values());
    dependents.removeAll(processedFiles);

    // Result with external dependencies
//    sink.add(getOutputDirectory(context, ijModule, false).getPath(), outputItems, new VirtualFile[0]);

    // Do it again
    if (!dependents.isEmpty()) {
      incrementalCompile(context, cache, ijModule, processedFiles, Lists.newArrayList(dependents), sink, false);
    }
  }

  private IExternalCompiler createOrGetExternalCompiler(Project project) {
    if (externalCompiler == null) {
      externalCompiler = project.getComponent(IExternalCompiler.class);
    }
    return externalCompiler;
  }

  private Set<VirtualFile> getDisplayKeysDependents(VirtualFile propFile, FileDependencyCache cache) {
    Set<VirtualFile> dependents = Sets.newHashSet();

    final Set<String> displayKeysNew = loadPropertyKeys(propFile);
    final Set<String> displayKeysOld = cache.getDisplayKeys(propFile);

    final Set<String> added = Sets.newHashSet(displayKeysNew);
    added.removeAll(displayKeysOld);

    final Set<String> deleted = Sets.newHashSet(displayKeysOld);
    deleted.removeAll(displayKeysNew);

    for (String key : added) {
      dependents.addAll(cache.getDependentsOnByDisplayKey(key));
    }

    for (String key : deleted) {
      dependents.addAll(cache.getDependentsOnByDisplayKey(key));
    }

    return dependents;
  }

  private static void handlePropertiesFileFingerprint(VirtualFile file, FP64 fp) {

    OrderedPropertyKeys keys = new OrderedPropertyKeys();
    StringReader reader = null;
    try {
      reader = new StringReader(FileDocumentManager.getInstance().getDocument(file).getText());
      keys.load(reader);
      for (String key : keys.getKeys()) {
        fp.extend(key);
      }
    } catch (IOException e) {
      LOG.warn("Could not load *.properties during taking fingerprint");
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  private static void handleFileText(VirtualFile file, FP64 fp) {
    final String text = FileDocumentManager.getInstance().getDocument(file).getText();
    fp.extend(text);
  }

  private static class OrderedPropertyKeys extends Properties {

    private ArrayList<String> orderedKeys = new ArrayList<>(128);

    @Override
    public synchronized Object put(Object key, Object value) {
      orderedKeys.add((String) key);
      return null;
    }

    private ArrayList<String> getKeys() {
      return orderedKeys;
    }
  }

  private static Set<String> loadPropertyKeys(VirtualFile file) {
    PropertyKeysSet keys = new PropertyKeysSet();
    StringReader reader = null;
    try {
      reader = new StringReader(FileDocumentManager.getInstance().getDocument(file).getText());
      keys.load(reader);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return keys.getKeys();
  }

  private static class PropertyKeysSet extends Properties {

    private Set<String> keys = Sets.newHashSet();

    @Override
    public synchronized Object put(Object key, Object value) {
      keys.add((String) key);
      return null;
    }

    private Set<String> getKeys() {
      return keys;
    }
  }
}
