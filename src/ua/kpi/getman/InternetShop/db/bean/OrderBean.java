package ua.kpi.getman.InternetShop.db.bean;

import java.util.Date;

import ua.kpi.getman.InternetShop.db.entity.Entity;

/**
 * Order entity.
 * 
 * @author Getman Valentine
 * 
 */
public class OrderBean extends Entity {

	private static final long serialVersionUID = 5692708766041889396L;

	private Long userId;

	private String status;
	
	private String login;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Date date;
	
	private float price;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	@Override
	public String toString() {
		return "Order [userId=" + userId + ", statusId="
				+ status + ", getId()=" + getId() + "]";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
