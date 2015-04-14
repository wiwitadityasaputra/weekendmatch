package com.wiwit.eplweb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.filter.CustomFilter;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.input.CheckUserModelInput;
import com.wiwit.eplweb.model.input.CheckUsernameModelInput;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.service.UserNetworkService;
import com.wiwit.eplweb.service.UserService;
import com.wiwit.eplweb.service.UserSessionService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.RestResult;
import com.wiwit.eplweb.util.UserNetworkType;

@RestController
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserNetworkService userNetworkService;
	@Autowired
	private UserSessionService sessionService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = ApiPath.USER_IS_REGISTERED_USER, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<RestResult> isUserExist(@RequestBody CheckUserModelInput model) {
		HttpStatus httpStatus;
		RestResult result;
		
		if (model == null || 
			model.getNetworkType() == null || model.getNetworkType().isEmpty() || 
			model.getEmail() == null || model.getEmail().isEmpty()) {
			
			httpStatus =  HttpStatus.BAD_REQUEST;
			result = new RestResult(httpStatus.value(), "Empty email or network type");
			
			return new ResponseEntity<RestResult>(result, httpStatus);
		}
		
		UserNetworkType type = UserNetworkType.valueOf(model.getNetworkType());
		if (type == null) {
			httpStatus =  HttpStatus.BAD_REQUEST;
			result = new RestResult(httpStatus.value(), "Network type is not match");
			
			return new ResponseEntity<RestResult>(result, httpStatus);
		}
		
		UserNetwork un = userNetworkService.findByEmailAndType(model.getEmail(), type);
		if (un != null) {
			httpStatus =  HttpStatus.OK;
			result = new RestResult(httpStatus.value(), "User has been registered");
			
			return new ResponseEntity<RestResult>(result, httpStatus);			
		}

		httpStatus =  HttpStatus.NOT_FOUND;
		result = new RestResult(httpStatus.value(), "Can't found the user");
		return new ResponseEntity<RestResult>(result, httpStatus);
	}
	
	@RequestMapping(value = ApiPath.USER_IS_USERNAME_EXIST, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<RestResult> isUsernameAvailable(@RequestBody CheckUsernameModelInput model) {
		HttpStatus httpStatus;
		RestResult result;
		
		if (model == null || 
			model.getUsername() == null || 
			model.getUsername().isEmpty()) {
			
			httpStatus =  HttpStatus.BAD_REQUEST;
			result = new RestResult(httpStatus.value(), "Empty username");
			
			return new ResponseEntity<RestResult>(result, httpStatus);
		}
		
		User user = userService.findUserByUsername(model.getUsername());
		if (user != null) {
			httpStatus =  HttpStatus.CONFLICT;			
			return new ResponseEntity<RestResult>(httpStatus);
		}
			
		httpStatus =  HttpStatus.OK;		
		return new ResponseEntity<RestResult>(httpStatus);
	}
	
	@RequestMapping(value = ApiPath.USER_MY_PROFILE, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<User> fetchMyProfile(HttpServletRequest req){
		logger.info("GET /api/usernetwork/me");
		
		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);

		return new ResponseEntity<User>(getUser(sessionId), HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.USER_SESSION, method = RequestMethod.DELETE, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> removeSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("DELETE /api/usernetwork/signin/" + session);

		sessionService.deleteSession(session);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.USER_SESSION, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<UserSession> checkSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/usernetwork/signin/" + session);

		UserSession us = sessionService.findBySession(session);

		if (us == null)
			return new ResponseEntity<UserSession>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<UserSession>(us, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.USER_SIGNIN, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<UserSession> doSignin(
			@RequestBody UserNetworkModelInput model)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("POST /api/usernetwork/signin");

		if (!model.isValidModel())
			return new ResponseEntity<UserSession>(HttpStatus.BAD_REQUEST);

		UserNetworkType type = UserNetworkType.valueOf(model.getType());
		UserNetwork user = userNetworkService.findByEmailAndType(model.getEmail(), type);
		
		if (user == null) {
			if (model.getUsername() == null || model.getUsername().isEmpty()) {
				return new ResponseEntity<UserSession>(HttpStatus.BAD_REQUEST);
			}
			
			user = new UserNetwork(model);
			userNetworkService.create(user, model);
		}
		UserSession session = sessionService.doLogin(user);

		return new ResponseEntity<UserSession>(session, HttpStatus.OK);
	}
}
