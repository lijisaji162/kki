package com.polus.fibicomp.committee.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberExpertise;
import com.polus.fibicomp.committee.pojo.CommitteeMemberRoles;
import com.polus.fibicomp.committee.pojo.CommitteeMembershipType;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.MembershipRole;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

@Transactional
@Service(value = "committeeDao")
public class CommitteeDaoImpl implements CommitteeDao {

	protected static Logger logger = Logger.getLogger(CommitteeDaoImpl.class.getName());

	private static final String DESCRIPTION = "description";

    private static final String SCHEDULED = "Scheduled";

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<ProtocolReviewType> fetchAllReviewType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolReviewType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("reviewTypeCode"), "reviewTypeCode");
		projList.add(Projections.property(DESCRIPTION), DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolReviewType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProtocolReviewType> reviewTypes = criteria.list();
		return reviewTypes;
	}

	@Override
	public List<Unit> fetchAllHomeUnits() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Unit.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("unitName"));
		@SuppressWarnings("unchecked")
		List<Unit> units = criteria.list();
		return units;
	}

	@Override
	public List<ResearchArea> fetchAllResearchAreas() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ResearchArea.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchAreaCode"), "researchAreaCode");
		projList.add(Projections.property(DESCRIPTION), DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ResearchArea.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ResearchArea> researchAreas = criteria.list();
		return researchAreas;
	}

	@Override
	public CommitteeType fetchCommitteeType(Integer committeeTypeCode) {
		CommitteeType committeeType = null;
		committeeType = hibernateTemplate.get(CommitteeType.class, committeeTypeCode);
		return committeeType;
	}

	@Override
	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return new Timestamp(this.getCurrentDate().getTime());
	}

	@Override
	public String convertObjectToJSON(Object object) {
		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			response = mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Committee saveCommittee(Committee committee) {
		hibernateTemplate.saveOrUpdate(committee);
		return committee;
	}

	@Override
	public Committee fetchCommitteeById(String committeeId) {
		Committee committee = null;
		committee = hibernateTemplate.get(Committee.class, committeeId);
		return committee;
	}

	@Override
	public ScheduleStatus fetchScheduleStatusByStatus(String scheduleStatus) {
		ScheduleStatus status = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleStatus.class);
		criteria.add(Restrictions.eq(DESCRIPTION, SCHEDULED));
		status = (ScheduleStatus) criteria.list().get(0);
		return status;
	}

	@Override
	public List<Committee> loadAllCommittee() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Committee.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("committeeId"), "committeeId");
		projList.add(Projections.property("committeeName"), "committeeName");
		projList.add(Projections.property("homeUnitNumber"), "homeUnitNumber");
		projList.add(Projections.property("homeUnitName"), "homeUnitName");
		projList.add(Projections.property("reviewTypeDescription"), "reviewTypeDescription");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Committee.class));
		criteria.addOrder(Order.asc("updateTimestamp"));
		@SuppressWarnings("unchecked")
		List<Committee> committees = criteria.list();
		return committees;
	}

	@Override
	public List<PersonDetailsView> getAllEmployees() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(PersonDetailsView.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("prncplId"), "prncplId");
		projList.add(Projections.property("fullName"), "fullName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(PersonDetailsView.class));
		criteria.addOrder(Order.asc("fullName"));
		@SuppressWarnings("unchecked")
		List<PersonDetailsView> employeesList = criteria.list();
		return employeesList;
	}

	@Override
	public List<Rolodex> getAllNonEmployees() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Rolodex.class);
		criteria.add(Restrictions.eq("active", true));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("rolodexId"), "rolodexId");
		projList.add(Projections.property("firstName"), "firstName");
		projList.add(Projections.property("lastName"), "lastName");
		projList.add(Projections.property("middleName"), "middleName");
		projList.add(Projections.property("prefix"), "prefix");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Rolodex.class));
		criteria.addOrder(Order.asc("lastName"));
		@SuppressWarnings("unchecked")
		List<Rolodex> nonEmployeesList = criteria.list();
		return nonEmployeesList;
	}

	@Override
	public List<CommitteeMembershipType> getMembershipTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeMembershipType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipTypeCode"), "membershipTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeMembershipType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<CommitteeMembershipType> membershipTypeList = criteria.list();
		return membershipTypeList;
	}

	@Override
	public List<MembershipRole> getMembershipRoles() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MembershipRole.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipRoleCode"), "membershipRoleCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(MembershipRole.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<MembershipRole> membershipRoleList = criteria.list();
		return membershipRoleList;
	}

	@Override
	public PersonDetailsView getPersonDetailsById(String personId) {
		PersonDetailsView person = hibernateTemplate.get(PersonDetailsView.class, personId);
		return person;
	}

	@Override
	public void deleteMemberRoles(Integer roleId) {
		hibernateTemplate.delete(hibernateTemplate.get(CommitteeMemberRoles.class, roleId));
	}

	@Override
	public void deleteExpertise(Integer expertiseId) {
		hibernateTemplate.delete(hibernateTemplate.get(CommitteeMemberExpertise.class, expertiseId));
	}

	@Override
	public Rolodex getRolodexById(Integer rolodexId) {
		Rolodex rolodex = hibernateTemplate.get(Rolodex.class, rolodexId);
		return rolodex;
	}

	@Override
	public void updateCommitteSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);
	}

	@Override
	public CommitteeSchedule getCommitteeScheduleById(Integer scheduleId) {
		CommitteeSchedule committeeSchedule = hibernateTemplate.get(CommitteeSchedule.class, scheduleId);
		return committeeSchedule;
	}

	@Override
	public CommitteeMembershipType getCommitteeMembershipTypeById(String membershipTypeCode) {
		CommitteeMembershipType committeeMembershipType = hibernateTemplate.get(CommitteeMembershipType.class, membershipTypeCode);
		return committeeMembershipType;
	}

	@Override
	public List<ScheduleStatus> fetchAllScheduleStatus() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleStatus.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("scheduleStatusCode"), "scheduleStatusCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleStatus.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ScheduleStatus> status = criteria.list();
		return status;
	}

	@Override
	public CommitteeMemberRoles saveCommitteeMemberRole(CommitteeMemberRoles memberRole) {
		hibernateTemplate.save(memberRole);
		return memberRole;
	}

	@Override
	public CommitteeMemberExpertise saveCommitteeMemberExpertise(CommitteeMemberExpertise expertise) {
		hibernateTemplate.save(expertise);
		return expertise;
	}

	@Override
	public CommitteeResearchAreas saveCommitteeResearchAreas(CommitteeResearchAreas researchAreas) {
		hibernateTemplate.save(researchAreas);
		return researchAreas;
	}

}
