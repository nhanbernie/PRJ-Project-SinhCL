package com.se4f7.prj301.model.request;

import com.se4f7.prj301.enums.StatusEnum;

public class CategoryModelRequest {

	private String name;
	private String description;
	private StatusEnum status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desciption) {
		this.description = desciption;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}
