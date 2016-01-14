/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.graph.DFSTBuilder;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.ITypeResolver;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.SafeCallable;
import gw.plugin.ij.util.InjectedElementEditor;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import gw.plugin.ij.util.TypeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class DefaultTypeResolver implements ITypeResolver {

  @Nullable
  public PsiElement resolveType(@NotNull final IType theType, @NotNull final PsiElement context) {
    if (theType instanceof ICompoundType) {
      return null;
    }

    ProgressManager.checkCanceled();

    return ExecutionUtil.execute(new SafeCallable<PsiElement>(context) {
      @Nullable
      public PsiElement execute() throws Exception {
        IType type = theType;
        if (type instanceof IMetaType) {
          return null;
        }

        type = TypeUtil.getConcreteType(type);
        if (type instanceof ITypeVariableType) {
          return PsiTypeResolver.resolveTypeVariable((ITypeVariableType) type, context);
        } else {
          return resolveType(type.getName(), context);
        }
      }
    });
  }

  @Nullable
  public PsiClass resolveType(@Nullable final String strFullName, @NotNull final PsiElement ctx) {
    if (strFullName == null) {
      return null;
    }
    return ExecutionUtil.execute(new SafeCallable<PsiClass>(ctx) {
      @Nullable
      public PsiClass execute() throws Exception {
        String fqn = getNonProxyClassName(strFullName);
        PsiElement aClass = resolveJavaType(fqn, ctx.getResolveScope());
        if (aClass == null) {
          if (GosuScratchpadFileImpl.FQN.equals(fqn)) {
            GosuScratchpadFileImpl scratchpadFile = GosuScratchpadFileImpl.instance(ctx.getProject());
            aClass = scratchpadFile == null ? null : scratchpadFile.getPsiClass();
          } else if (fqn.startsWith(GosuScratchpadFileImpl.FQN)) {
            aClass = resolveScratchpadInnerClass(fqn, ctx);
          } else {
            IType type = TypeSystem.getByFullNameIfValid(fqn);
            aClass = resolveGosuType(type, ctx.getProject(), ctx, ctx.getResolveScope());
          }
        }
        if (aClass == null && strFullName.contains(FileUtil.MAGIC_INJECTED_SUFFIX)) {
          PsiClass psiClass = ((IGosuFileBase) ctx.getContainingFile()).getPsiClass();
          //do not uncomment this "if" as it return false for injected fragment and corresponding injected editor, but should return true
//          if (strFullName.equals(psiClass.getQualifiedName()))
          {
            final GosuProgramFileImpl program = (GosuProgramFileImpl) InjectedElementEditor.getOriginalFile(psiClass.getContainingFile());
            final PsiClass[] classes = program != null ? program.getClasses() : new PsiClass[]{psiClass};
            aClass = classes.length > 0 ? classes[0] : psiClass;
          }
        }
        return aClass instanceof PsiClass ? (PsiClass) aClass : null;
      }
    });
  }

  // private

  @Nullable
  private static PsiElement resolveTypeImpl(@Nullable IType type, Project project, PsiElement ctx, @NotNull GlobalSearchScope scope) {
    if (type == null) {
      return null;
    }

    final String strFullName = getNonProxyClassName(type.getName());
    PsiElement aClass = resolveJavaType(strFullName, scope);
    if (aClass == null) {
      aClass = resolveGosuType(type, project, ctx, scope);
    }
    return aClass;
  }

  private static String getNonProxyClassName(String strFullName) {
    return IGosuClass.ProxyUtil.isProxyClass(strFullName) ?
        IGosuClass.ProxyUtil.getNameSansProxy(strFullName) : strFullName;
  }

  @Nullable
  private static PsiElement resolveJavaType(@NotNull String strFullName, @NotNull GlobalSearchScope scope) {
    final Project project = scope.getProject();

    PsiElement aClass;
    if (JavaTypes.IGOSU_OBJECT().getName().equals(strFullName)) {
      aClass = JavaPsiFacadeUtil.getElementFactory(project).createTypeByFQClassName(CommonClassNames.JAVA_LANG_OBJECT, scope).resolve();
    } else {
      PsiClass[] classes = JavaPsiFacadeUtil.findClasses(project, strFullName, scope);
      if(classes.length == 0) {
        aClass = null;
      } else if(classes.length == 1) {
        aClass = classes[0];
      } else {
        aClass = getTheRightClass(classes);
      }
    }
    if (aClass == null) {
      // this will find Gosu classes in jar files
      // the search is done this way to avoid the results being filtered out according to Java rules
      try {
        Collection<PsiClass> classes = StubIndex.getInstance().get(JavaFullClassNameIndex.getInstance().getKey(), strFullName.hashCode(), project, scope);
        if (!classes.isEmpty()) {
          aClass = classes.iterator().next();
        }
      } catch (IndexNotReadyException e) {
        // Nothing to do
      }
    }
    return aClass;
  }

  private static PsiElement getTheRightClass(PsiClass[] classes) {
    final Comparator<Module> cmp = new DFSTBuilder<>(ModuleManager.getInstance(classes[0].getProject()).moduleGraph(true)).comparator();
    Arrays.sort(classes, new Comparator<PsiClass>() {
      public int compare(PsiClass c1, PsiClass c2) {
        Module m1 = ModuleUtil.findModuleForPsiElement(c1);
        Module m2 = ModuleUtil.findModuleForPsiElement(c2);
        if (m1 == null || m2 == null) {
          if (m1 == null && m2 == null) {
            return 0;
          } else if (m1 == null) {
            return +1;
          } else {
            return -1;
          }
        }
        return -cmp.compare(m1, m2);
      }
    });
    return classes[0];

//    if(classes.length > 0) {
//      VirtualFile virtualFile = classes[0].getContainingFile().getVirtualFile();
//      final FileShadowingResolverExtensionBean[] extensions = Extensions.getExtensions(FileShadowingResolverExtensionBean.EP_NAME);
//      IFile match = null;
//      for (FileShadowingResolverExtensionBean extension : extensions) {
//        IFileShadowingResolver resolver = extension.getHandler();
//        match = resolver.resolveType(virtualFile);
//        if (match != null) {
//          break;
//        }
//      }
//      if(match == null) {
//        return classes[0];
//      }
//      for(PsiClass c : classes) {
//        IFile f = FileUtil.toIFile(c.getContainingFile().getVirtualFile());
//        if(f.equals(match)) {
//          return c;
//        }
//      }
//    }
//    return null;
  }

  @Nullable
  private static PsiElement resolveGosuType(@Nullable IType type, Project project, PsiElement context, @NotNull GlobalSearchScope scope) {
    PsiElement aClass = null;

    if (type instanceof IGosuClass && ((IGosuClass) type).isAnonymous()) {
      IGosuClass enclosingType = (IGosuClass) type.getEnclosingType();
      if (enclosingType instanceof IBlockClass) {
        enclosingType = (IGosuClass) enclosingType.getEnclosingType();
      }
      final PsiElement enclosingPsi = resolveTypeImpl(enclosingType, project, context, scope);
      aClass = resolveAnonymousGosuClass(enclosingPsi, type.getRelativeName());
    }

    if (aClass == null && type != null) {
      aClass = resolveScratchpadInnerClass(type.getName(), context);
    }

    if (aClass == null && type != null &&
        !(type instanceof IErrorType) &&
        !(type instanceof INamespaceType) &&
        !(type instanceof IGosuClass) &&
        !(type instanceof IJavaType) &&
        !(type instanceof IBlockType) &&
        type.getSourceFiles().length > 0) {
      aClass = CustomPsiClassCache.instance().getPsiClass(type);
    }

    return aClass;
  }

  @Nullable
  private static PsiElement resolveAnonymousGosuClass(@Nullable PsiElement psiParent, @NotNull String internalName) {
    if (psiParent == null) {
      return null;
    }

    for (PsiElement child : psiParent.getChildren()) {
      if ((child instanceof IGosuAnonymousClassDefinition && internalName.equals(((IGosuAnonymousClassDefinition) child).getName())) ||
          (child instanceof GosuBlockExpressionImpl && internalName.equals(((GosuBlockExpressionImpl) child).getParsedElement().getBlockGosuClass().getRelativeName()))) {
        return child;
      } else {
        PsiElement psiClass = resolveAnonymousGosuClass(child, internalName);
        if (psiClass != null) {
          return psiClass;
        }
      }
    }
    return null;
  }

  @Nullable
  private static PsiClass resolveScratchpadInnerClass(String fqn, @Nullable PsiElement context) {
    if (context != null) {
      final PsiFile file = context.getContainingFile();
      if (file instanceof GosuScratchpadFileImpl) {
        return findNestedPsiClass(fqn, ((GosuScratchpadFileImpl) file).getPsiClass());
      }
    }
    return null;
  }

  @Nullable
  private static PsiClass findNestedPsiClass(String fqn, @NotNull PsiClass enclosingClass) {
    String strEnclosingName = enclosingClass.getQualifiedName();
    if (strEnclosingName != null && strEnclosingName.equals(fqn)) {
      return enclosingClass;
    }

    for (PsiClass inner : enclosingClass.getInnerClasses()) {
      PsiClass foundClass = findNestedPsiClass(fqn, inner);
      if (foundClass != null) {
        return foundClass;
      }
    }
    return null;
  }
}
