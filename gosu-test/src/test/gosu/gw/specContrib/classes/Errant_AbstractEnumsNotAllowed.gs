package gw.specContrib.classes

abstract enum Errant_AbstractEnumsNotAllowed {  //## issuekeys: MODIFIER 'ABSTRACT' NOT ALLOWED HERE
  A; //## issuekeys: MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS

  abstract function foo()                       // issuekeys: MODIFIER 'ABSTRACT' NOT ALLOWED HERE
                                                // Studio flags the above but Gosu does not
}
