/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

/**
 */
import com.intellij.psi.PsiModifier;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.ui.VisibilityPanelBase;
import com.intellij.util.VisibilityUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.UIUtil;
import gw.lang.parser.Keyword;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;

public class GosuVisibilityPanel extends VisibilityPanelBase {
  private JRadioButton myRbAsIs;
  private JRadioButton myRbEscalate;
  private final JRadioButton myRbPrivate;
  private final JRadioButton myRbProtected;
  private final JRadioButton myRbPackageLocal;
  private final JRadioButton myRbPublic;

  public GosuVisibilityPanel(boolean hasAsIs, final boolean hasEscalate) {
    setBorder(IdeBorderFactory.createTitledBorder(RefactoringBundle.message("visibility.border.title"), true,
                                                  new Insets(IdeBorderFactory.TITLED_BORDER_TOP_INSET,
                                                             UIUtil.DEFAULT_HGAP,
                                                             IdeBorderFactory.TITLED_BORDER_BOTTOM_INSET,
                                                             IdeBorderFactory.TITLED_BORDER_RIGHT_INSET)));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    ButtonGroup bg = new ButtonGroup();

    ItemListener listener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          ChangeListener multicaster = (ChangeListener) myEventDispatcher.getMulticaster();
          multicaster.stateChanged(new ChangeEvent(this));
        }
      }
    };

    if (hasEscalate) {
      myRbEscalate = new JRadioButton();
      myRbEscalate.setText(RefactoringBundle.getEscalateVisibility());
      myRbEscalate.addItemListener(listener);
      add(myRbEscalate);
      bg.add(myRbEscalate);
    }

    if (hasAsIs) {
      myRbAsIs = new JRadioButton();
      myRbAsIs.setText(RefactoringBundle.getVisibilityAsIs());
      myRbAsIs.addItemListener(listener);
      add(myRbAsIs);
      bg.add(myRbAsIs);
    }


    myRbPrivate = new JRadioButton();
    myRbPrivate.setText(RefactoringBundle.getVisibilityPrivate());
    myRbPrivate.addItemListener(listener);
    myRbPrivate.setFocusable(false);
    add(myRbPrivate);
    bg.add(myRbPrivate);

    myRbPackageLocal = new JRadioButton();
    myRbPackageLocal.setText( "Internal" );
    myRbPackageLocal.addItemListener(listener);
    myRbPackageLocal.setFocusable(false);
    myRbPackageLocal.setMnemonic( 'I' );
    add(myRbPackageLocal);
    bg.add(myRbPackageLocal);

    myRbProtected = new JRadioButton();
    myRbProtected.setText(RefactoringBundle.getVisibilityProtected());
    myRbProtected.addItemListener(listener);
    myRbProtected.setFocusable(false);
    add(myRbProtected);
    bg.add(myRbProtected);

    myRbPublic = new JRadioButton();
    myRbPublic.setText(RefactoringBundle.getVisibilityPublic());
    myRbPublic.addItemListener(listener);
    myRbPublic.setFocusable(false);
    add(myRbPublic);
    bg.add(myRbPublic);
  }


  public String getVisibility() {
    if (myRbPublic.isSelected()) {
      return IGosuModifier.PUBLIC;
    }
    if (myRbPackageLocal.isSelected()) {
      return IGosuModifier.INTERNAL;
    }
    if (myRbProtected.isSelected()) {
      return IGosuModifier.PROTECTED;
    }
    if (myRbPrivate.isSelected()) {
      return IGosuModifier.PRIVATE;
    }
    if (myRbEscalate != null && myRbEscalate.isSelected()) {
      return VisibilityUtil.ESCALATE_VISIBILITY;
    }

    return null;
  }

  @Override
  public void setVisibility(Object visibility) {
    if (PsiModifier.PUBLIC.equals(visibility)) {
      myRbPublic.setSelected(true);
    }
    else if (PsiModifier.PROTECTED.equals(visibility)) {
      myRbProtected.setSelected(true);
    }
    else if (PsiModifier.PACKAGE_LOCAL.equals(visibility)) {
      myRbPackageLocal.setSelected(true);
    }
    else if (PsiModifier.PRIVATE.equals(visibility)) {
      myRbPrivate.setSelected(true);
    }
    else if (myRbEscalate != null) {
      myRbEscalate.setSelected(true);
    }
    else if (myRbAsIs != null) {
      myRbAsIs.setSelected(true);
    }
  }

  public void setVisibility(String visibility) {
    if (IGosuModifier.PUBLIC.equals(visibility)) {
      myRbPublic.setSelected(true);
    }
    else if (IGosuModifier.PROTECTED.equals(visibility)) {
      myRbProtected.setSelected(true);
    }
    else if (IGosuModifier.INTERNAL.equals(visibility)) {
      myRbPackageLocal.setSelected(true);
    }
    else if (IGosuModifier.PRIVATE.equals(visibility)) {
      myRbPrivate.setSelected(true);
    }
    else if (myRbEscalate != null) {
      myRbEscalate.setSelected(true);
    }
    else if (myRbAsIs != null) {
      myRbAsIs.setSelected(true);
    }
  }

  public void disableAllButPublic() {
    myRbPrivate.setEnabled(false);
    myRbProtected.setEnabled(false);
    myRbPackageLocal.setEnabled(false);
    if (myRbEscalate != null) {
      myRbEscalate.setEnabled(false);
    }
    if (myRbAsIs != null) {
      myRbAsIs.setEnabled(false);
    }
    myRbPublic.setEnabled(true);
    myRbPublic.setSelected(true);
  }
}
