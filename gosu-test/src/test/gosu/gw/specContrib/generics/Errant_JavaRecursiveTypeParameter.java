package gw.specContrib.generics;

import java.util.List;

public interface Errant_JavaRecursiveTypeParameter<T extends Errant_JavaRecursiveTypeParameter> {
  List<T> getChildren();
}