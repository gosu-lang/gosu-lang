/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.impl.java.stubs.index.JavaShortClassNameIndex;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.CollectionFactory;
import com.intellij.util.io.StringRef;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import gw.plugin.ij.lang.psi.stubs.impl.GosuTypeDefinitionStubImpl;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnnotatedMemberIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnonymousClassIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuShortClassNameIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public abstract class GosuTypeDefinitionElementType<TypeDef extends IGosuTypeDefinition>
    extends GosuStubElementType<GosuTypeDefinitionStub, TypeDef> {

  public GosuTypeDefinitionElementType(@NotNull String debugName, Class<? extends IParsedElement> peType) {
    super(debugName, peType);
  }

  @NotNull
  public GosuTypeDefinitionStub createStub(@NotNull TypeDef psi, StubElement parentStub) {
    String[] superClassNames = psi.getSuperClassNames();
    final byte flags = GosuTypeDefinitionStubImpl.buildFlags(psi);
    return new GosuTypeDefinitionStubImpl(parentStub, psi.getName(), superClassNames, this, psi.getQualifiedName(), getAnnotationNames(psi),
        flags);
  }

  @NotNull
  public static String[] getAnnotationNames(@NotNull PsiModifierListOwner psi) {
    List<String> annoNames = CollectionFactory.arrayList();
    final PsiModifierList modifierList = psi.getModifierList();
    if (modifierList instanceof IGosuModifierList) {
      for (IGosuAnnotation annotation : ((IGosuModifierList) modifierList).getAnnotations()) {
        final PsiJavaCodeReferenceElement element = annotation.getClassReference();
        if (element != null) {
          final String annoShortName = StringUtil.getShortName(element.getText()).trim();
          if (StringUtil.isNotEmpty(annoShortName)) {
            annoNames.add(annoShortName);
          }
        }
      }
    }
    return ArrayUtil.toStringArray(annoNames);
  }

  public void serialize(@NotNull GosuTypeDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeName(stub.getQualifiedName());
    dataStream.writeByte(stub.getFlags());
    writeStringArray(dataStream, stub.getSuperClassNames());
    writeStringArray(dataStream, stub.getAnnotations());
  }

  private static void writeStringArray(@NotNull StubOutputStream dataStream, @NotNull String[] names) throws IOException {
    dataStream.writeByte(names.length);
    for (String name : names) {
      dataStream.writeName(name);
    }
  }

  @NotNull
  public GosuTypeDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    String name = StringRef.toString(dataStream.readName());
    String qname = StringRef.toString(dataStream.readName());
    byte flags = dataStream.readByte();
    String[] superClasses = readStringArray(dataStream);
    String[] annos = readStringArray(dataStream);
    return new GosuTypeDefinitionStubImpl(parentStub, name, superClasses, this, qname, annos, flags);
  }

  @NotNull
  private static String[] readStringArray(@NotNull StubInputStream dataStream) throws IOException {
    byte supersNumber = dataStream.readByte();
    String[] superClasses = new String[supersNumber];
    for (int i = 0; i < supersNumber; i++) {
      superClasses[i] = StringRef.toString(dataStream.readName());
    }
    return superClasses;
  }

  public void indexStub(@NotNull GosuTypeDefinitionStub stub, @NotNull IndexSink sink) {
    if (stub.isAnonymous()) {
      final String[] classNames = stub.getSuperClassNames();
      if (classNames.length != 1) {
        return;
      }
      final String baseClassName = classNames[0];
      if (baseClassName != null) {
        final String shortName = PsiNameHelper.getShortClassName(baseClassName);
        sink.occurrence(GosuAnonymousClassIndex.KEY, shortName);
      }
    } else {
      String shortName = stub.getName();
      if (shortName != null) {
        sink.occurrence(JavaShortClassNameIndex.getInstance().getKey(), shortName);
        sink.occurrence(GosuShortClassNameIndex.KEY, shortName);
      }
      final String fqn = stub.getQualifiedName();
      if (fqn != null) {
        sink.occurrence(JavaFullClassNameIndex.getInstance().getKey(), fqn.hashCode());
      }
    }

    for (String annName : stub.getAnnotations()) {
      if (annName != null) {
        sink.occurrence(GosuAnnotatedMemberIndex.KEY, annName);
      }
    }
  }
}
