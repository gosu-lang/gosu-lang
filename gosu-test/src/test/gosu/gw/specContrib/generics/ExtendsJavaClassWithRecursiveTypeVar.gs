package gw.specContrib.generics

uses java.util.ListIterator
uses java.util.Collection
uses java.util.Iterator

class ExtendsJavaClassWithRecursiveTypeVar extends JavaClassWithRecursiveTypeVar<ExtendsJavaClassWithRecursiveTypeVar> implements List<ExtendsJavaClassWithRecursiveTypeVar> {
  override function size(): int {
    return 0
  }

  override property get Empty(): boolean {
    return false
  }

  override function contains(o: Object): boolean {
    return false
  }

  override function iterator(): Iterator<ExtendsJavaClassWithRecursiveTypeVar> {
    return null
  }

  override function toArray(): Object[] {
    return null
  }

  override function toArray<T>(a: T[]): T[] {
    return null
  }

  override function add(e: ExtendsJavaClassWithRecursiveTypeVar): boolean {
    return false
  }

  override function remove(o: Object): boolean {
    return false
  }

  override function containsAll(c: Collection<Object>): boolean {
    return false
  }

  override function addAll(c: Collection<ExtendsJavaClassWithRecursiveTypeVar>): boolean {
    return false
  }

  override function addAll(index: int, c: Collection<ExtendsJavaClassWithRecursiveTypeVar>): boolean {
    return false
  }

  override function removeAll(c: Collection ): boolean {
    return false
  }

  override function retainAll(c: Collection): boolean {
    return false
  }

  override function clear() {
  }

  override function get(index: int): ExtendsJavaClassWithRecursiveTypeVar {
    return null
  }

  override function set(index: int, element: ExtendsJavaClassWithRecursiveTypeVar): ExtendsJavaClassWithRecursiveTypeVar {
    return null
  }

  override function add(index: int, element: ExtendsJavaClassWithRecursiveTypeVar) {
  }

  override function remove(index: int): ExtendsJavaClassWithRecursiveTypeVar {
    return null
  }

  override function indexOf(o: Object): int {
    return 0
  }

  override function lastIndexOf(o: Object): int {
    return 0
  }

  override function listIterator(): ListIterator<ExtendsJavaClassWithRecursiveTypeVar> {
    return null
  }

  override function listIterator(index: int): ListIterator<ExtendsJavaClassWithRecursiveTypeVar> {
    return null
  }

  override function subList(fromIndex: int, toIndex: int): List<ExtendsJavaClassWithRecursiveTypeVar> {
    return null
  }
}