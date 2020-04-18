package gw.abc

class GosuExtendsJavaBase extends JavaBase {
  property Id: String

  construct(name: String, id: StringBuilder) {
    super(name)
    _Id = id.toString()
  }

  protected override function baseMethod(num: int) : String {
    return String.valueOf( num )
  }
}