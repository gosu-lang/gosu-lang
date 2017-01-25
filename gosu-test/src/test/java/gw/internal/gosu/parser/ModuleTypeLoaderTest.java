package gw.internal.gosu.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModuleTypeLoaderTest {
  
  @Test
  public void stripArrayBrackets() throws Exception {
    assertEquals("entity.Coverage", ModuleTypeLoader.stripArrayBrackets("entity.Coverage"));
    assertEquals("entity.Coverage", ModuleTypeLoader.stripArrayBrackets("entity.Coverage[]"));
    assertEquals("entity.Coverage", ModuleTypeLoader.stripArrayBrackets("entity.Coverage[][]"));
    assertEquals("entity.Coverage", ModuleTypeLoader.stripArrayBrackets("entity.Coverage[][][]"));
  }

}