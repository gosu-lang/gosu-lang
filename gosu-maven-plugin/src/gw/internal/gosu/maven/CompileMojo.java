/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import com.google.common.collect.Lists;
import org.apache.maven.model.Resource;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 */
@SuppressWarnings("unused")
@Mojo(name = "compile",
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        defaultPhase = LifecyclePhase.COMPILE)
public class CompileMojo extends AbstractCompileMojo {

  @Parameter(property = "project.build.outputDirectory")
  private File output;

  @Parameter
  private List<File> sources;

  @Parameter
  private List<File> classes;

  @Parameter
  private List<File> jreClasses;

  @Override
  protected File getOutputFolder() {
    return output;
  }

  @Override
  protected List<File> getSources() {
    if (sources != null) {
      return sources;
    }
    List<File> sources = Lists.newArrayList();
    for (String source : mavenProject.getCompileSourceRoots()) {
      sources.add(new File(source));
    }
    for (Resource res : mavenProject.getResources()) {
      sources.add(new File(res.getDirectory()));
    }
    return sources;
  }

  @Override
  protected List<File> getClassPath() {
    if (classes != null) {
      return classes;
    }
    File output = new File(mavenProject.getBuild().getOutputDirectory());
    return Collections.singletonList(output);
  }

  protected List<File> getJreClassPath() {
    return jreClasses != null ? jreClasses : Collections.<File>emptyList();
  }
}
