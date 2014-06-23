classpath "support"

uses testpackage.TestOptions
uses gw.lang.cli.CommandLineAccess

CommandLineAccess.initialize(TestOptions)

print("TestOptions.Foo: " + TestOptions.Foo)
print("TestOptions.Bar: " + TestOptions.Bar)
