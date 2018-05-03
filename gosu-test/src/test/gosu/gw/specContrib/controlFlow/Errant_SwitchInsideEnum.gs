package gw.specContrib.controlFlow

class Errant_SwitchInsideEnum {
  enum Relop1 {
    GreaterThan, LessThan
  }
  private enum InvoiceItemAmountSignum  {
    POSITIVE,
    NEGATIVE

    property get OperatorToUseForFiltering() : Relop1 {
      switch (this) {
        case POSITIVE:
          return Relop1.GreaterThan
        case NEGATIVE:
          return Relop1.LessThan
      }
      return null      //## issuekeys: UNREACHABLE STATEMENT
    }
  }
}