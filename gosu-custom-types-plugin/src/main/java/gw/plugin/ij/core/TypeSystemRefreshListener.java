/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import gw.fs.IResource;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassLightParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TypeSystemRefreshListener implements IFileListener {
  private final boolean DEBUG = false;

  @Override
  public void modified(IResource file) {
    TypeSystem.refreshed( file );
  }

  @Override
  public void modified(IResource file, String oldText, String newText) {
    List<String> oldInnerClasses = new GosuClassLightParser(oldText).getInnerClasses();
    List<String> newInnerClasses = new GosuClassLightParser(newText).getInnerClasses();
    Set<String> addedClasses = new HashSet<>(newInnerClasses);
    addedClasses.removeAll(oldInnerClasses);
    Set<String> removedClasses = new HashSet<>(oldInnerClasses);
    removedClasses.removeAll(newInnerClasses);
    for (String name : removedClasses) {
      TypeSystem.deleted( file, name );
    }
    for (String name : addedClasses) {
      TypeSystem.created( file, name );
    }

    if (DEBUG) {
      if (!addedClasses.isEmpty() || !removedClasses.isEmpty()) {
        System.out.println("+ " + toString(addedClasses) + " , - " + toString(removedClasses));
      }
    }
  }

  private String toString(Set<String> set) {
    String s = "";
    for (String e : set) {
      s += e + " ";
    }
    return s;
  }

  @Override
  public void deleted(IResource file) {
    TypeSystem.deleted( file );
  }

  @Override
  public void created(IResource file) {
    TypeSystem.created( file );
  }
}
