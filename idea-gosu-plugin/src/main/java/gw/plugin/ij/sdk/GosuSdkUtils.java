/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JavaSdkVersion;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.jar.JarFileSystemImpl;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.util.ui.UIUtil;
import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.Gosu;
import gw.lang.GosuVersion;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.core.GosuAppComponent;
import gw.plugin.ij.core.IDEAExtensionFolderLocator;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.util.StreamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GosuSdkUtils {

  private static final Logger LOG = Logger.getInstance(GosuSdkUtils.class);

  public static final Set<String> EXCLUDED_JARS = new ImmutableSet.Builder<String>()
          .add("jfxrt.jar") // jfxrt.jar is NOT in classpath of JDK 7 (u6, u7)
          .build();

  public static final Key<Boolean> NEW_GOSU_ACTIONS_AVAILABLE_KEY = Key.create("NEW_GOSU_ACTIONS_AVAILABLE");

  private static final String DEFAULT_GOSU_SDK_NAME = "Default Gosu SDK";
  public static final String JAR_EXTENSION = "jar";
  public static final String SDK_FOLDER = "sdk";
  public static final String LIB_FOLDER = "lib";

  private static final List<Pattern> IGNORED_JAR_PATTERNS = ImmutableList.of(
      Pattern.compile("alt-.*"),
      Pattern.compile("gw-ij-.*"),
      Pattern.compile("ij-.*"));

  public static boolean isIgnoredJar(String name) {
    for (Pattern pattern : IGNORED_JAR_PATTERNS) {
      if (pattern.matcher(name).matches()) {
        return true;
      }
    }
    return false;
  }

  public static Sdk initDefaultGosuSDK() {
    Sdk sdk = getDefaultGosuSdk();
    if (sdk != null) {
      SdkAdditionalData data = sdk.getSdkAdditionalData();
      if (data instanceof GosuSdkAdditionalData) {
        GosuSdkAdditionalData gData = (GosuSdkAdditionalData) data;
        GosuVersion version = gData.getGosuVersion();
        if (version != null) {
          GosuVersion embeddedInPluginVersion = Gosu.getVersion();
          if (embeddedInPluginVersion.compareTo(version) > 0 || gData.getJavaSdk() == null) {
            // The existing Sdk is not compatible with the plugin, delete it then create a shiny new Default Gosu Sdk
            deleteDefaultGosuSDK(sdk);
            // TODO - blc - show a warning that we're replacing the SDK
            sdk = null;
          }
        }
      }
    }
    return sdk != null ? sdk : createDefaultGosuSDK(null);
  }

  @Nullable
  public static Sdk createDefaultGosuSDK(@Nullable Sdk jdk) {
    final ProjectJdkTable table = ProjectJdkTable.getInstance();
    if (jdk == null) {
      jdk = findJavaSDK( null );
      if (jdk == null) {
        jdk = createJavaSdk();
      }
      if (jdk == null) {
        ExceptionUtil.showInfo(GosuBundle.message("gosu.initialization"),
            GosuBundle.message("error.cannot.create.sdk", DEFAULT_GOSU_SDK_NAME));
        return null;
      }
    }

    final Sdk gosuSdk = table.createSdk(DEFAULT_GOSU_SDK_NAME, GosuSdkType.getInstance());
    final String homePath = getPluginRootFolder();
    configure(gosuSdk, homePath, jdk);

    addSdk(gosuSdk);

    return gosuSdk;
  }

  public static void addSdk(final Sdk platformSDK) {
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      @Override
      public void run() {
        SdkConfigurationUtil.addSdk(platformSDK);
      }
    });

//    ExecutionUtil.runInDispatchThread(new Runnable() {
//      @Override
//      public void run() {
//        SdkConfigurationUtil.addSdk(gosuSdk);
//      }
//    }, true);
  }

  public static Sdk findJavaSDK(String name) {
    final ProjectJdkTable table = ProjectJdkTable.getInstance();
    final List<Sdk> jdks = table.getSdksOfType(JavaSdk.getInstance());

    if (name != null) {
      // First, try to find by name
      for (Sdk jdk : jdks) {
        if (jdk.getName().equals(name)) {
          return jdk;
        }
      }
    }

    // If not found, get first non-platform
    for (Sdk jdk : jdks) {
      if (!jdk.getName().contains("Platform")) {
        if (name != null) {
          LOG.warn("Cannot find Java SDK '" + name + "', selecting '" + jdk.getName() + "'");
        }
        if(GosuSdkUtils.isApplicableJdk(jdk)) {
          return jdk;
        }
      }
    }
    return null;
  }

  /**
   * Try to create Java SDK based on the JAVA_HOME environment variable or
   * java.home system property.
   */
  public static Sdk createJavaSdk() {
    Sdk sdk = null;
    String javaHome = System.getProperty("java.home");
    if (!Strings.isNullOrEmpty(javaHome) && !javaHome.toLowerCase().contains("intellij")) {
      if (javaHome.endsWith("jre")) {
        javaHome = javaHome.substring(0, javaHome.length() - 3);
      }
      sdk = createJavaSdk(javaHome);
    }

    if (sdk == null) {
      javaHome = System.getenv("JAVA_HOME");
      if (!Strings.isNullOrEmpty(javaHome)) {
        sdk = createJavaSdk(javaHome);
      }
    }
    return sdk;
  }

  public static Sdk createJavaSdk(final String javaHome) {
    final Sdk[] result = new Sdk[1];
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      public void run() {
        result[0] = SdkConfigurationUtil.createAndAddSDK(javaHome, JavaSdk.getInstance());
        final VirtualFile toolsFile = LocalFileSystem.getInstance().findFileByIoFile(new File(javaHome, "lib/tools.jar"));
        if (toolsFile != null) {
          final VirtualFile toolsfileJar = JarFileSystemImpl.getInstance().getJarRootForLocalFile(toolsFile);

          SdkModificator modificator = result[0].getSdkModificator();
          modificator.addRoot(toolsfileJar, OrderRootType.CLASSES);
          modificator.commitChanges();
        }
      }
    });

    if (result[0] != null) {
      refreshSdk(result[0]);
    }
    return result[0];
  }

  private static void refreshSdk(Sdk sdk) {
    final SdkModificator modificator = sdk.getSdkModificator();
    for (VirtualFile file : sdk.getRootProvider().getFiles(OrderRootType.CLASSES)) {
      if (EXCLUDED_JARS.contains(file.getName())) {
        modificator.removeRoot(file, OrderRootType.CLASSES);
      }
    }
    modificator.commitChanges();
  }

  private static void deleteDefaultGosuSDK(@NotNull final Sdk gosuSdk) {
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          @Override
          public void run() {
            ProjectJdkTable.getInstance().removeJdk(gosuSdk);
          }
        });
      }
    });
  }

  public static boolean isApplicableJdk(@NotNull Sdk sdk) {
    if (sdk.getSdkType() instanceof JavaSdk) {
      final JavaSdkVersion version = JavaSdk.getInstance().getVersion(sdk);
      if(version == null) {
        return false;
      }
      return version.getMaxLanguageLevel().isAtLeast(LanguageLevel.JDK_1_7);
    }
    return false;
  }

  public static void configure(@NotNull Sdk sdk, @NotNull String homePath, @NotNull Sdk jdk) {
    SdkModificator modificator = sdk.getSdkModificator();
    modificator.setHomePath(homePath);
    modificator.setVersionString(jdk.getVersionString());
    addJdkFiles(modificator, jdk);
    File pluginHome = new File(homePath);
    if (!isPluginRootAJar(pluginHome)) {
      File jarsDir = findJarsDir(pluginHome);
      if (jarsDir != null) {
        Iterable<File> jars = listFiles(jarsDir, withExtension(JAR_EXTENSION));
        addSdkElements(modificator, jars);
      }
    } // else pluginHome is a jar in the TH environment

    addExtlibFolderToClasspath(modificator);
    final GosuVersion gosuVersion = findGosuVersion(homePath);
    if(gosuVersion != null) {
      modificator.setSdkAdditionalData(new GosuSdkAdditionalData(jdk, gosuVersion));
    }
    modificator.commitChanges();
  }

  public static void addJdkFiles(@NotNull SdkModificator modificator, @NotNull Sdk jdk) {
    // Add Java jars to SDK
    VirtualFile[] jars = jdk.getRootProvider().getFiles(OrderRootType.CLASSES);
    addVirtualFiles(modificator, Arrays.asList(jars));

    // Add Java sources to SDK
    VirtualFile[] sources = jdk.getRootProvider().getFiles(OrderRootType.SOURCES);
    for (VirtualFile file : sources) {
      modificator.addRoot(file, OrderRootType.SOURCES);
    }
  }

  public static void addSdkElements(@NotNull SdkModificator modificator, Iterable<File> elements) {
    addVirtualFiles(modificator, Iterables.transform(elements, new Function<File, VirtualFile>() {
      public VirtualFile apply(@Nullable File file) {
        final VirtualFile vfile = LocalFileSystem.getInstance().findFileByIoFile(file);
        if (vfile == null ) {
          LOG.warn("SDK file not present: " + file.getAbsolutePath());
          return null;
        }
        return JAR_EXTENSION.equals(vfile.getExtension()) ?
                JarFileSystemImpl.getInstance().getJarRootForLocalFile(vfile) :
                vfile;
      }
    }));
  }

  public static void addVirtualFiles(@NotNull SdkModificator modificator, Iterable<VirtualFile> jars) {
    addVirtualFiles(modificator, jars,  OrderRootType.CLASSES);
  }

  public static void addVirtualFiles(@NotNull SdkModificator modificator, Iterable<VirtualFile> jars, OrderRootType type) {
    for (VirtualFile file : jars) {
      if (file != null && !isIgnoredJar(file.getName())) {
        modificator.addRoot(file, type);
      }
    }
  }

  private static void addExtlibFolderToClasspath(@NotNull SdkModificator modificator) {
    final File extensionFolder = new IDEAExtensionFolderLocator().getExtensionFolderPath();
    if (extensionFolder != null) {
      Iterable<File> extJars = listFiles(extensionFolder, withExtension(JAR_EXTENSION));
      addSdkElements(modificator, extJars);
    }
  }

  private static Iterable<File> getApprovedJars(File dir) {
    Iterable<File> files = listFiles(dir, withExtension(JAR_EXTENSION));
    return Iterables.filter(files, new Predicate<File>() {
      @Override
      public boolean apply(@Nullable File file) {
        return !isIgnoredJar(file.getName());
      }
    });
  }

  public static Iterable<File> listFiles(File dir, Predicate<File> predicate) {
    final File[] contents = dir.listFiles();
    return contents != null ?
            Iterables.filter(Arrays.asList(contents), predicate) :
            Collections.<File>emptyList();
  }

  public static Predicate<File> withExtension(final String extension) {
    return new Predicate<File>() {
      @Override
      public boolean apply(@Nullable File file) {
        return FileUtil.getExtension(file.getName()).equals(extension);
      }
    };
  }

  public static Sdk getDefaultGosuSdk() {
    return ProjectJdkTable.getInstance().findJdk(DEFAULT_GOSU_SDK_NAME);
  }

  public static String getPluginRootFolder() {
    return GosuAppComponent.getEditorPlugin().getPath().getAbsolutePath();
  }

  public static boolean isGosuSdkSet(Project project) {
    final Sdk sdk = ProjectRootManagerImpl.getInstance(project).getProjectSdk();
    return sdk != null && sdk.getSdkType() instanceof GosuSdkType;
  }

  public static boolean isGosuApiModuleInProject(Project project) {
    final IModule rootModule = GosuModuleUtil.getGlobalModule(project);
    if (rootModule == null) {
      return false;
    }

    TypeSystem.pushModule(rootModule);
    try {
      return TypeSystem.getByFullNameIfValid("gw.lang.reflect.IType", TypeSystem.getGlobalModule()) != null;
    } finally {
      TypeSystem.popModule(rootModule);
    }
  }

  private static File findJarsDir(File sdkHome) {
    for (String dirName : new String[] { SDK_FOLDER, LIB_FOLDER }) {
      File dir = new File(sdkHome, dirName);
      if (dir.exists()) {
        return dir;
      }
    }
    return null;
  }

  private static boolean isPluginRootAJar(File pluginRoot) {
    // this happens to return true in TH
    return (!pluginRoot.isDirectory()
            && pluginRoot.getName().endsWith(".jar"))
            ||
            (pluginRoot.isDirectory()
            && pluginRoot.getName().equals("classes"));
  }

  static File findGosuCoreApiJar(File sdkHome) {
    File jarsDir = findJarsDir(sdkHome);
    if (jarsDir == null) {
      return null;
    }

    Iterable<File> jars = getApprovedJars(jarsDir);
    File gosuCoreApiJar = Iterables.find(jars, new Predicate<File>() {
      @Override
      public boolean apply(@Nullable File file) {
        return file != null && file.getName().startsWith("gosu-core-api") && file.getName().endsWith(".jar");
      }
    }, null);

    return gosuCoreApiJar;
  }

  static GosuVersion findGosuVersion(String sdkHome) {
    File pluginRoot = new File(sdkHome);
    if (isPluginRootAJar(pluginRoot)) {
      return new GosuVersion(0, 0);
    }
    File gosuCoreApiJar = findGosuCoreApiJar(pluginRoot);
    if (gosuCoreApiJar == null) {
      gosuCoreApiJar = new File(pluginRoot, "classes");
    }
    IDirectory iDirectory = FileFactory.instance().getIDirectory(gosuCoreApiJar);
    IFile gosuVersionProps = iDirectory.file("gw/lang/gosu-version.properties");
    if (gosuVersionProps.exists()) {
      InputStream in = null;
      try {
        in = gosuVersionProps.openInputStream();
        Reader reader = StreamUtil.getInputStreamReader(in);
        return GosuVersion.parse(reader);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        try {
          StreamUtil.close(in);
        } catch (IOException e) {
          System.err.println("error closing input stream: " + e.getMessage());
        }
      }
    } else {
      return null;
    }
  }
}
