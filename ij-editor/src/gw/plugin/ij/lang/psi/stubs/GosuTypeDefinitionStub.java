/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;

public interface GosuTypeDefinitionStub extends NamedStub<IGosuTypeDefinition> {
  String[] getSuperClassNames();

  String getQualifiedName();

  String getSourceFileName();

  String[] getAnnotations();

  boolean isAnonymous();

  boolean isAnonymousInQualifiedNew();

  boolean isInterface();

  boolean isEnum();

  boolean isAnnotationType();

  byte getFlags();
}
