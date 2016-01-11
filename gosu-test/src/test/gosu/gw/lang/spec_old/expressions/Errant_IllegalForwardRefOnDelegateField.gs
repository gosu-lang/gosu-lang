package gw.lang.spec_old.expressions
uses java.lang.Runnable
uses java.util.concurrent.Callable
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IllegalForwardRefOnDelegateField implements Runnable {

  delegate _d1 represents Runnable = _d2
  delegate _d2 represents Runnable
}
