package gw.util.concurrent;

import java.lang.Thread;
import java.util.Random;

/**
 * @author carson
 */
public class CacheLiveLockTest {

  private static int RANDOM_BOUNDARY = 20;

  public static void main(String[] args){
    // demonstrates the live lock scenario
    runLiveLockTestWithCacheOfSize(10);

    // demonstrates no live lock when sized up
    // runLiveLockTestWithCacheOfSize(100);
  }

  private static void runLiveLockTestWithCacheOfSize(int cacheSize) {
    Random r = new Random();

    Cache<String, String> cache = new Cache<>("test", cacheSize, str1 -> {
      return str1;
    });

    for (int i = 0; i < 20; i++) {
      print("Starting Update Thread " + i);
      new Thread(() -> {
        while (true) {
          int randomInt = r.nextInt(RANDOM_BOUNDARY);
          cache.get(String.valueOf(randomInt));
          if (randomInt % (r.nextInt(RANDOM_BOUNDARY) + 1) == 0) {
            cache.evict(String.valueOf(randomInt));
          }
        }
      }, "Cache Updating Thread " + i).start();
    }

    new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(10000);
          print("Cache Status: " + cache);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }, "Cache Status Thread").start();
  }

  private static void print(String x) {
    System.out.println(x);
  }

}


