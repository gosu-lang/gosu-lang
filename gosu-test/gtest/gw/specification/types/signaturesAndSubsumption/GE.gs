package gw.specification.types.signaturesAndSubsumption

class GE {
  public function m(i : double, j : int) : int {
    return 1
  }
  public function m(i : int, j : double) : int {
    return 2
  }
  public function m(i : int, j : int) : int {
    return 3
  }

}