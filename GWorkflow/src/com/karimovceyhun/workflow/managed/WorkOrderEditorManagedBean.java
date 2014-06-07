package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.component.timeline.TimelineUpdater;
import org.primefaces.extensions.event.timeline.TimelineAddEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;

import com.karimovceyhun.workflow.data.Decision;
import com.karimovceyhun.workflow.data.Field;
import com.karimovceyhun.workflow.data.FieldValue;
import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.data.Process;
import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.TaskNode;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IAttachmentService;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.interfaces.IStatusService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;

@ManagedBean(name = "workorderEditorManagedBean")
@ViewScoped
public class WorkOrderEditorManagedBean implements Serializable {
    private static final long serialVersionUID = 4426815554605967048L;

    private List<Workflow> workflows = null;
    private List<Field> fields = null;
    private WorkOrder workorder = new WorkOrder();
    private Workflow selectedWorkflow;
    private Project selectedProject;
    private User selectedUser;
    private List<User> users = null;
    private User currentUser;
    private HashMap<Long, FieldValue> fieldValue = new HashMap<Long, FieldValue>();
    private HashMap<Long, Boolean> isFinishedFileUpload = new HashMap<Long, Boolean>();
    private Date dueDate;
    private TimelineModel model;
    private TimelineEvent event; // current event to be changed, edited, deleted
				 // or added
    private long zoomMax;

    private Date startDate;
    private Date endDate;
    private TimeZone timeZone = TimeZone.getTimeZone("Europe/Madrid");
    private Process selectedProcess = new Process();
    private Decision selectedDecision = new Decision();
    private Separator selectedSeparator = new Separator();
    private TaskNode selectedTaskNode;


    public WorkOrderEditorManagedBean() // find current user
    {
	FacesContext context = FacesContext.getCurrentInstance();
	HttpServletRequest request = (HttpServletRequest) context
		.getExternalContext().getRequest();
	Long id = (Long) request.getSession().getAttribute("id");
	if (id != null) {
	    currentUser = ((IUserService) ServiceFinder.findBean("UserService"))
		    .findUser(id);
	   // initTimelineModel();
	}

    }

    // when double clicked on timeline element
    public void onSelectTimelineEvent(TimelineSelectEvent e){
        event = e.getTimelineEvent();  
        selectedTaskNode = (TaskNode) event.getData();
        switch (selectedTaskNode.getTaskNodeType()) {
	case 1:
	    selectedProcess = (Process) selectedTaskNode;
	    break;
	case 2:
	    selectedDecision = (Decision) selectedTaskNode;
	    break;
	case 3:
	    selectedSeparator = (Separator) selectedTaskNode;
	    break;

	default:
	    break;
	}
    }
    
    public void handleWorkorderChange(AjaxBehaviorEvent event){
	initTimelineModel();
    }
    
    
    private void initTimelineModel() {
	zoomMax = 1000L * 60 * 60 * 24 * 30;
	startDate = new Date();
	if (selectedWorkflow.getUsingDueDate()) {
	    endDate = dueDate;
	} else {
	    endDate = (new DateTime()).plusHours(
		    selectedWorkflow.getMaxProcessingTime()).toDate();
	}

	DateTime dynamicStartDate = new DateTime();
	model = new TimelineModel();
	selectedWorkflow.getNodes().toArray();
	for (Node node : selectedWorkflow.getNodes()) {
	    if(node instanceof TaskNode){
        	TaskNode taskNode = (TaskNode) node;
        	DateTime dynamicEndDate = dynamicStartDate
        	    .plusMinutes((int) (taskNode.getEstimatedTime() * 60));
        	model.add(new TimelineEvent(node, dynamicStartDate.toDate(),
        					dynamicEndDate.toDate()));
        					dynamicStartDate = dynamicEndDate;
	    }
	}
    }

    public void onChange(TimelineModificationEvent e) {
	// get clone of the TimelineEvent to be changed with new start / end
	// dates
	event = e.getTimelineEvent();

	// update booking in DB...

	// if everything was ok, no UI update is required. Only the model should
	// be updated
	model.update(event);

	// FacesMessage msg =
	// new FacesMessage(FacesMessage.SEVERITY_INFO, "The booking dates " +
	// getRoom() + " have been updated", null);
	// FacesContext.getCurrentInstance().addMessage(null, msg);

	// otherwise (if DB operation failed) a rollback can be done with the
	// same response as follows:
	// TimelineEvent oldEvent = model.getEvent(model.getIndex(event));
	// TimelineUpdater timelineUpdater =
	// TimelineUpdater.getCurrentInstance(":mainForm:timeline");
	// model.update(oldEvent, timelineUpdater);
    }

    public void onEdit(TimelineModificationEvent e) {
	// get clone of the TimelineEvent to be edited
	event = e.getTimelineEvent();
    }

    public void onAdd(TimelineAddEvent e) {
	// get TimelineEvent to be added
	// event = new TimelineEvent(new Booking(), e.getStartDate(),
	// e.getEndDate(), true, e.getGroup());

	// add the new event to the model in case if user will close or cancel
	// the "Add dialog"
	// without to update details of the new event. Note: the event is
	// already added in UI.
	// model.add(event);
    }

    public void onDelete(TimelineModificationEvent e) {
	// get clone of the TimelineEvent to be deleted
	event = e.getTimelineEvent();
    }

    public void delete() {
	// delete booking in DB...

	// if everything was ok, delete the TimelineEvent in the model and
	// update UI with the same response.
	// otherwise no server-side delete is necessary (see
	// timelineWdgt.cancelDelete() in the p:ajax onstart).
	// we assume, delete in DB was successful
	TimelineUpdater timelineUpdater = TimelineUpdater
		.getCurrentInstance(":mainForm:timeline");
	model.delete(event, timelineUpdater);

	// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
	// "The booking " + getRoom() + " has been deleted", null);
	// FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void saveDetails() {
	// save the updated booking in DB...

	// if everything was ok, update the TimelineEvent in the model and
	// update UI with the same response.
	// otherwise no server-side update is necessary because UI is already
	// up-to-date.
	// we assume, save in DB was successful
	TimelineUpdater timelineUpdater = TimelineUpdater
		.getCurrentInstance(":mainForm:timeline");
	model.update(event, timelineUpdater);

	// FacesMessage msg =
	// new FacesMessage(FacesMessage.SEVERITY_INFO, "The booking details " +
	// getRoom() + " have been saved", null);
	// FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void loadWorkFlow() {
	workflows = new ArrayList<>();
	workflows.addAll(new LinkedHashSet<>(((IWorkflowService) ServiceFinder
		.findBean("WorkflowService"))
		.findWorkflowsDueToUserAbleToOpen(currentUser.getId())));
	// workflows = ( ( IWorkflowService ) ServiceFinder.findBean (
	// "WorkflowService" ) ).findWorkflowsDueToUserAbleToOpen (
	// currentUser.getId () );
    }

    public IService getCommonService() {
	return (IService) ServiceFinder.findBean("Service");
    }

    public Workflow getSelectedWorkflow() {
	return selectedWorkflow;
    }

    public void setSelectedWorkflow(Workflow selectedWorkflow) {
	if (selectedWorkflow != null) {
	    fields = new ArrayList<Field>(selectedWorkflow.getFields());
	    if (fields.size() > 0
		    && !fieldValue.containsKey(fields.get(0).getId())) {
		fieldValue = new HashMap<Long, FieldValue>();
	    }
	    for (Field field : fields) {
		if (!fieldValue.containsKey(field.getId())) {
		    FieldValue value = new FieldValue();
		    value.setField(field);
		    fieldValue.put(field.getId(), value);
		}
		if (getFieldType(field) == 2) {
		    isFinishedFileUpload.put(field.getId(), false);
		}
	    }
	}
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.HOUR, selectedWorkflow.getMaxProcessingTime());
	setDueDate(calendar.getTime());
	this.selectedWorkflow = selectedWorkflow;
	//initTimelineModel();
    }

    public void setWorkflows(List<Workflow> workflows) {
	this.workflows = workflows;
    }

    public List<Workflow> getWorkflows() {
	if (workflows == null) {
	    loadWorkFlow();
	}
	return workflows;
    }

    public int getFieldType(Field field) {
	if (field.getType().equals("Attachment")) {
	    return 2;
	} else if (field.getType().equals("String")) {
	    return 1;
	} else if (field.getType().equals("Integer")) {
	    return 1;
	} else if (field.getType().equals("Date")) {
	    return 4;
	} else {
	    return 3;
	}
    }

    public String save() {
	if (selectedWorkflow == null) {
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
			    "Work Flow must be selected"));
	    return null;
	} else if (selectedWorkflow.getProjectDependant()
		&& selectedProject == null) {
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
			    "Project must be selected"));
	    return null;
	}
	if (workorder.getSummary().equals(null)
		|| workorder.getSummary().trim().equals("")) {
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
			    "Work Order Summary Must be selected"));
	    return null;
	}

	workorder.setStartDate(Calendar.getInstance().getTime());
	workorder.setWorkflow(selectedWorkflow);
	if (selectedProject != null) {
	    workorder.setProject(selectedProject);
	}
	workorder.setDueDate(dueDate);
	FacesContext context = FacesContext.getCurrentInstance();
	HttpServletRequest request = (HttpServletRequest) context
		.getExternalContext().getRequest();
	IUserService ius = (IUserService) ServiceFinder.findBean("UserService");
	workorder.setRequesterUser(ius.findUser((Long) request.getSession()
		.getAttribute("id")));

	workorder.setTasks(new ArrayList<Task>());
	Task task = new Task();
	task.setPreviousTask(null);
	task.setStartTime(Calendar.getInstance().getTime());
	task.setNode(selectedWorkflow.getStartProcess());
	// getCommonService ().save(workorder);

	// task.setWorkorder ( workorder );

	workorder.getTasks().add(task);
	if (selectedWorkflow.getStartProcess()
		.getSucceedingResponsableSelectable()) {
	    task.setResponsible(getSelectedUser());
	    Status status = ((IStatusService) ServiceFinder
		    .findBean("StatusService")).findStatus(selectedWorkflow
		    .getStartProcess().getType(), Status.ASSIGNED);
	    task.setStatus(status);
	} else {
	    if (getUsers().size() == 1) {
		task.setResponsible(getUsers().get(0));
		Status status = ((IStatusService) ServiceFinder
			.findBean("StatusService")).findStatus(selectedWorkflow
			.getStartProcess().getType(), Status.ASSIGNED);
		task.setStatus(status);
	    } else {
		Status status = ((IStatusService) ServiceFinder
			.findBean("StatusService")).findStatus(selectedWorkflow
			.getStartProcess().getType(), Status.NEW);
		task.setStatus(status);
	    }

	}
	// workorder.getTasks ().add ( task );
	if (workorder.getFieldValues() == null
		|| workorder.getFieldValues().size() == 0) {
	    workorder.setFieldValues(new HashSet<FieldValue>());
	}
	if (fields != null && fields.size() > 0) {
	    for (Field field : fields) {
		if (getFieldType(field) != 2) {
		    FieldValue fv = fieldValue.get(field.getId());
		    workorder.getFieldValues().add(fv);
		} else {
		    boolean req = field.getRequired();
		    for (FieldValue fieldValue : workorder.getFieldValues()) {

			if (field.getId().equals(fieldValue.getField().getId())
				&& (!field.getRequired() || (fieldValue
					.getValue() != null && !fieldValue
					.getValue().equals("")))) {
			    req = false;
			    break;
			}

		    }
		    if (req) {
			FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Work Order Field is required", field
						.getFieldname()));
			return null;
		    }
		}
	    }
	}
	// for (Field field : fields)
	// {
	// if(! field.getType().equals("Attachment"))
	// {
	// FieldValue value = new FieldValue();
	// value.setField(field);
	// value.setValue(field.getValue());
	// workorder.getFieldValues().add(value);
	// }
	// }

	// workorder.setFieldValues ( new HashSet < FieldValue > (
	// fieldValue.values () ) );
	for (FieldValue fv : workorder.getFieldValues()) {
	    getCommonService().save(fv);
	}
	getCommonService().save(workorder);
	task.setWorkorder(workorder);
	setTaskPriority(task);
	getCommonService().save(task);

	// TODO send mail to responsbles;
	return "success";
    }

    public void setTaskPriority(Task tsk) {
	if (tsk.getNode().getEstimatedTime().equals(new Double("0"))) {
	    tsk.setPriority(new Long("1"));
	} else {
	    tsk.setPriority(tsk.getWorkorder().getDueDate().getTime());
	}
    }

    public List<User> getUsers() {
	if (users == null) {
	    if (selectedProject != null
		    && selectedWorkflow != null
		    && selectedWorkflow.getStartProcess().getProjectDependant() != null
		    && selectedWorkflow.getStartProcess().getProjectDependant()) {
		users = new ArrayList<User>(selectedWorkflow.getStartProcess()
			.getAssignableUser());
		users.retainAll(new ArrayList<User>(selectedProject.getUsers()));
	    } else if (selectedWorkflow != null) {
		users = new ArrayList<User>(selectedWorkflow.getStartProcess()
			.getAssignableUser());
	    }
	}
	return users;
    }

    public void setUsers(List<User> users) {
	this.users = users;
    }

    public String[] getFieldEnums(Field field) {
	return field.getType().split(",");
    }

    public void setSelectedProject(Project selectedProject) {
	if (selectedProject != null
		&& selectedWorkflow.getStartProcess()
			.getSucceedingResponsableSelectable()) {
	    if (selectedWorkflow.getStartProcess().getProjectDependant() != null
		    && selectedWorkflow.getStartProcess().getProjectDependant()) {
		users = new ArrayList<User>(selectedWorkflow.getStartProcess()
			.getAssignableUser());
		users.retainAll(new ArrayList<User>(selectedProject.getUsers()));
	    } else {
		users = new ArrayList<User>(selectedWorkflow.getStartProcess()
			.getAssignableUser());
	    }
	}
	this.selectedProject = selectedProject;
    }

    public Project getSelectedProject() {
	return selectedProject;
    }

    public void setFields(List<Field> fields) {
	this.fields = fields;
    }

    public List<Field> getFields() {
	return fields;
    }

    public void handleFileUpload(FileUploadEvent event) {
	Long itemId = (Long) event.getComponent().getAttributes().get("itemId");
	IAttachmentService ias = (IAttachmentService) ServiceFinder
		.findBean("AttachmentService");
	String path = ias.addFieldAttachment(selectedWorkflow.getName(),
		event.getFile());
	ArrayList<Field> tempFields = new ArrayList<Field>(
		selectedWorkflow.getFields());
	FieldValue fv = new FieldValue();
	for (Field f : tempFields) {
	    if (f.getId().equals(itemId)) {
		fv.setField(f);
		fv.setValue(path);
		if (workorder.getFieldValues() == null
			|| workorder.getFieldValues().size() == 0) {
		    workorder.setFieldValues(new HashSet<FieldValue>());
		}
		workorder.getFieldValues().add(fv);
		break;
	    }
	}
	isFinishedFileUpload.put(itemId, true);
	// Field field = selectedWorkflow.getFields().

    }

    public String removeFile() {
	IAttachmentService ias = (IAttachmentService) ServiceFinder
		.findBean("AttachmentService");
	Long fieldId = new Long(JSFUtil.getRequestParameter("removedFieldId"));
	for (FieldValue fv : workorder.getFieldValues()) {
	    if (fv.getField().getId().equals(fieldId)) {
		ias.deleteAttachment(fv.getValue());
		fv.setValue("");
	    }
	}
	isFinishedFileUpload.remove(fieldId);
	return null;
    }

    public String downloadFile() {
	Long fieldId = new Long(
		JSFUtil.getRequestParameter("downloadedFieldId"));
	for (FieldValue fv : workorder.getFieldValues()) {
	    if (fv.getField().getId().equals(fieldId)) {
		String path = fv.getValue();
		String url = "./../FileDownload?path=" + path;
		FacesContext context = FacesContext.getCurrentInstance();
		try {
		    context.getExternalContext().dispatch(url);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    context.responseComplete();
		}
	    }
	}

	return null;
    }

    public User getSelectedUser() {
	return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
	this.selectedUser = selectedUser;
    }

    public WorkOrder getWorkorder() {
	return workorder;
    }

    public void setWorkorder(WorkOrder workorder) {
	this.workorder = workorder;
    }

    public HashMap<Long, FieldValue> getFieldValue() {
	return fieldValue;
    }

    public void setFieldValue(HashMap<Long, FieldValue> fieldValue) {
	this.fieldValue = fieldValue;
    }

    public User getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }

    public HashMap<Long, Boolean> getIsFinishedFileUpload() {
	return isFinishedFileUpload;
    }

    public void setIsFinishedFileUpload(
	    HashMap<Long, Boolean> isFinishedFileUpload) {
	this.isFinishedFileUpload = isFinishedFileUpload;
    }

    public Date getDueDate() {
	return dueDate;
    }

    public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
    }

    public TimelineModel getModel() {
	return model;
    }

    public void setModel(TimelineModel model) {
	this.model = model;
    }

    public TimelineEvent getEvent() {
	return event;
    }

    public void setEvent(TimelineEvent event) {
	this.event = event;
    }

    public long getZoomMax() {
	return zoomMax;
    }

    public void setZoomMax(long zoomMax) {
	this.zoomMax = zoomMax;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public TimeZone getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
	this.timeZone = timeZone;
    }
    
    public Process getSelectedProcess() {
        return selectedProcess;
    }

    public void setSelectedProcess(Process selectedProcess) {
        this.selectedProcess = selectedProcess;
    }

    public Decision getSelectedDecision() {
        return selectedDecision;
    }

    public void setSelectedDecision(Decision selectedDecision) {
        this.selectedDecision = selectedDecision;
    }

    public Separator getSelectedSeparator() {
        return selectedSeparator;
    }

    public void setSelectedSeparator(Separator selectedSeparator) {
        this.selectedSeparator = selectedSeparator;
    }

    public TaskNode getSelectedTaskNode() {
        return selectedTaskNode;
    }

    public void setSelectedTaskNode(TaskNode selectedTaskNode) {
        this.selectedTaskNode = selectedTaskNode;
    }


}
