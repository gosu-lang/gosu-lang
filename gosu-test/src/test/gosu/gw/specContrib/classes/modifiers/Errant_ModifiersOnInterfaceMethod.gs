package gw.specContrib.classes.modifiers

uses java.lang.Integer

interface Errant_ModifiersOnInterfaceMethod {

  public function foo1()
  abstract function foo6()

  protected function foo2()         //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  internal function foo3()          //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  private function foo4()           //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  static function foo5()            //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  final function foo7()             //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  synchronized function foo8()      //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  transient function foo9()         //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  default function foo10()          //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  override function foo11()         //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE

}
