package com.polus.fibicomp.common.dao;

import org.springframework.stereotype.Service;

@Service
public interface CommonDao {

	public Long getNextSequenceNumber(String sequenceName);

	public boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName);

	public Integer getParameter(String namespaceCode, String componentCode, String parameterName);

	public String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName);

}
