/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.intentions.ImportClassHelper;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.util.Collections.sort;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.intellij.psi.util.ClassUtil.extractClassName;
import static com.intellij.psi.util.ClassUtil.extractPackageName;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;
import static gw.plugin.ij.completion.GosuClassNameInsertHandler.addImportForItem;
import static gw.plugin.ij.intentions.ImportClassHelper.ResolveTypeResult.CONFLICT;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.parseProgramm;

public class ClassLord {

  private static final Pattern PURGE_PATTERN = Pattern.compile("[\\(\\[<].*$");

  public static String purgeClassName(String className) {
    return PURGE_PATTERN.matcher(className).replaceFirst("").trim();
  }

  /**
   * Adds import for the type and for all generic declarations inside the type
   */
  public static void importAllTypes(PsiType rootType, PsiFile file) {
    Set<PsiType> types = new LinkedHashSet<>();
    collectAllTypes(rootType, types);
    for (PsiType type : types) {
      String qName = purgeClassName(type.getCanonicalText());
      if (!hasImplicitImport(qName)) {
        ImportClassHelper importHelper = new ImportClassHelper(qName, (IGosuFileBase) file);
        importHelper.resolveType();
        if (importHelper.resolveResult != CONFLICT) {
          addImportForItem(file, qName, extractClassName(qName));
        }
      }
    }
  }

  public static boolean doImport(String fqn, PsiFile file) {
    return addImportForItem(file, fqn, extractClassName(fqn));
  }

  public static void doImportAndStick(String fqn, PsiFile file) {
    if (doImport(fqn, file)) {
      stickImport(fqn, file);
    }
  }
  
	public static void stickImport(String className, PsiFile file) {

		IGosuUsesStatementList usesList = findChildOfType(file,
				IGosuUsesStatementList.class);

		if (usesList == null) {
			return;
		}

		List<String> imports = new ArrayList<>();
		Map<String, IGosuUsesStatement> nameToStatement = new HashMap<>();

		for (IGosuUsesStatement stmt : usesList.getUsesStatements()) {
			GosuTypeLiteralImpl typeLiteral = findChildOfType(stmt,
					GosuTypeLiteralImpl.class);
			if (typeLiteral == null) {
				continue;
			}
			String name = typeLiteral.getText();

			if (name == null) {
				continue;
			}
			nameToStatement.put(name, stmt);
			imports.add(name);
		}

		if (imports.size() < 2) {
			return;
		}

		IGosuUsesStatement targetImport = nameToStatement.get(className);
		if (targetImport == null) {
			return;
		}

		sort(imports);
		int idx = imports.indexOf(className);

		if (idx < 0) {
		  return;
		}
		
		final boolean BEFORE = true;
		final boolean AFTER = !BEFORE;

		String sibling;
		boolean addingType;

		int lastIdx = imports.size() - 1;
		if (idx == 0) {
			sibling = imports.get(1);
			addingType = BEFORE;
		} else if (idx == lastIdx) {
			sibling = imports.get(lastIdx - 1);
			addingType = AFTER;
		} else {
			String upper = imports.get(idx - 1);
			String lower = imports.get(idx + 1);
			int upperDistance = abs(className.compareTo(upper));
			int lowerDistance = abs(className.compareTo(lower));

			if (upperDistance < lowerDistance) {
				sibling = upper;
				addingType = AFTER;
			} else {
				sibling = lower;
				addingType = BEFORE;
			}
		}

		IGosuUsesStatement stmt = nameToStatement.get(sibling);
		PsiElement newTargetImport = targetImport.copy();
		targetImport.delete();

		if (BEFORE == addingType) {
			usesList.addBefore(newTargetImport, stmt);
		} else {
			usesList.addAfter(newTargetImport, stmt);
		}
	}
  
  /**
   * Collects all types which the type contains inside itself
   */
  public static void collectAllTypes(PsiType type, Set<PsiType> types) {
    types.add(type);
    if (type instanceof PsiClassType) {
      for (PsiType generic : ((PsiClassType) type).getParameters()) {
        collectAllTypes(generic, types);
      }
    }
  }

  public static interface TypeSubstitutor {
    String substitute(GosuTypeLiteralImpl typeLiteral);
  }

  /**
   * returns list of [Set, List, String] for string "Set<List<String>>"
   */
  public static List<String> flatten(String type, IGosuPsiElement context) {
    if (Strings.isBlank(type)) {
      return Collections.emptyList();
    }
    PsiElement expr = parseProgramm("var x : " + type, context.getManager(), null);
    List<GosuTypeLiteralImpl> types = new ArrayList<>(findChildrenOfType(expr, GosuTypeLiteralImpl.class));
    if (types.isEmpty()) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<>();
    for (GosuTypeLiteralImpl typeLiteral : types) {
      result.add(typeLiteral.getType().getCanonicalText());
    }
    return result;
  }

  public static String substitueTypes(String type, IGosuPsiElement context, TypeSubstitutor substitutor) {
    if (Strings.isBlank(type)) {
      return type;
    }
    PsiElement expr = parseProgramm("var x : " + type, context.getManager(), null);
    List<GosuTypeLiteralImpl> types = new ArrayList<>(findChildrenOfType(expr, GosuTypeLiteralImpl.class));

    if (types.isEmpty()) {
      return "";
    }

    GosuTypeLiteralImpl stubType = types.get(0);
    final int rootOffset = stubType.getTextOffset();
    String result = stubType.getText();
    int lastOffset = result.length();
    ListIterator<GosuTypeLiteralImpl> it = types.listIterator(types.size());
    while (it.hasPrevious()) {
      GosuTypeLiteralImpl typeLiteral = it.previous();
      String typeText = purgeClassName(typeLiteral.getText());
      int offset = typeLiteral.getTextOffset() - rootOffset;
      try {
        if (offset >= lastOffset) {
          continue;
        }
      } finally {
        lastOffset = offset;
      }
      String substitution = substitutor.substitute(typeLiteral);
      if (substitution != null &&
          checkRange(result, 0, offset) &&
          checkRange(result, offset + typeText.length(), result.length())) {
        result = result.substring(0, offset) + substitution + result.substring(offset + typeText.length(), result.length());
      }
    }
    return result;
  }

  public static boolean checkRange(String value, int beginIndex, int endIndex) {
    if (beginIndex < 0) {
      return false;
    }
    if (endIndex > value.length()) {
      return false;
    }
    int subLen = endIndex - beginIndex;
    if (subLen < 0) {
      return false;
    }
    return true;
  }

  public static String reduceCollections(String type, IGosuPsiElement context) {
    final String[] SIMPLE_INTERFACES = {"java.util.List", "java.util.Set", "java.util.Map"};
    return substitueTypes(type, context, new TypeSubstitutor() {
      public String substitute(GosuTypeLiteralImpl typeLiteral) {
        Set<String> inheritance = new HashSet<>();
        extractInhertance(typeLiteral.getType(), inheritance);
        for (String iface : SIMPLE_INTERFACES) {
          if (inheritance.contains(iface)) {
            return iface;
          }
        }
        return null;
      }
    });
  }

  public static String simplifyTypes(String type,
      final IGosuPsiElement context, final Map<String, String> simplified) {
    return simplifyTypes(type, context, false, simplified);
  }

  public static String simplifyTypes(String type, final IGosuPsiElement context, 
      final boolean analizeFQNUsing, final Map<String, String> simplified) {
    return substitueTypes(type, context, new TypeSubstitutor() {
      public String substitute(GosuTypeLiteralImpl typeLiteral) {
        String typeText = purgeClassName(typeLiteral.getText());
        int dotIdx = typeText.lastIndexOf('.');
        if (dotIdx == -1) {
          return null;
        }
        ImportClassHelper importHelper = new ImportClassHelper(typeText, context);
        importHelper.resolveType();
        if (importHelper.resolveResult == CONFLICT) {
          return null;
        }
        if (analizeFQNUsing) {
          importHelper.analizeFQNUsing();
          if (importHelper.fqnIsPreferred()) {
            return null;
          }
        }
        String simpleName = extractClassName(typeText);
        String existing = simplified.get(simpleName);
        if (existing != null && !existing.equals(typeText)) {
          return null;
        }
        simplified.put(simpleName, typeText);

        return simpleName;
      }
    });
  }

  private static void extractInhertance(PsiType type, Set<String> inheritance) {
    inheritance.add(purgeClassName(type.getCanonicalText()));
    for (PsiType t : type.getSuperTypes()) {
      extractInhertance(t, inheritance);
    }
  }

  private static final Set<String> GOSU_IMPLICIT_IMPORTS = ImmutableSet.of(
      "java.lang.String", "java.lang.Boolean", "java.lang.List", "java.lang.Number", "java.lang.Object");

  public static boolean hasImplicitImport(String fqn) {
    return hasImplicitImport(fqn, TypeSystem.getDefaultTypeUsesMap());
  }

  public static boolean hasImplicitImport(String fqn, ITypeUsesMap usesMap) {
    String namespace = extractPackageName(fqn);
    if (isNullOrEmpty(namespace)) {
      return true;
    }
    if ("gw.lang".equals(namespace)) {
      return true;
    }
    if (GOSU_IMPLICIT_IMPORTS.contains(fqn)) {
      return true;
    }

    IType resolved = usesMap.resolveType(fqn);
    if (resolved == null) {
      return false;
    }
    return usesMap.resolveType(resolved.getRelativeName()) == resolved;
  }

  public static boolean inUsesMap(String namespace, ITypeUsesMap usesMap) {
    return usesMap.containsType(namespace + ".");
  }


  public static boolean resolveRelativeTypeInParser(String strQName, String strRelativeName, @NotNull IGosuClass gosuClass) {
    IGosuParser parser = gosuClass.getParser();
    if (parser != null) {
      TypeSystem.pushModule(gosuClass.getTypeLoader().getModule());
      try {
        IType type = parser.resolveTypeLiteral(strRelativeName).getType().getType();
        if (!(type instanceof IErrorType) && strQName.equals(type.getName())) {
          return true;
        }
      } finally {
        TypeSystem.popModule(gosuClass.getTypeLoader().getModule());
      }
    }
    return false;
  }

}
