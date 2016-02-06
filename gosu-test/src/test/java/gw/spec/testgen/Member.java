/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:04:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Member {

  /**
   * The access modifier (i.e. public, private, protected, internal) for this member
   * @return
   */
  AccessModifier getAccessMod();

  /**
   * The scope of this member (static or instance)
   * @return
   */
  Scope getScope();

  /**
   * The context in which this member is declared
   * @return
   */
  DeclarationContext getContext();

  /**
   * The name of this member
   * @return
   */
  String memberName();

  /**
   * The name of the type of this member
   * @return
   */
  String memberTypeName();

  /**
   * The value of this member when it's accessed
   * @return
   */
  String memberValue();

  String generateGosuMemberDeclaration();

  String generateJavaMemberDeclaration();

  boolean isMethod();
  
}