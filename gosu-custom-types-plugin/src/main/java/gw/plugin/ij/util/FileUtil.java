/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.base.Function;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.filesystem.IDEADirectory;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.filesystem.IDEAFileSystem;
import gw.plugin.ij.filesystem.IDEAResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;

public class FileUtil
{
  public static final String MAGIC_INJECTED_SUFFIX = "_INJECTED_"; // TODO: implement in a general way
  private static final Logger LOG = Logger.getInstance(FileUtil.class);
  private static final Function<XmlTag, String> GET_XML_TAG_NAME = new Function<XmlTag, String>() {
    @NotNull
    @Override
    public String apply(@Nullable XmlTag tag) {
      return tag.getName();
    }
  };

  public static PsiFile[] getFiles(@NotNull final PsiDirectory directory) {
    return ApplicationManager.getApplication().runReadAction(new Computable<PsiFile[]>() {
      @NotNull
      public PsiFile[] compute() {
        return directory.getFiles();
      }
    });
  }

  @Nullable
  public static VirtualFile getFileFromPsi(@NotNull PsiFile file) {
    VirtualFile vfile = file.getUserData(IndexingDataKeys.VIRTUAL_FILE);
    if (vfile == null) {
      vfile = file.getVirtualFile();
      if (vfile == null) {
        vfile = file.getOriginalFile().getVirtualFile();
        if (vfile == null) {
          vfile = file.getViewProvider().getVirtualFile();
        }
      } else if (vfile instanceof LightVirtualFile) {
        PsiFile containingFile = file.getContainingFile();
        if (containingFile != null && containingFile != file) {
          PsiFile originalFile = containingFile.getOriginalFile();
          SmartPsiElementPointer owningFile = originalFile.getUserData(FileContextUtil.INJECTED_IN_ELEMENT);
          if (owningFile != null) {
            vfile = owningFile.getVirtualFile();
          }
        }
      }
    }
    return vfile;
  }

  public static VirtualFile getOriginalFile(VirtualFileWindow window) {
    VirtualFile file = window.getDelegate();
    if (file instanceof LightVirtualFile) {
      final VirtualFile original = ((LightVirtualFile) file).getOriginalFile();
      if (original != null) {
        file = original;
      }
    }
    return file;
  }

  public static String getSourceQualifiedName(@NotNull VirtualFile file, @NotNull IModule module) {
    if (file instanceof VirtualFileWindow) {
      final VirtualFileWindow window = (VirtualFileWindow) file;
      final VirtualFile original = getOriginalFile(window);
      final List<String> typeNames = TypeUtil.getTypesForFile(module, original);
      if (typeNames.isEmpty())
      {
        LOG.warn( String.format( "No types for '%s', original is '%s'", file, original ) );
        return "unable.to.find.name";
      }
    }

    if (file instanceof LightVirtualFile) {
      return file.getNameWithoutExtension().replace(File.separatorChar, '.');
    }

    // General case
    final List<String> typeNames = TypeUtil.getTypesForFile(module, file);
    if (typeNames.isEmpty()) {
      LOG.warn(String.format("File '%s' is outside of any source root", file));
      return "unable.to.find.name";
    } else {
      return typeNames.get(0);
    }
  }

  @NotNull
  public static IDEAResource toIResource(VirtualFile file) {
    if (file.isDirectory()) {
      return toIDirectory(file);
    } else {
      return toIFile(file);
    }
  }

  public static IDEAFile toIFile(VirtualFile file) {
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIFile(file);
  }

  @NotNull
  public static IDEADirectory toIDirectory(VirtualFile file) {
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIDirectory(file);
  }

  // ID generation
  @Nullable
  private static Iterator<XmlTag> selfWithParents(XmlTag tag) {
    return new AbstractSequentialIterator<XmlTag>(tag) {
      @Nullable
      protected XmlTag computeNext(@Nullable XmlTag previous) {
        return previous != null ? previous.getParentTag() : null;
      }
    };
  }

  @Nullable
  private static Iterator<XmlTagChild> selfWithPrevSiblings(XmlTagChild element) {
    return new AbstractSequentialIterator<XmlTagChild>(element) {
      @Nullable
      protected XmlTagChild computeNext(@Nullable XmlTagChild previous) {
        return previous != null ? previous.getPrevSiblingInTag() : null;
      }
    };
  }

  private static <T extends XmlTagChild> Iterator<T> selfWithPrevSiblingsOfType(XmlTagChild element, Class<T> klass) {
    return Iterators.filter(selfWithPrevSiblings(element), klass);
  }

  protected static List<XmlLocation.Segment> getPath(XmlTag tag) {
    final List<XmlLocation.Segment> path = Lists.newArrayList();
    for (XmlTag parent : skip(reverse(newArrayList(selfWithParents(tag))), 1)) {
      final String name = parent.getName();
      final int index = size(filter(
          selfWithPrevSiblingsOfType(parent, XmlTag.class),
          compose(equalTo(name), GET_XML_TAG_NAME))) - 1;
      path.add(new XmlLocation.Segment(name, index));
    }
    return ImmutableList.copyOf(path);
  }

  @NotNull
  public static XmlLocation getXmlTagLocation(@NotNull XmlTag tag) {
    final XmlFile file = (XmlFile) tag.getContainingFile();
    return new XmlLocation(file.getRootTag().getName(), getPath(tag));
  }

  @NotNull
  public static XmlLocation getXmlAttributeLocation(@NotNull XmlAttribute attr) {
    final XmlTag tag = attr.getParent();
    final XmlFile file = (XmlFile) tag.getContainingFile();
    return new XmlLocation(file.getRootTag().getName(), getPath(tag), attr.getName());
  }

  @NotNull
  public static List<VirtualFile> getTypeResourceFiles(@NotNull IType type) {
    final List<VirtualFile> result = Lists.newArrayList();
    if (type instanceof IFileBasedType ) {
      for (IFile file : ((IFileBasedType) type).getSourceFiles()) {
        result.add(((IDEAFile) file).getVirtualFile());
      }
    }
    return result;
  }

  @NotNull
  public static String removeJarSeparator(@NotNull String path) {
    if (path.endsWith(JarFileSystem.JAR_SEPARATOR)) {
      path = path.substring(0, path.length() - JarFileSystem.JAR_SEPARATOR.length());
    }
    return path;
  }

  public static boolean underGosuSources(VirtualFile dir, Project project) {
    if (dir != null && dir.isDirectory()) {
      final IModule module = GosuModuleUtil.findModuleForFile( dir, project );
      if (module != null) {
        IResource sourceRoot = getSourceRoot(module, toIDirectory(dir));
        if (sourceRoot != null && sourceRoot.getPath().getPathString().endsWith("gsrc")) {
          return true;
        }
      }
    }
    return false;
  }

  private static IResource getSourceRoot(IModule module, IResource resource) {
    for (IDirectory src : module.getSourcePath()) {
      if (resource.isDescendantOf(src)) {
        return src;
      }
    }
    return null;
  }

  public static VirtualFile findSourceRoot(VirtualFile virtualFile, Project project) {
    Module module = ModuleUtil.findModuleForFile(virtualFile, project);
    if (module != null) {
      final String path = virtualFile.getPath();
      for (VirtualFile sourceRoot : ModuleRootManager.getInstance(module).getSourceRoots()) {
        String sourcePath = sourceRoot.getPath();
        if (path.startsWith(sourcePath + "/") || path.equals(sourcePath)) {
          return sourceRoot;
        }
      }
    }
    return null;
  }

  public static boolean isSourceRoot(VirtualFile file, Project project) {
    return file.equals(findSourceRoot(file, project));
  }
}
