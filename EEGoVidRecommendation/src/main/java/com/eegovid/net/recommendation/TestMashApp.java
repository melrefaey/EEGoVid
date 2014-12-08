package com.eegovid.net.recommendation;

import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author Mohamed El-Refaey
 *
 */

public class TestMashApp {

	public static void main(String[] args) {

		try {

			String imageUrl = "http://upload.wikimedia.org/wikipedia/en/2/2d/Mashape_logo.png";

			HttpResponse<JsonNode> response = imageRequest(imageUrl);
			System.out.println(response.getBody());
			String token = response.getBody().getArray().getJSONObject(0).getString("token");
			System.out.println("token is " + token);

			String imageFeatures = imageFeatureResponse(token);
			System.out.println("The image Feature is: " + imageFeatures);

			Unirest.shutdown();

		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static HttpResponse<JsonNode> imageRequest(String imageUrl) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.post("https://camfind.p.mashape.com/image_requests")
				.header("X-Mashape-Key",
						"Put Your Key here")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.field("focus[x]", "480")
				.field("focus[y]", "640")
				.field("image_request[altitude]", "27.912109375")
				.field("image_request[language]", "en")
				.field("image_request[latitude]", "35.8714220766008")
				.field("image_request[locale]", "en_US")
				.field("image_request[longitude]", "14.3583203002251")
				.field("image_request[remote_image_url]",
						imageUrl)
				.asJson();
		return response;
	}

	public static String imageFeatureResponse(String token) throws UnirestException {
		//String token = "VC-pOWGX8A2f_17pm_Affg";
		String url = "https://camfind.p.mashape.com/image_responses/" + token;

		HttpResponse<JsonNode> response = Unirest
				.get(url)
				.header("X-Mashape-Key",
						"Put Your Key here")
				.asJson();

		System.out.println(response.getBody());
		String feature = response.getBody().getArray().getJSONObject(0).getString("name");

		return feature;
	}

}

