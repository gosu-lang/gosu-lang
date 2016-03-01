package gw.internal.gosu.regression;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class PL33421Test extends TestClass
{
  public void testToStringWorksOnParseTreeWithBadArrayAccess()
  {
    ParseResultsException pre = GosuTestUtil.getParseResultsException( "var arr : Object[]\n" +
                                                                       "arr[].someMethod()" );

    IParsedElement elt = pre.getParsedElement();
    assertNotNull( elt.toString() );
  }

}
