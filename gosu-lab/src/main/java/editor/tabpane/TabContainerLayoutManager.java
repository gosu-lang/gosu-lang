package editor.tabpane;

import javax.swing.*;
import java.awt.*;

/**
 */
public class TabContainerLayoutManager implements LayoutManager
{
  public void removeLayoutComponent( Component comp )
  {
  }

  public void addLayoutComponent( String name, Component comp )
  {
  }

  public void layoutContainer( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    TabPosition tabPosition = tabContainer.getTabPosition();
    if( tabPosition == TabPosition.TOP )
    {
      layoutContainerTop( tabContainer );
    }
    else if( tabPosition == TabPosition.BOTTOM )
    {
      layoutContainerBottom( tabContainer );
    }
    else if( tabPosition == TabPosition.LEFT )
    {
      layoutContainerLeft( tabContainer );
    }
    else if( tabPosition == TabPosition.RIGHT )
    {
      layoutContainerRight( tabContainer );
    }
  }

  public Dimension minimumLayoutSize( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    TabPosition tabPosition = tabContainer.getTabPosition();
    if( tabPosition == TabPosition.TOP ||
        tabPosition == TabPosition.BOTTOM )
    {
      return minimumLayoutSizeHorizontal( tabContainer );
    }
    else
    {
      return minimumLayoutSizeVertical( tabContainer );
    }
  }

  public Dimension preferredLayoutSize( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    TabPosition tabPosition = tabContainer.getTabPosition();
    if( tabPosition == TabPosition.TOP ||
        tabPosition == TabPosition.BOTTOM )
    {
      return preferredLayoutSizeHorizontal( tabContainer );
    }
    else
    {
      return preferredLayoutSizeVertical( tabContainer );
    }
  }

  private Dimension preferredLayoutSizeHorizontal( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    if( tabs.length == 0 )
    {
      return new Dimension( 0, 0 );
    }

    int iWidth = 0;
    int iHeight = 0;
    for( int i = 0; i < tabs.length; i++ )
    {
      Component comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      iHeight = Math.max( pref.height, iHeight );
      if( i+1 < tabs.length )
      {
        iWidth += pref.width - pref.height/2;
      }
      else
      {
        iWidth += pref.width;
      }
    }
    return new Dimension( iWidth, iHeight );
  }

  private Dimension preferredLayoutSizeVertical( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    if( tabs.length == 0 )
    {
      return new Dimension( 0, 0 );
    }

    int iWidth = 0;
    int iHeight = 0;
    for( int i = 0; i < tabs.length; i++ )
    {
      Component comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      iWidth = Math.max( pref.width, iWidth );
      if( i+1 < tabs.length )
      {
        iHeight += 2 + pref.height - pref.width/2;
      }
      else
      {
        iHeight += 2 + pref.height;
      }
    }
    return new Dimension( iWidth, iHeight );
  }

  private void layoutContainerTop( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    int iX = 0;
    int iParentFloor = parent.getHeight();
    boolean bShowMore = false;
    MoreTab moreTab = tabContainer.getMoreTab();
    for( int i = 0; i < tabs.length; i++ )
    {
      JComponent comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      comp.setBounds( iX, iParentFloor - pref.height, pref.width, pref.height );
      iX += pref.width - pref.height/2;
      comp.setVisible( true );
      if( !bShowMore && comp.getWidth() + comp.getX() > parent.getWidth() )
      {
        bShowMore = true;
        Dimension dimMore = moreTab.getPreferredSize();
        moreTab.setBounds( parent.getWidth() - dimMore.width, iParentFloor - pref.height - 1, dimMore.width, pref.height );
        if( parent.getComponentZOrder( moreTab ) > parent.getComponentZOrder( comp ) )
        {
          parent.add( moreTab, parent.getComponentZOrder( comp ) );
        }
        iX += dimMore.width;
      }
    }
    moreTab.setVisible( bShowMore );
  }

  private void layoutContainerBottom( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    int iX = 0;
    boolean bShowMore = false;
    MoreTab moreTab = tabContainer.getMoreTab();
    for( int i = 0; i < tabs.length; i++ )
    {
      JComponent comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      comp.setBounds( iX, 0, pref.width, pref.height );
      iX += pref.width - pref.height/2;
      comp.setVisible( true );
      if( !bShowMore && comp.getWidth() + comp.getX() > parent.getWidth() )
      {
        bShowMore = true;
        Dimension dimMore = moreTab.getPreferredSize();
        moreTab.setBounds( parent.getWidth() - dimMore.width, 2, dimMore.width, pref.height );
        if( parent.getComponentZOrder( moreTab ) > parent.getComponentZOrder( comp ) )
        {
          parent.add( moreTab, parent.getComponentZOrder( comp ) );
        }
        iX += dimMore.width;
      }
    }
    moreTab.setVisible( bShowMore );
  }

  private void layoutContainerLeft( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    int iY = 0;
    int iParentRigthWall = parent.getWidth();
    boolean bShowMore = false;
    MoreTab moreTab = tabContainer.getMoreTab();
    for( int i = 0; i < tabs.length; i++ )
    {
      JComponent comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      comp.setBounds( iParentRigthWall - pref.width, iY, pref.width, pref.height );
      iY += pref.height - pref.width/2;
      comp.setVisible( true );
      if( !bShowMore && comp.getHeight() + comp.getY() > parent.getHeight() )
      {
        bShowMore = true;
        Dimension dimMore = moreTab.getPreferredSize();
        moreTab.setBounds( iParentRigthWall - pref.width, parent.getHeight() - dimMore.height, pref.width, dimMore.height );
        if( parent.getComponentZOrder( moreTab ) > parent.getComponentZOrder( comp ) )
        {
          parent.add( moreTab, parent.getComponentZOrder( comp ) );
        }
        iY += dimMore.height;
      }
    }
    moreTab.setVisible( bShowMore );
  }

  private void layoutContainerRight( Container parent )
  {
    TabContainer tabContainer = (TabContainer)parent;
    ITab[] tabs = tabContainer.getTabs();
    int iY = 0;
    boolean bShowMore = false;
    MoreTab moreTab = tabContainer.getMoreTab();
    for( int i = 0; i < tabs.length; i++ )
    {
      JComponent comp = tabs[i].getComponent();
      Dimension pref = comp.getPreferredSize();
      comp.setBounds( 0, iY, pref.width, pref.height );
      iY += pref.height - pref.width/2;
      comp.setVisible( true );
      if( !bShowMore && comp.getHeight() + comp.getY() > parent.getHeight() )
      {
        bShowMore = true;
        Dimension dimMore = moreTab.getPreferredSize();
        moreTab.setBounds( 0, parent.getHeight() - dimMore.height, pref.width, dimMore.height );
        if( parent.getComponentZOrder( moreTab ) > parent.getComponentZOrder( comp ) )
        {
          parent.add( moreTab, parent.getComponentZOrder( comp ) );
        }
        iY += dimMore.height;
      }
    }
    moreTab.setVisible( bShowMore );
  }

  private Dimension minimumLayoutSizeHorizontal( Container parent )
  {
    Component[] comps = parent.getComponents();
    if( comps.length == 0 )
    {
      return new Dimension( 0, 0 );
    }

    int iWidth = 0;
    int iHeight = 0;
    for( int i = 0; i < comps.length; i++ )
    {
      Component comp = comps[i];
      Dimension min = comp.getMinimumSize();
      iHeight = Math.max( min.height, iHeight );
      iWidth += min.width - min.height/2;
    }
    return new Dimension( iWidth, iHeight );
  }

  private Dimension minimumLayoutSizeVertical( Container parent )
  {
    Component[] comps = parent.getComponents();
    if( comps.length == 0 )
    {
      return new Dimension( 0, 0 );
    }

    int iWidth = 0;
    int iHeight = 0;
    for( int i = 0; i < comps.length; i++ )
    {
      Component comp = comps[i];
      Dimension min = comp.getMinimumSize();
      iWidth = Math.max( min.width, iWidth );
      iHeight += min.height - min.width/2;
    }
    return new Dimension( iWidth, iHeight );
  }
}
