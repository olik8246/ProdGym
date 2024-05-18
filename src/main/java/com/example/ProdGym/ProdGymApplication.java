package com.example.ProdGym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ProdGymApplication extends TelegramLongPollingBot {

	public static void main(String[] args) {
		SpringApplication.run(ProdGymApplication.class, args);
	}

	@Override
	public String getBotUsername() {
		return "ProdGym_bot";
	}

	@Override
	public String getBotToken() {
		return "7062085182:AAH1nuue3IhuF26X_s_VlzlfYXjGG5vMG-8";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Reviews reviews = new Reviews();
		Shops shops = new Shops();
		if (update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();
			String messageText = message.getText();
			long chatId = message.getChatId();

			SendMessage sendMessage = new SendMessage();
			sendMessage.enableMarkdown(true);
			sendMessage.setChatId(String.valueOf(chatId));

			switch (messageText) {
				case "/start":
					sendMessage.setText("куку\n");
					sendMessage.setReplyMarkup(getMenuKeyboard());
					break;
				case "Tренери":
					sendMessage.setText("ckoro");
					break;
				case "Профіль":
					sendMessage.setText("skoro");
					break;
				case "Акції":
					sendMessage.setText("skoro");
					break;
				case "Магазин":
					sendMessage.setText("Виберіть товар:");
					sendMessage.setReplyMarkup(shops.getShopKeyboard());
					break;
				case "Тех-підтримка":
					sendMessage.setText("skoro");
					break;
				case "Відгуки":
					sendMessage.setText("Виберіть опцію відгуків:");
					sendMessage.setReplyMarkup(reviews.getReviewsMenu());
					break;
				case "Вихід":
					sendMessage.setText("До побачення!");
					break;
				case "Назад":
					sendMessage.setText("Повернення до головного меню:");
					sendMessage.setReplyMarkup(getMenuKeyboard());
					break;
				case "Шейкери":
					sendProductPhoto(chatId, "https://content.rozetka.com.ua/goods/images/big/116602159.jpg", "Ціна: 150 грн");
					return; // Вихід з методу, так як вже відправили повідомлення
				case "Пояси":
					sendProductPhoto(chatId, "https://sport90-60-90.com/image/cache/catalog/image/catalog/silovye-trenazhery/pojasa-atleticheskie/7/upload-iblock-fcd-fcd4de95e4b7deec77537ea9a7138d4c.webp", "Ціна: 300 грн");
					return; // Вихід з методу, так як вже відправили повідомлення
				case "Ризинки":
					sendProductPhoto(chatId, "https://troli.shop/image/cache/catalog/product/troli/999999999999YKF8oeG4I9D1-1100x1100.jpg", "Ціна: 50 грн");
					return; // Вихід з методу, так як вже відправили повідомлення

				default:
					sendMessage.setText("Невідома команда. Спробуйте ще раз.");
					break;
			}

			try {
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
	private void sendProductPhoto(long chatId, String photoUrl, String caption) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(String.valueOf(chatId));
		sendPhoto.setPhoto(new InputFile(photoUrl));
		sendPhoto.setCaption(caption);

		try {
			execute(sendPhoto);
		} catch (TelegramApiException e) {
			e.printStackTrace();
			SendMessage errorMessage = new SendMessage();
			errorMessage.setChatId(String.valueOf(chatId));
			errorMessage.setText("шото не то");
			try {
				execute(errorMessage);
			} catch (TelegramApiException ex) {
				ex.printStackTrace();
			}
		}
	}

	private ReplyKeyboardMarkup getMenuKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboardRows = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add("Tренери");
		row1.add("Профіль");

		KeyboardRow row2 = new KeyboardRow();
		row2.add("Акції");
		row2.add("Магазин");

		KeyboardRow row3 = new KeyboardRow();
		row3.add("Тех-підтримка");
		row3.add("Відгуки");

		KeyboardRow row4 = new KeyboardRow();
		row4.add("Вихід");

		keyboardRows.add(row1);
		keyboardRows.add(row2);
		keyboardRows.add(row3);
		keyboardRows.add(row4);

		replyKeyboardMarkup.setKeyboard(keyboardRows);
		return replyKeyboardMarkup;
	}


}