<#-- See also org.seasar.doma.gradle.codegen.desc.SqlTestDesc -->
<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName}
</#if>

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.Statement

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo

import org.seasar.doma.jdbc.NoCacheSqlFileRepository
import org.seasar.doma.jdbc.SqlFile
import org.seasar.doma.jdbc.SqlFileRepository
import org.seasar.doma.jdbc.dialect.Dialect

/**
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
class <#if entityPrefix??>${entityPrefix}</#if>${simpleName}<#if entitySuffix??>${entitySuffix}</#if> {

    lateinit var repository: SqlFileRepository
    lateinit var dialect: Dialect
    lateinit var driver: Driver
    lateinit var url: String
    lateinit var user: String
    lateinit var password: String

    @BeforeEach
    protected fun setUp() {
        repository = NoCacheSqlFileRepository()
        dialect = ${dialectClassName}()
        url = "${url}"
        user = "${user}"
        password = "${password}"
    }

    protected fun execute(sqlFile: SqlFile) {
        val connection = getConnection()
        try {
            connection.setAutoCommit(false)
            val statement = connection.createStatement()
            try {
                statement.execute(sqlFile.getSql())
            } finally {
                statement.close()
            }
        } finally {
            try {
                connection.rollback()
            } finally {
                connection.close()
            }
        }
    }

    protected fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
<#list methodDescs as methodDesc>

    @Test
    public fun ${methodDesc.methodName}(testInfo: TestInfo) {
        val sqlFile = repository.getSqlFile(testInfo.getTestMethod().get(), "${methodDesc.path}", dialect)
        execute(sqlFile)
    }
</#list>

}
