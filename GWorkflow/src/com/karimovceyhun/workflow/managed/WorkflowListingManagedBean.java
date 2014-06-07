package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;

@ManagedBean(name="workflowListingManagedBean")
@ViewScoped
public class WorkflowListingManagedBean extends ListingManagedBean<Workflow> implements Serializable
{
/**
	 * 
	 */
	private static final long serialVersionUID = -183758072705081559L;
	private List<Workflow> workflows = null;
	
	public WorkflowListingManagedBean() 
	{
		super("WorkflowBean");
	}


	@Override
	protected void doSave() {

		if (getBean().getId() != null && getBean().getId().longValue() == 0) {
			getBean().setId(null);
		}
		getWorkflowService().save(getBean());
	}
	


	@Override
	protected void initializeBean() 
	{
		String id = JSFUtil.getRequestParameter("id");
		setBean(getWorkflowService().findWorkflow(new Long(id)));
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
		workflows = getWorkflowService().list(Workflow.class,
				getDetachedCriteria());
	}

	public DetachedCriteria getDefaultCriteria() {
		return DetachedCriteria.forClass(Workflow.class);
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

	public void setWorkflows(List<Workflow> workflows)
	{
		this.workflows = workflows;
	}

	public List<Workflow> getWorkflows() 
	{

		if (workflows == null) {
			loadWorkflows();
		}
		return workflows;
	}

	private void loadWorkflows() 
	{

		setWorkflows(getWorkflowService().list(Workflow.class));
	}



	public IWorkflowService getWorkflowService() 
	{
		return (IWorkflowService)ServiceFinder.findBean("WorkflowService");
	}


	@Override
	protected void doBeforeSave(List<FacesMessage> messages) 
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public String edit()
	{
		String id = JSFUtil.getRequestParameter("id");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		request.getSession(true).setAttribute("isWorkflowEditMode", new Boolean(true));
		request.getSession(true).setAttribute("workflowId", id);
		return "editWorkflow";
	}
	
	
	public String addNew()
	{
		return "addNewWorkflow";
	}
	
}
