package com.karimovceyhun.workflow.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.karimovceyhun.workflow.data.Bean;


@Entity
@Table(name="attachmentholder")
@Inheritance(strategy=InheritanceType.JOINED)
public class AttachmentHolder implements Bean, Serializable{
	
	private Long id;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2787071582041006861L;
	
	List<Attachment> attachments = new ArrayList<Attachment>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="holder")
	public List<Attachment> getAttachments()
	{
		return this.attachments;
	}
	
	public void setAttachments(List<Attachment> attachments)
	{
		this.attachments = attachments;
	}	
	
	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
}
