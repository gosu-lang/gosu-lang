package gw.internal.gosu.parser.statements
uses gw.testharness.DoNotVerifyResource

uses java.util.Date
uses java.util.Date  //## issuekeys: MSG_USES_STMT_DUPLICATE
uses java.sql.Date  //## issuekeys: MSG_USES_STMT_CONFLICT
uses javax.accessibility.*
uses javax.accessibility.*  //## issuekeys: MSG_USES_STMT_DUPLICATE
uses javax.*

@DoNotVerifyResource
class Errant_DuplicateNames {
}
