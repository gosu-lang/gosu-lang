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
uses java.lang.System
uses java.util.regex.Pattern

class BaseVerifyErrantTest extends TestClass {
  var _skipKnownBreak : boolean

  construct()  {
    var propValue = System.getProperty("gw.tests.skip.knownbreak")
    _skipKnownBreak =  propValue != null && Boolean.valueOf(propValue)
  }

  protected function processErrantType( gsClass: IGosuClass ) {
    print("Verifying ${gsClass.Name}")
    var bValid = gsClass.Valid
    var knowBreakLines : ArrayList<Integer> = {}

    var issuesByLine = bValid and gsClass.ParseResultsException == null
                       ? Collections.emptyMap<Integer,List<IParseIssue>>()
                       : gsClass.ParseResultsException.ParseIssues.partition( \ issue -> issue.Line )
    var iLine = 0
    var kbPattern =  Pattern.compile("//## KB\\([A-Z]+-[0-9]+\\)")
    using( var reader = new BufferedReader( new StringReader( gsClass.Source ) ) ) {
      var line = reader.readLine()
      while( line != null ) {
        iLine++
        if( line.indexOf( "//## KB(" ) != -1 ) {
          assertTrue( gsClass.Name + " : Wrong jira format in known break on line " + iLine, kbPattern.matcher(line).find())
          if(!_skipKnownBreak) {
            knowBreakLines.add(iLine)
          }
        } else {
          var iOffset = line.indexOf( "//## issuekeys:" )
          var issuesForLine = issuesByLine.get( iLine )
          if( issuesForLine == null ) {
            assertTrue( gsClass.Name + " : Found unexpected error[s] on line " + iLine, iOffset < 0 )
          }
          else {
            assertTrue( gsClass.Name + " : Expected to find error[s] on line " + iLine, iOffset >= 0 )
            verifySameIssues( gsClass.Name, iLine, issuesForLine, parseIssues( line.substring( iOffset + "//## issuekeys:".length() ) ) )
          }
        }
        line = reader.readLine()
      }
      assertTrue( gsClass.Name + " : found known break[s] at line[s]: [" + knowBreakLines.join(", ") + "]", knowBreakLines.Empty)
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



