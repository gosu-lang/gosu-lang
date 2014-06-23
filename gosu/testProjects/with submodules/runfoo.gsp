classpath "foo,fooImpl"

uses testpackage.IFoo
uses testpackage.impl.Foo

var foo : IFoo

foo = new Foo()

print(foo.bar())
