package com.ibm.mj.core.ceObject;

import java.util.List;

public class GroupDTO {

	private String Name;
	private List<GroupDTO> groups;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public List<GroupDTO> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}
	
	@Override
	public String toString() {
		
		// TODO Auto-generated method stub
		StringBuffer textresult = new StringBuffer();
		textresult.append(" -- Name " ).append(Name);
		textresult.append("\n");
		for(GroupDTO te : groups){
			textresult.append("\t");
			textresult.append(te.toString());
			
		}
		
		return textresult.toString();
		
	
	}
}
