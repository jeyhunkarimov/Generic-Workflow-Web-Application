package com.karimovceyhun.workflow.services;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.karimovceyhun.workflow.interfaces.IMailSenderService;



public class MailSenderService extends Service implements IMailSenderService,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4653098848022185080L;
	private  MailSender sender;
	
	public MailSenderService(SessionFactory sessionFactory,MailSender sender)
	{
		super(sessionFactory);
		this.sender = sender;
	}
	
	public void send(String mailReceiver, String content,String subjects,String mailSender)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailReceiver);
		message.setText(content);
		message.setSubject(subjects);
		message.setFrom(mailSender);
		getSender().send(message);
	}

	public MailSender getSender() {
		return sender;
	}

	public void setSender(MailSender sender) {
		this.sender = sender;
	}
}

