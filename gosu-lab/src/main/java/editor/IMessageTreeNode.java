package editor;

/**
 */
public interface IMessageTreeNode
{
  boolean hasTarget();
  void jumpToTarget();

  static IMessageTreeNode empty()
  {
    return new IMessageTreeNode() {

      @Override
      public boolean hasTarget()
      {
        return false;
      }

      @Override
      public void jumpToTarget()
      {

      }
    };
  }
}
