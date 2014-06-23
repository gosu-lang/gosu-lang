package gw.internal.gosu.parser.structural

uses gw.testharness.DoNotVerifyResource
uses java.lang.CharSequence
uses java.lang.Comparable
uses java.io.Serializable
uses java.lang.StringBuilder
uses java.lang.Runnable

@DoNotVerifyResource
class Errant_JavaTypeAssignabilityTest {
  structure ComparableStringStruct extends Comparable<String> {
  }

  structure ComparableCharSequenceStruct extends Comparable<CharSequence> {
  }

  structure StringInterfacesStruct extends Serializable, Comparable<String>, CharSequence {
  }


  function assignabilityComparableStringStruct() {
    var struct: ComparableStringStruct
    var str: String
    // Ok
    struct = str
  }

  function assignabilityComparableCharSequenceStruct() {
    var struct: ComparableCharSequenceStruct
    var str: String
    // Error, compareTo( String ) is covariant, it needs to be contravarient
    struct = str  //## issuekeys: MSG_TYPE_MISMATCH
    var cs: Comparable<CharSequence>
    // Ok
    struct = cs
    var o: Comparable<Object>
    // Ok
    struct = o
  }

  function assignabilityStringInterfacesStruct() {
    var struct: StringInterfacesStruct
    var str: String
    // Ok
    struct = str

    var allIfaces : Serializable & Comparable<String> & CharSequence
    // Ok
    struct = allIfaces

    var allIfacesMinusSerializable : Comparable<String> & CharSequence
    // Ok
    struct = allIfacesMinusSerializable

    var contravariantComparable : Comparable<CharSequence> & CharSequence
    // Ok
    struct = contravariantComparable

    var justComparableString : Comparable<String>
    // Error
    struct = justComparableString  //## issuekeys: MSG_TYPE_MISMATCH

    var wrongComparable : Serializable & Comparable<StringBuilder> & CharSequence
    // Error
    struct = wrongComparable  //## issuekeys: MSG_TYPE_MISMATCH

    var additionalIface : Serializable & Comparable<String> & CharSequence & Runnable
    // Ok
    struct = additionalIface
  }
}