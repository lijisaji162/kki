package com.polus.fibicomp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.pojo.UnitAdministrator;
import com.polus.fibicomp.view.PersonDetailsView;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

@Transactional
@Service(value = "loginDao")
public class LoginDaoImpl implements LoginDao {

	protected static Logger logger = Logger.getLogger(LoginDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public PrincipalBo authenticate(String userName, String password) {
		PrincipalBo principalBo = null;
		try {
			logger.info("userName : " + userName + " and password : " + password);
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria crit = session.createCriteria(PrincipalBo.class);
			crit.add(Restrictions.eq("principalName", userName));
			principalBo = (PrincipalBo) crit.uniqueResult();
			logger.info("principalBo :" + principalBo);
		} catch (Exception e) {
			logger.debug("Sql Exception: " + e);
		}
		return principalBo;
	}

	public PersonDTO readPersonData(String userName) {
		PersonDTO personDTO = new PersonDTO();
		try {
			logger.info("readPersonData :" + userName);
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(PersonDetailsView.class);
			criteria.add(Restrictions.eq("prncplName", userName));
			PersonDetailsView person = (PersonDetailsView) criteria.uniqueResult();
			logger.info("Person Detail :" + person);
			if (person != null) {
				personDTO.setPersonID(person.getPrncplId());
				personDTO.setFirstName(person.getFirstName());
				personDTO.setLastName(person.getLastName());
				personDTO.setFullName(person.getFullName());
				personDTO.setEmail(person.getEmailAddress());
				personDTO.setUnitNumber(person.getUnitNumber());
				personDTO.setUserName(userName);
				personDTO.setUnitAdmin(isUnitAdmin(person.getPrncplId()));
				personDTO.setLogin(true);
				personDTO.setGrantManager(isGrantManager(person.getPrncplId()));
				personDTO.setProvost(isProvost(person.getPrncplId()));
				personDTO.setReviewer(isReviewer(person.getPrncplId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method readPersonData", e);
		}
		return personDTO;
	}

	@Override
	public boolean isUnitAdmin(String personId) {
		logger.info("isUnitAdmin --- personId : " + personId);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		boolean isAdmin = false;
		Criteria criteria = session.createCriteria(UnitAdministrator.class);
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("unitAdministratorTypeCode", "3"));
		@SuppressWarnings("unchecked")
		List<UnitAdministrator> administrators = criteria.list();
		if (administrators != null && !administrators.isEmpty()) {
			isAdmin = true;
		}
		logger.info("isAdmin : " + isAdmin);
		return isAdmin;
	}

	public boolean isGrantManager(String personId) {
		logger.info("isGrantManager --- personId : " + personId);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		boolean isGrantManager = false;
		Criteria criteria = session.createCriteria(UnitAdministrator.class);
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("unitAdministratorTypeCode", Constants.SMU_GRANT_MANAGER_CODE));
		@SuppressWarnings("unchecked")
		List<UnitAdministrator> administrators = criteria.list();
		if (administrators != null && !administrators.isEmpty()) {
			isGrantManager = true;
		}
		logger.info("isGrantManager : " + isGrantManager);
		return isGrantManager;
	}

	public boolean isProvost(String personId) {
		logger.info("isProvost --- personId : " + personId);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		boolean isProvost = false;
		Criteria criteria = session.createCriteria(UnitAdministrator.class);
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("unitAdministratorTypeCode", Constants.SMU_GRANT_PROVOST_CODE));
		@SuppressWarnings("unchecked")
		List<UnitAdministrator> administrators = criteria.list();
		if (administrators != null && !administrators.isEmpty()) {
			isProvost = true;
		}
		logger.info("isProvost : " + isProvost);
		return isProvost;
	}

	public boolean isReviewer(String personId) {
		logger.info("isReviewer --- personId : " + personId);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		boolean isReviewer = false;
		Criteria criteria = session.createCriteria(WorkflowMapDetail.class);
		criteria.add(Restrictions.eq("approverPersonId", personId));
		criteria.add(Restrictions.eq("roleTypeCode", Constants.REVIEWER_ROLE_TYPE_CODE));
		@SuppressWarnings("unchecked")
		List<WorkflowMapDetail> reviewers = criteria.list();
		if (reviewers != null && !reviewers.isEmpty()) {
			isReviewer = true;
		}
		logger.info("isReviewer : " + isReviewer);
		return isReviewer;
	}
}
