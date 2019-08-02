package com.outreach.interviews.map.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.outreach.interviews.map.enums.MapModes;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class MapCoordinatesHelper {

	public static class CoordinatesBuilder {

		private String address; 
		private MapRegions region;
		private MapOperations operation;
		private JsonObject result;

		private final String URL = "https://maps.googleapis.com/maps/api/"; //this is the URL needed to access API, find geocode
		private CloseableHttpClient httpclient = HttpClients.createDefault();

		/**
		 * Set the address point
		 * @param address String representing the address point
		 * @return {@link CoordinatesBuilder}
		 */
		public CoordinatesBuilder setAddress(String address) {
			this.address = address;
			return this;
		}

		/**
		 * Set the region {@link MapRegions}
		 * @param region Allows for en, es
		 * @return {@link CoordinatesBuilder}
		 */
		public CoordinatesBuilder setRegion(MapRegions region) {
			this.region = region;
			return this;
		}

		/**
		 * Create the URL to communicate with the Google Maps API
		 *this provides geocode isntead of directions
		 * @param type URL to provide to Apache HttpClient
		 * @return {@link RoutesBuilder}
		 */
		public CoordinatesBuilder setURL(MapOperations type) {
			//will not allow for directions in this case
			if(type.equals(MapOperations.directions)) 
				throw new UnsupportedOperationException();

			this.operation = type;
			return this;

		}

		/**
		 * Perform the HTTP request and retrieve the data from the HttpClient object
		 * @return {@link CoordinatesBuilder}  
		 * @throws UnsupportedOperationException Thrown to indicate that the requested operation is not supported.
		 * @throws IOException Thrown to indicate that the requested operation is not supported.
		 * @throws IllegalArgumentException Thrown to indicate that a method has been passed an illegal orinappropriate argument.
		 */
		public CoordinatesBuilder build() throws UnsupportedOperationException, IOException, IllegalArgumentException {
			//the url needs and address and the API key, both of which can be referenced from private access methods below
			String requestURL = this.getURL()  	+ "address=" + this.getAddress() 
												+ "&key=" + this.getAPIKey();
			
			HttpGet httpGet = new HttpGet(requestURL);
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			try {
				HttpEntity entity = response.getEntity();
				String result = IOUtils.toString(entity.getContent(), "UTF-8");
				this.result = new JsonParser().parse(result).getAsJsonObject();
			}
			finally {
				response.close();
			}
			
			return this;
		}
		/**
		 *Retrieve the coordinates (longitude and latitude) of the address from API
		 * @return List of Strings lat + lng 
		*/
		public List<String> getLocation() {
			if(this.operation.equals(MapOperations.geocode)) {
				List<String> list = new ArrayList<String>();
				//going through the JSON file, will access lan and lng here
				JsonObject coords = this.result.get("result").getAsJsonArray().get(0).getAsJsonObject()
					.get("geometry").getAsJsonObject()
					.get("location").getAsJsonObject();
				

				//only 2 elements to add to the list, latitude and longitude
				JsonObject lat = (JsonObject) coords.get("lat");
				list.add(lat.getAsString()); //adding latitude to the list
				JsonObject lng = (JsonObject) coords.get("lng");
				list.add(lng.getAsString()); //adding longitude to the list

				return list;
			} else {
				//will not return directions
				throw new IllegalArgumentException("Does not support " + MapOperations.directions.name());
			}
		}

		//*************************For Internal Use Only***********************************//
		private final String getURL() {
			return this.URL + this.operation.name() + "/json?";
		}

		private final String getAPIKey() {
			return System.getenv("AIzaSyD8WtnBwxDPsHW7VwsxsBMie9uphGqQcSA");
		}
		
		
		private final String getAddress() {
			if(this.address == null)
				throw new IllegalArgumentException("Address cannot be empty");
			
			return this.address;
		}
		
		private final String getRegion() {
			if(this.address == null)
				throw new IllegalArgumentException("Region cannot be empty");
			
			return this.region.name();
		}
		
		
	}
}
