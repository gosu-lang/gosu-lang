/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.util;

import gw.util.StreamUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TestTimingData {

  public static void main(String[] args) {
    TestTimingData timingData = readDataFromFile(new File(args[0]));
    timingData.printStatistics();
  }

  public static TestTimingData readDataFromFile(File f) {
    TestTimingData timingData = new TestTimingData();
    timingData.populateFromFile(f);
    return timingData;
  }

  private Map<String, ClassTimingData> _classTimingData = new HashMap<String, ClassTimingData>();
  private List<ClassTimingData> _orderedClasses = new ArrayList<ClassTimingData>();
  private List<MethodTimingData> _allMethods = new ArrayList<MethodTimingData>();
  private long _totalSuiteTime;

  private TestTimingData() {}

  public void printStatistics() {
    long totalClassRunningTime = 0;
    for (ClassTimingData ctd : _orderedClasses) {
      totalClassRunningTime += ctd._time;
    }

    System.out.println("Total suite time:                " + formatNanoTime(_totalSuiteTime));
    System.out.println("Total suite startup/teardown:    " + formatNanoTime(_totalSuiteTime - totalClassRunningTime));
    System.out.println("Num test classes:                " + _orderedClasses.size());
    System.out.println("Total test class time:           " + formatNanoTime(totalClassRunningTime));
    System.out.println("Average test class time:         " + formatNanoTime(totalClassRunningTime / _orderedClasses.size()));

    List<ClassTimingData> classesOrderedByTime = sortClassesByTime();
    int medianClassIndex = classesOrderedByTime.size() / 2;
    int percentileClassIndex =(int) (0.05 * classesOrderedByTime.size());
    long percentileClassRunningTime = 0;
    for (int i = 0; i <= percentileClassIndex; i++) {
      percentileClassRunningTime += classesOrderedByTime.get(i).getTime();
    }
    System.out.println("Median test class time:          " + formatNanoTime(classesOrderedByTime.get(medianClassIndex).getTime()));
    System.out.println("95th percentile test class time: " + formatNanoTime(classesOrderedByTime.get(percentileClassIndex).getTime()));
    System.out.println("Time taken by slowest 5%:        " + formatNanoTime(percentileClassRunningTime));
    System.out.println("Percentage taken by slowest 5%:  " + formatPercentage((double) percentileClassRunningTime / (double) totalClassRunningTime));

    List<MethodTimingData> methodsOrderedByTime = sortMethodsByTime();
    int medianMethodIndex = methodsOrderedByTime.size() / 2;
    int percentileMethodIndex =(int) (0.05 * methodsOrderedByTime.size());
    System.out.println("Median method time:              " + formatNanoTime(methodsOrderedByTime.get(medianMethodIndex).getTime()));
    System.out.println("95th percentile method time:     " + formatNanoTime(methodsOrderedByTime.get(percentileMethodIndex).getTime()));

    System.out.println("");
    System.out.println("Slowest 5% of test classes:");
    for (int i = 0; i <= percentileClassIndex; i++) {
      ClassTimingData ctd = classesOrderedByTime.get(i);
      System.out.println(formatNanoTime(ctd._time) + "   " + ctd._className);
    }

    System.out.println("");
    System.out.println("Slowest 5% of test methods:");
    for (int i = 0; i <= percentileMethodIndex; i++) {
      MethodTimingData mtd = methodsOrderedByTime.get(i);
      System.out.println(formatNanoTime(mtd.getTime()) + "   " + mtd.getClassName() + " " + mtd.getMethodName());
    }

    System.out.println("");
    System.out.println("All test classes:");
    for (ClassTimingData ctd : classesOrderedByTime) {
      System.out.println(formatNanoTime(ctd._time) + "   " + ctd._className);
    }
  }

  private List<ClassTimingData> sortClassesByTime() {
    List<ClassTimingData> classesOrderedByTime = new ArrayList<ClassTimingData>(_orderedClasses);
    Collections.sort(classesOrderedByTime, new Comparator<ClassTimingData>() {
      @Override
      public int compare(ClassTimingData o1, ClassTimingData o2) {
        if (o1._time > o2._time) {
          return -1;
        } else if (o1._time == o2._time) {
          return 0;
        } else {
          return 1;
        }
      }
    });

    return classesOrderedByTime;
  }

  private List<MethodTimingData> sortMethodsByTime() {
    List<MethodTimingData> methodsOrderedByTime = new ArrayList<MethodTimingData>(_allMethods);
    Collections.sort(methodsOrderedByTime, new Comparator<MethodTimingData>() {
      @Override
      public int compare(MethodTimingData o1, MethodTimingData o2) {
        if (o1._time > o2._time) {
          return -1;
        } else if (o1._time == o2._time) {
          return 0;
        } else {
          return 1;
        }
      }
    });

    return methodsOrderedByTime;
  }

  private String formatNanoTime(long ns) {
    return (ns / 1000000) + " ms";
  }

  private String formatPercentage(double d) {
    NumberFormat format = NumberFormat.getPercentInstance();
    format.setMinimumFractionDigits(2);
    return format.format(d);
  }

  private void populateFromFile(File f) {
    try {
      List<String> fileLines = readLines(f);

      for (String line : fileLines) {
        if (line.startsWith("***** TestRunTime")) {
          String message = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
          String time = line.substring(line.indexOf(']') + 2, line.lastIndexOf("*****") - 1);
          processTestLine(message, time);
        }       
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void processTestLine(String message, String timeStr) {
    long time = Long.parseLong(timeStr);
    if (message.startsWith("Method ")) {
      String className = message.substring(message.indexOf(' ') + 1, message.lastIndexOf(' '));
      String methodName = message.substring(message.lastIndexOf(' ') + 1, message.length());
      MethodTimingData mtd = new MethodTimingData(methodName, className, time);
      _allMethods.add(mtd);
      getOrCreateClassTimingData(className).addMethod(mtd);
    } else if (message.startsWith("Class ")) {
      String className = message.substring(message.lastIndexOf(' ') + 1, message.length());
      getOrCreateClassTimingData(className).setTime(time);
    } else if (message.startsWith("Suite")) {
      _totalSuiteTime = time;
    } else {
      throw new IllegalArgumentException("Unrecognized timing message " + message);
    }
  }

  private ClassTimingData getOrCreateClassTimingData(String className) {
    ClassTimingData classTimingData = _classTimingData.get(className);
    if (classTimingData == null) {
      classTimingData = new ClassTimingData(className);
      _orderedClasses.add(classTimingData);
      _classTimingData.put(className, classTimingData);
    }

    return classTimingData;
  }

  private List<String> readLines(File f) throws IOException {
    List<String> fileLines = new ArrayList<String>();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
    String line = bufferedReader.readLine();
    while (line != null) {
      fileLines.add(line);
      line = bufferedReader.readLine();
    }
    return fileLines;
  }

  private static class ClassTimingData {
    private String _className;
    private long _time;
    private List<MethodTimingData> _methods = new ArrayList<MethodTimingData>();

    private ClassTimingData(String className) {
      _className = className;
    }

    public void setTime(long time) {
      _time = time;
    }

    public void addMethod(MethodTimingData method) {
      _methods.add(method);
    }

    public String getClassName() {
      return _className;
    }

    public long getTime() {
      return _time;
    }

    public List<MethodTimingData> getMethods() {
      return _methods;
    }
  }

  private static class MethodTimingData {
    private String _methodName;
    private String _className;
    private long _time;

    private MethodTimingData(String methodName, String className, long time) {
      _methodName = methodName;
      _className = className;
      _time = time;
    }

    public String getMethodName() {
      return _methodName;
    }

    public String getClassName() {
      return _className;
    }

    public long getTime() {
      return _time;
    }
  }
}
