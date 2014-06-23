/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import gw.lang.GosuShop;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.Keyword;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;

public class PackageNameExtractor {
  @Nullable
  public static String getPackageNameFromSource(String text) {
    ISourceCodeTokenizer tokenizer = GosuShop.createSourceCodeTokenizer(new StringReader(text));
    do {
      tokenizer.nextToken();
      if (match(tokenizer, Keyword.KW_package)) {
        return parseDotPathWord(tokenizer);
      }
    } while (!tokenizer.isEOF());
    return null;
  }

  private static String parseDotPathWord(@NotNull ISourceCodeTokenizer tokenizer) {
    StringBuilder sb = new StringBuilder(tokenizer.getStringValue());
    if (match(tokenizer, null, ISourceCodeTokenizer.TT_WORD, false)) {
      while (match(tokenizer, null, '.', false)) {
        sb.append('.');
        sb.append(tokenizer.getStringValue());
        match(tokenizer, null, ISourceCodeTokenizer.TT_WORD, false);
      }
    }
    return sb.toString();
  }


  private static boolean match(@NotNull ISourceCodeTokenizer tokenizer, @NotNull Keyword token) {
    return match(tokenizer, token, false);
  }

  private static boolean match(@NotNull ISourceCodeTokenizer tokenizer, @NotNull Keyword token, boolean bPeek) {
    boolean bMatch = false;

    if (ISourceCodeTokenizer.TT_KEYWORD == tokenizer.getType()) {
      bMatch = token.toString().equalsIgnoreCase(tokenizer.getStringValue());
    }

    if (bMatch && !bPeek) {
      tokenizer.nextToken();
    }

    return bMatch;
  }

  private static boolean match(@NotNull ISourceCodeTokenizer tokenizer, @Nullable String token, int iType, boolean bPeek) {
    boolean bMatch = false;

    if (token != null) {
      if ((iType == tokenizer.getType()) || ((iType == 0) && (tokenizer.getType() == ISourceCodeTokenizer.TT_WORD))) {
        bMatch = token.equalsIgnoreCase(tokenizer.getStringValue());
      }
    } else {
      bMatch = (tokenizer.getType() == iType) || isValueKeyword(tokenizer, iType);
    }

    if (bMatch && !bPeek) {
      tokenizer.nextToken();
    }

    return bMatch;
  }

  private static boolean isValueKeyword(@NotNull ISourceCodeTokenizer tokenizer, int iType) {
    return iType == ISourceCodeTokenizer.TT_WORD &&
        tokenizer.getType() == ISourceCodeTokenizer.TT_KEYWORD &&
        Keyword.isValueKeyword( tokenizer.getStringValue() );
  }
}
