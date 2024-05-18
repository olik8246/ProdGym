package com.example.ProdGym;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
    private ReviewsDAO reviewsDAO = new ReviewsDAO();
    private Long id;
    private Long chatId;
    private String name;
    private String review;
    ReplyKeyboardMarkup getReviewsMenu() {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> keyboardRows = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add("Написати відгук");
    row.add("Прочитати відгук");

    KeyboardRow backRow = new KeyboardRow();
    backRow.add("Назад");

    keyboardRows.add(row);
    keyboardRows.add(backRow);

    replyKeyboardMarkup.setKeyboard(keyboardRows);
    return replyKeyboardMarkup;
}
    public void saveReview(String name, String review) {
        reviewsDAO.saveReview(name, review);
    }

    public String getAllReviews() {
        return reviewsDAO.getAllReviews();
    }

}