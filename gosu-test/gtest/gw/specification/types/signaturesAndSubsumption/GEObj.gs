package gw.specification.types.signaturesAndSubsumption
class GEObj {
  public function m(i : GAnimal, j : GDog) : int {
    return 1
  }
  public function m(i : GDog, j : GAnimal) : int {
    return 2
  }
  public function m(i : GDog, j : GDog) : int {
    return 3
  }

}