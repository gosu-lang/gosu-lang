package gosujavactest.extensions.java.lang.String;

import java.math.BigDecimal;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class MyStringExt
{
  public static BigDecimal bd( @This String thiz ) {
    return new BigDecimal( thiz );
  }
}
