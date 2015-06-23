/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.ide.ApplicationLoadListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.idea.IdeaApplication;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationStarter;
import com.intellij.openapi.diagnostic.Logger;
import gw.config.CommonServices;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GosuApplicationStarter implements ApplicationStarter, ApplicationLoadListener {
  private static final Logger LOG = Logger.getInstance(GosuApplicationStarter.class);

  public static final String COM_GUIDEWIRE = "com.guidewire.";
  public static final String COM_GUIDEWIRE_GOSU = COM_GUIDEWIRE + "gosu";
  public static final String COM_GUIDEWIRE_GOSU_INTERNAL = COM_GUIDEWIRE + "gosu";

  private ApplicationStarter defaultStarter;

  public GosuApplicationStarter() {
    Class<?> aClass = lookupStarterClass( "com.intellij.idea.IdeaUltimateApplication", "IdeaUltimateStarter" );
    if (aClass == null) {
      aClass = lookupStarterClass( "com.intellij.idea.IdeaApplication", "IdeStarter" );
    }

    try {
      final Constructor<?> constructor = aClass.getDeclaredConstructors()[0];
      constructor.setAccessible(true);
      defaultStarter = (ApplicationStarter) constructor.newInstance(IdeaApplication.getInstance());
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  @Override
  public void beforeApplicationLoaded(Application application) {
    // When started without "gosu" command line argument
    fixClassLoaders();
  }

  private Class<?> lookupStarterClass(String appClass, String starterClassName) {
    try {
      // IDEA Ultimate
      Class<?> aClass = Class.forName( appClass );
      for (Class<?> starterClass : aClass.getDeclaredClasses()) {
        if (starterClass.getSimpleName().equals(starterClassName)) {
          return starterClass;
        }
      }
    } catch ( ClassNotFoundException ex ) {
      // Just ignore
    }
    return null;
  }

  @NotNull
  @Override
  public String getCommandName() {
    return "gosu";
  }

  @Override
  public void premain(String[] args) {
    removeCommandLineArgument();
    // When started with "gosu" command line argument
    fixClassLoaders();
    defaultStarter.premain(args);
  }

  public static void fixClassLoaders() {
    // Only fix if we are running in regular IntelliJ (not in tests)
    PluginClassLoader mainClassLoader = (PluginClassLoader) CommonServices.getEntityAccess().getPluginClassLoader();
    for (IdeaPluginDescriptor plugin : PluginManager.getPlugins()) {
      String id = plugin.getPluginId().getIdString();
      if (id.startsWith(COM_GUIDEWIRE) && !id.equals(COM_GUIDEWIRE_GOSU_INTERNAL)) {
        if (id.equals(COM_GUIDEWIRE_GOSU)) {
          LOG.warn(
                  String.format(
                          "Loading open-source Gosu plugin (id=%s). " +
                                  "In Ferrite we expect to use \"internal\" Gosu plugin with Studio instead." +
                                  "Are you sure IDEA is configured properly?",
                          COM_GUIDEWIRE_GOSU));
        }
        recoverParents(plugin, mainClassLoader);
        recoverClasspath(plugin, mainClassLoader);
        ((IdeaPluginDescriptorImpl)plugin).setLoader(mainClassLoader);
      }
    }
  }

  private static void recoverParents(IdeaPluginDescriptor plugin, PluginClassLoader mainPluginPluginClassLoader) {
    try {
      final Field field = IdeaReflectionUtil.getField( "myParents", "a", PluginClassLoader.class );
      field.setAccessible(true);

      List<ClassLoader> mainParents = new ArrayList<>(Arrays.asList((ClassLoader[]) field.get(mainPluginPluginClassLoader)));
      for (ClassLoader cl : (ClassLoader[])field.get(plugin.getPluginClassLoader())) {
        if (!mainParents.contains(cl) && !((PluginClassLoader)cl).getPluginId().getIdString().startsWith(COM_GUIDEWIRE)) {
          mainParents.add(cl);
        }
      }
      field.set(mainPluginPluginClassLoader, mainParents.toArray(new ClassLoader[mainParents.size()]));
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  private static void recoverClasspath(@NotNull IdeaPluginDescriptor plugin, @NotNull PluginClassLoader mainClassLoader) {
    HashSet<URL> existingURLs = new HashSet<>(mainClassLoader.getUrls());
    PluginClassLoader pluginClassLoader;
    try {
      pluginClassLoader = (PluginClassLoader) plugin.getPluginClassLoader();
    } catch (ClassCastException e) {
      LOG.error(String.format(
              "Provided plugin %s (%s) has class loader of type %s, but PluginClassLoader is expected",
              plugin.getName(), plugin.getPluginId().toString(), plugin.getPluginClassLoader().getClass().toString()
      ));
      throw e;
    }
    for (URL url : pluginClassLoader.getUrls()) {
      if (!existingURLs.contains(url)) {
        mainClassLoader.addURL(url);
      }
    }
  }

  private void removeCommandLineArgument() {
    try {
      final Field field = IdeaReflectionUtil.getField( "myArgs", "a", IdeaApplication.class );
      field.setAccessible(true);
      field.set(IdeaApplication.getInstance(), new String[0]);
    } catch (Exception e) {
      LOG.error(e);
    }
  }

  @Override
  public void main(String[] args) {
    defaultStarter.main(new String[0]);
  }
}
