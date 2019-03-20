package ua.kpi.getman.InternetShop.db.entity;

import java.util.Date;

public class Product extends Entity {
	private static final long serialVersionUID = 2386302708905518585L;

	private String title;
	private String brand;
	private String description;
	private String img;
	
	private int quantity;
	private float price;
	
	private Date date ;
	
	
	
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brend) {
		this.brand = brend;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	private Long categoryId;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
