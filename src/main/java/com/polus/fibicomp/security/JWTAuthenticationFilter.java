package com.polus.fibicomp.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.common.dao.CommonDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.dao.LoginDao;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	protected static Logger logger = Logger.getLogger(JWTAuthenticationFilter.class.getName());

	private AuthenticationManager authenticationManager;
	private LoginDao loginDao;
	private RoleDao roleDao;
	private CommonDao commonDao;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, LoginDao loginDao, RoleDao roleDao, ProposalDao proposalDao, CommonDao commonDao) throws Exception {
		this.authenticationManager = authenticationManager;
		this.loginDao = loginDao;
		this.roleDao = roleDao;
		this.commonDao = commonDao;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			PrincipalBo creds = new ObjectMapper().readValue(req.getInputStream(), PrincipalBo.class);
			String encryptedPWD = "";
			try {
				encryptedPWD = hash(creds.getPassword());
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getPrincipalName(), encryptedPWD, new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		logger.info("-------- successfulAuthentication --------");
		String token = Jwts.builder().setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, Constants.SECRET).compact();

		PersonDTO personDTO = new PersonDTO();
		personDTO = loginDao.readPersonData(((User) auth.getPrincipal()).getUsername());
		String proposalCreatorRole = commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.PROPOSAL_CREATOR_ROLE);
		List<RoleMemberBo> memberBos = roleDao.fetchCreateProposalPersonRole(personDTO.getPersonID(), proposalCreatorRole);
		if (memberBos != null && !memberBos.isEmpty()) {
			Set<String> unitNumbers = new HashSet<>();
			for (RoleMemberBo memberBo : memberBos) {
				List<RoleMemberAttributeDataBo> attributeDataBos = memberBo.getAttributeDetails();
				if (attributeDataBos != null && !attributeDataBos.isEmpty()) {
					for (RoleMemberAttributeDataBo bo : attributeDataBos) {
						unitNumbers.add(bo.getAttributeValue());
					}
				}
			}

			logger.info("create proposal unitNumbers : " + unitNumbers);
			if (!unitNumbers.isEmpty()) {
				// personDTO.setLeadUnits(proposalDao.fetchLeadUnitsByUnitNumbers(unitNumbers));
				personDTO.setCreateProposal(true);
			}
		}
		String superUserRole = commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.KC_SUPERUSER_ROLE);
		boolean isSuperUser = roleDao.fetchSuperUserPersonRole(personDTO.getPersonID(), superUserRole);
		personDTO.setSuperUser(isSuperUser);
		personDTO.setJwtRoles(auth.getAuthorities());
		String response = new ObjectMapper().writeValueAsString(personDTO);
		res.getWriter().write(response);
		res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);
	}

	public String hash(Object valueToHide) throws GeneralSecurityException {
		if (valueToHide != null && !StringUtils.isEmpty(valueToHide.toString())) {
			try {
				MessageDigest md = MessageDigest.getInstance(Constants.HASH_ALGORITHM);
				return new String(Base64.encodeBase64(md.digest(valueToHide.toString().getBytes(Constants.CHARSET))),
						Constants.CHARSET);
			} catch (UnsupportedEncodingException arg2) {
				return "";
			}
		} else {
			return "";
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse res,
			AuthenticationException failed) throws IOException, ServletException {
		logger.info("-------- unsuccessfulAuthentication --------");
		PersonDTO personDTO = new PersonDTO();
		personDTO.setLogin(false);
		String response = new ObjectMapper().writeValueAsString(personDTO);
		res.getWriter().write(response);
	}

}
