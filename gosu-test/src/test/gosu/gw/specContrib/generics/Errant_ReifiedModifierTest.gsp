uses java.util.concurrent.Callable


interface IInterface {
  function notReified<T>()  
  reified function reified<T>()  
}

static class ImplementsIInterface implements IInterface
{
  override reified function notReified<T>()  //## issuekeys: MSG_REIFIED_DONT_MATCH
  {
  }
  
  override function reified<T>()  //## issuekeys: MSG_REIFIED_DONT_MATCH
  {    
  }
}

static class ImplementsIInterface2 implements IInterface
{
  override function notReified<T>()
  {
  }
  
  override reified function reified<T>()
  {    
  }
}

static abstract class AClass
{
  abstract function notReified<T>()  
  abstract reified function reified<T>()  
}

static class ExtendsAClass extends AClass
{
  override reified function notReified<T>()  //## issuekeys: MSG_REIFIED_DONT_MATCH
  {
  }
  
  override function reified<T>()  //## issuekeys: MSG_REIFIED_DONT_MATCH
  {    
  }
}

static class ExtendsAClass2 extends AClass
{
  override function notReified<T>()
  {
  }
  
  override reified function reified<T>()
  {    
  }
}


static class ReifiedTest
{
  function test_1<T>()
  {
  }  
  reified function test_1_0()  //## issuekeys: NOTHING_TO_REIFY
  {
  }
  reified static function test_1_3(t: String) : String  //## issuekeys: NOTHING_TO_REIFY
  {    
    return null
  }
  reified static function test_1_3<T>(t: T): T
  {    
    return t
  }
  reified property get test_prop() : String  //## issuekeys: NOTHING_TO_REIFY
  {
    return "hi"
  }  
  reified property set test_prop(value: String)  //## issuekeys: NOTHING_TO_REIFY
  {
  }  
  reified static property get test_static_prop() : String  //## issuekeys: NOTHING_TO_REIFY
  {
    return "hi"
  }  
  reified static property set test_static_prop(value: String)  //## issuekeys: NOTHING_TO_REIFY
  {
  }  

  reified function test_2<T>()
  {
  }  
  function test_3<T>()
  {    
    new T()  //## issuekeys: MSG_TYPE_NOT_REIFIED
  }  
  function test_3_0<T>()
  {    
    // ok, java class generics erased
    new ArrayList<T>()
  }  
  function test_3_1<T>()
  {    
    // not ok, anon class is Gosu class
    new Callable<T>() {  //## issuekeys: MSG_TYPE_NOT_REIFIED
      override function call() : T {
        return null
      }  
    }
  }    
  function test_3_2<T>()
  {    
    new Callable<Object>() {
      override function call() : Object {
        return null
      }  
    }
  }  
  function test_3_3<T>()
  {    
    new Callable<Object>() {
      override function call() : Object {
        return new T()  //## issuekeys: MSG_TYPE_NOT_REIFIED
      }  
    }
  }  
  reified function test_3_4<T>()
  {    
    new Callable<Object>() {
      override function call() : Object {
        return new T() // only outmost function needs 'reified'
      }  
    }
  }  
  function test_4<T>()
  {    
    var x = new T[2]  //## issuekeys: MSG_TYPE_NOT_REIFIED
  }
  function test_4_0<T>()
  {
    var x = new T[] {}  //## issuekeys: MSG_TYPE_NOT_REIFIED
  }
  function test_4_1<T>()
  {    
    // java class erases type params
    var x = new ArrayList<T>[2]
  }
  function test_4_2<T>( t: T )
  {    
    // java class erases type params
    var x = new ArrayList<T>[] {}
    var y = new ArrayList<T>[] { null }
    var z = new ArrayList<T>[] { {t} }
  }
 
  function test_5<T>()
  {
    var x = "asdf" as T  //## issuekeys: MSG_TYPE_NOT_REIFIED
  } 
  function test_5_0<T>()
  {
    var x = {"asdf"} as List<T>  //## issuekeys: MSG_TYPE_NOT_REIFIED
  } 
  function test_5_1<T>()
  {
    var y: List<String>[] = {{"asdf"}}  //## issuekeys: MSG_PARAMETERIZED_ARRAY_COMPONENT
    var x = y as List<T>[]  //## issuekeys: MSG_PARAMETERIZED_ARRAY_COMPONENT
  }
  function test_5_2<T>()
  {
    var x: T[] = {"asdf"} as T[]  //## issuekeys: MSG_TYPE_NOT_REIFIED
  }
  
  function test_6<T>()
  {
    var x = "" typeis T  //## issuekeys: MSG_TYPE_NOT_REIFIED
  } 
  function test_6_0<T>()
  {
    var x = {"SDF"} typeis List<T>  //## issuekeys: MSG_TYPE_NOT_REIFIED
  } 
  
  reified function foo_reified<T>() {}
  function foo_not_reified<T>() {}
  function test_7<T>()
  {
    foo_reified<String>()
    foo_reified<T>()  //## issuekeys: MSG_TYPE_NOT_REIFIED
    this.foo_reified<T>()  //## issuekeys: MSG_TYPE_NOT_REIFIED
    new ReifiedTest().foo_reified<T>()  //## issuekeys: MSG_TYPE_NOT_REIFIED
  } 
  reified function test_7_0<T>()
  {
    foo_reified<String>()
    foo_reified<T>()
    this.foo_reified<T>()
    new ReifiedTest().foo_reified<T>()
  }

  function test_8<T>( t: Type<T> )
  {
    var x: IType[] = { t }  //## issuekeys: MSG_TYPE_NOT_REIFIED
  }

  final reified function test_9<T>()
  {
  }
  reified final function test_9_1<T>()
  {
  }
}
