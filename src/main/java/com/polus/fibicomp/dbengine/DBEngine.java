package com.polus.fibicomp.dbengine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.polus.fibicomp.questionnaire.transactions.TransactionStatement;

@Service
public class DBEngine {
	@Autowired
	private HibernateTemplate hibernateTemplate;
	SessionFactory sessionFactory;

	public DBEngine() {
		// session = hibernateTemplate.getSessionFactory().getCurrentSession();
		// dbConnectionManager = DBConnectionManager.getInstance();
	}

	private Connection getConnection() throws Exception, IOException {
		Connection connection = null;
		try {
			sessionFactory = hibernateTemplate.getSessionFactory();
			Session session = sessionFactory.openSession();
			// Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			connection = ((SessionImpl) session).connection();
			// conn = dbConnectionManager.getConnection();
		} catch (Exception sqlEx) {
			System.out.println(sqlEx);
		}
		return connection;
	}

	public Connection beginTxn() throws Exception, IOException {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
		} catch (SQLException sqlEx) {
			// throwDBException(sqlEx);
		}
		return conn;
	}

	public void commit(Connection conn) throws Exception {
		try {

			if (conn != null && !conn.isClosed()) {
				conn.commit();
				closeConnection(conn);
			}
		} catch (SQLException sqlEx) {
			// throwDBException(sqlEx);
		}
	}

	public void rollback(Connection conn) throws Exception {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				// closeConnection(conn);
			}
		} catch (SQLException sqlEx) {
			// throwDBException(sqlEx);
		}
	}

	private void closeConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				if (sessionFactory != null) {
					try {
						sessionFactory.close();
					} catch (HibernateException ignored) {
						// log.error("Couldn't close SessionFactory", ignored);
					}
				}
			}
		} catch (SQLException sqlEx) {

		}
	}

	public void endTxn(Connection conn) throws Exception {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.commit();
				// closeConnection(conn);
			}
		} catch (SQLException sqlEx) {
			// throwDBException(sqlEx);
		}
	}

	public ArrayList<HashMap<String, Object>> executeQuery(ArrayList<Parameter> inParam, String sqlId)
			throws Exception {
		ArrayList<HashMap<String, Object>> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = beginTxn();
			String sql = TransactionStatement.getSQL(sqlId, getDatabaseName(conn));
			result = executeQuery(inParam, sql, conn);
		} catch (DBException ex) {
			rollback(conn);
			throw ex;
		} catch (Exception ex) {
			rollback(conn);
			throw new DBException(0, "executeQuery(inParam,sqlQuery) ", ex);
		} finally {
			endTxn(conn);
		}
		return result;
	}

	public ArrayList<HashMap<String, Object>> executeQuerySQL(ArrayList<Parameter> inParam, String sql)
			throws Exception {
		ArrayList<HashMap<String, Object>> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = beginTxn();
			result = executeQuery(inParam, sql, conn);
		} catch (DBException ex) {
			rollback(conn);
			throw ex;
		} catch (Exception ex) {
			rollback(conn);
			throw new DBException(0, "executeQuery(inParam,sqlQuery) ", ex);
		} finally {
			endTxn(conn);
		}
		return result;
	}

	public ArrayList<HashMap<String, Object>> executeQuery(ArrayList<Parameter> inParam, String sqlQuery,
			Connection conn) throws DBException, IOException, SQLException {
		ArrayList<HashMap<String, Object>> result = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		try {
			sqlQuery = buildStatement(sqlQuery);
			preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement = setParameter(preparedStatement, inParam);
			ResultSet resultSet = preparedStatement.executeQuery();
			result = packageResult(resultSet);
		} catch (Exception ex) {
			throw new DBException(0, "executeQuery(inParam,sqlQuery,conn)", ex);
		} finally {
			preparedStatement.close();
		}
		return result;

	}

	public int executeUpdate(ArrayList<Parameter> inParam, String sqlId) throws Exception {
		int rowsUpdated = 0;
		Connection conn = null;
		try {
			conn = beginTxn();
			String sql = TransactionStatement.getSQL(sqlId, getDatabaseName(conn));
			rowsUpdated = executeUpdate(inParam, sql, conn);
		} catch (DBException ex) {
			rollback(conn);
			throw ex;
		} catch (Exception ex) {
			rollback(conn);
			throw new DBException(0, "executeUpdate(inParam,sqlQuery) ", ex);
		} finally {
			endTxn(conn);
		}
		return rowsUpdated;
	}

	public int executeUpdateSQL(ArrayList<Parameter> inParam, String sql) throws Exception {
		int rowsUpdated = 0;
		Connection conn = null;
		try {
			conn = beginTxn();
			rowsUpdated = executeUpdate(inParam, sql, conn);
		} catch (DBException ex) {
			rollback(conn);
			throw ex;
		} catch (Exception ex) {
			rollback(conn);
			throw new DBException(0, "executeUpdate(inParam,sqlQuery) ", ex);
		} finally {
			endTxn(conn);
		}
		return rowsUpdated;
	}

	public int executeUpdate(ArrayList<Parameter> inParam, String sqlQuery, Connection conn)
			throws DBException, IOException, SQLException {
		PreparedStatement preparedStatement = null;
		int rowsUpdated = 0;
		try {
			sqlQuery = buildStatement(sqlQuery);
			preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement = setParameter(preparedStatement, inParam);
			rowsUpdated = preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new DBException(0, "executeUpdate(inParam,sqlQuery,conn)", ex);
		} finally {
			preparedStatement.close();
		}
		return rowsUpdated;

	}

	private String buildStatement(String sql) {
		sql = sql.replaceAll("<<([a-zA-Z])\\w+>>", "?");
		return sql;
	}

	private PreparedStatement setParameter(PreparedStatement preparedStatement, ArrayList<Parameter> inParam)
			throws SQLException, DBException, IOException {
		int parameterId = 0;
		for (Parameter input : inParam) {
			if (input.getParameterType().equals(DBEngineConstants.TYPE_STRING)) {
				preparedStatement.setString(++parameterId, (String) input.getParameterValue());

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_INT)) {
				preparedStatement.setInt(++parameterId, (int) input.getParameterValue());

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)) {
				if (getIntegerValue(input.getParameterValue()) == null) {
					preparedStatement.setNull(++parameterId, java.sql.Types.INTEGER);

				} else {
					preparedStatement.setInt(++parameterId, getIntegerValue(input.getParameterValue()).intValue());

				}
			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_FLOAT)) {
				preparedStatement.setFloat(++parameterId, getFloatValue(input.getParameterValue()));

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_DOUBLE)) {
				preparedStatement.setDouble(++parameterId, getDoubleValue(input.getParameterValue()));

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_DATE)) {
				preparedStatement.setDate(++parameterId, getDateValue(input.getParameterValue()));

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_TIMESTAMP)) {
				preparedStatement.setTimestamp(++parameterId, getTimestampValue(input.getParameterValue()));

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_TIME)) {
				preparedStatement.setTime(++parameterId, getTimeValue(input.getParameterValue()));

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_CLOB)) {
				Clob nullClob = null;
				String clobData = getClobValue(input.getParameterValue());
				if (clobData != null) {
					ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
					preparedStatement.setAsciiStream(++parameterId, bais, bais.available());
					bais.close();
				} else {
					preparedStatement.setClob(++parameterId, nullClob);
				}

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_BLOB)) {
				byte[] buffer = null;
				buffer = (byte[]) getBlobValue(input.getParameterValue());
				InputStream inputStream;
				inputStream = new ByteArrayInputStream(buffer);
				preparedStatement.setBinaryStream(++parameterId, inputStream, inputStream.available());
				inputStream.close();

			} else if (input.getParameterType().equals(DBEngineConstants.TYPE_LONG)) {
				byte[] buffer = null;
				String strValue = (String) input.getParameterValue();
				buffer = strValue.getBytes();
				InputStream inputStream;
				inputStream = new ByteArrayInputStream(buffer);
				preparedStatement.setAsciiStream(++parameterId, inputStream, inputStream.available());
				inputStream.close();
			}

		}
		return preparedStatement;
	}

	private ArrayList<HashMap<String, Object>> packageResult(ResultSet rset) {
		ArrayList<HashMap<String, Object>> alResSet = new ArrayList<>();
		try {
			java.sql.ResultSetMetaData metaData = rset.getMetaData();
			int colCount = metaData.getColumnCount();
			String colName = null;
			Object colValue = null;
			while (rset.next()) {
				HashMap<String, Object> htRow = new HashMap<>();
				for (int i = 1; i <= colCount; i++) {
					//colName = metaData.getColumnName(i);
					colName = metaData.getColumnLabel(i);
					int colDatatype = metaData.getColumnType(i);
					switch (colDatatype) {
					case java.sql.Types.BLOB:
						try {
							ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
							if (colDatatype == java.sql.Types.LONGVARCHAR) { // data
																				// type
																				// is
																				// LONG
																				// RAW

							} else { // data type is BLOB
								Blob binaryData = null;
								binaryData = rset.getBlob(i);
								// Null check code added for blob data.
								if (binaryData != null) {
									bytesOut.write(binaryData.getBytes(1, (int) binaryData.length()));
								}
							}
							bytesOut.flush();
							bytesOut.close();
							colValue = bytesOut;
							break;
						} catch (Exception Ex) {
							System.out.println(Ex);
						}
					case java.sql.Types.CLOB:
						try {
							java.sql.Clob characterData = null;
							characterData = rset.getClob(i);
							if (characterData != null && characterData.length() > 0) {
								char charData[] = new char[(int) characterData.length()];
								characterData.getCharacterStream().read(charData);
								colValue = new String(charData);
							} else {
								colValue = null;
							}
							break;
						} catch (Exception Ex) {
							System.out.println(Ex);
						}
						break;
					case java.sql.Types.DATE:
						colValue = rset.getTimestamp(i);
						break;
					case java.sql.Types.TIMESTAMP:
						colValue = rset.getTimestamp(i);
						break;
					default: // data type OTHERS
						colValue = rset.getObject(i);
						break;
					}
					htRow.put(colName.toUpperCase(), colValue);
				}
				alResSet.add(htRow);
			}
		} catch (SQLException sqlEx) {
			System.out.println(sqlEx);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
			} catch (SQLException sqlEx) {
				System.out.println(sqlEx);
			}
		}
		return alResSet;
	}

	public Integer getIntegerValue(Object value) throws DBException {
		if (value == null)
			return null;
		if (value instanceof Integer) {
			return (Integer) value;
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value + " is :java.lang.Integer\n"
					+ "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
	}

	/**
	 * Get the long value if the type is "Long"
	 * 
	 * @return double Long value
	 */
	public double getLongValue(Object value) {
		if (value != null && value instanceof String) {
			return (Long.valueOf((String) value)).longValue();
		} else
			return -1;
	}

	/**
	 * Get the String value if the type is "String"
	 * 
	 * @return String value
	 */
	public String getStrValue(Object value) {
		if (value != null && value instanceof String)
			return (String) value;
		else
			return null;
	}

	/**
	 * Get the <code>java.sql.Date</code> value if the type is "Date"
	 * 
	 * @return Date value
	 */
	public java.sql.Date getDateValue(Object value) throws DBException {
		if (value != null) {
			/*
			 * if(value instanceof java.sql.Timestamp){ return (java.sql.Timestamp)value;
			 * }else
			 */
			if (value instanceof java.sql.Date) {
				return (java.sql.Date) value;
			} else {
				String exMsg = "Wrong type:\n Expected type of the " + value + "is : java.sql.Date\n"
						+ "Passed value type is : " + value.getClass();
				throw new DBException(exMsg);
			}
		} else
			return null;
	}

	/**
	 * Get the <code>java.sql.Timestamp</code> value if the type is "Timestamp"
	 * 
	 * @return Timestamp Timestamp value
	 */
	public java.sql.Timestamp getTimestampValue(Object value) throws DBException {
		if (value != null) {
			if (value instanceof java.sql.Timestamp) {
				return (java.sql.Timestamp) value;
			} else {
				String exMsg = "Type is wrong:\n Expected type of the " + value + "is : java.sql.TimeStamp\n"
						+ "Passed value type is : " + value.getClass();
				throw new DBException(exMsg);
			}
		} else
			return null;
	}

	/**
	 * Get the <code>java.sql.Time</code> value if the type is "Time"
	 * 
	 * @return Time Time value
	 */
	public java.sql.Time getTimeValue(Object value) throws DBException {
		if (value != null) {
			if (value instanceof java.sql.Time) {
				return (java.sql.Time) value;
			} else {
				String exMsg = "Type is wrong:\n Expected type of the " + value + "is : java.sql.Time\n"
						+ "Passed value type is : " + value.getClass();
				throw new DBException(exMsg);
			}
		} else
			return null;
	}

	/**
	 * Get the int value if the type is "int"
	 * 
	 * @return int value
	 */
	public int getIntValue(Object value) throws DBException {
		if (value == null)
			return -1;
		if ((value instanceof Integer || value instanceof String)) {
			return Integer.parseInt(value.toString());
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value
					+ " is :java.lang.String or java.lang.Integer\n" + "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
//        return -1;
	}

	/**
	 * Get the float value if the type is "float"
	 * 
	 * @return float value
	 */
	public float getFloatValue(Object value) throws DBException {
		if (value == null)
			return 0;
		if ((value instanceof Float || value instanceof String)) {
			return (Float.valueOf(value.toString())).floatValue();
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value
					+ " is :java.lang.String or java.lang.Float\n" + "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
//        return -1;
	}

	/**
	 * Get the Float value if the type is "Float"
	 * 
	 * @return Float value
	 */
	public Float getFloatObjValue(Object value) throws DBException {
		if (value == null)
			return null;
		if (value instanceof Float) {
			return (Float) value;
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value + " is :java.lang.Float\n"
					+ "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
	}

	/**
	 * Get the double value if the type is "double"
	 * 
	 * @return double value
	 */
	public double getDoubleValue(Object value) throws DBException {
		if (value == null)
			return 0;
		if ((value instanceof Double || value instanceof String)) {
			return (Double.valueOf(value.toString())).doubleValue();
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value
					+ " is :java.lang.String or java.lang.Double\n" + "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
	}

	/**
	 * Get the Double value if the type is "Double"
	 * 
	 * @return Double value
	 */
	public Double getDoubleObjValue(Object value) throws DBException {
		if (value == null)
			return null;
		if (value instanceof Double) {
			return (Double) value;
		} else {
			String exMsg = "Type is wrong:\n Expected type of the " + value + " is :java.lang.Double\n"
					+ "Passed value type is : " + value.getClass();
			throw new DBException(exMsg);
		}
	}

	/**
	 * Get the Object if the type is "Blob"
	 * 
	 * @return Object value
	 */
	public Object getBlobValue(Object value) {
		if (value != null && !(value instanceof String)) {
			return value;
		} else
			return null;
	}

	/**
	 * Get the Object if the type is "Clob"
	 * 
	 * @return Object value
	 */
	public String getClobValue(Object value) {
		if (value != null) {
			return value.toString();
		} else
			return null;
	}

	/**
	 * Get the Object if the type is "Binary"
	 * 
	 * @return Object value
	 */
	public Object getBinaryValue(Object value) {
		if (value != null && !(value instanceof String)) {
			return value;
		} else
			return null;
	}

	private String getDatabaseName(Connection conn) throws SQLException {
		return conn.getMetaData().getDatabaseProductName();
	}
}
