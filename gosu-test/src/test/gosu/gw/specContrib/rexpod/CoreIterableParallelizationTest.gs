package gw.specContrib.rexpod


uses gw.test.TestClass

uses java.util.stream.Collectors
uses java.util.stream.IntStream

class CoreIterableParallelizationTest extends TestClass {


  function testParallelExecutionOfCoreIterableEnhancement() {
    for( 0..100)
    {
      IntStream.rangeClosed(0, 100)
          .parallel()
          .forEach(\id -> functionThatCallsLast(id))
      gw.lang.reflect.TypeSystem.refresh( true )
    }
  }

  private function functionThatCallsLast(id : int) {

    var listOfObjects : List<ClassRoot> = {}
    listOfObjects = IntStream.rangeClosed(0, 100)
        .mapToObj<ClassRoot>(\i -> createRandomSubtype(i))
        .collect(Collectors.toList<ClassRoot>())


    var arrayOfObjects : ClassRoot[] = listOfObjects.toTypedArray()

    var foo = arrayOfObjects.last()


    print("Done!" + id)
  }

  private function createRandomSubtype(seed : int) : ClassRoot {
    switch (seed % 4) {
      case 0:
        return new ClassA()
      case 1:
        return new ClassB()
      case 2:
        return new ClassC()
      case 3:
        return new ClassD()
      default:
        return new ClassA()
    }
  }

}