.. _introduction:

************
Introduction
************

Gosu is a pragmatic programming language for the JVM. It has been designed with
Java developers in mind by providing a set of features that allow them to be 
more productive without sacrificing the benefits of static typing. Gosu is an 
object oriented language with a sprinkle of functional programming features.


History
=======

The development of Gosu around early 2002 during the earliest stage of 
engineering at Guidewire Software. The language's humble origin sprang from an 
unsuccessful search for a scripting language as the basis for a rule engine we 
needed to build. Our language criteria didn't seem too demanding; but we were 
naive. First and foremost, the language had to be embeddable in the JVM and be 
compatible with leading application servers. It also had to be familiar -- the 
vast majority of programmers are comfortable with imperative, object-oriented 
languages, and we wanted our scripting language to be the same. We also wanted
a language with static typing, so that our customers could edit rules with 
instant parser feedback and we could provide modern features, such as 
deterministic code completion, navigation, etc. In addition, we needed to 
represent our ORM as a set of first-class types in the language, whether it be
through code generation or meta-programming or what have you. Lastly, the 
entity types from our ORM needed to be individually configurable by our 
customers via custom methods and properties. To our dismay, all the serious 
scripting languages at that time were dynamically typed, which ruled them out.
So began the development of Gosu (which is today an open-source language, 
available at http://gosu-lang.org).

By the time we had a working language, we started seeing it as a solution for 
rewriting our company's product UIs, our document management system, our 
Velocity-template systems; and each step exposed new features that were needed
in the language. Of these, by far the most innovative aspect of Gosu is the 
open type system. Unlike other languages, Gosu's type system is statically 
extendable to external domains, whereby abstract types can be defined and 
exposed with first-class representation. This is a big deal and is what makes 
Gosu extremely vital to the success of our enterprise applications. 


Grammar Notation
================

.. index:: EBNF, grammar, syntax, notation

A language is an infinite set of sentences, namely the sentences well formed 
according to its syntax. In Gosu, these sentences are called compilation units. 
Each unit is a finite sequence of symbols from a finite vocabulary. The 
vocabulary of Gosu consists of identifiers, numbers, strings, operators, 
delimiters and comments. They are called lexical symbols and are composed
of sequences of characters. (Note the distinction between symbols and 
characters.)

To describe the syntax, an extended Backus-Naur Formalism called EBNF is used. 
Brackets ``[`` and ``]`` denote optionality of the enclosed sentential form, and
braces ``{`` and ``}`` denote its repetition (possibly 0 times). Parentheses 
``(`` and ``)`` are used for grouping while vertical bars ``|`` are used to 
separate alternatives. Each production rule begins with a name (which is the 
name defined by the rule) and ``=``, followed by an expression and terminated 
by ``.``.

Syntactic entities (non-terminal symbols) are denoted by English words 
expressing their intuitive  meaning. Symbols of the language vocabulary 
(terminal symbols) are denoted by strings enclosed in quote marks.
In the definition of a lexical symbol two literal characters separated by two 
dots ``..`` mean a choice of any single character in the given (inclusive) 
range of characters.

This EBNF meta language can be used to define its own syntax, which may serve as
an example of its use.

.. 
    NOTE
    I had to modify Sphinx source code to render = instead of ::= in the production rules
    Affected files:  
    D:\tools\Sphinx-1.2b3>D:\UnxUtils\usr\local\wbin\grep.exe -r "::=" .
    ./doc/markup/para.rst:   (e.g. ``sum ::= `integer` "+" `integer```) -- this generates cross-references
    Binary file ./doc/themes/fullsize/scrolls.png matches
    ./sphinx/texinputs/sphinx.sty:  \def\production##1##2{\\\code{##1}&::=&\code{##2}}
    ./sphinx/writers/html.py:                self.body.append(lastname + '</strong> ::= ')
    ./sphinx/writers/manpage.py:                self.body.append(' ::= ')
    ./sphinx/writers/texinfo.py:                s = production['tokenname'].ljust(maxlen) + ' ::='
    ./sphinx/writers/text.py:                self.add_text(production['tokenname'].ljust(maxlen) + ' ::=')


.. productionlist:: EBNF
    Syntax  :  {`Production`} .
    Production :  `Identifier` "=" `Expression` "." .
    Expression :  `Term` {"|" `Term`} .
    Term  :  `Factor` {`Factor`} .
    Factor  :  `Identifier` | `String` | "(" `Expression` ")" | "[" `Expression` "]" | "{" `Expression` "}" .
    Identifier  :  `Letter` {`Letter` | `Digit`}.  
    String  : """ {character} """ .
    Letter  :  "A".."Z" | "a".."z" .
    Digit  :  "0".."9" .



Notational Conventions
======================

.. index:: conventions

.. 
    NOTE
    We may want to add Gosu specific rows in this table

    
===========  ================================================
Symbol       Meaning
===========  ================================================
a            Expression or value of array type
b            Boolean or byte array
C            Class
cs           Character array (type ``char[]``)
cseq         Character sequence (type CharSequence)
E            Exception type
e            Expression
f            Field
I            Interface
i            Expression or value of integer type
m            Method
o            Expression or value of object type
p            Package
s            Expression of type String
sig          Signature of method or constructor
t            Type (primitive or reference type)
T, U, K, V   Type parameter in generic type or method
u            Expression or value of thread type
v            Value of any type
x            Variable or parameter or field or array element
===========  ================================================

