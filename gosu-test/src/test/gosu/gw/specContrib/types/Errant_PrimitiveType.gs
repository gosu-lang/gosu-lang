package gw.specContrib.types

class Errant_PrimitiveType {
    var int : int            //## issuekeys: MSG_
    var double : double      //## issuekeys: MSG_
    var float : float        //## issuekeys: MSG_
    var char : char          //## issuekeys: MSG_
    var byte : byte          //## issuekeys: MSG_
    var short : short        //## issuekeys: MSG_
    var long : long          //## issuekeys: MSG_

    var b: block(int): int = \int -> int  //## issuekeys: MSG_

    function f(int: int) {   //## issuekeys: MSG_
    }
}  //## issuekeys: MSG_EXPECTED_PARSING_BROKEN
