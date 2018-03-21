package gw.specContrib.expressions.intervals

uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_BooleanIntervalExpressions {

  var boolean1011 = true..false      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var boolean1012 = false..|true      //## issuekeys: OPERATOR '..|' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var boolean1013 = true|..false      //## issuekeys: OPERATOR '|..' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var boolean1014 = false|..|true      //## issuekeys: OPERATOR '|..|' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var boolean1015 = false..-true      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'BOOLEAN'

  var boolean1111 = (false..'c')      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'CHAR'
  var boolean1112 = (false..1b)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'BYTE'
  var boolean1113 = (false..1s)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'SHORT'
  var boolean1114 = (false..10     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'INT'
  var boolean1115 = (false..10L)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'LONG'
  var boolean1116 = (false..10.5f)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'FLOAT'
  var boolean1117 = (false..10.5)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'DOUBLE'
  var boolean1118 = (false..BigInteger.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGINTEGER'
  var boolean1119 = (false..BigDecimal.TEN)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.MATH.BIGDECIMAL'
  var boolean1120 = (false..(new Date()))      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'JAVA.UTIL.DATE'

  var boolean1131 = ('c'..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'CHAR', 'BOOLEAN'
  var boolean1132 = (1b..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BYTE', 'BOOLEAN'
  var boolean1133 = (1s..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'SHORT', 'BOOLEAN'
  var boolean1134 = (10..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT', 'BOOLEAN'
  var boolean1135 = (10L..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'LONG', 'BOOLEAN'
  var boolean1136 = (10.5f..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'FLOAT', 'BOOLEAN'
  var boolean1137 = (10.5..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'DOUBLE', 'BOOLEAN'
  var boolean1138 = (BigInteger.TEN..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGINTEGER', 'BOOLEAN'
  var boolean1139 = (BigDecimal.TEN..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.MATH.BIGDECIMAL', 'BOOLEAN'
  var boolean1140 = (false..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'BOOLEAN'
  var boolean1141 = ((new Date())..true)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.UTIL.DATE', 'BOOLEAN'

  var boolean1151 = Boolean.FALSE..Boolean.TRUE      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.BOOLEAN', 'JAVA.LANG.BOOLEAN'
  var boolean1152 = Boolean.FALSE..true      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.BOOLEAN', 'BOOLEAN'
  var boolean1153 = Boolean.FALSE..new Boolean(true)     //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.BOOLEAN', 'JAVA.LANG.BOOLEAN'
}