/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.custom;

import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
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
import com.intellij.psi.PsiJavaToken;
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
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class JavaFacadePsiClass implements PsiClass
{
  public static final Key<JavaFacadePsiClass> KEY_JAVAFACADE = new Key<JavaFacadePsiClass>("Facade");

  @Nullable
  private PsiFile _file;
  private IFileBasedType _type;
  private PsiClass _delegate;

  public JavaFacadePsiClass( PsiClass delegate, IFileBasedType type )
  {
    super();

    initialize( delegate, type );
  }

  public void initialize( PsiClass delegate, IFileBasedType type )
  {
    final VirtualFile virtualFile = FileUtil.getTypeResourceFiles( type ).get( 0 );
    final PsiManager manager = PsiManagerImpl.getInstance( (Project)type.getTypeLoader().getModule().getExecutionEnvironment().getProject().getNativeProject() );
    _delegate = delegate;
    _type = type;
    _file = manager.findFile( virtualFile );

    final Module module = virtualFile.getUserData( ModuleUtil.KEY_MODULE );
    if( module != null )
    {
      _file.putUserData( ModuleUtil.KEY_MODULE, module );
    }
    _delegate.getContainingFile().putUserData( KEY_JAVAFACADE, this );
  }

  @Override
  public String getQualifiedName()
  {
    return _type.getName();
  }

  @Override
  public String getName()
  {
    return _type.getRelativeName();
  }

  public String getNamespace()
  {
    return _type.getNamespace();
  }

  @Override
  public boolean isInterface()
  {
    return _type.isInterface();
  }

  @Override
  public boolean isEnum()
  {
    return _type.isEnum();
  }

  public IFileBasedType getType()
  {
    return _type;
  }

  @Override
  public boolean isAnnotationType()
  {
    return (_type instanceof IGosuClass) && ((IGosuClass)_type).isAnnotation();
  }

  @NotNull
  @Override
  public PsiMethod[] getConstructors()
  {
    return _delegate.getConstructors();
  }

  @NotNull
  @Override
  public PsiMethod[] getMethods()
  {
    return _delegate.getMethods();
  }

  @NotNull
  @Override
  public PsiMethod[] findMethodsByName( @NonNls String name, boolean checkBases )
  {
    return _delegate.findMethodsByName( name, checkBases );
  }

  @Override
  public boolean isValid()
  {
    return _delegate.isValid();
  }

  public PsiClass getDelegate()
  {
    return _delegate;
  }

  @NotNull
  @Override
  public Project getProject() throws PsiInvalidElementAccessException
  {
    return _file.getProject();
  }

  @Override
  public boolean isWritable()
  {
    return true;
  }

  @NotNull
  @Override
  public PsiManagerEx getManager()
  {
    return (PsiManagerEx)_file.getManager();
  }

  @Override
  public boolean isPhysical()
  {
    return false;
  }

  @Override
  public PsiClass getContainingClass()
  {
    return _type.getEnclosingType() != null ? CustomPsiClassCache.instance().getPsiClass( _type.getEnclosingType() ) : null;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() throws PsiInvalidElementAccessException
  {
    return _delegate.getContainingFile(); //_file;
  }
  public PsiFile getRawFile()
  {
    return _file;
  }

  @Override
  public boolean hasModifierProperty( @PsiModifier.ModifierConstant @NonNls @NotNull String name )
  {
    return _delegate.hasModifierProperty( name );
  }

  @Override
  public PsiField findFieldByName( @NonNls String name, boolean checkBases )
  {
    return _delegate.findFieldByName( name, checkBases );
  }

  @Override
  public PsiReferenceList getExtendsList()
  {
    return _delegate.getExtendsList();
  }

  @Override
  public PsiReferenceList getImplementsList()
  {
    return _delegate.getImplementsList();
  }

  @NotNull
  @Override
  public PsiClassType[] getExtendsListTypes()
  {
    return _delegate.getExtendsListTypes();
  }

  @NotNull
  @Override
  public PsiClassType[] getImplementsListTypes()
  {
    return _delegate.getImplementsListTypes();
  }

  @Override
  public PsiClass getSuperClass()
  {
    return _delegate.getSuperClass();
  }

  @NotNull
  @Override
  public PsiClass[] getInterfaces()
  {
    return _delegate.getInterfaces();
  }

  @NotNull
  @Override
  public PsiClass[] getSupers()
  {
    return _delegate.getSupers();
  }

  @NotNull
  @Override
  public PsiClassType[] getSuperTypes()
  {
    return _delegate.getSuperTypes();
  }

  @NotNull
  @Override
  public PsiField[] getFields()
  {
    return _delegate.getFields();
  }

  @NotNull
  @Override
  public PsiClass[] getInnerClasses()
  {
    return _delegate.getInnerClasses();
  }

  @NotNull
  @Override
  public PsiClassInitializer[] getInitializers()
  {
    return _delegate.getInitializers();
  }

  @NotNull
  @Override
  public PsiField[] getAllFields()
  {
    return _delegate.getAllFields();
  }

  @NotNull
  @Override
  public PsiMethod[] getAllMethods()
  {
    return _delegate.getAllMethods();
  }

  @NotNull
  @Override
  public PsiClass[] getAllInnerClasses()
  {
    return _delegate.getAllInnerClasses();
  }

  @Override
  public PsiClass findInnerClassByName( @NonNls String name, boolean checkBases )
  {
    return _delegate.findInnerClassByName( name, checkBases );
  }

  @Override
  public PsiJavaToken getLBrace()
  {
    return (PsiJavaToken)_delegate.getLBrace();
  }

  @Override
  public PsiJavaToken getRBrace()
  {
    return (PsiJavaToken)_delegate.getRBrace();
  }

  @Override
  public PsiIdentifier getNameIdentifier()
  {
    return _delegate.getNameIdentifier();
  }

  @Nullable
  @Override
  public PsiElement getScope()
  {
    return _delegate.getScope();
  }

  @Override
  public boolean isInheritor( @NotNull PsiClass baseClass, boolean checkDeep )
  {
    return _delegate.isInheritor( baseClass, checkDeep );
  }

  @Override
  public boolean isInheritorDeep( PsiClass baseClass, @Nullable PsiClass classToByPass )
  {
    return _delegate.isInheritorDeep( baseClass, classToByPass );
  }

  @NotNull
  @Override
  public Collection<HierarchicalMethodSignature> getVisibleSignatures()
  {
    return _delegate.getVisibleSignatures();
  }

  @Nullable
  @Override
  public PsiElement setName( @NonNls @NotNull String name ) throws IncorrectOperationException
  {
    return _delegate.setName( name );
  }

  @Override
  public PsiDocComment getDocComment()
  {
    return _delegate.getDocComment();
  }

  @Override
  public boolean isDeprecated()
  {
    return _delegate.isDeprecated();
  }

  @Override
  public boolean hasTypeParameters()
  {
    return _delegate.hasTypeParameters();
  }

  @Override
  public PsiTypeParameterList getTypeParameterList()
  {
    return _delegate.getTypeParameterList();
  }

  @NotNull
  @Override
  public PsiTypeParameter[] getTypeParameters()
  {
    return _delegate.getTypeParameters();
  }

  @Override
  public ItemPresentation getPresentation()
  {
    return _delegate.getPresentation();
  }

  @Override
  public void navigate( boolean requestFocus )
  {
    final Navigatable navigatable = PsiNavigationSupport.getInstance().getDescriptor( this );
    if( navigatable != null )
    {
      navigatable.navigate( requestFocus );
    }
  }

  @Override
  public boolean canNavigate()
  {
    return true;
  }

  @Override
  public boolean canNavigateToSource()
  {
    return true;
  }

  @Override
  public PsiModifierList getModifierList()
  {
    return _delegate.getModifierList();
  }

  @Nullable
  @Override
  public String getText()
  {
    return _file.getText();
  }

  @NotNull
  @Override
  public char[] textToCharArray()
  {
    return _file.getText().toCharArray();
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement()
  {
    return _file.getNavigationElement();
  }

  @Nullable
  @Override
  public PsiElement getOriginalElement()
  {
    return _delegate.getOriginalElement();
  }

//  @Override
//  public void checkAdd( @NotNull PsiElement element ) throws IncorrectOperationException
//  {
//
//  }

  @NotNull
  @Override
  public GlobalSearchScope getResolveScope()
  {
    return GlobalSearchScope.allScope( getProject() );
  }

  @NotNull
  @Override
  public SearchScope getUseScope()
  {
    return PsiClassImplUtil.getClassUseScope( this );
  }

  @Override
  public Icon getIcon( int flags )
  {
    return _file.getIcon( flags );
  }

  public PsiElement add( @NotNull PsiElement element ) throws IncorrectOperationException
  {
    return _delegate.add( element );
  }

  @Nullable
  @Override
  public String toString()
  {
    return _delegate.toString();
  }

  ///


  @Nullable
  @Override
  public PsiMethod findMethodBySignature( PsiMethod patternMethod, boolean checkBases )
  {
    return _delegate.findMethodBySignature( patternMethod, checkBases );
  }

  @NotNull
  @Override
  public PsiMethod[] findMethodsBySignature( PsiMethod patternMethod, boolean checkBases )
  {
    return _delegate.findMethodsBySignature( patternMethod, checkBases );
  }

  @NotNull
  @Override
  public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName( @NonNls String name, boolean checkBases )
  {
    return _delegate.findMethodsAndTheirSubstitutorsByName( name, checkBases );
  }

  @NotNull
  @Override
  public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors()
  {
    return _delegate.getAllMethodsAndTheirSubstitutors();
  }

  @NotNull
  @Override
  public Language getLanguage()
  {
    return _delegate.getLanguage();
  }

  @NotNull
  @Override
  public PsiElement[] getChildren()
  {
    return _delegate.getChildren();
  }

  @Override
  public PsiElement getParent()
  {
    return _delegate.getParent();
  }

  @Override
  public PsiElement getFirstChild()
  {
    return _delegate.getFirstChild();
  }

  @Override
  public PsiElement getLastChild()
  {
    return _delegate.getLastChild();
  }

  @Override
  public PsiElement getNextSibling()
  {
    return _delegate.getNextSibling();
  }

  @Override
  public PsiElement getPrevSibling()
  {
    return _delegate.getPrevSibling();
  }

  @Override
  public TextRange getTextRange()
  {
    return _delegate.getTextRange();
  }

  @Override
  public int getStartOffsetInParent()
  {
    return _delegate.getStartOffsetInParent();
  }

  @Override
  public int getTextLength()
  {
    return _delegate.getTextLength();
  }

  @Nullable
  @Override
  public PsiElement findElementAt( int offset )
  {
    return _delegate.findElementAt( offset );
  }

  @Nullable
  @Override
  public PsiReference findReferenceAt( int offset )
  {
    return _delegate.findReferenceAt( offset );
  }

  @Override
  public int getTextOffset()
  {
    return _delegate.getTextOffset();
  }

  @Override
  public boolean textMatches( @NotNull @NonNls CharSequence text )
  {
    return _delegate.textMatches( text );
  }

  @Override
  public boolean textMatches( @NotNull PsiElement element )
  {
    return _delegate.textMatches( element );
  }

  @Override
  public boolean textContains( char c )
  {
    return _delegate.textContains( c );
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor )
  {
    _delegate.accept( visitor );
  }

  @Override
  public void acceptChildren( @NotNull PsiElementVisitor visitor )
  {
    _delegate.acceptChildren( visitor );
  }

  @Override
  public PsiElement copy()
  {
    return new JavaFacadePsiClass( (PsiClass)_delegate.copy(), _type );
  }

  @Override
  public PsiElement addBefore( @NotNull PsiElement element, @Nullable PsiElement anchor ) throws IncorrectOperationException
  {
    return _delegate.addBefore( element, anchor );
  }

  @Override
  public PsiElement addAfter( @NotNull PsiElement element, @Nullable PsiElement anchor ) throws IncorrectOperationException
  {
    return _delegate.addAfter( element, anchor );
  }

  @Override
  public void checkAdd( @NotNull PsiElement element ) throws IncorrectOperationException
  {
    _delegate.checkAdd( element );
  }

  @Override
  public PsiElement addRange( PsiElement first, PsiElement last ) throws IncorrectOperationException
  {
    return _delegate.addRange( first, last );
  }

  @Override
  public PsiElement addRangeBefore( @NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor ) throws IncorrectOperationException
  {
    return _delegate.addRangeBefore( first, last, anchor );
  }

  @Override
  public PsiElement addRangeAfter( PsiElement first, PsiElement last, PsiElement anchor ) throws IncorrectOperationException
  {
    return _delegate.addRangeAfter( first, last, anchor );
  }

  @Override
  public void delete() throws IncorrectOperationException
  {
    _delegate.delete();
  }

  @Override
  public void checkDelete() throws IncorrectOperationException
  {
    _delegate.checkDelete();
  }

  @Override
  public void deleteChildRange( PsiElement first, PsiElement last ) throws IncorrectOperationException
  {
    _delegate.deleteChildRange( first, last );
  }

  @Override
  public PsiElement replace( @NotNull PsiElement newElement ) throws IncorrectOperationException
  {
    return _delegate.replace( newElement );
  }

  @Nullable
  @Override
  public PsiReference getReference()
  {
    return _delegate.getReference();
  }

  @NotNull
  @Override
  public PsiReference[] getReferences()
  {
    return _delegate.getReferences();
  }

  @Nullable
  @Override
  public <T> T getCopyableUserData( Key<T> key )
  {
    return _delegate.getCopyableUserData( key );
  }

  @Override
  public <T> void putCopyableUserData( Key<T> key, @Nullable T value )
  {
    _delegate.putCopyableUserData( key, value );
  }

  @Override
  public boolean processDeclarations( @NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place )
  {
    return _delegate.processDeclarations( processor, state, lastParent, place );
  }

  @Nullable
  @Override
  public PsiElement getContext()
  {
    return _delegate.getContext();
  }

  @Override
  public ASTNode getNode()
  {
    return _delegate.getNode();
  }

  @Override
  public boolean isEquivalentTo( PsiElement another )
  {
    return _delegate.isEquivalentTo( another );
  }

  @Nullable
  @Override
  public <T> T getUserData( @NotNull Key<T> key )
  {
    return _delegate.getUserData( key );
  }

  @Override
  public <T> void putUserData( @NotNull Key<T> key, @Nullable T value )
  {
    _delegate.putUserData( key, value );
  }
}
