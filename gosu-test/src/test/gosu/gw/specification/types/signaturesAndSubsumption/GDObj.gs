package gw.specification.types.signaturesAndSubsumption
class GDObj {
  public function m(i : GAnimal, j : GDog) : int {
    return 1
  }
  public function m(i : GDog, j : GAnimal) : int {
    return 2
  }

}