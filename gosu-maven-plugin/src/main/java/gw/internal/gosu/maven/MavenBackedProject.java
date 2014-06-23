/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.maven;

import gw.lang.reflect.module.IProject;
import org.apache.maven.project.MavenProject;

/**
* Created with IntelliJ IDEA.
* User: idubrov
* Date: 1/22/13
* Time: 1:56 PM
* To change this template use File | Settings | File Templates.
*/
class MavenBackedProject implements IProject {
  private final MavenProject _mavenProject;

  MavenBackedProject(MavenProject mavenProject) {
    _mavenProject = mavenProject;
  }

  @Override
  public String getName() {
    return _mavenProject.getName();
  }

  @Override
  public Object getNativeProject() {
    return _mavenProject;
  }

  @Override
  public boolean isDisposed() {
    return false;
  }

  @Override
  public boolean isHeadless() {
    return true;
  }

  @Override
  public boolean isShadowMode() {
    return false;
  }
}
