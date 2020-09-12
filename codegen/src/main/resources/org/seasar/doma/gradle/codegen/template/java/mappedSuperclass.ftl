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
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
public abstract class ${simpleName}<#if superclassSimpleName??> extends ${superclassSimpleName}</#if> {
}
