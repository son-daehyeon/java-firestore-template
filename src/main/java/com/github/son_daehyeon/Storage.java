package com.github.son_daehyeon;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;

import lombok.SneakyThrows;

public final class Storage<T extends Data> {

	private static final Firestore db = FirestoreClient.getFirestore();

	private static final Map<Class<? extends Data>, Storage<? extends Data>> STORAGES = new HashMap<>();

	private static final Map<Class<? extends Data>, List<String>> FIELD_LIST_CACHE = new HashMap<>();

	private final Class<T> clazz;

	private Storage(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T extends Data> Storage<T> of(Class<T> clazz) {
		if (!STORAGES.containsKey(clazz)) {
			STORAGES.put(clazz, new Storage<>(clazz));
		}

		//noinspection unchecked
		return (Storage<T>) STORAGES.get(clazz);
	}

	@SneakyThrows({InterruptedException.class, ExecutionException.class})
	void save(T data) {
		if (!FIELD_LIST_CACHE.containsKey(clazz)) {
			FIELD_LIST_CACHE.put(clazz, Arrays.stream(data.getClass().getDeclaredFields()).map(Field::getName).toList());
		}

		db.collection(clazz.getSimpleName())
				.document(data.getId().toString())
				.set(data, SetOptions.mergeFields(FIELD_LIST_CACHE.get(clazz)))
				.get();
	}

	@SneakyThrows({InterruptedException.class, ExecutionException.class})
	Optional<T> get(UUID id) {
		T object = db.collection(clazz.getSimpleName())
				.document(id.toString())
				.get()
				.get()
				.toObject(clazz);

		if (object == null) {
			return Optional.empty();
		}

		object.setId(id);

		return Optional.of(object);
	}

	void update(UUID id, Consumer<T> query) {
		T data = get(id).orElseThrow(() -> new NoSuchElementException("ID: " + id));

		query.accept(data);

		save(data);
	}

	@SneakyThrows({InterruptedException.class, ExecutionException.class})
	void delete(UUID id) {
		get(id).orElseThrow(() -> new NoSuchElementException("ID: " + id));

		db.collection(clazz.getSimpleName())
				.document(id.toString())
				.delete()
				.get();
	}
}
