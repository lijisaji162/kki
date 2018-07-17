package com.polus.fibicomp.committee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.committee.service.CommitteeService;
import com.polus.fibicomp.committee.vo.CommitteeVo;

@RestController
public class CommitteeController {

	protected static Logger logger = Logger.getLogger(CommitteeController.class.getName());

	@Autowired
	@Qualifier(value = "committeeService")
	private CommitteeService committeeService;

	@RequestMapping(value = "/createCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createCommittee");
		logger.info("Committee Type Code : " + vo.getCommitteeTypeCode());
		String committeeDatas = committeeService.createCommittee(vo.getCommitteeTypeCode());
		return committeeDatas;
	}

	@RequestMapping(value = "/saveCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommittee");
		String committeeDatas = committeeService.saveCommittee(vo);
		return committeeDatas;
	}

	@RequestMapping(value = "/loadCommitteeById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadCommitteeById(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadCommitteeById");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		String committeeDatas = committeeService.loadCommitteeById(vo.getCommitteeId());
		return committeeDatas;
	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addSchedule");
		String committeeDatas = "";
		try {
			committeeDatas = committeeService.addSchedule(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return committeeDatas;
	}

	/*@RequestMapping(value = "/fetchInitialDatas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchInitialDatas(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchInitialDatas");
		String initialDatas = committeeService.fetchInitialDatas();
		return initialDatas;
	}*/

	@RequestMapping(value = "/saveAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveAreaOfResearch");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ResearchAreaCode : " + vo.getCommitteeResearchArea().getResearchAreaCode());
		logger.info("ResearchAreaDescription : " + vo.getCommitteeResearchArea().getResearchAreaDescription());
		return committeeService.saveAreaOfResearch(vo);
	}

	@RequestMapping(value = "/deleteAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteAreaOfResearch");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("researchAreaId : " + vo.getCommResearchAreasId());
		return committeeService.deleteAreaOfResearch(vo);
	}

	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteSchedule");
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("committeeId : " + vo.getCommitteeId());
		return committeeService.deleteSchedule(vo);
	}

	@RequestMapping(value = "/filterCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String filterCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for filterCommitteeScheduleDates");
		return committeeService.filterCommitteeScheduleDates(vo);
	}

	@RequestMapping(value = "/resetCommitteeScheduleDates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String resetCommitteeScheduleDates(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for resetCommitteeScheduleDates");
		return committeeService.resetCommitteeScheduleDates(vo);
	}

	@RequestMapping(value = "/updateCommitteeSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateCommitteeSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateCommitteeSchedule");
		logger.info("CommitteeId : " + vo.getCommitteeId());
		logger.info("ScheduleId : " + vo.getCommitteeSchedule().getScheduleId());
		String committeeDatas = committeeService.updateCommitteeSchedule(vo);
		return committeeDatas;
	}

}
