<#-- See also org.seasar.doma.gradle.codegen.desc.EntityDesc -->
<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName};
</#if>

<#list importNames as importName>
import ${importName};
</#list>

/**
<#if showDbComment && comment??>
 * ${comment}
</#if>
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Entity<#if useListener || namingType != "NONE" || useMetamodel>(</#if><#if useListener>listener = ${listenerClassSimpleName}.class</#if><#if namingType != "NONE"><#if useListener>, </#if>naming = ${namingType.referenceName}</#if><#if useMetamodel><#if useListener || namingType != "NONE">, </#if>metamodel = @Metamodel</#if><#if useListener || namingType != "NONE" || useMetamodel>)</#if>
<#if showCatalogName && catalogName?? || showSchemaName && schemaName?? || showTableName && tableName??>
@Table(<#if showCatalogName && catalogName??>catalog = "${catalogName}"</#if><#if showSchemaName && schemaName??><#if showCatalogName && catalogName??>, </#if>schema = "${schemaName}"</#if><#if showTableName><#if showCatalogName && catalogName?? || showSchemaName && schemaName??>, </#if>name = "${tableName}"</#if>)
</#if>
public class <#if entityPrefix??>${entityPrefix}</#if>${simpleName}<#if entitySuffix??>${entitySuffix}</#if><#if superclassSimpleName??> extends ${superclassSimpleName}</#if> {
<#list ownEntityPropertyDescs as property>

  <#if showDbComment && property.comment??>
    /** ${property.comment} */
  <#else>
    /** */
  </#if>
  <#if property.id>
    @Id
    <#if property.generationType??>
    @GeneratedValue(strategy = ${property.generationType.referenceName})
      <#if property.generationType == "SEQUENCE">
    @SequenceGenerator(sequence = "${tableName}_${property.columnName}"<#if property.initialValue??>, initialValue = ${property.initialValue}</#if><#if property.allocationSize??>, allocationSize = ${property.allocationSize}</#if>)
      <#elseif property.generationType == "TABLE">
    @TableGenerator(pkColumnValue = "${tableName}_${property.columnName}"<#if property.initialValue??>, initialValue = ${property.initialValue}</#if><#if property.allocationSize??>, allocationSize = ${property.allocationSize}</#if>)
      </#if>
    </#if>
  </#if>
  <#if property.version>
    @Version
  </#if>
  <#if property.showColumnName && property.columnName??>
    @Column(name = "${property.columnName}")
  </#if>
    <#if !useAccessor>public </#if>${property.propertyClassSimpleName} ${property.name};
</#list>
<#if originalStatesPropertyName??>

    /** */
    @OriginalStates
    <#if entityPrefix??>${entityPrefix}</#if>${simpleName}<#if entitySuffix??>${entitySuffix}</#if> ${originalStatesPropertyName};
</#if>
<#if useAccessor>
  <#list ownEntityPropertyDescs as property>

    /** 
     * Returns the ${property.name}.
     * 
     * @return the ${property.name}
     */
    public ${property.propertyClassSimpleName} get${property.name?cap_first}() {
        return ${property.name};
    }

    /** 
     * Sets the ${property.name}.
     * 
     * @param ${property.name} the ${property.name}
     */
    public void set${property.name?cap_first}(${property.propertyClassSimpleName} ${property.name}) {
        this.${property.name} = ${property.name};
    }
  </#list>
</#if>
}