package gw.specification.types.signaturesAndSubsumption
class GKObj {
  public function m(i : GAnimal, j : GAnimal) : int {
    return 0
  }
  public function m(i : GAnimal, j : GDog) : int {
    return 1
  }
  public function m(i : GDog, j : GAnimal) : int {
    return 2
  }

}