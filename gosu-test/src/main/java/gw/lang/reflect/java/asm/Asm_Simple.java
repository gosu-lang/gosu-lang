/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 */
public class Asm_Simple<S extends List<T>, T extends Comparable<T>> extends ArrayList<Object[]> {
  private int _int;
  private int[] _intArray;
  private int[][] _intArrayArray;
  private String _string;
  private String[] _stringArray;
  private List<String> _listOfString;
  private List<List<String>> _listOfListOfString;
  private List<S> _listOfS;
  private List<?> _listofWild;
  private Map<String, ?> _mapofWild;
  private Map<?, S> _mapofWildS;
  private List<? extends S> _listofWildS;
  private List<? extends List<S>> _listofWildListS;
  private List<? super List<S>> _listofContraWildListS;
  private Map<String, List<String>> _mapStringListOfString;
  private Map<String, InnerClass<String>> _mapStringInnerClassOfString;
  private Map<String, InnerClass<T>> _mapStringInnerClassOfT;

  private InnerClass _yay;
  private InnerClass<T> _yay1;
  private InnerClass<String> _yay2;
  private InnerClass<InnerClass<String>> _yay3;

  private List<String>[] _listOfStringA;
  private List<List<String>>[] _listOfListOfStringA;
  private List<S>[] _listOfSA;
  private List<?>[] _listofWildA;
  private Map<String, ?>[] _mapofWildA;
  private List<? extends S>[] _listofWildSA;
  private List<? extends List<S>>[] _listofWildListSA;
  private List<? super List<S>>[] _listofContraWildListSA;
  private Map<String, List<String>>[] _mapStringListOfStringA;
  private Map<String, InnerClass<String>>[] _mapStringInnerClassOfStringA;

  public int intMethod( int p0 ) {return 0;}
  public int varArgIntMethod( int... p0 ) {return 0;}
  public int varArgStringMethod( String... p0 ) {return 0;}
  public <E> E varArgStringMethod( int... p0 ) {return null;}
  public java.util.List<Object[]> varArgStringMethod() {return null;}
  public int[] intArrayMethod( int[] p0 ) {return null;}
  public int[][] intArrayArrayMethod( int[][] p0 ) {return null;}
  public String[][] stringArrayArrayMethod( String[][] p0 ) {return null;}
  public List<Object[]> enumArrayArrayMethod( Asm_Enum[][] p0, String[] p1, String p2, int[] p3, int p4 ) {return null;}
  public <E> String stringMethod( E p0, String p1 ) {return null;}
  public <E extends List<String>> String[] stringArrayMethod( E e ) {return null;}
  public <E, R extends List<? extends String>> List<String> listOfStringMethod( E e, R r ) {return null;}
  public List<List<String>> listOfListOfStringMethod() {return null;}
  public List<S> listOfSMethod() {return null;}
  public List<?> listofWildMethod() {return null;}
  public Map<String, ?> mapofWildMethod() {return null;}
  public List<? extends S> listofWildSMethod() {return null;}
  public List<? extends List<S>> listofWildListSMethod() {return null;}
  public List<? super List<S>> listofContraWildListSMethod() {return null;}
  public Map<String, List<String>> mapStringListOfStringMethod() {return null;}
  public static <E> HashSet<E> newHashSet(E... elements) {return null;}
  public <E> InnerClass<E> returnsInnerClass( InnerClass p0 ) {return null;}
  public Map<String,byte[]> mapOfStringToPrimitiveByteArray() {return null;}
  public Map<String,byte[][]> mapOfStringToPrimitiveByteArrayArray() {return null;}
  public Map<byte[][], String> mapOfPrimitiveByteArrayArrayToString() {return null;}

  private class InnerClass<F> {}
}
