package com.polus.fibicomp.schedule.dao;

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

import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachType;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachment;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttendance;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleMinutes;
import com.polus.fibicomp.committee.pojo.MinuteEntryType;
import com.polus.fibicomp.committee.pojo.ProtocolContingency;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.view.ProtocolView;

@Transactional
@Service(value = "scheduleDao")
public class ScheduleDaoImpl implements ScheduleDao {

	protected static Logger logger = Logger.getLogger(ScheduleDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public ProtocolView fetchProtocolViewByParams(Integer protocolId, String personId, String fullName) {
		ProtocolView protocolView = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolView.class);
		criteria.add(Restrictions.eq("protocolId", protocolId));
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("fullName", fullName));
		protocolView = (ProtocolView) criteria.uniqueResult();
		return protocolView;
	}

	@Override
	public List<ScheduleActItemType> fetchAllScheduleActItemType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleActItemType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("scheduleActItemTypecode"), "scheduleActItemTypecode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleActItemType.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ScheduleActItemType> scheduleActItemTypes = criteria.list();
		return scheduleActItemTypes;
	}

	@Override
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);
		return committeeSchedule;
	}

	@Override
	public CommitteeScheduleActItems addOtherActions(CommitteeScheduleActItems committeeScheduleActItems) {
		hibernateTemplate.save(committeeScheduleActItems);
		return committeeScheduleActItems;
	}

	@Override
	public List<MinuteEntryType> fetchAllMinuteEntryTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MinuteEntryType.class);
		criteria.addOrder(Order.asc("sortId"));
		@SuppressWarnings("unchecked")
		List<MinuteEntryType> minuteEntrytypes = criteria.list();
		return minuteEntrytypes;
	}

	@Override
	public List<ProtocolContingency> fetchAllProtocolContingency() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolContingency.class);
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ProtocolContingency> protocolContingencies = criteria.list();
		return protocolContingencies;
	}

	@Override
	public List<CommitteeScheduleAttachType> fetchAllCommitteeScheduleAttachType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeScheduleAttachType.class);
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<CommitteeScheduleAttachType> committeeScheduleAttachTypes = criteria.list();
		return committeeScheduleAttachTypes;
	}

	@Override
	public CommitteeScheduleMinutes addCommitteeScheduleMinute(CommitteeScheduleMinutes committeeScheduleMinutes) {
		hibernateTemplate.save(committeeScheduleMinutes);
		return committeeScheduleMinutes;
	}

	@Override
	public CommitteeScheduleAttendance addCommitteeScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance) {
		hibernateTemplate.save(scheduleAttendance);
		return scheduleAttendance;
	}

	@Override
	public CommitteeScheduleAttachment addScheduleAttachment(CommitteeScheduleAttachment committeeScheduleAttachment) {
		hibernateTemplate.save(committeeScheduleAttachment);
		return committeeScheduleAttachment;
	}

	@Override
	public CommitteeScheduleAttachment fetchAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(CommitteeScheduleAttachment.class, attachmentId);
	}

}
