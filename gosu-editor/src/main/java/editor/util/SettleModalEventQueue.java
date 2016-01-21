package editor.util;

import java.awt.*;

/**
 * This is a utility for basically pumping messages in-place until there are
 * none left in the queue. It's very handy for cases where one or more messages
 * are in the queue as a result of prior calls to EventQueue.invokeLater() and
 * you need those messages to be processed before you check state or whatever.
 * The idea is that you want to distribute all those messages and messages
 * resulting from distributing them etc. When there are no more messages in the
 * queue you can proceed with whatever.
 * <p/>
 * Simply call SettleModelEventQueue.instance().run()
 */
public class SettleModalEventQueue extends ModalEventQueue
{
  private static final SettleModalEventQueue INSTANCE = new SettleModalEventQueue();

  public static SettleModalEventQueue instance()
  {
    return INSTANCE;
  }

  private SettleModalEventQueue()
  {
    super( new SettleEventsHandler() );
  }

  private static class SettleEventsHandler implements IModalHandler
  {
    public boolean isModal()
    {
      // Keep pumping messages until there are none left in the queue.
      return Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() != null;
    }
  }
}
