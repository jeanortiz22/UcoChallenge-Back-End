package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;


import java.util.UUID;

public class RegisterUserDomain {
	
	private UUID idType;
	private String idNumber;
	private String firstName;
	private String secondName;
	private String firstSurname;
	private String secondSurname;
	private UUID homeCity;
	private String email;
	private String mobileNumber;
	
	
	public RegisterUserDomain(UUID idType, String idNumber, String firstName, String secondName, String firstSurname,
			String secondSurname, UUID homeCity, String email, String mobileNumber) {
		this.idType = idType;
		this.idNumber = idNumber;
		this.firstName = firstName;
		this.secondName = secondName;
		this.firstSurname = firstSurname;
		this.secondSurname = secondSurname;
		this.homeCity = homeCity;
		this.email = email;
		this.mobileNumber = mobileNumber;
	}
	
}	
