/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
import com.intellij.codeInsight.daemon.ProblemHighlightFilter;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.quickFix.LightQuickFixTestCase;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.IntentionManager;
import com.intellij.codeInsight.intention.impl.ShowIntentionActionsHandler;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ModifiableModel;
import com.intellij.codeInspection.ex.InspectionProfileImpl;
import com.intellij.codeInspection.ex.InspectionTool;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.ide.startup.StartupManagerEx;
import com.intellij.ide.startup.impl.StartupManagerImpl;
import com.intellij.lang.ExternalAnnotatorsFilter;
import com.intellij.lang.LanguageAnnotators;
import com.intellij.lang.StdLanguages;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import com.intellij.testFramework.FileTreeAccessFilter;
import com.intellij.testFramework.LightPlatformTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.IncorrectOperationException;
import com.intellij.xml.XmlSchemaProvider;
import gnu.trove.THashMap;
import gnu.trove.TIntArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DaemonAnalyzerTestCase extends CodeInsightTestCase {
  private final Map<String, LocalInspectionTool> myAvailableTools = new THashMap<>();
  private final Map<String, LocalInspectionToolWrapper> myAvailableLocalTools = new THashMap<>();
  private boolean toInitializeDaemon;
  private final FileTreeAccessFilter myFileTreeAccessFilter = new FileTreeAccessFilter();

  @Override
  protected void beforeMethod() throws Exception {
    super.beforeMethod();
    //((VirtualFilePointerManagerImpl) VirtualFilePointerManager.getInstance()).cleanupForNextTest();

    final LocalInspectionTool[] tools = configureLocalInspectionTools();
    for (LocalInspectionTool tool : tools) {
      enableInspectionTool(tool);
    }

    final InspectionProfileImpl profile = new InspectionProfileImpl(LightPlatformTestCase.PROFILE) {
      @Override
      @NotNull
      public ModifiableModel getModifiableModel() {
        mySource = this;
        return this;
      }

      @Override
      @NotNull
      public InspectionProfileEntry[] getInspectionTools(PsiElement element) {
        final Collection<LocalInspectionToolWrapper> tools = myAvailableLocalTools.values();
        return tools.toArray(new LocalInspectionToolWrapper[tools.size()]);
      }

      @Override
      public boolean isToolEnabled(@Nullable HighlightDisplayKey key, PsiElement element) {
        return key != null && myAvailableTools.containsKey(key.toString());
      }

      @NotNull
      @Override
      public HighlightDisplayLevel getErrorLevel(@NotNull HighlightDisplayKey key, PsiElement element) {
        final LocalInspectionTool localInspectionTool = myAvailableTools.get(key.toString());
        return localInspectionTool != null ? localInspectionTool.getDefaultLevel() : HighlightDisplayLevel.WARNING;
      }

      @Override
      public InspectionTool getInspectionTool(@NotNull String shortName, @NotNull PsiElement element) {
        return myAvailableLocalTools.get(shortName);
      }
    };
    final InspectionProfileManager inspectionProfileManager = InspectionProfileManager.getInstance();
    inspectionProfileManager.addProfile(profile);
    inspectionProfileManager.setRootProfile(LightPlatformTestCase.PROFILE);
    Disposer.register(getProject(), new Disposable() {
      @Override
      public void dispose() {
        inspectionProfileManager.deleteProfile(LightPlatformTestCase.PROFILE);
      }
    });
    InspectionProjectProfileManager.getInstance(getProject()).updateProfile(profile);
    InspectionProjectProfileManager.getInstance(getProject()).setProjectProfile(profile.getName());
    DaemonCodeAnalyzerImpl daemonCodeAnalyzer = (DaemonCodeAnalyzerImpl) DaemonCodeAnalyzer.getInstance(getProject());
    toInitializeDaemon = !isRunning(daemonCodeAnalyzer);
    daemonCodeAnalyzer.prepareForTest();
    final StartupManagerImpl startupManager = (StartupManagerImpl) StartupManagerEx.getInstanceEx(getProject());
    startupManager.runStartupActivities();
    startupManager.startCacheUpdate();
    startupManager.runPostStartupActivities();
    DaemonCodeAnalyzerSettings.getInstance().setImportHintEnabled(false);

    if (isPerformanceTest()) {
      IntentionManager.getInstance().getAvailableIntentionActions();  // hack to avoid slowdowns in PyExtensionFactory
      PathManagerEx.getTestDataPath(); // to cache stuff
      ReferenceProvidersRegistry.getInstance(); // preload tons of classes
      InjectedLanguageManager.getInstance(getProject()); // zillion of Dom Sem classes
      LanguageAnnotators.INSTANCE.allForLanguage(StdLanguages.JAVA); // pile of annotator classes loads
      LanguageAnnotators.INSTANCE.allForLanguage(StdLanguages.XML);
      ProblemHighlightFilter.EP_NAME.getExtensions();
      Extensions.getExtensions(ImplicitUsageProvider.EP_NAME);
      Extensions.getExtensions(XmlSchemaProvider.EP_NAME);
      Extensions.getExtensions(XmlFileNSInfoProvider.EP_NAME);
      Extensions.getExtensions(ExternalAnnotatorsFilter.EXTENSION_POINT_NAME);
      Extensions.getExtensions(IndexPatternBuilder.EP_NAME);
    }
  }

  private boolean isRunning(DaemonCodeAnalyzerImpl daemonCodeAnalyzer) {
    try {
      Method method = daemonCodeAnalyzer.getClass().getDeclaredMethod("isRunning");
      method.setAccessible(true);
      return (Boolean)method.invoke(daemonCodeAnalyzer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void afterMethod() throws Exception {
    ((StartupManagerImpl) StartupManager.getInstance(getProject())).checkCleared();
    if (toInitializeDaemon) {
      ((DaemonCodeAnalyzerImpl) DaemonCodeAnalyzer.getInstance(getProject())).cleanupAfterTest(false);
    }
    super.afterMethod();
//    ((VirtualFilePointerManagerImpl) VirtualFilePointerManager.getInstance()).assertPointersDisposed();
  }

  @Override
  protected boolean isRunInWriteAction() {
    return false;
  }

  protected void enableInspectionTool(@NotNull LocalInspectionTool tool) {
    final String shortName = tool.getShortName();
    final HighlightDisplayKey key = HighlightDisplayKey.find(shortName);
    if (key == null) {
      HighlightDisplayKey.register(shortName, tool.getDisplayName(), tool.getID());
    }
    myAvailableTools.put(shortName, tool);
    myAvailableLocalTools.put(shortName, new LocalInspectionToolWrapper(tool));
  }

  protected void enableInspectionToolsFromProvider(@NotNull InspectionToolProvider toolProvider) {
    try {
      for (Class c : toolProvider.getInspectionClasses()) {
        enableInspectionTool((LocalInspectionTool) c.newInstance());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected void disableInspectionTool(String shortName) {
    myAvailableTools.remove(shortName);
    myAvailableLocalTools.remove(shortName);
  }

  protected LocalInspectionTool[] configureLocalInspectionTools() {
    return LocalInspectionTool.EMPTY_ARRAY;
  }

  protected LocalInspectionTool[] createLocalInspectionTools(@NotNull final InspectionToolProvider... provider) {
    final ArrayList<LocalInspectionTool> result = new ArrayList<>();
    for (InspectionToolProvider toolProvider : provider) {
      for (Class aClass : toolProvider.getInspectionClasses()) {
        try {
          final Object tool = aClass.newInstance();
          assertTrue(tool instanceof LocalInspectionTool);
          result.add((LocalInspectionTool) tool);
        } catch (Exception e) {
          LOG.error(e);
        }
      }
    }
    return result.toArray(new LocalInspectionTool[result.size()]);
  }

//  protected Collection<HighlightInfo> doDoTest(boolean checkWarnings, boolean checkInfos) {
//    return doDoTest(checkWarnings, checkInfos, false);
//  }
//
//  protected Collection<HighlightInfo> doDoTest(boolean checkWarnings, boolean checkInfos, boolean checkWeakWarnings) {
//    return checkHighlighting(new ExpectedHighlightingData(getCurrentEditor().getDocument(), checkWarnings, checkWeakWarnings, checkInfos, getCurrentFile()));
//  }
//
//  protected Collection<HighlightInfo> checkHighlighting(final ExpectedHighlightingData data) {
//    PsiDocumentManager.getInstance(myProject).commitAllDocuments();
//
//    //to load text
//    ApplicationManager.getApplication().runWriteAction(new Runnable() {
//      public void run() {
//        TreeUtil.clearCaches((TreeElement) getCurrentFile().getNode());
//      }
//    });
//
//
//    //to initialize caches
//    if (!DumbService.isDumb(getProject())) {
//      myPsiManager.getCacheManager().getFilesWithWord("XXX", UsageSearchContext.IN_COMMENTS, GlobalSearchScope.allScope(myProject), true);
//    }
//
//    Collection<HighlightInfo> infos = doHighlighting();
//    String text = getCurrentEditor().getDocument().getText();
//    data.checkLineMarkers(DaemonCodeAnalyzerImpl.getLineMarkers(getDocument(getCurrentFile()), getProject()), text);
//    data.checkResult(infos, text);
//    return infos;
//  }

  public void allowTreeAccessForFile(final VirtualFile file) {
    myFileTreeAccessFilter.allowTreeAccessForFile(file);
  }

  @NotNull
  protected Collection<HighlightInfo> highlightErrors() {
    return filter(doHighlighting(), HighlightSeverity.ERROR);
  }

  @NotNull
  protected List<HighlightInfo> doHighlighting() {
    PsiDocumentManager.getInstance(myProject).commitAllDocuments();

    TIntArrayList toIgnore = new TIntArrayList();
    if (!doTestLineMarkers()) {
      toIgnore.add(Pass.UPDATE_OVERRIDEN_MARKERS);
      toIgnore.add(Pass.VISIBLE_LINE_MARKERS);
      toIgnore.add(Pass.LINE_MARKERS);
    }

    if (!doExternalValidation()) {
      toIgnore.add(Pass.EXTERNAL_TOOLS);
    }
    if (forceExternalValidation()) {
      toIgnore.add(Pass.LINE_MARKERS);
      toIgnore.add(Pass.LOCAL_INSPECTIONS);
      toIgnore.add(Pass.POPUP_HINTS);
      toIgnore.add(Pass.POST_UPDATE_ALL);
      toIgnore.add(Pass.UPDATE_ALL);
      toIgnore.add(Pass.UPDATE_OVERRIDEN_MARKERS);
      toIgnore.add(Pass.VISIBLE_LINE_MARKERS);
    }

    boolean canChange = canChangeDocumentDuringHighlighting();
    List<HighlightInfo> infos = CodeInsightTestFixtureImpl.instantiateAndRun(getCurrentFile(), getCurrentEditor(), toIgnore.toNativeArray(), canChange);

    if (!canChange) {
      Document document = getDocument(getCurrentFile());
      ((DaemonCodeAnalyzerImpl) DaemonCodeAnalyzer.getInstance(getProject())).getFileStatusMap().assertAllDirtyScopesAreNull(document);
    }

    return infos;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  public @interface CanChangeDocumentDuringHighlighting {
  }

  private boolean canChangeDocumentDuringHighlighting() {
    return annotatedWith(CanChangeDocumentDuringHighlighting.class);
  }

  @NotNull
  public static List<HighlightInfo> filter(@NotNull final List<HighlightInfo> infos, HighlightSeverity minSeverity) {
    ArrayList<HighlightInfo> result = new ArrayList<>();
    for (final HighlightInfo info : infos) {
      if (info.getSeverity().compareTo(minSeverity) >= 0) result.add(info);
    }
    return result;
  }

  protected boolean doTestLineMarkers() {
    return false;
  }

  protected boolean doExternalValidation() {
    return true;
  }

  protected boolean forceExternalValidation() {
    return false;
  }

  protected static void findAndInvokeIntentionAction(@NotNull final Collection<HighlightInfo> infos, String intentionActionName, @NotNull final Editor editor,
                                                     @NotNull final PsiFile file) throws IncorrectOperationException {
    IntentionAction intentionAction = findIntentionAction(infos, intentionActionName, editor, file);

    assertNotNull(intentionActionName, intentionAction);
    assertTrue(ShowIntentionActionsHandler.chooseActionAndInvoke(file, editor, intentionAction, intentionActionName));
  }

  protected static void cannotFindAndInvokeIntentionAction(@NotNull final Collection<HighlightInfo> infos, String intentionActionName, @NotNull final Editor editor,
                                                     @NotNull final PsiFile file) throws IncorrectOperationException {
    IntentionAction intentionAction = findIntentionAction(infos, intentionActionName, editor, file);

    assertNull(intentionActionName, intentionAction);
  }

  protected static IntentionAction findIntentionAction(@NotNull final Collection<HighlightInfo> infos, final String intentionActionName, @NotNull final Editor editor,
                                                       @NotNull final PsiFile file) {
    List<IntentionAction> actions = LightQuickFixTestCase.getAvailableActions(editor, file);
    IntentionAction intentionAction = LightQuickFixTestCase.findActionWithText(actions, intentionActionName);

    if (intentionAction == null) {
      final List<IntentionAction> availableActions = new ArrayList<>();

      for (HighlightInfo info : infos) {
        if (info.quickFixActionRanges != null) {
          for (Pair<HighlightInfo.IntentionActionDescriptor, TextRange> pair : info.quickFixActionRanges) {
            IntentionAction action = pair.first.getAction();
            if (action.isAvailable(file.getProject(), editor, file)) availableActions.add(action);
          }
        }
      }

      intentionAction = LightQuickFixTestCase.findActionWithText(
          availableActions,
          intentionActionName
      );
    }
    return intentionAction;
  }

//  public void checkHighlighting(Editor editor, boolean checkWarnings, boolean checkInfos) {
//    setActiveEditor(editor);
//    doDoTest(checkWarnings, checkInfos);
//  }

  protected void completeBasic() {
    new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(myProject, getCurrentEditor(), 1, false, false);
  }

}
