/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef.members;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Key;
import com.intellij.psi.HierarchicalMethodSignature;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodReceiver;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.impl.PsiSuperMethodImplUtil;
import com.intellij.psi.impl.source.HierarchicalMethodSignatureImpl;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.presentation.java.JavaPresentationUtil;
import com.intellij.psi.scope.NameHint;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.ui.RowIcon;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMember;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariableList;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuDeclaredElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterListImpl;
import gw.plugin.ij.lang.psi.util.GosuDocUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public abstract class GosuMethodBaseImpl<T extends NamedStub> extends GosuDeclaredElementImpl<IFunctionStatement, T> implements IGosuMethod {
  public static final Key<GosuMethodImpl> ORIGINAL_CONSTRUCTOR = new Key<>("OriginalConstructor");

  protected GosuMethodBaseImpl(@NotNull final T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public GosuMethodBaseImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitMethod(this);
  }

  public int getTextOffset() {
    if (isConstructor()) {
      return getNode().getStartOffset();
    } else {
      return getNameIdentifier().getTextRange().getStartOffset();
    }
  }

  @NotNull
  public IGosuParameter[] getParameters() {
    final GosuParameterListImpl parameters = findChildByClass(GosuParameterListImpl.class);
    return parameters != null ? parameters.getParameters() : IGosuParameter.EMPTY_ARRAY;
  }

  @Nullable
  public IGosuTypeElement getReturnTypeElementGosu() {
    return findChildByClass(IGosuTypeElement.class);
  }

  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    for (final IGosuTypeVariable typeParameter : getTypeParameters()) {
      if (!processElement(processor, typeParameter, state)) {
        return false;
      }
    }

    for (final IGosuParameter parameter : getParameters()) {
      if (!processElement(processor, parameter, state)) {
        return false;
      }
    }

    processor.handleEvent(DECLARATION_SCOPE_PASSED, this);
    return true;
  }

  public static final PsiScopeProcessor.Event DECLARATION_SCOPE_PASSED = new PsiScopeProcessor.Event() {
  };

  boolean processElement(@NotNull PsiScopeProcessor processor, @NotNull PsiNamedElement namedElement, ResolveState state) {
    NameHint nameHint = processor.getHint(NameHint.KEY);
    String name = nameHint == null ? null : nameHint.getName(state);
    if (name == null || name.equals(namedElement.getName())) {
      return processor.execute(namedElement, state);
    }

    return true;

  }

  @NotNull
  public IGosuMember[] getMembers() {
    return new IGosuMember[]{this};
  }

//  private static final Function<IGosuMethod, PsiType> ourTypesCalculator = new NullableFunction<IGosuMethod, PsiType>()
//  {
//    public PsiType fun( IGosuMethod method )
//    {
//      PsiType nominal = method.getReturnType();
//      if( nominal != null && nominal.equals( PsiType.VOID ) )
//      {
//        return nominal;
//      }
//
//      if( GppTypeConverter.hasTypedContext( method ) )
//      {
//        if( nominal != null )
//        {
//          return nominal;
//        }
//
//        return PsiType.getJavaLangObject( method.getManager(), method.getResolveScope() );
//      }
//
//      PsiType inferred = getInferredType( method );
//      if( nominal == null )
//      {
//        if( inferred == null )
//        {
//          return PsiType.getJavaLangObject( method.getManager(), method.getResolveScope() );
//        }
//        return inferred;
//      }
//      if( inferred != null && inferred != PsiType.NULL )
//      {
//        if( inferred instanceof PsiClassType && nominal instanceof PsiClassType )
//        {
//          final PsiClassType.ClassResolveResult declaredResult = ((PsiClassType)nominal).resolveGenerics();
//          final PsiClass declaredClass = declaredResult.getElement();
//          if( declaredClass != null )
//          {
//            final PsiClassType.ClassResolveResult initializerResult = ((PsiClassType)inferred).resolveGenerics();
//            final PsiClass initializerClass = initializerResult.getElement();
//            if( initializerClass != null &&
//                com.intellij.psi.util.PsiUtil.isRawSubstitutor( initializerClass, initializerResult.getSubstitutor() ) )
//            {
//              if( declaredClass == initializerClass )
//              {
//                return nominal;
//              }
//              final PsiSubstitutor declaredResultSubstitutor = declaredResult.getSubstitutor();
//              final PsiSubstitutor superSubstitutor =
//                TypeConversionUtil.getClassSubstitutor( declaredClass, initializerClass, declaredResultSubstitutor );
//
//              if( superSubstitutor != null )
//              {
//                return JavaPsiFacade.getInstance( method.getProject() ).getElementFactory()
//                  .createType( declaredClass, TypesUtil.composeSubstitutors( declaredResultSubstitutor, superSubstitutor ) );
//              }
//            }
//          }
//        }
//        if( nominal.isAssignableFrom( inferred ) )
//        {
//          return inferred;
//        }
//      }
//      return nominal;
//    }
//
//    @Nullable
//    private PsiType getInferredType( IGosuMethod method )
//    {
//      final GosuOpenBlock block = method.getBlock();
//      if( block == null )
//      {
//        return null;
//      }
//
//      if( GosuPsiManager.isTypeBeingInferred( method ) )
//      {
//        return null;
//      }
//
//      return GosuPsiManager.inferType( method, new MethodTypeInferencer( block ) );
//    }
//  };

  @Nullable
  public PsiType getReturnType() {
    if (isConstructor()) {
      return null;
    } else {
      final IGosuTypeElement element = getReturnTypeElementGosu();
      return element != null ? element.getType() : PsiType.VOID;
    }
  }

  @Nullable
  public IGosuTypeElement setReturnType(@Nullable PsiType newReturnType) {
    //## todo:
    return null;
//    IGosuTypeElement typeElement = getReturnTypeElementGosu();
//    if( newReturnType == null )
//    {
//      if( typeElement != null )
//      {
//        typeElement.delete();
//      }
//      return null;
//    }
//    IGosuTypeElement newTypeElement = GosuPsiElementFactory.getInstance( getProject() ).createTypeElement( newReturnType );
//    if( typeElement == null )
//    {
//      IGosuModifierList list = getModifierList();
//      newTypeElement = (IGosuTypeElement)addAfter( newTypeElement, list );
//    }
//    else
//    {
//      newTypeElement = (IGosuTypeElement)typeElement.replace( newTypeElement );
//    }
//
//    newTypeElement.accept( new GosuRecursiveElementVisitor()
//    {
//      @Override
//      public void visitCodeReferenceElement( IGosuCodeReferenceElement refElement )
//      {
//        super.visitCodeReferenceElement( refElement );
//        PsiUtil.shortenReference( refElement );
//      }
//    } );
//    return newTypeElement;
  }

  @Override
  public ItemPresentation getPresentation() {
    return JavaPresentationUtil.getMethodPresentation(this);
  }

  @Nullable
  public PsiTypeElement getReturnTypeElement() {
    return null;
  }

  @NotNull
  public IGosuParameterList getParameterList() {
    return (IGosuParameterList) findElement(this, GosuElementTypes.ELEM_TYPE_ParameterListClause);
  }

  @NotNull
  public PsiReferenceList getThrowsList() {
    return new ThrowsReferenceList(getManager(), getTextOffset());
  }

  @Nullable
  public PsiCodeBlock getBody() {
    return findChildByClass(PsiCodeBlock.class);
  }

  public boolean isConstructor() {
    PsiIdentifier nameIdentifier = getNameIdentifier();
    if (nameIdentifier == null || nameIdentifier.getText() == null) {
      return true;
    } else {
      return getContainingClass() != null && nameIdentifier.getText().equals(getContainingClass().getName());
    }
  }

  public boolean isVarArgs() {
    final IGosuParameter[] parameters = getParameters();
    return parameters.length > 0 && parameters[parameters.length - 1].isVarArgs();
  }

  @NotNull
  public MethodSignature getSignature(@NotNull PsiSubstitutor substitutor) {
    return MethodSignatureBackedByPsiMethod.create(this, substitutor);
  }

  @Nullable
  public PsiIdentifier getNameIdentifier() {
    PsiIdentifier id = null;
    ASTNode[] children = this.getNode().getChildren(null);
    for (ASTNode child : children) {
      if (child instanceof LeafPsiElement && child.getText().equals("(")) {
        break;
      }
      if( child instanceof GosuCompositeElement &&
          child.getFirstChildNode() instanceof GosuIdentifierImpl &&
          child.getLastChildNode() == child.getFirstChildNode() ) {
        // This can happen when the child is a keyword like "get" where the GosuIdentifierImpl is wrapped in a GosuCompositeElement
        child = child.getFirstChildNode();
      }
      if (child instanceof GosuIdentifierImpl) {
        id = (PsiIdentifier) child;
        // Avoid keeping an id for "get" "set" property qualifiers
        if (!id.getText().equals(Keyword.KW_get.getName()) &&
            !id.getText().equals(Keyword.KW_set.getName())) {
          break;
        }
      }
    }
    if (id == null) {
      return null;
    }
    PsiElement firstChild = id.getFirstChild();
    if (firstChild != null && firstChild instanceof PsiIdentifier) {
      // Always return the leaf token node; we always want to patch in just the name and not mess with upper-level tree nodes
      id = (PsiIdentifier) firstChild;
    }
    return id;
  }

  @NotNull
  public PsiMethod[] findDeepestSuperMethods() {
    return PsiSuperMethodImplUtil.findDeepestSuperMethods(this);
  }

  @NotNull
  public PsiMethod[] findSuperMethods(boolean checkAccess) {
    PsiMethod[] superMethods = PsiSuperMethodImplUtil.findSuperMethods( this, checkAccess );
    if( superMethods.length > 0 ) {
      return superMethods;
    }
    if( !canHaveSuperMethod( checkAccess ) ) {
      return PsiMethod.EMPTY_ARRAY;
    }
    return resolveMethodFromSuperJavaClass();
  }

  // Handle odd cases where param types may not be directly equal in IJ's eyes
  // e.g., Gosu: Collection<Object> Java: Collection<? extends Object>
  private PsiMethod[] resolveMethodFromSuperJavaClass() {
    PsiMethod[] superMethods = PsiMethod.EMPTY_ARRAY;
    IParsedElement pe = getParsedElement();
    if( pe instanceof FunctionStatement ) {
      FunctionStatement funcStmt = (FunctionStatement)pe;
      DynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
      DynamicFunctionSymbol superDfs = dfs.getSuperDfs();
      if( superDfs != null ) {
        IType declaringType = superDfs.getDeclaringTypeInfo().getOwnersType();
        if( IGosuClass.ProxyUtil.isProxy( declaringType ) ) {
          IJavaType javaType = ((IGosuClass)declaringType).getJavaType();
          TypeSystem.pushModule( javaType.getTypeLoader().getModule() );
          try {
            IMethodInfo mi = ((IRelativeTypeInfo)javaType.getTypeInfo()).getMethod( javaType, superDfs.getDisplayName(), superDfs.getArgTypes() );
            superMethods = new PsiMethod[] {(PsiMethod)PsiFeatureResolver.resolveMethodOrConstructor( mi, this )};
          }
          finally {
            TypeSystem.popModule( javaType.getTypeLoader().getModule() );
          }
        }
      }
    }
    return superMethods;
  }

  private boolean canHaveSuperMethod( boolean checkAccess ) {
    if( isConstructor() ) {
      return false;
    }
    if( hasModifierProperty( PsiModifier.STATIC ) ) {
      return false;
    }
    if( checkAccess && hasModifierProperty( PsiModifier.PRIVATE ) ) {
      return false;
    }
    PsiClass parentClass = getContainingClass();
    return parentClass != null && !"java.lang.Object".equals( parentClass.getQualifiedName() );
  }

  @NotNull
  public PsiMethod[] findSuperMethods(PsiClass parentClass) {
    return PsiSuperMethodImplUtil.findSuperMethods(this, parentClass);
  }

  @NotNull
  public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(boolean checkAccess) {
    return PsiSuperMethodImplUtil.findSuperMethodSignaturesIncludingStatic(this, checkAccess);
  }

  @NotNull
  public PsiMethod[] findSuperMethods() {
    return PsiSuperMethodImplUtil.findSuperMethods(this);
  }

  @Nullable
  public PsiMethod findDeepestSuperMethod() {
    final PsiMethod[] methods = findDeepestSuperMethods();
    return methods.length > 0 ? methods[0] : null;
  }

  @NotNull
  public IGosuModifierList getModifierList() {
    return (IGosuModifierList)findChildByClass( PsiModifierList.class );
  }

  public boolean hasModifierProperty(@NonNls @NotNull String name) {
    if (name.equals(PsiModifier.ABSTRACT)) {
      final PsiClass klass = getContainingClass();
      if (klass != null && klass.isInterface()) {
        return true;
      }
    }

    return getModifierList().hasModifierProperty( name );
  }

  @NotNull
  public String getName() {
    // for constructors this method should always return the enclosing type's name (not 'construct')
    // otherwise a class rename will take forever and run out of memory
    if (isConstructor()) {
      PsiClass containingClass = getContainingClass();
      if (containingClass == null) {
        GosuMethodImpl originalConstructor = getUserData(ORIGINAL_CONSTRUCTOR);
        return originalConstructor.getName();
      }
      return containingClass.getName();
    } else {
      return GosuPsiImplUtil.getName(this);
    }
  }

  @NotNull
  public HierarchicalMethodSignature getHierarchicalMethodSignature() {
    try {
      return PsiSuperMethodImplUtil.getHierarchicalMethodSignature(this);
    } catch (IllegalArgumentException e) {
      //ignore it as it will always throw for methods with generics (Equal objects must have equal hashcodes.)
      return new HierarchicalMethodSignatureImpl((MethodSignatureBackedByPsiMethod) getSignature(PsiSubstitutor.EMPTY));
    }
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    PsiIdentifier psiName = getNameIdentifier();
    if (psiName != null) {
      GosuPsiImplUtil.setName(psiName, name);
    }
    return this;
  }

  public boolean hasTypeParameters() {
    return getTypeParameters().length > 0;
  }

  @Nullable
  public IGosuTypeVariableList getTypeParameterList() {
    return findChildByClass(IGosuTypeVariableList.class);
  }

  @NotNull
  public IGosuTypeVariable[] getTypeParameters() {
    final IGosuTypeVariableList list = getTypeParameterList();
    if (list != null) {
      return (IGosuTypeVariable[]) list.getTypeParameters();
    }

    return IGosuTypeVariable.EMPTY_ARRAY;
  }

  public PsiClass getContainingClass() {
    PsiElement parent = getParent();
    if (parent instanceof PsiClass) {
      return (PsiClass) parent;
    }

    final PsiFile file = getContainingFile();
    if (file instanceof IGosuFileBase) {
      return ((IGosuFileBase) file).getPsiClass();
    }

    return null;
  }

  @Nullable
  public PsiDocComment getDocComment() {
    return GosuDocUtil.findDocCommnentNode(getNode());
  }

  public boolean isDeprecated() {
    return GosuPsiImplUtil.isDeprecatedByAnnotation(this) || PsiImplUtil.isDeprecatedByDocTag(this);
  }

  @NotNull
  public SearchScope getUseScope() {
    return com.intellij.psi.impl.PsiImplUtil.getMemberUseScope(this);
  }

  @NotNull
  public PsiElement getOriginalElement() {
    final PsiClass containingClass = getContainingClass();
    if (containingClass == null) {
      return this;
    }
    PsiClass originalClass = (PsiClass) containingClass.getOriginalElement();
    final PsiMethod originalMethod = originalClass.findMethodBySignature(this, false);
    return originalMethod != null ? originalMethod : this;
  }


  public void delete() throws IncorrectOperationException {
    PsiElement parent = getParent();
    if (parent instanceof AbstractGosuClassFileImpl || parent instanceof IGosuTypeDefinition) {
      super.delete();
      return;
    }
    throw new IncorrectOperationException("Invalid enclosing type definition");
  }

  public String[] getNamedParametersArray() {
    return ArrayUtil.EMPTY_STRING_ARRAY;
//    GosuOpenBlock body = getBlock();
//    if( body == null )
//    {
//      return ArrayUtil.EMPTY_STRING_ARRAY;
//    }
//
//    IGosuParameter[] parameters = getParameters();
//    if( parameters.length == 0 )
//    {
//      return ArrayUtil.EMPTY_STRING_ARRAY;
//    }
//    IGosuParameter firstParameter = parameters[0];
//
//    PsiType type = firstParameter.getTypeGosu();
//    IGosuTypeElement typeElement = firstParameter.getTypeElementGosu();
//    //equalsToText can't be called here because of stub creating
//
//    if( type != null && typeElement != null && type.getPresentableText() != null && !type.getPresentableText().endsWith( "Map" ) )
//    {
//      return ArrayUtil.EMPTY_STRING_ARRAY;
//    }
//
//    GosuNamedArgumentSearchVisitor visitor = new GosuNamedArgumentSearchVisitor( firstParameter.getNameIdentifierGosu().getText() );
//
//    body.accept( visitor );
//    return visitor.getResult();
  }

  @Override
  public boolean isForProperty() {
    IFunctionStatement parsedElement = getParsedElement();
    if(parsedElement == null) {
      return false;
    }
    IDynamicFunctionSymbol dynamicFunctionSymbol = parsedElement.getDynamicFunctionSymbol();
    return dynamicFunctionSymbol != null && dynamicFunctionSymbol.getName().startsWith( "@" );
  }

  @Override
  public boolean isForPropertySetter() {
    IFunctionStatement parsedElement = getParsedElement();
    if (parsedElement != null) {
      IParsedElement parent = parsedElement.getParent();
      if (parent instanceof IPropertyStatement) {
        return ((IPropertyStatement) parent).getDps().getSetterDfs() == parsedElement.getDynamicFunctionSymbol();
      }
    }
    return false;
  }

  @Override
  public boolean isForPropertyGetter() {
    IFunctionStatement parsedElement = getParsedElement();
    if (parsedElement != null) {
      IParsedElement parent = parsedElement.getParent();
      if (parent instanceof IPropertyStatement) {
        return ((IPropertyStatement) parent).getDps().getGetterDfs() == parsedElement.getDynamicFunctionSymbol();
      }
    }
    return false;
  }

  public PsiMethodReceiver getMethodReceiver() {
    return null;
  }

  public PsiType getReturnTypeNoResolve() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return PsiClassImplUtil.isMethodEquivalentTo(this, another);
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    Icon methodIcon = hasModifierProperty(PsiModifier.ABSTRACT) ? GosuIcons.ABSTRACT_METHOD : GosuIcons.METHOD;
    RowIcon baseIcon = ElementPresentationUtil.createLayeredIcon(methodIcon, this, false);
    return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
  }

  @Override
  public String toString() {
    return getName();
  }
}
