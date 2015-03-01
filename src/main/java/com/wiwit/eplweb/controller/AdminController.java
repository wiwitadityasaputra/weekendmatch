package com.wiwit.eplweb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.UserService;
import com.wiwit.eplweb.service.UserSessionService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class AdminController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);

	private UserService userService;
	private UserSessionService sessionService;
	
	@RequestMapping(value = ApiPath.ADMIN_SESSION, method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	public String removeSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("DELETE /api/admin/login/" + session);

		sessionService.deleteSession(session);

		return "ok";
	}

	@RequestMapping(value = ApiPath.ADMIN_SESSION, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public SimpleResult checkSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/admin/login/" + session);

		UserSession us = sessionService.findBySession(session);

		if (us == null)
			throw404();

		return SimpleResult.generateResult(us);
	}

	@RequestMapping(value = ApiPath.ADMIN_LOGIN, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SimpleResult createSession(HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("POST /api/admin/login");

		String adminEmailEncode = request.getParameter("adminEmailEncode");
		String adminPaswdEncode = request.getParameter("adminPaswdEncode");

		logger.info("adminEmailEncode : " + adminEmailEncode);
		logger.info("adminPaswdEncode : " + adminPaswdEncode);

		if (adminEmailEncode == null || adminEmailEncode.isEmpty()
				|| adminPaswdEncode == null || adminPaswdEncode.isEmpty()) {
			throw404();
		}
		byte[] decodedBytes = Base64.decodeBase64(adminEmailEncode);
		String decodedEmail = new String(decodedBytes);

		User u = userService.findUserByEmail(decodedEmail);

		if (u == null)
			throw404();

		if (u.getPassword().equals(adminPaswdEncode)) {
			UserSession session = sessionService.doLogin(u);
			return SimpleResult.generateResult(session);
		}

		throw404();
		return null;
	}

	@Autowired(required = true)
	public void setSessionService(UserSessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Autowired(required = true)
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
