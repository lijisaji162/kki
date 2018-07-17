package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.service.LoginService;
import com.polus.fibicomp.view.MobileProfile;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class LoginController {

	protected static Logger logger = Logger.getLogger(LoginController.class.getName());

	@Autowired
	@Qualifier(value = "loginService")
	private LoginService loginService;

	@Value("${LOGIN_MODE}")
	private String login_mode;

	/*@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> loginUser(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("Received request for login: ");
		HttpStatus status = HttpStatus.OK;
		String userName = vo.getUserName();
		String password = vo.getPassword();
		PersonDTO personDTO = loginService.loginCheck(login_mode, userName, password, request, response);
		if (!personDTO.isLogin()) {
			status = HttpStatus.BAD_REQUEST;
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(personDTO);
		return new ResponseEntity<String>(responseData, status);
	}*/

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Log Out");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/fibiLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fibiMobileLogin(@RequestBody CommonVO vo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("Received request for Mobile login: ");
		String userName = vo.getUserName();
		String password = vo.getPassword();
		return loginService.fibiMobileLogin(login_mode, userName, password, request, response);
	}

	@RequestMapping(value = "/fibiLogout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fibiMobileLogout(@RequestBody CommonVO vo, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		try {
			String status = logout(model, request, response);
			if("SUCCESS".equalsIgnoreCase(status)) {
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Logged out successfully");
			} else {
				mobileProfile.setMessage("Logout failed");
			}
		} catch (Exception e) {
			logger.error("Error in method LoginController.fibiMobileLogout", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}
}
