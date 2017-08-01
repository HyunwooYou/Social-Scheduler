package com.example.json;

import org.json.JSONObject;

import lombok.Data;

/*
 * 카테고리별로 비율을 설정한다. 
 */
@Data
public class SocialData {

	JSONObject jsonResult;
	String writer; 

	int tourMax = 7;
	int restaurantMax = 7;
	int lodgeMax = 3;

	int denoTour = 1; // [관광지] 분모
	int denoRestaurant = 1; // [맛집] 분모
	int denoLodge = 1; // [숙박] 분모

	// 관광지 {"산","섬","바다","레포츠","역사","문화","축제"}
	int tour[] = { 0, 0, 0, 0, 0, 0, 0 };
	// 맛집 {"한식","중식","일식","양식","분식","베이커리","카페"}
	int restaurant[] = { 0, 0, 0, 0, 0, 0, 0 };
	// 숙박 {"호텔","리조트","펜션"}
	int lodge[] = { 0, 0, 0 };

	/*
	 * JSONObject 값을 설정한다.
	 */
	public void setJsonResult() {
		jsonResult = new JSONObject();

		denoTour = calDenoTour();
		System.out.println(denoTour);

		denoRestaurant = calDenoRestaurant();
		System.out.println(denoRestaurant);

		denoLodge = calDenoLodge();
		System.out.println(denoLodge);

		// [작성자]
		jsonResult.put("작성자", writer).toString();

		try {
			// [관광지]
			jsonResult.put("산", (Double.parseDouble(String.format("%.3f", (double) tour[0] / (double) denoTour))))
					.toString();
			jsonResult.put("섬", (Double.parseDouble(String.format("%.3f", (double) tour[1] / (double) denoTour))))
					.toString();
			jsonResult.put("바다", (Double.parseDouble(String.format("%.3f", (double) tour[2] / (double) denoTour))))
					.toString();
			jsonResult.put("레포츠", (Double.parseDouble(String.format("%.3f", (double) tour[3] / (double) denoTour))))
					.toString();
			jsonResult.put("역사", (Double.parseDouble(String.format("%.3f", (double) tour[4] / (double) denoTour))))
					.toString();
			jsonResult.put("문화", (Double.parseDouble(String.format("%.3f", (double) tour[5] / (double) denoTour))))
					.toString();
			jsonResult.put("축제", (Double.parseDouble(String.format("%.3f", (double) tour[6] / (double) denoTour))))
					.toString();
			// [맛집]
			jsonResult
					.put("한식",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[0] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("중식",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[1] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("일식",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[2] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("양식",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[3] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("분식",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[4] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("베이커리",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[5] / (double) denoRestaurant))))
					.toString();
			jsonResult
					.put("카페",
							(Double.parseDouble(
									String.format("%.3f", (double) restaurant[6] / (double) denoRestaurant))))
					.toString();
			// [맛집]
			jsonResult.put("호텔", (Double.parseDouble(String.format("%.3f", (double) lodge[0] / (double) denoLodge))))
					.toString();
			jsonResult.put("리조트", (Double.parseDouble(String.format("%.3f", (double) lodge[1] / (double) denoLodge))))
					.toString();
			jsonResult.put("펜션", (Double.parseDouble(String.format("%.3f", (double) lodge[2] / (double) denoLodge))))
					.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * 관광지 선호 비율을 구하기 위한 분모 값을 구한다.
	 */
	public int calDenoTour() {
		int result = 0;
		for (int i = 0; i < tourMax; i++) {
			result += tour[i];
		}
		return result;
	}

	/*
	 * 맛집 선호 비율을 구하기 위한 분모 값을 구한다.
	 */
	public int calDenoRestaurant() {
		int result = 0;
		for (int i = 0; i < restaurantMax; i++) {
			result += restaurant[i];
		}
		return result;
	}

	/*
	 * 숙박 선호 비율을 구하기 위한 분모 값을 구한다.
	 */
	public int calDenoLodge() {
		int result = 0;
		for (int i = 0; i < lodgeMax; i++) {
			result += lodge[i];
		}
		return result;
	}
}
