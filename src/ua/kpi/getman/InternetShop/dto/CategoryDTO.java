package ua.kpi.getman.InternetShop.dto;

public class CategoryDTO {
	private String ruCategoryTitle;
	
	private String enCategoryTitle;
	
	private int sortOrder;

	public String getRuCategoryTitle() {
		return ruCategoryTitle;
	}

	public void setRuCategoryTitle(String ruCategoryTitle) {
		this.ruCategoryTitle = ruCategoryTitle;
	}

	public String getEnCategoryTitle() {
		return enCategoryTitle;
	}

	public void setEnCategoryTitle(String enCategoryTitle) {
		this.enCategoryTitle = enCategoryTitle;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
