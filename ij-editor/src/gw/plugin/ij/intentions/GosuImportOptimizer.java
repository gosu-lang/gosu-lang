/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.lang.ImportOptimizer;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import org.jetbrains.annotations.NotNull;

public class GosuImportOptimizer implements ImportOptimizer {
  public boolean supports(PsiFile file) {
    return file instanceof IGosuFile;
  }

  @NotNull
  public Runnable processFile(PsiFile file) {
    return new ImportProcessor((IGosuFile) file);
  }

  private static class ImportProcessor implements Runnable {
    private final IGosuFile _gosuFile;
    private GosuImportReferenceAnalyzer _importAnalyzer;

    private ImportProcessor(IGosuFile file) {
      _gosuFile = file;
    }

    public void run() {
      if(_gosuFile.getName().endsWith(".gst")) {
        //todo: see PL-27628
        return;
      }
      final PsiDocumentManager documentManager = PsiDocumentManager.getInstance(_gosuFile.getProject());
      final Document document = documentManager.getDocument(_gosuFile);
      if (document != null) {
        documentManager.commitDocument(document);
      }

      _importAnalyzer = new GosuImportReferenceAnalyzer(_gosuFile);
      _importAnalyzer.analyze();

      _importAnalyzer.processImports();

      final IGosuUsesStatementList oldUsesList = PsiTreeUtil.findChildOfType(_gosuFile, IGosuUsesStatementList.class);
      final IGosuUsesStatementList newUsesList = _importAnalyzer.getNewImportList();
      if (oldUsesList != null) {
        if (newUsesList == null) {
          oldUsesList.delete();
        } else {
          oldUsesList.replace(newUsesList);
        }
      }
    }
  }
}
