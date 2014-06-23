/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import gw.config.AbstractPlatformHelper;
import gw.config.CommonServices;
import gw.config.IPlatformHelper;
import gw.config.Registry;
import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeSystem;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.IProject;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 */
@SuppressWarnings("unused")
public abstract class AbstractCompileMojo extends AbstractMojo {

  @Component
  protected MavenProject mavenProject;

  @Parameter(property = "gosu.compile.skip")
  protected boolean skip;

  @Parameter
  protected List<String> packages;

  @Parameter
  protected List<String> exclusions = Collections.emptyList();

  @Parameter
  protected List<File> roots;

  /**
   * Name of the class (which should implement Runnable) to call before setting up Gosu.
   */
  @Parameter
  protected String setupClass;

  /**
   * Module to compile.
   */
  @Parameter(defaultValue = "${project.artifactId}")
  protected String moduleName;

  /**
   * Ignore all compilation errors.
   */
  @Parameter(defaultValue = "false")
  protected boolean ignoreErrors;

  // We need really global lock due to the URL#handlers being JVM-wide global.
  private static Object LOCK = "reallygloballock";

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (skip) {
      getLog().info("Skipping Gosu compiler plugin");
      return;
    }

    // Sure, we are thread safe.
    synchronized(LOCK) {
      // FIXME-isd: instead of resetting gosu protocol handler, we would rather like to have total control over
      // Gosu classloading.
      GosuClassPathThing.cleanup();
      setupAndCompile();
    }
  }

  private void setupAndCompile() throws MojoFailureException {
    GosuInitialization init = null;
    try {
      IProject project = new MavenBackedProject(mavenProject);
      IExecutionEnvironment env = TypeSystem.getExecutionEnvironment(project);
      init = setupGosu(env);

      IModule module = env.getModule(moduleName);
      TypeSystem.pushModule(module);
      try {
        compileGosu();
      } finally {
        TypeSystem.popModule(module);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new MojoFailureException("Failed to compile gosu classes", e);
    } finally {
      if (init != null) {
        init.uninitializeMultipleModules();
      }
    }
  }

  protected abstract File getOutputFolder();

  protected abstract List<File> getSources();

  protected abstract List<File> getClassPath();

  protected List<File> getJreClassPath() {
    return Collections.emptyList();
  }

  protected List<File> getRoots() {
    return roots == null ? Collections.singletonList(mavenProject.getBasedir()) : roots;
  }

  private GosuInitialization setupGosu(IExecutionEnvironment env) throws URISyntaxException, IOException, MojoExecutionException {
    // Run setup
    runCustomSetup();
    GosuInitialization init = GosuInitialization.instance(env);

    // Always override platform helper
    CommonServices.getKernel().redefineService_Privileged(IPlatformHelper.class, new MavenPlatformHelper());

    IModule jre = createJreModule(env);
    IModule current = createMasterModule(env);
    IModule global = createGlobalModule(env, current, jre);

    // Setup dependencies
    current.addDependency(new Dependency(jre, true));
    global.addDependency(new Dependency(current, true));
    global.addDependency(new Dependency(jre, true));
    init.initializeMultipleModules(Lists.newArrayList(jre, current, global));
    return init;
  }

  protected void runCustomSetup() throws MojoExecutionException {
    // Run setup class
    if (setupClass != null) {
      ClassLoader cl = createSetupClassLoader();
      try {
        Class<?> setup = cl.loadClass(setupClass);
        Runnable runnable = (Runnable) setup.getConstructor(String.class).newInstance(moduleName);
        runnable.run();
      } catch (Exception e) {
        throw new MojoExecutionException("Cannot run custom setup step!", e);
      }
    } else {
      // Reset CommonServices
      ITypeSystem ts = CommonServices.getTypeSystem();
      Registry.initDefaults();
      // It has all our shutdown listeners! We need it back!
      CommonServices.sneakySetTypeSystem(ts);
    }
  }

  /**
   * Create a classloader to load setup class from.
   */
  protected ClassLoader createSetupClassLoader() {
    List<URL> urls = Lists.newArrayList();
    urls.addAll(Lists.transform(getDependencies(), ToURL.INSTANCE));
    urls.addAll(Lists.transform(getJreClassPath(), ToURL.INSTANCE));
    urls.addAll(Lists.transform(getClassPath(), ToURL.INSTANCE));
    return new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
  }

  protected void compileGosu() throws IOException {
    // FIXME-isd: Iterate through gosu classes in the source directories only...
    ITypeLoader typeLoader = TypeSystem.getTypeLoader(GosuClassTypeLoader.class, TypeSystem.getCurrentModule());
    Set<? extends CharSequence> allTypeNames = typeLoader.getAllTypeNames();
    int count = 0;
    for (CharSequence cs : allTypeNames) {
      String typeName = cs.toString();
      if (includeType(typeName)) {
        IType type = TypeSystem.getByFullName(typeName);
        if (type instanceof IGosuClass) {
          IGosuClass gosuClass = (IGosuClass) type;

          // Write class + inner classes
          String fileName = type.getName().replace('.', '/');
          count += compileClass(gosuClass, fileName);
        }
      }
    }
    getLog().info("Compiled " + count + " Gosu types to the " + getOutputFolder());
  }

  private int compileClass(IGosuClass gosuClass, String fileName) throws IOException {
    writeClassToDisk(gosuClass, fileName);

    int count = 1;
    for (IGosuClass innerClass : gosuClass.getInnerClasses()) {
      compileClass(innerClass, fileName + '$' + innerClass.getRelativeName());
      count++;
    }
    return count;
  }

  private IModule createJreModule(IExecutionEnvironment env) throws URISyntaxException, IOException {
    IModule jreModule = env.createJreModule();

    List<File> cp = Lists.newArrayList();

    // Let's get all JARs from the bootstrap classloader.
    URLClassLoader bootstrap = (URLClassLoader) ClassLoader.getSystemClassLoader().getParent();
    for (URL url : bootstrap.getURLs()) {
      File file = new File(url.toURI());
      cp.add(file);
    }

    // Wee need rt.jar, too
    URL url = bootstrap.getResource("java/lang/Object.class");
    if (url != null) {
      final JarURLConnection connection =
              (JarURLConnection) url.openConnection();
      File rtJar = new File(connection.getJarFileURL().toURI());
      cp.add(rtJar);
    }

    // Add dependencies classpath
    cp.addAll(getDependencies());
    cp.addAll(getJreClassPath());
    jreModule.configurePaths(Lists.transform(cp, ToDirectory.INSTANCE),
            Collections.<IDirectory>emptyList());

    // FIXME-isd: we put all Java classes in JRE module, to support PL initialization.
    List<IDirectory> cp2 = Lists.newArrayList(jreModule.getJavaClassPath());
    cp2.addAll(Lists.transform(getClassPath(), ToDirectory.INSTANCE));
    jreModule.setJavaClassPath(cp2);
    return jreModule;
  }

  protected List<File> getDependencies() {
    List<File> cp = Lists.newArrayList();
    for (Artifact art : mavenProject.getArtifacts()) {
      if (art.getArtifactHandler().isAddedToClasspath() && art.getFile() != null) {
        cp.add(art.getFile());
      }
    }
    return cp;
  }

  private IModule createMasterModule(IExecutionEnvironment env) {
    IModule module = GosuShop.createModule(env, mavenProject.getArtifactId());

    // Roots
    List<IDirectory> roots = Lists.newArrayList();
    for (File root : getRoots()) {
      roots.add(CommonServices.getFileSystem().getIDirectory(root));
    }
    module.setRoots(roots);

    // Sources and classes
    // FIXME: should scan for classes in dependencies?...
    List<IDirectory> sources = Lists.transform(getSources(), ToDirectory.INSTANCE);
    module.setSourcePath(sources);

    // FIXME: See JRE module setup.
    //List<String> classpath = getClassPath();
    //module.setJavaClassPath(classpath);
    return module;
  }

  private IModule createGlobalModule(IExecutionEnvironment env, IModule... modules) {
    // Collect all source paths
    List<IDirectory> sources = Lists.newArrayList();
    for (IModule module : modules) {
      sources.addAll(module.getSourcePath());
    }

    IModule globalModule = GosuShop.createGlobalModule(env);
    globalModule.configurePaths(Collections.<IDirectory>emptyList(), sources);
    return globalModule;
  }

  private void writeClassToDisk(IGosuClass type, String fileName) throws IOException {
    File outputFile = new File(getOutputFolder(), fileName + ".class");
    byte[] bytes = null;
    if (ignoreErrors) {
      try {
        bytes = type.compile();
      } catch (Exception e) {
        getLog().warn("Failed to compile type '" + type.getName() + "', ignoring.");
        getLog().debug("Compilation errors are ", e);
      }
    } else {
      bytes = type.compile();
    }
    if (bytes != null) {
      outputFile.getParentFile().mkdirs();
      Files.write(bytes, outputFile);
    }
  }

  private boolean includeType(String typeName) {
    // FIXME-isd: make this configurable?
    if (typeName.contains("Errant")) {
      return false;
    }
    for (String exclusion : exclusions) {
      if (typeName.startsWith(exclusion)) {
        return false;
      }
    }
    return true;
  }

  private static class MavenPlatformHelper extends AbstractPlatformHelper {
    @Override
    public boolean isInIDE() {
      // XXX: Seems like in multiple modules mode, this has to be 'true'
      return true;
    }

    @Override
    public boolean shouldCacheTypeNames() {
      return false;
    }

    @Override
    public void refresh(IModule module) {
    }
  }
}
