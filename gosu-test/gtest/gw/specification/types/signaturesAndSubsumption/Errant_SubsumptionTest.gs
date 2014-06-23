package gw.specification.types.signaturesAndSubsumption

class Errant_SubsumptionTest {
  class GA {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: double): int {
      return 2
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GB {
    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GC {
    public function m(i: int, j: double): int {
      return 2
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GD {
    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: double): int {
      return 2
    }
  }

  class GE {
    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: double): int {
      return 2
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GF {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GG {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: int, j: double): int {
      return 2
    }
  }

  class GH {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: int, j: double): int {
      return 2
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GI {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: double, j: int): int {
      return 1
    }
  }

  class GJ {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: int): int {
      return 3
    }
  }

  class GK {
    public function m(i: double, j: double): int {
      return 0
    }

    public function m(i: double, j: int): int {
      return 1
    }

    public function m(i: int, j: double): int {
      return 2
    }
  }

  class GL {
    public function m(i : int) : int {
      return 0
    }
    public function m(i : long) : int {
      return 1
    }
  }
}
