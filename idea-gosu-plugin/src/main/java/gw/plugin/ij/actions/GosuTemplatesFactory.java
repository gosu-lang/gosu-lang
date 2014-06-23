/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.io.OutputSupplier;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import static com.google.common.io.CharStreams.newWriterSupplier;

public class GosuTemplatesFactory implements FileTemplateGroupDescriptorFactory {
  public static final String GOSU_ANNOTATION_TEMPLATE = "GosuAnnotation.gs";
  public static final String GOSU_CLASS_TEMPLATE = "GosuClass.gs";
  public static final String GOSU_INTERFACE_TEMPLATE = "GosuInterface.gs";
  public static final String GOSU_ENUM_TEMPLATE = "GosuEnum.gs";
  public static final String GOSU_ENHANCEMENT_TEMPLATE = "GosuEnhancement.gsx";
  public static final String GOSU_PROGRAM_TEMPLATE = "GosuProgram.gsp";
  public static final String GOSU_TEMPLATE_TEMPLATE = "GosuTemplate.gst";

  public static final List<String> TEMPLATES = ImmutableList.of(
      GOSU_ANNOTATION_TEMPLATE,
      GOSU_CLASS_TEMPLATE,
      GOSU_INTERFACE_TEMPLATE,
      GOSU_ENUM_TEMPLATE,
      GOSU_ENHANCEMENT_TEMPLATE,
      GOSU_PROGRAM_TEMPLATE,
      GOSU_TEMPLATE_TEMPLATE);

  public static final String PROPERTY_ENHANCED_CLASS = "ENHANCED_CLASS";
  public static final String PROPERTY_NAME = "NAME";
  public static final String PROPERTY_EXTENDS_IMPLEMENTS = "EXTENDS_IMPLEMENTS";
  public static final String PROPERTY_PACKAGE_NAME = "PACKAGE_NAME";

  private static class GosuTemplatesFactoryHolder {
    private static final GosuTemplatesFactory myInstance = new GosuTemplatesFactory();
  }

  @NotNull
  public static GosuTemplatesFactory getInstance() {
    return GosuTemplatesFactoryHolder.myInstance;
  }

  @NotNull
  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    final FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("Gosu", GosuIcons.CLASS);
    final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
    for (String template : TEMPLATES) {
      group.addTemplate(new FileTemplateDescriptor(template, fileTypeManager.getFileTypeByFileName(template).getIcon()));
    }
    return group;
  }

  @NotNull
  private static Properties makeProperties(@NotNull PsiDirectory directory, String className, @NotNull String... parameters) {
    final Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());
    for (int i = 0; i < parameters.length; i += 2) {
      properties.setProperty(parameters[i], parameters[i + 1]);
    }
    properties.setProperty(PROPERTY_NAME, className);
    if (properties.getProperty(PROPERTY_EXTENDS_IMPLEMENTS) == null) {
      properties.setProperty(PROPERTY_EXTENDS_IMPLEMENTS, "");
    }

    final PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(directory);
    if (pkg == null) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.nopackage"));
    }
    properties.setProperty(PROPERTY_PACKAGE_NAME, pkg.getQualifiedName());
    return properties;
  }

  @NotNull
  public static PsiFile createFromTemplate(@NotNull final PsiDirectory directory, final String className, @NotNull String fileName, @NotNull String templateName, @NonNls String... parameters) {
    final FileTemplate template = FileTemplateManager.getInstance().getTemplate(templateName);

    String text;
    try {
      text = template.getText(makeProperties(directory, className, parameters));
    } catch (IOException e) {
      throw new IncorrectOperationException(GosuBundle.message("error.file.template", FileTemplateManager.getInstance().internalTemplateToSubject(templateName)), e);
    }

    directory.checkCreateFile(fileName);

    final PsiFile file = directory.createFile(fileName);

    try {
      CharStreams.write(text, newWriterSupplier(new OutputSupplier<OutputStream>() {
        @Override
        public OutputStream getOutput() throws IOException {
          return file.getVirtualFile().getOutputStream(null);
        }
      }, Charsets.UTF_8));
    } catch (IOException e) {
      throw new IncorrectOperationException(e.getMessage(), e);
    }

    return file;
  }
}
