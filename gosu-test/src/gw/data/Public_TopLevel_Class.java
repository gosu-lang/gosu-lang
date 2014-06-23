/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.data;

import gw.data.pac1.Pac1_Public_TopLevel_Class;
import gw.data.pac1.pac1_sub.Pac1Sub_Public_TopLevel_Class;

/**
 */
public class Public_TopLevel_Class extends Public_Super_Class implements Public_Interface
{
  // Qualified
  private gw.data.Public_TopLevel_Class _field_SelfQualified_Class;
  private gw.data.pac1.Pac1_Public_TopLevel_Class _field_Qualified_Class;
  private gw.data.Public_TopLevel_Class.Public_Inner_Class _field_Qualified_Public_Inner_Class;
  private gw.data.Public_Super_Class.Public_Super_Inner_Class _field_Qualified_Public_Super_Inner_Class;
  private gw.data.Public_Interface.Public_Interface_Inner_Class _field_Qualified_Public_Interface_Inner_Class;
  private gw.data.Public_Super_Class.Protected_Super_Inner_Class _field_Qualified_Protected_Super_Inner_Class;
  private gw.data.Public_Super_Class.Package_Super_Inner_Class _field_Qualified_Package_Super_Inner_Class;

  // Relative
  private Public_TopLevel_Class _field_Public_TopLevel_Class;
  private Public_Inner_Class _field_Public_Inner_Class;
  private Public_Super_Class _field_Public_Super_Class;
  private Public_Interface _field_Public_Interface;
  private Pac1_Public_TopLevel_Class _field_ExplicitImport_Class;
  private Pac1Sub_Public_TopLevel_Class _field_ImplicitImport_Class;
  private Public_Super_Inner_Class _field_Public_Super_Inner_Class;
  private Public_Interface_Inner_Class _field_Public_Interface_Inner_Class;
  private Protected_Super_Inner_Class _field_Protected_Super_Inner_Class;
  private Package_Super_Inner_Class _field_Package_Super_Inner_Class;
  private Pac1_SomeClass _field_private_Pac1_SomeClass__Shadow;
  private Public_ShadowMe _field_private_Public_ShadowMe__Shadow;

  // Semi-Qualified
  private Public_TopLevel_Class.Public_Inner_Class _field_SemiQualified_Public_Inner_Class;
  private Public_Super_Class.Public_Super_Inner_Class _field_SemiQualified_Public_Super_Inner_Class;
  private Public_Interface.Public_Interface_Inner_Class _field_SemiQualified_Public_Interface_Inner_Class;
  private Pac1_Public_TopLevel_Class.Pac1_Public_Inner _field_SemiQualified_Pac1_Public_Inner;
  private Public_Super_Class.Protected_Super_Inner_Class _field_SemiQualified_Protected_Super_Inner_Class;
  private Public_Super_Class.Package_Super_Inner_Class _field_SemiQualified_Package_Super_Inner_Class;

  public Public_TopLevel_Class( Public_TopLevel_Class p1 ) {
  }

  public Public_TopLevel_Class method_Public_TopLevel_Class() {
    return null;
  }

  public class Public_Inner_Class {
    // Qualified
    private gw.data.Public_TopLevel_Class _field_SelfQualified_Class;
    private gw.data.pac1.Pac1_Public_TopLevel_Class _field_Qualified_Class;
    private gw.data.Public_TopLevel_Class.Public_Inner_Class _field_Qualified_Public_Inner_Class;
    private gw.data.Public_Super_Class.Public_Super_Inner_Class _field_Qualified_Public_Super_Inner_Class;
    private gw.data.Public_Interface.Public_Interface_Inner_Class _field_Qualified_Public_Interface_Inner_Class;
    private gw.data.Public_Super_Class.Protected_Super_Inner_Class _field_Qualified_Protected_Super_Inner_Class;
    private gw.data.Public_Super_Class.Package_Super_Inner_Class _field_Qualified_Package_Super_Inner_Class;

    // Relative
    private Public_TopLevel_Class _field_Public_TopLevel_Class;
    private Public_Inner_Class _field_Public_Inner_Class;
    private Public_Super_Class _field_Public_Super_Class;
    private Public_Interface _field_Public_Interface;
    private Pac1_Public_TopLevel_Class _field_ExplicitImport_Class;
    private Pac1Sub_Public_TopLevel_Class _field_ImplicitImport_Class;
    private Public_Super_Inner_Class _field_Public_Super_Inner_Class;
    private Public_Interface_Inner_Class _field_Public_Interface_Inner_Class;
    private Protected_Super_Inner_Class _field_Protected_Super_Inner_Class;
    private Package_Super_Inner_Class _field_Package_Super_Inner_Class;
    private Pac1_SomeClass _field_private_Pac1_SomeClass__Shadow;
    private Public_ShadowMe _field_private_Public_ShadowMe__Shadow;

    // Semi-Qualified
    private Public_TopLevel_Class.Public_Inner_Class _field_SemiQualified_Public_Inner_Class;
    private Public_Super_Class.Public_Super_Inner_Class _field_SemiQualified_Public_Super_Inner_Class;
    private Public_Interface.Public_Interface_Inner_Class _field_SemiQualified_Public_Interface_Inner_Class;
    private Pac1_Public_TopLevel_Class.Pac1_Public_Inner _field_SemiQualified_Pac1_Public_Inner;
    private Public_Super_Class.Protected_Super_Inner_Class _field_SemiQualified_Protected_Super_Inner_Class;
    private Public_Super_Class.Package_Super_Inner_Class _field_SemiQualified_Package_Super_Inner_Class;
  }

  // Shadows imported class with same name
  class Pac1_SomeClass {
  }

  // Shadows same-package class with same name
  class Public_ShadowMe {
  }
}
