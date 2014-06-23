/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.util.Stack;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class DescendentsIterator implements Iterator<SimpleXmlNode> {

  private Stack<Iterator<SimpleXmlNode>> _iterators;

  public DescendentsIterator(SimpleXmlNode root) {
    _iterators = new Stack<Iterator<SimpleXmlNode>>();
    _iterators.push(root.getChildren().iterator());
  }

  @Override
  public boolean hasNext() {
    while(!_iterators.isEmpty() && !_iterators.peek().hasNext()) {
      _iterators.pop();
    }

    return !_iterators.isEmpty();
  }

  @Override
  public SimpleXmlNode next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Iterator<SimpleXmlNode> top = _iterators.peek();
    SimpleXmlNode result = top.next();
    if (!result.getChildren().isEmpty()) {
      _iterators.push(result.getChildren().iterator());
    }

    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
