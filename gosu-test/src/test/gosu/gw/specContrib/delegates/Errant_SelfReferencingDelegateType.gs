package gw.specContrib.delegates

uses java.lang.*

class Errant_SelfReferencingDelegateType implements Runnable {
  delegate _d : Errant_SelfReferencingDelegateType represents Runnable //## issuekeys: MSG_DELEGATES_SHOULD_NOT_SELF_DELEGATE
}
