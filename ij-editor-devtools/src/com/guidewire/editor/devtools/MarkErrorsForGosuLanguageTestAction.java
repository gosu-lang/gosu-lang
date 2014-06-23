/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkErrorsForGosuLanguageTestAction extends TypeSystemAwareAction {

  @Override
  public void actionPerformed( AnActionEvent event ) {
    PsiFile psiFile = LangDataKeys.PSI_FILE.getData( event.getDataContext() );
    if( psiFile instanceof AbstractGosuClassFileImpl ) {
      TypeSystem.pushModule( ((AbstractGosuClassFileImpl)psiFile).getModule() );
      try {
        final AbstractGosuClassFileImpl gsFile = (AbstractGosuClassFileImpl)psiFile;
        ParseResultsException pre = gsFile.getParseData().getClassFileStatement().getGosuClass().getParseResultsException();
        if( pre == null || (!pre.hasParseExceptions() && !pre.hasParseWarnings()) ) {
          return;
        }

        if( !CodeInsightUtilBase.prepareFileForWrite( gsFile ) ) {
          return;
        }

        final Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        for( IParseIssue pi: pre.getParseIssues() ) {
          String issue = pi.getMessageKey().getKey();
          int iLine = pi.getLine();
          List<String> issues = map.get( iLine );
          if( issues == null ) {
            map.put( iLine, issues = new ArrayList<String>() );
          }
          issues.add( issue );
        }
        final List<Integer> lines = new ArrayList<Integer>( map.keySet() );
        Collections.sort( lines );

        ApplicationManager.getApplication().runWriteAction( new Runnable() {
          @Override
          public void run() {
            Document document = gsFile.getViewProvider().getDocument();
            removeOldIssueKeyMarkers( document );
            addIssueKeyMarkers( document, lines, map );
            gsFile.reparsePsiFromContent();
          }
        } );
      }
      finally {
        TypeSystem.popModule( ((AbstractGosuClassFileImpl)psiFile).getModule() );
      }
    }
  }

  private void removeOldIssueKeyMarkers( Document document ) {
    for( int i = 0; i < document.getLineCount(); i++ ) {
      int iStart = document.getLineStartOffset( i );
      int iEnd = document.getLineEndOffset( i );
      String line = document.getText( new TextRange( iStart, iEnd ) );
      int iOffset = line.indexOf( "  //## issuekeys:" );
      if( iOffset >= 0 ) {
        document.replaceString( iStart + iOffset, iEnd, "" );
      }
    }
  }

  private void addIssueKeyMarkers( Document document, List<Integer> lines, Map<Integer, List<String>> map ) {
    for( int iLine : lines ) {
      String issues = makeIssueString( map.get( iLine ) );
      int iEnd = document.getLineEndOffset( iLine - 1 );
      document.insertString( iEnd, issues );
    }
  }

  private String makeIssueString( List<String> issues ) {
    StringBuilder sb = new StringBuilder();
    for( String issue: issues ) {
      sb.append( sb.length() != 0 ? ", " : "" ).append( issue );
    }
    sb.insert( 0, "  //## issuekeys: " );
    return sb.toString();
  }
}
