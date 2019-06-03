package com.polus.fibicomp.questionnaire.transactions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.polus.fibicomp.dbengine.DBEngineConstants;
import com.polus.fibicomp.dbengine.DBException;
import com.polus.fibicomp.dbengine.Parameter;

import java.sql.Types;
public class TransactionBuilder {

	
	public void insertQuestionnaireAnsHeader(Integer moduleCode, String moduleItemId, String userId,
			Integer questionnaireId,Integer questionnaireHeaderId, Connection conn) throws Exception {
		
		HashMap<String, Object> hmInput = new HashMap<String, Object>();
		ArrayList<Parameter> inParam = new ArrayList<Parameter>();		
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();	

		inParam.add(new Parameter("<<QUESTIONNAIRE_ANS_HEADER_ID>>",DBEngineConstants.TYPE_INTEGER,questionnaireHeaderId));
		inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>",DBEngineConstants.TYPE_INTEGER,questionnaireId));
		inParam.add(new Parameter("<<MODULE_ITEM_CODE>>",DBEngineConstants.TYPE_INTEGER,moduleCode));
		inParam.add(new Parameter("<<MODULE_ITEM_ID>>",DBEngineConstants.TYPE_INTEGER,Integer.parseInt(moduleItemId)));
		inParam.add(new Parameter("<<QUESTIONNAIRE_COMPLETED_FLAG>>",DBEngineConstants.TYPE_STRING,"N"));
		inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>",DBEngineConstants.TYPE_DATE,getCurrentDate()));
		inParam.add(new Parameter("<<UPDATE_USER>>",DBEngineConstants.TYPE_STRING,userId));		
		dbconnection(TransactionsStatements.INSERT_QUESTIONNAIRE_ANS_HEADER,inParam,output,conn);		
	}
	
	public void insertQuestionnaireAnsHeader(ArrayList<Parameter> inParam, Connection conn) throws Exception {
		HashMap<String, Object> hmInput = new HashMap<String, Object>();		
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();	
		dbconnection(TransactionsStatements.INSERT_QUESTIONNAIRE_ANS_HEADER,inParam,output,conn);		
	}
	
	
	public static void main(String arg[]) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");  
	//	Connection con=DriverManager.getConnection("jdbc:mysql://192.168.1.46:3306/kuali","root",null);  	
		//TransactionsStatements st = new TransactionsStatements();	
		//TransactionBuilder tb = new TransactionBuilder();
		//tb.insertQuestionnaireAnsHeader(1, "53", "quickstart", 1003, 1,con);
				
	
		ResourceBundle mybundle = ResourceBundle.getBundle("com.polus.generic.utils.genericSQL");	
	}	
		
	
	
	private static void dbconnection(String sql,
			ArrayList<Parameter> inParam,
			ArrayList<HashMap<String, Object>> output,Connection con){
		
		try{

		//Class.forName("oracle.jdbc.driver.OracleDriver");
		
		
		  
		  
		String statement = buildStatement(sql,inParam,output);
		
		//Connection con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.60:1521:coeusmig","coeusdev","coeusdev");  
		
		
		
		
		PreparedStatement stmt=con.prepareStatement(statement);  
		stmt = setParameter(stmt,inParam);		
		int i =stmt.executeUpdate();  
		/*ResultSet rs;
		while (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rsmd.getColumnType(1) == Types.VARCHAR){
				
			}
			int col = rsmd.getColumnType(1);
			String colName = rsmd.getColumnName(1);
			
		}*/
		
		System.out.println(" records inserted");  
		  
		con.close();  
		  
		}catch(Exception e){
			System.out.println(e);
		}  		  
		
	}
	
	private static String buildStatement(String sql,
			ArrayList<Parameter> inParam,
			ArrayList<HashMap<String, Object>> output){
			sql = sql.replaceAll("<<([a-zA-Z])\\w+>>", "?");
			return sql;
	}
	
	private static PreparedStatement setStatement(PreparedStatement stmt,ArrayList<Parameter> inParam) throws SQLException{
		int index = 1;
		for(Parameter input: inParam){
			if(input.getParameterType().equals(DBEngineConstants.TYPE_STRING)){				
				stmt.setString(index,(String) input.getParameterValue());				
				
			}else if(input.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)){
				stmt.setInt(index,(Integer) input.getParameterValue());				
				
			}else if(input.getParameterType().equals(DBEngineConstants.TYPE_DATE)){
				stmt.setDate(index,(java.sql.Date)input.getParameterValue());				
				
			}				
			
			index++;
		}	
		return stmt;
	}
	
	private static Date getCurrentDate(){
		return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	}
	
	
	private static PreparedStatement setParameter(PreparedStatement preparedStatement, ArrayList<Parameter> inParam)
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

	private static  ArrayList<HashMap<String, Object>> packageResult(ResultSet rset) {
		ArrayList<HashMap<String, Object>> alResSet = new ArrayList<>();
		try {
			java.sql.ResultSetMetaData metaData = rset.getMetaData();
			int colCount = metaData.getColumnCount();
			String colName = null;
			Object colValue = null;
			while (rset.next()) {
				HashMap<String, Object> htRow = new HashMap<>();
				for (int i = 1; i <= colCount; i++) {
					colName = metaData.getColumnName(i);
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

	public static  Integer getIntegerValue(Object value) throws DBException {
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
     *  Get the long value if the type is "Long"
     *  @return double Long value
     */
    public static  double getLongValue(Object value) {
        if (value!=null && value instanceof String ){
            return (Long.valueOf((String)value)).longValue();
        }
        else
            return -1;
    }
    /**
     *  Get the String value if the type is "String"
     *  @return String value
     */
    public static  String getStrValue(Object value){
        if (value!=null && value instanceof String )
            return (String)value;
        else
            return null;        
    }
    
    /**
     *  Get the <code>java.sql.Date</code> value if the type is "Date"
     *  @return Date value
     */
    public static  java.sql.Date getDateValue(Object value) throws DBException{
        if (value != null ){
            /*if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else
             */
            if(value instanceof java.sql.Date){
                return (java.sql.Date)value;
            }else {
                String exMsg = "Wrong type:\n Expected type of the "+value+
                                "is : java.sql.Date\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Timestamp</code> value if the type is "Timestamp"
     *  @return Timestamp Timestamp value
     */
    public static  java.sql.Timestamp getTimestampValue(Object value) throws DBException{
        if (value!=null ){
            if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.TimeStamp\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Time</code> value if the type is "Time"
     *  @return Time Time value
     */
    public static  java.sql.Time getTimeValue(Object value) throws DBException{
        if (value!=null){
            if(value instanceof java.sql.Time){
                return (java.sql.Time)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.Time\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the int value if the type is "int"
     *  @return int value
     */
    public static  int getIntValue(Object value) throws DBException{
        if(value==null)
            return -1;
        if ((value instanceof Integer || value instanceof String)){
            return Integer.parseInt(value.toString());
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Integer\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }   
//        return -1;
    }
    /**
     *  Get the float value if the type is "float"
     *  @return float value
     */
    public static  float getFloatValue(Object value) throws DBException{
        if(value==null)
            return 0;
        if ((value instanceof Float  || value instanceof String)){
            return (Float.valueOf(value.toString())).floatValue();
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
//        return -1;
    }
    /**
     *  Get the Float value if the type is "Float"
     *  @return Float value
     */
    public static  Float getFloatObjValue(Object value) throws DBException{
        if(value==null)
            return null;
        if (value instanceof Float){
            return (Float)value;
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
    }
    /**
     *  Get the double value if the type is "double"
     *  @return double value
     */
    public static double getDoubleValue(Object value) throws DBException{
        if(value==null)
            return 0;
        if ((value instanceof Double || value instanceof String )){
            return (Double.valueOf(value.toString())).doubleValue();
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.String or java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Double value if the type is "Double"
     *  @return Double value
     */
    public static Double getDoubleObjValue(Object value) throws DBException{
        if(value==null)
            return null;
        if (value instanceof Double){
            return (Double)value;
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Object if the type is "Blob"
     *  @return Object value
     */
    public static Object  getBlobValue(Object value) {
        if (value!=null && !(value instanceof String)  ){
            return value;
        }
        else
            return null;
    }
    /**
     *  Get the Object if the type is "Clob"
     *  @return Object value
     */
    public static String  getClobValue(Object value) {
        if (value!=null){
            return value.toString();
        }else
            return null;
    }
    /**
     *  Get the Object if the type is "Binary"
     *  @return Object value
     */
    public static Object  getBinaryValue(Object value) {
        if (value!=null && !(value instanceof String)  ){
            return value;
        }
        else
            return null;
    }
	
	
}
