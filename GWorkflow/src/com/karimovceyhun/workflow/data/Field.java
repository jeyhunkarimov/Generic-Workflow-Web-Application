package com.karimovceyhun.workflow.data;

import java.io.Serializable;

import javax.faces.model.SelectItem;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="field")
public class Field implements Bean, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2160719392870808548L;

	private Long id;
	private String fieldname;
	private String defaultValue;
	private String value;
	private String type;
	private Boolean required;
	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Transient
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Field )
		{
			if( id != null )
				return this.id.equals(((Field)obj).getId());
			else
				return this.fieldname.equals(((Field)obj).getFieldname());
		}
		return super.equals(obj);
	}
}
