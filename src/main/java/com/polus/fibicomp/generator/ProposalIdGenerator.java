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

public class ProposalIdGenerator implements IdentifierGenerator {

	protected static Logger logger = Logger.getLogger(ProposalIdGenerator.class.getName());

	@Override
	public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
		String prefix = "A";
		Connection connection = sessionImplementor.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(1) from FIBI_SMU_PROPOSAL");

			if (rs.next()) {
				int id = rs.getInt(1) + 1001;
				String generatedId = prefix + "" + new Integer(id).toString();
				logger.info("Generated Application Id : " + generatedId);
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
