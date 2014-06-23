/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.project;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JavaSdkVersion;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.pom.java.LanguageLevel;
import gw.plugin.ij.sdk.GosuSdkType;
import org.jetbrains.annotations.NotNull;

public class GosuModuleBuilder extends JavaModuleBuilder {
  @Override
  public boolean isSuitableSdk( @NotNull Sdk sdk ) {
    if( !(sdk.getSdkType() instanceof GosuSdkType) ) {
      return false;
    }
    JavaSdkVersion version = JavaSdk.getInstance().getVersion( sdk );
    return version.getMaxLanguageLevel().isAtLeast( LanguageLevel.JDK_1_5 );
  }

  @Override
  public ModuleType getModuleType() {
    return GosuModuleType.getInstance();
  }
}
