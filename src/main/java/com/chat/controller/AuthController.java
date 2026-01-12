package com.chat.controller;

import com.chat.model.User;
import com.chat.service.AuthService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthController extends HttpServlet {
    private AuthService authService = new AuthService();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        try {
            JsonObject input = gson.fromJson(req.getReader(), JsonObject.class);
            String username = input.get("username").getAsString();
            String password = input.get("password").getAsString();

            if ("/register".equals(path)) {
                User user = authService.register(username, password);
                if (user != null) {
                    JsonObject response = new JsonObject();
                    response.addProperty("id", user.getId());
                    response.addProperty("username", user.getUsername());
                    resp.getWriter().write(gson.toJson(response));
                } else {
                    resp.setStatus(400);
                    resp.getWriter().write("{\"error\":\"Registration failed\"}");
                }
            } else if ("/login".equals(path)) {
                User user = authService.login(username, password);
                if (user != null) {
                    JsonObject response = new JsonObject();
                    response.addProperty("id", user.getId());
                    response.addProperty("username", user.getUsername());
                    resp.getWriter().write(gson.toJson(response));
                } else {
                    resp.setStatus(401);
                    resp.getWriter().write("{\"error\":\"Invalid credentials\"}");
                }
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
