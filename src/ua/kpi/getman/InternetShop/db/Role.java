package ua.kpi.getman.InternetShop.db;

import ua.kpi.getman.InternetShop.db.entity.User;

/**
 * Role entity.
 * 
 * @authorGetman Valentine
 * 
 */

public enum Role {
	ADMIN, CLIENT;
	
	public static Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
}
