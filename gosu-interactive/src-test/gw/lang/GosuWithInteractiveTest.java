/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.launch.Launch;
import gw.lang.mode.GosuMode;
import gw.lang.shell.InteractiveMode;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: bchang
 * Date: 3/20/12
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class GosuWithInteractiveTest extends Assert {

  @BeforeClass
  public static void init() {
    assertEquals(GosuMode.GOSU_MODE_PRIORITY_INTERACTIVE, new InteractiveMode().getPriority());
  }

  @Test
  public void accepts() {
    InteractiveMode mode = new InteractiveMode();
    mode.setArgInfo(Launch.factory().createArgInfo("-i"));
    assertTrue(mode.accept());
  }

  @Test
  public void rejects() {
    InteractiveMode mode = new InteractiveMode();
    mode.setArgInfo(Launch.factory().createArgInfo());
    assertFalse(mode.accept());
  }
}
