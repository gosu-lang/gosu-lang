/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.light.LightClassReference;
import com.intellij.psi.impl.source.JavaDummyHolder;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.tree.ChangeUtil;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlText;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.IGosuElementType;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.InjectedElementEditor;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;

public class GosuBaseElementImpl<E extends IParsedElement, T extends StubElement> extends StubBasedPsiElementBase<T> implements IGosuPsiElement {
  private final IElementType _elementType;

  public GosuBaseElementImpl(@NotNull GosuCompositeElement node) {
    super(node);
    _elementType = node.getElementType();
  }

  protected GosuBaseElementImpl(@NotNull T stub, @NotNull IStubElementType elementType) {
    super(stub, elementType);
    _elementType = elementType;
  }

  public IElementType getGosuElementType() {
    return _elementType;
  }

  @Nullable
  public PsiIdentifier getNameIdentifierImpl() {
    final List<ASTNode> children = Arrays.asList(getNode().getChildren(null));
    PsiIdentifier id = getFirst(filter(children, IGosuIdentifier.class), null);
    if (id == null) {
      // Look for a generic identifier to handle cases where we fudge the psi tree e.g., program's synthetic top-level class
      id = findElement(this, PsiIdentifier.class);
    }
    return id;
  }

  @Nullable
  public static PsiElement findElement(@NotNull PsiElement parent, IElementType elementType) {
    if (parent.getNode().getElementType() == elementType) {
      return parent;
    }

    PsiElement child = parent.getFirstChild();
    while (child != null) {
      PsiElement elem = findElement(child, elementType);
      if (elem != null) {
        return elem;
      }
      child = child.getNextSibling();
    }
    return null;
  }

  @Nullable
  public static <P extends PsiElement, C extends Class<P>> P findElement(@NotNull PsiElement parent, @NotNull C classType) {
    if (classType.isAssignableFrom(parent.getNode().getClass())) {
      return (P) parent;
    }

    PsiElement child = parent.getFirstChild();
    while (child != null) {
      final PsiElement elem = findElement(child, classType);
      if (elem != null) {
        return (P) elem;
      }
      child = child.getNextSibling();
    }
    return null;
  }

  public static void findElements(@NotNull PsiElement parent, IElementType elementType, List<PsiElement> list) {
    if (parent.getNode().getElementType() == elementType) {
      list.add(parent);
    }
    PsiElement child = parent.getFirstChild();
    while (child != null) {
      findElements(child, elementType, list);
      child = child.getNextSibling();
    }
  }

  public void removeElements(@NotNull PsiElement[] elements) throws IncorrectOperationException {
    ASTNode parentNode = getNode();
    for (PsiElement element : elements) {
      if (element.isValid()) {
        ASTNode node = element.getNode();
        if (node == null || node.getTreeParent() != parentNode) {
          throw new IncorrectOperationException();
        }
        parentNode.removeChild(node);
      }
    }
  }

  public void removeStatement() throws IncorrectOperationException {
    if (getParent() == null ||
        getParent().getNode() == null) {
      throw new IncorrectOperationException();
    }
    ASTNode parentNode = getParent().getNode();
    ASTNode prevNode = getNode().getTreePrev();
    parentNode.removeChild(this.getNode());
    if (prevNode != null && GosuTokenSets.SEPARATORS.contains(prevNode.getElementType())) {
      parentNode.removeChild(prevNode);
    }
  }

  @Override
  public PsiElement getParent() {
    return getParentByStub();
  }

  @NotNull
  public <T extends IGosuStatement> T replaceWithStatement(@NotNull T newStmt) {
    PsiElement parent = getParent();
    if (parent == null) {
      throw new PsiInvalidElementAccessException(this);
    }
    return (T) replace(newStmt);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitElement(this);
  }

  public void acceptChildren(GosuElementVisitor visitor) {
    PsiElement child = getFirstChild();
    while (child != null) {
      if (child instanceof IGosuPsiElement) {
        ((IGosuPsiElement) child).accept(visitor);
      }

      child = child.getNextSibling();
    }
  }

  @Nullable
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    CompositeElement treeElement = calcTreeElement();
    assert treeElement.getTreeParent() != null;
    CheckUtil.checkWritable(this);
    TreeElement elementCopy = ChangeUtil.copyToElement(newElement);
    treeElement.getTreeParent().replaceChildInternal(treeElement, elementCopy);
    elementCopy = ChangeUtil.decodeInformation(elementCopy);
    PsiElement elem = SourceTreeToPsiMap.treeElementToPsi(elementCopy);
    if (elem.getContainingFile() instanceof AbstractGosuClassFileImpl) {
      ((AbstractGosuClassFileImpl) elem.getContainingFile()).reparseGosuFromPsi();
    }
    return elem;
  }

  @NotNull
  protected CompositeElement calcTreeElement() {
    return getNode();
  }

  @NotNull
  public GosuCompositeElement getNode() {
    return (GosuCompositeElement) super.getNode();
  }

  @Nullable
  public E getParsedElement() {
    return (E) getParsedElementImpl();
  }

  @Nullable
  public IParsedElement getParsedElementImpl() {
    GosuCompositeElement node = getNode();
    final FileElement fileElement = TreeUtil.getFileElement(node);
    if (fileElement == null) {
      throw new RuntimeException("No file element found");
    }

    final PsiElement plainPsiFile = fileElement.getPsi();
    if (plainPsiFile instanceof JavaDummyHolder) {
      return null;
    }

    final AbstractGosuClassFileImpl psiFile = (AbstractGosuClassFileImpl) plainPsiFile;
    final GosuClassParseData parseData = psiFile.getParseData();

    final String fileSource = GosuPsiImplUtil.getFileSource(psiFile);
    String parsedSource = parseData.getSource();

    if (!fileSource.equals(parsedSource)) {
      psiFile.reparseGosuFromPsi(); // In the case of obsolete parse results
      parsedSource = parseData.getSource();
    }

    final IClassFileStatement classFileStatement = parseData.getClassFileStatement();
    if (classFileStatement == null) {
      if (!GosuPsiParseUtil.TRANSIENT_PROGRAM.equals(psiFile.getName())) {
        throw new RuntimeException("No latest parse information found for " + psiFile.getName());
      }
    } else {
      verifyFileSource(fileSource, parsedSource);
      final PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(psiFile.getProject()).getInjectionHost(psiFile);

      TextRange range = node.getTextRange();
      boolean skip = false;
      if (host instanceof XmlAttributeValue) {
        String hostText = host.getText();
        String uAttribute = parsedSource;
        String uFrag = InjectedLanguageManager.getInstance(psiFile.getProject()).getUnescapedText(node.getPsi());
        String eFrag = node.getText();
        String eAttribute = hostText.substring(1, hostText.length() - 1);
        TextRange newRange = adjustRange(uAttribute, uFrag, eAttribute, eFrag, range);

        if (!range.equals(newRange)) {
          skip = true;
          range = newRange;
        }
      }

      if (host instanceof XmlText) {
        String hostText = host.getText();
        String uAttribute = parsedSource;
        String uFrag = InjectedLanguageManager.getInstance(psiFile.getProject()).getUnescapedText(node.getPsi());
        String eFrag = node.getText();
        String eAttribute = hostText.replace("<![CDATA[", "").replace("]]>", "");
        TextRange newRange = adjustRange(uAttribute, uFrag, eAttribute, eFrag, range);

        if (!range.equals(newRange)) {
          skip = true;
          range = newRange;
        }
      }

      final IParseTree location = classFileStatement.getLocation().getMatchingElement(range.getStartOffset(), range.getLength());
      if (location != null) {
        final IParsedElement element = getTheRightOne(location);
        if (element != null) {
          if (!skip) {
            verifyPsiText(parsedSource, location);
          }
          return element;
        }
      }
    }

    return null;
  }

  private TextRange adjustRange(String uAttribute,  String uFrag,  String eAttribute, String eFrag, TextRange eRange) {
    if (uAttribute.equals(eAttribute) || eRange.getLength() == 0) {
      return eRange;
    }
    int num = 0;
    int i = 0;

    //count how many times we meet eFrag in eAttribute before eRange.startOffset
    while(true) {
      i = eAttribute.indexOf(eFrag, i);
      num++;
      if (i == -1 || i == eRange.getStartOffset()) {
        break;
      }
      i += eFrag.length();

    }
    int j = 0;
    i = 0;
    while(j < num) {
      i = uAttribute.indexOf(uFrag, i);
      i += uFrag.length();
      j++;
    }
    return new TextRange(i-uFrag.length(), i);
  }

  @Nullable
  private E getTheRightOne(@NotNull IParseTree location) {
    final Class<? extends IParsedElement> peType = getParsedElementType();
    final IParsedElement parsedElement = location.getParsedElement();
    if (peType == null || peType.isAssignableFrom(parsedElement.getClass())) {
      return (E) parsedElement;
    }

    // Drill down, checking the first child until we find the element that matches the footprint and is the same type
    List<IParseTree> children = location.getChildren();
    while (!children.isEmpty()) {
      final IParseTree child = children.get(0);
      if (child.getOffset() == location.getOffset() && child.getExtent() == location.getExtent()) {
        final IParsedElement pe = child.getParsedElement();
        if (peType.isAssignableFrom(pe.getClass())) {
          return (E) pe;
        } else {
          children = child.getChildren();
        }
      } else {
        return null;
      }
    }
    return (E) parsedElement;
  }

  private Class<? extends IParsedElement> getParsedElementType() {
    if (_elementType instanceof IGosuElementType) {
      return ((IGosuElementType) _elementType).getParsedElementType();
    }

    throw new RuntimeException("Unknown element type: " + _elementType.getClass().getName());
  }

  private void verifyFileSource(@NotNull String fileSource, String parsedSource) {
    if (!fileSource.equals(parsedSource)) {
      ExceptionUtil.showDiffWindow(fileSource, parsedSource, "File Source", "Parsed Source", "File source does not match parsed source", getProject());
    }
  }

  private void verifyPsiText(@NotNull String parsedSource, @NotNull IParseTree location) {
    final String psiText = getText();
    final String parsedText = parsedSource.substring(location.getOffset(), location.getExtent() + 1);
    if (!psiText.equals(parsedText)) {
      ExceptionUtil.showDiffWindow(psiText, parsedText, "Psi Text", "Parsed Text", "Psi text does not match parsed text", getProject());
    }
  }

  private IGosuClass getTopLevelClass(@NotNull IParsedElement pe) {
    IGosuClass gsClass = pe.getGosuClass();
    while (gsClass.getEnclosingType() != null) {
      gsClass = (IGosuClass) gsClass.getEnclosingType();
    }
    return gsClass;
  }

  @NotNull
  private T findCorrespondingElementInClassFileStmt(IClassFileStatement cfs, @NotNull IParsedElement pe, @NotNull List<Integer> path) {
    if (pe instanceof IClassFileStatement) {
      IParsedElement csr = cfs;
      for (Integer i : path) {
        csr = csr.getLocation().getChildren().get(i).getParsedElement();
      }
      return (T) csr;
    }

    List<IParseTree> children = pe.getParent().getLocation().getChildren();
    path.add(0, children.indexOf(pe.getLocation()));
    return findCorrespondingElementInClassFileStmt(cfs, pe.getParent(), path);
  }

  public String toString() {
    Class<? extends IParsedElement> elemType = ((IGosuElementType) _elementType).getParsedElementType();
    return elemType == null ? "Unhandled" : elemType.getSimpleName();
  }

  @Nullable
  public PsiType createType(IType type) {
    return createType(type, this);
  }

  @Nullable
  public static PsiType createType(@Nullable IType type, @Nullable PsiElement context) {
    if (type == null) {
      return null;
    }

    if( context == null ) {
      return null;
    }

    if (type instanceof IMetaType) {
      type = ((IMetaType) type).getType();
    }

    final PsiElementFactory factory = JavaPsiFacadeUtil.getElementFactory(context.getProject());


    IModule mod = GosuModuleUtil.findModuleForPsiElement( context );
    TypeSystem.pushModule( mod );
    try {
      if (type.getName().equals(TypeSystem.getDefaultType().getType().getName())) {
        final PsiElement psiClass = PsiTypeResolver.resolveType(type, context);
        return psiClass instanceof PsiClass ? factory.createType((PsiClass) psiClass) : null;
      }

      if (type.isPrimitive()) {
        return factory.createPrimitiveType(type.getName());
      }

      if (type.isArray()) {
        return new PsiArrayType(createType(type.getComponentType(), context));
      }

      if (type.isParameterizedType()) {
        final IType[] parameters = type.getTypeParameters();
        final PsiType[] psiParameters = new PsiType[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
          psiParameters[i] = createType(parameters[i], context);
        }

        final PsiElement psiClass = PsiTypeResolver.resolveType(type.getGenericType(), context);
        return psiClass instanceof PsiClass ? factory.createType((PsiClass) psiClass, psiParameters) : null;
      }

      final String qualifiedName = type.getName();
      final GlobalSearchScope scope = GlobalSearchScope.allScope(context.getProject());

      if (type instanceof IBlockType) {
        GosuBlockLightClassReference reference = new GosuBlockLightClassReference(context.getManager(), qualifiedName, qualifiedName, scope);
        return new GosuPsiClassReferenceType(reference, null);
      }

      if (context instanceof PsiIdentifier) {
        final String shortName = PsiNameHelper.getShortClassName(qualifiedName);
        GosuLightClassReference referenceElement = new GosuLightClassReference(
            context.getManager(), shortName, qualifiedName, scope, (GosuIdentifierImpl) context);
        return new GosuPsiClassReferenceType(referenceElement, null);
      } else {
        final String shortName = PsiNameHelper.getShortClassName(qualifiedName);
        LightClassReference reference = new LightClassReference(context.getManager(), shortName, qualifiedName, scope);
        return new GosuPsiClassReferenceType(reference, null);
      }
    }
    finally {
      TypeSystem.popModule( mod );
    }
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    return InjectedElementEditor.getOriginalElement(this);
  }
}
