package com.github.ioloolo.firestore;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

public class Main {

	@Getter
	@Setter
	@SuperBuilder
	@NoArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class Money extends Data {
		int money;
	}

	@SneakyThrows
	public static void main(String[] args) {
		init();

		Storage<Money> moneyStorage = Storage.of(Money.class);

		UUID uuid = UUID.randomUUID();

		// ###########################################################################

		moneyStorage.save(
				Money.builder()
						.id(uuid)
						.money(1000)
						.build()
		);

		// ###########################################################################

		Optional<Money> money = moneyStorage.get(uuid);
		System.out.println(money.orElseThrow());

		// ###########################################################################

		moneyStorage.update(uuid, prev -> {
			prev.setMoney(prev.getMoney() + 1000);
		});

		System.out.println(moneyStorage.get(uuid).orElseThrow());

		// ###########################################################################

		moneyStorage.delete(uuid);
	}

	@SneakyThrows
	private static void init() {
		InputStream serviceAccount = new FileInputStream("YOUR_FIREBASE_ADMIN_SDK.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(credentials)
				.build();

		FirebaseApp.initializeApp(options);
	}
}
