/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.daemon.QuickFixBundle;

import java.util.List;

/**
 * DUPLICATED AND MODIFIED FROM IJ.
 *
 * @author ven
 */
public enum GosuCreateClassKind {
  JAVA_CLASS("Java " + QuickFixBundle.message("create.class"), null),
  JAVA_INTERFACE("Java " + QuickFixBundle.message("create.interface"), null),
  JAVA_ENUM("Java " + QuickFixBundle.message("create.enum"), null),

  GOSU_CLASS("Gosu " + QuickFixBundle.message("create.class"), "GosuClass.gs"),
  GOSU_INTERFACE("Gosu " + QuickFixBundle.message("create.interface"), "GosuInterface.gs"),
  GOSU_ENUM("Gosu " + QuickFixBundle.message("create.enum"), "GosuEnum.gs");

  public static final List<GosuCreateClassKind> INTERFACES = ImmutableList.of(GosuCreateClassKind.GOSU_INTERFACE, GosuCreateClassKind.JAVA_INTERFACE);
  public static final List<GosuCreateClassKind> CLASSES = ImmutableList.of(GosuCreateClassKind.GOSU_CLASS, GosuCreateClassKind.JAVA_CLASS);
  public static final List<GosuCreateClassKind> ALL = ImmutableList.copyOf(values());

  private final String myDescription;
  private final String myTemplateName;

  GosuCreateClassKind(final String description, String templateName) {
    myDescription = description;
    myTemplateName = templateName;
  }

  public String getDescription() {
    return myDescription;
  }

  public String getTemplateName() {
    return myTemplateName;
  }

  public boolean isGosu() {
    return this == GOSU_CLASS ||
        this == GOSU_INTERFACE ||
        this == GOSU_ENUM;
  }

  public boolean isJava() {
    return !isGosu();
  }
}
