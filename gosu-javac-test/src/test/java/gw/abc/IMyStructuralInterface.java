package gw.abc;

import manifold.ext.rt.api.Structural;

@Structural
public interface IMyStructuralInterface
{
  String getName();
  void setName(String name);

  int getAge();
  void setAge(int age);

  String foo(String arg);
}
