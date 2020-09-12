<#-- See also org.seasar.doma.gradle.codegen.desc.SqlTestDesc -->
<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if packageName??>
package ${packageName};
</#if>

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
public class <#if entityPrefix??>${entityPrefix}</#if>${simpleName}<#if entitySuffix??>${entitySuffix}</#if> {

    /** */
    protected SqlFileRepository repository;

    /** */
    protected Dialect dialect;

    /** */
    protected Driver driver;

    /** */
    protected String url;

    /** */
    protected String user;

    /** */
    protected String password;

    @BeforeEach
    protected void setUp() throws Exception {
        repository = new NoCacheSqlFileRepository();
        dialect = new ${dialectClassName}();
        url = "${url}";
        user = "${user}";
        password = "${password}";
    }

    /**
     * 
     * @param sqlFile
     * @throws Exception
     */
    protected void execute(SqlFile sqlFile) throws Exception {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            try {
                statement.execute(sqlFile.getSql());
            } finally {
                statement.close();
            }
        } finally {
            try {
                connection.rollback();
            } finally {
                connection.close();
            }
        }
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    protected Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
<#list methodDescs as methodDesc>

    /**
     * 
     * @throws Exception
     */
    @Test
    public void ${methodDesc.methodName}(TestInfo testInfo) throws Exception {
        SqlFile sqlFile = repository.getSqlFile(testInfo.getTestMethod().get(), "${methodDesc.path}", dialect);
        execute(sqlFile);
    }
</#list>

}
