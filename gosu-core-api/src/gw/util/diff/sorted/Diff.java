/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.diff.sorted;

public class Diff<T> {

  private final T _oldItem;
  private final T _newItem;

  Diff(T oldItem, T newItem) {
    this._oldItem = oldItem;
    this._newItem = newItem;
  }

  /**
   * Returns the old item.
   * @return the old item or null
   */
  public T getOldItem() {
    return _oldItem;
  }

  /**
   * Returns the new item.
   * @return the new item or null
   */
  public T getNewItem() {
    return _newItem;
  }

  public String toString() {
    return "[" + _oldItem + ", " + _newItem + "]";
  }

}
