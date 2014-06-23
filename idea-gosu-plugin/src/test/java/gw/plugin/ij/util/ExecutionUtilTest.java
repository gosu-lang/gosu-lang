/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Disposer;
import junit.framework.TestCase;

import static gw.plugin.ij.util.ExecutionUtil.*;

public class ExecutionUtilTest extends TestCase {
  private Disposable myAppDisposable = null;
  private MockApp application;

  public void setUp() {
    myAppDisposable = Disposer.newDisposable();
    application = new MockApp(myAppDisposable);
    ApplicationManager.setApplication(application, myAppDisposable);
  }

  public void tearDown() {
    if (myAppDisposable != null) {
      Disposer.dispose(myAppDisposable);
    }
  }

  public void testSafeRunnable() {
    final String[] threadName = new String[1];
    final boolean[] inInWriteAction = new boolean[1];
    ExecutionUtil.execute(WRITE | DISPATCH | BLOCKING, new SafeRunnable() {
      public void execute() throws Exception {
        threadName[0] = Thread.currentThread().getName();
        inInWriteAction[0] = application.inWriteAction;
      }
    });
    assertTrue(threadName[0].startsWith("AWT-EventQueue-0"));
    assertEquals(true, inInWriteAction[0]);
  }

  public void testSafeCallable() {
    final String[] threadName = new String[1];
    final boolean[] inInWriteAction = new boolean[1];
    ExecutionUtil.execute(WRITE | DISPATCH | BLOCKING, new SafeCallable<Object>() {
      public Object execute() throws Exception {
        threadName[0] = Thread.currentThread().getName();
        inInWriteAction[0] = application.inWriteAction;
        return null;
      }
    });
    assertTrue(threadName[0].startsWith("AWT-EventQueue-0"));
    assertEquals(true, inInWriteAction[0]);
  }
}
