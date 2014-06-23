/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.awt.*;

/**
 */
public class TetrisTest extends ByteCodeTestBase
{
  public void testTetris() throws Exception
  {
    if( false )
    {
      EventQueue.invokeLater(
        new Runnable()
        {
          @Override
          public void run()
          {
            try
            {
              Object obj = newTestArrayAccess();
            }
            catch( Exception e )
            {
              throw new RuntimeException( e );
            }
          }
        });
      Thread.sleep( 60000 );
    }
  }

  private Object newTestArrayAccess() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.tetris.Tetris";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    return javaClass.newInstance();
  }
}