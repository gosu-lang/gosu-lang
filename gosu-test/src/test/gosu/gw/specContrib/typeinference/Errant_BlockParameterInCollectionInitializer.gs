package gw.specContrib.typeinference

uses java.util.List

class Errant_BlockParameterInCollectionInitializer {
  // IDE-1831
  var blocks : List<block(parm: String)> = {
      \p -> {
        print(p.length)
      }
  }
}