/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.util.List;

public class FilteredListModel<T> extends AbstractListModel {
  private List<T> items;
  private List<T> filteredItems;
  private Predicate<T> predicate;

  public FilteredListModel(List<T> items, Predicate<T> predicate) {
    this.items = Preconditions.checkNotNull(items);
    this.predicate = predicate;
    update();
  }

  public void setItems(List<T> items) {
    this.items = Preconditions.checkNotNull(items);
  }

  public void setPredicate(Predicate<T> predicate) {
    this.predicate = Preconditions.checkNotNull(predicate);
  }

  public void update() {
    this.filteredItems = Lists.newArrayList(Iterables.filter(items, predicate));
    fireContentsChanged(this, -1, -1);
  }

  public int getOriginalSize() {
    return items.size();
  }

  // ListModel
  @Override
  public int getSize() {
    return filteredItems.size();
  }

  @Override
  public Object getElementAt(int index) {
    return filteredItems.get(index);
  }
}