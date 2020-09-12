<#-- See also org.seasar.doma.gradle.codegen.desc.EntityListenerDesc -->
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
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
public class ${simpleName}<#if superclassSimpleName??> extends ${superclassSimpleName}<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>><#else> implements EntityListener<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>></#if> {
<#if !superclassSimpleName??>

    @Override
    public void preInsert(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PreInsertContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }

    @Override
    public void preUpdate(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PreUpdateContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }

    @Override
    public void preDelete(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PreDeleteContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }

    @Override
    public void postInsert(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PostInsertContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }

    @Override
    public void postUpdate(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PostUpdateContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }

    @Override
    public void postDelete(<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if> entity, PostDeleteContext<<#if entityDesc.entityPrefix??>${entityDesc.entityPrefix}</#if>${entityClassSimpleName}<#if entityDesc.entitySuffix??>${entityDesc.entitySuffix}</#if>> context) {
    }
</#if>
}