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
import ua.kpi.getman.InternetShop.db.entity.User;
import ua.kpi.getman.InternetShop.exception.AppException;

/**
 * Login command.
 * 
 * @author Getman Valentine
 * 
 */
public class RegistrationCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();

		// obtain login and password from a request
		DBManager manager = DBManager.getInstance();
		String login = request.getParameter("login");
		String userName = request.getParameter("userName");
		String userSername = request.getParameter("userSername");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		LOG.trace("Request parameter: loging --> " + login);
		LOG.trace("Request parameter: userName --> " + userName);
		LOG.trace("Request parameter: userSername --> " + userSername);
		// LOG.trace("Request parameter: email --> " + email);

		
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			throw new AppException("Login/password cannot be empty");
		}

		if (manager.findUserByLoginEmail(login,email) == null) {
			User user = new User();
			user.setLogin(login);
			user.setLastName(userSername);
			user.setFirstName(userName);
			user.setPassword(password);
			user.setEmail(email);
			user.setStatusId(1);
			user.setRoleId(1);
			try {
				manager.createUser(user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		else {
			throw new AppException("This login is existing");
		}

		

		String forward = Path.PAGE_LOGIN;
		LOG.trace("User created successfully redirect Main page ");
		LOG.debug("Command finished");
		return forward;
	}

}
