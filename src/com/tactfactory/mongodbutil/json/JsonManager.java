package com.tactfactory.mongodbutil.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonManager {
	private final String LOCAL_REPO = ".\\saves\\";
	private final String FILE_NAME = "default.test.json";

	private ObjectMapper mapper = new ObjectMapper();
	private ArrayList<Object> objects = new ArrayList<Object>();

	public JsonManager addItem(Object item) {
		this.objects.add(item);
		return this;
	}

	public JsonManager addItems(ArrayList<?> items) {
		this.objects.addAll(items);
		return this;
	}

	public JsonManager clear() {
		this.objects.clear();
		return this;
	}

	private JsonManager() {
		mapper.configure(
				org.codehaus.jackson.JsonGenerator.Feature.QUOTE_FIELD_NAMES,
				true);
	}

	private static JsonManager INSTANCE = new JsonManager();

	public static JsonManager getInstance() {
		return INSTANCE;
	}

	public String toJSON() {
		try {
			return mapper.writeValueAsString(this.objects);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendToFile() {
		this.sendToFile(this.FILE_NAME, this.LOCAL_REPO);
	}

	public void sendToFile(String filename) {
		this.sendToFile(filename, this.LOCAL_REPO);
	}

	public void sendToFile(String filename, String path) {
		File file = new File(path);

		if (!file.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = false;

			try {
				result = file.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}

			if (result) {
				System.out.println("DIR created");
			}
		}

		file = new File(path + "\\" + filename);

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, false))); // append mode file writer
			mapper.writeValue(out, objects);
			out.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File " + filename + " create in " + path);
	}

	public <T> ArrayList<T> readFromFile(Class<?> elem) {
		return this.<T> readFromFile(this.FILE_NAME, this.LOCAL_REPO, elem);
	}

	public <T> ArrayList<T> readFromFile(String filename, Class<?> elem) {
		return this.<T> readFromFile(filename, this.LOCAL_REPO, elem);
	}

	public <T> ArrayList<T> readFromFile(String filename, String path,
			Class<?> elem) {

		try {
			return mapper.readValue(
					new File(path + filename),
					mapper.getTypeFactory().constructCollectionType(
							ArrayList.class, elem));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
