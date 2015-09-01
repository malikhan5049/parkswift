package com.ews.parkswift.service.util.paypal;

/**
 * 
 * @author umair.ali
 *
 */
public class PaypalUtils {

//	public static GetAccessTokenResponse getAccessToken() {
		//
//				Logger logger = Logger.getLogger(this.getClass().toString());
		//
//				// ## GetAccessTokenRequest
//				GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest();
		//
//				// The request token from the response to RequestPermissions.
//				getAccessTokenRequest.setToken("AAAAAAAXO-JZhFLpTLLe");
		//
//				// The verification code returned in the redirect from PayPal to the
//				// return URL after `RequestPermissions` call
//				getAccessTokenRequest.setVerifier("R.X1BWK7QEv-dcjQEzk2xg");
		//
//				// ## Creating service wrapper object
//				// Creating service wrapper object to make API call and loading
//				// configuration file for your credentials and endpoint
//				PermissionsService service = null;
//				try {
//					service = new PermissionsService(
//							"src/main/resources/sdk_config.properties");
//				} catch (IOException e) {
//					logger.severe("Error Message : " + e.getMessage());
//				}
//				GetAccessTokenResponse getAccessTokenResponse = null;
//				try {
		//
//					// ## Making API call
//					// Invoke the appropriate method corresponding to API in service
//					// wrapper object
//					getAccessTokenResponse = service
//							.getAccessToken(getAccessTokenRequest);
//				} catch (Exception e) {
//					logger.severe("Error Message : " + e.getMessage());
//				}
		//
//				// ## Accessing response parameters
//				// You can access the response parameters using getter methods in
//				// response object as shown below
//				// ### Success values
//				if (getAccessTokenResponse.getResponseEnvelope().getAck().getValue()
//						.equalsIgnoreCase("Success")) {
		//
//					// The access token that identifies a set of permissions.
//					logger.info("Access Token : " + getAccessTokenResponse.getToken());
		//
//					// The secret associated with the access token.
//					logger.info("Token Secret : "
//							+ getAccessTokenResponse.getTokenSecret());
//				}
//				// ### Error Values
//				// Access error values from error list using getter methods
//				else {
//					logger.severe("API Error Message : "
//							+ getAccessTokenResponse.getError().get(0).getMessage());
//				}
		//
//				return getAccessTokenResponse;
		//
//			}
	
	
//	public static RequestPermissionsResponse requestPermissions() {
//
//		Logger logger = Logger.getLogger(this.getClass().toString());
//
//		// ##RequestPermissionsRequest
//		// `Scope`, which can take at least 1 of the following permission
//		// categories:
//		// 
//		// * EXPRESS_CHECKOUT
//		// * DIRECT_PAYMENT
//		// * AUTH_CAPTURE
//		// * AIR_TRAVEL
//		// * TRANSACTION_SEARCH
//		// * RECURRING_PAYMENTS
//		// * ACCOUNT_BALANCE
//		// * ENCRYPTED_WEBSITE_PAYMENTS
//		// * REFUND
//		// * BILLING_AGREEMENT
//		// * REFERENCE_TRANSACTION
//		// * MASS_PAY
//		// * TRANSACTION_DETAILS
//		// * NON_REFERENCED_CREDIT
//		// * SETTLEMENT_CONSOLIDATION.
//		// * SETTLEMENT_REPORTING
//		// * BUTTON_MANAGER
//		// * MANAGE_PENDING_TRANSACTION_STATUS
//		// * RECURRING_PAYMENT_REPORT
//		// * EXTENDED_PRO_PROCESSING_REPORT
//		// * EXCEPTION_PROCESSING_REPORT
//		// * ACCOUNT_MANAGEMENT_PERMISSION
//		// * INVOICING
//		// * ACCESS_BASIC_PERSONAL_DATA
//		// * ACCESS_ADVANCED_PERSONAL_DATA
//		List<String> scopeList = new ArrayList<String>();
//		scopeList.add("INVOICING");
//		scopeList.add("EXPRESS_CHECKOUT");
//
//		// Create RequestPermissionsRequest object which takes mandatory params:
//		// 
//		// * `Scope`
//		// * `Callback` - Your callback function that specifies actions to take
//		// after the account holder grants or denies the request.
//		RequestPermissionsRequest requestPermissionsRequest = new RequestPermissionsRequest(
//				scopeList, "http://localhost/callback");
//
//		// ## Creating service wrapper object
//		// Creating service wrapper object to make API call and loading
//		// configuration file for your credentials and endpoint
//		PermissionsService service = null;
//		try {
//			service = new PermissionsService(
//					"src/main/resources/sdk_config.properties");
//		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
//		}
//
//		RequestPermissionsResponse requestPermissionsResponse = null;
//		try {
//			// ## Making API call
//			// Invoke the appropriate method corresponding to API in service
//			// wrapper object
//			requestPermissionsResponse = service
//					.requestPermissions(requestPermissionsRequest);
//		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
//		}
//
//		// ## Accessing response parameters
//		// You can access the response parameters using getter methods in
//		// response object as shown below
//		// ### Success values
//		if (requestPermissionsResponse.getResponseEnvelope().getAck()
//				.getValue().equalsIgnoreCase("Success")) {
//			// ###Redirecting to PayPal
//			// Once you get the success response, user needs to redirect to
//			// paypal to authorize. Construct the `redirectUrl` as follows,
//			// `redirectURL=https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_grant-permission&request_token="+requestPermissionsResponse.getToken();`
//			// Once you are done with authorization, you will be returning
//			// back to `callback` url mentioned in your request. While
//			// returning, PayPal will send two parameters in 8:
//			// 
//			// * `request_token`
//			// * `token_verifier`
//			// You have to use these values in `GetAccessToken` API call to
//			// generate `AccessToken` and `TokenSecret`
//
//			// A token from PayPal that enables the request to obtain
//			// permissions.
//			logger.info("Request_token : "
//					+ requestPermissionsResponse.getToken());
//
//		}
//		// ### Error Values
//		// Access error values from error list using getter methods
//		else {
//			logger.severe("API Error Message : "
//					+ requestPermissionsResponse.getError().get(0).getMessage());
//		}
//		return requestPermissionsResponse;
//
//	}
	
//	public static Tokeninfo getPaypalInfo(String authToken)
//	throws PayPalRESTException {
//
//Map<String, String> configurationMap = new HashMap<String, String>();
//configurationMap.put("mode", "sandbox");
//
//APIContext apiContext = new APIContext();
//apiContext.setConfigurationMap(configurationMap);
//
//List<String> scopelist = new ArrayList<String>();
//scopelist.add("openid");
//scopelist.add("email");
//String redirectURI = Constants.PAYPALL_REDIRECT_URI;
//
//ClientCredentials clientCredentials = new ClientCredentials();
//clientCredentials.setClientID(Constants.PAYPALL_CLIENT_ID);
//
//String redirectUrl = Session.getRedirectURL(redirectURI, scopelist,	apiContext, clientCredentials);
//System.out.println("redirect url == " + redirectUrl);
//
//CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
//param.setClientID(Constants.PAYPALL_CLIENT_ID);
//param.setClientSecret(Constants.PAYPALL_CLIENT_SECRET);
//param.setCode(authToken);
//
//Tokeninfo info = Tokeninfo.createFromAuthorizationCode(apiContext,param);
//
//return info;
//}
//
//
//public PreapprovalResponse preapproval(String senderPaypalEmail) throws SSLConfigurationException, InvalidCredentialException, UnsupportedEncodingException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, OAuthException, IOException, InterruptedException {
//
//RequestEnvelope requestEnvelope = new RequestEnvelope(Constants.LOCALE_US);
//PreapprovalRequest preapprovalRequest = new PreapprovalRequest();
//preapprovalRequest.setRequestEnvelope(requestEnvelope);
//
//preapprovalRequest.setCurrencyCode(Constants.CURRENCY_USD);
//preapprovalRequest.setStartingDate("2015-08-25");
//preapprovalRequest.setEndingDate("2016-08-25");
//
//preapprovalRequest.setMaxAmountPerPayment(30.0);
//preapprovalRequest.setMaxNumberOfPayments(5);
//preapprovalRequest.setMaxTotalAmountOfAllPayments(150.0);
//
//preapprovalRequest.setSenderEmail(senderPaypalEmail);
//preapprovalRequest.setCancelUrl(Constants.PAYPALL_PREAPPROVAL_CANCEL_URL);
//preapprovalRequest.setReturnUrl(Constants.PAYPALL_PREAPPROVAL_RETURN_URL);
//preapprovalRequest.setIpnNotificationUrl(Constants.PAYPALL_IPN_NOTIFICATION_URL);
//
//Map<String, String> sdkConfig = getSdkConf();
//
//AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
//PreapprovalResponse preapprovalResponse = adaptivePaymentsService.preapproval(preapprovalRequest);
//System.out.println(preapprovalResponse);
//System.out.println("preapproval key == "+preapprovalResponse.getPreapprovalKey());
//
////Note: For a pre-approval, the authorization redirect would be to:
////https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-preapproval&preapprovalkey=PA-86H269467E6688502
////Logger logger = Logger.getLogger(this.getClass().toString());
//
//return preapprovalResponse;
//}
//
//
//public PayResponse simpleOrChainPay(String preApprovalKey) throws SSLConfigurationException, InvalidCredentialException, UnsupportedEncodingException, HttpErrorException, InvalidResponseDataException, ClientActionRequiredException, MissingCredentialException, OAuthException, IOException, InterruptedException {
//
//PayRequest payRequest = new PayRequest();
//ReceiverList receiverList = new ReceiverList(generateReceiverList());
//
//payRequest.setReceiverList(receiverList);
//payRequest.setPreapprovalKey(preApprovalKey);
//RequestEnvelope requestEnvelope = new RequestEnvelope(Constants.LOCALE_US);
//payRequest.setRequestEnvelope(requestEnvelope); 
//payRequest.setActionType(Constants.PAYPALL_ACTION_TYPE_PAY);
//payRequest.setCancelUrl(Constants.PAYPALL_PREAPPROVAL_CANCEL_URL);
//payRequest.setReturnUrl(Constants.PAYPALL_PREAPPROVAL_RETURN_URL);
//payRequest.setCurrencyCode(Constants.CURRENCY_USD);
//payRequest.setIpnNotificationUrl(Constants.PAYPALL_IPN_NOTIFICATION_URL);
//
//Map<String, String> sdkConfig = getSdkConf();
//
//AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
//PayResponse payResponse = adaptivePaymentsService.pay(payRequest);
//
//System.out.println("Pay Key : " + payResponse.getPayKey());
//
//return payResponse;
//}
//
//private List<Receiver> generateReceiverList(){
//List<Receiver> listReceivers = new ArrayList<Receiver>();
//Receiver receiver = new Receiver();
//receiver.setAmount(15.0);
//receiver.setEmail("ews2@ews.com");
//Receiver receiver2 = new Receiver();
//receiver2.setAmount(15.0);
//receiver2.setEmail("ews@ews.com");
//
//listReceivers.add(receiver);
//listReceivers.add(receiver2);
//
//return listReceivers;
//
//}
//
//private Map<String, String> getSdkConf(){
//Map<String, String> sdkConfig = new HashMap<String, String>();
//sdkConfig.put("mode", "sandbox");
//sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
//sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
//sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
//sdkConfig.put("acct1.AppId","APP-80W284485P519543T");
//
//return sdkConfig;
//} 	
}
