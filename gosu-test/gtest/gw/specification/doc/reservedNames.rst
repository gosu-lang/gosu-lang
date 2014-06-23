.. _reservedNames:

************************
Names and Reserved Names
************************

.. index:: reserved word, keyword

A *legal name* (of a variable, method, property, field, parameter, delegate(?), 
block, class, interface, structure,  enhancement  or package) starts with a 
letter or dollar sign (``$``) or underscore (``_``), and continues with zero or 
more letters or dollar signs or underscores or digits (``0-9``). 

.. productionlist:: Identifier
    Ident : `Letter` {`Digit` | `Letter`} .
    Letter  :  "A".."Z" | "a".."z" | "_" | "$" .
    Digit  :  "0".."9" .


Avoid dollar signs in class names. Uppercase letters and lowercase letters are
considered distinct. A legal name cannot be one of the following *reserved names*:

============    ==============
Name            Fully Reserved
============    ==============
abstract        true
and             false
application     true
as              true
assert          false
block           true
break           false
case            false
catch           false
class           false
classpath       true
construct       false
contains        true 
continue        false
default         false
delegate        false
do              false
else            false
enhancement     true
enum            false
eval            false
except          true
execution       true
exists          true
extends         false
false           true
final           true
finally         false
find            true
for             false
foreach         false
function        false
get             true
hide            true
if              false
implements      false
in              false
index           true
Infinity        true
interface       false
internal        true
iterator        true
length          true
NaN             true 
new             false
not             false
null            true
or              false
outer           true
override        false
package         false
private         true
property        false
protected       true
public          true
readonly        true
represents      false
request         true
return          false
session         true
set             true
startswith      true
static          true
statictypeof    false
structure       false
super           false
switch          false
this            true
throw           false
transient       false
try             false
typeas          false
typeis          false
typeloader      true
typeof          false
unless          false
uses            false
using           false
var             false
void            true
where           true
while           false
true            true 
============    ==============

TODO:

*  Add some new reserved words that we will use in the future (ex. volatile) 
*  Remove some words entirely (ex. where)
*  Make some word  fully reserved  (ex. true)
*  Remove  the fully reserved column from the spec once done