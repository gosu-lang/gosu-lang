package gw.specContrib.delegates

uses java.lang.*

class Errant_DelegateCompoundType implements Runnable {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD

  delegate _d represents Runnable & Iterable  //## issuekeys: MSG_DELEGATES_REPRESENT_INTERFACES_ONLY, MSG_CLASS_DOES_NOT_IMPL
}