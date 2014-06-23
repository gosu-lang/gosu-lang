/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvocationCounter {  
  private Map<String, Count> map = new HashMap<String, Count>();
  private List<String> list = new ArrayList<String>();
  private boolean _storeList;

  public InvocationCounter(boolean storeList) {
    _storeList = storeList;
  }
    
  public void recordInvocation(String description, int n) {
    description.replace("\n", "\\n");
    Count c = map.get(description);
    if (c == null) {
      c = new Count();
      c.description = description;
      map.put(description, c);
    }
    c.count += n;
    if (_storeList) {
      list.add(description);
    }
  }
  
  public void recordInvocation(String description) {
    recordInvocation(description, 1);
  }

  public void print() {
    print(System.out);
  }

  public void print(PrintStream stream) {
    List<Count> counts = new ArrayList<Count>(map.values());
    Collections.sort(counts);
    int total = 0;
    for (Count c : counts) {
      total += c.count;
    }

    for (Count c : counts) {
      stream.println(c.toString(total));
    }
    stream.println("\n\n Total: " + total);
  }

  public void printList() {
    printList(System.out);
  }

  public void saveListTo(final String fileName) {
    try {
      PrintStream stream = new PrintStream(new File(fileName));
      printList(stream);
      stream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void printList(PrintStream stream) {
    for (String s : list) {
      stream.println(s);
    }
  }

  public void saveTo(final String fileName) {
    int i = 1;
    String uniqueFileName = fileName;
    while (new File(uniqueFileName).exists()) {
      int index = fileName.indexOf('.');
      if (index < 0) {
        uniqueFileName = fileName + i;
      } else {        
        uniqueFileName = fileName.substring(0, index) + i + fileName.substring(index);
      }
      i++;
    }
    try {
      PrintStream stream = new PrintStream(new File(uniqueFileName));
      print(stream);
      stream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }  
  }

  public void clear() {
    map.clear();
  }
  
  private static class Count implements Comparable<Count> {
    public String description;
    public int count;
    
    public int compareTo(Count c) {
      return c.count - count;
    }
    
    public boolean equals(Object o) {
      if (o instanceof Count) {
        return description.equals(((Count)o).description);
      } else {
        return false;
      }
    }
    
    public String toString() {
      StringBuilder s = new StringBuilder(256);
      s.append(count);
      int n = s.length();
      for (int i = 0; i < 15 - n; i++) {
        s.append(' '); 
      }
      s.append(description);
      return s.toString();
    }

    public String toString(int total) {
      StringBuilder s = new StringBuilder(256);
      s.append(count);
      s.append(" (");
      s.append(new DecimalFormat("##.#").format((100.0 * count)/total));
      s.append("%)");
      int n = s.length();
      for (int i = 0; i < 20 - n; i++) {
        s.append(' '); 
      }
      s.append(description);
      return s.toString();
    }
}

}
