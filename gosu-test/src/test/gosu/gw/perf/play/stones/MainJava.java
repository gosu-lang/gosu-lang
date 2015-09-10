package gw.perf.play.stones;

import java.lang.reflect.Method;

/**
 */
public class MainJava {
  public static void main( String[] args ) throws Exception {
    Class<?> aClass = Class.forName( "play.stones.BoardFrame" );
    Object o = aClass.newInstance();
    aClass.getMethod( "setVisible", boolean.class ).invoke( o, true );
  }
}
