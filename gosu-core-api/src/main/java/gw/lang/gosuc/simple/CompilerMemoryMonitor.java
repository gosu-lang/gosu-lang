package gw.lang.gosuc.simple;

//import com.guidewire.pl.system.dependency.PLDependencies;
import gw.config.BaseService;
import gw.config.IMemoryMonitor;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class CompilerMemoryMonitor extends BaseService implements IMemoryMonitor {
  private static double usedMemoryRatio;

  public CompilerMemoryMonitor() {
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      private LinkedList<Double> stack = new LinkedList<Double>();

      public void run() {
        Runtime runtime = Runtime.getRuntime();
        double freeMem = runtime.freeMemory() * 1e-9;
        double totalMem = runtime.totalMemory() * 1e-9;
        double maxMem = runtime.maxMemory() * 1e-9;
        double usedMem = totalMem - freeMem;
        double ratio = usedMem / maxMem;

        stack.addFirst(ratio);
        if (stack.size() > 5) {
          stack.removeLast();
        }

        double averageRatio = 0;
        for (Double v : stack) {
          averageRatio += v;
        }
        usedMemoryRatio = averageRatio / stack.size();
      }
    }, 1000, 1000);
  }

  @Override
  public void reclaimMemory(RefreshRequest request) {
//      if (usedMemoryRatio > 0.75) {
//        request.setLimitlessHunt();
//      }
    if (usedMemoryRatio > 0.90) {
//      System.out.print(ratio + " -> ");
//      System.gc();
//      freeMem = runtime.freeMemory();
//      totalMem = runtime.maxMemory();
//      ratio = (totalMem - freeMem) / totalMem;
//      System.out.println(ratio);
      if (usedMemoryRatio > 0.90) {
//        System.out.println("Doing deep clean.");
//        request.setLimitlessHunt();
        TypeSystem.refresh(true);
        //TODO: isabinin: check this
//        PLDependencies.getPCFService().clearNodeMap();
        System.gc();
      }
    }
  }

}
