package com.karimovceyhun.workflow.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="emailnotification")
public class EmailNotification implements Bean,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7483734325348712174L;

	public static String SENDER_MAIL = "noreply@GWorkflow.com";
	public static String STATUS_SUBJECT = "Status changed";
	
	private static int PROCESS = 1;
	private static int DECISION = 2;
	private static int SEPERATOR = 3;
	private static int COLLECTOR = 4;
	
	public static String		ACKNOWLEDGE = "Acknowledge";
	public static String		ASSIGN = "Assign";
	public static String		SEND_BACK = "Send Back";
	public static String		ADD_NOTE = "Add Note";
	public static String		HOLD = "Assign";
	public static String		CHANGE_STATUS = "Change Status";

	public static String		RESUME = "Resume";
	public static String		ADD_ATTACHMENT = "Add Attachment";
	public static String		CLOSE = "Close";

	
	
	Long id;
	String action;
	String mailSendTo;
	Integer forNode;
	
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMailSendTo() {
		return mailSendTo;
	}
	public void setMailSendTo(String mailSendTo) {
		this.mailSendTo = mailSendTo;
	}
	
	public void setForNode(Integer forNode) {
		this.forNode = forNode;
	}
	public Integer getForNode() {
		return forNode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof EmailNotification )
		{
			return ( (EmailNotification)obj).getId().equals(getId());
		}
		return super.equals(obj);
	}
	
}
