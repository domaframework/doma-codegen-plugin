package org.seasar.doma.gradle.codegen.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;

public class SimpleDataSource implements DataSource {

  protected static final String UNABLE_TO_ESTABLISH_CONNECTION = "08001";

  protected Driver driver;

  protected String url;

  protected String user;

  protected String password;

  protected final Properties properties = new Properties();

  public Driver getDriver() {
    return driver;
  }

  public void setDriver(Driver driver) {
    this.driver = driver;
    try {
      DriverManager.registerDriver(driver);
    } catch (SQLException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void addProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  @Override
  public int getLoginTimeout() {
    return DriverManager.getLoginTimeout();
  }

  @Override
  public void setLoginTimeout(int seconds) {
    DriverManager.setLoginTimeout(seconds);
  }

  @Override
  public Connection getConnection() throws SQLException {
    Properties info = new Properties();
    info.putAll(properties);
    if (user != null) {
      info.setProperty("user", user);
    }
    if (password != null) {
      info.setProperty("password", password);
    }
    return getConnectionInternal(info);
  }

  @Override
  public Connection getConnection(String user, String password) throws SQLException {
    Properties info = new Properties();
    info.putAll(properties);
    if (user != null) {
      info.setProperty("user", user);
    }
    if (password != null) {
      info.setProperty("password", password);
    }
    return getConnectionInternal(info);
  }

  protected Connection getConnectionInternal(Properties info) throws SQLException {
    if (url == null) {
      throw new SQLException(Message.DOMAGEN5002.getMessage());
    }
    try {
      return DriverManager.getConnection(url, info);
    } catch (SQLException e) {
      if (UNABLE_TO_ESTABLISH_CONNECTION.equals(e.getSQLState())) {
        throw new SQLException(Message.DOMAGEN5001.getMessage(), UNABLE_TO_ESTABLISH_CONNECTION, e);
      }
      throw e;
    }
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {}

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLException("unwrap method is unsupported.");
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }
}
