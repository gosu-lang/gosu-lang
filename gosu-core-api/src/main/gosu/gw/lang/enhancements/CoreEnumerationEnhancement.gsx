package gw.lang.enhancements

uses java.util.Enumeration
uses java.util.Collections
uses java.util.List

/**
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreEnumerationEnhancement<E> : Enumeration<E> {

  function toList() : List<E> {
    return Collections.list( this )
  }

}
