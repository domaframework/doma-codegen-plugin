package org.seasar.doma.gradle.codegen.util;

import java.util.function.Function;

public final class TableUtil {

  public static String getQualifiedTableName(
      String catalogName, String schemaName, String tableName) {
    StringBuilder buf = new StringBuilder();
    if (catalogName != null && !catalogName.isEmpty()) {
      buf.append(catalogName).append(".");
    }
    if (schemaName != null && !schemaName.isEmpty()) {
      buf.append(schemaName).append(".");
    }
    return buf.append(tableName).toString();
  }

  public static String getEnquoteQualifiedTableName(
      Function<String, String> enquote, String catalogName, String schemaName, String tableName) {
    StringBuilder buf = new StringBuilder();
    if (catalogName != null && !catalogName.isEmpty()) {
      buf.append(enquote.apply(catalogName)).append(".");
    }
    if (schemaName != null && !schemaName.isEmpty()) {
      buf.append(enquote.apply(schemaName)).append(".");
    }
    return buf.append(enquote.apply(tableName)).toString();
  }
}
