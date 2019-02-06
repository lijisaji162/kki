package com.polus.fibicomp.dao;

import java.util.ArrayList;
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
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.pojo.UnitAdministrator;
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;
import com.polus.fibicomp.view.PersonDetailsView;

@Transactional
@Service(value = "loginDao")
public class LoginDaoImpl implements LoginDao {

	protected static Logger logger = Logger.getLogger(LoginDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private RoleDao roleDao;

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
				// personDTO.setUnitAdministrators(isUnitAdmin(person.getPrncplId()));
				/*List<UnitAdministrator> unitAdministrators = isUnitAdmin(person.getPrncplId());
				if (unitAdministrators != null && !unitAdministrators.isEmpty()) {
					personDTO.setUnitAdmin(true);
				} else {
					personDTO.setUnitAdmin(false);
				}*/
				List<RoleMemberBo> unitAdminMemberBos = roleDao.fetchUserRole(person.getPrncplId(), Constants.UNIT_ADMINISTRATOR_ROLE);
				if (unitAdminMemberBos != null && !unitAdminMemberBos.isEmpty()) {
					personDTO.setUnitAdmin(true);
				} else {
					personDTO.setUnitAdmin(false);
				}
				personDTO.setLogin(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method readPersonData", e);
		}
		return personDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UnitAdministrator> isUnitAdmin(String personId) {
		logger.info("isUnitAdmin --- personId : " + personId);
		List<UnitAdministrator> unitAdministrators = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UnitAdministrator.class);
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("unitAdministratorTypeCode", "3"));
		unitAdministrators = criteria.list();
		return unitAdministrators;
	}

	@Override
	public List<Unit> isUnitAdminDetail(String personId) {
		logger.info("isUnitAdminDetail --- personId : " + personId);
		List<Unit> unitAdministrators = new ArrayList<>();
		List<RoleMemberBo> unitAdminMemberBos = roleDao.fetchUserRole(personId, Constants.UNIT_ADMINISTRATOR_ROLE);
		if (unitAdminMemberBos != null && !unitAdminMemberBos.isEmpty()) {
			for (RoleMemberBo unitAdminMemberBo : unitAdminMemberBos) {
				List<RoleMemberAttributeDataBo> attributeDataBos = unitAdminMemberBo.getAttributeDetails();
				if (attributeDataBos != null && !attributeDataBos.isEmpty()) {
					for (RoleMemberAttributeDataBo bo : attributeDataBos) {
						unitAdministrators.add(getUnitDetail(bo.getAttributeValue()));
					}
				}
			}
		}
		return unitAdministrators;
	}

	public Unit getUnitDetail(String unitNumber) {
		return hibernateTemplate.get(Unit.class, unitNumber);
	}

}
