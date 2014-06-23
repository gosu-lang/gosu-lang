/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.file.impl.FileManager;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.LightVirtualFile;
import gw.internal.gosu.parser.ContextSensitiveCodeRunner;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.handlers.AbstractCompletionHandler;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.impl.types.CompletionVoter;
import gw.plugin.ij.lang.psi.stubs.elements.GosuStubFileElementType;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GosuFragmentFileImpl extends GosuProgramFileImpl implements JavaCodeFragment, CompletionVoter {
  public static final Key<PsiElement> PSI_CONTEXT = Key.create("PSI CONTEXT");
  public static final Key<String> FILE_NAME = Key.create("FRAGMENT_FILE_NAME");

  private PsiElement _psiCtx;
  private boolean _bPhysical;
  private FileViewProvider _viewProvider;
  private LinkedHashMap<String, String> _pseudoImports;
  private ExceptionHandler _exceptionHandler;

  public GosuFragmentFileImpl( Project project, String strSource, PsiElement psiCtx ) {
    this(project, strSource, makeName(psiCtx), psiCtx);
  }

  public GosuFragmentFileImpl( Project project, String strSource, String name, PsiElement psiCtx ) {
    super( ((PsiManagerEx)PsiManager.getInstance( project )).getFileManager().createFileViewProvider(
      new LightVirtualFile( name, GosuCodeFileType.INSTANCE, strSource ), true ) );
    _psiCtx = psiCtx;
    _bPhysical = true;
    _pseudoImports = new LinkedHashMap<>();
    addImportsFromMap( psiCtx );
    setContentElementType( new FragmentElementType() );
    ((SingleRootFileViewProvider)getViewProvider()).forceCachedPsi( this );
  }

  public GosuFragmentFileImpl( FileViewProvider viewProvider ) {
    super( viewProvider );
    String name = viewProvider.getVirtualFile().getName();
    StringTokenizer tokenizer = new StringTokenizer( name, "#" );
    String strGosuFrag = tokenizer.nextToken();
    String strEnclosingClass = tokenizer.nextToken();
    String strOffset = tokenizer.nextToken();

    PsiClass psiClass = PsiTypeResolver.resolveType( strEnclosingClass, this );
    int iOffset = Integer.parseInt( strOffset );
    _psiCtx = psiClass.findElementAt( iOffset );
  }

  protected GosuFragmentFileImpl clone() {
    FileElement treeClone = (FileElement)calcTreeElement().clone();
    GosuFragmentFileImpl clone = (GosuFragmentFileImpl)cloneImpl( treeClone );
//    if( getTreeElement() != null ) {
//      treeClone.setPsi( clone );
//    }
    clone.myOriginalFile = this;
    clone._bPhysical = false;
    clone._pseudoImports = new LinkedHashMap<>( _pseudoImports );
    clone._psiCtx = _psiCtx;
    FileManager fileManager = ((PsiManagerEx)getManager()).getFileManager();
    SingleRootFileViewProvider cloneViewProvider = (SingleRootFileViewProvider)fileManager.createFileViewProvider( new LightVirtualFile(
      getName(),
      getLanguage(),
      getText() ), false );
    cloneViewProvider.forceCachedPsi( clone );
    clone._viewProvider = cloneViewProvider;
    return clone;
  }

  @Override
  public boolean isPhysical() {
    return _bPhysical;
  }

  @Override
  @NotNull
  public FileViewProvider getViewProvider() {
    return _viewProvider != null
           ? _viewProvider
           : super.getViewProvider();
  }

  public GosuClassParseData getParseData() {
    return GosuClassParseDataCache.getParseData(null, getOriginalFile().getViewProvider().getVirtualFile(), getModule());
  }

  @Override
  public VirtualFile getVirtualFile() {
    return getViewProvider().getVirtualFile();
  }

  private void addImportsFromMap( PsiElement psiCtx ) {
    if( psiCtx == null ) {
      return;
    }
    IGosuClass gsClass = getGosuClassFromCtx( psiCtx );
    if( gsClass != null ) {
      ITypeUsesMap ctxTypeUsesMap = getTopLevelClass( gsClass ).getTypeUsesMap();
      if( ctxTypeUsesMap != null ) {
        for( String qName : (Set<String>)ctxTypeUsesMap.getTypeUses() ) {
          String shortClassName = PsiNameHelper.getShortClassName( qName );
          _pseudoImports.put( shortClassName.isEmpty() ? qName : shortClassName, qName );
        }
      }
    }
  }

  private IGosuClass getTopLevelClass( IGosuClass gsClass ) {
    return ((IGosuClass)TypeLord.getOuterMostEnclosingClass( gsClass ));
  }

  private static IGosuClass getGosuClassFromCtx( PsiElement psiCtx ) {
    if (psiCtx == null) {
      return null;
    }
    PsiClass psiClass = PsiTreeUtil.getParentOfType( psiCtx, PsiClass.class, false );
    while ( psiClass == null && psiCtx.getParent() != null ) {
      psiCtx = psiCtx.getParent().getContext();
      psiClass = PsiTreeUtil.getParentOfType( psiCtx, PsiClass.class, false );
    }
    if (psiClass == null) {
      return null;
    }
    TypeSystem.pushModule( TypeSystem.getGlobalModule() );
    try {
      // Note type can come back null for Scratchpad class
      IType type = TypeSystem.getByFullNameIfValid( psiClass.getQualifiedName() );
      return type == null ? null : IGosuClassInternal.Util.getGosuClassFrom( type );
    }
    finally {
      TypeSystem.popModule( TypeSystem.getGlobalModule() );
    }
//    IGosuFile gsFile = (IGosuFile)psiCtx.getContainingFile();
//    IParseTree loc = gsFile.getParseData().getClassFileStatement().getLocation().getDeepestLocation( psiCtx.getTextOffset(), false );
//    return loc.getParsedElement().getGosuClass();
  }

  private static String makeName(PsiElement psiCtx) {
    return "Gosu_Frag.gsp";
  }

  @Override
  public IModule getModuleForPsi() {
    return getModule();
  }

  @Override
  public IModule getModule() {
    if( _psiCtx == null ) {
      return TypeSystem.getGlobalModule();
    }
    return GosuModuleUtil.findModuleForPsiElement( getContext() );
  }

  public PsiElement getContext() {
    return _psiCtx;
  }

  @Override
  public IGosuProgram parseType( String strClassName, String contents, int completionMarkerOffset ) {
    TypeSystem.pushIncludeAll();
    try {
      return super.parseType( strClassName, contents, completionMarkerOffset );
    }
    finally {
      TypeSystem.popIncludeAll();
    }
  }

  public IGosuClass reparse() {
    final String strClassName = getQualifiedClassNameFromFile();
    final String contents = GosuPsiImplUtil.getFileSource(this);
    final int completionMarkerOffset = contents.indexOf(CompletionInitializationContext.DUMMY_IDENTIFIER);
    return parseType(strClassName, contents, completionMarkerOffset);
  }

  @Override
  protected IGosuParser createParser( CharSequence contents ) {
    IGosuParser parser = super.createParser(contents);
    addSymbolsFromContext(parser);
    addUsesStmts(parser);
    return parser;
  }

  public void addImport(String qName) {
    String name = PsiNameHelper.getShortClassName(qName);
    _pseudoImports.put( name.isEmpty() ? qName : name, qName );
    reparsePsiFromContent();
  }

  private void addUsesStmts( IGosuParser parser ) {
    PsiElement psiCtx = getContext();
    IGosuClass gsClass = getGosuClassFromCtx( psiCtx );
    ITypeUsesMap ctxTypeUsesMap = getTypeUsesMap( parser, gsClass );
    for( String type: _pseudoImports.values() ) {
      if( !ctxTypeUsesMap.containsType( type ) ) {
        ctxTypeUsesMap.addToSpecialTypeUses( type );
      }
    }
    parser.setTypeUsesMap( ctxTypeUsesMap );
  }

  private ITypeUsesMap getTypeUsesMap( IGosuParser parser, IGosuClass gsClass ) {
    ITypeUsesMap typeUsesMap = parser.getTypeUsesMap();
    return typeUsesMap == null ? getTopLevelClass( gsClass ).getTypeUsesMap().copy() : typeUsesMap;
  }

  private void addSymbolsFromContext( IGosuParser parser ) {
    PsiElement psiCtx = getContext();
    IGosuClassInternal gsClass = (IGosuClassInternal)getGosuClassFromCtx( psiCtx );
    if( gsClass == null ) {
      return;
    }

    // Put class members
    gsClass.putClassMembers( (GosuParser)parser, parser.getSymbolTable(), gsClass, isContextStatic( psiCtx ) );

    // Put local vars
    psiCtx = psiCtx.getParent();
    if( psiCtx != null && psiCtx.getNode() != null ) {
      IParsedElement parsedElem = getParsedElemFrom( psiCtx );
      if( parsedElem != null ) {
        ContextSensitiveCodeRunner.collectLocalSymbols( gsClass, parser.getSymbolTable(), parsedElem, psiCtx.getTextOffset() );
      }
    }
  }

  private IParsedElement getParsedElemFrom( PsiElement psiCtx ) {
    if( psiCtx == null ) {
      return null;
    }
    if( psiCtx instanceof IGosuPsiElement ) {
      return ((IGosuPsiElement)psiCtx).getParsedElement();
    }
    return getParsedElemFrom(psiCtx.getParent());
  }

  private boolean isContextStatic( PsiElement psiCtx ) {
    if( psiCtx == null ) {
      // top-level program code runs inside the program's evaluate() method, which is non-static
      return false;
    }
    if( psiCtx instanceof PsiField || psiCtx instanceof PsiMethod ) {
      PsiModifierList modifierList = ((PsiModifierListOwner)psiCtx).getModifierList();
      return modifierList.hasModifierProperty( IGosuModifier.STATIC );
    }
    return isContextStatic(psiCtx.getParent());
  }

  @Override
  public PsiType getThisType() {
    return null;
  }

  @Override
  public void setThisType( PsiType psiType ) {

  }

  @Override
  public PsiType getSuperType() {
    return null;
  }

  @Override
  public void setSuperType( PsiType superType ) {

  }

  @Override
  public String importsToString() {
    return StringUtil.join( _pseudoImports.values(), "," );
  }

  @Override
  public void addImportsFromString( String imports ) {
    StringTokenizer tokenizer = new StringTokenizer( imports, "," );
    while( tokenizer.hasMoreTokens() ) {
      String qName = tokenizer.nextToken();
      String name = PsiNameHelper.getShortClassName( qName );
      _pseudoImports.put( name.isEmpty() ? qName : name, qName );
    }
    reparseGosuFromPsi();
  }

  @Override
  public void setVisibilityChecker( VisibilityChecker checker ) {
  }

  @Override
  public VisibilityChecker getVisibilityChecker() {
    return VisibilityChecker.EVERYTHING_VISIBLE;
  }

  @Override
  public void setExceptionHandler( ExceptionHandler exceptionHandler ) {
    _exceptionHandler = exceptionHandler;
  }

  @Override
  public ExceptionHandler getExceptionHandler() {
    return _exceptionHandler;
  }

  @Override
  public void forceResolveScope( GlobalSearchScope scope ) {
  }

  @Override
  public GlobalSearchScope getForcedResolveScope() {
    return null;
  }

  private Set<Class<? extends AbstractCompletionHandler>> allowedCompletionHandlers;
  private Set<CompletionVoter.Type> allowedCompletionTypes;

@Override
  public boolean isCompletionAllowed(AbstractCompletionHandler handler) {
    if (allowedCompletionHandlers == null) {
      return true;
    }
    for (Class<? extends AbstractCompletionHandler> hclass : allowedCompletionHandlers) {
      return hclass.isInstance(handler);
    }
    return false;
  }

  @Override
  public boolean isCompletionAllowed(CompletionVoter.Type type) {
    if (allowedCompletionTypes == null) {
      return true;
    }
    return allowedCompletionTypes.contains(type);
  }

  public void setAllowedCompletionTypes(Type... allowedCompletionTypes) {
    if (allowedCompletionTypes == null) {
      this.allowedCompletionTypes = null;
    }
    this.allowedCompletionTypes = new HashSet<>(Arrays.asList(allowedCompletionTypes));
  }

  public void setAllowedCompletionHandlers(Class<? extends AbstractCompletionHandler>... allowedCompletionHandlers) {
    if (allowedCompletionHandlers == null) {
      this.allowedCompletionHandlers = null;
    }
    this.allowedCompletionHandlers = new HashSet<>(Arrays.asList(allowedCompletionHandlers));
  }

  public class FragmentElementType extends GosuStubFileElementType {
    public FragmentElementType() {
      super( "GosuExpression", GosuLanguage.instance() );
    }

    @Nullable
    @Override
    public ASTNode parseContents( @NotNull ASTNode chameleon ) {
      chameleon.putUserData(PSI_CONTEXT, _psiCtx);
      chameleon.putUserData(FILE_NAME, getName());
      return super.parseContents( chameleon );
//      chameleon.getPsi();
//      try {
//        Field myWrapper = CompositeElement.class.getDeclaredField( "myWrapper" );
//        myWrapper.setAccessible( true );
//        if( myWrapper.get( chameleon ) == null ) {
//          String text = chameleon.getText();
//          GosuFragmentFileImpl psiFile = new GosuFragmentFileImpl( getProject(), text, _psiCtx );
//          ((CompositeElement)chameleon).setPsi( psiFile );
//        }
//        return super.parseContents( chameleon );
//      }
//      catch( Exception e ) {
//        throw new RuntimeException( e );
//      }
    }
  }

  public Map<String, String> getImports() {
    return _pseudoImports;
  }
}
