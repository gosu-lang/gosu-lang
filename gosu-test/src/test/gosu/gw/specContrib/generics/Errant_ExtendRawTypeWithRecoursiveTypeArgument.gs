package gw.specContrib.generics

class Errant_ExtendRawTypeWithRecoursiveTypeArgument {
  interface I1<T extends I1> {}

  class C1 implements I1 {} //## issuekeys:  MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE

  interface I2<T extends I2<T>> {}

  class C2 implements I2 {} //## issuekeys:  MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE

  interface I3<T extends Comparable<Comparable<T>>> {}

  class C3 implements I3 {} //## issuekeys:  MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE

  interface I4<T extends Comparable<Comparable<I4>>> {}

  class C4 implements I4 {} //## issuekeys:  MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE

  abstract class AC1<T extends AC1<T>> {}

  class C5 extends AC1 {} //## issuekeys:  MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE
}