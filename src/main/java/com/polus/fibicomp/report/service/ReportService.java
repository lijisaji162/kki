package com.polus.fibicomp.report.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.report.vo.ReportVO;

@Service
public interface ReportService {

	public String applicationReport(ReportVO reportVO);

	public String fetchReportData(ReportVO reportVO);

}
