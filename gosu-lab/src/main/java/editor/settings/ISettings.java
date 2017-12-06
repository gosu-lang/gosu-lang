package editor.settings;

import editor.util.Experiment;
import gw.lang.reflect.json.IJsonIO;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 */
public interface ISettings<T extends ISettingsParameters<T>> extends IJsonIO
{
  default void resetToDefaultSettings( Experiment target )
  {
    setParams( makeDefaultParameters( target ), false );
  }

  default String getParentPath()
  {
    String path = getPath();
    String parentPath = path.substring( 0, path.length() - getName().length() );
    if( parentPath.isEmpty() )
    {
      return "";
    }
    return parentPath.substring( 0, parentPath.length()-1 );
  }

  T getParams();
  void setParams( T params, boolean persistent );
  T makeDefaultParameters( Experiment experiment );

  /**
   * Path consisting of names of ancestors including this: "&lt;root&gt;/../&lt;my-name&gt;"
   */
  String getPath();

  String getName();

  boolean isValid();
  Icon getIcon();

  JComponent makePanel( T params, Consumer<T> changeListener );

  boolean isExperimentSetting();

  boolean isIdeSetting();

  void addChangeListener( BiConsumer<T, T> listener );
}
