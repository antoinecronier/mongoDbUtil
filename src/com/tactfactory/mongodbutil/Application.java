package com.tactfactory.mongodbutil;

import com.mongodb.BasicDBObject;
import com.tactfactory.mongodbutil.mongo.MongoClientUtil;
import com.tactfactory.mongodbutil.network.NetworkUtil;

public class Application {

	public static void main(String[] args) {

		NetworkUtil networkUtil = new NetworkUtil();
		MongoClientUtil clientUtil = new MongoClientUtil();

		for (String string : networkUtil.reponseFromIpsForMongo(new byte[] {10,3,5},27017)) {
			clientUtil.addClient(string, 27017);
		}

		BasicDBObject request = new BasicDBObject();
		request.put("company", "MUSIX");
		//clientUtil.printAllFrom("contacts", "phones", request);

		MongoClientUtil.sendToFile(clientUtil.getAllFrom(null,null,null));

		System.out.println("Ended");
	}

}
