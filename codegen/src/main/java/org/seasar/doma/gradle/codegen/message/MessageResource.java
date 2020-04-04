package org.seasar.doma.gradle.codegen.message;

public interface MessageResource {

  String getCode();

  String getMessagePattern();

  String getMessage(Object... args);
}
