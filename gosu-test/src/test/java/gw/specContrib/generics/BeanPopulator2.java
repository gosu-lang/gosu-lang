package gw.specContrib.generics;

public interface BeanPopulator2<T extends CharSequence> {
  void execute(T Bean);
}