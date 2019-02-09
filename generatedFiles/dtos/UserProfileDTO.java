package com.sg.netex.dto;

import org.rutebanken.netex.model.UserProfile;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UserProfile")
public class UserProfileDTO extends BaseDTO {
	
	private UserProfile userProfile;
	
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	
}
