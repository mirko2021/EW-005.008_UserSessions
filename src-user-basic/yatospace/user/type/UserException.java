package yatospace.user.type;

import yatospace.user.object.User;

/**
 * Обиљежје изузетка да је ријеч о раду са корисницима. 
 * @author MV
 * @version 1.0
 */
public interface UserException {
	public User getUser(); 
}
