/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.project

uses com.intellij.openapi.module.Module
uses com.intellij.openapi.module.ModuleManager
uses com.intellij.openapi.module.StdModuleTypes
uses com.intellij.openapi.roots.ModuleOrderEntry
uses com.intellij.openapi.roots.ModuleRootManager
uses com.intellij.openapi.roots.OrderRootType
uses com.intellij.openapi.roots.impl.ProjectRootManagerImpl
uses com.intellij.openapi.roots.impl.RootModelImpl
uses com.intellij.openapi.roots.impl.libraries.LibraryImpl
uses com.intellij.openapi.vfs.VfsUtil
uses gw.lang.reflect.TypeSystem
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.sdk.GosuSdkAdditionalData
uses gw.plugin.ij.util.GosuModuleUtil
uses gw.plugin.ij.util.ExecutionUtil
uses gw.test.AssertUtil
uses gw.testharness.Disabled

uses java.io.File

class ModuleModificationTest extends GosuTestCase {

  override function runInDispatchThread(): boolean {
    return false
  }

  function testAddingModule() {
    addModule("NewName")
    assertNotNull(TypeSystem.getExecutionEnvironment().getModule("NewName"))
  }

  function testRemovingModule() {
    addModule("NewName")
    var m = TypeSystem.getExecutionEnvironment().getModule("NewName")
    assertNotNull(m)
    removeModule(m.NativeModule as Module )
    assertNull(TypeSystem.getExecutionEnvironment().getModule("NewName"))
  }

  function testRenamingModule() {
    var oldName = Module.Name
    var oldModule = TypeSystem.getExecutionEnvironment().getModule(oldName)

    renameModule(Module, "NewName")
    assertEquals("NewName", Module.Name)
    assertEquals("NewName", oldModule.Name)

    renameModule(Module, oldName)
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testAddingJarToModule() {
    AssertUtil.assertCollectionEquals({}, GosuModule.getJavaClassPath())
    var jarPath = addJar()
    AssertUtil.assertCollectionEquals({jarPath}, GosuModule.getJavaClassPath())
    removeJar()
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testRemovingJarFromModule() {
    var jarPath = addJar()
    AssertUtil.assertCollectionEquals({jarPath}, GosuModule.getJavaClassPath())
    removeJar()
    AssertUtil.assertCollectionEquals({}, GosuModule.getJavaClassPath())
  }

  function testAddingModuleDependency() {
    var newModule = addModule("NewModule")
    AssertUtil.assertCollectionEquals({"_jre_module_"}, GosuModule.Dependencies.map(\elt -> elt.Module.Name))
    addDependency(Module, newModule)
    AssertUtil.assertCollectionEquals({"NewModule", "_jre_module_"}, GosuModule.Dependencies.map(\elt -> elt.Module.Name))
    removeDependency()
    AssertUtil.assertCollectionEquals({"_jre_module_"}, GosuModule.Dependencies.map(\elt -> elt.Module.Name))
  }

  function testRemovingModuleDependency() {
    addModule("NewModule")
    var newModule = TypeSystem.getExecutionEnvironment().getModule("NewModule").NativeModule as Module
    addDependency(Module, newModule)
    AssertUtil.assertCollectionEquals({"NewModule", "_jre_module_"}, GosuModule.Dependencies.map(\elt -> elt.Module.Name))
    removeDependency()
    AssertUtil.assertCollectionEquals({"_jre_module_"}, GosuModule.Dependencies.map(\elt -> elt.Module.Name))
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testChangingModuleDependencyOrder() {
    var A = addModule("A")
    var B = addModule("B")
    var C = addModule("C")
    addDependency(A, B)
    addDependency(A, C)
    var gA = TypeSystem.getExecutionEnvironment().getModule("A")

    AssertUtil.assertCollectionEquals({"B", "C", "_jre_module_"}, gA.Dependencies.map(\elt -> elt.Module.Name))

    makeDependencyLast(A, B)

    AssertUtil.assertCollectionEquals({"C", "B", "_jre_module_"}, gA.Dependencies.map(\elt -> elt.Module.Name))

    removeModule(A)
    removeModule(B)
    removeModule(C)
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testExportingModule() {
    var A = addModule("A")
    var B = addModule("B")
    var C = addModule("C")
    addDependency(A, B, true)
    addDependency(B, C, false)
    var gA = TypeSystem.getExecutionEnvironment().getModule("A")
    var gB = TypeSystem.getExecutionEnvironment().getModule("B")
    var gC = TypeSystem.getExecutionEnvironment().getModule("C")

    AssertUtil.assertCollectionEquals({"B", "_jre_module_"}, gA.Dependencies.map(\elt -> elt.Module.Name))
    AssertUtil.assertCollectionEquals({"C", "_jre_module_"}, gB.Dependencies.map(\elt -> elt.Module.Name))
    AssertUtil.assertCollectionEquals({"_jre_module_"}, gC.Dependencies.map(\elt -> elt.Module.Name))
    assertTrue(gA.Dependencies.where( \ d -> d.Module.Name == "B").single().Exported)
    assertFalse(gB.Dependencies.where( \ d -> d.Module.Name == "C").single().Exported)

    exportDependency(B, C, true);

    assertTrue(gB.Dependencies.where( \ d -> d.Module.Name == "C").single().Exported)

    removeModule(A)
    removeModule(B)
    removeModule(C)
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testAddingSourceFolder() {
    AssertUtil.assertCollectionEquals({"src"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    addSourceFolder("src2")
    AssertUtil.assertCollectionEquals({"src", "src2"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    removeSourceFolder("src2")
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testRemovingSourceFolder() {
    AssertUtil.assertCollectionEquals({"src"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    addSourceFolder("src2")
    AssertUtil.assertCollectionEquals({"src", "src2"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    removeSourceFolder("src2")
    AssertUtil.assertCollectionEquals({"src"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
  }

  @Disabled("dpetrusca", "Only supported in open source")
  function testRenamingSourceFolder() {
    AssertUtil.assertCollectionEquals({"src"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    renameSourceFolder("src", "src2")
    AssertUtil.assertCollectionEquals({"src2"}, GosuModule.SourcePath.map( \ elt -> elt.Name))
    renameSourceFolder("src2", "src")
  }

  // private

  function renameSourceFolder(folder: String, newFolder: String) {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushModule(GosuModuleUtil.getModule(Module))
      try {
        var modulePath = Module.ModuleFile.Parent.findChild(Module.Name)
        var srcFolder = modulePath.findChild(folder)
        srcFolder.rename(this, newFolder)
      } finally {
        TypeSystem.popModule(GosuModuleUtil.getModule(Module))
      }
    }, true)
  }

  function addSourceFolder(folder: String) {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushModule(GosuModuleUtil.getModule(Module))
      try {
        var modulePath = Module.ModuleFile.Parent.findChild(Module.Name)
        var srcFolder = modulePath.createChildDirectory(this, folder)
        var model = ModuleRootManager.getInstance(Module).ModifiableModel as RootModelImpl
        var contentEntry = model.ContentEntries[0]
        contentEntry.addSourceFolder(srcFolder, false)
        model.commit()
      } finally {
        TypeSystem.popModule(GosuModuleUtil.getModule(Module))
      }
    }, true)
  }

  function removeSourceFolder(folder: String) {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushModule(GosuModuleUtil.getModule(Module))
      try {
        var model = ModuleRootManager.getInstance(Module).ModifiableModel as RootModelImpl
        var contentEntry = model.ContentEntries[0]
        for (f in contentEntry.SourceFolders) {
          if (f.File.Name.equals(folder)) {
            contentEntry.removeSourceFolder(f)
            f.File.delete(this);
          }
        }
        model.commit()
      } finally {
        TypeSystem.popModule(GosuModuleUtil.getModule(Module))
      }
    }, true)
  }

  static function addDependency(parent: Module, m: Module) {
    addDependency(parent, m, true)
  }

  static function addDependency(parent: Module, m: Module, exported: boolean) {
    runWriteActionInDispatchThread(\-> {
      var model = ModuleRootManager.getInstance(parent).ModifiableModel as RootModelImpl
      var entry = model.addModuleOrderEntry(m)
      entry.setExported(exported)
      model.commit()
    }, true)
  }

  static function exportDependency(parent: Module, m: Module, exported: boolean) {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushGlobalModule()
      try {
        var model = ModuleRootManager.getInstance(parent).ModifiableModel as RootModelImpl
        for (e in model.OrderEntries) {
          if (e typeis ModuleOrderEntry && e.Module == m) {
            e.setExported(exported)
          }
        }
        model.commit()
      } finally {
        TypeSystem.popGlobalModule()
      }
    }, true)
  }

  static function makeDependencyLast(parent: Module, m: Module) {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushGlobalModule()
      try {
        var model = ModuleRootManager.getInstance(parent).ModifiableModel as RootModelImpl
        var d = model.OrderEntries.where(\e -> e typeis ModuleOrderEntry && e.Module == m).single()
        model.removeOrderEntry(d);
        model.addOrderEntry(d)
        model.commit()
      } finally {
        TypeSystem.popGlobalModule()
      }
    }, true)
  }

  function removeDependency() {
    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushModule(GosuModuleUtil.getModule(Module))
      try {
        var model = ModuleRootManager.getInstance(Module).ModifiableModel as RootModelImpl
        for (e in model.OrderEntries) {
          if (e typeis ModuleOrderEntry) {
            model.removeOrderEntry(e)
          }
        }
        model.commit()
      } finally {
        TypeSystem.popModule(GosuModuleUtil.getModule(Module))
      }
    }, true)
  }

  function addJar(): String {
    var jarPath: String = null;

    runWriteActionInDispatchThread(\-> {
      TypeSystem.pushModule(GosuModuleUtil.getModule(Module))
      try {
        var manager = ProjectRootManagerImpl.getInstance(Project)
        var javaSdk = (manager.ProjectSdk.SdkAdditionalData as GosuSdkAdditionalData).JavaSdk
        jarPath = javaSdk.HomePath + File.separator + "lib" + File.separator + "tools.jar"
        var model = ModuleRootManager.getInstance(Module).ModifiableModel as RootModelImpl
        var lib = model.ModuleLibraryTable.createLibrary("myLib") as LibraryImpl
        var modifiableLib = lib.getModifiableModel()
        var jar = VfsUtil.findFileByURL(new File(jarPath).toURL())
        modifiableLib.addRoot(jar, OrderRootType.CLASSES)
        modifiableLib.commit()
        model.commit()
      } finally {
        TypeSystem.popModule(GosuModuleUtil.getModule(Module))
      }
    }, true)

    return jarPath
  }

  function removeJar() {
    runWriteActionInDispatchThread(\-> {
      var model = ModuleRootManager.getInstance(Module).ModifiableModel as RootModelImpl
      var lib = model.ModuleLibraryTable.getLibraryByName("myLib") as LibraryImpl
      model.ModuleLibraryTable.removeLibrary(lib)
      model.commit()
    }, true)
  }

  function addModule(moduleName: String): Module {
    var path = Module.ModuleFile.Path
    path = path.substring(0, path.lastIndexOf('/'))
    new File(path, moduleName).mkdir()
    var iml = new File(path, moduleName + ".iml")
    iml.createNewFile()

    var model = ModuleManager.getInstance(Project).ModifiableModel
    runWriteActionInDispatchThread(\-> {
      model.newModule(iml.AbsolutePath, StdModuleTypes.JAVA.Id)
      model.commit()
    }, true)

    return TypeSystem.getExecutionEnvironment().getModule(moduleName).NativeModule as Module
  }

  function removeModule(m: Module) {
    var model = ModuleManager.getInstance(Project).ModifiableModel
    runWriteActionInDispatchThread(\-> {
      model.disposeModule(m)
      model.commit()
    }, true)
  }

  function renameModule(m: Module, newName: String) {
    var model = ModuleManager.getInstance(Project).ModifiableModel
    runWriteActionInDispatchThread(\-> {
      model.renameModule(m, newName)
      model.commit()
    }, true)
  }
}
