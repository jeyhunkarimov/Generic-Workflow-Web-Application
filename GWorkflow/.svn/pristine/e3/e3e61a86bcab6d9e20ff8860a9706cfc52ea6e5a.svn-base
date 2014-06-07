package com.karimovceyhun.workflow.managed;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.services.UserService;
import com.karimovceyhun.workflow.utilities.JSFUtil;



@ManagedBean(name = "userListingManagedBean")
@ViewScoped
public class UserListingManagedBean extends ListingManagedBean<User> implements Serializable {

	private static final long serialVersionUID = 4426815554605967048L;

	//public EmployeeView selectedEmployee = null;

	// private LazyDataModel<Employee> employeesModel = null;
	//private List<EmployeeView> employeesModel = null;

	private List<User> users = null;
	
	public UserListingManagedBean() 
	{
		super("UserBean");
	}


	@Override
	protected void doSave() {

		if (getBean().getId() != null && getBean().getId().longValue() == 0) {
			getBean().setId(null);
		}
		getUserService().save(getBean());
	}
	


	@Override
	protected void initializeBean() 
	{
		String id = JSFUtil.getRequestParameter("id");

		setBean(getUserService().findUser(new Long(id)));
	}


	@Override
	public SelectItem[] getListOfColumns() 
	{
		SelectItem[] items = new SelectItem[2];
		items[0] = new SelectItem("String|fullName", "Full Name");
		items[1] = new SelectItem("Enum:project:String|project", "Project");
		return items;
	}

	/**
	 * loads data
	 */
	@Override
	protected void loadData() {
		// employeesModel = new LazyDataModel<Employee>() {
		//
		// private static final long serialVersionUID = 1842875701172576990L;
		//
		// @Override
		// public List<Employee> load(int first, int pageSize,
		// String sortField, boolean sortOrder,
		// Map<String, String> filters) {
		//
		// return getEmployeeService().list(first, pageSize, sortField,
		// sortOrder,
		// getDetachedCriteria());
		// }
		//
		// };
		// employeesModel.setRowCount(getEmployeeService().countList(
		// Employee.class, getDetachedCriteria()));
		users = getUserService().list(User.class,
				getDetachedCriteria());
	}

	public DetachedCriteria getDefaultCriteria() {
		return DetachedCriteria.forClass(User.class);
	}
	
	@Override
	public DetachedCriteria getDetachedCriteria() {
		DetachedCriteria detachedCriteria = getDefaultCriteria();

		if (getSelectedColumn() == null || getSelectedColumn().trim().equals("")
				|| getInput() == null || getInput().trim().equals(""))
		
			return detachedCriteria;
		
		else 
		{
			String[] parsedColumn = getSelectedColumn().split("\\|");

			String type = parsedColumn[0];
			String query = parsedColumn[1];

			Object inputObject = null;

			if (type.equals("String") || type.equals("AutoComplete")) {

				inputObject = new String(getInput());

				if (query.indexOf(":") > 0) {

					String parts[] = query.split("\\:");

					DetachedCriteria next = detachedCriteria
							.createCriteria(parts[0]);

					for (int i = 1; i < parts.length - 1; i++)
						next = next.createCriteria(parts[i]);

					next.add(Restrictions.ilike(parts[parts.length - 1],
							(String) inputObject, MatchMode.ANYWHERE));

				}
				else
				{
					detachedCriteria.add(Restrictions.ilike(query,
							(String) inputObject, MatchMode.ANYWHERE));
				}

			} 
			else if (type.startsWith("Enum")) {
				
				String typeAnalysis[] = type.split("\\:");
				String orjType = typeAnalysis[typeAnalysis.length-1];
				
				if(orjType.equals("Long"))
					inputObject = new Long(getInput());
				else if(orjType.equals("String"))
					inputObject = getInput();
				else
					inputObject = new Integer(getInput());

				if (query.indexOf(":") > 0) {
					String parts[] = query.split("\\:");

					DetachedCriteria next = detachedCriteria
							.createCriteria(parts[0]);

					for (int i = 1; i < parts.length - 1; i++)
						next = next.createCriteria(parts[i]);

					next.add(Restrictions.eq(parts[parts.length - 1],
							inputObject));

				} else {
					detachedCriteria.add(Restrictions.eq(query, inputObject));
				}
			}
			
			return detachedCriteria;
		}
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public List<User> getUsers() 
	{

		if (users == null) {
			loadUsers();
		}
		return users;
	}

	private void loadUsers() 
	{

		setUsers(getUserService().list(User.class));
	}



	public IUserService getUserService() 
	{
		return (IUserService)ServiceFinder.findBean("UserService");
	}


	@Override
	protected void doBeforeSave(List<FacesMessage> messages) 
	{
		// TODO Auto-generated method stub
	}


}
