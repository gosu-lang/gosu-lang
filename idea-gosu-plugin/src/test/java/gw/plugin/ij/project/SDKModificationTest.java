/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.project;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.impl.ProjectRootManagerImpl;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.framework.GosuTestCase;
import gw.plugin.ij.sdk.GosuSdkAdditionalData;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeRunnable;
import gw.plugin.ij.util.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class SDKModificationTest extends GosuTestCase {
  IModule oldRootModule;

  protected boolean runInDispatchThread() {
    return false;
  }

  public void before() {
    oldRootModule = TypeSystem.getGlobalModule();
  }

  private void after() {
    TypeSystem.pushModule(oldRootModule);
  }

  public void testChangingToNewSdkWorks() throws InterruptedException {
    before();
    try {
      assertTrue(PluginLoaderUtil.instance(getProject()).isStarted());
      final Sdk gosuSdk = GosuSdkUtils.getDefaultGosuSdk();
      final GosuSdkAdditionalData sdkAdditionalData = (GosuSdkAdditionalData) gosuSdk.getSdkAdditionalData();
      final Sdk javaSdk = sdkAdditionalData.getJavaSdk();
      setSDK(javaSdk);
      assertTrue(PluginLoaderUtil.instance(getProject()).isStarted());
      assertEquals(javaSdk, ((IJreModule)TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( getProject() ) ).getJreModule()).getNativeSDK());

      setSDK(gosuSdk);
    } finally {
      after();
    }
  }

  public void testModifitingTheSdkWorks() throws InterruptedException, MalformedURLException {
    before();
    try {
      final List<IDirectory> cp1 = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( getProject() ) ).getJreModule().getJavaClassPath();
      assertTrue(PluginLoaderUtil.instance(getProject()).isStarted());
      String addedJar = modifySDK();
      assertTrue(PluginLoaderUtil.instance(getProject()).isStarted());
      final List<IDirectory> cp2 = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( getProject() ) ).getJreModule().getJavaClassPath();
      assertEquals(1, cp2.size() - cp1.size());
      cp2.removeAll(cp1);
      assertEquals(cp2.get(0), addedJar);
    } finally {
      after();
    }
  }

  // private

  @NotNull
  String modifySDK() throws InterruptedException {
    final GosuSdkAdditionalData sdkAdditionalData = (GosuSdkAdditionalData) GosuSdkUtils.getDefaultGosuSdk().getSdkAdditionalData();
    final Sdk javaSdk = sdkAdditionalData.getJavaSdk();
    final String jar = javaSdk.getHomePath() + File.separator + "lib" + File.separator + "htmlconverter.jar";
    ExecutionUtil.execute(ExecutionUtil.WRITE | ExecutionUtil.DISPATCH | ExecutionUtil.BLOCKING, new SafeRunnable() {
      public void execute() {
        try {
          final SdkModificator sdkModificator = GosuSdkUtils.getDefaultGosuSdk().getSdkModificator();
          VirtualFile file = VfsUtil.findFileByURL(new File(jar).toURL());
          if (file == null) {
            throw new IllegalStateException("Cannot find htmlconverter.jar!");
          }
          sdkModificator.addRoot(file, OrderRootType.CLASSES);
          sdkModificator.commitChanges();
        } catch (MalformedURLException e) {
          throw new RuntimeException(e);
        }
      }
    });
    Thread.sleep(5000);
    UIUtil.settleModalEventQueue();
    return jar;
  }

  @Nullable
  Sdk setSDK(final Sdk sdk) throws InterruptedException {
    final ProjectRootManager instance = ProjectRootManagerImpl.getInstance(getProject());
    final Sdk oldSDK = instance.getProjectSdk();
    ExecutionUtil.execute(ExecutionUtil.WRITE | ExecutionUtil.DISPATCH | ExecutionUtil.BLOCKING, new SafeRunnable() {
      public void execute() {
        instance.setProjectSdk(sdk);
      }
    });
    Thread.sleep(5000);
    UIUtil.settleModalEventQueue();
    return oldSDK;
  }
}
