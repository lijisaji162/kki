package com.polus.fibicomp.role.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.role.pojo.RoleMemberBo;

@Transactional
@Service(value = "roleDao")
public class RoleDaoImpl implements RoleDao {

	protected static Logger logger = Logger.getLogger(RoleDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<RoleMemberBo> fetchCreateProposalPersonRole(String personId, String roleId) {
		List<RoleMemberBo> roleMemberBos = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(RoleMemberBo.class);
		criteria.add(Restrictions.eq("memberId", personId));
		criteria.add(Restrictions.eq("roleId", roleId));
		@SuppressWarnings("unchecked")
		List<RoleMemberBo> createProposalRoles = criteria.list();
		if (createProposalRoles != null && !createProposalRoles.isEmpty()) {
			roleMemberBos = new ArrayList<>();
			for (RoleMemberBo memberBo : createProposalRoles) {
				if (memberBo.isActive()) {
					roleMemberBos.add(memberBo);
				}
			}
		}
		return roleMemberBos;
	}

}
