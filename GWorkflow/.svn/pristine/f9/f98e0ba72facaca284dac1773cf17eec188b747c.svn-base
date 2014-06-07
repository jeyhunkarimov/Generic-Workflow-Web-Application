package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.primefaces.extensions.component.timeline.TimelineUpdater;
import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.SortOrder;

import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.WorkOrder;
import com.karimovceyhun.workflow.interfaces.ITaskService;
import com.karimovceyhun.workflow.services.ServiceFinder;


@ManagedBean ( name = "userTimelineManagedBean" )
@ViewScoped
public class UserTimelineManagedBean implements Serializable{
	private static final long serialVersionUID = -6126958404103494408L;
	private TimelineModel modelFirst;  // model of the first timeline  
	private TimelineModel modelSecond; // model of the second timeline  
	private boolean aSelected;         // flag if the project A is selected (for test of select() call on the 2. model)  
	private List<Task> assignedTasks;
	private List<WorkOrder>assignedWorkorders;
	private Long currentUserId;

	public UserTimelineManagedBean(){
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		currentUserId = ( Long ) request.getSession ().getAttribute ( "id" );
		loadAssignedTasks();
		createFirstTimeline();  
		createSecondTimeline();  
	}

	private void loadAssignedTasks(){
		assignedTasks = new ArrayList<>();
		assignedTasks.addAll(new LinkedHashSet<>(getTaskService ().getAssignedToMeTasks ( currentUserId , 0 , 0 , null , null )));	
	}

	public ITaskService getTaskService()
	{
		return ( ITaskService ) ServiceFinder.findBean ( "TaskService" );
	}


	private void createFirstTimeline() {
		modelFirst = new TimelineModel();
		for(Task task : assignedTasks){
			DateTime starDate = new DateTime(task.getStartTime());
			int addedHours = task.getNode().getEstimatedTime().intValue();
			DateTime endDate = starDate.plusHours(addedHours);
			
			double addedMinutes = task.getNode().getEstimatedTime() - addedHours;
			if(addedMinutes>0){
				addedMinutes = addedMinutes * 60;
				endDate = endDate.plusMinutes((int) addedMinutes);
			}
			modelFirst.add(new TimelineEvent(task, task.getStartTime(), endDate.toDate()));
		}
	}

	private void createSecondTimeline() {
		assignedWorkorders = new ArrayList<>();
		for(Task task: assignedTasks){
			if(!assignedWorkorders.contains(task.getWorkorder())){
				assignedWorkorders.add(task.getWorkorder());
			}
		}
		modelSecond = new TimelineModel();

		for(WorkOrder wo: assignedWorkorders){
			modelSecond.add(new TimelineEvent(wo.getWorkflow().getName(), wo.getStartDate(), wo.getEndDate()));
		}
	}

	public void onSelect(TimelineSelectEvent e) {
		// get a thread-safe TimelineUpdater instance for the second timeline
		TimelineUpdater timelineUpdater = TimelineUpdater.getCurrentInstance(":mainForm:timelineSecond");

		if (aSelected) {
			// select project B visually (index in the event's list is 1)
			timelineUpdater.select(1);
		} else {
			// select project A visually (index in the event's list is 0)
			timelineUpdater.select(0);
		}

		aSelected = !aSelected;

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected project: " + (aSelected ? "A" : "B"), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public TimelineModel getModelFirst() {
		return modelFirst;
	}

	public TimelineModel getModelSecond() {
		return modelSecond;
	}


}
