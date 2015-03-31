package mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// http://docs.mongodb.org/ecosystem/tutorial/getting-started-with-java-driver/
public class MongodbTest {
	
	public static void main(String[] args) {
	
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017)));
			
			DB db = mongoClient.getDB( "NEXT" );
			System.out.println("*** Connected to mongodb.");
			
			System.out.println("\n*** List of collections\n-----------------------");
			Set<String> colls = db.getCollectionNames();
			for (String s : colls) {
			    System.out.println(s);
			}
			
			// insert a movie
//			DBCollection movieColl = db.getCollection("movie");
//			BasicDBObject doc = new BasicDBObject("id", "3")
//	        .append("name", "Robocop")
//	        .append("genre", "SF");	        
//			movieColl.insert(doc);
//			System.out.println("\n*** Inserted movie: " + doc);
			
			// record number
			DBCollection movieColl = db.getCollection("movie");
			movieColl.createIndex(new BasicDBObject("id", 1));  // create index on "id", ascending
			System.out.println("\n*** Movie has " + movieColl.count() + " records.");
			
			DBObject obj = findDocumentById(movieColl, "1");
			BasicDBObject updateQuery = new BasicDBObject();
			updateQuery.append("$set", new BasicDBObject().append("genre", "SF"));
			movieColl.update(obj, updateQuery);
			System.out.println("*** Movie updated.");
			
			List<DBObject> movies = findDocumentsByProperty(movieColl, "genre", "SF");
			System.out.println("\n*** SF Movies\n-------------");
			for (DBObject m : movies) {
				System.out.println("-- " + m.get("name"));
			}
			
			// sort by rating and name properties
			List<DBObject> sortMovies = sortCollection(movieColl, "rating", false, "name", true);
			System.out.println("\n*** Movies sorted by Rating\n---------------------------");
			for (DBObject m : sortMovies) {
				System.out.println("-- " + m.get("rating") + " : " + m.get("name") +  " (" + m.get("genre") + ")");
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public static DBObject findDocumentById(DBCollection collection, String id) {
	    BasicDBObject query = new BasicDBObject();
	    query.put("id", id);
	    DBObject dbObj = collection.findOne(query);
	    return dbObj;
	}
	
	public static List<DBObject> findDocumentsByProperty(DBCollection collection, String propertyName, String propertyValue) {
		List<DBObject> result = new ArrayList<DBObject>();
	    BasicDBObject query = new BasicDBObject();
	    query.put(propertyName, propertyValue);
	    DBCursor dbCursor = collection.find(query);
	    while( dbCursor.hasNext() ) {
	        result.add(dbCursor.next());
	    }
	    return result;
	}
	
	public static List<DBObject> sortCollection(DBCollection collection, String propertyName, boolean ascending, String propertyName2, boolean ascending2) {	
		List<DBObject> result = new ArrayList<DBObject>();
		int asc = ascending ? 1 : -1;
		int asc2 = ascending2 ? 1 : -1; 
		// sort first by rating, second by name ascending
		BasicDBObject orderBy = new BasicDBObject(propertyName, asc).append(propertyName2, asc2);			
		DBCursor dbCursor = collection.find().sort(orderBy);
		while( dbCursor.hasNext() ) {
	        result.add(dbCursor.next());
	    }
	    return result;
	}
	

}