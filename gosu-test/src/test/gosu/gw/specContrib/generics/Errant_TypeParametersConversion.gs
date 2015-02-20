package gw.specContrib.generics

class Errant_TypeParametersConversion {
  static function cast<T, S>(p: T): S {
    return p;                               //## issuekeys: INCOMPATIBLE TYPES
  }

  // IDE-1540
  static function castArray<T, S>(p: T[]): S[] {
    return p;                               //## issuekeys: INCOMPATIBLE TYPES
  }
}
