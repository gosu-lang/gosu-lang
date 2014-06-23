/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.core.PluginLoaderUtil;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadTypesProfilingAction extends TypeSystemAwareAction {
  static Executor executor = new ThreadPoolExecutor(
      1, 1, 1000000, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>(),
      new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread t = new Thread(r, "Profiler Thread");
          t.setDaemon(false);
          return t;
        }
      });

  public void actionPerformed(AnActionEvent e) {
    final List<String> typeNames = new ArrayList<String>();
    final List<IModule> modules = new ArrayList<IModule>();
    try {
      LineNumberReader reader = new LineNumberReader(new FileReader("c:\\request-list.txt"));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] split = line.split(" - ");
        IModule module = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( e.getProject() ) ).getModule(split[1].trim());
        modules.add(module);
        typeNames.add(split[0].trim());
      }
      reader.close();
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    executor.execute(new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runReadAction(new Runnable() {
          public void run() {
            runWithTiming("Loading types", 0, new Runnable() {
              public void run() {
                int size = typeNames.size();
                for (int i = 0; i < size; i++) {
                  IModule gosuModule = modules.get(i);
                  TypeSystem.pushModule(gosuModule);
                  try {
                    IType type = TypeSystem.getByFullNameIfValid(typeNames.get(i));
                  } finally {
                    TypeSystem.popModule(gosuModule);
                  }
                }
              }
            });
          }
        });
      }
    });
  }

  public static void runWithTiming(String description, double thresholdSecs, @NotNull Runnable runnable) {
    long start = System.nanoTime();
    try {
      runnable.run();
    } finally {
      printTiming(description, thresholdSecs, start);
    }
  }

}
