package gw.specContrib.enhancements

enhancement Errant_Gosu_31_Enh : String
{
  final static function hello() {}  //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER
  final static property get Age () : Integer { return null} //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER
}