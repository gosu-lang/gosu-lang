package gw.internal.gosu.regression

class HasDoublyNestedBlocksAndStringTemplates {

  function doStuffThatMightBreak(args : List<List<String>>) {
    args.each(\ list -> {
      list.each(\str -> {} )
      var x = "Some String template literal ${list.size()}"
    })
  }

}
