package org.seasar.doma.gradle.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import example.hoge.CommonEntity;
import example.hoge.ParentEntity;
import java.io.File;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.gradle.codegen.desc.DaoDesc;
import org.seasar.doma.gradle.codegen.desc.DaoDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDesc;
import org.seasar.doma.gradle.codegen.desc.EntityDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityListenerDesc;
import org.seasar.doma.gradle.codegen.desc.EntityListenerDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyClassNameResolver;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyDescFactory;
import org.seasar.doma.gradle.codegen.desc.GenerationType;
import org.seasar.doma.gradle.codegen.desc.KotlinClassResolver;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.desc.MappedSuperclassDesc;
import org.seasar.doma.gradle.codegen.desc.MappedSuperclassDescFactory;
import org.seasar.doma.gradle.codegen.desc.NamingType;
import org.seasar.doma.gradle.codegen.desc.SqlDesc;
import org.seasar.doma.gradle.codegen.desc.SqlDescFactory;
import org.seasar.doma.gradle.codegen.desc.SqlTestDesc;
import org.seasar.doma.gradle.codegen.desc.SqlTestDescFactory;
import org.seasar.doma.gradle.codegen.desc.SqlTestMethodDesc;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.dialect.PostgresCodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;
import org.seasar.doma.gradle.codegen.meta.TableMeta;
import org.seasar.doma.gradle.codegen.util.ResourceUtil;

public class KotlinGeneratorTest {

  private GlobalFactory factory = new GlobalFactory();

  private CodeGenDialect dialect = new PostgresCodeGenDialect();

  private GeneratorStub generator = new GeneratorStub(LanguageType.KOTLIN);

  @Test
  public void testSimpleEntity(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntity_with_prefix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", null);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntity_with_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, null, "Entity");
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntity_with_prefix_and_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", "Entity");
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testMetamodel(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            true,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testOriginalStates(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            "original",
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testUseEntityListener(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            true,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testEntitySuperclass(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta name = new ColumnMeta();
    name.setComment("COMMENT for NAME");
    name.setName("NAME");
    name.setTypeName("varchar");
    name.setPrimaryKey(false);
    name.setNullable(false);

    ColumnMeta privateString = new ColumnMeta();
    privateString.setComment("COMMENT for PRIVATESTRING");
    privateString.setName("PRIVATESTRING");
    privateString.setTypeName("varchar");
    privateString.setPrimaryKey(false);
    privateString.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(name);
    tableMeta.addColumnMeta(privateString);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            CommonEntity.class,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testNamingType(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.SNAKE_UPPER_CASE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testShowTableName_false(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for EMP_NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varchar");
    empName.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            false,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testShowColumnName_false(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for EMP_NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varchar");
    empName.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, false);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testGenerationType_IDENTITY(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setAutoIncrement(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testGenerationType_SEQUENCE(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", GenerationType.SEQUENCE, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testGenerationType_TABLE(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", GenerationType.TABLE, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testCompositeId(TestInfo testInfo) throws Exception {
    ColumnMeta id1 = new ColumnMeta();
    id1.setComment("COMMENT for ID");
    id1.setName("ID1");
    id1.setTypeName("integer");
    id1.setPrimaryKey(true);
    id1.setNullable(false);

    ColumnMeta id2 = new ColumnMeta();
    id2.setComment("COMMENT for ID");
    id2.setName("ID2");
    id2.setTypeName("integer");
    id2.setPrimaryKey(true);
    id2.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id1);
    tableMeta.addColumnMeta(id2);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, null, null, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testEntityPropertyClassNameResolver(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    ColumnMeta xvalue = new ColumnMeta();
    xvalue.setComment("COMMENT for XVAL");
    xvalue.setName("XVAL");
    xvalue.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);
    tableMeta.addColumnMeta(xvalue);

    File file =
        ResourceUtil.getResourceAsFile(
            getClass().getPackage().getName().replace(".", "/")
                + "/entityPropertyClassNames.properties");
    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(file);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    generator.generate(new EntityContext(entityDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntityListener(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    EntityListenerDescFactory entityListenerDescFactory =
        factory.createEntityListenerDescFactory("example.entity", null);
    EntityListenerDesc entityListenerDesc =
        entityListenerDescFactory.createEntityListenerDesc(entityDesc);
    generator.generate(new EntityListenerContext(entityListenerDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntityListener_with_prefix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", null);
    EntityListenerDescFactory entityListenerDescFactory =
        factory.createEntityListenerDescFactory("example.entity", null);
    EntityListenerDesc entityListenerDesc =
        entityListenerDescFactory.createEntityListenerDesc(entityDesc);
    generator.generate(new EntityListenerContext(entityListenerDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntityListener_with_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, null, "Entity");
    EntityListenerDescFactory entityListenerDescFactory =
        factory.createEntityListenerDescFactory("example.entity", null);
    EntityListenerDesc entityListenerDesc =
        entityListenerDescFactory.createEntityListenerDesc(entityDesc);
    generator.generate(new EntityListenerContext(entityListenerDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleEntityListener_with_prefix_and_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", "Entity");
    EntityListenerDescFactory entityListenerDescFactory =
        factory.createEntityListenerDescFactory("example.entity", null);
    EntityListenerDesc entityListenerDesc =
        entityListenerDescFactory.createEntityListenerDesc(entityDesc);
    generator.generate(new EntityListenerContext(entityListenerDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testExtendingEntityListener(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    EntityListenerDescFactory entityListenerDescFactory =
        factory.createEntityListenerDescFactory("example.entity", "aaa.FooListener");
    EntityListenerDesc entityListenerDesc =
        entityListenerDescFactory.createEntityListenerDesc(entityDesc);
    generator.generate(new EntityListenerContext(entityListenerDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleMappedSuperclass(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            true);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    MappedSuperclassDescFactory mappedSuperclassDescFactory =
        factory.createMappedSuperclassDescFactory("example.entity", null);
    MappedSuperclassDesc mappedSuperclassDesc =
        mappedSuperclassDescFactory.createMappedSuperclassDesc(entityDesc);
    generator.generate(new MappedSuperclassContext(mappedSuperclassDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testExtendingMappedSuperclass(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta name = new ColumnMeta();
    name.setComment("COMMENT for NAME");
    name.setName("NAME");
    name.setTypeName("varchar");
    name.setPrimaryKey(false);
    name.setNullable(false);

    ColumnMeta privateString = new ColumnMeta();
    privateString.setComment("COMMENT for PRIVATESTRING");
    privateString.setName("PRIVATESTRING");
    privateString.setTypeName("varchar");
    privateString.setPrimaryKey(false);
    privateString.setNullable(false);

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(name);
    tableMeta.addColumnMeta(privateString);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            CommonEntity.class,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            true);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);
    MappedSuperclassDescFactory mappedSuperclassDescFactory =
        factory.createMappedSuperclassDescFactory("example.entity", CommonEntity.class.getName());
    MappedSuperclassDesc mappedSuperclassDesc =
        mappedSuperclassDescFactory.createMappedSuperclassDesc(entityDesc);
    generator.generate(new MappedSuperclassContext(mappedSuperclassDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleDao(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);

    DaoDescFactory daoDescFactory =
        factory.createDaoDescFactory("example.dao", "Dao", "dao.config.MyConfig");
    DaoDesc daoDesc = daoDescFactory.createDaoDesc(entityDesc);
    generator.generate(new DaoContext(daoDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleDao_with_prefix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", null);

    DaoDescFactory daoDescFactory =
        factory.createDaoDescFactory("example.dao", "Dao", "dao.config.MyConfig");
    DaoDesc daoDesc = daoDescFactory.createDaoDesc(entityDesc);
    generator.generate(new DaoContext(daoDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleDao_with_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, null, "Entity");

    DaoDescFactory daoDescFactory =
        factory.createDaoDescFactory("example.dao", "Dao", "dao.config.MyConfig");
    DaoDesc daoDesc = daoDescFactory.createDaoDesc(entityDesc);
    generator.generate(new DaoContext(daoDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleDao_with_prefix_and_suffix(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta, "T", "Entity");

    DaoDescFactory daoDescFactory =
        factory.createDaoDescFactory("example.dao", "Dao", "dao.config.MyConfig");
    DaoDesc daoDesc = daoDescFactory.createDaoDesc(entityDesc);
    generator.generate(new DaoContext(daoDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testDefaultConfigDao(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta version = new ColumnMeta();
    version.setComment("COMMENT for VERSION");
    version.setName("VERSION");
    version.setTypeName("integer");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(version);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);

    DaoDescFactory daoDescFactory = factory.createDaoDescFactory("example.dao", "Dao", null);
    DaoDesc daoDesc = daoDescFactory.createDaoDesc(entityDesc);
    generator.generate(new DaoContext(daoDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSelectById(TestInfo testInfo) throws Exception {
    ColumnMeta id = new ColumnMeta();
    id.setComment("COMMENT for ID");
    id.setName("ID");
    id.setTypeName("integer");
    id.setPrimaryKey(true);
    id.setNullable(false);

    ColumnMeta empName = new ColumnMeta();
    empName.setComment("COMMENT for NAME");
    empName.setName("EMP_NAME");
    empName.setTypeName("varcar");

    TableMeta tableMeta = new TableMeta();
    tableMeta.setCatalogName("CATALOG");
    tableMeta.setSchemaName("SCHEMA");
    tableMeta.setName("HOGE");
    tableMeta.setComment("COMMENT for HOGE");
    tableMeta.addColumnMeta(id);
    tableMeta.addColumnMeta(empName);

    EntityPropertyClassNameResolver resolver = factory.createEntityPropertyClassNameResolver(null);
    EntityPropertyDescFactory entityPropertyDescFactory =
        factory.createEntityPropertyDescFactory(
            dialect, resolver, new KotlinClassResolver(), "version", null, 100L, 50L, true);
    EntityDescFactory entityDescFactory =
        factory.createEntityDescFactory(
            "example.entity",
            null,
            entityPropertyDescFactory,
            NamingType.NONE,
            null,
            false,
            false,
            true,
            true,
            true,
            false,
            false,
            false);
    EntityDesc entityDesc = entityDescFactory.createEntityDesc(tableMeta);

    DaoDesc daoDesc = new DaoDesc();
    daoDesc.setPackageName("example.dao");
    daoDesc.setSimpleName("HogeDao");

    SqlDescFactory sqlDescFactory = factory.createSqlDescFactory(null, dialect);
    SqlDesc sqlDesc =
        sqlDescFactory.createSqlDesc(entityDesc, daoDesc, "dummy", "selectById.sql.ftl");
    generator.generate(new SqlContext(sqlDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  @Test
  public void testSimpleSqlTest(TestInfo testInfo) throws Exception {
    SqlTestMethodDesc select =
        new SqlTestMethodDesc(
            "testSelect", "META-INF/" + getClass().getName().replace(".", "/") + "/select.sql");
    SqlTestMethodDesc insert =
        new SqlTestMethodDesc(
            "testInsert", "META-INF/" + getClass().getName().replace(".", "/") + "/insert.sql");
    SqlTestMethodDesc update =
        new SqlTestMethodDesc(
            "testUpdate", "META-INF/" + getClass().getName().replace(".", "/") + "/update.sql");
    SqlTestDescFactory sqlFileTestDescFactory =
        factory.createSqlTestCaseDescFactory(
            "org.seasar.doma.jdbc.dialect.StandardDialect", "jdbc:hsqldb:mem:example", "sa", "");
    SqlTestDesc sqlTestDesc =
        sqlFileTestDescFactory.createSqlFileTestDesc(
            "example.dao.SqlTest", Arrays.asList(select, insert, update));
    generator.generate(new SqlTestContext(sqlTestDesc));

    assertEquals(expect(testInfo), generator.getResult());
  }

  private String expect(TestInfo testInfo) {
    System.out.println(generator.getResult());

    String path =
        testInfo.getTestClass().get().getName().replace(".", "/")
            + "_"
            + testInfo.getTestMethod().get().getName().substring(4)
            + ".txt";
    return ResourceUtil.getResourceAsString(path);
  }

  private class EntityContext extends GenerationContext {

    public EntityContext(EntityDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }

  private class EntityListenerContext extends GenerationContext {

    public EntityListenerContext(EntityListenerDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }

  private class MappedSuperclassContext extends GenerationContext {

    public MappedSuperclassContext(MappedSuperclassDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }

  private class DaoContext extends GenerationContext {

    public DaoContext(DaoDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }

  private class SqlTestContext extends GenerationContext {

    public SqlTestContext(SqlTestDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }

  private class SqlContext extends GenerationContext {

    public SqlContext(SqlDesc model) {
      super(model, new File("dummy"), model.getTemplateName(), "UTF-8", true);
    }
  }
}
