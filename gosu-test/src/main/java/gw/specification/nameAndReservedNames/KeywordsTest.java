package gw.specification.nameAndReservedNames;

import gw.lang.parser.Keyword;
import gw.test.TestClass;

import java.util.HashMap;
import java.util.Set;

public class KeywordsTest extends TestClass {

  public void testReservedAndValueWords()
  {
    HashMap<String, Boolean> kw = new HashMap<String, Boolean>();
    kw.put("abstract", true);
    kw.put("and", false);
    kw.put("application", true);
    kw.put("as", true);
    kw.put("assert", false);
    kw.put("block", true);
    kw.put("break", false);
    kw.put("case", false);
    kw.put("catch", false);
    kw.put("class", false);
    kw.put("classpath", true);
    kw.put("construct", false);
    kw.put("contains", true);
    kw.put("continue", false);
    kw.put("default", false);
    kw.put("delegate", false);
    kw.put("do", false);
    kw.put("else", false);
    kw.put("enhancement", true);
    kw.put("enum", false);
    kw.put("eval", false);
    kw.put("except", true);
    kw.put("execution", true);
    kw.put("exists", true);
    kw.put("extends", false);
    kw.put("false", true);
    kw.put("final", true);
    kw.put("finally", false);
    kw.put("find", true);
    kw.put("for", false);
    kw.put("foreach", false);
    kw.put("function", false);
    kw.put("get", true);
    kw.put("hide", true);
    kw.put("if", false);
    kw.put("implements", false);
    kw.put("in", false);
    kw.put("index", true);
    kw.put("Infinity", true);
    kw.put("interface", false);
    kw.put("annotation", false);
    kw.put("internal", true);
    kw.put("iterator", true);
    kw.put("length", true);
    kw.put("NaN", true);
    kw.put("new", false);
    kw.put("not", false);
    kw.put("null", true);
    kw.put("or", false);
    kw.put("outer", true);
    kw.put("override", false);
    kw.put("package", false);
    kw.put("print", true);
    kw.put("private", true);
    kw.put("property", false);
    kw.put("protected", true);
    kw.put("public", true);
    kw.put("readonly", true);
    kw.put("represents", false);
    kw.put("request", true);
    kw.put("return", false);
    kw.put("session", true);
    kw.put("set", true);
    kw.put("startswith", true);
    kw.put("static", true);
    kw.put("statictypeof", false);
    kw.put("structure", false);
    kw.put("super", false);
    kw.put("switch", false);
    kw.put("this", true);
    kw.put("throw", false);
    kw.put("transient", false);
    kw.put("try", false);
    kw.put("typeas", false);
    kw.put("typeis", false);
    kw.put("typeloader", true);
    kw.put("typeof", false);
    kw.put("unless", false);
    kw.put("uses", false);
    kw.put("using", false);
    kw.put("var", false);
    kw.put("void", false);
    kw.put("boolean", false);
    kw.put("char", false);
    kw.put("byte", false);
    kw.put("short", false);
    kw.put("int", false);
    kw.put("long", false);
    kw.put("float", false);
    kw.put("double", false);
    kw.put("where", true);
    kw.put("while", false);
    kw.put("true", true);

    final Set<String> all = Keyword.getAll();
    assertEquals(all.size(), kw.size());
    for(String k : kw.keySet()) {
      if(kw.get(k)) {
        Keyword.isValueKeyword(k);
      } else {
        Keyword.isReservedKeyword(k);
      }
    }
  }

}
