/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import gw.config.CommonServices;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.Keyword;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ClassGenerator {

  public StringBuilder genClassProxy(IType type) {
    if (type.isParameterizedType()) {
      type = type.getGenericType();
    }
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(type.getNamespace()).append('\n');
    genClassImpl(type, sb);
    return sb;
  }

  private void genClassImpl(IType type, StringBuilder sb) {
    if (type.isAbstract()) {
      sb.append("abstract ");
    }
    if (type.getEnclosingType() != null && Modifier.isStatic(type.getModifiers())) {
      sb.append("static ");
    }

    sb.append("class ").append(getRelativeName(type));

    IType supertype = type.getSupertype();
    if (supertype != null) {
      sb.append(" extends " + supertype.getName());
    }
    IType[] interfaces = type.getInterfaces();
    if (interfaces.length != 0) {
      sb.append(" implements ");
      for (IType anInterface : interfaces) {
        sb.append(anInterface.getName() + ", ");
      }
    }


    sb.append("{\n");

    ITypeInfo ti = type.getTypeInfo();

    // Constructors
    for (Object o : ti.getConstructors()) {
      IConstructorInfo ci = (IConstructorInfo) o;
      genConstructor(sb, ci);
    }

    // Properties
    for (Object o : ti.getProperties()) {
      IPropertyInfo pi = (IPropertyInfo) o;
      genProperty(pi, sb, type);
    }

    // Methods
    for (Object o : ti.getMethods()) {
      IMethodInfo mi = (IMethodInfo) o;
      genMethodImpl(sb, mi, type);
    }
    // Inner classes/interfaces
    if (type instanceof IHasInnerClass) {
      for (IType innerClass : ((IHasInnerClass) type).getInnerClasses()) {
        if ((Modifier.isPublic(innerClass.getModifiers()) ||
            Modifier.isProtected(innerClass.getModifiers())) && //## todo: maybe change to include internal for bytecode?
            !Modifier.isFinal(innerClass.getModifiers())) {
          if (innerClass.isInterface()) {
            genInterfaceImpl(innerClass, sb);
          } else if (Modifier.isStatic(innerClass.getModifiers())) // must be static, otherwise can't really generate a super class proxy
          {
            genClassImpl(innerClass, sb);
          }
        }
      }
    }
    sb.append("}\n");
  }

  private String getRelativeName(IType type) {
    String strName = TypeSystem.getGenericRelativeName(type, false);
    if (type.getEnclosingType() != null) {
      int iParamsIndex = strName.indexOf('<');
      int iIndex = iParamsIndex > 0
          ? strName.substring(0, iParamsIndex).lastIndexOf('.')
          : strName.lastIndexOf('.');
      if (iIndex > 0) {
        strName = strName.substring(iIndex + 1);
      }
    }
    return strName;
  }

  private void genInterfaceImpl(IType type, StringBuilder sb) {
    sb.append(Modifier.toModifierString(type.getModifiers())).append(" interface ").append(getRelativeName(type)).append("{\n");

    ITypeInfo ti = type.getTypeInfo();

    // Interface properties
    for (Object o : ti.getProperties()) {
      IPropertyInfo pi = (IPropertyInfo) o;
      genInterfacePropertyDecl(sb, pi, type);
    }

    // Interface methods
    for (Object o : ti.getMethods()) {
      IMethodInfo mi = (IMethodInfo) o;
      genInterfaceMethodDecl(sb, mi);
    }

    // Inner interfaces
    if (type instanceof IHasInnerClass) {
      for (IType iface : ((IHasInnerClass) type).getInnerClasses()) {
        if (iface.isInterface() &&
            (Modifier.isPublic(iface.getModifiers()) ||
                Modifier.isProtected(iface.getModifiers())) && //## todo: maybe change to include internal for bytecode?
            !Modifier.isFinal(iface.getModifiers())) {
          genInterfaceImpl(iface, sb);
        }
      }
    }
    sb.append("}\n");
  }

  private void genMethodImpl(StringBuilder sb, IMethodInfo mi, IType type) {
    if (mi.isPrivate() || mi.isInternal()) {
      return;
    }

    if (mi.isStatic() && mi.getDisplayName().indexOf('$') < 0) {
      genStaticMethod(sb, mi, type);
    } else {
      genMemberMethod(sb, mi);
    }
  }

  private void genConstructor(StringBuilder sb, IConstructorInfo ci) {
    if (ci.isPrivate()) {
      return;
    }

    sb.append("  construct(");
    IParameterInfo[] params = ci.getParameters();
    for (int i = 0; i < params.length; i++) {
      IParameterInfo pi = params[i];
      sb.append(' ').append("p").append(i).append(" : ").append(pi.getFeatureType().getName())
          .append(i < params.length - 1 ? ',' : ' ');
    }
    sb.append(")").append("{}\n");
  }

  private StringBuilder appendVisibilityModifier(IAttributedFeatureInfo fi) {
    StringBuilder sb = new StringBuilder();
    if (fi.isProtected()) {
      sb.append(Keyword.KW_protected).append(" ");
    } else if (fi.isInternal()) {
      sb.append(Keyword.KW_internal).append(" ");
    }
    return sb;
  }

  private void genMemberMethod(StringBuilder sb, IMethodInfo mi) {
    if (!canExtendMethod(mi)) {
      return;
    }

    StringBuilder sbModifiers = buildModifiers(mi);
    if (mi.getDescription() != null) {
      sb.append("/** ").append(mi.getDescription()).append(" */");
    }
    sb.append("  ").append(sbModifiers).append("function ").append(mi.getDisplayName()).append(TypeInfoUtil.getTypeVarList(mi)).append("(");
    IParameterInfo[] params = mi.getParameters();
    for (int i = 0; i < params.length; i++) {
      IParameterInfo pi = params[i];
      sb.append(' ').append("p").append(i).append(" : ").append(pi.getFeatureType().getName())
          .append(i < params.length - 1 ? ',' : ' ');
    }
    sb.append(") : ").append( mi.getReturnType().getName()).append("\n");
    if (!mi.isAbstract()) {
      generateStub(sb, mi.getReturnType());
    }
  }

  private void generateStub(StringBuilder sb, IType returnType) {
    sb.append("{\n")
        .append((returnType == JavaTypes.pVOID()
            ? ""
            : "    return " +
            (!returnType.isPrimitive()
                ? "null"
                : CommonServices.getCoercionManager().convertNullAsPrimitive(returnType, false))));
    sb.append("}\n");
  }

  private boolean canExtendMethod(IMethodInfo mi) {
    return !isPropertyMethod(mi);

  }

  private void genStaticMethod(StringBuilder sb, IMethodInfo mi, IType type) {
    if (isPropertyMethod(mi)) {
      // We favor properties over methods -- gotta pick one
      return;
    }

    StringBuilder sbModifiers = appendVisibilityModifier(mi);
    if (mi.getDescription() != null) {
      sb.append("/** ").append(mi.getDescription()).append(" */");
    }
    sb.append("  ").append(sbModifiers).append("static function ").append(mi.getDisplayName()).append(TypeInfoUtil.getTypeVarList(mi)).append("(");
    IParameterInfo[] params = mi.getParameters();
    for (int i = 0; i < params.length; i++) {
      IParameterInfo pi = params[i];
      sb.append(' ').append("p").append(i).append(" : ").append(pi.getFeatureType().getName())
          .append(i < params.length - 1 ? ',' : ' ');
    }
    sb.append(") : ").append( mi.getReturnType().getName()).append("\n")
        .append("{\n")
        .append((mi.getReturnType() == GosuParserTypes.NULL_TYPE()
            ? ""
            : "    return "))
        .append(type.getName()).append('.').append(mi.getDisplayName()).append(TypeInfoUtil.getTypeVarListNoBounds(mi)).append("(");
    for (int i = 0; i < params.length; i++) {
      sb.append(' ').append("p").append(i).append(i < params.length - 1 ? ',' : ' ');
    }
    sb.append(");\n");
    sb.append("}\n");
  }

  private void genInterfaceMethodDecl(StringBuilder sb, IMethodInfo mi) {
    if (isPropertyMethod(mi)) {
      return;
    }
    if (mi.getDisplayName().equals("hashCode") || mi.getDisplayName().equals("equals") || mi.getDisplayName().equals("toString")) {
      if (!mi.getOwnersType().getName().equals(IGosuObject.class.getName())) {
        return;
      }
    }
    if (mi.getDescription() != null) {
      sb.append("/** ").append(mi.getDescription()).append(" */");
    }
    sb.append("  function ").append(mi.getDisplayName()).append(TypeInfoUtil.getTypeVarList(mi)).append("(");
    IParameterInfo[] params = mi.getParameters();
    for (int i = 0; i < params.length; i++) {
      IParameterInfo pi = params[i];
      sb.append(' ').append("p").append(i).append(" : ").append(pi.getFeatureType().getName());
      sb.append(i < params.length - 1 ? ',' : ' ');
    }
    sb.append(") : ").append( mi.getReturnType().getName()).append(";\n");
  }

  private boolean isPropertyMethod(IMethodInfo mi) {
    return isPropertyGetter(mi) ||
        isPropertySetter(mi);
  }

  private boolean isPropertyGetter(IMethodInfo mi) {
    return isPropertyGetter(mi, "get") ||
        isPropertyGetter(mi, "is");
  }

  private boolean isPropertySetter(IMethodInfo mi) {
    String strMethod = mi.getDisplayName();
    if (strMethod.startsWith("set") &&
        strMethod.length() > 3 &&
        mi.getParameters().length == 1 &&
        mi.getReturnType() == JavaTypes.pVOID()) {
      String strProp = strMethod.substring("set".length());
      if (Character.isUpperCase(strProp.charAt(0))) {
        ITypeInfo ti = (ITypeInfo) mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
            ? ((IRelativeTypeInfo) ti).getProperty(mi.getOwnersType(), strProp)
            : ti.getProperty(strProp);
        if (pi != null && pi.isReadable() &&
            pi.getFeatureType().getName().equals( mi.getParameters()[0].getFeatureType().getName())) {
          return !Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() );
        }
      }
    }
    return false;
  }

  private boolean isPropertyGetter(IMethodInfo mi, String strPrefix) {
    String strMethod = mi.getDisplayName();
    if (strMethod.startsWith(strPrefix) &&
        mi.getParameters().length == 0) {
      String strProp = strMethod.substring(strPrefix.length());
      if (strProp.length() > 0 && Character.isUpperCase(strProp.charAt(0))) {
        ITypeInfo ti = (ITypeInfo) mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
            ? ((IRelativeTypeInfo) ti).getProperty(mi.getOwnersType(), strProp)
            : ti.getProperty(strProp);
        if (pi != null && pi.getFeatureType().getName().equals( mi.getReturnType().getName())) {
          return !Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() );
        }
      }
    }
    return false;
  }

  private void genInterfacePropertyDecl(StringBuilder sb, IPropertyInfo pi, IType iType) {
    if (pi.isStatic()) {
      genStaticProperty(pi, sb, iType);
      return;
    }
    if (!pi.isReadable()) {
      return;
    }
    IType type = pi.getFeatureType();
    if (pi.getDescription() != null) {
      sb.append("/** ").append(pi.getDescription()).append(" */");
    }
    sb.append(" property get ").append(pi.getName()).append("() : ").append(type.getName()).append("\n");
    if (pi.isWritable(pi.getOwnersType())) {
      sb.append(" property set ").append(pi.getName()).append("( _proxy_arg_value : ").append(type.getName()).append(" )\n");
    }
  }

  private void genProperty(IPropertyInfo pi, StringBuilder sb, IType type) {
    if (pi.isPrivate() || pi.isInternal()) {
      return;
    }

    if (pi.isStatic()) {
      genStaticProperty(pi, sb, type);
    } else {
      genMemberProperty(pi, sb, type);
    }
  }

  private void genMemberProperty(IPropertyInfo pi, StringBuilder sb, IType type) {
    if (pi.isStatic()) {
      return;
    }

    if (Keyword.isKeyword( pi.getName() ) && !Keyword.isValueKeyword( pi.getName() )) {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    {
      IMethodInfo mi = getPropertyGetMethod(pi, type);
      boolean bFinal = false;

      if (mi != null && !bFinal) {
        if (mi.getDescription() != null) {
          sb.append("/** ").append(mi.getDescription()).append(" */");
        }
        StringBuilder sbModifiers = buildModifiers(mi);
        sb.append("  ").append(sbModifiers).append("property get ").append(pi.getName()).append("() : ").append( pi.getFeatureType().getName()).append("\n");
        if (!mi.isAbstract()) {
          generateStub(sb, mi.getReturnType());
        }
      } else {
        StringBuilder sbModifiers;
        boolean bAbstact = false;
        if (bFinal) {
          bAbstact = mi.isAbstract();
          sbModifiers = buildModifiers(mi);
        } else {
          sbModifiers = appendVisibilityModifier(pi);
        }
        sb.append("  ").append(sbModifiers).append("property get ").append(pi.getName()).append("() : ").append( pi.getFeatureType().getName()).append("\n");
        if (!bAbstact) {
          generateStub(sb, pi.getFeatureType() );
        }
      }

      mi = getPropertySetMethod(pi, type);
      bFinal = false;

      if (mi != null && !bFinal) {
        StringBuilder sbModifiers = buildModifiers(mi);
        if (pi.isWritable(pi.getOwnersType())) {
          sb.append("  ").append(sbModifiers).append("property set ").append(pi.getName()).append("( _proxy_arg_value : ").append( pi.getFeatureType().getName()).append(" )\n");
          if (!mi.isAbstract()) {
            generateStub(sb, JavaTypes.pVOID());
          }
        }
      } else {
        if (pi.isWritable(type.getEnclosingType() != null ? null : pi.getOwnersType())) {
          StringBuilder sbModifiers;
          boolean bAbstact = false;
          if (bFinal) {
            bAbstact = mi.isAbstract();
            sbModifiers = buildModifiers(mi);
          } else {
            sbModifiers = appendVisibilityModifier(pi);
          }
          sb.append("  ").append(sbModifiers).append("property set ").append(pi.getName()).append("( _proxy_arg_value : ").append( pi.getFeatureType().getName()).append(" )\n");
          if (!bAbstact) {
            generateStub(sb, JavaTypes.pVOID());
          }
        }
      }
    }
  }

  private StringBuilder buildModifiers(IAttributedFeatureInfo fi) {
    StringBuilder sbModifiers = new StringBuilder();
    if (fi.isAbstract()) {
      sbModifiers.append(Keyword.KW_abstract).append(" ");
    } else if (fi.isFinal()) {
      sbModifiers.append(Keyword.KW_final).append(" ");
    }
    if (fi.isProtected()) {
      sbModifiers.append(Keyword.KW_protected).append(" ");
    } else if (fi.isInternal()) {
      sbModifiers.append(Keyword.KW_internal).append(" ");
    }

    return sbModifiers;
  }

  private IMethodInfo getPropertyGetMethod(IPropertyInfo pi, IType ownerType) {
    ITypeInfo ti = ownerType.getTypeInfo();

    IType propType = pi.getFeatureType();

    String strAccessor = "get" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod(strAccessor);
    if (mi == null || mi.getReturnType() != propType) {
      strAccessor = "is" + pi.getDisplayName();
      mi = ti.getMethod(strAccessor);
    }
    if (mi != null && mi.getReturnType() == propType) {
      return mi;
    }

    return null;
  }

  private IMethodInfo getPropertySetMethod(IPropertyInfo pi, IType ownerType) {
    ITypeInfo ti = ownerType.getTypeInfo();
    IType propType = pi.getFeatureType();

    // Check for Setter

    String strAccessor = "set" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod(strAccessor, propType);
    if (mi != null && mi.getReturnType() == JavaTypes.pVOID()) {
      return mi;
    }

    return null;
  }

  private void genStaticProperty(IPropertyInfo pi, StringBuilder sb, IType type) {
    if (!pi.isStatic()) {
      return;
    }

    if (Keyword.isKeyword( pi.getName() )) {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    if (pi.getDescription() != null) {
      sb.append("/** ").append(pi.getDescription()).append(" */");
    }
  {
      StringBuilder sbModifiers = appendVisibilityModifier(pi);
    sb.append("  ").append(sbModifiers).append("static property get ").append(pi.getName()).append("() : ").append( pi.getFeatureType().getName()).append("\n")
          .append("  {\n")
          .append("    return ").append(type.getName()).append('.').append(pi.getName()).append(";\n")
          .append("  }\n");

      if (pi.isWritable(pi.getOwnersType())) {
        sb
            .append("  static property set ").append(pi.getName()).append("( _proxy_arg_value : ").append( pi.getFeatureType().getName()).append(" )\n")
            .append("  {\n")
            .append("  ").append(type.getName()).append('.').append(pi.getName()).append(" = _proxy_arg_value;\n")
            .append("  }\n");
      }
    }
  }

}
