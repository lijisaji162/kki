package com.polus.fibicomp.report.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.vo.CommonVO;

@Service
public interface ReportService {

	public String applicationReport(ReportVO reportVO);

	public String fetchReportData(ReportVO reportVO);

	public XSSFWorkbook getXSSFWorkbookForReport(CommonVO vo) throws Exception;

	public String getProposalDataBySponsorTypes(ReportVO reportVO);

}
