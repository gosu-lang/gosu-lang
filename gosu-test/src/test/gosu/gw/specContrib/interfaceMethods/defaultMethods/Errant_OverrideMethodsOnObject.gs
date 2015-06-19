package gw.specContrib.interfaceMethods.defaultMethods

interface Errant_OverrideMethodsOnObject {
  //IDE-2598 to be fixed in OS Gosu
  public function equals(obj: Object): boolean {      //## issuekeys: DEFAULT METHOD 'EQUALS' OVERRIDES A MEMBER OF 'JAVA.LANG.OBJECT'
    return false;
  }

  public function hashCode(): int {      //## issuekeys: DEFAULT METHOD 'HASHCODE' OVERRIDES A MEMBER OF 'JAVA.LANG.OBJECT'
    return 0;
  }

  public function toString(): String {      //## issuekeys: DEFAULT METHOD 'TOSTRING' OVERRIDES A MEMBER OF 'JAVA.LANG.OBJECT'
    return null;
  }

  function finalize() {
  }

  function clone(): int {
    return 0
  }
}