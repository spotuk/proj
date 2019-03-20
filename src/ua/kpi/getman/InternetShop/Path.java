package ua.kpi.getman.InternetShop;

/**
 * Path holder (jsp pages, controller commands).
 * 
 * @author Getman Valentine
 * 
 */
public final class Path {
	
	// pages
	public static final String PAGE_LOGIN = "/login.jsp";
	public static final String PAGE_CATEGORIES = "/categories.jsp";
	public static final String PAGE_ORDERS = "/orders.jsp";
	public static final String PAGE_ORDER_DETAILS = "/orderDetails.jsp";
	public static final String PAGE_PRODUCT = "/product.jsp";
	public static final String PAGE_PRODUCTS = "/products.jsp";
	public static final String PAGE_USERS = "/users.jsp";
	public static final String PAGE_MAIN = "/main.jsp";
	public static final String PAGE_CART = "/shopping_cart.jsp";
	public static final String PAGE_CABINET = "/personal_cabinet.jsp";
	public static final String PAGE_ADMIN_PANEL = "/admin_panel.jsp";
	public static final String PAGE_ADMIN_PRODUCT_CREATE = "/productCreate.jsp";
	public static final String PAGE_ADMIN_CATEGORY_CREATE = "/categoryCreate.jsp";

	
	public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
	public static final String PAGE_LIST_MENU = "/WEB-INF/jsp/client/list_menu.jsp";
	public static final String PAGE_LIST_ORDERS = "/WEB-INF/jsp/admin/list_orders.jsp";
	public static final String PAGE_SETTINGS = "/WEB-INF/jsp/settings.jsp";

	// commands
	public static final String COMMAND_LIST_ORDERS = "/controller?command=listOrders";
	public static final String COMMAND_LIST_MENU = "/controller?command=listMenu";
	
	//directories
	public static final String UPLOAD_DIR = "uploads";

}