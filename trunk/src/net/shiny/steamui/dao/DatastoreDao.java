package net.shiny.steamui.dao;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Dao for reading and writing Entities
 * from/to the Google Datastore
 * @author shiny
 *
 */
public class DatastoreDao {
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static final Logger LOG = Logger.getLogger(DatastoreDao.class.getName());
	
	/**
	 * Persists the Entity in the datastore
	 * @param entity
	 */
	public static void persistEntity(Entity entity) {
		datastore.put(entity);
	}
	
	/**
	 * Retrieves the Entity from the datastore for a given type and name.
	 * Returns null if no Entity with given params is found in the datastore.
	 * @param type
	 * @param name
	 * @return
	 */
	public static Entity getEntity(String type, String name) {
		Key key = KeyFactory.createKey(type, name);
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			LOG.info("Entity not found in the datastore: " + type + " : " + name);
		}
		return null;
	}
	
	/**
	 * Creates a new Entity for a given type and name.
	 * @param type
	 * @param name
	 * @return
	 */
	public static Entity createEntity(String type, String name) {
		Key key = KeyFactory.createKey(type, name);
		return new Entity(key);
	}
}
