/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuEnhancementInternal;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.IJavaType;

public class RequiresReflectionDeterminer
{

  public static boolean shouldUseReflection( IType declaringClass, ICompilableTypeInternal compilingClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    boolean bRet =
      isEnhancementAccessRequiringReflection( declaringClass, compilingClass, accessibility ) ||
      isEvalProgramBetweenCallingClassAndDeclaringClass( compilingClass, declaringClass, accessibility ) ||
      isDeclaringClassInAncestryOfEnclosingClassesOfEvalProgram( compilingClass, declaringClass, accessibility ) ||
      isCallingClassEnclosedInDifferentPackageFromDeclaringSuperclass( compilingClass, declaringClass, accessibility ) ||
      isGosuClassAccessingProtectedOrInternalMethodOfClassInDifferentClassloader( compilingClass, declaringClass, accessibility ) ||
      isGosuClassAccessingProtectedMemberOfClassNotInHierarchy( compilingClass, declaringClass, accessibility ) ||
      isProgramCompilingDuringDebuggerSuspension( compilingClass, accessibility ) ||
      (isProgramNotEval( compilingClass, declaringClass ) && accessibility != IRelativeTypeInfo.Accessibility.PUBLIC); // for studio debugger expressions
    return bRet;
  }

  private static boolean isProgramCompilingDuringDebuggerSuspension( IType compilingClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    if( accessibility == IRelativeTypeInfo.Accessibility.PUBLIC )
    {
      return false;
    }

    return TypeLord.getOuterMostEnclosingClass( compilingClass ) instanceof IGosuProgram &&
           TypeSystem.isIncludeAll();
  }

  private static boolean isEnhancementAccessRequiringReflection( IType declaringClass, ICompilableTypeInternal callingClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    if( accessibility == IRelativeTypeInfo.Accessibility.PUBLIC || callingClass == null )
    {
      return false;
    }
    else if( callingClass instanceof IGosuEnhancementInternal )
    {
      IGosuEnhancementInternal enhancement = (IGosuEnhancementInternal)callingClass;
      IType pureEnhancedType = TypeLord.getPureGenericType( enhancement.getEnhancedType() );
      IType pureTargetType = TypeLord.getPureGenericType( declaringClass );
      return pureTargetType.isAssignableFrom( pureEnhancedType );
    }
    else
    {
      return isEnhancementAccessRequiringReflection( declaringClass, callingClass.getEnclosingType(), accessibility );
    }
  }

  private static boolean isEvalProgramBetweenCallingClassAndDeclaringClass( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    if( accessibility == IRelativeTypeInfo.Accessibility.PUBLIC )
    {
      return false;
    }

    if( TypeLord.encloses( declaringClass, callingClass ) )
    {
      while( true )
      {
        if( isInSeparateClassLoader( callingClass, declaringClass ) )
        {
          return true;
        }
        else if( callingClass == declaringClass )
        {
          return false;
        }
        callingClass = callingClass.getEnclosingType();
      }
    }
    else if( declaringClass instanceof IGosuClassInternal && TypeLord.encloses( callingClass, declaringClass ) )
    {
      if( TypeLord.encloses( declaringClass, callingClass ) )
      {
        while( true )
        {
          if( isInSeparateClassLoader( (IGosuClassInternal)declaringClass, null ) )
          {
            return true;
          }
          else if( callingClass == declaringClass )
          {
            return false;
          }
          declaringClass = declaringClass.getEnclosingType();
        }
      }
    }
    return false;
  }

  private static boolean isDeclaringClassInAncestryOfEnclosingClassesOfEvalProgram( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    if( accessibility == IRelativeTypeInfo.Accessibility.PUBLIC || callingClass == null )
    {
      return false;
    }
    if( isInSeparateClassLoader( callingClass, declaringClass ) && isDeclaringClassInAncestryOfEnclosingClasses( callingClass, declaringClass ) )
    {
      return true;
    }
    else
    {
      return isDeclaringClassInAncestryOfEnclosingClassesOfEvalProgram( callingClass.getEnclosingType(), declaringClass, accessibility );
    }
  }

  private static boolean isCallingClassEnclosedInDifferentPackageFromDeclaringSuperclass( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    return accessibility == IRelativeTypeInfo.Accessibility.PROTECTED &&
           isEnclosedInSubtypeOfClass( callingClass, declaringClass ) &&
           !getTopLevelNamespace( callingClass ).equals( getTopLevelNamespace( declaringClass ) );
  }

  // If we're calling a protected or internal method on a class in a different classloader
  // and the package names are the same, then we assume the access is being allowed by Gosu in virtue of the package matching.
  // Java will blow up if the package-level access is relied upon across class loaders, though, so we make the call reflectively.
  private static boolean isGosuClassAccessingProtectedOrInternalMethodOfClassInDifferentClassloader( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    return (accessibility == IRelativeTypeInfo.Accessibility.PROTECTED ||
            accessibility == IRelativeTypeInfo.Accessibility.INTERNAL ||
            AccessibilityUtil.forType( declaringClass ) == IRelativeTypeInfo.Accessibility.INTERNAL)
           && (javaClassLoadsInSeparateLoader( declaringClass ) || isInSeparateClassLoader( callingClass, declaringClass ))
           && getTopLevelNamespace( callingClass ).equals( getTopLevelNamespace( declaringClass ) );
  }

  private static boolean javaClassLoadsInSeparateLoader( IType declaringClass ) {
    return (!GosuClassPathThing.canWrapChain() && declaringClass instanceof IJavaType);
  }

  private static boolean isGosuClassAccessingProtectedMemberOfClassNotInHierarchy( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    // This is legal in Gosu if the member is accessed indirectly through a subclass that lives in same package as calling class
    return accessibility == IRelativeTypeInfo.Accessibility.PROTECTED && !declaringClass.isAssignableFrom( callingClass );
  }

  private static String getTopLevelNamespace( IType type )
  {
    if( type.getEnclosingType() == null )
    {
      return type.getNamespace();
    }
    else
    {
      return getTopLevelNamespace( type.getEnclosingType() );
    }
  }

  private static boolean isEnclosedInSubtypeOfClass( ICompilableTypeInternal potentiallyEnclosedClass, IType potentialSuperType )
  {
    if( potentiallyEnclosedClass == null )
    {
      return false;
    }
    else if( TypeLord.isSubtype( potentiallyEnclosedClass, potentialSuperType ) )
    {
      return true;
    }
    else
    {
      return isEnclosedInSubtypeOfClass( potentiallyEnclosedClass.getEnclosingType(), potentialSuperType );
    }
  }

  private static boolean isInSeparateClassLoader( ICompilableTypeInternal callingClass, IType declaringClass )
  {
    return callingClass != declaringClass &&
           (isInEvalProgram( callingClass ) ||
            isInEvalProgram( declaringClass ) ||
            isThrowawayProgram( callingClass ) ||
            isThrowawayProgram( declaringClass ) ||
            BytecodeOptions.isSingleServingLoader());
  }

  private static boolean isDeclaringClassInAncestryOfEnclosingClasses( ICompilableTypeInternal callingClass, IType declaringClass )
  {
    if( callingClass == null )
    {
      return false;
    }
    else if( TypeLord.isSubtype( callingClass, declaringClass ) )
    {
      return true;
    }
    else
    {
      return isDeclaringClassInAncestryOfEnclosingClasses( callingClass.getEnclosingType(), declaringClass );
    }
  }

  private static boolean isInEvalProgram( IType gsClass )
  {
    if( gsClass == null ) {
      return false;
    }
    return (gsClass instanceof IGosuProgram && ((IGosuProgram)gsClass).isAnonymous()) ||
           isInEvalProgram( gsClass.getEnclosingType() );
  }

  private static boolean isThrowawayProgram( IType gsClass )
  {
    return gsClass instanceof IGosuProgram &&
           ((IGosuProgramInternal)gsClass).isThrowaway();
  }

  private static boolean isProgramNotEval( IType callingClass, IType declaringClass )
  {
    return callingClass != declaringClass &&
           callingClass instanceof IGosuProgram &&
           !((IGosuProgram)callingClass).isAnonymous();
  }

}
