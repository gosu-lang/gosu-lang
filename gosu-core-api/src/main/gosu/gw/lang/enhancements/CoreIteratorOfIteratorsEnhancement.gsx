package gw.lang.enhancements

uses java.util.Iterator

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreIteratorOfIteratorsEnhancement<E, T extends Iterator<E>> : Iterator<T> {
  reified function flatten() : Iterator<E> {
    var outerIter : Iterator<T> = this
    return new Iterator<E>() {
      var _cur : Iterator<E>
      var _removable : Iterator<E>

      override function hasNext() : boolean {
        this.maybeAdvanceCursor()
        return _cur != null and _cur.hasNext()
      }
      
      override function next() : E {
        this.maybeAdvanceCursor()
        _removable = _cur
        return _cur.next()
      }
      
      override function remove() {
        _removable.remove()
        _removable = null
      }
      
      private function maybeAdvanceCursor() {
        while ((_cur == null or not _cur.hasNext()) and outerIter.hasNext()) {
          _cur = outerIter.next()
        }
      }
    }
  }
}
