package ua.kpi.getman.InternetShop.db.entity;

/**
 * Category entity.
 * 
 * @author D.Kolesnikov
 * 
 */
public class Category extends Entity {

	private static final long serialVersionUID = 2386302708905518585L;

	private String title;
	
	private int sortOrder;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Category [title=" + title + ", getId()=" + getId() + "]";
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
