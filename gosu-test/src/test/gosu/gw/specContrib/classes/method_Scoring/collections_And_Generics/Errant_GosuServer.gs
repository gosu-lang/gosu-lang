package gw.specContrib.classes.method_Scoring.collections_And_Generics


class Errant_GosuServer extends JavaBaseServer {
  function main() {
    //IDE-957
    foo({1, 2})  // error appears/disappears here
  }
}