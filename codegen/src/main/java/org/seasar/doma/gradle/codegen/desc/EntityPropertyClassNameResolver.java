package org.seasar.doma.gradle.codegen.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.IOUtil;

public class EntityPropertyClassNameResolver {

  protected final LinkedHashMap<Pattern, String> patternMap;

  public EntityPropertyClassNameResolver(File propertyFile) {
    if (propertyFile == null) {
      patternMap = new LinkedHashMap<Pattern, String>();
    } else {
      patternMap = load(propertyFile);
    }
  }

  protected LinkedHashMap<Pattern, String> load(File propertyFile) {
    LinkedHashMap<Pattern, String> result = new LinkedHashMap<Pattern, String>();
    BufferedReader reader = null;
    try {
      reader =
          new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile), "UTF-8"));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.length() == 0) {
          continue;
        }
        char firstChar = line.charAt(0);
        if (firstChar == '#' || firstChar == '!') {
          continue;
        }
        int pos = line.indexOf('=');
        if (pos < 0) {
          continue;
        }
        String key = line.substring(0, pos);
        String value = line.substring(pos + 1, line.length());
        int pos2 = line.indexOf('@');
        if (pos2 == 0) {
          key = key.substring(1);
        } else if (pos2 > 0) {
          key = Pattern.quote(key.substring(0, pos2)) + key.substring(pos2);
        }
        result.put(Pattern.compile(key), value);
      }
    } catch (IOException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    } finally {
      IOUtil.close(reader);
    }
    return result;
  }

  public String resolve(
      EntityDesc entityDesc, String propertyName, String defaultPropertyClassName) {
    String qualifiedPropertyName = entityDesc.getQualifiedName() + "@" + propertyName;
    for (Map.Entry<Pattern, String> entry : patternMap.entrySet()) {
      Pattern pattern = entry.getKey();
      String input = pattern.pattern().contains("@") ? qualifiedPropertyName : propertyName;
      Matcher matcher = pattern.matcher(input);
      if (!matcher.matches()) {
        continue;
      }
      matcher.reset();
      StringBuffer buf = new StringBuffer();
      String replacement = entry.getValue();
      for (; matcher.find(); ) {
        matcher.appendReplacement(buf, replacement);
        if (matcher.hitEnd()) {
          break;
        }
      }
      matcher.appendTail(buf);
      return buf.toString();
    }
    return defaultPropertyClassName;
  }
}
