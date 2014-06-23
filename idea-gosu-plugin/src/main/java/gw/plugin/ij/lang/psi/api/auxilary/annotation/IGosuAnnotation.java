/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.auxilary.annotation;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import org.jetbrains.annotations.Nullable;

public interface IGosuAnnotation extends PsiAnnotation {
  IGosuAnnotation[] EMPTY_ARRAY = new IGosuAnnotation[0];

  @Nullable
  PsiJavaCodeReferenceElement getClassReference();

  String getShortName();
}
