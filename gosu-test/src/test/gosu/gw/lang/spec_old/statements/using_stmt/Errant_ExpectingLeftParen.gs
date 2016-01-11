package gw.lang.spec_old.statements.using_stmt
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ExpectingLeftParen
{
  function failure()
  {
    using "" as IMonitorLock )
    {
    }
  }
}
