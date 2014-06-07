package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.SwingWorker;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.event.FileUploadEvent;

import com.karimovceyhun.workflow.data.Attachment;
import com.karimovceyhun.workflow.data.AttachmentHolder;
import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Decision;
import com.karimovceyhun.workflow.data.EmailNotification;
import com.karimovceyhun.workflow.data.Field;
import com.karimovceyhun.workflow.data.FieldValue;
import com.karimovceyhun.workflow.data.History;
import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.data.Note;
import com.karimovceyhun.workflow.data.Process;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.Status;
import com.karimovceyhun.workflow.data.StatusChange;
import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.TaskNode;
import com.karimovceyhun.workflow.data.Terminate;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.interfaces.IAttachmentService;
import com.karimovceyhun.workflow.interfaces.IEmailNotificationService;
import com.karimovceyhun.workflow.interfaces.IMailSenderService;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.interfaces.IStatusService;
import com.karimovceyhun.workflow.interfaces.ITaskService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.interfaces.IWorkOrderService;
import com.karimovceyhun.workflow.interfaces.IWorkflowService;
import com.karimovceyhun.workflow.services.EmailNotificationService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

@ManagedBean ( name = "taskViewManagedBean" )
@ViewScoped
public class TaskViewManagedBean implements Serializable
{

	private static final long	serialVersionUID		= 6261708997485634549L;
	private List < Status >		changedTaskStatuses;
	private Status				selectedStatus;
	private Boolean				isManager;
	private List < User >		assignableUsers;
	private User				assignedUser;
	private List < FieldValue >	workorderFieldValues;
	private Task				currentTask;
	private User				requestor;
	private Boolean				showDialog				= false;
	private List < User >		nextTaskUsers;
	private User				nextTaskResponsible;
	private User				currentUser;
	private String				noteText;
	private Boolean				currentUserAssignable	= null;
	private Note				currentNote;
	private Note				noteToBeDeleted;
	public static Integer		STATUS_CHANGE_MAIL		= 1;
	public static Integer		ADD_NOTE_MAIL			= 2;
	public static Integer		ADD_ATTACHMENT_MAIL		= 3;

	public static String mailContent = "Work order summary: <workorder_sum>\n" +
										"Task name: <task_name>\n" +
										"Task id: <task_id>\n" +
										"Action: <action>\n" +
										"Action done by: <action_by>\n" +
										"Current Responsible: <resp_name>";
	
	public TaskViewManagedBean()
	{
		try
		// in case of logout is pressed.
		{
			FacesContext context = FacesContext.getCurrentInstance ();
			HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
			currentUser = getUserService().findUser(  ( Long ) request.getSession ().getAttribute ( "id" ));
			Long taskId = ( Long ) request.getSession ().getAttribute ( "taskId" );
			setCurrentTask ( getTaskService ().getTask ( taskId ) );
			requestor = currentTask.getWorkorder ().getRequesterUser ();
			assignedUser = currentTask.getResponsible ();
			changedTaskStatuses = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).getChangedStatuses ( currentTask.getStatus ().getId () );
			
			if ( changedTaskStatuses != null && changedTaskStatuses.size () != 0 )
			{
				selectedStatus = changedTaskStatuses.get ( 0 );
				if( currentTask.getNode() instanceof Decision )
				{
					for (Status status : changedTaskStatuses) {
						if( status.getStatusType() == Status.ACCEPTED )
						{
							status.setLabel( ((Decision) currentTask.getNode()).getAcceptLabel() );	
						}
						else if( status.getStatusType() == Status.REJECTED )
						{
							status.setLabel( ((Decision) currentTask.getNode()).getRejectLabel() );	
						}
					}
				}
			}
			Long id = ( Long ) request.getSession ().getAttribute ( "id" );
			if( currentTask.getWorkorder ().getWorkflow().getProjectDependant() ) 
				isManager = ( id.equals ( currentTask.getWorkorder ().getProject ().getManager ().getId () ) );
			else
				isManager = ( id.equals(  currentTask.getWorkorder ().getWorkflow().getManager().getId() ));
			currentUser = getUserService ().findUser ( id );
		}
		catch ( Exception e )
		{
			System.out.println(e);
			// TODO: handle exception
		}
	}

	public String addNote()
	{
		History hs = createHistory ( "Note" , "" , noteText );

		if ( currentTask.getWorkorder ().getHistory () == null )
		{
			currentTask.getWorkorder ().setHistory ( new ArrayList < History > () );
		}
		getTaskService ().save ( hs );
		currentTask.getWorkorder ().getHistory ().add ( hs );
		getTaskService ().save ( currentTask.getWorkorder () );

		Note note = new Note ();
		note.setDate ( Calendar.getInstance ().getTime () );
		note.setUser ( currentUser );
		getTaskService ().save ( note );
		if ( currentTask.getWorkorder ().getNotes () == null )
		{
			currentTask.getWorkorder ().setNotes ( new ArrayList < Note > () );
		}
		note.setNote ( noteText );
		getTaskService ().save ( note );
		currentTask.getWorkorder ().getNotes ().add ( note );
		getTaskService ().save ( currentTask.getWorkorder () );
		
		String content = mailContent.replace("<workorder_sum>", currentTask.getWorkorder().getSummary());
		content = content.replace("<task_name>", currentTask.getNode().getName());
		content = content.replace("<task_id>", "" + currentTask.getId() );
		content = content.replace("<action>", EmailNotification.ADD_NOTE );
		content = content.replace("<action_by>", currentUser.getFullName() );
		if( currentTask.getResponsible() != null )
			content = content.replace("<resp_name>", currentTask.getResponsible().getFullName());
		else
			content = content.replace("<resp_name>", "");
		
		sendEmailNotifications ( currentTask.getNode ().getEmailNotifications () , content , EmailNotification.ADD_NOTE );
		return null;
	}

	public String downloadFile()
	{
		String path = JSFUtil.getRequestParameter ( "path" );
		String url = "./../FileDownload?path=" + path;
		FacesContext context = FacesContext.getCurrentInstance ();
		try
		{
			context.getExternalContext ().dispatch ( url );
		}
		catch ( Exception e )
		{
			e.printStackTrace ();
		}
		finally
		{
			context.responseComplete ();
		}
		return null;
	}

	public List < Status > getChangedTaskStatuses()
	{
		return changedTaskStatuses;
	}

	public String changeStatus()
	{
		if ( currentTask.getStatus ().getId ().equals ( selectedStatus.getId () ) )
		{
			return "";
		}
		if ( selectedStatus.getStatusType () == Status.CLOSED && selectedStatus.getForNode () == Process.PROCESS_TYPE && ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () != null )
		{
			showDialog = isOpenDialog ();
		}
		else if ( selectedStatus.getStatusType () == Status.ACCEPTED && selectedStatus.getForNode ().equals ( Decision.DESICION_TYPE ) && ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () != null )
		{
			showDialog = isOpenDialog ();
		}
		else if ( selectedStatus.getStatusType () == Status.REJECTED && selectedStatus.getForNode ().equals ( Decision.DESICION_TYPE ) && ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () != null )
		{
			showDialog = isOpenDialog ();
		}
		else if ( selectedStatus.getStatusType () == Status.CLOSED && selectedStatus.getForNode ().equals ( Separator.SEPARATOR_TYPE ) )
		{
			showDialog = isOpenDialog ();
		}
		else if ( selectedStatus.getStatusType () == Status.CLOSED )
		{
			Node node = ( ( Process ) currentTask.getNode () ).getSucceedingProcess ();
			finalize ( node );
		}
		else if ( selectedStatus.getStatusType () == Status.ACCEPTED )
		{
			Node node = ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ();
			finalize ( node );
		}
		else if ( selectedStatus.getStatusType () == Status.REJECTED )
		{
			Node node = ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ();
			finalize ( node );
		}
		else
		{
			if ( selectedStatus.getStatusType () == Status.ASSIGNED )
			{
				currentTask.setResponsible ( currentUser );
			}
			saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
		}

		return null;
	}

	public String calculateAction( Long oldStatus, Long newStatus )
	{
		for ( StatusChange sc : StatusChange.allStatusChanges )
		{
			if ( sc.getOldStatus ().getId ().equals ( oldStatus ) && sc.getNewStatus ().getId ().equals ( newStatus ) )
			{
				return sc.getAction ();
			}
		}
		return "";
	}

	public boolean isAssignableUser()
	{
		if ( currentUserAssignable == null )
		{
			currentUserAssignable = false;
			for ( User user : getAssignableUsers () )
			{
				if ( user.getId ().equals ( currentUser.getId () ) )
				{
					currentUserAssignable = true;
				}
			}
		}

		return currentUserAssignable;
	}

	public History createHistory( String field, String oldVal, String newVal )
	{
		History hs = new History ();
		hs.setDateModified ( Calendar.getInstance ().getTime () );
		hs.setField ( field );
		hs.setUser ( currentUser );
		hs.setChnge ( oldVal + " => " + newVal );
		return hs;
	}

	private void finalize( Node node )
	{
		History hs = createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () );

		getTaskService ().save ( hs );
		currentTask.getWorkorder ().getHistory ().add ( hs );
		getTaskService ().save ( currentTask.getWorkorder () );

		if ( node instanceof Terminate )
		{
			currentTask.setEndTime ( Calendar.getInstance ().getTime () );
			saveCurrentTask ( createHistory ( "Workorder" , "" , "terminated" ) );
			currentTask.getWorkorder ().setEndDate ( Calendar.getInstance ().getTime () );
			getTaskService ().save ( currentTask.getWorkorder () );

		}
		else if ( node instanceof Collector )
		{
			saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
			if ( getTaskService ().isCollectorFinalize ( ( Collector ) node , currentTask.getWorkorder () ) )
			{
				if ( ( ( Collector ) node ).getSucceedingProcess () instanceof TaskNode )
				{
					TaskNode taskNode = ( TaskNode ) ( ( Collector ) node ).getSucceedingProcess ();
					User responsible = null;
					Status status = null;
					if ( taskNode.getAssignableUser ().size () == 1 )
					{
						responsible = taskNode.getAssignableUser ().iterator ().next ();
						status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( taskNode.getType () , Status.ASSIGNED );
					}
					else
					{
						status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( taskNode.getType () , Status.NEW );
					}

					createNextTask ( taskNode , status , responsible );
				}
				else if ( ( ( Collector ) node ).getSucceedingProcess () instanceof Terminate )
				{
					currentTask.setEndTime ( Calendar.getInstance ().getTime () );
					saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
					currentTask.getWorkorder ().setEndDate ( Calendar.getInstance ().getTime () );
					getTaskService ().save ( currentTask.getWorkorder () );
				}
			}
		}
		else
		{
			String action = calculateAction ( currentTask.getStatus().getId() , selectedStatus.getId () );
			
			String content = mailContent.replace("<workorder_sum>", currentTask.getWorkorder().getSummary());
			content = content.replace("<task_name>", currentTask.getNode().getName());
			content = content.replace("<task_id>", "" + currentTask.getId() );
			content = content.replace("<action>", action );
			content = content.replace("<action_by>", currentUser.getFullName() );
			if( currentTask.getResponsible() != null )
				content = content.replace("<resp_name>", currentTask.getResponsible().getFullName());
			else
				content = content.replace("<resp_name>", "");
			
			sendEmailNotifications ( currentTask.getNode ().getEmailNotifications () , content , action );
		}
	}

	public void saveCurrentTask( History hs )
	{
		if ( hs != null )
		{
			if ( currentTask.getWorkorder ().getHistory () == null )
			{
				currentTask.getWorkorder ().setHistory ( new ArrayList < History > () );
			}
			getTaskService ().save ( hs );
			currentTask.getWorkorder ().getHistory ().add ( hs );
			getTaskService ().save ( currentTask.getWorkorder () );
		}

		getTaskService ().save ( hs );
		Long oldStatus = currentTask.getStatus ().getId ();
		currentTask.setStatus ( selectedStatus );

		getTaskService ().save ( currentTask );
		changedTaskStatuses = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).getChangedStatuses ( currentTask.getStatus ().getId () );
		if( currentTask.getNode() instanceof Decision )
		{
			for (Status status : changedTaskStatuses) {
				if( status.getStatusType() == Status.ACCEPTED )
				{
					status.setLabel( ((Decision) currentTask.getNode()).getAcceptLabel() );	
				}
				else if( status.getStatusType() == Status.REJECTED )
				{
					status.setLabel( ((Decision) currentTask.getNode()).getRejectLabel() );	
				}
			}
		}
		String action = calculateAction ( oldStatus , selectedStatus.getId () );
		
		String content = mailContent.replace("<workorder_sum>", currentTask.getWorkorder().getSummary());
		content = content.replace("<task_name>", currentTask.getNode().getName());
		content = content.replace("<task_id>", "" + currentTask.getId() );
		content = content.replace("<action>", action );
		content = content.replace("<action_by>", currentUser.getFullName() );
		if( currentTask.getResponsible() != null )
			content = content.replace("<resp_name>", currentTask.getResponsible().getFullName());
		else
			content = content.replace("<resp_name>", "");
		
		sendEmailNotifications ( currentTask.getNode ().getEmailNotifications () , content , action );
	}

	public Boolean isOpenDialog()
	{
		TaskNode tNode = currentTask.getNode ();
		if ( tNode instanceof Process )
		{
			List < User > nextTaskUsers = new ArrayList < User > ( ( ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess () ).getAssignableUser () );
			if ( ( ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess () ).getProjectDependant () != null && ( ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess () ).getProjectDependant () )
			{
				List < User > projectUsers = new ArrayList < User > ( currentTask.getWorkorder ().getProject ().getUsers () );
				nextTaskUsers.retainAll ( projectUsers );
			}
			return chooseUserForNextTask ( nextTaskUsers , Process.PROCESS_TYPE , null );
		}
		else if ( tNode instanceof Decision && selectedStatus.getStatusType () == Status.ACCEPTED ) // desicion
																									// =>
																									// accept
																									// is
																									// called
		{
			List < User > nextTaskUsers = new ArrayList < User > ( ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess () ).getAssignableUser () );
			if ( ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess () ).getProjectDependant () != null && ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess () ).getProjectDependant () )
			{
				List < User > projectUsers = new ArrayList < User > ( currentTask.getWorkorder ().getProject ().getUsers () );
				nextTaskUsers.retainAll ( projectUsers );
			}
			return chooseUserForNextTask ( nextTaskUsers , Decision.DESICION_TYPE , true );
		}
		else if ( tNode instanceof Decision && selectedStatus.getStatusType () == Status.REJECTED ) // desicion
																									// =>
																									// reject
																									// is
																									// called
		{
			List < User > nextTaskUsers = new ArrayList < User > ( ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess () ).getAssignableUser () );
			if ( ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess () ).getProjectDependant () != null && ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess () ).getProjectDependant () )
			{
				List < User > projectUsers = new ArrayList < User > ( currentTask.getWorkorder ().getProject ().getUsers () );
				nextTaskUsers.retainAll ( projectUsers );
			}
			return chooseUserForNextTask ( nextTaskUsers , Decision.DESICION_TYPE , false );
		}
		else if ( tNode instanceof Separator )
		{
			List < Node > nextNodes = ( ( Separator ) currentTask.getNode () ).getSucceedingProcesses ();
			for ( Node node : nextNodes )
			{
				List < User > nextTaskUsers = new ArrayList < User > ( ( ( TaskNode ) node ).getAssignableUser () );
				List < User > projectUsers = new ArrayList < User > ( currentTask.getWorkorder ().getProject ().getUsers () );
				nextTaskUsers.retainAll ( projectUsers );
				if ( nextTaskUsers == null || nextTaskUsers.size () == 0 || nextTaskUsers.size () > 1 )
				{
					Status st = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( node.getType () , Status.NEW );
					createNextTask ( ( TaskNode ) node , st , null );
				}
				else if ( nextTaskUsers.size () == 1 )
				{
					Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( node.getType () , Status.ASSIGNED );
					createNextTask ( ( TaskNode ) node , status , nextTaskUsers.get ( 0 ) );
				}
			}

			saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
			return false;
		}
		return false;
	}

	public Boolean chooseUserForNextTask( List < User > ntu, int nodeType, Boolean desicionSelection )
	{
		currentTask.setEndTime ( Calendar.getInstance ().getTime () );
		if ( ntu == null || ntu.size () == 0 )
		{
			if ( nodeType == Process.PROCESS_TYPE )
			{
				TaskNode tn = ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess ();
				Status st = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , st , null );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
			else if ( nodeType == Decision.DESICION_TYPE && desicionSelection ) // decision
																				// =>
																				// accept
																				// is
																				// called
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ();
				Status st = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , st , null );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
			else if ( nodeType == Decision.DESICION_TYPE && ! desicionSelection ) // decision
																					// =>
																					// reject
																					// is
																					// called
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ();
				Status st = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , st , null );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
		}
		else if ( ntu.size () == 1 )
		{
			if ( nodeType == Process.PROCESS_TYPE )
			{
				TaskNode tn = ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , ntu.get ( 0 ) );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
			else if ( nodeType == Decision.DESICION_TYPE && desicionSelection )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , ntu.get ( 0 ) );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
			else if ( nodeType == Decision.DESICION_TYPE && ! desicionSelection )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , ntu.get ( 0 ) );
				saveCurrentTask ( createHistory ( "Status" , currentTask.getStatus ().getLabel () , selectedStatus.getLabel () ) );
				return false;
			}
		}
		else if ( ntu.size () > 1 )
		{
			if ( ( nodeType == Process.PROCESS_TYPE && ( ( TaskNode ) ( ( Process ) currentTask.getNode () ) ).getSucceedingResponsableSelectable () ) || ( nodeType == Decision.DESICION_TYPE && desicionSelection && ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ) ).getSucceedingResponsableSelectable () ) || ( nodeType == Decision.DESICION_TYPE && ! desicionSelection && ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ) ).getSucceedingResponsableSelectable () ) )
			{
				this.nextTaskUsers = ntu;
				return true;
			}
			else
			{

				chooseUserForNextTask ( null , nodeType , desicionSelection );
			}
		}
		return false;
	}

	public String dialogAction()
	{
		String whichButton = JSFUtil.getRequestParameter ( "whichButton" );
		if ( whichButton.equals ( "assign" ) )
		{
			if ( currentTask.getNode () instanceof Process )
			{
				TaskNode tn = ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , nextTaskResponsible );
				saveCurrentTask ( createHistory ( "NextTaskUser is selected" , "(Process is closed) Nobody" , nextTaskResponsible.getFullName () ) );
			}
			else if ( currentTask.getNode () instanceof Decision && selectedStatus.getStatusType () == Status.ACCEPTED )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , nextTaskResponsible );
				saveCurrentTask ( createHistory ( "NextTaskUser is selected" , "(Decision is Accepted) Nobody" , nextTaskResponsible.getFullName () ) );
			}
			else if ( currentTask.getNode () instanceof Decision && selectedStatus.getStatusType () == Status.REJECTED )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () , Status.ASSIGNED );
				createNextTask ( tn , status , nextTaskResponsible );
				saveCurrentTask ( createHistory ( "NextTaskUser is selected" , "(Decision is Closed)  Nobody" , nextTaskResponsible.getFullName () ) );
			}
			showDialog = false;
		}
		else if ( whichButton.equals ( "ignore" ) )
		{
			if ( currentTask.getNode () instanceof Process )
			{
				TaskNode tn = ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , status , null );
				saveCurrentTask ( createHistory ( "NextTaskUser is ignored" , "(Process is closed) Nobody" , "Pool" ) );
			}
			else if ( currentTask.getNode () instanceof Decision && selectedStatus.getStatusType () == Status.ACCEPTED )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , status , null );
				saveCurrentTask ( createHistory ( "NextTaskUser is ignored" , "(Decision is Accepted) Nobody" , "Pool" ) );
			}
			else if ( currentTask.getNode () instanceof Decision && selectedStatus.getStatusType () == Status.REJECTED )
			{
				TaskNode tn = ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ();
				Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () , Status.NEW );
				createNextTask ( tn , status , null );
				saveCurrentTask ( createHistory ( "NextTaskUser is ignored" , "(Decision is Rejected) Nobody" , "Pool" ) );
			}
			showDialog = false;
		}
		else if ( whichButton.equals ( "cancel" ) )
		{
			showDialog = false;
			selectedStatus = changedTaskStatuses.get ( 0 );
			// do nothing
		}
		return null;
	}

	public void createNextTask( TaskNode tn, Status status, User responsible )
	{
		Task task = new Task ();
		task.setPreviousTask ( currentTask );
		task.setStartTime ( Calendar.getInstance ().getTime () );
		task.setNode ( tn );
		task.setWorkorder ( currentTask.getWorkorder () );
		task.setStatus ( status );
		task.setResponsible ( responsible );
		setTaskPriority ( task );
		getTaskService ().save ( task );
	}

	public void setTaskPriority( Task tsk )
	{
		if ( tsk.getNode ().getEstimatedTime ().equals ( new Double ( "0" ) ) )
		{
			tsk.setPriority ( new Long ( "1" ) );
		}
		else
		{
			tsk.setPriority ( tsk.getWorkorder ().getDueDate ().getTime () );
		}
	}

	public Boolean getIsManager()
	{
		return isManager;
	}

	public Status getSelectedStatus()
	{
		return selectedStatus;
	}

	public List < User > getAssignableUsers()
	{
		if ( assignableUsers == null || assignableUsers.size () == 0 )
		{
			List < User > possibleUsers = new ArrayList < User > ( currentTask.getNode ().getAssignableUser () );
			assignableUsers = new ArrayList < User > ( possibleUsers );
			if ( currentTask.getNode ().getProjectDependant () != null && currentTask.getNode ().getProjectDependant () )
			{
				List < User > projectUsers = new ArrayList < User > ( currentTask.getWorkorder ().getProject ().getUsers () );
				assignableUsers.retainAll ( projectUsers );
			}
		}
		return assignableUsers;
	}

	public List < FieldValue > getWorkorderFieldValues()
	{
		if ( workorderFieldValues == null || workorderFieldValues.size () == 0 )
		{
			workorderFieldValues = new ArrayList < FieldValue > ( currentTask.getWorkorder ().getFieldValues () );
		}
		return workorderFieldValues;
	}

	public void sendEmailNotifications( List < EmailNotification > enl, String content, String action )
	{
		MySwingWorker worker = new MySwingWorker();
		
		worker.setAction(action);
		worker.setEnl(enl);
		worker.setContent(content);
		worker.setMss( ( IMailSenderService ) ServiceFinder.findBean ( "MailSenderService" ) );
		
		worker.execute();
	}

	public void sendMailToUserlist( Set < User > userList, IMailSenderService mss, String content )
	{
		for ( User user : userList )
		{
			try
			{
				mss.send ( user.getEmail () , content , EmailNotification.STATUS_SUBJECT , EmailNotification.SENDER_MAIL );
			}
			catch ( Exception e )
			{
				ListingManagedBean.addMessage ( FacesMessage.SEVERITY_INFO , e.toString () , e.toString () );
			}
		}
	}

	public String assignTo()
	{
		History hs = null;
		if ( currentTask.getResponsible () != null )
			hs = createHistory ( "AssignedUser" , currentTask.getResponsible ().getFullName () , assignedUser.getFullName () );
		else
			hs = createHistory ( "AssignedUser" , "" , assignedUser.getFullName () );
		if ( currentTask.getWorkorder ().getHistory () == null )
		{
			currentTask.getWorkorder ().setHistory ( new ArrayList < History > () );
		}
		getTaskService ().save ( hs );
		currentTask.getWorkorder ().getHistory ().add ( hs );
		getTaskService ().save ( currentTask.getWorkorder () );

		currentTask.setResponsible ( assignedUser );
		
		Status status = ( ( IStatusService ) ServiceFinder.findBean ( "StatusService" ) ).findStatus ( currentTask.getNode().getType () , Status.ASSIGNED );
		currentTask.setStatus(status);
		
		getTaskService ().save ( currentTask );
		return null;
	}

	public void handleFileUpload( FileUploadEvent event )
	{
		IAttachmentService ias = ( IAttachmentService ) ServiceFinder.findBean ( "AttachmentService" );
		String path = ias.addFieldAttachment ( currentTask.getWorkorder ().getWorkflow ().getName () , event.getFile () );

		Attachment attachment = new Attachment ();
		attachment.setFileAddress ( path );
		attachment.setFilename ( event.getFile ().getFileName () );
		attachment.setFileType ( event.getFile ().getContentType () );
		AttachmentHolder attHolder = ( ( IService ) ServiceFinder.findBean ( "Service" ) ).find ( AttachmentHolder.class , currentTask.getWorkorder ().getId () );
		attachment.setHolder ( attHolder );

		if ( currentTask.getWorkorder ().getAttachments () == null )
		{
			currentTask.getWorkorder ().setAttachments ( new ArrayList < Attachment > () );
		}
		( ( ITaskService ) ServiceFinder.findBean ( "TaskService" ) ).save ( attachment );
		currentTask.getWorkorder ().getAttachments ().add ( attachment );
		
		String content = mailContent.replace("<workorder_sum>", currentTask.getWorkorder().getSummary());
		content = content.replace("<task_name>", currentTask.getNode().getName());
		content = content.replace("<task_id>", "" + currentTask.getId() );
		content = content.replace("<action>", EmailNotification.ADD_ATTACHMENT );
		content = content.replace("<action_by>", currentUser.getFullName() );
		if( currentTask.getResponsible() != null )
			content = content.replace("<resp_name>", currentTask.getResponsible().getFullName());
		else
			content = content.replace("<resp_name>", "");
		
		sendEmailNotifications ( currentTask.getNode ().getEmailNotifications () , content , EmailNotification.ADD_ATTACHMENT );
	}

	public String editNote()
	{
		Long noteId = new Long ( JSFUtil.getRequestParameter ( "noteId" ) );
		currentNote = ( ( IService ) ServiceFinder.findBean ( "Service" ) ).find ( Note.class , noteId );
		return null;
	}

	public String beanInitBeforeDelete()
	{
		String id = JSFUtil.getRequestParameter ( "id" );
		noteToBeDeleted = ( ( IService ) ServiceFinder.findBean ( "Service" ) ).find ( Note.class , new Long ( id ) );
		return null;
	}

	public void changeNote()
	{
		getTaskService ().save ( currentNote );
		for ( Note note : currentTask.getWorkorder ().getNotes () )
		{
			if ( note.getId ().equals ( currentNote.getId () ) )
			{
				currentTask.getWorkorder ().getNotes ().remove ( note );
				currentTask.getWorkorder ().getNotes ().add ( currentNote );
				break;
			}
		}
	}

	public String delete()
	{
		for ( Note note : currentTask.getWorkorder ().getNotes () )
		{
			if ( note.getId ().equals ( noteToBeDeleted.getId () ) )
			{
				currentTask.getWorkorder ().getNotes ().remove ( note );
				break;
			}
		}

		( ( IWorkOrderService ) ServiceFinder.findBean ( "WorkOrderService" ) ).save ( currentTask.getWorkorder () );
		( ( IService ) ServiceFinder.findBean ( "Service" ) ).delete ( noteToBeDeleted );
		return null;
	}

	public void setSelectedStatus( Status selectedStatus )
	{
		this.selectedStatus = selectedStatus;
	}

	public void setIsManager( Boolean isManager )
	{
		this.isManager = isManager;
	}

	public void setAssignableUsers( List < User > assignableUsers )
	{
		this.assignableUsers = assignableUsers;
	}

	public void setChangedTaskStatuses( List < Status > changedTaskStatuses )
	{
		this.changedTaskStatuses = changedTaskStatuses;
	}

	public User getRequestor()
	{
		return requestor;
	}

	public void setRequestor( User requestor )
	{
		this.requestor = requestor;
	}

	public Task getCurrentTask()
	{
		return currentTask;
	}

	public void setCurrentTask( Task currentTask )
	{
		this.currentTask = currentTask;
	}

	public ITaskService getTaskService()
	{
		return ( ITaskService ) ServiceFinder.findBean ( "TaskService" );
	}

	public IUserService getUserService()
	{
		return ( IUserService ) ServiceFinder.findBean ( "UserService" );
	}

	public User getAssignedUser()
	{
		return assignedUser;
	}

	public void setAssignedUser( User assignedUser )
	{
		this.assignedUser = assignedUser;
	}

	public void setWorkorderFieldValues( List < FieldValue > workorderFieldValues )
	{
		this.workorderFieldValues = workorderFieldValues;
	}

	public Boolean getShowDialog()
	{
		return showDialog;
	}

	public void setShowDialog( Boolean showDialog )
	{
		this.showDialog = showDialog;
	}

	public List < User > getNextTaskUsers()
	{
		return nextTaskUsers;
	}

	public void setNextTaskUsers( List < User > nextTaskUsers )
	{
		this.nextTaskUsers = nextTaskUsers;
	}

	public User getNextTaskResponsible()
	{
		return nextTaskResponsible;
	}

	public void setNextTaskResponsible( User nextTaskResponsible )
	{
		this.nextTaskResponsible = nextTaskResponsible;
	}

	public User getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser( User currentUser )
	{
		this.currentUser = currentUser;
	}

	public String getNoteText()
	{
		return noteText;
	}

	public void setNoteText( String noteText )
	{
		this.noteText = noteText;
	}

	public Note getCurrentNote()
	{
		return currentNote;
	}

	public void setCurrentNote( Note currentNote )
	{
		this.currentNote = currentNote;
	}

	public Note getNoteToBeDeleted()
	{
		return noteToBeDeleted;
	}

	public void setNoteToBeDeleted( Note noteToBeDeleted )
	{
		this.noteToBeDeleted = noteToBeDeleted;
	}
	
	private class MySwingWorker extends SwingWorker
	{
		List < EmailNotification > enl;
		String content;
		String action;
		IMailSenderService mss;
		@Override
		protected Void doInBackground() throws Exception 
		{
			for ( EmailNotification en : enl )
			{
				if ( en.getAction ().equals ( action ) || en.getAction ().equals ( EmailNotification.CHANGE_STATUS ) )
				{
					if ( en.getMailSendTo ().equals ( "Successors" ) )
					{
						if ( currentTask.getNode ().getType () == Process.PROCESS_TYPE && ( ( Process ) currentTask.getNode () ).getSucceedingProcess ().getType () != null )
						{
							Set < User > users = ( ( TaskNode ) ( ( Process ) currentTask.getNode () ).getSucceedingProcess () ).getAssignableUser ();
							sendMailToUserlist ( users , mss , content );
						}
	
						else if ( currentTask.getNode ().getType () == Decision.DESICION_TYPE && ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess ().getType () != null && ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess ().getType () != null )
						{
							Set < User > usersAccept = ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getAcceptedsucceedingProcess () ).getAssignableUser ();
							Set < User > usersReject = ( ( TaskNode ) ( ( Decision ) currentTask.getNode () ).getRejectedsucceedingProcess () ).getAssignableUser ();
							usersAccept.retainAll ( usersReject );
							sendMailToUserlist ( usersAccept , mss , content );
						}
						else if ( currentTask.getNode ().getType () == Separator.SEPARATOR_TYPE )
						{
							Set < User > usrs = new HashSet < User > ();
							for ( Node tsk : ( ( Separator ) currentTask.getNode () ).getSucceedingProcesses () )
							{
								usrs.retainAll ( ( ( TaskNode ) tsk ).getAssignableUser () );
							}
							sendMailToUserlist ( usrs , mss , content );
						}
					}
					else if ( en.getMailSendTo ().equals ( "Predecessor" ) )
					{
						if ( currentTask.getPreviousTask () != null )
						{
							sendMailToUserlist ( currentTask.getPreviousTask ().getNode ().getAssignableUser () , mss , content );
						}
					}
	
					else if ( en.getMailSendTo ().equals ( "Requester" ) )
					{
						List < User > users = new ArrayList < User > ();
						users.add ( currentTask.getWorkorder ().getRequesterUser () );
						Set < User > userSet = new HashSet < User > ( users );
						sendMailToUserlist ( userSet , mss , content );
					}
					else if ( en.getMailSendTo ().equals ( "Manager" ) )
					{
						List < User > users = new ArrayList < User > ();
						if( currentTask.getWorkorder ().getWorkflow().getProjectDependant() )
							users.add ( currentTask.getWorkorder ().getProject ().getManager () );
						else
							users.add ( currentTask.getWorkorder ().getWorkflow().getManager() );
						Set < User > userSet = new HashSet < User > ( users );
						sendMailToUserlist ( userSet , mss , content );
					}
				}
			}
			return null;
		}
		
		public void setAction(String action) {
			this.action = action;
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public void setEnl(List<EmailNotification> enl) {
			this.enl = enl;
		}
		
		public String getAction() {
			return action;
		}
		
		public String getContent() {
			return content;
		}
		
		public List<EmailNotification> getEnl() {
			return enl;
		}
		
		public void setMss(IMailSenderService mss) {
			this.mss = mss;
		}
		
		public IMailSenderService getMss() {
			return mss;
		}
	}

}
