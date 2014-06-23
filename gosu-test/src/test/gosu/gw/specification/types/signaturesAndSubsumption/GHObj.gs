package gw.specification.types.signaturesAndSubsumption
class GHObj {
  public function m(i : GAnimal, j : GAnimal) : int {
    return 0
  }
  public function m(i : GDog, j : GAnimal) : int {
    return 2
  }
  public function m(i : GDog, j : GDog) : int {
    return 3
  }

}