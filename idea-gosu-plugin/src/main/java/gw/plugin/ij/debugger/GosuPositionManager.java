/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.google.common.collect.ImmutableList;
import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.CompoundPositionManager;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.JVMNameUtil;
import com.intellij.debugger.engine.TopLevelParentClassProvider;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.openapi.util.NullableComputable;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.Trinity;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.Processor;
import com.intellij.util.Query;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.filetypes.GosuFileTypes;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GosuPositionManager implements PositionManager {
  private static final Logger LOG = Logger.getInstance( "#gw.plugin.ij.debugger.GosuPositionManager" );

  private final DebugProcess _debugProcess;

  public GosuPositionManager( DebugProcess debugProcess ) {
    _debugProcess = debugProcess;
  }

  public DebugProcess getDebugProcess() {
    return _debugProcess;
  }

  /**
   * Returns the list of bytecode locations in a specific class corresponding to the specified position in the source code.
   *
   * @param type     a Java class (one of the list returned by {@link #getAllClasses}).
   * @param position the position in the source code.
   *
   * @return the list of corresponding bytecode locations.
   *
   * @throws NoDataException if the location is not in the code managed by this {@code PositionManager}
   * @see ReferenceType#locationsOfLine(int)
   */
  @NotNull
  public List<Location> locationsOfLine( @NotNull ReferenceType type, @NotNull SourcePosition position ) throws NoDataException {
    if( !GosuFileTypes.isTopLevelGosuFile( position.getFile() ) ) {
      throw new NoDataException();
    }
    try {
      int line = position.getLine() + 1;
      List<Location> locations = getDebugProcess().getVirtualMachineProxy().versionHigher( "1.4" )
                                 ? type.locationsOfLine( DebugProcessImpl.JAVA_STRATUM, null, line )
                                 : type.locationsOfLine( line );
      if( locations == null || locations.isEmpty() ) {
        throw new NoDataException();
      }
      return locations;
    }
    catch( AbsentInformationException e ) {
      throw new NoDataException();
    }
  }

  public ClassPrepareRequest createPrepareRequest( final ClassPrepareRequestor requestor, final SourcePosition position ) throws NoDataException {
    final Ref<String> waitPrepareFor = new Ref<>( null );
    final Ref<ClassPrepareRequestor> waitRequestor = new Ref<>( null );
    ApplicationManager.getApplication().runReadAction(
      new Runnable() {
        public void run() {
          PsiClass psiClass = JVMNameUtil.getClassAt( position );
          if( psiClass == null ) {
            return;
          }

          if( PsiUtil.isLocalOrAnonymousClass( psiClass ) || getBlockAt( position.getElementAt() ) != null ) {
            PsiClass parent = TopLevelParentClassProvider.getTopLevelParentClass( psiClass );

            if( parent == null ) {
              return;
            }

            final String parentQName = getClassName( parent );
            if( parentQName == null ) {
              return;
            }
            waitPrepareFor.set( parentQName + "$*" );
            waitRequestor.set( new ClassPrepareRequestor() {
              public void processClassPrepare( DebugProcess debuggerProcess, ReferenceType referenceType ) {
                final CompoundPositionManager positionManager = ((DebugProcessImpl)debuggerProcess).getPositionManager();
                final List<ReferenceType> positionClasses = positionManager.getAllClasses( position );
                if( positionClasses.isEmpty() ) {
                  // fallback
                  if( positionManager.locationsOfLine( referenceType, position ).size() > 0 ) {
                    requestor.processClassPrepare( debuggerProcess, referenceType );
                  }
                }
                else {
                  if( positionClasses.contains( referenceType ) ) {
                    requestor.processClassPrepare( debuggerProcess, referenceType );
                  }
                }
              }
            } );
          }
          else {
            waitPrepareFor.set( getClassName( psiClass ) );
            waitRequestor.set( requestor );
          }
        }
      } );
    if( waitPrepareFor.get() == null ) {
      return null;  // no suitable class found for this name
    }
    return _debugProcess.getRequestsManager().createClassPrepareRequest( waitRequestor.get(), waitPrepareFor.get() );
  }

  private static String getClassName( PsiClass psiClass ) {
    if( psiClass.getContainingFile() instanceof GosuScratchpadFileImpl ) {
      return getScratchPadName( psiClass );
    }
    return JVMNameUtil.getNonAnonymousClassName( psiClass );
  }

  private static GosuBlockExpressionImpl getBlockAt( PsiElement element ) {
    if( element == null ) {
      return null;
    }
    if( element instanceof GosuBlockExpressionImpl ) {
      return (GosuBlockExpressionImpl)element;
    }
    return getBlockAt( element.getParent() );
  }

  /**
   * Transform the fake name "scratchpad.Gosu ScratchPad" to the class name on disk,
   * this is also the actual bytecode class name dynamically created in the debug process's Gosu runtime.
   */
  private static String getScratchPadName( PsiClass psiClass ) {
    String name = psiClass.getQualifiedName();
    if( name.startsWith( GosuScratchpadFileImpl.FQN ) ) {
      VirtualFile file = GosuScratchpadFileImpl.getScratchpadFile( psiClass.getProject() );
      String scratchPadFile = file.getCanonicalPath();
      int iIndex = scratchPadFile.indexOf( '/' );
      if( iIndex >= 0 ) {
        // remove root or drive
        scratchPadFile = scratchPadFile.substring( iIndex+1 );
      }
      iIndex = scratchPadFile.lastIndexOf( '.' );
      scratchPadFile = scratchPadFile.substring( 0, iIndex );
      name = name.replace( GosuScratchpadFileImpl.FQN, scratchPadFile );
      name = name.replace( '.', '$' );
      name = name.replace( '/', '.' );
    }
    return name;
  }

  /**
   * Returns the source position corresponding to the specified bytecode location.
   *
   * @param location the bytecode location.
   *
   * @return the corresponding source position.
   *
   * @throws NoDataException if the location is not in the code managed by this {@code PositionManager}
   */
  public SourcePosition getSourcePosition( @Nullable final Location location ) throws NoDataException {
    if( location == null ) {
      throw new NoDataException();
    }

    PsiFile psiFile = getPsiFileByLocation( getDebugProcess().getProject(), location );
    if( psiFile == null || !GosuFileTypes.isTopLevelGosuFile( psiFile ) ) {
      throw new NoDataException();
    }

    int lineNumber = calcLineIndex( location );
    if( lineNumber < 0 ) {
      throw new NoDataException();
    }
    return SourcePosition.createFromLine( psiFile, lineNumber );
  }

  private int calcLineIndex( @Nullable Location location ) {
    LOG.assertTrue( _debugProcess != null );
    if( location == null ) {
      return -1;
    }

    try {
      return location.lineNumber() - 1;
    }
    catch( InternalError e ) {
      return -1;
    }
  }

  public static List<String> getGosuFileExtensions() {
    return ImmutableList.copyOf( GosuCodeFileType.INSTANCE.getExtensions() );
  }

  @Nullable
  private PsiFile getPsiFileByLocation( @NotNull final Project project, @Nullable final Location location ) {
    if( location == null ) {
      return null;
    }

    final ReferenceType refType = location.declaringType();
    if( refType == null ) {
      return null;
    }

    final String originalQName = refType.name().replace( '/', '.' );
    int dollar = originalQName.indexOf( '$' );
    String qName = dollar >= 0 ? originalQName.substring( 0, dollar ) : originalQName;

    if( qName.endsWith( GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME ) ) {
      return GosuScratchpadFileImpl.instance( project );
    }

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) );
    TypeSystem.pushModule( execEnv.getGlobalModule() );
    try {
      PsiClass cls = PsiTypeResolver.resolveType( qName, new ProjectPsiElement( project ) );
      if( cls != null ) {
        return cls.getContainingFile();
      }
    }
    catch( ProcessCanceledException e ) {
      return null;
    }
    catch( IndexNotReadyException e ) {
      return null;
    }
    finally {
      TypeSystem.popModule( execEnv.getGlobalModule() );
    }

    DirectoryIndex directoryIndex = DirectoryIndex.getInstance( project );
    int dotIndex = qName.lastIndexOf( "." );
    String packageName = dotIndex > 0 ? qName.substring( 0, dotIndex ) : "";
    Query<VirtualFile> query = directoryIndex.getDirectoriesByPackageName( packageName, true );
    final String fileNameWithoutExtension = dotIndex > 0 ? qName.substring( dotIndex + 1 ) : qName;
    final List<String> extensions = getGosuFileExtensions();
    final Ref<PsiFile> result = new Ref<>();
    query.forEach( new Processor<VirtualFile>() {
      public boolean process( @NotNull VirtualFile vDir ) {
        for( final String extension : extensions ) {
          VirtualFile vFile = vDir.findChild( fileNameWithoutExtension + "." + extension );
          if( vFile != null ) {
            PsiFile psiFile = PsiManager.getInstance( project ).findFile( vFile );
            if( psiFile instanceof IGosuFileBase ) {
              result.set( psiFile );
              return false;
            }
          }
        }
        return true;
      }
    } );

    PsiFile res = result.get();
    if( res != null ) {
      return res;
    }

    if( StringUtil.isEmpty( packageName ) ) {
      final ProjectFileIndex fileIndex = ProjectRootManager.getInstance( project ).getFileIndex();
      for( final String extension : extensions ) {
        for( final PsiFile file : FilenameIndex.getFilesByName( project, qName + "." + extension, GlobalSearchScope.projectScope( project ) ) ) {
          final VirtualFile vFile = file.getVirtualFile();
          if( file instanceof IGosuFile && vFile != null && !fileIndex.isInSource( vFile ) ) {
            for( PsiClass aClass : ((IGosuFile)file).getClasses() ) {
              if( qName.equals( aClass.getQualifiedName() ) ) {
                return file;
              }
            }
          }
        }
      }
    }

//## todo: something like this for embedded scripts
//    for( ScriptPositionManagerHelper helper : ScriptPositionManagerHelper.EP_NAME.getExtensions() )
//    {
//      if( helper.isAppropriateRuntimeName( qName ) )
//      {
//        PsiFile file = helper.getExtraScriptIfNotFound( refType, qName, project );
//        if( file != null )
//        {
//          return file;
//        }
//      }
//    }
    return null;
  }

  @NotNull
  public List<ReferenceType> getAllClasses( final SourcePosition classPosition ) throws NoDataException {
    final Trinity<String, Boolean, PsiClass> trinity = calcClassName( classPosition );
    if( trinity == null ) {
      return Collections.emptyList();
    }
    final String className = trinity.getFirst();
    final boolean isNonAnonymousClass = trinity.getSecond();
    final PsiClass classAtPosition = trinity.getThird();

    if( isNonAnonymousClass ) {
      return _debugProcess.getVirtualMachineProxy().classesByName( className );
    }

    // the name is a parent class for a local or anonymous class
    final List<ReferenceType> outers = _debugProcess.getVirtualMachineProxy().classesByName( className );
    final List<ReferenceType> result = new ArrayList<>( outers.size() );
    for( ReferenceType outer : outers ) {
      final ReferenceType nested = findNested( outer, classAtPosition, classPosition );
      if( nested != null ) {
        result.add( nested );
      }
    }
    return result;
  }

  @Nullable
  private static Trinity<String, Boolean, PsiClass> calcClassName( final SourcePosition classPosition ) {
    return ApplicationManager.getApplication().runReadAction( new NullableComputable<Trinity<String, Boolean, PsiClass>>() {
      public Trinity<String, Boolean, PsiClass> compute() {
        final PsiClass psiClass = JVMNameUtil.getClassAt( classPosition );

        if( psiClass == null ) {
          return null;
        }

        if( PsiUtil.isLocalOrAnonymousClass( psiClass ) || getBlockAt( classPosition.getElementAt() ) != null) {
          final PsiClass parentNonLocal = TopLevelParentClassProvider.getTopLevelParentClass( psiClass );
          if( parentNonLocal == null ) {
            LOG.error( "Local or anonymous class has no non-local parent" );
            return null;
          }
          final String parentClassName = getClassName( parentNonLocal );
          if( parentClassName == null ) {
            LOG.error( "The name of a parent of a local (anonymous) class is null" );
            return null;
          }
          return new Trinity<>( parentClassName, Boolean.FALSE, psiClass );
        }

        final String className = getClassName( psiClass );
        return className != null ? new Trinity<>( className, Boolean.TRUE, psiClass ) : null;
      }
    } );
  }

  @Nullable
  private ReferenceType findNested( final ReferenceType fromClass, final PsiClass classToFind, SourcePosition classPosition ) {
    final VirtualMachineProxyImpl vmProxy = (VirtualMachineProxyImpl)_debugProcess.getVirtualMachineProxy();
    if( fromClass.isPrepared() ) {

      final List<ReferenceType> nestedTypes = vmProxy.nestedTypes( fromClass );

      try {
        final int lineNumber = classPosition.getLine() + 1;

        for( ReferenceType nested : nestedTypes ) {
          final ReferenceType found = findNested( nested, classToFind, classPosition );
          if( found != null ) {
            // check if enclosing class also has executable code at the same line, and if yes, prefer enclosing class
            return fromClass.locationsOfLine( lineNumber ).isEmpty() ? found : fromClass;
          }
        }

        if( fromClass.locationsOfLine( lineNumber ).size() > 0 ) {
          return fromClass;
        }

        int rangeBegin = Integer.MAX_VALUE;
        int rangeEnd = Integer.MIN_VALUE;
        for( Location location : fromClass.allLineLocations() ) {
          final int locationLine = location.lineNumber() - 1;
          rangeBegin = Math.min( rangeBegin, locationLine );
          rangeEnd = Math.max( rangeEnd, locationLine );
        }

        if( classPosition.getLine() >= rangeBegin && classPosition.getLine() <= rangeEnd ) {
          // choose the second line to make sure that only this class' code exists on the line chosen
          // Otherwise the line (depending on the offset in it) can contain code that belongs to different classes
          // and JVMNameUtil.getClassAt(candidatePosition) will return the wrong class.
          // Example of such line:
          // list.add(new Runnable(){......
          // First offsets belong to parent class, and offsets inside te substring "new Runnable(){" belong to anonymous runnable.
          final int finalRangeBegin = rangeBegin;
          final int finalRangeEnd = rangeEnd;
          return ApplicationManager.getApplication().runReadAction( new NullableComputable<ReferenceType>() {
            public ReferenceType compute() {
              final int line = Math.min( finalRangeBegin + 1, finalRangeEnd );
              final SourcePosition candidatePosition = SourcePosition.createFromLine( classToFind.getContainingFile(), line );
              return classToFind.equals( JVMNameUtil.getClassAt( candidatePosition ) ) ? fromClass : null;
            }
          } );
        }
      }
      catch( AbsentInformationException ignored ) {
      }
    }
    return null;
  }

}