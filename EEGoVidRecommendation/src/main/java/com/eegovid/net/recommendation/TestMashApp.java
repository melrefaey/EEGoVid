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

			HttpResponse<JsonNode> response = Unirest.post("https://camfind.p.mashape.com/image_requests")
			.header("X-Mashape-Key", "jutO7F3mggmshFo78N")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.field("focus[x]", "480")
			.field("focus[y]", "640")
			.field("image_request[altitude]", "27.912109375")
			.field("image_request[language]", "en")
			.field("image_request[latitude]", "35.8714220766008")
			.field("image_request[locale]", "en_US")
			.field("image_request[longitude]", "14.3583203002251")
			.field("image_request[remote_image_url]", "shttp://upload.wikimedia.org/wikipedia/en/2/2d/Mashape_logo.png")
			.asJson();


			System.out.println(response.getBody());
			System.out.println(response.getRawBody());
			System.out.println("testme again");
			Unirest.shutdown();
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
