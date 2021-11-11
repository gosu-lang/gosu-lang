package gw.internal.gosu.parser.generics.gwtest.java;

// an incorrectly defined recursive type using a wildcard (s/b T extends WrongRecursiveType<T>)
public class WrongRecursiveType<T extends WrongRecursiveType<?>>
{
}
