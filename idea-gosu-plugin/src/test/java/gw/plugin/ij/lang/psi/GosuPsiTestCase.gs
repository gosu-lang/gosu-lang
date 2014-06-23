/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

uses com.intellij.psi.impl.source.PsiFileImpl
uses gw.plugin.ij.framework.GosuTestCase

uses java.lang.StringBuilder
uses java.util.StringTokenizer

class GosuPsiTestCase extends GosuTestCase
{
  private function _assertPsiTextEqualsSource( strFileName: String, strSource: String )
  {
    var psiFile = configureByText( strFileName, strSource )
//    var documentManager = PsiDocumentManagerImpl.getInstance( psiFile.Project ) as PsiDocumentManagerImpl
//    var document = documentManager.getCachedDocument( psiFile )
//    documentManager.documentChanged( new DocumentEventImpl( document, 0, strSource, strSource, 0, true ) );
//    documentManager.commitDocument( document );
    var children = (psiFile as PsiFileImpl).calcTreeElement().Psi.Children // force psi creation
    var strPsiText = (psiFile as PsiFileImpl).calcTreeElement().Text
    print( strPsiText )
    assertEquals( strSource, strPsiText )
  }

  protected function assertIncremental_PsiTextEqualsSource( strFileName: String, strSource: String )
  {
    incrementallyAddEachChar( strFileName, strSource )
    incrementallyRemoveASingleToken( strFileName, strSource )
    incrementallyBackspaceOverEachLine( strFileName, strSource )
  }

  protected function incrementallyAddEachChar( strFileName: String, strSource: String )
  {
    for( i in 0..strSource.length() )
    {
      var strSourceIncrement = strSource.substring( 0, i )
      _assertPsiTextEqualsSource( strFileName, strSourceIncrement )
    }
  }

  protected function incrementallyBackspaceOverEachLine( strFileName: String, strSource: String )
  {
    var sb = new StringBuilder( strSource )
    var lines = strSource.split( "\n" )
    var iLineOffset = 0
    for( line in lines )
    {
      for( n in line.length()|..0 )
      {
        sb.deleteCharAt( iLineOffset + n )
        _assertPsiTextEqualsSource( strFileName, sb.toString() )
      }
      sb.insert( iLineOffset, line )
      iLineOffset += line.length+1
    }
  }

  protected function incrementallyRemoveASingleToken( strFileName: String, strSource: String )
  {
    var splitter = new StringTokenizer( strSource, " \n\t\r{}()[]+-*\\%@#&^=|:'\"<>,./~", true )
    var sb = new StringBuilder( strSource )
    var iPos = 0;
    while( splitter.hasMoreTokens() )
    {
      var token = splitter.nextToken()
      sb.delete( iPos, iPos + token.length )
      _assertPsiTextEqualsSource( strFileName, sb.toString() )
      sb.insert( iPos, token )
      iPos += token.length
    }
  }
}