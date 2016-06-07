package cz.plichtanet.honza.dao;

/**
 * @author jplichta
 */
public interface IUserDao {
    void addRole(String user, String role);

    void changePassword(String user, String oldPassword, String password);

    String getPasswordHash(String user);
}
