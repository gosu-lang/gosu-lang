/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.structure;

import com.google.common.collect.Sets;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.structureView.impl.java.FieldsFilter;
import com.intellij.ide.structureView.impl.java.JavaClassTreeElement;
import com.intellij.ide.structureView.impl.java.JavaInheritedMembersNodeProvider;
import com.intellij.ide.structureView.impl.java.KindSorter;
import com.intellij.ide.structureView.impl.java.PropertiesGrouper;
import com.intellij.ide.structureView.impl.java.PublicElementsFilter;
import com.intellij.ide.structureView.impl.java.SuperTypesGrouper;
import com.intellij.ide.structureView.impl.java.VisibilitySorter;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

public class GosuStructureViewModel extends TextEditorBasedStructureViewModel {
  @NotNull
  private final IGosuFileBase rootElement;
  private static final Collection<NodeProvider> NODE_PROVIDERS = Arrays.<NodeProvider>asList(new JavaInheritedMembersNodeProvider());

  private static final Class[] SUITABLE_CLASSES = new Class[]{
      IGosuFileBase.class,
      IGosuTypeDefinition.class,
      IGosuMethod.class,
      IGosuVariable.class};

  public GosuStructureViewModel(@NotNull IGosuFileBase rootElement) {
    super(rootElement);
    this.rootElement = rootElement;
  }

  @Override
  public Collection<NodeProvider> getNodeProviders() {
    return NODE_PROVIDERS;
  }

  @NotNull
  protected PsiFile getPsiFile() {
    return rootElement;
  }

  @NotNull
  public StructureViewTreeElement getRoot() {
    PsiClass psiClass = rootElement.getPsiClass();
    if(psiClass == null) {
      psiClass = new FakePsiClass();
    }
    return new JavaClassTreeElement(psiClass, false, Sets.<PsiClass>newHashSet());
  }

  @NotNull
  public Filter[] getFilters() {
    return new Filter[]{new FieldsFilter(),
        new PublicElementsFilter()};
  }

  @NotNull
  public Grouper[] getGroupers() {
    return new Grouper[]{new SuperTypesGrouper(),
        new PropertiesGrouper()};
  }

  @Override
  public boolean shouldEnterElement(Object element) {
    return element instanceof IGosuTypeDefinition;
  }

  @NotNull
  public Sorter[] getSorters() {
    return new Sorter[]{KindSorter.INSTANCE,
        VisibilitySorter.INSTANCE,
        Sorter.ALPHA_SORTER};
  }

  @NotNull
  protected Class[] getSuitableClasses() {
    return SUITABLE_CLASSES;
  }

  @Nullable
  protected Object findAcceptableElement(@Nullable PsiElement element) {
    while (element != null && !(element instanceof PsiDirectory)) {
      if (isSuitable(element)) {
        if (element instanceof IGosuFileBase) {
          return ((IGosuFileBase) element).getPsiClass();
        }
        return element;
      }
      element = element.getParent();
    }
    return null;
  }

  @Override
  protected boolean isSuitable(@NotNull final PsiElement element) {
    if (super.isSuitable(element)) {
      if (element instanceof IGosuMethod) {
        IGosuMethod method = (IGosuMethod) element;
        PsiElement parent = method.getParent().getParent();
        if (parent instanceof IGosuTypeDefinition) {
          return ((IGosuTypeDefinition) parent).getQualifiedName() != null;
        }
      } else if (element instanceof IGosuVariable) {
        IGosuVariable field = (IGosuVariable) element;
        PsiElement parent = field.getParent().getParent().getParent();
        if (parent instanceof IGosuTypeDefinition) {
          return ((IGosuTypeDefinition) parent).getQualifiedName() != null;
        }
      } else if (element instanceof IGosuTypeDefinition) {
        return ((IGosuTypeDefinition) element).getQualifiedName() != null;
      } else if (element instanceof IGosuFileBase) {
        return true;
      }
    }
    return false;
  }
}
