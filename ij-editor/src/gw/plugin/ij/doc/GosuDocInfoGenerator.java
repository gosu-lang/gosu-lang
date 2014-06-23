/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.doc;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.ExternalAnnotationsManager;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.javadoc.ColorUtil;
import com.intellij.codeInsight.javadoc.JavaDocInfoGenerator;
import com.intellij.codeInsight.javadoc.JavaDocUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LangBundle;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDisjunctionType;
import com.intellij.psi.PsiDocCommentOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiEllipsisType;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterListOwner;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.PsiWildcardType;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.javadoc.PsiInlineDocTag;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnhancementDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.lang.psi.custom.GosuXMLField;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuAnnotationExpressionImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldPropertyImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuTypeDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copied from JavaDocInfoGenerator.
 */
public class GosuDocInfoGenerator {
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.javadoc.JavaDocInfoGenerator");

  private static final @NonNls
  Pattern ourNotDot = Pattern.compile("[^.]");
  private static final @NonNls Pattern ourWhitespaces = Pattern.compile("[ \\n\\r\\t]+");
  private static final @NonNls
  Matcher ourNotDotMatcher = ourNotDot.matcher("");
  private static final @NonNls Matcher ourWhitespacesMatcher = ourWhitespaces.matcher("");
  public static final String FUNCTION = "function ";

  private final Project myProject;
  private final PsiElement myElement;
  private static final @NonNls String THROWS_KEYWORD = "throws";
  private static final @NonNls String BR_TAG = "<br>";
  private static final @NonNls String LINK_TAG = "link";
  private static final @NonNls String LITERAL_TAG = "literal";
  private static final @NonNls String CODE_TAG = "code";
  private static final @NonNls String LINKPLAIN_TAG = "linkplain";
  private static final @NonNls String INHERITDOC_TAG = "inheritDoc";
  private static final @NonNls String DOCROOT_TAG = "docRoot";
  private static final @NonNls String VALUE_TAG = "value";
  private static final String PROPERTY_SET = "property set ";
  private static final String PROPERTY_GET = "property get ";

  interface InheritDocProvider <T> {
    @Nullable
    Pair<T, InheritDocProvider<T>> getInheritDoc();

    @Nullable
    PsiClass getElement();
  }

  @Nullable
  private static final InheritDocProvider<PsiDocTag> ourEmptyProvider = new InheritDocProvider<PsiDocTag>() {
    @Nullable
    public Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> getInheritDoc() {
      return null;
    }

    @Nullable
    public PsiClass getElement() {
      return null;
    }
  };

  @Nullable
  private static final InheritDocProvider<PsiElement[]> ourEmptyElementsProvider = mapProvider(ourEmptyProvider, false);

  @Nullable
  private static InheritDocProvider<PsiElement[]> mapProvider(@NotNull final InheritDocProvider<PsiDocTag> i,
                                                              final boolean dropFirst) {
    return new InheritDocProvider<PsiElement[]>() {
      @Nullable
      public Pair<PsiElement[], InheritDocProvider<PsiElement[]>> getInheritDoc() {
        Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> pair = i.getInheritDoc();

        if (pair == null) {
          return null;
        }

        PsiElement[] elements;
        PsiElement[] rawElements = pair.first.getDataElements();

        if (dropFirst && rawElements != null && rawElements.length > 0) {
          elements = new PsiElement[rawElements.length - 1];

          System.arraycopy(rawElements, 1, elements, 0, elements.length);
        }
        else {
          elements = rawElements;
        }

        return new Pair<>(elements, mapProvider(pair.second, dropFirst));
      }

      @Nullable
      public PsiClass getElement() {
        return i.getElement();
      }
    };
  }

  interface DocTagLocator <T> {
    @Nullable
    T find(PsiDocComment comment);
  }

  @Nullable
  private static DocTagLocator<PsiDocTag> parameterLocator(final String name) {
    return new DocTagLocator<PsiDocTag>() {
      @Nullable
      public PsiDocTag find(@Nullable PsiDocComment comment) {
        if (comment == null) {
          return null;
        }

        PsiDocTag[] tags = comment.findTagsByName("param");

        for (PsiDocTag tag : tags) {
          PsiDocTagValue value = tag.getValueElement();

          if (value != null) {
            String text = value.getText();

            if (text != null && text.equals(name)) {
              return tag;
            }
          }
        }

        return null;
      }
    };
  }

  @Nullable
  private static DocTagLocator<PsiDocTag> exceptionLocator(@NotNull final String name) {
    return new DocTagLocator<PsiDocTag>() {
      @Nullable
      public PsiDocTag find(@Nullable PsiDocComment comment) {
        if (comment == null) {
          return null;
        }

        PsiDocTag[] tags = getThrowsTags(comment);

        for (PsiDocTag tag : tags) {
          PsiDocTagValue value = tag.getValueElement();

          if (value != null) {
            String text = value.getText();

            if (text != null && areWeakEqual(text, name)) {
              return tag;
            }
          }
        }

        return null;
      }
    };
  }

  public GosuDocInfoGenerator(Project project, PsiElement element) {
    myProject = project;
    myElement = element;
  }

  @Nullable
  public String generateFileInfo() {
    StringBuilder buffer = new StringBuilder();
    if (myElement instanceof PsiFile) {
      generateFileJavaDoc(buffer, (PsiFile) myElement); //used for Ctrl-Click
    }

    return fixupDoc(buffer);
  }

  @Nullable
  private static String fixupDoc(@NotNull final StringBuilder buffer) {
    String text = buffer.toString();
    if (text.length() == 0) {
      return null;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Generated JavaDoc:");
      LOG.debug(text);
    }

    return StringUtil.replace(text, "/>", ">");
  }

  @Nullable
  public String generateDocInfo(@Nullable List<String> docURLs) {
    StringBuilder buffer = new StringBuilder();
    if (myElement instanceof PsiClass) {
      generateClassJavaDoc(buffer, (PsiClass) myElement);
    }
    else if (myElement instanceof GosuMethodImpl) {
      generateMethodJavaDoc(buffer, (GosuMethodImpl)myElement);
    } else if (myElement instanceof PsiParameter) {
      generateMethodParameterJavaDoc(buffer, (PsiParameter)myElement);
    }
    else if (myElement instanceof PsiField) {
      generateFieldJavaDoc(buffer, (PsiField)myElement);
    }
    else if (myElement instanceof PsiVariable) {
      generateVariableJavaDoc(buffer, (PsiVariable)myElement);
    }
    else if (myElement instanceof PsiDirectory) {
      final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory)myElement);
      if (aPackage == null) return null;
      generatePackageJavaDoc(buffer, aPackage);
    }
    else if (myElement instanceof PsiPackage) {
      generatePackageJavaDoc(buffer, (PsiPackage) myElement);
    } else {
      return null;
    }
    if (docURLs != null) {
      final StringBuilder sb = new StringBuilder("<p id=\"error\">Following external urls were checked:<br>&nbsp;&nbsp;&nbsp;<i>");
      sb.append(StringUtil.join(docURLs, "</i><br>&nbsp;&nbsp;&nbsp;<i>"));
      sb.append("</i><br>The documentation for this element is not found. Please add all the needed paths to API docs in ");
      sb.append("<a href=\"open://Project Settings\">Project Settings.</a></p>");
      buffer.insert(buffer.indexOf("<body>"), sb.toString());
    }
    return fixupDoc(buffer);
  }

  private void generateClassJavaDoc(@NotNull @NonNls StringBuilder buffer, @NotNull PsiClass aClass) {
    if (aClass instanceof PsiAnonymousClass) return;
    PsiManager manager = aClass.getManager();
    generatePrologue(buffer);

    PsiFile file = aClass.getContainingFile();
    if (file instanceof AbstractGosuClassFileImpl) {
      String packageName = ((AbstractGosuClassFileImpl)file).getPackageName();
      if (packageName.length() > 0) {
        buffer.append("<small><b>");
        buffer.append(packageName);
        buffer.append("</b></small>");
        //buffer.append("<br>");
      }
    }

    buffer.append("<PRE>");
    generateAnnotations(buffer, aClass);
    String modifiers = PsiFormatUtil.formatModifiers(aClass, PsiFormatUtil.JAVADOC_MODIFIERS_ONLY);
    if (modifiers.length() > 0) {
      buffer.append(modifiers);
      buffer.append(" ");
    }
    buffer.append(getClassType(aClass));
    buffer.append(" ");
    String refText = JavaDocUtil.getReferenceText(myProject, aClass);
    if (refText == null) {
      buffer.setLength(0);
      return;
    }
    String labelText = JavaDocUtil.getLabelText(myProject, manager, refText, aClass);
    buffer.append("<b>");
    buffer.append(labelText);
    buffer.append("</b>");

    buffer.append(generateTypeParameters(aClass));

    if (isEnhancement(aClass)) {
      buffer.append(" : ");
      PsiElement enhancedType = ((IGosuEnhancementDefinition) aClass).getEnhancedType();
      generateLink(buffer, getQualifiedName(enhancedType), null, aClass, false);
      buffer.append("\n");
    }

    buffer.append("\n");

    PsiClassType[] refs = aClass.getExtendsListTypes();
    String qName = aClass.getQualifiedName();
    if (!isEnhancement(aClass) && !(aClass instanceof GosuSyntheticClassDefinitionImpl) &&
        (refs.length > 0 || !aClass.isInterface() && (qName == null || !qName.equals("java.lang.Object")))) {
      buffer.append("extends ");
      if (refs.length == 0) {
        generateLink(buffer, "java.lang.Object", null, aClass, false);
      }
      else {
        for (int i = 0; i < refs.length; i++) {
          generateType(buffer, refs[i], aClass);
          if (i < refs.length - 1) {
            buffer.append(",&nbsp;");
          }
        }
      }
      buffer.append("\n");
    }

    refs = aClass.getImplementsListTypes();
    if (refs.length > 0) {
      boolean firstInterface = true;
      for (int i = 0; i < refs.length; i++) {
        if (!refs[i].getCanonicalText().equals("gosu.lang.GosuObject")) {
          if (firstInterface) {
            buffer.append("implements ");
            firstInterface = false;
          }
          generateType(buffer, refs[i], aClass);
          if (i < refs.length - 1) {
            buffer.append(",&nbsp;");
          }
        }
      }
      buffer.append("\n");
    }
    if (buffer.charAt(buffer.length() - 1) == '\n') {
      buffer.setLength(buffer.length() - 1);
    }
    buffer.append("</PRE>");
    //buffer.append("<br>");

    PsiDocComment comment = getDocComment(aClass);
    if (comment != null) {
      generateCommonSection(buffer, comment);
      generateTypeParametersSection(buffer, aClass);
    }
    generateEpilogue(buffer);
  }

  private boolean isEnhancement(PsiClass aClass) {
    return aClass instanceof GosuTypeDefinitionImpl && ((IGosuTypeDefinition)aClass).isEnhancement();
  }

  @Nullable
  private String getQualifiedName(@NotNull PsiElement type) {
    if (type instanceof PsiClass) {
      return ((PsiClass) type).getQualifiedName();
    } else {
      return type.toString();
    }
  }

  private String getClassType(@NotNull PsiClass aClass) {
    if (aClass.isInterface()) {
      return LangBundle.message("java.terms.interface");
    } else if (isEnhancement(aClass)) {
      return "enhancement";
    } else if (aClass instanceof GosuSyntheticClassDefinitionImpl) {
      return "template";
    } else {
      return LangBundle.message("java.terms.class");
    }
  }

  private void generateTypeParametersSection(@NotNull final StringBuilder buffer, @NotNull final PsiClass aClass) {
    final PsiDocComment docComment = aClass.getDocComment();
    if (docComment == null) return;
    final LinkedList<Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>> result =
      new LinkedList<>();
    final PsiTypeParameter[] typeParameters = aClass.getTypeParameters();
    for (PsiTypeParameter typeParameter : typeParameters) {
      final DocTagLocator<PsiDocTag> locator = parameterLocator("<" + typeParameter.getName() + ">");
      final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> inClassComment = findInClassComment(aClass, locator);
      if (inClassComment != null) {
        result.add(inClassComment);
      }
      else {
        final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> pair = findInHierarchy(aClass, locator);
        if (pair != null) {
          result.add(pair);
        }
      }
    }
    generateTypeParametersSection(buffer, result);
  }

  @Nullable
  private static Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> findInHierarchy(@NotNull PsiClass psiClass, @NotNull final DocTagLocator<PsiDocTag> locator) {
    for (final PsiClass superClass : psiClass.getSupers()) {
      final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> pair = findInClassComment(superClass, locator);
      if (pair != null) return pair;
    }
    for (PsiClass superInterface : psiClass.getInterfaces()) {
      final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> pair = findInClassComment(superInterface, locator);
      if (pair != null) return pair;
    }
    return null;
  }

  @Nullable
  private static Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> findInClassComment(@NotNull final PsiClass psiClass, @NotNull final DocTagLocator<PsiDocTag> locator) {
    final PsiDocTag tag = locator.find(getDocComment(psiClass));
    if (tag != null) {
      return new Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>(tag, new InheritDocProvider<PsiDocTag>() {
        @Nullable
        public Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> getInheritDoc() {
          return findInHierarchy(psiClass, locator);
        }

        @NotNull
        public PsiClass getElement() {
          return psiClass;
        }
      });
    }
    return null;
  }

  @Nullable
  private static PsiDocComment getDocComment(@NotNull PsiDocCommentOwner docOwner) {
    if (docOwner instanceof CustomGosuClass || docOwner instanceof GosuXMLField) {
      return docOwner.getDocComment();
    }
    if (docOwner instanceof GosuFieldPropertyImpl && docOwner.getParent() instanceof GosuFieldImpl) {
      docOwner = (PsiDocCommentOwner) docOwner.getParent();
    }
    PsiDocComment comment = ((PsiDocCommentOwner)docOwner.getNavigationElement()).getDocComment();
//    if (comment == null) { //check for non-normalized fields
//      final PsiModifierList modifierList = docOwner.getModifierList();
//      if (modifierList != null) {
//        final PsiElement parent = modifierList.getParent();
//        if (parent instanceof PsiDocCommentOwner) {
//          return ((PsiDocCommentOwner)parent.getNavigationElement()).getDocComment();
//        }
//      }
//    }
    return comment;
  }

  private void generateFieldJavaDoc(@NotNull @NonNls StringBuilder buffer, @NotNull PsiField field) {
    generatePrologue(buffer);

    PsiClass parentClass = field.getContainingClass();
    if (parentClass != null) {
      String qName = parentClass.getQualifiedName();
      if (qName != null) {
        buffer.append("<small><b>");
        //buffer.append(qName);
        generateLink(buffer, qName, qName, field, false);
        buffer.append("</b></small>");
        //buffer.append("<br>");
      }
    }

    buffer.append("<PRE>");
    generateAnnotations(buffer, field);
    String modifiers = PsiFormatUtil.formatModifiers(field, PsiFormatUtil.JAVADOC_MODIFIERS_ONLY);
    if (modifiers.length() > 0) {
      buffer.append(modifiers);
      buffer.append(" ");
    }

    buffer.append("var ");

    // name
    buffer.append("<b>");
    buffer.append(field.getName());
    appendInitializer(buffer, field);
    buffer.append("</b>");

    // type
    buffer.append(": ");
    generateType(buffer, field.getType(), field);
    buffer.append(" ");
    buffer.append("</PRE>");

    ColorUtil.appendColorPreview(field, buffer);

    PsiDocComment comment = getDocComment(field);
    if (comment != null) {
      generateCommonSection(buffer, comment);
    }

    generateEpilogue(buffer);
  }

  // not a javadoc in fact..
  private void generateVariableJavaDoc(@NotNull @NonNls StringBuilder buffer, @NotNull PsiVariable variable) {
    generatePrologue(buffer);

    buffer.append("<PRE>");
    String modifiers = PsiFormatUtil.formatModifiers(variable, PsiFormatUtil.JAVADOC_MODIFIERS_ONLY);
    if (modifiers.length() > 0) {
      buffer.append(modifiers);
      buffer.append(" ");
    }
    generateType(buffer, variable.getType(), variable);
    buffer.append(" ");
    buffer.append("<b>");
    buffer.append(variable.getName());
    appendInitializer(buffer, variable);
    buffer.append("</b>");
    buffer.append("</PRE>");
    //buffer.append("<br>");

    ColorUtil.appendColorPreview(variable, buffer);

    generateEpilogue(buffer);
  }

  // not a javadoc in fact..
  private static void generateFileJavaDoc(@NotNull StringBuilder buffer, @NotNull PsiFile file) {
    generatePrologue(buffer);
    final VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile != null) {
      buffer.append(virtualFile.getPresentableUrl());
    }
    generateEpilogue(buffer);
  }

  private void generatePackageJavaDoc(@NotNull final StringBuilder buffer, @NotNull final PsiPackage psiPackage) {
    for(PsiDirectory directory: psiPackage.getDirectories()) {
      final PsiFile packageInfoFile = directory.findFile("package-info.java");
      if (packageInfoFile != null) {
        final ASTNode node = packageInfoFile.getNode();
        if (node != null) {
          final ASTNode docCommentNode = node.findChildByType(JavaDocElementType.DOC_COMMENT);
          if (docCommentNode != null) {
            final PsiDocComment docComment = (PsiDocComment)docCommentNode.getPsi();

            generatePrologue(buffer);

            generateCommonSection(buffer, docComment);

            generateEpilogue(buffer);
            break;
          }
        }
      }
      PsiFile packageHtmlFile = directory.findFile("package.html");
      if (packageHtmlFile != null) {
        generatePackageHtmlJavaDoc(buffer, packageHtmlFile);
        break;
      }
    }
  }

  private void generateCommonSection(@NotNull StringBuilder buffer, @NotNull PsiDocComment docComment) {
    generateDescription(buffer, docComment);
    generateDeprecatedSection(buffer, docComment);
    generateSinceSection(buffer, docComment);
    generateSeeAlsoSection(buffer, docComment);
  }

  private void generatePackageHtmlJavaDoc(@NotNull final StringBuilder buffer, @NotNull final PsiFile packageHtmlFile) {
    String htmlText;
    XmlFile packageXmlFile = (XmlFile) packageHtmlFile;
    final XmlTag rootTag = packageXmlFile.getDocument().getRootTag();
    if (rootTag != null) {
      final XmlTag subTag = rootTag.findFirstSubTag("body");
      if (subTag != null) {
        htmlText = subTag.getValue().getText();
      }
      else {
        htmlText = packageHtmlFile.getText();
      }
    }
    else {
      htmlText = packageHtmlFile.getText();
    }
    htmlText = StringUtil.replace(htmlText, "*/", "&#42;&#47;");

    final String fileText = "/** " + htmlText + " */";
    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(packageHtmlFile.getProject()).getElementFactory();
    final PsiDocComment docComment;
    try {
      docComment = elementFactory.createDocCommentFromText(fileText);
    }
    catch (IncorrectOperationException e) {
      LOG.error(e);
      return;
    }

    generatePrologue(buffer);

    generateCommonSection(buffer, docComment);

    generateEpilogue(buffer);
  }

  private static void appendInitializer(@NotNull StringBuilder buffer, @NotNull PsiVariable variable) {
    PsiExpression initializer = variable.getInitializer();
    if (initializer != null) {
      String text = initializer.getText();
      text = text.trim();
      int index1 = text.indexOf('\n');
      if (index1 < 0) index1 = text.length();
      int index2 = text.indexOf('\r');
      if (index2 < 0) index2 = text.length();
      int index = Math.min(index1, index2);
      boolean trunc = index < text.length();
      text = text.substring(0, index);
      buffer.append(" = ");
      text = StringUtil.replace(text, "<", "&lt;");
      text = StringUtil.replace(text, ">", "&gt;");
      buffer.append(text);
      if (trunc) {
        buffer.append("...");
      }
    }
  }

  private void generateAnnotations (@NotNull @NonNls StringBuilder buffer, @NotNull PsiModifierListOwner owner) {
    final PsiModifierList ownerModifierList = owner.getModifierList();
    if (ownerModifierList == null) return;
    PsiAnnotation[] annotations = ownerModifierList.getAnnotations();
    final PsiAnnotation[] externalAnnotations = ExternalAnnotationsManager.getInstance(owner.getProject()).findExternalAnnotations(owner);
    if (externalAnnotations != null) {
      annotations = ArrayUtil.mergeArrays(annotations, externalAnnotations, PsiAnnotation.ARRAY_FACTORY);
    }
    PsiManager manager = owner.getManager();

    for (PsiAnnotation annotation : annotations) {
//      final PsiJavaCodeReferenceElement nameReferenceElement = annotation.getNameReferenceElement();
//      if (nameReferenceElement == null) continue;
//      final PsiElement resolved = nameReferenceElement.resolve();
      PsiClass annotationType = ((GosuAnnotationExpressionImpl) annotation).resolve();
      if (annotationType!=null && AnnotationUtil.isAnnotated(annotationType, "java.lang.annotation.Documented", false)) {
        final PsiClassType type = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory().createType(annotationType, PsiSubstitutor.EMPTY);
        buffer.append("@");
        generateType(buffer, type, owner);
        final PsiNameValuePair[] attributes = annotation.getParameterList().getAttributes();
        if (attributes.length > 0) {
          boolean first = true;
          buffer.append("(");
          for (PsiNameValuePair pair : attributes) {
            if (!first) buffer.append("&nbsp;");
            first = false;
            final String name = pair.getName();
            if (name != null) {
              buffer.append(name);
              buffer.append(" = ");
            }
            final PsiAnnotationMemberValue value = pair.getValue();
            if (value != null) {
              buffer.append(value.getText());
            }
          }
          buffer.append(")");
        }
        buffer.append("&nbsp;");
      }
    }
  }

  private void generateMethodParameterJavaDoc(@NotNull @NonNls StringBuilder buffer, @NotNull PsiParameter parameter) {
    generatePrologue(buffer);

    buffer.append("<PRE>");
    String modifiers = PsiFormatUtil.formatModifiers(parameter, PsiFormatUtil.JAVADOC_MODIFIERS_ONLY);
    if (modifiers.length() > 0) {
      buffer.append(modifiers);
      buffer.append(" ");
    }
    generateAnnotations(buffer, parameter);
    buffer.append("<b>");
    buffer.append(parameter.getName());
    buffer.append("</b>");
    buffer.append(": ");
    generateType(buffer, parameter.getType(), parameter);
    appendInitializer(buffer, parameter);
    buffer.append("</PRE>");

    final PsiMethod method = PsiTreeUtil.getParentOfType(parameter, PsiMethod.class);

    if (method != null) {
      final PsiDocComment docComment = getDocComment(method);
      if (docComment != null) {
        final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> tagInfoProvider =
          findDocTag(docComment.getTags(), parameter.getName(), method);

        if (tagInfoProvider != null) {
          PsiElement[] elements = tagInfoProvider.first.getDataElements();
          if (elements.length != 0) generateOneParameter(elements, buffer, tagInfoProvider);
        }
      }
    }

    generateEpilogue(buffer);
  }

  private void generateMethodJavaDoc(@NotNull @NonNls StringBuilder buffer, @NotNull GosuMethodImpl method) {
    generatePrologue(buffer);

    PsiClass parentClass = method.getContainingClass();
    if (parentClass != null) {
      String qName = parentClass.getQualifiedName();
      if (qName != null) {
        buffer.append("<small><b>");
        generateLink(buffer, qName, qName, method, false);
        //buffer.append(qName);
        buffer.append("</b></small>");
        //buffer.append("<br>");
      }
    }

    buffer.append("<PRE>");
    int indent = 0;
    generateAnnotations(buffer, method);
    String modifiers = PsiFormatUtil.formatModifiers(method, PsiFormatUtil.JAVADOC_MODIFIERS_ONLY);
    if (modifiers.length() > 0) {
      buffer.append(modifiers);
      buffer.append("&nbsp;");
      indent += modifiers.length() + 1;
    }

    // type params
    final String typeParamsString = generateTypeParameters(method);
    indent += typeParamsString.length();
    if (typeParamsString.length() > 0) {
      buffer.append(typeParamsString);
      buffer.append("&nbsp;");
      indent++;
    }

    // name
    String name = method.getName();
    if (method.isForPropertySetter()) {
      buffer.append(PROPERTY_SET);
      indent += PROPERTY_SET.length() + name.length();
    } else if (method.isForPropertyGetter()) {
      buffer.append(PROPERTY_GET);
      indent += PROPERTY_GET.length() + name.length();
    } else if (method.isConstructor()) {
      name = "construct";
    } else {
      buffer.append(FUNCTION);
      indent += FUNCTION.length() + name.length();
    }
    buffer.append("<b>");
    buffer.append(name);
    buffer.append("</b>");

    buffer.append("(");

    // params
    PsiParameter[] parms = method.getParameterList().getParameters();
    for (int i = 0; i < parms.length; i++) {
      PsiParameter parm = parms[i];
      generateAnnotations(buffer, parm);
//      buffer.append("&nbsp;");
      if (parm.getName() != null) {
        buffer.append(parm.getName());
        buffer.append(": ");
      }
      generateType(buffer, parm.getType(), method);

      if (i < parms.length - 1) {
        buffer.append(",\n ");
        for (int j = 0; j < indent; j++) {
          buffer.append(" ");
        }
      }
    }
    buffer.append(")");

    //return
    PsiType returnType = method.getReturnType();
    if (returnType != null && !returnType.getCanonicalText().equals("void")) {
      buffer.append(": ");
      indent += generateType(buffer, returnType, method);
      buffer.append("&nbsp;");
      indent++;
    }

    PsiClassType[] refs = method.getThrowsList().getReferencedTypes();
    if (refs.length > 0) {
      buffer.append("\n");
      indent -= THROWS_KEYWORD.length() + 1;
      for (int i = 0; i < indent; i++) {
        buffer.append(" ");
      }
      indent += THROWS_KEYWORD.length() + 1;
      buffer.append(THROWS_KEYWORD);
      buffer.append("&nbsp;");
      for (int i = 0; i < refs.length; i++) {
        generateLink(buffer, refs[i].getCanonicalText(), null, method, false);
        if (i < refs.length - 1) {
          buffer.append(",\n");
          for (int j = 0; j < indent; j++) {
            buffer.append(" ");
          }
        }
      }
    }

    buffer.append("</PRE>");
    //buffer.append("<br>");

    PsiDocComment comment = getMethodDocComment(method);

    generateMethodDescription(buffer, method, comment);

    generateSuperMethodsSection(buffer, method, false);
    generateSuperMethodsSection(buffer, method, true);

    if (comment != null) {
      generateDeprecatedSection(buffer, comment);
    }

    generateParametersSection(buffer, method, comment);
    generateTypeParametersSection(buffer, method);
    generateReturnsSection(buffer, method, comment);
    generateThrowsSection(buffer, method, comment);

    if (comment != null) {
      generateSinceSection(buffer, comment);
      generateSeeAlsoSection(buffer, comment);
    }

    generateEpilogue(buffer);
  }

  @Nullable
  @SuppressWarnings({"HardCodedStringLiteral"})
  private PsiDocComment getMethodDocComment(@NotNull final PsiMethod method) {
    final PsiClass parentClass = method.getContainingClass();
    if (parentClass != null && parentClass.isEnum()) {
      if (method.getName().equals("values") && method.getParameterList().getParametersCount() == 0) {
        return loadSyntheticDocComment(method, "/javadoc/EnumValues.java.template");
      }
      if (method.getName().equals("valueOf") && method.getParameterList().getParametersCount() == 1) {
        return loadSyntheticDocComment(method, "/javadoc/EnumValueOf.java.template");
      }
    }
    return getDocComment(method);
  }

  @Nullable
  private PsiDocComment loadSyntheticDocComment(@NotNull final PsiMethod method, final String resourceName) {
    final InputStream commentStream = JavaDocInfoGenerator.class.getResourceAsStream(resourceName);
    if (commentStream == null) {
      return null;
    }

    final StringBuilder buffer;
    try {
      buffer = new StringBuilder();
      try {
        for (int ch; (ch = commentStream.read()) != -1;) {
          buffer.append((char)ch);
        }
      }
      catch (IOException e) {
        return null;
      }
    }
    finally {
      try {
        commentStream.close();
      }
      catch (IOException e) {
        LOG.error(e);
      }
    }

    String s = buffer.toString();
    s = StringUtil.replace(s, "<ClassName>", method.getContainingClass().getName());
    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(myProject).getElementFactory();
    try {
      return elementFactory.createDocCommentFromText(s);
    }
    catch (IncorrectOperationException e) {
      return null;
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private static void generatePrologue(@NotNull StringBuilder buffer) {
    buffer.append("<html><head>" +
                  "    <style type=\"text/css\">" +
                  "        #error {" +
                  "            background-color: #eeeeee;" +
                  "            margin-bottom: 10px;" +
                  "        }" +
                  "        p {" +
                  "            margin: 5px 0;" +
                  "        }" +
                  "    </style>" +
                  "</head><body>");
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private static void generateEpilogue(@NotNull StringBuilder buffer) {
    while (true) {
      if (buffer.length() < BR_TAG.length()) break;
      char c = buffer.charAt(buffer.length() - 1);
      if (c == '\n' || c == '\r' || c == ' ' || c == '\t') {
        buffer.setLength(buffer.length() - 1);
        continue;
      }
      String tail = buffer.substring(buffer.length() - BR_TAG.length());
      if (tail.equalsIgnoreCase(BR_TAG)) {
        buffer.setLength(buffer.length() - BR_TAG.length());
        continue;
      }
      break;
    }
    buffer.append("</body></html>");
  }

  private void generateDescription(@NotNull StringBuilder buffer, @NotNull PsiDocComment comment) {
    PsiElement[] elements = comment.getDescriptionElements();
    generateValue(buffer, elements, ourEmptyElementsProvider);
  }

  private static boolean isEmptyDescription(@NotNull PsiDocComment comment) {
    PsiElement[] descriptionElements = comment.getDescriptionElements();

    for (PsiElement description : descriptionElements) {
      String text = description.getText();

      if (text != null) {
        if (ourWhitespacesMatcher.reset(text).replaceAll("").length() != 0) {
          return false;
        }
      }
    }

    return true;
  }

  private void generateMethodDescription(@NotNull @NonNls StringBuilder buffer, @NotNull final PsiMethod method, @Nullable final PsiDocComment comment) {
    final DocTagLocator<PsiElement[]> descriptionLocator = new DocTagLocator<PsiElement[]>() {
      @Nullable
      public PsiElement[] find(@Nullable PsiDocComment comment) {
        if (comment == null) {
          return null;
        }

        if (isEmptyDescription(comment)) {
          return null;
        }

        return comment.getDescriptionElements();
      }
    };

    if (comment != null) {
      if (!isEmptyDescription(comment)) {
        generateValue
          (buffer, comment.getDescriptionElements(),
           new InheritDocProvider<PsiElement[]>() {
             @Nullable
             public Pair<PsiElement[], InheritDocProvider<PsiElement[]>> getInheritDoc() {
               return findInheritDocTag(method, descriptionLocator);
             }

             @Nullable
             public PsiClass getElement() {
               return method.getContainingClass();
             }
           });
        return;
      }
    }

    Pair<PsiElement[], InheritDocProvider<PsiElement[]>> pair = findInheritDocTag(method, descriptionLocator);

    if (pair != null) {
      PsiElement[] elements = pair.first;
      PsiClass extendee = pair.second.getElement();

      if (elements != null) {
        buffer.append("<DD><DL>");
        buffer.append("<DT><b>");
        buffer.append(extendee.isInterface() ?
                      CodeInsightBundle.message("javadoc.description.copied.from.interface") :
                      CodeInsightBundle .message("javadoc.description.copied.from.class"));
        buffer.append("</b>&nbsp;");
        generateLink(buffer, extendee, JavaDocUtil.getShortestClassName(extendee, method));
        buffer.append(BR_TAG);
        generateValue(buffer, elements, pair.second);
        buffer.append("</DD></DL></DD>");
      }
    }
  }

  private void generateValue(@NotNull StringBuilder buffer, @NotNull PsiElement[] elements, @NotNull InheritDocProvider<PsiElement[]> provider) {
    generateValue(buffer, elements, 0, provider);
  }

  @NotNull
  private String getDocRoot() {
    PsiClass aClass = null;

    if (myElement instanceof PsiClass) {
      aClass = (PsiClass)myElement;
    }
    else if (myElement instanceof PsiMember) {
      aClass = ((PsiMember)myElement).getContainingClass();
    }
    else {
      LOG.error("Class or member expected but found " + myElement.getClass().getName());
    }

    String qName = aClass.getQualifiedName();

    if (qName == null) {
      return "";
    }

    return "../" + ourNotDotMatcher.reset(qName).replaceAll("").replaceAll(".", "../");
  }

  private void generateValue(@NotNull StringBuilder buffer,
                             @NotNull PsiElement[] elements,
                             int startIndex,
                             @NotNull InheritDocProvider<PsiElement[]> provider) {
    int predictOffset =
      startIndex < elements.length ?
      elements[startIndex].getTextOffset() + elements[startIndex].getText().length() :
      0;

    for (int i = startIndex; i < elements.length; i++) {
      if (elements[i].getTextOffset() > predictOffset) buffer.append(" ");
      predictOffset = elements[i].getTextOffset() + elements[i].getText().length();
      PsiElement element = elements[i];
      if (element instanceof PsiInlineDocTag) {
        PsiInlineDocTag tag = (PsiInlineDocTag)element;
        final String tagName = tag.getName();
        if (tagName == null) {
          buffer.append(element.getText());
        }
        else if (tagName.equals(LINK_TAG)) {
          generateLinkValue(tag, buffer, false);
        }
        else if (tagName.equals(LITERAL_TAG)) {
          generateLiteralValue(tag, buffer);
        }
        else if (tagName.equals(CODE_TAG)) {
          generateCodeValue(tag, buffer);
        }
        else if (tagName.equals(LINKPLAIN_TAG)) {
          generateLinkValue(tag, buffer, true);
        }
        else if (tagName.equals(INHERITDOC_TAG)) {
          Pair<PsiElement[], InheritDocProvider<PsiElement[]>> inheritInfo = provider.getInheritDoc();

          if (inheritInfo != null) {
            generateValue(buffer, inheritInfo.first, inheritInfo.second);
          }
        }
        else if (tagName.equals(DOCROOT_TAG)) {
          buffer.append(getDocRoot());
        }
        else if (tagName.equals(VALUE_TAG)) {
          generateValueValue(tag, buffer, element);
        }
        else {
          buffer.append(element.getText());
        }
      }
      else {
        buffer.append(element.getText());
      }
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private static void generateCodeValue(@NotNull PsiInlineDocTag tag, @NotNull StringBuilder buffer) {
    buffer.append("<code>");
    generateLiteralValue(tag, buffer);
    buffer.append("</code>");
  }

  private static void generateLiteralValue(@NotNull PsiInlineDocTag tag, @NotNull StringBuilder buffer) {
    PsiElement[] elements = tag.getDataElements();

    for (PsiElement element : elements) {
      appendPlainText(element.getText(), buffer);
    }
  }

  private static void appendPlainText(@NonNls String text, @NotNull final StringBuilder buffer) {
    text = text.replaceAll("<", "&lt;");
    text = text.replaceAll(">", "&gt;");

    buffer.append(text);
  }

  private void generateLinkValue(@NotNull PsiInlineDocTag tag, @NotNull StringBuilder buffer, boolean plainLink) {
    PsiElement[] tagElements = tag.getDataElements();
    String text = createLinkText(tagElements);
    if (text.length() > 0) {
      int index = JavaDocUtil.extractReference(text);
      String refText = text.substring(0, index).trim();
      String label = text.substring(index).trim();
      if (label.length() == 0) {
        label = null;
      }
      generateLink(buffer, refText, label, tagElements[0], plainLink);
    }
  }

  private void generateValueValue(@NotNull final PsiInlineDocTag tag, @NotNull final StringBuilder buffer, @NotNull final PsiElement element) {
    String text = createLinkText(tag.getDataElements());
    PsiField valueField = null;
    if (text.length() == 0) {
      if (myElement instanceof PsiField) valueField = (PsiField) myElement;
    }
    else {
      PsiElement target = JavaDocUtil.findReferenceTarget(PsiManager.getInstance(myProject), text, myElement);
      if (target instanceof PsiField) {
        valueField = (PsiField) target;
      }
    }

    Object value = null;
    if (valueField != null) {
      PsiExpression initializer = valueField.getInitializer();
      value = JavaConstantExpressionEvaluator.computeConstantExpression(initializer, false);
    }

    if (value != null) {
      buffer.append(value.toString());
    }
    else {
      buffer.append(element.getText());
    }
  }

  private static String createLinkText(@NotNull final PsiElement[] tagElements) {
    int predictOffset = tagElements.length > 0
                        ? tagElements[0].getTextOffset() + tagElements[0].getText().length()
                        : 0;
    StringBuilder buffer1 = new StringBuilder();
    for (int j = 0; j < tagElements.length; j++) {
      PsiElement tagElement = tagElements[j];

      if (tagElement.getTextOffset() > predictOffset) buffer1.append(" ");
      predictOffset = tagElement.getTextOffset() + tagElement.getText().length();

      buffer1.append(tagElement.getText());

      if (j < tagElements.length - 1) {
        buffer1.append(" ");
      }
    }
    return buffer1.toString().trim();
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateDeprecatedSection(@NotNull StringBuilder buffer, @NotNull PsiDocComment comment) {
    PsiDocTag tag = comment.findTagByName("deprecated");
    if (tag != null) {
      buffer.append("<DD><DL>");
      buffer.append("<B>").append(CodeInsightBundle.message("javadoc.deprecated")).append("</B>&nbsp;");
      buffer.append("<I>");
      generateValue(buffer, tag.getDataElements(), ourEmptyElementsProvider);
      buffer.append("</I>");
      buffer.append("</DL></DD>");
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateSinceSection(@NotNull StringBuilder buffer, @NotNull PsiDocComment comment) {
    PsiDocTag tag = comment.findTagByName("since");
    if (tag != null) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.since")).append("</b>");
      buffer.append("<DD>");
      generateValue(buffer, tag.getDataElements(), ourEmptyElementsProvider);
      buffer.append("</DD></DL></DD>");
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateSeeAlsoSection(@NotNull StringBuilder buffer, @NotNull PsiDocComment comment) {
    PsiDocTag[] tags = comment.findTagsByName("see");
    if (tags.length > 0) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.see.also")).append("</b>");
      buffer.append("<DD>");
      for (int i = 0; i < tags.length; i++) {
        PsiDocTag tag = tags[i];
        PsiElement[] elements = tag.getDataElements();
        if (elements.length > 0) {
          String text = createLinkText(elements);

          if (text.startsWith("<")) {
            buffer.append(text);
          }
          else if (text.startsWith("\"")) {
            appendPlainText(text, buffer);
          }
          else {
            int index = JavaDocUtil.extractReference(text);
            String refText = text.substring(0, index).trim();
            String label = text.substring(index).trim();
            if (label.length() == 0) {
              label = null;
            }
            generateLink(buffer, refText, label, comment, false);
          }
        }
        if (i < tags.length - 1) {
          buffer.append(",\n");
        }
      }
      buffer.append("</DD></DL></DD>");
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateParametersSection(@NotNull StringBuilder buffer, @NotNull final PsiMethod method, @Nullable final PsiDocComment comment) {
    PsiParameter[] params = method.getParameterList().getParameters();
    PsiDocTag[] localTags = comment != null ? comment.findTagsByName("param") : PsiDocTag.EMPTY_ARRAY;

    LinkedList<Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>> collectedTags =
      new LinkedList<>();

    for (PsiParameter param : params) {
      final String paramName = param.getName();
      Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> parmTag = findDocTag(localTags, paramName, method);

      if (parmTag != null) {
        collectedTags.addLast(parmTag);
      }
    }

    if (collectedTags.size() > 0) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.parameters")).append("</b>");
      for (Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> tag : collectedTags) {
        PsiElement[] elements = tag.first.getDataElements();
        if (elements.length == 0) continue;
        generateOneParameter(elements, buffer, tag);
      }
      buffer.append("</DD></DL></DD>");
    }
  }

  private void generateTypeParametersSection(@NotNull final StringBuilder buffer, @NotNull final PsiMethod method) {
    final PsiDocComment docComment = method.getDocComment();
    if (docComment == null) return;
    final PsiDocTag[] localTags = docComment.findTagsByName("param");
    final PsiTypeParameter[] typeParameters = method.getTypeParameters();
    final LinkedList<Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>> collectedTags = new LinkedList<>();
    for (PsiTypeParameter typeParameter : typeParameters) {
      final String paramName = "<" + typeParameter.getName() + ">";
      Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> parmTag = findDocTag(localTags, paramName, method);

      if (parmTag != null) {
        collectedTags.addLast(parmTag);
      }
    }
    generateTypeParametersSection(buffer, collectedTags);
  }

  private void generateTypeParametersSection(@NotNull final StringBuilder buffer, @NotNull final LinkedList<Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>> collectedTags) {
    if (collectedTags.size() > 0) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.type.parameters")).append("</b>");
      for (Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> tag : collectedTags) {
        PsiElement[] elements = tag.first.getDataElements();
        if (elements.length == 0) continue;
        generateOneParameter(elements, buffer, tag);
      }
      buffer.append("</DD></DL></DD>");
    }
  }

  @Nullable private Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> findDocTag(@NotNull final PsiDocTag[] localTags,
                                                                                         final String paramName,
                                                                                         @NotNull final PsiMethod method) {
    Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> parmTag = null;
    for (PsiDocTag localTag : localTags) {
      PsiDocTagValue value = localTag.getValueElement();

      if (value != null) {
        String tagName = value.getText();

        if (tagName != null && tagName.equals(paramName)) {
          parmTag =
          new Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>
            (localTag,
             new InheritDocProvider<PsiDocTag>() {
               @Nullable
               public Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> getInheritDoc() {
                 return findInheritDocTag(method, parameterLocator(paramName));
               }

               @Nullable
               public PsiClass getElement() {
                 return method.getContainingClass();
               }
             });
          break;
        }
      }
    }

    if (parmTag == null) {
      parmTag = findInheritDocTag(method, parameterLocator(paramName));
    }
    return parmTag;
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateOneParameter(@NotNull final PsiElement[] elements,
                                    @NotNull final StringBuilder buffer,
                                    @NotNull final Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> tag) {
    String text = elements[0].getText();
    buffer.append("<DD>");
    int spaceIndex = text.indexOf(' ');
    if (spaceIndex < 0) {
      spaceIndex = text.length();
    }
    String parmName = text.substring(0, spaceIndex);
    buffer.append("<code>");
    buffer.append(StringUtil.escapeXml(parmName));
    buffer.append("</code>");
    buffer.append(" - ");
    buffer.append(text.substring(spaceIndex));
    generateValue(buffer, elements, 1, mapProvider(tag.second, true));
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateReturnsSection(@NotNull StringBuilder buffer, @NotNull final PsiMethod method, @Nullable final PsiDocComment comment) {
    PsiDocTag tag = comment == null ? null : comment.findTagByName("return");
    Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> pair =
      tag == null ? null : new Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>(tag,
                                                                              new InheritDocProvider<PsiDocTag>(){
                                                                                @Nullable
                                                                                public Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> getInheritDoc() {
                                                                                  return findInheritDocTag(method, new ReturnTagLocator());

                                                                                }

                                                                                @Nullable
                                                                                public PsiClass getElement() {
                                                                                  return method.getContainingClass();
                                                                                }
                                                                              });

    if (pair == null && myElement instanceof PsiMethod) {
      pair = findInheritDocTag(((PsiMethod)myElement), new ReturnTagLocator());
    }

    if (pair != null) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.returns")).append("</b>");
      buffer.append("<DD>");
      generateValue(buffer, pair.first.getDataElements(), mapProvider(pair.second, false));
      buffer.append("</DD></DL></DD>");
    }
  }

  private static PsiDocTag[] getThrowsTags(@Nullable PsiDocComment comment) {
    if (comment == null) {
      return PsiDocTag.EMPTY_ARRAY;
    }

    PsiDocTag[] tags1 = comment.findTagsByName(THROWS_KEYWORD);
    PsiDocTag[] tags2 = comment.findTagsByName("exception");

    return ArrayUtil.mergeArrays(tags1, tags2, PsiDocTag.class);
  }

  private static boolean areWeakEqual(@NotNull String one, @NotNull String two) {
    return one.equals(two) || one.endsWith("." + two) || two.endsWith("." + one);
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateThrowsSection(@NotNull StringBuilder buffer, @NotNull PsiMethod method, final PsiDocComment comment) {
    PsiDocTag[] localTags = getThrowsTags(comment);

    LinkedList<Pair<PsiDocTag, InheritDocProvider<PsiDocTag>>> collectedTags =
      new LinkedList<>();

    final List<PsiClassType> declaredThrows = new ArrayList<>(Arrays.asList(method.getThrowsList().getReferencedTypes()));

    for (int i = localTags.length - 1; i > -1; i--) {
      PsiDocTagValue valueElement = localTags[i].getValueElement();

      if (valueElement != null) {
        for (Iterator<PsiClassType> iterator = declaredThrows.iterator(); iterator.hasNext();) {
          PsiClassType classType = iterator.next();
          if (Comparing.strEqual(valueElement.getText(), classType.getClassName()) || Comparing.strEqual(valueElement.getText(), classType.getCanonicalText())) {
            iterator.remove();
            break;
          }
        }

        collectedTags.addFirst(new Pair<>(localTags[i], ourEmptyProvider));
      }
    }


    PsiClassType[] trousers = declaredThrows.toArray(new PsiClassType[declaredThrows.size()]);

    for (PsiClassType trouser : trousers) {
      if (trouser != null) {
        String paramName = trouser.getCanonicalText();
        Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> parmTag = null;

        for (PsiDocTag localTag : localTags) {
          PsiDocTagValue value = localTag.getValueElement();

          if (value != null) {
            String tagName = value.getText();

            if (tagName != null && areWeakEqual(tagName, paramName)) {
              parmTag = new Pair<>(localTag, ourEmptyProvider);
              break;
            }
          }
        }

        if (parmTag == null) {
          parmTag = findInheritDocTag(method, exceptionLocator(paramName));
        }

        if (parmTag != null) {
          collectedTags.addLast(parmTag);
        }
        else {
          try {
            final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(method.getProject()).getElementFactory();
            final PsiDocTag tag = elementFactory.createDocTagFromText("@exception " + paramName);
            collectedTags.addLast(new Pair<>(tag, ourEmptyProvider));
          }
          catch (IncorrectOperationException e) {
            LOG.error(e);
          }
        }
      }
    }

    if (collectedTags.size() > 0) {
      buffer.append("<DD><DL>");
      buffer.append("<DT><b>").append(CodeInsightBundle.message("javadoc.throws")).append("</b>");
      for (Pair<PsiDocTag, InheritDocProvider<PsiDocTag>> tag : collectedTags) {
        PsiElement[] elements = tag.first.getDataElements();
        if (elements.length == 0) continue;
        buffer.append("<DD>");
        String text = elements[0].getText();
        int index = JavaDocUtil.extractReference(text);
        String refText = text.substring(0, index).trim();
        generateLink(buffer, refText, null, method, false);
        String rest = text.substring(index);
        if (rest.length() > 0 || elements.length > 1) buffer.append(" - ");
        buffer.append(rest);
        generateValue(buffer, elements, 1, mapProvider(tag.second, true));
      }
      buffer.append("</DD></DL></DD>");
    }
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void generateSuperMethodsSection(@NotNull StringBuilder buffer, @NotNull PsiMethod method, boolean overrides) {
    PsiClass parentClass = method.getContainingClass();
    if (parentClass == null) return;
    if (parentClass.isInterface() && !overrides) return;
    PsiMethod[] supers = method.findSuperMethods();
    if (supers.length == 0) return;
    boolean headerGenerated = false;
    for (PsiMethod superMethod : supers) {
      boolean isAbstract = superMethod.hasModifierProperty(PsiModifier.ABSTRACT);
      if (overrides) {
        if (parentClass.isInterface() ? !isAbstract : isAbstract) continue;
      }
      else {
        if (!isAbstract) continue;
      }
      PsiClass superClass = superMethod.getContainingClass();
      if (!headerGenerated) {
        buffer.append("<DD><DL>");
        buffer.append("<DT><b>");
        buffer.append(overrides ?
                      CodeInsightBundle.message("javadoc.method.overrides") :
                      CodeInsightBundle .message("javadoc.method.specified.by"));
        buffer.append("</b>");
        headerGenerated = true;
      }
      buffer.append("<DD>");

      StringBuilder methodBuffer = new StringBuilder();
      generateLink(methodBuffer, superMethod, superMethod.getName());
      StringBuilder classBuffer = new StringBuilder();
      generateLink(classBuffer, superClass, superClass.getName());
      if (superClass.isInterface()) {
        buffer.append(CodeInsightBundle.message("javadoc.method.in.interface", methodBuffer.toString(), classBuffer.toString()));
      }
      else {
        buffer.append(CodeInsightBundle.message("javadoc.method.in.class", methodBuffer.toString(), classBuffer.toString()));
      }
    }
    if (headerGenerated) {
      buffer.append("</DD></DL></DD>");
    }
  }

  private void generateLink(StringBuilder buffer, PsiElement element, String label) {
    String refText = JavaDocUtil.getReferenceText(myProject, element);
    if (refText != null) {
      DocumentationManager.createHyperlink(buffer, refText, label, false);
      //return generateLink(buffer, refText, label, context, false);
    }
  }

  /**
   * @return Length of the generated label.
   */
  @SuppressWarnings({"HardCodedStringLiteral"})
  private static int generateLink(@NotNull StringBuilder buffer, @Nullable String refText, @Nullable String label, @NotNull PsiElement context, boolean plainLink) {
    if (label == null) {
      final PsiManager manager = context.getManager();
      label = JavaDocUtil.getLabelText(manager.getProject(), manager, refText, context);
    }

    LOG.assertTrue(refText != null, "refText appears to be null.");

    final PsiElement target = JavaDocUtil.findReferenceTarget(context.getManager(), refText, context);
    boolean isBrokenLink = target == null;
    if (isBrokenLink) {
      buffer.append("<font color=red>");
      buffer.append(label);
      buffer.append("</font>");
      return label.length();
    }


    DocumentationManager.createHyperlink(buffer, JavaDocUtil.getReferenceText(context.getProject(), target), label, plainLink);
    return label.length();
  }

  /**
   * @return Length of the generated label.
   */
  @SuppressWarnings({"HardCodedStringLiteral"})
  public static int generateType(@NotNull StringBuilder buffer, @NotNull PsiType type, @NotNull PsiElement context) {
    return generateType(buffer, type, context, true);
  }

  /**
   * @return Length of the generated label.
   */
  @SuppressWarnings({"HardCodedStringLiteral"})
  public static int generateType(@NotNull StringBuilder buffer, @NotNull PsiType type, @NotNull PsiElement context, boolean generateLink) {
    if (type instanceof PsiPrimitiveType) {
      String text = type.getCanonicalText();
      buffer.append(text);
      return text.length();
    }

    if (type instanceof PsiArrayType) {
      int rest = generateType(buffer, ((PsiArrayType)type).getComponentType(), context, generateLink);
      if ((type instanceof PsiEllipsisType)) {
        buffer.append("...");
        return rest + 3;
      }
      else {
        buffer.append("[]");
        return rest + 2;
      }
    }

    if (type instanceof PsiWildcardType){
      PsiWildcardType wt = ((PsiWildcardType)type);

      buffer.append("?");

      PsiType bound = wt.getBound();

      if (bound != null){
        final String keyword = wt.isExtends() ? " extends " : " super ";
        buffer.append(keyword);
        return generateType(buffer, bound, context, generateLink) + 1 + keyword.length();
      }

      return 1;
    }

    if (type instanceof PsiClassType) {
      PsiClassType.ClassResolveResult result = ((PsiClassType)type).resolveGenerics();
      PsiClass psiClass = result.getElement();
      PsiSubstitutor psiSubst = result.getSubstitutor();

      if (psiClass == null) {
        String text = "<font color=red>" + type.getCanonicalText() + "</font>";
        buffer.append(text);
        return text.length();
      }

      String qName = psiClass.getQualifiedName();

      if (qName == null || psiClass instanceof PsiTypeParameter) {
        String text = type.getCanonicalText();
        buffer.append(text);
        return text.length();
      }

      int length;
      if (generateLink) {
        length = generateLink(buffer, qName, null, context, false);
      }
      else {
        buffer.append(qName);
        length = buffer.length();
      }

      if (psiClass.hasTypeParameters()) {
        StringBuilder subst = new StringBuilder();
        boolean goodSubst = true;

        PsiTypeParameter[] params = psiClass.getTypeParameters();

        subst.append("&lt;");
        for (int i = 0; i < params.length; i++) {
          PsiType t = psiSubst.substitute(params[i]);

          if (t == null) {
            goodSubst = false;
            break;
          }

          length += generateType(subst, t, context, generateLink);

          if (i < params.length - 1) {
            subst.append(", ");
          }
        }

        if (goodSubst) {
          subst.append("&gt;");
          String text = subst.toString();

          buffer.append(text);
        }
      }

      return length;
    }

    if (type instanceof PsiDisjunctionType) {
      if (!generateLink) {
        final String text = type.getCanonicalText();
        buffer.append(text);
        return text.length();
      }
      else {
        int length = 0;
        for (PsiType psiType : ((PsiDisjunctionType)type).getDisjunctions()) {
          if (length > 0) {
            buffer.append(" | ");
            length += 3;
          }
          length += generateType(buffer, psiType, context, generateLink);
        }
        return length;
      }
    }

    return 0;
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private String generateTypeParameters(@NotNull PsiTypeParameterListOwner owner) {
    if (owner.hasTypeParameters()) {
      PsiTypeParameter[] parms = owner.getTypeParameters();

      StringBuilder buffer = new StringBuilder();

      buffer.append("&lt;");

      for (int i = 0; i < parms.length; i++) {
        PsiTypeParameter p = parms[i];

        buffer.append(p.getName());

        PsiClassType[] refs = JavaDocUtil.getExtendsList(p);

        if (refs.length > 0) {
          buffer.append(" extends ");

          for (int j = 0; j < refs.length; j++) {
            generateType(buffer, refs[j], owner);

            if (j < refs.length - 1) {
              buffer.append(" & ");
            }
          }
        }

        if (i < parms.length - 1) {
          buffer.append(", ");
        }
      }

      buffer.append("&gt;");

      return buffer.toString();
    }

    return "";
  }

  @Nullable
  private <T> Pair<T, InheritDocProvider<T>> searchDocTagInOverridenMethod(@NotNull PsiMethod method,
                                                                           @Nullable final PsiClass aSuper,
                                                                           @NotNull final DocTagLocator<T> loc) {
    if (aSuper != null) {

      final PsiMethod overriden =  findMethodInSuperClass(method, aSuper);

      if (overriden != null) {
        T tag = loc.find(getDocComment(overriden));

        if (tag != null) {
          return new Pair<T, InheritDocProvider<T>>
            (tag,
             new InheritDocProvider<T>() {
               @Nullable
               public Pair<T, InheritDocProvider<T>> getInheritDoc() {
                 return findInheritDocTag(overriden, loc);
               }

               @Nullable
               public PsiClass getElement() {
                 return aSuper;
               }
             });
        }
      }
    }

    return null;
  }

  @Nullable
  private <T> PsiMethod findMethodInSuperClass(@NotNull PsiMethod method, @NotNull PsiClass aSuper) {
    PsiMethod overriden = null;
    for (PsiMethod superMethod : method.findDeepestSuperMethods()) {
      overriden = aSuper.findMethodBySignature(superMethod, false);
      if (overriden != null) {
        return overriden;
      }
    }
    return overriden;
  }

  @Nullable private <T> Pair<T, InheritDocProvider<T>> searchDocTagInSupers(@NotNull PsiClassType[] supers,
                                                                  @NotNull PsiMethod method,
                                                                  @NotNull DocTagLocator<T> loc) {
    for (PsiClassType superType : supers) {
      PsiClass aSuper = superType.resolve();

      if (aSuper != null) {
        Pair<T, InheritDocProvider<T>> tag = searchDocTagInOverridenMethod(method, aSuper, loc);

        if (tag != null) return tag;
      }
    }

    for (PsiClassType superType : supers) {
      PsiClass aSuper = superType.resolve();

      if (aSuper != null) {
        Pair<T, InheritDocProvider<T>> tag = findInheritDocTagInClass(method, aSuper, loc);

        if (tag != null) {
          return tag;
        }
      }
    }

    return null;
  }

  @Nullable
  private <T> Pair<T, InheritDocProvider<T>> findInheritDocTagInClass(@NotNull PsiMethod aMethod,
                                                                      @Nullable PsiClass aClass,
                                                                      @NotNull DocTagLocator<T> loc) {
    if (aClass == null) {
      return null;
    }

    PsiClassType[] implementsTypes = aClass.getImplementsListTypes();
    Pair<T, InheritDocProvider<T>> tag = searchDocTagInSupers(implementsTypes, aMethod, loc);

    if (tag != null) {
      return tag;
    }

    PsiClassType[] extendsTypes = aClass.getExtendsListTypes();
    return searchDocTagInSupers(extendsTypes, aMethod, loc);
  }

  @Nullable private <T> Pair<T, InheritDocProvider<T>> findInheritDocTag(@NotNull PsiMethod method, @NotNull DocTagLocator<T> loc) {
    PsiClass aClass = method.getContainingClass();

    if (aClass == null) return null;

    return findInheritDocTagInClass(method, aClass, loc);
  }

  private static class ReturnTagLocator implements DocTagLocator<PsiDocTag> {
    @Nullable
    public PsiDocTag find(@Nullable PsiDocComment comment) {
      if (comment == null) {
        return null;
      }

      return comment.findTagByName("return");
    }
  }

}
