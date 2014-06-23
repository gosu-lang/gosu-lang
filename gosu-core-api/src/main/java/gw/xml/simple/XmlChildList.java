/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;

class XmlChildList<T extends SimpleXmlNode> implements List<T> {

  private List<T> _delegate = new ArrayList<T>();
  private SimpleXmlNode _owner;

  public XmlChildList(SimpleXmlNode owner) {
    _owner = owner;
  }

  @Override
  public int size() {
    return _delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return _delegate.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return _delegate.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return new WrappingIterator(_delegate.iterator());
  }

  @Override
  public Object[] toArray() {
    return _delegate.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return _delegate.toArray(a);
  }

  @Override
  public boolean add(T t) {
    validateArgumentForAdd(t, "XmlChildList.add()");
    t.setParent(_owner);
    return _delegate.add(t);
  }

  @Override
  public boolean remove(Object o) {
    boolean result = _delegate.remove(o);
    if (result) {
      ((SimpleXmlNode) o).setParent(null);
    }
    return result;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return _delegate.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    validateArgumentsForAdd(c, "addAll(Collection)");
    setParents(c);
    return _delegate.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    validateArgumentsForAdd(c, "addAll(int, Collection)");
    setParents(c);
    return _delegate.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean modified = false;
    for (int i = 0; i < _delegate.size(); i++) {
      if (c.contains(_delegate.get(i))) {
        remove(i);
        i--;
        modified = true;
      }
    }

    return modified;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean modified = false;
    for (int i = 0; i < _delegate.size(); i++) {
      if (!c.contains(_delegate.get(i))) {
        remove(i);
        i--;
        modified = true;
      }
    }

    return modified;
  }

  @Override
  public void clear() {
    for (SimpleXmlNode node : _delegate) {
      node.setParent(null);
    }
    _delegate.clear();
  }

  @Override
  public T get(int index) {
    return _delegate.get(index);
  }

  @Override
  public T set(int index, T element) {
    // We allow set() to be called with an element already in the list because otherwise sorting the list
    // of children would result in exceptions.
    validateArgumentForSet(element, "XmlChildList.set(int, T)");

    if (element.getParent() == null) {
      element.setParent(_owner);
    }
    T original = _delegate.set(index, element);

    // This isn't efficient, and if that's an issue in practice it could be replaced with
    // reference counting within SimpleXmlNode instead.  Since set(int, T) calls can result
    // in an element being added to the list multiple times, we can't just assume that
    // the call will result in the replaced element being removed from the list entirely,
    // so we have to check
    if (!contains(original)) {
      original.setParent(null);
    }
    return original;
  }

  @Override
  public void add(int index, T element) {
    validateArgumentForAdd(element, "XmlChildList.add(int, T)");
    element.setParent(_owner);
    _delegate.add(index, element);
  }

  @Override
  public T remove(int index) {
    T result = _delegate.remove(index);
    result.setParent(null);
    return result;
  }

  @Override
  public int indexOf(Object o) {
    return _delegate.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return _delegate.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return new WrappingListIterator(_delegate.listIterator());
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return new WrappingListIterator(_delegate.listIterator(index));
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException("XmlChildList.subList(int, int) is not currently supported");
  }

  // ---------------------------- Private helper methods

  private void setParents(Collection<? extends T> c) {
    for (T arg : c) {
      arg.setParent(_owner);
    }
  }

  private void validateArgumentsForAdd(Collection<? extends T> c, String functionName) {
    for (T arg : c) {
      validateArgumentForAdd(arg, functionName);
    }
  }

  private void validateArgumentForAdd(T arg, String functionName) {
    if (arg == null) {
      throw new IllegalArgumentException(functionName + " cannot be called with a null argument");
    } else if (arg.getParent() != null) {
      throw new IllegalArgumentException(functionName + " cannot be called with an argument that already has a non-null parent");
    }
  }

  private void validateArgumentForSet(T element, String functionName) {
    if (element == null) {
      throw new IllegalArgumentException(functionName + " cannot be called with a null argument");
    } else if (element.getParent() != null && element.getParent() != _owner) {
      throw new IllegalArgumentException(functionName + " cannot be called with an element that has a non-null parent unless that parent is already set to the owner of this list");
    }
  }

  private class WrappingIterator implements Iterator<T> {
    private Iterator<T> _wrapped;
    private T _lastRet;

    private WrappingIterator(Iterator<T> wrapped) {
      _wrapped = wrapped;
    }

    @Override
    public boolean hasNext() {
      return _wrapped.hasNext();
    }

    @Override
    public T next() {
      _lastRet = _wrapped.next();
      return _lastRet;
    }

    @Override
    public void remove() {
      _wrapped.remove();
      _lastRet.setParent(null);
    }
  }

  private class WrappingListIterator implements ListIterator<T> {

    private ListIterator<T> _wrapped;
    private T _lastRet;

    private WrappingListIterator(ListIterator<T> wrapped) {
      _wrapped = wrapped;
    }

    @Override
    public boolean hasNext() {
      return _wrapped.hasNext();
    }

    @Override
    public T next() {
      _lastRet = _wrapped.next();
      return _lastRet;
    }

    @Override
    public boolean hasPrevious() {
      return _wrapped.hasPrevious();
    }

    @Override
    public T previous() {
      _lastRet = _wrapped.previous();
      return _lastRet;
    }

    @Override
    public int nextIndex() {
      return _wrapped.nextIndex();
    }

    @Override
    public int previousIndex() {
      return _wrapped.previousIndex();
    }

    @Override
    public void remove() {
      _wrapped.remove();
      _lastRet.setParent(null);
    }

    @Override
    public void set(T t) {
      validateArgumentForSet(t, "WrappingListIterator.set(T)");
      _wrapped.set(t);
      if (t.getParent() == null) {
        t.setParent(_owner);
      }
      if (!contains(_lastRet)) {
        _lastRet.setParent(null);
      }
    }

    @Override
    public void add(T t) {
      validateArgumentForAdd(t, "WrappingListIterator.add(T)");
      _wrapped.add(t);
      t.setParent(_owner);
    }
  }
}
