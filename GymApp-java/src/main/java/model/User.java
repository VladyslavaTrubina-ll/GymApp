package model;

public class User {

	 private String uid;

	    private String firstName;

	    private String lastName;

	    private String email;

	    private String birthDate;

	    private int level;

	    private String password;

	    /**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}



		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}



		public User() {

	    }



	    public User(String uid, String firstName, String lastName,

	                String email, String birthDate, int level) {

	        this.uid = uid;

	        this.firstName = firstName;

	        this.lastName = lastName;

	        this.email = email;

	        this.birthDate = birthDate;

	        this.level = level;

	    }



	    public String getUid() {

	        return uid;

	    }



	    public void setUid(String uid) {

	        this.uid = uid;

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



	    public String getEmail() {

	        return email;

	    }



	    public void setEmail(String email) {

	        this.email = email;

	    }



	    public String getBirthDate() {

	        return birthDate;

	    }



	    public void setBirthDate(String birthDate) {

	        this.birthDate = birthDate;

	    }



	    public int getLevel() {

	        return level;

	    }



	    public void setLevel(int level) {

	        this.level = level;

	    }

}
