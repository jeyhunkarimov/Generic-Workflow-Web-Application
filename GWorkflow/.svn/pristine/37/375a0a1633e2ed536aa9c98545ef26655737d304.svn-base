package com.karimovceyhun.workflow.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;

@Entity
@Table(name="attachments")
public class Attachment implements Bean, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2160719392870808548L;
	private Long id;
	private String filename;
	private String fileType;
	private String fileAddress;
	private AttachmentHolder holder;
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}
	public String getFileAddress() {
		return fileAddress;
	}
	
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public void setHolder(AttachmentHolder holder) {
		this.holder = holder;
	}
	
	@ManyToOne
	@JoinColumn(name="holder_id", nullable=false)
	public AttachmentHolder getHolder() {
		return holder;
	}
	
	

}
