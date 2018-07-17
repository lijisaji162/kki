package com.polus.fibicomp.common.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.common.dao.CommonDao;

@Transactional
@Service(value = "commonService")
public class CommonServiceImpl implements CommonService {

	protected static Logger logger = Logger.getLogger(CommonServiceImpl.class.getName());

	protected static final String FISCAL_YEAR_MONTH_PARAMETER_NAME = "KC_FISCAL_START_MONTH";
    protected static final String KC_GENERAL_NAMESPACE = "KC-GEN";
    protected static final String DOCUMENT_COMPONENT_NAME = "Document";
	protected static final String MONTH_KEY = "month";
    protected static final String YEAR_KEY = "year";

	@Autowired
	private CommonDao commonDao;

	@Override
	public Long getNextSequenceNumber(String sequenceName) {
		return commonDao.getNextSequenceNumber(sequenceName);
	}

	@Override
	public boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName) {
		return commonDao.getParameterValueAsBoolean(namespaceCode, componentCode, parameterName);
	}

	@Override
	public Integer getCurrentFiscalYear() {			       
        return getCurrentFiscalData(null).get(YEAR_KEY);
	}

	@Override
	public Integer getCurrentFiscalMonthForDisplay() {
		 return getCurrentFiscalData(null).get(MONTH_KEY) + 1;
	}

	protected Integer getFiscalYearMonth() {
		return commonDao.getParameter(KC_GENERAL_NAMESPACE, DOCUMENT_COMPONENT_NAME, FISCAL_YEAR_MONTH_PARAMETER_NAME);
    }

	private int findMonth(int startingMonth, int currentMonth) {
        /**
         * We are building an array of integers.  The array position number is the fiscal month position of the calendar month.
         * The array values are the calendar months.  So an example with a fiscal year starting in September would be as follows:
         * YEAR[0] = Calendar.September
         * Year[1] = Calendar.October
         * Year[11 = Calendar.August
         */
        int nextMonth = startingMonth;
        int[] YEAR = {0, 1, 2, 3, 4, 5, 6 ,7, 8, 9, 10, 11};
        for (int i = 0; i < 12; i++) {
            YEAR[i] = nextMonth;
            if (nextMonth == 11) {
                nextMonth = 0;
            } else {
                nextMonth++;
            }
        }
        for (int i : YEAR) {
            if (YEAR[i] == currentMonth) {
                return i;
            }
        }
        throw new IllegalArgumentException("Could not find the current month: " + currentMonth);
    }

	protected Map<String, Integer> getCurrentFiscalData(Calendar calendar) {
		Map<String, Integer> data = new HashMap<String, Integer>();
        
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        Integer fiscalStartMonth = getFiscalYearMonth();
        //assuming July is the fiscal start month         
        if (calendar.get(Calendar.MONTH) == fiscalStartMonth.intValue()) {
            //July 1st, 2012, is the 1st month of FY 2013 
            data.put(MONTH_KEY, findMonth(fiscalStartMonth, calendar.get(Calendar.MONTH)));
            if (fiscalStartMonth.equals(Calendar.JANUARY)) {
                data.put(YEAR_KEY, calendar.get(Calendar.YEAR));
            } else {
                data.put(YEAR_KEY, calendar.get(Calendar.YEAR) + 1);
            }
        } else if (calendar.get(Calendar.MONTH) > fiscalStartMonth.intValue()) {
            //August 1st 2012, is the second month of FY 2013
            data.put(MONTH_KEY, findMonth(fiscalStartMonth, calendar.get(Calendar.MONTH)));
            if (fiscalStartMonth.equals(Calendar.JANUARY)) {
                data.put(YEAR_KEY, calendar.get(Calendar.YEAR));
            } else {
                data.put(YEAR_KEY, calendar.get(Calendar.YEAR) + 1);
            }
        } else {
            //June 1st 2012, is the 12th month of FY 2012
            data.put(MONTH_KEY, findMonth(fiscalStartMonth, calendar.get(Calendar.MONTH)));
            data.put(YEAR_KEY, calendar.get(Calendar.YEAR));
        }        
        return data;
	}
}
