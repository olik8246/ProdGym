package com.example.ProdGym;

import java.sql.*;
public class ReviewsDAO {
    private final String url = "jdbc:mysql://localhost:3306/reviews";
    private final String user = "root";
    private final String password = "password";


    public void saveReview(String name, String review) {
        String query = "INSERT INTO reviews (name, review) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, review);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAllReviews() {
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT name, review FROM reviews";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String review = rs.getString("review");
                stringBuilder.append("Name: ").append(name).append("\nReview: ").append(review).append("\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}