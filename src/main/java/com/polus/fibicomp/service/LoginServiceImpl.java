package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.LoginDao;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.view.MobileProfile;

@Transactional
@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {

	protected static Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

	@Autowired
	private LoginDao loginDao;

	@Override
	public PersonDTO loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrincipalBo principalBo = null;
		PersonDTO personDTO = new PersonDTO();
		if ("USERID".equalsIgnoreCase(loginMode)) {
			principalBo = loginDao.authenticate(userName, password);
		}
		if (principalBo != null) {
			personDTO = loginDao.readPersonData(userName);
			personDTO.setLogin(true);
		}
		return personDTO;
	}

	@Override
	public boolean isUnitAdmin(String personId) {
		return loginDao.isUnitAdmin(personId);
	}

	@Override
	public String fibiMobileLogin(String login_mode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		try {
			PersonDTO personDTO =  loginCheck(login_mode, userName, password, request, response);
			if(personDTO.isLogin()) {
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Logged in successfully");
				mobileProfile.setData(personDTO);
			} else {
				mobileProfile.setMessage("Invalid login");
				mobileProfile.setData(null);
			}
		} catch (Exception e) {
			logger.error("Error in method LoginServiceImpl.fibiMobileLogin", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}
}
