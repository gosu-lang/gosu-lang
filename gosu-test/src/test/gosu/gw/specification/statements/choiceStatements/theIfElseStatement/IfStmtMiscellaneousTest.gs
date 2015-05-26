package gw.specification.statements.choiceStatements.theIfElseStatement

uses gw.BaseVerifyErrantTest

class IfStmtMiscellaneousTest extends BaseVerifyErrantTest {
  function testWithNoElse() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testWithNoElse("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testWithNoElse("other"));
  }

  function testWithElse() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testWithElse("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testWithElse("other"));
  }

  function testWithElseNonTerminal() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testWithElseNonTerminal("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testWithElseNonTerminal("other"));
  }

  function testIfWithNoCurlyBraces() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testIfWithNoCurlyBraces("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testIfWithNoCurlyBraces("other"));
  }

  function testIfElseWithNoCurlyBraces() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testIfElseWithNoCurlyBraces("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testIfElseWithNoCurlyBraces("other"));
  }

  function testSimpleNested() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testSimpleNested("a1"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testSimpleNested("a2"));
    assertEquals("neither", IfStmtMiscellaneousTestCases.testSimpleNested("b"));
  }

  function testDeeplyNested() {
    assertEquals("aaaa-", IfStmtMiscellaneousTestCases.testDeeplyNested("aaaadfsdfsd"));
    assertEquals("aa-", IfStmtMiscellaneousTestCases.testDeeplyNested("aabbbb"));
    assertEquals("aba-", IfStmtMiscellaneousTestCases.testDeeplyNested("abafdsfds"));
    assertEquals("ab-", IfStmtMiscellaneousTestCases.testDeeplyNested("abfdsfsdf"));
    assertEquals("neither", IfStmtMiscellaneousTestCases.testDeeplyNested("aaadfssdfdsf"));
    assertEquals("neither", IfStmtMiscellaneousTestCases.testDeeplyNested("aggfdgfdg"));
    assertEquals("-", IfStmtMiscellaneousTestCases.testDeeplyNested("bfdsfds"));
  }

  function testCascadingIfElseWithTerminalElse() {
    assertEquals("a-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithTerminalElse("adsdkfdskjlf"));
    assertEquals("b-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithTerminalElse("b"));
    assertEquals("c-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithTerminalElse("cdsfdsf"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testCascadingIfElseWithTerminalElse("d"));
  }

  function testCascadingIfElseWIthNoElse() {
    assertEquals("a-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithNoElse("adsdkfdskjlf"));
    assertEquals("b-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithNoElse("b"));
    assertEquals("c-", IfStmtMiscellaneousTestCases.testCascadingIfElseWithNoElse("cdsfdsf"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testCascadingIfElseWithNoElse("d"));
  }

  function testIfWithNoCurlyBracesWithVariableInsideInnerStatement() {
    assertEquals("true", IfStmtMiscellaneousTestCases.testIfWithNoCurlyBracesWithVariableInsideInnerStatement("true"));
  }

  function testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement() {
    assertEquals("if", IfStmtMiscellaneousTestCases.testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement("true"));
    assertEquals("else", IfStmtMiscellaneousTestCases.testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement("false"));
  }
}