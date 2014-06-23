package gw.internal.gosu.parser.classTests.gwtest.enums
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_IllegalCtorAccess
{
  A, B, C

  function bad() {
    new Errant_IllegalCtorAccess()
  }
}