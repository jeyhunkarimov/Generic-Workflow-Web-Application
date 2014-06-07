package com.karimovceyhun.workflow.services;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IMailSenderService;
import com.karimovceyhun.workflow.interfaces.IUserService;

public class UserService extends Service implements IUserService,Serializable 
{
	private static final long serialVersionUID = -4147845212760564343L;
	private MailSender sender;
	private String username;
	
	

	public UserService(SessionFactory sessionFactory) 
	{
		super(sessionFactory);
	}
	
	public User findUser(Long id) 
	{
		User user = find(User.class, id);
		// added to fetch all
		
		return user;
	}
	
	@Transactional
	public User checkForTheUser(String email, String password) 
	{
		List users = getHibernateTemplate().find("select e from User e where e.email = ? and e.password = ?", new Object[]{email, password});
		
		if(users != null && users.size() == 1)
		{
			User userTarget = (User) users.get(0);
			return userTarget;
		}
		else
			return null;
		
	}


	@Transactional
	public List<User> list(int firstRow, int rowCount, String sortField, boolean sortAscending) 
	{

		List<User> employees = super.list(User.class, firstRow, rowCount, sortField, sortAscending);

		return employees;

	}

	@Transactional
	public List<User> list(int firstRow, int rowCount, String sortField,boolean sortAscending, DetachedCriteria criteria) 
	{
		List<User> employees = super.list(User.class, firstRow, rowCount, sortField, sortAscending, criteria);
		return employees;
	}

	
	public void forgetPassword(String email)
	{
		List users = getHibernateTemplate().find("select e from User e where e.email = ?", new Object[]{email});
		User targetEmployee = (User) users.get(0);
		//MailSenderService.getInstance().send(targetEmployee.getEmail(), "Your password is " +  targetEmployee.getPassword() , "Mobicom Workorder Password", "noreply@mobicom.it");
		IMailSenderService mss = (IMailSenderService) ServiceFinder.findBean("MailSenderService");
		mss.send(targetEmployee.getEmail(), "Your password is " +  targetEmployee.getPassword() , "Mobicom Workorder Password", "noreply@mobicom.it");
		/*SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(targetEmployee.getEmail());
		message.setText("Your password is " +  targetEmployee.getPassword());
		message.setSubject("Mobicom Workorder Password");
		message.setFrom(getUsername());
		sender.send(message);
		*/
	}

	
	public MailSender getSender() {
		return sender;
	}

	public void setSender(MailSender sender) {
		this.sender = sender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
