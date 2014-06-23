/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IParsedElementWithAtLeastOneDeclaration extends IParsedElement
{
  /**
   * The offset of the token representing the name for the declaration
   * @param identifierName
   */
  int getNameOffset( String identifierName );
  void setNameOffset( int iOffset, String identifierName );

  /**
   * @param identifierName
   * @return True if this statement declares the given identifier; false otherwise
   */
  boolean declares( String identifierName);
  
  /**
   * @return all names declared by this element
   */
  String[] getDeclarations();
}
