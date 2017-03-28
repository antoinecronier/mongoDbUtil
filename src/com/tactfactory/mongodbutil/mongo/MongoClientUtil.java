package com.tactfactory.mongodbutil.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tactfactory.mongodbutil.json.JsonManager;

public class MongoClientUtil {

	private ArrayList<MongoClient> clients;
	private Map<MongoClient, Map<MongoDatabase, ArrayList<MongoCollection<Document>>>> clientsDbs;

	public Map<MongoClient, Map<MongoDatabase, ArrayList<MongoCollection<Document>>>> getClientsDbs() {
		return this.clientsDbs;
	}

	public ArrayList<MongoClient> getClients() {
		return this.clients;
	}

	public MongoClientUtil removeClient(MongoClient client) {
		this.clients.remove(client);
		this.clientsDbs.remove(client);
		return this;
	}

	public MongoClientUtil addClient(String address, Integer port) {
		MongoClient client = new MongoClient(address, port);
		this.clients.add(client);

		Map<MongoDatabase, ArrayList<MongoCollection<Document>>> mongoDatabases = new HashMap<MongoDatabase, ArrayList<MongoCollection<Document>>>();
		for (String mongoDatabaseName : client.listDatabaseNames()) {

			ArrayList<MongoCollection<Document>> dbCollections = new ArrayList<MongoCollection<Document>>();
			for (String mongoCollectionName : (client
					.getDatabase(mongoDatabaseName)).listCollectionNames()) {
				dbCollections.add(client.getDatabase(mongoDatabaseName)
						.getCollection(mongoCollectionName, Document.class));
			}

			mongoDatabases.put(client.getDatabase(mongoDatabaseName),
					dbCollections);
		}

		this.clientsDbs.put(client, mongoDatabases);

		return this;
	}

	public MongoClientUtil addDbOnClient(MongoClient client, String dbName,
			List<String> collections) {
		ArrayList<MongoCollection<Document>> mongoCollections = new ArrayList<MongoCollection<Document>>();
		for (String collectionName : collections) {
			client.getDatabase(dbName).createCollection(collectionName);
			mongoCollections.add(client.getDatabase(dbName).getCollection(
					collectionName, Document.class));
		}

		this.clientsDbs.get(client).put(client.getDatabase(dbName),
				mongoCollections);

		return this;
	}

	public MongoClientUtil() {
		clients = new ArrayList<MongoClient>();
		clientsDbs = new HashMap<MongoClient, Map<MongoDatabase, ArrayList<MongoCollection<Document>>>>();
	}

	public void printAll() {
		for (Map<MongoDatabase, ArrayList<MongoCollection<Document>>> baseMap : this
				.getClientsDbs().values()) {
			for (Entry<MongoDatabase, ArrayList<MongoCollection<Document>>> mapDb : baseMap
					.entrySet()) {
				for (MongoCollection<Document> collection : mapDb.getValue()) {
					for (Document results : collection.find()) {
						System.out.println(results.toJson());
					}
				}
			}
		}
	}

	public void printAllFrom(String databaseName) {
		BasicDBObject request = new BasicDBObject();
		printAllFrom(databaseName, null, request);
	}

	public void printAllFrom(String databaseName, String collectionName) {
		BasicDBObject request = new BasicDBObject();
		printAllFrom(databaseName, collectionName, request);
	}

	public void printAllFrom(String databaseName, String collectionName,
			BasicDBObject request) {
		for (Map<MongoDatabase, ArrayList<MongoCollection<Document>>> baseMap : this
				.getClientsDbs().values()) {
			for (Entry<MongoDatabase, ArrayList<MongoCollection<Document>>> mapDb : baseMap
					.entrySet()) {
				if (databaseName != null
						&& mapDb.getKey().getName().equals(databaseName)) {
					for (MongoCollection<Document> collection : mapDb
							.getValue()) {
						if (collectionName != null
								&& collection.getNamespace()
										.getCollectionName()
										.equals(collectionName)) {
							if (request != null) {
								for (Document results : collection
										.find(request)) {
									System.out.println(results.toJson());
								}
							} else {
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}
							}
						}
					}
				} else {
					for (MongoCollection<Document> collection : mapDb
							.getValue()) {
						if (collectionName != null
								&& collection.getNamespace()
										.getCollectionName()
										.equals(collectionName)) {
							if (request != null) {
								for (Document results : collection
										.find(request)) {
									System.out.println(results.toJson());
								}
							} else {
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}
							}
						} else {
							if (request != null) {
								for (Document results : collection
										.find(request)) {
									System.out.println(results.toJson());
								}
							} else {
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}
							}
						}
					}
				}
			}
		}
	}

	public Map<MongoClient, Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>> getAllFrom(
			String databaseName, String collectionName, BasicDBObject request) {
		Map<MongoClient, Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>> result = new HashMap<MongoClient, Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>>();

		for (Entry<MongoClient, Map<MongoDatabase, ArrayList<MongoCollection<Document>>>> baseMap : this
				.getClientsDbs().entrySet()) {
			for (Entry<MongoDatabase, ArrayList<MongoCollection<Document>>> mapDb : baseMap
					.getValue().entrySet()) {
				Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>> subResult = new HashMap<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>();

				if (databaseName != null
						&& mapDb.getKey().getName().equals(databaseName)) {
					for (MongoCollection<Document> collection : mapDb
							.getValue()) {
						Map<MongoCollection<Document>, ArrayList<Document>> subSubResult = new HashMap<MongoCollection<Document>, ArrayList<Document>>();

						if (collectionName != null
								&& collection.getNamespace()
										.getCollectionName()
										.equals(collectionName)) {
							if (request != null) {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection
										.find(request)) {
									documents.add(results);
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							} else {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							}
						}
					}
				} else {
					for (MongoCollection<Document> collection : mapDb
							.getValue()) {
						Map<MongoCollection<Document>, ArrayList<Document>> subSubResult = new HashMap<MongoCollection<Document>, ArrayList<Document>>();

						if (collectionName != null
								&& collection.getNamespace()
										.getCollectionName()
										.equals(collectionName)) {
							if (request != null) {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection
										.find(request)) {
									System.out.println(results.toJson());
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							} else {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							}
						} else {
							if (request != null) {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection
										.find(request)) {
									System.out.println(results.toJson());
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							} else {
								ArrayList<Document> documents = new ArrayList<Document>();
								for (Document results : collection.find()) {
									System.out.println(results.toJson());
								}

								subSubResult.put(collection, documents);
								subResult.put(mapDb.getKey(), subSubResult);
								result.put(baseMap.getKey(), subResult);
							}
						}
					}
				}
			}
		}
		return result;
	}

	public static void sendToFile(
			Map<MongoClient, Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>> getResult) {
		for (Entry<MongoClient, Map<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>>> client : getResult
				.entrySet()) {
			for (Entry<MongoDatabase, Map<MongoCollection<Document>, ArrayList<Document>>> database : client
					.getValue().entrySet()) {
				for (Entry<MongoCollection<Document>, ArrayList<Document>> collection : database
						.getValue().entrySet()) {
					for (Document singleValue : collection.getValue()) {
						JsonManager.getInstance().addItem(singleValue.toJson());
					}
					JsonManager.getInstance().sendToFile(
							collection.getKey().getNamespace()
									.getCollectionName(),
							".\\mongoDump\\" + client.getKey().getAddress().getHost()
									+ "\\"
									+ client.getKey().getAddress().getPort()
									+ "\\" + database.getKey().getName());
					JsonManager.getInstance().clear();
				}
			}
		}
	}
}
