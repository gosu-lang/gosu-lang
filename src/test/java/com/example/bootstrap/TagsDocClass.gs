package com.example.bootstrap

/**
 * Created by carson on 10/20/15.
 */
class TagsDocClass {

  /*
   * {@link Sub}
   */
  function hasTypeLink() {}

  /*
 * {@code var x = 10}
 */
  function hasCodeTag() {}

  /*
   * test
   * {@code }
   */
  function emptyCodeTag() {}

  /*
   * test
   * {@docRoot}
   */
  function docRootTag() {}

  /*
   * test
   * {@link #docRootTag()}
   */
  function localFeatureLinkTag() {}

  /*
   * test
   * {@link Super#methodToOverrideDocs()}
   */
  function packageRelativeLink() {}

  /*
   * test
   * {@link com.example.bootstrap.test.AnotherTestClass#foo()}
   */
  function fullyQualifiedLink() {}

}