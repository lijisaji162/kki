package com.polus.fibicomp.common.service;

import org.springframework.stereotype.Service;

@Service
public interface CommonService {

	/**
	 * This method is used to get next sequence number.
	 * @param sequenceName - Name of the sequence.
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
	 * This method is used to get current fiscal year.
	 * @return fiscal year.
	 */
	public Integer getCurrentFiscalYear();

	/**
	 * This method is used to get current fiscal month.
	 * @return fiscal month.
	 */
	public Integer getCurrentFiscalMonthForDisplay();

}
