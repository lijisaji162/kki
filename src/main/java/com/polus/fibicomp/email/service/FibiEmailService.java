package com.polus.fibicomp.email.service;

import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public interface FibiEmailService {

	/**
	 * @param to - defines the receiver
	 * @param subject - subject of mail
	 * @param text - body
	 */
	public void sendEmail(Set<String> toAddresses, String subject, Set<String> ccAddresses, Set<String> bccAddresses, String body, boolean htmlMessage);

}
