package org.seasar.doma.gradle.codegen.generator;

import static freemarker.template.Configuration.VERSION_2_3_30;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Generator {

  protected static final String DEFAULT_TEMPLATE_DIR_NAME =
      "org/seasar/doma/gradle/codegen/template";

  private final Logger logger = LoggerFactory.getLogger(Generator.class);

  protected final Configuration configuration;

  protected Generator() {
    this("UTF-8", null);
  }

  public Generator(String templateEncoding, File templatePrimaryDir) {
    if (templateEncoding == null) {
      throw new NullPointerException("templateFileEncoding");
    }
    this.configuration = new Configuration(VERSION_2_3_30);
    configuration.setObjectWrapper(new DefaultObjectWrapper(VERSION_2_3_30));
    configuration.setSharedVariable("currentDate", new OnDemandDateModel());
    configuration.setEncoding(Locale.getDefault(), templateEncoding);
    configuration.setNumberFormat("0.#####");
    configuration.setTemplateLoader(createTemplateLoader(templatePrimaryDir));
  }

  protected TemplateLoader createTemplateLoader(File templateFilePrimaryDir) {
    TemplateLoader primary = null;
    if (templateFilePrimaryDir != null) {
      try {
        primary = new FileTemplateLoader(templateFilePrimaryDir);
      } catch (IOException e) {
        throw new CodeGenException(Message.DOMAGEN9001, e, e);
      }
    }
    TemplateLoader secondary = new ResourceTemplateLoader(DEFAULT_TEMPLATE_DIR_NAME);
    if (primary == null) {
      return secondary;
    }
    return new MultiTemplateLoader(new TemplateLoader[] {primary, secondary});
  }

  public void generate(GenerationContext context) {
    boolean exists = exists(context.getFile());
    if (!context.isOverwrite() && exists) {
      return;
    }
    File dir = context.getFile().getParentFile();
    if (dir != null) {
      mkdirs(dir);
    }
    Writer writer = openWriter(context);
    try {
      Template template = getTemplate(context.getTemplateName());
      process(template, context.getModel(), writer);
    } finally {
      IOUtil.close(writer);
    }
    if (exists) {
      logger.info(Message.DOMAGEN0020.getMessage(context.getFile().getPath()));
    } else {
      logger.info(Message.DOMAGEN0019.getMessage(context.getFile().getPath()));
    }
  }

  protected boolean exists(File file) {
    return file.exists();
  }

  protected void mkdirs(File dir) {
    dir.mkdirs();
  }

  protected Writer openWriter(GenerationContext context) {
    Charset charset = Charset.forName(context.getEncoding());
    return new BufferedWriter(
        new OutputStreamWriter(createFileOutputStream(context.getFile()), charset));
  }

  protected OutputStream createFileOutputStream(File file) {
    try {
      return new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }

  protected Template getTemplate(String name) {
    try {
      return configuration.getTemplate(name);
    } catch (IOException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }

  protected void process(Template template, Object dataModel, Writer writer) {
    try {
      template.process(dataModel, writer);
    } catch (IOException | TemplateException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }
}
