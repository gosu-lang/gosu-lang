package gw.specContrib.classes

uses gw.specContrib.classes.abc.FUToo

class FUMaybe<T extends java.util.AbstractList> extends FUToo<T> {
  construct( t: T ) {
    super( t )
  }
}