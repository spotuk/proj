package ua.kpi.getman.InternetShop.db;

/**
 * Holder for fields names of DB tables and beans.
 * 
 * @author Getman Valentine
 * 
 */
public final class Fields {

	// entities
	public static final String ENTITY_ID = "id";

	public static final String USER_LOGIN = "login";
	public static final String USER_PASSWORD = "password";
	public static final String USER_FIRST_NAME = "first_name";
	public static final String USER_LAST_NAME = "last_name";
	public static final String USER_ROLE_ID = "role_id";
	public static final String USER_STATUS_ID = "status_id";
	public static final String USER_EMAIL = "email";
	
	
	public static final String USER_ROLE_FOR_ADMIN = "role";
	public static final String USER_DATE_FOR_ADMIN = "date_added";
	public static final String USER_STATUS_FOR_ADMIN = "status";

	public static final String PRODUCT_TITLE = "title";
	public static final String PRODUCT_PRICE = "price";
	public static final String PRODUCT_CATEGORY_ID = "category_id";
	public static final String PRODUCT_QUANTITY = "quantity";
	public static final String PRODUCT_BRAND = "brand";
	public static final String PRODUCT_DATE = "date_added";
	public static final String PRODUCT_DESCRIPTION = "description";
	public static final String PRODUCT_IMG_LOCATION = "img";

	public static final String ORDER_USER_ID = "user_id";
	public static final String ORDER_USER_LOGIN = "login";
	public static final String ORDER_STATUS = "status";
	public static final String ORDER_DATE = "date_added";
	public static final String ORDER_PRICE = "price";
	
	
	public static final String ORDER_DETAILS_ORDER_ID = "order_id";
	public static final String ORDER_DETAILS_PRODUCT_ID = "product_id";
	public static final String ORDER_DETAILS_TITLE = "title";
	public static final String ORDER_DETAILS_BRAND = "brand";
	public static final String ORDER_DETAILS_PRICE= "price";
	public static final String ORDER_DETAILS_QUANTITY = "quantity";
	public static final String ORDER_DETAILS_TOTAL = "total";


	public static final String CATEGORY_TITLE = "title";
	public static final String CATEGORY_SORT_ORDER = "sort_order";

	public static final String LANGUAGE_TITLE = "title";
	public static final String MENU_ITEM_PRICE = "price";
	public static final String MENU_ITEM_NAME = "name";
	public static final String MENU_ITEM_CATEGORY_ID = "category_id";



}