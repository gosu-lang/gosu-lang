/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.impl.ModuleManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.InheritedJdkOrderEntry;
import com.intellij.openapi.roots.ModuleJdkOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.containers.HashSet;
import com.intellij.util.indexing.IndexingDataKeys;
import gw.lang.IModuleAware;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.core.IJModuleNode;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.core.UnidirectionalCyclicGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * wrapper for methods in Idea's ModuleUtil class that adds some additional functionality to properly handle
 * file fragments.  May be obsolete once VirtualFileWindow/DocumentWindow implementation is complete.
 */
public class GosuModuleUtil
{
  public static final String JUNIT_FRAMEWORK_TEST_CASE = "junit.framework.TestCase";
  public static final Key<IModule> KEY_GOSU_MODULE = new Key<>( "GosuModule" );

  @Nullable
  public static IModule findModuleForPsiElement( @NotNull PsiElement element )
  {
    if( element instanceof IModuleAware )
    {
      return ((IModuleAware)element).getModule();
    }
    if( element.getContainingFile() instanceof PsiCodeFragment )
    {
      element = element.getContainingFile().getContext();
    }
    IModule module = findGosuModuleForPsiElementInternal( element );
    if( module == null )
    {
      final VirtualFile file = element.getUserData( IndexingDataKeys.VIRTUAL_FILE );
      if( file != null )
      {
        module = findModuleForFile( file, element.getProject() );
      }

      final PsiFile psiFile = element.getContainingFile();
      if( module == null )
      {
        module = getGosuModuleFromOriginalFile( psiFile, element.getProject() );
      }
    }
    return module;
  }

  @Nullable
  public static IModule findModuleForFile( @NotNull VirtualFile file, @NotNull Project project )
  {
    IModule gsModule = findGosuModuleByOrderEntries( file, project );
    if( gsModule == null )
    {
      gsModule = getModule( ModuleUtil.findModuleForFile( file, project ) );
    }
    return gsModule;
  }

  @Nullable
  private static IModule findGosuModuleForPsiElementInternal( @NotNull PsiElement element )
  {
    IModule module = element.getUserData( KEY_GOSU_MODULE );
    if( module != null )
    {
      return module;
    }
    final Project project = element.getProject();
    final ProjectFileIndex fileIndex = ProjectRootManager.getInstance( project ).getFileIndex();
    if( element instanceof PsiFileSystemItem )
    {
      VirtualFile file = ((PsiFileSystemItem)element).getVirtualFile();
      if( file == null )
      {
        final PsiFile psiFile = element.getContainingFile();
        file = psiFile == null ? null : psiFile.getOriginalFile().getVirtualFile();
        if( file == null )
        {
          return getModule( element.getUserData( ModuleUtil.KEY_MODULE ) );
        }
      }

      module = findGosuModuleByOrderEntries( file, project );
      if( module != null )
      {
        return module;
      }

      return getModule( fileIndex.getModuleForFile( file ) );
    }

    PsiFile psiFile = element.getContainingFile();
    if( psiFile != null )
    {
      PsiElement context;
      while( (context = psiFile.getContext()) != null )
      {
        final PsiFile file = context.getContainingFile();
        if( file == null )
        {
          break;
        }
        psiFile = file;
      }

      if( psiFile.getUserData( ModuleUtil.KEY_MODULE ) != null )
      {
        return getModule( psiFile.getUserData( ModuleUtil.KEY_MODULE ) );
      }

      final PsiFile originalFile = psiFile.getOriginalFile();
      if( originalFile.getUserData( ModuleUtil.KEY_MODULE ) != null )
      {
        return getModule( originalFile.getUserData( ModuleUtil.KEY_MODULE ) );
      }

      final VirtualFile virtualFile = originalFile.getVirtualFile();
      IModule fileModule = null;
      if( fileModule != null )
      {
        return fileModule;
      }
      else if( virtualFile != null )
      {
        Module moduleForFile = fileIndex.getModuleForFile( virtualFile );
        if( moduleForFile != null )
        {
          return getModule( moduleForFile );
        }
        module = findGosuModuleByOrderEntries( virtualFile, project );
        if( module != null )
        {
          return module;
        }
      }
    }

    return getModule( element.getUserData( ModuleUtil.KEY_MODULE ) );
  }

  @Nullable
  private static IModule getGosuModuleFromOriginalFile( @Nullable PsiFile containingFile, @NotNull Project project )
  {
    // TODO the duplication here is related to locating the module for a Gosu fragment - need to implement VirtualFileWindow
    // for fragments to eliminate it.
    IModule gosuModule = null;
    if( containingFile != null )
    {
      PsiFile originalFile = containingFile.getOriginalFile();
      SmartPsiElementPointer owningFile = originalFile.getUserData( FileContextUtil.INJECTED_IN_ELEMENT );
      if( owningFile != null )
      {
        VirtualFile owningVF = owningFile.getVirtualFile();
        if( owningVF != null )
        {
          gosuModule = findModuleForFile( owningVF, project );
        }
      }
    }

    if( gosuModule == null && containingFile != null )
    {
      VirtualFile file = containingFile.getVirtualFile();
      if( file instanceof VirtualFileWindow )
      {
        file = ((VirtualFileWindow)file).getDelegate();
      }
      if( file instanceof LightVirtualFile )
      {
        VirtualFile originalFile = ((LightVirtualFile)file).getOriginalFile();
        file = originalFile != null ? originalFile : file;
      }
      if( file != null )
      {
        return findModuleForFile( file, project );
      }
    }

    return gosuModule;
  }

  @Nullable
  private static IModule findGosuModuleByOrderEntries( @NotNull VirtualFile file, @NotNull Project project )
  {
    final ProjectFileIndex fileIndex = ProjectRootManager.getInstance( project ).getFileIndex();

    if( project.isInitialized() && (fileIndex.isInLibrarySource( file ) || fileIndex.isInLibraryClasses( file )) )
    {
      final List<OrderEntry> entries = fileIndex.getOrderEntriesForFile( file );
      if( entries.isEmpty() )
      {
        return null;
      }

      Set<Module> modules = new HashSet<>();
      for( OrderEntry entry : entries )
      {
        if( entry instanceof InheritedJdkOrderEntry || entry instanceof ModuleJdkOrderEntry )
        {
          return GosuModuleUtil.getJreModule( project );
        }
        modules.add( entry.getOwnerModule() );
      }

      final List<Module> sortedModules = Ordering.from( ModuleManager.getInstance( project ).moduleDependencyComparator() ).sortedCopy( modules );
      return getModule( sortedModules.iterator().next() );
    }

    return null;
  }

  // Gosu Module <-> IJ Module
  @Nullable
  public static IModule getModule( @Nullable Module ijModule )
  {
    return ijModule != null ? TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( ijModule.getProject() ) ).getModule( ijModule.getName() ) : null;
  }

  @Nullable
  public static Module getModule( @Nullable IModule gsModule )
  {
    return gsModule != null ? (Module)gsModule.getNativeModule() : null;
  }

  public static boolean isGosuTest( @Nullable String fqn, @Nullable String pkg, @Nullable Set<String> patterns, Module ijMod, Project proj )
  {
    if( patterns != null && !patterns.isEmpty() )
    {
      return true;
    }
    IModule module = GosuModuleUtil.getModule( ijMod );
    if( module == null )
    {
      return true;
    }
    TypeSystem.pushModule( module );
    try
    {
      if( fqn != null )
      {
        return TypeSystem.getByFullNameIfValid( fqn, module ) instanceof IGosuClass;
      }
      else if( pkg != null )
      {
        return pkg.length() == 0;
      }
      else
      {
        return true;
      }
    }
    finally
    {
      TypeSystem.popModule( module );
    }
  }

  public static String getActualClassName( String name, Project project )
  {
    return name;
  }

  private static boolean extendsTestCase( @Nullable PsiClass psiClass )
  {
    while( psiClass != null && !JUNIT_FRAMEWORK_TEST_CASE.equals( psiClass.getQualifiedName() ) )
    {
      psiClass = psiClass.getSuperClass();
    }
    return psiClass != null && JUNIT_FRAMEWORK_TEST_CASE.equals( psiClass.getQualifiedName() );
  }

  public static List<? extends IModule> getModules( @NotNull Project project )
  {
    return TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ).getModules();
  }

  public static IModule getGlobalModule( @NotNull Project project )
  {
    return TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ).getGlobalModule();
  }

  @NotNull
  public static IJreModule getJreModule( @NotNull Project project )
  {
    return (IJreModule)TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ).getJreModule();
  }

  public static IModule findOrGlobal( String module )
  {
    IExecutionEnvironment env = TypeSystem.getExecutionEnvironment();
    IModule moduleInstance = env.getModule( module );
    if( moduleInstance != null )
    {
      return moduleInstance;
    }
    return env.getGlobalModule();
  }

  @Nullable
  public static String getCircularModuleDependency( @NotNull Project project )
  {
    final UnidirectionalCyclicGraph<Module> graph = new UnidirectionalCyclicGraph<>();
    final ModuleManagerImpl instance = (ModuleManagerImpl)ModuleManagerImpl.getInstance( project );
    for( Module module : instance.getModules() )
    {
      final IJModuleNode node = new IJModuleNode( module );
      graph.registerNode( node.getId(), node );
    }
    try
    {
      graph.resolveLinks();
      return null;
    }
    catch( Exception e )
    {
      return e.getMessage();
    }
  }

  public static List<Module> getDependencies( @NotNull Module module )
  {
    return ImmutableList.copyOf( ModuleRootManager.getInstance( module ).getDependencies() );
  }
}
