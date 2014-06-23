/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.icons;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface GosuIcons {
  @Nullable
  Icon CLASS = IconLoader.findIcon("/gw/plugin/ij/icons/class.png");
  @Nullable
  Icon ANNOTATION = IconLoader.findIcon("/gw/plugin/ij/icons/ann.png");
  @Nullable
  Icon ANONYMOUS_CLASS = CLASS;
  @Nullable
  Icon ENHANCEMENT = IconLoader.findIcon("/gw/plugin/ij/icons/enh.png");
  @Nullable
  Icon ENUM = IconLoader.findIcon("/gw/plugin/ij/icons/enum.png");
  @Nullable
  Icon INTERFACE = IconLoader.findIcon("/gw/plugin/ij/icons/int.png");
  @Nullable
  Icon PROGRAM = IconLoader.findIcon("/gw/plugin/ij/icons/prog.png");
  @Nullable
  Icon TEMPLATE = IconLoader.findIcon("/gw/plugin/ij/icons/temp.png");

  @Nullable
  Icon FILE_CLASS = CLASS;
  @Nullable
  Icon FILE_ENHANCEMENT = ENHANCEMENT;
  @Nullable
  Icon FILE_PROGRAM = PROGRAM;
  @Nullable
  Icon FILE_TEMPLATE = TEMPLATE;
  @Nullable
  Icon FILE_SCRATCHPAD = IconLoader.findIcon("/gw/plugin/ij/icons/Scratchpad.png");

  Icon FIELD = PlatformIcons.FIELD_ICON;
  Icon METHOD = PlatformIcons.METHOD_ICON;
  Icon ABSTRACT_METHOD = PlatformIcons.ABSTRACT_METHOD_ICON;
  Icon PROPERTY = PlatformIcons.PROPERTY_ICON;
  Icon VARIABLE = PlatformIcons.VARIABLE_ICON;
  Icon PARAMETER = PlatformIcons.PARAMETER_ICON;
  Icon FUNCTION = PlatformIcons.FUNCTION_ICON;

  @Nullable
  Icon ENH = IconLoader.findIcon("/gw/plugin/ij/icons/enh_tag.png");

  @Nullable
  Icon G_16 = IconLoader.findIcon("/gw/plugin/ij/icons/g_16.png");
  @Nullable
  Icon G_24 = IconLoader.findIcon("/gw/plugin/ij/icons/g_24.png");
  @Nullable
  Icon G_32 = IconLoader.findIcon("/gw/plugin/ij/icons/g_32.png");

  @Nullable
  Icon EXEC_IN_PROCESS = IconLoader.findIcon("/gw/plugin/ij/icons/ExecuteInDebugProcess.png");
  @Nullable
  Icon PASTE_AS_GOSU = IconLoader.findIcon("/gw/plugin/ij/icons/PasteAsGosu.png");
}
