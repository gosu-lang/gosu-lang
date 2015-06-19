package gw.internal.gosu.compiler.sample.benchmark.reification

uses java.lang.Runnable
uses java.util.concurrent.Callable

class ReificationBenchmark implements Callable<List<Runnable>> {
  override function call(): List<Runnable> {
    return {
      new FrequentlyUsedJavaClass(),
      new JavaClass(),
      new GenericJavaClass(),
      new GosuClass(),
      new GenericGosuClass(),
      new TypeVarWithGenericGosuClass<ReificationBenchmark>(),
      new MapCount()
    }
  }

  static class FrequentlyUsedJavaClass extends Base {
    override function run() {
      var x = new GenericClass<String>()
    }
  }

  static class JavaClass extends Base {
    override function run() {
      var x = new GenericClass<java.lang.Math>()
    }
  }

  static class GenericJavaClass extends Base {
    override function run() {
      var x = new GenericClass<java.util.List<String>>()
    }
  }

  static class GosuClass extends Base {
    override function run() {
      var x = new GenericClass<NotGenericClass>()
    }
  }

  static class GenericGosuClass extends Base {
    override function run() {
      var x = new GenericClass<GenericClass<java.util.List>>()
    }
  }

  static class TypeVarWithGenericGosuClass<T> extends Base {
    override function run() {
      var x = new GenericClass<Foo<T>>()
    }
  }

  class MapCount extends Base {
    var _map = new java.util.HashMap<String, ReificationBenchmark>()
    override function run() {
      var x = _map.Count
    }
  }

  static abstract class Base implements Runnable {
    override function toString() : String {
      return (typeof this).RelativeName
    }
  }

  static class Foo<E> {}
}
