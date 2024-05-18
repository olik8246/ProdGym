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
	private boolean isAwaitingPromoCode = false;
	private boolean isAwaitingName = false;
	private boolean isAwaitingReview = false;
	private String userName;



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
		Actions actions = new Actions();

		if (update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();
			String messageText = message.getText();
			long chatId = message.getChatId();

			SendMessage sendMessage = new SendMessage();
			sendMessage.enableMarkdown(true);
			sendMessage.setChatId(String.valueOf(chatId));

			if (isAwaitingPromoCode) {
				if (messageText.equalsIgnoreCase("20")) {
					shops.setDiscount(0.8); //  20% discount
					sendMessage.setText("Промокод прийнято! Ціни в магазині знижено на 20%.");
					isAwaitingPromoCode = false;
				} else {
					sendMessage.setText("Невірний промокод. Спробуйте ще раз або поверніться назад.");
				}
				sendMessage.setReplyMarkup(actions.getDiscountKeyboard());
			} else {
				switch (messageText) {
					case "/start":
						sendMessage.setText("куку\n");
						sendMessage.setReplyMarkup(getMenuKeyboard());
						break;
					case "Tренери":
						sendMessage.setText("скоро");
						break;
					case "Профіль":
						sendMessage.setText("скоро");
						break;
					case "Акції":
						sendMessage.setText("Введіть промокод для знижки:");
						sendMessage.setReplyMarkup(actions.getDiscountKeyboard());
						break;
					case "Ввести промокод":
						sendMessage.setText("Будь ласка, введіть ваш промокод:");
						isAwaitingPromoCode = true;
						break;
					case "Магазин":
						sendMessage.setText("Виберіть товар:");
						sendMessage.setReplyMarkup(shops.getShopKeyboard());
						break;
					case "Тех-підтримка":
						sendMessage.setText("Оберіть опцію тех-підтримки:");
						sendMessage.setReplyMarkup(getSupportKeyboard());
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
						sendProductPhoto(chatId, "https://content.rozetka.com.ua/goods/images/big/116602159.jpg", "Ціна: " + shops.applyDiscount(150) + " грн");
						return;
					case "Пояси":
						sendProductPhoto(chatId, "https://sport90-60-90.com/image/cache/catalog/image/catalog/silovye-trenazhery/pojasa-atleticheskie/7/upload-iblock-fcd-fcd4de95e4b7deec77537ea9a7138d4c.webp", "Ціна: " + shops.applyDiscount(300) + " грн");
						return;
					case "Ризинки":
						sendProductPhoto(chatId, "https://troli.shop/image/cache/catalog/product/troli/999999999999YKF8oeG4I9D1-1100x1100.jpg", "Ціна: " + shops.applyDiscount(50) + " грн");
						return;
					case "Контактні данні":
						sendMessage.setText("Телефон: +380 123 456 789\nЕлектронна пошта: support@gmail.com");
						break;
					case "Розташування":
						sendMessage.setText("Тут буде карта з розташуванням.");
						break;
					case "Написати відгук":
						sendMessage.setText("Будь ласка, введіть ваше ім'я:");
						isAwaitingName = true;
						break;
					case "Прочитати відгук":
						sendMessage.setText(reviews.getAllReviews());
						break;
					default:
						if (isAwaitingName) {
							userName = messageText;
							sendMessage.setText("Дякуємо! Будь ласка, напишіть свій відгук:");
							isAwaitingName = false;
							isAwaitingReview = true;
						} else if (isAwaitingReview) {
							String review = messageText;
							reviews.saveReview(userName, review);
							sendMessage.setText("Дякуємо за ваш відгук!");
							isAwaitingReview = false;
						} else {
							sendMessage.setText("Невідома команда. Спробуйте ще раз.");
						}
						break;
				}
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

	private ReplyKeyboardMarkup getSupportKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboardRows = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add("Контактні данні");
		row1.add("Розташування");

		KeyboardRow row2 = new KeyboardRow();
		row2.add("Назад");

		keyboardRows.add(row1);
		keyboardRows.add(row2);

		replyKeyboardMarkup.setKeyboard(keyboardRows);
		return replyKeyboardMarkup;
	}


}
