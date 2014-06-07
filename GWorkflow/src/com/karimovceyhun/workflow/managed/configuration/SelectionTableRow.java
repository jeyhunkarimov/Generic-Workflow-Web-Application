package com.karimovceyhun.workflow.managed.configuration;

import java.awt.CheckboxMenuItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.karimovceyhun.workflow.data.EmailNotification;

public class SelectionTableRow implements Serializable
{
	private String rowName; 
	private  Boolean[] columns;
	private int type;

	private static final long serialVersionUID = 2051765699921811476L;
	public SelectionTableRow(String rowName,Boolean[] columns,int type) 
	{
		this.rowName = rowName;
		this.setColumns(columns);
		this.type = type;
	}
	public List<EmailNotification> getMailNotifications()
	{
		int r;
		int c;
		int pos;
		List<EmailNotification> enl = new ArrayList<EmailNotification>();
		for(int i = 0; i < columns.length ; i++ )
		{
			if(columns[i])
			{
				switch (this.type) 
				{
				case 0:
					r = SelectionRowAndColumns.getProcessRows().indexOf(rowName) + 1;
					c = i + 1;
					pos = (r - 1) * SelectionRowAndColumns.getProcessColumns().size() + c  - 1;
					enl.add(SelectionRowAndColumns.processNots.get(pos));
					break;
				case 1:
					r = SelectionRowAndColumns.getDecisionRows().indexOf(rowName) + 1;
					c = i + 1;
					pos = (r - 1) * SelectionRowAndColumns.getDecisionColumns().size() + c - 1;
					enl.add(SelectionRowAndColumns.decisionNots.get(pos));
					break;
				case 2:
					r = SelectionRowAndColumns.getSeperatorRows().indexOf(rowName) + 1;
					c = i + 1;
					pos = (r - 1) * SelectionRowAndColumns.getSeperatorColumns().size() + c - 1;
					enl.add(SelectionRowAndColumns.seperatorNots.get(pos));
					break;
				case 3:
					r = SelectionRowAndColumns.getCollectorRows().indexOf(rowName) + 1;
					c = i + 1;
					pos = (r - 1) * SelectionRowAndColumns.getCollectorColumns().size() + c - 1;
					enl.add(SelectionRowAndColumns.collectorNots.get(pos));
					break;
				default:
					break;
				}
			}
		}
		return enl;
	}
	public String getRowName() {
		return rowName;
	}
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	
	
	
	public Boolean[] getColumns() {
		return columns;
	}
	public void setColumns(Boolean[] columns) {
		this.columns = new Boolean[columns.length];
		for(int i = 0; i < columns.length ; i ++)
		{
			this.columns[i] = columns[i];
		}
	}

}
