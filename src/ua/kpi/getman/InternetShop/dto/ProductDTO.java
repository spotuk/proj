package ua.kpi.getman.InternetShop.dto;

public class ProductDTO {
	
	private String ruProductTitle;
	
	private String enProductTitle;
	
	private String ruProductDescription;
	
	private String enProductDescription;
	
	private String brand;
	
	private long categoryId;
	
	private float price;
	
	private int quantity;
	
	private String img;

	public String getRuProductTitle() {
		return ruProductTitle;
	}

	public void setRuProductTitle(String ruProductTitle) {
		this.ruProductTitle = ruProductTitle;
	}

	public String getEnProductTitle() {
		return enProductTitle;
	}

	public void setEnProductTitle(String enProductTitle) {
		this.enProductTitle = enProductTitle;
	}

	public String getRuProductDescription() {
		return ruProductDescription;
	}

	public void setRuProductDescription(String ruProductDescription) {
		this.ruProductDescription = ruProductDescription;
	}

	public String getEnProductDescription() {
		return enProductDescription;
	}

	public void setEnProductDescription(String enProductDescription) {
		this.enProductDescription = enProductDescription;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
