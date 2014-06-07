package com.karimovceyhun.workflow.managed.configuration;

import java.util.ArrayList;
import java.util.List;

import com.karimovceyhun.workflow.data.EmailNotification;

public class EmailNotificationGenerator 
{
	public static List<SelectionTableRow> table;
	
	public static List<SelectionTableRow> initTable(int whichInit)
	{
		switch (whichInit) 
		{
		case 0:
			 initProcessTable();
			break;
		case 1:
			initDecisionTable();
			break;
		case 2:
			initSeperatorTable();
			break;
		case 3:
			initCollectorTable();
			break;
		}
		return table;
	}
	

	public static void initProcessTable()
	{
		table = new ArrayList<SelectionTableRow>();
		Boolean[] checkBoxes = new Boolean[SelectionRowAndColumns.getProcessColumns().size()];
		/*for(int i = 0; i < SelectionRowAndColumns.getProcessColumns().size()-1 ; i ++)
		{
			checkBoxes[i] = false;
		}
		*/
		SelectionTableRow row1 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(0),checkBoxes,0);
		SelectionTableRow row2 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(1),checkBoxes,0);
		SelectionTableRow row3 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(2),checkBoxes,0);
		SelectionTableRow row4 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(3),checkBoxes,0);
		SelectionTableRow row5 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(4),checkBoxes,0);
		SelectionTableRow row6 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(5),checkBoxes,0);
		SelectionTableRow row7 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(6),checkBoxes,0);
		SelectionTableRow row8 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(7),checkBoxes,0);
		SelectionTableRow row9 = new SelectionTableRow(SelectionRowAndColumns.getProcessRows().get(8),checkBoxes,0);
    	table.add(row1);
    	table.add(row2);
    	table.add(row3);
    	table.add(row4);
    	table.add(row5);
    	table.add(row6);
    	table.add(row7);
    	table.add(row8);
    	table.add(row9);
	}
	
	public static void initDecisionTable()
	{
		table = new ArrayList<SelectionTableRow>();
		Boolean[] checkBoxes = new Boolean[SelectionRowAndColumns.getDecisionColumns().size() ];
		/*for(int i = 0; i < SelectionRowAndColumns.getProcessColumns().size()-1 ; i ++)
		{
			checkBoxes[i] = false;
		}
		*/
		SelectionTableRow row1 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(0),checkBoxes,1);
		SelectionTableRow row2 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(1),checkBoxes,1);
		SelectionTableRow row3 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(2),checkBoxes,1);
		SelectionTableRow row4 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(3),checkBoxes,1);
		SelectionTableRow row5 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(4),checkBoxes,1);
		SelectionTableRow row6 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(5),checkBoxes,1);
		SelectionTableRow row7 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(6),checkBoxes,1);
		SelectionTableRow row8 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(7),checkBoxes,1);
		SelectionTableRow row9 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(8),checkBoxes,1);
		SelectionTableRow row10 = new SelectionTableRow(SelectionRowAndColumns.getDecisionRows().get(9),checkBoxes,1);
    	table.add(row1);
    	table.add(row2);
    	table.add(row3);
    	table.add(row4);
    	table.add(row5);
    	table.add(row6);
    	table.add(row7);
    	table.add(row8);
    	table.add(row9);
    	table.add(row10);
	}
	
	public static void initSeperatorTable()
	{
		table = new ArrayList<SelectionTableRow>();
		Boolean[] checkBoxes = new Boolean[SelectionRowAndColumns.getSeperatorColumns().size() ];
		/*for(int i = 0; i < SelectionRowAndColumns.getProcessColumns().size()-1 ; i ++)
		{
			checkBoxes[i] = false;
		}
		*/
		SelectionTableRow row1 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(0),checkBoxes,2);
		SelectionTableRow row2 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(1),checkBoxes,2);
		SelectionTableRow row3 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(2),checkBoxes,2);
		SelectionTableRow row4 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(3),checkBoxes,2);
		SelectionTableRow row5 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(4),checkBoxes,2);
		SelectionTableRow row6 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(5),checkBoxes,2);
		SelectionTableRow row7 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(6),checkBoxes,2);
		SelectionTableRow row8 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(7),checkBoxes,2);
		SelectionTableRow row9 = new SelectionTableRow(SelectionRowAndColumns.getSeperatorRows().get(8),checkBoxes,2);
    	table.add(row1);
    	table.add(row2);
    	table.add(row3);
    	table.add(row4);
    	table.add(row5);
    	table.add(row6);
    	table.add(row7);
    	table.add(row8);
    	table.add(row9);
	}
	
	public static void initCollectorTable()
	{
		table = new ArrayList<SelectionTableRow>();
		Boolean[] checkBoxes = new Boolean[SelectionRowAndColumns.getCollectorColumns().size() ];
		/*for(int i = 0; i < SelectionRowAndColumns.getProcessColumns().size()-1 ; i ++)
		{
			checkBoxes[i] = false;
		}
		*/
		SelectionTableRow row1 = new SelectionTableRow(SelectionRowAndColumns.getCollectorRows().get(0),checkBoxes,3);
		SelectionTableRow row2 = new SelectionTableRow(SelectionRowAndColumns.getCollectorRows().get(1),checkBoxes,3);
		
    	table.add(row1);
    	table.add(row2);
	}
	
	public static List<EmailNotification> getEmailNotifications(List<SelectionTableRow> tb)
	{
		List<EmailNotification> enl = new ArrayList<EmailNotification>();
		if(tb == null)
		{
			return enl;
		}
		for(SelectionTableRow selectionTableRow : tb)
		{
			List<EmailNotification> temp = selectionTableRow.getMailNotifications();
			enl.addAll(temp);
		}
		return enl;
	}
	
	public static List<SelectionTableRow> setMailNotifications(List<EmailNotification> enl,int index)
	{
		initTable(index);
		if(enl == null || enl.size() == 0)
		{
			return table;
		}
		for(EmailNotification en : enl)
		{
			addEmailNotificationToTable(en,index);
		}
		return table;
	}
	
	public static void addEmailNotificationToTable(EmailNotification en,int index)
	{
		String action = en.getAction();
		String sendTo = en.getMailSendTo();
		int row = 0;
		int col = 0;
		switch (index) 
		{
		case 0:
			row = SelectionRowAndColumns.getProcessRows().indexOf(action);
			col = SelectionRowAndColumns.getProcessColumns().indexOf(sendTo);
			break;
		case 1:
			row = SelectionRowAndColumns.getDecisionRows().indexOf(action);
			col = SelectionRowAndColumns.getDecisionColumns().indexOf(sendTo);
			break;
		case 2:
			row = SelectionRowAndColumns.getSeperatorRows().indexOf(action);
			col = SelectionRowAndColumns.getSeperatorColumns().indexOf(sendTo);
			break;
		case 3:
			row = SelectionRowAndColumns.getCollectorRows().indexOf(action);
			col = SelectionRowAndColumns.getCollectorColumns().indexOf(sendTo);
			break;

		default:
			break;
		}
		
		table.get(row).getColumns()[col]= true;
		
	}
}
