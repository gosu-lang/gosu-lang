package gw.lang.spec_old.statements.using_stmt
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ExpectingRightParen
{
  function failure()
  {
    using( "" as IMonitorLock 
    {
    }
  }
}
