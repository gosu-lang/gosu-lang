/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.Keyword;
import gw.lang.reflect.IDefaultTypeLoader;

public enum ClassType
{
  Enhancement,
  Program,
  Template,
  Eval,
  Class,
  Interface,
  Structure,
  Annotation,
  Enum,
  JavaClass,
  Unknown
  ;

  public boolean isJava() {
    return this == JavaClass;
  }

  public boolean isGosu() {
    return
        this == Enhancement ||
        this == Program ||
        this == Template ||
        this == Eval ||
        this == Class ||
        this == Interface ||
        this == Structure ||
        this == Annotation ||
        this == Enum;
  }

  public static ClassType getFromFileName(String name) {
    if (name.endsWith( IDefaultTypeLoader.DOT_JAVA_EXTENSION)) {
      return JavaClass;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT )) {
      return Enhancement;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT )) {
      return Program;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT )) {
      return Template;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_CLASS_FILE_EXT) || name.endsWith( ".gr" ) || name.endsWith( ".grs" )) {
      return Class;
    }
    return Unknown;
  }

  public String getExt()
  {
    switch( this )
    {
      case Class:
        return GosuClassTypeLoader.GOSU_CLASS_FILE_EXT;
      case Program:
        return GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT;
      case Enhancement:
        return GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT;
      case Template:
        return GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT;
      default:
        return "";
    }
  }

  public String keyword() {
    switch( this ) {
      case Enhancement:
        return Keyword.KW_enhancement.getName();
      case Interface:
        return Keyword.KW_interface.getName();
      case Structure:
        return Keyword.KW_structure.getName();
      case Annotation:
        return Keyword.KW_annotation.getName();
      case Enum:
        return Keyword.KW_annotation.getName();
      case Class:
      case Program:
      case Template:
      case Eval:
         return Keyword.KW_class.getName();
      default:
        return "<unknown>";
    }
  }

}
