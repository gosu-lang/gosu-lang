/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import gw.lang.GosuVersion;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuSdkAdditionalData implements SdkAdditionalData {
  private static final String JDK = "jdk";

  private static final String GOSU_VERSION = "gosuVersion";

  private String _javaSdkName;
  private Sdk _javaSdk;
  private final GosuVersion _gosuVersion;

  public GosuSdkAdditionalData(Sdk javaSdk, GosuVersion gosuVersion) {
    _javaSdk = javaSdk;
    _gosuVersion = gosuVersion;
  }

  public GosuSdkAdditionalData(@NotNull Element element) {
    _javaSdkName = element.getAttributeValue(JDK);
    String versionAttr = element.getAttributeValue(GOSU_VERSION);
    _gosuVersion = versionAttr != null && versionAttr.length() > 0 ? GosuVersion.parse(versionAttr) : null;
  }

  @NotNull
  public Object clone() throws CloneNotSupportedException {
    GosuSdkAdditionalData data = (GosuSdkAdditionalData) super.clone();
    return data;
  }

  @Nullable
  public Sdk getJavaSdk() {
    final ProjectJdkTable table = ProjectJdkTable.getInstance();
    if (_javaSdk == null) {
      if (_javaSdkName != null) {
        _javaSdk = table.findJdk(_javaSdkName);
        _javaSdkName = null;
      } else {
        for (Sdk jdk : table.getAllJdks()) {
          if (GosuSdkUtils.isApplicableJdk(jdk)) {
            _javaSdk = jdk;
            break;
          }
        }
      }
    }
    return _javaSdk;
  }

  public void setJavaSdk(final Sdk javaSdk) {
    _javaSdk = javaSdk;
  }

  public void save(@NotNull Element element) {
    final Sdk sdk = getJavaSdk();
    if (sdk != null) {
      element.setAttribute(JDK, sdk.getName());
    }
    if (_gosuVersion != null) {
      element.setAttribute(GOSU_VERSION, _gosuVersion.toString());
    }
  }

  public GosuVersion getGosuVersion() {
    return _gosuVersion;
  }

  public String getVersion() {
    return _gosuVersion == null ? null : _gosuVersion.toString() + " (" + (_javaSdk == null ? "" : _javaSdk.getVersionString()) + ")";
  }
}
