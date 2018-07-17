package com.polus.fibicomp.email.service;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.common.dao.CommonDao;
import com.polus.fibicomp.constants.Constants;

@Transactional
@Configuration
@Service(value = "fibiEmailService")
public class FibiEmailServiceImpl implements FibiEmailService {

	protected static Logger logger = Logger.getLogger(FibiEmailServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "mailSender")
	public JavaMailSenderImpl mailSender;

	@Autowired
	public CommonDao commonDao;

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private String port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String tls;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void sendEmail(Set<String> toAddresses, String subject, Set<String> ccAddresses, Set<String> bccAddresses, String body, boolean htmlMessage) {
		logger.info("Received request for mail sending");
		if (mailSender != null) {
			if (CollectionUtils.isEmpty(toAddresses)) {
				return;
			}
			
			mailSender.setUsername(username);
			mailSender.setPassword(password);
			Properties props = mailSender.getJavaMailProperties();
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.starttls.enable", tls);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.host", host);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = null;

			try {
				helper = new MimeMessageHelper(message, true, Constants.CHARSET);

				if (StringUtils.isNotBlank(subject)) {
					helper.setSubject(subject);
				} else {
					logger.warn("Sending message with empty subject.");
				}

				if (isEmailTestEnabled()) {
					helper.setText(getTestMessageBody(body, toAddresses, ccAddresses, bccAddresses), true);
					String toAddress = getEmailNotificationTestAddress();
					if (StringUtils.isNotBlank(toAddress)) {
						helper.addTo(toAddress);
					}
				} else {
					helper.setText(body, htmlMessage);
					if (CollectionUtils.isNotEmpty(toAddresses)) {
						for (String toAddress : toAddresses) {
							try {
								helper.addTo(toAddress);
							} catch (Exception ex) {
								logger.warn("Could not set to address:", ex);
							}
						}
					}
					if (CollectionUtils.isNotEmpty(ccAddresses)) {
						for (String ccAddress : ccAddresses) {
							try {
								helper.addCc(ccAddress);
							} catch (Exception ex) {
								logger.warn("Could not set to address:", ex);
							}
						}
					}
					if (CollectionUtils.isNotEmpty(bccAddresses)) {
						for (String bccAddress : bccAddresses) {
							try {
								helper.addBcc(bccAddress);
							} catch (Exception ex) {
								logger.warn("Could not set to address:", ex);
							}
						}
					}
				}
				executorService.execute(() -> mailSender.send(message));

			} catch (MessagingException ex) {
				logger.error("Failed to create mime message helper.", ex);
			}
		} else {
			logger.info("Failed to send email due to inability to obtain valid email mailSender, please check your configuration.");
		}
	}

	private boolean isEmailTestEnabled() {
		return commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, "EMAIL_NOTIFICATION_TEST_ENABLED");
	}

	private String getTestMessageBody(String body, Set<String> toAddresses, Set<String> ccAddresses, Set<String> bccAddresses) {
		StringBuffer testEmailBody = new StringBuffer("");
		testEmailBody.append("-----------------------------------------------------------<br/>");
		testEmailBody.append("TEST MODE<br/>");
		testEmailBody.append("In Production mode this mail will be sent to the following... <br/>");
		if (CollectionUtils.isNotEmpty(toAddresses)) {
			testEmailBody.append("TO: ");
			testEmailBody.append(toAddresses);
		}
		if (CollectionUtils.isNotEmpty(ccAddresses)) {
			testEmailBody.append("CC: ");
			testEmailBody.append(toAddresses);
		}
		if (CollectionUtils.isNotEmpty(bccAddresses)) {
			testEmailBody.append("BCC: ");
			testEmailBody.append(toAddresses);
		}
		testEmailBody.append("<br/>-----------------------------------------------------------");
		return testEmailBody.toString() + "<br/>" + body;

	}

	private String getEmailNotificationTestAddress() {
		return commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, "EMAIL_NOTIFICATION_TEST_ADDRESS");
	}

}
