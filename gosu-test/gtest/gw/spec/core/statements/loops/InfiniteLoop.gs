package gw.spec.core.statements.loops

uses java.lang.*
uses java.util.concurrent.locks.ReentrantLock

class InfiniteLoop {

  static function whileLoop0() : int {
    while(true) {
      if (true) {
        return 1
      }
    } // PL-25780: if last two instructions are iconst_1 + ifne label, it would cause java.lang.VerifyError
  }

  static function whileLoop1() : int {
    while(true) {
      return 1
    }
  }

  static function whileLoop2() : int {
    var _lock = new ReentrantLock()
    using(_lock) {
      while (true) {
        if (true) {
          return 1
        }
      }
    }
  }

  static function whileLoop3() : int {
    try {
      while (true) {
        if (true) {
          return 1
        }
      }
    }
    catch (t: java.lang.Throwable) {
      throw t
    }
  }


  static function whileLoop4() : int {
    try {
      while(1==1) {
        while (true) {
          if (true) {
            return 1
          }
        }
      }
    }
    catch (t: java.lang.Throwable) {
      throw t
    }
    finally {
       var a = 0
    }
    return 1
  }

  static function whileLoop5() : block(int):int {
    return \x -> {
      while(true) {
        if (true) {
          return 1
        }
      }
    }
  }

  static function doWhileLoop0() : int {
    do {
      if (true) {
        return 1
      }
    } while(true)
  }

  static function doWhileLoop1() : int {
    do {
      return 1
    } while(true)
  }

  static function doWhileLoop2() : int {
    var _lock = new ReentrantLock()
    using(_lock) {
      do {
        if (true) {
          return 1
        }
      } while (true)
    }
  }

  static function doWhileLoop3() : int {
    try {
      do {
        if (true) {
          return 1
        }
      } while (true)
    }
    catch (t: java.lang.Throwable) {
      throw t
    }
  }

  static function doWhileLoop4() : int {
    try {
      do {
        do {
          if (true) {
            return 1
          }
        } while (true)
      } while(1==1)
    }
    catch (t: java.lang.Throwable) {
      throw t
    }
    finally {
       var a = 0
    }
  }

  static function doWhileLoop5() : block(int):int {
    return \x -> {
      do {
        if (true) {
          return 1
        }
      } while(true)
    }
  }
}
