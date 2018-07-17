package com.polus.fibicomp.common.service;

import org.springframework.stereotype.Service;

@Service
public interface CommonService {

	public Long getNextSequenceNumber(String sequenceName);

	public boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName);

	public Integer getCurrentFiscalYear();

	public Integer getCurrentFiscalMonthForDisplay();

}
