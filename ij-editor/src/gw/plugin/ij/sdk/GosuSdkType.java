/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import gw.plugin.ij.icons.GosuIcons;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class GosuSdkType extends SdkType implements JavaSdkType {
  @NonNls
  public static final String SDK_NAME = "Gosu SDK";

  public GosuSdkType() {
    super(SDK_NAME);
  }

  @Override
  public String suggestHomePath() {
    return "c:\\";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return GosuIcons.CLASS; // TODO: another icon for sdk
  }

  @Override
  public boolean isValidSdkHome(String path) {
    return GosuSdkUtils.findGosuCoreApiJar(new File(path)) != null;
  }

  @NotNull
  @Override
  public String suggestSdkName(String currentSdkName, String sdkHome) {
    return SDK_NAME;
  }

  @Override
  public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
    final GosuSdkConfigurable c = new GosuSdkConfigurable(sdkModel, sdkModificator);

    sdkModel.addListener(new SdkModel.Listener() {
      public void sdkAdded(@NotNull Sdk sdk) {
        if (sdk.getSdkType().equals(JavaSdk.getInstance())) {
          c.addJavaSdk(sdk);
        }
      }

      public void beforeSdkRemove(@NotNull Sdk sdk) {
        if (sdk.getSdkType().equals(JavaSdk.getInstance())) {
          c.removeJavaSdk(sdk);
        }
      }

      public void sdkChanged(@NotNull Sdk sdk, String previousName) {
        if (sdk.getSdkType().equals(JavaSdk.getInstance())) {
          c.updateJavaSdkList(sdk, previousName);
        }
      }

      public void sdkHomeSelected(@NotNull final Sdk sdk, final String newSdkHome) {
        if (sdk.getSdkType().equals(GosuSdkType.getInstance())) {
          c.internalJdkUpdate(sdk);
        }
      }
    });

    return c;
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return SDK_NAME;
  }

  public static GosuSdkType getInstance() {
    return SdkType.findInstance(GosuSdkType.class);
  }

  @Override
  public String getVersionString(@NotNull Sdk sdk) {
    final GosuSdkAdditionalData data = (GosuSdkAdditionalData) sdk.getSdkAdditionalData();
    return data.getVersion();
  }

  @Nullable
  public String getVersionString(String sdkHome) {
    for (Sdk sdk : ProjectJdkTable.getInstance().getSdksOfType(JavaSdk.getInstance())) {
      if (sdk.getHomePath().equals(sdkHome)) {
        return getVersionString(sdk);
      }
    }
    return "Unknown Gosu SDK version";
  }

  @Override
  public SdkAdditionalData loadAdditionalData(@NotNull Sdk currentSdk, @NotNull Element additional) {
    return new GosuSdkAdditionalData(additional);
  }

  @Override
  public void saveAdditionalData(@NotNull SdkAdditionalData data, @NotNull Element e) {
    if (data instanceof GosuSdkAdditionalData) {
      ((GosuSdkAdditionalData) data).save(e);
    }
  }

  // JavaSdkType
  @Nullable
  private static Sdk getJavaSdk(@NotNull Sdk sdk) {
    final SdkAdditionalData data = sdk.getSdkAdditionalData();
    if (data instanceof GosuSdkAdditionalData) {
      return ((GosuSdkAdditionalData) data).getJavaSdk();
    }
    return null;
  }

  @Nullable
  @Override
  public String getBinPath(@NotNull Sdk sdk) {
    final Sdk jdk = getJavaSdk(sdk);
    return jdk != null ? JavaSdk.getInstance().getBinPath(jdk) : null;
  }

  @Nullable
  public String getToolsPath(@NotNull Sdk sdk) {
    final Sdk jdk = getJavaSdk(sdk);
    return (jdk != null && jdk.getVersionString() != null) ? JavaSdk.getInstance().getToolsPath(jdk) : null;
  }

  @Nullable
  public String getVMExecutablePath(@NotNull Sdk sdk) {
    final Sdk jdk = getJavaSdk(sdk);
    return jdk != null ? JavaSdk.getInstance().getVMExecutablePath(jdk) : null;
  }
}
