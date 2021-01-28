package net.romvoid95.discord.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataManager<T> implements DataManager<T> {
	
	private static final ObjectMapper mapper = new ObjectMapper()
			.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true) // Allow newlines.
			.configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true);
	private final Path configPath;
	private final T data;

	public JsonDataManager(Class<T> clazz, String file, Supplier<T> constructor) {
		this.configPath = Paths.get(file);
		if (!configPath.toFile().exists() && !configPath.toFile().getName().equalsIgnoreCase("cache.json")) {
			try {
				if (configPath.toFile().createNewFile()) {
					FileIOUtils.write(configPath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(constructor.get()));
				} else {
					System.err.println(String.format("Could not create config file at %s", file));
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.exit(0);
		} else {
			try {
				if (configPath.toFile().createNewFile()) {
					FileIOUtils.write(configPath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(constructor.get()));
				} else {
					System.err.println(String.format("Could not create cache file at %s", file));
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		try {
			this.data = fromJson(FileIOUtils.read(configPath), clazz);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public T get() {
		return data;
	}

	@Override
	public void save() {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(configPath.toFile(), data);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <T> String toJson(T object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}

	public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
		return mapper.readValue(json, clazz);
	}

	public static <T> T fromJson(String json, TypeReference<T> type) throws JsonProcessingException {
		return mapper.readValue(json, type);
	}

	public static <T> T fromJson(String json, JavaType type) throws JsonProcessingException {
		return mapper.readValue(json, type);
	}
}
