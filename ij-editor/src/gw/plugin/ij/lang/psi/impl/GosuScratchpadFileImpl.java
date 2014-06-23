/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.PsiManagerImpl;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class GosuScratchpadFileImpl extends GosuProgramFileImpl {
  public static final String GOSU_SCRATCHPAD_NAME = "Gosu Scratchpad";
  public static final String GOSU_SCRATCHPAD_PKG = "scratchpad";
  public static final String FQN = GOSU_SCRATCHPAD_PKG + '.' + GOSU_SCRATCHPAD_NAME;

  @Nullable
  public static GosuScratchpadFileImpl instance(@NotNull Project project) {
    return (GosuScratchpadFileImpl) PsiManagerImpl.getInstance(project).findFile(getScratchpadFile(project));
  }

  public GosuScratchpadFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider);
  }

  @Override
  public VirtualFile getVirtualFile() {
    return getScratchpadFile( getProject() );
  }

  @Override
  public boolean isPhysical() {
    return true;
  }

  @NotNull
  @Override
  public String getQualifiedClassNameFromFile() {
    return FQN;
  }

  @NotNull
  @Override
  public String getPackageName() {
    return GOSU_SCRATCHPAD_PKG;
  }

  @Override
  public IModule getModuleForPsi() {
    return GosuModuleUtil.getGlobalModule(getProject());
  }

  @Override
  public IModule getModule() {
    return GosuModuleUtil.getGlobalModule(getProject());
  }

  @Nullable
  public static VirtualFile getScratchpadFile(@NotNull Project project) {
    String strFile = getScratchpadFileName(project);
    return LocalFileSystem.getInstance().findFileByPath(strFile);
  }

  public static String getScratchpadFileName(@NotNull Project project) {
    String strTempDir = System.getProperty( "java.io.tmpdir" );
    String strProjectDir = project.getLocationHash();
    String strTempFile = GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME + GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT;
    File dir = new File( strTempDir, strProjectDir );
    if( !dir.isDirectory() ) {
      dir.mkdir();
    }
    File tempFile = new File(dir, strTempFile);
    try {
      tempFile.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return tempFile.getAbsolutePath();
  }

  @Override
  public PsiElement getNavigationElement() {
    return this;
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    return GosuIcons.FILE_SCRATCHPAD;
  }
}
