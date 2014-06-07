package com.karimovceyhun.workflow.interfaces;

import java.io.Serializable;
import java.util.List;

import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Task;
import com.karimovceyhun.workflow.data.WorkOrder;

public interface ITaskService extends IService,Serializable 
{
     public List<Task> getAssignedToMeTasks(Long responsibleId, int first, int pageSize, String sortField, Boolean sortAssending);
     public List<Task> getClosedByMeTasks(Long responsibleId, int first, int pageSize,String sortField, Boolean sortAssending) ;
     public List<Task> getMonitoringByMeTasks(Long userId, int first, int pageSize, String sortField, Boolean sortAssending) ;
     public List<Task> getReportedByMeTasks(Long userId, int first, int pageSize, String sortField, Boolean sortAssending) ;
     public List<Task> getAssignableToMeTasks(Long responsibleId, int first, int pageSize, String sortField, Boolean sortAssending) ;
     
     public boolean isCollectorFinalize(Collector collector,WorkOrder workOrder) ;
     
     public Task getTask(Long id);
          
     public Integer getAssignedToMeTasksCount(Long responsibleId);
     public Integer getClosedByMeTasksCount(Long responsibleId);
     public Integer getMonitoringByMeTasksCount(Long responsibleId);
     public Integer getReportedByMeTasksCount(Long responsibleId);
     public Integer getAssignableToMeTasksCount(Long userId);
     public List<Task> getManagedByMeTasks(Long currentUserId, int first,int pageSize, String sortField, boolean equals);
     public int getManagedByMeTasksCount(Long currentUserId);
}
