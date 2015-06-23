/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.impl.ModuleRootEventImpl;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileCopyEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent;
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiDocumentTransactionListener;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.testFramework.LightVirtualFile;
import gw.config.CommonServices;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.DelayedRunner;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class FileModificationManager implements PsiDocumentTransactionListener, BulkFileListener
{
  public static int TYPE_REFRESH_DELAY_MS = 0;
  private final DelayedRunner typeRefresher = new DelayedRunner();
  private final Project project;
  private IFileListener[] fileListeners;

  private class Refresher implements Runnable {
    private final Project project;
    private final VirtualFile file;

    public Refresher(Project project, VirtualFile file) {
      this.project = project;
      this.file = file;
    }

    public void run() {
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        public void run() {
          final TypeSystemStarter starter = TypeSystemStarter.instance( project );
          if (!starter.isStarted()) {
            return;
          }

          try {
            starter.startRefresh();
            // in case <stop> just got called, check again after bumping refresh counter
            if (!starter.isStarted()) {
              return;
            }
            fireModifiedEvent(file);
          } finally {
            starter.stopRefresh();
          }
        }
      });
    }
  }

  public FileModificationManager( Project project ) {
    this.project = project;
    final FileListenerExtensionBean[] extensions = Extensions.getExtensions( FileListenerExtensionBean.PLUGIN_NAME );
    fileListeners = new IFileListener[extensions.length];
    for (int i = 0; i < extensions.length; i++) {
      fileListeners[i] = extensions[i].getHandler();
    }
  }

  @Nullable
  private VirtualFile getVirtualFile(@NotNull PsiFile psiFile) {
    return FileUtil.getFileFromPsi( psiFile );
  }

  private String getRefreshTaskId(@NotNull VirtualFile virtualFile) {
    return virtualFile.getPath();
  }

  private String oldText;

  // PsiDocumentTransactionListener
  public void transactionStarted(final Document doc, final PsiFile file) {
    final FileElement myTreeElementBeingReparsedSoItWontBeCollected = ((PsiFileImpl) file).calcTreeElement();
    oldText = myTreeElementBeingReparsedSoItWontBeCollected.getText();
  }

  // Type creation and refresh
  // FIXME-dp this method does NOT get invoked when a Gosu class gets created, why?
  // Also, this will not fire when a file is created externally (Use VirtualFileManager.VFS_CHANGES)
  public void transactionCompleted(final Document doc, @NotNull final PsiFile psiFile) {
    VirtualFile file = getVirtualFile(psiFile);
    if (file instanceof VirtualFileWindow ) {
      file = ((VirtualFileWindow) file).getDelegate();
    }

//    if (!GosuFileTypes.isGosuFile(file)) {
      final Runnable refresher = new Refresher(project, file);
      if (TYPE_REFRESH_DELAY_MS == 0) {
        refresher.run();
      } else {
        if ( PluginLoaderUtil.instance( project ).isStarted()) {
          typeRefresher.scheduleTask(getRefreshTaskId(file), TYPE_REFRESH_DELAY_MS, refresher);
        }
      }
//    }

    // process inner class changes
    if (psiFile instanceof PsiClassOwner ) {
      String newText = doc.getText();
      for (IFileListener fileListener : fileListeners) {
        fileListener.modified( FileUtil.toIFile( file ), oldText, newText);
      }
    }
  }

  // BulkRefreshListener
  public void before(@NotNull final List<? extends VFileEvent> events) {
    // Nothing to do
  }

  public void after(@NotNull final List<? extends VFileEvent> events) {
    final IExecutionEnvironment env = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) );
    IModule globalModule = env.getGlobalModule();
    TypeSystem.pushModule( globalModule );
    try {
      if ( GosuInitialization.instance( env ).isInitialized()) {
        for (VFileEvent event : events) {
          final VirtualFile file = event.getFile();
          if (file != null) {
            if (event instanceof VFilePropertyChangeEvent ) {
              processPropertyChangeEvent((VFilePropertyChangeEvent) event);
            } else if (event instanceof VFileMoveEvent ) {
              processFileMoveEvent((VFileMoveEvent) event);
            } else if (event instanceof VFileCreateEvent ) {
              fireCreatedEvent(file);
            } else if (event instanceof VFileDeleteEvent ) {
              fireDeletedEvent(file);
            } else if (event instanceof VFileCopyEvent ) {
              processFileCopyEvent((VFileCopyEvent) event);
            } else {
              fireModifiedEvent(file);
            }
          }
        }
      }
    } finally {
      TypeSystem.popModule( globalModule );
    }
  }

  private void processFileCopyEvent(@NotNull VFileCopyEvent event) {
    String newFileName = event.getNewParent().getPath() + "/" + event.getNewChildName();
    IFile newFile = CommonServices.getFileSystem().getIFile(new File(newFileName));
    fireCreatedEvent(newFile);
  }

  private void processFileMoveEvent(@NotNull VFileMoveEvent event) {
    VirtualFile newFile = event.getFile();
    String oldFileName = event.getOldParent().getPath() + "/" + newFile.getName();
    IFile oldFile = CommonServices.getFileSystem().getIFile(new File(oldFileName));
    fireDeletedEvent(oldFile);
    fireCreatedEvent(newFile);
  }

  private void processPropertyChangeEvent(@NotNull VFilePropertyChangeEvent event) {
    if (event.getFile().isDirectory()) { // a source folder could have been renamed
      PluginLoaderUtil.instance( project ).getModuleClasspathListener().rootsChanged(new ModuleRootEventImpl(project, false));
    }

    if (event.getPropertyName().equals( VirtualFile.PROP_NAME)) { // collect file renames
      final VirtualFile newFile = event.getFile();
      if (newFile instanceof LightVirtualFile ) {
        return;
      }

      final String oldFileName = (String) event.getOldValue();
      final IFile oldFile = FileUtil.toIDirectory( newFile.getParent() ).file(oldFileName);
      fireDeletedEvent(oldFile);
      fireCreatedEvent( FileUtil.toIResource( newFile ));
    }
  }

  private void fireModifiedEvent(VirtualFile file) {
    fireModifiedEvent( FileUtil.toIResource( file ));
  }

  private void fireDeletedEvent(VirtualFile file) {
    fireDeletedEvent( FileUtil.toIResource( file ));
  }

  private void fireCreatedEvent(VirtualFile file) {
    fireCreatedEvent( FileUtil.toIResource( file ));
  }

  private void fireModifiedEvent(IResource file) {
    for (IFileListener fileListener : fileListeners) {
      fileListener.modified(file);
    }
  }

  private void fireDeletedEvent(IResource file) {
    for (IFileListener fileListener : fileListeners) {
      fileListener.deleted(file);
    }
  }

  private void fireCreatedEvent(IResource file) {
    for (IFileListener fileListener : fileListeners) {
      fileListener.created(file);
    }
  }
}
