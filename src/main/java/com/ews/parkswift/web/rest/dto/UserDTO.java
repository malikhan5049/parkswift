package com.ews.parkswift.web.rest.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserDTO {
	
    @Size(min = 5, max = 100)
    private String login;

    
    
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;
    
    @Size(max = 20)
    private String contactNumber;
    
    @Email
    @NotNull
    @Size(min = 5, max = 100)
    private String email;
    
    @Email
    @Size(min = 5, max = 100)
    private String paypalEmail;

	@Size(min = 2, max = 5)
    private String langKey;

    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login == null ? email : login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLangKey() {
        return langKey == null?"en":langKey;
    }

    public List<String> getRoles() {
        return roles;
    }
    
    public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

    @Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", langKey='" + langKey + '\'' +
        ", roles=" + roles +
        '}';
    }
}
