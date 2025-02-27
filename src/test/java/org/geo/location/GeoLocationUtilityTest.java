package org.geo.location;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeoLocationUtilityTest {


    @Test
    public void testGetLocationDataBasedOnLocation_EmptyLocation() throws Exception {
        // Test for empty city
        String location = "";

        assertThrows(IllegalArgumentException.class, () -> {
            GeoLocationUtility.getLocationDataBasedOnLocation(location);
        });
    }

    @Test
    public void testGetLocationDataBasedOnLocation_EmptyZip() throws Exception {
        // Test for empty zip
        String zip = "";

        assertThrows(IllegalArgumentException.class, () -> {
            GeoLocationUtility.getLocationDataBasedOnZip(zip);
        });
    }

    @Test
    public void testGetLocationDataBasedOnLocation_ValidLocation() throws Exception {
        // Test for valid city
        String location = "Madison";
        List<GeoResponse> geoResponse = GeoLocationUtility.getLocationDataBasedOnLocation(location);

        // Check if response is not empty
        assertNotNull(geoResponse);
        assertFalse(geoResponse.isEmpty());
        // Verify that the data contains the correct city
        assertEquals("Madison", geoResponse.get(0).getName());
        // Validate latitude and longitude are not null
        assertNotNull(geoResponse.get(0).getLat());
        assertNotNull(geoResponse.get(0).getLon());
    }

    @Test
    public void testGetLocationDataBasedOnZip_ValidZip() throws Exception {
        // Test for valid zip code
        String zip = "94040";
        GeoResponse geoResponse = GeoLocationUtility.getLocationDataBasedOnZip(zip);
        // Check if response is not null
        assertNotNull(geoResponse);
        // Verify that city name is correct
        assertEquals("Mountain View", geoResponse.getName());
        // Validate latitude and longitude are valid
        assertNotNull(geoResponse.getLat());
        assertNotNull(geoResponse.getLon());
    }

    @Test
    public void testGetLocationDataBasedOnLocation_InvalidLocation() throws Exception {
        // Test for invalid location
        String invalidLocation = "InvalidLocationXYZ123";
        List<GeoResponse> geoResponse = GeoLocationUtility.getLocationDataBasedOnLocation(invalidLocation);
        // Ensure the response is empty
        assertTrue(geoResponse.isEmpty());
    }

    @Test
    public void testGetLocationDataBasedOnZip_InvalidZip() throws Exception {
        // Test for invalid zip code
        String invalidZip = "00000"; // an invalid zip code that doesn't exist
        GeoResponse geoResponse = GeoLocationUtility.getLocationDataBasedOnZip(invalidZip);
        // Ensure name is null
        assertNull(geoResponse.getName());
    }

    @Test
    public void testGetLocationDataBasedOnZip_InvalidSpecialCharacterZip() throws Exception {
        // Test for invalid zip code which throws URISyntaxException with invalid URL
        String invalidZip = "7%0@1#"; // an invalid zip code that doesn't exist
        assertThrows(URISyntaxException.class, () -> {
            GeoLocationUtility.getLocationDataBasedOnZip(invalidZip);

        });
    }

    @Test
    public void testGetLocationDataBasedOnZip_TwoZipCodesCombined() throws Exception {
        // Test for valid zip code but will give two zipcodes combined using & symbol, and it will return the first zip values
        String validCombinedTwoZip = "75454&75006";
        GeoResponse geoResponse = GeoLocationUtility.getLocationDataBasedOnZip(validCombinedTwoZip);
        assertNotNull(geoResponse);
        // Verify data contains the correct city
        assertEquals("Melissa", geoResponse.getName());
        // Validate latitude and longitude are not null
        assertNotNull(geoResponse.getLat());
        assertNotNull(geoResponse.getLon());
    }

    @Test
    public void testGetLocationDataBasedOnLocation_InvalidWithMaliciousSQL() throws Exception {
        // Test for invalid location with malicious SQL
        String location = "'; DROP TABLE users; --";
        List<GeoResponse> geoResponse = GeoLocationUtility.getLocationDataBasedOnLocation(location);
        assertTrue(geoResponse.isEmpty());
    }

    @Test
    public void testGetLocationDataBasedOnLocation_WithURLManipulation() throws Exception {
        // Test with URL manipulation such as appending extra query parameters
        String location = "Los Angeles?foo=bar";
        List<GeoResponse> geoResponse = GeoLocationUtility.getLocationDataBasedOnLocation(location);
        assertTrue(geoResponse.isEmpty());
    }

    @Test
    public void testGetLocationDataBasedOnLocation_WithNullLocation() {
        // Test null location
        String location = null;
        assertThrows(IllegalArgumentException.class, () -> {
            GeoLocationUtility.getLocationDataBasedOnLocation(location);
        });
    }

    @Test
    public void testGetLocationDataBasedOnLocation_WithLongInput() throws Exception {
        // Test with a long location string
        String location = "A".repeat(1000);  // 1000 characters
        List<GeoResponse> geoResponse = GeoLocationUtility.getLocationDataBasedOnLocation(location);
        assertTrue(geoResponse.isEmpty());
    }

    @Test
    public void testGetLocationDataBasedOnZip_WithNullZip() {
        // Test null zip
        String zip = null;
        assertThrows(IllegalArgumentException.class, () -> {
            GeoLocationUtility.getLocationDataBasedOnZip(zip);
        });
    }



}
