/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.codeInsight.CodeInsightTestData;
import com.intellij.codeInsight.EditorInfo;
import com.intellij.codeInsight.TargetElementUtilBase;
import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionProgressIndicator;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter;
import com.intellij.ide.DataManager;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.testFramework.PsiTestData;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.UsageTargetUtil;
import com.intellij.util.ui.UIUtil;
import gw.config.CommonServices;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.core.IDEAPlatformHelper;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.framework.FileMarkers;
import gw.plugin.ij.framework.MarkerType;
import gw.plugin.ij.framework.SmartTextRange;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mike
 */
public abstract class CodeInsightTestCase extends PsiTestCase {
  private final Map<PsiFile, FileMarkers> myMarkers = new HashMap<>();
  private final List<Editor> editors = new ArrayList<>();
  @Nullable
  private Editor currentEditor;

  @Override
  protected void afterClass() throws Exception {
    try {
      myMarkers.clear();
    } finally {
      super.afterClass();
    }
  }

  @Override
  protected void beforeMethod() throws Exception {
    deleteAllFiles();
    super.beforeMethod();    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  protected void afterMethod() throws Exception {
    FileEditorManager editorManager = FileEditorManager.getInstance(myProject);
    VirtualFile[] openFiles = editorManager.getOpenFiles();
    for (VirtualFile openFile : openFiles) {
      editorManager.closeFile(openFile);
    }
    currentEditor = null;
    super.afterMethod();
  }

  @Nullable
  protected Editor createEditor(@NotNull VirtualFile file) {
    final FileEditorManager instance = FileEditorManager.getInstance(myProject);

    if (file.getFileType().isBinary()) return null;

    Editor editor = instance.openTextEditor(new OpenFileDescriptor(myProject, file, 0), false);
    ((EditorImpl) editor).setCaretActive();

    ((IDEAPlatformHelper)CommonServices.getPlatformHelper()).fileOpened(FileEditorManager.getInstance(getProject()), file);
    return editor;
  }

  @NotNull
  @Override
  protected PsiTestData createData() {
    return new CodeInsightTestData();
  }

  public FileMarkers getMarkersForCurrentFile() {
    return myMarkers.get(currentEditor);
  }

  public FileMarkers getMarkers(PsiFile file) {
    return myMarkers.get(file);
  }

  @NotNull
  public FileMarkers getAllMarkers(@NotNull PsiFile[] psiFiles) {
    FileMarkers allMarkers = new FileMarkers();
    for (PsiFile psiFile : psiFiles) {
      allMarkers.add(myMarkers.get(psiFile));
    }
    return allMarkers;
  }

  protected String configureMarker(@NotNull String text, @NotNull FileMarkers myFileMarkers) {
    CodeTokenizer codeTokenizer = new CodeTokenizer(text, new MarkerType[]{MarkerType.DELTA_START, MarkerType.DELTA_END});
    int deltaStartIndex = -1;
    while (codeTokenizer.next()) {
      if (codeTokenizer.markerType == MarkerType.DELTA_START) {
        deltaStartIndex = codeTokenizer.markerIndex;
      } else if (codeTokenizer.markerType == MarkerType.DELTA_END) {
        myFileMarkers.addDelta(deltaStartIndex, codeTokenizer.markerIndex, text.substring(deltaStartIndex, codeTokenizer.markerIndex));
      }
    }

    for (SmartTextRange delta : myFileMarkers.getDeltas()) {
      delta.setText(codeTokenizer.text.substring(delta.getStartOffset(), delta.getEndOffset()));
    }
    myFileMarkers.setTextWithDeltas(codeTokenizer.text);
    codeTokenizer.removeDeltas(myFileMarkers.getDeltas());

    codeTokenizer = new CodeTokenizer(codeTokenizer.text);
    int rangeStartIndex = -1;

    while (codeTokenizer.next()) {
      if (codeTokenizer.markerType == MarkerType.CARET1 ||
          codeTokenizer.markerType == MarkerType.CARET2 ||
          codeTokenizer.markerType == MarkerType.CARET3 ||
          codeTokenizer.markerType == MarkerType.CARET4) {
        myFileMarkers.addCaret(codeTokenizer.markerIndex, codeTokenizer.markerType);
      } else if (codeTokenizer.markerType == MarkerType.RANGE_START) {
        rangeStartIndex = codeTokenizer.markerIndex;
      } else if (codeTokenizer.markerType == MarkerType.RANGE_END) {
        myFileMarkers.addRange(rangeStartIndex, codeTokenizer.markerIndex, text.substring(rangeStartIndex, codeTokenizer.markerIndex));
      }
    }

    for (SmartTextRange range : myFileMarkers.getRanges()) {
      range.setText(codeTokenizer.text.substring(range.getStartOffset(), range.getEndOffset()));
    }
    myFileMarkers.setText(codeTokenizer.text);

    return codeTokenizer.text;
  }

  public void deleteFile(@NotNull final VirtualFile file) {
    new WriteCommandAction(getProject()) {
      protected void run(Result result) throws Throwable {
        ((IDEAPlatformHelper)CommonServices.getPlatformHelper()).fileClosed(FileEditorManager.getInstance(getProject()), file);
        file.delete(null);
      }
    }.execute();
  }

  public void deleteAllFiles() {
    deleteAllFiles(getTestClassName());
  }

  public void deleteAllFiles(String moduleName) {
    final Module module = ModuleManager.getInstance(getProject()).findModuleByName(moduleName);
    if(module != null) {
      TypeSystem.refresh(TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( getProject() ) ).getModule(moduleName));
      new WriteCommandAction(getProject()) {
        protected void run(Result result) throws Throwable {
          ModifiableRootModel mRoot = ModuleRootManager.getInstance(module).getModifiableModel();
          for (VirtualFile srcFolder : mRoot.getSourceRoots()) {
            deleteFiles(srcFolder);
          }
          mRoot.dispose();
        }
      }.execute();
    } else {
      System.out.println("Could not find module to delete files from " + moduleName);
    }
  }

  private void deleteFiles(@NotNull VirtualFile file) throws IOException {
    if (file.isDirectory()) {
      for (VirtualFile child : file.getChildren()) {
        deleteFiles(child);
      }
    } else {
      ((IDEAPlatformHelper)CommonServices.getPlatformHelper()).fileClosed(FileEditorManager.getInstance(getProject()), file);
      // We have to pass FileDocumentManager.getInstance() here. Otherwise FileBasedIndexImpl will not clean in-memory
      // storage, since it has a check saying:
      // (requestor instanceof FileDocumentManager || requestor instanceof PsiManager || requestor == LocalHistory.VFS_EVENT_REQUESTOR)
      // This causes some weird confusion inside IntelliJ, when new stub is saved into a file, but stale one is read
      // from in-memory storage (left by the previous test).
      file.delete(FileDocumentManager.getInstance());
    }
  }

  public PsiFile configureByText(@NotNull @NonNls final String fileName, @NotNull @NonNls final String text) {
    assertFalse("Multiple modules exists, please specify the module.", _myModuleDependencies != null && _myModuleDependencies.size() > 0);
    return this.configureByText(getTestClassName(), fileName, text);
  }

  public PsiFile configureByText(@NonNls final String moduleName, @NotNull @NonNls final String fileName, @NotNull @NonNls final String text) {
    final Module m = getModuleByName(moduleName);
    assertNotNull("unable to find module " + moduleName, m);

    FileMarkers myFileMarkers = new FileMarkers();
    final String fileText = configureMarker(text, myFileMarkers);

    new WriteCommandAction(getProject()) {
      @Override
      protected void run(Result result) throws Throwable {
        final VirtualFile vFile;
        ModifiableRootModel mRoot = ModuleRootManager.getInstance(m).getModifiableModel();
        SourceFolder srcFolder = mRoot.getContentEntries()[0].getSourceFolders()[0];
        VirtualFile root = srcFolder.getFile();
        //System.out.println("Creating " + fileName + " in module " + moduleName + " at " + m.getModuleFile().getPath() + "in root " + root.getPath());
        mRoot.dispose();

        root.refresh(false, false);
        String pkg;
        String name = fileName;
        int index = name.indexOf('/');
        while (index >= 0) {
          pkg = name.substring(0, index);
          name = name.substring(index + 1);
          VirtualFile dir = root.findChild(pkg);
          if (dir == null) {
            root = root.createChildDirectory(this, pkg);
            root.refresh(false, false);
          } else
            root = dir;
          index = name.indexOf('/');
        }
        vFile = root.findOrCreateChildData(this, name);

        final Document document = FileDocumentManager.getInstance().getCachedDocument(vFile);
        if (document != null) {
          FileDocumentManager.getInstance().saveDocument(document);
        }

        VfsUtil.saveText(vFile, fileText);
        vFile.setBinaryContent(fileText.getBytes(vFile.getCharset()), 0, 0, null);

        configureByExistingFile(vFile);

        // AHK - Try clearing caches to see if that helps anything
        CommonServices.getFileSystem().clearAllCaches();

        String fqn = fileName.replace('/', '.').substring(0, fileName.lastIndexOf('.'));
        //TODO-dp refresh only the newly created file
        final IModule module = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( getProject() ) ).getModule(m.getName());
        TypeSystem.refresh(module);
        TypeSystem.created(FileUtil.toIFile(vFile));
        assertNotNull("Newly created type could not be loaded: " + fqn  + " at location " + vFile.getPath(), TypeSystem.getByFullNameIfValid(fqn, module));

        // TODO-dp I don't know why without this line sometimes the class cannot be later found in the JavaPsiFacade
        // looks like the class disappers from the JavaFullClassNameIndex
        JavaPsiFacadeUtil.findClass(getProject(), fqn, GlobalSearchScope.allScope(getProject()));

        for (Facet facet : FacetManager.getInstance(m).getAllFacets()) {
          m.getMessageBus().syncPublisher(FacetManager.FACETS_TOPIC).facetConfigurationChanged(facet);
        }
      }
    }.execute();

    myFileMarkers.setEditor(currentEditor);
    myMarkers.put(getCurrentFile(), myFileMarkers);

    return getCurrentFile();
  }

  public static class CodeTokenizer {
    @NotNull
    private final int[] indexes;
    private String text;
    private boolean ended = false;
    public int markerIndex = 0;
    public MarkerType markerType;
    @NotNull
    public final MarkerType[] markerTypes;

    public CodeTokenizer(String text) {
      this(text, MarkerType.values());
    }

    public CodeTokenizer(String text, @NotNull MarkerType[] markerTypes) {
      this.text = text;
      this.markerTypes = markerTypes;
      this.indexes = new int[markerTypes.length];
    }

    public boolean next() {
      if (ended) {
        throw new RuntimeException("No more tokens");
      }

      boolean found = false;
      for (int i = 0; i < indexes.length; i++) {
        indexes[i] = text.indexOf(markerTypes[i].markerText, markerIndex);
        found |= indexes[i] >= 0;
      }

      if (!found) {
        ended = true;
        return false;
      }

      markerIndex = Integer.MAX_VALUE;
      int markerTypeIndex = -1;
      for (int i = 0; i < indexes.length; i++) {
        int min = Math.min(markerIndex, indexes[i] >= 0 ? indexes[i] : Integer.MAX_VALUE);
        if (min < markerIndex) {
          markerIndex = min;
          markerTypeIndex = i;
        }
      }
      markerType = markerTypes[markerTypeIndex];
      text = text.substring(0, markerIndex) + text.substring(markerIndex + markerType.markerText.length());

      return true;
    }

    public void removeDeltas(@NotNull List<SmartTextRange> deltas) {
      int offsetDelta = 0;
      for (SmartTextRange delta : deltas) {
        text = text.substring(0, delta.getStartOffset() - offsetDelta) + text.substring(delta.getEndOffset() - offsetDelta);
        offsetDelta = delta.getEndOffset() - delta.getStartOffset() + 1;
      }
    }
  }


  @Override
  protected String getTestDataPath() {
    return PathManagerEx.getTestDataPath();
  }


  protected void configureByExistingFile(@NotNull final VirtualFile virtualFile) {
    final Editor editor = createEditor(virtualFile);
    editors.add(editor);
    currentEditor = editor;

    final Document document = editor.getDocument();
    final EditorInfo editorInfo = new EditorInfo(document.getText());

    final String newFileText = editorInfo.getNewFileText();
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        if (!document.getText().equals(newFileText)) {
          document.setText(newFileText);
        }
        editorInfo.applyToEditor(editor);
      }
    });

    PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
  }

  protected final void setActiveEditor(Editor editor) {
    currentEditor = editor;
  }

  protected void checkResult(@NotNull String afterText) {
    PsiDocumentManager.getInstance(myProject).commitAllDocuments();

    final String textExpected = configureMarker(afterText, new FileMarkers());
    final String actualText = getCurrentFile().getText();

    assertEquals("result text is different", textExpected, actualText);

    // todo: add range check
//    RangeMarker rangeMarker = myEditor.getDocument().getRangeGuard(1,1);
//    rangeMarker.
  }

  @Override
  public Object getData(String dataId) {
    return PlatformDataKeys.EDITOR.is(dataId) ? currentEditor : super.getData(dataId);
  }

  @Nullable
  protected VirtualFile getVirtualFile(@NonNls String filePath) {
    String fullPath = getTestDataPath() + filePath;

    final VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(fullPath.replace(File.separatorChar, '/'));
    assertNotNull("file " + fullPath + " not found", vFile);
    return vFile;
  }

  @Nullable
  public Editor getCurrentEditor() {
    return currentEditor;
  }

  public PsiFile getCurrentFile() {
    return getPsiFile(currentEditor);
  }

  protected static void type(char c, Editor editor) {
    EditorActionManager actionManager = EditorActionManager.getInstance();
    DataContext dataContext = DataManager.getInstance().getDataContext();
    if (c == '\n') {
      actionManager.getActionHandler(IdeActions.ACTION_EDITOR_ENTER).execute(editor, dataContext);
      return;
    }
    TypedAction action = actionManager.getTypedAction();
    action.actionPerformed(editor, c, dataContext);
  }

  protected void caretRight() {
    EditorActionManager actionManager = EditorActionManager.getInstance();
    EditorActionHandler action = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT);
    action.execute(getCurrentEditor(), DataManager.getInstance().getDataContext());
  }

  protected void deleteLine() {
    EditorActionManager actionManager = EditorActionManager.getInstance();
    EditorActionHandler action = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_DELETE_LINE);
    action.execute(getCurrentEditor(), DataManager.getInstance().getDataContext());
  }

  protected void type(@NotNull @NonNls String s, Editor editor) {
    for (char c : s.toCharArray()) {
      type(c, editor);
    }
  }

  protected void undo() {
    UndoManager undoManager = UndoManager.getInstance(myProject);
    TextEditor textEditor = TextEditorProvider.getInstance().getTextEditor(getCurrentEditor());
    undoManager.undo(textEditor);
  }

  /**
   * ActionsIDs are in IdeActions.
   */
  protected void runAction(@NotNull final String actionID, @NotNull final Editor editor) {
    CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
      @Override
      public void run() {
        EditorActionManager actionManager = EditorActionManager.getInstance();
        EditorActionHandler actionHandler = actionManager.getActionHandler(actionID);

        actionHandler.execute(editor, DataManager.getInstance().getDataContext());
      }
    }, actionID, editor.getDocument());
  }

  protected void ctrlShiftF7(PsiFile file) {
    HighlightUsagesHandler.invoke(getProject(), getCurrentEditor(), file);
  }

  @Nullable
  protected UsageTarget[] getOccurencesInCurrentFile() {
    Editor editor = getCurrentEditor();
    Project project = getProject();
    PsiFile file = getCurrentFile();
//    HighlightUsagesHandler.invoke(project, editor, file);

    PsiDocumentManager.getInstance(project).commitAllDocuments();

    SelectionModel selectionModel = editor.getSelectionModel();
    if (file == null && !selectionModel.hasSelection()) {
      selectionModel.selectWordAtCaret(false);
    }

    UsageTarget[] usageTargets = UsageTargetUtil.findUsageTargets(editor, file);

    if (usageTargets == null) {
      PsiElement targetElement = getTargetElement(editor, file);
      if (targetElement != null) {
        if (!(targetElement instanceof NavigationItem)) {
          targetElement = targetElement.getNavigationElement();
        }
        if (targetElement instanceof NavigationItem) {
          usageTargets = new UsageTarget[]{new PsiElement2UsageTargetAdapter(targetElement)};
        }
      }
    }

    if (usageTargets == null) {
      PsiReference ref = TargetElementUtilBase.findReference(editor);

      if (ref instanceof PsiPolyVariantReference) {
        ResolveResult[] results = ((PsiPolyVariantReference) ref).multiResolve(false);

        if (results.length > 0) {
          usageTargets = new UsageTarget[results.length];
          for (int i = 0; i < results.length; ++i) {
            usageTargets[i] = new PsiElement2UsageTargetAdapter(results[i].getElement());
          }
        }
      }
    }

    if (usageTargets == null) {
      if (file.findElementAt(editor.getCaretModel().getOffset()) instanceof PsiWhiteSpace) return usageTargets;
      selectionModel.selectWordAtCaret(false);
      String selection = selectionModel.getSelectedText();
      LOG.assertTrue(selection != null);
      for (int i = 0; i < selection.length(); i++) {
        if (!Character.isJavaIdentifierPart(selection.charAt(i))) {
          selectionModel.removeSelection();
          return usageTargets;
        }
      }
      return null;
    }

    return usageTargets;
  }

  @Nullable
  private static PsiElement getTargetElement(@NotNull Editor editor, @NotNull PsiFile file) {
    PsiElement target = TargetElementUtilBase.findTargetElement(editor, TargetElementUtilBase.getInstance().getReferenceSearchFlags());

    if (target == null) {
      int offset = TargetElementUtilBase.adjustOffset(editor.getDocument(), editor.getCaretModel().getOffset());
      PsiElement element = file.findElementAt(offset);
      if (element == null) return null;
    }

    return target;
  }

  public static void ctrlW() {
    AnAction action = ActionManager.getInstance().getAction(IdeActions.ACTION_EDITOR_SELECT_WORD_AT_CARET);
    DataContext dataContext = DataManager.getInstance().getDataContext();
    AnActionEvent event = new AnActionEvent(null, dataContext, "", action.getTemplatePresentation(), ActionManager.getInstance(), 0);
    event.setInjectedContext(true);
    action.actionPerformed(event);
  }

  @NotNull
  protected PsiClass findClass(@NotNull @NonNls final String name) {
    final PsiClass aClass = JavaPsiFacadeUtil.findClass(getProject(), name, ProjectScope.getProjectScope(getProject()));
    assertNotNull("Class " + name + " not found", aClass);
    return aClass;
  }

  @NotNull
  protected PsiPackage findPackage(@NotNull @NonNls final String name) {
    final PsiPackage aPackage = JavaPsiFacadeUtil.findPackage(getProject(), name);
    assertNotNull("Package " + name + " not found", aPackage);
    return aPackage;
  }

  public String normalize(@NotNull String text) {
    return text.replace(" ", "");
  }

  public LookupElement[] complete(final CompletionType type, final int invocationCount, final Editor editor, final String stuffToType) {
    final List<LookupElement[]> savedItems = new ArrayList<>(1);

    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      public void run() {
        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
          public void run() {
            final CodeCompletionHandlerBase handler = new CodeCompletionHandlerBase(type) {
//              @Override
//              protected PsiFile createFileCopy(final PsiFile file) {
//                final PsiFile copy = super.createFileCopy(file);
//                if (myFileContext != null) {
//                  final PsiElement contextCopy = myFileContext.copy();
//                  final PsiFile containingFile = contextCopy.getContainingFile();
//                  if (containingFile instanceof PsiFileImpl) {
//                    ((PsiFileImpl)containingFile).setOriginalFile(myFileContext.getContainingFile());
//                  }
//                  setContext(copy, contextCopy);
//                }
//                return copy;
//              }

              @Override
              protected void completionFinished(
                  int offset1, int offset2, CompletionProgressIndicator indicator, LookupElement[] items, boolean hasModifiers) {
                savedItems.add(0, items);
                super.completionFinished(offset1, offset2, indicator, items, hasModifiers);
                if (stuffToType != null) {
                  type(stuffToType, editor);
                }
              }
            };
            handler.invokeCompletion(getProject(), editor, invocationCount);
          }
        }, null, null);
      }
    });

    return savedItems.get(0);
  }

  protected void selectItem(LookupElement item, char ch) {
    final LookupImpl lookup = (LookupImpl) LookupManager.getInstance(myProject).getActiveLookup();
    assert lookup != null;
    lookup.setCurrentItem(item);
    lookup.finishLookup(ch);
  }

  public void selectItem(LookupElement item) {
    selectItem(item, (char)0);
  }
}
