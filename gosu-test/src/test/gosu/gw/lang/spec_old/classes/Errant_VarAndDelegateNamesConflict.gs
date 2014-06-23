package gw.lang.spec_old.classes

// This was causing an internal error in the parser
@gw.testharness.DoNotVerifyResource
class Errant_VarAndDelegateNamesConflict {
    delegate x represents java.util.List
    var x = 1
}
