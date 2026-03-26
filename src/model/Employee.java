package model;

import javax.persistence.Entity;
import javax.persistence.Transient;

import main.Logable;
import dao.*;

@Entity
public class Employee extends Person implements Logable {
	
	private int employeeId;
	private String password;
	
	// La anotación @Transient evita que ObjectDB intente guardar el objeto DAO en la base de datos
	@Transient
	private Dao dao = new DaoImplObjectDB();
	
	public Employee(String name) {
		super(name);
	}
	
	public Employee(int employeeId, String name, String password) {
		super(name);
		this.employeeId = employeeId;
		this.password = password;
	}
	
	public Employee() {
		super();
	}
	
	/**
	 * @return the employeeId
	 */
	public int getEmployeeId() {
		return employeeId;
	}
	
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

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

	/**
	 * @param user from application, password from application
	 * @return true if credentials are correct or false if not
	 */
	@Override
	public boolean login(int user, String password) {
		boolean success = false;
		
		// connect to data
		dao.connect();
		
		// get employee data
		if(dao.getEmployee(user, password) != null) {
			success = true;
		}
		
		// disconnect data
		dao.disconnect();
		return success;
	}
}