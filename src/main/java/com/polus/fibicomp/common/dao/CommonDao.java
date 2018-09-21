package com.polus.fibicomp.common.dao;

import org.springframework.stereotype.Service;

@Service
public interface CommonDao {

	/**
	 * This method is used to get next sequence number.
	 * @param sequenceName - name of the sequence.
	 * @return sequence number.
	 */
	public Long getNextSequenceNumber(String sequenceName);

	/**
	 * This method is used to parameter value as boolean.
	 * @param namespaceCode - Namespace code of the parameter.
	 * @param componentCode - Component code of the parameter.
	 * @param parameterName - Name of the parameter.
	 * @return boolean value of parameter.
	 */
	public boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName);

	/**
	 * This method is used to parameter value.
	 * @param namespaceCode - Namespace code of the parameter.
	 * @param componentCode - Component code of the parameter.
	 * @param parameterName - Name of the parameter.
	 * @return value of parameter.
	 */
	public Integer getParameter(String namespaceCode, String componentCode, String parameterName);

	/**
	 * This method is used to parameter value as string.
	 * @param namespaceCode - Namespace code of the parameter.
	 * @param componentCode - Component code of the parameter.
	 * @param parameterName - Name of the parameter.
	 * @return value of parameter.
	 */
	public String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName);

}
