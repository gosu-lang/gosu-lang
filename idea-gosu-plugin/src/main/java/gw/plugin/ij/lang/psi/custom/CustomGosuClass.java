/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.google.common.collect.Maps;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.HierarchicalMethodSignature;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterList;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import gw.fs.IFile;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomGosuClass extends UserDataHolderBase implements PsiClass {
  @Nullable
  private final PsiFile file;
  private final Map<String, PsiField> fields = Maps.newHashMap();
  private final Map<String, PsiMethod> methods = Maps.newHashMap();
  private final String relativeName;
  private final String namespace;
  private final IType superType;
  private final IFile[] sourceFiles;
  private final boolean valid;

  public CustomGosuClass(@NotNull PsiFile file) {
    this(file, file.getVirtualFile().getNameWithoutExtension(), file.getVirtualFile().getParent().getName());
  }

  public CustomGosuClass(@NotNull PsiFile file, String relativeName, String namespace) {
    this.file = file;
    this.relativeName = relativeName;
    this.namespace = namespace;
    this.superType = null;
    this.sourceFiles = null;
    this.valid = isClassValid();
  }

  public CustomGosuClass(@NotNull IFileBasedType type) {
    final VirtualFile virtualFile = FileUtil.getTypeResourceFiles(type).get(0);
    final PsiManager manager = PsiManagerImpl.getInstance((Project) type.getTypeLoader().getModule().getExecutionEnvironment().getProject().getNativeProject());

    this.file = manager.findFile(virtualFile);

    final Module module = virtualFile.getUserData(ModuleUtil.KEY_MODULE);
    if (module != null) {
      this.file.putUserData(ModuleUtil.KEY_MODULE, module);
    }

    this.relativeName = type.getRelativeName();
    this.namespace = type.getNamespace();
    this.superType = type.getSupertype();
    this.sourceFiles = type.getSourceFiles();
    this.valid = isClassValid();
  }

  private boolean isClassValid() {
    return true;
  }

  @Override
  public String getQualifiedName() {
    if (namespace == null || namespace.isEmpty()) {
      return relativeName;
    }
    return namespace + "." + relativeName;
  }

  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getName() {
    return relativeName;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public boolean isAnnotationType() {
    return false;
  }

  @NotNull
  @Override
  public PsiMethod[] getConstructors() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiMethod[] getMethods() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiMethod[] findMethodsByName(@NonNls String name, boolean checkBases) {
    return PsiMethod.EMPTY_ARRAY;
  }

  @Override
  public boolean isValid() {
    return valid;
  }

  @NotNull
  @Override
  public Project getProject() throws PsiInvalidElementAccessException {
    return file.getProject();
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  @NotNull
  @Override
  public PsiManager getManager() {
    return PsiManager.getInstance(getProject());
  }

  @Override
  public boolean isPhysical() {
    return true;
  }

  @Override
  public PsiClass getContainingClass() {
    return null;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
    return file;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    return false;
  }

  @Override
  public PsiField findFieldByName(@NonNls String name, boolean checkBases) {
    return null;
  }

  //////

  @Override
  public PsiReferenceList getExtendsList() {
    return null;
  }

  @Override
  public PsiReferenceList getImplementsList() {
    return null;
  }

  @NotNull
  @Override
  public PsiClassType[] getExtendsListTypes() {
    return PsiClassType.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClassType[] getImplementsListTypes() {
    return PsiClassType.EMPTY_ARRAY;
  }

  @Override
  public PsiClass getSuperClass() {
    return superType != null ? CustomPsiClassCache.instance().getPsiClass(superType) : null;
  }

  @NotNull
  @Override
  public PsiClass[] getInterfaces() {
    return PsiClass.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClass[] getSupers() {
    final PsiClass klass = getSuperClass();
    return klass != null ? new PsiClass[]{klass} : PsiClass.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClassType[] getSuperTypes() {
    return PsiClassType.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiField[] getFields() {
    return PsiField.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClass[] getInnerClasses() {
    return PsiClass.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClassInitializer[] getInitializers() {
    return PsiClassInitializer.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiField[] getAllFields() {
    return PsiField.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiMethod[] getAllMethods() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public PsiClass[] getAllInnerClasses() {
    return PsiClass.EMPTY_ARRAY;
  }

  @Override
  public PsiMethod findMethodBySignature(PsiMethod patternMethod, boolean checkBases) {
    return null;
  }

  @NotNull
  @Override
  public PsiMethod[] findMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(@NonNls String name, boolean checkBases) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
    return Collections.emptyList();
  }

  @Override
  public PsiClass findInnerClassByName(@NonNls String name, boolean checkBases) {
    return null;
  }

  @Override
  public PsiElement getLBrace() {
    return null;
  }

  @Override
  public PsiElement getRBrace() {
    return null;
  }

  @Override
  public PsiIdentifier getNameIdentifier() {
    return null;
  }

  @Nullable
  @Override
  public PsiElement getScope() {
    return null;
  }

  @Override
  public boolean isInheritor(@NotNull PsiClass baseClass, boolean checkDeep) {
    return false;
  }

  @Override
  public boolean isInheritorDeep(PsiClass baseClass, @Nullable PsiClass classToByPass) {
    return false;
  }

  @NotNull
  @Override
  public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
    return null;
  }

  @Nullable
  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return null;
  }

  @Override
  public PsiDocComment getDocComment() {
    return null;
  }

  @Override
  public boolean isDeprecated() {
    return false;
  }

  @Override
  public boolean hasTypeParameters() {
    return false;
  }

  @Override
  public PsiTypeParameterList getTypeParameterList() {
    return null;
  }

  @NotNull
  @Override
  public PsiTypeParameter[] getTypeParameters() {
    return PsiTypeParameter.EMPTY_ARRAY;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new CustomGosuClassPresentation(this);
  }

  @Override
  public void navigate(boolean requestFocus) {
    final Navigatable navigatable = PsiNavigationSupport.getInstance().getDescriptor(this);
    if (navigatable != null) {
      navigatable.navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return true;
  }

  @Override
  public boolean canNavigateToSource() {
    return true;
  }

  @Override
  public PsiModifierList getModifierList() {
    return null;
  }

  @NotNull
  @Override
  public Language getLanguage() {
    return GosuLanguage.instance();
  }

  @NotNull
  @Override
  public PsiElement[] getChildren() {
    return PsiElement.EMPTY_ARRAY;
  }

  @Nullable
  @Override
  public PsiElement getParent() {
    return null;
  }

  @Override
  public PsiElement getFirstChild() {
    return null;
  }

  @Override
  public PsiElement getLastChild() {
    return null;
  }

  @Override
  public PsiElement getNextSibling() {
    return null;
  }

  @Override
  public PsiElement getPrevSibling() {
    return null;
  }

  @Nullable
  @Override
  public TextRange getTextRange() {
    return null;
  }

  @Override
  public int getStartOffsetInParent() {
    return 0;
  }

  @Override
  public int getTextLength() {
    return 0;
  }

  @Override
  public PsiElement findElementAt(int offset) {
    return null;
  }

  @Override
  public PsiReference findReferenceAt(int offset) {
    return null;
  }

  @Override
  public int getTextOffset() {
    return 0;
  }

  @Nullable
  @Override
  public String getText() {
    return file.getText();
  }

  @NotNull
  @Override
  public char[] textToCharArray() {
    return file.getText().toCharArray();
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    if (sourceFiles != null && sourceFiles.length > 1) {
      //TODO-dp this is a hack, add some extension point
      if (namespace.equals("entity") || namespace.equals("typekey")) {
        for (int i = sourceFiles.length - 1; i >= 0; i--) {
          final IFile file = sourceFiles[i];
          final VirtualFile virtualFile = ((IDEAFile) file).getVirtualFile();
          final PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(virtualFile);
          if (psiFile instanceof XmlFile) { //Avoid java and gosu files
            return psiFile.getNavigationElement();
          }
        }
      }
    }

    return file.getNavigationElement();
  }

  @Nullable
  @Override
  public PsiElement getOriginalElement() {
    return null;
  }

  @Override
  public boolean textMatches(@NotNull @NonNls CharSequence text) {
    return false;
  }

  @Override
  public boolean textMatches(@NotNull PsiElement element) {
    return false;
  }

  @Override
  public boolean textContains(char c) {
    return false;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {

  }

  @Override
  public void acceptChildren(@NotNull PsiElementVisitor visitor) {

  }

  @Nullable
  @Override
  public PsiElement copy() {
    return null;
  }

  @Nullable
  @Override
  public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
    return null;
  }

  @Nullable
  @Override
  public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return null;
  }

  @Nullable
  @Override
  public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return null;
  }

  @Override
  public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {

  }

  @Nullable
  @Override
  public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    return null;
  }

  @Nullable
  @Override
  public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
    return null;
  }

  @Nullable
  @Override
  public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
    return null;
  }

  @Override
  public void delete() throws IncorrectOperationException {
  }

  @Override
  public void checkDelete() throws IncorrectOperationException {
  }

  @Override
  public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
  }

  @Nullable
  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    return null;
  }

  @Override
  public PsiReference getReference() {
    return null;
  }

  @NotNull
  @Override
  public PsiReference[] getReferences() {
    return PsiReference.EMPTY_ARRAY;
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
    return false;
  }

  @Override
  public PsiElement getContext() {
    return null;
  }

  @NotNull
  @Override
  public GlobalSearchScope getResolveScope() {
    return GlobalSearchScope.allScope(getProject());
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    return PsiClassImplUtil.getClassUseScope(this);
  }

  @Nullable
  @Override
  public ASTNode getNode() {
    return null;
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return false;
  }

  @Override
  public Icon getIcon(int flags) {
    return file.getIcon(flags);
  }

  public PsiElement getField(String name, PsiElement element) {
    PsiField psiField = fields.get(name);
    if (psiField == null) {
      psiField = new GosuXMLField(name, element, this);
      fields.put(name, psiField);
    }
    return psiField;
  }

  public PsiElement getMethod(@NotNull IMethodInfo mi, XmlTag element) {
    final String signature = getSignature(mi);
    PsiMethod psiMethod = methods.get(signature);
    if (psiMethod == null) {
      psiMethod = new GosuXMLMethod(mi, this);
      methods.put(signature, psiMethod);
    }
    return psiMethod;
  }

  @NotNull
  public static String getSignature(@NotNull IMethodInfo mi) {
    String s = mi.getDisplayName() + "(";
    for (IParameterInfo parameter : mi.getParameters()) {
      s += parameter.getFeatureType().getName() + ", ";
    }
    return s + ")";
  }

  @Nullable
  @Override
  public String toString() {
    return getName();
  }
}
