/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.google.common.base.Strings;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.ClassUtil;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.IncorrectOperationException;
import gw.fs.IFile;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.*;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.SymbolNotFoundException;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.GosuClassNameInsertHandler;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.formatting.GosuLanguageCodeStyleSettingsProvider;
import gw.plugin.ij.intentions.IntentionsFilter;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.stubs.elements.GosuStubFileElementType;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.InjectedElementEditor;
import gw.util.fingerprint.FP64;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.google.common.collect.Iterables.filter;

public abstract class AbstractGosuClassFileImpl extends PsiFileBase implements IGosuFileBase, PsiClassOwnerEx, IGosuFile, IntentionFilterOwner {
  private static final Logger LOG = Logger.getInstance(AbstractGosuClassFileImpl.class);
  private boolean _bReparse;
  private IntentionActionsFilter myIntentionActionsFilter;

//  private GosuClassParseData parseData;

  public AbstractGosuClassFileImpl(@NotNull FileViewProvider viewProvider, @NotNull Language language) {
    super(viewProvider, language);
    myIntentionActionsFilter = new IntentionsFilter();
  }

  @NotNull
  public FileType getFileType() {
    return getViewProvider().getVirtualFile().getFileType();
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitElement(this);
  }

  public void acceptChildren(@NotNull GosuElementVisitor visitor) {
    PsiElement child = getFirstChild();
    while (child != null) {
      if (child instanceof IGosuPsiElement) {
        ((IGosuPsiElement) child).accept(visitor);
      }

      child = child.getNextSibling();
    }
  }

  public IGosuTypeDefinition[] getTypeDefinitions() {
    final StubElement<?> stub = getStub();
    if (stub != null) {
      return stub.getChildrenByType(GosuElementTypes.TYPE_DEFINITION_TYPES, IGosuTypeDefinition.ARRAY_FACTORY);
    } else {
      return calcTreeElement().getChildrenAsPsiElements(GosuElementTypes.TYPE_DEFINITION_TYPES, IGosuTypeDefinition.ARRAY_FACTORY);
    }
  }

  @NotNull
  public PsiClass[] getClasses() {
    try {
      return getTypeDefinitions();
    } catch (ProcessCanceledException e) {
      throw e;
    } catch (Throwable e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw new ProcessCanceledException(e.getCause());
      } else {
        LOG.error(e);
        return PsiClass.EMPTY_ARRAY;
      }
    }
  }

  @Nullable
  public PsiClass getPsiClass() {
    final PsiClass[] classes = getClasses();
    return classes.length > 0 ? classes[0] : null;
  }

  @Override
  public PsiModifierList getModifierList() {
    return null;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    return false;
  }

  protected boolean isForComplection(@NotNull CharSequence contents) {
    final int offset = contents.toString().indexOf(CompletionInitializationContext.DUMMY_IDENTIFIER);
    return offset >= 0;
  }

  protected IGosuParser createParser(CharSequence contents) {
    final IGosuParser parser = GosuParserFactory.createParser(TypeSystem.getCompiledGosuClassSymbolTable(), ScriptabilityModifiers.SCRIPTABLE);
    parser.setThrowParseExceptionForWarnings(true);
    parser.setDontOptimizeStatementLists(true);
    parser.setWarnOnCaseIssue(true);
    parser.setEditorParser(true);
    parser.setScript(contents);
    return parser;
  }

  @Nullable
  protected SymbolNotFoundException getExceptionWithSymbolTable(@NotNull ParseResultsException parseException) {
    for (SymbolNotFoundException e : filter(parseException.getParseExceptions(), SymbolNotFoundException.class)) {
      if (e.getSymbolName().endsWith(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED)) {
        return e;
      }
    }
    return null;
  }

  // "package" + "." + "class"
  public String getQualifiedClassNameFromFile() {
    VirtualFile psiFile = FileUtil.getFileFromPsi(this);
    if( psiFile == null ) {
      return "#Err#NullFile";
    }
    IModule module = getModule();
    if( module == null ) {
      return "Err#NullModule";
    }
    return FileUtil.getSourceQualifiedName(psiFile, getModule());
  }

  // TODO: merge into getModule() ?
  public IModule getModuleForPsi() {
    PsiFile targetFile = this;

    // Special case for code injection
    final FileViewProvider provider = getViewProvider();
    if (provider instanceof InjectedFileViewProvider) {
      VirtualFile file = provider.getVirtualFile();
      if (file instanceof VirtualFileWindow) {
        file = FileUtil.getOriginalFile((VirtualFileWindow) file);
      }
      targetFile = PsiManager.getInstance(getProject()).findFile(file);
    }

    IModule module = GosuModuleUtil.findModuleForPsiElement(targetFile);
    if (module == null) {
      final VirtualFile file = FileUtil.getFileFromPsi(targetFile);
      if (file != null) {
        module = GosuModuleUtil.findModuleForFile(file, getProject());
      }
    }
    return module;
  }

  public IModule getModule() {
    final IModule module = getModuleForPsi();

    if (module == null) {
      final VirtualFile vfile = getVirtualFile();
      if (getName().equals(GosuLanguageCodeStyleSettingsProvider.FORMATTING_FILE_NAME) &&
          (vfile == null || vfile instanceof LightVirtualFile)) {
        return GosuModuleUtil.getGlobalModule(getProject()); // this is the code style class case
      } else {
        // throw new IllegalStateException("Module should not be null");
        return GosuModuleUtil.getGlobalModule(getProject()); // TODO: For code fragment editor (it contains a copy of injected code fragment)
      }
    }

    return module;
  }

  public abstract ASTNode parse(ASTNode chameleon);

  public final IGosuClass parseType(boolean forceRefresh) {
    final String strClassName = getQualifiedClassNameFromFile();
    final String contents = (this instanceof GosuProgramFileImpl) ? GosuPsiImplUtil.getFileSource(this) : getViewProvider().getContents().toString();
    final int completionMarkerOffset = contents.indexOf(CompletionInitializationContext.DUMMY_IDENTIFIER);
    final boolean isForCompletion = completionMarkerOffset >= 0;

//    long t1 = System.nanoTime();
    try {
      IGosuClass gsClass;
      if (!isForCompletion) {
        final IModule module = getModule();
        TypeSystem.pushModule(module);
        TypeSystem.lock();
        try {
          IType type = TypeSystem.getByFullNameIfValid(strClassName, module);
          if (type != null) {
            gsClass = (IGosuClass) type;
            // if the class is not compiled yet gsClass.getSourceFingerprint() will return 0 and we will NOT refresh the type
            // if the class is already compiled but it is old, we will refresh the type first
            final long contentsFingerPrint = new FP64(contents).getRawFingerprint();
            if (forceRefresh || (contentsFingerPrint != ((IGosuClassInternal)gsClass).getParseInfo().getSourceFingerprint())) {
              refreshFile(gsClass);
            }
            gsClass.setCreateEditorParser(true);
            try {
              gsClass.isValid();
            } finally {
              gsClass.setCreateEditorParser(false);
            }
            if (contentsFingerPrint == ((IGosuClassInternal)gsClass).getParseInfo().getSourceFingerprint()) {
              saveParseData(contents, gsClass);
//            System.out.println("Class source: " + gsClass.getSource());
              return gsClass;
            }
          }
        } finally {
          TypeSystem.unlock();
          TypeSystem.popModule(module);
        }
      }
      return parseType(strClassName, contents, completionMarkerOffset);
    } finally {
//      System.out.println((isForCompletion ? "Compl. parse: " : "Normal parse: ") + "(" + forceRefresh + ")" + "" + ", in: " + (System.nanoTime() - t1) * 1e-6 + "ms");
    }
  }

  private void refreshFile( IGosuClass gsClass ) {
    IFile[] sourceFiles = gsClass.getSourceFiles();
    if( sourceFiles.length > 0 ) {
      for( IFile file: sourceFiles ) {
        TypeLoaderAccess.instance().refreshed( file, null, RefreshKind.MODIFICATION);
      }
    }
    else {
      TypeSystem.refresh( (ITypeRef)gsClass);
    }
  }

  private void saveParseData(@NotNull String contents, @NotNull IGosuClass gsClass) {
    if (getOriginalFile().getVirtualFile() != null) {
      final GosuClassParseData parseData = getParseData();
      if (parseData != null) {
        parseData.setClassFileStatement(gsClass.getClassStatement().getClassFileStatement());
        parseData.setSource(contents);
      }
    }
  }

  protected abstract IGosuClass parseType(String strClassName, String contents, int completionMarkerOffset);

  protected void refreshTheOldType() {
    if (!DumbService.isDumb(getProject())) {
      TypeSystem.refreshed(getFilePath());
    }
  }

  @Nullable
  protected IDEAFile getFilePath() {
    final VirtualFile file = FileUtil.getFileFromPsi(this);
    return file != null ? FileUtil.toIFile(file) : null;
  }

  @Nullable
  public String getFileExtension() {
    final VirtualFile file = FileUtil.getFileFromPsi(this);
    return file.getExtension();
  }

  public String toString() {
    return String.format( "%s [%s]", getClass().getName(), getQualifiedClassNameFromFile() );
  }

  // PsiClassOwner
  @NotNull
  public String getPackageName() {
    // TODO: use stab to get package name
    return ClassUtil.extractPackageName( getQualifiedClassNameFromFile() );
  }

  public void setPackageName(@NotNull String packageName) throws IncorrectOperationException {
    final IModule module = getModule();
    TypeSystem.pushModule(module);
    try {
      final ASTNode node = getNode();
      final ASTNode packageNode = calcTreeElement().findChildByType(GosuElementTypes.ELEM_TYPE_NamespaceStatement);
      final IGosuPackageDefinition currentPackage = packageNode != null ? (IGosuPackageDefinition) packageNode.getPsi() : null;
      if (Strings.isNullOrEmpty(packageName)) {
        // Empty name
        if (currentPackage != null) {
          node.removeChild(currentPackage.getNode());
        }
      } else {
        // Non-empty name
        redefineImports(packageName);
        final PsiElement newPackage = GosuPsiParseUtil.parsePackageStatement(packageName, this);
        final ASTNode newNode = newPackage.getNode();
        if (currentPackage != null) {
          if (!packageName.equals(currentPackage.getPackageName())) {
            final ASTNode currNode = currentPackage.getNode();
            CodeEditUtil.setOldIndentation((TreeElement) newNode, 0); // this is to avoid a stupid exception
            node.replaceChild(currNode, newNode);
          }
        } else {
          final ASTNode anchor = node.getFirstChildNode();
          node.addChild(newNode, anchor);
          node.addLeaf(GosuTokenTypes.TT_WHITESPACE, "\n", anchor);
        }
      }
      reparseAfterSetPackageName();
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private void redefineImports(String packageName) {
    List<PsiElement> typeLiterals = findTypeLiterals();
    if (typeLiterals.isEmpty()) {
      return;
    }
    IGosuUsesStatementList oldUsesList = findUsesStatementList();
    final StringBuilder sb = new StringBuilder();
    Set<String> types = new HashSet<>();
    for (PsiElement typeLiteral : typeLiterals) {
      GosuTypeLiteralImpl gsTypeLiteral = (GosuTypeLiteralImpl) typeLiteral;
      String relativeStr = gsTypeLiteral.getText();
      if (!relativeStr.startsWith(packageName)) {
        ITypeLiteralExpression parsedElement = gsTypeLiteral.getParsedElement();
        if(parsedElement == null) {
          continue;
        }

        IType type = parsedElement.getType().getType();
        if( type.getEnclosingType() == null) {
          String namespace = type.getNamespace();
          if (!(type instanceof ErrorType) &&
              !type.isArray() &&
              namespace != null &&
              !namespace.equals(packageName)) {

            String fqn = type.getName();
            //fqn is used
            if (relativeStr.equals(fqn)) {
              continue;
            }
            if (isTypeVisibleInsideFile(fqn, relativeStr)) {
              continue;
            }
            String name = TypeLord.getPureGenericType(type).getName();
            if (!types.add(name)) {
              sb.append(String.format("uses %s\n", name));
            }
          }
        }
      }
    }
    final String usesList = sb.toString();
    IGosuUsesStatementList newUsesList = !usesList.isEmpty() ? GosuPsiParseUtil.parseUsesList(sb.toString(), this) : null;
    if (oldUsesList == null && newUsesList != null) {
      addAfter(newUsesList, findChildByClass(IGosuPackageDefinition.class));

    } else {
      if (newUsesList != null) {
        oldUsesList.replace(newUsesList);
      } else if (oldUsesList != null) {
        oldUsesList.removeStatement();
      }
    }
  }

  private boolean isTypeVisibleInsideFile(String fqn, String relativeStr){
    GosuClassParseData parseData = getParseData();
    if (parseData != null) {
      IClassFileStatement classFileStatement = parseData.getClassFileStatement();
      if (classFileStatement != null) {
        IGosuClass gosuClass = classFileStatement.getGosuClass();
        if (gosuClass != null) {
          if(GosuClassNameInsertHandler.resolveRelativeTypeInParser(fqn, relativeStr, gosuClass) ) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private List<PsiElement> findTypeLiterals() {
    List<PsiElement> list = new ArrayList<>();
    GosuBaseElementImpl.findElements(this, GosuElementTypes.ELEM_TYPE_TypeLiteral, list);
    return list;
  }

  protected void reparseAfterSetPackageName() {
    reparseGosuFromPsi();
  }

  // PsiClassOwnerEx
  @Override
  public Set<String> getClassNames() {
    final VirtualFile file = getVirtualFile();
    return file == null ? Collections.<String>emptySet() : Collections.singleton(file.getNameWithoutExtension());
  }

  // IGosuFile
  @Override
  public void addImport(String qualifiedName) {
    Boolean singleLine = getUserData( InjectedElementEditor.SINGLE_LINE_EDITOR );
    if (singleLine != null && singleLine) {
      return;
    }
    final PsiElement uses = GosuPsiParseUtil.parseImport(qualifiedName, this);
    final IGosuUsesStatementList usesList = findUsesStatementList();
    if (usesList != null) {
      usesList.add(uses);
    } else {
      addAfter(uses, findChildByClass(IGosuPackageDefinition.class));
    }

    // Do we need reparseFromPsi() here?
  }

  public IGosuUsesStatementList findUsesStatementList() {
    IGosuUsesStatementList usesStmtList = findChildByClass( IGosuUsesStatementList.class );
    if( usesStmtList == null ) {
      PsiElement firstChild = getFirstChild();
      if( firstChild instanceof IGosuClassDefinition ) {
        for( PsiElement child : firstChild.getChildren() ) {
          if( child instanceof IGosuUsesStatementList ) {
            usesStmtList = (IGosuUsesStatementList) child;
            break;
          }
        }
      }
    }
    return usesStmtList;
  }

  public GosuClassParseData getParseData() {
//    if (parseData == null) {
      return GosuClassParseDataCache.getParseData(null, getOriginalFile().getVirtualFile(), getModule());
//    }
//    return parseData;
  }

  /**
   * Call after manipulating PSI so that Gosu parse tree results attached to the psi file are in sync
   */
  public IGosuClass reparseGosuFromPsi() {
    return parseType(true);
  }

  /**
   * If need be force the PSI to reparse from the psiFile's existing content
   */
  public boolean reparsePsiFromContent() {
    if( !reparsePsiFromContentIfNecessary() ) {
      if( needsGosuReparsing() ) {
        reparseGosuFromPsi();
      }
      return false;
    }
    return true;
  }

  private boolean reparsePsiFromContentIfNecessary() {
    if( needsPsiReparse() ) {
      if( EventQueue.isDispatchThread() ) {
        reloadPsi();
      }
      else {
        EventQueue.invokeLater(
          new Runnable() {
            public void run() {
              reloadPsi();
            }
          } );
      }
      return true;
    }
    return false;
  }

  private boolean needsPsiReparse() {
    PsiElement firstChild = getFirstChild();
    if(firstChild == null) {
      return false;
    }
    Boolean isRawText = firstChild.getUserData( GosuStubFileElementType.KEY_RAW_TEXT );
    return isRawText != null && isRawText;
  }

  private boolean needsGosuReparsing() {
    if( isReparse() ) {
      setReparse( false );
      return false;
    }
    AbstractGosuClassFileImpl origFile = (AbstractGosuClassFileImpl)getOriginalFile();
    if( origFile != null && origFile.isReparse() ) {
      origFile.setReparse( false );
      return false;
    }
    return true;
  }

  private void reloadPsi() {
    ApplicationManager.getApplication().runWriteAction(
      new Runnable() {
        public void run() {
          // Mark this file so that it reparses
          ((AbstractGosuClassFileImpl)getOriginalFile()).setReparse( true );
          // Force this file to reload its contents which in turn leads to a reparse
          onContentReload();
        }
      } );
  }

  public void setReparse( boolean bReparse ) {
    _bReparse = bReparse;
  }
  public boolean isReparse() {
    return _bReparse;
  }

  // PsiImportHolder
  public boolean importClass(@NotNull PsiClass psiClass) {
    addImport(psiClass.getQualifiedName());
    return true;
  }

  protected IGosuClass parseGosuClassDirectly(String strClassName, String contents, int completionMarkerOffset, ClassType classType) {
    long t1 = System.nanoTime();
    final IModule module = getModule();
    TypeSystem.pushModule(module);
    // DP: this line should execute outside the TS lock to avoid deadlock
    final IGosuParser parser = createParser(contents);
    TypeSystem.lock();
    try {
      if (completionMarkerOffset >= 0) {
        parser.setSnapshotSymbols();
      }

      refreshTheOldType();
      IGosuClass gsClass = null;
      ISymbolTable snapshotSymbols = null;
      IType ctxType = null;
      try {
        final ISourceFileHandle sourceFileHandle = new StringSourceFileHandle(strClassName, contents, getFilePath(), false, classType);
        gsClass = parser.parseClass(strClassName, sourceFileHandle, true, true);
      } catch (ParseResultsException e) {
        if (e.getParsedElement() instanceof IClassFileStatement) {
          IClassFileStatement classFileStmt = (IClassFileStatement) e.getParsedElement();
          if (classFileStmt.getClassStatement() != null) {
            gsClass = classFileStmt.getClassStatement().getGosuClass();
          } else {
            gsClass = classFileStmt.getGosuClass();
          }
        }

        final SymbolNotFoundException issue = getExceptionWithSymbolTable(e);
        if (issue != null) {
          snapshotSymbols = issue.getSymbolTable();
          ctxType = issue.getExpectedType();
        }
      }

      if (getOriginalFile().getVirtualFile() != null) {
        GosuClassParseData parseData = getParseData();
        if(parseData != null) {
          parseData.setClassFileStatement(gsClass.getClassStatement().getClassFileStatement());
          parseData.setSource(contents);
          if (completionMarkerOffset >= 0) {
            parseData.setSnapshotSymbols(snapshotSymbols);
            parseData.setContextType(ctxType);
          }
        }
      }
      return gsClass;
    } finally {
      TypeSystem.unlock();
      TypeSystem.popModule( module );
      boolean compl = completionMarkerOffset >= 0;
//      System.out.println("\t" + strClassName + " - " + (System.nanoTime() - t1) * 1e-6 + " (direct) " + compl);
    }
  }

  @Override
  public void setIntentionActionsFilter(final IntentionActionsFilter filter) {
    myIntentionActionsFilter = filter;
  }

  @Override
  public IntentionActionsFilter getIntentionActionsFilter() {
    return myIntentionActionsFilter;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitFile(this);
    }
    else {
      visitor.visitFile( this );
    }
  }

  @Nullable
  @Override
  public IParsedElement getParsedElement() {
    PsiElement element = getFirstChild();
    while (!(element instanceof IGosuPsiElement) && element != null) {
      element = element.getNextSibling();
    }
    if (element != null) {
      return ((IGosuPsiElement) element).getParsedElement();
    }
    return null;
  }
}
