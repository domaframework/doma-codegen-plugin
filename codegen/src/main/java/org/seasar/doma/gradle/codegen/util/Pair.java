package org.seasar.doma.gradle.codegen.util;

public class Pair<FIRST, SECOND> {

  private final FIRST first;

  private final SECOND second;

  public Pair(FIRST first, SECOND second) {
    super();
    this.first = first;
    this.second = second;
  }

  public FIRST getFirst() {
    return first;
  }

  public SECOND getSecond() {
    return second;
  }
}
