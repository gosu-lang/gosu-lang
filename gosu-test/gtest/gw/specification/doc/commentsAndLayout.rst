.. _commentsAndLayout:

***************************
Comments and Program Layout
***************************

.. index:: comment

*Comments* have no effect on the execution of the program but may be inserted 
anywhere to help humans understand the program. There are two forms: one-line 
comments and delimited comments. Delimited comments may nest.

.. productionlist:: Comments
    Comment : "/*" {any_character_but_*/} | Comment "*/" .
    LineComment : "//" {any_character} .

*Program layout* has no effect on the computer's execution of the program but is
used to help humans understand the structure of the program.