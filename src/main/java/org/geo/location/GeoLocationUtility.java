package org.geo.location;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class GeoLocationUtility {

    public static void main(String[] args) {

        String[] locations = {"Madison, WI", "75454", "Chicago, IL"}; // you can also pass them from command line
        List<GeoResponse> responseList;

        for (String location : locations) {
            System.out.println("Fetching data for: " + location);
            try {
                GeoResponse locationData;
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
        // Removes spaces
        String formattedLocation = location.replaceAll("\\s", "");

        // Append ",US" at the end as we are limiting the location scope to be within the United States as per the question
        formattedLocation += ",US";

        List<GeoResponse> geoResponse = Collections.singletonList(new GeoResponse());

        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI(Constants.API_URL + "/direct?q=" + formattedLocation + "&appid=" + Constants.API_KEY))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            geoResponse = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, GeoResponse.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return geoResponse;
    }

    public static GeoResponse getLocationDataBasedOnZip(String zip) throws Exception {
        GeoResponse geoResponse = new GeoResponse();
        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI(Constants.API_URL + "/zip?zip=" + zip + "&appid=" + Constants.API_KEY))
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
