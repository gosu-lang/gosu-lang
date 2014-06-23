package gw.lang.spec_old.namespaces

uses
uses .
uses java.
uses java.util
uses java.util.
uses java.util..
uses java.util.BadClassName
uses GoodNameSpaces.
uses GoodNameSpaces.*
uses gw.lang.spec_old.namespaces.GoodNameSpaces.
uses gw.lang.spec_old.namespaces.GoodNameSpaces..
uses GoodNameSpaces.SampleInnerClass.
uses GoodNameSpaces.SampleInnerClass..
uses gw.lang.spec_old.namespaces.GoodNameSpaces.SampleInnerClass.
uses gw.lang.spec_old.namespaces.GoodNameSpaces.SampleInnerClass..
uses gw.lang.spec_old.namespaces.GoodNameSpaces.SampleInnerClass.SampleInnerClass2.
vuses gw.lang.spec_old.namespaces.GoodNameSpaces.SampleInnerClass.SampleInnerClass2..

@gw.testharness.DoNotVerifyResource
class Errant_BadNameSpaces {
  class SampleInnerClass {
    class SampleInnerClass2 {
    }
  }
}
