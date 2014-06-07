package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;

public interface IMailSenderService extends IService,Serializable
{
	public void send(String mailReceiver, String content,String subjects,String mailSender);
}
