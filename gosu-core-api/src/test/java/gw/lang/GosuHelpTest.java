/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import junit.framework.Assert;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: bchang
 * Date: 3/20/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GosuHelpTest extends Assert {

  @Test
  public void showHelpShowsRegisteredKeysInProperOrder() {
    StringWriter writer = new StringWriter();
    PrintWriter out = new PrintWriter(writer);
    Gosu.showHelp(out);
    String sep = System.getProperty("line.separator");
    assertEquals(
            "Usage:" + sep +
                    "        gosu [options] [program [args...]]" + sep +
                    "" + sep +
                    "Options:" + sep +
                    "        -f, -file FILE              load a file-based Gosu source" + sep +
                    "            -url URL                load a url-based Gosu source" + sep +
                    "        -e, -eval EXPR              load a Gosu expression" + sep +
                    "            -classpath PATH         additional elements for the classpath, separated by commas" + sep +
                    "        -i, -interactive            starts an interactive Gosu shell" + sep +
                    "            -verify                 verifies the Gosu source" + sep +
                    "            -version                displays the version of Gosu" + sep +
                    "        -h, -help                   displays this command-line help" + sep,
            writer.toString()
    );
  }
}
