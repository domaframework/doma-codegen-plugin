package org.seasar.doma.gradle.codegen.exception;

import org.seasar.doma.gradle.codegen.message.Message;

public class CodeGenNullPointerException extends CodeGenException {

  private static final long serialVersionUID = 1L;

  protected final String parameterName;

  public CodeGenNullPointerException(String parameterName) {
    super(Message.DOMAGEN0001, parameterName);
    this.parameterName = parameterName;
  }

  public String getParameterName() {
    return parameterName;
  }
}
