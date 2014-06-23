/*
 * Copyright 2013. Guidewire Software, Inc.
 */

grammar EBNF;

start :  ('lexer' | 'parser' | 'tree')? 'grammar' id ';' optionsSpec? tokensSpec? attrScope* action* rule+ ;

tokensSpec : TOKENS tokenSpec+ '}' ;

tokenSpec : TOKEN_REF ('=' STRING_LITERAL)?  ';' ;

attrScope : 'scope' id ACTION ;

action : '@' (actionScopeName '::')? id ACTION ;

actionScopeName : id | 'lexer' |  'parser' ;

optionsSpec :   OPTIONS (option ';')+ '}' ;

option : id '=' optionValue ;
    
optionValue : id | STRING_LITERAL | INT | '*' ;
    

rule : ('protected'|'public'|'private'|'fragment')? id '!'? ARG_ACTION? ( 'returns' ARG_ACTION)?
       throwsSpec? optionsSpec? ruleScopeSpec? ruleAction*
       ':' altList ';' exceptionGroup? {System.out.print("<b>"+$id.text+"</b>" + " <b>=</b> " + $altList.str + "<b>.</b>\n");} ;

ruleAction : '@' id ACTION ;

throwsSpec : 'throws' id ( ',' id )* ;

ruleScopeSpec : 'scope' ACTION | 'scope' id (',' id)* ';' | 'scope' ACTION 'scope' id (',' id)* ';' ;
       
block returns [String str] : '(' ( optionsSpec? ':')? altList ')' {$str= $altList.str; } ;

altList returns [String str] : {$str= ""; }  a1=alternative {$str+= $a1.str;} ( '|'  a2=alternative {$str+= "<b>|</b> " + $a2.str;} )* ;

alternative returns [String str] : {str="";} (element {if(!$element.str.equals("")) {$str = $str+ $element.str + " ";}})* ;

exceptionGroup : exceptionHandler+ finallyClause? | finallyClause ;

exceptionHandler : 'catch' ARG_ACTION ACTION  ;

finallyClause : 'finally' ACTION ;

element returns [String str] :               
                                ACTION                                        {$str = "";}   |
                                treeSpec  ('?' | '*' |'+')?                   {$str = "";}   |
                                (id ('='| '+='))? (a=atom | b=block) 
                                (op1='?' | op2='*' | op3='+' | op4='=>')?                  
                                                                           {
                                                                                String s;
                                                                                boolean block = false;
                                                                                
                                                                                if($a.str != null)  {
                                                                                  s = $a.str;
                                                                                } else {
                                                                                  s = $b.str.trim();
                                                                                  block = true;
                                                                                } 
                                                                                if($op1 != null) {
                                                                                  $str = "<b>[</b>"+ s +"<b>]</b>";
                                                                                }
                                                                                else if($op2 != null) {
                                                                                  $str = "<b>{</b>"+ s +"<b>}</b>";
                                                                                }
                                                                                else if($op3 != null) {
                                                                                  if(block) {
                                                                                    $str = "<b>(</b>"+ s +"<b>)</b>" + " <b>{</b>"+ s +"<b>}</b>";
                                                                                  } else {
                                                                                    $str = s + " <b>{</b>"+ s +"<b>}</b>";
                                                                                  }
                                                                                } else if($op4 != null) {
                                                                                  $str = "";
                                                                                }else if(block) {
                                                                                  $str = "<b>(</b>"+ s +"<b>)</b>";
                                                                                } else {
                                                                                  $str = s;
                                                                                }
                                                                             }   
                             ;

atom returns [String str] :  ( e=terminal | e=notSet |  e=ruleRef ARG_ACTION? ('^'|'!')?) {$str = $e.str;}
                          ;

notSet returns[String str] :   '~' ( a=notTerminal | b=block) {
                                                                    if($a.text != null)  {
                                                                      $str = "~" + $a.text;
                                                                    } else {
                                                                      $str = "~(" + $b.str.trim() + ")"; 
                                                                    } 
                                                              } ;

treeSpec : '^(' element ( element )+ ')' ;

terminal returns[String str] :   e=((STRING_LITERAL  | TOKEN_REF | '.' ) {$str = $e.text;} ) | 
                                 a=STRING_LITERAL RANGE b=STRING_LITERAL  {$str = $a.text + ".." +$b.text;}                             
                             ;

notTerminal :  STRING_LITERAL | TOKEN_REF ;

ruleRef returns[String str] : RULE_REF {$str = $RULE_REF.text;} ;

id  : TOKEN_REF | RULE_REF ;

ML_COMMENT :   '/*' .* '*/' { skip(); } ;

SL_COMMENT :  '//' ~('\n'|'\r')*  '\r'? '\n'? { skip(); } ;


STRING_LITERAL :   '\'' LITERAL_CHAR LITERAL_CHAR* '\'' 
                    {
                      char[] s = getText().toCharArray();
                      char[] r = new char[s.length];

                      int i = 1;
                      int j = 1;
                      r[0] = '"';
                      while(i < s.length-1) {
                        if(s[i] == '\\') {
                          if(s[i+1] == '\\') {
                            r[j] = s[i];
                            i++;
                            j++;
                          }
                          i++;
                        } else {
                          r[j] = s[i];
                          i++;
                          j++;
                        }
                      }
                      r[j] = '"';
                      j++;
                      setText(new String("<font color=\"green\">" + new String(r, 0, j) + "</font>"));
                    };
    

fragment
LITERAL_CHAR :   ESC | ~('\''|'\\') ;

RANGE:'..';

fragment
ESC :   '\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '"' | '\'' | '\\' | '>' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT | .) 
    ;

fragment
XDIGIT : '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ;

INT :   '0'..'9'+ ;

ARG_ACTION  :  NESTED_ARG_ACTION;

fragment
NESTED_ARG_ACTION :
                    '[' ( options {greedy=false; k=1;} : NESTED_ARG_ACTION | .)* ']'
                  ;

ACTION :   NESTED_ACTION '?'?;

fragment
NESTED_ACTION :
                '{' ( options {greedy=false; k=1;} : NESTED_ACTION | .)* '}'
              ;

fragment
ACTION_CHAR_LITERAL : '\'' (ACTION_ESC|~('\\'|'\'')) '\'' ;

fragment
ACTION_STRING_LITERAL : '"' (ACTION_ESC|~('\\'|'"'))* '"' ;

fragment
ACTION_ESC : '\\\'' |'\\' '"' | '\\' ~('\''|'"') ;

TOKEN_REF : 'A'..'Z' ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

RULE_REF : 'a'..'z' ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

OPTIONS : 'options' WS_LOOP '{' ;
    
TOKENS : 'tokens' WS_LOOP '{' ;
    
WS : (' ' | '\r' | '\t' | '\n' )+  {$channel=HIDDEN;};
    
fragment
WS_LOOP :  (WS | SL_COMMENT | ML_COMMENT)* ;
