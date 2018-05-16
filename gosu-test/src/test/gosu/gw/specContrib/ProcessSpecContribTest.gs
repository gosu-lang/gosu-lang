package gw.specContrib

uses gw.BaseVerifyErrantTest
uses java.io.File
uses java.lang.*
uses java.util.ArrayList
uses java.util.HashMap
uses java.lang.StringBuilder
uses gw.lang.reflect.gs.IGosuClass
uses java.util.Collections
uses java.io.BufferedReader
uses java.io.StringReader
uses gw.lang.parser.IParseIssue
uses gw.lang.reflect.TypeSystem
uses java.util.Set
uses java.util.regex.Pattern

class ProcessSpecContribTest extends BaseVerifyErrantTest {

  private function findAllErrantFiles(file : File, errantClasses: List<String>) {
    var files = file.listFiles()
    for(f in files) {
      if(f.Directory) {
        findAllErrantFiles(f, errantClasses)
      } else if(f.File and f.Name.startsWith("Errant") and !f.Name.endsWith(".java")) {
        errantClasses.add(convertToClassPath(f))
      } else {
        print("Not an Errant file: " + f.AbsolutePath)
      }
    }
  }

  private function compareErrantTypeWithCompilerBehaviour( gsClass: IGosuClass ) {
    print("Processing " + gsClass.Name)
    var bValid = gsClass.Valid
    var knowBreakLines : ArrayList<Integer> = {}

    var issuesByLine = bValid and gsClass.ParseResultsException == null ?
                                           Collections.emptyMap<Integer,List<IParseIssue>>()
                                           : gsClass.ParseResultsException.ParseIssues.partition( \ issue -> issue.Line )
    var iLine = 0
    var kbPattern =  Pattern.compile("//## KB\\([A-Z]+-[0-9]+\\)")
    var missingDiag = new HashMap<Integer, String>()
    using( var reader = new BufferedReader( new StringReader( gsClass.Source ) ) ) {
      var line = reader.readLine()
      while( line != null ) {
        iLine++
        if( line.indexOf( "//## KB(" ) != -1 ) {
          assertTrue( gsClass.Name + " : Wrong jira format in known break on line " + iLine, kbPattern.matcher(line).find())
          if(!skipKnownBreak) {
            knowBreakLines.add(iLine)
          } else {
            issuesByLine.remove(iLine)
          }
        } else {
          var iOffset = line.indexOf( "//## issuekeys:" )
          if(iOffset != -1) {
            var issuesForLine = issuesByLine.get( iLine )
	    if (issuesForLine == null) {
	      missingDiag.put(iLine, line.substring(line.indexOf("//## issuekeys:")).replaceFirst("//## issuekeys:",""))
	    }
            issuesByLine.remove(iLine)
          }
        }
        line = reader.readLine()
      }
      assertTrue( gsClass.Name + " : found known break[s] at line[s]: [" + knowBreakLines.join(", ") + "]", knowBreakLines.Empty)
    }
    var err = new StringBuilder()
    err.append(gsClass.Name)
    if(!missingDiag.Empty) {
      err.append("\nExpected errors not seen:")
      for(l in missingDiag.Keys.toList().sort()) {
        err.append("\n    Line " + l + ": " + missingDiag.get(l))
      }
    }
    if(!issuesByLine.Empty) {
      err.append("\nFound Unannotated Errors:");
      for(l in issuesByLine.Keys.toList().sort()) {
        err.append("\n    Line " + l + ": ").append(issuesByLine[l].map( \ el -> el.MessageKey.Key).join(","))
      }
    }
    assertTrue(err.toString(), issuesByLine.Empty && missingDiag.Empty )
  }

  private function convertToClassPath(f : File) : String {
    var dotPath = f.AbsolutePath.replace(File.separator, ".")
    var i = dotPath.indexOf("gw.specContrib")
    var extLength = f.Extension.length()+1
    return dotPath.substring(i, dotPath.length() - extLength)
  }

  function testAnalyzeSpecContrib() {
    var excluded : Set<String> = {
                                   "gw.specContrib.classes.Errant_ConstructorOverrideInAnonymousClass",  // IDE-1821
                                   "gw.specContrib.classes.enhancements.Errant_SymbolCollision_ListEnh2", // IDE-1824
                                   "gw.specContrib.typeinference.Errant_GenericMethodAndBlockArgument", // IDE-1943

                                   "gw.specContrib.generics.Errant_RecursiveTypeParameter", //IDE-2203
                                   "gw.specContrib.featureLiterals.gosuMembersBinding.Errant_BindOverloadedGosuMethods", //IDE-1466
                                   "gw.specContrib.featureLiterals.gosuMembersBinding.Errant_BindOverloadedGosuMethodsToInstance", //IDE-1466
                                   "gw.specContrib.types.Errant_MetaType", // IDE-2283
                                   "gw.specContrib.interfaceMethods.defaultMethods.Errant_DefaultMethodsGenerics", //IDE-2581
                                   "gw.specContrib.interfaceMethods.defaultMethods.Errant_DefaultMethodsAccessModifier", //IDE-2576
                                   "gw.specContrib.interfaceMethods.defaultMethods.Errant_DefaultAndAbstractConflict_1",  //IDE-2607
                                   "gw.specContrib.interfaceMethods.defaultMethods.Errant_DefaultAndAbstractConflict_2", //IDE-2614
                                   "gw.specContrib.interfaceMethods.staticMethods.javaInteraction.Errant_GosuTestClass1", //IDE-2594
                                   "gw.specContrib.interfaceMethods.staticMethods.Errant_StaticDefaultMethodsResolution_1", //IDE-2618
                                   "gw.specContrib.classes.enhancements.shadowingMore.javaVsGosu.methodsVsMethods.Errant_ShadowingJavaMethodsWithMethodsInEnhancements",

                                   /* to skip as we don't support this check in our testing framework */
                                   "gw.specContrib.classes.Errant_ClassDeclaredInEnhancement",
                                   "gw.specContrib.classes.Errant_ClassNotNamedAfterFile",
                                   "gw.specContrib.classes.Errant_EnhancementDeclaredInClass",
                                   "gw.specContrib.classes.Errant_EnhancementNotNamedAfterFile"
                                 }
    var errantClasses = new ArrayList<String>()
    findAllErrantFiles(new File("src/test/gosu/gw/specContrib"), errantClasses)
    var j = errantClasses.size()-1

    for(c in errantClasses index i) {
      if(!excluded.contains(c)) {
        System.out.print("["+ i + "/" + j +  "]  ")
        compareErrantTypeWithCompilerBehaviour(TypeSystem.getByFullName(c) as IGosuClass)
      }
    }
  }
}