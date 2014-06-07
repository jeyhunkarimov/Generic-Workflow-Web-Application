package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.karimovceyhun.workflow.data.Bean;
import com.karimovceyhun.workflow.interfaces.IService;
import com.karimovceyhun.workflow.services.Service;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;
import com.karimovceyhun.workflow.utilities.SecurityCheck;

public abstract class ListingManagedBean<T extends Bean> implements Serializable 
{

	private String input = null;
	private String selectedColumn = null;
	
	/**
	* 
	*/
	private static final long serialVersionUID = 5712592661127967798L;
	
	private T bean;
	
	private String beanName;
	
	public ListingManagedBean(String beanName) 
	{
		this.setBeanName(beanName);
		clearTheBean();
	}
	
	public void beanInitBeforeDelete(ActionEvent event) 
	{
		String id = JSFUtil.getRequestParameter("id");
		T bean = (T) this.getCommonService().find(this.getBean().getClass(),new Long(id));
		this.setBean(bean);
	}
	
	public void clearTheBean() 
	{
		bean = (T) ServiceFinder.findBean(this.getBeanName());
	}
	
	/**
	* Holds if editor should be displayed
	*/
	private boolean editMode = false;
	
	public String save() 
	{
	
		try 
		{
			List<FacesMessage> facesMessages = new ArrayList<FacesMessage>();
			doBeforeSave(facesMessages);
		
			if (facesMessages.size() > 0) 
			{
				setEditMode(true);
		
				for (FacesMessage message : facesMessages) 
				{
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
		
			} 
			else 
			{
				doSave();
				addMessage(FacesMessage.SEVERITY_INFO, "node is saved",
						"node is saved");
				loadData();
				setEditMode(false);
				clearTheBean();
				doAfterSave();
			}
		
		} 
		catch (Exception e) 
		{
			setEditMode(true);
			addMessage(FacesMessage.SEVERITY_INFO,
					"node is not saved" + e.getMessage(), "node is not saved" + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	
	}
	
	public void doAfterSave() {
	
	}
	
	/**
	* Load data method to refresh list.
	*/
	abstract protected void loadData();
	
	/**
	* Things to do before save phase
	*/
	abstract protected void doBeforeSave(List<FacesMessage> messages);
	
	/**
	* Things to do at save.
	*/
	
	protected void doSave() 
	{
		/**
		 * Hibernates gives error because it searches for given id if not there,
		 * it puts error
		 */
		if (getBean().getId() != null && getBean().getId().longValue() == 0) {
			getBean().setId(null);
		}
		getCommonService().save(getBean());
	}
	
	/**
	* Add new bean screen
	* 
	* @return
	*/
	public String addNew() 
	{
		clearTheBean();
		doInitializeForNew();
		setEditMode(true);
		return null;
	}
	
	protected void doInitializeForNew(){
	
	}
	
	/**
	* Common message take it to above
	* 
	* @param severity
	* @param summary
	* @param detail
	*/
	public static void addMessage(Severity severity, String summary,
		String detail) {
	FacesMessage message = new FacesMessage(severity, summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	/**
	* cancel action
	* 
	* @return
	*/
	public String cancel() {
	setEditMode(false);
	refresh();
	clearTheBean();
	return null;
	}
	
	/**
	* Edit link clicked
	*/
	/**
	* Edit action
	* 
	* @return
	*/
	public String edit() 
	{
	
	initializeBean();
	doAfterInitialization();
	setEditMode(true);
	return null;
	}
	
	/**
	* Here do the staff after edit is clicked
	*/
	protected void doAfterInitialization() 
	{
	
	}
	
	/**
	* Set bean method Is called when edit of a bean is clicked
	*/
	protected void initializeBean() 
	{
		String id = JSFUtil.getRequestParameter("id");
		setBean((T) getCommonService().find(this.getBean().getClass(),new Long(id)));
	}
	
	/**
	* Sets mode as edit.
	* 
	* @param editMode
	*/
	public void setEditMode(boolean editMode) {
	this.editMode = editMode;
	}
	
	public boolean isEditMode() {
	return editMode;
	}
	
	/**
	* Forces a new ui tree.
	*/
	public void refresh() {
	FacesContext context = FacesContext.getCurrentInstance();
	UIViewRoot root = context.getViewRoot();
	clean(root.getChildren());
	}
	
	public void clean(List<UIComponent> components) {
	for (UIComponent component : components) {
		if (component instanceof UIInput) {
			((UIInput) component).setSubmittedValue(null);
			((UIInput) component).setValue(null);
			((UIInput) component).setLocalValueSet(false);
		}
	
		clean(component.getChildren());
	}
	}
	
	/**
	* Delete action
	* 
	* @return
	*/
	public String delete() {
	try {
		doDelete();
		addMessage(FacesMessage.SEVERITY_INFO, "node is deleted",
				"node is deleted");
	
		loadData();
	
	} catch (Exception e) {
		e.printStackTrace();
		addMessage(FacesMessage.SEVERITY_INFO, "node is not deleted",
				"node is not deleted");
	}
	
	return null;
	}
	
	/**
	* Deletes the node
	*/
	protected void doDelete() 
	{
		this.getCommonService().delete(this.getBean());
	}
	
	public IService getCommonService() 
	{
		return (IService) ServiceFinder.findBean("Service");
	}
	
	public void setBean(T bean) {
	this.bean = bean;
	}
	
	public T getBean() {
	return bean;
	}
	
	public boolean checkSecurity(String page, String action) {
	return SecurityCheck.securityCheck(page, action);
	}
	
	public void setBeanName(String beanName) {
	this.beanName = beanName;
	}
	
	public String getBeanName() {
	return beanName;
	}
	
	public void setInput(String input) {
	this.input = input;
	}
	
	public String getInput() {
	return input;
	}
	
	public SelectItem[] getListOfColumns() {
	SelectItem[] items = new SelectItem[1];
	items[0] = new SelectItem("", "");
	return items;
	}
	
	public void setSelectedColumn(String selectedColumn) {
	this.selectedColumn = selectedColumn;
	}
	
	public String getSelectedColumn() {
	return selectedColumn;
	}
	
	public void filter() {
	loadData();
	}
	
	public DetachedCriteria getDetachedCriteria() 
	{
	
		DetachedCriteria detachedCriteria = getDefaultCriteria();
		
		if (selectedColumn == null || selectedColumn.trim().equals("")
				|| input == null || input.trim().equals(""))
		
			return detachedCriteria;
		
		else 
		{
			String[] parsedColumn = selectedColumn.split("\\|");
		
			String type = parsedColumn[0];
			String query = parsedColumn[1];
		
			Object inputObject = null;
		
			if (type.equals("String") || type.equals("AutoComplete")) {
		
				inputObject = new String(input);
		
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
		
			} else if (type.startsWith("Enum")) {
				
				String typeAnalysis[] = type.split("\\:");
				String orjType = typeAnalysis[typeAnalysis.length-1];
				
				if(orjType.equals("Long"))
					inputObject = new Long(input);
				else
					inputObject = new Integer(input);
		
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
			} else if (type.equals("Long")) {
				
				inputObject = new Long(input);
		
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
			else if (type.equals("Integer")) {
				
				inputObject = new Integer(input);
		
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
	
	
	public DetachedCriteria getDefaultCriteria() {
		return DetachedCriteria.forClass(this.getBean().getClass());
	}
	
	public SelectItem[] getEnums() {
		SelectItem[] items = new SelectItem[1];
		items[0] = new SelectItem("", "");
		return items;
	}
	
	public boolean isEnumType() {
		String selectedColumn = getSelectedColumn();
		if (selectedColumn != null && selectedColumn.startsWith("Enum")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean isAutoComplete() {
		String selectedColumn = getSelectedColumn();
		if (selectedColumn != null && selectedColumn.startsWith("AutoComplete")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void changeColumn(AjaxBehaviorEvent event) {
		this.setInput("");
	}
	
	
	public String getCurrentMaxDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		return dateFormat.format( Calendar.getInstance().getTime());
	}
	
	public Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

}

