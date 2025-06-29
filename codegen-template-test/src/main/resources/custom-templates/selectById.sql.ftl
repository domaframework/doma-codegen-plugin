<#-- Custom SQL template for selectById -->
SELECT
<#list entityDesc.entityPropertyDescs as property>
    ${property.columnName}<#if property_has_next>,</#if>
</#list>
FROM
    ${entityDesc.tableName}
WHERE
<#list entityDesc.idEntityPropertyDescs as id>
    ${id.columnName} = /* ${id.name} */<#if id.number>1<#elseif id.time>${toTime("12:34:56")}<#elseif id.date>${toDate("2010-01-23")}<#elseif id.timestamp>${toTimestamp("2010-01-23 12:34:56")}<#else>'a'</#if><#if id_has_next> AND</#if>
</#list>