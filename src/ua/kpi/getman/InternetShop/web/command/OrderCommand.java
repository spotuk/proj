package ua.kpi.getman.InternetShop.web.command;

import java.io.IOException;
import java.sql.SQLException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.kpi.getman.InternetShop.Path;
import ua.kpi.getman.InternetShop.bean.CartBean;
import ua.kpi.getman.InternetShop.db.DBManager;
import ua.kpi.getman.InternetShop.exception.AppException;
import ua.kpi.getman.InternetShop.exception.DBException;

/**
 * Lists orders.
 * 
 * @author D.Kolesnikov
 * 
 */
public class OrderCommand extends Command {

	private static final long serialVersionUID = 1863978254689586513L;

	private static final Logger LOG = Logger.getLogger(OrderCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Commands starts");
		CategoryCommand.languages(request);

		String forward = null;

		String strAction = request.getParameter("action");
		try {
			switch (strAction) {
			case "orderList":
				orderListByUserId(request);
				forward = Path.PAGE_ORDERS;
				break;
			case "addOrder":
				addOrder(request);
				forward = Path.PAGE_CABINET;

				break;
			case "orderDetails":
				orderDetails(request);
				forward = Path.PAGE_ORDER_DETAILS;
				break;
			case "allOrders":
				allOrders(request);
				forward = Path.PAGE_ORDERS;
				break;
			case "deleteOrder":
				deleteOrderById(request);
				forward = Path.PAGE_CABINET;
				break;
			case "updateOrderStatus":
				updateOrderStatusById(request);
				forward = Path.PAGE_CABINET;
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return forward;
	}

	protected void addOrder(HttpServletRequest request) throws DBException, SQLException {
		String userId = request.getParameter("user_id");
		DBManager manager = DBManager.getInstance();

		HttpSession session = request.getSession();

		CartBean cartBean = null;

		Object objCartBean = session.getAttribute("cart");
		if (objCartBean != null) {
			cartBean = (CartBean) objCartBean;

			if ((userId != null) && !(userId.isEmpty())) {
				manager.createOrder(cartBean, Long.valueOf(userId));
				LOG.trace("Request parameter: user_id --> " + userId);

			}

		} else {
			cartBean = new CartBean();
		}
	}

	protected void orderListByUserId(HttpServletRequest request) throws DBException, SQLException {
		String userId = request.getParameter("user_id");

		LOG.debug(" userId " + userId);
		DBManager manager = DBManager.getInstance();

		request.setAttribute("orders", manager.findOrdersByUserId(Long.valueOf(userId)));

	}

	protected void deleteOrderById(HttpServletRequest request) throws DBException, SQLException {
		String orderId = request.getParameter("orderId");

		LOG.debug(" orderId " + orderId);
		DBManager manager = DBManager.getInstance();

		manager.deleteOrderById(Long.valueOf(orderId));

	}

	protected void updateOrderStatusById(HttpServletRequest request) throws DBException, SQLException {
		String orderId = request.getParameter("orderId");
		String statusId = request.getParameter("statusId");

		LOG.debug(" orderId " + orderId);
		LOG.debug(" statusId " + statusId);
		DBManager manager = DBManager.getInstance();
		if ((statusId != null) && !(statusId.isEmpty() && (orderId != null) && !(orderId.isEmpty()))) {
			manager.updateOrderStatusByUserId(Integer.valueOf(statusId), Long.valueOf(orderId));
		}
	}

	protected void allOrders(HttpServletRequest request) throws DBException, SQLException {

		DBManager manager = DBManager.getInstance();

		request.setAttribute("orders", manager.findAllOrders());

	}

	protected void orderDetails(HttpServletRequest request) throws DBException, SQLException {
		String orderId = request.getParameter("orderId");
		String priceTotal = request.getParameter("priceTotal");
		LOG.debug(" orderId " + orderId);
		DBManager manager = DBManager.getInstance();
		request.setAttribute("priceTotal", priceTotal);
		request.setAttribute("orderDetails",
				manager.findOrderDetailsByOrderId(Long.valueOf(orderId), CategoryCommand.localization(request)));

	}

}