package org.geo.location;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeoLocationUtility {

    public static void main(String[] args) {

        List<String> locations = new ArrayList<>();

        // Add all command-line arguments to the List
        for (String arg : args) {
            locations.add(arg);
        }
        GeoResponse locationData;
        List<GeoResponse> responseList;

        for (String location : locations) {
            System.out.println("Fetching data for: " + location);
            try {

                if (isValidDouble(location)) {
                    locationData = getLocationDataBasedOnZip(location);
                }
                else{
                    responseList = getLocationDataBasedOnLocation(location);
                    locationData = responseList.get(0);
                }
                if (locationData != null) {
                    System.out.println("Place Name: " + locationData.getName());
                    System.out.println("Latitude: " + locationData.getLat());
                    System.out.println("Longitude: " + locationData.getLon());
                }
            } catch (Exception e) {
                System.out.println("Error fetching data for " + location + ": " + e.getMessage());
            }
        }


    }

    public static List<GeoResponse> getLocationDataBasedOnLocation(String location) throws Exception {
        // Check if location is null or empty
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        // Removes spaces
        String formattedLocation = location.replaceAll("\\s", "");

        // Append ",US" at the end as we are limiting the location scope to be within the United States as per the question
        formattedLocation += ",US";

        List<GeoResponse> geoResponse = Collections.singletonList(new GeoResponse());

        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI(LoadConfiguration.getApiUrl() + "/direct?q=" + formattedLocation + "&appid=" + LoadConfiguration.getApiKey()))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            geoResponse = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, GeoResponse.class));

        } catch (Exception e) {
            e.printStackTrace();

        }
        return geoResponse;
    }


    public static GeoResponse getLocationDataBasedOnZip(String zip) throws Exception {
        // Check if zip is null or empty
        if (zip == null || zip.isEmpty()) {
            throw new IllegalArgumentException("zip cannot be null or empty");
        }
        GeoResponse geoResponse = new GeoResponse();
        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI(LoadConfiguration.getApiUrl() + "/zip?zip=" + zip + "&appid=" + LoadConfiguration.getApiKey()))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            geoResponse = objectMapper.readValue(response.body(), GeoResponse.class);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return geoResponse;
    }

    public static boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
