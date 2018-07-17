package com.polus.fibicomp.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.polus.fibicomp.committee.pojo.Committee;

public class CommitteeIdGenerator implements IdentifierGenerator {

	protected static Logger logger = Logger.getLogger(CommitteeIdGenerator.class.getName());

	@Override
	public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
		Committee committee = (Committee) object;
		String prefix = committee.getCommitteeType().getDescription();
		Connection connection = sessionImplementor.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(1) from FIBI_COMMITTEE");

			if (rs.next()) {
				int id = rs.getInt(1) + 101;
				String generatedId = prefix + "-" + new Integer(id).toString();
				logger.info("Generated Id: " + generatedId);
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
