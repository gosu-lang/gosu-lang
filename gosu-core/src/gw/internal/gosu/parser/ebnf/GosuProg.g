/*
 * Copyright 2013. Guidewire Software, Inc.
 */

grammar GosuProg;

options { backtrack = true;  memoize = true; }

//Disable error recovery
@parser::members {
  @Override
  protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
    throw new MismatchedTokenException(ttype, input);
  }
  @Override
  public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
    throw e;
  }
}
@rulecatch {
  catch (RecognitionException e) {
    throw e;
  }
}
@lexer::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new RuntimeException(e);
  }
}

start : header ( modifiers (gClass | gInterfaceOrStructure | gEnum | gEnhancement) | statementTop )+ ;

statementTop : statement                                    |
               modifiers functionDefn functionBody          |
               modifiers propertyDefn functionBody
             ;

header : HASHBANG? classpathStatements? typeLoaderStatements? ('package' namespaceStatement)? usesStatementList? ;

classpathStatements : ('classpath' StringLiteral)+ ; 

typeLoaderStatements : ('typeloader' typeLiteral)+ ; 

annotation : '@' idAll ('.' idAll)* annotationArguments? ;

gClass : 'class' id typeVariableDefs ('extends' classOrInterfaceType)? ('implements' classOrInterfaceType (',' classOrInterfaceType)*)? classBody ;

gInterfaceOrStructure : ('interface' | 'structure') id typeVariableDefs ('extends' classOrInterfaceType (',' classOrInterfaceType)*)? interfaceBody ;

gEnum : 'enum' id typeVariableDefs ('implements' classOrInterfaceType (',' classOrInterfaceType)*)? enumBody ;

gEnhancement : 'enhancement' id typeVariableDefs ':' classOrInterfaceType ('[' ']')* enhancementBody ;

classBody : '{' classMembers '}' ;

enhancementBody : '{' enhancementMembers '}' ;

interfaceBody : '{' interfaceMembers '}' ;

enumBody : '{' enumConstants? classMembers '}' ;

enumConstants : enumConstant  (',' enumConstant)*  ','? ';'?  ;

enumConstant : id optionalArguments ;

interfaceMembers : ( modifiers 
                        (
                          functionDefn           |
                          propertyDefn           |
                          fieldDefn              |
                          gClass                 |
                          gInterfaceOrStructure  |
                          gEnum
                        ) ';'?
                   )*;


classMembers : declaration* ;

declaration :
                 modifiers
                 (
                   functionDefn functionBody?     |
                   constructorDefn functionBody   |
                   propertyDefn functionBody?     |
                   fieldDefn                      |
                   delegateDefn                   |
                   gClass                         |
                   gInterfaceOrStructure          |
                   gEnum
                 ) ';'?
            ;

enhancementMembers : ( modifiers
                        (
                          functionDefn functionBody  |
                          propertyDefn functionBody
                        ) ';'?
                     )*
                   ;

delegateDefn : 'delegate' id delegateStatement ;

delegateStatement : (':' typeLiteral)? 'represents' typeLiteral (',' typeLiteral)* ('=' expression)? ;

optionalType : (':' typeLiteral | blockTypeLiteral)? ;

fieldDefn : 'var' id optionalType ('as' 'readonly'? id)? ('=' expression)? ;

propertyDefn : 'property' ('get' | 'set') id parameters (':' typeLiteral)? ;

dotPathWord : idAll ('.' idAll)*;

namespaceStatement :  dotPathWord (';')* ;

usesStatementList : ('uses' usesStatement)+ ;

usesStatement : dotPathWord ('.' '*')? (';')* ;

typeVariableDefs : ('<' typeVariableDefinition (',' typeVariableDefinition)* '>')?;

typeVariableDefinition : id ('extends' typeLiteralList)? ;

functionBody : statementBlock ;

parameters : '(' parameterDeclarationList? ')' ;

functionDefn : 'function' id typeVariableDefs  parameters (':' typeLiteral)? ;

constructorDefn : 'construct' parameters (':' typeLiteral)? ;

modifiers :  (annotation  |
              'private'   |
              'internal'  |
              'protected' |
              'public'    |
              'static'    |
              'abstract'  |
              'override'  |
              //'hide'    |  not used 
              'final'     |
              'transient')*
          ;

statement : (
              ifStatement                   |
              tryCatchFinallyStatement      |
              throwStatement                |
              'continue'                    |
              'break'                       |
              returnStatement               |
              forEachStatement              |
              whileStatement                |
              doWhileStatement              |
              switchStatement               |
              usingStatement                |
              assertStatement               |           
              'final' localVarStatement     |
              localVarStatement             |
              evalExpr                      |
              assignmentOrMethodCall        |
              statementBlock) ';'?          |
              ';'
          ;

ifStatement : 'if' '(' expression ')' statement  ';'? ('else' statement )? ;

tryCatchFinallyStatement : 'try' statementBlock ( catchClause+ ('finally' statementBlock)? | 'finally' statementBlock);

catchClause :  'catch' '(' 'var'? id (':' typeLiteral)? ')' statementBlock ;

assertStatement : 'assert' expression (':' expression )? ;

usingStatement : 'using' '(' (localVarStatement (',' localVarStatement)* | expression) ')' statementBlock ('finally' statementBlock)? ;

returnStatement : 'return' ( (expression ((~'=')|EOF)) => expression )? ;

whileStatement : 'while' '('expression ')' statement ;

doWhileStatement : 'do' statement 'while' '(' expression ')' ;

switchStatement : 'switch' '(' expression ')' '{' switchBlockStatementGroup* '}' ;

switchBlockStatementGroup : ('case' expression ':'| 'default' ':') statement* ;

throwStatement : 'throw' expression ;

localVarStatement : 'var' id optionalType ('=' expression)? ;

forEachStatement : ('foreach' | 'for') '(' (expression indexVar? | 'var'? id 'in' expression indexRest?) ')' statement ;

indexRest : indexVar iteratorVar  | 
            iteratorVar indexVar  |
            indexVar              |
            iteratorVar
          ;

indexVar : 'index' id ;

iteratorVar : 'iterator' id ;

thisSuperExpr : 'this' | 'super' ;

assignmentOrMethodCall : (
                           (newExpr | thisSuperExpr | typeLiteralExpr | parenthExpr | StringLiteral)
                           indirectMemberAccess
                         ) (incrementOp | assignmentOp expression)?
                         
                       ;       

statementBlock : statementBlockBody ;

statementBlockBody : '{' statement* '}' ;

blockTypeLiteral : blockLiteral ;

blockLiteral : '(' (blockLiteralArg (',' blockLiteralArg)*)?')' (':' typeLiteral )?  ;

//blockLiteralArg :  id ((':' typeLiteral ('=' expression)?) | blockTypeLiteral |'=' expression ) ;
blockLiteralArg :  id ('=' expression | blockTypeLiteral) |
                   (id ':')? typeLiteral ('=' expression)?  ;

typeLiteral : type  ('&' type)*;

typeLiteralType : typeLiteral ;

typeLiteralExpr : typeLiteral ;

typeLiteralList : typeLiteral ;

type
    : 
        classOrInterfaceType  ('[' ']')* |
       'block' blockLiteral    
    ;

classOrInterfaceType
    :   idclassOrInterfaceType typeArguments ('.' id typeArguments )*
    ;

typeArguments
    :   ('<' typeArgument (',' typeArgument)* '>')?
    ;
    
typeArgument
    :   typeLiteralType | '?' (('extends' | 'super') typeLiteralType)?
    ;

expression : conditionalExpr  ;

conditionalExpr : conditionalOrExpr ('?' conditionalExpr ':' conditionalExpr | '?:' conditionalExpr)?; 

conditionalOrExpr : conditionalAndExpr (orOp conditionalAndExpr)* ;

conditionalAndExpr : bitwiseOrExpr (andOp bitwiseOrExpr)* ;

bitwiseOrExpr : bitwiseXorExpr ('|' bitwiseXorExpr)* ; 

bitwiseXorExpr : bitwiseAndExpr ('^' bitwiseAndExpr)* ;

bitwiseAndExpr : equalityExpr ('&' equalityExpr)* ;

equalityExpr : relationalExpr (equalityOp relationalExpr)* ;

relationalExpr : intervalExpr (relOp intervalExpr | 'typeis' typeLiteralType)*  ;

intervalExpr : bitshiftExpr (intervalOp bitshiftExpr)? ;
             
bitshiftExpr : additiveExpr (bitshiftOp additiveExpr )* ; 

additiveExpr : multiplicativeExpr (additiveOp multiplicativeExpr)* ;

multiplicativeExpr : typeAsExpr (multiplicativeOp typeAsExpr)* ;

typeAsExpr : unaryExpr (typeAsOp typeLiteral)* ;

unaryExpr :  ('+' | '-') unaryExprNotPlusMinus | unaryExprNotPlusMinus ;

unaryExprNotPlusMinus :  unaryOp unaryExpr | 
                         '\\' blockExpr    | 
                         evalExpr          |
                         primaryExpr
                      ;
                      
blockExpr : parameterDeclarationList? '->' (expression | statementBlock ) ;

parameterDeclarationList : parameterDeclaration (',' parameterDeclaration)* ;

parameterDeclaration : annotation* 'final'? id ((':' typeLiteral ('=' expression)?) | blockTypeLiteral | '=' expression )? ;

annotationArguments : arguments ;

arguments : '(' (argExpression (',' argExpression)*)? ')' ;

optionalArguments : arguments? ;

argExpression : namedArgumentExpression | expression ;

namedArgumentExpression : ':' id '=' expression;

evalExpr : 'eval' '(' expression ')' ;

featureLiteral : '#' (id | 'construct')  typeArguments  optionalArguments ;

standAloneDataStructureInitialization : '{' (initializerExpression)? '}' ;

primaryExpr :
                (newExpr                                    |
                 thisSuperExpr                              |
                 //'exists' existsExpr                        | /* DEPRECATED */
                 //'find' queryExpr                           | /* DEPRECATED */
                 literal                                    |
                 typeLiteralExpr                            |
                 parenthExpr                                |
                 standAloneDataStructureInitialization)  
                indirectMemberAccess                            
                
            ;

parenthExpr : '(' expression ')' ;

 /* DEPRECATED */
//existsExpr : '(' 'var'? id ('in' expression ('index' id )? ('where' expression)?)+ ')' ;

/* DEPRECATED */
//queryExpr : existsExpr ;
 
newExpr :  'new' classOrInterfaceType?  (
                                            (arguments ( '{' (initializer | anonymousInnerClass) '}' )?)      |
                                    ( 
                                      '['
                                          (  
                                             ']' ('[' ']')* arrayInitializer                 |
                                             expression ']' ('[' expression ']')* ('[' ']')*
                                          )             
                                    )
                                  )                                                
        ;
                                                            
anonymousInnerClass :  classMembers;
                                                            
arrayInitializer : '{' ( expression (','  expression)*)? '}';
 
initializer : (initializerExpression | objectInitializer)? ; 

initializerExpression : mapInitializerList | arrayValueList  ;

arrayValueList : expression (',' expression)* ;

mapInitializerList : expression '->' expression (',' expression '->' expression )*  ;

objectInitializer : initializerAssignment (',' initializerAssignment)* ;

initializerAssignment : ':' id '=' expression  ;

indirectMemberAccess : 
                       ( 
                          ('.' | '?.' | '*.') idAll typeArguments  |
                          featureLiteral                           |
                          ('['|'?[') expression  ']'               |
                          {input.LT(-1).getLine() == input.LT(1).getLine()  }? arguments /* I really need type info to understand if this is legal
                                                                                                       var auditPeriod = new String().toLowerCase()
                                                                                                       ("1" as String).length() */         
                        )*
                     ;

literal : NumberLiteral          |
          featureLiteral         | 
          StringLiteral          |
          CharLiteral            |
          'true'                 |
          'false'                |
          'null'
        ;

orOp : '||' | 'or' ;

andOp : '&&' | 'and' ;

assignmentOp :
        '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    |   '&='
    |   '&&='
    |   '|='
    |   '||='
    |   '^='
    |   '%='
    |   ('<' '<' '=')=> t1='<' t2='<' t3='=' 
        { $t1.getLine() == $t2.getLine() &&
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>' '>' '=')=> t1='>' t2='>' t3='>' t4='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&
          $t3.getLine() == $t4.getLine() && 
          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() }?
    |   ('>' '>' '=')=> t1='>' t2='>' t3='='
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && 
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    ;
    
    
incrementOp : '++' | '--' ;

equalityOp : '===' | '!==' | '==' | '!=' | '<>' ;

intervalOp : '..' | '|..' | '..|' | '|..|' ;

relOp :
        ('<' '=')=> t1='<' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '=')=> t1='>' t2='=' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   '<' 
    |   '>' 
    ;

bitshiftOp : 
        ('<' '<')=> t1='<' t2='<' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }?
    |   ('>' '>' '>')=> t1='>' t2='>' t3='>' 
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&
          $t2.getLine() == $t3.getLine() && 
          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() }?
    |   ('>' '>')=> t1='>' t2='>'
        { $t1.getLine() == $t2.getLine() && 
          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() }? 
    ;

additiveOp : '+' | '-' | '?+' | '?-';

multiplicativeOp : '*' | '/' | '%' | '?*' | '?/' | '?%' ;

typeAsOp : 'typeas' | 'as' ;

unaryOp : '~' | '!' | 'not' | 'typeof' | 'statictypeof';

id : Ident               |
     'true'              |
     'false'             |
     'NaN'               |
     'Infinity'          |
     'null'              |
     'length'            |
     'exists'            |
     'startswith'        |
     'contains'          |
     'where'             |
     'find'              |
     'as'                |
     'except'            |
     'index'             |
     'iterator'          |
     'get'               |
     'set'               |
     'assert'            |
     'private'           |
     'internal'          |
     'protected'         |
     'public'            |
     'abstract'          |
     'hide'              |
     'final'             |
     'static'            |
     'readonly'          |
     'outer'             |
     'execution'         |
     'request'           |
     'session'           |
     'application'       |
     'void'              |
     'block'             |
     'enhancement'       |
     'classpath'         |
     'typeloader'        
   ;


idclassOrInterfaceType : Ident               | 
                         'true'              |
                         'false'             |
                         'NaN'               |
                         'Infinity'          |
                         'null'              |
                         'length'            |
                         'exists'            |
                         'startswith'        |
                         'contains'          |
                         'where'             |
                         'find'              |
                         'as'                |
                         'except'            |
                         'index'             |
                         'iterator'          |
                         'get'               |
                         'set'               |
                         'assert'            |
                         'private'           |
                         'internal'          |
                         'protected'         |
                         'public'            |
                         'abstract'          |
                         'hide'              |
                         'final'             |
                         'static'            |
                         'readonly'          |
                         'outer'             |
                         'execution'         |
                         'request'           |
                         'session'           |
                         'application'       |
                         'void'              |
                         'enhancement'       |
                         'classpath'         |
                         'typeloader'        
   ;

idAll :  id                  |
         'and'               |
         'or'                |
         'not'               |
         'in'                |
         'var'               |
         'delegate'          |
         'represents'        |
         'typeof'            |
         'statictypeof'      |
         'typeis'            |
         'typeas'            |
         'package'           |
         'uses'              |
         'if'                |
         'else'              |
         'unless'            |
         'foreach'           |
         'for'               |
         'while'             |
         'do'                |
         'continue'          |
         'break'             |
         'return'            |
         'construct'         |
         'function'          |
         'property'          |
         'try'               |
         'catch'             |
         'finally'           |
         'throw'             |
         'new'               |
         'switch'            |
         'case'              |
         'default'           |
         'eval'              |
         'override'          |
         'extends'           |
         'transient'         |
         'implements'        |
         'class'             |
         'interface'         |
         'structure'         |
         'enum'              |
         'using'             
       ;

Ident : Letter (Digit | Letter)* ;

NumberLiteral :  'NaN'                |
                 'Infinity'           |
                 HexLiteral           |
                 BinLiteral           |
                 IntOrFloatPointLiteral
              ;

fragment
BinLiteral : ('0b'|'0B') ('0' | '1')+ IntegerTypeSuffix? ;

fragment
HexLiteral : ('0x'|'0X') HexDigit+ ('s'|'S'|'l'|'L')? ;

fragment
IntOrFloatPointLiteral : '.' Digit+ FloatTypeSuffix?                                                                   |
                         Digit+ ( ('.' ~'.')=> '.' Digit* Exponent? FloatTypeSuffix?  |
                                  Exponent FloatTypeSuffix?                           |
                                  FloatTypeSuffix                                     |
                                  IntegerTypeSuffix                                   |
                                 // nothing, somehow ANTLR get confused if you do Digit+ (...)? -> 1b cannot be parsed
                                )          
                       ; 

CharLiteral :  '\'' ( EscapeSequence | ~('\''|'\\'|'\r'|'\n') ) '\'' ;

StringLiteral : '"' ( EscapeSequence | ~('\\'|'"'|'$'|'\r'|'\n') | '$'('{' ~('}')* '}'| ~('{'|'"'|'\r'|'\n') ) )* ('"'|'$"')  |
                '\'' ( EscapeSequence | ~('\\'|'\''|'$'|'\r'|'\n')| '$'('{' ~('}')* '}'| ~('{'|'"'|'\r'|'\n') ) )* ('\''|'$\'')
              ;
              
fragment
HexDigit : Digit | 'a'..'f' | 'A'..'F' ;

fragment
IntegerTypeSuffix : ('l'|'L'|'s'|'S'|'bi'|'BI'|'b'|'B') ;

fragment
Letter : 'A' .. 'Z' | 'a' .. 'z' | '_' | '$' ;

fragment
Digit : '0'..'9' ;

fragment
NonZeroDigit : '1'..'9' ;

fragment
Exponent : ('e'|'E') ('+'|'-')? Digit+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D'|'bd'|'BD') ;

fragment
EscapeSequence :   '\\' ('v'|'a'|'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\'|'$'|'<')   |
                   UnicodeEscape                                               |
                   OctalEscape
               ;

fragment
OctalEscape : '\\' ('0'..'3') ('0'..'7') ('0'..'7')  |
              '\\' ('0'..'7') ('0'..'7')             |      
              '\\' ('0'..'7')
            ;

fragment
UnicodeEscape : '\\' 'u' HexDigit HexDigit HexDigit HexDigit ;

WS :   (' ' | '\r' | '\t' | '\n' )+  { skip();  } ;
    
COMMENT :   '/*' (options { greedy=false; } : . )* '*/' { skip(); } ;

LINE_COMMENT :  '//' ~('\n'|'\r')*  '\r'? '\n'? { skip(); } ;

HASHBANG : '#!' ~('\n'|'\r')*  '\r'? '\n'? { skip(); } ; 