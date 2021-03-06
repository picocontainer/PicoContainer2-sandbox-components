/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *****************************************************************************/
package org.picocontainer.persistence.jdbc;

import org.picocontainer.persistence.ExceptionHandler;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.ClientInfoStatus;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for Connection components. It delegates all calls to the connection obtained by getDelegatedConnection
 * method. Error handling is also there.
 * 
 * @author Juze Peleteiro
 */
public abstract class AbstractConnection implements Connection {

	private final ExceptionHandler jdbcExceptionHandler;
	
	protected AbstractConnection(ExceptionHandler jdbcExceptionHandler) {
		this.jdbcExceptionHandler = jdbcExceptionHandler;
	}

	protected AbstractConnection() {
		jdbcExceptionHandler = null;
	}

	protected abstract Connection getDelegatedConnection() throws SQLException;

	protected abstract void invalidateDelegatedConnection();

	/**
	 * Invalidates the connection calling {@link #invalidateDelegatedConnection()} and convert the <code>cause</code>
	 * using a {@link ExceptionHandler}. if it's available otherwise just return the <code>cause</code> back.
     * @throws RuntimeException
     * @param cause
     * @return
     */
	protected SQLException handleSQLException(Exception cause) throws RuntimeException {
		try {
			invalidateDelegatedConnection();
		} catch (Exception e) {
			// Do nothing, only the original exception should be reported.
		}

		if (jdbcExceptionHandler == null) {
			if (cause instanceof SQLException) {
				return (SQLException) cause;
			}

			throw (RuntimeException) cause;
		}

		throw jdbcExceptionHandler.handle(cause);
	}

	/**
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException {
		try {
			return getDelegatedConnection().createStatement();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		try {
			return getDelegatedConnection().prepareCall(sql);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public String nativeSQL(String sql) throws SQLException {
		try {
			return getDelegatedConnection().nativeSQL(sql);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		try {
			getDelegatedConnection().setAutoCommit(autoCommit);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit() throws SQLException {
		try {
			return getDelegatedConnection().getAutoCommit();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#commit()
	 */
	public void commit() throws SQLException {
		try {
			getDelegatedConnection().commit();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback() throws SQLException {
		try {
			getDelegatedConnection().rollback();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#close()
	 */
	public void close() throws SQLException {
		try {
			getDelegatedConnection().close();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		try {
			return getDelegatedConnection().isClosed();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		try {
			return getDelegatedConnection().getMetaData();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) throws SQLException {
		try {
			getDelegatedConnection().setReadOnly(readOnly);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly() throws SQLException {
		try {
			return getDelegatedConnection().isReadOnly();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public void setCatalog(String catalog) throws SQLException {
		try {
			getDelegatedConnection().setCatalog(catalog);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getCatalog()
	 */
	public String getCatalog() throws SQLException {
		try {

			return getDelegatedConnection().getCatalog();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		try {
			getDelegatedConnection().setTransactionIsolation(level);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation() throws SQLException {
		try {
			return getDelegatedConnection().getTransactionIsolation();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		try {
			return getDelegatedConnection().getWarnings();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		try {
			getDelegatedConnection().clearWarnings();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			return getDelegatedConnection().createStatement(resultSetType, resultSetConcurrency);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			return getDelegatedConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getTypeMap()
	 */
	public Map getTypeMap() throws SQLException {
		try {
			return getDelegatedConnection().getTypeMap();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public void setTypeMap(Map map) throws SQLException {
		try {
			getDelegatedConnection().setTypeMap(map);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		try {
			getDelegatedConnection().setHoldability(holdability);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		try {
			return getDelegatedConnection().getHoldability();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException {
		try {
			return getDelegatedConnection().setSavepoint();
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		try {
			return getDelegatedConnection().setSavepoint(name);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		try {
			getDelegatedConnection().rollback(savepoint);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		try {
			getDelegatedConnection().releaseSavepoint(savepoint);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			return getDelegatedConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		try {
			return getDelegatedConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql, autoGeneratedKeys);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql, columnIndexes);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		try {
			return getDelegatedConnection().prepareStatement(sql, columnNames);
		} catch (Exception e) {
			throw handleSQLException(e);
		}
	}

	/**
	 * @param typeName
	 * @param elements
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
	 */
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return getDelegatedConnection().createArrayOf(typeName, elements);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createBlob()
	 */
	public Blob createBlob() throws SQLException {
		return getDelegatedConnection().createBlob();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createClob()
	 */
	public Clob createClob() throws SQLException {
		return getDelegatedConnection().createClob();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createNClob()
	 */
	public NClob createNClob() throws SQLException {
		return getDelegatedConnection().createNClob();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createSQLXML()
	 */
	public SQLXML createSQLXML() throws SQLException {
		return getDelegatedConnection().createSQLXML();
	}

	/**
	 * @param typeName
	 * @param attributes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
	 */
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return getDelegatedConnection().createStruct(typeName, attributes);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo()
	 */
	public Properties getClientInfo() throws SQLException {
		return getDelegatedConnection().getClientInfo();
	}

	/**
	 * @param name
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	public String getClientInfo(String name) throws SQLException {
		return getDelegatedConnection().getClientInfo(name);
	}

	/**
	 * @param timeout
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#isValid(int)
	 */
	public boolean isValid(int timeout) throws SQLException {
		return getDelegatedConnection().isValid(timeout);
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getDelegatedConnection().isWrapperFor(iface);
	}

	/**
	 * @param properties
	 * @throws SQLClientInfoException
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		try {
			getDelegatedConnection().setClientInfo(properties);
		} catch (SQLException e) {
			throw new SQLClientInfoException("Error getting delegate connection",
					(Map<String, ClientInfoStatus>)Collections.EMPTY_MAP, e);		
		}
	}

	/**
	 * @param name
	 * @param value
	 * @throws SQLClientInfoException
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		try {
			getDelegatedConnection().setClientInfo(name, value);
		} catch (SQLException e) {
			throw new SQLClientInfoException("Error getting delegate connection",
					(Map<String, ClientInfoStatus>)Collections.EMPTY_MAP, e);		
		}
	}

	/**
	 * @param <T>
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getDelegatedConnection().unwrap(iface);
	}

}
