package gw.specification.types.signaturesAndSubsumption
class GIObj {
  public function m(i : GAnimal, j : GAnimal) : int {
    return 0
  }
  public function m(i : GAnimal, j : GDog) : int {
    return 1
  }

}