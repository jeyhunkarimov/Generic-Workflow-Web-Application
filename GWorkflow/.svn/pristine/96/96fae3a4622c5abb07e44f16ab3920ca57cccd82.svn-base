package com.karimovceyhun.workflow.menu;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;











import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;

@ManagedBean(name = "menuManagedBean")
@ApplicationScoped
public class MenuManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6560274671583169075L;

	
	private MenuModel model = null;

	public MenuModel getModel() {

		if (model == null) {
			loadMenu();
		}

		return model;
	}

	private void loadMenu() 
	{
		model = new DefaultMenuModel();
		// First submenu
		DefaultSubMenu settingsMenu = new DefaultSubMenu();
		settingsMenu.setLabel("Settings");
		
		DefaultSubMenu userMenu = new DefaultSubMenu();
		userMenu.setLabel("User Menu");
		
		DefaultMenuItem projectList = new DefaultMenuItem();
		projectList.setUrl("pages/projectListing.xhtml");
		projectList.setValue("Add Project");
		settingsMenu.getElements().add(projectList);
		
		DefaultMenuItem workflowList = new DefaultMenuItem();
		workflowList.setUrl("pages/workflowListing.xhtml");
		workflowList.setValue("Add Workflow");
		settingsMenu.getElements().add(workflowList);
		
		DefaultMenuItem statistics = new DefaultMenuItem();
		statistics.setUrl("pages/statistics.xhtml");
		statistics.setValue("Statistics");
		settingsMenu.getElements().add(statistics);
		
		DefaultMenuItem changePassword = new DefaultMenuItem();
		changePassword.setUrl("pages/changePassword.xhtml");
		changePassword.setValue("Change Password");
		userMenu.getElements().add(changePassword);
		
		DefaultMenuItem taskList = new DefaultMenuItem();
		taskList.setUrl("pages/taskListing.xhtml");
		taskList.setValue("Tasks");
		userMenu.getElements().add(taskList);
		
		DefaultMenuItem monitorPage = new DefaultMenuItem();
		monitorPage.setUrl("pages/monitoringPageSelection.xhtml");
		monitorPage.setValue("Monitor Pages");
		userMenu.getElements().add(monitorPage);

		DefaultMenuItem workOrderList = new DefaultMenuItem();
		workOrderList.setUrl("pages/workOrderEditor.xhtml");
		workOrderList.setValue("Add WorkOrder");
		userMenu.getElements().add(workOrderList);
		
		DefaultMenuItem projectListUser = new DefaultMenuItem();
		projectListUser.setUrl("pages/projectListing.xhtml");
		projectListUser.setValue("Add Project");
		userMenu.getElements().add(projectListUser);
		
		DefaultMenuItem userTimeline = new DefaultMenuItem();
		userTimeline.setUrl("pages/userTimeline.xhtml");
		userTimeline.setValue("User Timeline");
		userMenu.getElements().add(userTimeline);

		

		DefaultMenuItem userList = new DefaultMenuItem();
		userList.setUrl("pages/userListing.xhtml");
		userList.setValue("Add User");
		settingsMenu.getElements().add(userList);
		
		
		model.addElement(settingsMenu);
		model.addElement(userMenu);

//		model.addSubmenu(userMenu);
//		model.addSubmenu(employees);
//		model.addSubmenu(leaveSubMenu);
//		model.addSubmenu(deductions);
//		model.addSubmenu(roles);
//		model.addSubmenu(projectMenu);
//		model.addSubmenu(toolMenu);
//		model.addSubmenu(financeMenu);
		
	}
}
