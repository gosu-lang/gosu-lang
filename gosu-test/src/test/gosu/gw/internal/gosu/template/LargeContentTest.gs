package gw.internal.gosu.template

uses gw.test.*
uses java.lang.*

class LargeContentTest extends TestClass {
  function testLargeContent() {
    var result = eolToN( LargeContent.renderToString( "hello" ) )
    assertEquals(
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "Param: hello",
      result )
  }

  function testVeryLargeContent() {
    var result = eolToN( VeryLargeContent.renderToString( "hello" ) )
    var expected =
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~o\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~p\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~q\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~s\n" +
      "0 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♦\n" +
      "1 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~v\n" +
      "2 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~w\n" +
      "3 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♂\n" +
      "4 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♀\n" +
      "5 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♪\n" +
      "6 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~♫\n" +
      "7 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~☼\n" +
      "8 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~►\n" +
      "9 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\\{}|:;\"',./<>?`~↕\n" +
      "\n"
      expected = expected.repeat( 10 ) + "Param: hello"
      assertEquals( expected, result )
  }

  function testTooLargeContent() {
    var result = RtfLargeContent.renderToString()
    assertEquals( (RtfLargeContent.Type as gw.lang.reflect.gs.ITemplateType).TemplateGenerator.Source,
    result )
  }

  function testTooTooLargeContent() {
    var result = RtfVeryLargeContent.renderToString()
    assertEquals( (RtfVeryLargeContent.Type as gw.lang.reflect.gs.ITemplateType).TemplateGenerator.Source, result )
  }

  function eolToN( s: String ) : String {
    return s.replaceAll( "\r\n", "\n" )
  }
}