package ua.kpi.getman.InternetShop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.kpi.getman.InternetShop.bean.CartBean;
import ua.kpi.getman.InternetShop.bean.CartItemBean;
import ua.kpi.getman.InternetShop.db.bean.OrderBean;
import ua.kpi.getman.InternetShop.db.entity.Brand;
import ua.kpi.getman.InternetShop.db.entity.Category;
import ua.kpi.getman.InternetShop.db.entity.Language;
import ua.kpi.getman.InternetShop.db.entity.MenuItem;
import ua.kpi.getman.InternetShop.db.entity.OrderDetails;
import ua.kpi.getman.InternetShop.db.entity.Product;
import ua.kpi.getman.InternetShop.db.entity.User;
import ua.kpi.getman.InternetShop.db.entity.UserForAdmin;
import ua.kpi.getman.InternetShop.dto.CategoryDTO;
import ua.kpi.getman.InternetShop.dto.ProductDTO;
import ua.kpi.getman.InternetShop.exception.DBException;
import ua.kpi.getman.InternetShop.exception.Messages;

/**
 * DB manager. Works with Apache Derby DB. Only the required DAO methods are
 * defined!
 * 
 * @author Getman Valentine
 * 
 */
public final class DBManager {

	private static final Logger LOG = Logger.getLogger(DBManager.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static DBManager instance;

	public static synchronized DBManager getInstance() throws DBException {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private DBManager() throws DBException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			// ST4DB - the name of data source
			ds = (DataSource) envContext.lookup("jdbc/ST4DB");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
	}

	private DataSource ds;

	// //////////////////////////////////////////////////////////
	// SQL queries
	// //////////////////////////////////////////////////////////
	
	private static final String SQL_UPDATE_PRODUCT_PARAMS_BY_ID = "UPDATE products SET category_id=?,quantity=?,price=?,brand=?,img=? WHERE products.id = ?";

	private static final String SQL_UPDATE_PRODUCT_PARAMS_WITHOUT_IMG_BY_ID = "UPDATE products SET category_id=?,quantity=?,price=?,brand=?  WHERE products.id = ?";

	private static final String SQL_INSERT_PRODUCT = "INSERT INTO  products(id,category_id,quantity,price,img,brand)  VALUES (DEFAULT,?,?,?,?,? )";

	private static final String SQL_INSERT_PRODUCTS_LOCALIZATION = "INSERT INTO  products_localization(id,product_id,language_id,title,description) VALUES (DEFAULT,?,?,?,?)";

	private static final String SQL_INSERT_USER = "INSERT INTO "
			+ "users(id,login,password,first_name,last_name,role_id,status_id,email) "
			+ "VALUES (DEFAULT,?,?,?,?,?,?,?)";
	
	private static final String SQL_FIND_PRODUCT_PARAMS_BY_ID = "SELECT products.id,products_localization.title,products.category_id,products_localization.description,products.quantity,products.price,products.brand,products.img"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)INNER JOIN language ON products_localization.language_id=language.id WHERE language.id = ? and products.id = ?";

	
	private static final String SQL_INSERT_CATEGORY = "INSERT INTO  categories(id,sort_order) " + " VALUES (DEFAULT,?)";

	private static final String SQL_INSERT_CATEGORIES_LOCALIZATION = "INSERT INTO "
			+ "categories_localization(id,category_id,language_id,title) " + "VALUES (DEFAULT,?,?,?)";

	private static final String SQL_INSERT_ORDER = "INSERT INTO orders(id,user_id,status_id) " + "VALUES (DEFAULT,?,?)";
	private static final String SQL_INSERT_ORDER_DETAILS = "INSERT INTO "
			+ "order_details(id,order_id,product_id,quantity)  VALUES (DEFAULT,?,?,?)";

	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";

	private static final String SQL_UPDATE_USER_STATUS_BY_ID = "UPDATE users SET status_id=? WHERE users.id = ?";

	private static final String SQL_UPDATE_ORDER_STATUS_BY_ID = "UPDATE orders SET status_id=? WHERE orders.id = ?";

	private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users " + " WHERE users.id = ?";

	private static final String SQL_DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE orders.id  = ?";

	private static final String SQL_DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE categories.id  = ?";

	private static final String SQL_DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE products.id  = ?";

	private static final String SQL_FIND_USER_BY_LOGIN_EMAIL = "SELECT * FROM users WHERE login=? and email = ?";

	private static final String SQL_FIND_ORDERS_BY_USER_ID = "SELECT orders.id,orders.user_id, users.login, orders_status.name as Status,orders.date_added, SUM(order_details.quantity*products.price) as price  FROM ( orders INNER JOIN orders_status ON orders.status_id = orders_status.id) "
			+ "INNER JOIN order_details ON order_details.order_id = orders.id "
			+ " INNER JOIN users on orders.user_id = users.id "
			+ "INNER JOIN products on order_details.product_id = products.id " + "WHERE orders.user_id =? "
			+ "GROUP BY orders.id";

	private static final String SQL_FIND_ALL_ORDERS = "SELECT orders.id,orders.user_id,users.login,orders_status.name as Status,orders.date_added, SUM(order_details.quantity*products.price) as price  FROM ( orders INNER JOIN orders_status ON orders.status_id = orders_status.id) \r\n"
			+ " INNER JOIN order_details ON order_details.order_id = orders.id "
			+ " INNER JOIN products on order_details.product_id = products.id "
			+ " INNER JOIN users on orders.user_id = users.id " + " GROUP BY orders.id";

	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";
	private static final String SQL_FIND_ALL_USERS = "SELECT users.id,users.login,users.password,users.first_name,users.last_name,users.email, users_status.name as status, roles.name role, users.date_added FROM users "
			+ "INNER JOIN roles on users.role_id = roles.id "
			+ "INNER JOIN users_status on users.status_id = users_status.id " + "ORDER BY users.login ";
	private static final String SQL_FIND_CATEGORY_PARAMS_BY_ID = "SELECT categories.id,categories_localization.title,categories.sort_order FROM(categories INNER JOIN categories_localization ON categories.id=categories_localization.category_id)INNER JOIN language ON categories_localization.language_id=language.id WHERE language.id = ? and categories.id = ?";

	private static final String SQL_UPDATE_CATEGORY_SORTORDER_BY_ID = "UPDATE categories SET sort_order=? WHERE categories.id = ?";

	private static final String SQL_UPDATE_CATEGORY_TITLE_BY_ID = "UPDATE categories_localization SET title=? WHERE category_id =? and language_id = ?";
	
	private static final String SQL_UPDATE_PRODUCT_TITLE_DESCRIPTION_BY_ID = "UPDATE products_localization SET title=?,description=? WHERE product_id =? and language_id = ?";

	private static final String SQL_FIND_ORDER_DETAILS_BY_ID = "SELECT order_details.id,order_details.product_id,order_details.order_id,products_localization.title,products.brand, products.price, order_details.quantity, (products.price*order_details.quantity) as total "
			+ "FROM (orders INNER JOIN order_details ON orders.id = order_details.order_id) "
			+ "INNER JOIN orders_status on orders_status.id = orders.status_id "
			+ "INNER JOIN products ON order_details.product_id = products.id "
			+ "INNER JOIN products_localization on order_details.product_id = products_localization.product_id "
			+ "INNER JOIN language ON products_localization.language_id=language.id WHERE language.id=? and order_details.order_id = ? "
			+ "ORDER BY `products_localization`.`title` ASC";

	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.price BETWEEN ? AND ? ";

	private static final String SQL_FIND_PRODUCTS_BY_CATEGORY_ID = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id WHERE language.id = ? AND products.category_id=?";

	private static final String SQL_FIND_PRODUCT_BY_ID = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added "
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id WHERE products.id=? AND language.id = ?";

	private static final String SQL_FIND_PRODUCTS = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added "
			+ "FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id WHERE language.id = ?";

	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_PRICE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY price ASC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_PRICE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY price DESC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_TITLE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY title ASC";

	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_TITLE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY title DESC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_DATE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY date_added ASC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_DATE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? AND products.price BETWEEN ? AND ? ORDER BY date_added DESC";

	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_PRICE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.price BETWEEN ? AND ? ORDER BY price ASC";
	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_PRICE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  AND products.price BETWEEN ? AND ? ORDER BY price DESC";
	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_TITLE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.price BETWEEN ? AND ? ORDER BY title ASC";

	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_TITLE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  AND products.price BETWEEN ? AND ? ORDER BY title DESC";
	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_DATE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.price BETWEEN ? AND ? ORDER BY date_added ASC";
	private static final String SQL_SORT_PRODUCTS_BY_PRICE_RANGE_DATE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.price BETWEEN ? AND ? ORDER BY date_added DESC";

	private static final String SQL_SORT_PRODUCTS_BY_BRAND = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=?";

	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY price ASC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_PRICE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY price DESC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_TITLE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY title ASC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_TITLE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY title DESC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_DATE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY date_added ASC";
	private static final String SQL_SORT_PRODUCTS_BY_BRAND_DATE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? AND products.brand=? ORDER BY date_added DESC";

	private static final String SQL_SORT_PRODUCTS_BY_PRICE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? ORDER BY price ASC";
	private static final String SQL_SORT_PRODUCTS_BY_PRICE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=? ORDER BY price DESC";
	private static final String SQL_SORT_PRODUCTS_BY_TITLE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  ORDER BY title ASC";

	private static final String SQL_SORT_PRODUCTS_BY_TITLE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  ORDER BY title DESC";
	private static final String SQL_SORT_PRODUCTS_BY_DATE_ASC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  ORDER BY date_added ASC";
	private static final String SQL_SORT_PRODUCTS_BY_DATE_DESC = "SELECT products_localization.title, products.brand,products.price,products_localization.description,products.id,products.category_id,products.quantity,products.img,products.date_added"
			+ " FROM(products INNER JOIN products_localization ON products.id=products_localization.product_id)"
			+ "INNER JOIN language ON products_localization.language_id=language.id "
			+ "WHERE language.id=?  ORDER BY date_added DESC";

	private static final String SQL_FIND_ALL_MENU_ITEMS = "SELECT * FROM menu";

	private static final String SQL_FIND_ORDERS_BY_STATUS_AND_USER = "SELECT * FROM orders WHERE status_id=? AND user_id=?";

	private static final String SQL_FIND_MENU_ITEMS_BY_ORDER = "select * from menu where id in (select menu_id from orders_menu where order_id=?)";

	private static final String SQL_FIND_ORDERS_BY_STATUS = "SELECT * FROM orders WHERE status_id=?";

	private static final String SQL_FIND_ALL_CATEGORIES = "SELECT categories_localization.title,categories.id ,categories.sort_order "
			+ "FROM(categories INNER JOIN categories_localization ON categories.id=categories_localization.category_id) "
			+ "INNER JOIN language ON categories_localization.language_id=language.id WHERE language.id = ? "
			+ " ORDER BY categories.sort_order";
	
	private static final String SQL_FIND_CATEGORY_BY_TITLE = "SELECT categories_localization.title,categories.id, categories.sort_order "
			+ "FROM(categories INNER JOIN categories_localization ON categories.id=categories_localization.category_id) "
			+ "INNER JOIN language ON categories_localization.language_id=language.id WHERE language.id = ? and categories.id = ? ";

	private static final String SQL_FIND_ALL_LANGUAGES = "SELECT * FROM language";

	private static final String SQL_FIND_LANGUAGE_BY_NAME = "SELECT language.id, language.title FROM `language` WHERE language.title = ?";

	private static final String SQL_SORT_BY_CATEGORY_ID_PRODUCTS_DESC = "SELECT * FROM products WHERE category_id=?"
			+ " ORDER BY price DESC";

	private static final String SQL_SORT_BY_CATEGORY_ID_PRODUCTS_ASC = "SELECT * FROM products WHERE category_id=?"
			+ " ORDER BY price ASC";

	private static final String SQL_FIND_BRANDS = "SELECT DISTINCT brand FROM products";

	private static final String SQL_UPDATE_USER = "UPDATE users SET  first_name=?, last_name=?" + "	WHERE login=?";

	private static final String SQL_GET_USER_ORDER_BEANS = "SELECT o.id, u.first_name, u.last_name, o.bill, s.name"
			+ "	FROM users u, orders o, statuses s" + "	WHERE o.user_id=u.id AND o.status_id=s.id";

	/**
	 * Returns a DB connection from the Pool Connections. Before using this method
	 * you must configure the Date Source and the Connections Pool in your
	 * WEB_APP_ROOT/META-INF/context.xml file.
	 * 
	 * @return DB connection.
	 * @throws SQLException
	 */
	/*
	 * public Connection getConnection() throws DBException { Connection con = null;
	 * try { con = getConnectionWithDriverManager(); } catch (SQLException ex) {
	 * LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex); throw new
	 * DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex); } return con;
	 * 
	 * }
	 */

	public Connection getConnection() throws DBException, SQLException {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
		// return getConnectionWithDriverManager();

	}

	// //////////////////////////////////////////////////////////s
	// Methods to obtain beans
	// //////////////////////////////////////////////////////////
	/**
	 * Returns all categories.
	 * 
	 * @return List of category entities.
	 */

	// //////////////////////////////////////////////////////////
	// Entity access methods
	// //////////////////////////////////////////////////////////

	/**
	 * Returns all categories.
	 * 
	 * @return List of category entities.
	 */
	public List<Category> findCategories(long langId) throws DBException {
		List<Category> categoriesList = new ArrayList<Category>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_CATEGORIES);
			pstmt.setLong(1, langId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				categoriesList.add(extractCategory(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return categoriesList;
	}
	
	public Category findCategoryNameByIdAndLangId(long langId, long categoryId) throws DBException {
		Category category = new Category();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_CATEGORY_BY_TITLE);
			pstmt.setLong(1, langId);
			pstmt.setLong(2, categoryId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				category = (extractCategory(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return category;
	}

	public List<Language> findLanguages() throws DBException {
		List<Language> langList = new ArrayList<Language>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_LANGUAGES);
			while (rs.next()) {
				langList.add(extractLanguage(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return langList;
	}

	public long findLanguageByName(String langTitle) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		Language lang = new Language();
		long langId = 0;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_LANGUAGE_BY_NAME);
			pstmt.setString(1, langTitle);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				lang = extractLanguage(rs);
				langId = lang.getId();
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return langId;
	}

	public List<Brand> findBrands() throws DBException {
		List<Brand> brandsList = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_BRANDS);
			while (rs.next()) {
				brandsList.add(extractBrand(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return brandsList;
	}

	/**
	 * Returns all menu items.
	 * 
	 * @return List of menu item entities.
	 */
	public List<MenuItem> findMenuItems() throws DBException {
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_MENU_ITEMS);
			while (rs.next()) {
				menuItemsList.add(extractMenuItem(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_MENU_ITEMS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_MENU_ITEMS, ex);
		} finally {
			close(con, stmt, rs);
		}
		return menuItemsList;
	}

	/**
	 * Returns menu items of the given order.
	 * 
	 * @param order Order entity.
	 * @return List of menu item entities.
	 */
	public List<MenuItem> findMenuItems(OrderBean order) throws DBException {
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_MENU_ITEMS_BY_ORDER);
			pstmt.setLong(1, order.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				menuItemsList.add(extractMenuItem(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_MENU_ITEMS_BY_ORDER, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_MENU_ITEMS_BY_ORDER, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return menuItemsList;
	}

	/**
	 * Returns menu items with given identifiers.
	 * 
	 * @param ids Identifiers of menu items.
	 * @return List of menu item entities.
	 */
	public List<MenuItem> findMenuItems(String[] ids) throws DBException {
		List<MenuItem> menuItemsList = new ArrayList<MenuItem>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();

			// create SQL query like "... id IN (1, 2, 7)"
			StringBuilder query = new StringBuilder("SELECT * FROM menu WHERE id IN (");
			for (String idAsString : ids) {
				query.append(idAsString).append(',');
			}
			query.deleteCharAt(query.length() - 1);
			query.append(')');

			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			while (rs.next()) {
				menuItemsList.add(extractMenuItem(rs));
			}
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_MENU_ITEMS_BY_IDENTIFIERS, ex);
		} finally {
			close(con, stmt, rs);
		}
		return menuItemsList;
	}

	/**
	 * Returns all orders.
	 * 
	 * @return List of order entities.
	 */
	public List<OrderBean> findOrdersByUserId(long user_id) throws DBException {
		List<OrderBean> ordersList = new ArrayList<OrderBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ORDERS_BY_USER_ID);
			pstmt.setLong(1, user_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordersList.add(extractOrder(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return ordersList;
	}

	public List<OrderBean> findAllOrders() throws DBException {
		List<OrderBean> ordersList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_ORDERS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordersList.add(extractOrder(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return ordersList;
	}

	public List<UserForAdmin> findAllUsers() throws DBException {
		List<UserForAdmin> usersList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_USERS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usersList.add(extractUserForAdmin(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con, pstmt, rs);
		}
		return usersList;
	}

	/**
	 * Returns orders with the given status.
	 * 
	 * @param statusId Status identifier.
	 * @return List of order entities.
	 */
	public List<OrderBean> findOrders(int statusId) throws DBException {
		List<OrderBean> ordersList = new ArrayList<OrderBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ORDERS_BY_STATUS);
			pstmt.setInt(1, statusId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordersList.add(extractOrder(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_STATUS_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return ordersList;
	}

	/**
	 * Returns orders with given identifiers.
	 * 
	 * @param ids Orders identifiers.
	 * @return List of order entities.
	 */
	public List<OrderBean> findOrders(String[] ids) throws DBException {
		List<OrderBean> ordersList = new ArrayList<OrderBean>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();

			// create SQL query like "... id IN (1, 2, 7)"
			StringBuilder query = new StringBuilder("SELECT * FROM orders WHERE id IN (");
			for (String idAsString : ids) {
				query.append(idAsString).append(',');
			}
			query.deleteCharAt(query.length() - 1);
			query.append(')');

			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			while (rs.next()) {
				ordersList.add(extractOrder(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_IDENTIFIERS, ex);
		} finally {
			close(con, stmt, rs);
		}
		return ordersList;
	}

	/**
	 * Returns orders of the given user and status
	 * 
	 * @param user     User entity.
	 * @param statusId Status identifier.
	 * @return List of order entities.
	 * @throws DBException
	 */
	public List<OrderBean> findOrders(User user, int statusId) throws DBException {
		List<OrderBean> ordersList = new ArrayList<OrderBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ORDERS_BY_STATUS_AND_USER);
			pstmt.setInt(1, statusId);
			pstmt.setLong(2, user.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordersList.add(extractOrder(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_USER_AND_STATUS_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return ordersList;
	}

	/**
	 * Returns a user with the given identifier.
	 * 
	 * @param id User identifier.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUserById(long id) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	public List<OrderDetails> findOrderDetailsByOrderId(long categoryId, long langId) throws DBException {
		List<OrderDetails> ordersList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_ORDER_DETAILS_BY_ID);
			pstmt.setLong(1, langId);
			pstmt.setLong(2, categoryId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordersList.add(extractOrderDetails(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return ordersList;
	}

	public CategoryDTO findCategoryForUpdateById(long categoryId) throws DBException {
		CategoryDTO categoryDTO = new CategoryDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_CATEGORY_PARAMS_BY_ID);
			pstmt.setLong(1, findLanguageByName("RU"));
			pstmt.setLong(2, categoryId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				categoryDTO.setRuCategoryTitle(rs.getString(Fields.CATEGORY_TITLE));
				categoryDTO.setSortOrder(rs.getInt(Fields.CATEGORY_SORT_ORDER));
			}

			pstmt = con.prepareStatement(SQL_FIND_CATEGORY_PARAMS_BY_ID);
			pstmt.setLong(1, findLanguageByName("EN"));
			pstmt.setLong(2, categoryId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				categoryDTO.setEnCategoryTitle(rs.getString(Fields.CATEGORY_TITLE));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return categoryDTO;
	}

	public ProductDTO findProductForUpdateById(long productId) throws DBException {
		ProductDTO productDTO = new ProductDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_PRODUCT_PARAMS_BY_ID);
			pstmt.setLong(1, findLanguageByName("RU"));
			pstmt.setLong(2, productId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				productDTO.setRuProductTitle(rs.getString(Fields.PRODUCT_TITLE));
				productDTO.setRuProductDescription(rs.getString(Fields.PRODUCT_DESCRIPTION));
				productDTO.setBrand(rs.getString(Fields.PRODUCT_BRAND));
				productDTO.setImg(rs.getString(Fields.PRODUCT_IMG_LOCATION));

				productDTO.setCategoryId(rs.getLong(Fields.PRODUCT_CATEGORY_ID));
				productDTO.setPrice(rs.getFloat(Fields.PRODUCT_PRICE));
				productDTO.setQuantity(rs.getInt(Fields.PRODUCT_QUANTITY));
			}

			pstmt = con.prepareStatement(SQL_FIND_PRODUCT_PARAMS_BY_ID);
			pstmt.setLong(1, findLanguageByName("EN"));
			pstmt.setLong(2, productId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				productDTO.setEnProductTitle(rs.getString(Fields.PRODUCT_TITLE));
				productDTO.setEnProductDescription(rs.getString(Fields.PRODUCT_DESCRIPTION));

			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productDTO;
	}

	public boolean updateCategoryById(long categoryId, CategoryDTO categoryParams) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_CATEGORY_SORTORDER_BY_ID);
			pstmt.setLong(1, categoryParams.getSortOrder());
			pstmt.setLong(2, categoryId);
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_UPDATE_CATEGORY_TITLE_BY_ID);
			pstmt.setString(1, categoryParams.getEnCategoryTitle());
			pstmt.setLong(2, categoryId);
			pstmt.setLong(3, findLanguageByName("EN"));
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_UPDATE_CATEGORY_TITLE_BY_ID);

			pstmt.setString(1, categoryParams.getRuCategoryTitle());
			pstmt.setLong(2, categoryId);
			pstmt.setLong(3, findLanguageByName("RU"));

			pstmt.executeUpdate();

			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
	}
	
	
	public boolean updateProductById(long productId, ProductDTO productParams) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			if(productParams.getImg().isEmpty() || productParams.getImg() == null) {
				pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_PARAMS_WITHOUT_IMG_BY_ID);
				pstmt.setLong(1, productParams.getCategoryId());
				pstmt.setInt(2, productParams.getQuantity());
				pstmt.setFloat(3, productParams.getPrice());
				pstmt.setString(4, productParams.getBrand());
				pstmt.setLong(5, productId);
			}else {
			pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_PARAMS_BY_ID);
			pstmt.setLong(1, productParams.getCategoryId());
			pstmt.setInt(2, productParams.getQuantity());
			pstmt.setFloat(3, productParams.getPrice());
			pstmt.setString(4, productParams.getBrand());
			pstmt.setString(5, productParams.getImg());
			pstmt.setLong(6, productId);
			
			}
			
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_TITLE_DESCRIPTION_BY_ID);
			pstmt.setString(1, productParams.getEnProductTitle());
			pstmt.setString(2, productParams.getEnProductDescription());
			pstmt.setLong(3, productId);
			pstmt.setLong(4, findLanguageByName("EN"));
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_TITLE_DESCRIPTION_BY_ID);

			pstmt.setString(1, productParams.getRuProductTitle());
			pstmt.setString(2, productParams.getRuProductDescription());
			pstmt.setLong(3, productId);
			pstmt.setLong(4, findLanguageByName("RU"));
			pstmt.executeUpdate();

			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
	}

	public List<Product> findProductsByCategoryId(long id, long langId) throws DBException {
		List<Product> productsList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_PRODUCTS_BY_CATEGORY_ID);
			pstmt.setLong(1, langId);
			pstmt.setLong(2, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> findProducts(long langId) throws DBException {
		List<Product> productsList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_PRODUCTS);
			pstmt.setLong(1, langId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> sortProducts(String choosedBrand, String sortType, float priceFrom, float priceTo, long langId)
			throws DBException {
		List<Product> productsList = new LinkedList<Product>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			switch (sortType) {
			case "title ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_TITLE_ASC);
				break;
			case "title DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_TITLE_DESC);
				break;
			case "price ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_PRICE_ASC);
				break;
			case "price DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_PRICE_DESC);
				break;
			case "date_added ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_DATE_ASC);
				break;
			case "date_added DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_DATE_DESC);
				break;
			default:
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_RANGE_DATE_DESC);
				break;
			}

			pstmt.setLong(1, langId);
			pstmt.setString(2, choosedBrand);
			pstmt.setFloat(3, priceFrom);
			pstmt.setFloat(4, priceTo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> sortProducts(String choosedBrand, String sortType, long langId) throws DBException {
		List<Product> productsList = new LinkedList<Product>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();

			switch (sortType) {
			case "title ASC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_TITLE_ASC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_TITLE_ASC);
				}
				break;
			case "title DESC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_TITLE_DESC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_TITLE_DESC);
				}
				break;
			case "price ASC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_ASC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_ASC);
				}
				break;
			case "price DESC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_PRICE_DESC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_DESC);
				}
				break;
			case "date_added ASC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_DATE_ASC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_DATE_ASC);
				}
				break;
			case "date_added DESC":
				if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND_DATE_DESC);
				} else {
					pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_DATE_DESC);
				}
				break;
			default:
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_BRAND);
				break;
			}

			pstmt.setLong(1, langId);
			if (!(choosedBrand.isEmpty()) && (choosedBrand != null)) {
				pstmt.setString(2, choosedBrand);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> sortProducts(String sortType, float priceFrom, float priceTo, long langId) throws DBException {
		List<Product> productsList = new LinkedList<Product>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			switch (sortType) {
			case "title ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_TITLE_ASC);
				break;
			case "title DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_TITLE_DESC);
				break;
			case "price ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_PRICE_ASC);
				break;
			case "price DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_PRICE_DESC);
				break;
			case "date_added ASC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_DATE_ASC);
				break;
			case "date_added DESC":
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_DATE_DESC);
				break;
			default:
				pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE_DATE_DESC);
				break;
			}

			pstmt.setLong(1, langId);
			pstmt.setFloat(2, priceFrom);
			pstmt.setFloat(3, priceTo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> sortProducts(float priceFrom, float priceTo, long langId) throws DBException {
		List<Product> productsList = new LinkedList<Product>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_SORT_PRODUCTS_BY_PRICE_RANGE);
			pstmt.setLong(1, langId);
			pstmt.setFloat(2, priceFrom);
			pstmt.setFloat(3, priceTo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				productsList.add(extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return productsList;
	}

	public List<Product> sortProductsByCategoryId(String typeOfSort, long categoryId) throws DBException {
		List<Product> productsList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			if ("asc".equals(typeOfSort)) {

				stmt = con.prepareStatement(SQL_SORT_BY_CATEGORY_ID_PRODUCTS_ASC);
				stmt.setLong(1, categoryId);
				rs = stmt.executeQuery();
				while (rs.next()) {
					productsList.add(extractProduct(rs));
				}
			} else if ("desc".equals(typeOfSort)) {
				stmt = con.prepareStatement(SQL_SORT_BY_CATEGORY_ID_PRODUCTS_DESC);
				stmt.setLong(1, categoryId);
				rs = stmt.executeQuery();
				while (rs.next()) {
					productsList.add(extractProduct(rs));
				}
			}

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return productsList;
	}

	public Product findProductById(long id, long langId) throws DBException {
		Product product = new Product();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(SQL_FIND_PRODUCT_BY_ID);
			stmt.setLong(1, id);
			stmt.setLong(2, langId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				product = (extractProduct(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return product;
	}

	/**
	 * Returns a user with the given login.
	 * 
	 * @param login User login.
	 * @return User entity.
	 * @throws DBException
	 */
	/*
	 * public User findUserByLogin(String login) throws DBException { User user =
	 * null; Statement pstmt = null; ResultSet rs = null; Connection con = null; try
	 * { con = getConnection(); pstmt = con.createStatement(); //pstmt.setString(1,
	 * login); rs = pstmt.executeQuery(SQL_FIND_USER_BY_LOGIN); if (rs.next()) {
	 * user = extractUser(rs); } con.commit(); } catch (SQLException ex) {
	 * rollback(con); throw new
	 * DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex); } finally {
	 * close(con, pstmt, rs); } return user; }
	 */

	public User findUserByLogin(String login) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	public User findUserByLoginEmail(String login, String email) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN_EMAIL);
			pstmt.setString(1, login);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	public boolean createUser(User user) throws SQLException, DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, user.getLogin());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getFirstName());
			pstmt.setString(4, user.getLastName());
			pstmt.setLong(5, user.getRoleId());
			pstmt.setLong(6, user.getStatusId());
			pstmt.setString(7, user.getEmail());

			pstmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con, pstmt, rs);
		}

		return false;
	}

	public boolean createOrder(CartBean cartBean, long userId) throws SQLException, DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		long orderId = 0;
		int statusId = 1;// registered status

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setLong(1, userId);
			pstmt.setInt(2, statusId);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				LOG.trace("Result Set: " + rs.toString());
				orderId = rs.getInt(1);
			}

			int j = 1;
			for (CartItemBean cartItem : cartBean.getCartItems()) {
				LOG.debug("Commands starts" + cartItem.getProductId());
				LOG.debug("Commands starts" + cartItem.getQuantity());

				pstmt = con.prepareStatement(SQL_INSERT_ORDER_DETAILS);

				pstmt.setLong(1, orderId);
				pstmt.setLong(2, cartItem.getProductId());
				pstmt.setLong(3, cartItem.getQuantity()); // registered status
				pstmt.executeUpdate();

			}

			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con, pstmt, rs);
		}
		return false;

	}

	public boolean createCategory(CategoryDTO categoryParams) throws SQLException, DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		long categoryId = 0;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, categoryParams.getSortOrder());

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				LOG.trace("Result Set: " + rs.toString());
				categoryId = rs.getInt(1);
			}
			pstmt = con.prepareStatement(SQL_INSERT_CATEGORIES_LOCALIZATION);
			pstmt.setLong(1, categoryId);
			pstmt.setLong(2, findLanguageByName("RU"));
			pstmt.setString(3, categoryParams.getRuCategoryTitle());
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_INSERT_CATEGORIES_LOCALIZATION);
			pstmt.setLong(1, categoryId);
			pstmt.setLong(2, findLanguageByName("EN"));
			pstmt.setString(3, categoryParams.getEnCategoryTitle());
			pstmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con, pstmt, rs);
		}
		return false;

	}

	public boolean createProduct(ProductDTO productParams) throws SQLException, DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		long productId = 0;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setLong(1, productParams.getCategoryId());
			pstmt.setInt(2, productParams.getQuantity());
			pstmt.setFloat(3, productParams.getPrice());
			pstmt.setString(4, productParams.getImg());
			pstmt.setString(5, productParams.getBrand());

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				LOG.trace("Result Set: " + rs.toString());
				productId = rs.getInt(1);
			}
			int i = 1;
			pstmt = con.prepareStatement(SQL_INSERT_PRODUCTS_LOCALIZATION);
			pstmt.setLong(1, productId);
			pstmt.setLong(2, findLanguageByName("RU"));
			pstmt.setString(3, productParams.getRuProductTitle());
			pstmt.setString(4, productParams.getRuProductDescription());
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(SQL_INSERT_PRODUCTS_LOCALIZATION);
			int j = 1;
			pstmt.setLong(1, productId);
			pstmt.setLong(2, findLanguageByName("EN"));
			pstmt.setString(3, productParams.getEnProductTitle());
			pstmt.setString(4, productParams.getEnProductDescription());
			pstmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con, pstmt, rs);
		}
		return false;

	}

	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws DBException
	 */
	public void updateUser(User user) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			updateUser(con, user);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
		}
	}

	public void updateUserStatusByUserId(int statusId, long userId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_USER_STATUS_BY_ID);

			pstmt.setLong(1, statusId);
			pstmt.setLong(2, userId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
			close(pstmt);
		}
	}

	public void updateOrderStatusByUserId(int statusId, long userId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_ORDER_STATUS_BY_ID);

			pstmt.setLong(1, statusId);
			pstmt.setLong(2, userId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
			close(pstmt);
		}
	}

	public void deleteUserById(long userId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_USER_BY_ID);
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			// throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
			close(pstmt);
		}
	}

	public void deleteOrderById(long orderId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_ORDER_BY_ID);
			pstmt.setLong(1, orderId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			// throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
			close(pstmt);
		}
	}

	public void deleteCategoryById(long categoryId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_CATEGORY_BY_ID);
			pstmt.setLong(1, categoryId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con);
			close(pstmt);
		}
	}

	public void deleteProductById(long categoryId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_DELETE_PRODUCT_BY_ID);
			pstmt.setLong(1, categoryId);
			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
		} finally {
			close(con);
			close(pstmt);
		}
	}

	// //////////////////////////////////////////////////////////
	// Entity access methods (for transactions)
	// //////////////////////////////////////////////////////////

	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws SQLException
	 */
	private void updateUser(Connection con, User user) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_UPDATE_USER);
			int k = 1;
			LOG.trace("Request parameter: login --> " + user.getLogin());
			LOG.trace("Request parameter: getFirstName --> " + user.getFirstName());
			LOG.trace("Request parameter: getLastName --> " + user.getLastName());
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getLogin());
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}

	// //////////////////////////////////////////////////////////
	// DB util methods
	// //////////////////////////////////////////////////////////

	/**
	 * Closes a connection.
	 * 
	 * @param con Connection to be closed.
	 */
	private void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
			}
		}
	}

	/**
	 * Closes a statement object.
	 */
	private void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
			}
		}
	}

	/**
	 * Closes a result set object.
	 */
	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
			}
		}
	}

	/**
	 * Closes resources.
	 */
	private void close(Connection con, Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(con);
	}

	/**
	 * Rollbacks a connection.
	 * 
	 * @param con Connection to be rollbacked.
	 */
	private void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}

	// //////////////////////////////////////////////////////////
	// Other methods
	// //////////////////////////////////////////////////////////
	/**
	 * Extracts a user order bean from the result set.
	 * 
	 * @param rs Result set from which a user order bean will be extracted.
	 * @return UserOrderBean object
	 */

	/**
	 * Extracts a user entity from the result set.
	 * 
	 * @param rs Result set from which a user entity will be extracted.
	 * @return User entity
	 */
	private User extractUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong(Fields.ENTITY_ID));
		user.setLogin(rs.getString(Fields.USER_LOGIN));
		user.setPassword(rs.getString(Fields.USER_PASSWORD));
		user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
		user.setLastName(rs.getString(Fields.USER_LAST_NAME));
		user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
		user.setStatusId(rs.getInt(Fields.USER_STATUS_ID));
		user.setEmail(rs.getString(Fields.USER_EMAIL));
		return user;
	}

	private UserForAdmin extractUserForAdmin(ResultSet rs) throws SQLException {
		UserForAdmin user = new UserForAdmin();
		user.setId(rs.getLong(Fields.ENTITY_ID));
		user.setLogin(rs.getString(Fields.USER_LOGIN));
		user.setPassword(rs.getString(Fields.USER_PASSWORD));
		user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
		user.setLastName(rs.getString(Fields.USER_LAST_NAME));
		user.setRole(rs.getString(Fields.USER_ROLE_FOR_ADMIN));
		user.setStatus(rs.getString(Fields.USER_STATUS_FOR_ADMIN));
		user.setDate(rs.getTimestamp(Fields.USER_DATE_FOR_ADMIN));
		user.setEmail(rs.getString(Fields.USER_EMAIL));
		return user;
	}

	private OrderDetails extractOrderDetails(ResultSet rs) throws SQLException {
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setId(rs.getLong(Fields.ENTITY_ID));
		orderDetails.setProductId(rs.getLong(Fields.ORDER_DETAILS_PRODUCT_ID));
		orderDetails.setOrderId(rs.getLong(Fields.ORDER_DETAILS_ORDER_ID));
		orderDetails.setProductBrand(rs.getString(Fields.ORDER_DETAILS_BRAND));
		orderDetails.setProductTitle(rs.getString(Fields.ORDER_DETAILS_TITLE));
		orderDetails.setQuantity(rs.getInt(Fields.ORDER_DETAILS_QUANTITY));
		orderDetails.setPrice(rs.getFloat(Fields.ORDER_DETAILS_PRICE));
		orderDetails.setTotal(rs.getDouble(Fields.ORDER_DETAILS_TOTAL));

		return orderDetails;
	}

	/**
	 * Extracts an order entity from the result set.
	 * 
	 * @param rs Result set from which an order entity will be extracted.
	 * @return
	 */
	private OrderBean extractOrder(ResultSet rs) throws SQLException {
		OrderBean order = new OrderBean();
		order.setId(rs.getLong(Fields.ENTITY_ID));
		order.setUserId(rs.getLong(Fields.ORDER_USER_ID));
		order.setLogin(rs.getString(Fields.ORDER_USER_LOGIN));
		order.setStatus(rs.getString(Fields.ORDER_STATUS));
		order.setDate(rs.getTimestamp(Fields.PRODUCT_DATE));
		order.setPrice(rs.getFloat(Fields.ORDER_PRICE));
		return order;
	}

	/**
	 * Extracts a category entity from the result set.
	 * 
	 * @param rs Result set from which a category entity will be extracted.
	 * @return Category entity.
	 */
	private Category extractCategory(ResultSet rs) throws SQLException {
		Category category = new Category();
		category.setId(rs.getLong(Fields.ENTITY_ID));
		category.setSortOrder(rs.getInt(Fields.CATEGORY_SORT_ORDER));
		category.setTitle(rs.getString(Fields.CATEGORY_TITLE));
		return category;
	}

	private Language extractLanguage(ResultSet rs) throws SQLException {
		Language lang = new Language();
		lang.setId(rs.getLong(Fields.ENTITY_ID));
		lang.setTitle(rs.getString(Fields.LANGUAGE_TITLE));
		return lang;
	}

	private Brand extractBrand(ResultSet rs) throws SQLException {
		Brand brand = new Brand();
		brand.setBrand(rs.getString(Fields.PRODUCT_BRAND));
		return brand;
	}

	private Product extractProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(rs.getLong(Fields.ENTITY_ID));
		product.setTitle(rs.getString(Fields.PRODUCT_TITLE));
		product.setPrice(rs.getFloat(Fields.PRODUCT_PRICE));
		product.setCategoryId(rs.getLong(Fields.PRODUCT_CATEGORY_ID));
		product.setQuantity(rs.getInt(Fields.PRODUCT_QUANTITY));
		product.setBrand(rs.getString(Fields.PRODUCT_BRAND));
		product.setDescription(rs.getString(Fields.PRODUCT_DESCRIPTION));
		product.setDate(rs.getTimestamp(Fields.PRODUCT_DATE));
		product.setImg(rs.getString(Fields.PRODUCT_IMG_LOCATION));
		return product;
	}

	/**
	 * Extracts a menu item from the result set.
	 * 
	 * @param rs Result set from which a menu item entity will be extracted.
	 * @return Menu item entity.
	 */
	private MenuItem extractMenuItem(ResultSet rs) throws SQLException {
		MenuItem menuItem = new MenuItem();
		menuItem.setId(rs.getLong(Fields.ENTITY_ID));
		menuItem.setName(rs.getString(Fields.MENU_ITEM_NAME));
		menuItem.setPrice(rs.getInt(Fields.MENU_ITEM_PRICE));
		menuItem.setCategoryId(rs.getLong(Fields.MENU_ITEM_CATEGORY_ID));
		return menuItem;
	}

	/**************** THIS METHOD IS NOT USED IN THE PROJECT *******/
	/**
	 * Returns a DB connection. This method is just for a example how to use the
	 * DriverManager to obtain a DB connection. It does not use a pool connections
	 * and not used in this project. It is preferable to use
	 * {@link #getConnection()} method instead.
	 * 
	 * @return A DB connection.
	 */
	public Connection getConnectionWithDriverManager() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			LOG.error("Cannot obtain a connection", ex);
		}
		Connection connection = DriverManager
				// .getConnection("jdbc:mysql://127.0.0.1:3306/st4db?user=root&password=");
				.getConnection(
						"jdbc:mysql://127.0.0.1:3306/st4db?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC&user=root&password=");
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		connection.setAutoCommit(false);
		return connection;
	}
	/**************************************************************/

}