package com.outreach.interviews;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.outreach.interviews.map.builder.MapCoordinatesHelper;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class TestMapCoordinatesHelper 
{
	/*
	 *6 unit tests for different locations to get their coordinates
	 *3 passes and 3 expected to fail
	*/
	@Test
	//first unit test --> Pass 
	public void testMapCoordinatesHelperApiKey1() throws UnsupportedOperationException, IOException {
		//IOException if for example there was no internet
		new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("Ottawa") //finding coordinates for Ottawa
			.setRegion(MapRegions.en)
			.setURL(MapOperations.geocode)
			.build() //will request link from google API services
			.getLocation(); //actually will get the latitude and longitude by calling the method
	}
	@Test(expected = java.lang.UnsupportedOperationException.class) 
	//second unit test --> Fail
	//in setURL will be calling directions isntead of geocode, throws exception
	public void testMapCoordinatesHelperApiKey2() throws UnsupportedOperationException, IOException {
		List<String> coords= new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("Montreal") 
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions) //error in operation, should be type geocode
			.build() 
			.getLocation(); 

		assertNotNull(coords); //make sure that they're not null
	}
	@Test 
	//Third unit test --> Pass
	public void testMapCoordinatesHelperApiKey3() throws UnsupportedOperationException, IOException {
		List<String> coords= new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("Montreal") //finding coordinates for Montreal
			.setRegion(MapRegions.en)
			.setURL(MapOperations.geocode) 
			.build() 
			.getLocation(); 

		assertNotNull(coords); 
	}
	@Test
	//Fourth unit test --> Pass
	public void testMapCoordinatesHelperApiKey4() throws UnsupportedOperationException, IOException {
		List<String> coords= new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("Sudbury") //finding coordinates for Sudbury
			.setRegion(MapRegions.en)
			.setURL(MapOperations.geocode) 
			.build() 
			.getLocation(); 

		assertNotNull(coords); 
	}
	@Test (expected = java.lang.IllegalArgumentException.class)
	//5th unit test --> Fail
	//no destination specified in setAddress()
	public void testMapCoordinatesHelperApiKey5() throws UnsupportedOperationException, IOException {
		List<String> coords= new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("") //error here, no location specified 
			.setRegion(MapRegions.en)
			.setURL(MapOperations.geocode) 
			.build() 
			.getLocation(); 

		assertNotNull(coords); 
	}
	@Test (expected = java.lang.UnsupportedOperationException.class)
	//6th unit test --> Fail
	public void testMapCoordinatesHelperApiKey6() throws UnsupportedOperationException, IOException {
		List<String> coords= new MapCoordinatesHelper.CoordinatesBuilder()
			.setAddress("Hawkesbury") //set as integer instead of string, not a valid input
			.setRegion(MapRegions.en)
			.setURL(MapOperations.directions) 
			.build() 
			.getLocation(); 

		assertNotNull(coords); 
	}
}