package gw.lang.reflect.json;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest {
  //@Test
  public void readBigFile() throws URISyntaxException, IOException {
    URL url = getResource("citylots.json");
    String bigJson = new String(Files.readAllBytes(Paths.get(url.toURI())), StandardCharsets.UTF_8);
    long times = 10;
    long before = System.currentTimeMillis();
    int errors = 0;
    for(int i = 0; i < times; i++) {
      Tokenizer tokenizer = new Tokenizer(new StringReader(bigJson));
      SimpleParserImpl p = new SimpleParserImpl(tokenizer, false);
      p.parse();
      errors = p.getErrors().size();
    }
    long after = System.currentTimeMillis();
    System.out.println("readBigFile Time: " + ((after - before) / (double) times) + " ms");
    assertEquals(0, errors);
  }

  //@Test
  public void readValueFromBigFile() throws FileNotFoundException {
    URL url = getResource("citylots.json");
    long times = 10;
    long before = System.currentTimeMillis();
    int errors = 0;
    for(int i = 0; i < times; i++) {
      Tokenizer tokenizer = new Tokenizer(new BufferedReader(new FileReader(url.getPath())));
      SimpleParserImpl p = new SimpleParserImpl(tokenizer, false);
      p.advance();
      p.skipMember();
      for(int k = 0; k < 4; k++) {
        p.advance();
      }
      for(int k = 0; k < 206559; k++) {
        p.skipValue();
        p.advance();
      }
      p.advance();
      p.skipMember();
      p.advance();
      HashMap val = new HashMap();
      p.parseMember(val);
      assertEquals(((Map) val.get("properties")).get("MAPBLKLOT"), "VACSTWIL");
      assertEquals(0, p.getErrors().size());
    }
    long after = System.currentTimeMillis();
    System.out.println("readValueFromBigFile Time: " + ((after - before) / (double) times) + " ms");
    assertEquals(0, errors);
  }

  @Test
  public void testGitHub() {
    SimpleParserImpl parser = makeParser("github.json");
    Object o = parser.parse();
    assertEquals(0, parser.getErrors().size());
  }

  @Test
  public void testYahoo() {
    SimpleParserImpl parser = makeParser("yahoo.json");
    Object o = parser.parse();
    assertEquals(0, parser.getErrors().size());
  }

  @Test
  public void testSample() {
    String sample = "{\n" +
      "  \"firstName\": \"John\",\n" +
      "  \"lastName\": \"Smith\",\n" +
      "  \"age\": 25,\n" +
      "  \"address\": {\n" +
      "    \"streetAddress\": \"21 2nd Street\",\n" +
      "    \"city\": \"New York\",\n" +
      "    \"state\": \"NY\",\n" +
      "    \"postalCode\": \"10021\"\n" +
      "  },\n" +
      "  \"etc\": [true, false, null, 3.14, [\"a\", 8]]\n" +
      "}";
    HashMap expected = new HashMap();
    expected.put("firstName", "John");
    expected.put("lastName", "Smith");
    expected.put("age", 25);

    HashMap address = new HashMap();
    address.put("streetAddress", "21 2nd Street");
    address.put("city", "New York");
    address.put("state", "NY");
    address.put("postalCode", "10021");

    expected.put("address", address);
    expected.put("etc", Arrays.asList(true, false, null, 3.14, Arrays.asList("a", 8)));
    Tokenizer tokenizer = new Tokenizer(new StringReader(sample));
    SimpleParserImpl p = new SimpleParserImpl(tokenizer, false);
    Object val = p.parse();
    assertEquals(0, p.getErrors().size());
    assertEquals(expected, val);
  }

  @Test
  public void testSampleStreaming() {
    String sample = "{\n" +
      "  \"firstName\": \"John\",\n" +
      "  \"lastName\": \"Smith\",\n" +
      "  \"age\": 25,\n" +
      "  \"address\": {\n" +
      "    \"streetAddress\": \"21 2nd Street\",\n" +
      "    \"city\": \"New York\",\n" +
      "    \"state\": \"NY\",\n" +
      "    \"postalCode\": \"10021\"\n" +
      "  },\n" +
      "  \"etc\": [true, false, null, 3.14, [\"a\", 8]]\n" +
      "}";
    Tokenizer tokenizer = new Tokenizer(new StringReader(sample));
    SimpleParserImpl p = new SimpleParserImpl(tokenizer, false);
    assertEquals(TokenType.LCURLY, p.currentToken().getType());
    p.advance();
    while(!p.currentToken().getString().equals("etc")) { p.skipMember(); p.advance(); }
    p.advance();
    assertEquals(TokenType.COLON, p.currentToken().getType());
    p.advance();
    assertEquals(TokenType.LSQUARE, p.currentToken().getType());
    p.advance();
    for(int i = 0; i < 3; i++) { p.skipValue(); p.advance(); }
    assertEquals(0, p.getErrors().size());
    assertEquals(3.14, (double)p.parseValue(), 0.001);
  }

  @Test
  public void testSampleErr() {
    String sample = "{\n" +
      "  \"firstName\": \"John\",\n" +
      "  \"lastName\": \"Smith\",\n" +
      "  \"age\": 25,\n" +
      "  \"address\": {\n" +
      "    \"streetAddress\" BUG \"21 2nd Street\",\n" +
      "    \"city\": New York,\n" +
      "    \"state\": \"NY\",\n" +
      "    \"postalCode\": \"10021\"\n" +
      "  },\n" +
      "  \"etc\": [true, false, null, 3.14, [\"a\", 8]]\n" +
      "}";
    HashMap expected = new HashMap();
    expected.put("firstName", "John");
    expected.put("lastName", "Smith");
    expected.put("age", 25);
    HashMap address = new HashMap();
    address.put("streetAddress", "21 2nd Street");
    address.put("city", null);
    expected.put("address", address);
    expected.put("etc", Arrays.asList(true, false, null, 3.14, Arrays.asList("a", 8)));
    Tokenizer tokenizer = new Tokenizer(new StringReader(sample));
    SimpleParserImpl p = new SimpleParserImpl(tokenizer, false);
    Object val = p.parse();
    assertEquals("[[6:21] expecting ':', found 'BUG', [7:13] Unexpected token 'New', [7:17] expecting '}', found 'York']", p.getErrors().toString());
    assertEquals(expected, val);
  }

  @Test
  public void testParseObject() {
    // empty
    assertEquals(map(), parse("{}"));

    // simple single
    assertEquals(map("foo", "bar"), parse("{\"foo\":\"bar\"}"));
    assertEquals(map("foo", 1), parse("{\"foo\":1}"));
    assertEquals(map("foo", 1.1), parse("{\"foo\":1.1}"));
    assertEquals(map("foo", true), parse("{\"foo\":true}"));
    assertEquals(map("foo", false), parse("{\"foo\":false}"));
    assertEquals(map("foo", null), parse("{\"foo\":null}"));
    assertEquals(map("foo", list()), parse("{\"foo\":[]}"));

    // complex single
    assertEquals(map("foo", map("foo", "bar")), parse("{\"foo\" : {\"foo\":\"bar\"}}"));
    assertEquals(map("foo", list("foo", "bar")), parse("{\"foo\" : [\"foo\", \"bar\"}]"));

    // simple multi
    assertEquals(map("foo", "bar", "doh", "rey"), parse("{\"foo\":\"bar\", \"doh\":\"rey\"}"));
    assertEquals(map("foo", "rey"), parse("{\"foo\":\"bar\", \"foo\":\"rey\"}"));
  }


  @Test
  public void testParseArray() {
    assertEquals(list(), parse("[]"));
    assertEquals(list("foo"), parse("[\"foo\"]"));
    assertEquals(list("foo", "bar"), parse("[\"foo\", \"bar\"]"));
    assertEquals(list("string", 1, 1.1, map("foo", "bar"), list("doh"), true, false, null),
      parse("[\"string\", 1, 1.1, {\"foo\" : \"bar\"}, [\"doh\"], true, false, null]"));
  }

  @Test
  public void testParseLiterals() {
    // strings
    assertEquals("", parse("\"\""));
    assertEquals("foo", parse("\"foo\""));
    assertEquals("foo\"bar", parse("\"foo\\\"bar\""));

    // numbers
    assertEquals(0, parse("0"));
    assertEquals(1, parse("1"));
    assertEquals(123, parse("123"));
    assertEquals(123456789, (int) parse("123456789"));
    assertEquals(12345678999L, (long) parse("12345678999"));
    assertEquals(0, parse("12345678954654654654656545"));
    assertEquals(new BigInteger("12345678954654654654656545"), (BigInteger) parseBig("12345678954654654654656545"));
    assertEquals(-1, parse("-1"));
    assertEquals(-0, parse("-0"));
    assertEquals(-123456789, parse("-123456789"));
    assertEquals(1.1, parse("1.1"));
    assertEquals(123456789.1, parse("123456789.1"));
    assertEquals(123456.123456, parse("123456.123456"));
    assertEquals(-1.1, parse("-1.1"));
    assertEquals(-123456789.1, parse("-123456789.1"));
    assertEquals(-123456.123456, (double) parse("-123456.123456"), 0.01);
    assertEquals(new BigDecimal("-123456.123456"), (BigDecimal) parseBig("-123456.123456"));
    assertEquals(1e1, parse("1e1"));
    assertEquals(123456789e1, parse("123456789e1"));
    assertEquals(1e+1, parse("1e+1"));
    assertEquals(1e+1, parse("1e+1"));
    assertEquals(1e-1, parse("1e-1"));
    assertEquals(1E1, parse("1E1"));
    assertEquals(1E+1, parse("1E+1"));
    assertEquals(1E-1, parse("1E-1"));
    assertEquals(-0.1E4, parse("-0.1E4"));

    // literals
    assertEquals(true, parse("true"));
    assertEquals(false, parse("false"));
    assertEquals(null, parse("null"));
  }


  @Test
  public void testErrors() {

    // bad punctuation
    assertTrue(hasErrors("{"));
    assertTrue(hasErrors("}"));
    assertTrue(hasErrors("}{"));
    assertTrue(hasErrors("["));
    assertTrue(hasErrors("]"));
    assertTrue(hasErrors("]["));
    assertTrue(hasErrors(","));
    assertTrue(hasErrors(":"));

    // bad strings
    assertTrue(hasErrors("\""));
    assertTrue(hasErrors("\"foo"));

    // bad numbers
    assertTrue(hasErrors(".1"));
    assertTrue(hasErrors("1..23"));
    assertTrue(hasErrors("-.1"));
    assertTrue(hasErrors("e1"));
    assertTrue(hasErrors("e+1"));
    assertTrue(hasErrors("e-1"));
    assertTrue(hasErrors("E1"));
    assertTrue(hasErrors("E+1"));
    assertTrue(hasErrors("E-1"));
    assertTrue(hasErrors("1E.1"));
    assertTrue(hasErrors("[01.3]"));
    assertTrue(hasErrors("[3E-4.0]"));
    assertTrue(hasErrors("[3E-4a]"));
    assertTrue(hasErrors("[3.4a]"));
    assertTrue(hasErrors("[1.4ea]"));

    // bad objects
    assertTrue(hasErrors("{\"foo\"}"));
    assertTrue(hasErrors("{\"foo\":}"));
    assertTrue(hasErrors("{\"foo\": badToken}"));

    // bad arrays
    assertTrue(hasErrors("[1"));
    assertTrue(hasErrors("[1,"));
    assertTrue(hasErrors("[1,]"));
    assertTrue(hasErrors("[1, badToken]"));
    assertTrue(hasErrors("[1, [badToken]]"));

    // bad literals
    assertTrue(hasErrors("badToken"));
    assertTrue(hasErrors("True"));
    assertTrue(hasErrors("nil"));
  }

  private boolean hasErrors(String src) {
    SimpleParserImpl parser = new SimpleParserImpl(new Tokenizer(new StringReader(src)), false);
    parser.parse();
    return parser.getErrors().size() != 0;
  }

  private Object parse(String src) {
    return new SimpleParserImpl(new Tokenizer(new StringReader(src)), false).parse();
  }

  private Object parseBig(String src) {
    return new SimpleParserImpl(new Tokenizer(new StringReader(src)), true).parse();
  }


  private List list(Object... listVals) {
    return Arrays.asList(listVals);
  }

  private HashMap map(Object... mapVals) {
    HashMap m = new HashMap();
    Iterator it = Arrays.asList(mapVals).iterator();
    while(it.hasNext()) {
      m.put(it.next(), it.next());
    }
    return m;
  }

  URL getResource(String res) {
    URL url = getClass().getClassLoader().getResource(res);
    if(url == null) {
      fail("Unable to load '" + res + "'");
    }
    return url;
  }

  SimpleParserImpl makeParser(String res) {
    SimpleParserImpl p = null;
    try {
      Tokenizer tokenizer = new Tokenizer(new BufferedReader(new FileReader(getResource(res).getPath())));
      p = new SimpleParserImpl(tokenizer, false);
    } catch (FileNotFoundException e) {
      fail("Unable to create a parser for '" + res + "'");
    }
    return p;
  }
}
