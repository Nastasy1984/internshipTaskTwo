package my.app.model;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import my.app.model.serialize.Deserializer;
import my.app.model.serialize.Serializer;

@JsonAutoDetect
public class User {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.model.User.class.getName());
	private Integer id;
	private String firstName;
	private String lastName;
	private String eMail;
	private List <String> phoneNumbers;

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-ddThh:mm:ss")
	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class)
	public LocalDateTime createdOn;

	public User() {
		LOG.debug("New user was created using constructor without parameters");
	}
	
	public User(String firstName, String lastName) {
		LOG.debug("New user was created using constructor with parameters firstName: {}, lastName: {}", firstName, lastName);
		this.firstName = firstName;
		this.lastName = lastName;
		this.eMail = "";
		this.phoneNumbers = new ArrayList<String>();
	}
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		if (eMail == null) {
			this.eMail = "";
		}
		else {
			this.eMail = eMail;
		}
	}

	
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	
	//if user's ids are equal then users are equal because ids are autogenerated and unique  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", eMail=" + eMail
				+ ", phoneNumbers=" + phoneNumbers + ", createdOn=" + createdOn + "]";
	}



}
