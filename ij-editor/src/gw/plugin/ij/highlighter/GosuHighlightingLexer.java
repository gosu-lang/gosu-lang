/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.pom.java.LanguageLevel;
import gw.plugin.ij.lang.GosuLexer;

public class GosuHighlightingLexer extends LayeredLexer {
  public GosuHighlightingLexer(LanguageLevel languageLevel) {
    super(new GosuLexer());
//    registerSelfStoppingLayer( new StringLiteralLexer( '\"', JavaTokenType.STRING_LITERAL ),
//                               new IElementType[]{JavaTokenType.STRING_LITERAL}, IElementType.EMPTY_ARRAY );
//
//    registerSelfStoppingLayer( new StringLiteralLexer( '\'', JavaTokenType.STRING_LITERAL ),
//                               new IElementType[]{JavaTokenType.CHARACTER_LITERAL}, IElementType.EMPTY_ARRAY );
//

//    LayeredLexer docLexer = new LayeredLexer( new JavaDocLexer( languageLevel.hasEnumKeywordAndAutoboxing() ) );
//
//    HtmlHighlightingLexer lexer = new HtmlHighlightingLexer();
//    lexer.setHasNoEmbeddments( true );
//    docLexer.registerLayer( lexer,
//                            new IElementType[]{JavaDocTokenType.DOC_COMMENT_DATA} );
//
//    registerSelfStoppingLayer( docLexer,
//                               new IElementType[]{JavaTokenType.DOC_COMMENT},
//                               new IElementType[]{JavaDocTokenType.DOC_COMMENT_END} );
  }
}
