package org.seasar.doma.gradle.codegen.generator;

import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;
import java.util.Date;

public class OnDemandDateModel implements TemplateDateModel {

  @Override
  public Date getAsDate() throws TemplateModelException {
    return new Date();
  }

  @Override
  public int getDateType() {
    return UNKNOWN;
  }
}
