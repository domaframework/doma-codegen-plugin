<#-- See also org.seasar.doma.gradle.codegen.desc.SqlDesc -->
select
  /*%expand*/*
from
  ${entityDesc.tableName}
where
<#list entityDesc.idEntityPropertyDescs as property>
  ${property.columnName} = /* ${property.name} */<#if property.number>1<#elseif property.time>${toTime("12:34:56")}<#elseif property.date>${toDate("2010-01-23")}<#elseif property.timestamp>${toTimestamp("2010-01-23 12:34:56")}<#else>'a'</#if><#if property_has_next>
  and</#if>
</#list>
