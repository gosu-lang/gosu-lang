package gw.specContrib.generics

uses java.util.ListIterator
uses java.util.Collection
uses java.util.Iterator

class Errant_ExtendsJavaClassWithRecursiveTypeVar2 extends JavaClassWithRecursiveTypeVar<Errant_ExtendsJavaClassWithRecursiveTypeVar2> implements List<Errant_ExtendsJavaClassWithRecursiveTypeVar2> {
  override function size(): int {
    return 0
  }

  override property get Empty(): boolean {
    return false
  }

  override function contains(o: Object): boolean {
    return false
  }

  override function iterator(): Iterator<Errant_ExtendsJavaClassWithRecursiveTypeVar2> {
    return null
  }

  override function toArray(): Object[] {
    return null
  }

  override function toArray<T>(a: T[]): T[] {
    return null
  }

  override function add(e: Errant_ExtendsJavaClassWithRecursiveTypeVar2): boolean {
    return false
  }

  override function remove(o: Object): boolean {
    return false
  }

  override function containsAll(c: Collection<Object>): boolean {
    return false
  }

  override function addAll(c: Collection<Errant_ExtendsJavaClassWithRecursiveTypeVar2>): boolean {
    return false
  }

  override function addAll(index: int, c: Collection<Errant_ExtendsJavaClassWithRecursiveTypeVar2>): boolean {
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

  override function get(index: int): Errant_ExtendsJavaClassWithRecursiveTypeVar2 {
    return null
  }

  override function set(index: int, element: Errant_ExtendsJavaClassWithRecursiveTypeVar2): Errant_ExtendsJavaClassWithRecursiveTypeVar2 {
    return null
  }

  override function add(index: int, element: Errant_ExtendsJavaClassWithRecursiveTypeVar2) {
  }

  override function remove(index: int): Errant_ExtendsJavaClassWithRecursiveTypeVar2 {
    return null
  }

  override function indexOf(o: Object): int {
    return 0
  }

  override function lastIndexOf(o: Object): int {
    return 0
  }

  override function listIterator(): ListIterator<Errant_ExtendsJavaClassWithRecursiveTypeVar2> {
    return null
  }

  override function listIterator(index: int): ListIterator<Errant_ExtendsJavaClassWithRecursiveTypeVar2> {
    return null
  }

  override function subList(fromIndex: int, toIndex: int): List<Errant_ExtendsJavaClassWithRecursiveTypeVar2> {
    return null
  }
}