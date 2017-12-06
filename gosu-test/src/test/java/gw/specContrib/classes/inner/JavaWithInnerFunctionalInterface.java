package gw.specContrib.classes.inner;

/**
 */
public class JavaWithInnerFunctionalInterface {

  public static void runWith( BlockRunnable block, String msg ) {
    block.run( msg );
  }

  public interface BlockRunnable {
    void run( String bundle );
  }
}