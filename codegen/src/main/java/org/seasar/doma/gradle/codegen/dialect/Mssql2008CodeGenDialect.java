package org.seasar.doma.gradle.codegen.dialect;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.time.LocalDateTime;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public class Mssql2008CodeGenDialect extends StandardCodeGenDialect {

  /** the quotation mark of the start */
  protected static final char OPEN_QUOTE = '[';

  /** the quotation mark of the end */
  protected static final char CLOSE_QUOTE = ']';

  public Mssql2008CodeGenDialect() {
    classNameMap.put("datetime", LocalDateTime.class.getName());
    classNameMap.put("datetime2", LocalDateTime.class.getName());
    classNameMap.put("datetimeoffset", LocalDateTime.class.getName());
    classNameMap.put("image", Blob.class.getName());
    classNameMap.put("int", Integer.class.getName());
    classNameMap.put("money", BigDecimal.class.getName());
    classNameMap.put("ntext", NClob.class.getName());
    classNameMap.put("smalldatetime", LocalDateTime.class.getName());
    classNameMap.put("smallmoney", BigDecimal.class.getName());
    classNameMap.put("text", Clob.class.getName());
    classNameMap.put("timestamp", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("uniqueidentifier", String.class.getName());
    classNameMap.put("xml", String.class.getName());
  }

  @Override
  public String getName() {
    return "mssql2008";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.Mssql2008.getQualifiedName();
  }

  @Override
  public String getDefaultSchemaName(String userName) {
    return "dbo";
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public String getMappedPropertyClassName(ColumnMeta columnMeta) {
    String typeName = columnMeta.getTypeName();
    if (typeName.startsWith("int")) {
      typeName = "int";
    } else if (typeName.startsWith("bigint")) {
      typeName = "bigint";
    } else if (typeName.startsWith("smallint")) {
      typeName = "smallint";
    } else if (typeName.startsWith("tinyint")) {
      typeName = "tinyint";
    } else if (typeName.startsWith("decimal")) {
      typeName = "decimal";
    } else if (typeName.startsWith("numeric")) {
      typeName = "numeric";
    }
    return super.getMappedPropertyClassName(columnMeta);
  }

  @Override
  public String applyQuote(String name) {
    return OPEN_QUOTE + name + CLOSE_QUOTE;
  }
}
