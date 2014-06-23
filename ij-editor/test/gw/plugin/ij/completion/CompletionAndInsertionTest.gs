/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

/**
 * Created with IntelliJ IDEA.
 * User: bchang
 * Date: 10/2/12
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
class CompletionAndInsertionTest extends AbstractCodeCompletionTest {

  function testAutocompletingNoArgMethodWithSingleMatchingMethod() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar() { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[()]]]!! \n" +
            "  } \n" +
            "}"
    }, {
        "bar(): void"
    }
    )
  }

  function testAutocompleting1ArgMethodWithSingleMatchingMethod() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar(x : int) { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[(!!)]]] \n" +
            "  } \n" +
            "}"
    }, {
        "bar(x: int): void"
    }
    )
  }

  function testAutocompletingBlockArgMethodWithSingleMatchingMethod() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar(op(x:Object, y:Object)) { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[( \\ x, y -> !!)]]] \n" +  // interestingly, without "this." we end up with RawSymbolLookupItem rather than GosuFeatureInfoLookupItem
            "  } \n" +
            "}"
    }, {
        "bar(op(x:Object, y:Object):void): void"
    }, null
    )
  }

  function testAutoCompletingNoArgMethodWithMultipleMatchingMethodsWithUserTypingLeftParen() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar() { } \n" +
            "  function bar2() { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[()]]]!! \n" +  // interestingly, without "this." we end up with RawSymbolLookupItem rather than GosuFeatureInfoLookupItem
            "  } \n" +
            "}"
    }, {
        "bar(): void",
        "bar2(): void"
    }, "("
    )
  }

  function testAutoCompleting1ArgMethodWithMultipleMatchingMethodsWithUserTypingLeftParen() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar(x : int) { } \n" +
            "  function bar2() { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[(!!)]]] \n" +
            "  } \n" +
            "}"
    }, {
        "bar(x: int): void",
        "bar2(): void"
    }, "("
    )
  }

  function testAutocompletingBlockArgMethodWithMultipleMatchingMethodWithUserTypingLeftParen() {
    test({
        "package pkg\n" +
            "class GosuClass { \n" +
            "  function bar(op(x:Object, y:Object)) { } \n" +
            "  function bar2() { } \n" +
            "  function foo() { \n" +
            "    var list : List \n" +
            "    this.bar^^[[[( \\ x, y -> !!)]]] \n" +
            "  } \n" +
            "}"
    }, {
        "bar(op(x:Object, y:Object):void): void",
        "bar2(): void"
    }, "("
    )
  }

}