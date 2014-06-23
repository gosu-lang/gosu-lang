/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.ide.highlighter.ModuleFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.module.impl.ModuleImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectImpl;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.impl.ModuleRootManagerImpl;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class ModuleTestCase extends IdeaTestCase {
  //  protected final Collection<Module> myModulesToDispose = new ArrayList<Module>();
  protected final HashMap<String, LinkedList<String>> _myModuleDependencies = new HashMap<>();

  @Override
  protected void afterClass() throws Exception {
    if (myProject == null) {
      return;
    }
    try {
      final ModuleManager moduleManager = ModuleManager.getInstance(myProject);
      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          for (Module module : moduleManager.getModules()) {
            String moduleName = module.getName();
            if (moduleManager.findModuleByName(moduleName) != null) {
              if (!module.isDisposed()) {
                moduleManager.disposeModule(module);
              }
            }
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      super.afterClass();
    }
  }

  @Override
  protected void beforeMethod() throws Exception {
    super.beforeMethod();
//    myModulesToDispose.clear();
  }

  @Override
  protected void afterMethod() throws Exception {
//      myModulesToDispose.clear();
    super.afterMethod();
  }

  protected void initModules(@NotNull String[] moduleWithDependencies) {
    _myModuleDependencies.clear();
    for (String module : moduleWithDependencies) {
      String[] mdepend = module.split("<");
      assertTrue("mismatched module data format: ModuleName<DependentModuleName1,DependentModuleName2", mdepend.length == 1 || mdepend.length == 2);
      assertTrue("empty module name not allowed", mdepend[0].length() > 0);
      LinkedList<String> mdlist = new LinkedList<>();
      if (mdepend.length == 2) {
        for (String m : mdepend[1].split(",")) {
          assertTrue("empty module name not allowed", m.length() > 0);
          mdlist.add(m);
        }
      }
      _myModuleDependencies.put(mdepend[0], mdlist);
    }
    // module dependency cycle detection
    for (String module : _myModuleDependencies.keySet()) {
      LinkedList<String> list = _myModuleDependencies.get(module);
      Set<String> visited = new HashSet<>();
      visited.add(module);
      detectLoop(visited, list);
    }
  }

  private void detectLoop(@NotNull Set<String> visited, @NotNull LinkedList<String> list) {
    for (String m : list) {
      assertFalse("module cycle dependency detected.", visited.contains(m));
      visited.add(m);
      Object childList = _myModuleDependencies.get(m);
      if (childList != null && ((LinkedList<String>) childList).size() > 0) {
        detectLoop(visited, (LinkedList<String>) childList);
      }
    }
  }

  @Override
  protected void setUpModule() {
    // a switch from single main module test case to multiple modules test case
    if (_myModuleDependencies == null || _myModuleDependencies.isEmpty()) {
      // set up default single module
      super.setUpModule();
    } else {
      // set up multiple modules and module dependencies if available
      for (String moduleName : _myModuleDependencies.keySet()) {
        createModule(moduleName);
      }

      for (String module : _myModuleDependencies.keySet()) {
        LinkedList<String> moduleDepends = _myModuleDependencies.get(module);
        for (String moduleDepend : moduleDepends) {
          addModuleDependency(module, moduleDepend);
        }
      }
    }
  }

  @Override
  protected Module createModule(@NonNls final String moduleName) {
    final Module m = super.createModule(moduleName);
    // module m is created with
    // getProject().getBaseDir().getPath().replace('/', File.separatorChar) + File.separatorChar +
    // moduleName + ModuleFileType.DOT_DEFAULT_EXTENSION

    new WriteAction<ModifiableRootModel>() {
      @Override
      protected void run(@NotNull Result<ModifiableRootModel> result) throws Throwable {
        // add content root and source root
        final VirtualFile baseDir = getProject().getBaseDir();
        assertNotNull(baseDir);
        baseDir.refresh(false, false);
        VirtualFile moduleDir = baseDir.createChildDirectory(this, moduleName);
        moduleDir.refresh(false, false);

        VirtualFile srcDir = moduleDir.createChildDirectory(this, "src");
        srcDir.refresh(false, false);

        final ModifiableRootModel rootModel = ModuleRootManager.getInstance(m).getModifiableModel();
        final ContentEntry contentRoot = rootModel.addContentEntry(moduleDir);
        contentRoot.addSourceFolder(srcDir, false);
        rootModel.commit();
        result.setResult(rootModel);
      }
    }.execute().getResultObject();

    // alternatively, use:
//    try {
//      final VirtualFile baseDir = getProject().getBaseDir();
//      assertNotNull(baseDir);
//      baseDir.refresh(false, false);
//      VirtualFile moduleDir = null;
//      moduleDir = baseDir.createChildDirectory(this, moduleName);
//      moduleDir.refresh(false, false);
//      addSourceContentToRoots (m, moduleDir);
//    } catch (IOException e) {
//
//    }

//    myModulesToDispose.add(m);
    return m;
  }

  protected void addModuleDependency(@NotNull String module, @NotNull String moduleDepend) {
    final Module m = getModuleByName(module);
    final Module md = getModuleByName(moduleDepend);

    new WriteCommandAction(getProject()) {
      @Override
      protected void run(Result result) throws Throwable {
        ModifiableRootModel mRoot = ModuleRootManager.getInstance(m).getModifiableModel();
        mRoot.addModuleOrderEntry(md);
        mRoot.commit();
      }
    }.execute();
  }

  @Nullable
  protected Module getModuleByName(@NotNull String module) {
    Module m = ModuleManager.getInstance(getProject()).findModuleByName(module);
    assertNotNull("Module " + module + " does not exist", m);
    return m;
  }

  protected Module createModule(@NotNull final File moduleFile) {
    return createModule(moduleFile, StdModuleTypes.JAVA);
  }

  protected Module createModule(@NotNull final File moduleFile, @NotNull final ModuleType moduleType) {
    final String path = moduleFile.getAbsolutePath();
    return createModule(path, moduleType);
  }

  protected Module createModule(@NotNull final String path, @NotNull final ModuleType moduleType) {
    Module module = ApplicationManager.getApplication().runWriteAction(
        new Computable<Module>() {
          @NotNull
          @Override
          public Module compute() {
            return ModuleManager.getInstance(myProject).newModule(path, moduleType.getId());
          }
        }
    );

//    myModulesToDispose.add(module);
    return module;
  }

  protected Module loadModule(@NotNull final File moduleFile) {
    Module module = ApplicationManager.getApplication().runWriteAction(
        new Computable<Module>() {
          @Nullable
          @Override
          public Module compute() {
            try {
              return ModuleManager.getInstance(myProject).loadModule(moduleFile.getAbsolutePath());
            } catch (Exception e) {
              LOG.error(e);
              return null;
            }
          }
        }
    );

//    myModulesToDispose.add(module);
    return module;
  }

  public Module loadModule(final String modulePath, Project project) {
    return loadModule(new File(modulePath));
  }


  @Nullable
  protected ModuleImpl loadAllModulesUnder(@NotNull VirtualFile rootDir) throws Exception {
    ModuleImpl module = null;
    final VirtualFile[] children = rootDir.getChildren();
    for (VirtualFile child : children) {
      if (child.isDirectory()) {
        final ModuleImpl childModule = loadAllModulesUnder(child);
        if (module == null) module = childModule;
      } else if (child.getName().endsWith(ModuleFileType.DOT_DEFAULT_EXTENSION)) {
        String modulePath = child.getPath();
        module = (ModuleImpl) loadModule(new File(modulePath));
        readJdomExternalizables(module);
      }
    }
    return module;
  }

  protected void readJdomExternalizables(@NotNull final ModuleImpl module) {
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        final ProjectImpl project = (ProjectImpl) myProject;
        project.setOptimiseTestLoadSpeed(false);
        final ModuleRootManagerImpl moduleRootManager = (ModuleRootManagerImpl) ModuleRootManager.getInstance(module);
        module.getStateStore().initComponent(moduleRootManager, false);
        project.setOptimiseTestLoadSpeed(true);
      }
    });
  }

  protected Module createModuleFromTestData(final String dirInTestData, final String newModuleFileName, @NotNull final ModuleType moduleType,
                                            final boolean addSourceRoot)
      throws IOException {
    final File dirInTestDataFile = new File(dirInTestData);
    assertTrue(dirInTestDataFile.isDirectory());
    final File moduleDir = createTempDirectory();
    FileUtil.copyDir(dirInTestDataFile, moduleDir);
    final Module module = createModule(moduleDir + "/" + newModuleFileName, moduleType);
    VirtualFile root = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(moduleDir);
    if (addSourceRoot) {
      PsiTestUtil.addSourceContentToRoots(module, root);
    } else {
      PsiTestUtil.addContentRoot(module, root);
    }
    return module;
  }
}
