package cz.plichtanet.honza.dao;

/**
 * @author jplichta
 */
public interface IUserDao {
    void logDownload(String user, String uri);

    void addRole(String user, String role);

    void changePassword(String user, String oldPassword, String password);

    void addUser(String user, String password);

    String getPasswordHash(String user);
}
