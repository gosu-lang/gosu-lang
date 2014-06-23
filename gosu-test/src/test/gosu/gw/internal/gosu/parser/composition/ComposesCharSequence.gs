package gw.internal.gosu.parser.composition
uses java.lang.CharSequence

class ComposesCharSequence implements CharSequence
{
  delegate _l represents CharSequence

  construct()
  {
    _l = "hello" 
  }
}