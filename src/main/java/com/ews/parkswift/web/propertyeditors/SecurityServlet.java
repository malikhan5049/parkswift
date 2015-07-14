package com.ews.parkswift.web.propertyeditors;

import java.io.IOException;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
@WebServlet("*.sec")
public class SecurityServlet extends HttpServlet {
	 private static final long serialVersionUID = 8071426090770097330L;
	 
	    public SecurityServlet() {
	    }
	 
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        doPost(request, response);
	    }
	 
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        HttpSession httpSession = request.getSession();
	        String faceCode = request.getParameter("code");
	        String state = request.getParameter("state");
	        String accessToken = getFacebookAccessToken(faceCode);
	        String email = "null";
			try {
				email = getUserMailAddressFromJsonResponse(accessToken, httpSession);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        String sessionID = httpSession.getId();
	        if (state.equals(sessionID)){
	            try {
	                //do some specific user data operation like saving to DB or login user
	                request.login(email, "somedefaultpassword");
	            } catch (Exception e) {
	                e.printStackTrace();
	                response.sendRedirect(request.getContextPath() +"/facebookConnectError.jsf");
	                return;
	            }
	            response.sendRedirect(request.getContextPath() +"/login.jsf");
	        } else {
	            System.err.println("CSRF protection validation");
	        }
	    }
	 
	    private String getFacebookAccessToken(String faceCode){
	        String token = null;
	        if (faceCode != null && ! "".equals(faceCode)) {
	            String appId = "431631200204072";
	            String redirectUrl = "http://localhost:8080/index.sec";
	            String faceAppSecret = "7f43f0ce85285349e75bec6170ea960f";
	            String newUrl = "https://graph.facebook.com/oauth/access_token?client_id="
	                    + appId + "&redirect_uri=" + redirectUrl + "&client_secret="
	                    + faceAppSecret + "&code=" + faceCode;
	            HttpClient httpclient = new DefaultHttpClient();
	            try {
	                HttpGet httpget = new HttpGet(newUrl);
	                ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                String responseBody = httpclient.execute(httpget, responseHandler);
	                token = StringUtils.removeEnd
	                        (StringUtils.removeStart(responseBody, "access_token="),
	                                                 "&expires=5180795");
	            } catch (ClientProtocolException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                httpclient.getConnectionManager().shutdown();
	            }
	        }
	        return token;
	    }
	 
	    private String getUserMailAddressFromJsonResponse(String accessToken,
	             HttpSession httpSession) throws JSONException {
	        String email = null;
	        HttpClient httpclient = new DefaultHttpClient();
	        try {
	            if (accessToken != null && ! "".equals(accessToken)) {
	                String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;
	                httpclient = new DefaultHttpClient();
	                HttpGet httpget = new HttpGet(newUrl);
	                System.out.println("Get info from face --> executing request: "
	                                  + httpget.getURI());
	                ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                String responseBody = httpclient.execute(httpget, responseHandler);
	              // JSONObject json = (JSONObject)JSONSerializer.toJSON(responseBody);
	               //ObjectMapper mapper = new ObjectMapper();
	               //JsonNode json = mapper.readTree(responseBody);
	                JSONObject json = new JSONObject(responseBody);
	                String facebookId = json.getString("id");
	                String firstName = json.getString("first_name");
	                String lastName = json.getString("last_name");
	                email= json.getString("email");
	                //put user data in session
	                httpSession.setAttribute("FACEBOOK_USER", firstName+" "
	                        +lastName+", facebookId:" + facebookId);
	 
	            } else {
	                System.err.println("Token for facebook is null");
	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            httpclient.getConnectionManager().shutdown();
	        }
	        return email;
	    }
	}