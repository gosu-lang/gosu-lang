/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.HierarchicalMethodSignature;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.impl.InheritanceImplUtil;
import com.intellij.psi.impl.JavaPsiImplementationHelperImpl;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.PsiSuperMethodImplUtil;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.ui.RowIcon;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.parser.GosuSyntheticCompositeElement;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuExtendsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMembersDeclaration;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariableList;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuDeclaredElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.impl.statements.GosuSyntheticModifierListImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import gw.plugin.ij.lang.psi.util.GosuClassImplUtil;
import gw.plugin.ij.lang.psi.util.GosuDocUtil;
import gw.plugin.ij.lang.psi.util.PsiUtil;
import gw.plugin.ij.util.GosuIconsUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.TypeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class GosuTypeDefinitionImpl extends GosuDeclaredElementImpl<IClassStatement, GosuTypeDefinitionStub> implements IGosuTypeDefinition {
  @Nullable
  private volatile PsiClass[] myInnerClasses;
  @Nullable
  private volatile List<PsiMethod> myMethods;
  @Nullable
  private IGosuField[] myFields;
  @Nullable
  private volatile IGosuMethod[] myGosuMethods;
  @Nullable
  private volatile IGosuMethod[] myConstructors;

  public GosuTypeDefinitionImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  protected GosuTypeDefinitionImpl(@NotNull GosuTypeDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitTypeDefinition( this );
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeDefinition(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  public int getTextOffset() {
     final PsiIdentifier identifier = getNameIdentifier();
     return identifier != null ? identifier.getTextRange().getStartOffset() : -1;
   }

  @Nullable
  public String getQualifiedName() {
    final GosuTypeDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getQualifiedName();
    }

    final PsiElement parent = getParent();
    if (parent instanceof IGosuFile) {
      final String packageName = ((IGosuFile) parent).getPackageName();
      return !packageName.isEmpty() ? packageName + "." + getName() : getName();
    }

    final PsiClass containingClass = getContainingClass();
    if (containingClass != null) {
      return containingClass.getQualifiedName() + "." + getName();
    }

    return null;
  }

  @NotNull
  public IGosuMembersDeclaration[] getMemberDeclarations() {
    return findChildrenByClass(IGosuMembersDeclaration.class);
  }

  public ColoredItemPresentation getPresentation() {
    return new ColoredItemPresentation() {
      @Nullable
      public String getPresentableText() {
        return getName();
      }

      @Nullable
      public String getLocationString() {
        final PsiFile file = getContainingFile();
        if (file instanceof IGosuFile) {
          final String packageName = ((IGosuFile) file).getPackageName();
          return packageName.isEmpty() ? "" : String.format("(%s)", packageName);
        } else {
          return "";
        }
      }

      @Nullable
      public Icon getIcon(boolean open) {
        return GosuTypeDefinitionImpl.this.getIcon(ICON_FLAG_VISIBILITY | ICON_FLAG_READ_STATUS);
      }

      @Nullable
      public TextAttributesKey getTextAttributesKey() {
        if (isDeprecated()) {
          return CodeInsightColors.DEPRECATED_ATTRIBUTES;
        }
        return null;
      }
    };
  }

  @Nullable
  public IGosuExtendsClause getExtendsClause() {
    return getStubOrPsiChild(GosuElementTypes.EXTENDS_CLAUSE);
  }

  @Nullable
  public IGosuImplementsClause getImplementsClause() {
    return getStubOrPsiChild(GosuElementTypes.IMPLEMENTS_CLAUSE);
  }

  @NotNull
  public String[] getSuperClassNames() {
//    final GosuTypeDefinitionStub stub = getStub();
//    if( stub != null )
//    {
//      return stub.getSuperClassNames();
//    }
    return ArrayUtil.mergeArrays( getExtendsNames(), getImplementsNames(), String.class );
  }

  @NotNull
  protected String[] getImplementsNames() {
    IGosuImplementsClause implementsClause = getImplementsClause();
    IGosuCodeReferenceElement[] implementsRefs =
        implementsClause != null ? implementsClause.getReferenceElements() : IGosuCodeReferenceElement.EMPTY_ARRAY;
    ArrayList<String> implementsNames = new ArrayList<>(implementsRefs.length);
    for (IGosuCodeReferenceElement ref : implementsRefs) {
      String name = ref.getReferenceName();
      if (name != null) {
        implementsNames.add(name);
      }
    }

    return ArrayUtil.toStringArray( implementsNames );
  }

  @NotNull
  protected String[] getExtendsNames() {
    IGosuExtendsClause extendsClause = getExtendsClause();
    IGosuCodeReferenceElement[] extendsRefs =
        extendsClause != null ? extendsClause.getReferenceElements() : IGosuCodeReferenceElement.EMPTY_ARRAY;
    ArrayList<String> extendsNames = new ArrayList<>(extendsRefs.length);
    for (IGosuCodeReferenceElement ref : extendsRefs) {
      String name = ref.getReferenceName();
      if (name != null) {
        extendsNames.add(name);
      }
    }
    return ArrayUtil.toStringArray(extendsNames);
  }

  public void checkDelete() throws IncorrectOperationException {
  }

  public void delete() throws IncorrectOperationException {
    PsiElement parent = getParent();
    if (parent instanceof AbstractGosuClassFileImpl) {
      AbstractGosuClassFileImpl file = (AbstractGosuClassFileImpl) parent;
      file.delete();
      return;
    }

    ASTNode astNode = parent.getNode();
    if (astNode != null) {
      astNode.removeChild(getNode());
    }
  }

  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     @Nullable PsiElement lastParent,
                                     @NotNull PsiElement place) {
    //## todo:
    return PsiClassImplUtil.processDeclarationsInClass(this, processor, state, null, lastParent, place, false);
  }

  public String getName() {
    final GosuTypeDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getName();
    }
    return GosuPsiImplUtil.getName( this );
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return GosuClassImplUtil.isClassEquivalentTo( this, another );
  }

  public boolean isInterface() {
    return false;
  }

  public boolean isEnhancement() {
    return false;
  }

  public boolean isAnnotationType() {
    return false;
  }

  public boolean isEnum() {
    return false;
  }

  @Nullable
  public PsiReferenceList getExtendsList() {
    return null;
  }

  @Nullable
  public PsiReferenceList getImplementsList() {
    return null;
  }

  @NotNull
  public PsiClassType[] getExtendsListTypes() {
    return GosuClassImplUtil.getExtendsListTypes(this);
  }

  @NotNull
  public PsiClassType[] getImplementsListTypes() {
    return GosuClassImplUtil.getImplementsListTypes( this );
  }

  @Nullable
  public PsiClass getSuperClass() {
    return GosuClassImplUtil.getSuperClass( this );
  }

  public PsiClass[] getInterfaces() {
    return GosuClassImplUtil.getInterfaces( this );
  }

  @NotNull
  public final PsiClass[] getSupers() {
    return GosuClassImplUtil.getSupers( this );
  }

  @NotNull
  public PsiClassType[] getSuperTypes() {
    return GosuClassImplUtil.getSuperTypes( this );
  }

  @NotNull
  public PsiMethod[] getMethods() {
    List<PsiMethod> cached = myMethods;
    if (cached == null) {
      cached = new ArrayList<>();
      cached.addAll(Arrays.asList(findChildrenByClass(IGosuMethod.class)));

      myMethods = cached;
    }
    // yay
    List<PsiMethod> result = new ArrayList<>(cached);
    return result.toArray( new PsiMethod[result.size()] );
  }

  public void subtreeChanged() {
    myMethods = null;
    myInnerClasses = null;
    myConstructors = null;
    myGosuMethods = null;

    myFields = null;

    super.subtreeChanged();
  }

  @NotNull
  public PsiMethod[] getConstructors() {
    IGosuMethod[] cached = myConstructors;
    if (cached == null) {
      List<IGosuMethod> result = new ArrayList<>();
      for (final PsiMethod method : getMethods()) {
        if (method.isConstructor()) {
          result.add((IGosuMethod) method);
        }
      }

      myConstructors = cached = result.toArray(new IGosuMethod[result.size()]);
    }
    return cached;
  }

  @NotNull
  public PsiClass[] getInnerClasses() {
    PsiClass[] inners = myInnerClasses;
    if (inners == null) {
      myInnerClasses = inners = findChildrenByClass( PsiClass.class );
    }

    return inners;
  }

  @NotNull
  public PsiClassInitializer[] getInitializers() {
    return PsiClassInitializer.EMPTY_ARRAY;
  }

  @NotNull
  public PsiField[] getAllFields() {
    return GosuClassImplUtil.getAllFields(this);
  }

  @NotNull
  public PsiMethod[] getAllMethods() {
    return GosuClassImplUtil.getAllMethods( this );
  }

  @NotNull
  public PsiClass[] getAllInnerClasses() {
    return PsiClassImplUtil.getAllInnerClasses( this );
  }

  @Nullable
  public PsiField findFieldByName(String name, boolean checkBases) {
    return GosuClassImplUtil.findFieldByName( this, name, checkBases );
  }

  @Nullable
  public PsiMethod findMethodBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findMethodBySignature( this, patternMethod, checkBases );
  }

  @NotNull
  public PsiMethod[] findMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findMethodsBySignature( this, patternMethod, checkBases );
  }

  @NotNull
  public PsiMethod[] findCodeMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsBySignature( this, patternMethod, checkBases );
  }

  @NotNull
  public PsiMethod[] findMethodsByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findMethodsByName( this, name, checkBases );
  }

  @NotNull
  public PsiMethod[] findCodeMethodsByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsByName( this, name, checkBases );
  }

  @NotNull
  public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(String name, boolean checkBases) {
    return GosuClassImplUtil.findMethodsAndTheirSubstitutorsByName( this, name, checkBases );
  }

  @NotNull
  public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
    return GosuClassImplUtil.getAllMethodsAndTheirSubstitutors( this );
  }

  @Nullable
  public PsiClass findInnerClassByName(String name, boolean checkBases) {
    return null;
  }

  public boolean isAnonymous() {
    return false;
  }

  @Nullable
  public PsiIdentifier getNameIdentifier() {
    PsiIdentifier id = getNameIdentifierImpl();
    if (id == null) {
      return null;
    }
    if (id.getFirstChild() != null && id.getFirstChild() instanceof PsiIdentifier) {
      // Always return the leaf token node; we always want to patch in just the name and not mess with upper-level tree nodes
      id = (PsiIdentifier) id.getFirstChild();
    }
    return id;
  }

  @Nullable
  public PsiElement getScope() {
    return null;
  }

  public boolean isInheritor(@NotNull PsiClass baseClass, boolean checkDeep) {
    // implemented in terns of TS for perf reasons
//    return InheritanceImplUtil.isInheritor(this, baseClass, checkDeep);
    return isInheritorFast(baseClass, checkDeep);
  }

  private boolean isInheritorFast(PsiClass baseClass, boolean checkDeep) {
    if (baseClass instanceof PsiAnonymousClass) {
      return false;
    }
    if (baseClass instanceof CustomGosuClass) {
      return false;
    }
    IType baseType = findBaseType( baseClass );
    IType thisType = TypeUtil.getType(this);
    if (baseType == null || thisType == null) {
      return InheritanceImplUtil.isInheritor(this, baseClass, checkDeep);
    }
    if (checkDeep) {
      IModule module = baseType.getTypeLoader().getModule();
      TypeSystem.pushModule(module);
      try {
        return baseType != thisType && baseType.isAssignableFrom(thisType);
      } finally {
        TypeSystem.popModule(module);
      }
    } else {
      return isDirectInheritor(thisType, baseType);
    }
  }

  //
  // Resolve IType using same module as this type's module, then resolve the PsiClass from the IType.
  // If the PsiClass is the same as the original baseClass, use this resolved IType.
  // Otherwise, resolve the IType using the module of the baseClass (as we normally would).
  //
  // Note we do all of this so that our IJ-Editor tests are recognized as TestCase classes.
  // The problem is that when IJ tests if this class inherits from TestCase, the junit.framework.TestCase
  // class passed in is from the JRE module, but the editor tests get their junit.framework.TestCase
  // from the ij-editor module, since the classes are not the same, IJ thinks our editor tests aren't
  // test classes.
  //
  // The trick here, and I'm not sure why it works, but is why InheritanceImplUtil.isInheritor() works,
  // is to resolve to an IType, then resolve that back to a PsiClass and see if we have the same class.
  // For some reason our junit.framework.TestCase from the ij-editor module resolves as a reference to
  // the junit.framework.TestCase in the *JRE* module. Each module does indeed have it's own copy of that
  // class, but somehow IJ consistently resolves PsiClass refs to the JRE one.
  //
  private IType findBaseType( PsiClass baseClass ) {
    IType baseType = null;
    IModule module = GosuModuleUtil.findModuleForPsiElement(this);
    if( !(baseClass instanceof IGosuTypeDefinition) ) {
      baseType = TypeUtil.getType(baseClass, module);
      if( baseType != null ) {
        PsiElement resolvedBase = PsiTypeResolver.resolveType( baseType, this );
        if( resolvedBase != baseClass ) {
          baseType = TypeUtil.getType(baseClass, module);
        }
      }
    }
    if( baseType == null ) {
      baseType = TypeUtil.getType(baseClass, module);
    }
    return baseType;
  }

  private boolean isDirectInheritor(IType thisType, IType baseType) {
    IType supertype = thisType.getSupertype();
    if (supertype != null && baseType.equals(TypeUtil.getConcreteType(supertype))) {
      return true;
    }
    for (IType iface : thisType.getInterfaces()) {
      if (!TypeSystem.isDeleted(iface) && baseType.equals(TypeUtil.getConcreteType(iface))) {
        return true;
      }
    }
    return false;
  }

  public boolean isInheritorDeep(@NotNull PsiClass baseClass, @Nullable PsiClass classToByPass) {
    // not called often, safe to leave as-is, non-performant
    return InheritanceImplUtil.isInheritorDeep(this, baseClass, classToByPass);
  }

  @Nullable
  public PsiClass getContainingClass() {
    PsiElement element = this.getParent();
    while (element != null && !(element instanceof PsiClass)) {
      element = element.getParent();
    }
    return element instanceof PsiClass ? (PsiClass) element : null;
  }

  @NotNull
  public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
    return PsiSuperMethodImplUtil.getVisibleSignatures(this);
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
    final PsiIdentifier identifier = getNameIdentifier();
    if (identifier instanceof GosuIdentifierImpl) {
      ((GosuIdentifierImpl) identifier).setName(newName);
    } else {
      ((PsiNamedElement) identifier).setName(newName);
    }
    return this;
  }

  @Nullable
  public IGosuModifierList getModifierList() {
    return (IGosuModifierList)findChildByClass( PsiModifierList.class );
  }

  @Override
  public boolean hasModifierProperty( @PsiModifier.ModifierConstant @NonNls @NotNull String name ) {
    IGosuModifierList modifierList = getModifierList();
    modifierList = modifierList == null
                   ? new GosuSyntheticModifierListImpl( new GosuSyntheticCompositeElement( GosuElementTypes.ELEM_TYPE_ModifierListClause ) )
                   : modifierList;
    return modifierList.hasModifierProperty( name );
  }

  @Nullable
  public PsiDocComment getDocComment() {
    return GosuDocUtil.findDocCommnentNode(getNode());
  }

  public boolean isDeprecated() {
    final String qualifiedName = getQualifiedName();
    final IModule module = GosuModuleUtil.findModuleForPsiElement(this);
    TypeSystem.pushModule(module);
    try {
      IType type = TypeSystem.getByFullNameIfValid(qualifiedName, module);
      return type != null && type.getTypeInfo().isDeprecated();
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public boolean hasTypeParameters() {
    return getTypeParameters().length > 0;
  }

  @Nullable
  public IGosuTypeVariableList getTypeParameterList() {
    return (IGosuTypeVariableList) findChildByType(GosuElementTypes.ELEM_TYPE_TypeVariableListClause);
  }

  @NotNull
  public IGosuTypeVariable[] getTypeParameters() {
    final IGosuTypeVariableList list = getTypeParameterList();
    if (list != null) {
      return (IGosuTypeVariable[]) list.getTypeParameters();
    }

    return IGosuTypeVariable.EMPTY_ARRAY;
  }

  @Nullable
  public PsiElement getOriginalElement() {
    return GosuPsiImplUtil.getOriginalElement(this, getContainingFile());
  }

  public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
    if (anchor == null) {
      return add(element);
    }
    if (anchor.getParent() == this) {

      final PsiElement nextChild = anchor.getNextSibling();
      if (nextChild == null) {
        add(element);
        return element;
      }

      ASTNode node = element.getNode();
      assert node != null;
      //body.getNode().addLeaf(GosuElementTypes.mNLS, "\n", nextChild.getNode());
      return addBefore(element, nextChild);
    } else {
      return super.addAfter(element, anchor);
    }
  }

  @NotNull
  public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
    if (anchor == null) {
      add(element);
      return element;
    }

    ASTNode node = element.getNode();
    assert node != null;
    final ASTNode bodyNode = getNode();
    final ASTNode anchorNode = anchor.getNode();
    bodyNode.addLeaf(GosuTokenTypes.TT_WHITESPACE, "\n", anchorNode);
    if (element instanceof PsiMethod) {
      bodyNode.addLeaf(GosuTokenTypes.TT_WHITESPACE, "\n", anchorNode);
    }
    bodyNode.addChild(node, anchorNode);
    bodyNode.addLeaf(GosuTokenTypes.TT_WHITESPACE, "\n", anchorNode);
    return element;
  }

  @NotNull
  public PsiElement add(@NotNull PsiElement psiElement) throws IncorrectOperationException {
    final PsiElement lBrace = getGosuLBrace();

    if (lBrace == null) {
      throw new IncorrectOperationException("No left brace");
    }

    PsiMember member = getAnyMember(psiElement);
    PsiElement anchor = member != null ? getDefaultAnchor(member) : null;
    if (anchor == null) {
      anchor = lBrace.getNextSibling();
    }

    if (anchor != null) {
      ASTNode node = anchor.getNode();
      assert node != null;
      if (GosuTokenTypes.TT_OP_semicolon.equals(node.getElementType())) {
        anchor = anchor.getNextSibling();
      }
      psiElement = addBefore(psiElement, anchor);
    } else {
      add(psiElement);
    }

    return psiElement;
  }

  @Nullable
  private PsiElement getDefaultAnchor(PsiMember member) {
    CodeStyleSettings settings = CodeStyleSettingsManager.getSettings(getProject());

    int order = JavaPsiImplementationHelperImpl.getMemberOrderWeight(member, settings);
    if (order < 0) {
      return null;
    }

    PsiElement lastMember = null;
    for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
      int order1 = JavaPsiImplementationHelperImpl.getMemberOrderWeight(getAnyMember(child), settings);
      if (order1 < 0) {
        continue;
      }
      if (order1 > order) {
        final PsiElement lBrace = getGosuLBrace();
        if (lastMember != null) {
          PsiElement nextSibling = lastMember.getNextSibling();
          while (nextSibling instanceof LeafPsiElement && (nextSibling.getText().equals(",") || nextSibling.getText().equals(";"))) {
            nextSibling = nextSibling.getNextSibling();
          }
          return nextSibling == null && lBrace != null ? PsiUtil.skipWhitespaces(lBrace.getNextSibling(), true) : nextSibling;
        } else if (lBrace != null) {
          return PsiUtil.skipWhitespaces(lBrace.getNextSibling(), true);
        }
      }
      lastMember = child;
    }
    return getGosuRBrace();
  }

  @Nullable
  private static PsiMember getAnyMember(@Nullable PsiElement psiElement) {
    if (psiElement instanceof PsiMember) {
      return (PsiMember) psiElement;
    }
    if (psiElement instanceof IGosuVariable) {
      return (PsiMember) psiElement;
    }
    return null;
  }

  @NotNull
  public <T extends IGosuMembersDeclaration> T addMemberDeclaration(@NotNull T decl, @Nullable PsiElement anchorBefore)
      throws IncorrectOperationException {

    if (anchorBefore == null) {
      return (T) add(decl);
    }

    decl = (T) addBefore(decl, anchorBefore);
//    node.addLeaf(GosuTokenTypes.mWS, " ", decl.getNode()); //add whitespaces before and after to hack over incorrect auto reformat
//    node.addLeaf(GosuTokenTypes.mWS, " ", anchorNode);
    return decl;
  }

  @NotNull
  public IGosuField[] getFields() {
    if (myFields == null) {
      IGosuVariable[] declarations = findChildrenByClass(IGosuVariable.class);
      if (declarations.length == 0) {
        return IGosuField.EMPTY_ARRAY;
      }
      List<IGosuField> result = new ArrayList<>();
      for (IGosuVariable variable : declarations) {
        if (variable instanceof IGosuField) {
          result.add((IGosuField) variable);
        }
      }
      myFields = result.toArray(new IGosuField[result.size()]);
    }

    return myFields;
  }

  @Nullable
  public PsiJavaToken getLBrace() {
    return null; //(PsiJavaToken)findChildByType( GosuTokenTypes.TT_OP_brace_left );
  }

  @Nullable
  public PsiJavaToken getRBrace() {
    return null; //(PsiJavaToken)findChildByType( GosuTokenTypes.TT_OP_brace_right );
  }

  @Nullable
  public LeafPsiElement getGosuLBrace() {
    return (LeafPsiElement) findChildByType(GosuTokenTypes.TT_OP_brace_left);
  }

  @Nullable
  public LeafPsiElement getGosuRBrace() {
    return (LeafPsiElement) findChildByType(GosuTokenTypes.TT_OP_brace_right);
  }

  public void removeVariable(IGosuVariable variable) {
    throw new UnsupportedOperationException("Men at work");
    //## todo:
    // GosuPsiImplUtil.removeVariable( variable );
  }

  @NotNull
  public IGosuVariable addVariableBefore(@NotNull IGosuVariable variable, @Nullable IGosuStatement anchor) throws IncorrectOperationException {
    PsiElement rBrace = getGosuRBrace();
    if (anchor == null && rBrace == null) {
      throw new IncorrectOperationException();
    }

    if (anchor != null && !this.equals(anchor.getParent())) {
      throw new IncorrectOperationException();
    }

    ASTNode elemNode = variable.getNode();
    final ASTNode anchorNode = anchor != null ? anchor.getNode() : rBrace.getNode();
    getNode().addChild(elemNode, anchorNode);
    return (IGosuVariable) elemNode.getPsi();
  }

  @Nullable
  public String toString() {
    return "PsiClass:" + getName();
  }

  @Override
  public Icon getElementIcon(@IconFlags int flags) {
    final String qualifiedName = getQualifiedName();
    final Icon icon = getSpecificIcon(this);
    if (icon == null) {
      return null;
    }

    IType type = TypeSystem.getByFullNameIfValid(qualifiedName, GosuModuleUtil.findModuleForPsiElement(this));
    if (!(type instanceof IGosuClass)) {
      return icon;
    }

    final boolean isLocked = (flags & ICON_FLAG_READ_STATUS) != 0 && !isWritable();
    flags = GosuIconsUtil.getFlags(this, (IGosuClass) type, isLocked);

    final RowIcon rowIcon = ElementBase.createLayeredIcon(this, icon, flags);
    if ((flags & ICON_FLAG_VISIBILITY) != 0) {
      GosuIconsUtil.setVisibilityIcon((IGosuClass) type, rowIcon);
    }
    return rowIcon;
  }

  // TODO: move to subclasses
  private Icon getSpecificIcon(PsiElement element) {
    if (element instanceof GosuSyntheticClassDefinitionImpl) {
      return GosuIcons.FILE_PROGRAM;
    } else if (element instanceof GosuAnonymousClassDefinitionImpl) {
      return GosuIcons.ANONYMOUS_CLASS;
    } else if (element instanceof GosuClassDefinitionImpl) {
      return GosuIcons.CLASS;
    } else if (element instanceof GosuEnhancementDefinitionImpl) {
      return GosuIcons.ENHANCEMENT;
    } else if (element instanceof GosuEnumDefinitionImpl) {
      return GosuIcons.ENUM;
    } else if (element instanceof GosuInterfaceDefinitionImpl) {
      return GosuIcons.INTERFACE;
    } else if (element instanceof GosuAnnotationDefinitionImpl) {
      return GosuIcons.ANNOTATION;
    } else
    return null;
  }

  public boolean hasExplicitModifier(@NotNull @NonNls String name) {
    IType type = TypeUtil.getType(this);
    int modifiers = type != null ? type.getModifiers() : 0;

    if (name.equals(IGosuModifier.PUBLIC)) {
      return Modifier.isPublic(modifiers);
    }
    if (name.equals(IGosuModifier.ABSTRACT)) {
      return Modifier.isAbstract(modifiers);
    }
    if (name.equals(IGosuModifier.PRIVATE)) {
      return Modifier.isPrivate(modifiers);
    }
    if (name.equals(IGosuModifier.PROTECTED)) {
      return Modifier.isProtected(modifiers);
    }
    if (name.equals(IGosuModifier.PACKAGE_LOCAL) || name.equals(IGosuModifier.INTERNAL)) {
      return Modifier.isInternal(modifiers);
    }
    if (name.equals(IGosuModifier.STATIC)) {
      return Modifier.isStatic(modifiers);
    }
    if (name.equals(IGosuModifier.FINAL)) {
      return Modifier.isFinal(modifiers);
    }
    if (name.equals(IGosuModifier.TRANSIENT)) {
      return Modifier.isTransient(modifiers);
    }
    return false;
  }

}
