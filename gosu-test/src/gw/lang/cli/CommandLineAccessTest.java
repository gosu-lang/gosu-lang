/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommandLineAccessTest extends TestClass {
  public static final String PROPERTY_NEWLINE = System.getProperty("line.separator");

  @Override
  public void beforeTestClass() {
    CommandLineAccess.setUseTerminalWidth(false);
  }

  public void testLongLine() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClassFormatting"));
    String nl = PROPERTY_NEWLINE;
    assertEquals("command line help output bad", "usage: unknown program" + nl +
            " -checkdbconsistencyasbatch,--checkdbconsistencyasbatch <tableselection" + nl +
            " checktypeselection>   Run the database consistency checks as a batch job." + nl +
            " See the Admin Guide page for details." + nl +
            " -plain_bool_arg,--plain_bool_arg" + nl +
            " A plain boolean argument. The second line of a short boolean argument." + nl +
            " -plain_str_arg,--plain_str_arg <arg>" + nl +
            " A plain string argument. The line is long but the message is short. This" + nl +
            " should be broken better." + nl +
            " -s,--str_with_short_arg <arg>" + nl +
            " A string argument with a short name. A second long line of an argument" + nl +
            " with a short name but lots of description. This could be said better with" + nl +
            " less." + nl +
            " -strings_arg,--strings_arg <arg>" + nl +
            " A string array argument that has too many comment lines." + nl, helpMsg);
  }


  public void testOptionsSorted() {
    // Three options AA, BB, CC. In the gs file they're arranged as AA, CC, BB.
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClassFormattingAlpha"));
    String[] line = helpMsg.split(PROPERTY_NEWLINE);
    assertMatchRegex("wrong sort order", ".*aaarg.*", line[1]);
    assertMatchRegex("wrong sort order", ".*bbarg.*", line[2]);
    assertMatchRegex("wrong sort order", ".*ccarg.*", line[3]);
  }

  public void testSystemToolsOptionsSorted() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.SystemToolsArgs"));
    // Remove non options lines (starting with -)
    List<String> lines = new ArrayList<String>();
    for (String l : helpMsg.split(PROPERTY_NEWLINE)) {
      if (l.matches("^\\s*-.*")) {
        lines.add(l);
      }
    }
    // The lines sorted for comparison.
    List<String> sorted = new ArrayList<String>();
    sorted.addAll(lines);
    Collections.sort(sorted, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.toLowerCase().compareTo(s2.toLowerCase());
      }
    });

    // Both should be the same.
    assertListEquals(lines, sorted);
  }

  public void testBasicStringPropertyIsSet() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_str_arg", "bar"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("bar", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainStrArg"));
  }

  public void testBasicStringPropertyIsSetAsLongArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("--plain_str_arg", "bar"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("bar", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainStrArg"));
  }

  public void testBasicBooleanPropertyIsSet() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_bool_arg"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals(Boolean.TRUE, ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainBoolArg"));
  }

  public void testBasicBooleanPropertyIsSetAsLongArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("--plain_bool_arg"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals(Boolean.TRUE, ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainBoolArg"));
  }

  public void testStringWithShortArgIsSetWithShortArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("-s", "foo"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("foo", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StrWithShortArg"));
  }

  public void testStringWithShortArgIsSetWithLongArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("--str_with_short_arg", "foo"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("foo", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StrWithShortArg"));
  }

/*
  public void testStringWithShortArgIsNotSetWhenUsingLongNameWithShortArgPrefix() {
    CommandLineAccess.setRawArgs(Arrays.asList("-str_with_short_arg", "foo")); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertFalse(goodArgs);
  }
*/

  public void testDocumentationStringIsCorrect() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"));
    String nl = PROPERTY_NEWLINE;
    assertEquals("Documentation string incorrect", "usage: unknown program" + nl +
            " -d,--str_with_short_arg_and_default_value <arg>   A string argument with" + nl +
            "                                                   a short name and" + nl +
            "                                                   default" + nl +
            " -plain_bool_arg,--plain_bool_arg                  A plain boolean" + nl +
            "                                                   argument" + nl +
            " -plain_str_arg,--plain_str_arg <arg>              A plain string argument" + nl +
            " -s,--str_with_short_arg <arg>                     A string argument with" + nl +
            "                                                   a short name" + nl +
            " -strings_arg,--strings_arg <arg>                  A string array argument" + nl, helpMsg);
  }

  public void testStringWithDefaultValueWorksCorrectly() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList()); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("defaultValue", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StrWithShortArgAndDefaultValue"));
  }

  public void testAllArgumentsTogetherWorks() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_str_arg", "foo1", "-plain_bool_arg", "-s", "foo2", "-d", "foo3"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertEquals("foo1", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainStrArg"));
    assertEquals(true, ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainBoolArg"));
    assertEquals("foo2", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StrWithShortArg"));
    assertEquals("foo3", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StrWithShortArgAndDefaultValue"));
  }

  public void testCommandLineDoesNotParseWithoutRequiredArg() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList()); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass2"), false);
    assertFalse(goodArgs);
  }

  public void testCommandLineDoesParseWithRequiredArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("-required_arg", "foo")); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass2"), false);
    assertTrue(goodArgs);
    assertEquals("foo", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass2", "RequiredArg"));
  }

  public void testStringArrayArgGrabsMultipleStrings() {
    CommandLineAccess.setRawArgs(Arrays.asList("-strings_arg", "foo", "bar", "baz"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertArrayEquals(new String[]{"foo", "bar", "baz"}, (String[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StringsArg"));
  }

  public void testStringArrayArgStopsGrabbingStringsAtNextArg() {
    CommandLineAccess.setRawArgs(Arrays.asList("-strings_arg", "foo", "bar", "baz", "-plain_str_arg", "foo1"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass1"), false);
    assertTrue(goodArgs);
    assertArrayEquals(new String[]{"foo", "bar", "baz"}, (String[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "StringsArg"));
    assertEquals("foo1", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass1", "PlainStrArg"));
  }

  public void testNoArgNeedePropertyCorrectlyGetsSetToEmptyString() {
    CommandLineAccess.setRawArgs(Arrays.asList("-no_arg_needed_str"));
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false);
    assertTrue(goodArgs);
    assertEquals("", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "NoArgNeededStr"));
  }

  public void testArgNamesAreCorrectlyPutOnScalarValues() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"));
    assertTrue(helpMsg.contains("-named_arg,--named_arg <test name>"));
  }

  public void testArgNamesAreCorrectlyPutOnArrayValues() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"));
    assertTrue(helpMsg.contains("-named_arg_array,--named_arg_array <name1 name2 name3>"));
  }

  public void testArrayArgsNullWhenNotPresent() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList());
    boolean goodArgs = CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false);
    assertTrue(goodArgs);
    assertNull("should be null", ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "NamedArgArray"));
  }

  public void testArrayArgsRequiresCorrectNumberOfArgs() {
    //no arguments should be rejected
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array"));
    assertFalse(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false));
  }

  public void testArrayArgsWithNoArgRequiredDoesNotRequiresCorrectNumberOfArgs() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array_arg_optional"));
    assertTrue(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false));
    assertArrayEquals(new Object[0], (Object[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "NamedArgArrayArgOptional"));
  }

  public void testArrayWithDefaultValuesWorksCorrectly() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Collections.<String>emptyList());
    assertTrue(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false));
    assertArrayEquals(new String[]{"a", "b", "c"}, (Object[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "ArrayArgWithDefaults"));
  }

  public void testArrayWithValuesWorksCorrectly() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array_arg_optional", "d", "e", "f"));
    assertTrue(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false));
    assertArrayEquals(new String[]{"d", "e", "f"}, (Object[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "NamedArgArrayArgOptional"));
  }

  public void testArraySeparatorWorksCorrectly() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-array_with_separator", "d,e,f"));
    assertTrue(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass3"), false));
    assertArrayEquals(new String[]{"d", "e", "f"}, (Object[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass3", "ArrayWithSeparator"));
  }

  public void testArgsAreSetProperly() {
    CommandLineAccess.setRawArgs(Arrays.asList("a", "b", "c"));
    assertTrue(CommandLineAccess.initialize(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass8"), false));
    assertArrayEquals(new String[]{"a", "b", "c"}, (Object[]) ReflectUtil.getStaticProperty("gw.lang.cli.test.ArgClass8", "OtherArgs"));
  }

  public void testBasicStringPropertyIsSetInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_str_arg", "bar"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("bar", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainStrArg"));
  }

  public void testBasicStringPropertyIsSetAsLongArgInstanceInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("--plain_str_arg", "bar"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("bar", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainStrArg"));
  }

  public void testBasicBooleanPropertyIsSetInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_bool_arg"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals(Boolean.TRUE, GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainBoolArg"));
  }

  public void testBasicBooleanPropertyIsSetAsLongArgInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("--plain_bool_arg"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals(Boolean.TRUE, GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainBoolArg"));
  }

  public void testStringWithShortArgIsSetWithShortArgInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-s", "foo"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("foo", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StrWithShortArg"));
  }

  public void testStringWithShortArgIsSetWithLongArgInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("--str_with_short_arg", "foo"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("foo", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StrWithShortArg"));
  }

/* Have no idea what this is supposed to test. Disabling....
  public void testStringWithShortArgIsNotSetWhenUsingLongNameWithShortArgPrefixInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-str_with_short_arg", "foo")); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertFalse(goodArgs);
  }
*/

  public void testDocumentationStringIsCorrectInstance() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"));
    String nl = PROPERTY_NEWLINE;
    assertEquals("bad doc string", "usage: unknown program" + nl +
            " -d,--str_with_short_arg_and_default_value <arg>   A string argument with" + nl +
            "                                                   a short name and" + nl +
            "                                                   default" + nl +
            " -plain_bool_arg,--plain_bool_arg                  A plain boolean" + nl +
            "                                                   argument" + nl +
            " -plain_str_arg,--plain_str_arg <arg>              A plain string argument" + nl +
            " -s,--str_with_short_arg <arg>                     A string argument with" + nl +
            "                                                   a short name" + nl +
            " -strings_arg,--strings_arg <arg>                  A string array argument" + nl, helpMsg);
  }

  public void testStringWithDefaultValueWorksCorrectlyInstance() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList()); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("defaultValue", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StrWithShortArgAndDefaultValue"));
  }

  public void testAllArgumentsTogetherWorksInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-plain_str_arg", "foo1", "-plain_bool_arg", "-s", "foo2", "-d", "foo3"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("foo1", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainStrArg"));
    assertEquals(true, GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainBoolArg"));
    assertEquals("foo2", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StrWithShortArg"));
    assertEquals("foo3", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StrWithShortArgAndDefaultValue"));
  }

  public void testCommandLineDoesNotParseWithoutRequiredArgInstance() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList()); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass2a.INSTANCE"), false);
    assertFalse(goodArgs);
  }

  public void testCommandLineDoesParseWithRequiredArgInstace() {
    CommandLineAccess.setRawArgs(Arrays.asList("-required_arg", "foo")); //note that there is only one preceding dash
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass2a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("foo", GosuTestUtil.eval("gw.lang.cli.test.ArgClass2a.INSTANCE.RequiredArg"));
  }

  public void testStringArrayArgGrabsMultipleStringsInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-strings_arg", "foo", "bar", "baz"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertArrayEquals(new String[]{"foo", "bar", "baz"}, (String[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StringsArg"));
  }

  public void testStringArrayArgStopsGrabbingStringsAtNextArgInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-strings_arg", "foo", "bar", "baz", "-plain_str_arg", "foo1"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertArrayEquals(new String[]{"foo", "bar", "baz"}, (String[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.StringsArg"));
    assertEquals("foo1", GosuTestUtil.eval("gw.lang.cli.test.ArgClass1a.INSTANCE.PlainStrArg"));
  }

  public void testNoArgNeedePropertyCorrectlyGetsSetToEmptyStringInstance() {
    CommandLineAccess.setRawArgs(Arrays.asList("-no_arg_needed_str"));
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertEquals("", GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.NoArgNeededStr"));
  }

  public void testArgNamesAreCorrectlyPutOnScalarValuesInstance() {
    CommandLineAccess.setRawArgs(Arrays.<String>asList());
    String helpMsg = CommandLineAccess.getHelpMessageFor(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"));
    System.out.println(helpMsg);
    assertTrue(helpMsg.contains("-named_arg,--named_arg <test name>"));
  }

  public void testArgNamesAreCorrectlyPutOnArrayValuesInstance() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"));
    System.out.println(helpMsg);
    assertTrue(helpMsg.contains("-named_arg_array,--named_arg_array <name1 name2 name3>"));
  }

  public void testArrayArgsNullWhenNotPresentInstance() {
    CommandLineAccess.setRawArgs(Collections.<String>emptyList());
    boolean goodArgs = CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false);
    assertTrue(goodArgs);
    assertNull("should be null", GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.NamedArgArray"));
  }

  public void testArrayArgsRequiresCorrectNumberOfArgsInstance() {
    //no arguments should be rejected
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array"));
    assertFalse(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false));
  }

  public void testArrayArgsWithNoArgRequiredDoesNotRequiresCorrectNumberOfArgsInstance() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array_arg_optional"));
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false));
    assertArrayEquals(new Object[0], (Object[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.NamedArgArrayArgOptional"));
  }

  public void testArrayWithDefaultValuesWorksCorrectlyInstance() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Collections.<String>emptyList());
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false));
    assertArrayEquals(new String[]{"a", "b", "c"}, (Object[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.ArrayArgWithDefaults"));
  }

  public void testArrayWithValuesWorksCorrectlyInstance() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-named_arg_array_arg_optional", "d", "e", "f"));
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false));
    assertArrayEquals(new String[]{"d", "e", "f"}, (Object[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.NamedArgArrayArgOptional"));
  }

  public void testArraySeparatorWorksCorrectlyInstance() {
    //no arguments should be accepted, because the property is marked as ArgOptional
    CommandLineAccess.setRawArgs(Arrays.asList("-array_with_separator", "d,e,f"));
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE"), false));
    assertArrayEquals(new String[]{"d", "e", "f"}, (Object[]) GosuTestUtil.eval("gw.lang.cli.test.ArgClass3a.INSTANCE.ArrayWithSeparator"));
  }

  public void testSimple() throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser("var x = 1\n" +
            "return 1");
    IExpression expression = parser.parseProgram(null);
    assertEquals(1, expression.evaluate());
  }

  public void testConversionMethod() throws ParseException {
    assertEquals(Short.parseShort("-1"), CommandLineAccess.convertValue(JavaTypes.SHORT(), "-1"));
    assertEquals(Short.parseShort("0"), CommandLineAccess.convertValue(JavaTypes.SHORT(), "0"));
    assertEquals(Short.parseShort("1"), CommandLineAccess.convertValue(JavaTypes.SHORT(), "1"));

    assertEquals(Short.parseShort("-1"), CommandLineAccess.convertValue(JavaTypes.pSHORT(), "-1"));
    assertEquals(Short.parseShort("0"), CommandLineAccess.convertValue(JavaTypes.pSHORT(), "0"));
    assertEquals(Short.parseShort("1"), CommandLineAccess.convertValue(JavaTypes.pSHORT(), "1"));

    assertEquals(Integer.parseInt("-1"), CommandLineAccess.convertValue(JavaTypes.INTEGER(), "-1"));
    assertEquals(Integer.parseInt("0"), CommandLineAccess.convertValue(JavaTypes.INTEGER(), "0"));
    assertEquals(Integer.parseInt("1"), CommandLineAccess.convertValue(JavaTypes.INTEGER(), "1"));

    assertEquals(Integer.parseInt("-1"), CommandLineAccess.convertValue(JavaTypes.pINT(), "-1"));
    assertEquals(Integer.parseInt("0"), CommandLineAccess.convertValue(JavaTypes.pINT(), "0"));
    assertEquals(Integer.parseInt("1"), CommandLineAccess.convertValue(JavaTypes.pINT(), "1"));

    assertEquals(Long.parseLong("-1"), CommandLineAccess.convertValue(JavaTypes.LONG(), "-1"));
    assertEquals(Long.parseLong("0"), CommandLineAccess.convertValue(JavaTypes.LONG(), "0"));
    assertEquals(Long.parseLong("1"), CommandLineAccess.convertValue(JavaTypes.LONG(), "1"));

    assertEquals(Long.parseLong("-1"), CommandLineAccess.convertValue(JavaTypes.pLONG(), "-1"));
    assertEquals(Long.parseLong("0"), CommandLineAccess.convertValue(JavaTypes.pLONG(), "0"));
    assertEquals(Long.parseLong("1"), CommandLineAccess.convertValue(JavaTypes.pLONG(), "1"));

    assertEquals(Float.parseFloat("-1"), CommandLineAccess.convertValue(JavaTypes.FLOAT(), "-1"));
    assertEquals(Float.parseFloat("0"), CommandLineAccess.convertValue(JavaTypes.FLOAT(), "0"));
    assertEquals(Float.parseFloat("1"), CommandLineAccess.convertValue(JavaTypes.FLOAT(), "1"));

    assertEquals(Float.parseFloat("-1"), CommandLineAccess.convertValue(JavaTypes.pFLOAT(), "-1"));
    assertEquals(Float.parseFloat("0"), CommandLineAccess.convertValue(JavaTypes.pFLOAT(), "0"));
    assertEquals(Float.parseFloat("1"), CommandLineAccess.convertValue(JavaTypes.pFLOAT(), "1"));

    assertEquals(Double.parseDouble("-1"), CommandLineAccess.convertValue(JavaTypes.DOUBLE(), "-1"));
    assertEquals(Double.parseDouble("0"), CommandLineAccess.convertValue(JavaTypes.DOUBLE(), "0"));
    assertEquals(Double.parseDouble("1"), CommandLineAccess.convertValue(JavaTypes.DOUBLE(), "1"));

    assertEquals(Double.parseDouble("-1"), CommandLineAccess.convertValue(JavaTypes.pDOUBLE(), "-1"));
    assertEquals(Double.parseDouble("0"), CommandLineAccess.convertValue(JavaTypes.pDOUBLE(), "0"));
    assertEquals(Double.parseDouble("1"), CommandLineAccess.convertValue(JavaTypes.pDOUBLE(), "1"));
  }

  public void testClassWithConflictingStaticMemberVariableAndProperty() {
    CommandLineAccess.setRawArgs(Arrays.asList("-t", "Foo", "-p", "bar"));
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass4"), false));
    assertEquals("Foo", GosuTestUtil.eval("gw.lang.cli.test.ArgClass4.TaskClassName"));
    assertEquals("bar", GosuTestUtil.eval("gw.lang.cli.test.ArgClass4.PropertyFileName"));
  }

  public void testPublicFinalVariablesAreNotNukedByCommandLineAccess() {
    CommandLineAccess.setRawArgs(Arrays.<String>asList());
    assertTrue(CommandLineAccess.initialize(GosuTestUtil.eval("gw.lang.cli.test.ArgClass5"), false));
    assertEquals("Constant", GosuTestUtil.eval("gw.lang.cli.test.ArgClass5.SAMPLE_CONSTANT"));
  }

  @gw.testharness.Disabled(assignee = "dpetrusca", reason = "Conflict between the properties and Gosu Type loaders")
  public void testDeriveDescription() {
    CommandLineAccess.setRawArgs(Arrays.<String>asList());
    IType argClass6 = TypeSystem.getByFullName("gw.lang.cli.test.ArgClass6");
    IPropertyInfo testProperty1 = argClass6.getTypeInfo().getProperty("TestProperty1");
    assertEquals("TestProperty1 from props", CommandLineAccess.deriveDescription(testProperty1));
    IPropertyInfo testProperty2 = argClass6.getTypeInfo().getProperty("TestProperty2");
    assertEquals("TestProperty2 from comment", CommandLineAccess.deriveDescription(testProperty2));
  }

  public void testDocumentationStringDoesNotContainHiddenOptions() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(TypeSystem.getByFullName("gw.lang.cli.test.ArgClass7"));
    String nl = PROPERTY_NEWLINE;
    assertEquals("usage: unknown program" + nl +
            " -foo_arg,--foo_arg <arg>   A regular argument" + nl, helpMsg);
  }

  public void testDocumentationStringDoesNotContainHiddenOptionsInstance() {
    String helpMsg = CommandLineAccess.getHelpMessageFor(GosuTestUtil.eval("gw.lang.cli.test.ArgClass7a.INSTANCE"));
    String nl = PROPERTY_NEWLINE;
    assertEquals("hidden options", "usage: unknown program" + nl +
            " -foo_arg,--foo_arg <arg>   A regular argument" + nl, helpMsg);
  }
}