package com.karimovceyhun.workflow.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.primefaces.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;

import com.karimovceyhun.workflow.interfaces.IAttachmentService;
import com.karimovceyhun.workflow.interfaces.IService;


public class AttachmentService extends Service implements
IAttachmentService, IService {

	public static final String FILEUPLOADPATH = "C:\\fileupload";

	public AttachmentService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Transactional
	public String addFieldAttachment(String workflowName ,UploadedFile file) {
		File fileOriginal = null;

		try {
			// check for existence
			String uploadFileDirectory = FILEUPLOADPATH + File.separator + workflowName;
			File uploadFileDirectoryFile = new File(uploadFileDirectory);
			if(!uploadFileDirectoryFile.exists())
			{
				uploadFileDirectoryFile.mkdirs();
			}
			fileOriginal = new File(uploadFileDirectory + File.separator +  file.getFileName());

			for(int i = 1 ; fileOriginal.exists()  ; i++ )
			{
				String filePath = fileOriginal.getAbsolutePath();
				String newFilePath = filePath.replaceAll("\\([0-9]+\\)\\.", "(" + i + ").");
				if(filePath.equals( newFilePath))
				{
					int fileExtIndex = filePath.indexOf(".");
					filePath = filePath.substring(0, fileExtIndex) + "(" + i + ")" + filePath.substring(fileExtIndex, filePath.length());
				}
				else
				{
					filePath = newFilePath;
				}
				fileOriginal = new File(filePath);
			}

			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileOriginal));
			IOUtils.copy(file.getInputstream(), outputStream);
			outputStream.close();
			file.getInputstream().close();
			return fileOriginal.getAbsolutePath();

		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}

	}

	@Transactional
	public void deleteAttachment(String path)
	{
		File file = new File ( path );
		file.delete ();
	}

	

	

}

