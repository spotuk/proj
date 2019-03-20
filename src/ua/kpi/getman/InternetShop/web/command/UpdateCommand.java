package ua.kpi.getman.InternetShop.web.command;

import java.io.IOException;
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
public class UpdateCommand extends Command {

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
		// String email = request.getParameter("email");

		LOG.trace("Request parameter: login --> " + login);
		LOG.trace("Request parameter: userName --> " + userName);
		LOG.trace("Request parameter: userSername --> " + userSername);
		// LOG.trace("Request parameter: email --> " + email);

		//String password = request.getParameter("password");



		User user = new User();
		user.setLogin(login);
		user.setLastName(userSername);
		user.setFirstName(userName);
		//user.setPassword(password);
		manager.updateUser(user);

		String forward = Path.PAGE_MAIN;
		LOG.trace("User updated successfully redirect Main page ");
		LOG.debug("Command finished");
		return forward;
	}

}
