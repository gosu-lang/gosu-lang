/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.impl;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import gw.plugin.ij.util.JavaPsiFacadeUtil;

public class GosuReferenceListStubImpl extends StubBase<IGosuReferenceList> implements GosuReferenceListStub {
  private final String[] myNames;
  private PsiClassType[] myTypes;

  public GosuReferenceListStubImpl(final StubElement parentStub, IStubElementType elemtType, final String[] refNames) {
    super(parentStub, elemtType);
    myNames = refNames;
  }

  public String[] getBaseClasses() {
    return myNames;
  }

  @Override
  public PsiClassType[] getReferencedTypes() {
    if (myTypes != null) return myTypes;
    if (myNames.length == 0) {
      myTypes = PsiClassType.EMPTY_ARRAY;
      return myTypes;
    }

    PsiClassType[] types = new PsiClassType[myNames.length];

    final PsiElementFactory factory = JavaPsiFacadeUtil.getElementFactory(getProject());

    int nullCount = 0;
    final IGosuReferenceList psi = getPsi();
    for (int i = 0; i < types.length; i++) {
      PsiElement context = psi;
      //TODO-dp there is magic here, maybe ?
//      if (getParentStub() instanceof PsiClassStub) {
//        context = ((PsiClassImpl)getParentStub().getPsi()).calcBasesResolveContext(PsiNameHelper.getShortClassName(myNames[i]), psi);
//      }

      try {
        final PsiJavaCodeReferenceElement ref = factory.createReferenceFromText(myNames[i], context);
        ((PsiJavaCodeReferenceElementImpl)ref).setKindWhenDummy(PsiJavaCodeReferenceElementImpl.CLASS_NAME_KIND);
        types[i] = factory.createType(ref);
      }
      catch (IncorrectOperationException e) {
        types[i] = null;
        nullCount++;
      }
    }

    if (nullCount > 0) {
      PsiClassType[] newTypes = new PsiClassType[types.length - nullCount];
      int cnt = 0;
      for (PsiClassType type : types) {
        if (type != null) newTypes[cnt++] = type;
      }
      types = newTypes;
    }

    myTypes = types;
    return types;
  }

}
