package org.seasar.doma.gradle.codegen.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A wrapper for JDBC Driver that manages class loader context switching.
 *
 * <p>This wrapper ensures that all driver operations are executed with the correct class loader
 * context, preventing class loading issues when the driver and calling code are loaded by different
 * class loaders.
 *
 * @see java.sql.Driver
 */
public class DriverWrapper implements Driver {

  private final Driver driver;

  /**
   * Constructs a new DriverWrapper with the specified driver.
   *
   * @param driver the driver to wrap, must not be null
   * @throws NullPointerException if driver is null
   */
  public DriverWrapper(Driver driver) {
    this.driver = Objects.requireNonNull(driver);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    return execute(() -> driver.connect(url, info));
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return execute(() -> driver.acceptsURL(url));
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return execute(() -> driver.getPropertyInfo(url, info));
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public int getMajorVersion() {
    return execute(driver::getMajorVersion);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public int getMinorVersion() {
    return execute(driver::getMinorVersion);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public boolean jdbcCompliant() {
    return execute(driver::jdbcCompliant);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Executes with the driver's class loader context.
   */
  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return execute(driver::getParentLogger);
  }

  /**
   * Executes an operation with the driver's class loader as the context class loader.
   *
   * <p>This method temporarily switches the thread's context class loader to the driver's class
   * loader, executes the operation, and then restores the original class loader. This ensures that
   * the driver can properly load its internal classes and resources.
   *
   * @param <R> the return type of the executable
   * @param <TH> the type of exception that may be thrown
   * @param executable the operation to execute
   * @return the result of the executable operation
   * @throws TH if the executable operation throws an exception
   */
  private <R, TH extends Exception> R execute(Executable<R, TH> executable) throws TH {
    var classLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(driver.getClass().getClassLoader());
    try {
      return executable.execute();
    } finally {
      Thread.currentThread().setContextClassLoader(classLoader);
    }
  }

  /**
   * Represents an executable operation that can return a result and throw an exception.
   *
   * @param <R> the return type
   * @param <TH> the type of exception that may be thrown
   */
  private interface Executable<R, TH extends Exception> {
    /**
     * Executes the operation.
     *
     * @return the result of the operation
     * @throws TH if an error occurs during execution
     */
    R execute() throws TH;
  }
}
