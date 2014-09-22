package gw.internal.gosu.parser.blocks

class ConstructorTest {

  construct(modelData : Object,
          loadChildrenFunc : block(Object) : java.util.List<Object>,
          expandToDepth : int) {
    if(expandToDepth > 0) {
      loadChildrenFunc(modelData).each( \ c -> print(expandToDepth - 1) )
    }
  }

}
