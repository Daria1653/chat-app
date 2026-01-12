package com.chat.service;

import com.chat.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private DatabaseService db = new DatabaseService();

    public User register(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return db.createUser(username, hashed);
    }

    public User login(String username, String password) {
        User user = db.getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
