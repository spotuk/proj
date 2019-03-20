package ua.kpi.getman.InternetShop.web.command;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Holder for all commands.<br/>
 * 
 * @author Getman Valentine
 * 
 */
public class CommandContainer {
	
	private static final Logger LOG = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<>();
	
	static {
		// common commands
		commands.put("login", new LoginCommand());
		commands.put("registration", new RegistrationCommand());
		commands.put("category", new CategoryCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("product", new ProductCommand());
		commands.put("cart", new CartCommand());
		commands.put("cabinet", new CabinetCommand());
		
		
		
		
		commands.put("viewSettings", new ViewSettingsCommand());
		commands.put("noCommand", new NoCommand());
		
		// client commands

		commands.put("update", new UpdateCommand());
		
		// admin commands
		commands.put("order", new OrderCommand());
		
		LOG.debug("Command container was successfully initialized");
		LOG.trace("Number of commands --> " + commands.size());
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}