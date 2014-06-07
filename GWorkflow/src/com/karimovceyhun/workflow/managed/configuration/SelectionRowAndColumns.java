package com.karimovceyhun.workflow.managed.configuration;

import java.util.ArrayList;
import java.util.List;








import com.karimovceyhun.workflow.data.EmailNotification;
import com.karimovceyhun.workflow.interfaces.IEmailNotificationService;
import com.karimovceyhun.workflow.services.ServiceFinder;

public class SelectionRowAndColumns 
{
	
	public static List<EmailNotification> processNots = null;
	public static List<EmailNotification> decisionNots = null;
	public static List<EmailNotification> seperatorNots = null;
	public static List<EmailNotification> collectorNots = null;
	public static List<String> processColumns = null;
	public static List<String> processRows = null;
	public static List<String> decisionColumns = null;
	public static List<String> decisionRows = null;
	public static List<String> seperatorColumns = null;
	public static List<String> seperatorRows = null;
	public static List<String> collectorColumns = null;
	public static List<String> collectorRows = null;

	public static void initRowAndColumnNames()
	{
		if( processNots == null )
		{
			processNots = ((IEmailNotificationService)ServiceFinder.findBean("EmailNotificationService")).listForNode(1);
		}
		processColumns = new ArrayList<String>();
		processRows = new ArrayList<String>();
		initArrays(processColumns, processRows, processNots);
		
		
		if( decisionNots == null )
		{
			decisionNots = ((IEmailNotificationService)ServiceFinder.findBean("EmailNotificationService")).listForNode(2);
		}
		decisionColumns = new ArrayList<String>();
		decisionRows = new ArrayList<String>();
		initArrays(decisionColumns, decisionRows, decisionNots);
		
		
		if( seperatorNots == null )
		{
			seperatorNots = ((IEmailNotificationService)ServiceFinder.findBean("EmailNotificationService")).listForNode(3);
		}
		seperatorColumns = new ArrayList<String>();
		seperatorRows = new ArrayList<String>();
		initArrays(seperatorColumns, seperatorRows, seperatorNots);
			
		
		if( collectorNots == null )
		{
			collectorNots = ((IEmailNotificationService)ServiceFinder.findBean("EmailNotificationService")).listForNode(4);
		}
		collectorColumns = new ArrayList<String>();
		collectorRows = new ArrayList<String>();
		initArrays(collectorColumns, collectorRows, collectorNots);
	}


	private static void initArrays(List<String> processColumns,List<String> processRows, List<EmailNotification> processNots) {
		for(EmailNotification en:processNots)
		{
			if(!processColumns.contains(en.getMailSendTo()))
			{	
				processColumns.add(en.getMailSendTo());
			}
			if(!processRows.contains(en.getAction()))
			{
				processRows.add(en.getAction());
			}
		}
	}


	public static String getProcessRowName(int index)
	{
		return processColumns.get(index);
	}
	
	public static String getDecisionRowName(int index)
	{
		return decisionColumns.get(index);
	}
	public static String getSeperatorRowName(int index)
	{
		return seperatorColumns.get(index);
	}
	public static String getCollectorRowName(int index)
	{
		return collectorColumns.get(index);
	}

	public static List<String> getProcessColumns() 
	{
		/*if(processColumns == null || processColumns.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return processColumns;
	}
	
	public static void setProcessColumns(List<String> processColumns) 
	{
		SelectionRowAndColumns.processColumns = processColumns;
	}
	public static List<String> getProcessRows() 
	{
		/*if(processRows == null || processRows.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return processRows;
	}
	public static void setProcessRows(List<String> processRows) {
		SelectionRowAndColumns.processRows = processRows;
	}
	public static List<String> getDecisionColumns() {
		/*if(decisionColumns == null || decisionColumns.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return decisionColumns;
	}
	public static void setDecisionColumns(List<String> decisionColumns) {
		SelectionRowAndColumns.decisionColumns = decisionColumns;
	}
	public static List<String> getDecisionRows() {
		/*if(decisionRows == null || decisionRows.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return decisionRows;
	}
	public static void setDecisionRows(List<String> decisionRows) {
		SelectionRowAndColumns.decisionRows = decisionRows;
	}
	public static List<String> getSeperatorColumns() {
		/*if(seperatorColumns == null || seperatorColumns.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return seperatorColumns;
	}
	public static void setSeperatorColumns(List<String> seperatorColumns) {
		SelectionRowAndColumns.seperatorColumns = seperatorColumns;
	}
	public static List<String> getSeperatorRows() {
		/*if(seperatorRows == null || seperatorRows.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return seperatorRows;
	}
	public static void setSeperatorRows(List<String> seperatorRows) {
		SelectionRowAndColumns.seperatorRows = seperatorRows;
	}
	
	public static List<String> getCollectorColumns() {
		/*if(collectorColumns == null || collectorColumns.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return collectorColumns;
	}
	public static void setCollectorColumns(List<String> collectorColumns) {
		SelectionRowAndColumns.collectorColumns = collectorColumns;
	}
	public static List<String> getCollectorRows() {
		/*if(collectorRows == null || collectorRows.size() == 0)
		{
			initRowAndColumnNames();
		}*/
		return collectorRows;
	}
	public static void setCollectorRows(List<String> collectorRows) {
		SelectionRowAndColumns.collectorRows = collectorRows;
	}
}
