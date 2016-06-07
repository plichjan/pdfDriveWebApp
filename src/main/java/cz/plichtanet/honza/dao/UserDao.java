package cz.plichtanet.honza.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author jplichta
 */
@Component
public class UserDao implements IUserDao {
    private JdbcTemplate template;

//    @Autowired //TODO uncomment until get changes from notebook
    private PasswordEncoder passwordEncoder = null;

    @Autowired
    public UserDao(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void addRole(String user, String role) {
        template.update("insert into authorities (username, authority) values (?, ?)", user, role);
    }

    @Override
    public void changePassword(String user, String oldPassword, String password) {
        String hash = getPasswordHash(user);
        if (hash == null || !passwordEncoder.matches(oldPassword, hash)) {
            throw new BadCredentialsException("Old password is incorrect.");
        }
        template.update("update users set password = ? where username = ?", passwordEncoder.encode(password), user);
    }

    @Override
    public String getPasswordHash(String user) {
        return template.queryForObject("select password from users where username = ?", String.class, user);
    }
}
