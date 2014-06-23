/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.intellij.openapi.module.impl.scopes.ModuleWithDependentsScope;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.PackageEntry;
import com.intellij.psi.search.SearchScope;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.init.ModuleFileUtil;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.expressions.ITemplateStringLiteral;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.util.ClassLord;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.intellij.psi.util.ClassUtil.extractClassName;
import static com.intellij.psi.util.ClassUtil.extractPackageName;
import static com.intellij.psi.util.PsiTreeUtil.getChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static gw.internal.gosu.parser.TypeLord.getPureGenericType;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.parseUsesList;
import static gw.plugin.ij.util.ClassLord.hasImplicitImport;

public class GosuImportReferenceAnalyzer {

  private final CodeStyleSettings settings;
  private final FqnCache<Integer> packageWeights = new FqnCache<>();
  private final Collector collector;
  private final PsiFile file;
  private final ITypeUsesMap usesMap;

  private Integer defaultWeight;
  private String inPackage = "__unknown__";
  private IGosuUsesStatementList newImports;


  public GosuImportReferenceAnalyzer(PsiFile file) {
    this.file = file;
    settings = CodeStyleSettingsManager.getSettings(this.file.getProject());
    collector = new Collector();

    int weight = 0;
    for (PackageEntry entry : settings.IMPORT_LAYOUT_TABLE.getEntries()) {
      if (entry.isStatic()) {
        continue;
      }
      if (entry == PackageEntry.BLANK_LINE_ENTRY ||
          entry == PackageEntry.ALL_OTHER_STATIC_IMPORTS_ENTRY) {
        collector.entries.add(new ImportEntry(weight));
      } else if (entry == PackageEntry.ALL_OTHER_IMPORTS_ENTRY) {
        defaultWeight = weight;
      } else {
        packageWeights.add(entry.getPackageName(), weight);
      }
      ++ weight;
    }

    if (defaultWeight == null) {
      defaultWeight = weight;
    }

    usesMap = findUsesMap(file);
  }

  private ITypeUsesMap findUsesMap(PsiFile file) {
//    if (file instanceof AbstractGosuClassFileImpl) {
//      ITypeUsesMap usesMap = GosuParserConfigurer.getTypeUsesMap((AbstractGosuClassFileImpl) file);
//      if (usesMap != null) {
//        return usesMap;
//      }
//    }
    return TypeSystem.getDefaultTypeUsesMap();
  }

  @Nullable
  public IGosuUsesStatementList getNewImportList() {
    return newImports;
  }

  public void analyze() {
    file.accept(new Analyzer());
  }

  class Analyzer extends PsiRecursiveElementVisitor {

    public void visitElement(PsiElement element) {
      boolean handled = false;
      if (element instanceof IGosuCodeReferenceElement) {
        handled = visitRefElement((IGosuCodeReferenceElement) element);
      } else if (element instanceof IGosuPackageDefinition) {
        visitPackageDefinition((IGosuPackageDefinition) element);
        handled = true;
      }

      if (!handled && element instanceof IGosuPsiElement)   {
        IParsedElement pe = ((IGosuPsiElement) element).getParsedElement();
        if (pe instanceof ITemplateStringLiteral) {
          List<TypeLiteral> literalsInString = new ArrayList<>();
          pe.getContainedParsedElementsByType(TypeLiteral.class, literalsInString);
          for (TypeLiteral literal : literalsInString) {
            processReferenceToClass(literal, element);
          }
        } else if (pe instanceof TypeLiteral) {
          processReferenceToClass((TypeLiteral) pe, element);
        }
      }
      super.visitElement(element);
    }
  }

  public void visitPackageDefinition(@NotNull IGosuPackageDefinition packageDefinition) {
    String inPackage = packageDefinition.getPackageName();
    if (inPackage != null) {
      this.inPackage = inPackage;
    }
  }

  private boolean visitRefElement(@NotNull IGosuCodeReferenceElement referenceElement) {
    boolean handled = false;
    for (IGosuResolveResult resolveResult : (IGosuResolveResult[]) referenceElement.multiResolve(false)) {
      final PsiElement element = resolveResult.getElement();
      if (element == null ||
          element instanceof IGosuTypeVariable ||
          element.getLanguage().getID().equals("Properties") ||
          element.getContainingFile() == file) {
        continue;
      }

      if (!(referenceElement instanceof GosuTypeLiteralImpl)) {
        continue;
      }
      GosuTypeLiteralImpl literal = (GosuTypeLiteralImpl) referenceElement;

      if (underUses(referenceElement)) {
        continue;
      }

      if (element instanceof PsiClass) {
        processReferenceToClass((PsiClass) element, literal);
        handled = true;
      }
    }
    return handled;
  }

  private boolean underUses(PsiElement element) {
    return getParentOfType(element, IGosuUsesStatement.class) != null;
  }

  private final Set<String> implicitImport = new java.util.HashSet<>();

  private boolean isImplicitImport(String fqn) {
    IModule module = GosuModuleUtil.findModuleForPsiElement(file);
    if (module == null) {
      module = TypeSystem.getGlobalModule();
    }
    TypeSystem.pushModule(module);
    try {
      if (hasImplicitImport(fqn, usesMap)) {
        implicitImport.add(fqn);
        return true;
      } else {
        return false;
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private void processReferenceToClass(@NotNull PsiClass referredClass, @NotNull GosuTypeLiteralImpl referenceElement ) {

    final String qualifiedName = purgeFQN(referredClass.getQualifiedName());

    if (isNullOrEmpty(qualifiedName)) {
      return;
    }

    boolean explicitAccess = qualifiedName.equals(purgeFQN(referenceElement.getText()));
    if (explicitAccess) {
      return;
    }

    if (isImplicitImport(qualifiedName)) {
      return;
    }

    //Access to inner classes case
    if (getChildOfType(referenceElement, GosuTypeLiteralImpl.class) != null) {
      return;
    }

    // Reference from type literal
    final String referredClassPackage = getPackageName(referredClass);
    PsiClass outerClass = getOutermostClass(referredClass);
    String actualPackage = outerClass != null ? getPackageName(outerClass) : referredClassPackage;

    if (!inPackage.equals(referredClassPackage)) {
      collector.add(getPackageWeight(actualPackage),
                    referredClassPackage,
                    qualifiedName, referredClass.getName());
    }
  }

  private String purgeFQN(String name) {
    return ClassLord.purgeClassName(name);
  }

  private void processReferenceToClass(@NotNull TypeLiteral referredClass, @NotNull PsiElement referenceElement) {
    if (underUses(referenceElement)) {
      return;
    }
    if (referredClass.getType() == null) {
      return;
    }
    IType type = referredClass.getType().getType();
    if (type == null) {
      return;
    }
    final String qualifiedName = purgeFQN(getPureGenericType(type).getName());
    if (isNullOrEmpty(qualifiedName)) {
      return;
    }

    boolean explicitAccess = qualifiedName.equals(purgeFQN(referenceElement.getText()));
    if (explicitAccess) {
      return;
    }

    if (isImplicitImport(qualifiedName)) {
      return;
    }

    //Access to inner classes case
    if (getChildOfType(referenceElement, GosuTypeLiteralImpl.class) != null) {
      return;
    }

    // Reference from type literal
    final String referredClassPackage = type.getNamespace();
    String actualPackage = referredClassPackage;

    if (!inPackage.equals(referredClassPackage)) {
      collector.add(getPackageWeight(actualPackage),
              referredClassPackage,
              qualifiedName, type.getRelativeName());
    }
  }

  class ImportsBuilder {

    boolean newLineComing;
    boolean hasImports;
    final StringBuilder result;

    ImportsBuilder(int capacity) {
      result = new StringBuilder(capacity);
    }

    public void newLine() {
      newLineComing = true;
    }

    public void appendSingle(String fqn) {
      checkNewLine();
      hasImports = true;
      result.append("uses ").append(fqn).append("\n");
    }

    public void appendWildcard(String packageName) {
      checkNewLine();
      hasImports = true;
      result.append("uses ").append(packageName).append(".*\n");
    }


    private void checkNewLine() {
      if (newLineComing) {
        result.append("\n");
        newLineComing = false;
      }
    }
  }

  public void processImports() {
    if (collector.imported.isEmpty()) {
      return;
    }

    Set<String> wildcardPackagesFromSettings = new HashSet<>();
    for (PackageEntry entry : settings.PACKAGES_TO_USE_IMPORT_ON_DEMAND.getEntries()) {
      wildcardPackagesFromSettings.add(entry.getPackageName());
    }

    ImportsBuilder importsBuilder = new ImportsBuilder(collector.entries.size() * 30);
    Set<String> wildcardImport = new HashSet<>();

    for (Object o :  usesMap.getTypeUses()) {
      String type = o.toString();
      if (type.endsWith(".")) {
        wildcardImport.add(type.substring(0, type.length() - 1));
      }
    }

    Set<String> useWilcard = new HashSet<>();
    for (ImportEntry impEntry : collector.entries) {
      if (impEntry instanceof ClassImportEntry) {
        String pkg = ((ClassImportEntry) impEntry).packageName;
        int packageCount = collector.packagesUsage.count(pkg);
        boolean wildcard =
                (! settings.USE_SINGLE_CLASS_IMPORTS
                || wildcardPackagesFromSettings.contains(pkg)
                || packageCount >= settings.CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND)
                && !interferencesWithImplicit(pkg);
        if (wildcard) {
          useWilcard.add(pkg);
        }
      }
    }

    for (ImportEntry impEntry : collector.entries) {
      if (!(impEntry instanceof ClassImportEntry)) {
        importsBuilder.newLine();
        continue;
      }

      ClassImportEntry entry = (ClassImportEntry) impEntry;

      String pkg = entry.packageName;

      boolean wildcard =
              useWilcard.contains(pkg)
              && !conflicting(pkg, entry.simpleName, useWilcard);

      if (wildcard) {
        if (!wildcardImport.add(pkg)) {
          continue;
        }
        importsBuilder.appendWildcard(pkg);
      } else {
        importsBuilder.appendSingle(entry.qualifiedName);
      }
    }
    if (importsBuilder.hasImports) {
      newImports = parseUsesList(importsBuilder.result.toString(), file);
    } else {
      newImports = null;
    }
  }

  private boolean interferencesWithImplicit(String wildPckg) {
    Set<String> implicitImport = new HashSet<>(this.implicitImport);
    Set<String> implicitSimpleNames = newHashSet(transform(implicitImport, FQN_TO_SIMPLE_NAME));
    for (PsiClass pc : filter(findClasses(wildPckg), scopePredicate)) {
      if (implicitImport.contains(pc.getQualifiedName())) {
        continue;
      }
      if (implicitSimpleNames.contains(pc.getName())) {
        return true;
      }
    }
    return false;
  }

  private boolean conflicting(String basePackage, String simpleName, Set<String> imported) {
    for (String pkg : imported) {
      if (pkg.equals(basePackage)) {
        continue;
      }
      if (contains(pkg, simpleName)) {
        return true;
      }
    }
    return false;
  }

  public static final Function<PsiClass, String> PSI_TO_SIMPLE_NAME = new Function<PsiClass, String>() {
    public String apply(@Nullable PsiClass psiClass) {
      return psiClass.getName();
    }
  };

  public static final Function<String, String> FQN_TO_SIMPLE_NAME = new Function<String, String>() {
    public String apply(String fqn) {
      return extractClassName(fqn);
    }
  };

  private Predicate<PsiClass> scopePredicate = new Predicate<PsiClass>() {
    public boolean apply(PsiClass psiClass) {
      SearchScope scope = psiClass.getUseScope();
      if (scope instanceof ModuleWithDependentsScope) {
        return ((ModuleWithDependentsScope) scope).contains(file.getVirtualFile());
      } else {
        return true;
      }
    }
  };

  private Iterable<PsiClass> findClasses(String fromPackage) {
    PsiPackage pckg = JavaPsiFacade.getInstance(file.getProject()).findPackage(fromPackage);
    if (pckg == null) {
      return Collections.emptyList();
    }  else {
      return newArrayList(pckg.getClasses());
    }
  }

  private boolean contains(String basePackage, String simpleName) {
    for (PsiClass pc : filter(findClasses(basePackage), scopePredicate)) {
      if (simpleName.equals(pc.getName())) {
        return true;
      }
    }
    return false;
  }

  private String getPackageName(@NotNull PsiClass cls) {
    return firstNonNull(extractPackageName(cls.getQualifiedName()), "");
  }

  @Nullable
  private PsiClass getOutermostClass(@NotNull PsiClass cls) {
    PsiClass candidate = null;
    PsiClass enclosingClass = cls.getContainingClass();
    while (enclosingClass != null) {
      candidate = enclosingClass;
      enclosingClass = enclosingClass.getContainingClass();
    }
    return candidate;
  }

  private int getPackageWeight(@NotNull String packageName) {

    String[] fqn = FqnCache.getParts(packageName);

    FqnCacheNode<Integer> root = packageWeights.getRoot();
    FqnCacheNode<Integer> lastFound = root;

    for (String part : fqn) {
      FqnCacheNode<Integer> segment = lastFound.getChild(part);
      if (segment != null) {
        lastFound = segment;
      } else {
        break;
      }
    }

    Integer weight = lastFound.getUserData();
    if (weight == null) {
      weight = defaultWeight;
    }
    return weight;
  }

  public Set<String> getRequiredImports() {
    return collector.imported;
  }
}

class EntriesComparator implements Comparator<ImportEntry> {

  @Override
  public int compare(ImportEntry o1, ImportEntry o2) {

    ComparisonChain chain = ComparisonChain.start().compare(o1.weight, o2.weight);

    boolean i1 = o1 instanceof ClassImportEntry;
    boolean i2 = o2 instanceof ClassImportEntry;

    if (i1 && i2) {
      ClassImportEntry c1 = (ClassImportEntry) o1;
      ClassImportEntry c2 = (ClassImportEntry) o2;
      chain = chain
        .compare(c1.packageName, c2.packageName)
        .compare(c1.qualifiedName, c2.qualifiedName);
    } else {
      chain = chain.compareFalseFirst(i2, i1);
    }
    return chain.result();
  }
}


class ImportEntry {
  final int weight;

  ImportEntry(int weight) {
    this.weight = weight;
  }
}

class ClassImportEntry extends  ImportEntry {

  final String packageName;
  final String qualifiedName;
  final String simpleName;

  public ClassImportEntry(int weight, String packageName, String qualifiedName, String simpleName) {
    super(weight);
    this.packageName = nullToEmpty(packageName);
    this.qualifiedName = nullToEmpty(qualifiedName);
    this.simpleName = nullToEmpty(simpleName);
  }
}

class Collector {
  final Set<ImportEntry> entries = new TreeSet<>(new EntriesComparator());
  final Multiset<String> packagesUsage = HashMultiset.create();
  final Set<String> imported = new HashSet<>();

  public void add(int weight, String packageName, String qualifiedName, String simpleName) {
    if (imported.add(qualifiedName)) {
      entries.add(new ClassImportEntry(weight, packageName, qualifiedName, simpleName));
      packagesUsage.add(packageName);
    }
  }
}
