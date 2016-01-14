/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.explorer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import com.intellij.util.PlatformIcons;
import gw.fs.IFile;
import gw.internal.gosu.parser.JavaTypeInfo;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;


public class TypeSystemExplorerForm {
  private JTree tree;
  private JTextField searchField;
  private JList list;
  private JPanel panel;
  private JSplitPane splitPane;
  private JPanel toolbarPanel;
  private JLabel infoLabel;

  public JPanel getPanel() {
    return panel;
  }

  public JTextField getSearchField() {
    return searchField;
  }

  private Project project;

  private void updateInfo() {
    final FilteredListModel<String> model = (FilteredListModel<String>) list.getModel();
    final int originalSize = model.getOriginalSize();
    final int filteredSize = model.getSize();
    if (originalSize != filteredSize) {
      infoLabel.setText(String.format("%,d out of %,d types", filteredSize, originalSize));
    } else {
      infoLabel.setText(String.format("%,d types", originalSize));
    }
  }

  public TypeSystemExplorerForm(Project project) {
    this.project = project;

    // Search Field
    searchField.getDocument().addDocumentListener(new DocumentListener() {
      private void update(DocumentEvent e) {
        final String text = searchField.getText();
        final FilteredListModel<String> model = (FilteredListModel<String>) list.getModel();
        model.setPredicate(new Predicate<String>() {
          @Override
          public boolean apply(@Nullable String typeName) {
            return typeName.toLowerCase().contains(text.toLowerCase());
          }
        });
        model.update();
        updateInfo();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        update(e);
      }
    });

    // List
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {

      }
    });

    list.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list,
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean hasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        return label;
      }
    });

    list.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          final String typeName = (String) list.getSelectedValue();
          tree.setModel(new DefaultTreeModel(getTreeForType(typeName)));
        }
      }
    });

    list.setModel(new FilteredListModel<String>(getTypeNames(), Predicates.<String>alwaysTrue()));
    tree.setModel(new DefaultTreeModel(null));

    GuiUtils.replaceJSplitPaneWithIDEASplitter(panel);
    splitPane.setDividerLocation(0.8);

    updateInfo();
  }

  private List<String> getTypeNames() {
    final IModule module = GosuModuleUtil.getGlobalModule(project);
    final List<String> result = Lists.newArrayList();
    try {
      TypeSystem.pushModule(module);
      for (CharSequence seq : TypeSystem.getAllTypeNames()) {
        result.add(seq.toString());
      }
    } finally {
      TypeSystem.popModule(module);
    }
    Collections.sort(result);
    return result;
  }

  private TreeNode getTreeForType(String typeName) {
    final IModule module = GosuModuleUtil.getGlobalModule(project);

    try {
      TypeSystem.pushModule(module);
      final IType type = TypeSystem.getByFullName(typeName);
      final ITypeInfo typeInfo = type.getTypeInfo();

       final DefaultMutableTreeNode root = new DefaultMutableTreeNode(getTypeTitle(type));
      root.add(new TypeConstructorsTreeNode(project, type, typeInfo));
      root.add(new TypePropertiesTreeNode(project, type, typeInfo));
      root.add(new TypeMethodsTreeNode(project, type, typeInfo));
      root.add(new TypeFilesTreeNode(project, type, typeInfo));
      return root;
    } catch (Exception e) {
      return new DefaultMutableTreeNode(e.getMessage());
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private String getTypeTitle(IType type) {
    return type.getName() + " [" + type.getTypeLoader() + "]";
  }

  protected AnAction createSynchronizeAction() {
    return new TypeSystemAwareAction("Synchronize", "Synchronize", PlatformIcons.SYNCHRONIZE_ICON) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        final FilteredListModel<String> model = (FilteredListModel) list.getModel();
        model.setItems(getTypeNames());
        model.update();
      }
    };
  }

  protected AnAction createRefreshAction() {
    return new TypeSystemAwareAction("Refresh Type", "Refresh Type", PlatformIcons.DELETE_ICON) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        final String typeName = (String) list.getSelectedValue();
        final IType type = TypeSystem.getByFullName(typeName, TypeSystem.getGlobalModule());
        final IFile[] sourceFiles = type.getSourceFiles();
        if( sourceFiles.length > 0 ) {
          for (IFile sourceFile : sourceFiles) {
            TypeSystem.refreshed(sourceFile);
          }
        } else {
          TypeSystem.refresh((ITypeRef) type);
        }
      }
    };
  }

  protected AnAction createPrintAction() {
    return new TypeSystemAwareAction("Generate Code", "GenerateCode", PlatformIcons.ANALYZE) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        final String typeName = (String) list.getSelectedValue();
        TypeSystem.pushGlobalModule();
        try {
          final IType type = TypeSystem.getByFullName(typeName, TypeSystem.getGlobalModule());
          StringBuilder stringBuilder = new ClassGenerator().genClassProxy(type);
          System.out.println(stringBuilder.toString());
        } finally {
          TypeSystem.popGlobalModule();
        }
      }
    };
  }

  public DefaultActionGroup createActions() {
    final DefaultActionGroup group = new DefaultActionGroup(null, false);
    group.add(createSynchronizeAction());
    group.add(createRefreshAction());
    group.add(createPrintAction());
    return group;
  }

  private void createUIComponents() {
    final ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("", createActions(), false);
    this.toolbarPanel = (JPanel) toolbar.getComponent();
  }
}
