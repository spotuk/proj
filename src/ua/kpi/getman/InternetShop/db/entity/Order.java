package ua.kpi.getman.InternetShop.db.entity;

import java.util.Date;

public class Order extends Entity {

	private static final long serialVersionUID = 5692708766041889396L;

	private Long userId;

	private String status;
	private Date date;

	private float price;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Order [userId=" + userId + ", statusId=" + status + ", getId()=" + getId() + "]";
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

}
