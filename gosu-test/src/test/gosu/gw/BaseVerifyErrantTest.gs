package gw

uses gw.test.TestClass
uses gw.lang.reflect.gs.IGosuClass
uses java.io.StringReader
uses java.io.BufferedReader
uses gw.lang.parser.resources.Res
uses java.util.ArrayList
uses gw.lang.parser.IParseIssue
uses gw.lang.parser.resources.ResourceKey
uses java.util.Collections
uses java.lang.Integer

class BaseVerifyErrantTest extends TestClass {

  protected function processErrantType( gsClass: IGosuClass ) {
    print("Verifying ${gsClass.Name}")
    var bValid = gsClass.Valid

    var issuesByLine = bValid and gsClass.ParseResultsException == null
                       ? Collections.emptyMap<Integer,List<IParseIssue>>()
                       : gsClass.ParseResultsException.ParseIssues.partition( \ issue -> issue.Line )
    var iLine = 0
    using( var reader = new BufferedReader( new StringReader( gsClass.Source ) ) ) {
      var line = reader.readLine()
      while( line != null ) {
        iLine++
        var iOffset = line.indexOf( "//## issuekeys:" )
        var issuesForLine = issuesByLine.get( iLine )
        if( issuesForLine == null ) {
          assertTrue( gsClass.Name + " : Found unexpected error[s] on line " + iLine, iOffset < 0 )
        }
        else {
          assertTrue( gsClass.Name + " : Expected to find error[s] on line " + iLine, iOffset >= 0 )
          verifySameIssues( gsClass.Name, iLine, issuesForLine, parseIssues( line.substring( iOffset + "//## issuekeys:".length() ) ) )
        }
        line = reader.readLine()
      }
    }
  }

  private function parseIssues( line: String ) : ArrayList<ResourceKey> {
    var issues = line.split(",")
    var resKeys = new ArrayList<ResourceKey>()
    for( issue in issues ) {
      resKeys.add((Res.Type.TypeInfo.getProperty( issue.trim() ).Accessor.getValue( null )) as ResourceKey )
    }
    return resKeys
  }

  private function verifySameIssues( typeName: String, iLine: int, issues: List <IParseIssue>, resKey: ArrayList <ResourceKey> ) {
    assertSame( typeName + " : Different number of errors found for line " + iLine + " found: ${issues.map( \ i -> i.MessageKey.Key )}", issues.Count, resKey.Count )
    issues.eachWithIndex( \ issue, i -> assertTrue( typeName + " : Different error messages on line " + iLine + "\n" + issue.MessageKey.Key + " vs. " + resKey[i].Key, issue.MessageKey.Key == resKey[i].Key ) )
  }

}



