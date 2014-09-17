package gw.specContrib.expressions

uses java.lang.Integer

class Errant_NewExpressionTest {

    function foo() {
        var v = new gw()    //## issuekeys: MSG_
    }

}
