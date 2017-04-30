package com.sicnu.yudidi.service;

import java.io.IOException;
import java.io.Writer;
import java.rmi.server.UID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class RecommendationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String userId = req.getParameter("uid");
		Writer writer = resp.getWriter();
		writer.write(Recommendation.generateJson(userId));
	}
}
