/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.ir.compiler.bytecode.expression.IRMethodCallExpressionCompiler;
import gw.internal.gosu.parser.AsmClassJavaClassInfo;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuEnhancementInternal;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.lang.ir.IRType;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.ILanguageLevel;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuObjectUtil;

public class RequiresReflectionDeterminer
{

  public static boolean shouldUseReflection( IType declaringClass, ICompilableTypeInternal compilingClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    return shouldUseReflection( declaringClass, compilingClass, null, accessibility );
  }
  public static boolean shouldUseReflection( IType declaringClass, ICompilableTypeInternal compilingClass, IRType root, IRelativeTypeInfo.Accessibility accessibility )
  {
    boolean bRet =
      isIllegalAccess( declaringClass, compilingClass, accessibility ) ||
      isEnhancementAccessRequiringReflection( declaringClass, compilingClass, accessibility ) ||
      isEvalProgramBetweenCallingClassAndDeclaringClass( compilingClass, declaringClass, accessibility ) ||
      isDeclaringClassInAncestryOfEnclosingClassesOfEvalProgram( compilingClass, declaringClass, accessibility ) ||
      isCallingClassEnclosedInDifferentPackageFromDeclaringSuperclass( compilingClass, declaringClass, root, accessibility ) ||
      isGosuClassAccessingProtectedOrInternalMethodOfClassInDifferentClassloader( compilingClass, declaringClass, accessibility ) ||
      isProgramCompilingDuringDebuggerSuspension( compilingClass, accessibility ) ||
      (isProgramNotEval( compilingClass, declaringClass ) && accessibility != IRelativeTypeInfo.Accessibility.PUBLIC); // for studio debugger expressions
    return bRet;
  }

  private static boolean isIllegalAccess( IType declaringClass, ICompilableTypeInternal compilingClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    if( accessibility == IRelativeTypeInfo.Accessibility.PUBLIC )
    {
      return false;
    }

    // this is mostly for use with (Lab) debugger expressions e.g., private members are accessible in debugger expressions
    return !FeatureManager.getAccessibilityForClass( declaringClass, compilingClass ).isAccessible( accessibility );
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

  public static boolean isCallingClassEnclosedInDifferentPackageFromDeclaringSuperclass( ICompilableTypeInternal callingClass, IType declaringClass, IRType root, IRelativeTypeInfo.Accessibility accessibility )
  {
    return accessibility == IRelativeTypeInfo.Accessibility.PROTECTED &&
           !getTopLevelNamespace( callingClass ).equals( getTopLevelNamespace( declaringClass ) ) &&
           !(isEnclosedInSubtypeOfClass( callingClass, declaringClass ) && isRootSame( root, callingClass ));
  }

  private static boolean isRootSame( IRType root, ICompilableTypeInternal callingClass )
  {
    IType rootType = root == null ? null : root.getType();
    return rootType == null ||
           rootType == callingClass
           // We can't do this because the verifier prohibits an inner class from accessing an *inherited* protected method (crazy pills).
           // Note Java's compiler works around the genius of this limitation by generating an accessor delegator method on the outer class; while we generate a reflective call.  Someday we will do something similar to Java, someday.
           /*|| TypeLord.encloses( rootType, callingClass )*/;
  }

  // If we're calling a protected or internal method on a class in a different classloader
  // and the package names are the same, then we assume the access is being allowed by Gosu in virtue of the package matching.
  // Java will blow up if the package-level access is relied upon across class loaders, though, so we make the call reflectively.
  private static boolean isGosuClassAccessingProtectedOrInternalMethodOfClassInDifferentClassloader( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    return (accessibility != IRelativeTypeInfo.Accessibility.PUBLIC ||
            AccessibilityUtil.forType( declaringClass ) == IRelativeTypeInfo.Accessibility.INTERNAL ||
            AccessibilityUtil.forType( declaringClass ) == IRelativeTypeInfo.Accessibility.PRIVATE)
           && getTopLevelNamespace( callingClass ).equals( getTopLevelNamespace( declaringClass ) )
           && (isInSeparateClassLoader( callingClass, declaringClass ) ||
               classesLoadInSeparateLoader( callingClass, declaringClass ));

  }

  private static boolean classesLoadInSeparateLoader( ICompilableTypeInternal callingClass, IType declaringClass )
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      return classesLoadInSeparateLoader_Standard( callingClass, declaringClass );
    }

    // To support Guidewire's plugin classloader API calls, we always generate
    // reflective call sites for internal/protected access calls within the same package
    return classesLoadInSeparateLoader_Legacy( callingClass, declaringClass );
  }

  private static boolean classesLoadInSeparateLoader_Legacy( ICompilableTypeInternal callingClass, IType declaringClass )
  {
    if( declaringClass instanceof IFileRepositoryBasedType )
    {
      if( TypeLord.getOuterMostEnclosingClass( declaringClass ) == TypeLord.getOuterMostEnclosingClass( callingClass ) )
      {
        // Classes are in the same file
        return false;
      }
      if( IGosuClass.ProxyUtil.isProxy( callingClass ) || callingClass.getName().contains( IRMethodCallExpressionCompiler.STRUCTURAL_PROXY ) )
      {
        // Proxy classes generated in same class loader
        return false;
      }
      IDirectory callingSourcePath = getSourcePathFor( callingClass );
      IDirectory declaringSourcePath = getSourcePathFor( (IFileRepositoryBasedType)declaringClass );
      return !GosuObjectUtil.equals( callingSourcePath, declaringSourcePath );
    }
    else
    {
      return false;
    }
  }

  private static boolean classesLoadInSeparateLoader_Standard( ICompilableTypeInternal callingClass, IType declaringClass )
  {
    if( declaringClass instanceof IJavaType )
    {
      IJavaType javaClass = (IJavaType)declaringClass;
      IJavaClassInfo classInfo = javaClass.getBackingClassInfo();
      if( classInfo instanceof AsmClassJavaClassInfo )
      {
        // The Asm-based class is indicative of compiling Gosu statically, which
        // means the Gosu class will be dropped in the same package/directory as
        // Java classes it may be using, therefore at runtime they are guaranteed
        // to use the same classloader, therefore internal/package usage does not
        // need to be compiled reflectively.  If they are not in the same loader,
        // it is the responsibility of the user to find a remedy as is the case
        // with normal Java development.
        return false;
      }
      if( classInfo instanceof JavaSourceType )
      {
        // The Source-based class indicates we are compiling Gosu statically from
        // a "special" place, like inside an IDE's process where, for example, a
        // Gosu class can be compiled before the Java class it references, in which
        // case Gosu parsed Java directly from Source.  In this case we can
        // determine if the Java source file and Gosu file are in the same module.
        return callingClass.getTypeLoader().getModule() != declaringClass.getTypeLoader().getModule();
      }
      return true;
    }
    else
    {
      return false;
    }
  }

  private static IDirectory getSourcePathFor( IFileRepositoryBasedType gsClass )
  {
    ISourceFileHandle sourceFileHandle = gsClass.getSourceFileHandle();
    if( sourceFileHandle == null )
    {
      return null;
    }
    IFile filePath = sourceFileHandle.getFile();
    if( filePath == null )
    {
      return null;
    }
    for( IDirectory dir: gsClass.getTypeLoader().getModule().getSourcePath() )
    {
      if( filePath.isDescendantOf( dir ) )
      {
        return dir;
      }
    }
    return null;
  }

  private static boolean isGosuClassAccessingProtectedMemberOfClassNotInHierarchy( ICompilableTypeInternal callingClass, IType declaringClass, IRelativeTypeInfo.Accessibility accessibility )
  {
    // This is legal in Gosu if the member is accessed indirectly through a subclass that lives in same package as calling class
    if( IGosuClass.ProxyUtil.isProxy( declaringClass ) )
    {
      declaringClass = IGosuClass.ProxyUtil.getProxiedType( declaringClass );
    }
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
