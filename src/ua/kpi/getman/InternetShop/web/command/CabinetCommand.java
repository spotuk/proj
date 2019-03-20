package ua.kpi.getman.InternetShop.web.command;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.kpi.getman.InternetShop.Path;
import ua.kpi.getman.InternetShop.db.DBManager;
import ua.kpi.getman.InternetShop.db.Role;
import ua.kpi.getman.InternetShop.db.entity.User;
import ua.kpi.getman.InternetShop.exception.AppException;
import ua.kpi.getman.InternetShop.exception.DBException;

public class CabinetCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		CategoryCommand.languages(request);
		 long langId = CategoryCommand.localization(request);
		
		
		String forward = Path.PAGE_ERROR_PAGE;

		String strAction = request.getParameter("action");
		switch (strAction) {
		case "categoryList":
			CategoryCommand.allCategories(request, langId);
			 forward = Path.PAGE_CATEGORIES;
			break;
			
		case "productsList":
			CategoryCommand.products(request, langId);
			 forward = Path.PAGE_PRODUCTS;
			break;
		case "usersList":
			usersList(request);
			 forward = Path.PAGE_USERS;
			break;
		case "deleteUser":
			deleteUser(request);
			 forward = Path.PAGE_ADMIN_PANEL;
			break;
		case "updateUser":
			updateUserStatus(request);
			 forward = Path.PAGE_ADMIN_PANEL;
			break;
		
		}

		LOG.debug("Command finished");
		return forward;
	}
	protected void usersList(HttpServletRequest request) throws DBException {
		DBManager manager = DBManager.getInstance();
		request.setAttribute("usersList", manager.findAllUsers());
	}
	protected void deleteUser(HttpServletRequest request) throws DBException {
		String userId = request.getParameter("userId");
		DBManager manager = DBManager.getInstance();
		if(manager.findUserById(Long.valueOf(userId))!=null) {
			manager.deleteUserById(Long.valueOf(userId));
		}
	}
	protected void updateUserStatus(HttpServletRequest request) throws DBException {
		String userId = request.getParameter("userId");
		String statusId = request.getParameter("statusId");
		DBManager manager = DBManager.getInstance();
		if((userId != null) && (statusId != null)) {
		 manager.updateUserStatusByUserId(Integer.valueOf(statusId), Long.valueOf(userId));
		}
	}

}