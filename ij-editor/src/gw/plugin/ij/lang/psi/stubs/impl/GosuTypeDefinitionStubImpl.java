/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuTypeDefinitionStubImpl extends StubBase<IGosuTypeDefinition> implements GosuTypeDefinitionStub {
  private static final int ANONYMOUS = 0x01;
  private static final int INTERFACE = 0x02;
  private static final int ENUM = 0x04;
  private static final int ANNOTATION = 0x08;
  private static final int IS_IN_QUALIFIED_NEW = 0x10;

  private final StringRef myName;
  private final String[] mySuperClasses;
  private final StringRef myQualifiedName;
  private StringRef mySourceFileName;
  private final String[] myAnnotations;
  private final byte myFlags;


  public GosuTypeDefinitionStubImpl(StubElement parent,
                                    final String name,
                                    final String[] supers,
                                    final IStubElementType elementType,
                                    final String qualifiedName,
                                    String[] annotations,
                                    byte flags) {
    super(parent, elementType);
    myAnnotations = annotations;
    myName = StringRef.fromString(name);
    mySuperClasses = supers;
    myQualifiedName = StringRef.fromString(qualifiedName);
    myFlags = flags;
  }

  public String[] getSuperClassNames() {
    return mySuperClasses;
  }

  public String getName() {
    return StringRef.toString(myName);
  }

  public String[] getAnnotations() {
    return myAnnotations;
  }

  public String getQualifiedName() {
    return StringRef.toString(myQualifiedName);
  }

  public String getSourceFileName() {
    return StringRef.toString(mySourceFileName);
  }

  public void setSourceFileName(final StringRef sourceFileName) {
    mySourceFileName = sourceFileName;
  }

  public void setSourceFileName(final String sourceFileName) {
    mySourceFileName = StringRef.fromString(sourceFileName);
  }

  public boolean isAnnotationType() {
    return (myFlags & ANNOTATION) != 0;
  }

  public boolean isAnonymous() {
    return (myFlags & ANONYMOUS) != 0;
  }

  public boolean isAnonymousInQualifiedNew() {
    return (myFlags & IS_IN_QUALIFIED_NEW) != 0;
  }

  public boolean isInterface() {
    return (myFlags & INTERFACE) != 0;
  }

  public boolean isEnum() {
    return (myFlags & ENUM) != 0;
  }

  public byte getFlags() {
    return myFlags;
  }

  public static byte buildFlags(@NotNull IGosuTypeDefinition typeDefinition) {
    byte flags = 0;
    if (typeDefinition.isAnonymous()) {
      flags |= ANONYMOUS;
      if (((IGosuAnonymousClassDefinition) typeDefinition).isInQualifiedNew()) {
        flags |= IS_IN_QUALIFIED_NEW;
      }
    }
    if (typeDefinition.isAnnotationType()) {
      flags |= ANNOTATION;
    }
    if (typeDefinition.isInterface()) {
      flags |= INTERFACE;
    }
    if (typeDefinition.isEnum()) {
      flags |= ENUM;
    }
    return flags;
  }

}
