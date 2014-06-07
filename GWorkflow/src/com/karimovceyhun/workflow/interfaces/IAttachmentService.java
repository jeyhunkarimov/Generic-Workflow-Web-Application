package com.karimovceyhun.workflow.interfaces;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.karimovceyhun.workflow.data.Attachment;
import com.karimovceyhun.workflow.data.AttachmentHolder;

public interface IAttachmentService {
	public String addFieldAttachment(String workflowName,UploadedFile file);
	
	public void deleteAttachment(String path);
	
	//public AttachmentHolder findHolder(long id, String clazz);

	//public List<Attachment> listAttachments(long id, String clazz);
	
	//public Attachment findAttachment(long id);
	
	//public String getWorkflowName(long id);
}