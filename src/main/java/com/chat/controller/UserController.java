package com.chat.controller;

import com.chat.service.DatabaseService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/api/users")
public class UserController extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://postgres:5432/chatdb", "chatuser", "chatpass");
            PreparedStatement stmt = conn.prepareStatement("SELECT id, username FROM users");
            ResultSet rs = stmt.executeQuery();
            
            JsonArray users = new JsonArray();
            while (rs.next()) {
                JsonObject user = new JsonObject();
                user.addProperty("id", rs.getLong("id"));
                user.addProperty("username", rs.getString("username"));
                users.add(user);
            }
            
            resp.getWriter().write(gson.toJson(users));
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
