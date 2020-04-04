package org.seasar.doma.gradle.codegen.exception;

import org.seasar.doma.gradle.codegen.message.MessageResource;

public class CodeGenException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected final MessageResource messageResource;

  protected final Object args;

  public CodeGenException(MessageResource messageResource, Object... args) {
    this(messageResource, null, args);
  }

  public CodeGenException(MessageResource messageResource, Throwable cause, Object... args) {
    super(messageResource.getMessage(args), cause);
    this.messageResource = messageResource;
    this.args = args;
  }

  public MessageResource getMessageResource() {
    return messageResource;
  }

  public Object getArgs() {
    return args;
  }
}
