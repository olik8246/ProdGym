package com.example.ProdGym;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Shops {
        private double discount = 1.0;

        public ReplyKeyboardMarkup getShopKeyboard() {
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setSelective(true);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setOneTimeKeyboard(false);

                List<KeyboardRow> keyboardRows = new ArrayList<>();

                KeyboardRow row1 = new KeyboardRow();
                row1.add("Шейкери");
                row1.add("Пояси");

                KeyboardRow row2 = new KeyboardRow();
                row2.add("Ризинки");

                KeyboardRow row3 = new KeyboardRow();
                row3.add("Назад");

                keyboardRows.add(row1);
                keyboardRows.add(row2);
                keyboardRows.add(row3);

                replyKeyboardMarkup.setKeyboard(keyboardRows);
                return replyKeyboardMarkup;
        }

        public void setDiscount(double discount) {
                this.discount = discount;
        }

        public String applyDiscount(double price) {
                return String.format("%.2f", price * discount);
        }
}
