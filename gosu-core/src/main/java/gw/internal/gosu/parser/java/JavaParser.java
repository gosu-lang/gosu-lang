/*
 * Copyright 2013 Guidewire Software, Inc.
 */

// $ANTLR 3.4 Java.g 2012-10-01 17:36:29

package gw.internal.gosu.parser.java;


import gw.internal.ext.org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created
 *          elementValuePair and elementValuePairs rules, then used them in the
 *          annotation rule.  Allows it to recognize annotation references with
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which
 *          has the Identifier portion in it, the parser would fail on constants in
 *          annotation definitions because it expected two identifiers.
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *         
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and
 *          normalInterfaceDeclaration rather than classDeclaration and
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation,
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java
 *      letter-or-digit is a character for which the method
 *      Character.isJavaIdentifierPart(int) returns true."
 */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ABSTRACT", "AMP", "AMPAMP", "AMPEQ", "ASSERT", "BANG", "BANGEQ", "BAR", "BARBAR", "BAREQ", "BINLITERAL", "BOOLEAN", "BREAK", "BYTE", "BinPrefix", "CARET", "CARETEQ", "CASE", "CATCH", "CHAR", "CHARLITERAL", "CLASS", "COLON", "COMMA", "COMMENT", "CONST", "CONTINUE", "DEFAULT", "DO", "DOT", "DOUBLE", "DOUBLELITERAL", "DoubleSuffix", "ELLIPSIS", "ELSE", "ENUM", "EQ", "EQEQ", "EXTENDS", "EscapeSequence", "Exponent", "FALSE", "FINAL", "FINALLY", "FLOAT", "FLOATLITERAL", "FOR", "FloatSuffix", "GOTO", "GT", "HexDigit", "HexNumber", "HexPrefix", "IDENTIFIER", "IF", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "INTLITERAL", "IdentifierPart", "IdentifierStart", "IntegerNumber", "LBRACE", "LBRACKET", "LINE_COMMENT", "LONG", "LONGLITERAL", "LPAREN", "LT", "LongSuffix", "MONKEYS_AT", "NATIVE", "NEW", "NULL", "NonIntegerNumber", "PACKAGE", "PERCENT", "PERCENTEQ", "PLUS", "PLUSEQ", "PLUSPLUS", "PRIVATE", "PROTECTED", "PUBLIC", "QUES", "RBRACE", "RBRACKET", "RETURN", "RPAREN", "SEMI", "SHORT", "SLASH", "SLASHEQ", "STAR", "STAREQ", "STATIC", "STRICTFP", "STRINGLITERAL", "SUB", "SUBEQ", "SUBSUB", "SUPER", "SWITCH", "SYNCHRONIZED", "SurrogateIdentifer", "THIS", "THROW", "THROWS", "TILDE", "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE", "WS"
    };

    public static final int EOF=-1;
    public static final int ABSTRACT=4;
    public static final int AMP=5;
    public static final int AMPAMP=6;
    public static final int AMPEQ=7;
    public static final int ASSERT=8;
    public static final int BANG=9;
    public static final int BANGEQ=10;
    public static final int BAR=11;
    public static final int BARBAR=12;
    public static final int BAREQ=13;
    public static final int BINLITERAL=14;
    public static final int BOOLEAN=15;
    public static final int BREAK=16;
    public static final int BYTE=17;
    public static final int BinPrefix=18;
    public static final int CARET=19;
    public static final int CARETEQ=20;
    public static final int CASE=21;
    public static final int CATCH=22;
    public static final int CHAR=23;
    public static final int CHARLITERAL=24;
    public static final int CLASS=25;
    public static final int COLON=26;
    public static final int COMMA=27;
    public static final int COMMENT=28;
    public static final int CONST=29;
    public static final int CONTINUE=30;
    public static final int DEFAULT=31;
    public static final int DO=32;
    public static final int DOT=33;
    public static final int DOUBLE=34;
    public static final int DOUBLELITERAL=35;
    public static final int DoubleSuffix=36;
    public static final int ELLIPSIS=37;
    public static final int ELSE=38;
    public static final int ENUM=39;
    public static final int EQ=40;
    public static final int EQEQ=41;
    public static final int EXTENDS=42;
    public static final int EscapeSequence=43;
    public static final int Exponent=44;
    public static final int FALSE=45;
    public static final int FINAL=46;
    public static final int FINALLY=47;
    public static final int FLOAT=48;
    public static final int FLOATLITERAL=49;
    public static final int FOR=50;
    public static final int FloatSuffix=51;
    public static final int GOTO=52;
    public static final int GT=53;
    public static final int HexDigit=54;
    public static final int HexNumber=55;
    public static final int HexPrefix=56;
    public static final int IDENTIFIER=57;
    public static final int IF=58;
    public static final int IMPLEMENTS=59;
    public static final int IMPORT=60;
    public static final int INSTANCEOF=61;
    public static final int INT=62;
    public static final int INTERFACE=63;
    public static final int INTLITERAL=64;
    public static final int IdentifierPart=65;
    public static final int IdentifierStart=66;
    public static final int IntegerNumber=67;
    public static final int LBRACE=68;
    public static final int LBRACKET=69;
    public static final int LINE_COMMENT=70;
    public static final int LONG=71;
    public static final int LONGLITERAL=72;
    public static final int LPAREN=73;
    public static final int LT=74;
    public static final int LongSuffix=75;
    public static final int MONKEYS_AT=76;
    public static final int NATIVE=77;
    public static final int NEW=78;
    public static final int NULL=79;
    public static final int NonIntegerNumber=80;
    public static final int PACKAGE=81;
    public static final int PERCENT=82;
    public static final int PERCENTEQ=83;
    public static final int PLUS=84;
    public static final int PLUSEQ=85;
    public static final int PLUSPLUS=86;
    public static final int PRIVATE=87;
    public static final int PROTECTED=88;
    public static final int PUBLIC=89;
    public static final int QUES=90;
    public static final int RBRACE=91;
    public static final int RBRACKET=92;
    public static final int RETURN=93;
    public static final int RPAREN=94;
    public static final int SEMI=95;
    public static final int SHORT=96;
    public static final int SLASH=97;
    public static final int SLASHEQ=98;
    public static final int STAR=99;
    public static final int STAREQ=100;
    public static final int STATIC=101;
    public static final int STRICTFP=102;
    public static final int STRINGLITERAL=103;
    public static final int SUB=104;
    public static final int SUBEQ=105;
    public static final int SUBSUB=106;
    public static final int SUPER=107;
    public static final int SWITCH=108;
    public static final int SYNCHRONIZED=109;
    public static final int SurrogateIdentifer=110;
    public static final int THIS=111;
    public static final int THROW=112;
    public static final int THROWS=113;
    public static final int TILDE=114;
    public static final int TRANSIENT=115;
    public static final int TRUE=116;
    public static final int TRY=117;
    public static final int VOID=118;
    public static final int VOLATILE=119;
    public static final int WHILE=120;
    public static final int WS=121;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public JavaParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public JavaParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[378+1];
         

    }

    public String[] getTokenNames() { return JavaParser.tokenNames; }
    public String getGrammarFileName() { return "Java.g"; }


        private TreeBuilder T;

        public void setTreeBuilder(TreeBuilder T){
            this.T = T;
        }

        public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        }



    // $ANTLR start "compilationUnit"
    // Java.g:344:1: compilationUnit : ( (t0= annotations )? t1= packageDeclaration )? (t2= importDeclaration )* (t3= typeDeclaration )* ;
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();

        annotations_return t0 =null;

        packageDeclaration_return t1 =null;

        importDeclaration_return t2 =null;

        typeDeclaration_return t3 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }

            // Java.g:345:5: ( ( (t0= annotations )? t1= packageDeclaration )? (t2= importDeclaration )* (t3= typeDeclaration )* )
            // Java.g:345:9: ( (t0= annotations )? t1= packageDeclaration )? (t2= importDeclaration )* (t3= typeDeclaration )*
            {
            // Java.g:345:9: ( (t0= annotations )? t1= packageDeclaration )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==MONKEYS_AT) ) {
                int LA2_1 = input.LA(2);

                if ( (synpred2_Java()) ) {
                    alt2=1;
                }
            }
            else if ( (LA2_0==PACKAGE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // Java.g:345:13: (t0= annotations )? t1= packageDeclaration
                    {
                    // Java.g:345:13: (t0= annotations )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==MONKEYS_AT) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // Java.g:345:14: t0= annotations
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotations"));}

                            pushFollow(FOLLOW_annotations_in_compilationUnit97);
                            t0=annotations();

                            state._fsp--;
                            if (state.failed) return ;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t0!=null?((Token)t0.start):null), (t0!=null?((Token)t0.stop):null));}

                            }
                            break;

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("packageDeclaration"));}

                    pushFollow(FOLLOW_packageDeclaration_in_compilationUnit132);
                    t1=packageDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t1!=null?((Token)t1.start):null), (t1!=null?((Token)t1.stop):null));}

                    }
                    break;

            }


            // Java.g:349:9: (t2= importDeclaration )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==IMPORT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // Java.g:349:10: t2= importDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("importDeclaration"));}

            	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit160);
            	    t2=importDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t2!=null?((Token)t2.start):null), (t2!=null?((Token)t2.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            // Java.g:351:9: (t3= typeDeclaration )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==ABSTRACT||LA4_0==BOOLEAN||LA4_0==BYTE||LA4_0==CHAR||LA4_0==CLASS||LA4_0==DOUBLE||LA4_0==ENUM||LA4_0==FINAL||LA4_0==FLOAT||LA4_0==IDENTIFIER||(LA4_0 >= INT && LA4_0 <= INTERFACE)||LA4_0==LONG||LA4_0==LT||(LA4_0 >= MONKEYS_AT && LA4_0 <= NATIVE)||(LA4_0 >= PRIVATE && LA4_0 <= PUBLIC)||(LA4_0 >= SEMI && LA4_0 <= SHORT)||(LA4_0 >= STATIC && LA4_0 <= STRICTFP)||LA4_0==SYNCHRONIZED||LA4_0==TRANSIENT||(LA4_0 >= VOID && LA4_0 <= VOLATILE)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // Java.g:351:10: t3= typeDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeDeclaration"));}

            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit188);
            	    t3=typeDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t3!=null?((Token)t3.start):null), (t3!=null?((Token)t3.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "compilationUnit"


    public static class packageDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "packageDeclaration"
    // Java.g:355:1: packageDeclaration : 'package' t4= qualifiedName ( ';' ) ;
    public final packageDeclaration_return packageDeclaration() throws RecognitionException {
        packageDeclaration_return retval = new packageDeclaration_return();
        retval.start = input.LT(1);

        int packageDeclaration_StartIndex = input.index();

        qualifiedName_return t4 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }

            // Java.g:356:5: ( 'package' t4= qualifiedName ( ';' ) )
            // Java.g:356:9: 'package' t4= qualifiedName ( ';' )
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDeclaration221); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'package'",input.LT(-1));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedName"));}

            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration228);
            t4=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t4!=null?((Token)t4.start):null), (t4!=null?((Token)t4.stop):null));}

            // Java.g:357:9: ( ';' )
            // Java.g:357:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_packageDeclaration241); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 2, packageDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "packageDeclaration"


    public static class importDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "importDeclaration"
    // Java.g:360:1: importDeclaration : ( 'import' ( 'static' )? IDENTIFIER ( '.' ) ( '*' ) ( ';' ) | 'import' ( 'static' )? IDENTIFIER ( ( '.' ) IDENTIFIER )+ ( ( '.' ) ( '*' ) )? ( ';' ) );
    public final importDeclaration_return importDeclaration() throws RecognitionException {
        importDeclaration_return retval = new importDeclaration_return();
        retval.start = input.LT(1);

        int importDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }

            // Java.g:361:5: ( 'import' ( 'static' )? IDENTIFIER ( '.' ) ( '*' ) ( ';' ) | 'import' ( 'static' )? IDENTIFIER ( ( '.' ) IDENTIFIER )+ ( ( '.' ) ( '*' ) )? ( ';' ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==IMPORT) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==STATIC) ) {
                    int LA9_2 = input.LA(3);

                    if ( (LA9_2==IDENTIFIER) ) {
                        int LA9_3 = input.LA(4);

                        if ( (LA9_3==DOT) ) {
                            int LA9_4 = input.LA(5);

                            if ( (LA9_4==STAR) ) {
                                alt9=1;
                            }
                            else if ( (LA9_4==IDENTIFIER) ) {
                                alt9=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 9, 4, input);

                                throw nvae;

                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 3, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA9_1==IDENTIFIER) ) {
                    int LA9_3 = input.LA(3);

                    if ( (LA9_3==DOT) ) {
                        int LA9_4 = input.LA(4);

                        if ( (LA9_4==STAR) ) {
                            alt9=1;
                        }
                        else if ( (LA9_4==IDENTIFIER) ) {
                            alt9=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // Java.g:361:9: 'import' ( 'static' )? IDENTIFIER ( '.' ) ( '*' ) ( ';' )
                    {
                    match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration264); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'import'",input.LT(-1));}

                    // Java.g:362:9: ( 'static' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==STATIC) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // Java.g:362:10: 'static'
                            {
                            match(input,STATIC,FOLLOW_STATIC_in_importDeclaration277); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'static'",input.LT(-1));}

                            }
                            break;

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration299); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:364:90: ( '.' )
                    // Java.g:364:91: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_importDeclaration304); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    // Java.g:364:128: ( '*' )
                    // Java.g:364:129: '*'
                    {
                    match(input,STAR,FOLLOW_STAR_in_importDeclaration309); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'*'",input.LT(-1));}

                    }


                    // Java.g:365:9: ( ';' )
                    // Java.g:365:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_importDeclaration322); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:366:9: 'import' ( 'static' )? IDENTIFIER ( ( '.' ) IDENTIFIER )+ ( ( '.' ) ( '*' ) )? ( ';' )
                    {
                    match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration341); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'import'",input.LT(-1));}

                    // Java.g:367:9: ( 'static' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==STATIC) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // Java.g:367:10: 'static'
                            {
                            match(input,STATIC,FOLLOW_STATIC_in_importDeclaration354); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'static'",input.LT(-1));}

                            }
                            break;

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration376); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:370:9: ( ( '.' ) IDENTIFIER )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==DOT) ) {
                            int LA7_1 = input.LA(2);

                            if ( (LA7_1==IDENTIFIER) ) {
                                alt7=1;
                            }


                        }


                        switch (alt7) {
                    	case 1 :
                    	    // Java.g:370:10: ( '.' ) IDENTIFIER
                    	    {
                    	    // Java.g:370:10: ( '.' )
                    	    // Java.g:370:11: '.'
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_importDeclaration390); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    	    }


                    	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration394); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    // Java.g:372:9: ( ( '.' ) ( '*' ) )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==DOT) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // Java.g:372:10: ( '.' ) ( '*' )
                            {
                            // Java.g:372:10: ( '.' )
                            // Java.g:372:11: '.'
                            {
                            match(input,DOT,FOLLOW_DOT_in_importDeclaration419); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                            }


                            // Java.g:372:48: ( '*' )
                            // Java.g:372:49: '*'
                            {
                            match(input,STAR,FOLLOW_STAR_in_importDeclaration424); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'*'",input.LT(-1));}

                            }


                            }
                            break;

                    }


                    // Java.g:374:9: ( ';' )
                    // Java.g:374:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_importDeclaration448); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 3, importDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "importDeclaration"



    // $ANTLR start "qualifiedImportName"
    // Java.g:377:1: qualifiedImportName : IDENTIFIER ( ( '.' ) IDENTIFIER )* ;
    public final void qualifiedImportName() throws RecognitionException {
        int qualifiedImportName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }

            // Java.g:378:5: ( IDENTIFIER ( ( '.' ) IDENTIFIER )* )
            // Java.g:378:9: IDENTIFIER ( ( '.' ) IDENTIFIER )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName470); if (state.failed) return ;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:379:9: ( ( '.' ) IDENTIFIER )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==DOT) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // Java.g:379:10: ( '.' ) IDENTIFIER
            	    {
            	    // Java.g:379:10: ( '.' )
            	    // Java.g:379:11: '.'
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualifiedImportName484); if (state.failed) return ;

            	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

            	    }


            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName488); if (state.failed) return ;

            	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 4, qualifiedImportName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "qualifiedImportName"


    public static class typeDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeDeclaration"
    // Java.g:383:1: typeDeclaration : (t5= classOrInterfaceDeclaration | ( ';' ) );
    public final typeDeclaration_return typeDeclaration() throws RecognitionException {
        typeDeclaration_return retval = new typeDeclaration_return();
        retval.start = input.LT(1);

        int typeDeclaration_StartIndex = input.index();

        classOrInterfaceDeclaration_return t5 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }

            // Java.g:384:5: (t5= classOrInterfaceDeclaration | ( ';' ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ABSTRACT||LA11_0==BOOLEAN||LA11_0==BYTE||LA11_0==CHAR||LA11_0==CLASS||LA11_0==DOUBLE||LA11_0==ENUM||LA11_0==FINAL||LA11_0==FLOAT||LA11_0==IDENTIFIER||(LA11_0 >= INT && LA11_0 <= INTERFACE)||LA11_0==LONG||LA11_0==LT||(LA11_0 >= MONKEYS_AT && LA11_0 <= NATIVE)||(LA11_0 >= PRIVATE && LA11_0 <= PUBLIC)||LA11_0==SHORT||(LA11_0 >= STATIC && LA11_0 <= STRICTFP)||LA11_0==SYNCHRONIZED||LA11_0==TRANSIENT||(LA11_0 >= VOID && LA11_0 <= VOLATILE)) ) {
                alt11=1;
            }
            else if ( (LA11_0==SEMI) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // Java.g:384:9: t5= classOrInterfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceDeclaration"));}

                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration525);
                    t5=classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t5!=null?((Token)t5.start):null), (t5!=null?((Token)t5.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:385:9: ( ';' )
                    {
                    // Java.g:385:9: ( ';' )
                    // Java.g:385:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_typeDeclaration538); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 5, typeDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeDeclaration"


    public static class classOrInterfaceDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classOrInterfaceDeclaration"
    // Java.g:388:1: classOrInterfaceDeclaration : (t6= classDeclaration |t7= interfaceDeclaration );
    public final classOrInterfaceDeclaration_return classOrInterfaceDeclaration() throws RecognitionException {
        classOrInterfaceDeclaration_return retval = new classOrInterfaceDeclaration_return();
        retval.start = input.LT(1);

        int classOrInterfaceDeclaration_StartIndex = input.index();

        classDeclaration_return t6 =null;

        interfaceDeclaration_return t7 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }

            // Java.g:389:5: (t6= classDeclaration |t7= interfaceDeclaration )
            int alt12=2;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA12_1 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA12_2 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA12_3 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA12_4 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA12_5 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA12_6 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA12_7 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA12_8 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA12_9 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA12_10 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA12_11 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA12_12 = input.LA(2);

                if ( (synpred12_Java()) ) {
                    alt12=1;
                }
                else if ( (true) ) {
                    alt12=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 12, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt12=1;
                }
                break;
            case INTERFACE:
                {
                alt12=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // Java.g:389:10: t6= classDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classDeclaration"));}

                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration565);
                    t6=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t6!=null?((Token)t6.start):null), (t6!=null?((Token)t6.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:390:9: t7= interfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceDeclaration"));}

                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration581);
                    t7=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t7!=null?((Token)t7.start):null), (t7!=null?((Token)t7.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 6, classOrInterfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classOrInterfaceDeclaration"


    public static class modifiers_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "modifiers"
    // Java.g:394:1: modifiers : (t8= annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* ;
    public final modifiers_return modifiers() throws RecognitionException {
        modifiers_return retval = new modifiers_return();
        retval.start = input.LT(1);

        int modifiers_StartIndex = input.index();

        annotation_return t8 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }

            // Java.g:395:5: ( (t8= annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* )
            // Java.g:396:5: (t8= annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
            {
            // Java.g:396:5: (t8= annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
            loop13:
            do {
                int alt13=13;
                switch ( input.LA(1) ) {
                case MONKEYS_AT:
                    {
                    int LA13_2 = input.LA(2);

                    if ( (LA13_2==IDENTIFIER) ) {
                        alt13=1;
                    }


                    }
                    break;
                case PUBLIC:
                    {
                    alt13=2;
                    }
                    break;
                case PROTECTED:
                    {
                    alt13=3;
                    }
                    break;
                case PRIVATE:
                    {
                    alt13=4;
                    }
                    break;
                case STATIC:
                    {
                    alt13=5;
                    }
                    break;
                case ABSTRACT:
                    {
                    alt13=6;
                    }
                    break;
                case FINAL:
                    {
                    alt13=7;
                    }
                    break;
                case NATIVE:
                    {
                    alt13=8;
                    }
                    break;
                case SYNCHRONIZED:
                    {
                    alt13=9;
                    }
                    break;
                case TRANSIENT:
                    {
                    alt13=10;
                    }
                    break;
                case VOLATILE:
                    {
                    alt13=11;
                    }
                    break;
                case STRICTFP:
                    {
                    alt13=12;
                    }
                    break;

                }

                switch (alt13) {
            	case 1 :
            	    // Java.g:396:10: t8= annotation
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotation"));}

            	    pushFollow(FOLLOW_annotation_in_modifiers622);
            	    t8=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t8!=null?((Token)t8.start):null), (t8!=null?((Token)t8.stop):null));}

            	    }
            	    break;
            	case 2 :
            	    // Java.g:397:9: 'public'
            	    {
            	    match(input,PUBLIC,FOLLOW_PUBLIC_in_modifiers634); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'public'",input.LT(-1));}

            	    }
            	    break;
            	case 3 :
            	    // Java.g:398:9: 'protected'
            	    {
            	    match(input,PROTECTED,FOLLOW_PROTECTED_in_modifiers645); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'protected'",input.LT(-1));}

            	    }
            	    break;
            	case 4 :
            	    // Java.g:399:9: 'private'
            	    {
            	    match(input,PRIVATE,FOLLOW_PRIVATE_in_modifiers656); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'private'",input.LT(-1));}

            	    }
            	    break;
            	case 5 :
            	    // Java.g:400:9: 'static'
            	    {
            	    match(input,STATIC,FOLLOW_STATIC_in_modifiers667); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'static'",input.LT(-1));}

            	    }
            	    break;
            	case 6 :
            	    // Java.g:401:9: 'abstract'
            	    {
            	    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_modifiers678); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'abstract'",input.LT(-1));}

            	    }
            	    break;
            	case 7 :
            	    // Java.g:402:9: 'final'
            	    {
            	    match(input,FINAL,FOLLOW_FINAL_in_modifiers689); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'final'",input.LT(-1));}

            	    }
            	    break;
            	case 8 :
            	    // Java.g:403:9: 'native'
            	    {
            	    match(input,NATIVE,FOLLOW_NATIVE_in_modifiers700); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'native'",input.LT(-1));}

            	    }
            	    break;
            	case 9 :
            	    // Java.g:404:9: 'synchronized'
            	    {
            	    match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_modifiers711); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'synchronized'",input.LT(-1));}

            	    }
            	    break;
            	case 10 :
            	    // Java.g:405:9: 'transient'
            	    {
            	    match(input,TRANSIENT,FOLLOW_TRANSIENT_in_modifiers722); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'transient'",input.LT(-1));}

            	    }
            	    break;
            	case 11 :
            	    // Java.g:406:9: 'volatile'
            	    {
            	    match(input,VOLATILE,FOLLOW_VOLATILE_in_modifiers733); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'volatile'",input.LT(-1));}

            	    }
            	    break;
            	case 12 :
            	    // Java.g:407:9: 'strictfp'
            	    {
            	    match(input,STRICTFP,FOLLOW_STRICTFP_in_modifiers744); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'strictfp'",input.LT(-1));}

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            if ( state.backtracking==0 ) {
                   Token cur = ((Token)retval.start);
                   if(cur.getTokenIndex() > 0) {
                       Token prev = getTokenStream().get(cur.getTokenIndex()-1);
                       if(prev.getChannel() == Token.HIDDEN_CHANNEL) {
                           if (prev.getText().contains("@deprecated")) {
                               T.pushTop();T.setCurrentParent(T.addNode("annotation"));
                               T.addLeaf("'@'",new CommonToken(MONKEYS_AT, "@"));
                               T.pushTop();T.setCurrentParent(T.addNode("qualifiedName"));
                               T.addLeaf("IDENTIFIER['Deprecated']", new CommonToken(IDENTIFIER, "Deprecated") );
                               T.popTop();
                               T.popTop();
                           }
                       }
                   }
                }

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 7, modifiers_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "modifiers"


    public static class variableModifiers_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "variableModifiers"
    // Java.g:428:1: variableModifiers : ( 'final' |t9= annotation )* ;
    public final variableModifiers_return variableModifiers() throws RecognitionException {
        variableModifiers_return retval = new variableModifiers_return();
        retval.start = input.LT(1);

        int variableModifiers_StartIndex = input.index();

        annotation_return t9 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }

            // Java.g:429:5: ( ( 'final' |t9= annotation )* )
            // Java.g:429:9: ( 'final' |t9= annotation )*
            {
            // Java.g:429:9: ( 'final' |t9= annotation )*
            loop14:
            do {
                int alt14=3;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==FINAL) ) {
                    alt14=1;
                }
                else if ( (LA14_0==MONKEYS_AT) ) {
                    alt14=2;
                }


                switch (alt14) {
            	case 1 :
            	    // Java.g:429:13: 'final'
            	    {
            	    match(input,FINAL,FOLLOW_FINAL_in_variableModifiers783); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'final'",input.LT(-1));}

            	    }
            	    break;
            	case 2 :
            	    // Java.g:430:13: t9= annotation
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotation"));}

            	    pushFollow(FOLLOW_annotation_in_variableModifiers802);
            	    t9=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t9!=null?((Token)t9.start):null), (t9!=null?((Token)t9.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 8, variableModifiers_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableModifiers"


    public static class classDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classDeclaration"
    // Java.g:435:1: classDeclaration : (t10= normalClassDeclaration |t11= enumDeclaration );
    public final classDeclaration_return classDeclaration() throws RecognitionException {
        classDeclaration_return retval = new classDeclaration_return();
        retval.start = input.LT(1);

        int classDeclaration_StartIndex = input.index();

        normalClassDeclaration_return t10 =null;

        enumDeclaration_return t11 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }

            // Java.g:436:5: (t10= normalClassDeclaration |t11= enumDeclaration )
            int alt15=2;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA15_1 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA15_2 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA15_3 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA15_4 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA15_5 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA15_6 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA15_7 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA15_8 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA15_9 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA15_10 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA15_11 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA15_12 = input.LA(2);

                if ( (synpred27_Java()) ) {
                    alt15=1;
                }
                else if ( (true) ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 12, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
                {
                alt15=1;
                }
                break;
            case ENUM:
                {
                alt15=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // Java.g:436:9: t10= normalClassDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalClassDeclaration"));}

                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration844);
                    t10=normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t10!=null?((Token)t10.start):null), (t10!=null?((Token)t10.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:437:9: t11= enumDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumDeclaration"));}

                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration860);
                    t11=enumDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t11!=null?((Token)t11.start):null), (t11!=null?((Token)t11.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 9, classDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classDeclaration"


    public static class normalClassDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "normalClassDeclaration"
    // Java.g:440:1: normalClassDeclaration :t12= modifiers 'class' IDENTIFIER (t13= typeParameters )? ( 'extends' t14= type )? ( 'implements' t15= typeList )? t16= classBody ;
    public final normalClassDeclaration_return normalClassDeclaration() throws RecognitionException {
        normalClassDeclaration_return retval = new normalClassDeclaration_return();
        retval.start = input.LT(1);

        int normalClassDeclaration_StartIndex = input.index();

        modifiers_return t12 =null;

        typeParameters_return t13 =null;

        type_return t14 =null;

        typeList_return t15 =null;

        classBody_return t16 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }

            // Java.g:441:5: (t12= modifiers 'class' IDENTIFIER (t13= typeParameters )? ( 'extends' t14= type )? ( 'implements' t15= typeList )? t16= classBody )
            // Java.g:441:9: t12= modifiers 'class' IDENTIFIER (t13= typeParameters )? ( 'extends' t14= type )? ( 'implements' t15= typeList )? t16= classBody
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_normalClassDeclaration886);
            t12=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t12!=null?((Token)t12.start):null), (t12!=null?((Token)t12.stop):null));}

            match(input,CLASS,FOLLOW_CLASS_in_normalClassDeclaration891); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'class'",input.LT(-1));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalClassDeclaration894); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:442:9: (t13= typeParameters )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==LT) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // Java.g:442:10: t13= typeParameters
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameters"));}

                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration911);
                    t13=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t13!=null?((Token)t13.start):null), (t13!=null?((Token)t13.stop):null));}

                    }
                    break;

            }


            // Java.g:444:9: ( 'extends' t14= type )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==EXTENDS) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // Java.g:444:10: 'extends' t14= type
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_normalClassDeclaration935); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'extends'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_normalClassDeclaration942);
                    t14=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t14!=null?((Token)t14.start):null), (t14!=null?((Token)t14.stop):null));}

                    }
                    break;

            }


            // Java.g:446:9: ( 'implements' t15= typeList )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==IMPLEMENTS) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // Java.g:446:10: 'implements' t15= typeList
                    {
                    match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_normalClassDeclaration966); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'implements'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeList"));}

                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration973);
                    t15=typeList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t15!=null?((Token)t15.start):null), (t15!=null?((Token)t15.stop):null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classBody"));}

            pushFollow(FOLLOW_classBody_in_normalClassDeclaration1012);
            t16=classBody();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t16!=null?((Token)t16.start):null), (t16!=null?((Token)t16.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 10, normalClassDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalClassDeclaration"


    public static class typeParameters_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeParameters"
    // Java.g:452:1: typeParameters : ( '<' ) t17= typeParameter ( ( ',' ) t18= typeParameter )* ( '>' ) ;
    public final typeParameters_return typeParameters() throws RecognitionException {
        typeParameters_return retval = new typeParameters_return();
        retval.start = input.LT(1);

        int typeParameters_StartIndex = input.index();

        typeParameter_return t17 =null;

        typeParameter_return t18 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }

            // Java.g:453:5: ( ( '<' ) t17= typeParameter ( ( ',' ) t18= typeParameter )* ( '>' ) )
            // Java.g:453:9: ( '<' ) t17= typeParameter ( ( ',' ) t18= typeParameter )* ( '>' )
            {
            // Java.g:453:9: ( '<' )
            // Java.g:453:10: '<'
            {
            match(input,LT,FOLLOW_LT_in_typeParameters1036); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameter"));}

            pushFollow(FOLLOW_typeParameter_in_typeParameters1056);
            t17=typeParameter();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t17!=null?((Token)t17.start):null), (t17!=null?((Token)t17.stop):null));}

            // Java.g:455:13: ( ( ',' ) t18= typeParameter )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==COMMA) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // Java.g:455:14: ( ',' ) t18= typeParameter
            	    {
            	    // Java.g:455:14: ( ',' )
            	    // Java.g:455:15: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeParameters1074); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameter"));}

            	    pushFollow(FOLLOW_typeParameter_in_typeParameters1082);
            	    t18=typeParameter();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t18!=null?((Token)t18.start):null), (t18!=null?((Token)t18.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            // Java.g:457:9: ( '>' )
            // Java.g:457:10: '>'
            {
            match(input,GT,FOLLOW_GT_in_typeParameters1110); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 11, typeParameters_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeParameters"


    public static class typeParameter_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeParameter"
    // Java.g:460:1: typeParameter : IDENTIFIER ( 'extends' t19= typeBound )? ;
    public final typeParameter_return typeParameter() throws RecognitionException {
        typeParameter_return retval = new typeParameter_return();
        retval.start = input.LT(1);

        int typeParameter_StartIndex = input.index();

        typeBound_return t19 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }

            // Java.g:461:5: ( IDENTIFIER ( 'extends' t19= typeBound )? )
            // Java.g:461:9: IDENTIFIER ( 'extends' t19= typeBound )?
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_typeParameter1132); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:462:9: ( 'extends' t19= typeBound )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==EXTENDS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // Java.g:462:10: 'extends' t19= typeBound
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_typeParameter1145); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'extends'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeBound"));}

                    pushFollow(FOLLOW_typeBound_in_typeParameter1152);
                    t19=typeBound();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t19!=null?((Token)t19.start):null), (t19!=null?((Token)t19.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 12, typeParameter_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeParameter"


    public static class typeBound_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeBound"
    // Java.g:467:1: typeBound :t20= type ( ( '&' ) t21= type )* ;
    public final typeBound_return typeBound() throws RecognitionException {
        typeBound_return retval = new typeBound_return();
        retval.start = input.LT(1);

        int typeBound_StartIndex = input.index();

        type_return t20 =null;

        type_return t21 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }

            // Java.g:468:5: (t20= type ( ( '&' ) t21= type )* )
            // Java.g:468:9: t20= type ( ( '&' ) t21= type )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_typeBound1190);
            t20=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t20!=null?((Token)t20.start):null), (t20!=null?((Token)t20.stop):null));}

            // Java.g:469:9: ( ( '&' ) t21= type )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==AMP) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // Java.g:469:10: ( '&' ) t21= type
            	    {
            	    // Java.g:469:10: ( '&' )
            	    // Java.g:469:11: '&'
            	    {
            	    match(input,AMP,FOLLOW_AMP_in_typeBound1204); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'&'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            	    pushFollow(FOLLOW_type_in_typeBound1212);
            	    t21=type();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t21!=null?((Token)t21.start):null), (t21!=null?((Token)t21.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 13, typeBound_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeBound"


    public static class enumDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "enumDeclaration"
    // Java.g:474:1: enumDeclaration :t22= modifiers ( 'enum' ) IDENTIFIER ( 'implements' t23= typeList )? t24= enumBody ;
    public final enumDeclaration_return enumDeclaration() throws RecognitionException {
        enumDeclaration_return retval = new enumDeclaration_return();
        retval.start = input.LT(1);

        int enumDeclaration_StartIndex = input.index();

        modifiers_return t22 =null;

        typeList_return t23 =null;

        enumBody_return t24 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }

            // Java.g:475:5: (t22= modifiers ( 'enum' ) IDENTIFIER ( 'implements' t23= typeList )? t24= enumBody )
            // Java.g:475:9: t22= modifiers ( 'enum' ) IDENTIFIER ( 'implements' t23= typeList )? t24= enumBody
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_enumDeclaration1250);
            t22=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t22!=null?((Token)t22.start):null), (t22!=null?((Token)t22.stop):null));}

            // Java.g:476:9: ( 'enum' )
            // Java.g:476:10: 'enum'
            {
            match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration1264); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'enum'",input.LT(-1));}

            }


            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumDeclaration1286); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:479:9: ( 'implements' t23= typeList )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==IMPLEMENTS) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // Java.g:479:10: 'implements' t23= typeList
                    {
                    match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_enumDeclaration1299); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'implements'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeList"));}

                    pushFollow(FOLLOW_typeList_in_enumDeclaration1306);
                    t23=typeList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t23!=null?((Token)t23.start):null), (t23!=null?((Token)t23.stop):null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumBody"));}

            pushFollow(FOLLOW_enumBody_in_enumDeclaration1333);
            t24=enumBody();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t24!=null?((Token)t24.start):null), (t24!=null?((Token)t24.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 14, enumDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumDeclaration"


    public static class enumBody_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "enumBody"
    // Java.g:485:1: enumBody : ( '{' ) (t25= enumConstants )? ( ',' )? (t26= enumBodyDeclarations )? ( '}' ) ;
    public final enumBody_return enumBody() throws RecognitionException {
        enumBody_return retval = new enumBody_return();
        retval.start = input.LT(1);

        int enumBody_StartIndex = input.index();

        enumConstants_return t25 =null;

        enumBodyDeclarations_return t26 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }

            // Java.g:486:5: ( ( '{' ) (t25= enumConstants )? ( ',' )? (t26= enumBodyDeclarations )? ( '}' ) )
            // Java.g:486:9: ( '{' ) (t25= enumConstants )? ( ',' )? (t26= enumBodyDeclarations )? ( '}' )
            {
            // Java.g:486:9: ( '{' )
            // Java.g:486:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_enumBody1361); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:487:9: (t25= enumConstants )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==IDENTIFIER||LA23_0==MONKEYS_AT) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // Java.g:487:10: t25= enumConstants
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumConstants"));}

                    pushFollow(FOLLOW_enumConstants_in_enumBody1378);
                    t25=enumConstants();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t25!=null?((Token)t25.start):null), (t25!=null?((Token)t25.stop):null));}

                    }
                    break;

            }


            // Java.g:489:9: ( ',' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==COMMA) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // Java.g:489:10: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_enumBody1403); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    }
                    break;

            }


            // Java.g:490:9: (t26= enumBodyDeclarations )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==SEMI) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // Java.g:490:10: t26= enumBodyDeclarations
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumBodyDeclarations"));}

                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody1422);
                    t26=enumBodyDeclarations();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t26!=null?((Token)t26.start):null), (t26!=null?((Token)t26.stop):null));}

                    }
                    break;

            }


            // Java.g:492:9: ( '}' )
            // Java.g:492:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_enumBody1447); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 15, enumBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumBody"


    public static class enumConstants_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "enumConstants"
    // Java.g:495:1: enumConstants :t27= enumConstant ( ( ',' ) t28= enumConstant )* ;
    public final enumConstants_return enumConstants() throws RecognitionException {
        enumConstants_return retval = new enumConstants_return();
        retval.start = input.LT(1);

        int enumConstants_StartIndex = input.index();

        enumConstant_return t27 =null;

        enumConstant_return t28 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return retval; }

            // Java.g:496:5: (t27= enumConstant ( ( ',' ) t28= enumConstant )* )
            // Java.g:496:9: t27= enumConstant ( ( ',' ) t28= enumConstant )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumConstant"));}

            pushFollow(FOLLOW_enumConstant_in_enumConstants1473);
            t27=enumConstant();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t27!=null?((Token)t27.start):null), (t27!=null?((Token)t27.stop):null));}

            // Java.g:497:9: ( ( ',' ) t28= enumConstant )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==COMMA) ) {
                    int LA26_1 = input.LA(2);

                    if ( (LA26_1==IDENTIFIER||LA26_1==MONKEYS_AT) ) {
                        alt26=1;
                    }


                }


                switch (alt26) {
            	case 1 :
            	    // Java.g:497:10: ( ',' ) t28= enumConstant
            	    {
            	    // Java.g:497:10: ( ',' )
            	    // Java.g:497:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_enumConstants1487); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumConstant"));}

            	    pushFollow(FOLLOW_enumConstant_in_enumConstants1495);
            	    t28=enumConstant();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t28!=null?((Token)t28.start):null), (t28!=null?((Token)t28.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 16, enumConstants_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumConstants"


    public static class enumConstant_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "enumConstant"
    // Java.g:505:1: enumConstant : (t29= annotations )? IDENTIFIER (t30= arguments )? (t31= classBody )? ;
    public final enumConstant_return enumConstant() throws RecognitionException {
        enumConstant_return retval = new enumConstant_return();
        retval.start = input.LT(1);

        int enumConstant_StartIndex = input.index();

        annotations_return t29 =null;

        arguments_return t30 =null;

        classBody_return t31 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return retval; }

            // Java.g:506:5: ( (t29= annotations )? IDENTIFIER (t30= arguments )? (t31= classBody )? )
            // Java.g:506:9: (t29= annotations )? IDENTIFIER (t30= arguments )? (t31= classBody )?
            {
            // Java.g:506:9: (t29= annotations )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==MONKEYS_AT) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // Java.g:506:10: t29= annotations
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotations"));}

                    pushFollow(FOLLOW_annotations_in_enumConstant1535);
                    t29=annotations();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t29!=null?((Token)t29.start):null), (t29!=null?((Token)t29.stop):null));}

                    }
                    break;

            }


            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumConstant1558); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:509:9: (t30= arguments )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==LPAREN) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // Java.g:509:10: t30= arguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_enumConstant1575);
                    t30=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t30!=null?((Token)t30.start):null), (t30!=null?((Token)t30.stop):null));}

                    }
                    break;

            }


            // Java.g:511:9: (t31= classBody )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==LBRACE) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // Java.g:511:10: t31= classBody
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classBody"));}

                    pushFollow(FOLLOW_classBody_in_enumConstant1603);
                    t31=classBody();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t31!=null?((Token)t31.start):null), (t31!=null?((Token)t31.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 17, enumConstant_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumConstant"


    public static class enumBodyDeclarations_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "enumBodyDeclarations"
    // Java.g:517:1: enumBodyDeclarations : ( ';' ) (t32= classBodyDeclaration )* ;
    public final enumBodyDeclarations_return enumBodyDeclarations() throws RecognitionException {
        enumBodyDeclarations_return retval = new enumBodyDeclarations_return();
        retval.start = input.LT(1);

        int enumBodyDeclarations_StartIndex = input.index();

        classBodyDeclaration_return t32 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return retval; }

            // Java.g:518:5: ( ( ';' ) (t32= classBodyDeclaration )* )
            // Java.g:518:9: ( ';' ) (t32= classBodyDeclaration )*
            {
            // Java.g:518:9: ( ';' )
            // Java.g:518:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_enumBodyDeclarations1647); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            // Java.g:519:9: (t32= classBodyDeclaration )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==ABSTRACT||LA30_0==BOOLEAN||LA30_0==BYTE||LA30_0==CHAR||LA30_0==CLASS||LA30_0==DOUBLE||LA30_0==ENUM||LA30_0==FINAL||LA30_0==FLOAT||LA30_0==IDENTIFIER||(LA30_0 >= INT && LA30_0 <= INTERFACE)||LA30_0==LBRACE||LA30_0==LONG||LA30_0==LT||(LA30_0 >= MONKEYS_AT && LA30_0 <= NATIVE)||(LA30_0 >= PRIVATE && LA30_0 <= PUBLIC)||(LA30_0 >= SEMI && LA30_0 <= SHORT)||(LA30_0 >= STATIC && LA30_0 <= STRICTFP)||LA30_0==SYNCHRONIZED||LA30_0==TRANSIENT||(LA30_0 >= VOID && LA30_0 <= VOLATILE)) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // Java.g:519:10: t32= classBodyDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classBodyDeclaration"));}

            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1665);
            	    t32=classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t32!=null?((Token)t32.start):null), (t32!=null?((Token)t32.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 18, enumBodyDeclarations_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumBodyDeclarations"


    public static class interfaceDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "interfaceDeclaration"
    // Java.g:523:1: interfaceDeclaration : (t33= normalInterfaceDeclaration |t34= annotationTypeDeclaration );
    public final interfaceDeclaration_return interfaceDeclaration() throws RecognitionException {
        interfaceDeclaration_return retval = new interfaceDeclaration_return();
        retval.start = input.LT(1);

        int interfaceDeclaration_StartIndex = input.index();

        normalInterfaceDeclaration_return t33 =null;

        annotationTypeDeclaration_return t34 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return retval; }

            // Java.g:524:5: (t33= normalInterfaceDeclaration |t34= annotationTypeDeclaration )
            int alt31=2;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA31_1 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA31_2 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA31_3 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA31_4 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA31_5 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA31_6 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA31_7 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA31_8 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA31_9 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA31_10 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA31_11 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA31_12 = input.LA(2);

                if ( (synpred43_Java()) ) {
                    alt31=1;
                }
                else if ( (true) ) {
                    alt31=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 12, input);

                    throw nvae;

                }
                }
                break;
            case INTERFACE:
                {
                alt31=1;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;

            }

            switch (alt31) {
                case 1 :
                    // Java.g:524:9: t33= normalInterfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalInterfaceDeclaration"));}

                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1702);
                    t33=normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t33!=null?((Token)t33.start):null), (t33!=null?((Token)t33.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:525:9: t34= annotationTypeDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotationTypeDeclaration"));}

                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1718);
                    t34=annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t34!=null?((Token)t34.start):null), (t34!=null?((Token)t34.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 19, interfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceDeclaration"


    public static class normalInterfaceDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "normalInterfaceDeclaration"
    // Java.g:528:1: normalInterfaceDeclaration :t35= modifiers 'interface' IDENTIFIER (t36= typeParameters )? ( 'extends' t37= typeList )? t38= interfaceBody ;
    public final normalInterfaceDeclaration_return normalInterfaceDeclaration() throws RecognitionException {
        normalInterfaceDeclaration_return retval = new normalInterfaceDeclaration_return();
        retval.start = input.LT(1);

        int normalInterfaceDeclaration_StartIndex = input.index();

        modifiers_return t35 =null;

        typeParameters_return t36 =null;

        typeList_return t37 =null;

        interfaceBody_return t38 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return retval; }

            // Java.g:529:5: (t35= modifiers 'interface' IDENTIFIER (t36= typeParameters )? ( 'extends' t37= typeList )? t38= interfaceBody )
            // Java.g:529:9: t35= modifiers 'interface' IDENTIFIER (t36= typeParameters )? ( 'extends' t37= typeList )? t38= interfaceBody
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_normalInterfaceDeclaration1748);
            t35=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t35!=null?((Token)t35.start):null), (t35!=null?((Token)t35.stop):null));}

            match(input,INTERFACE,FOLLOW_INTERFACE_in_normalInterfaceDeclaration1752); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'interface'",input.LT(-1));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1755); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:530:9: (t36= typeParameters )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==LT) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // Java.g:530:10: t36= typeParameters
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameters"));}

                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration1772);
                    t36=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t36!=null?((Token)t36.start):null), (t36!=null?((Token)t36.stop):null));}

                    }
                    break;

            }


            // Java.g:532:9: ( 'extends' t37= typeList )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==EXTENDS) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // Java.g:532:10: 'extends' t37= typeList
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_normalInterfaceDeclaration1796); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'extends'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeList"));}

                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration1803);
                    t37=typeList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t37!=null?((Token)t37.start):null), (t37!=null?((Token)t37.stop):null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceBody"));}

            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration1830);
            t38=interfaceBody();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t38!=null?((Token)t38.start):null), (t38!=null?((Token)t38.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 20, normalInterfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalInterfaceDeclaration"


    public static class typeList_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeList"
    // Java.g:537:1: typeList :t39= type ( ( ',' ) t40= type )* ;
    public final typeList_return typeList() throws RecognitionException {
        typeList_return retval = new typeList_return();
        retval.start = input.LT(1);

        int typeList_StartIndex = input.index();

        type_return t39 =null;

        type_return t40 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }

            // Java.g:538:5: (t39= type ( ( ',' ) t40= type )* )
            // Java.g:538:9: t39= type ( ( ',' ) t40= type )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_typeList1856);
            t39=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t39!=null?((Token)t39.start):null), (t39!=null?((Token)t39.stop):null));}

            // Java.g:539:9: ( ( ',' ) t40= type )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==COMMA) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // Java.g:539:10: ( ',' ) t40= type
            	    {
            	    // Java.g:539:10: ( ',' )
            	    // Java.g:539:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeList1870); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            	    pushFollow(FOLLOW_type_in_typeList1878);
            	    t40=type();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t40!=null?((Token)t40.start):null), (t40!=null?((Token)t40.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 21, typeList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeList"


    public static class classBody_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classBody"
    // Java.g:543:1: classBody : ( '{' ) (t41= classBodyDeclaration )* ( '}' ) ;
    public final classBody_return classBody() throws RecognitionException {
        classBody_return retval = new classBody_return();
        retval.start = input.LT(1);

        int classBody_StartIndex = input.index();

        classBodyDeclaration_return t41 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return retval; }

            // Java.g:544:5: ( ( '{' ) (t41= classBodyDeclaration )* ( '}' ) )
            // Java.g:544:9: ( '{' ) (t41= classBodyDeclaration )* ( '}' )
            {
            // Java.g:544:9: ( '{' )
            // Java.g:544:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_classBody1912); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:545:9: (t41= classBodyDeclaration )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==ABSTRACT||LA35_0==BOOLEAN||LA35_0==BYTE||LA35_0==CHAR||LA35_0==CLASS||LA35_0==DOUBLE||LA35_0==ENUM||LA35_0==FINAL||LA35_0==FLOAT||LA35_0==IDENTIFIER||(LA35_0 >= INT && LA35_0 <= INTERFACE)||LA35_0==LBRACE||LA35_0==LONG||LA35_0==LT||(LA35_0 >= MONKEYS_AT && LA35_0 <= NATIVE)||(LA35_0 >= PRIVATE && LA35_0 <= PUBLIC)||(LA35_0 >= SEMI && LA35_0 <= SHORT)||(LA35_0 >= STATIC && LA35_0 <= STRICTFP)||LA35_0==SYNCHRONIZED||LA35_0==TRANSIENT||(LA35_0 >= VOID && LA35_0 <= VOLATILE)) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // Java.g:545:10: t41= classBodyDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classBodyDeclaration"));}

            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody1930);
            	    t41=classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t41!=null?((Token)t41.start):null), (t41!=null?((Token)t41.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            // Java.g:547:9: ( '}' )
            // Java.g:547:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_classBody1955); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 22, classBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classBody"


    public static class interfaceBody_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "interfaceBody"
    // Java.g:550:1: interfaceBody : ( '{' ) (t42= interfaceBodyDeclaration )* ( '}' ) ;
    public final interfaceBody_return interfaceBody() throws RecognitionException {
        interfaceBody_return retval = new interfaceBody_return();
        retval.start = input.LT(1);

        int interfaceBody_StartIndex = input.index();

        interfaceBodyDeclaration_return t42 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return retval; }

            // Java.g:551:5: ( ( '{' ) (t42= interfaceBodyDeclaration )* ( '}' ) )
            // Java.g:551:9: ( '{' ) (t42= interfaceBodyDeclaration )* ( '}' )
            {
            // Java.g:551:9: ( '{' )
            // Java.g:551:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_interfaceBody1978); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:552:9: (t42= interfaceBodyDeclaration )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==ABSTRACT||LA36_0==BOOLEAN||LA36_0==BYTE||LA36_0==CHAR||LA36_0==CLASS||LA36_0==DOUBLE||LA36_0==ENUM||LA36_0==FINAL||LA36_0==FLOAT||LA36_0==IDENTIFIER||(LA36_0 >= INT && LA36_0 <= INTERFACE)||LA36_0==LONG||LA36_0==LT||(LA36_0 >= MONKEYS_AT && LA36_0 <= NATIVE)||(LA36_0 >= PRIVATE && LA36_0 <= PUBLIC)||(LA36_0 >= SEMI && LA36_0 <= SHORT)||(LA36_0 >= STATIC && LA36_0 <= STRICTFP)||LA36_0==SYNCHRONIZED||LA36_0==TRANSIENT||(LA36_0 >= VOID && LA36_0 <= VOLATILE)) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // Java.g:552:10: t42= interfaceBodyDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceBodyDeclaration"));}

            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody1996);
            	    t42=interfaceBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t42!=null?((Token)t42.start):null), (t42!=null?((Token)t42.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            // Java.g:554:9: ( '}' )
            // Java.g:554:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_interfaceBody2021); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 23, interfaceBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceBody"


    public static class classBodyDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classBodyDeclaration"
    // Java.g:557:1: classBodyDeclaration : ( ( ';' ) | ( 'static' )? t43= block |t44= memberDecl );
    public final classBodyDeclaration_return classBodyDeclaration() throws RecognitionException {
        classBodyDeclaration_return retval = new classBodyDeclaration_return();
        retval.start = input.LT(1);

        int classBodyDeclaration_StartIndex = input.index();

        block_return t43 =null;

        memberDecl_return t44 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return retval; }

            // Java.g:558:5: ( ( ';' ) | ( 'static' )? t43= block |t44= memberDecl )
            int alt38=3;
            switch ( input.LA(1) ) {
            case SEMI:
                {
                alt38=1;
                }
                break;
            case STATIC:
                {
                int LA38_2 = input.LA(2);

                if ( (LA38_2==LBRACE) ) {
                    alt38=2;
                }
                else if ( (LA38_2==ABSTRACT||LA38_2==BOOLEAN||LA38_2==BYTE||LA38_2==CHAR||LA38_2==CLASS||LA38_2==DOUBLE||LA38_2==ENUM||LA38_2==FINAL||LA38_2==FLOAT||LA38_2==IDENTIFIER||(LA38_2 >= INT && LA38_2 <= INTERFACE)||LA38_2==LONG||LA38_2==LT||(LA38_2 >= MONKEYS_AT && LA38_2 <= NATIVE)||(LA38_2 >= PRIVATE && LA38_2 <= PUBLIC)||LA38_2==SHORT||(LA38_2 >= STATIC && LA38_2 <= STRICTFP)||LA38_2==SYNCHRONIZED||LA38_2==TRANSIENT||(LA38_2 >= VOID && LA38_2 <= VOLATILE)) ) {
                    alt38=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 2, input);

                    throw nvae;

                }
                }
                break;
            case LBRACE:
                {
                alt38=2;
                }
                break;
            case ABSTRACT:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CLASS:
            case DOUBLE:
            case ENUM:
            case FINAL:
            case FLOAT:
            case IDENTIFIER:
            case INT:
            case INTERFACE:
            case LONG:
            case LT:
            case MONKEYS_AT:
            case NATIVE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case SHORT:
            case STRICTFP:
            case SYNCHRONIZED:
            case TRANSIENT:
            case VOID:
            case VOLATILE:
                {
                alt38=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;

            }

            switch (alt38) {
                case 1 :
                    // Java.g:558:9: ( ';' )
                    {
                    // Java.g:558:9: ( ';' )
                    // Java.g:558:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_classBodyDeclaration2044); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:559:9: ( 'static' )? t43= block
                    {
                    // Java.g:559:9: ( 'static' )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==STATIC) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // Java.g:559:10: 'static'
                            {
                            match(input,STATIC,FOLLOW_STATIC_in_classBodyDeclaration2057); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'static'",input.LT(-1));}

                            }
                            break;

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                    pushFollow(FOLLOW_block_in_classBodyDeclaration2084);
                    t43=block();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t43!=null?((Token)t43.start):null), (t43!=null?((Token)t43.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:562:9: t44= memberDecl
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("memberDecl"));}

                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration2100);
                    t44=memberDecl();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t44!=null?((Token)t44.start):null), (t44!=null?((Token)t44.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 24, classBodyDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classBodyDeclaration"


    public static class memberDecl_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "memberDecl"
    // Java.g:565:1: memberDecl : (t45= fieldDeclaration |t46= methodDeclaration |t47= classDeclaration |t48= interfaceDeclaration );
    public final memberDecl_return memberDecl() throws RecognitionException {
        memberDecl_return retval = new memberDecl_return();
        retval.start = input.LT(1);

        int memberDecl_StartIndex = input.index();

        fieldDeclaration_return t45 =null;

        methodDeclaration_return t46 =null;

        classDeclaration_return t47 =null;

        interfaceDeclaration_return t48 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return retval; }

            // Java.g:566:5: (t45= fieldDeclaration |t46= methodDeclaration |t47= classDeclaration |t48= interfaceDeclaration )
            int alt39=4;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA39_1 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA39_2 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA39_3 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA39_4 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA39_5 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA39_6 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA39_7 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA39_8 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA39_9 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA39_10 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA39_11 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA39_12 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else if ( (synpred54_Java()) ) {
                    alt39=3;
                }
                else if ( (true) ) {
                    alt39=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA39_13 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
                {
                int LA39_14 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 14, input);

                    throw nvae;

                }
                }
                break;
            case CHAR:
                {
                int LA39_15 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 15, input);

                    throw nvae;

                }
                }
                break;
            case BYTE:
                {
                int LA39_16 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 16, input);

                    throw nvae;

                }
                }
                break;
            case SHORT:
                {
                int LA39_17 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 17, input);

                    throw nvae;

                }
                }
                break;
            case INT:
                {
                int LA39_18 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 18, input);

                    throw nvae;

                }
                }
                break;
            case LONG:
                {
                int LA39_19 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 19, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA39_20 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 20, input);

                    throw nvae;

                }
                }
                break;
            case DOUBLE:
                {
                int LA39_21 = input.LA(2);

                if ( (synpred52_Java()) ) {
                    alt39=1;
                }
                else if ( (synpred53_Java()) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 21, input);

                    throw nvae;

                }
                }
                break;
            case LT:
            case VOID:
                {
                alt39=2;
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt39=3;
                }
                break;
            case INTERFACE:
                {
                alt39=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;

            }

            switch (alt39) {
                case 1 :
                    // Java.g:566:10: t45= fieldDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("fieldDeclaration"));}

                    pushFollow(FOLLOW_fieldDeclaration_in_memberDecl2127);
                    t45=fieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t45!=null?((Token)t45.start):null), (t45!=null?((Token)t45.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:567:10: t46= methodDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("methodDeclaration"));}

                    pushFollow(FOLLOW_methodDeclaration_in_memberDecl2144);
                    t46=methodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t46!=null?((Token)t46.start):null), (t46!=null?((Token)t46.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:568:10: t47= classDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classDeclaration"));}

                    pushFollow(FOLLOW_classDeclaration_in_memberDecl2161);
                    t47=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t47!=null?((Token)t47.start):null), (t47!=null?((Token)t47.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:569:10: t48= interfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceDeclaration"));}

                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl2178);
                    t48=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t48!=null?((Token)t48.start):null), (t48!=null?((Token)t48.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 25, memberDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "memberDecl"


    public static class methodDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "methodDeclaration"
    // Java.g:573:1: methodDeclaration : (t49= modifiers (t50= typeParameters )? IDENTIFIER t51= formalParameters ( 'throws' t52= qualifiedNameList )? ( '{' ) (t53= explicitConstructorInvocation )? (t54= blockStatement )* ( '}' ) |t55= modifiers (t56= typeParameters )? (t57= type | 'void' ) IDENTIFIER t58= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t59= qualifiedNameList )? (t60= block | ( ';' ) ) );
    public final methodDeclaration_return methodDeclaration() throws RecognitionException {
        methodDeclaration_return retval = new methodDeclaration_return();
        retval.start = input.LT(1);

        int methodDeclaration_StartIndex = input.index();

        modifiers_return t49 =null;

        typeParameters_return t50 =null;

        formalParameters_return t51 =null;

        qualifiedNameList_return t52 =null;

        explicitConstructorInvocation_return t53 =null;

        blockStatement_return t54 =null;

        modifiers_return t55 =null;

        typeParameters_return t56 =null;

        type_return t57 =null;

        formalParameters_return t58 =null;

        qualifiedNameList_return t59 =null;

        block_return t60 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return retval; }

            // Java.g:574:5: (t49= modifiers (t50= typeParameters )? IDENTIFIER t51= formalParameters ( 'throws' t52= qualifiedNameList )? ( '{' ) (t53= explicitConstructorInvocation )? (t54= blockStatement )* ( '}' ) |t55= modifiers (t56= typeParameters )? (t57= type | 'void' ) IDENTIFIER t58= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t59= qualifiedNameList )? (t60= block | ( ';' ) ) )
            int alt49=2;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA49_1 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA49_2 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA49_3 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA49_4 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA49_5 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA49_6 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA49_7 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA49_8 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA49_9 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA49_10 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA49_11 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA49_12 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 12, input);

                    throw nvae;

                }
                }
                break;
            case LT:
                {
                int LA49_13 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 13, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA49_14 = input.LA(2);

                if ( (synpred59_Java()) ) {
                    alt49=1;
                }
                else if ( (true) ) {
                    alt49=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 49, 14, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case VOID:
                {
                alt49=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;

            }

            switch (alt49) {
                case 1 :
                    // Java.g:576:10: t49= modifiers (t50= typeParameters )? IDENTIFIER t51= formalParameters ( 'throws' t52= qualifiedNameList )? ( '{' ) (t53= explicitConstructorInvocation )? (t54= blockStatement )* ( '}' )
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

                    pushFollow(FOLLOW_modifiers_in_methodDeclaration2222);
                    t49=modifiers();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t49!=null?((Token)t49.start):null), (t49!=null?((Token)t49.stop):null));}

                    // Java.g:577:9: (t50= typeParameters )?
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==LT) ) {
                        alt40=1;
                    }
                    switch (alt40) {
                        case 1 :
                            // Java.g:577:10: t50= typeParameters
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameters"));}

                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration2239);
                            t50=typeParameters();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t50!=null?((Token)t50.start):null), (t50!=null?((Token)t50.stop):null));}

                            }
                            break;

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration2262); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("formalParameters"));}

                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration2278);
                    t51=formalParameters();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t51!=null?((Token)t51.start):null), (t51!=null?((Token)t51.stop):null));}

                    // Java.g:581:9: ( 'throws' t52= qualifiedNameList )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==THROWS) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // Java.g:581:10: 'throws' t52= qualifiedNameList
                            {
                            match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration2291); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'throws'",input.LT(-1));}

                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedNameList"));}

                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration2298);
                            t52=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t52!=null?((Token)t52.start):null), (t52!=null?((Token)t52.stop):null));}

                            }
                            break;

                    }


                    // Java.g:583:9: ( '{' )
                    // Java.g:583:10: '{'
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_methodDeclaration2322); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

                    }


                    // Java.g:584:9: (t53= explicitConstructorInvocation )?
                    int alt42=2;
                    alt42 = dfa42.predict(input);
                    switch (alt42) {
                        case 1 :
                            // Java.g:584:10: t53= explicitConstructorInvocation
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("explicitConstructorInvocation"));}

                            pushFollow(FOLLOW_explicitConstructorInvocation_in_methodDeclaration2340);
                            t53=explicitConstructorInvocation();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t53!=null?((Token)t53.start):null), (t53!=null?((Token)t53.stop):null));}

                            }
                            break;

                    }


                    // Java.g:586:9: (t54= blockStatement )*
                    loop43:
                    do {
                        int alt43=2;
                        int LA43_0 = input.LA(1);

                        if ( (LA43_0==ABSTRACT||(LA43_0 >= ASSERT && LA43_0 <= BANG)||(LA43_0 >= BINLITERAL && LA43_0 <= BYTE)||(LA43_0 >= CHAR && LA43_0 <= CLASS)||LA43_0==CONTINUE||LA43_0==DO||(LA43_0 >= DOUBLE && LA43_0 <= DOUBLELITERAL)||LA43_0==ENUM||(LA43_0 >= FALSE && LA43_0 <= FINAL)||(LA43_0 >= FLOAT && LA43_0 <= FOR)||(LA43_0 >= IDENTIFIER && LA43_0 <= IF)||(LA43_0 >= INT && LA43_0 <= INTLITERAL)||LA43_0==LBRACE||(LA43_0 >= LONG && LA43_0 <= LT)||(LA43_0 >= MONKEYS_AT && LA43_0 <= NULL)||LA43_0==PLUS||(LA43_0 >= PLUSPLUS && LA43_0 <= PUBLIC)||LA43_0==RETURN||(LA43_0 >= SEMI && LA43_0 <= SHORT)||(LA43_0 >= STATIC && LA43_0 <= SUB)||(LA43_0 >= SUBSUB && LA43_0 <= SYNCHRONIZED)||(LA43_0 >= THIS && LA43_0 <= THROW)||(LA43_0 >= TILDE && LA43_0 <= WHILE)) ) {
                            alt43=1;
                        }


                        switch (alt43) {
                    	case 1 :
                    	    // Java.g:586:10: t54= blockStatement
                    	    {
                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("blockStatement"));}

                    	    pushFollow(FOLLOW_blockStatement_in_methodDeclaration2368);
                    	    t54=blockStatement();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t54!=null?((Token)t54.start):null), (t54!=null?((Token)t54.stop):null));}

                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);


                    // Java.g:588:9: ( '}' )
                    // Java.g:588:10: '}'
                    {
                    match(input,RBRACE,FOLLOW_RBRACE_in_methodDeclaration2392); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:589:9: t55= modifiers (t56= typeParameters )? (t57= type | 'void' ) IDENTIFIER t58= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t59= qualifiedNameList )? (t60= block | ( ';' ) )
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

                    pushFollow(FOLLOW_modifiers_in_methodDeclaration2408);
                    t55=modifiers();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t55!=null?((Token)t55.start):null), (t55!=null?((Token)t55.stop):null));}

                    // Java.g:590:9: (t56= typeParameters )?
                    int alt44=2;
                    int LA44_0 = input.LA(1);

                    if ( (LA44_0==LT) ) {
                        alt44=1;
                    }
                    switch (alt44) {
                        case 1 :
                            // Java.g:590:10: t56= typeParameters
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameters"));}

                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration2425);
                            t56=typeParameters();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t56!=null?((Token)t56.start):null), (t56!=null?((Token)t56.stop):null));}

                            }
                            break;

                    }


                    // Java.g:592:9: (t57= type | 'void' )
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==BOOLEAN||LA45_0==BYTE||LA45_0==CHAR||LA45_0==DOUBLE||LA45_0==FLOAT||LA45_0==IDENTIFIER||LA45_0==INT||LA45_0==LONG||LA45_0==SHORT) ) {
                        alt45=1;
                    }
                    else if ( (LA45_0==VOID) ) {
                        alt45=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 45, 0, input);

                        throw nvae;

                    }
                    switch (alt45) {
                        case 1 :
                            // Java.g:592:10: t57= type
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                            pushFollow(FOLLOW_type_in_methodDeclaration2453);
                            t57=type();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t57!=null?((Token)t57.start):null), (t57!=null?((Token)t57.stop):null));}

                            }
                            break;
                        case 2 :
                            // Java.g:593:13: 'void'
                            {
                            match(input,VOID,FOLLOW_VOID_in_methodDeclaration2469); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'void'",input.LT(-1));}

                            }
                            break;

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration2490); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("formalParameters"));}

                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration2506);
                    t58=formalParameters();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t58!=null?((Token)t58.start):null), (t58!=null?((Token)t58.stop):null));}

                    // Java.g:597:9: ( ( '[' ) ( ']' ) )*
                    loop46:
                    do {
                        int alt46=2;
                        int LA46_0 = input.LA(1);

                        if ( (LA46_0==LBRACKET) ) {
                            alt46=1;
                        }


                        switch (alt46) {
                    	case 1 :
                    	    // Java.g:597:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:597:10: ( '[' )
                    	    // Java.g:597:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_methodDeclaration2520); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:597:48: ( ']' )
                    	    // Java.g:597:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_methodDeclaration2525); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop46;
                        }
                    } while (true);


                    // Java.g:599:9: ( 'throws' t59= qualifiedNameList )?
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==THROWS) ) {
                        alt47=1;
                    }
                    switch (alt47) {
                        case 1 :
                            // Java.g:599:10: 'throws' t59= qualifiedNameList
                            {
                            match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration2549); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'throws'",input.LT(-1));}

                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedNameList"));}

                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration2556);
                            t59=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t59!=null?((Token)t59.start):null), (t59!=null?((Token)t59.stop):null));}

                            }
                            break;

                    }


                    // Java.g:601:9: (t60= block | ( ';' ) )
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==LBRACE) ) {
                        alt48=1;
                    }
                    else if ( (LA48_0==SEMI) ) {
                        alt48=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 48, 0, input);

                        throw nvae;

                    }
                    switch (alt48) {
                        case 1 :
                            // Java.g:602:13: t60= block
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                            pushFollow(FOLLOW_block_in_methodDeclaration2617);
                            t60=block();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t60!=null?((Token)t60.start):null), (t60!=null?((Token)t60.stop):null));}

                            }
                            break;
                        case 2 :
                            // Java.g:603:13: ( ';' )
                            {
                            // Java.g:603:13: ( ';' )
                            // Java.g:603:14: ';'
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_methodDeclaration2634); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                            }


                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 26, methodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "methodDeclaration"


    public static class fieldDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "fieldDeclaration"
    // Java.g:608:1: fieldDeclaration :t61= modifiers t62= type t63= variableDeclarator ( ( ',' ) t64= variableDeclarator )* ( ';' ) ;
    public final fieldDeclaration_return fieldDeclaration() throws RecognitionException {
        fieldDeclaration_return retval = new fieldDeclaration_return();
        retval.start = input.LT(1);

        int fieldDeclaration_StartIndex = input.index();

        modifiers_return t61 =null;

        type_return t62 =null;

        variableDeclarator_return t63 =null;

        variableDeclarator_return t64 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return retval; }

            // Java.g:609:5: (t61= modifiers t62= type t63= variableDeclarator ( ( ',' ) t64= variableDeclarator )* ( ';' ) )
            // Java.g:609:9: t61= modifiers t62= type t63= variableDeclarator ( ( ',' ) t64= variableDeclarator )* ( ';' )
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_fieldDeclaration2672);
            t61=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t61!=null?((Token)t61.start):null), (t61!=null?((Token)t61.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_fieldDeclaration2688);
            t62=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t62!=null?((Token)t62.start):null), (t62!=null?((Token)t62.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2704);
            t63=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t63!=null?((Token)t63.start):null), (t63!=null?((Token)t63.stop):null));}

            // Java.g:612:9: ( ( ',' ) t64= variableDeclarator )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==COMMA) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // Java.g:612:10: ( ',' ) t64= variableDeclarator
            	    {
            	    // Java.g:612:10: ( ',' )
            	    // Java.g:612:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_fieldDeclaration2718); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            	    pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2726);
            	    t64=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t64!=null?((Token)t64.start):null), (t64!=null?((Token)t64.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);


            // Java.g:614:9: ( ';' )
            // Java.g:614:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_fieldDeclaration2750); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 27, fieldDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "fieldDeclaration"


    public static class variableDeclarator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "variableDeclarator"
    // Java.g:617:1: variableDeclarator : IDENTIFIER ( ( '[' ) ( ']' ) )* ( ( '=' ) t65= variableInitializer )? ;
    public final variableDeclarator_return variableDeclarator() throws RecognitionException {
        variableDeclarator_return retval = new variableDeclarator_return();
        retval.start = input.LT(1);

        int variableDeclarator_StartIndex = input.index();

        variableInitializer_return t65 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return retval; }

            // Java.g:618:5: ( IDENTIFIER ( ( '[' ) ( ']' ) )* ( ( '=' ) t65= variableInitializer )? )
            // Java.g:618:9: IDENTIFIER ( ( '[' ) ( ']' ) )* ( ( '=' ) t65= variableInitializer )?
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableDeclarator2772); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:619:9: ( ( '[' ) ( ']' ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==LBRACKET) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // Java.g:619:10: ( '[' ) ( ']' )
            	    {
            	    // Java.g:619:10: ( '[' )
            	    // Java.g:619:11: '['
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_variableDeclarator2786); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

            	    }


            	    // Java.g:619:48: ( ']' )
            	    // Java.g:619:49: ']'
            	    {
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_variableDeclarator2791); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

            	    }


            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            // Java.g:621:9: ( ( '=' ) t65= variableInitializer )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==EQ) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // Java.g:621:10: ( '=' ) t65= variableInitializer
                    {
                    // Java.g:621:10: ( '=' )
                    // Java.g:621:11: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclarator2816); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableInitializer"));}

                    pushFollow(FOLLOW_variableInitializer_in_variableDeclarator2824);
                    t65=variableInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t65!=null?((Token)t65.start):null), (t65!=null?((Token)t65.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 28, variableDeclarator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableDeclarator"


    public static class interfaceBodyDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "interfaceBodyDeclaration"
    // Java.g:628:1: interfaceBodyDeclaration : (t66= interfaceFieldDeclaration |t67= interfaceMethodDeclaration |t68= interfaceDeclaration |t69= classDeclaration | ( ';' ) );
    public final interfaceBodyDeclaration_return interfaceBodyDeclaration() throws RecognitionException {
        interfaceBodyDeclaration_return retval = new interfaceBodyDeclaration_return();
        retval.start = input.LT(1);

        int interfaceBodyDeclaration_StartIndex = input.index();

        interfaceFieldDeclaration_return t66 =null;

        interfaceMethodDeclaration_return t67 =null;

        interfaceDeclaration_return t68 =null;

        classDeclaration_return t69 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return retval; }

            // Java.g:629:5: (t66= interfaceFieldDeclaration |t67= interfaceMethodDeclaration |t68= interfaceDeclaration |t69= classDeclaration | ( ';' ) )
            int alt53=5;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA53_1 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA53_2 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA53_3 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA53_4 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA53_5 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA53_6 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA53_7 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA53_8 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA53_9 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA53_10 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA53_11 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA53_12 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else if ( (synpred70_Java()) ) {
                    alt53=3;
                }
                else if ( (synpred71_Java()) ) {
                    alt53=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA53_13 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
                {
                int LA53_14 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 14, input);

                    throw nvae;

                }
                }
                break;
            case CHAR:
                {
                int LA53_15 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 15, input);

                    throw nvae;

                }
                }
                break;
            case BYTE:
                {
                int LA53_16 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 16, input);

                    throw nvae;

                }
                }
                break;
            case SHORT:
                {
                int LA53_17 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 17, input);

                    throw nvae;

                }
                }
                break;
            case INT:
                {
                int LA53_18 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 18, input);

                    throw nvae;

                }
                }
                break;
            case LONG:
                {
                int LA53_19 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 19, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA53_20 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 20, input);

                    throw nvae;

                }
                }
                break;
            case DOUBLE:
                {
                int LA53_21 = input.LA(2);

                if ( (synpred68_Java()) ) {
                    alt53=1;
                }
                else if ( (synpred69_Java()) ) {
                    alt53=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 21, input);

                    throw nvae;

                }
                }
                break;
            case LT:
            case VOID:
                {
                alt53=2;
                }
                break;
            case INTERFACE:
                {
                alt53=3;
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt53=4;
                }
                break;
            case SEMI:
                {
                alt53=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                throw nvae;

            }

            switch (alt53) {
                case 1 :
                    // Java.g:630:9: t66= interfaceFieldDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceFieldDeclaration"));}

                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration2869);
                    t66=interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t66!=null?((Token)t66.start):null), (t66!=null?((Token)t66.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:631:9: t67= interfaceMethodDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceMethodDeclaration"));}

                    pushFollow(FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration2885);
                    t67=interfaceMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t67!=null?((Token)t67.start):null), (t67!=null?((Token)t67.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:632:9: t68= interfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceDeclaration"));}

                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration2901);
                    t68=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t68!=null?((Token)t68.start):null), (t68!=null?((Token)t68.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:633:9: t69= classDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classDeclaration"));}

                    pushFollow(FOLLOW_classDeclaration_in_interfaceBodyDeclaration2917);
                    t69=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t69!=null?((Token)t69.start):null), (t69!=null?((Token)t69.stop):null));}

                    }
                    break;
                case 5 :
                    // Java.g:634:9: ( ';' )
                    {
                    // Java.g:634:9: ( ';' )
                    // Java.g:634:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_interfaceBodyDeclaration2930); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 29, interfaceBodyDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceBodyDeclaration"


    public static class interfaceMethodDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "interfaceMethodDeclaration"
    // Java.g:637:1: interfaceMethodDeclaration :t70= modifiers (t71= typeParameters )? (t72= type | 'void' ) IDENTIFIER t73= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t74= qualifiedNameList )? ( ';' ) ;
    public final interfaceMethodDeclaration_return interfaceMethodDeclaration() throws RecognitionException {
        interfaceMethodDeclaration_return retval = new interfaceMethodDeclaration_return();
        retval.start = input.LT(1);

        int interfaceMethodDeclaration_StartIndex = input.index();

        modifiers_return t70 =null;

        typeParameters_return t71 =null;

        type_return t72 =null;

        formalParameters_return t73 =null;

        qualifiedNameList_return t74 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return retval; }

            // Java.g:638:5: (t70= modifiers (t71= typeParameters )? (t72= type | 'void' ) IDENTIFIER t73= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t74= qualifiedNameList )? ( ';' ) )
            // Java.g:638:9: t70= modifiers (t71= typeParameters )? (t72= type | 'void' ) IDENTIFIER t73= formalParameters ( ( '[' ) ( ']' ) )* ( 'throws' t74= qualifiedNameList )? ( ';' )
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_interfaceMethodDeclaration2956);
            t70=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t70!=null?((Token)t70.start):null), (t70!=null?((Token)t70.stop):null));}

            // Java.g:639:9: (t71= typeParameters )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==LT) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // Java.g:639:10: t71= typeParameters
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeParameters"));}

                    pushFollow(FOLLOW_typeParameters_in_interfaceMethodDeclaration2973);
                    t71=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t71!=null?((Token)t71.start):null), (t71!=null?((Token)t71.stop):null));}

                    }
                    break;

            }


            // Java.g:641:9: (t72= type | 'void' )
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==BOOLEAN||LA55_0==BYTE||LA55_0==CHAR||LA55_0==DOUBLE||LA55_0==FLOAT||LA55_0==IDENTIFIER||LA55_0==INT||LA55_0==LONG||LA55_0==SHORT) ) {
                alt55=1;
            }
            else if ( (LA55_0==VOID) ) {
                alt55=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;

            }
            switch (alt55) {
                case 1 :
                    // Java.g:641:10: t72= type
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_interfaceMethodDeclaration3001);
                    t72=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t72!=null?((Token)t72.start):null), (t72!=null?((Token)t72.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:642:10: 'void'
                    {
                    match(input,VOID,FOLLOW_VOID_in_interfaceMethodDeclaration3014); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'void'",input.LT(-1));}

                    }
                    break;

            }


            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration3035); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("formalParameters"));}

            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaration3051);
            t73=formalParameters();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t73!=null?((Token)t73.start):null), (t73!=null?((Token)t73.stop):null));}

            // Java.g:646:9: ( ( '[' ) ( ']' ) )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==LBRACKET) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // Java.g:646:10: ( '[' ) ( ']' )
            	    {
            	    // Java.g:646:10: ( '[' )
            	    // Java.g:646:11: '['
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_interfaceMethodDeclaration3065); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

            	    }


            	    // Java.g:646:48: ( ']' )
            	    // Java.g:646:49: ']'
            	    {
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_interfaceMethodDeclaration3070); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

            	    }


            	    }
            	    break;

            	default :
            	    break loop56;
                }
            } while (true);


            // Java.g:648:9: ( 'throws' t74= qualifiedNameList )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==THROWS) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // Java.g:648:10: 'throws' t74= qualifiedNameList
                    {
                    match(input,THROWS,FOLLOW_THROWS_in_interfaceMethodDeclaration3094); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'throws'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedNameList"));}

                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration3101);
                    t74=qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t74!=null?((Token)t74.start):null), (t74!=null?((Token)t74.stop):null));}

                    }
                    break;

            }


            // Java.g:649:12: ( ';' )
            // Java.g:649:13: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_interfaceMethodDeclaration3117); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 30, interfaceMethodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceMethodDeclaration"


    public static class interfaceFieldDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "interfaceFieldDeclaration"
    // Java.g:657:1: interfaceFieldDeclaration :t75= modifiers t76= type t77= variableDeclarator ( ( ',' ) t78= variableDeclarator )* ( ';' ) ;
    public final interfaceFieldDeclaration_return interfaceFieldDeclaration() throws RecognitionException {
        interfaceFieldDeclaration_return retval = new interfaceFieldDeclaration_return();
        retval.start = input.LT(1);

        int interfaceFieldDeclaration_StartIndex = input.index();

        modifiers_return t75 =null;

        type_return t76 =null;

        variableDeclarator_return t77 =null;

        variableDeclarator_return t78 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return retval; }

            // Java.g:658:5: (t75= modifiers t76= type t77= variableDeclarator ( ( ',' ) t78= variableDeclarator )* ( ';' ) )
            // Java.g:658:9: t75= modifiers t76= type t77= variableDeclarator ( ( ',' ) t78= variableDeclarator )* ( ';' )
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_interfaceFieldDeclaration3145);
            t75=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t75!=null?((Token)t75.start):null), (t75!=null?((Token)t75.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_interfaceFieldDeclaration3153);
            t76=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t76!=null?((Token)t76.start):null), (t76!=null?((Token)t76.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3161);
            t77=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t77!=null?((Token)t77.start):null), (t77!=null?((Token)t77.stop):null));}

            // Java.g:659:9: ( ( ',' ) t78= variableDeclarator )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==COMMA) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // Java.g:659:10: ( ',' ) t78= variableDeclarator
            	    {
            	    // Java.g:659:10: ( ',' )
            	    // Java.g:659:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_interfaceFieldDeclaration3175); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            	    pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3183);
            	    t78=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t78!=null?((Token)t78.start):null), (t78!=null?((Token)t78.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);


            // Java.g:661:9: ( ';' )
            // Java.g:661:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_interfaceFieldDeclaration3207); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 31, interfaceFieldDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceFieldDeclaration"


    public static class type_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "type"
    // Java.g:665:1: type : (t79= classOrInterfaceType ( ( '[' ) ( ']' ) )* |t80= primitiveType ( ( '[' ) ( ']' ) )* );
    public final type_return type() throws RecognitionException {
        type_return retval = new type_return();
        retval.start = input.LT(1);

        int type_StartIndex = input.index();

        classOrInterfaceType_return t79 =null;

        primitiveType_return t80 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return retval; }

            // Java.g:666:5: (t79= classOrInterfaceType ( ( '[' ) ( ']' ) )* |t80= primitiveType ( ( '[' ) ( ']' ) )* )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==IDENTIFIER) ) {
                alt61=1;
            }
            else if ( (LA61_0==BOOLEAN||LA61_0==BYTE||LA61_0==CHAR||LA61_0==DOUBLE||LA61_0==FLOAT||LA61_0==INT||LA61_0==LONG||LA61_0==SHORT) ) {
                alt61=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;

            }
            switch (alt61) {
                case 1 :
                    // Java.g:666:9: t79= classOrInterfaceType ( ( '[' ) ( ']' ) )*
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

                    pushFollow(FOLLOW_classOrInterfaceType_in_type3234);
                    t79=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t79!=null?((Token)t79.start):null), (t79!=null?((Token)t79.stop):null));}

                    // Java.g:667:9: ( ( '[' ) ( ']' ) )*
                    loop59:
                    do {
                        int alt59=2;
                        int LA59_0 = input.LA(1);

                        if ( (LA59_0==LBRACKET) ) {
                            alt59=1;
                        }


                        switch (alt59) {
                    	case 1 :
                    	    // Java.g:667:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:667:10: ( '[' )
                    	    // Java.g:667:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_type3248); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:667:48: ( ']' )
                    	    // Java.g:667:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_type3253); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop59;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // Java.g:669:9: t80= primitiveType ( ( '[' ) ( ']' ) )*
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primitiveType"));}

                    pushFollow(FOLLOW_primitiveType_in_type3280);
                    t80=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t80!=null?((Token)t80.start):null), (t80!=null?((Token)t80.stop):null));}

                    // Java.g:670:9: ( ( '[' ) ( ']' ) )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==LBRACKET) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // Java.g:670:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:670:10: ( '[' )
                    	    // Java.g:670:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_type3294); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:670:48: ( ']' )
                    	    // Java.g:670:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_type3299); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop60;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 32, type_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "type"


    public static class classOrInterfaceType_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classOrInterfaceType"
    // Java.g:675:1: classOrInterfaceType : IDENTIFIER (t81= typeArgumentsOrDiamond )? ( ( '.' ) IDENTIFIER (t82= typeArgumentsOrDiamond )? )* ;
    public final classOrInterfaceType_return classOrInterfaceType() throws RecognitionException {
        classOrInterfaceType_return retval = new classOrInterfaceType_return();
        retval.start = input.LT(1);

        int classOrInterfaceType_StartIndex = input.index();

        typeArgumentsOrDiamond_return t81 =null;

        typeArgumentsOrDiamond_return t82 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return retval; }

            // Java.g:676:5: ( IDENTIFIER (t81= typeArgumentsOrDiamond )? ( ( '.' ) IDENTIFIER (t82= typeArgumentsOrDiamond )? )* )
            // Java.g:676:9: IDENTIFIER (t81= typeArgumentsOrDiamond )? ( ( '.' ) IDENTIFIER (t82= typeArgumentsOrDiamond )? )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType3333); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:677:9: (t81= typeArgumentsOrDiamond )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==LT) ) {
                int LA62_1 = input.LA(2);

                if ( (LA62_1==BOOLEAN||LA62_1==BYTE||LA62_1==CHAR||LA62_1==DOUBLE||LA62_1==FLOAT||LA62_1==GT||LA62_1==IDENTIFIER||LA62_1==INT||LA62_1==LONG||LA62_1==QUES||LA62_1==SHORT) ) {
                    alt62=1;
                }
            }
            switch (alt62) {
                case 1 :
                    // Java.g:677:10: t81= typeArgumentsOrDiamond
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArgumentsOrDiamond"));}

                    pushFollow(FOLLOW_typeArgumentsOrDiamond_in_classOrInterfaceType3350);
                    t81=typeArgumentsOrDiamond();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t81!=null?((Token)t81.start):null), (t81!=null?((Token)t81.stop):null));}

                    }
                    break;

            }


            // Java.g:679:9: ( ( '.' ) IDENTIFIER (t82= typeArgumentsOrDiamond )? )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==DOT) ) {
                    alt64=1;
                }


                switch (alt64) {
            	case 1 :
            	    // Java.g:679:10: ( '.' ) IDENTIFIER (t82= typeArgumentsOrDiamond )?
            	    {
            	    // Java.g:679:10: ( '.' )
            	    // Java.g:679:11: '.'
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_classOrInterfaceType3375); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

            	    }


            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType3379); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            	    // Java.g:680:13: (t82= typeArgumentsOrDiamond )?
            	    int alt63=2;
            	    int LA63_0 = input.LA(1);

            	    if ( (LA63_0==LT) ) {
            	        int LA63_1 = input.LA(2);

            	        if ( (LA63_1==BOOLEAN||LA63_1==BYTE||LA63_1==CHAR||LA63_1==DOUBLE||LA63_1==FLOAT||LA63_1==GT||LA63_1==IDENTIFIER||LA63_1==INT||LA63_1==LONG||LA63_1==QUES||LA63_1==SHORT) ) {
            	            alt63=1;
            	        }
            	    }
            	    switch (alt63) {
            	        case 1 :
            	            // Java.g:680:14: t82= typeArgumentsOrDiamond
            	            {
            	            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArgumentsOrDiamond"));}

            	            pushFollow(FOLLOW_typeArgumentsOrDiamond_in_classOrInterfaceType3400);
            	            t82=typeArgumentsOrDiamond();

            	            state._fsp--;
            	            if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.popTop().setTextRange((t82!=null?((Token)t82.start):null), (t82!=null?((Token)t82.stop):null));}

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 33, classOrInterfaceType_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classOrInterfaceType"


    public static class primitiveType_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "primitiveType"
    // Java.g:685:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final primitiveType_return primitiveType() throws RecognitionException {
        primitiveType_return retval = new primitiveType_return();
        retval.start = input.LT(1);

        int primitiveType_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return retval; }

            // Java.g:686:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            int alt65=8;
            switch ( input.LA(1) ) {
            case BOOLEAN:
                {
                alt65=1;
                }
                break;
            case CHAR:
                {
                alt65=2;
                }
                break;
            case BYTE:
                {
                alt65=3;
                }
                break;
            case SHORT:
                {
                alt65=4;
                }
                break;
            case INT:
                {
                alt65=5;
                }
                break;
            case LONG:
                {
                alt65=6;
                }
                break;
            case FLOAT:
                {
                alt65=7;
                }
                break;
            case DOUBLE:
                {
                alt65=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;

            }

            switch (alt65) {
                case 1 :
                    // Java.g:686:9: 'boolean'
                    {
                    match(input,BOOLEAN,FOLLOW_BOOLEAN_in_primitiveType3449); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'boolean'",input.LT(-1));}

                    }
                    break;
                case 2 :
                    // Java.g:687:9: 'char'
                    {
                    match(input,CHAR,FOLLOW_CHAR_in_primitiveType3460); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'char'",input.LT(-1));}

                    }
                    break;
                case 3 :
                    // Java.g:688:9: 'byte'
                    {
                    match(input,BYTE,FOLLOW_BYTE_in_primitiveType3471); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'byte'",input.LT(-1));}

                    }
                    break;
                case 4 :
                    // Java.g:689:9: 'short'
                    {
                    match(input,SHORT,FOLLOW_SHORT_in_primitiveType3482); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'short'",input.LT(-1));}

                    }
                    break;
                case 5 :
                    // Java.g:690:9: 'int'
                    {
                    match(input,INT,FOLLOW_INT_in_primitiveType3493); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'int'",input.LT(-1));}

                    }
                    break;
                case 6 :
                    // Java.g:691:9: 'long'
                    {
                    match(input,LONG,FOLLOW_LONG_in_primitiveType3504); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'long'",input.LT(-1));}

                    }
                    break;
                case 7 :
                    // Java.g:692:9: 'float'
                    {
                    match(input,FLOAT,FOLLOW_FLOAT_in_primitiveType3515); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'float'",input.LT(-1));}

                    }
                    break;
                case 8 :
                    // Java.g:693:9: 'double'
                    {
                    match(input,DOUBLE,FOLLOW_DOUBLE_in_primitiveType3526); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'double'",input.LT(-1));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 34, primitiveType_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "primitiveType"


    public static class typeArguments_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeArguments"
    // Java.g:696:1: typeArguments : ( '<' ) t83= typeArgument ( ( ',' ) t84= typeArgument )* ( '>' ) ;
    public final typeArguments_return typeArguments() throws RecognitionException {
        typeArguments_return retval = new typeArguments_return();
        retval.start = input.LT(1);

        int typeArguments_StartIndex = input.index();

        typeArgument_return t83 =null;

        typeArgument_return t84 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return retval; }

            // Java.g:697:5: ( ( '<' ) t83= typeArgument ( ( ',' ) t84= typeArgument )* ( '>' ) )
            // Java.g:697:9: ( '<' ) t83= typeArgument ( ( ',' ) t84= typeArgument )* ( '>' )
            {
            // Java.g:697:9: ( '<' )
            // Java.g:697:10: '<'
            {
            match(input,LT,FOLLOW_LT_in_typeArguments3548); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArgument"));}

            pushFollow(FOLLOW_typeArgument_in_typeArguments3556);
            t83=typeArgument();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t83!=null?((Token)t83.start):null), (t83!=null?((Token)t83.stop):null));}

            // Java.g:698:9: ( ( ',' ) t84= typeArgument )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==COMMA) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // Java.g:698:10: ( ',' ) t84= typeArgument
            	    {
            	    // Java.g:698:10: ( ',' )
            	    // Java.g:698:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeArguments3570); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArgument"));}

            	    pushFollow(FOLLOW_typeArgument_in_typeArguments3578);
            	    t84=typeArgument();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t84!=null?((Token)t84.start):null), (t84!=null?((Token)t84.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);


            // Java.g:700:9: ( '>' )
            // Java.g:700:10: '>'
            {
            match(input,GT,FOLLOW_GT_in_typeArguments3603); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 35, typeArguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeArguments"


    public static class typeArgument_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeArgument"
    // Java.g:703:1: typeArgument : (t85= type | ( '?' ) ( ( 'extends' | 'super' ) t86= type )? );
    public final typeArgument_return typeArgument() throws RecognitionException {
        typeArgument_return retval = new typeArgument_return();
        retval.start = input.LT(1);

        int typeArgument_StartIndex = input.index();

        type_return t85 =null;

        type_return t86 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return retval; }

            // Java.g:704:5: (t85= type | ( '?' ) ( ( 'extends' | 'super' ) t86= type )? )
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==BOOLEAN||LA69_0==BYTE||LA69_0==CHAR||LA69_0==DOUBLE||LA69_0==FLOAT||LA69_0==IDENTIFIER||LA69_0==INT||LA69_0==LONG||LA69_0==SHORT) ) {
                alt69=1;
            }
            else if ( (LA69_0==QUES) ) {
                alt69=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;

            }
            switch (alt69) {
                case 1 :
                    // Java.g:704:9: t85= type
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_typeArgument3629);
                    t85=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t85!=null?((Token)t85.start):null), (t85!=null?((Token)t85.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:705:9: ( '?' ) ( ( 'extends' | 'super' ) t86= type )?
                    {
                    // Java.g:705:9: ( '?' )
                    // Java.g:705:10: '?'
                    {
                    match(input,QUES,FOLLOW_QUES_in_typeArgument3642); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'?'",input.LT(-1));}

                    }


                    // Java.g:706:9: ( ( 'extends' | 'super' ) t86= type )?
                    int alt68=2;
                    int LA68_0 = input.LA(1);

                    if ( (LA68_0==EXTENDS||LA68_0==SUPER) ) {
                        alt68=1;
                    }
                    switch (alt68) {
                        case 1 :
                            // Java.g:707:13: ( 'extends' | 'super' ) t86= type
                            {
                            // Java.g:707:13: ( 'extends' | 'super' )
                            int alt67=2;
                            int LA67_0 = input.LA(1);

                            if ( (LA67_0==EXTENDS) ) {
                                alt67=1;
                            }
                            else if ( (LA67_0==SUPER) ) {
                                alt67=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 67, 0, input);

                                throw nvae;

                            }
                            switch (alt67) {
                                case 1 :
                                    // Java.g:707:14: 'extends'
                                    {
                                    match(input,EXTENDS,FOLLOW_EXTENDS_in_typeArgument3669); if (state.failed) return retval;

                                    if ( state.backtracking==0 ) {T.addLeaf("'extends'",input.LT(-1));}

                                    }
                                    break;
                                case 2 :
                                    // Java.g:708:14: 'super'
                                    {
                                    match(input,SUPER,FOLLOW_SUPER_in_typeArgument3685); if (state.failed) return retval;

                                    if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                                    }
                                    break;

                            }


                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                            pushFollow(FOLLOW_type_in_typeArgument3718);
                            t86=type();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t86!=null?((Token)t86.start):null), (t86!=null?((Token)t86.stop):null));}

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 36, typeArgument_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeArgument"


    public static class qualifiedNameList_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "qualifiedNameList"
    // Java.g:714:1: qualifiedNameList :t87= qualifiedName ( ( ',' ) t88= qualifiedName )* ;
    public final qualifiedNameList_return qualifiedNameList() throws RecognitionException {
        qualifiedNameList_return retval = new qualifiedNameList_return();
        retval.start = input.LT(1);

        int qualifiedNameList_StartIndex = input.index();

        qualifiedName_return t87 =null;

        qualifiedName_return t88 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return retval; }

            // Java.g:715:5: (t87= qualifiedName ( ( ',' ) t88= qualifiedName )* )
            // Java.g:715:9: t87= qualifiedName ( ( ',' ) t88= qualifiedName )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedName"));}

            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3755);
            t87=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t87!=null?((Token)t87.start):null), (t87!=null?((Token)t87.stop):null));}

            // Java.g:716:9: ( ( ',' ) t88= qualifiedName )*
            loop70:
            do {
                int alt70=2;
                int LA70_0 = input.LA(1);

                if ( (LA70_0==COMMA) ) {
                    alt70=1;
                }


                switch (alt70) {
            	case 1 :
            	    // Java.g:716:10: ( ',' ) t88= qualifiedName
            	    {
            	    // Java.g:716:10: ( ',' )
            	    // Java.g:716:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_qualifiedNameList3769); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedName"));}

            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3777);
            	    t88=qualifiedName();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t88!=null?((Token)t88.start):null), (t88!=null?((Token)t88.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop70;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 37, qualifiedNameList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "qualifiedNameList"


    public static class formalParameters_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "formalParameters"
    // Java.g:720:1: formalParameters : ( '(' ) (t89= formalParameterDecls )? ( ')' ) ;
    public final formalParameters_return formalParameters() throws RecognitionException {
        formalParameters_return retval = new formalParameters_return();
        retval.start = input.LT(1);

        int formalParameters_StartIndex = input.index();

        formalParameterDecls_return t89 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return retval; }

            // Java.g:721:5: ( ( '(' ) (t89= formalParameterDecls )? ( ')' ) )
            // Java.g:721:9: ( '(' ) (t89= formalParameterDecls )? ( ')' )
            {
            // Java.g:721:9: ( '(' )
            // Java.g:721:10: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3811); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            // Java.g:722:9: (t89= formalParameterDecls )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==BOOLEAN||LA71_0==BYTE||LA71_0==CHAR||LA71_0==DOUBLE||LA71_0==FINAL||LA71_0==FLOAT||LA71_0==IDENTIFIER||LA71_0==INT||LA71_0==LONG||LA71_0==MONKEYS_AT||LA71_0==SHORT) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // Java.g:722:10: t89= formalParameterDecls
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("formalParameterDecls"));}

                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters3828);
                    t89=formalParameterDecls();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t89!=null?((Token)t89.start):null), (t89!=null?((Token)t89.stop):null));}

                    }
                    break;

            }


            // Java.g:724:9: ( ')' )
            // Java.g:724:10: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3853); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 38, formalParameters_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameters"


    public static class formalParameterDecls_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "formalParameterDecls"
    // Java.g:727:1: formalParameterDecls : (t90= ellipsisParameterDecl |t91= normalParameterDecl ( ( ',' ) t92= normalParameterDecl )* | (t93= normalParameterDecl ( ',' ) )+ t94= ellipsisParameterDecl );
    public final formalParameterDecls_return formalParameterDecls() throws RecognitionException {
        formalParameterDecls_return retval = new formalParameterDecls_return();
        retval.start = input.LT(1);

        int formalParameterDecls_StartIndex = input.index();

        ellipsisParameterDecl_return t90 =null;

        normalParameterDecl_return t91 =null;

        normalParameterDecl_return t92 =null;

        normalParameterDecl_return t93 =null;

        ellipsisParameterDecl_return t94 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return retval; }

            // Java.g:728:5: (t90= ellipsisParameterDecl |t91= normalParameterDecl ( ( ',' ) t92= normalParameterDecl )* | (t93= normalParameterDecl ( ',' ) )+ t94= ellipsisParameterDecl )
            int alt74=3;
            switch ( input.LA(1) ) {
            case FINAL:
                {
                int LA74_1 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 1, input);

                    throw nvae;

                }
                }
                break;
            case MONKEYS_AT:
                {
                int LA74_2 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 2, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA74_3 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
                {
                int LA74_4 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 4, input);

                    throw nvae;

                }
                }
                break;
            case CHAR:
                {
                int LA74_5 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 5, input);

                    throw nvae;

                }
                }
                break;
            case BYTE:
                {
                int LA74_6 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 6, input);

                    throw nvae;

                }
                }
                break;
            case SHORT:
                {
                int LA74_7 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 7, input);

                    throw nvae;

                }
                }
                break;
            case INT:
                {
                int LA74_8 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 8, input);

                    throw nvae;

                }
                }
                break;
            case LONG:
                {
                int LA74_9 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 9, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA74_10 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 10, input);

                    throw nvae;

                }
                }
                break;
            case DOUBLE:
                {
                int LA74_11 = input.LA(2);

                if ( (synpred96_Java()) ) {
                    alt74=1;
                }
                else if ( (synpred98_Java()) ) {
                    alt74=2;
                }
                else if ( (true) ) {
                    alt74=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 74, 11, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 74, 0, input);

                throw nvae;

            }

            switch (alt74) {
                case 1 :
                    // Java.g:728:9: t90= ellipsisParameterDecl
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("ellipsisParameterDecl"));}

                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3879);
                    t90=ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t90!=null?((Token)t90.start):null), (t90!=null?((Token)t90.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:729:9: t91= normalParameterDecl ( ( ',' ) t92= normalParameterDecl )*
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalParameterDecl"));}

                    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3895);
                    t91=normalParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t91!=null?((Token)t91.start):null), (t91!=null?((Token)t91.stop):null));}

                    // Java.g:730:9: ( ( ',' ) t92= normalParameterDecl )*
                    loop72:
                    do {
                        int alt72=2;
                        int LA72_0 = input.LA(1);

                        if ( (LA72_0==COMMA) ) {
                            alt72=1;
                        }


                        switch (alt72) {
                    	case 1 :
                    	    // Java.g:730:10: ( ',' ) t92= normalParameterDecl
                    	    {
                    	    // Java.g:730:10: ( ',' )
                    	    // Java.g:730:11: ','
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls3909); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    	    }


                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalParameterDecl"));}

                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3917);
                    	    t92=normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t92!=null?((Token)t92.start):null), (t92!=null?((Token)t92.stop):null));}

                    	    }
                    	    break;

                    	default :
                    	    break loop72;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // Java.g:732:9: (t93= normalParameterDecl ( ',' ) )+ t94= ellipsisParameterDecl
                    {
                    // Java.g:732:9: (t93= normalParameterDecl ( ',' ) )+
                    int cnt73=0;
                    loop73:
                    do {
                        int alt73=2;
                        switch ( input.LA(1) ) {
                        case FINAL:
                            {
                            int LA73_1 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case MONKEYS_AT:
                            {
                            int LA73_2 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case IDENTIFIER:
                            {
                            int LA73_3 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case BOOLEAN:
                            {
                            int LA73_4 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case CHAR:
                            {
                            int LA73_5 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case BYTE:
                            {
                            int LA73_6 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case SHORT:
                            {
                            int LA73_7 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case INT:
                            {
                            int LA73_8 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case LONG:
                            {
                            int LA73_9 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case FLOAT:
                            {
                            int LA73_10 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;
                        case DOUBLE:
                            {
                            int LA73_11 = input.LA(2);

                            if ( (synpred99_Java()) ) {
                                alt73=1;
                            }


                            }
                            break;

                        }

                        switch (alt73) {
                    	case 1 :
                    	    // Java.g:732:10: t93= normalParameterDecl ( ',' )
                    	    {
                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalParameterDecl"));}

                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3945);
                    	    t93=normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t93!=null?((Token)t93.start):null), (t93!=null?((Token)t93.stop):null));}

                    	    // Java.g:733:9: ( ',' )
                    	    // Java.g:733:10: ','
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls3958); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt73 >= 1 ) break loop73;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(73, input);
                                throw eee;
                        }
                        cnt73++;
                    } while (true);


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("ellipsisParameterDecl"));}

                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3986);
                    t94=ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t94!=null?((Token)t94.start):null), (t94!=null?((Token)t94.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 39, formalParameterDecls_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameterDecls"


    public static class normalParameterDecl_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "normalParameterDecl"
    // Java.g:738:1: normalParameterDecl :t95= variableModifiers t96= type IDENTIFIER ( ( '[' ) ( ']' ) )* ;
    public final normalParameterDecl_return normalParameterDecl() throws RecognitionException {
        normalParameterDecl_return retval = new normalParameterDecl_return();
        retval.start = input.LT(1);

        int normalParameterDecl_StartIndex = input.index();

        variableModifiers_return t95 =null;

        type_return t96 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return retval; }

            // Java.g:739:5: (t95= variableModifiers t96= type IDENTIFIER ( ( '[' ) ( ']' ) )* )
            // Java.g:739:9: t95= variableModifiers t96= type IDENTIFIER ( ( '[' ) ( ']' ) )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

            pushFollow(FOLLOW_variableModifiers_in_normalParameterDecl4012);
            t95=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t95!=null?((Token)t95.start):null), (t95!=null?((Token)t95.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_normalParameterDecl4020);
            t96=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t96!=null?((Token)t96.start):null), (t96!=null?((Token)t96.stop):null));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalParameterDecl4024); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:740:9: ( ( '[' ) ( ']' ) )*
            loop75:
            do {
                int alt75=2;
                int LA75_0 = input.LA(1);

                if ( (LA75_0==LBRACKET) ) {
                    alt75=1;
                }


                switch (alt75) {
            	case 1 :
            	    // Java.g:740:10: ( '[' ) ( ']' )
            	    {
            	    // Java.g:740:10: ( '[' )
            	    // Java.g:740:11: '['
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_normalParameterDecl4038); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

            	    }


            	    // Java.g:740:48: ( ']' )
            	    // Java.g:740:49: ']'
            	    {
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_normalParameterDecl4043); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

            	    }


            	    }
            	    break;

            	default :
            	    break loop75;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 40, normalParameterDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalParameterDecl"


    public static class ellipsisParameterDecl_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "ellipsisParameterDecl"
    // Java.g:744:1: ellipsisParameterDecl :t97= variableModifiers t98= type '...' IDENTIFIER ;
    public final ellipsisParameterDecl_return ellipsisParameterDecl() throws RecognitionException {
        ellipsisParameterDecl_return retval = new ellipsisParameterDecl_return();
        retval.start = input.LT(1);

        int ellipsisParameterDecl_StartIndex = input.index();

        variableModifiers_return t97 =null;

        type_return t98 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return retval; }

            // Java.g:745:5: (t97= variableModifiers t98= type '...' IDENTIFIER )
            // Java.g:745:9: t97= variableModifiers t98= type '...' IDENTIFIER
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

            pushFollow(FOLLOW_variableModifiers_in_ellipsisParameterDecl4080);
            t97=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t97!=null?((Token)t97.start):null), (t97!=null?((Token)t97.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_ellipsisParameterDecl4096);
            t98=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t98!=null?((Token)t98.start):null), (t98!=null?((Token)t98.stop):null));}

            match(input,ELLIPSIS,FOLLOW_ELLIPSIS_in_ellipsisParameterDecl4101); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'...'",input.LT(-1));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_ellipsisParameterDecl4112); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 41, ellipsisParameterDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "ellipsisParameterDecl"


    public static class explicitConstructorInvocation_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "explicitConstructorInvocation"
    // Java.g:751:1: explicitConstructorInvocation : ( (t99= nonWildcardTypeArguments )? ( 'this' | 'super' ) t100= arguments ( ';' ) |t101= primary ( '.' ) (t102= nonWildcardTypeArguments )? 'super' t103= arguments ( ';' ) );
    public final explicitConstructorInvocation_return explicitConstructorInvocation() throws RecognitionException {
        explicitConstructorInvocation_return retval = new explicitConstructorInvocation_return();
        retval.start = input.LT(1);

        int explicitConstructorInvocation_StartIndex = input.index();

        nonWildcardTypeArguments_return t99 =null;

        arguments_return t100 =null;

        primary_return t101 =null;

        nonWildcardTypeArguments_return t102 =null;

        arguments_return t103 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return retval; }

            // Java.g:752:5: ( (t99= nonWildcardTypeArguments )? ( 'this' | 'super' ) t100= arguments ( ';' ) |t101= primary ( '.' ) (t102= nonWildcardTypeArguments )? 'super' t103= arguments ( ';' ) )
            int alt79=2;
            switch ( input.LA(1) ) {
            case LT:
                {
                alt79=1;
                }
                break;
            case THIS:
                {
                int LA79_2 = input.LA(2);

                if ( (synpred103_Java()) ) {
                    alt79=1;
                }
                else if ( (true) ) {
                    alt79=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 79, 2, input);

                    throw nvae;

                }
                }
                break;
            case SUPER:
                {
                int LA79_3 = input.LA(2);

                if ( (synpred103_Java()) ) {
                    alt79=1;
                }
                else if ( (true) ) {
                    alt79=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 79, 3, input);

                    throw nvae;

                }
                }
                break;
            case BINLITERAL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case TRUE:
            case VOID:
                {
                alt79=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 79, 0, input);

                throw nvae;

            }

            switch (alt79) {
                case 1 :
                    // Java.g:752:9: (t99= nonWildcardTypeArguments )? ( 'this' | 'super' ) t100= arguments ( ';' )
                    {
                    // Java.g:752:9: (t99= nonWildcardTypeArguments )?
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( (LA76_0==LT) ) {
                        alt76=1;
                    }
                    switch (alt76) {
                        case 1 :
                            // Java.g:752:10: t99= nonWildcardTypeArguments
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4140);
                            t99=nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t99!=null?((Token)t99.start):null), (t99!=null?((Token)t99.stop):null));}

                            }
                            break;

                    }


                    // Java.g:754:9: ( 'this' | 'super' )
                    int alt77=2;
                    int LA77_0 = input.LA(1);

                    if ( (LA77_0==THIS) ) {
                        alt77=1;
                    }
                    else if ( (LA77_0==SUPER) ) {
                        alt77=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 77, 0, input);

                        throw nvae;

                    }
                    switch (alt77) {
                        case 1 :
                            // Java.g:754:10: 'this'
                            {
                            match(input,THIS,FOLLOW_THIS_in_explicitConstructorInvocation4169); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'this'",input.LT(-1));}

                            }
                            break;
                        case 2 :
                            // Java.g:755:10: 'super'
                            {
                            match(input,SUPER,FOLLOW_SUPER_in_explicitConstructorInvocation4181); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                            }
                            break;

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation4206);
                    t100=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t100!=null?((Token)t100.start):null), (t100!=null?((Token)t100.stop):null));}

                    // Java.g:757:134: ( ';' )
                    // Java.g:757:135: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation4211); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:759:9: t101= primary ( '.' ) (t102= nonWildcardTypeArguments )? 'super' t103= arguments ( ';' )
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primary"));}

                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation4228);
                    t101=primary();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t101!=null?((Token)t101.start):null), (t101!=null?((Token)t101.stop):null));}

                    // Java.g:760:9: ( '.' )
                    // Java.g:760:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_explicitConstructorInvocation4241); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    // Java.g:761:9: (t102= nonWildcardTypeArguments )?
                    int alt78=2;
                    int LA78_0 = input.LA(1);

                    if ( (LA78_0==LT) ) {
                        alt78=1;
                    }
                    switch (alt78) {
                        case 1 :
                            // Java.g:761:10: t102= nonWildcardTypeArguments
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4258);
                            t102=nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t102!=null?((Token)t102.start):null), (t102!=null?((Token)t102.stop):null));}

                            }
                            break;

                    }


                    match(input,SUPER,FOLLOW_SUPER_in_explicitConstructorInvocation4281); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation4296);
                    t103=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t103!=null?((Token)t103.start):null), (t103!=null?((Token)t103.stop):null));}

                    // Java.g:764:134: ( ';' )
                    // Java.g:764:135: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation4301); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 42, explicitConstructorInvocation_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "explicitConstructorInvocation"


    public static class qualifiedName_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "qualifiedName"
    // Java.g:767:1: qualifiedName : IDENTIFIER ( ( '.' ) IDENTIFIER )* ;
    public final qualifiedName_return qualifiedName() throws RecognitionException {
        qualifiedName_return retval = new qualifiedName_return();
        retval.start = input.LT(1);

        int qualifiedName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return retval; }

            // Java.g:768:5: ( IDENTIFIER ( ( '.' ) IDENTIFIER )* )
            // Java.g:768:9: IDENTIFIER ( ( '.' ) IDENTIFIER )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName4323); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:769:9: ( ( '.' ) IDENTIFIER )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==DOT) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // Java.g:769:10: ( '.' ) IDENTIFIER
            	    {
            	    // Java.g:769:10: ( '.' )
            	    // Java.g:769:11: '.'
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualifiedName4337); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

            	    }


            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName4341); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            	    }
            	    break;

            	default :
            	    break loop80;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 43, qualifiedName_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "qualifiedName"


    public static class annotations_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotations"
    // Java.g:773:1: annotations : (t104= annotation )+ ;
    public final annotations_return annotations() throws RecognitionException {
        annotations_return retval = new annotations_return();
        retval.start = input.LT(1);

        int annotations_StartIndex = input.index();

        annotation_return t104 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return retval; }

            // Java.g:774:5: ( (t104= annotation )+ )
            // Java.g:774:9: (t104= annotation )+
            {
            // Java.g:774:9: (t104= annotation )+
            int cnt81=0;
            loop81:
            do {
                int alt81=2;
                int LA81_0 = input.LA(1);

                if ( (LA81_0==MONKEYS_AT) ) {
                    alt81=1;
                }


                switch (alt81) {
            	case 1 :
            	    // Java.g:774:10: t104= annotation
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotation"));}

            	    pushFollow(FOLLOW_annotation_in_annotations4379);
            	    t104=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t104!=null?((Token)t104.start):null), (t104!=null?((Token)t104.stop):null));}

            	    }
            	    break;

            	default :
            	    if ( cnt81 >= 1 ) break loop81;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(81, input);
                        throw eee;
                }
                cnt81++;
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 44, annotations_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotations"


    public static class annotation_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotation"
    // Java.g:782:1: annotation : ( '@' ) t105= qualifiedName ( ( '(' ) (t106= elementValuePairs |t107= elementValue )? ( ')' ) )? ;
    public final annotation_return annotation() throws RecognitionException {
        annotation_return retval = new annotation_return();
        retval.start = input.LT(1);

        int annotation_StartIndex = input.index();

        qualifiedName_return t105 =null;

        elementValuePairs_return t106 =null;

        elementValue_return t107 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return retval; }

            // Java.g:783:5: ( ( '@' ) t105= qualifiedName ( ( '(' ) (t106= elementValuePairs |t107= elementValue )? ( ')' ) )? )
            // Java.g:783:9: ( '@' ) t105= qualifiedName ( ( '(' ) (t106= elementValuePairs |t107= elementValue )? ( ')' ) )?
            {
            // Java.g:783:9: ( '@' )
            // Java.g:783:10: '@'
            {
            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_annotation4415); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'@'",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("qualifiedName"));}

            pushFollow(FOLLOW_qualifiedName_in_annotation4423);
            t105=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t105!=null?((Token)t105.start):null), (t105!=null?((Token)t105.stop):null));}

            // Java.g:784:9: ( ( '(' ) (t106= elementValuePairs |t107= elementValue )? ( ')' ) )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==LPAREN) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // Java.g:784:13: ( '(' ) (t106= elementValuePairs |t107= elementValue )? ( ')' )
                    {
                    // Java.g:784:13: ( '(' )
                    // Java.g:784:14: '('
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_annotation4440); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

                    }


                    // Java.g:785:19: (t106= elementValuePairs |t107= elementValue )?
                    int alt82=3;
                    int LA82_0 = input.LA(1);

                    if ( (LA82_0==IDENTIFIER) ) {
                        int LA82_1 = input.LA(2);

                        if ( (LA82_1==EQ) ) {
                            alt82=1;
                        }
                        else if ( ((LA82_1 >= AMP && LA82_1 <= AMPAMP)||(LA82_1 >= BANGEQ && LA82_1 <= BARBAR)||LA82_1==CARET||LA82_1==DOT||LA82_1==EQEQ||LA82_1==GT||LA82_1==INSTANCEOF||LA82_1==LBRACKET||(LA82_1 >= LPAREN && LA82_1 <= LT)||LA82_1==PERCENT||LA82_1==PLUS||LA82_1==PLUSPLUS||LA82_1==QUES||LA82_1==RPAREN||LA82_1==SLASH||LA82_1==STAR||LA82_1==SUB||LA82_1==SUBSUB) ) {
                            alt82=2;
                        }
                    }
                    else if ( (LA82_0==BANG||(LA82_0 >= BINLITERAL && LA82_0 <= BOOLEAN)||LA82_0==BYTE||(LA82_0 >= CHAR && LA82_0 <= CHARLITERAL)||(LA82_0 >= DOUBLE && LA82_0 <= DOUBLELITERAL)||LA82_0==FALSE||(LA82_0 >= FLOAT && LA82_0 <= FLOATLITERAL)||LA82_0==INT||LA82_0==INTLITERAL||LA82_0==LBRACE||(LA82_0 >= LONG && LA82_0 <= LPAREN)||LA82_0==MONKEYS_AT||(LA82_0 >= NEW && LA82_0 <= NULL)||LA82_0==PLUS||LA82_0==PLUSPLUS||LA82_0==SHORT||(LA82_0 >= STRINGLITERAL && LA82_0 <= SUB)||(LA82_0 >= SUBSUB && LA82_0 <= SUPER)||LA82_0==THIS||LA82_0==TILDE||LA82_0==TRUE||LA82_0==VOID) ) {
                        alt82=2;
                    }
                    switch (alt82) {
                        case 1 :
                            // Java.g:785:23: t106= elementValuePairs
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValuePairs"));}

                            pushFollow(FOLLOW_elementValuePairs_in_annotation4473);
                            t106=elementValuePairs();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t106!=null?((Token)t106.start):null), (t106!=null?((Token)t106.stop):null));}

                            }
                            break;
                        case 2 :
                            // Java.g:786:23: t107= elementValue
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValue"));}

                            pushFollow(FOLLOW_elementValue_in_annotation4503);
                            t107=elementValue();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t107!=null?((Token)t107.start):null), (t107!=null?((Token)t107.stop):null));}

                            }
                            break;

                    }


                    // Java.g:788:13: ( ')' )
                    // Java.g:788:14: ')'
                    {
                    match(input,RPAREN,FOLLOW_RPAREN_in_annotation4542); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

                    }


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 45, annotation_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotation"


    public static class elementValuePairs_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "elementValuePairs"
    // Java.g:792:1: elementValuePairs :t108= elementValuePair ( ( ',' ) t109= elementValuePair )* ;
    public final elementValuePairs_return elementValuePairs() throws RecognitionException {
        elementValuePairs_return retval = new elementValuePairs_return();
        retval.start = input.LT(1);

        int elementValuePairs_StartIndex = input.index();

        elementValuePair_return t108 =null;

        elementValuePair_return t109 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return retval; }

            // Java.g:793:5: (t108= elementValuePair ( ( ',' ) t109= elementValuePair )* )
            // Java.g:793:9: t108= elementValuePair ( ( ',' ) t109= elementValuePair )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValuePair"));}

            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs4580);
            t108=elementValuePair();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t108!=null?((Token)t108.start):null), (t108!=null?((Token)t108.stop):null));}

            // Java.g:794:9: ( ( ',' ) t109= elementValuePair )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==COMMA) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // Java.g:794:10: ( ',' ) t109= elementValuePair
            	    {
            	    // Java.g:794:10: ( ',' )
            	    // Java.g:794:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_elementValuePairs4594); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValuePair"));}

            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs4602);
            	    t109=elementValuePair();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t109!=null?((Token)t109.start):null), (t109!=null?((Token)t109.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop84;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 46, elementValuePairs_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValuePairs"


    public static class elementValuePair_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "elementValuePair"
    // Java.g:798:1: elementValuePair : IDENTIFIER ( '=' ) t110= elementValue ;
    public final elementValuePair_return elementValuePair() throws RecognitionException {
        elementValuePair_return retval = new elementValuePair_return();
        retval.start = input.LT(1);

        int elementValuePair_StartIndex = input.index();

        elementValue_return t110 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return retval; }

            // Java.g:799:5: ( IDENTIFIER ( '=' ) t110= elementValue )
            // Java.g:799:9: IDENTIFIER ( '=' ) t110= elementValue
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_elementValuePair4635); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:799:90: ( '=' )
            // Java.g:799:91: '='
            {
            match(input,EQ,FOLLOW_EQ_in_elementValuePair4640); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValue"));}

            pushFollow(FOLLOW_elementValue_in_elementValuePair4648);
            t110=elementValue();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t110!=null?((Token)t110.start):null), (t110!=null?((Token)t110.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 47, elementValuePair_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValuePair"


    public static class elementValue_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "elementValue"
    // Java.g:802:1: elementValue : (t111= conditionalExpression |t112= annotation |t113= elementValueArrayInitializer );
    public final elementValue_return elementValue() throws RecognitionException {
        elementValue_return retval = new elementValue_return();
        retval.start = input.LT(1);

        int elementValue_StartIndex = input.index();

        conditionalExpression_return t111 =null;

        annotation_return t112 =null;

        elementValueArrayInitializer_return t113 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return retval; }

            // Java.g:803:5: (t111= conditionalExpression |t112= annotation |t113= elementValueArrayInitializer )
            int alt85=3;
            switch ( input.LA(1) ) {
            case BANG:
            case BINLITERAL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case SHORT:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt85=1;
                }
                break;
            case MONKEYS_AT:
                {
                alt85=2;
                }
                break;
            case LBRACE:
                {
                alt85=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 85, 0, input);

                throw nvae;

            }

            switch (alt85) {
                case 1 :
                    // Java.g:803:9: t111= conditionalExpression
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalExpression"));}

                    pushFollow(FOLLOW_conditionalExpression_in_elementValue4674);
                    t111=conditionalExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t111!=null?((Token)t111.start):null), (t111!=null?((Token)t111.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:804:9: t112= annotation
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotation"));}

                    pushFollow(FOLLOW_annotation_in_elementValue4690);
                    t112=annotation();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t112!=null?((Token)t112.start):null), (t112!=null?((Token)t112.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:805:9: t113= elementValueArrayInitializer
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValueArrayInitializer"));}

                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue4706);
                    t113=elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t113!=null?((Token)t113.start):null), (t113!=null?((Token)t113.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 48, elementValue_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValue"


    public static class elementValueArrayInitializer_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "elementValueArrayInitializer"
    // Java.g:808:1: elementValueArrayInitializer : ( '{' ) (t114= elementValue ( ( ',' ) t115= elementValue )* )? ( ( ',' ) )? ( '}' ) ;
    public final elementValueArrayInitializer_return elementValueArrayInitializer() throws RecognitionException {
        elementValueArrayInitializer_return retval = new elementValueArrayInitializer_return();
        retval.start = input.LT(1);

        int elementValueArrayInitializer_StartIndex = input.index();

        elementValue_return t114 =null;

        elementValue_return t115 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return retval; }

            // Java.g:809:5: ( ( '{' ) (t114= elementValue ( ( ',' ) t115= elementValue )* )? ( ( ',' ) )? ( '}' ) )
            // Java.g:809:9: ( '{' ) (t114= elementValue ( ( ',' ) t115= elementValue )* )? ( ( ',' ) )? ( '}' )
            {
            // Java.g:809:9: ( '{' )
            // Java.g:809:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_elementValueArrayInitializer4729); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:810:9: (t114= elementValue ( ( ',' ) t115= elementValue )* )?
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==BANG||(LA87_0 >= BINLITERAL && LA87_0 <= BOOLEAN)||LA87_0==BYTE||(LA87_0 >= CHAR && LA87_0 <= CHARLITERAL)||(LA87_0 >= DOUBLE && LA87_0 <= DOUBLELITERAL)||LA87_0==FALSE||(LA87_0 >= FLOAT && LA87_0 <= FLOATLITERAL)||LA87_0==IDENTIFIER||LA87_0==INT||LA87_0==INTLITERAL||LA87_0==LBRACE||(LA87_0 >= LONG && LA87_0 <= LPAREN)||LA87_0==MONKEYS_AT||(LA87_0 >= NEW && LA87_0 <= NULL)||LA87_0==PLUS||LA87_0==PLUSPLUS||LA87_0==SHORT||(LA87_0 >= STRINGLITERAL && LA87_0 <= SUB)||(LA87_0 >= SUBSUB && LA87_0 <= SUPER)||LA87_0==THIS||LA87_0==TILDE||LA87_0==TRUE||LA87_0==VOID) ) {
                alt87=1;
            }
            switch (alt87) {
                case 1 :
                    // Java.g:810:10: t114= elementValue ( ( ',' ) t115= elementValue )*
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValue"));}

                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer4746);
                    t114=elementValue();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t114!=null?((Token)t114.start):null), (t114!=null?((Token)t114.stop):null));}

                    // Java.g:811:13: ( ( ',' ) t115= elementValue )*
                    loop86:
                    do {
                        int alt86=2;
                        int LA86_0 = input.LA(1);

                        if ( (LA86_0==COMMA) ) {
                            int LA86_1 = input.LA(2);

                            if ( (LA86_1==BANG||(LA86_1 >= BINLITERAL && LA86_1 <= BOOLEAN)||LA86_1==BYTE||(LA86_1 >= CHAR && LA86_1 <= CHARLITERAL)||(LA86_1 >= DOUBLE && LA86_1 <= DOUBLELITERAL)||LA86_1==FALSE||(LA86_1 >= FLOAT && LA86_1 <= FLOATLITERAL)||LA86_1==IDENTIFIER||LA86_1==INT||LA86_1==INTLITERAL||LA86_1==LBRACE||(LA86_1 >= LONG && LA86_1 <= LPAREN)||LA86_1==MONKEYS_AT||(LA86_1 >= NEW && LA86_1 <= NULL)||LA86_1==PLUS||LA86_1==PLUSPLUS||LA86_1==SHORT||(LA86_1 >= STRINGLITERAL && LA86_1 <= SUB)||(LA86_1 >= SUBSUB && LA86_1 <= SUPER)||LA86_1==THIS||LA86_1==TILDE||LA86_1==TRUE||LA86_1==VOID) ) {
                                alt86=1;
                            }


                        }


                        switch (alt86) {
                    	case 1 :
                    	    // Java.g:811:14: ( ',' ) t115= elementValue
                    	    {
                    	    // Java.g:811:14: ( ',' )
                    	    // Java.g:811:15: ','
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer4764); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    	    }


                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValue"));}

                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer4772);
                    	    t115=elementValue();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t115!=null?((Token)t115.start):null), (t115!=null?((Token)t115.stop):null));}

                    	    }
                    	    break;

                    	default :
                    	    break loop86;
                        }
                    } while (true);


                    }
                    break;

            }


            // Java.g:813:12: ( ( ',' ) )?
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==COMMA) ) {
                alt88=1;
            }
            switch (alt88) {
                case 1 :
                    // Java.g:813:13: ( ',' )
                    {
                    // Java.g:813:13: ( ',' )
                    // Java.g:813:14: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer4804); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    }


                    }
                    break;

            }


            // Java.g:813:53: ( '}' )
            // Java.g:813:54: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_elementValueArrayInitializer4811); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 49, elementValueArrayInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValueArrayInitializer"


    public static class annotationTypeDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotationTypeDeclaration"
    // Java.g:820:1: annotationTypeDeclaration :t116= modifiers ( '@' ) 'interface' IDENTIFIER t117= annotationTypeBody ;
    public final annotationTypeDeclaration_return annotationTypeDeclaration() throws RecognitionException {
        annotationTypeDeclaration_return retval = new annotationTypeDeclaration_return();
        retval.start = input.LT(1);

        int annotationTypeDeclaration_StartIndex = input.index();

        modifiers_return t116 =null;

        annotationTypeBody_return t117 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return retval; }

            // Java.g:821:5: (t116= modifiers ( '@' ) 'interface' IDENTIFIER t117= annotationTypeBody )
            // Java.g:821:9: t116= modifiers ( '@' ) 'interface' IDENTIFIER t117= annotationTypeBody
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_annotationTypeDeclaration4840);
            t116=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t116!=null?((Token)t116.start):null), (t116!=null?((Token)t116.stop):null));}

            // Java.g:821:134: ( '@' )
            // Java.g:821:135: '@'
            {
            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration4845); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'@'",input.LT(-1));}

            }


            match(input,INTERFACE,FOLLOW_INTERFACE_in_annotationTypeDeclaration4857); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'interface'",input.LT(-1));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationTypeDeclaration4868); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotationTypeBody"));}

            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration4884);
            t117=annotationTypeBody();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t117!=null?((Token)t117.start):null), (t117!=null?((Token)t117.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 50, annotationTypeDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeDeclaration"


    public static class annotationTypeBody_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotationTypeBody"
    // Java.g:828:1: annotationTypeBody : ( '{' ) (t118= annotationTypeElementDeclaration )* ( '}' ) ;
    public final annotationTypeBody_return annotationTypeBody() throws RecognitionException {
        annotationTypeBody_return retval = new annotationTypeBody_return();
        retval.start = input.LT(1);

        int annotationTypeBody_StartIndex = input.index();

        annotationTypeElementDeclaration_return t118 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return retval; }

            // Java.g:829:5: ( ( '{' ) (t118= annotationTypeElementDeclaration )* ( '}' ) )
            // Java.g:829:9: ( '{' ) (t118= annotationTypeElementDeclaration )* ( '}' )
            {
            // Java.g:829:9: ( '{' )
            // Java.g:829:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_annotationTypeBody4908); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:830:9: (t118= annotationTypeElementDeclaration )*
            loop89:
            do {
                int alt89=2;
                int LA89_0 = input.LA(1);

                if ( (LA89_0==ABSTRACT||LA89_0==BOOLEAN||LA89_0==BYTE||LA89_0==CHAR||LA89_0==CLASS||LA89_0==DOUBLE||LA89_0==ENUM||LA89_0==FINAL||LA89_0==FLOAT||LA89_0==IDENTIFIER||(LA89_0 >= INT && LA89_0 <= INTERFACE)||LA89_0==LONG||LA89_0==LT||(LA89_0 >= MONKEYS_AT && LA89_0 <= NATIVE)||(LA89_0 >= PRIVATE && LA89_0 <= PUBLIC)||(LA89_0 >= SEMI && LA89_0 <= SHORT)||(LA89_0 >= STATIC && LA89_0 <= STRICTFP)||LA89_0==SYNCHRONIZED||LA89_0==TRANSIENT||(LA89_0 >= VOID && LA89_0 <= VOLATILE)) ) {
                    alt89=1;
                }


                switch (alt89) {
            	case 1 :
            	    // Java.g:830:10: t118= annotationTypeElementDeclaration
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotationTypeElementDeclaration"));}

            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody4926);
            	    t118=annotationTypeElementDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t118!=null?((Token)t118.start):null), (t118!=null?((Token)t118.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop89;
                }
            } while (true);


            // Java.g:832:9: ( '}' )
            // Java.g:832:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_annotationTypeBody4951); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 51, annotationTypeBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeBody"


    public static class annotationTypeElementDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotationTypeElementDeclaration"
    // Java.g:838:1: annotationTypeElementDeclaration : (t119= annotationMethodDeclaration |t120= interfaceFieldDeclaration |t121= normalClassDeclaration |t122= normalInterfaceDeclaration |t123= enumDeclaration |t124= annotationTypeDeclaration | ( ';' ) );
    public final annotationTypeElementDeclaration_return annotationTypeElementDeclaration() throws RecognitionException {
        annotationTypeElementDeclaration_return retval = new annotationTypeElementDeclaration_return();
        retval.start = input.LT(1);

        int annotationTypeElementDeclaration_StartIndex = input.index();

        annotationMethodDeclaration_return t119 =null;

        interfaceFieldDeclaration_return t120 =null;

        normalClassDeclaration_return t121 =null;

        normalInterfaceDeclaration_return t122 =null;

        enumDeclaration_return t123 =null;

        annotationTypeDeclaration_return t124 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return retval; }

            // Java.g:839:5: (t119= annotationMethodDeclaration |t120= interfaceFieldDeclaration |t121= normalClassDeclaration |t122= normalInterfaceDeclaration |t123= enumDeclaration |t124= annotationTypeDeclaration | ( ';' ) )
            int alt90=7;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                int LA90_1 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA90_2 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA90_3 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA90_4 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA90_5 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA90_6 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA90_7 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA90_8 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA90_9 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA90_10 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA90_11 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA90_12 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else if ( (synpred119_Java()) ) {
                    alt90=3;
                }
                else if ( (synpred120_Java()) ) {
                    alt90=4;
                }
                else if ( (synpred121_Java()) ) {
                    alt90=5;
                }
                else if ( (synpred122_Java()) ) {
                    alt90=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA90_13 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
                {
                int LA90_14 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 14, input);

                    throw nvae;

                }
                }
                break;
            case CHAR:
                {
                int LA90_15 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 15, input);

                    throw nvae;

                }
                }
                break;
            case BYTE:
                {
                int LA90_16 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 16, input);

                    throw nvae;

                }
                }
                break;
            case SHORT:
                {
                int LA90_17 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 17, input);

                    throw nvae;

                }
                }
                break;
            case INT:
                {
                int LA90_18 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 18, input);

                    throw nvae;

                }
                }
                break;
            case LONG:
                {
                int LA90_19 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 19, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA90_20 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 20, input);

                    throw nvae;

                }
                }
                break;
            case DOUBLE:
                {
                int LA90_21 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt90=1;
                }
                else if ( (synpred118_Java()) ) {
                    alt90=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 90, 21, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
                {
                alt90=3;
                }
                break;
            case INTERFACE:
                {
                alt90=4;
                }
                break;
            case ENUM:
                {
                alt90=5;
                }
                break;
            case SEMI:
                {
                alt90=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;

            }

            switch (alt90) {
                case 1 :
                    // Java.g:839:9: t119= annotationMethodDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotationMethodDeclaration"));}

                    pushFollow(FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration4979);
                    t119=annotationMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t119!=null?((Token)t119.start):null), (t119!=null?((Token)t119.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:840:9: t120= interfaceFieldDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("interfaceFieldDeclaration"));}

                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration4995);
                    t120=interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t120!=null?((Token)t120.start):null), (t120!=null?((Token)t120.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:841:9: t121= normalClassDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalClassDeclaration"));}

                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration5011);
                    t121=normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t121!=null?((Token)t121.start):null), (t121!=null?((Token)t121.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:842:9: t122= normalInterfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("normalInterfaceDeclaration"));}

                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration5027);
                    t122=normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t122!=null?((Token)t122.start):null), (t122!=null?((Token)t122.stop):null));}

                    }
                    break;
                case 5 :
                    // Java.g:843:9: t123= enumDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("enumDeclaration"));}

                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration5043);
                    t123=enumDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t123!=null?((Token)t123.start):null), (t123!=null?((Token)t123.stop):null));}

                    }
                    break;
                case 6 :
                    // Java.g:844:9: t124= annotationTypeDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("annotationTypeDeclaration"));}

                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration5059);
                    t124=annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t124!=null?((Token)t124.start):null), (t124!=null?((Token)t124.stop):null));}

                    }
                    break;
                case 7 :
                    // Java.g:845:9: ( ';' )
                    {
                    // Java.g:845:9: ( ';' )
                    // Java.g:845:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_annotationTypeElementDeclaration5072); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 52, annotationTypeElementDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeElementDeclaration"


    public static class annotationMethodDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "annotationMethodDeclaration"
    // Java.g:848:1: annotationMethodDeclaration :t125= modifiers t126= type IDENTIFIER ( '(' ) ( ')' ) ( 'default' t127= elementValue )? ( ';' ) ;
    public final annotationMethodDeclaration_return annotationMethodDeclaration() throws RecognitionException {
        annotationMethodDeclaration_return retval = new annotationMethodDeclaration_return();
        retval.start = input.LT(1);

        int annotationMethodDeclaration_StartIndex = input.index();

        modifiers_return t125 =null;

        type_return t126 =null;

        elementValue_return t127 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return retval; }

            // Java.g:849:5: (t125= modifiers t126= type IDENTIFIER ( '(' ) ( ')' ) ( 'default' t127= elementValue )? ( ';' ) )
            // Java.g:849:9: t125= modifiers t126= type IDENTIFIER ( '(' ) ( ')' ) ( 'default' t127= elementValue )? ( ';' )
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("modifiers"));}

            pushFollow(FOLLOW_modifiers_in_annotationMethodDeclaration5098);
            t125=modifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t125!=null?((Token)t125.start):null), (t125!=null?((Token)t125.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_annotationMethodDeclaration5106);
            t126=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t126!=null?((Token)t126.start):null), (t126!=null?((Token)t126.stop):null));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationMethodDeclaration5110); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:850:9: ( '(' )
            // Java.g:850:10: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_annotationMethodDeclaration5123); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            // Java.g:850:47: ( ')' )
            // Java.g:850:48: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_annotationMethodDeclaration5128); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            // Java.g:850:85: ( 'default' t127= elementValue )?
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==DEFAULT) ) {
                alt91=1;
            }
            switch (alt91) {
                case 1 :
                    // Java.g:850:86: 'default' t127= elementValue
                    {
                    match(input,DEFAULT,FOLLOW_DEFAULT_in_annotationMethodDeclaration5133); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'default'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("elementValue"));}

                    pushFollow(FOLLOW_elementValue_in_annotationMethodDeclaration5140);
                    t127=elementValue();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t127!=null?((Token)t127.start):null), (t127!=null?((Token)t127.stop):null));}

                    }
                    break;

            }


            // Java.g:852:9: ( ';' )
            // Java.g:852:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_annotationMethodDeclaration5172); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 53, annotationMethodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationMethodDeclaration"


    public static class block_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "block"
    // Java.g:855:1: block : ( '{' ) (t128= blockStatement )* ( '}' ) ;
    public final block_return block() throws RecognitionException {
        block_return retval = new block_return();
        retval.start = input.LT(1);

        int block_StartIndex = input.index();

        blockStatement_return t128 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return retval; }

            // Java.g:856:5: ( ( '{' ) (t128= blockStatement )* ( '}' ) )
            // Java.g:856:9: ( '{' ) (t128= blockStatement )* ( '}' )
            {
            // Java.g:856:9: ( '{' )
            // Java.g:856:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_block5199); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:857:9: (t128= blockStatement )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==ABSTRACT||(LA92_0 >= ASSERT && LA92_0 <= BANG)||(LA92_0 >= BINLITERAL && LA92_0 <= BYTE)||(LA92_0 >= CHAR && LA92_0 <= CLASS)||LA92_0==CONTINUE||LA92_0==DO||(LA92_0 >= DOUBLE && LA92_0 <= DOUBLELITERAL)||LA92_0==ENUM||(LA92_0 >= FALSE && LA92_0 <= FINAL)||(LA92_0 >= FLOAT && LA92_0 <= FOR)||(LA92_0 >= IDENTIFIER && LA92_0 <= IF)||(LA92_0 >= INT && LA92_0 <= INTLITERAL)||LA92_0==LBRACE||(LA92_0 >= LONG && LA92_0 <= LT)||(LA92_0 >= MONKEYS_AT && LA92_0 <= NULL)||LA92_0==PLUS||(LA92_0 >= PLUSPLUS && LA92_0 <= PUBLIC)||LA92_0==RETURN||(LA92_0 >= SEMI && LA92_0 <= SHORT)||(LA92_0 >= STATIC && LA92_0 <= SUB)||(LA92_0 >= SUBSUB && LA92_0 <= SYNCHRONIZED)||(LA92_0 >= THIS && LA92_0 <= THROW)||(LA92_0 >= TILDE && LA92_0 <= WHILE)) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // Java.g:857:10: t128= blockStatement
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("blockStatement"));}

            	    pushFollow(FOLLOW_blockStatement_in_block5216);
            	    t128=blockStatement();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t128!=null?((Token)t128.start):null), (t128!=null?((Token)t128.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);


            // Java.g:859:9: ( '}' )
            // Java.g:859:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_block5240); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 54, block_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "block"


    public static class blockStatement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "blockStatement"
    // Java.g:862:1: blockStatement : (t129= localVariableDeclarationStatement |t130= classOrInterfaceDeclaration |t131= statement );
    public final blockStatement_return blockStatement() throws RecognitionException {
        blockStatement_return retval = new blockStatement_return();
        retval.start = input.LT(1);

        int blockStatement_StartIndex = input.index();

        localVariableDeclarationStatement_return t129 =null;

        classOrInterfaceDeclaration_return t130 =null;

        statement_return t131 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return retval; }

            // Java.g:863:5: (t129= localVariableDeclarationStatement |t130= classOrInterfaceDeclaration |t131= statement )
            int alt93=3;
            alt93 = dfa93.predict(input);
            switch (alt93) {
                case 1 :
                    // Java.g:863:9: t129= localVariableDeclarationStatement
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("localVariableDeclarationStatement"));}

                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement5265);
                    t129=localVariableDeclarationStatement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t129!=null?((Token)t129.start):null), (t129!=null?((Token)t129.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:864:9: t130= classOrInterfaceDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceDeclaration"));}

                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement5281);
                    t130=classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t130!=null?((Token)t130.start):null), (t130!=null?((Token)t130.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:865:9: t131= statement
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_blockStatement5297);
                    t131=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t131!=null?((Token)t131.start):null), (t131!=null?((Token)t131.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 55, blockStatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "blockStatement"


    public static class localVariableDeclarationStatement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "localVariableDeclarationStatement"
    // Java.g:869:1: localVariableDeclarationStatement :t132= localVariableDeclaration ( ';' ) ;
    public final localVariableDeclarationStatement_return localVariableDeclarationStatement() throws RecognitionException {
        localVariableDeclarationStatement_return retval = new localVariableDeclarationStatement_return();
        retval.start = input.LT(1);

        int localVariableDeclarationStatement_StartIndex = input.index();

        localVariableDeclaration_return t132 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return retval; }

            // Java.g:870:5: (t132= localVariableDeclaration ( ';' ) )
            // Java.g:870:9: t132= localVariableDeclaration ( ';' )
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("localVariableDeclaration"));}

            pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement5324);
            t132=localVariableDeclaration();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t132!=null?((Token)t132.start):null), (t132!=null?((Token)t132.stop):null));}

            // Java.g:871:9: ( ';' )
            // Java.g:871:10: ';'
            {
            match(input,SEMI,FOLLOW_SEMI_in_localVariableDeclarationStatement5337); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 56, localVariableDeclarationStatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclarationStatement"


    public static class localVariableDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "localVariableDeclaration"
    // Java.g:874:1: localVariableDeclaration :t133= variableModifiers t134= type t135= variableDeclarator ( ( ',' ) t136= variableDeclarator )* ;
    public final localVariableDeclaration_return localVariableDeclaration() throws RecognitionException {
        localVariableDeclaration_return retval = new localVariableDeclaration_return();
        retval.start = input.LT(1);

        int localVariableDeclaration_StartIndex = input.index();

        variableModifiers_return t133 =null;

        type_return t134 =null;

        variableDeclarator_return t135 =null;

        variableDeclarator_return t136 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return retval; }

            // Java.g:875:5: (t133= variableModifiers t134= type t135= variableDeclarator ( ( ',' ) t136= variableDeclarator )* )
            // Java.g:875:9: t133= variableModifiers t134= type t135= variableDeclarator ( ( ',' ) t136= variableDeclarator )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

            pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration5363);
            t133=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t133!=null?((Token)t133.start):null), (t133!=null?((Token)t133.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

            pushFollow(FOLLOW_type_in_localVariableDeclaration5371);
            t134=type();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t134!=null?((Token)t134.start):null), (t134!=null?((Token)t134.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration5387);
            t135=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t135!=null?((Token)t135.start):null), (t135!=null?((Token)t135.stop):null));}

            // Java.g:877:9: ( ( ',' ) t136= variableDeclarator )*
            loop94:
            do {
                int alt94=2;
                int LA94_0 = input.LA(1);

                if ( (LA94_0==COMMA) ) {
                    alt94=1;
                }


                switch (alt94) {
            	case 1 :
            	    // Java.g:877:10: ( ',' ) t136= variableDeclarator
            	    {
            	    // Java.g:877:10: ( ',' )
            	    // Java.g:877:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_localVariableDeclaration5401); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclarator"));}

            	    pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration5409);
            	    t136=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t136!=null?((Token)t136.start):null), (t136!=null?((Token)t136.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop94;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 57, localVariableDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclaration"


    public static class statement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "statement"
    // Java.g:881:1: statement : (t137= block | ( 'assert' ) t138= expression ( ( ':' ) t139= expression )? ( ';' ) | 'assert' t140= expression ( ( ':' ) t141= expression )? ( ';' ) | 'if' t142= parExpression t143= statement ( 'else' t144= statement )? |t145= forstatement | 'while' t146= parExpression t147= statement | 'do' t148= statement 'while' t149= parExpression ( ';' ) |t150= trystatement | 'switch' t151= parExpression ( '{' ) t152= switchBlockStatementGroups ( '}' ) | 'synchronized' t153= parExpression t154= block | 'return' (t155= expression )? ( ';' ) | 'throw' t156= expression ( ';' ) | 'break' ( IDENTIFIER )? ( ';' ) | 'continue' ( IDENTIFIER )? ( ';' ) |t157= expression ( ';' ) | IDENTIFIER ( ':' ) t158= statement | ( ';' ) );
    public final statement_return statement() throws RecognitionException {
        statement_return retval = new statement_return();
        retval.start = input.LT(1);

        int statement_StartIndex = input.index();

        block_return t137 =null;

        expression_return t138 =null;

        expression_return t139 =null;

        expression_return t140 =null;

        expression_return t141 =null;

        parExpression_return t142 =null;

        statement_return t143 =null;

        statement_return t144 =null;

        forstatement_return t145 =null;

        parExpression_return t146 =null;

        statement_return t147 =null;

        statement_return t148 =null;

        parExpression_return t149 =null;

        trystatement_return t150 =null;

        parExpression_return t151 =null;

        switchBlockStatementGroups_return t152 =null;

        parExpression_return t153 =null;

        block_return t154 =null;

        expression_return t155 =null;

        expression_return t156 =null;

        expression_return t157 =null;

        statement_return t158 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return retval; }

            // Java.g:882:5: (t137= block | ( 'assert' ) t138= expression ( ( ':' ) t139= expression )? ( ';' ) | 'assert' t140= expression ( ( ':' ) t141= expression )? ( ';' ) | 'if' t142= parExpression t143= statement ( 'else' t144= statement )? |t145= forstatement | 'while' t146= parExpression t147= statement | 'do' t148= statement 'while' t149= parExpression ( ';' ) |t150= trystatement | 'switch' t151= parExpression ( '{' ) t152= switchBlockStatementGroups ( '}' ) | 'synchronized' t153= parExpression t154= block | 'return' (t155= expression )? ( ';' ) | 'throw' t156= expression ( ';' ) | 'break' ( IDENTIFIER )? ( ';' ) | 'continue' ( IDENTIFIER )? ( ';' ) |t157= expression ( ';' ) | IDENTIFIER ( ':' ) t158= statement | ( ';' ) )
            int alt101=17;
            switch ( input.LA(1) ) {
            case LBRACE:
                {
                alt101=1;
                }
                break;
            case ASSERT:
                {
                int LA101_2 = input.LA(2);

                if ( (synpred130_Java()) ) {
                    alt101=2;
                }
                else if ( (synpred132_Java()) ) {
                    alt101=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 101, 2, input);

                    throw nvae;

                }
                }
                break;
            case IF:
                {
                alt101=4;
                }
                break;
            case FOR:
                {
                alt101=5;
                }
                break;
            case WHILE:
                {
                alt101=6;
                }
                break;
            case DO:
                {
                alt101=7;
                }
                break;
            case TRY:
                {
                alt101=8;
                }
                break;
            case SWITCH:
                {
                alt101=9;
                }
                break;
            case SYNCHRONIZED:
                {
                alt101=10;
                }
                break;
            case RETURN:
                {
                alt101=11;
                }
                break;
            case THROW:
                {
                alt101=12;
                }
                break;
            case BREAK:
                {
                alt101=13;
                }
                break;
            case CONTINUE:
                {
                alt101=14;
                }
                break;
            case BANG:
            case BINLITERAL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case SHORT:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt101=15;
                }
                break;
            case IDENTIFIER:
                {
                int LA101_22 = input.LA(2);

                if ( (synpred148_Java()) ) {
                    alt101=15;
                }
                else if ( (synpred149_Java()) ) {
                    alt101=16;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 101, 22, input);

                    throw nvae;

                }
                }
                break;
            case SEMI:
                {
                alt101=17;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 101, 0, input);

                throw nvae;

            }

            switch (alt101) {
                case 1 :
                    // Java.g:882:9: t137= block
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                    pushFollow(FOLLOW_block_in_statement5446);
                    t137=block();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t137!=null?((Token)t137.start):null), (t137!=null?((Token)t137.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:884:9: ( 'assert' ) t138= expression ( ( ':' ) t139= expression )? ( ';' )
                    {
                    // Java.g:884:9: ( 'assert' )
                    // Java.g:884:10: 'assert'
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement5472); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'assert'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_statement5497);
                    t138=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t138!=null?((Token)t138.start):null), (t138!=null?((Token)t138.stop):null));}

                    // Java.g:886:136: ( ( ':' ) t139= expression )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==COLON) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // Java.g:886:137: ( ':' ) t139= expression
                            {
                            // Java.g:886:137: ( ':' )
                            // Java.g:886:138: ':'
                            {
                            match(input,COLON,FOLLOW_COLON_in_statement5503); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                            }


                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                            pushFollow(FOLLOW_expression_in_statement5511);
                            t139=expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t139!=null?((Token)t139.start):null), (t139!=null?((Token)t139.stop):null));}

                            }
                            break;

                    }


                    // Java.g:886:304: ( ';' )
                    // Java.g:886:305: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5518); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 3 :
                    // Java.g:887:9: 'assert' t140= expression ( ( ':' ) t141= expression )? ( ';' )
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement5530); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'assert'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_statement5538);
                    t140=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t140!=null?((Token)t140.start):null), (t140!=null?((Token)t140.stop):null));}

                    // Java.g:887:183: ( ( ':' ) t141= expression )?
                    int alt96=2;
                    int LA96_0 = input.LA(1);

                    if ( (LA96_0==COLON) ) {
                        alt96=1;
                    }
                    switch (alt96) {
                        case 1 :
                            // Java.g:887:184: ( ':' ) t141= expression
                            {
                            // Java.g:887:184: ( ':' )
                            // Java.g:887:185: ':'
                            {
                            match(input,COLON,FOLLOW_COLON_in_statement5544); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                            }


                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                            pushFollow(FOLLOW_expression_in_statement5552);
                            t141=expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t141!=null?((Token)t141.start):null), (t141!=null?((Token)t141.stop):null));}

                            }
                            break;

                    }


                    // Java.g:887:351: ( ';' )
                    // Java.g:887:352: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5559); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 4 :
                    // Java.g:888:9: 'if' t142= parExpression t143= statement ( 'else' t144= statement )?
                    {
                    match(input,IF,FOLLOW_IF_in_statement5583); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'if'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_statement5590);
                    t142=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t142!=null?((Token)t142.start):null), (t142!=null?((Token)t142.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_statement5598);
                    t143=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t143!=null?((Token)t143.start):null), (t143!=null?((Token)t143.stop):null));}

                    // Java.g:888:305: ( 'else' t144= statement )?
                    int alt97=2;
                    int LA97_0 = input.LA(1);

                    if ( (LA97_0==ELSE) ) {
                        int LA97_1 = input.LA(2);

                        if ( (synpred133_Java()) ) {
                            alt97=1;
                        }
                    }
                    switch (alt97) {
                        case 1 :
                            // Java.g:888:306: 'else' t144= statement
                            {
                            match(input,ELSE,FOLLOW_ELSE_in_statement5603); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'else'",input.LT(-1));}

                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                            pushFollow(FOLLOW_statement_in_statement5610);
                            t144=statement();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t144!=null?((Token)t144.start):null), (t144!=null?((Token)t144.stop):null));}

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // Java.g:889:9: t145= forstatement
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("forstatement"));}

                    pushFollow(FOLLOW_forstatement_in_statement5638);
                    t145=forstatement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t145!=null?((Token)t145.start):null), (t145!=null?((Token)t145.stop):null));}

                    }
                    break;
                case 6 :
                    // Java.g:890:9: 'while' t146= parExpression t147= statement
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_statement5650); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'while'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_statement5657);
                    t146=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t146!=null?((Token)t146.start):null), (t146!=null?((Token)t146.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_statement5665);
                    t147=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t147!=null?((Token)t147.start):null), (t147!=null?((Token)t147.stop):null));}

                    }
                    break;
                case 7 :
                    // Java.g:891:9: 'do' t148= statement 'while' t149= parExpression ( ';' )
                    {
                    match(input,DO,FOLLOW_DO_in_statement5677); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'do'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_statement5684);
                    t148=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t148!=null?((Token)t148.start):null), (t148!=null?((Token)t148.stop):null));}

                    match(input,WHILE,FOLLOW_WHILE_in_statement5688); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'while'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_statement5695);
                    t149=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t149!=null?((Token)t149.start):null), (t149!=null?((Token)t149.stop):null));}

                    // Java.g:891:349: ( ';' )
                    // Java.g:891:350: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5700); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 8 :
                    // Java.g:892:9: t150= trystatement
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("trystatement"));}

                    pushFollow(FOLLOW_trystatement_in_statement5716);
                    t150=trystatement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t150!=null?((Token)t150.start):null), (t150!=null?((Token)t150.stop):null));}

                    }
                    break;
                case 9 :
                    // Java.g:893:9: 'switch' t151= parExpression ( '{' ) t152= switchBlockStatementGroups ( '}' )
                    {
                    match(input,SWITCH,FOLLOW_SWITCH_in_statement5728); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'switch'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_statement5735);
                    t151=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t151!=null?((Token)t151.start):null), (t151!=null?((Token)t151.stop):null));}

                    // Java.g:893:188: ( '{' )
                    // Java.g:893:189: '{'
                    {
                    match(input,LBRACE,FOLLOW_LBRACE_in_statement5740); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("switchBlockStatementGroups"));}

                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement5748);
                    t152=switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t152!=null?((Token)t152.start):null), (t152!=null?((Token)t152.stop):null));}

                    // Java.g:893:385: ( '}' )
                    // Java.g:893:386: '}'
                    {
                    match(input,RBRACE,FOLLOW_RBRACE_in_statement5753); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

                    }


                    }
                    break;
                case 10 :
                    // Java.g:894:9: 'synchronized' t153= parExpression t154= block
                    {
                    match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_statement5765); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'synchronized'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_statement5772);
                    t153=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t153!=null?((Token)t153.start):null), (t153!=null?((Token)t153.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                    pushFollow(FOLLOW_block_in_statement5780);
                    t154=block();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t154!=null?((Token)t154.start):null), (t154!=null?((Token)t154.stop):null));}

                    }
                    break;
                case 11 :
                    // Java.g:895:9: 'return' (t155= expression )? ( ';' )
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_statement5792); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'return'",input.LT(-1));}

                    // Java.g:895:55: (t155= expression )?
                    int alt98=2;
                    int LA98_0 = input.LA(1);

                    if ( (LA98_0==BANG||(LA98_0 >= BINLITERAL && LA98_0 <= BOOLEAN)||LA98_0==BYTE||(LA98_0 >= CHAR && LA98_0 <= CHARLITERAL)||(LA98_0 >= DOUBLE && LA98_0 <= DOUBLELITERAL)||LA98_0==FALSE||(LA98_0 >= FLOAT && LA98_0 <= FLOATLITERAL)||LA98_0==IDENTIFIER||LA98_0==INT||LA98_0==INTLITERAL||(LA98_0 >= LONG && LA98_0 <= LPAREN)||(LA98_0 >= NEW && LA98_0 <= NULL)||LA98_0==PLUS||LA98_0==PLUSPLUS||LA98_0==SHORT||(LA98_0 >= STRINGLITERAL && LA98_0 <= SUB)||(LA98_0 >= SUBSUB && LA98_0 <= SUPER)||LA98_0==THIS||LA98_0==TILDE||LA98_0==TRUE||LA98_0==VOID) ) {
                        alt98=1;
                    }
                    switch (alt98) {
                        case 1 :
                            // Java.g:895:56: t155= expression
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                            pushFollow(FOLLOW_expression_in_statement5800);
                            t155=expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t155!=null?((Token)t155.start):null), (t155!=null?((Token)t155.stop):null));}

                            }
                            break;

                    }


                    // Java.g:895:186: ( ';' )
                    // Java.g:895:187: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5808); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 12 :
                    // Java.g:896:9: 'throw' t156= expression ( ';' )
                    {
                    match(input,THROW,FOLLOW_THROW_in_statement5820); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'throw'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_statement5827);
                    t156=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t156!=null?((Token)t156.start):null), (t156!=null?((Token)t156.stop):null));}

                    // Java.g:896:180: ( ';' )
                    // Java.g:896:181: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5832); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 13 :
                    // Java.g:897:9: 'break' ( IDENTIFIER )? ( ';' )
                    {
                    match(input,BREAK,FOLLOW_BREAK_in_statement5844); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'break'",input.LT(-1));}

                    // Java.g:898:13: ( IDENTIFIER )?
                    int alt99=2;
                    int LA99_0 = input.LA(1);

                    if ( (LA99_0==IDENTIFIER) ) {
                        alt99=1;
                    }
                    switch (alt99) {
                        case 1 :
                            // Java.g:898:14: IDENTIFIER
                            {
                            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5860); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                            }
                            break;

                    }


                    // Java.g:899:16: ( ';' )
                    // Java.g:899:17: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5880); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 14 :
                    // Java.g:900:9: 'continue' ( IDENTIFIER )? ( ';' )
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement5892); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'continue'",input.LT(-1));}

                    // Java.g:901:13: ( IDENTIFIER )?
                    int alt100=2;
                    int LA100_0 = input.LA(1);

                    if ( (LA100_0==IDENTIFIER) ) {
                        alt100=1;
                    }
                    switch (alt100) {
                        case 1 :
                            // Java.g:901:14: IDENTIFIER
                            {
                            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5908); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                            }
                            break;

                    }


                    // Java.g:902:16: ( ';' )
                    // Java.g:902:17: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5928); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 15 :
                    // Java.g:903:9: t157= expression ( ';' )
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_statement5944);
                    t157=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t157!=null?((Token)t157.start):null), (t157!=null?((Token)t157.stop):null));}

                    // Java.g:903:137: ( ';' )
                    // Java.g:903:138: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5950); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;
                case 16 :
                    // Java.g:904:9: IDENTIFIER ( ':' ) t158= statement
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5967); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:904:90: ( ':' )
                    // Java.g:904:91: ':'
                    {
                    match(input,COLON,FOLLOW_COLON_in_statement5972); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_statement5980);
                    t158=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t158!=null?((Token)t158.start):null), (t158!=null?((Token)t158.stop):null));}

                    }
                    break;
                case 17 :
                    // Java.g:905:9: ( ';' )
                    {
                    // Java.g:905:9: ( ';' )
                    // Java.g:905:10: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement5993); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 58, statement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "statement"


    public static class switchBlockStatementGroups_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "switchBlockStatementGroups"
    // Java.g:909:1: switchBlockStatementGroups : (t159= switchBlockStatementGroup )* ;
    public final switchBlockStatementGroups_return switchBlockStatementGroups() throws RecognitionException {
        switchBlockStatementGroups_return retval = new switchBlockStatementGroups_return();
        retval.start = input.LT(1);

        int switchBlockStatementGroups_StartIndex = input.index();

        switchBlockStatementGroup_return t159 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return retval; }

            // Java.g:910:5: ( (t159= switchBlockStatementGroup )* )
            // Java.g:910:9: (t159= switchBlockStatementGroup )*
            {
            // Java.g:910:9: (t159= switchBlockStatementGroup )*
            loop102:
            do {
                int alt102=2;
                int LA102_0 = input.LA(1);

                if ( (LA102_0==CASE||LA102_0==DEFAULT) ) {
                    alt102=1;
                }


                switch (alt102) {
            	case 1 :
            	    // Java.g:910:10: t159= switchBlockStatementGroup
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("switchBlockStatementGroup"));}

            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups6021);
            	    t159=switchBlockStatementGroup();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t159!=null?((Token)t159.start):null), (t159!=null?((Token)t159.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop102;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 59, switchBlockStatementGroups_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchBlockStatementGroups"


    public static class switchBlockStatementGroup_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "switchBlockStatementGroup"
    // Java.g:913:1: switchBlockStatementGroup :t160= switchLabel (t161= blockStatement )* ;
    public final switchBlockStatementGroup_return switchBlockStatementGroup() throws RecognitionException {
        switchBlockStatementGroup_return retval = new switchBlockStatementGroup_return();
        retval.start = input.LT(1);

        int switchBlockStatementGroup_StartIndex = input.index();

        switchLabel_return t160 =null;

        blockStatement_return t161 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return retval; }

            // Java.g:914:5: (t160= switchLabel (t161= blockStatement )* )
            // Java.g:915:9: t160= switchLabel (t161= blockStatement )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("switchLabel"));}

            pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup6056);
            t160=switchLabel();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t160!=null?((Token)t160.start):null), (t160!=null?((Token)t160.stop):null));}

            // Java.g:916:9: (t161= blockStatement )*
            loop103:
            do {
                int alt103=2;
                int LA103_0 = input.LA(1);

                if ( (LA103_0==ABSTRACT||(LA103_0 >= ASSERT && LA103_0 <= BANG)||(LA103_0 >= BINLITERAL && LA103_0 <= BYTE)||(LA103_0 >= CHAR && LA103_0 <= CLASS)||LA103_0==CONTINUE||LA103_0==DO||(LA103_0 >= DOUBLE && LA103_0 <= DOUBLELITERAL)||LA103_0==ENUM||(LA103_0 >= FALSE && LA103_0 <= FINAL)||(LA103_0 >= FLOAT && LA103_0 <= FOR)||(LA103_0 >= IDENTIFIER && LA103_0 <= IF)||(LA103_0 >= INT && LA103_0 <= INTLITERAL)||LA103_0==LBRACE||(LA103_0 >= LONG && LA103_0 <= LT)||(LA103_0 >= MONKEYS_AT && LA103_0 <= NULL)||LA103_0==PLUS||(LA103_0 >= PLUSPLUS && LA103_0 <= PUBLIC)||LA103_0==RETURN||(LA103_0 >= SEMI && LA103_0 <= SHORT)||(LA103_0 >= STATIC && LA103_0 <= SUB)||(LA103_0 >= SUBSUB && LA103_0 <= SYNCHRONIZED)||(LA103_0 >= THIS && LA103_0 <= THROW)||(LA103_0 >= TILDE && LA103_0 <= WHILE)) ) {
                    alt103=1;
                }


                switch (alt103) {
            	case 1 :
            	    // Java.g:916:10: t161= blockStatement
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("blockStatement"));}

            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup6073);
            	    t161=blockStatement();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t161!=null?((Token)t161.start):null), (t161!=null?((Token)t161.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop103;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 60, switchBlockStatementGroup_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchBlockStatementGroup"


    public static class switchLabel_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "switchLabel"
    // Java.g:920:1: switchLabel : ( 'case' t162= expression ( ':' ) | 'default' ( ':' ) );
    public final switchLabel_return switchLabel() throws RecognitionException {
        switchLabel_return retval = new switchLabel_return();
        retval.start = input.LT(1);

        int switchLabel_StartIndex = input.index();

        expression_return t162 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return retval; }

            // Java.g:921:5: ( 'case' t162= expression ( ':' ) | 'default' ( ':' ) )
            int alt104=2;
            int LA104_0 = input.LA(1);

            if ( (LA104_0==CASE) ) {
                alt104=1;
            }
            else if ( (LA104_0==DEFAULT) ) {
                alt104=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 104, 0, input);

                throw nvae;

            }
            switch (alt104) {
                case 1 :
                    // Java.g:921:9: 'case' t162= expression ( ':' )
                    {
                    match(input,CASE,FOLLOW_CASE_in_switchLabel6106); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'case'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_switchLabel6113);
                    t162=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t162!=null?((Token)t162.start):null), (t162!=null?((Token)t162.stop):null));}

                    // Java.g:921:178: ( ':' )
                    // Java.g:921:179: ':'
                    {
                    match(input,COLON,FOLLOW_COLON_in_switchLabel6118); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:922:9: 'default' ( ':' )
                    {
                    match(input,DEFAULT,FOLLOW_DEFAULT_in_switchLabel6130); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'default'",input.LT(-1));}

                    // Java.g:922:57: ( ':' )
                    // Java.g:922:58: ':'
                    {
                    match(input,COLON,FOLLOW_COLON_in_switchLabel6134); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 61, switchLabel_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchLabel"


    public static class trystatement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "trystatement"
    // Java.g:926:1: trystatement : 'try' (t163= resourceSpecification )? t164= block (t165= catches 'finally' t166= block |t167= catches | 'finally' t168= block )? ;
    public final trystatement_return trystatement() throws RecognitionException {
        trystatement_return retval = new trystatement_return();
        retval.start = input.LT(1);

        int trystatement_StartIndex = input.index();

        resourceSpecification_return t163 =null;

        block_return t164 =null;

        catches_return t165 =null;

        block_return t166 =null;

        catches_return t167 =null;

        block_return t168 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return retval; }

            // Java.g:927:5: ( 'try' (t163= resourceSpecification )? t164= block (t165= catches 'finally' t166= block |t167= catches | 'finally' t168= block )? )
            // Java.g:927:9: 'try' (t163= resourceSpecification )? t164= block (t165= catches 'finally' t166= block |t167= catches | 'finally' t168= block )?
            {
            match(input,TRY,FOLLOW_TRY_in_trystatement6157); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'try'",input.LT(-1));}

            // Java.g:927:49: (t163= resourceSpecification )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==LPAREN) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // Java.g:927:50: t163= resourceSpecification
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("resourceSpecification"));}

                    pushFollow(FOLLOW_resourceSpecification_in_trystatement6165);
                    t163=resourceSpecification();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t163!=null?((Token)t163.start):null), (t163!=null?((Token)t163.stop):null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

            pushFollow(FOLLOW_block_in_trystatement6175);
            t164=block();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t164!=null?((Token)t164.start):null), (t164!=null?((Token)t164.stop):null));}

            // Java.g:928:9: (t165= catches 'finally' t166= block |t167= catches | 'finally' t168= block )?
            int alt106=4;
            alt106 = dfa106.predict(input);
            switch (alt106) {
                case 1 :
                    // Java.g:928:13: t165= catches 'finally' t166= block
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("catches"));}

                    pushFollow(FOLLOW_catches_in_trystatement6195);
                    t165=catches();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t165!=null?((Token)t165.start):null), (t165!=null?((Token)t165.stop):null));}

                    match(input,FINALLY,FOLLOW_FINALLY_in_trystatement6199); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'finally'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                    pushFollow(FOLLOW_block_in_trystatement6206);
                    t166=block();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t166!=null?((Token)t166.start):null), (t166!=null?((Token)t166.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:929:13: t167= catches
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("catches"));}

                    pushFollow(FOLLOW_catches_in_trystatement6226);
                    t167=catches();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t167!=null?((Token)t167.start):null), (t167!=null?((Token)t167.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:930:13: 'finally' t168= block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_trystatement6242); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'finally'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

                    pushFollow(FOLLOW_block_in_trystatement6249);
                    t168=block();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t168!=null?((Token)t168.start):null), (t168!=null?((Token)t168.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 62, trystatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "trystatement"


    public static class resourceSpecification_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "resourceSpecification"
    // Java.g:934:1: resourceSpecification : ( '(' ) t169= resources ( ( ';' ) )? ( ')' ) ;
    public final resourceSpecification_return resourceSpecification() throws RecognitionException {
        resourceSpecification_return retval = new resourceSpecification_return();
        retval.start = input.LT(1);

        int resourceSpecification_StartIndex = input.index();

        resources_return t169 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return retval; }

            // Java.g:935:5: ( ( '(' ) t169= resources ( ( ';' ) )? ( ')' ) )
            // Java.g:936:7: ( '(' ) t169= resources ( ( ';' ) )? ( ')' )
            {
            // Java.g:936:7: ( '(' )
            // Java.g:936:8: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_resourceSpecification6287); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("resources"));}

            pushFollow(FOLLOW_resources_in_resourceSpecification6295);
            t169=resources();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t169!=null?((Token)t169.start):null), (t169!=null?((Token)t169.stop):null));}

            // Java.g:936:170: ( ( ';' ) )?
            int alt107=2;
            int LA107_0 = input.LA(1);

            if ( (LA107_0==SEMI) ) {
                alt107=1;
            }
            switch (alt107) {
                case 1 :
                    // Java.g:936:171: ( ';' )
                    {
                    // Java.g:936:171: ( ';' )
                    // Java.g:936:172: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_resourceSpecification6301); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    }
                    break;

            }


            // Java.g:936:211: ( ')' )
            // Java.g:936:212: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_resourceSpecification6308); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 63, resourceSpecification_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "resourceSpecification"


    public static class resources_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "resources"
    // Java.g:939:1: resources :t170= resource ( ( ';' ) t171= resource )* ;
    public final resources_return resources() throws RecognitionException {
        resources_return retval = new resources_return();
        retval.start = input.LT(1);

        int resources_StartIndex = input.index();

        resource_return t170 =null;

        resource_return t171 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return retval; }

            // Java.g:940:5: (t170= resource ( ( ';' ) t171= resource )* )
            // Java.g:941:8: t170= resource ( ( ';' ) t171= resource )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("resource"));}

            pushFollow(FOLLOW_resource_in_resources6338);
            t170=resource();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t170!=null?((Token)t170.start):null), (t170!=null?((Token)t170.stop):null));}

            // Java.g:941:131: ( ( ';' ) t171= resource )*
            loop108:
            do {
                int alt108=2;
                int LA108_0 = input.LA(1);

                if ( (LA108_0==SEMI) ) {
                    int LA108_1 = input.LA(2);

                    if ( (LA108_1==FINAL||LA108_1==IDENTIFIER||LA108_1==MONKEYS_AT) ) {
                        alt108=1;
                    }


                }


                switch (alt108) {
            	case 1 :
            	    // Java.g:941:133: ( ';' ) t171= resource
            	    {
            	    // Java.g:941:133: ( ';' )
            	    // Java.g:941:134: ';'
            	    {
            	    match(input,SEMI,FOLLOW_SEMI_in_resources6345); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("resource"));}

            	    pushFollow(FOLLOW_resource_in_resources6353);
            	    t171=resource();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t171!=null?((Token)t171.start):null), (t171!=null?((Token)t171.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop108;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 64, resources_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "resources"


    public static class resource_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "resource"
    // Java.g:944:1: resource :t172= variableModifiers t173= classOrInterfaceType t174= variableDeclaratorId ( '=' ) t175= expression ;
    public final resource_return resource() throws RecognitionException {
        resource_return retval = new resource_return();
        retval.start = input.LT(1);

        int resource_StartIndex = input.index();

        variableModifiers_return t172 =null;

        classOrInterfaceType_return t173 =null;

        variableDeclaratorId_return t174 =null;

        expression_return t175 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return retval; }

            // Java.g:945:5: (t172= variableModifiers t173= classOrInterfaceType t174= variableDeclaratorId ( '=' ) t175= expression )
            // Java.g:946:7: t172= variableModifiers t173= classOrInterfaceType t174= variableDeclaratorId ( '=' ) t175= expression
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

            pushFollow(FOLLOW_variableModifiers_in_resource6385);
            t172=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t172!=null?((Token)t172.start):null), (t172!=null?((Token)t172.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

            pushFollow(FOLLOW_classOrInterfaceType_in_resource6393);
            t173=classOrInterfaceType();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t173!=null?((Token)t173.start):null), (t173!=null?((Token)t173.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableDeclaratorId"));}

            pushFollow(FOLLOW_variableDeclaratorId_in_resource6401);
            t174=variableDeclaratorId();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t174!=null?((Token)t174.start):null), (t174!=null?((Token)t174.stop):null));}

            // Java.g:946:442: ( '=' )
            // Java.g:946:443: '='
            {
            match(input,EQ,FOLLOW_EQ_in_resource6406); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

            pushFollow(FOLLOW_expression_in_resource6414);
            t175=expression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t175!=null?((Token)t175.start):null), (t175!=null?((Token)t175.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 65, resource_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "resource"


    public static class variableDeclaratorId_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "variableDeclaratorId"
    // Java.g:949:1: variableDeclaratorId : IDENTIFIER ( ( '[' ) ( ']' ) )* ;
    public final variableDeclaratorId_return variableDeclaratorId() throws RecognitionException {
        variableDeclaratorId_return retval = new variableDeclaratorId_return();
        retval.start = input.LT(1);

        int variableDeclaratorId_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return retval; }

            // Java.g:950:5: ( IDENTIFIER ( ( '[' ) ( ']' ) )* )
            // Java.g:951:9: IDENTIFIER ( ( '[' ) ( ']' ) )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableDeclaratorId6441); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:951:90: ( ( '[' ) ( ']' ) )*
            loop109:
            do {
                int alt109=2;
                int LA109_0 = input.LA(1);

                if ( (LA109_0==LBRACKET) ) {
                    alt109=1;
                }


                switch (alt109) {
            	case 1 :
            	    // Java.g:951:91: ( '[' ) ( ']' )
            	    {
            	    // Java.g:951:91: ( '[' )
            	    // Java.g:951:92: '['
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_variableDeclaratorId6447); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

            	    }


            	    // Java.g:951:129: ( ']' )
            	    // Java.g:951:130: ']'
            	    {
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_variableDeclaratorId6452); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

            	    }


            	    }
            	    break;

            	default :
            	    break loop109;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 66, variableDeclaratorId_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableDeclaratorId"


    public static class catches_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "catches"
    // Java.g:954:1: catches :t176= catchClause (t177= catchClause )* ;
    public final catches_return catches() throws RecognitionException {
        catches_return retval = new catches_return();
        retval.start = input.LT(1);

        int catches_StartIndex = input.index();

        catchClause_return t176 =null;

        catchClause_return t177 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 67) ) { return retval; }

            // Java.g:955:5: (t176= catchClause (t177= catchClause )* )
            // Java.g:955:9: t176= catchClause (t177= catchClause )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("catchClause"));}

            pushFollow(FOLLOW_catchClause_in_catches6479);
            t176=catchClause();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t176!=null?((Token)t176.start):null), (t176!=null?((Token)t176.stop):null));}

            // Java.g:956:9: (t177= catchClause )*
            loop110:
            do {
                int alt110=2;
                int LA110_0 = input.LA(1);

                if ( (LA110_0==CATCH) ) {
                    alt110=1;
                }


                switch (alt110) {
            	case 1 :
            	    // Java.g:956:10: t177= catchClause
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("catchClause"));}

            	    pushFollow(FOLLOW_catchClause_in_catches6496);
            	    t177=catchClause();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t177!=null?((Token)t177.start):null), (t177!=null?((Token)t177.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop110;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 67, catches_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "catches"


    public static class catchClause_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "catchClause"
    // Java.g:960:1: catchClause : 'catch' ( '(' ) t178= formalParameter ( ')' ) t179= block ;
    public final catchClause_return catchClause() throws RecognitionException {
        catchClause_return retval = new catchClause_return();
        retval.start = input.LT(1);

        int catchClause_StartIndex = input.index();

        formalParameter_return t178 =null;

        block_return t179 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 68) ) { return retval; }

            // Java.g:961:5: ( 'catch' ( '(' ) t178= formalParameter ( ')' ) t179= block )
            // Java.g:961:9: 'catch' ( '(' ) t178= formalParameter ( ')' ) t179= block
            {
            match(input,CATCH,FOLLOW_CATCH_in_catchClause6529); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'catch'",input.LT(-1));}

            // Java.g:961:53: ( '(' )
            // Java.g:961:54: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause6533); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("formalParameter"));}

            pushFollow(FOLLOW_formalParameter_in_catchClause6541);
            t178=formalParameter();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t178!=null?((Token)t178.start):null), (t178!=null?((Token)t178.stop):null));}

            // Java.g:962:9: ( ')' )
            // Java.g:962:10: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause6554); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("block"));}

            pushFollow(FOLLOW_block_in_catchClause6562);
            t179=block();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t179!=null?((Token)t179.start):null), (t179!=null?((Token)t179.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 68, catchClause_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "catchClause"


    public static class formalParameter_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "formalParameter"
    // Java.g:965:1: formalParameter :t180= variableModifiers t181= catchType IDENTIFIER ( ( '[' ) ( ']' ) )* ;
    public final formalParameter_return formalParameter() throws RecognitionException {
        formalParameter_return retval = new formalParameter_return();
        retval.start = input.LT(1);

        int formalParameter_StartIndex = input.index();

        variableModifiers_return t180 =null;

        catchType_return t181 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 69) ) { return retval; }

            // Java.g:966:5: (t180= variableModifiers t181= catchType IDENTIFIER ( ( '[' ) ( ']' ) )* )
            // Java.g:966:9: t180= variableModifiers t181= catchType IDENTIFIER ( ( '[' ) ( ']' ) )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

            pushFollow(FOLLOW_variableModifiers_in_formalParameter6589);
            t180=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t180!=null?((Token)t180.start):null), (t180!=null?((Token)t180.stop):null));}

            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("catchType"));}

            pushFollow(FOLLOW_catchType_in_formalParameter6597);
            t181=catchType();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t181!=null?((Token)t181.start):null), (t181!=null?((Token)t181.stop):null));}

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_formalParameter6601); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:967:9: ( ( '[' ) ( ']' ) )*
            loop111:
            do {
                int alt111=2;
                int LA111_0 = input.LA(1);

                if ( (LA111_0==LBRACKET) ) {
                    alt111=1;
                }


                switch (alt111) {
            	case 1 :
            	    // Java.g:967:10: ( '[' ) ( ']' )
            	    {
            	    // Java.g:967:10: ( '[' )
            	    // Java.g:967:11: '['
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_formalParameter6615); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

            	    }


            	    // Java.g:967:48: ( ']' )
            	    // Java.g:967:49: ']'
            	    {
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_formalParameter6620); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

            	    }


            	    }
            	    break;

            	default :
            	    break loop111;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 69, formalParameter_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameter"


    public static class catchType_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "catchType"
    // Java.g:971:1: catchType :t182= classOrInterfaceType ( ( '|' ) t183= classOrInterfaceType )* ;
    public final catchType_return catchType() throws RecognitionException {
        catchType_return retval = new catchType_return();
        retval.start = input.LT(1);

        int catchType_StartIndex = input.index();

        classOrInterfaceType_return t182 =null;

        classOrInterfaceType_return t183 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 70) ) { return retval; }

            // Java.g:972:5: (t182= classOrInterfaceType ( ( '|' ) t183= classOrInterfaceType )* )
            // Java.g:972:8: t182= classOrInterfaceType ( ( '|' ) t183= classOrInterfaceType )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

            pushFollow(FOLLOW_classOrInterfaceType_in_catchType6655);
            t182=classOrInterfaceType();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t182!=null?((Token)t182.start):null), (t182!=null?((Token)t182.stop):null));}

            // Java.g:972:155: ( ( '|' ) t183= classOrInterfaceType )*
            loop112:
            do {
                int alt112=2;
                int LA112_0 = input.LA(1);

                if ( (LA112_0==BAR) ) {
                    alt112=1;
                }


                switch (alt112) {
            	case 1 :
            	    // Java.g:972:157: ( '|' ) t183= classOrInterfaceType
            	    {
            	    // Java.g:972:157: ( '|' )
            	    // Java.g:972:158: '|'
            	    {
            	    match(input,BAR,FOLLOW_BAR_in_catchType6662); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'|'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

            	    pushFollow(FOLLOW_classOrInterfaceType_in_catchType6670);
            	    t183=classOrInterfaceType();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t183!=null?((Token)t183.start):null), (t183!=null?((Token)t183.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop112;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 70, catchType_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "catchType"


    public static class forstatement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "forstatement"
    // Java.g:975:1: forstatement : ( 'for' ( '(' ) t184= variableModifiers t185= type IDENTIFIER ( ( '[' ) ( ']' ) )* ( ':' ) t186= expression ( ')' ) t187= statement | 'for' ( '(' ) (t188= forInit )? ( ';' ) (t189= expression )? ( ';' ) (t190= expressionList )? ( ')' ) t191= statement );
    public final forstatement_return forstatement() throws RecognitionException {
        forstatement_return retval = new forstatement_return();
        retval.start = input.LT(1);

        int forstatement_StartIndex = input.index();

        variableModifiers_return t184 =null;

        type_return t185 =null;

        expression_return t186 =null;

        statement_return t187 =null;

        forInit_return t188 =null;

        expression_return t189 =null;

        expressionList_return t190 =null;

        statement_return t191 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 71) ) { return retval; }

            // Java.g:976:5: ( 'for' ( '(' ) t184= variableModifiers t185= type IDENTIFIER ( ( '[' ) ( ']' ) )* ( ':' ) t186= expression ( ')' ) t187= statement | 'for' ( '(' ) (t188= forInit )? ( ';' ) (t189= expression )? ( ';' ) (t190= expressionList )? ( ')' ) t191= statement )
            int alt117=2;
            int LA117_0 = input.LA(1);

            if ( (LA117_0==FOR) ) {
                int LA117_1 = input.LA(2);

                if ( (synpred164_Java()) ) {
                    alt117=1;
                }
                else if ( (true) ) {
                    alt117=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 117, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 117, 0, input);

                throw nvae;

            }
            switch (alt117) {
                case 1 :
                    // Java.g:978:9: 'for' ( '(' ) t184= variableModifiers t185= type IDENTIFIER ( ( '[' ) ( ']' ) )* ( ':' ) t186= expression ( ')' ) t187= statement
                    {
                    match(input,FOR,FOLLOW_FOR_in_forstatement6712); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'for'",input.LT(-1));}

                    // Java.g:978:49: ( '(' )
                    // Java.g:978:50: '('
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forstatement6716); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableModifiers"));}

                    pushFollow(FOLLOW_variableModifiers_in_forstatement6724);
                    t184=variableModifiers();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t184!=null?((Token)t184.start):null), (t184!=null?((Token)t184.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_forstatement6732);
                    t185=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t185!=null?((Token)t185.start):null), (t185!=null?((Token)t185.stop):null));}

                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_forstatement6736); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:978:424: ( ( '[' ) ( ']' ) )*
                    loop113:
                    do {
                        int alt113=2;
                        int LA113_0 = input.LA(1);

                        if ( (LA113_0==LBRACKET) ) {
                            alt113=1;
                        }


                        switch (alt113) {
                    	case 1 :
                    	    // Java.g:978:425: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:978:425: ( '[' )
                    	    // Java.g:978:426: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_forstatement6742); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:978:463: ( ']' )
                    	    // Java.g:978:464: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_forstatement6747); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop113;
                        }
                    } while (true);


                    // Java.g:978:503: ( ':' )
                    // Java.g:978:504: ':'
                    {
                    match(input,COLON,FOLLOW_COLON_in_forstatement6754); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_forstatement6770);
                    t186=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t186!=null?((Token)t186.start):null), (t186!=null?((Token)t186.stop):null));}

                    // Java.g:979:136: ( ')' )
                    // Java.g:979:137: ')'
                    {
                    match(input,RPAREN,FOLLOW_RPAREN_in_forstatement6775); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_forstatement6783);
                    t187=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t187!=null?((Token)t187.start):null), (t187!=null?((Token)t187.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:982:9: 'for' ( '(' ) (t188= forInit )? ( ';' ) (t189= expression )? ( ';' ) (t190= expressionList )? ( ')' ) t191= statement
                    {
                    match(input,FOR,FOLLOW_FOR_in_forstatement6817); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'for'",input.LT(-1));}

                    // Java.g:982:49: ( '(' )
                    // Java.g:982:50: '('
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_forstatement6821); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

                    }


                    // Java.g:983:17: (t188= forInit )?
                    int alt114=2;
                    int LA114_0 = input.LA(1);

                    if ( (LA114_0==BANG||(LA114_0 >= BINLITERAL && LA114_0 <= BOOLEAN)||LA114_0==BYTE||(LA114_0 >= CHAR && LA114_0 <= CHARLITERAL)||(LA114_0 >= DOUBLE && LA114_0 <= DOUBLELITERAL)||(LA114_0 >= FALSE && LA114_0 <= FINAL)||(LA114_0 >= FLOAT && LA114_0 <= FLOATLITERAL)||LA114_0==IDENTIFIER||LA114_0==INT||LA114_0==INTLITERAL||(LA114_0 >= LONG && LA114_0 <= LPAREN)||LA114_0==MONKEYS_AT||(LA114_0 >= NEW && LA114_0 <= NULL)||LA114_0==PLUS||LA114_0==PLUSPLUS||LA114_0==SHORT||(LA114_0 >= STRINGLITERAL && LA114_0 <= SUB)||(LA114_0 >= SUBSUB && LA114_0 <= SUPER)||LA114_0==THIS||LA114_0==TILDE||LA114_0==TRUE||LA114_0==VOID) ) {
                        alt114=1;
                    }
                    switch (alt114) {
                        case 1 :
                            // Java.g:983:18: t188= forInit
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("forInit"));}

                            pushFollow(FOLLOW_forInit_in_forstatement6847);
                            t188=forInit();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t188!=null?((Token)t188.start):null), (t188!=null?((Token)t188.stop):null));}

                            }
                            break;

                    }


                    // Java.g:984:20: ( ';' )
                    // Java.g:984:21: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_forstatement6871); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    // Java.g:985:17: (t189= expression )?
                    int alt115=2;
                    int LA115_0 = input.LA(1);

                    if ( (LA115_0==BANG||(LA115_0 >= BINLITERAL && LA115_0 <= BOOLEAN)||LA115_0==BYTE||(LA115_0 >= CHAR && LA115_0 <= CHARLITERAL)||(LA115_0 >= DOUBLE && LA115_0 <= DOUBLELITERAL)||LA115_0==FALSE||(LA115_0 >= FLOAT && LA115_0 <= FLOATLITERAL)||LA115_0==IDENTIFIER||LA115_0==INT||LA115_0==INTLITERAL||(LA115_0 >= LONG && LA115_0 <= LPAREN)||(LA115_0 >= NEW && LA115_0 <= NULL)||LA115_0==PLUS||LA115_0==PLUSPLUS||LA115_0==SHORT||(LA115_0 >= STRINGLITERAL && LA115_0 <= SUB)||(LA115_0 >= SUBSUB && LA115_0 <= SUPER)||LA115_0==THIS||LA115_0==TILDE||LA115_0==TRUE||LA115_0==VOID) ) {
                        alt115=1;
                    }
                    switch (alt115) {
                        case 1 :
                            // Java.g:985:18: t189= expression
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                            pushFollow(FOLLOW_expression_in_forstatement6897);
                            t189=expression();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t189!=null?((Token)t189.start):null), (t189!=null?((Token)t189.stop):null));}

                            }
                            break;

                    }


                    // Java.g:986:20: ( ';' )
                    // Java.g:986:21: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_forstatement6921); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("';'",input.LT(-1));}

                    }


                    // Java.g:987:17: (t190= expressionList )?
                    int alt116=2;
                    int LA116_0 = input.LA(1);

                    if ( (LA116_0==BANG||(LA116_0 >= BINLITERAL && LA116_0 <= BOOLEAN)||LA116_0==BYTE||(LA116_0 >= CHAR && LA116_0 <= CHARLITERAL)||(LA116_0 >= DOUBLE && LA116_0 <= DOUBLELITERAL)||LA116_0==FALSE||(LA116_0 >= FLOAT && LA116_0 <= FLOATLITERAL)||LA116_0==IDENTIFIER||LA116_0==INT||LA116_0==INTLITERAL||(LA116_0 >= LONG && LA116_0 <= LPAREN)||(LA116_0 >= NEW && LA116_0 <= NULL)||LA116_0==PLUS||LA116_0==PLUSPLUS||LA116_0==SHORT||(LA116_0 >= STRINGLITERAL && LA116_0 <= SUB)||(LA116_0 >= SUBSUB && LA116_0 <= SUPER)||LA116_0==THIS||LA116_0==TILDE||LA116_0==TRUE||LA116_0==VOID) ) {
                        alt116=1;
                    }
                    switch (alt116) {
                        case 1 :
                            // Java.g:987:18: t190= expressionList
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expressionList"));}

                            pushFollow(FOLLOW_expressionList_in_forstatement6947);
                            t190=expressionList();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t190!=null?((Token)t190.start):null), (t190!=null?((Token)t190.stop):null));}

                            }
                            break;

                    }


                    // Java.g:988:20: ( ')' )
                    // Java.g:988:21: ')'
                    {
                    match(input,RPAREN,FOLLOW_RPAREN_in_forstatement6971); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("statement"));}

                    pushFollow(FOLLOW_statement_in_forstatement6979);
                    t191=statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t191!=null?((Token)t191.start):null), (t191!=null?((Token)t191.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 71, forstatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "forstatement"


    public static class forInit_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "forInit"
    // Java.g:991:1: forInit : (t192= localVariableDeclaration |t193= expressionList );
    public final forInit_return forInit() throws RecognitionException {
        forInit_return retval = new forInit_return();
        retval.start = input.LT(1);

        int forInit_StartIndex = input.index();

        localVariableDeclaration_return t192 =null;

        expressionList_return t193 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 72) ) { return retval; }

            // Java.g:992:5: (t192= localVariableDeclaration |t193= expressionList )
            int alt118=2;
            switch ( input.LA(1) ) {
            case FINAL:
            case MONKEYS_AT:
                {
                alt118=1;
                }
                break;
            case IDENTIFIER:
                {
                int LA118_3 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
                {
                int LA118_4 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 4, input);

                    throw nvae;

                }
                }
                break;
            case CHAR:
                {
                int LA118_5 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 5, input);

                    throw nvae;

                }
                }
                break;
            case BYTE:
                {
                int LA118_6 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 6, input);

                    throw nvae;

                }
                }
                break;
            case SHORT:
                {
                int LA118_7 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 7, input);

                    throw nvae;

                }
                }
                break;
            case INT:
                {
                int LA118_8 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 8, input);

                    throw nvae;

                }
                }
                break;
            case LONG:
                {
                int LA118_9 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 9, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA118_10 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 10, input);

                    throw nvae;

                }
                }
                break;
            case DOUBLE:
                {
                int LA118_11 = input.LA(2);

                if ( (synpred168_Java()) ) {
                    alt118=1;
                }
                else if ( (true) ) {
                    alt118=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 118, 11, input);

                    throw nvae;

                }
                }
                break;
            case BANG:
            case BINLITERAL:
            case CHARLITERAL:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case INTLITERAL:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt118=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 118, 0, input);

                throw nvae;

            }

            switch (alt118) {
                case 1 :
                    // Java.g:992:9: t192= localVariableDeclaration
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("localVariableDeclaration"));}

                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit7005);
                    t192=localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t192!=null?((Token)t192.start):null), (t192!=null?((Token)t192.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:993:9: t193= expressionList
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expressionList"));}

                    pushFollow(FOLLOW_expressionList_in_forInit7021);
                    t193=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t193!=null?((Token)t193.start):null), (t193!=null?((Token)t193.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 72, forInit_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "forInit"


    public static class parExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "parExpression"
    // Java.g:996:1: parExpression : ( '(' ) t194= expression ( ')' ) ;
    public final parExpression_return parExpression() throws RecognitionException {
        parExpression_return retval = new parExpression_return();
        retval.start = input.LT(1);

        int parExpression_StartIndex = input.index();

        expression_return t194 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 73) ) { return retval; }

            // Java.g:997:5: ( ( '(' ) t194= expression ( ')' ) )
            // Java.g:997:9: ( '(' ) t194= expression ( ')' )
            {
            // Java.g:997:9: ( '(' )
            // Java.g:997:10: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_parExpression7044); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

            pushFollow(FOLLOW_expression_in_parExpression7052);
            t194=expression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t194!=null?((Token)t194.start):null), (t194!=null?((Token)t194.stop):null));}

            // Java.g:997:174: ( ')' )
            // Java.g:997:175: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_parExpression7057); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 73, parExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "parExpression"


    public static class expressionList_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "expressionList"
    // Java.g:1000:1: expressionList :t195= expression ( ( ',' ) t196= expression )* ;
    public final expressionList_return expressionList() throws RecognitionException {
        expressionList_return retval = new expressionList_return();
        retval.start = input.LT(1);

        int expressionList_StartIndex = input.index();

        expression_return t195 =null;

        expression_return t196 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 74) ) { return retval; }

            // Java.g:1001:5: (t195= expression ( ( ',' ) t196= expression )* )
            // Java.g:1001:9: t195= expression ( ( ',' ) t196= expression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

            pushFollow(FOLLOW_expression_in_expressionList7083);
            t195=expression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t195!=null?((Token)t195.start):null), (t195!=null?((Token)t195.stop):null));}

            // Java.g:1002:9: ( ( ',' ) t196= expression )*
            loop119:
            do {
                int alt119=2;
                int LA119_0 = input.LA(1);

                if ( (LA119_0==COMMA) ) {
                    alt119=1;
                }


                switch (alt119) {
            	case 1 :
            	    // Java.g:1002:10: ( ',' ) t196= expression
            	    {
            	    // Java.g:1002:10: ( ',' )
            	    // Java.g:1002:11: ','
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_expressionList7097); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

            	    pushFollow(FOLLOW_expression_in_expressionList7105);
            	    t196=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t196!=null?((Token)t196.start):null), (t196!=null?((Token)t196.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop119;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 74, expressionList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "expressionList"


    public static class expression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "expression"
    // Java.g:1007:1: expression :t197= conditionalExpression (t198= assignmentOperator t199= expression )? ;
    public final expression_return expression() throws RecognitionException {
        expression_return retval = new expression_return();
        retval.start = input.LT(1);

        int expression_StartIndex = input.index();

        conditionalExpression_return t197 =null;

        assignmentOperator_return t198 =null;

        expression_return t199 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 75) ) { return retval; }

            // Java.g:1008:5: (t197= conditionalExpression (t198= assignmentOperator t199= expression )? )
            // Java.g:1008:9: t197= conditionalExpression (t198= assignmentOperator t199= expression )?
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalExpression"));}

            pushFollow(FOLLOW_conditionalExpression_in_expression7143);
            t197=conditionalExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t197!=null?((Token)t197.start):null), (t197!=null?((Token)t197.stop):null));}

            // Java.g:1009:9: (t198= assignmentOperator t199= expression )?
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==AMPEQ||LA120_0==BAREQ||LA120_0==CARETEQ||LA120_0==EQ||LA120_0==GT||LA120_0==LT||LA120_0==PERCENTEQ||LA120_0==PLUSEQ||LA120_0==SLASHEQ||LA120_0==STAREQ||LA120_0==SUBEQ) ) {
                alt120=1;
            }
            switch (alt120) {
                case 1 :
                    // Java.g:1009:10: t198= assignmentOperator t199= expression
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("assignmentOperator"));}

                    pushFollow(FOLLOW_assignmentOperator_in_expression7160);
                    t198=assignmentOperator();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t198!=null?((Token)t198.start):null), (t198!=null?((Token)t198.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_expression7168);
                    t199=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t199!=null?((Token)t199.start):null), (t199!=null?((Token)t199.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 75, expression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "expression"


    public static class assignmentOperator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "assignmentOperator"
    // Java.g:1014:1: assignmentOperator : ( ( '=' ) | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' ) ( '<' ) ( '=' ) | ( '>' ) ( '>' ) ( '>' ) ( '=' ) | ( '>' ) ( '>' ) ( '=' ) );
    public final assignmentOperator_return assignmentOperator() throws RecognitionException {
        assignmentOperator_return retval = new assignmentOperator_return();
        retval.start = input.LT(1);

        int assignmentOperator_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 76) ) { return retval; }

            // Java.g:1015:5: ( ( '=' ) | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' ) ( '<' ) ( '=' ) | ( '>' ) ( '>' ) ( '>' ) ( '=' ) | ( '>' ) ( '>' ) ( '=' ) )
            int alt121=12;
            switch ( input.LA(1) ) {
            case EQ:
                {
                alt121=1;
                }
                break;
            case PLUSEQ:
                {
                alt121=2;
                }
                break;
            case SUBEQ:
                {
                alt121=3;
                }
                break;
            case STAREQ:
                {
                alt121=4;
                }
                break;
            case SLASHEQ:
                {
                alt121=5;
                }
                break;
            case AMPEQ:
                {
                alt121=6;
                }
                break;
            case BAREQ:
                {
                alt121=7;
                }
                break;
            case CARETEQ:
                {
                alt121=8;
                }
                break;
            case PERCENTEQ:
                {
                alt121=9;
                }
                break;
            case LT:
                {
                alt121=10;
                }
                break;
            case GT:
                {
                int LA121_11 = input.LA(2);

                if ( (LA121_11==GT) ) {
                    int LA121_12 = input.LA(3);

                    if ( (LA121_12==GT) ) {
                        alt121=11;
                    }
                    else if ( (LA121_12==EQ) ) {
                        alt121=12;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 121, 12, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 121, 11, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 121, 0, input);

                throw nvae;

            }

            switch (alt121) {
                case 1 :
                    // Java.g:1015:9: ( '=' )
                    {
                    // Java.g:1015:9: ( '=' )
                    // Java.g:1015:10: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator7203); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1016:9: '+='
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator7215); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'+='",input.LT(-1));}

                    }
                    break;
                case 3 :
                    // Java.g:1017:9: '-='
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator7226); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'-='",input.LT(-1));}

                    }
                    break;
                case 4 :
                    // Java.g:1018:9: '*='
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator7237); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'*='",input.LT(-1));}

                    }
                    break;
                case 5 :
                    // Java.g:1019:9: '/='
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator7248); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'/='",input.LT(-1));}

                    }
                    break;
                case 6 :
                    // Java.g:1020:9: '&='
                    {
                    match(input,AMPEQ,FOLLOW_AMPEQ_in_assignmentOperator7259); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'&='",input.LT(-1));}

                    }
                    break;
                case 7 :
                    // Java.g:1021:9: '|='
                    {
                    match(input,BAREQ,FOLLOW_BAREQ_in_assignmentOperator7270); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'|='",input.LT(-1));}

                    }
                    break;
                case 8 :
                    // Java.g:1022:9: '^='
                    {
                    match(input,CARETEQ,FOLLOW_CARETEQ_in_assignmentOperator7281); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'^='",input.LT(-1));}

                    }
                    break;
                case 9 :
                    // Java.g:1023:9: '%='
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator7292); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'%='",input.LT(-1));}

                    }
                    break;
                case 10 :
                    // Java.g:1024:10: ( '<' ) ( '<' ) ( '=' )
                    {
                    // Java.g:1024:10: ( '<' )
                    // Java.g:1024:11: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_assignmentOperator7305); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1024:48: ( '<' )
                    // Java.g:1024:49: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_assignmentOperator7310); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1024:86: ( '=' )
                    // Java.g:1024:87: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator7315); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;
                case 11 :
                    // Java.g:1025:10: ( '>' ) ( '>' ) ( '>' ) ( '=' )
                    {
                    // Java.g:1025:10: ( '>' )
                    // Java.g:1025:11: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator7329); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1025:48: ( '>' )
                    // Java.g:1025:49: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator7334); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1025:86: ( '>' )
                    // Java.g:1025:87: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator7339); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1025:124: ( '=' )
                    // Java.g:1025:125: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator7344); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;
                case 12 :
                    // Java.g:1026:10: ( '>' ) ( '>' ) ( '=' )
                    {
                    // Java.g:1026:10: ( '>' )
                    // Java.g:1026:11: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator7358); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1026:48: ( '>' )
                    // Java.g:1026:49: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator7363); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1026:86: ( '=' )
                    // Java.g:1026:87: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator7368); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 76, assignmentOperator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "assignmentOperator"


    public static class conditionalExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "conditionalExpression"
    // Java.g:1030:1: conditionalExpression :t200= conditionalOrExpression ( ( '?' ) t201= expression ( ':' ) t202= conditionalExpression )? ;
    public final conditionalExpression_return conditionalExpression() throws RecognitionException {
        conditionalExpression_return retval = new conditionalExpression_return();
        retval.start = input.LT(1);

        int conditionalExpression_StartIndex = input.index();

        conditionalOrExpression_return t200 =null;

        expression_return t201 =null;

        conditionalExpression_return t202 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 77) ) { return retval; }

            // Java.g:1031:5: (t200= conditionalOrExpression ( ( '?' ) t201= expression ( ':' ) t202= conditionalExpression )? )
            // Java.g:1031:9: t200= conditionalOrExpression ( ( '?' ) t201= expression ( ':' ) t202= conditionalExpression )?
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalOrExpression"));}

            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression7395);
            t200=conditionalOrExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t200!=null?((Token)t200.start):null), (t200!=null?((Token)t200.stop):null));}

            // Java.g:1032:9: ( ( '?' ) t201= expression ( ':' ) t202= conditionalExpression )?
            int alt122=2;
            int LA122_0 = input.LA(1);

            if ( (LA122_0==QUES) ) {
                alt122=1;
            }
            switch (alt122) {
                case 1 :
                    // Java.g:1032:10: ( '?' ) t201= expression ( ':' ) t202= conditionalExpression
                    {
                    // Java.g:1032:10: ( '?' )
                    // Java.g:1032:11: '?'
                    {
                    match(input,QUES,FOLLOW_QUES_in_conditionalExpression7409); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'?'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_conditionalExpression7417);
                    t201=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t201!=null?((Token)t201.start):null), (t201!=null?((Token)t201.stop):null));}

                    // Java.g:1032:175: ( ':' )
                    // Java.g:1032:176: ':'
                    {
                    match(input,COLON,FOLLOW_COLON_in_conditionalExpression7422); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("':'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalExpression"));}

                    pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression7430);
                    t202=conditionalExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t202!=null?((Token)t202.start):null), (t202!=null?((Token)t202.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 77, conditionalExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalExpression"


    public static class conditionalOrExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "conditionalOrExpression"
    // Java.g:1036:1: conditionalOrExpression :t203= conditionalAndExpression ( '||' t204= conditionalAndExpression )* ;
    public final conditionalOrExpression_return conditionalOrExpression() throws RecognitionException {
        conditionalOrExpression_return retval = new conditionalOrExpression_return();
        retval.start = input.LT(1);

        int conditionalOrExpression_StartIndex = input.index();

        conditionalAndExpression_return t203 =null;

        conditionalAndExpression_return t204 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 78) ) { return retval; }

            // Java.g:1037:5: (t203= conditionalAndExpression ( '||' t204= conditionalAndExpression )* )
            // Java.g:1037:9: t203= conditionalAndExpression ( '||' t204= conditionalAndExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalAndExpression"));}

            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression7467);
            t203=conditionalAndExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t203!=null?((Token)t203.start):null), (t203!=null?((Token)t203.stop):null));}

            // Java.g:1038:9: ( '||' t204= conditionalAndExpression )*
            loop123:
            do {
                int alt123=2;
                int LA123_0 = input.LA(1);

                if ( (LA123_0==BARBAR) ) {
                    alt123=1;
                }


                switch (alt123) {
            	case 1 :
            	    // Java.g:1038:10: '||' t204= conditionalAndExpression
            	    {
            	    match(input,BARBAR,FOLLOW_BARBAR_in_conditionalOrExpression7480); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'||'",input.LT(-1));}

            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("conditionalAndExpression"));}

            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression7487);
            	    t204=conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t204!=null?((Token)t204.start):null), (t204!=null?((Token)t204.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop123;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 78, conditionalOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalOrExpression"


    public static class conditionalAndExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "conditionalAndExpression"
    // Java.g:1042:1: conditionalAndExpression :t205= inclusiveOrExpression ( '&&' t206= inclusiveOrExpression )* ;
    public final conditionalAndExpression_return conditionalAndExpression() throws RecognitionException {
        conditionalAndExpression_return retval = new conditionalAndExpression_return();
        retval.start = input.LT(1);

        int conditionalAndExpression_StartIndex = input.index();

        inclusiveOrExpression_return t205 =null;

        inclusiveOrExpression_return t206 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 79) ) { return retval; }

            // Java.g:1043:5: (t205= inclusiveOrExpression ( '&&' t206= inclusiveOrExpression )* )
            // Java.g:1043:9: t205= inclusiveOrExpression ( '&&' t206= inclusiveOrExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("inclusiveOrExpression"));}

            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression7524);
            t205=inclusiveOrExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t205!=null?((Token)t205.start):null), (t205!=null?((Token)t205.stop):null));}

            // Java.g:1044:9: ( '&&' t206= inclusiveOrExpression )*
            loop124:
            do {
                int alt124=2;
                int LA124_0 = input.LA(1);

                if ( (LA124_0==AMPAMP) ) {
                    alt124=1;
                }


                switch (alt124) {
            	case 1 :
            	    // Java.g:1044:10: '&&' t206= inclusiveOrExpression
            	    {
            	    match(input,AMPAMP,FOLLOW_AMPAMP_in_conditionalAndExpression7537); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'&&'",input.LT(-1));}

            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("inclusiveOrExpression"));}

            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression7544);
            	    t206=inclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t206!=null?((Token)t206.start):null), (t206!=null?((Token)t206.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop124;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 79, conditionalAndExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalAndExpression"


    public static class inclusiveOrExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "inclusiveOrExpression"
    // Java.g:1048:1: inclusiveOrExpression :t207= exclusiveOrExpression ( ( '|' ) t208= exclusiveOrExpression )* ;
    public final inclusiveOrExpression_return inclusiveOrExpression() throws RecognitionException {
        inclusiveOrExpression_return retval = new inclusiveOrExpression_return();
        retval.start = input.LT(1);

        int inclusiveOrExpression_StartIndex = input.index();

        exclusiveOrExpression_return t207 =null;

        exclusiveOrExpression_return t208 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 80) ) { return retval; }

            // Java.g:1049:5: (t207= exclusiveOrExpression ( ( '|' ) t208= exclusiveOrExpression )* )
            // Java.g:1049:9: t207= exclusiveOrExpression ( ( '|' ) t208= exclusiveOrExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("exclusiveOrExpression"));}

            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression7581);
            t207=exclusiveOrExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t207!=null?((Token)t207.start):null), (t207!=null?((Token)t207.stop):null));}

            // Java.g:1050:9: ( ( '|' ) t208= exclusiveOrExpression )*
            loop125:
            do {
                int alt125=2;
                int LA125_0 = input.LA(1);

                if ( (LA125_0==BAR) ) {
                    alt125=1;
                }


                switch (alt125) {
            	case 1 :
            	    // Java.g:1050:10: ( '|' ) t208= exclusiveOrExpression
            	    {
            	    // Java.g:1050:10: ( '|' )
            	    // Java.g:1050:11: '|'
            	    {
            	    match(input,BAR,FOLLOW_BAR_in_inclusiveOrExpression7595); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'|'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("exclusiveOrExpression"));}

            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression7603);
            	    t208=exclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t208!=null?((Token)t208.start):null), (t208!=null?((Token)t208.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop125;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 80, inclusiveOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "inclusiveOrExpression"


    public static class exclusiveOrExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "exclusiveOrExpression"
    // Java.g:1054:1: exclusiveOrExpression :t209= andExpression ( ( '^' ) t210= andExpression )* ;
    public final exclusiveOrExpression_return exclusiveOrExpression() throws RecognitionException {
        exclusiveOrExpression_return retval = new exclusiveOrExpression_return();
        retval.start = input.LT(1);

        int exclusiveOrExpression_StartIndex = input.index();

        andExpression_return t209 =null;

        andExpression_return t210 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 81) ) { return retval; }

            // Java.g:1055:5: (t209= andExpression ( ( '^' ) t210= andExpression )* )
            // Java.g:1055:9: t209= andExpression ( ( '^' ) t210= andExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("andExpression"));}

            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression7640);
            t209=andExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t209!=null?((Token)t209.start):null), (t209!=null?((Token)t209.stop):null));}

            // Java.g:1056:9: ( ( '^' ) t210= andExpression )*
            loop126:
            do {
                int alt126=2;
                int LA126_0 = input.LA(1);

                if ( (LA126_0==CARET) ) {
                    alt126=1;
                }


                switch (alt126) {
            	case 1 :
            	    // Java.g:1056:10: ( '^' ) t210= andExpression
            	    {
            	    // Java.g:1056:10: ( '^' )
            	    // Java.g:1056:11: '^'
            	    {
            	    match(input,CARET,FOLLOW_CARET_in_exclusiveOrExpression7654); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'^'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("andExpression"));}

            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression7662);
            	    t210=andExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t210!=null?((Token)t210.start):null), (t210!=null?((Token)t210.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop126;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 81, exclusiveOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "exclusiveOrExpression"


    public static class andExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "andExpression"
    // Java.g:1060:1: andExpression :t211= equalityExpression ( ( '&' ) t212= equalityExpression )* ;
    public final andExpression_return andExpression() throws RecognitionException {
        andExpression_return retval = new andExpression_return();
        retval.start = input.LT(1);

        int andExpression_StartIndex = input.index();

        equalityExpression_return t211 =null;

        equalityExpression_return t212 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 82) ) { return retval; }

            // Java.g:1061:5: (t211= equalityExpression ( ( '&' ) t212= equalityExpression )* )
            // Java.g:1061:9: t211= equalityExpression ( ( '&' ) t212= equalityExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("equalityExpression"));}

            pushFollow(FOLLOW_equalityExpression_in_andExpression7699);
            t211=equalityExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t211!=null?((Token)t211.start):null), (t211!=null?((Token)t211.stop):null));}

            // Java.g:1062:9: ( ( '&' ) t212= equalityExpression )*
            loop127:
            do {
                int alt127=2;
                int LA127_0 = input.LA(1);

                if ( (LA127_0==AMP) ) {
                    alt127=1;
                }


                switch (alt127) {
            	case 1 :
            	    // Java.g:1062:10: ( '&' ) t212= equalityExpression
            	    {
            	    // Java.g:1062:10: ( '&' )
            	    // Java.g:1062:11: '&'
            	    {
            	    match(input,AMP,FOLLOW_AMP_in_andExpression7713); if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.addLeaf("'&'",input.LT(-1));}

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("equalityExpression"));}

            	    pushFollow(FOLLOW_equalityExpression_in_andExpression7721);
            	    t212=equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t212!=null?((Token)t212.start):null), (t212!=null?((Token)t212.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 82, andExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "andExpression"


    public static class equalityExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "equalityExpression"
    // Java.g:1066:1: equalityExpression :t213= instanceOfExpression ( ( '==' | '!=' ) t214= instanceOfExpression )* ;
    public final equalityExpression_return equalityExpression() throws RecognitionException {
        equalityExpression_return retval = new equalityExpression_return();
        retval.start = input.LT(1);

        int equalityExpression_StartIndex = input.index();

        instanceOfExpression_return t213 =null;

        instanceOfExpression_return t214 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 83) ) { return retval; }

            // Java.g:1067:5: (t213= instanceOfExpression ( ( '==' | '!=' ) t214= instanceOfExpression )* )
            // Java.g:1067:9: t213= instanceOfExpression ( ( '==' | '!=' ) t214= instanceOfExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("instanceOfExpression"));}

            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression7758);
            t213=instanceOfExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t213!=null?((Token)t213.start):null), (t213!=null?((Token)t213.stop):null));}

            // Java.g:1068:9: ( ( '==' | '!=' ) t214= instanceOfExpression )*
            loop129:
            do {
                int alt129=2;
                int LA129_0 = input.LA(1);

                if ( (LA129_0==BANGEQ||LA129_0==EQEQ) ) {
                    alt129=1;
                }


                switch (alt129) {
            	case 1 :
            	    // Java.g:1069:13: ( '==' | '!=' ) t214= instanceOfExpression
            	    {
            	    // Java.g:1069:13: ( '==' | '!=' )
            	    int alt128=2;
            	    int LA128_0 = input.LA(1);

            	    if ( (LA128_0==EQEQ) ) {
            	        alt128=1;
            	    }
            	    else if ( (LA128_0==BANGEQ) ) {
            	        alt128=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 128, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt128) {
            	        case 1 :
            	            // Java.g:1069:17: '=='
            	            {
            	            match(input,EQEQ,FOLLOW_EQEQ_in_equalityExpression7791); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'=='",input.LT(-1));}

            	            }
            	            break;
            	        case 2 :
            	            // Java.g:1070:17: '!='
            	            {
            	            match(input,BANGEQ,FOLLOW_BANGEQ_in_equalityExpression7810); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'!='",input.LT(-1));}

            	            }
            	            break;

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("instanceOfExpression"));}

            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression7843);
            	    t214=instanceOfExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t214!=null?((Token)t214.start):null), (t214!=null?((Token)t214.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop129;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 83, equalityExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "equalityExpression"


    public static class instanceOfExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "instanceOfExpression"
    // Java.g:1076:1: instanceOfExpression :t215= relationalExpression ( 'instanceof' t216= type )? ;
    public final instanceOfExpression_return instanceOfExpression() throws RecognitionException {
        instanceOfExpression_return retval = new instanceOfExpression_return();
        retval.start = input.LT(1);

        int instanceOfExpression_StartIndex = input.index();

        relationalExpression_return t215 =null;

        type_return t216 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 84) ) { return retval; }

            // Java.g:1077:5: (t215= relationalExpression ( 'instanceof' t216= type )? )
            // Java.g:1077:9: t215= relationalExpression ( 'instanceof' t216= type )?
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("relationalExpression"));}

            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression7880);
            t215=relationalExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t215!=null?((Token)t215.start):null), (t215!=null?((Token)t215.stop):null));}

            // Java.g:1078:9: ( 'instanceof' t216= type )?
            int alt130=2;
            int LA130_0 = input.LA(1);

            if ( (LA130_0==INSTANCEOF) ) {
                alt130=1;
            }
            switch (alt130) {
                case 1 :
                    // Java.g:1078:10: 'instanceof' t216= type
                    {
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression7893); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'instanceof'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_instanceOfExpression7900);
                    t216=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t216!=null?((Token)t216.start):null), (t216!=null?((Token)t216.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 84, instanceOfExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "instanceOfExpression"


    public static class relationalExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "relationalExpression"
    // Java.g:1082:1: relationalExpression :t217= shiftExpression (t218= relationalOp t219= shiftExpression )* ;
    public final relationalExpression_return relationalExpression() throws RecognitionException {
        relationalExpression_return retval = new relationalExpression_return();
        retval.start = input.LT(1);

        int relationalExpression_StartIndex = input.index();

        shiftExpression_return t217 =null;

        relationalOp_return t218 =null;

        shiftExpression_return t219 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 85) ) { return retval; }

            // Java.g:1083:5: (t217= shiftExpression (t218= relationalOp t219= shiftExpression )* )
            // Java.g:1083:9: t217= shiftExpression (t218= relationalOp t219= shiftExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("shiftExpression"));}

            pushFollow(FOLLOW_shiftExpression_in_relationalExpression7937);
            t217=shiftExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t217!=null?((Token)t217.start):null), (t217!=null?((Token)t217.stop):null));}

            // Java.g:1084:9: (t218= relationalOp t219= shiftExpression )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==LT) ) {
                    int LA131_2 = input.LA(2);

                    if ( (LA131_2==BANG||(LA131_2 >= BINLITERAL && LA131_2 <= BOOLEAN)||LA131_2==BYTE||(LA131_2 >= CHAR && LA131_2 <= CHARLITERAL)||(LA131_2 >= DOUBLE && LA131_2 <= DOUBLELITERAL)||LA131_2==EQ||LA131_2==FALSE||(LA131_2 >= FLOAT && LA131_2 <= FLOATLITERAL)||LA131_2==IDENTIFIER||LA131_2==INT||LA131_2==INTLITERAL||(LA131_2 >= LONG && LA131_2 <= LPAREN)||(LA131_2 >= NEW && LA131_2 <= NULL)||LA131_2==PLUS||LA131_2==PLUSPLUS||LA131_2==SHORT||(LA131_2 >= STRINGLITERAL && LA131_2 <= SUB)||(LA131_2 >= SUBSUB && LA131_2 <= SUPER)||LA131_2==THIS||LA131_2==TILDE||LA131_2==TRUE||LA131_2==VOID) ) {
                        alt131=1;
                    }


                }
                else if ( (LA131_0==GT) ) {
                    int LA131_3 = input.LA(2);

                    if ( (LA131_3==BANG||(LA131_3 >= BINLITERAL && LA131_3 <= BOOLEAN)||LA131_3==BYTE||(LA131_3 >= CHAR && LA131_3 <= CHARLITERAL)||(LA131_3 >= DOUBLE && LA131_3 <= DOUBLELITERAL)||LA131_3==EQ||LA131_3==FALSE||(LA131_3 >= FLOAT && LA131_3 <= FLOATLITERAL)||LA131_3==IDENTIFIER||LA131_3==INT||LA131_3==INTLITERAL||(LA131_3 >= LONG && LA131_3 <= LPAREN)||(LA131_3 >= NEW && LA131_3 <= NULL)||LA131_3==PLUS||LA131_3==PLUSPLUS||LA131_3==SHORT||(LA131_3 >= STRINGLITERAL && LA131_3 <= SUB)||(LA131_3 >= SUBSUB && LA131_3 <= SUPER)||LA131_3==THIS||LA131_3==TILDE||LA131_3==TRUE||LA131_3==VOID) ) {
                        alt131=1;
                    }


                }


                switch (alt131) {
            	case 1 :
            	    // Java.g:1084:10: t218= relationalOp t219= shiftExpression
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("relationalOp"));}

            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression7954);
            	    t218=relationalOp();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t218!=null?((Token)t218.start):null), (t218!=null?((Token)t218.stop):null));}

            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("shiftExpression"));}

            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression7962);
            	    t219=shiftExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t219!=null?((Token)t219.start):null), (t219!=null?((Token)t219.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 85, relationalExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "relationalExpression"


    public static class relationalOp_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "relationalOp"
    // Java.g:1088:1: relationalOp : ( ( '<' ) ( '=' ) | ( '>' ) ( '=' ) | ( '<' ) | ( '>' ) );
    public final relationalOp_return relationalOp() throws RecognitionException {
        relationalOp_return retval = new relationalOp_return();
        retval.start = input.LT(1);

        int relationalOp_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 86) ) { return retval; }

            // Java.g:1089:5: ( ( '<' ) ( '=' ) | ( '>' ) ( '=' ) | ( '<' ) | ( '>' ) )
            int alt132=4;
            int LA132_0 = input.LA(1);

            if ( (LA132_0==LT) ) {
                int LA132_1 = input.LA(2);

                if ( (LA132_1==EQ) ) {
                    alt132=1;
                }
                else if ( (LA132_1==BANG||(LA132_1 >= BINLITERAL && LA132_1 <= BOOLEAN)||LA132_1==BYTE||(LA132_1 >= CHAR && LA132_1 <= CHARLITERAL)||(LA132_1 >= DOUBLE && LA132_1 <= DOUBLELITERAL)||LA132_1==FALSE||(LA132_1 >= FLOAT && LA132_1 <= FLOATLITERAL)||LA132_1==IDENTIFIER||LA132_1==INT||LA132_1==INTLITERAL||(LA132_1 >= LONG && LA132_1 <= LPAREN)||(LA132_1 >= NEW && LA132_1 <= NULL)||LA132_1==PLUS||LA132_1==PLUSPLUS||LA132_1==SHORT||(LA132_1 >= STRINGLITERAL && LA132_1 <= SUB)||(LA132_1 >= SUBSUB && LA132_1 <= SUPER)||LA132_1==THIS||LA132_1==TILDE||LA132_1==TRUE||LA132_1==VOID) ) {
                    alt132=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 132, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA132_0==GT) ) {
                int LA132_2 = input.LA(2);

                if ( (LA132_2==EQ) ) {
                    alt132=2;
                }
                else if ( (LA132_2==BANG||(LA132_2 >= BINLITERAL && LA132_2 <= BOOLEAN)||LA132_2==BYTE||(LA132_2 >= CHAR && LA132_2 <= CHARLITERAL)||(LA132_2 >= DOUBLE && LA132_2 <= DOUBLELITERAL)||LA132_2==FALSE||(LA132_2 >= FLOAT && LA132_2 <= FLOATLITERAL)||LA132_2==IDENTIFIER||LA132_2==INT||LA132_2==INTLITERAL||(LA132_2 >= LONG && LA132_2 <= LPAREN)||(LA132_2 >= NEW && LA132_2 <= NULL)||LA132_2==PLUS||LA132_2==PLUSPLUS||LA132_2==SHORT||(LA132_2 >= STRINGLITERAL && LA132_2 <= SUB)||(LA132_2 >= SUBSUB && LA132_2 <= SUPER)||LA132_2==THIS||LA132_2==TILDE||LA132_2==TRUE||LA132_2==VOID) ) {
                    alt132=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 132, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 132, 0, input);

                throw nvae;

            }
            switch (alt132) {
                case 1 :
                    // Java.g:1089:10: ( '<' ) ( '=' )
                    {
                    // Java.g:1089:10: ( '<' )
                    // Java.g:1089:11: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_relationalOp7997); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1089:48: ( '=' )
                    // Java.g:1089:49: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_relationalOp8002); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1090:10: ( '>' ) ( '=' )
                    {
                    // Java.g:1090:10: ( '>' )
                    // Java.g:1090:11: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_relationalOp8016); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1090:48: ( '=' )
                    // Java.g:1090:49: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_relationalOp8021); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'='",input.LT(-1));}

                    }


                    }
                    break;
                case 3 :
                    // Java.g:1091:9: ( '<' )
                    {
                    // Java.g:1091:9: ( '<' )
                    // Java.g:1091:10: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_relationalOp8034); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    }
                    break;
                case 4 :
                    // Java.g:1092:9: ( '>' )
                    {
                    // Java.g:1092:9: ( '>' )
                    // Java.g:1092:10: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_relationalOp8047); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 86, relationalOp_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "relationalOp"


    public static class shiftExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "shiftExpression"
    // Java.g:1095:1: shiftExpression :t220= additiveExpression (t221= shiftOp t222= additiveExpression )* ;
    public final shiftExpression_return shiftExpression() throws RecognitionException {
        shiftExpression_return retval = new shiftExpression_return();
        retval.start = input.LT(1);

        int shiftExpression_StartIndex = input.index();

        additiveExpression_return t220 =null;

        shiftOp_return t221 =null;

        additiveExpression_return t222 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 87) ) { return retval; }

            // Java.g:1096:5: (t220= additiveExpression (t221= shiftOp t222= additiveExpression )* )
            // Java.g:1096:9: t220= additiveExpression (t221= shiftOp t222= additiveExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("additiveExpression"));}

            pushFollow(FOLLOW_additiveExpression_in_shiftExpression8073);
            t220=additiveExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t220!=null?((Token)t220.start):null), (t220!=null?((Token)t220.stop):null));}

            // Java.g:1097:9: (t221= shiftOp t222= additiveExpression )*
            loop133:
            do {
                int alt133=2;
                int LA133_0 = input.LA(1);

                if ( (LA133_0==LT) ) {
                    int LA133_1 = input.LA(2);

                    if ( (LA133_1==LT) ) {
                        int LA133_4 = input.LA(3);

                        if ( (LA133_4==BANG||(LA133_4 >= BINLITERAL && LA133_4 <= BOOLEAN)||LA133_4==BYTE||(LA133_4 >= CHAR && LA133_4 <= CHARLITERAL)||(LA133_4 >= DOUBLE && LA133_4 <= DOUBLELITERAL)||LA133_4==FALSE||(LA133_4 >= FLOAT && LA133_4 <= FLOATLITERAL)||LA133_4==IDENTIFIER||LA133_4==INT||LA133_4==INTLITERAL||(LA133_4 >= LONG && LA133_4 <= LPAREN)||(LA133_4 >= NEW && LA133_4 <= NULL)||LA133_4==PLUS||LA133_4==PLUSPLUS||LA133_4==SHORT||(LA133_4 >= STRINGLITERAL && LA133_4 <= SUB)||(LA133_4 >= SUBSUB && LA133_4 <= SUPER)||LA133_4==THIS||LA133_4==TILDE||LA133_4==TRUE||LA133_4==VOID) ) {
                            alt133=1;
                        }


                    }


                }
                else if ( (LA133_0==GT) ) {
                    int LA133_2 = input.LA(2);

                    if ( (LA133_2==GT) ) {
                        int LA133_5 = input.LA(3);

                        if ( (LA133_5==GT) ) {
                            int LA133_7 = input.LA(4);

                            if ( (LA133_7==BANG||(LA133_7 >= BINLITERAL && LA133_7 <= BOOLEAN)||LA133_7==BYTE||(LA133_7 >= CHAR && LA133_7 <= CHARLITERAL)||(LA133_7 >= DOUBLE && LA133_7 <= DOUBLELITERAL)||LA133_7==FALSE||(LA133_7 >= FLOAT && LA133_7 <= FLOATLITERAL)||LA133_7==IDENTIFIER||LA133_7==INT||LA133_7==INTLITERAL||(LA133_7 >= LONG && LA133_7 <= LPAREN)||(LA133_7 >= NEW && LA133_7 <= NULL)||LA133_7==PLUS||LA133_7==PLUSPLUS||LA133_7==SHORT||(LA133_7 >= STRINGLITERAL && LA133_7 <= SUB)||(LA133_7 >= SUBSUB && LA133_7 <= SUPER)||LA133_7==THIS||LA133_7==TILDE||LA133_7==TRUE||LA133_7==VOID) ) {
                                alt133=1;
                            }


                        }
                        else if ( (LA133_5==BANG||(LA133_5 >= BINLITERAL && LA133_5 <= BOOLEAN)||LA133_5==BYTE||(LA133_5 >= CHAR && LA133_5 <= CHARLITERAL)||(LA133_5 >= DOUBLE && LA133_5 <= DOUBLELITERAL)||LA133_5==FALSE||(LA133_5 >= FLOAT && LA133_5 <= FLOATLITERAL)||LA133_5==IDENTIFIER||LA133_5==INT||LA133_5==INTLITERAL||(LA133_5 >= LONG && LA133_5 <= LPAREN)||(LA133_5 >= NEW && LA133_5 <= NULL)||LA133_5==PLUS||LA133_5==PLUSPLUS||LA133_5==SHORT||(LA133_5 >= STRINGLITERAL && LA133_5 <= SUB)||(LA133_5 >= SUBSUB && LA133_5 <= SUPER)||LA133_5==THIS||LA133_5==TILDE||LA133_5==TRUE||LA133_5==VOID) ) {
                            alt133=1;
                        }


                    }


                }


                switch (alt133) {
            	case 1 :
            	    // Java.g:1097:10: t221= shiftOp t222= additiveExpression
            	    {
            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("shiftOp"));}

            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression8090);
            	    t221=shiftOp();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t221!=null?((Token)t221.start):null), (t221!=null?((Token)t221.stop):null));}

            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("additiveExpression"));}

            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression8098);
            	    t222=additiveExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t222!=null?((Token)t222.start):null), (t222!=null?((Token)t222.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop133;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 87, shiftExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "shiftExpression"


    public static class shiftOp_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "shiftOp"
    // Java.g:1102:1: shiftOp : ( ( '<' ) ( '<' ) | ( '>' ) ( '>' ) ( '>' ) | ( '>' ) ( '>' ) );
    public final shiftOp_return shiftOp() throws RecognitionException {
        shiftOp_return retval = new shiftOp_return();
        retval.start = input.LT(1);

        int shiftOp_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 88) ) { return retval; }

            // Java.g:1103:5: ( ( '<' ) ( '<' ) | ( '>' ) ( '>' ) ( '>' ) | ( '>' ) ( '>' ) )
            int alt134=3;
            int LA134_0 = input.LA(1);

            if ( (LA134_0==LT) ) {
                alt134=1;
            }
            else if ( (LA134_0==GT) ) {
                int LA134_2 = input.LA(2);

                if ( (LA134_2==GT) ) {
                    int LA134_3 = input.LA(3);

                    if ( (LA134_3==GT) ) {
                        alt134=2;
                    }
                    else if ( (LA134_3==BANG||(LA134_3 >= BINLITERAL && LA134_3 <= BOOLEAN)||LA134_3==BYTE||(LA134_3 >= CHAR && LA134_3 <= CHARLITERAL)||(LA134_3 >= DOUBLE && LA134_3 <= DOUBLELITERAL)||LA134_3==FALSE||(LA134_3 >= FLOAT && LA134_3 <= FLOATLITERAL)||LA134_3==IDENTIFIER||LA134_3==INT||LA134_3==INTLITERAL||(LA134_3 >= LONG && LA134_3 <= LPAREN)||(LA134_3 >= NEW && LA134_3 <= NULL)||LA134_3==PLUS||LA134_3==PLUSPLUS||LA134_3==SHORT||(LA134_3 >= STRINGLITERAL && LA134_3 <= SUB)||(LA134_3 >= SUBSUB && LA134_3 <= SUPER)||LA134_3==THIS||LA134_3==TILDE||LA134_3==TRUE||LA134_3==VOID) ) {
                        alt134=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 134, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 134, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 134, 0, input);

                throw nvae;

            }
            switch (alt134) {
                case 1 :
                    // Java.g:1103:10: ( '<' ) ( '<' )
                    {
                    // Java.g:1103:10: ( '<' )
                    // Java.g:1103:11: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_shiftOp8134); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1103:48: ( '<' )
                    // Java.g:1103:49: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_shiftOp8139); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1104:10: ( '>' ) ( '>' ) ( '>' )
                    {
                    // Java.g:1104:10: ( '>' )
                    // Java.g:1104:11: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp8153); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1104:48: ( '>' )
                    // Java.g:1104:49: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp8158); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1104:86: ( '>' )
                    // Java.g:1104:87: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp8163); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    }
                    break;
                case 3 :
                    // Java.g:1105:10: ( '>' ) ( '>' )
                    {
                    // Java.g:1105:10: ( '>' )
                    // Java.g:1105:11: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp8177); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    // Java.g:1105:48: ( '>' )
                    // Java.g:1105:49: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp8182); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 88, shiftOp_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "shiftOp"


    public static class additiveExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "additiveExpression"
    // Java.g:1109:1: additiveExpression :t223= multiplicativeExpression ( ( ( '+' ) | ( '-' ) ) t224= multiplicativeExpression )* ;
    public final additiveExpression_return additiveExpression() throws RecognitionException {
        additiveExpression_return retval = new additiveExpression_return();
        retval.start = input.LT(1);

        int additiveExpression_StartIndex = input.index();

        multiplicativeExpression_return t223 =null;

        multiplicativeExpression_return t224 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 89) ) { return retval; }

            // Java.g:1110:5: (t223= multiplicativeExpression ( ( ( '+' ) | ( '-' ) ) t224= multiplicativeExpression )* )
            // Java.g:1110:9: t223= multiplicativeExpression ( ( ( '+' ) | ( '-' ) ) t224= multiplicativeExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("multiplicativeExpression"));}

            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression8209);
            t223=multiplicativeExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t223!=null?((Token)t223.start):null), (t223!=null?((Token)t223.stop):null));}

            // Java.g:1111:9: ( ( ( '+' ) | ( '-' ) ) t224= multiplicativeExpression )*
            loop136:
            do {
                int alt136=2;
                int LA136_0 = input.LA(1);

                if ( (LA136_0==PLUS||LA136_0==SUB) ) {
                    alt136=1;
                }


                switch (alt136) {
            	case 1 :
            	    // Java.g:1112:13: ( ( '+' ) | ( '-' ) ) t224= multiplicativeExpression
            	    {
            	    // Java.g:1112:13: ( ( '+' ) | ( '-' ) )
            	    int alt135=2;
            	    int LA135_0 = input.LA(1);

            	    if ( (LA135_0==PLUS) ) {
            	        alt135=1;
            	    }
            	    else if ( (LA135_0==SUB) ) {
            	        alt135=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 135, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt135) {
            	        case 1 :
            	            // Java.g:1112:17: ( '+' )
            	            {
            	            // Java.g:1112:17: ( '+' )
            	            // Java.g:1112:18: '+'
            	            {
            	            match(input,PLUS,FOLLOW_PLUS_in_additiveExpression8243); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'+'",input.LT(-1));}

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // Java.g:1113:17: ( '-' )
            	            {
            	            // Java.g:1113:17: ( '-' )
            	            // Java.g:1113:18: '-'
            	            {
            	            match(input,SUB,FOLLOW_SUB_in_additiveExpression8264); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'-'",input.LT(-1));}

            	            }


            	            }
            	            break;

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("multiplicativeExpression"));}

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression8298);
            	    t224=multiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t224!=null?((Token)t224.start):null), (t224!=null?((Token)t224.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop136;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 89, additiveExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "additiveExpression"


    public static class multiplicativeExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "multiplicativeExpression"
    // Java.g:1119:1: multiplicativeExpression :t225= unaryExpression ( ( ( '*' ) | ( '/' ) | ( '%' ) ) t226= unaryExpression )* ;
    public final multiplicativeExpression_return multiplicativeExpression() throws RecognitionException {
        multiplicativeExpression_return retval = new multiplicativeExpression_return();
        retval.start = input.LT(1);

        int multiplicativeExpression_StartIndex = input.index();

        unaryExpression_return t225 =null;

        unaryExpression_return t226 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 90) ) { return retval; }

            // Java.g:1120:5: (t225= unaryExpression ( ( ( '*' ) | ( '/' ) | ( '%' ) ) t226= unaryExpression )* )
            // Java.g:1121:9: t225= unaryExpression ( ( ( '*' ) | ( '/' ) | ( '%' ) ) t226= unaryExpression )*
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression8342);
            t225=unaryExpression();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t225!=null?((Token)t225.start):null), (t225!=null?((Token)t225.stop):null));}

            // Java.g:1122:9: ( ( ( '*' ) | ( '/' ) | ( '%' ) ) t226= unaryExpression )*
            loop138:
            do {
                int alt138=2;
                int LA138_0 = input.LA(1);

                if ( (LA138_0==PERCENT||LA138_0==SLASH||LA138_0==STAR) ) {
                    alt138=1;
                }


                switch (alt138) {
            	case 1 :
            	    // Java.g:1123:13: ( ( '*' ) | ( '/' ) | ( '%' ) ) t226= unaryExpression
            	    {
            	    // Java.g:1123:13: ( ( '*' ) | ( '/' ) | ( '%' ) )
            	    int alt137=3;
            	    switch ( input.LA(1) ) {
            	    case STAR:
            	        {
            	        alt137=1;
            	        }
            	        break;
            	    case SLASH:
            	        {
            	        alt137=2;
            	        }
            	        break;
            	    case PERCENT:
            	        {
            	        alt137=3;
            	        }
            	        break;
            	    default:
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 137, 0, input);

            	        throw nvae;

            	    }

            	    switch (alt137) {
            	        case 1 :
            	            // Java.g:1123:17: ( '*' )
            	            {
            	            // Java.g:1123:17: ( '*' )
            	            // Java.g:1123:18: '*'
            	            {
            	            match(input,STAR,FOLLOW_STAR_in_multiplicativeExpression8376); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'*'",input.LT(-1));}

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // Java.g:1124:17: ( '/' )
            	            {
            	            // Java.g:1124:17: ( '/' )
            	            // Java.g:1124:18: '/'
            	            {
            	            match(input,SLASH,FOLLOW_SLASH_in_multiplicativeExpression8397); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'/'",input.LT(-1));}

            	            }


            	            }
            	            break;
            	        case 3 :
            	            // Java.g:1125:17: ( '%' )
            	            {
            	            // Java.g:1125:17: ( '%' )
            	            // Java.g:1125:18: '%'
            	            {
            	            match(input,PERCENT,FOLLOW_PERCENT_in_multiplicativeExpression8418); if (state.failed) return retval;

            	            if ( state.backtracking==0 ) {T.addLeaf("'%'",input.LT(-1));}

            	            }


            	            }
            	            break;

            	    }


            	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression8452);
            	    t226=unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t226!=null?((Token)t226.start):null), (t226!=null?((Token)t226.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop138;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 90, multiplicativeExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "multiplicativeExpression"


    public static class unaryExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "unaryExpression"
    // Java.g:1135:1: unaryExpression : ( ( '+' ) t227= unaryExpression | ( '-' ) t228= unaryExpression | '++' t229= unaryExpression | '--' t230= unaryExpression |t231= unaryExpressionNotPlusMinus );
    public final unaryExpression_return unaryExpression() throws RecognitionException {
        unaryExpression_return retval = new unaryExpression_return();
        retval.start = input.LT(1);

        int unaryExpression_StartIndex = input.index();

        unaryExpression_return t227 =null;

        unaryExpression_return t228 =null;

        unaryExpression_return t229 =null;

        unaryExpression_return t230 =null;

        unaryExpressionNotPlusMinus_return t231 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 91) ) { return retval; }

            // Java.g:1136:5: ( ( '+' ) t227= unaryExpression | ( '-' ) t228= unaryExpression | '++' t229= unaryExpression | '--' t230= unaryExpression |t231= unaryExpressionNotPlusMinus )
            int alt139=5;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                alt139=1;
                }
                break;
            case SUB:
                {
                alt139=2;
                }
                break;
            case PLUSPLUS:
                {
                alt139=3;
                }
                break;
            case SUBSUB:
                {
                alt139=4;
                }
                break;
            case BANG:
            case BINLITERAL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt139=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 139, 0, input);

                throw nvae;

            }

            switch (alt139) {
                case 1 :
                    // Java.g:1136:9: ( '+' ) t227= unaryExpression
                    {
                    // Java.g:1136:9: ( '+' )
                    // Java.g:1136:10: '+'
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_unaryExpression8488); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'+'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression8497);
                    t227=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t227!=null?((Token)t227.start):null), (t227!=null?((Token)t227.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1137:9: ( '-' ) t228= unaryExpression
                    {
                    // Java.g:1137:9: ( '-' )
                    // Java.g:1137:10: '-'
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryExpression8510); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'-'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression8518);
                    t228=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t228!=null?((Token)t228.start):null), (t228!=null?((Token)t228.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:1138:9: '++' t229= unaryExpression
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpression8530); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'++'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression8537);
                    t229=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t229!=null?((Token)t229.start):null), (t229!=null?((Token)t229.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:1139:9: '--' t230= unaryExpression
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpression8549); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'--'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression8556);
                    t230=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t230!=null?((Token)t230.start):null), (t230!=null?((Token)t230.stop):null));}

                    }
                    break;
                case 5 :
                    // Java.g:1140:9: t231= unaryExpressionNotPlusMinus
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpressionNotPlusMinus"));}

                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression8572);
                    t231=unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t231!=null?((Token)t231.start):null), (t231!=null?((Token)t231.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 91, unaryExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "unaryExpression"


    public static class unaryExpressionNotPlusMinus_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "unaryExpressionNotPlusMinus"
    // Java.g:1143:1: unaryExpressionNotPlusMinus : ( ( '~' ) t232= unaryExpression | ( '!' ) t233= unaryExpression |t234= castExpression |t235= primary (t236= selector )* ( '++' | '--' )? );
    public final unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus() throws RecognitionException {
        unaryExpressionNotPlusMinus_return retval = new unaryExpressionNotPlusMinus_return();
        retval.start = input.LT(1);

        int unaryExpressionNotPlusMinus_StartIndex = input.index();

        unaryExpression_return t232 =null;

        unaryExpression_return t233 =null;

        castExpression_return t234 =null;

        primary_return t235 =null;

        selector_return t236 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 92) ) { return retval; }

            // Java.g:1144:5: ( ( '~' ) t232= unaryExpression | ( '!' ) t233= unaryExpression |t234= castExpression |t235= primary (t236= selector )* ( '++' | '--' )? )
            int alt142=4;
            switch ( input.LA(1) ) {
            case TILDE:
                {
                alt142=1;
                }
                break;
            case BANG:
                {
                alt142=2;
                }
                break;
            case LPAREN:
                {
                int LA142_3 = input.LA(2);

                if ( (synpred209_Java()) ) {
                    alt142=3;
                }
                else if ( (true) ) {
                    alt142=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 142, 3, input);

                    throw nvae;

                }
                }
                break;
            case BINLITERAL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case SUPER:
            case THIS:
            case TRUE:
            case VOID:
                {
                alt142=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 142, 0, input);

                throw nvae;

            }

            switch (alt142) {
                case 1 :
                    // Java.g:1144:9: ( '~' ) t232= unaryExpression
                    {
                    // Java.g:1144:9: ( '~' )
                    // Java.g:1144:10: '~'
                    {
                    match(input,TILDE,FOLLOW_TILDE_in_unaryExpressionNotPlusMinus8595); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'~'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus8603);
                    t232=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t232!=null?((Token)t232.start):null), (t232!=null?((Token)t232.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1145:9: ( '!' ) t233= unaryExpression
                    {
                    // Java.g:1145:9: ( '!' )
                    // Java.g:1145:10: '!'
                    {
                    match(input,BANG,FOLLOW_BANG_in_unaryExpressionNotPlusMinus8616); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'!'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus8624);
                    t233=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t233!=null?((Token)t233.start):null), (t233!=null?((Token)t233.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:1146:9: t234= castExpression
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("castExpression"));}

                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus8640);
                    t234=castExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t234!=null?((Token)t234.start):null), (t234!=null?((Token)t234.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:1147:9: t235= primary (t236= selector )* ( '++' | '--' )?
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primary"));}

                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus8656);
                    t235=primary();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t235!=null?((Token)t235.start):null), (t235!=null?((Token)t235.stop):null));}

                    // Java.g:1148:9: (t236= selector )*
                    loop140:
                    do {
                        int alt140=2;
                        int LA140_0 = input.LA(1);

                        if ( (LA140_0==DOT||LA140_0==LBRACKET) ) {
                            alt140=1;
                        }


                        switch (alt140) {
                    	case 1 :
                    	    // Java.g:1148:10: t236= selector
                    	    {
                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("selector"));}

                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus8673);
                    	    t236=selector();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t236!=null?((Token)t236.start):null), (t236!=null?((Token)t236.stop):null));}

                    	    }
                    	    break;

                    	default :
                    	    break loop140;
                        }
                    } while (true);


                    // Java.g:1150:9: ( '++' | '--' )?
                    int alt141=3;
                    int LA141_0 = input.LA(1);

                    if ( (LA141_0==PLUSPLUS) ) {
                        alt141=1;
                    }
                    else if ( (LA141_0==SUBSUB) ) {
                        alt141=2;
                    }
                    switch (alt141) {
                        case 1 :
                            // Java.g:1150:13: '++'
                            {
                            match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpressionNotPlusMinus8700); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'++'",input.LT(-1));}

                            }
                            break;
                        case 2 :
                            // Java.g:1151:13: '--'
                            {
                            match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpressionNotPlusMinus8715); if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.addLeaf("'--'",input.LT(-1));}

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 92, unaryExpressionNotPlusMinus_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"


    public static class castExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "castExpression"
    // Java.g:1155:1: castExpression : ( ( '(' ) t237= primitiveType ( ')' ) t238= unaryExpression | ( '(' ) t239= type ( ')' ) t240= unaryExpressionNotPlusMinus );
    public final castExpression_return castExpression() throws RecognitionException {
        castExpression_return retval = new castExpression_return();
        retval.start = input.LT(1);

        int castExpression_StartIndex = input.index();

        primitiveType_return t237 =null;

        unaryExpression_return t238 =null;

        type_return t239 =null;

        unaryExpressionNotPlusMinus_return t240 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 93) ) { return retval; }

            // Java.g:1156:5: ( ( '(' ) t237= primitiveType ( ')' ) t238= unaryExpression | ( '(' ) t239= type ( ')' ) t240= unaryExpressionNotPlusMinus )
            int alt143=2;
            int LA143_0 = input.LA(1);

            if ( (LA143_0==LPAREN) ) {
                int LA143_1 = input.LA(2);

                if ( (synpred213_Java()) ) {
                    alt143=1;
                }
                else if ( (true) ) {
                    alt143=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 143, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 143, 0, input);

                throw nvae;

            }
            switch (alt143) {
                case 1 :
                    // Java.g:1156:9: ( '(' ) t237= primitiveType ( ')' ) t238= unaryExpression
                    {
                    // Java.g:1156:9: ( '(' )
                    // Java.g:1156:10: '('
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_castExpression8748); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primitiveType"));}

                    pushFollow(FOLLOW_primitiveType_in_castExpression8756);
                    t237=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t237!=null?((Token)t237.start):null), (t237!=null?((Token)t237.stop):null));}

                    // Java.g:1156:180: ( ')' )
                    // Java.g:1156:181: ')'
                    {
                    match(input,RPAREN,FOLLOW_RPAREN_in_castExpression8761); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpression"));}

                    pushFollow(FOLLOW_unaryExpression_in_castExpression8769);
                    t238=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t238!=null?((Token)t238.start):null), (t238!=null?((Token)t238.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1157:9: ( '(' ) t239= type ( ')' ) t240= unaryExpressionNotPlusMinus
                    {
                    // Java.g:1157:9: ( '(' )
                    // Java.g:1157:10: '('
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_castExpression8782); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("type"));}

                    pushFollow(FOLLOW_type_in_castExpression8790);
                    t239=type();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t239!=null?((Token)t239.start):null), (t239!=null?((Token)t239.stop):null));}

                    // Java.g:1157:162: ( ')' )
                    // Java.g:1157:163: ')'
                    {
                    match(input,RPAREN,FOLLOW_RPAREN_in_castExpression8795); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("unaryExpressionNotPlusMinus"));}

                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression8803);
                    t240=unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t240!=null?((Token)t240.start):null), (t240!=null?((Token)t240.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 93, castExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "castExpression"


    public static class primary_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "primary"
    // Java.g:1163:1: primary : (t241= parExpression | 'this' ( ( '.' ) IDENTIFIER )* (t242= identifierSuffix )? | IDENTIFIER ( ( '.' ) IDENTIFIER )* (t243= identifierSuffix )? | 'super' t244= superSuffix |t245= literal |t246= creator |t247= primitiveType ( ( '[' ) ( ']' ) )* ( '.' ) 'class' | 'void' ( '.' ) 'class' );
    public final primary_return primary() throws RecognitionException {
        primary_return retval = new primary_return();
        retval.start = input.LT(1);

        int primary_StartIndex = input.index();

        parExpression_return t241 =null;

        identifierSuffix_return t242 =null;

        identifierSuffix_return t243 =null;

        superSuffix_return t244 =null;

        literal_return t245 =null;

        creator_return t246 =null;

        primitiveType_return t247 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 94) ) { return retval; }

            // Java.g:1164:5: (t241= parExpression | 'this' ( ( '.' ) IDENTIFIER )* (t242= identifierSuffix )? | IDENTIFIER ( ( '.' ) IDENTIFIER )* (t243= identifierSuffix )? | 'super' t244= superSuffix |t245= literal |t246= creator |t247= primitiveType ( ( '[' ) ( ']' ) )* ( '.' ) 'class' | 'void' ( '.' ) 'class' )
            int alt149=8;
            switch ( input.LA(1) ) {
            case LPAREN:
                {
                alt149=1;
                }
                break;
            case THIS:
                {
                alt149=2;
                }
                break;
            case IDENTIFIER:
                {
                alt149=3;
                }
                break;
            case SUPER:
                {
                alt149=4;
                }
                break;
            case BINLITERAL:
            case CHARLITERAL:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case INTLITERAL:
            case LONGLITERAL:
            case NULL:
            case STRINGLITERAL:
            case TRUE:
                {
                alt149=5;
                }
                break;
            case NEW:
                {
                alt149=6;
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                alt149=7;
                }
                break;
            case VOID:
                {
                alt149=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 149, 0, input);

                throw nvae;

            }

            switch (alt149) {
                case 1 :
                    // Java.g:1164:9: t241= parExpression
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("parExpression"));}

                    pushFollow(FOLLOW_parExpression_in_primary8831);
                    t241=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t241!=null?((Token)t241.start):null), (t241!=null?((Token)t241.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1165:9: 'this' ( ( '.' ) IDENTIFIER )* (t242= identifierSuffix )?
                    {
                    match(input,THIS,FOLLOW_THIS_in_primary8855); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'this'",input.LT(-1));}

                    // Java.g:1166:9: ( ( '.' ) IDENTIFIER )*
                    loop144:
                    do {
                        int alt144=2;
                        int LA144_0 = input.LA(1);

                        if ( (LA144_0==DOT) ) {
                            int LA144_2 = input.LA(2);

                            if ( (LA144_2==IDENTIFIER) ) {
                                int LA144_3 = input.LA(3);

                                if ( (synpred215_Java()) ) {
                                    alt144=1;
                                }


                            }


                        }


                        switch (alt144) {
                    	case 1 :
                    	    // Java.g:1166:10: ( '.' ) IDENTIFIER
                    	    {
                    	    // Java.g:1166:10: ( '.' )
                    	    // Java.g:1166:11: '.'
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_primary8868); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    	    }


                    	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary8872); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    	    }
                    	    break;

                    	default :
                    	    break loop144;
                        }
                    } while (true);


                    // Java.g:1168:9: (t242= identifierSuffix )?
                    int alt145=2;
                    switch ( input.LA(1) ) {
                        case LBRACKET:
                            {
                            int LA145_1 = input.LA(2);

                            if ( (synpred216_Java()) ) {
                                alt145=1;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            alt145=1;
                            }
                            break;
                        case DOT:
                            {
                            int LA145_3 = input.LA(2);

                            if ( (synpred216_Java()) ) {
                                alt145=1;
                            }
                            }
                            break;
                    }

                    switch (alt145) {
                        case 1 :
                            // Java.g:1168:10: t242= identifierSuffix
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("identifierSuffix"));}

                            pushFollow(FOLLOW_identifierSuffix_in_primary8900);
                            t242=identifierSuffix();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t242!=null?((Token)t242.start):null), (t242!=null?((Token)t242.stop):null));}

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // Java.g:1170:9: IDENTIFIER ( ( '.' ) IDENTIFIER )* (t243= identifierSuffix )?
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary8923); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:1171:9: ( ( '.' ) IDENTIFIER )*
                    loop146:
                    do {
                        int alt146=2;
                        int LA146_0 = input.LA(1);

                        if ( (LA146_0==DOT) ) {
                            int LA146_2 = input.LA(2);

                            if ( (LA146_2==IDENTIFIER) ) {
                                int LA146_3 = input.LA(3);

                                if ( (synpred218_Java()) ) {
                                    alt146=1;
                                }


                            }


                        }


                        switch (alt146) {
                    	case 1 :
                    	    // Java.g:1171:10: ( '.' ) IDENTIFIER
                    	    {
                    	    // Java.g:1171:10: ( '.' )
                    	    // Java.g:1171:11: '.'
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_primary8937); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    	    }


                    	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary8941); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    	    }
                    	    break;

                    	default :
                    	    break loop146;
                        }
                    } while (true);


                    // Java.g:1173:9: (t243= identifierSuffix )?
                    int alt147=2;
                    switch ( input.LA(1) ) {
                        case LBRACKET:
                            {
                            int LA147_1 = input.LA(2);

                            if ( (synpred219_Java()) ) {
                                alt147=1;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            alt147=1;
                            }
                            break;
                        case DOT:
                            {
                            int LA147_3 = input.LA(2);

                            if ( (synpred219_Java()) ) {
                                alt147=1;
                            }
                            }
                            break;
                    }

                    switch (alt147) {
                        case 1 :
                            // Java.g:1173:10: t243= identifierSuffix
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("identifierSuffix"));}

                            pushFollow(FOLLOW_identifierSuffix_in_primary8969);
                            t243=identifierSuffix();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t243!=null?((Token)t243.start):null), (t243!=null?((Token)t243.stop):null));}

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // Java.g:1175:9: 'super' t244= superSuffix
                    {
                    match(input,SUPER,FOLLOW_SUPER_in_primary8992); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("superSuffix"));}

                    pushFollow(FOLLOW_superSuffix_in_primary9007);
                    t244=superSuffix();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t244!=null?((Token)t244.start):null), (t244!=null?((Token)t244.stop):null));}

                    }
                    break;
                case 5 :
                    // Java.g:1177:9: t245= literal
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("literal"));}

                    pushFollow(FOLLOW_literal_in_primary9023);
                    t245=literal();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t245!=null?((Token)t245.start):null), (t245!=null?((Token)t245.stop):null));}

                    }
                    break;
                case 6 :
                    // Java.g:1178:9: t246= creator
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("creator"));}

                    pushFollow(FOLLOW_creator_in_primary9039);
                    t246=creator();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t246!=null?((Token)t246.start):null), (t246!=null?((Token)t246.stop):null));}

                    }
                    break;
                case 7 :
                    // Java.g:1179:9: t247= primitiveType ( ( '[' ) ( ']' ) )* ( '.' ) 'class'
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primitiveType"));}

                    pushFollow(FOLLOW_primitiveType_in_primary9055);
                    t247=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t247!=null?((Token)t247.start):null), (t247!=null?((Token)t247.stop):null));}

                    // Java.g:1180:9: ( ( '[' ) ( ']' ) )*
                    loop148:
                    do {
                        int alt148=2;
                        int LA148_0 = input.LA(1);

                        if ( (LA148_0==LBRACKET) ) {
                            alt148=1;
                        }


                        switch (alt148) {
                    	case 1 :
                    	    // Java.g:1180:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:1180:10: ( '[' )
                    	    // Java.g:1180:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_primary9069); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:1180:48: ( ']' )
                    	    // Java.g:1180:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_primary9074); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop148;
                        }
                    } while (true);


                    // Java.g:1182:9: ( '.' )
                    // Java.g:1182:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_primary9098); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,CLASS,FOLLOW_CLASS_in_primary9102); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'class'",input.LT(-1));}

                    }
                    break;
                case 8 :
                    // Java.g:1183:9: 'void' ( '.' ) 'class'
                    {
                    match(input,VOID,FOLLOW_VOID_in_primary9113); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'void'",input.LT(-1));}

                    // Java.g:1183:51: ( '.' )
                    // Java.g:1183:52: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_primary9117); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,CLASS,FOLLOW_CLASS_in_primary9121); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'class'",input.LT(-1));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 94, primary_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "primary"


    public static class superSuffix_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "superSuffix"
    // Java.g:1187:1: superSuffix : (t248= arguments | ( '.' ) (t249= typeArguments )? IDENTIFIER (t250= arguments )? );
    public final superSuffix_return superSuffix() throws RecognitionException {
        superSuffix_return retval = new superSuffix_return();
        retval.start = input.LT(1);

        int superSuffix_StartIndex = input.index();

        arguments_return t248 =null;

        typeArguments_return t249 =null;

        arguments_return t250 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 95) ) { return retval; }

            // Java.g:1188:5: (t248= arguments | ( '.' ) (t249= typeArguments )? IDENTIFIER (t250= arguments )? )
            int alt152=2;
            int LA152_0 = input.LA(1);

            if ( (LA152_0==LPAREN) ) {
                alt152=1;
            }
            else if ( (LA152_0==DOT) ) {
                alt152=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 152, 0, input);

                throw nvae;

            }
            switch (alt152) {
                case 1 :
                    // Java.g:1188:9: t248= arguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_superSuffix9152);
                    t248=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t248!=null?((Token)t248.start):null), (t248!=null?((Token)t248.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1189:9: ( '.' ) (t249= typeArguments )? IDENTIFIER (t250= arguments )?
                    {
                    // Java.g:1189:9: ( '.' )
                    // Java.g:1189:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_superSuffix9165); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    // Java.g:1189:47: (t249= typeArguments )?
                    int alt150=2;
                    int LA150_0 = input.LA(1);

                    if ( (LA150_0==LT) ) {
                        alt150=1;
                    }
                    switch (alt150) {
                        case 1 :
                            // Java.g:1189:48: t249= typeArguments
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArguments"));}

                            pushFollow(FOLLOW_typeArguments_in_superSuffix9174);
                            t249=typeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t249!=null?((Token)t249.start):null), (t249!=null?((Token)t249.stop):null));}

                            }
                            break;

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_superSuffix9197); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:1192:9: (t250= arguments )?
                    int alt151=2;
                    int LA151_0 = input.LA(1);

                    if ( (LA151_0==LPAREN) ) {
                        alt151=1;
                    }
                    switch (alt151) {
                        case 1 :
                            // Java.g:1192:10: t250= arguments
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                            pushFollow(FOLLOW_arguments_in_superSuffix9214);
                            t250=arguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t250!=null?((Token)t250.start):null), (t250!=null?((Token)t250.stop):null));}

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 95, superSuffix_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "superSuffix"


    public static class identifierSuffix_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "identifierSuffix"
    // Java.g:1197:1: identifierSuffix : ( ( ( '[' ) ( ']' ) )+ ( '.' ) 'class' | ( ( '[' ) t251= expression ( ']' ) )+ |t252= arguments | ( '.' ) 'class' | ( '.' ) t253= nonWildcardTypeArguments IDENTIFIER t254= arguments | ( '.' ) 'this' | ( '.' ) 'super' t255= arguments |t256= innerCreator );
    public final identifierSuffix_return identifierSuffix() throws RecognitionException {
        identifierSuffix_return retval = new identifierSuffix_return();
        retval.start = input.LT(1);

        int identifierSuffix_StartIndex = input.index();

        expression_return t251 =null;

        arguments_return t252 =null;

        nonWildcardTypeArguments_return t253 =null;

        arguments_return t254 =null;

        arguments_return t255 =null;

        innerCreator_return t256 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 96) ) { return retval; }

            // Java.g:1198:5: ( ( ( '[' ) ( ']' ) )+ ( '.' ) 'class' | ( ( '[' ) t251= expression ( ']' ) )+ |t252= arguments | ( '.' ) 'class' | ( '.' ) t253= nonWildcardTypeArguments IDENTIFIER t254= arguments | ( '.' ) 'this' | ( '.' ) 'super' t255= arguments |t256= innerCreator )
            int alt155=8;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                int LA155_1 = input.LA(2);

                if ( (LA155_1==RBRACKET) ) {
                    alt155=1;
                }
                else if ( (LA155_1==BANG||(LA155_1 >= BINLITERAL && LA155_1 <= BOOLEAN)||LA155_1==BYTE||(LA155_1 >= CHAR && LA155_1 <= CHARLITERAL)||(LA155_1 >= DOUBLE && LA155_1 <= DOUBLELITERAL)||LA155_1==FALSE||(LA155_1 >= FLOAT && LA155_1 <= FLOATLITERAL)||LA155_1==IDENTIFIER||LA155_1==INT||LA155_1==INTLITERAL||(LA155_1 >= LONG && LA155_1 <= LPAREN)||(LA155_1 >= NEW && LA155_1 <= NULL)||LA155_1==PLUS||LA155_1==PLUSPLUS||LA155_1==SHORT||(LA155_1 >= STRINGLITERAL && LA155_1 <= SUB)||(LA155_1 >= SUBSUB && LA155_1 <= SUPER)||LA155_1==THIS||LA155_1==TILDE||LA155_1==TRUE||LA155_1==VOID) ) {
                    alt155=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 155, 1, input);

                    throw nvae;

                }
                }
                break;
            case LPAREN:
                {
                alt155=3;
                }
                break;
            case DOT:
                {
                switch ( input.LA(2) ) {
                case CLASS:
                    {
                    alt155=4;
                    }
                    break;
                case LT:
                    {
                    alt155=5;
                    }
                    break;
                case THIS:
                    {
                    alt155=6;
                    }
                    break;
                case SUPER:
                    {
                    alt155=7;
                    }
                    break;
                case NEW:
                    {
                    alt155=8;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 155, 3, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 155, 0, input);

                throw nvae;

            }

            switch (alt155) {
                case 1 :
                    // Java.g:1198:9: ( ( '[' ) ( ']' ) )+ ( '.' ) 'class'
                    {
                    // Java.g:1198:9: ( ( '[' ) ( ']' ) )+
                    int cnt153=0;
                    loop153:
                    do {
                        int alt153=2;
                        int LA153_0 = input.LA(1);

                        if ( (LA153_0==LBRACKET) ) {
                            alt153=1;
                        }


                        switch (alt153) {
                    	case 1 :
                    	    // Java.g:1198:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:1198:10: ( '[' )
                    	    // Java.g:1198:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix9250); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:1198:48: ( ']' )
                    	    // Java.g:1198:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix9255); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt153 >= 1 ) break loop153;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(153, input);
                                throw eee;
                        }
                        cnt153++;
                    } while (true);


                    // Java.g:1200:9: ( '.' )
                    // Java.g:1200:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix9279); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix9283); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'class'",input.LT(-1));}

                    }
                    break;
                case 2 :
                    // Java.g:1201:9: ( ( '[' ) t251= expression ( ']' ) )+
                    {
                    // Java.g:1201:9: ( ( '[' ) t251= expression ( ']' ) )+
                    int cnt154=0;
                    loop154:
                    do {
                        int alt154=2;
                        int LA154_0 = input.LA(1);

                        if ( (LA154_0==LBRACKET) ) {
                            int LA154_2 = input.LA(2);

                            if ( (synpred231_Java()) ) {
                                alt154=1;
                            }


                        }


                        switch (alt154) {
                    	case 1 :
                    	    // Java.g:1201:10: ( '[' ) t251= expression ( ']' )
                    	    {
                    	    // Java.g:1201:10: ( '[' )
                    	    // Java.g:1201:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix9296); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    	    pushFollow(FOLLOW_expression_in_identifierSuffix9304);
                    	    t251=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t251!=null?((Token)t251.start):null), (t251!=null?((Token)t251.stop):null));}

                    	    // Java.g:1201:175: ( ']' )
                    	    // Java.g:1201:176: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix9309); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt154 >= 1 ) break loop154;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(154, input);
                                throw eee;
                        }
                        cnt154++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // Java.g:1203:9: t252= arguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_identifierSuffix9336);
                    t252=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t252!=null?((Token)t252.start):null), (t252!=null?((Token)t252.stop):null));}

                    }
                    break;
                case 4 :
                    // Java.g:1204:9: ( '.' ) 'class'
                    {
                    // Java.g:1204:9: ( '.' )
                    // Java.g:1204:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix9349); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix9353); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'class'",input.LT(-1));}

                    }
                    break;
                case 5 :
                    // Java.g:1205:9: ( '.' ) t253= nonWildcardTypeArguments IDENTIFIER t254= arguments
                    {
                    // Java.g:1205:9: ( '.' )
                    // Java.g:1205:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix9365); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_identifierSuffix9373);
                    t253=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t253!=null?((Token)t253.start):null), (t253!=null?((Token)t253.stop):null));}

                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifierSuffix9377); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_identifierSuffix9385);
                    t254=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t254!=null?((Token)t254.start):null), (t254!=null?((Token)t254.stop):null));}

                    }
                    break;
                case 6 :
                    // Java.g:1206:9: ( '.' ) 'this'
                    {
                    // Java.g:1206:9: ( '.' )
                    // Java.g:1206:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix9398); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,THIS,FOLLOW_THIS_in_identifierSuffix9402); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'this'",input.LT(-1));}

                    }
                    break;
                case 7 :
                    // Java.g:1207:9: ( '.' ) 'super' t255= arguments
                    {
                    // Java.g:1207:9: ( '.' )
                    // Java.g:1207:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix9414); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,SUPER,FOLLOW_SUPER_in_identifierSuffix9418); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_identifierSuffix9425);
                    t255=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t255!=null?((Token)t255.start):null), (t255!=null?((Token)t255.stop):null));}

                    }
                    break;
                case 8 :
                    // Java.g:1208:9: t256= innerCreator
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("innerCreator"));}

                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix9441);
                    t256=innerCreator();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t256!=null?((Token)t256.start):null), (t256!=null?((Token)t256.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 96, identifierSuffix_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "identifierSuffix"


    public static class selector_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "selector"
    // Java.g:1212:1: selector : ( ( '.' ) IDENTIFIER (t257= arguments )? | ( '.' ) t258= nonWildcardTypeArguments IDENTIFIER t259= arguments | ( '.' ) 'this' | ( '.' ) 'super' t260= superSuffix |t261= innerCreator | ( '[' ) t262= expression ( ']' ) );
    public final selector_return selector() throws RecognitionException {
        selector_return retval = new selector_return();
        retval.start = input.LT(1);

        int selector_StartIndex = input.index();

        arguments_return t257 =null;

        nonWildcardTypeArguments_return t258 =null;

        arguments_return t259 =null;

        superSuffix_return t260 =null;

        innerCreator_return t261 =null;

        expression_return t262 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 97) ) { return retval; }

            // Java.g:1213:5: ( ( '.' ) IDENTIFIER (t257= arguments )? | ( '.' ) t258= nonWildcardTypeArguments IDENTIFIER t259= arguments | ( '.' ) 'this' | ( '.' ) 'super' t260= superSuffix |t261= innerCreator | ( '[' ) t262= expression ( ']' ) )
            int alt157=6;
            int LA157_0 = input.LA(1);

            if ( (LA157_0==DOT) ) {
                switch ( input.LA(2) ) {
                case IDENTIFIER:
                    {
                    alt157=1;
                    }
                    break;
                case LT:
                    {
                    alt157=2;
                    }
                    break;
                case THIS:
                    {
                    alt157=3;
                    }
                    break;
                case SUPER:
                    {
                    alt157=4;
                    }
                    break;
                case NEW:
                    {
                    alt157=5;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 157, 1, input);

                    throw nvae;

                }

            }
            else if ( (LA157_0==LBRACKET) ) {
                alt157=6;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 157, 0, input);

                throw nvae;

            }
            switch (alt157) {
                case 1 :
                    // Java.g:1213:9: ( '.' ) IDENTIFIER (t257= arguments )?
                    {
                    // Java.g:1213:9: ( '.' )
                    // Java.g:1213:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector9466); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_selector9470); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    // Java.g:1214:9: (t257= arguments )?
                    int alt156=2;
                    int LA156_0 = input.LA(1);

                    if ( (LA156_0==LPAREN) ) {
                        alt156=1;
                    }
                    switch (alt156) {
                        case 1 :
                            // Java.g:1214:10: t257= arguments
                            {
                            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                            pushFollow(FOLLOW_arguments_in_selector9487);
                            t257=arguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            if ( state.backtracking==0 ) {T.popTop().setTextRange((t257!=null?((Token)t257.start):null), (t257!=null?((Token)t257.stop):null));}

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1216:7: ( '.' ) t258= nonWildcardTypeArguments IDENTIFIER t259= arguments
                    {
                    // Java.g:1216:7: ( '.' )
                    // Java.g:1216:8: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector9509); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_selector9517);
                    t258=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t258!=null?((Token)t258.start):null), (t258!=null?((Token)t258.stop):null));}

                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_selector9521); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

                    pushFollow(FOLLOW_arguments_in_selector9529);
                    t259=arguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t259!=null?((Token)t259.start):null), (t259!=null?((Token)t259.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:1217:9: ( '.' ) 'this'
                    {
                    // Java.g:1217:9: ( '.' )
                    // Java.g:1217:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector9542); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,THIS,FOLLOW_THIS_in_selector9546); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'this'",input.LT(-1));}

                    }
                    break;
                case 4 :
                    // Java.g:1218:9: ( '.' ) 'super' t260= superSuffix
                    {
                    // Java.g:1218:9: ( '.' )
                    // Java.g:1218:10: '.'
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector9558); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

                    }


                    match(input,SUPER,FOLLOW_SUPER_in_selector9562); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'super'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("superSuffix"));}

                    pushFollow(FOLLOW_superSuffix_in_selector9577);
                    t260=superSuffix();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t260!=null?((Token)t260.start):null), (t260!=null?((Token)t260.stop):null));}

                    }
                    break;
                case 5 :
                    // Java.g:1220:9: t261= innerCreator
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("innerCreator"));}

                    pushFollow(FOLLOW_innerCreator_in_selector9593);
                    t261=innerCreator();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t261!=null?((Token)t261.start):null), (t261!=null?((Token)t261.stop):null));}

                    }
                    break;
                case 6 :
                    // Java.g:1221:9: ( '[' ) t262= expression ( ']' )
                    {
                    // Java.g:1221:9: ( '[' )
                    // Java.g:1221:10: '['
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_selector9606); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_selector9614);
                    t262=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t262!=null?((Token)t262.start):null), (t262!=null?((Token)t262.stop):null));}

                    // Java.g:1221:174: ( ']' )
                    // Java.g:1221:175: ']'
                    {
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_selector9619); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 97, selector_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "selector"


    public static class typeArgumentsOrDiamond_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeArgumentsOrDiamond"
    // Java.g:1225:1: typeArgumentsOrDiamond : ( ( '<' ) ( '>' ) |t263= typeArguments );
    public final typeArgumentsOrDiamond_return typeArgumentsOrDiamond() throws RecognitionException {
        typeArgumentsOrDiamond_return retval = new typeArgumentsOrDiamond_return();
        retval.start = input.LT(1);

        int typeArgumentsOrDiamond_StartIndex = input.index();

        typeArguments_return t263 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 98) ) { return retval; }

            // Java.g:1226:4: ( ( '<' ) ( '>' ) |t263= typeArguments )
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==LT) ) {
                int LA158_1 = input.LA(2);

                if ( (LA158_1==GT) ) {
                    alt158=1;
                }
                else if ( (LA158_1==BOOLEAN||LA158_1==BYTE||LA158_1==CHAR||LA158_1==DOUBLE||LA158_1==FLOAT||LA158_1==IDENTIFIER||LA158_1==INT||LA158_1==LONG||LA158_1==QUES||LA158_1==SHORT) ) {
                    alt158=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 158, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 158, 0, input);

                throw nvae;

            }
            switch (alt158) {
                case 1 :
                    // Java.g:1227:7: ( '<' ) ( '>' )
                    {
                    // Java.g:1227:7: ( '<' )
                    // Java.g:1227:8: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_typeArgumentsOrDiamond9645); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1227:45: ( '>' )
                    // Java.g:1227:46: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_typeArgumentsOrDiamond9650); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1228:7: t263= typeArguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeArguments"));}

                    pushFollow(FOLLOW_typeArguments_in_typeArgumentsOrDiamond9664);
                    t263=typeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t263!=null?((Token)t263.start):null), (t263!=null?((Token)t263.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 98, typeArgumentsOrDiamond_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeArgumentsOrDiamond"


    public static class nonWildcardTypeArgumentsOrDiamond_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "nonWildcardTypeArgumentsOrDiamond"
    // Java.g:1231:1: nonWildcardTypeArgumentsOrDiamond : ( ( '<' ) ( '>' ) |t264= nonWildcardTypeArguments );
    public final nonWildcardTypeArgumentsOrDiamond_return nonWildcardTypeArgumentsOrDiamond() throws RecognitionException {
        nonWildcardTypeArgumentsOrDiamond_return retval = new nonWildcardTypeArgumentsOrDiamond_return();
        retval.start = input.LT(1);

        int nonWildcardTypeArgumentsOrDiamond_StartIndex = input.index();

        nonWildcardTypeArguments_return t264 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 99) ) { return retval; }

            // Java.g:1232:4: ( ( '<' ) ( '>' ) |t264= nonWildcardTypeArguments )
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==LT) ) {
                int LA159_1 = input.LA(2);

                if ( (LA159_1==GT) ) {
                    alt159=1;
                }
                else if ( (LA159_1==BOOLEAN||LA159_1==BYTE||LA159_1==CHAR||LA159_1==DOUBLE||LA159_1==FLOAT||LA159_1==IDENTIFIER||LA159_1==INT||LA159_1==LONG||LA159_1==SHORT) ) {
                    alt159=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 159, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 159, 0, input);

                throw nvae;

            }
            switch (alt159) {
                case 1 :
                    // Java.g:1233:7: ( '<' ) ( '>' )
                    {
                    // Java.g:1233:7: ( '<' )
                    // Java.g:1233:8: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_nonWildcardTypeArgumentsOrDiamond9689); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

                    }


                    // Java.g:1233:45: ( '>' )
                    // Java.g:1233:46: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_nonWildcardTypeArgumentsOrDiamond9694); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1234:7: t264= nonWildcardTypeArguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_nonWildcardTypeArgumentsOrDiamond9708);
                    t264=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t264!=null?((Token)t264.start):null), (t264!=null?((Token)t264.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 99, nonWildcardTypeArgumentsOrDiamond_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "nonWildcardTypeArgumentsOrDiamond"


    public static class creator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "creator"
    // Java.g:1237:1: creator : ( 'new' t265= nonWildcardTypeArguments t266= classOrInterfaceType t267= classCreatorRest | 'new' t268= classOrInterfaceType t269= classCreatorRest |t270= arrayCreator );
    public final creator_return creator() throws RecognitionException {
        creator_return retval = new creator_return();
        retval.start = input.LT(1);

        int creator_StartIndex = input.index();

        nonWildcardTypeArguments_return t265 =null;

        classOrInterfaceType_return t266 =null;

        classCreatorRest_return t267 =null;

        classOrInterfaceType_return t268 =null;

        classCreatorRest_return t269 =null;

        arrayCreator_return t270 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 100) ) { return retval; }

            // Java.g:1238:5: ( 'new' t265= nonWildcardTypeArguments t266= classOrInterfaceType t267= classCreatorRest | 'new' t268= classOrInterfaceType t269= classCreatorRest |t270= arrayCreator )
            int alt160=3;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==NEW) ) {
                int LA160_1 = input.LA(2);

                if ( (synpred246_Java()) ) {
                    alt160=1;
                }
                else if ( (synpred247_Java()) ) {
                    alt160=2;
                }
                else if ( (true) ) {
                    alt160=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 160, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 160, 0, input);

                throw nvae;

            }
            switch (alt160) {
                case 1 :
                    // Java.g:1238:9: 'new' t265= nonWildcardTypeArguments t266= classOrInterfaceType t267= classCreatorRest
                    {
                    match(input,NEW,FOLLOW_NEW_in_creator9729); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'new'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator9736);
                    t265=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t265!=null?((Token)t265.start):null), (t265!=null?((Token)t265.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

                    pushFollow(FOLLOW_classOrInterfaceType_in_creator9744);
                    t266=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t266!=null?((Token)t266.start):null), (t266!=null?((Token)t266.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classCreatorRest"));}

                    pushFollow(FOLLOW_classCreatorRest_in_creator9752);
                    t267=classCreatorRest();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t267!=null?((Token)t267.start):null), (t267!=null?((Token)t267.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1239:9: 'new' t268= classOrInterfaceType t269= classCreatorRest
                    {
                    match(input,NEW,FOLLOW_NEW_in_creator9764); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'new'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

                    pushFollow(FOLLOW_classOrInterfaceType_in_creator9771);
                    t268=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t268!=null?((Token)t268.start):null), (t268!=null?((Token)t268.stop):null));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classCreatorRest"));}

                    pushFollow(FOLLOW_classCreatorRest_in_creator9779);
                    t269=classCreatorRest();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t269!=null?((Token)t269.start):null), (t269!=null?((Token)t269.stop):null));}

                    }
                    break;
                case 3 :
                    // Java.g:1240:9: t270= arrayCreator
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arrayCreator"));}

                    pushFollow(FOLLOW_arrayCreator_in_creator9795);
                    t270=arrayCreator();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t270!=null?((Token)t270.start):null), (t270!=null?((Token)t270.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 100, creator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "creator"


    public static class arrayCreator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "arrayCreator"
    // Java.g:1243:1: arrayCreator : ( 'new' t271= createdName ( '[' ) ( ']' ) ( ( '[' ) ( ']' ) )* t272= arrayInitializer | 'new' t273= createdName ( '[' ) t274= expression ( ']' ) ( ( '[' ) t275= expression ( ']' ) )* ( ( '[' ) ( ']' ) )* );
    public final arrayCreator_return arrayCreator() throws RecognitionException {
        arrayCreator_return retval = new arrayCreator_return();
        retval.start = input.LT(1);

        int arrayCreator_StartIndex = input.index();

        createdName_return t271 =null;

        arrayInitializer_return t272 =null;

        createdName_return t273 =null;

        expression_return t274 =null;

        expression_return t275 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 101) ) { return retval; }

            // Java.g:1244:5: ( 'new' t271= createdName ( '[' ) ( ']' ) ( ( '[' ) ( ']' ) )* t272= arrayInitializer | 'new' t273= createdName ( '[' ) t274= expression ( ']' ) ( ( '[' ) t275= expression ( ']' ) )* ( ( '[' ) ( ']' ) )* )
            int alt164=2;
            int LA164_0 = input.LA(1);

            if ( (LA164_0==NEW) ) {
                int LA164_1 = input.LA(2);

                if ( (synpred249_Java()) ) {
                    alt164=1;
                }
                else if ( (true) ) {
                    alt164=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 164, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 164, 0, input);

                throw nvae;

            }
            switch (alt164) {
                case 1 :
                    // Java.g:1244:9: 'new' t271= createdName ( '[' ) ( ']' ) ( ( '[' ) ( ']' ) )* t272= arrayInitializer
                    {
                    match(input,NEW,FOLLOW_NEW_in_arrayCreator9817); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'new'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("createdName"));}

                    pushFollow(FOLLOW_createdName_in_arrayCreator9824);
                    t271=createdName();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t271!=null?((Token)t271.start):null), (t271!=null?((Token)t271.stop):null));}

                    // Java.g:1245:9: ( '[' )
                    // Java.g:1245:10: '['
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator9837); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    }


                    // Java.g:1245:47: ( ']' )
                    // Java.g:1245:48: ']'
                    {
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator9842); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    }


                    // Java.g:1246:9: ( ( '[' ) ( ']' ) )*
                    loop161:
                    do {
                        int alt161=2;
                        int LA161_0 = input.LA(1);

                        if ( (LA161_0==LBRACKET) ) {
                            alt161=1;
                        }


                        switch (alt161) {
                    	case 1 :
                    	    // Java.g:1246:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:1246:10: ( '[' )
                    	    // Java.g:1246:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator9856); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:1246:48: ( ']' )
                    	    // Java.g:1246:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator9861); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop161;
                        }
                    } while (true);


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arrayInitializer"));}

                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreator9888);
                    t272=arrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t272!=null?((Token)t272.start):null), (t272!=null?((Token)t272.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1250:9: 'new' t273= createdName ( '[' ) t274= expression ( ']' ) ( ( '[' ) t275= expression ( ']' ) )* ( ( '[' ) ( ']' ) )*
                    {
                    match(input,NEW,FOLLOW_NEW_in_arrayCreator9901); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'new'",input.LT(-1));}

                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("createdName"));}

                    pushFollow(FOLLOW_createdName_in_arrayCreator9908);
                    t273=createdName();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t273!=null?((Token)t273.start):null), (t273!=null?((Token)t273.stop):null));}

                    // Java.g:1251:9: ( '[' )
                    // Java.g:1251:10: '['
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator9921); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    }


                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_arrayCreator9929);
                    t274=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t274!=null?((Token)t274.start):null), (t274!=null?((Token)t274.stop):null));}

                    // Java.g:1252:9: ( ']' )
                    // Java.g:1252:10: ']'
                    {
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator9942); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    }


                    // Java.g:1253:9: ( ( '[' ) t275= expression ( ']' ) )*
                    loop162:
                    do {
                        int alt162=2;
                        int LA162_0 = input.LA(1);

                        if ( (LA162_0==LBRACKET) ) {
                            int LA162_1 = input.LA(2);

                            if ( (synpred250_Java()) ) {
                                alt162=1;
                            }


                        }


                        switch (alt162) {
                    	case 1 :
                    	    // Java.g:1253:13: ( '[' ) t275= expression ( ']' )
                    	    {
                    	    // Java.g:1253:13: ( '[' )
                    	    // Java.g:1253:14: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator9959); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    	    pushFollow(FOLLOW_expression_in_arrayCreator9967);
                    	    t275=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t275!=null?((Token)t275.start):null), (t275!=null?((Token)t275.stop):null));}

                    	    // Java.g:1254:13: ( ']' )
                    	    // Java.g:1254:14: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator9984); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop162;
                        }
                    } while (true);


                    // Java.g:1256:9: ( ( '[' ) ( ']' ) )*
                    loop163:
                    do {
                        int alt163=2;
                        int LA163_0 = input.LA(1);

                        if ( (LA163_0==LBRACKET) ) {
                            int LA163_2 = input.LA(2);

                            if ( (LA163_2==RBRACKET) ) {
                                alt163=1;
                            }


                        }


                        switch (alt163) {
                    	case 1 :
                    	    // Java.g:1256:10: ( '[' ) ( ']' )
                    	    {
                    	    // Java.g:1256:10: ( '[' )
                    	    // Java.g:1256:11: '['
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator10009); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("'['",input.LT(-1));}

                    	    }


                    	    // Java.g:1256:48: ( ']' )
                    	    // Java.g:1256:49: ']'
                    	    {
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator10014); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("']'",input.LT(-1));}

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop163;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 101, arrayCreator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arrayCreator"


    public static class variableInitializer_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "variableInitializer"
    // Java.g:1260:1: variableInitializer : (t276= arrayInitializer |t277= expression );
    public final variableInitializer_return variableInitializer() throws RecognitionException {
        variableInitializer_return retval = new variableInitializer_return();
        retval.start = input.LT(1);

        int variableInitializer_StartIndex = input.index();

        arrayInitializer_return t276 =null;

        expression_return t277 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 102) ) { return retval; }

            // Java.g:1261:5: (t276= arrayInitializer |t277= expression )
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==LBRACE) ) {
                alt165=1;
            }
            else if ( (LA165_0==BANG||(LA165_0 >= BINLITERAL && LA165_0 <= BOOLEAN)||LA165_0==BYTE||(LA165_0 >= CHAR && LA165_0 <= CHARLITERAL)||(LA165_0 >= DOUBLE && LA165_0 <= DOUBLELITERAL)||LA165_0==FALSE||(LA165_0 >= FLOAT && LA165_0 <= FLOATLITERAL)||LA165_0==IDENTIFIER||LA165_0==INT||LA165_0==INTLITERAL||(LA165_0 >= LONG && LA165_0 <= LPAREN)||(LA165_0 >= NEW && LA165_0 <= NULL)||LA165_0==PLUS||LA165_0==PLUSPLUS||LA165_0==SHORT||(LA165_0 >= STRINGLITERAL && LA165_0 <= SUB)||(LA165_0 >= SUBSUB && LA165_0 <= SUPER)||LA165_0==THIS||LA165_0==TILDE||LA165_0==TRUE||LA165_0==VOID) ) {
                alt165=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 165, 0, input);

                throw nvae;

            }
            switch (alt165) {
                case 1 :
                    // Java.g:1261:9: t276= arrayInitializer
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arrayInitializer"));}

                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer10051);
                    t276=arrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t276!=null?((Token)t276.start):null), (t276!=null?((Token)t276.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1262:9: t277= expression
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expression"));}

                    pushFollow(FOLLOW_expression_in_variableInitializer10067);
                    t277=expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t277!=null?((Token)t277.start):null), (t277!=null?((Token)t277.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 102, variableInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableInitializer"


    public static class arrayInitializer_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "arrayInitializer"
    // Java.g:1265:1: arrayInitializer : ( '{' ) (t278= variableInitializer ( ( ',' ) t279= variableInitializer )* )? ( ( ',' ) )? ( '}' ) ;
    public final arrayInitializer_return arrayInitializer() throws RecognitionException {
        arrayInitializer_return retval = new arrayInitializer_return();
        retval.start = input.LT(1);

        int arrayInitializer_StartIndex = input.index();

        variableInitializer_return t278 =null;

        variableInitializer_return t279 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 103) ) { return retval; }

            // Java.g:1266:5: ( ( '{' ) (t278= variableInitializer ( ( ',' ) t279= variableInitializer )* )? ( ( ',' ) )? ( '}' ) )
            // Java.g:1266:9: ( '{' ) (t278= variableInitializer ( ( ',' ) t279= variableInitializer )* )? ( ( ',' ) )? ( '}' )
            {
            // Java.g:1266:9: ( '{' )
            // Java.g:1266:10: '{'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_arrayInitializer10090); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'{'",input.LT(-1));}

            }


            // Java.g:1267:13: (t278= variableInitializer ( ( ',' ) t279= variableInitializer )* )?
            int alt167=2;
            int LA167_0 = input.LA(1);

            if ( (LA167_0==BANG||(LA167_0 >= BINLITERAL && LA167_0 <= BOOLEAN)||LA167_0==BYTE||(LA167_0 >= CHAR && LA167_0 <= CHARLITERAL)||(LA167_0 >= DOUBLE && LA167_0 <= DOUBLELITERAL)||LA167_0==FALSE||(LA167_0 >= FLOAT && LA167_0 <= FLOATLITERAL)||LA167_0==IDENTIFIER||LA167_0==INT||LA167_0==INTLITERAL||LA167_0==LBRACE||(LA167_0 >= LONG && LA167_0 <= LPAREN)||(LA167_0 >= NEW && LA167_0 <= NULL)||LA167_0==PLUS||LA167_0==PLUSPLUS||LA167_0==SHORT||(LA167_0 >= STRINGLITERAL && LA167_0 <= SUB)||(LA167_0 >= SUBSUB && LA167_0 <= SUPER)||LA167_0==THIS||LA167_0==TILDE||LA167_0==TRUE||LA167_0==VOID) ) {
                alt167=1;
            }
            switch (alt167) {
                case 1 :
                    // Java.g:1267:14: t278= variableInitializer ( ( ',' ) t279= variableInitializer )*
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableInitializer"));}

                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer10112);
                    t278=variableInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t278!=null?((Token)t278.start):null), (t278!=null?((Token)t278.stop):null));}

                    // Java.g:1268:17: ( ( ',' ) t279= variableInitializer )*
                    loop166:
                    do {
                        int alt166=2;
                        int LA166_0 = input.LA(1);

                        if ( (LA166_0==COMMA) ) {
                            int LA166_1 = input.LA(2);

                            if ( (LA166_1==BANG||(LA166_1 >= BINLITERAL && LA166_1 <= BOOLEAN)||LA166_1==BYTE||(LA166_1 >= CHAR && LA166_1 <= CHARLITERAL)||(LA166_1 >= DOUBLE && LA166_1 <= DOUBLELITERAL)||LA166_1==FALSE||(LA166_1 >= FLOAT && LA166_1 <= FLOATLITERAL)||LA166_1==IDENTIFIER||LA166_1==INT||LA166_1==INTLITERAL||LA166_1==LBRACE||(LA166_1 >= LONG && LA166_1 <= LPAREN)||(LA166_1 >= NEW && LA166_1 <= NULL)||LA166_1==PLUS||LA166_1==PLUSPLUS||LA166_1==SHORT||(LA166_1 >= STRINGLITERAL && LA166_1 <= SUB)||(LA166_1 >= SUBSUB && LA166_1 <= SUPER)||LA166_1==THIS||LA166_1==TILDE||LA166_1==TRUE||LA166_1==VOID) ) {
                                alt166=1;
                            }


                        }


                        switch (alt166) {
                    	case 1 :
                    	    // Java.g:1268:18: ( ',' ) t279= variableInitializer
                    	    {
                    	    // Java.g:1268:18: ( ',' )
                    	    // Java.g:1268:19: ','
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer10134); if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    	    }


                    	    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("variableInitializer"));}

                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer10142);
                    	    t279=variableInitializer();

                    	    state._fsp--;
                    	    if (state.failed) return retval;

                    	    if ( state.backtracking==0 ) {T.popTop().setTextRange((t279!=null?((Token)t279.start):null), (t279!=null?((Token)t279.stop):null));}

                    	    }
                    	    break;

                    	default :
                    	    break loop166;
                        }
                    } while (true);


                    }
                    break;

            }


            // Java.g:1271:13: ( ( ',' ) )?
            int alt168=2;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==COMMA) ) {
                alt168=1;
            }
            switch (alt168) {
                case 1 :
                    // Java.g:1271:14: ( ',' )
                    {
                    // Java.g:1271:14: ( ',' )
                    // Java.g:1271:15: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer10195); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("','",input.LT(-1));}

                    }


                    }
                    break;

            }


            // Java.g:1272:9: ( '}' )
            // Java.g:1272:10: '}'
            {
            match(input,RBRACE,FOLLOW_RBRACE_in_arrayInitializer10211); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'}'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 103, arrayInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arrayInitializer"


    public static class createdName_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "createdName"
    // Java.g:1276:1: createdName : (t280= classOrInterfaceType |t281= primitiveType );
    public final createdName_return createdName() throws RecognitionException {
        createdName_return retval = new createdName_return();
        retval.start = input.LT(1);

        int createdName_StartIndex = input.index();

        classOrInterfaceType_return t280 =null;

        primitiveType_return t281 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 104) ) { return retval; }

            // Java.g:1277:5: (t280= classOrInterfaceType |t281= primitiveType )
            int alt169=2;
            int LA169_0 = input.LA(1);

            if ( (LA169_0==IDENTIFIER) ) {
                alt169=1;
            }
            else if ( (LA169_0==BOOLEAN||LA169_0==BYTE||LA169_0==CHAR||LA169_0==DOUBLE||LA169_0==FLOAT||LA169_0==INT||LA169_0==LONG||LA169_0==SHORT) ) {
                alt169=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 169, 0, input);

                throw nvae;

            }
            switch (alt169) {
                case 1 :
                    // Java.g:1277:9: t280= classOrInterfaceType
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classOrInterfaceType"));}

                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName10251);
                    t280=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t280!=null?((Token)t280.start):null), (t280!=null?((Token)t280.stop):null));}

                    }
                    break;
                case 2 :
                    // Java.g:1278:9: t281= primitiveType
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("primitiveType"));}

                    pushFollow(FOLLOW_primitiveType_in_createdName10267);
                    t281=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t281!=null?((Token)t281.start):null), (t281!=null?((Token)t281.stop):null));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 104, createdName_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "createdName"


    public static class innerCreator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "innerCreator"
    // Java.g:1281:1: innerCreator : ( '.' ) 'new' (t282= nonWildcardTypeArguments )? IDENTIFIER (t283= nonWildcardTypeArgumentsOrDiamond )? t284= classCreatorRest ;
    public final innerCreator_return innerCreator() throws RecognitionException {
        innerCreator_return retval = new innerCreator_return();
        retval.start = input.LT(1);

        int innerCreator_StartIndex = input.index();

        nonWildcardTypeArguments_return t282 =null;

        nonWildcardTypeArgumentsOrDiamond_return t283 =null;

        classCreatorRest_return t284 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 105) ) { return retval; }

            // Java.g:1282:5: ( ( '.' ) 'new' (t282= nonWildcardTypeArguments )? IDENTIFIER (t283= nonWildcardTypeArgumentsOrDiamond )? t284= classCreatorRest )
            // Java.g:1282:9: ( '.' ) 'new' (t282= nonWildcardTypeArguments )? IDENTIFIER (t283= nonWildcardTypeArgumentsOrDiamond )? t284= classCreatorRest
            {
            // Java.g:1282:9: ( '.' )
            // Java.g:1282:10: '.'
            {
            match(input,DOT,FOLLOW_DOT_in_innerCreator10291); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'.'",input.LT(-1));}

            }


            match(input,NEW,FOLLOW_NEW_in_innerCreator10295); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'new'",input.LT(-1));}

            // Java.g:1283:9: (t282= nonWildcardTypeArguments )?
            int alt170=2;
            int LA170_0 = input.LA(1);

            if ( (LA170_0==LT) ) {
                alt170=1;
            }
            switch (alt170) {
                case 1 :
                    // Java.g:1283:10: t282= nonWildcardTypeArguments
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArguments"));}

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator10311);
                    t282=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t282!=null?((Token)t282.start):null), (t282!=null?((Token)t282.stop):null));}

                    }
                    break;

            }


            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_innerCreator10334); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("IDENTIFIER['"+input.LT(-1).getText()+"']",input.LT(-1));}

            // Java.g:1286:9: (t283= nonWildcardTypeArgumentsOrDiamond )?
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==LT) ) {
                alt171=1;
            }
            switch (alt171) {
                case 1 :
                    // Java.g:1286:10: t283= nonWildcardTypeArgumentsOrDiamond
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("nonWildcardTypeArgumentsOrDiamond"));}

                    pushFollow(FOLLOW_nonWildcardTypeArgumentsOrDiamond_in_innerCreator10351);
                    t283=nonWildcardTypeArgumentsOrDiamond();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t283!=null?((Token)t283.start):null), (t283!=null?((Token)t283.stop):null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classCreatorRest"));}

            pushFollow(FOLLOW_classCreatorRest_in_innerCreator10378);
            t284=classCreatorRest();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t284!=null?((Token)t284.start):null), (t284!=null?((Token)t284.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 105, innerCreator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "innerCreator"


    public static class classCreatorRest_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "classCreatorRest"
    // Java.g:1292:1: classCreatorRest :t285= arguments (t286= classBody )? ;
    public final classCreatorRest_return classCreatorRest() throws RecognitionException {
        classCreatorRest_return retval = new classCreatorRest_return();
        retval.start = input.LT(1);

        int classCreatorRest_StartIndex = input.index();

        arguments_return t285 =null;

        classBody_return t286 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 106) ) { return retval; }

            // Java.g:1293:5: (t285= arguments (t286= classBody )? )
            // Java.g:1293:9: t285= arguments (t286= classBody )?
            {
            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("arguments"));}

            pushFollow(FOLLOW_arguments_in_classCreatorRest10405);
            t285=arguments();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t285!=null?((Token)t285.start):null), (t285!=null?((Token)t285.stop):null));}

            // Java.g:1294:9: (t286= classBody )?
            int alt172=2;
            int LA172_0 = input.LA(1);

            if ( (LA172_0==LBRACE) ) {
                alt172=1;
            }
            switch (alt172) {
                case 1 :
                    // Java.g:1294:10: t286= classBody
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("classBody"));}

                    pushFollow(FOLLOW_classBody_in_classCreatorRest10422);
                    t286=classBody();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t286!=null?((Token)t286.start):null), (t286!=null?((Token)t286.stop):null));}

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 106, classCreatorRest_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classCreatorRest"


    public static class nonWildcardTypeArguments_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "nonWildcardTypeArguments"
    // Java.g:1299:1: nonWildcardTypeArguments : ( '<' ) t287= typeList ( '>' ) ;
    public final nonWildcardTypeArguments_return nonWildcardTypeArguments() throws RecognitionException {
        nonWildcardTypeArguments_return retval = new nonWildcardTypeArguments_return();
        retval.start = input.LT(1);

        int nonWildcardTypeArguments_StartIndex = input.index();

        typeList_return t287 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 107) ) { return retval; }

            // Java.g:1300:5: ( ( '<' ) t287= typeList ( '>' ) )
            // Java.g:1300:9: ( '<' ) t287= typeList ( '>' )
            {
            // Java.g:1300:9: ( '<' )
            // Java.g:1300:10: '<'
            {
            match(input,LT,FOLLOW_LT_in_nonWildcardTypeArguments10457); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'<'",input.LT(-1));}

            }


            if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("typeList"));}

            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments10465);
            t287=typeList();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.popTop().setTextRange((t287!=null?((Token)t287.start):null), (t287!=null?((Token)t287.stop):null));}

            // Java.g:1301:9: ( '>' )
            // Java.g:1301:10: '>'
            {
            match(input,GT,FOLLOW_GT_in_nonWildcardTypeArguments10478); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'>'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 107, nonWildcardTypeArguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "nonWildcardTypeArguments"


    public static class arguments_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "arguments"
    // Java.g:1304:1: arguments : ( '(' ) (t288= expressionList )? ( ')' ) ;
    public final arguments_return arguments() throws RecognitionException {
        arguments_return retval = new arguments_return();
        retval.start = input.LT(1);

        int arguments_StartIndex = input.index();

        expressionList_return t288 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 108) ) { return retval; }

            // Java.g:1305:5: ( ( '(' ) (t288= expressionList )? ( ')' ) )
            // Java.g:1305:9: ( '(' ) (t288= expressionList )? ( ')' )
            {
            // Java.g:1305:9: ( '(' )
            // Java.g:1305:10: '('
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_arguments10501); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("'('",input.LT(-1));}

            }


            // Java.g:1305:47: (t288= expressionList )?
            int alt173=2;
            int LA173_0 = input.LA(1);

            if ( (LA173_0==BANG||(LA173_0 >= BINLITERAL && LA173_0 <= BOOLEAN)||LA173_0==BYTE||(LA173_0 >= CHAR && LA173_0 <= CHARLITERAL)||(LA173_0 >= DOUBLE && LA173_0 <= DOUBLELITERAL)||LA173_0==FALSE||(LA173_0 >= FLOAT && LA173_0 <= FLOATLITERAL)||LA173_0==IDENTIFIER||LA173_0==INT||LA173_0==INTLITERAL||(LA173_0 >= LONG && LA173_0 <= LPAREN)||(LA173_0 >= NEW && LA173_0 <= NULL)||LA173_0==PLUS||LA173_0==PLUSPLUS||LA173_0==SHORT||(LA173_0 >= STRINGLITERAL && LA173_0 <= SUB)||(LA173_0 >= SUBSUB && LA173_0 <= SUPER)||LA173_0==THIS||LA173_0==TILDE||LA173_0==TRUE||LA173_0==VOID) ) {
                alt173=1;
            }
            switch (alt173) {
                case 1 :
                    // Java.g:1305:48: t288= expressionList
                    {
                    if ( state.backtracking==0 ) {T.pushTop();T.setCurrentParent(T.addNode("expressionList"));}

                    pushFollow(FOLLOW_expressionList_in_arguments10510);
                    t288=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.popTop().setTextRange((t288!=null?((Token)t288.start):null), (t288!=null?((Token)t288.stop):null));}

                    }
                    break;

            }


            // Java.g:1306:12: ( ')' )
            // Java.g:1306:13: ')'
            {
            match(input,RPAREN,FOLLOW_RPAREN_in_arguments10526); if (state.failed) return retval;

            if ( state.backtracking==0 ) {T.addLeaf("')'",input.LT(-1));}

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 108, arguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arguments"


    public static class literal_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "literal"
    // Java.g:1309:1: literal : ( INTLITERAL | LONGLITERAL | BINLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL );
    public final literal_return literal() throws RecognitionException {
        literal_return retval = new literal_return();
        retval.start = input.LT(1);

        int literal_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 109) ) { return retval; }

            // Java.g:1310:5: ( INTLITERAL | LONGLITERAL | BINLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL )
            int alt174=10;
            switch ( input.LA(1) ) {
            case INTLITERAL:
                {
                alt174=1;
                }
                break;
            case LONGLITERAL:
                {
                alt174=2;
                }
                break;
            case BINLITERAL:
                {
                alt174=3;
                }
                break;
            case FLOATLITERAL:
                {
                alt174=4;
                }
                break;
            case DOUBLELITERAL:
                {
                alt174=5;
                }
                break;
            case CHARLITERAL:
                {
                alt174=6;
                }
                break;
            case STRINGLITERAL:
                {
                alt174=7;
                }
                break;
            case TRUE:
                {
                alt174=8;
                }
                break;
            case FALSE:
                {
                alt174=9;
                }
                break;
            case NULL:
                {
                alt174=10;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 174, 0, input);

                throw nvae;

            }

            switch (alt174) {
                case 1 :
                    // Java.g:1310:9: INTLITERAL
                    {
                    match(input,INTLITERAL,FOLLOW_INTLITERAL_in_literal10548); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("INTLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 2 :
                    // Java.g:1311:9: LONGLITERAL
                    {
                    match(input,LONGLITERAL,FOLLOW_LONGLITERAL_in_literal10560); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("LONGLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 3 :
                    // Java.g:1312:9: BINLITERAL
                    {
                    match(input,BINLITERAL,FOLLOW_BINLITERAL_in_literal10572); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("BINLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 4 :
                    // Java.g:1313:9: FLOATLITERAL
                    {
                    match(input,FLOATLITERAL,FOLLOW_FLOATLITERAL_in_literal10584); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("FLOATLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 5 :
                    // Java.g:1314:9: DOUBLELITERAL
                    {
                    match(input,DOUBLELITERAL,FOLLOW_DOUBLELITERAL_in_literal10596); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("DOUBLELITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 6 :
                    // Java.g:1315:9: CHARLITERAL
                    {
                    match(input,CHARLITERAL,FOLLOW_CHARLITERAL_in_literal10608); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("CHARLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 7 :
                    // Java.g:1316:9: STRINGLITERAL
                    {
                    match(input,STRINGLITERAL,FOLLOW_STRINGLITERAL_in_literal10620); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("STRINGLITERAL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 8 :
                    // Java.g:1317:9: TRUE
                    {
                    match(input,TRUE,FOLLOW_TRUE_in_literal10632); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("TRUE['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 9 :
                    // Java.g:1318:9: FALSE
                    {
                    match(input,FALSE,FOLLOW_FALSE_in_literal10644); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("FALSE['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;
                case 10 :
                    // Java.g:1319:9: NULL
                    {
                    match(input,NULL,FOLLOW_NULL_in_literal10656); if (state.failed) return retval;

                    if ( state.backtracking==0 ) {T.addLeaf("NULL['"+input.LT(-1).getText()+"']",input.LT(-1));}

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 109, literal_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "literal"

    // $ANTLR start synpred2_Java
    public final void synpred2_Java_fragment() throws RecognitionException {
        annotations_return t0 =null;

        packageDeclaration_return t1 =null;


        // Java.g:345:13: ( (t0= annotations )? t1= packageDeclaration )
        // Java.g:345:13: (t0= annotations )? t1= packageDeclaration
        {
        // Java.g:345:13: (t0= annotations )?
        int alt175=2;
        int LA175_0 = input.LA(1);

        if ( (LA175_0==MONKEYS_AT) ) {
            alt175=1;
        }
        switch (alt175) {
            case 1 :
                // Java.g:345:14: t0= annotations
                {
                pushFollow(FOLLOW_annotations_in_synpred2_Java97);
                t0=annotations();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        pushFollow(FOLLOW_packageDeclaration_in_synpred2_Java132);
        t1=packageDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_Java

    // $ANTLR start synpred12_Java
    public final void synpred12_Java_fragment() throws RecognitionException {
        classDeclaration_return t6 =null;


        // Java.g:389:10: (t6= classDeclaration )
        // Java.g:389:10: t6= classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred12_Java565);
        t6=classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred12_Java

    // $ANTLR start synpred27_Java
    public final void synpred27_Java_fragment() throws RecognitionException {
        normalClassDeclaration_return t10 =null;


        // Java.g:436:9: (t10= normalClassDeclaration )
        // Java.g:436:9: t10= normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred27_Java844);
        t10=normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred27_Java

    // $ANTLR start synpred43_Java
    public final void synpred43_Java_fragment() throws RecognitionException {
        normalInterfaceDeclaration_return t33 =null;


        // Java.g:524:9: (t33= normalInterfaceDeclaration )
        // Java.g:524:9: t33= normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1702);
        t33=normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred43_Java

    // $ANTLR start synpred52_Java
    public final void synpred52_Java_fragment() throws RecognitionException {
        fieldDeclaration_return t45 =null;


        // Java.g:566:10: (t45= fieldDeclaration )
        // Java.g:566:10: t45= fieldDeclaration
        {
        pushFollow(FOLLOW_fieldDeclaration_in_synpred52_Java2127);
        t45=fieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred52_Java

    // $ANTLR start synpred53_Java
    public final void synpred53_Java_fragment() throws RecognitionException {
        methodDeclaration_return t46 =null;


        // Java.g:567:10: (t46= methodDeclaration )
        // Java.g:567:10: t46= methodDeclaration
        {
        pushFollow(FOLLOW_methodDeclaration_in_synpred53_Java2144);
        t46=methodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred53_Java

    // $ANTLR start synpred54_Java
    public final void synpred54_Java_fragment() throws RecognitionException {
        classDeclaration_return t47 =null;


        // Java.g:568:10: (t47= classDeclaration )
        // Java.g:568:10: t47= classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred54_Java2161);
        t47=classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred54_Java

    // $ANTLR start synpred57_Java
    public final void synpred57_Java_fragment() throws RecognitionException {
        explicitConstructorInvocation_return t53 =null;


        // Java.g:584:10: (t53= explicitConstructorInvocation )
        // Java.g:584:10: t53= explicitConstructorInvocation
        {
        pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred57_Java2340);
        t53=explicitConstructorInvocation();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred57_Java

    // $ANTLR start synpred59_Java
    public final void synpred59_Java_fragment() throws RecognitionException {
        modifiers_return t49 =null;

        typeParameters_return t50 =null;

        formalParameters_return t51 =null;

        qualifiedNameList_return t52 =null;

        explicitConstructorInvocation_return t53 =null;

        blockStatement_return t54 =null;


        // Java.g:576:10: (t49= modifiers (t50= typeParameters )? IDENTIFIER t51= formalParameters ( 'throws' t52= qualifiedNameList )? ( '{' ) (t53= explicitConstructorInvocation )? (t54= blockStatement )* ( '}' ) )
        // Java.g:576:10: t49= modifiers (t50= typeParameters )? IDENTIFIER t51= formalParameters ( 'throws' t52= qualifiedNameList )? ( '{' ) (t53= explicitConstructorInvocation )? (t54= blockStatement )* ( '}' )
        {
        pushFollow(FOLLOW_modifiers_in_synpred59_Java2222);
        t49=modifiers();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:577:9: (t50= typeParameters )?
        int alt178=2;
        int LA178_0 = input.LA(1);

        if ( (LA178_0==LT) ) {
            alt178=1;
        }
        switch (alt178) {
            case 1 :
                // Java.g:577:10: t50= typeParameters
                {
                pushFollow(FOLLOW_typeParameters_in_synpred59_Java2239);
                t50=typeParameters();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred59_Java2262); if (state.failed) return ;

        pushFollow(FOLLOW_formalParameters_in_synpred59_Java2278);
        t51=formalParameters();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:581:9: ( 'throws' t52= qualifiedNameList )?
        int alt179=2;
        int LA179_0 = input.LA(1);

        if ( (LA179_0==THROWS) ) {
            alt179=1;
        }
        switch (alt179) {
            case 1 :
                // Java.g:581:10: 'throws' t52= qualifiedNameList
                {
                match(input,THROWS,FOLLOW_THROWS_in_synpred59_Java2291); if (state.failed) return ;

                pushFollow(FOLLOW_qualifiedNameList_in_synpred59_Java2298);
                t52=qualifiedNameList();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        // Java.g:583:9: ( '{' )
        // Java.g:583:10: '{'
        {
        match(input,LBRACE,FOLLOW_LBRACE_in_synpred59_Java2322); if (state.failed) return ;

        }


        // Java.g:584:9: (t53= explicitConstructorInvocation )?
        int alt180=2;
        alt180 = dfa180.predict(input);
        switch (alt180) {
            case 1 :
                // Java.g:584:10: t53= explicitConstructorInvocation
                {
                pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred59_Java2340);
                t53=explicitConstructorInvocation();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        // Java.g:586:9: (t54= blockStatement )*
        loop181:
        do {
            int alt181=2;
            int LA181_0 = input.LA(1);

            if ( (LA181_0==ABSTRACT||(LA181_0 >= ASSERT && LA181_0 <= BANG)||(LA181_0 >= BINLITERAL && LA181_0 <= BYTE)||(LA181_0 >= CHAR && LA181_0 <= CLASS)||LA181_0==CONTINUE||LA181_0==DO||(LA181_0 >= DOUBLE && LA181_0 <= DOUBLELITERAL)||LA181_0==ENUM||(LA181_0 >= FALSE && LA181_0 <= FINAL)||(LA181_0 >= FLOAT && LA181_0 <= FOR)||(LA181_0 >= IDENTIFIER && LA181_0 <= IF)||(LA181_0 >= INT && LA181_0 <= INTLITERAL)||LA181_0==LBRACE||(LA181_0 >= LONG && LA181_0 <= LT)||(LA181_0 >= MONKEYS_AT && LA181_0 <= NULL)||LA181_0==PLUS||(LA181_0 >= PLUSPLUS && LA181_0 <= PUBLIC)||LA181_0==RETURN||(LA181_0 >= SEMI && LA181_0 <= SHORT)||(LA181_0 >= STATIC && LA181_0 <= SUB)||(LA181_0 >= SUBSUB && LA181_0 <= SYNCHRONIZED)||(LA181_0 >= THIS && LA181_0 <= THROW)||(LA181_0 >= TILDE && LA181_0 <= WHILE)) ) {
                alt181=1;
            }


            switch (alt181) {
        	case 1 :
        	    // Java.g:586:10: t54= blockStatement
        	    {
        	    pushFollow(FOLLOW_blockStatement_in_synpred59_Java2368);
        	    t54=blockStatement();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop181;
            }
        } while (true);


        // Java.g:588:9: ( '}' )
        // Java.g:588:10: '}'
        {
        match(input,RBRACE,FOLLOW_RBRACE_in_synpred59_Java2392); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred59_Java

    // $ANTLR start synpred68_Java
    public final void synpred68_Java_fragment() throws RecognitionException {
        interfaceFieldDeclaration_return t66 =null;


        // Java.g:630:9: (t66= interfaceFieldDeclaration )
        // Java.g:630:9: t66= interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred68_Java2869);
        t66=interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred68_Java

    // $ANTLR start synpred69_Java
    public final void synpred69_Java_fragment() throws RecognitionException {
        interfaceMethodDeclaration_return t67 =null;


        // Java.g:631:9: (t67= interfaceMethodDeclaration )
        // Java.g:631:9: t67= interfaceMethodDeclaration
        {
        pushFollow(FOLLOW_interfaceMethodDeclaration_in_synpred69_Java2885);
        t67=interfaceMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred69_Java

    // $ANTLR start synpred70_Java
    public final void synpred70_Java_fragment() throws RecognitionException {
        interfaceDeclaration_return t68 =null;


        // Java.g:632:9: (t68= interfaceDeclaration )
        // Java.g:632:9: t68= interfaceDeclaration
        {
        pushFollow(FOLLOW_interfaceDeclaration_in_synpred70_Java2901);
        t68=interfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred70_Java

    // $ANTLR start synpred71_Java
    public final void synpred71_Java_fragment() throws RecognitionException {
        classDeclaration_return t69 =null;


        // Java.g:633:9: (t69= classDeclaration )
        // Java.g:633:9: t69= classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred71_Java2917);
        t69=classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred71_Java

    // $ANTLR start synpred96_Java
    public final void synpred96_Java_fragment() throws RecognitionException {
        ellipsisParameterDecl_return t90 =null;


        // Java.g:728:9: (t90= ellipsisParameterDecl )
        // Java.g:728:9: t90= ellipsisParameterDecl
        {
        pushFollow(FOLLOW_ellipsisParameterDecl_in_synpred96_Java3879);
        t90=ellipsisParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred96_Java

    // $ANTLR start synpred98_Java
    public final void synpred98_Java_fragment() throws RecognitionException {
        normalParameterDecl_return t91 =null;

        normalParameterDecl_return t92 =null;


        // Java.g:729:9: (t91= normalParameterDecl ( ( ',' ) t92= normalParameterDecl )* )
        // Java.g:729:9: t91= normalParameterDecl ( ( ',' ) t92= normalParameterDecl )*
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java3895);
        t91=normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:730:9: ( ( ',' ) t92= normalParameterDecl )*
        loop184:
        do {
            int alt184=2;
            int LA184_0 = input.LA(1);

            if ( (LA184_0==COMMA) ) {
                alt184=1;
            }


            switch (alt184) {
        	case 1 :
        	    // Java.g:730:10: ( ',' ) t92= normalParameterDecl
        	    {
        	    // Java.g:730:10: ( ',' )
        	    // Java.g:730:11: ','
        	    {
        	    match(input,COMMA,FOLLOW_COMMA_in_synpred98_Java3909); if (state.failed) return ;

        	    }


        	    pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java3917);
        	    t92=normalParameterDecl();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop184;
            }
        } while (true);


        }

    }
    // $ANTLR end synpred98_Java

    // $ANTLR start synpred99_Java
    public final void synpred99_Java_fragment() throws RecognitionException {
        normalParameterDecl_return t93 =null;


        // Java.g:732:10: (t93= normalParameterDecl ( ',' ) )
        // Java.g:732:10: t93= normalParameterDecl ( ',' )
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred99_Java3945);
        t93=normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:733:9: ( ',' )
        // Java.g:733:10: ','
        {
        match(input,COMMA,FOLLOW_COMMA_in_synpred99_Java3958); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred99_Java

    // $ANTLR start synpred103_Java
    public final void synpred103_Java_fragment() throws RecognitionException {
        nonWildcardTypeArguments_return t99 =null;

        arguments_return t100 =null;


        // Java.g:752:9: ( (t99= nonWildcardTypeArguments )? ( 'this' | 'super' ) t100= arguments ( ';' ) )
        // Java.g:752:9: (t99= nonWildcardTypeArguments )? ( 'this' | 'super' ) t100= arguments ( ';' )
        {
        // Java.g:752:9: (t99= nonWildcardTypeArguments )?
        int alt185=2;
        int LA185_0 = input.LA(1);

        if ( (LA185_0==LT) ) {
            alt185=1;
        }
        switch (alt185) {
            case 1 :
                // Java.g:752:10: t99= nonWildcardTypeArguments
                {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred103_Java4140);
                t99=nonWildcardTypeArguments();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        if ( input.LA(1)==SUPER||input.LA(1)==THIS ) {
            input.consume();
            state.errorRecovery=false;
            state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        pushFollow(FOLLOW_arguments_in_synpred103_Java4206);
        t100=arguments();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:757:134: ( ';' )
        // Java.g:757:135: ';'
        {
        match(input,SEMI,FOLLOW_SEMI_in_synpred103_Java4211); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred103_Java

    // $ANTLR start synpred117_Java
    public final void synpred117_Java_fragment() throws RecognitionException {
        annotationMethodDeclaration_return t119 =null;


        // Java.g:839:9: (t119= annotationMethodDeclaration )
        // Java.g:839:9: t119= annotationMethodDeclaration
        {
        pushFollow(FOLLOW_annotationMethodDeclaration_in_synpred117_Java4979);
        t119=annotationMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred117_Java

    // $ANTLR start synpred118_Java
    public final void synpred118_Java_fragment() throws RecognitionException {
        interfaceFieldDeclaration_return t120 =null;


        // Java.g:840:9: (t120= interfaceFieldDeclaration )
        // Java.g:840:9: t120= interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred118_Java4995);
        t120=interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred118_Java

    // $ANTLR start synpred119_Java
    public final void synpred119_Java_fragment() throws RecognitionException {
        normalClassDeclaration_return t121 =null;


        // Java.g:841:9: (t121= normalClassDeclaration )
        // Java.g:841:9: t121= normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred119_Java5011);
        t121=normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred119_Java

    // $ANTLR start synpred120_Java
    public final void synpred120_Java_fragment() throws RecognitionException {
        normalInterfaceDeclaration_return t122 =null;


        // Java.g:842:9: (t122= normalInterfaceDeclaration )
        // Java.g:842:9: t122= normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred120_Java5027);
        t122=normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred120_Java

    // $ANTLR start synpred121_Java
    public final void synpred121_Java_fragment() throws RecognitionException {
        enumDeclaration_return t123 =null;


        // Java.g:843:9: (t123= enumDeclaration )
        // Java.g:843:9: t123= enumDeclaration
        {
        pushFollow(FOLLOW_enumDeclaration_in_synpred121_Java5043);
        t123=enumDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred121_Java

    // $ANTLR start synpred122_Java
    public final void synpred122_Java_fragment() throws RecognitionException {
        annotationTypeDeclaration_return t124 =null;


        // Java.g:844:9: (t124= annotationTypeDeclaration )
        // Java.g:844:9: t124= annotationTypeDeclaration
        {
        pushFollow(FOLLOW_annotationTypeDeclaration_in_synpred122_Java5059);
        t124=annotationTypeDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred122_Java

    // $ANTLR start synpred125_Java
    public final void synpred125_Java_fragment() throws RecognitionException {
        localVariableDeclarationStatement_return t129 =null;


        // Java.g:863:9: (t129= localVariableDeclarationStatement )
        // Java.g:863:9: t129= localVariableDeclarationStatement
        {
        pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred125_Java5265);
        t129=localVariableDeclarationStatement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred125_Java

    // $ANTLR start synpred126_Java
    public final void synpred126_Java_fragment() throws RecognitionException {
        classOrInterfaceDeclaration_return t130 =null;


        // Java.g:864:9: (t130= classOrInterfaceDeclaration )
        // Java.g:864:9: t130= classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java5281);
        t130=classOrInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred126_Java

    // $ANTLR start synpred130_Java
    public final void synpred130_Java_fragment() throws RecognitionException {
        expression_return t138 =null;

        expression_return t139 =null;


        // Java.g:884:9: ( ( 'assert' ) t138= expression ( ( ':' ) t139= expression )? ( ';' ) )
        // Java.g:884:9: ( 'assert' ) t138= expression ( ( ':' ) t139= expression )? ( ';' )
        {
        // Java.g:884:9: ( 'assert' )
        // Java.g:884:10: 'assert'
        {
        match(input,ASSERT,FOLLOW_ASSERT_in_synpred130_Java5472); if (state.failed) return ;

        }


        pushFollow(FOLLOW_expression_in_synpred130_Java5497);
        t138=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:886:136: ( ( ':' ) t139= expression )?
        int alt188=2;
        int LA188_0 = input.LA(1);

        if ( (LA188_0==COLON) ) {
            alt188=1;
        }
        switch (alt188) {
            case 1 :
                // Java.g:886:137: ( ':' ) t139= expression
                {
                // Java.g:886:137: ( ':' )
                // Java.g:886:138: ':'
                {
                match(input,COLON,FOLLOW_COLON_in_synpred130_Java5503); if (state.failed) return ;

                }


                pushFollow(FOLLOW_expression_in_synpred130_Java5511);
                t139=expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        // Java.g:886:304: ( ';' )
        // Java.g:886:305: ';'
        {
        match(input,SEMI,FOLLOW_SEMI_in_synpred130_Java5518); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred130_Java

    // $ANTLR start synpred132_Java
    public final void synpred132_Java_fragment() throws RecognitionException {
        expression_return t140 =null;

        expression_return t141 =null;


        // Java.g:887:9: ( 'assert' t140= expression ( ( ':' ) t141= expression )? ( ';' ) )
        // Java.g:887:9: 'assert' t140= expression ( ( ':' ) t141= expression )? ( ';' )
        {
        match(input,ASSERT,FOLLOW_ASSERT_in_synpred132_Java5530); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred132_Java5538);
        t140=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:887:183: ( ( ':' ) t141= expression )?
        int alt189=2;
        int LA189_0 = input.LA(1);

        if ( (LA189_0==COLON) ) {
            alt189=1;
        }
        switch (alt189) {
            case 1 :
                // Java.g:887:184: ( ':' ) t141= expression
                {
                // Java.g:887:184: ( ':' )
                // Java.g:887:185: ':'
                {
                match(input,COLON,FOLLOW_COLON_in_synpred132_Java5544); if (state.failed) return ;

                }


                pushFollow(FOLLOW_expression_in_synpred132_Java5552);
                t141=expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        // Java.g:887:351: ( ';' )
        // Java.g:887:352: ';'
        {
        match(input,SEMI,FOLLOW_SEMI_in_synpred132_Java5559); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred132_Java

    // $ANTLR start synpred133_Java
    public final void synpred133_Java_fragment() throws RecognitionException {
        statement_return t144 =null;


        // Java.g:888:306: ( 'else' t144= statement )
        // Java.g:888:306: 'else' t144= statement
        {
        match(input,ELSE,FOLLOW_ELSE_in_synpred133_Java5603); if (state.failed) return ;

        pushFollow(FOLLOW_statement_in_synpred133_Java5610);
        t144=statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred133_Java

    // $ANTLR start synpred148_Java
    public final void synpred148_Java_fragment() throws RecognitionException {
        expression_return t157 =null;


        // Java.g:903:9: (t157= expression ( ';' ) )
        // Java.g:903:9: t157= expression ( ';' )
        {
        pushFollow(FOLLOW_expression_in_synpred148_Java5944);
        t157=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:903:137: ( ';' )
        // Java.g:903:138: ';'
        {
        match(input,SEMI,FOLLOW_SEMI_in_synpred148_Java5950); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred148_Java

    // $ANTLR start synpred149_Java
    public final void synpred149_Java_fragment() throws RecognitionException {
        statement_return t158 =null;


        // Java.g:904:9: ( IDENTIFIER ( ':' ) t158= statement )
        // Java.g:904:9: IDENTIFIER ( ':' ) t158= statement
        {
        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred149_Java5967); if (state.failed) return ;

        // Java.g:904:90: ( ':' )
        // Java.g:904:91: ':'
        {
        match(input,COLON,FOLLOW_COLON_in_synpred149_Java5972); if (state.failed) return ;

        }


        pushFollow(FOLLOW_statement_in_synpred149_Java5980);
        t158=statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred149_Java

    // $ANTLR start synpred154_Java
    public final void synpred154_Java_fragment() throws RecognitionException {
        catches_return t165 =null;

        block_return t166 =null;


        // Java.g:928:13: (t165= catches 'finally' t166= block )
        // Java.g:928:13: t165= catches 'finally' t166= block
        {
        pushFollow(FOLLOW_catches_in_synpred154_Java6195);
        t165=catches();

        state._fsp--;
        if (state.failed) return ;

        match(input,FINALLY,FOLLOW_FINALLY_in_synpred154_Java6199); if (state.failed) return ;

        pushFollow(FOLLOW_block_in_synpred154_Java6206);
        t166=block();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred154_Java

    // $ANTLR start synpred155_Java
    public final void synpred155_Java_fragment() throws RecognitionException {
        catches_return t167 =null;


        // Java.g:929:13: (t167= catches )
        // Java.g:929:13: t167= catches
        {
        pushFollow(FOLLOW_catches_in_synpred155_Java6226);
        t167=catches();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred155_Java

    // $ANTLR start synpred164_Java
    public final void synpred164_Java_fragment() throws RecognitionException {
        variableModifiers_return t184 =null;

        type_return t185 =null;

        expression_return t186 =null;

        statement_return t187 =null;


        // Java.g:978:9: ( 'for' ( '(' ) t184= variableModifiers t185= type IDENTIFIER ( ( '[' ) ( ']' ) )* ( ':' ) t186= expression ( ')' ) t187= statement )
        // Java.g:978:9: 'for' ( '(' ) t184= variableModifiers t185= type IDENTIFIER ( ( '[' ) ( ']' ) )* ( ':' ) t186= expression ( ')' ) t187= statement
        {
        match(input,FOR,FOLLOW_FOR_in_synpred164_Java6712); if (state.failed) return ;

        // Java.g:978:49: ( '(' )
        // Java.g:978:50: '('
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred164_Java6716); if (state.failed) return ;

        }


        pushFollow(FOLLOW_variableModifiers_in_synpred164_Java6724);
        t184=variableModifiers();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_type_in_synpred164_Java6732);
        t185=type();

        state._fsp--;
        if (state.failed) return ;

        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred164_Java6736); if (state.failed) return ;

        // Java.g:978:424: ( ( '[' ) ( ']' ) )*
        loop194:
        do {
            int alt194=2;
            int LA194_0 = input.LA(1);

            if ( (LA194_0==LBRACKET) ) {
                alt194=1;
            }


            switch (alt194) {
        	case 1 :
        	    // Java.g:978:425: ( '[' ) ( ']' )
        	    {
        	    // Java.g:978:425: ( '[' )
        	    // Java.g:978:426: '['
        	    {
        	    match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred164_Java6742); if (state.failed) return ;

        	    }


        	    // Java.g:978:463: ( ']' )
        	    // Java.g:978:464: ']'
        	    {
        	    match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred164_Java6747); if (state.failed) return ;

        	    }


        	    }
        	    break;

        	default :
        	    break loop194;
            }
        } while (true);


        // Java.g:978:503: ( ':' )
        // Java.g:978:504: ':'
        {
        match(input,COLON,FOLLOW_COLON_in_synpred164_Java6754); if (state.failed) return ;

        }


        pushFollow(FOLLOW_expression_in_synpred164_Java6770);
        t186=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:979:136: ( ')' )
        // Java.g:979:137: ')'
        {
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred164_Java6775); if (state.failed) return ;

        }


        pushFollow(FOLLOW_statement_in_synpred164_Java6783);
        t187=statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred164_Java

    // $ANTLR start synpred168_Java
    public final void synpred168_Java_fragment() throws RecognitionException {
        localVariableDeclaration_return t192 =null;


        // Java.g:992:9: (t192= localVariableDeclaration )
        // Java.g:992:9: t192= localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred168_Java7005);
        t192=localVariableDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred168_Java

    // $ANTLR start synpred209_Java
    public final void synpred209_Java_fragment() throws RecognitionException {
        castExpression_return t234 =null;


        // Java.g:1146:9: (t234= castExpression )
        // Java.g:1146:9: t234= castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred209_Java8640);
        t234=castExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred209_Java

    // $ANTLR start synpred213_Java
    public final void synpred213_Java_fragment() throws RecognitionException {
        primitiveType_return t237 =null;

        unaryExpression_return t238 =null;


        // Java.g:1156:9: ( ( '(' ) t237= primitiveType ( ')' ) t238= unaryExpression )
        // Java.g:1156:9: ( '(' ) t237= primitiveType ( ')' ) t238= unaryExpression
        {
        // Java.g:1156:9: ( '(' )
        // Java.g:1156:10: '('
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred213_Java8748); if (state.failed) return ;

        }


        pushFollow(FOLLOW_primitiveType_in_synpred213_Java8756);
        t237=primitiveType();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:1156:180: ( ')' )
        // Java.g:1156:181: ')'
        {
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred213_Java8761); if (state.failed) return ;

        }


        pushFollow(FOLLOW_unaryExpression_in_synpred213_Java8769);
        t238=unaryExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred213_Java

    // $ANTLR start synpred215_Java
    public final void synpred215_Java_fragment() throws RecognitionException {
        // Java.g:1166:10: ( ( '.' ) IDENTIFIER )
        // Java.g:1166:10: ( '.' ) IDENTIFIER
        {
        // Java.g:1166:10: ( '.' )
        // Java.g:1166:11: '.'
        {
        match(input,DOT,FOLLOW_DOT_in_synpred215_Java8868); if (state.failed) return ;

        }


        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred215_Java8872); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred215_Java

    // $ANTLR start synpred216_Java
    public final void synpred216_Java_fragment() throws RecognitionException {
        identifierSuffix_return t242 =null;


        // Java.g:1168:10: (t242= identifierSuffix )
        // Java.g:1168:10: t242= identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred216_Java8900);
        t242=identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred216_Java

    // $ANTLR start synpred218_Java
    public final void synpred218_Java_fragment() throws RecognitionException {
        // Java.g:1171:10: ( ( '.' ) IDENTIFIER )
        // Java.g:1171:10: ( '.' ) IDENTIFIER
        {
        // Java.g:1171:10: ( '.' )
        // Java.g:1171:11: '.'
        {
        match(input,DOT,FOLLOW_DOT_in_synpred218_Java8937); if (state.failed) return ;

        }


        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred218_Java8941); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred218_Java

    // $ANTLR start synpred219_Java
    public final void synpred219_Java_fragment() throws RecognitionException {
        identifierSuffix_return t243 =null;


        // Java.g:1173:10: (t243= identifierSuffix )
        // Java.g:1173:10: t243= identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred219_Java8969);
        t243=identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred219_Java

    // $ANTLR start synpred231_Java
    public final void synpred231_Java_fragment() throws RecognitionException {
        expression_return t251 =null;


        // Java.g:1201:10: ( ( '[' ) t251= expression ( ']' ) )
        // Java.g:1201:10: ( '[' ) t251= expression ( ']' )
        {
        // Java.g:1201:10: ( '[' )
        // Java.g:1201:11: '['
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred231_Java9296); if (state.failed) return ;

        }


        pushFollow(FOLLOW_expression_in_synpred231_Java9304);
        t251=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:1201:175: ( ']' )
        // Java.g:1201:176: ']'
        {
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred231_Java9309); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred231_Java

    // $ANTLR start synpred246_Java
    public final void synpred246_Java_fragment() throws RecognitionException {
        nonWildcardTypeArguments_return t265 =null;

        classOrInterfaceType_return t266 =null;

        classCreatorRest_return t267 =null;


        // Java.g:1238:9: ( 'new' t265= nonWildcardTypeArguments t266= classOrInterfaceType t267= classCreatorRest )
        // Java.g:1238:9: 'new' t265= nonWildcardTypeArguments t266= classOrInterfaceType t267= classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred246_Java9729); if (state.failed) return ;

        pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred246_Java9736);
        t265=nonWildcardTypeArguments();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classOrInterfaceType_in_synpred246_Java9744);
        t266=classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classCreatorRest_in_synpred246_Java9752);
        t267=classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred246_Java

    // $ANTLR start synpred247_Java
    public final void synpred247_Java_fragment() throws RecognitionException {
        classOrInterfaceType_return t268 =null;

        classCreatorRest_return t269 =null;


        // Java.g:1239:9: ( 'new' t268= classOrInterfaceType t269= classCreatorRest )
        // Java.g:1239:9: 'new' t268= classOrInterfaceType t269= classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred247_Java9764); if (state.failed) return ;

        pushFollow(FOLLOW_classOrInterfaceType_in_synpred247_Java9771);
        t268=classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classCreatorRest_in_synpred247_Java9779);
        t269=classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred247_Java

    // $ANTLR start synpred249_Java
    public final void synpred249_Java_fragment() throws RecognitionException {
        createdName_return t271 =null;

        arrayInitializer_return t272 =null;


        // Java.g:1244:9: ( 'new' t271= createdName ( '[' ) ( ']' ) ( ( '[' ) ( ']' ) )* t272= arrayInitializer )
        // Java.g:1244:9: 'new' t271= createdName ( '[' ) ( ']' ) ( ( '[' ) ( ']' ) )* t272= arrayInitializer
        {
        match(input,NEW,FOLLOW_NEW_in_synpred249_Java9817); if (state.failed) return ;

        pushFollow(FOLLOW_createdName_in_synpred249_Java9824);
        t271=createdName();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:1245:9: ( '[' )
        // Java.g:1245:10: '['
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred249_Java9837); if (state.failed) return ;

        }


        // Java.g:1245:47: ( ']' )
        // Java.g:1245:48: ']'
        {
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred249_Java9842); if (state.failed) return ;

        }


        // Java.g:1246:9: ( ( '[' ) ( ']' ) )*
        loop203:
        do {
            int alt203=2;
            int LA203_0 = input.LA(1);

            if ( (LA203_0==LBRACKET) ) {
                alt203=1;
            }


            switch (alt203) {
        	case 1 :
        	    // Java.g:1246:10: ( '[' ) ( ']' )
        	    {
        	    // Java.g:1246:10: ( '[' )
        	    // Java.g:1246:11: '['
        	    {
        	    match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred249_Java9856); if (state.failed) return ;

        	    }


        	    // Java.g:1246:48: ( ']' )
        	    // Java.g:1246:49: ']'
        	    {
        	    match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred249_Java9861); if (state.failed) return ;

        	    }


        	    }
        	    break;

        	default :
        	    break loop203;
            }
        } while (true);


        pushFollow(FOLLOW_arrayInitializer_in_synpred249_Java9888);
        t272=arrayInitializer();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred249_Java

    // $ANTLR start synpred250_Java
    public final void synpred250_Java_fragment() throws RecognitionException {
        expression_return t275 =null;


        // Java.g:1253:13: ( ( '[' ) t275= expression ( ']' ) )
        // Java.g:1253:13: ( '[' ) t275= expression ( ']' )
        {
        // Java.g:1253:13: ( '[' )
        // Java.g:1253:14: '['
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred250_Java9959); if (state.failed) return ;

        }


        pushFollow(FOLLOW_expression_in_synpred250_Java9967);
        t275=expression();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:1254:13: ( ']' )
        // Java.g:1254:14: ']'
        {
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred250_Java9984); if (state.failed) return ;

        }


        }

    }
    // $ANTLR end synpred250_Java

    // Delegated rules

    public final boolean synpred43_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred43_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred98_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred98_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred121_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred121_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred249_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred249_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred168_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred168_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred69_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred69_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred154_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred154_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred71_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred71_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred133_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred133_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred125_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred125_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred132_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred132_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred119_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred119_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred246_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred246_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred219_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred219_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred215_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred215_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred218_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred218_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred54_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred148_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred148_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred117_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred117_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred130_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred130_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred126_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred126_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred231_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred231_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred59_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred59_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred57_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred57_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred209_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred209_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred155_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred155_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred213_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred213_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred68_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred68_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred216_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred216_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred52_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred52_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred247_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred247_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred149_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred149_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred120_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred120_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred122_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred122_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred70_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred70_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred27_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred27_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred96_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred96_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred99_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred99_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred250_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred250_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred103_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred103_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred164_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred164_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred118_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred118_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA42 dfa42 = new DFA42(this);
    protected DFA93 dfa93 = new DFA93(this);
    protected DFA106 dfa106 = new DFA106(this);
    protected DFA180 dfa180 = new DFA180(this);
    static final String DFA42_eotS =
        "\75\uffff";
    static final String DFA42_eofS =
        "\75\uffff";
    static final String DFA42_minS =
        "\1\4\1\uffff\30\0\43\uffff";
    static final String DFA42_maxS =
        "\1\170\1\uffff\30\0\43\uffff";
    static final String DFA42_acceptS =
        "\1\uffff\1\1\30\uffff\1\2\42\uffff";
    static final String DFA42_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\43\uffff}>";
    static final String[] DFA42_transitionS = {
            "\1\32\3\uffff\2\32\4\uffff\1\10\1\21\1\32\1\23\5\uffff\1\22"+
            "\1\13\1\32\4\uffff\1\32\1\uffff\1\32\1\uffff\1\30\1\12\3\uffff"+
            "\1\32\5\uffff\1\16\1\32\1\uffff\1\27\1\11\1\32\6\uffff\1\5\1"+
            "\32\3\uffff\1\25\1\32\1\6\3\uffff\1\32\2\uffff\1\26\1\7\1\4"+
            "\1\1\1\uffff\2\32\1\20\1\17\4\uffff\1\32\1\uffff\4\32\1\uffff"+
            "\1\32\1\uffff\1\32\1\uffff\1\32\1\24\4\uffff\2\32\1\14\1\32"+
            "\1\uffff\1\32\1\3\2\32\1\uffff\1\2\1\32\1\uffff\2\32\1\15\1"+
            "\32\1\31\2\32",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA42_eot = DFA.unpackEncodedString(DFA42_eotS);
    static final short[] DFA42_eof = DFA.unpackEncodedString(DFA42_eofS);
    static final char[] DFA42_min = DFA.unpackEncodedStringToUnsignedChars(DFA42_minS);
    static final char[] DFA42_max = DFA.unpackEncodedStringToUnsignedChars(DFA42_maxS);
    static final short[] DFA42_accept = DFA.unpackEncodedString(DFA42_acceptS);
    static final short[] DFA42_special = DFA.unpackEncodedString(DFA42_specialS);
    static final short[][] DFA42_transition;

    static {
        int numStates = DFA42_transitionS.length;
        DFA42_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA42_transition[i] = DFA.unpackEncodedString(DFA42_transitionS[i]);
        }
    }

    class DFA42 extends DFA {

        public DFA42(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 42;
            this.eot = DFA42_eot;
            this.eof = DFA42_eof;
            this.min = DFA42_min;
            this.max = DFA42_max;
            this.accept = DFA42_accept;
            this.special = DFA42_special;
            this.transition = DFA42_transition;
        }
        public String getDescription() {
            return "584:9: (t53= explicitConstructorInvocation )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA42_2 = input.LA(1);

                         
                        int index42_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_2);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA42_3 = input.LA(1);

                         
                        int index42_3 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_3);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA42_4 = input.LA(1);

                         
                        int index42_4 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_4);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA42_5 = input.LA(1);

                         
                        int index42_5 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_5);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA42_6 = input.LA(1);

                         
                        int index42_6 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_6);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA42_7 = input.LA(1);

                         
                        int index42_7 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_7);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA42_8 = input.LA(1);

                         
                        int index42_8 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_8);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA42_9 = input.LA(1);

                         
                        int index42_9 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_9);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA42_10 = input.LA(1);

                         
                        int index42_10 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_10);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA42_11 = input.LA(1);

                         
                        int index42_11 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_11);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA42_12 = input.LA(1);

                         
                        int index42_12 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_12);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA42_13 = input.LA(1);

                         
                        int index42_13 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_13);

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA42_14 = input.LA(1);

                         
                        int index42_14 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_14);

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA42_15 = input.LA(1);

                         
                        int index42_15 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_15);

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA42_16 = input.LA(1);

                         
                        int index42_16 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_16);

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA42_17 = input.LA(1);

                         
                        int index42_17 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_17);

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA42_18 = input.LA(1);

                         
                        int index42_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_18);

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA42_19 = input.LA(1);

                         
                        int index42_19 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_19);

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA42_20 = input.LA(1);

                         
                        int index42_20 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_20);

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA42_21 = input.LA(1);

                         
                        int index42_21 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_21);

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA42_22 = input.LA(1);

                         
                        int index42_22 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_22);

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA42_23 = input.LA(1);

                         
                        int index42_23 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_23);

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA42_24 = input.LA(1);

                         
                        int index42_24 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_24);

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA42_25 = input.LA(1);

                         
                        int index42_25 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index42_25);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 42, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA93_eotS =
        "\74\uffff";
    static final String DFA93_eofS =
        "\74\uffff";
    static final String DFA93_minS =
        "\1\4\13\0\6\uffff\1\0\51\uffff";
    static final String DFA93_maxS =
        "\1\170\13\0\6\uffff\1\0\51\uffff";
    static final String DFA93_acceptS =
        "\14\uffff\1\2\14\uffff\1\3\41\uffff\1\1";
    static final String DFA93_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\6\uffff"+
        "\1\13\51\uffff}>";
    static final String[] DFA93_transitionS = {
            "\1\14\3\uffff\2\31\4\uffff\1\31\1\4\1\31\1\6\5\uffff\1\5\1\31"+
            "\1\14\4\uffff\1\31\1\uffff\1\31\1\uffff\1\13\1\31\3\uffff\1"+
            "\14\5\uffff\1\31\1\1\1\uffff\1\12\2\31\6\uffff\1\3\1\31\3\uffff"+
            "\1\10\1\14\1\31\3\uffff\1\31\2\uffff\1\11\2\31\2\uffff\1\2\1"+
            "\14\2\31\4\uffff\1\31\1\uffff\1\31\3\14\3\uffff\1\31\1\uffff"+
            "\1\31\1\7\4\uffff\2\14\2\31\1\uffff\3\31\1\22\1\uffff\2\31\1"+
            "\uffff\1\31\1\14\3\31\1\14\1\31",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA93_eot = DFA.unpackEncodedString(DFA93_eotS);
    static final short[] DFA93_eof = DFA.unpackEncodedString(DFA93_eofS);
    static final char[] DFA93_min = DFA.unpackEncodedStringToUnsignedChars(DFA93_minS);
    static final char[] DFA93_max = DFA.unpackEncodedStringToUnsignedChars(DFA93_maxS);
    static final short[] DFA93_accept = DFA.unpackEncodedString(DFA93_acceptS);
    static final short[] DFA93_special = DFA.unpackEncodedString(DFA93_specialS);
    static final short[][] DFA93_transition;

    static {
        int numStates = DFA93_transitionS.length;
        DFA93_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA93_transition[i] = DFA.unpackEncodedString(DFA93_transitionS[i]);
        }
    }

    class DFA93 extends DFA {

        public DFA93(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 93;
            this.eot = DFA93_eot;
            this.eof = DFA93_eof;
            this.min = DFA93_min;
            this.max = DFA93_max;
            this.accept = DFA93_accept;
            this.special = DFA93_special;
            this.transition = DFA93_transition;
        }
        public String getDescription() {
            return "862:1: blockStatement : (t129= localVariableDeclarationStatement |t130= classOrInterfaceDeclaration |t131= statement );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA93_1 = input.LA(1);

                         
                        int index93_1 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (synpred126_Java()) ) {s = 12;}

                         
                        input.seek(index93_1);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA93_2 = input.LA(1);

                         
                        int index93_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (synpred126_Java()) ) {s = 12;}

                         
                        input.seek(index93_2);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA93_3 = input.LA(1);

                         
                        int index93_3 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_3);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA93_4 = input.LA(1);

                         
                        int index93_4 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_4);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA93_5 = input.LA(1);

                         
                        int index93_5 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_5);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA93_6 = input.LA(1);

                         
                        int index93_6 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_6);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA93_7 = input.LA(1);

                         
                        int index93_7 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_7);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA93_8 = input.LA(1);

                         
                        int index93_8 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_8);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA93_9 = input.LA(1);

                         
                        int index93_9 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_9);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA93_10 = input.LA(1);

                         
                        int index93_10 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_10);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA93_11 = input.LA(1);

                         
                        int index93_11 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred125_Java()) ) {s = 59;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_11);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA93_18 = input.LA(1);

                         
                        int index93_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred126_Java()) ) {s = 12;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index93_18);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 93, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA106_eotS =
        "\104\uffff";
    static final String DFA106_eofS =
        "\1\3\103\uffff";
    static final String DFA106_minS =
        "\1\4\1\0\102\uffff";
    static final String DFA106_maxS =
        "\1\170\1\0\102\uffff";
    static final String DFA106_acceptS =
        "\2\uffff\1\3\1\4\76\uffff\1\1\1\2";
    static final String DFA106_specialS =
        "\1\uffff\1\0\102\uffff}>";
    static final String[] DFA106_transitionS = {
            "\1\3\3\uffff\2\3\4\uffff\4\3\3\uffff\1\3\1\1\3\3\4\uffff\3\3"+
            "\1\uffff\2\3\2\uffff\2\3\5\uffff\2\3\1\2\3\3\6\uffff\2\3\3\uffff"+
            "\3\3\3\uffff\1\3\2\uffff\3\3\2\uffff\4\3\4\uffff\1\3\1\uffff"+
            "\4\3\1\uffff\1\3\1\uffff\1\3\1\uffff\2\3\4\uffff\4\3\1\uffff"+
            "\4\3\1\uffff\2\3\1\uffff\7\3",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA106_eot = DFA.unpackEncodedString(DFA106_eotS);
    static final short[] DFA106_eof = DFA.unpackEncodedString(DFA106_eofS);
    static final char[] DFA106_min = DFA.unpackEncodedStringToUnsignedChars(DFA106_minS);
    static final char[] DFA106_max = DFA.unpackEncodedStringToUnsignedChars(DFA106_maxS);
    static final short[] DFA106_accept = DFA.unpackEncodedString(DFA106_acceptS);
    static final short[] DFA106_special = DFA.unpackEncodedString(DFA106_specialS);
    static final short[][] DFA106_transition;

    static {
        int numStates = DFA106_transitionS.length;
        DFA106_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA106_transition[i] = DFA.unpackEncodedString(DFA106_transitionS[i]);
        }
    }

    class DFA106 extends DFA {

        public DFA106(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 106;
            this.eot = DFA106_eot;
            this.eof = DFA106_eof;
            this.min = DFA106_min;
            this.max = DFA106_max;
            this.accept = DFA106_accept;
            this.special = DFA106_special;
            this.transition = DFA106_transition;
        }
        public String getDescription() {
            return "928:9: (t165= catches 'finally' t166= block |t167= catches | 'finally' t168= block )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA106_1 = input.LA(1);

                         
                        int index106_1 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred154_Java()) ) {s = 66;}

                        else if ( (synpred155_Java()) ) {s = 67;}

                         
                        input.seek(index106_1);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 106, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA180_eotS =
        "\75\uffff";
    static final String DFA180_eofS =
        "\75\uffff";
    static final String DFA180_minS =
        "\1\4\1\uffff\30\0\43\uffff";
    static final String DFA180_maxS =
        "\1\170\1\uffff\30\0\43\uffff";
    static final String DFA180_acceptS =
        "\1\uffff\1\1\30\uffff\1\2\42\uffff";
    static final String DFA180_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\43\uffff}>";
    static final String[] DFA180_transitionS = {
            "\1\32\3\uffff\2\32\4\uffff\1\10\1\21\1\32\1\23\5\uffff\1\22"+
            "\1\13\1\32\4\uffff\1\32\1\uffff\1\32\1\uffff\1\30\1\12\3\uffff"+
            "\1\32\5\uffff\1\16\1\32\1\uffff\1\27\1\11\1\32\6\uffff\1\5\1"+
            "\32\3\uffff\1\25\1\32\1\6\3\uffff\1\32\2\uffff\1\26\1\7\1\4"+
            "\1\1\1\uffff\2\32\1\20\1\17\4\uffff\1\32\1\uffff\4\32\1\uffff"+
            "\1\32\1\uffff\1\32\1\uffff\1\32\1\24\4\uffff\2\32\1\14\1\32"+
            "\1\uffff\1\32\1\3\2\32\1\uffff\1\2\1\32\1\uffff\2\32\1\15\1"+
            "\32\1\31\2\32",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA180_eot = DFA.unpackEncodedString(DFA180_eotS);
    static final short[] DFA180_eof = DFA.unpackEncodedString(DFA180_eofS);
    static final char[] DFA180_min = DFA.unpackEncodedStringToUnsignedChars(DFA180_minS);
    static final char[] DFA180_max = DFA.unpackEncodedStringToUnsignedChars(DFA180_maxS);
    static final short[] DFA180_accept = DFA.unpackEncodedString(DFA180_acceptS);
    static final short[] DFA180_special = DFA.unpackEncodedString(DFA180_specialS);
    static final short[][] DFA180_transition;

    static {
        int numStates = DFA180_transitionS.length;
        DFA180_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA180_transition[i] = DFA.unpackEncodedString(DFA180_transitionS[i]);
        }
    }

    class DFA180 extends DFA {

        public DFA180(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 180;
            this.eot = DFA180_eot;
            this.eof = DFA180_eof;
            this.min = DFA180_min;
            this.max = DFA180_max;
            this.accept = DFA180_accept;
            this.special = DFA180_special;
            this.transition = DFA180_transition;
        }
        public String getDescription() {
            return "584:9: (t53= explicitConstructorInvocation )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA180_2 = input.LA(1);

                         
                        int index180_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_2);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA180_3 = input.LA(1);

                         
                        int index180_3 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_3);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA180_4 = input.LA(1);

                         
                        int index180_4 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_4);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA180_5 = input.LA(1);

                         
                        int index180_5 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_5);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA180_6 = input.LA(1);

                         
                        int index180_6 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_6);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA180_7 = input.LA(1);

                         
                        int index180_7 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_7);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA180_8 = input.LA(1);

                         
                        int index180_8 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_8);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA180_9 = input.LA(1);

                         
                        int index180_9 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_9);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA180_10 = input.LA(1);

                         
                        int index180_10 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_10);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA180_11 = input.LA(1);

                         
                        int index180_11 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_11);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA180_12 = input.LA(1);

                         
                        int index180_12 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_12);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA180_13 = input.LA(1);

                         
                        int index180_13 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_13);

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA180_14 = input.LA(1);

                         
                        int index180_14 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_14);

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA180_15 = input.LA(1);

                         
                        int index180_15 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_15);

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA180_16 = input.LA(1);

                         
                        int index180_16 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_16);

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA180_17 = input.LA(1);

                         
                        int index180_17 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_17);

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA180_18 = input.LA(1);

                         
                        int index180_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_18);

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA180_19 = input.LA(1);

                         
                        int index180_19 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_19);

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA180_20 = input.LA(1);

                         
                        int index180_20 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_20);

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA180_21 = input.LA(1);

                         
                        int index180_21 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_21);

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA180_22 = input.LA(1);

                         
                        int index180_22 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_22);

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA180_23 = input.LA(1);

                         
                        int index180_23 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_23);

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA180_24 = input.LA(1);

                         
                        int index180_24 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_24);

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA180_25 = input.LA(1);

                         
                        int index180_25 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred57_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index180_25);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 180, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

    public static final BitSet FOLLOW_annotations_in_compilationUnit97 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit132 = new BitSet(new long[]{0x9000408002000012L,0x0088206083803000L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit160 = new BitSet(new long[]{0x9000408002000012L,0x0088206083803000L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit188 = new BitSet(new long[]{0x8000408002000012L,0x0088206083803000L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDeclaration221 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration228 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDeclaration241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration264 = new BitSet(new long[]{0x0200000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration277 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration299 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration304 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration309 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration341 = new BitSet(new long[]{0x0200000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration354 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration376 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration390 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration394 = new BitSet(new long[]{0x0000000200000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration419 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration424 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName470 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_DOT_in_qualifiedImportName484 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName488 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_typeDeclaration538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifiers622 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_PUBLIC_in_modifiers634 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_PROTECTED_in_modifiers645 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_PRIVATE_in_modifiers656 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_STATIC_in_modifiers667 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_ABSTRACT_in_modifiers678 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_FINAL_in_modifiers689 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_NATIVE_in_modifiers700 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_modifiers711 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_TRANSIENT_in_modifiers722 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_VOLATILE_in_modifiers733 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_STRICTFP_in_modifiers744 = new BitSet(new long[]{0x0000400000000012L,0x0088206003803000L});
    public static final BitSet FOLLOW_FINAL_in_variableModifiers783 = new BitSet(new long[]{0x0000400000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_annotation_in_variableModifiers802 = new BitSet(new long[]{0x0000400000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalClassDeclaration886 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_in_normalClassDeclaration891 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalClassDeclaration894 = new BitSet(new long[]{0x0800040000000000L,0x0000000000000410L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration911 = new BitSet(new long[]{0x0800040000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_EXTENDS_in_normalClassDeclaration935 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration942 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_normalClassDeclaration966 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration973 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration1012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeParameters1036 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters1056 = new BitSet(new long[]{0x0020000008000000L});
    public static final BitSet FOLLOW_COMMA_in_typeParameters1074 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters1082 = new BitSet(new long[]{0x0020000008000000L});
    public static final BitSet FOLLOW_GT_in_typeParameters1110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_typeParameter1132 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_typeParameter1145 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound1190 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AMP_in_typeBound1204 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_typeBound1212 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_modifiers_in_enumDeclaration1250 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration1264 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumDeclaration1286 = new BitSet(new long[]{0x0800000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_enumDeclaration1299 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration1306 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration1333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_enumBody1361 = new BitSet(new long[]{0x0200000008000000L,0x0000000088001000L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody1378 = new BitSet(new long[]{0x0000000008000000L,0x0000000088000000L});
    public static final BitSet FOLLOW_COMMA_in_enumBody1403 = new BitSet(new long[]{0x0000000000000000L,0x0000000088000000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody1422 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_enumBody1447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants1473 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_enumConstants1487 = new BitSet(new long[]{0x0200000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants1495 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant1535 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumConstant1558 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000210L});
    public static final BitSet FOLLOW_arguments_in_enumConstant1575 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_classBody_in_enumConstant1603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_enumBodyDeclarations1647 = new BitSet(new long[]{0xC201408402828012L,0x00C8206183803490L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1665 = new BitSet(new long[]{0xC201408402828012L,0x00C8206183803490L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalInterfaceDeclaration1748 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_normalInterfaceDeclaration1752 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1755 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000410L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration1772 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_EXTENDS_in_normalInterfaceDeclaration1796 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration1803 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration1830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList1856 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_typeList1870 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_typeList1878 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_LBRACE_in_classBody1912 = new BitSet(new long[]{0xC201408402828010L,0x00C820618B803490L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody1930 = new BitSet(new long[]{0xC201408402828010L,0x00C820618B803490L});
    public static final BitSet FOLLOW_RBRACE_in_classBody1955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_interfaceBody1978 = new BitSet(new long[]{0xC201408402828010L,0x00C820618B803480L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody1996 = new BitSet(new long[]{0xC201408402828010L,0x00C820618B803480L});
    public static final BitSet FOLLOW_RBRACE_in_interfaceBody2021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_classBodyDeclaration2044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATIC_in_classBodyDeclaration2057 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration2084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration2100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDecl2127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDecl2144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl2161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl2178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration2222 = new BitSet(new long[]{0x0200000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration2239 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration2262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration2278 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000010L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration2291 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration2298 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_LBRACE_in_methodDeclaration2322 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F791L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_methodDeclaration2340 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_blockStatement_in_methodDeclaration2368 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_RBRACE_in_methodDeclaration2392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration2408 = new BitSet(new long[]{0x4201000400828000L,0x0040000100000480L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration2425 = new BitSet(new long[]{0x4201000400828000L,0x0040000100000080L});
    public static final BitSet FOLLOW_type_in_methodDeclaration2453 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_VOID_in_methodDeclaration2469 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration2490 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration2506 = new BitSet(new long[]{0x0000000000000000L,0x0002000080000030L});
    public static final BitSet FOLLOW_LBRACKET_in_methodDeclaration2520 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_methodDeclaration2525 = new BitSet(new long[]{0x0000000000000000L,0x0002000080000030L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration2549 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration2556 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000010L});
    public static final BitSet FOLLOW_block_in_methodDeclaration2617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_methodDeclaration2634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_fieldDeclaration2672 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_fieldDeclaration2688 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2704 = new BitSet(new long[]{0x0000000008000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldDeclaration2718 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2726 = new BitSet(new long[]{0x0000000008000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_fieldDeclaration2750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableDeclarator2772 = new BitSet(new long[]{0x0000010000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_variableDeclarator2786 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_variableDeclarator2791 = new BitSet(new long[]{0x0000010000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_EQ_in_variableDeclarator2816 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C391L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator2824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration2869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration2885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration2901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceBodyDeclaration2917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_interfaceBodyDeclaration2930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceMethodDeclaration2956 = new BitSet(new long[]{0x4201000400828000L,0x0040000100000480L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceMethodDeclaration2973 = new BitSet(new long[]{0x4201000400828000L,0x0040000100000080L});
    public static final BitSet FOLLOW_type_in_interfaceMethodDeclaration3001 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_VOID_in_interfaceMethodDeclaration3014 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration3035 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaration3051 = new BitSet(new long[]{0x0000000000000000L,0x0002000080000020L});
    public static final BitSet FOLLOW_LBRACKET_in_interfaceMethodDeclaration3065 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_interfaceMethodDeclaration3070 = new BitSet(new long[]{0x0000000000000000L,0x0002000080000020L});
    public static final BitSet FOLLOW_THROWS_in_interfaceMethodDeclaration3094 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration3101 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceMethodDeclaration3117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceFieldDeclaration3145 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_interfaceFieldDeclaration3153 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3161 = new BitSet(new long[]{0x0000000008000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COMMA_in_interfaceFieldDeclaration3175 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3183 = new BitSet(new long[]{0x0000000008000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceFieldDeclaration3207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type3234 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_type3248 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_type3253 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_primitiveType_in_type3280 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_type3294 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_type3299 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType3333 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_typeArgumentsOrDiamond_in_classOrInterfaceType3350 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_DOT_in_classOrInterfaceType3375 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType3379 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_typeArgumentsOrDiamond_in_classOrInterfaceType3400 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_BOOLEAN_in_primitiveType3449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_in_primitiveType3460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BYTE_in_primitiveType3471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SHORT_in_primitiveType3482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_primitiveType3493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONG_in_primitiveType3504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_primitiveType3515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_primitiveType3526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeArguments3548 = new BitSet(new long[]{0x4201000400828000L,0x0000000104000080L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments3556 = new BitSet(new long[]{0x0020000008000000L});
    public static final BitSet FOLLOW_COMMA_in_typeArguments3570 = new BitSet(new long[]{0x4201000400828000L,0x0000000104000080L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments3578 = new BitSet(new long[]{0x0020000008000000L});
    public static final BitSet FOLLOW_GT_in_typeArguments3603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument3629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_typeArgument3642 = new BitSet(new long[]{0x0000040000000002L,0x0000080000000000L});
    public static final BitSet FOLLOW_EXTENDS_in_typeArgument3669 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_SUPER_in_typeArgument3685 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_typeArgument3718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3755 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_qualifiedNameList3769 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3777 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3811 = new BitSet(new long[]{0x4201400400828000L,0x0000000140001080L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters3828 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3895 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3909 = new BitSet(new long[]{0x4201400400828000L,0x0000000100001080L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3917 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3945 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3958 = new BitSet(new long[]{0x4201400400828000L,0x0000000100001080L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_normalParameterDecl4012 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_normalParameterDecl4020 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalParameterDecl4024 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_normalParameterDecl4038 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_normalParameterDecl4043 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_variableModifiers_in_ellipsisParameterDecl4080 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_ellipsisParameterDecl4096 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_ELLIPSIS_in_ellipsisParameterDecl4101 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_ellipsisParameterDecl4112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4140 = new BitSet(new long[]{0x0000000000000000L,0x0000880000000000L});
    public static final BitSet FOLLOW_THIS_in_explicitConstructorInvocation4169 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_SUPER_in_explicitConstructorInvocation4181 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation4206 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation4211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation4228 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_DOT_in_explicitConstructorInvocation4241 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4258 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_SUPER_in_explicitConstructorInvocation4281 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation4296 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation4301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName4323 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_DOT_in_qualifiedName4337 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName4341 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_annotation_in_annotations4379 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_annotation4415 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_annotation4423 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_annotation4440 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D814050D391L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation4473 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_elementValue_in_annotation4503 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_annotation4542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs4580 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_elementValuePairs4594 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs4602 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_elementValuePair4635 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_elementValuePair4640 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050D391L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair4648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue4674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue4690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue4706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_elementValueArrayInitializer4729 = new BitSet(new long[]{0x4203200C0982C200L,0x00548D810850D391L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer4746 = new BitSet(new long[]{0x0000000008000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer4764 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050D391L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer4772 = new BitSet(new long[]{0x0000000008000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer4804 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_elementValueArrayInitializer4811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeDeclaration4840 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration4845 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_annotationTypeDeclaration4857 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationTypeDeclaration4868 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration4884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_annotationTypeBody4908 = new BitSet(new long[]{0xC201408402828010L,0x008820618B803080L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody4926 = new BitSet(new long[]{0xC201408402828010L,0x008820618B803080L});
    public static final BitSet FOLLOW_RBRACE_in_annotationTypeBody4951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration4979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration4995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration5011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration5027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration5043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration5059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_annotationTypeElementDeclaration5072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationMethodDeclaration5098 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_annotationMethodDeclaration5106 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationMethodDeclaration5110 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_annotationMethodDeclaration5123 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_annotationMethodDeclaration5128 = new BitSet(new long[]{0x0000000080000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_DEFAULT_in_annotationMethodDeclaration5133 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050D391L});
    public static final BitSet FOLLOW_elementValue_in_annotationMethodDeclaration5140 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_annotationMethodDeclaration5172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block5199 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_blockStatement_in_block5216 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_RBRACE_in_block5240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement5265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement5281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement5297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement5324 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_localVariableDeclarationStatement5337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration5363 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration5371 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration5387 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_localVariableDeclaration5401 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration5409 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_block_in_statement5446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement5472 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_statement5497 = new BitSet(new long[]{0x0000000004000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COLON_in_statement5503 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_statement5511 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement5530 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_statement5538 = new BitSet(new long[]{0x0000000004000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COLON_in_statement5544 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_statement5552 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_statement5583 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_parExpression_in_statement5590 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_statement5598 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_ELSE_in_statement5603 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_statement5610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forstatement_in_statement5638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement5650 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_parExpression_in_statement5657 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_statement5665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_statement5677 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_statement5684 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_WHILE_in_statement5688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_parExpression_in_statement5695 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trystatement_in_statement5716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SWITCH_in_statement5728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_parExpression_in_statement5735 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_LBRACE_in_statement5740 = new BitSet(new long[]{0x0000000080200000L,0x0000000008000000L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement5748 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_statement5753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_statement5765 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_parExpression_in_statement5772 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_statement5780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_statement5792 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D818050C381L});
    public static final BitSet FOLLOW_expression_in_statement5800 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement5820 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_statement5827 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement5844 = new BitSet(new long[]{0x0200000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5860 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement5892 = new BitSet(new long[]{0x0200000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5908 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statement5944 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5967 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_statement5972 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_statement5980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement5993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups6021 = new BitSet(new long[]{0x0000000080200002L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup6056 = new BitSet(new long[]{0xC607608D4383C312L,0x01FDBDE1A3D0F391L});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup6073 = new BitSet(new long[]{0xC607608D4383C312L,0x01FDBDE1A3D0F391L});
    public static final BitSet FOLLOW_CASE_in_switchLabel6106 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_switchLabel6113 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel6118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_switchLabel6130 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel6134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_trystatement6157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000210L});
    public static final BitSet FOLLOW_resourceSpecification_in_trystatement6165 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_trystatement6175 = new BitSet(new long[]{0x0000800000400002L});
    public static final BitSet FOLLOW_catches_in_trystatement6195 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement6199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_trystatement6206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_trystatement6226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement6242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_trystatement6249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_resourceSpecification6287 = new BitSet(new long[]{0x0200400000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_resources_in_resourceSpecification6295 = new BitSet(new long[]{0x0000000000000000L,0x00000000C0000000L});
    public static final BitSet FOLLOW_SEMI_in_resourceSpecification6301 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_resourceSpecification6308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_resource_in_resources6338 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_resources6345 = new BitSet(new long[]{0x0200400000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_resource_in_resources6353 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_variableModifiers_in_resource6385 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_resource6393 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_resource6401 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_resource6406 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_resource6414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableDeclaratorId6441 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_variableDeclaratorId6447 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_variableDeclaratorId6452 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_catchClause_in_catches6479 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_catchClause_in_catches6496 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause6529 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause6533 = new BitSet(new long[]{0x0200400000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause6541 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause6554 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_catchClause6562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter6589 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_catchType_in_formalParameter6597 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_formalParameter6601 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_formalParameter6615 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_formalParameter6620 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_catchType6655 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_BAR_in_catchType6662 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_catchType6670 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_FOR_in_forstatement6712 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement6716 = new BitSet(new long[]{0x4201400400828000L,0x0000000100001080L});
    public static final BitSet FOLLOW_variableModifiers_in_forstatement6724 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_forstatement6732 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_forstatement6736 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_forstatement6742 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_forstatement6747 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_forstatement6754 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_forstatement6770 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement6775 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_forstatement6783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forstatement6817 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement6821 = new BitSet(new long[]{0x4203600C0182C200L,0x00548D818050D381L});
    public static final BitSet FOLLOW_forInit_in_forstatement6847 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement6871 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D818050C381L});
    public static final BitSet FOLLOW_expression_in_forstatement6897 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement6921 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D814050C381L});
    public static final BitSet FOLLOW_expressionList_in_forstatement6947 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement6971 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_forstatement6979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit7005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit7021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parExpression7044 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_parExpression7052 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_parExpression7057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList7083 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_expressionList7097 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_expressionList7105 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression7143 = new BitSet(new long[]{0x0020010000102082L,0x0000021400280400L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression7160 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_expression7168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator7203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator7215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator7226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator7237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator7248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPEQ_in_assignmentOperator7259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BAREQ_in_assignmentOperator7270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARETEQ_in_assignmentOperator7281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator7292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator7305 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator7310 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator7315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator7329 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator7334 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator7339 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator7344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator7358 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator7363 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator7368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression7395 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_QUES_in_conditionalExpression7409 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression7417 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_conditionalExpression7422 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression7430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression7467 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_BARBAR_in_conditionalOrExpression7480 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression7487 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression7524 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AMPAMP_in_conditionalAndExpression7537 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression7544 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression7581 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_BAR_in_inclusiveOrExpression7595 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression7603 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression7640 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_CARET_in_exclusiveOrExpression7654 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression7662 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression7699 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AMP_in_andExpression7713 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression7721 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression7758 = new BitSet(new long[]{0x0000020000000402L});
    public static final BitSet FOLLOW_EQEQ_in_equalityExpression7791 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_BANGEQ_in_equalityExpression7810 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression7843 = new BitSet(new long[]{0x0000020000000402L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression7880 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression7893 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression7900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression7937 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression7954 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression7962 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_LT_in_relationalOp7997 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp8002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp8016 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp8021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relationalOp8034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp8047 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression8073 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression8090 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression8098 = new BitSet(new long[]{0x0020000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_LT_in_shiftOp8134 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_LT_in_shiftOp8139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp8153 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp8158 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp8163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp8177 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp8182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression8209 = new BitSet(new long[]{0x0000000000000002L,0x0000010000100000L});
    public static final BitSet FOLLOW_PLUS_in_additiveExpression8243 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_SUB_in_additiveExpression8264 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression8298 = new BitSet(new long[]{0x0000000000000002L,0x0000010000100000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression8342 = new BitSet(new long[]{0x0000000000000002L,0x0000000A00040000L});
    public static final BitSet FOLLOW_STAR_in_multiplicativeExpression8376 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_SLASH_in_multiplicativeExpression8397 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_PERCENT_in_multiplicativeExpression8418 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression8452 = new BitSet(new long[]{0x0000000000000002L,0x0000000A00040000L});
    public static final BitSet FOLLOW_PLUS_in_unaryExpression8488 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression8497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryExpression8510 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression8518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpression8530 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression8537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryExpression8549 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression8556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression8572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_unaryExpressionNotPlusMinus8595 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus8603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BANG_in_unaryExpressionNotPlusMinus8616 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus8624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus8640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus8656 = new BitSet(new long[]{0x0000000200000002L,0x0000040000400020L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus8673 = new BitSet(new long[]{0x0000000200000002L,0x0000040000400020L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpressionNotPlusMinus8700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryExpressionNotPlusMinus8715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression8748 = new BitSet(new long[]{0x4001000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression8756 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression8761 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression8769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression8782 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_castExpression8790 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression8795 = new BitSet(new long[]{0x4203200C0182C200L,0x005488810000C381L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression8803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary8831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primary8855 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000220L});
    public static final BitSet FOLLOW_DOT_in_primary8868 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary8872 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000220L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary8900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary8923 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000220L});
    public static final BitSet FOLLOW_DOT_in_primary8937 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary8941 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000220L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary8969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primary8992 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_superSuffix_in_primary9007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary9023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creator_in_primary9039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary9055 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_primary9069 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_primary9074 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_DOT_in_primary9098 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_in_primary9102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOID_in_primary9113 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_DOT_in_primary9117 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_in_primary9121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix9152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_superSuffix9165 = new BitSet(new long[]{0x0200000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_typeArguments_in_superSuffix9174 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_superSuffix9197 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_superSuffix9214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix9250 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix9255 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix9279 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix9283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix9296 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix9304 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix9309 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix9336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix9349 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix9353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix9365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_identifierSuffix9373 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifierSuffix9377 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix9385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix9398 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_THIS_in_identifierSuffix9402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix9414 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_SUPER_in_identifierSuffix9418 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix9425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix9441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector9466 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_selector9470 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_selector9487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector9509 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_selector9517 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_selector9521 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_selector9529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector9542 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
    public static final BitSet FOLLOW_THIS_in_selector9546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector9558 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_SUPER_in_selector9562 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_superSuffix_in_selector9577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_selector9593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_selector9606 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_selector9614 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_selector9619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeArgumentsOrDiamond9645 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_typeArgumentsOrDiamond9650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeArguments_in_typeArgumentsOrDiamond9664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_nonWildcardTypeArgumentsOrDiamond9689 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_nonWildcardTypeArgumentsOrDiamond9694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_nonWildcardTypeArgumentsOrDiamond9708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator9729 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator9736 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator9744 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator9752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator9764 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator9771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator9779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayCreator_in_creator9795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator9817 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator9824 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator9837 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator9842 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator9856 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator9861 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreator9888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator9901 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator9908 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator9921 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_arrayCreator9929 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator9942 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator9959 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_arrayCreator9967 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator9984 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator10009 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator10014 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer10051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer10067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_arrayInitializer10090 = new BitSet(new long[]{0x4203200C0982C200L,0x00548D810850C391L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer10112 = new BitSet(new long[]{0x0000000008000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer10134 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C391L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer10142 = new BitSet(new long[]{0x0000000008000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer10195 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_RBRACE_in_arrayInitializer10211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName10251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName10267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_innerCreator10291 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_NEW_in_innerCreator10295 = new BitSet(new long[]{0x0200000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator10311 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_innerCreator10334 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000600L});
    public static final BitSet FOLLOW_nonWildcardTypeArgumentsOrDiamond_in_innerCreator10351 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator10378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest10405 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest10422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_nonWildcardTypeArguments10457 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments10465 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_GT_in_nonWildcardTypeArguments10478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments10501 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D814050C381L});
    public static final BitSet FOLLOW_expressionList_in_arguments10510 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_arguments10526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTLITERAL_in_literal10548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONGLITERAL_in_literal10560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BINLITERAL_in_literal10572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOATLITERAL_in_literal10584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLELITERAL_in_literal10596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHARLITERAL_in_literal10608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGLITERAL_in_literal10620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_literal10632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_literal10644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal10656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred2_Java97 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_packageDeclaration_in_synpred2_Java132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred12_Java565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred27_Java844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_synpred52_Java2127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_synpred53_Java2144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred54_Java2161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred57_Java2340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_synpred59_Java2222 = new BitSet(new long[]{0x0200000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_typeParameters_in_synpred59_Java2239 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred59_Java2262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameters_in_synpred59_Java2278 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000010L});
    public static final BitSet FOLLOW_THROWS_in_synpred59_Java2291 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_synpred59_Java2298 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_LBRACE_in_synpred59_Java2322 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F791L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred59_Java2340 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_blockStatement_in_synpred59_Java2368 = new BitSet(new long[]{0xC607608D4383C310L,0x01FDBDE1ABD0F391L});
    public static final BitSet FOLLOW_RBRACE_in_synpred59_Java2392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred68_Java2869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_synpred69_Java2885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_synpred70_Java2901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred71_Java2917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_synpred96_Java3879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java3895 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred98_Java3909 = new BitSet(new long[]{0x4201400400828000L,0x0000000100001080L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java3917 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred99_Java3945 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_synpred99_Java3958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred103_Java4140 = new BitSet(new long[]{0x0000000000000000L,0x0000880000000000L});
    public static final BitSet FOLLOW_set_in_synpred103_Java4168 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_arguments_in_synpred103_Java4206 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred103_Java4211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_synpred117_Java4979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred118_Java4995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred119_Java5011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred120_Java5027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_synpred121_Java5043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_synpred122_Java5059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred125_Java5265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java5281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_synpred130_Java5472 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred130_Java5497 = new BitSet(new long[]{0x0000000004000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COLON_in_synpred130_Java5503 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred130_Java5511 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred130_Java5518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_synpred132_Java5530 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred132_Java5538 = new BitSet(new long[]{0x0000000004000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_COLON_in_synpred132_Java5544 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred132_Java5552 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred132_Java5559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELSE_in_synpred133_Java5603 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_synpred133_Java5610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred148_Java5944 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred148_Java5950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred149_Java5967 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_synpred149_Java5972 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_synpred149_Java5980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred154_Java6195 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_FINALLY_in_synpred154_Java6199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_block_in_synpred154_Java6206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred155_Java6226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_synpred164_Java6712 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_synpred164_Java6716 = new BitSet(new long[]{0x4201400400828000L,0x0000000100001080L});
    public static final BitSet FOLLOW_variableModifiers_in_synpred164_Java6724 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_type_in_synpred164_Java6732 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred164_Java6736 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred164_Java6742 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred164_Java6747 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_synpred164_Java6754 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred164_Java6770 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred164_Java6775 = new BitSet(new long[]{0x4607200D4183C300L,0x0175BD81A050C391L});
    public static final BitSet FOLLOW_statement_in_synpred164_Java6783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred168_Java7005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred209_Java8640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred213_Java8748 = new BitSet(new long[]{0x4001000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_primitiveType_in_synpred213_Java8756 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred213_Java8761 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred213_Java8769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred215_Java8868 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred215_Java8872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred216_Java8900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred218_Java8937 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred218_Java8941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred219_Java8969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred231_Java9296 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred231_Java9304 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred231_Java9309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred246_Java9729 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred246_Java9736 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred246_Java9744 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred246_Java9752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred247_Java9764 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred247_Java9771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred247_Java9779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred249_Java9817 = new BitSet(new long[]{0x4201000400828000L,0x0000000100000080L});
    public static final BitSet FOLLOW_createdName_in_synpred249_Java9824 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred249_Java9837 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred249_Java9842 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred249_Java9856 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred249_Java9861 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
    public static final BitSet FOLLOW_arrayInitializer_in_synpred249_Java9888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred250_Java9959 = new BitSet(new long[]{0x4203200C0182C200L,0x00548D810050C381L});
    public static final BitSet FOLLOW_expression_in_synpred250_Java9967 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred250_Java9984 = new BitSet(new long[]{0x0000000000000002L});

}
