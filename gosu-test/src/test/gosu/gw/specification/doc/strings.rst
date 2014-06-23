.. _strings:

*******
Strings
*******

.. index:: string

A ``string`` is an object of the predefined class String. It is immutable: once 
created it cannot be changed. A string literal is a sequence of characters 
within double quotes or single quotes: ``"Don't worry"``, ``'Torino'``, ``""``, 
and so on. Internally, a character is stored as a number using the Unicode 
character encoding, whose character codes 0–127 coincide with the old ASCII 
character encoding. String literals and character literals may use character 
*escape sequences*: 

===========    ===============================================================================================
Escape Code    Meaning
===========    ===============================================================================================
``\b``         backspace
``\t``         horizontal tab
``\n``         newline
``\f``         form feed (page break)
``\r``         carriage return
``\v``         vertical tab
``\a``         alert (bell)
``\$``         the dollar character
``\<``         the less than character
``\"``         the double quote character
``\'``         the single quote character
``\\``         the backslash character
``\ddd``       the character whose character code is the octal number ddd expressed with at most three-digit.
``\udddd``     the character whose character code is the four-digit hexadecimal number dddd
===========    ===============================================================================================

A character escape sequence represents a single character. Since the letter A 
has code 65 (decimal), which is written 101 in octal and 0041 in hexadecimal, 
the string literal ``"A\101\u0041"`` is the same as ``"AAA"``.

.. productionlist:: string
    StringLiteral : """ {`EscapeSequence` | any_character} """ | "'" {`EscapeSequence` | any_character} "'" .
    EscapeSequence : "\" ("v" | "a" | "b" | "t" | "n" | "f" | "r" | """ | "'" | "\" | "$" | "<") | `UnicodeEscape` | `OctalEscape` .
    OctalEscape : "\" "0".."3" "0".."7" "0".."7" | "\" "0".."7" "0".."7" | "\" "0".."7" .
    UnicodeEscape : "\" "u" `HexDigit` `HexDigit` `HexDigit` `HexDigit` . 

If ``s1`` and ``s2`` are expressions of type ``String`` and ``v`` is an 
expression of any type, then 

* ``s1.length()`` of type ``int`` is the length of ``s1``, that is, the 
  number of characters in ``s1``. 
* ``s1.equals(s2)`` of type ``boolean`` is ``true`` if ``s1`` and ``s2`` 
  contain the same sequence of characters, and ``false`` otherwise; 
  ``equalsIgnoreCase`` is similar but does not distinguish lowercase and 
  uppercase. 
* ``s1.charAt(i)`` of type ``char`` is the character at position ``i`` in 
  ``s1``, counting from 0. If the index ``i`` is less than 0, or greater than or
  equal to ``s1.length()``, then StringIndexOutOfBoundsException is thrown. 
* ``s1.toString()`` of type ``String`` is the same object as ``s1``. 
* ``String.valueOf(v)`` returns the string representation of ``v``, which 
  can have any :ref:`primitive type<primitiveTypes>` or 
  :ref:`reference type<referenceTypes>`. When ``v`` has reference type and is 
  not ``null``, then it is converted using ``v.toString()``; if it is 
  ``null``, then it is converted to the string ``"null"``. Any class ``C`` 
  inherits from Object a default ``toString`` method that produces strings of 
  the form ``C@2a5734``, where ``2a5734`` is some memory address, but 
  ``toString`` may be overridden to produce more useful strings. 
* ``s1 + s2`` has the same meaning as ``s1.concat(s2)``: it constructs the 
  concatenation of ``s1`` and ``s2``, a new String consisting of the characters 
  of ``s1`` followed by the characters of ``s2``. Both ``s1 + v`` and ``v + s1``
  are evaluated by converting ``v`` to a string with ``String.valueOf(v)``, thus
  using ``v.toString()`` when ``v`` has reference type, and then concatenating 
  the resulting strings. 
* ``s1.compareTo(s2)`` returns a negative integer, zero, or a positive 
  integer, according as ``s1`` precedes, equals, or follows ``s2`` in the usual 
  lexicographical ordering based on the Unicode character encoding. If ``s1`` or
  ``s2`` is ``null``, then the exception NullPointerException is thrown. 
  Method ``compareToIgnoreCase`` is similar but does not distinguish lowercase 
  and uppercase. 
* ``s1.substring(i : int, k : int)`` returns a new String of the characters from
  ``s1`` with indexes ``i..(k-1)``. Throws IndexOutOfBoundsException if 
  ``i < 0`` or ``i > k`` or ``k > s1.length``.
* ``s1.subSequence(i : int, k : int)`` is like ``substring`` but returns a 
  CharSequence.
* More ``String`` methods are described in the Java class library documentation. 

String Templates
================

.. index:: string template

TODO
