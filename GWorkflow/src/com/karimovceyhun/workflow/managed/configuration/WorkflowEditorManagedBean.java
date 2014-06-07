package com.karimovceyhun.workflow.managed.configuration;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;

import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Decision;
import com.karimovceyhun.workflow.data.Field;
import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.data.Process;
import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.Terminate;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.image.WorkOrderImageGenerator;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;
import com.karimovceyhun.workflow.managed.ListingManagedBean;
import com.karimovceyhun.workflow.services.ServiceFinder;

@ManagedBean(name="workflowEditorManagedBean")
@ViewScoped
public class WorkflowEditorManagedBean implements Serializable
{
	public WorkflowEditorManagedBean()
	{
		SelectionRowAndColumns.initRowAndColumnNames();
		try
		{
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		if( (Boolean) request.getSession(true).getAttribute("isWorkflowEditMode") )
		{
			isEditMode = true;
			String id = (String) request.getSession(true).getAttribute("workflowId");
			IWorkflowService iwfs = (IWorkflowService)ServiceFinder.findBean("WorkflowService");
			setWorkflow(iwfs.findWorkflow(Long.valueOf(id)));
			
			editModeProjectList();
			editModeFieldList();
			editModeUsersAbleToMonitor();
			editModeUsersAbleToOpen();
			editModeNodes();
			editModeNodeConnections();
			request.getSession(true).setAttribute("isWorkflowEditMode", false);
		}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	private static final long serialVersionUID = -3326921673250593493L;
	
	private Boolean isEditMode = false;
	private Workflow workflow = new Workflow();
	private Field field = new Field();
	private List<Field> fields = new ArrayList<Field>();
	private String fieldType;
	private Process process = new Process();
	private List<Process> processes = new ArrayList<Process>();
	boolean oldValueProjectSelectable = false;
	
	private Decision decision = new Decision();
	private List<Decision> decisions = new ArrayList<Decision>();
	
	private Separator separator = new Separator();
	private List<Separator> separators = new ArrayList<Separator>();
	
	private Collector collector = new Collector();
	private List<Collector> collectors = new ArrayList<Collector>();
	
	private Terminate terminate = new Terminate();
	private List<Terminate> terminates = new ArrayList<Terminate>();

	private List<Node> nodes = null;
	private List<Node> succeedingNodes = null;
	private List<SelectionTableRow> processTable;
	private List<User> userList;
	
	private DualListModel<Project> projectPickList = null;
	private DualListModel<User> userAbleToMonitor = null;
	private DualListModel<User> userAbleToOpen = null;
	private DualListModel<User> assignableUsers = null;

	private List<Project> projectList;
	private byte[] imageInByte ;
	

	public byte[] getImageInByte() {
		return imageInByte;
	}

	public void setImageInByte(byte[] imageInByte) {
		this.imageInByte = imageInByte;
	}

	public List<Field> getFields() 
	{
		return fields;
	}
	
	public void setFields(List<Field> fields)
	{
		this.fields = fields;
	}
	
	
	public void editField() {
		field = (Field)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
		fieldType = field.getType();
	}

	public List<User> completeUser(String query) {  
        List<User> suggestions = new ArrayList<User>();
        
        /*for(User e : getUserList()) {  
            if(e.getFullName().toLowerCase().contains(query.toLowerCase()))  
                suggestions.add(e);
        }
        */
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
		detachedCriteria.add(Restrictions.like("fullName", "%"+ query +  "%"));
		suggestions = ((IUserService)ServiceFinder.findBean("UserService")).list(User.class,detachedCriteria);
        
        return suggestions;
    }

	public void editProcess() 
	{
		process = (Process)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
	//	setProcessConfiguration(process.getEmailNotifications(),processTable);
		
		processTable = EmailNotificationGenerator.setMailNotifications(process.getEmailNotifications(), 0);
//		List<User> source = getUserList();
//		List<User> target = new ArrayList<User>(process.getAssignableUser()) ;
//		source.removeAll(target);
//		assignableUsers = new DualListModel<User>(source, target);
		
		assignableUsers = null;
		if( process.getProjectDependant() != null )
			oldValueProjectSelectable = !process.getProjectDependant();
		
		getAssignableUsers().setTarget( new ArrayList<User>(process.getAssignableUser()));
		getAssignableUsers().getSource().removeAll(getAssignableUsers().getTarget());
		
		//assignableUsers.setTarget(process.getAssignableUser());
		//assignableUsers.setSource( new ArrayList<User>( getService().list(User.class) ) );
		//assignableUsers.getSource().removeAll(process.getAssignableUser());
	}
	
	public void editDecision() 
	{
		decision = (Decision)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
		processTable = EmailNotificationGenerator.setMailNotifications(decision.getEmailNotifications(), 1);

		assignableUsers = null;
		if( decision.getProjectDependant() != null )
			oldValueProjectSelectable = !decision.getProjectDependant();
		
		getAssignableUsers().setTarget( new ArrayList<User>(decision.getAssignableUser()));
		getAssignableUsers().getSource().removeAll(getAssignableUsers().getTarget());
	}	
	
	public void editSeparator() {
		separator = (Separator)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
		processTable = EmailNotificationGenerator.setMailNotifications(separator.getEmailNotifications(), 2);

		assignableUsers = null;
		if( separator.getProjectDependant() != null )
			oldValueProjectSelectable = !separator.getProjectDependant();
		
		getAssignableUsers().setTarget( new ArrayList<User>(separator.getAssignableUser()));
		getAssignableUsers().getSource().removeAll(getAssignableUsers().getTarget());
	}

	public void editCollector() {
		collector = (Collector)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
		processTable = EmailNotificationGenerator.setMailNotifications(collector.getEmailNotifications(), 3);
	}

	public void editTerminate() {
		terminate = (Terminate)(FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("item"));
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return field;
	}
	
	public Workflow getWorkflow() {
		return workflow;
	}
	
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
    public String onFlowProcess(FlowEvent event) {  

    	if( event.getOldStep().equalsIgnoreCase("workflow") && event.getNewStep().equalsIgnoreCase("fields") )
    	{
    		if( workflow.getName().equals("") )
    		{
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Please Enter Workflow Name"));
    			return "workflow";
    		}
    		if( workflow.getProjectDependant() && projectPickList.getTarget().isEmpty() )
    		{
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Please select Project"));
    			return "workflow";
    		}
    		else if( !workflow.getProjectDependant() && workflow.getManager() == null )
    		{
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Please select Manager"));
    			return "workflow";
    		}
    		
    		if( workflow.getProjectDependant() )
    		{
	    		workflow.setProjects(new HashSet<Project>( projectPickList.getTarget() ));
    		}
    		assignableUsers = null;
    	}

    	else if( event.getOldStep().equalsIgnoreCase("nodeConnections") && event.getNewStep().equalsIgnoreCase("finalize") )
    	{
    		if( workflow.getStartProcess() == null )
    		{
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Start Node cannot be empty"));
    			return "nodeConnections";
    		}
    		else
    		{
    			for (Process process : processes) {
					if( process.getSucceedingProcess() == null )
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,process.getName(),"Succeeding Node cannot be empty"));
						return "nodeConnections";
					}
				}
    			for (Collector collector : collectors) {
					if( collector.getSucceedingProcess() == null )
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,collector.getName(),"Succeeding Node cannot be empty"));
						return "nodeConnections";
					}
				}
    			for (Decision decision : decisions) {
					if( decision.getAcceptedsucceedingProcess() == null || decision.getRejectedsucceedingProcess() == null )
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,decision.getName(),"Succeeding Node cannot be empty"));
						return "nodeConnections";
					}
				}
    			for (Separator separator : separators) {
					if( separator.getSucceedingProcesses().isEmpty() )
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,separator.getName(),"Succeeding Nodes cannot be empty"));
						return "nodeConnections";
					}
				}
    		}
    		updateWorkflow();
    		
    		
    		
    		try
    		{
    			BufferedImage graphImage = WorkOrderImageGenerator.getBufferedImage(getWorkflow());
	    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    		ImageIO.write( graphImage, "png", baos );
	    		baos.flush();
	    		imageInByte = baos.toByteArray();
	    		baos.close();
	            FacesContext context = FacesContext.getCurrentInstance();
	            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		        request.getSession().setAttribute("imageInByte", imageInByte);

    		}
    		catch (Exception e) 
    		{
    			ListingManagedBean.addMessage(FacesMessage.SEVERITY_INFO, e.toString(),e.toString());
 
			}
    	}
    	return event.getNewStep();
    } 
    
	private void updateWorkflow() {
		
		workflow.setNodes(new ArrayList<Node>());
		
		workflow.getNodes().addAll(collectors);
		workflow.getNodes().addAll(separators);
		workflow.getNodes().addAll(processes);
		workflow.getNodes().addAll(decisions);
		workflow.getNodes().addAll(terminates);
		
		for (Collector collector : collectors) {
			collector.setPredecessorProcesses( new ArrayList<Node>() );
		}
		
		for (Decision decision : decisions) {
			if( decision.getAcceptedsucceedingProcess() instanceof Collector )
			{
				((Collector)decision.getAcceptedsucceedingProcess()).getPredecessorProcesses().add(decision);
			}
			else if( decision.getRejectedsucceedingProcess() instanceof Collector )
			{
				((Collector)decision.getRejectedsucceedingProcess()).getPredecessorProcesses().add(decision);
			}
		}
		
		for (Process process : processes) {
			if( process.getSucceedingProcess() instanceof Collector )
			{
				((Collector)process.getSucceedingProcess()).getPredecessorProcesses().add(process);
			}
		}
		
		workflow.setFields(new HashSet<Field>(fields));
		workflow.setUserAbleToMonitor(new HashSet<User>(userAbleToMonitor.getTarget()));
		workflow.setUserAbleToOpen(new HashSet<User>(userAbleToOpen.getTarget()));
	}

	public void addField()
	{
		if( !getFieldType().equals("Enum") )
			field.setType(getFieldType());
		
		field = new Field();
	}
	
	public String newNode() 
	{
		assignableUsers = null;
//		assignableUsers.setTarget(new ArrayList<User>());
//		assignableUsers.setSource(getUserList());
		process = new Process();
		decision = new Decision();
		separator = new Separator();
		collector = new Collector();
		terminate = new Terminate();
		processTable = EmailNotificationGenerator.initTable(1);
		return null;
	}
	
	public String newNodeProcess() 
	{
		assignableUsers = null;
//		assignableUsers.setTarget(new ArrayList<User>());
//		assignableUsers.setSource(getUserList());
		process = new Process();
		processTable = EmailNotificationGenerator.initTable(0);
		return null;
	}
	
	public String newNodeDecision() 
	{
		assignableUsers = null;
//		assignableUsers.setTarget(new ArrayList<User>());
//		assignableUsers.setSource(getUserList());
		decision = new Decision();
		processTable = EmailNotificationGenerator.initTable(1);
		return null;
	}
	
	public String newNodeSeperator() 
	{
		assignableUsers = null;
//		assignableUsers.setTarget(new ArrayList<User>());
//		assignableUsers.setSource(getUserList());
		separator = new Separator();
		processTable = EmailNotificationGenerator.initTable(2);
		return null;
	}
	
	public String newNodeCollector() 
	{
		assignableUsers = null;
//		assignableUsers.setTarget(new ArrayList<User>());
//		assignableUsers.setSource(getUserList());
		collector = new Collector();
		processTable = EmailNotificationGenerator.initTable(3);
		return null;
	}
	
	public void addProcess()
	{
		process.setType(Process.PROCESS_TYPE);
		process.setAssignableUser( new HashSet<User>( assignableUsers.getTarget())  );
		process.setEmailNotifications(EmailNotificationGenerator.getEmailNotifications(processTable));
		process = new Process();
		nodes = null;
		succeedingNodes = null;
	}
    
	public void addDecision()
	{
		decision.setType(Decision.DESICION_TYPE);
		decision.setAssignableUser( new HashSet<User>( assignableUsers.getTarget() ));
		decision.setEmailNotifications(EmailNotificationGenerator.getEmailNotifications(processTable));
		decision = new Decision();
		nodes = null;
		succeedingNodes = null;
	}

	public void addSeparator()
	{
		separator.setType(Separator.SEPARATOR_TYPE);
		separator.setAssignableUser( new HashSet<User>( assignableUsers.getTarget() ));
		
		separator.setEmailNotifications(EmailNotificationGenerator.getEmailNotifications(processTable));
		separator = new Separator();
		nodes = null;
		succeedingNodes = null;
	}

	public void addCollector()
	{
		collector.setEmailNotifications(EmailNotificationGenerator.getEmailNotifications(processTable));
		collector = new Collector();
		nodes = null;
		succeedingNodes = null;
	}

	public void addTerminate()
	{
		terminate = new Terminate();
		nodes = null;
		succeedingNodes = null;
	}
	
	public String addSucceedingNode()
	{
		
		return null;
	}
	
    private IService getService() {
    	IUserService userService = (IUserService) (ServiceFinder.findBean("UserService"));
		return userService;
	}

	public String save() {

		IWorkflowService workflowService = (IWorkflowService) (ServiceFinder.findBean("WorkflowService"));

		for(Field field: workflow.getFields()){
			workflowService.save(field);
		}
		workflowService.save(workflow);
		
		return "workflowListing";
    }
    
    public String cancel() {
    	return "workflowListing";
    }
    
    public void setUserAbleToMonitor(DualListModel<User> userAbleToMonitor) {
		this.userAbleToMonitor = userAbleToMonitor;
	}
    
    public DualListModel<User> getUserAbleToMonitor() {
		if( userAbleToMonitor == null )
		{
			
			userAbleToMonitor = new DualListModel<User>( getUserList(), new ArrayList<User>());
		}
    	return userAbleToMonitor;
	}
    
    public void setUserAbleToOpen(DualListModel<User> userAbleToOpen) {
		this.userAbleToOpen = userAbleToOpen;
	}
    
    public DualListModel<User> getUserAbleToOpen() {
    	if( userAbleToOpen == null )
		{
			userAbleToOpen = new DualListModel<User>(getUserList(),new ArrayList<User>());
		}
    	return userAbleToOpen;
	}
    
    public Process getProcess() {
		return process;
	}
    
    public void setProcess(Process process) {
		this.process = process;
	}
    
    public List<Process> getProcesses() {
		return processes;
	}
    
    public void setAssignableUsers(DualListModel<User> assignableUsers) {
		this.assignableUsers = assignableUsers;
	}
    
    public DualListModel<User> getAssignableUsers() {
    	
    	if( assignableUsers == null )
		{
			assignableUsers = new DualListModel<User>(getUserList(),new ArrayList<User>());
		}
    	if( process.getProjectDependant() != null && oldValueProjectSelectable != process.getProjectDependant().booleanValue() )
    	{
    		oldValueProjectSelectable = process.getProjectDependant().booleanValue();
    		
    		if( !process.getProjectDependant().booleanValue() )
    		{
    			assignableUsers = new DualListModel<User>(getUserList(),new ArrayList<User>());
    		}
    		else
    		{
    			ArrayList<Long> ids = new ArrayList<Long>();
    			for ( Project project : workflow.getProjects() ) {
    				ids.add( project.getId() );
				}
    			DetachedCriteria criteria = DetachedCriteria.forClass(Project.class);
    			
    			criteria.setFetchMode("users", FetchMode.JOIN);
    			criteria.add(Restrictions.in("id", ids));
    			
    			criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
    			
    			IWorkflowService workflowService = (IWorkflowService) (ServiceFinder.findBean("WorkflowService"));
    			
    			List<Project> projects = workflowService.list(Project.class,criteria);
    			
    			ArrayList<User> users = new ArrayList<User>();
    			
    			for (Project project : projects) {
					users.addAll(project.getUsers());
				}
    			assignableUsers = new DualListModel<User>(users,new ArrayList<User>());
    		}
    		
    	}
    	return assignableUsers;
	}
    
    public List<Decision> getDecisions() {
		return decisions;
	}
    
    public void setDecisions(List<Decision> decisions) {
		this.decisions = decisions;
	}
    
    public Decision getDecision() {
		return decision;
	}
    
    public void setDecision(Decision decision) {
		this.decision = decision;
	}
    
    public Separator getSeparator() {
		return separator;
	}
    
    public void setSeparator(Separator separator) {
		this.separator = separator;
	}
    
    public List<Separator> getSeparators() {
		return separators;
	}
    
    public void setSeparators(List<Separator> separators) {
		this.separators = separators;
	}
    
    public void setCollector(Collector collector) {
		this.collector = collector;
	}
    
    public Collector getCollector() {
		return collector;
	}
    
    public List<Collector> getCollectors() {
		return collectors;
	}
    
    public void setCollectors(List<Collector> collectors) {
		this.collectors = collectors;
	}
    
    public Terminate getTerminate() {
		return terminate;
	}
    
    public void setTerminate(Terminate terminate) {
		this.terminate = terminate;
	}
    
    public List<Terminate> getTerminates() {
		return terminates;
	}
    
    public void setTerminates(List<Terminate> terminates) {
		this.terminates = terminates;
	}
    
    public List<User> getUserList() {

		userList = getService().list(User.class);

    	return userList;
	}
    
    public List<Project> getProjectList() {
    	if( projectList == null )
		{
    		projectList = getService().list(Project.class);
		}
    	return projectList;
	}
    
    public List<Node> getNodes() {
    	
    	if( nodes == null )
    	{
    		nodes = new ArrayList<Node>();
    		
    		nodes.addAll(decisions);
    		nodes.addAll(processes);
    		nodes.addAll(separators);
    		
    	}
		return nodes;
	}
    
    public List<Node> getSucceedingNodes() {
    	if( succeedingNodes == null )
    	{
    		succeedingNodes = new ArrayList<Node>();
    		
    		succeedingNodes.addAll(decisions);
    		succeedingNodes.addAll(processes);
    		succeedingNodes.addAll(separators);
    		succeedingNodes.addAll(collectors);
    		succeedingNodes.addAll(terminates);
    	}
    	return succeedingNodes;
	}

    public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
    
    public String getFieldType() {
		return fieldType;
	}
    
    public DualListModel<Project> getProjectPickList() {
    	if( projectPickList == null )
		{
    		projectPickList = new DualListModel<Project>(getProjectList(),new ArrayList<Project>());
		}
    	return projectPickList;
	}
    
    public void setProjectPickList(DualListModel<Project> projectPickList) 
    {
		this.projectPickList = projectPickList;
	}


    public List<SelectionTableRow> getProcessTable() 
    {	

		return processTable;
	}

	public void setProcessTable(List<SelectionTableRow> processTable) {
		this.processTable = processTable;
	}


	
	/*public List<EmailNotification> getProcessConfiguration()
	{
		List<EmailNotification> enl = new ArrayList<EmailNotification>();
		for(SelectionTableData std:processTable)
		{
			enl.addAll(std.processChanges());
		}
		return enl;
	}
	
	public void setProcessConfiguration(List<EmailNotification> enl,List<SelectionTableData> tableData)
	{
		tableData = initProcessTable();
		for(EmailNotification en : enl)
		{
			addEmailNotificationToTable(en,tableData);
		}
	}
	
	public void addEmailNotificationToTable(EmailNotification en,List<SelectionTableData> tableData)
	{
		String action = en.getAction();
		String sendTo = en.getMailSendTo();
		int row = processRows.indexOf(action);
		int col = processColumns.indexOf(sendTo);
		switch (col) 
		{
		case 0:
			tableData.get(row).setColumn1(true);
			break;
		case 1:
			tableData.get(row).setColumn2(true);
			break;
		case 2:
			tableData.get(row).setColumn3(true);
			break;
		case 3:
			tableData.get(row).setColumn4(true);
			break;
		case 4:
			tableData.get(row).setColumn5(true);
			break;

		default:
			break;
		}
	}*/
	
	
	public void editModeProjectList()
	{
		List<Project> sourceProject = getService().list(Project.class);
		Set<Project> targetProject = workflow.getProjects();
		sourceProject.removeAll(new ArrayList<Project>(targetProject));
		projectPickList = new DualListModel<Project>(sourceProject,new ArrayList<Project>(targetProject) );
	}
	
	public void editModeFieldList()
	{
		setFields( new ArrayList<Field>( workflow.getFields()));
	}
	
    public void editModeUsersAbleToMonitor()
    {
    	List<User> source = getService().list(User.class);
    	List<User> target = new ArrayList<>();
    			target.addAll(getWorkflow().getUserAbleToMonitor());
    	source.removeAll(target);
		userAbleToMonitor = new DualListModel<User>( source, target);
    }
    
    public void editModeUsersAbleToOpen()
    {
    	List<User> source = getService().list(User.class);
    	List<User> target = new ArrayList<>();
    		target.addAll(	getWorkflow().getUserAbleToOpen());
    	source.removeAll(target);
    	userAbleToOpen = new DualListModel<User>( source, target);
    }
    
    public void editModeNodes()
    {
    	processes = new ArrayList<Process>();
    	decisions = new ArrayList<Decision>();
    	separators = new ArrayList<Separator>();
    	collectors = new ArrayList<Collector>();
    	terminates = new ArrayList<Terminate>();
    	for(Node node:workflow.getNodes())
    	{
    		if(node instanceof Process)
    		{
    			processes.add((Process) node);
    		}
    		else if(node instanceof Decision)
    		{
    			decisions.add((Decision) node);
    		}
    		else if(node instanceof Separator)
    		{
    			separators.add((Separator) node);
    		}
    		else if(node instanceof Collector)
    		{
    			collectors.add((Collector) node);
    		}
    		else if(node instanceof Terminate)
    		{
    			terminates.add((Terminate) node);
    		}
    	}
    }
    
    public void editModeNodeConnections()
    {
    	
    }

}
