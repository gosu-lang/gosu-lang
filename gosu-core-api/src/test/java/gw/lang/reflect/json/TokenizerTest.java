package gw.lang.reflect.json;

import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TokenizerTest {
  @Test
  public void bootstrapTest() {
    //basic constant
    List<Token> tokens = tokenize("true");
    assertTokensAre(tokens, token(TokenType.TRUE, "true"));

    // leading whitespace
    tokens = tokenize("   true");
    assertTokensAre(tokens, token(TokenType.TRUE, "true"));

    // trailing whitespace
    tokens = tokenize("true   ");
    assertTokensAre(tokens, token(TokenType.TRUE, "true"));

    // two tokens whitespace
    tokens = tokenize("true   false");
    assertTokensAre(tokens, token(TokenType.TRUE, "true"), token(TokenType.FALSE, "false"));
  }

  @Test
  public void testPunctuation() {
    List<Token> tokens;

    //braces
    tokens = tokenize("[]");
    assertTokensAre(tokens, token(TokenType.LSQUARE, "["), token(TokenType.RSQUARE, "]"));

    //single curly brace
    tokens = tokenize("{");
    assertTokensAre(tokens, token(TokenType.LCURLY, "{"));

    //curly braces with white space
    tokens = tokenize("{   }");
    assertTokensAre(tokens, token(TokenType.LCURLY, "{"), token(TokenType.RCURLY, "}"));

    //comma
    tokens = tokenize(",");
    assertTokensAre(tokens, token(TokenType.COMMA, ","));

    //colon
    tokens = tokenize(":");
    assertTokensAre(tokens, token(TokenType.COLON, ":"));


    //tests for PUNCTUATION
    tokens = tokenize("[");
    assertTokensAre(tokens, token(TokenType.LSQUARE, "["));

    tokens = tokenize("]");
    assertTokensAre(tokens, token(TokenType.RSQUARE, "]"));

    tokens = tokenize("{");
    assertTokensAre(tokens, token(TokenType.LCURLY, "{"));

    tokens = tokenize("}");
    assertTokensAre(tokens, token(TokenType.RCURLY, "}"));

    tokens = tokenize(":");
    assertTokensAre(tokens, token(TokenType.COLON, ":"));

    tokens = tokenize(",");
    assertTokensAre(tokens, token(TokenType.COMMA, ","));
  }

  @Test
  public void testStrings() {
    List<Token> tokens;

    //Test basic string
    tokens = tokenize("\"test\"");
    assertTokensAre(tokens, token(TokenType.STRING, "test"));

    tokens = tokenize("'test'");
    assertTokensAre(tokens, token(TokenType.STRING, "test"));

    //string not ending in quote
    tokens = tokenize("\"test");
    assertTokensAre(tokens, token(TokenType.ERROR, "test"));

    tokens = tokenize("'test\"");
    assertTokensAre(tokens, token(TokenType.ERROR, "test\""));

    //string not beginning in quote
    tokens = tokenize("test\"");
    assertTokensAre(tokens, token(TokenType.ERROR, "test"), token(TokenType.ERROR, ""));

    //escaped quote in string
    tokens = tokenize('"' + backSlash('"') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\""));

    //escaped backslash in string
    tokens = tokenize('"' + backSlash('\\') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\\"));

    //escaped frontslash in string
    tokens = tokenize('"' + backSlash('/') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "/"));

    //escaped backspace in string
    tokens = tokenize('"' + backSlash('b') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\b"));

    //escaped form feed in string
    tokens = tokenize('"' + backSlash('f') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\f"));

    //escaped new line in string
    tokens = tokenize('"' + backSlash('n') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\n"));

    //escaped carriage return in string
    tokens = tokenize('"' + backSlash('r') + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\r"));

    //Escaped unicode
    tokens = tokenize('"' + backSlash("u263A") + '"');
    assertTokensAre(tokens, token(TokenType.STRING, "\u263A"));
    assertTokensAre(tokens, token(TokenType.STRING, "☺"));

    tokens = tokenize('"' + backSlash("u263G") + "ops");
    assertTokensAre(tokens, token(TokenType.ERROR, ""), token(TokenType.ERROR, "ops"));

  }

  @Test
  public void testNumbers() {
    List<Token> tokens;

    //number by itself
    tokens = tokenize("1234.0");
    assertTokensAreNumbers(tokens, doubleNumberToken("1234.0"));

    //multiple numbers
    tokens = tokenize("1 2 3");
    assertTokensAreNumbers(tokens, integerNumberToken("1"), integerNumberToken("2"), integerNumberToken("3"));

    //decimal number
    tokens = tokenize("1.23");
    assertTokensAreNumbers(tokens, doubleNumberToken("1.23"));

    //invalid decimal number
    tokens = tokenize("1..23");
    assertTokensAre(tokens, token(TokenType.ERROR, "1."), token(TokenType.ERROR, "."), token(TokenType.INTEGER, "23"));

    //decimal number
    tokens = tokenize(".23");
    assertTokensAre(tokens, token(TokenType.ERROR, "."), token(TokenType.INTEGER, "23"));

    //multiple fractions
    tokens = tokenize("0.23 1.56");
    assertTokensAre(tokens, doubleNumberToken("0.23"), doubleNumberToken("1.56"));

    tokens = tokenize("01.3");
    assertTokensAreNumbers(tokens, integerNumberToken("0"), doubleNumberToken("1.3"));

    //exponents lowercase e
    tokens = tokenize("2e1");
    assertTokensAreNumbers(tokens, doubleNumberToken("2e1"));

    //exponents uppercase e
    tokens = tokenize("3E4");
    assertTokensAreNumbers(tokens, doubleNumberToken("3E4"));

    //negative number
    tokens = tokenize("-2");
    assertTokensAreNumbers(tokens, integerNumberToken("-2"));

    //negative decimal
    tokens = tokenize("-4.2");
    assertTokensAreNumbers(tokens, doubleNumberToken("-4.2"));

    //negative exp
    tokens = tokenize("-3E+4");
    assertTokensAreNumbers(tokens, doubleNumberToken("-3E+4"));

    //negative exp
    tokens = tokenize("2E-4");
    assertTokensAreNumbers(tokens, doubleNumberToken("2E-4"));

    tokens = tokenize("3E-4");
    assertTokensAreNumbers(tokens, doubleNumberToken("3E-4"));

    tokens = tokenize("-0.1E4");
    assertTokensAreNumbers(tokens, doubleNumberToken("-0.1E4"));

    //double negative exp
    tokens = tokenize("-2E-4");
    assertTokensAreNumbers(tokens, doubleNumberToken("-2E-4"));

    //double negative exp two
    tokens = tokenize("-2E-4 -5e1");
    assertTokensAreNumbers(tokens, doubleNumberToken("-2E-4"), doubleNumberToken("-5e1"));

    //exp and neg num
    tokens = tokenize("3E4,");
    assertTokensAre(tokens, token(TokenType.DOUBLE, "3E4"), token(TokenType.COMMA, ","));

    //invalid input exp and decimal
    tokens = tokenize("3E-4.0");
    assertTokensAre(tokens, token(TokenType.DOUBLE, "3E-4"), token(TokenType.ERROR, "."), token(TokenType.INTEGER, "0"));

    //invalid input exp
    tokens = tokenize("3E-4a");
    assertTokensAre(tokens, token(TokenType.DOUBLE, "3E-4"), token(TokenType.ERROR, "a"));

    //invalid input decimal
    tokens = tokenize("3.4a");
    assertTokensAre(tokens, token(TokenType.DOUBLE, "3.4"), token(TokenType.ERROR, "a"));

    tokens = tokenize("1.4ea");
    assertTokensAre(tokens, token(TokenType.ERROR, "1.4e"), token(TokenType.ERROR, "a"));

  }

  private Token doubleNumberToken(String value) {
    return new Token(TokenType.DOUBLE, value, 0, 0);
  }

  private Token integerNumberToken(String value) {
    return new Token(TokenType.INTEGER, value, 0, 0);
  }

  @Test
  public void testLiterals() {
    List<Token> tokens;

    tokens = tokenize("true");
    assertTokensAre(tokens, token(TokenType.TRUE, "true"));

    tokens = tokenize("false");
    assertTokensAre(tokens, token(TokenType.FALSE, "false"));

    tokens = tokenize("null");
    assertTokensAre(tokens, token(TokenType.NULL, "null"));

  }

  @Test
  public void testCombos() {
    List<Token> tokens;

    //assortment
    tokens = tokenize("\"type\": \"array\":");
    assertTokensAre(tokens, token(TokenType.STRING, "type"), token(TokenType.COLON, ":"), token(TokenType.STRING, "array"), token(TokenType.COLON, ":"));


    tokens = tokenize("123, true");
    assertTokensAre(tokens, token(TokenType.INTEGER, "123"), token(TokenType.COMMA, ","), token(TokenType.TRUE, "true"));

  }

  @Test
  public void testErrors() {
    List<Token> tokens;

    // unclosed string
    tokens = tokenize("\"foo");
    assertTokensAre(tokens, token(TokenType.ERROR, "foo"));

    // invalid constant (constant with typo)
    tokens = tokenize("truel");
    assertTokensAre(tokens, token(TokenType.ERROR, "truel"));

    // invalid constant (constant with typo)
    tokens = tokenize("truel false");
    assertTokensAre(tokens, token(TokenType.ERROR, "truel"), token(TokenType.FALSE, "false"));

    tokens = tokenize("BUG {");
    assertTokensAre(tokens, token(TokenType.ERROR, "BUG"), token(TokenType.LCURLY, "{"));
  }


  //========================================================================================
  // Test Helpers
  //========================================================================================

  private void assertTokensAre(List<Token> tokens, Token... matches) {
    if(tokens.size() != matches.length) {
      fail("Did not find " + matches.length + " tokens: " + tokens);
    }
    for(int i = 0; i < matches.length; i++) {
      assertTokenMatches(matches[i], tokens.get(i));
    }
  }

  private void assertTokensAreNumbers(List<Token> tokens, Token... matches) {
    if(tokens.size() != matches.length) {
      fail("Did not find " + matches.length + " tokens: " + tokens);
    }
    for(int i = 0; i < matches.length; i++) {
      assertEquals(matches[i].getType(), tokens.get(i).getType());
      assertEquals(matches[i].getString(), tokens.get(i).getString());
    }
  }

  private void assertTokenMatches(Token match, Token token) {
    assertEquals(match.getType(), token.getType());
    if(match.getString() != null) {
      assertEquals(match.getString(), token.getString());
    }
  }

  private Token token(TokenType type, String value) {
    return new Token(type, value, 0, 0);
  }

  private List<Token> tokenize(String str) {
    Tokenizer tokenizer = new Tokenizer(new StringReader(str));
    ArrayList<Token> list = new ArrayList<Token>();
    Token token = tokenizer.next();
    while(token.getType() != TokenType.EOF) {
      list.add(token);
      token = tokenizer.next();
    }
    return list;
  }

  private String backSlash(char s) {
    return "\\" + s;
  }

  private String backSlash(String s) {
    return "\\" + s;
  }

}
