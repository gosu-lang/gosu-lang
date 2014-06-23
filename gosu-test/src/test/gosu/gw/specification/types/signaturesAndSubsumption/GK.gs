package gw.specification.types.signaturesAndSubsumption

class GK {
  public function m(i : double, j : double) : int {
    return 0
  }
  public function m(i : double, j : int) : int {
    return 1
  }
  public function m(i : int, j : double) : int {
    return 2
  }
}