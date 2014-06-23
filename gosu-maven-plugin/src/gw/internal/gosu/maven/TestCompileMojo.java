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
import java.util.List;

/**
 */
@SuppressWarnings("unused")
@Mojo(name = "test-compile",
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.TEST,
        defaultPhase = LifecyclePhase.TEST_COMPILE)
public class TestCompileMojo extends AbstractCompileMojo {

  @Parameter(property = "project.build.testOutputDirectory")
  private File testOutput;

  @Override
  protected File getOutputFolder() {
    return testOutput;
  }

  @Override
  protected List<File> getSources() {
    List<File> sources = Lists.newArrayList();
    for (String source : mavenProject.getTestCompileSourceRoots()) {
      sources.add(new File(source));
    }
    for (Resource res : mavenProject.getTestResources()) {
      sources.add(new File(res.getDirectory()));
    }
    return sources;
  }

  @Override
  protected List<File> getClassPath() {
    return Lists.newArrayList(
            new File(mavenProject.getBuild().getTestOutputDirectory()),
            new File(mavenProject.getBuild().getOutputDirectory()));
  }
}
