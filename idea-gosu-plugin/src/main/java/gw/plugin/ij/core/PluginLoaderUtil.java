/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.google.common.collect.ImmutableSet;
import com.intellij.ProjectTopics;
import com.intellij.codeInsight.daemon.impl.EditorTracker;
import com.intellij.compiler.CompilerWorkspaceConfiguration;
import com.intellij.compiler.impl.CompileDriver;
import com.intellij.ide.startup.impl.StartupManagerImpl;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.DumbServiceImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.DefaultProject;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.impl.PsiDocumentTransactionListener;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.UIUtil;
import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.IProject;
import gw.plugin.ij.actions.java.CreateClassAction;
import gw.plugin.ij.compiler.DependencyCacheCleaner;
import gw.plugin.ij.compiler.GosuCompiler;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

public class PluginLoaderUtil {
  private static final Map<Project, PluginLoaderUtil> INSTANCES = new WeakHashMap<>();
  public static final Key<Disposable> TEST_ROOT_DISPOSABLE = Key.create("testRootDisposable");
  public static final Key<IProject> PROJECT_KEY = Key.create("projectKey");

  private final Project _project;
  @Nullable
  private GosuCompiler _gosuCompiler;
  @Nullable
  private FileModificationManager _fileModificationManager;
  private boolean _startupOk = true;
  @Nullable
  private Throwable _startupError = null;
  @Nullable
  private MessageBusConnection _projectConnection;
  private MessageBusConnection _permanentProjectConnection;
  @Nullable
  private MessageBusConnection _applicationConnection;
  private EditorTracker _editorTracker;


  private static final NotNullLazyValue<ITypeSystemStartupContributor[]> PLUGIN_LISTENERS = new NotNullLazyValue<ITypeSystemStartupContributor[]>() {
    @NotNull
    @Override
    protected ITypeSystemStartupContributor[] compute() {
      final TypeSystemStartupContributorExtensionBean[] extensions = Extensions.getExtensions(TypeSystemStartupContributorExtensionBean.EP_NAME);
      final ITypeSystemStartupContributor[] results = new ITypeSystemStartupContributor[extensions.length];
      for (int i = 0; i < extensions.length; i++) {
        results[i] = extensions[i].getHandler();
      }
      return results;
    }
  };


  private PluginFailureReason _failureReason = PluginFailureReason.NONE;
  private final ModuleClasspathListener _moduleClasspathListener = new ModuleClasspathListener();
  private boolean _guidewireApp;


  public static PluginLoaderUtil instance(Project project) {
    PluginLoaderUtil util = INSTANCES.get(project);
    if (util == null) {
      INSTANCES.put(project, util = new PluginLoaderUtil(project));
    }
    return util;
  }

  private PluginLoaderUtil(Project project) {
    _project = project;
  }

  @NotNull
  private ITypeSystemStartupContributor[] getStartupContributors() {
    return PLUGIN_LISTENERS.getValue();
  }

  @NotNull
  public ModuleClasspathListener getModuleClasspathListener() {
    return _moduleClasspathListener;
  }

  public void addCompiler() {
    _gosuCompiler = new GosuCompiler();

    final CompilerManager manager = CompilerManager.getInstance(_project);
    manager.addCompilableFileType(GosuCodeFileType.INSTANCE);
    manager.addTranslatingCompiler(_gosuCompiler,
            ImmutableSet.<FileType>of(StdFileTypes.JAVA, GosuCodeFileType.INSTANCE), // input
            ImmutableSet.<FileType>of(StdFileTypes.JAVA, GosuCodeFileType.INSTANCE)); // output

//    new CompilerLogger(_project).start();

    manager.addBeforeTask(new DependencyCacheCleaner());
  }

  public void removeCompiler() {
    final CompilerManager manager = CompilerManager.getInstance(_project);
    manager.removeCompilableFileType(GosuCodeFileType.INSTANCE);
    manager.removeCompiler(_gosuCompiler);

    _gosuCompiler = null;
  }

  private void initGosuPlugin() {
    final IModule rootModule = GosuModuleUtil.getGlobalModule(_project);
    TypeSystem.pushModule(rootModule);
    try {
      _projectConnection = _project.getMessageBus().connect();
      addCompiler();
      addTypeRefreshListener();
      addModuleRefreshListener();
      addEditorSourceProvider();

      ActionManagerImpl actionManager = (ActionManagerImpl) ActionManagerImpl.getInstance();
      AnAction oldNewClassAction = actionManager.getAction("NewClass");
      actionManager.unregisterAction("NewClass");
      CreateClassAction action = new CreateClassAction();
      actionManager.registerAction("NewClass", action);
    } finally {
      TypeSystem.popModule(rootModule);
    }
  }

  private void addEditorSourceProvider() {
    IDEAPlatformHelper platformHelper = (IDEAPlatformHelper) CommonServices.getPlatformHelper();
    _projectConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, platformHelper);
  }

  private void addTypeRefreshListener() {
    _fileModificationManager = new FileModificationManager(_project);
    _projectConnection.subscribe(PsiDocumentTransactionListener.TOPIC, _fileModificationManager);
    _applicationConnection.subscribe(VirtualFileManager.VFS_CHANGES, _fileModificationManager);
  }

  private void addModuleRefreshListener() {
    ModuleRefreshListener moduleRefreshListener = new ModuleRefreshListener();
    _projectConnection.subscribe(ProjectTopics.MODULES, moduleRefreshListener);
  }

  public void uninitGosuPlugin() {
    gw.plugin.ij.util.UIUtil.closeAllGosuEditors(_project, null);

    //DebuggerManager.getInstance( _project ).unregisterPositionManagerFactory( _positionManager );
    removeCompiler();

    _projectConnection.disconnect();
    _projectConnection = null;
    _applicationConnection.disconnect();
    _applicationConnection = null;
    _gosuCompiler = null;
    _fileModificationManager = null;
    _startupOk = true;
    _startupError = null;
  }

  private void setDumbMode(final boolean dumb) {
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      public void run() {
        DumbServiceImpl.getInstance(_project).setDumb(dumb);
      }
    });
  }

  public void reportStartupError(@NotNull Throwable e) {
    disablePlugin();
    _startupError = e;
    if (e instanceof GosuPluginException) {
      _failureReason = ((GosuPluginException) e).getReason();
    } else {
      _failureReason = PluginFailureReason.EXCEPTION;
    }
    ExceptionUtil.showError(GosuBundle.message("error.plugin_could_not_start"), e);
  }

  private void disablePlugin() {
    _startupOk = false;
  }

  public boolean isStartupOk() {
    return _startupOk;
  }

  public void setProject() {
    if (!(_project instanceof DefaultProject)) {
      _failureReason = PluginFailureReason.NONE;
      _permanentProjectConnection = _project.getMessageBus().connect();
      _permanentProjectConnection.subscribe(ProjectTopics.PROJECT_ROOTS, _moduleClasspathListener);
    }
  }

  @Nullable
  public Throwable getStartupError() {
    return _startupError;
  }

  public void setEditorTracker(EditorTracker editorTracker) {
    _editorTracker = editorTracker;
  }

  public boolean isStarted() {
    return TypeSystemStarter.instance(_project).isStarted();
  }

  public void startPLugin() {
    if (hasRanPreviously() && isGuidewireApp()) {
      ExceptionUtil.showError(GosuBundle.message("error.plugin_disabled"),
          GosuBundle.message("error.plugin_disabled.description"));
      return;
    }

    for (ITypeSystemStartupContributor contributor : getStartupContributors()) {
      String message = contributor.accepts(_project);
      if (message != null) {
        ExceptionUtil.showInfo(GosuBundle.message("info.plugin.not.started"), message);
        return;
      }
    }

    ApplicationManager.getApplication().invokeAndWait(
      new Runnable() {
        public void run() {
          ApplicationManager.getApplication().runWriteAction(new Runnable() {
              public void run() {
                // pre -startup
                try {
                  for (ITypeSystemStartupContributor pluginListener : getStartupContributors()) {
                    pluginListener.beforeTypesystemStartup(_project);
                  }
                } catch (Exception e) {
                  reportStartupError(e);
                  return;
                }

                // !PW leaks reference to GosuLoader, if we ever want to support clean unloading of plugin
                _applicationConnection = ApplicationManager.getApplication().getMessageBus().connect();


                setDumbMode(true);
                try {
                  startTypeSystem();
                  initGosuPlugin();
                  for (ITypeSystemStartupContributor pluginListener : getStartupContributors()) {
                    pluginListener.afterTypesystemStartup(_project);
                  }
                  System.out.println( "Initialized Gosu with IJ Project: " + _project );
                } catch (Throwable e) {
                  reportStartupError(e);
                  System.out.println( "¿prolbem?" );
                } finally {
                  setDumbMode(false);
                }
              }
          });
        }
      }, ModalityState.defaultModalityState() );
  }

  private boolean hasRanPreviously() {
    return ExecutionMode.isIDE();
  }

  public void closeProject(PluginFailureReason failureReason) {
    boolean previousValue = ModuleClasspathListener.ENABLED;
    ModuleClasspathListener.ENABLED = false;
    try {
      if (TypeSystemStarter.instance(_project).isStarted()) {
        try {
          if (TypeSystem.getCurrentModule() != null) {
            System.out.println("Cleaning type system, but current top module is not null!");
          }
          System.out.println("Stopping Type System " + TypeSystem.getExecutionEnvironment() );
          stopTypeSystem();
        } catch (Throwable e) {
          reportStartupError(e);
        }
        try {
          uninitGosuPlugin();
        } catch (Throwable e) {
          reportStartupError(e);
        }

        try {
          for (ITypeSystemStartupContributor pluginListener : getStartupContributors()) {
            pluginListener.afterPluginShutdown(_project);
          }
        } catch (Exception e) {
          ExceptionUtil.showNonFatalError("Exception during Gosu plugin shutdown.", e);
        }
      }
    } finally {
      _failureReason = failureReason;
      ModuleClasspathListener.ENABLED = previousValue;
    }
  }

  private void startTypeSystem() {
    TypeSystemStarter.instance(_project).start(_project);
  }

  private void stopTypeSystem() {
    TypeSystemStarter.instance(_project).stop(_project);
  }

  public boolean projectOpened() {
//    if (ProjectManager.getInstance().getOpenProjects().length > 1) {
//      ExceptionAUtil.showWarning("Gosu support disabled.", "Gosu cannot support multiple simultaneously open projects.");
//      stopPLugin(_project, PluginFailureReason.MULTIPLE_PROJECTS_OPEN);
//      return false;
//    }
    final long[] t1 = new long[1];
    StartupManagerImpl.getInstance(_project).registerStartupActivity(new Runnable() {
      public void run() {
        try {
          startPLugin();
        } finally {
          t1[0] = System.nanoTime();
        }
      }
    });
    StartupManagerImpl.getInstance(_project).registerPostStartupActivity(new Runnable() {
      public void run() {
        System.out.printf("Indexing done in %.3fs.\n", (System.nanoTime() - t1[0]) * 1e-9);
      }
    });
    return true;
  }

  public void projectClosed() {
    closeProject(PluginFailureReason.NONE);
  }

  public PluginFailureReason getFailureReason() {
    return _failureReason;
  }

  @Nullable
  public static IProject getFrom(@NotNull Project project) {
    IProject gsProject = project.getUserData(PROJECT_KEY);
    if (gsProject == null) {
      project.putUserData(PROJECT_KEY, gsProject = new IjProject(project));
    }
    return gsProject;
  }

  public boolean isGuidewireApp() {
    return !CommonServices.getEntityAccess().getClass().getSimpleName().equals("DefaultEntityAccess");
  }

  public void disableIJExternalCompiler() {
    CompilerWorkspaceConfiguration workspaceConfiguration = CompilerWorkspaceConfiguration.getInstance(_project);
    workspaceConfiguration.USE_COMPILE_SERVER = false;
  }
}
