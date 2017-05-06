package com.sicnu.yudidi.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class RecommendationTest {

	@Test
	public void testCheckExistance_case1() {
		boolean exist = Recommendation.checkExistance("1");
		assertEquals(false, exist);
	}

	@Test
	public void testCheckExistance_case2() {
		boolean exist = Recommendation.checkExistance("3619258");
		assertEquals(true, exist);
	}

	@Test
	public void testExtractSubjectId(){
		String url = "https://www.nowcoder.com/profile/2736253/codeBookDetail?submissionId=11305074";
		String expected = "9ae56e5bdf4f480387df781671db5172";
		assertEquals(expected, Recommendation.extractSubjectId(url));
	}
	
	@Test
	public void testGetSubjectIdsOfOnePage() {
		String url = "https://www.nowcoder.com/profile/3619258/codeBooks?onlyAcc=0&page=2";
		Set<String> subjectIds = Recommendation.getSubjectIdsOfOnePage(url);
		System.out.println(subjectIds);
		assertEquals(5, subjectIds.size());
	}

	@Test
	public void testGetPassedSubjectIdList() {
		List<String> passedSubjectUrls = Recommendation.getPassedSubjectIdList("3619258");
		assertEquals(21, passedSubjectUrls.size());
	}
	
	@Test
	public void testGenerateJsonByRecommendSubject() {
		List<String> recommendedList = Arrays.asList(new String[]{"e3b2cc44aa9b4851bdca89dd79c53150"});
		String json = Recommendation.generateJsonByRecommendSubject(recommendedList);
		System.out.println(json);
	}

	@Test
	public void testGetRecommendedSubjectIdsList() {
		List<String> recommendedList = Recommendation.getRecommendedSubjectIdsList("3619258");
		System.out.println(recommendedList);
		assertEquals(3, recommendedList.size());
	}
	
	@Test // error user-id
	public void testGenerateJson_case1() {
		String json = Recommendation.generateJson("");
		System.out.println(json);
		assertEquals("{msg:\"not found\"}", json);
	}
	
	@Test // right user-id, but not enough subject <10 
	public void testGenerateJson_case2() {
		String json = Recommendation.generateJson("5933350");
		System.out.println(json);
		assertEquals("{msg:\"not enough\",min_answered:10}", json);
	}
	
	@Test // normal user-id , and passed subjects > 10
	public void testGenerateJson_case3() {
		String json = Recommendation.generateJson("2736253");
		System.out.println(json);
		assertEquals("", json);
	}
	
	@Test
	public void testGenerateJson_case4() {
		String json = Recommendation.generateJson("");
		System.out.println(JSONObject.parse(json));
		assertEquals("{msg:\"not found\"}", json);
	}
	
}