package com.quicktutorialz.nio.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class Users {
	
	@Id
	@Column(name="id")
    private Integer id;
	
	@Id
	@Column(name="name")
    private String name;
	
	@Id
	@Column(name="email")
    private String email;
	
	@Id
	@Column(name="password")
    private String password;
	
	@Id
	@Column(name="estado")
    private int estado;
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Users() {
		
	}
	public Users(String name, String email, String password, int estado, int id) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.estado = estado;
		this.id = id;
	}
	
}

