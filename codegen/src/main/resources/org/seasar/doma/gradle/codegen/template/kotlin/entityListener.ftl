<#-- See also org.seasar.doma.gradle.codegen.desc.EntityListenerDesc -->
<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName}
</#if>

<#list importNames as importName>
import ${importName}
</#list>

/**
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
class ${simpleName}<#if superclassSimpleName??> : ${superclassSimpleName}<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>><#else> : EntityListener<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>></#if> {
<#if !superclassSimpleName??>

    override fun preInsert(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PreInsertContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }

    override fun preUpdate(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PreUpdateContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }

    override fun preDelete(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PreDeleteContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }

    override fun postInsert(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PostInsertContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }

    override fun postUpdate(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PostUpdateContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }

    override fun postDelete(entity: <#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>, context: PostDeleteContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>>) {
    }
</#if>
}
