package com.ews.parkswift.service.util.paypal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import urn.ebay.api.PayPalAPI.DoAuthorizationReq;
import urn.ebay.api.PayPalAPI.DoAuthorizationRequestType;
import urn.ebay.api.PayPalAPI.DoAuthorizationResponseType;
import urn.ebay.api.PayPalAPI.DoCaptureReq;
import urn.ebay.api.PayPalAPI.DoCaptureRequestType;
import urn.ebay.api.PayPalAPI.DoCaptureResponseType;
import urn.ebay.api.PayPalAPI.DoReauthorizationReq;
import urn.ebay.api.PayPalAPI.DoReauthorizationRequestType;
import urn.ebay.api.PayPalAPI.DoReauthorizationResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CompleteCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;

import com.ews.parkswift.config.Constants;
import com.ews.parkswift.web.rest.dto.PaypallAccountDTO;
import com.ews.parkswift.web.rest.dto.parking.PaymentDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.core.ClientCredentials;
import com.paypal.core.credential.ICredential;
import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.openidconnect.CreateFromAuthorizationCodeParameters;
import com.paypal.sdk.openidconnect.Session;
import com.paypal.sdk.openidconnect.Tokeninfo;
import com.paypal.sdk.openidconnect.Userinfo;
import com.paypal.sdk.openidconnect.UserinfoParameters;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.services.PermissionsService;
import com.paypal.svcs.types.ap.ExecutePaymentRequest;
import com.paypal.svcs.types.ap.ExecutePaymentResponse;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.PaymentDetailsRequest;
import com.paypal.svcs.types.ap.PaymentDetailsResponse;
import com.paypal.svcs.types.ap.PreapprovalRequest;
import com.paypal.svcs.types.ap.PreapprovalResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.ap.RefundInfo;
import com.paypal.svcs.types.ap.RefundRequest;
import com.paypal.svcs.types.ap.RefundResponse;
import com.paypal.svcs.types.common.RequestEnvelope;
import com.paypal.svcs.types.perm.RequestPermissionsRequest;
import com.paypal.svcs.types.perm.RequestPermissionsResponse;

/**
 * 
 * @author umair.ali
 *
 */
public class PaypalUtils {

	/***
	 * 
	 * GET ACCESS TOKEN FROM PAYPAL
	 *  
	 * @param token
	 * @param verifier
	 * @return
	 */
//	public static GetAccessTokenResponse getAccessToken(String token,
//			String verifier) {
//
//		// Logger logger = Logger.getLogger(this.getClass().toString());
//
//		// ## GetAccessTokenRequest
//		GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest();
//
//		// The request token from the response to RequestPermissions.
//		// getAccessTokenRequest.setToken("AAAAAAAXO-JZhFLpTLLe");
//		getAccessTokenRequest.setToken(token);
//
//		// The verification code returned in the redirect from PayPal to the
//		// return URL after `RequestPermissions` call
//		// getAccessTokenRequest.setVerifier("R.X1BWK7QEv-dcjQEzk2xg");
//		getAccessTokenRequest.setVerifier(verifier);
//
//		// ## Creating service wrapper object
//		// Creating service wrapper object to make API call and loading
//		// configuration file for your credentials and endpoint
//		PermissionsService service = new PermissionsService(getSdkConf());
//		// try {
//		// // service = new PermissionsService(
//		// // "src/main/resources/sdk_config.properties");
//		// } catch (IOException e) {
//		// logger.severe("Error Message : " + e.getMessage());
//		// }
//		GetAccessTokenResponse getAccessTokenResponse = null;
//		try {
//
//			// ## Making API call
//			// Invoke the appropriate method corresponding to API in service
//			// wrapper object
//			getAccessTokenResponse = service
//					.getAccessToken(getAccessTokenRequest);
//		} catch (Exception e) {
//			e.printStackTrace();
//			// logger.severe("Error Message : " + e.getMessage());
//		}
//
//		// ## Accessing response parameters
//		// You can access the response parameters using getter methods in
//		// response object as shown below
//		// ### Success values
//		if (getAccessTokenResponse.getResponseEnvelope().getAck().getValue()
//				.equalsIgnoreCase("Success")) {
//
//			// The access token that identifies a set of permissions.
//			// logger.info("Access Token : " +
//			// getAccessTokenResponse.getToken());
//			System.out.println("Access Token : "
//					+ getAccessTokenResponse.getToken());
//
//			// The secret associated with the access token.
//			// logger.info("Token Secret : "
//			// + getAccessTokenResponse.getTokenSecret());
//			System.out.println("Token Secret : "
//					+ getAccessTokenResponse.getTokenSecret());
//		}
//		// ### Error Values
//		// Access error values from error list using getter methods
//		else {
//			// logger.severe("API Error Message : "
//			// + getAccessTokenResponse.getError().get(0).getMessage());
//			System.out.println("API Error Message : "+getAccessTokenResponse.getError().get(0)
//					.getMessage());
//		}
//
//		return getAccessTokenResponse;
//
//	}

	/***
	 * 
	 * REQUEST PERMISSIONS FROM PAYPAL FOR TRANSACTION
	 * 
	 * @return
	 */
	public static RequestPermissionsResponse requestPermissions() {

		// Logger logger = Logger.getLogger(this.getClass().toString());

		// ##RequestPermissionsRequest
		// `Scope`, which can take at least 1 of the following permission
		// categories:
		//
		// * EXPRESS_CHECKOUT
		// * DIRECT_PAYMENT
		// * AUTH_CAPTURE
		// * AIR_TRAVEL
		// * TRANSACTION_SEARCH
		// * RECURRING_PAYMENTS
		// * ACCOUNT_BALANCE
		// * ENCRYPTED_WEBSITE_PAYMENTS
		// * REFUND
		// * BILLING_AGREEMENT
		// * REFERENCE_TRANSACTION
		// * MASS_PAY
		// * TRANSACTION_DETAILS
		// * NON_REFERENCED_CREDIT
		// * SETTLEMENT_CONSOLIDATION.
		// * SETTLEMENT_REPORTING
		// * BUTTON_MANAGER
		// * MANAGE_PENDING_TRANSACTION_STATUS
		// * RECURRING_PAYMENT_REPORT
		// * EXTENDED_PRO_PROCESSING_REPORT
		// * EXCEPTION_PROCESSING_REPORT
		// * ACCOUNT_MANAGEMENT_PERMISSION
		// * INVOICING
		// * ACCESS_BASIC_PERSONAL_DATA
		// * ACCESS_ADVANCED_PERSONAL_DATA
		List<String> scopeList = new ArrayList<String>();
		scopeList.add("INVOICING");
		scopeList.add("EXPRESS_CHECKOUT");
		scopeList.add("REFUND");
		scopeList.add("DIRECT_PAYMENT");
		scopeList.add("ACCOUNT_BALANCE");

		// Create RequestPermissionsRequest object which takes mandatory params:
		//
		// * `Scope`
		// * `Callback` - Your callback function that specifies actions to take
		// after the account holder grants or denies the request.
		RequestPermissionsRequest requestPermissionsRequest = new RequestPermissionsRequest(
				scopeList, "http://localhost/callback");

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PermissionsService service = new PermissionsService(getSdkConf());
		// try {
		// service = new PermissionsService(
		// "src/main/resources/sdk_config.properties");
		// } catch (IOException e) {
		// logger.severe("Error Message : " + e.getMessage());
		// }

		RequestPermissionsResponse requestPermissionsResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			requestPermissionsResponse = service
					.requestPermissions(requestPermissionsRequest);
		} catch (Exception e) {
			// logger.severe("Error Message : " + e.getMessage());
			e.printStackTrace();
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (requestPermissionsResponse.getResponseEnvelope().getAck()
				.getValue().equalsIgnoreCase("Success")) {
			// ###Redirecting to PayPal
			// Once you get the success response, user needs to redirect to
			// paypal to authorize. Construct the `redirectUrl` as follows,
			// `redirectURL=https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_grant-permission&request_token="+requestPermissionsResponse.getToken();`
			// Once you are done with authorization, you will be returning
			// back to `callback` url mentioned in your request. While
			// returning, PayPal will send two parameters in 8:
			//
			// * `request_token`
			// * `token_verifier`
			// You have to use these values in `GetAccessToken` API call to
			// generate `AccessToken` and `TokenSecret`

			// A token from PayPal that enables the request to obtain
			// permissions.
			// logger.info("Request_token : "
			// + requestPermissionsResponse.getToken());
			System.out.println("Request_token : "+ requestPermissionsResponse.getToken());

		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			// logger.severe("API Error Message : "
			// + requestPermissionsResponse.getError().get(0).getMessage());
			System.out
					.println("API Error Message : "
							+ requestPermissionsResponse.getError().get(0)
									.getMessage());
		}
		return requestPermissionsResponse;

	}

	/***
	 * 
	 * GET PAYPAL ACCOUNT INFO AGAINST AUTH TOKEN
	 *  
	 * @param authToken
	 * @return
	 * @throws PayPalRESTException
	 */
	 public static Tokeninfo getPaypalInfo(String authToken) throws PayPalRESTException {
	
	 Map<String, String> configurationMap = new HashMap<String, String>();
	 configurationMap.put(Constants.MODE, Constants.MODE_SANDBOX);
	
	 APIContext apiContext = new APIContext();
	 apiContext.setConfigurationMap(configurationMap);
	
	 List<String> scopelist = new ArrayList<String>();
	 scopelist.add(Constants.OPENID);
	 scopelist.add(Constants.EMAIL);
	 String redirectURI = Constants.PAYPALL_REDIRECT_URI;
	
	 ClientCredentials clientCredentials = new ClientCredentials();
	 clientCredentials.setClientID(Constants.PAYPALL_CLIENT_ID);
	
	 String redirectUrl = Session.getRedirectURL(redirectURI, scopelist, apiContext, clientCredentials);
	 System.out.println("redirect url == " + redirectUrl);
	
	 CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
	 param.setClientID(Constants.PAYPALL_CLIENT_ID);
	 param.setClientSecret(Constants.PAYPALL_CLIENT_SECRET);
	 param.setCode(authToken);
	
	 Tokeninfo info = Tokeninfo.createFromAuthorizationCode(apiContext,param);
	
	 return info;
	 }

	 /***
	  * 
	  * GET PRE APPROVAL KEY FROM PAYPAL
	  * 
	  * @param senderPaypalEmail
	  * @return
	  * @throws SSLConfigurationException
	  * @throws InvalidCredentialException
	  * @throws UnsupportedEncodingException
	  * @throws HttpErrorException
	  * @throws InvalidResponseDataException
	  * @throws ClientActionRequiredException
	  * @throws MissingCredentialException
	  * @throws OAuthException
	  * @throws IOException
	  * @throws InterruptedException
	  */
	 public static String preapproval(PaymentDTO paymentDTO)
			throws SSLConfigurationException, InvalidCredentialException,
			UnsupportedEncodingException, HttpErrorException,
			InvalidResponseDataException, ClientActionRequiredException,
			MissingCredentialException, OAuthException, IOException,
			InterruptedException {

		 DateTimeFormatter dateFormat = DateTimeFormat
	                .forPattern("G,C,Y,x,w,e,E,Y,D,M,d,a,K,h,H,k,m,s,S,z,Z");

	        String dob = "2002-01-15";
	        LocalTime localTime = new LocalTime();
	        LocalDate localDate = new LocalDate();
	        DateTime dateTime = new DateTime();
	        LocalDateTime localDateTime = new LocalDateTime();
	        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
 
		 
		 
		RequestEnvelope requestEnvelope = new RequestEnvelope(Constants.LOCALE_US);
		PreapprovalRequest preapprovalRequest = new PreapprovalRequest();
		preapprovalRequest.setRequestEnvelope(requestEnvelope);

		preapprovalRequest.setCurrencyCode(Constants.CURRENCY_USD);
		
		preapprovalRequest.setStartingDate(localDate.toString());
		preapprovalRequest.setEndingDate(localDate.plusDays(3).toString());
		preapprovalRequest.setMaxAmountPerPayment(paymentDTO.getAmountCharged().doubleValue()*0.80);
		preapprovalRequest.setMaxNumberOfPayments(1);
		preapprovalRequest.setMaxTotalAmountOfAllPayments(paymentDTO.getAmountCharged().doubleValue());
//		preapprovalRequest.setSenderEmail(senderPaypalEmail);
		
		preapprovalRequest.setCancelUrl(Constants.PAYPALL_PREAPPROVAL_CANCEL_URL);
		preapprovalRequest.setReturnUrl(Constants.PAYPALL_PREAPPROVAL_RETURN_URL);
		preapprovalRequest.setIpnNotificationUrl(Constants.PAYPALL_IPN_NOTIFICATION_URL);

//		AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(getSdkConf());
		AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService("src/main/resources/sdk_config.properties");
	
		PreapprovalResponse preapprovalResponse = null;
		try {
			preapprovalResponse = adaptivePaymentsService.preapproval(preapprovalRequest);
		} catch (com.paypal.exception.SSLConfigurationException
				| com.paypal.exception.InvalidCredentialException
				| com.paypal.exception.HttpErrorException
				| com.paypal.exception.InvalidResponseDataException
				| com.paypal.exception.ClientActionRequiredException
				| com.paypal.exception.MissingCredentialException
				| com.paypal.sdk.exceptions.OAuthException e) {
			e.printStackTrace();
		}
		System.out.println(preapprovalResponse);
		System.out.println("preapproval key == "+ preapprovalResponse.getPreapprovalKey());

		// Note: For a pre-approval, the authorization redirect would be to:
		// https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-preapproval&preapprovalkey=PA-86H269467E6688502
		// Logger logger = Logger.getLogger(this.getClass().toString());
		
		try {
			prepareCurl(preapprovalResponse.getPreapprovalKey()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return preapprovalResponse.getPreapprovalKey();
	}

	 
	 private static void prepareCurl(String preApprovalKey) throws JSONException, IOException{
		 String url="https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-preapproval&preapprovalkey="+preApprovalKey;
		 URL object=new URL(url);

		 HttpURLConnection con = (HttpURLConnection) object.openConnection();
		 con.setDoOutput(true);
		 con.setDoInput(true);
		 con.setRequestProperty("Content-Type", "application/json");
		 con.setRequestProperty("Accept", "application/json");
		 con.setRequestMethod("POST");

		 JSONObject cred   = new JSONObject();
		 JSONObject auth   = new JSONObject();
		 JSONObject parent = new JSONObject();

		 cred.put("username","adm");
		 cred.put("password", "pwd");

		 auth.put("tenantName", "adm");
		 auth.put("passwordCredentials", cred.toString());

		 parent.put("auth", auth.toString());

		 OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		 wr.write(parent.toString());
		 wr.flush();

		 //display what returns the POST request

		 StringBuilder sb = new StringBuilder();  
		 int HttpResult = con.getResponseCode(); 
		 if(HttpResult == HttpURLConnection.HTTP_OK){
		     BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
		     String line = null;  
		     while ((line = br.readLine()) != null) {  
		         sb.append(line + "\n");  
		     }  

		     br.close();  

		     System.out.println(""+sb.toString());  

		 }else{
		     System.out.println(con.getResponseMessage());  
		 }  
	 }
	 
	 
	 /**
	  * 
	  * SIMPLE, PARALLEL OR CHAIN PAYMENTS 
	  * 
	  * @param preApprovalKey
	  * @param listReceivers
	  * @return
	  * @throws SSLConfigurationException
	  * @throws InvalidCredentialException
	  * @throws UnsupportedEncodingException
	  * @throws HttpErrorException
	  * @throws InvalidResponseDataException
	  * @throws ClientActionRequiredException
	  * @throws MissingCredentialException
	  * @throws OAuthException
	  * @throws IOException
	  * @throws InterruptedException
	  */
	public static PayResponse simpleOrChainPay(String preApprovalKey, ReceiverList listReceivers)
			throws SSLConfigurationException, InvalidCredentialException,
			UnsupportedEncodingException, HttpErrorException,
			InvalidResponseDataException, ClientActionRequiredException,
			MissingCredentialException, OAuthException, IOException,
			InterruptedException {

		PayRequest payRequest = new PayRequest();

		payRequest.setReceiverList(listReceivers);
		payRequest.setPreapprovalKey(preApprovalKey);
		RequestEnvelope requestEnvelope = new RequestEnvelope(Constants.LOCALE_US);
		payRequest.setRequestEnvelope(requestEnvelope);
		payRequest.setActionType(Constants.PAYPALL_ACTION_TYPE_PAY);
		payRequest.setCancelUrl(Constants.PAYPALL_PREAPPROVAL_CANCEL_URL);
		payRequest.setReturnUrl(Constants.PAYPALL_PREAPPROVAL_RETURN_URL);
		payRequest.setCurrencyCode(Constants.CURRENCY_USD);
		payRequest.setIpnNotificationUrl(Constants.PAYPALL_IPN_NOTIFICATION_URL);

		Map<String, String> sdkConfig = getSdkConf();

//		AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
		AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService("src/main/resources/sdk_config.properties");
		
		PayResponse payResponse = null;
		try {
			payResponse = adaptivePaymentsService.pay(payRequest);
		} catch (com.paypal.exception.SSLConfigurationException
				| com.paypal.exception.InvalidCredentialException
				| com.paypal.exception.HttpErrorException
				| com.paypal.exception.InvalidResponseDataException
				| com.paypal.exception.ClientActionRequiredException
				| com.paypal.exception.MissingCredentialException
				| com.paypal.sdk.exceptions.OAuthException e) {
			e.printStackTrace();
		}

		System.out.println("Pay Key : " + payResponse.getPayKey());

		return payResponse;
	}

	/***
	 * REFUND TRANSACTION ON THE BASIS OF PAYKEY 
	 * @param payKey
	 * @return
	 */
	public static RefundResponse refund(String payKey) {

		// ##RefundRequest
		// The code for the language in which errors are returned, which must be
		// en_US.
		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(Constants.LOCALE_US);

		// RefundRequest which takes,
		// 
		// `Request Envelope` - Information common to each API operation, such
		// as the language in which an error message is returned.
		RefundRequest refundRequest = new RefundRequest(requestEnvelope);

		// You must specify either,
		// 
		// * `Pay Key` - The pay key that identifies the payment for which you
		// want to retrieve details. This is the pay key returned in the
		// PayResponse message.
		// * `Transaction ID` - The PayPal transaction ID associated with the
		// payment. The IPN message associated with the payment contains the
		// transaction ID.
		// `refundRequest.setTransactionId(transactionId)`
		// * `Tracking ID` - The tracking ID that was specified for this payment
		// in the PayRequest message.
		// `refundRequest.setTrackingId(trackingId)`
		
		//
		refundRequest.setPayKey(payKey);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		
		AdaptivePaymentsService service = new AdaptivePaymentsService(getSdkConf());
		RefundResponse refundResponse = null;
		try {

			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			refundResponse = service.refund(refundRequest);
		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (refundResponse.getResponseEnvelope().getAck().getValue()
				.equalsIgnoreCase("Success")) {

			// List of refunds associated with the payment.
			Iterator<RefundInfo> iterator = refundResponse
					.getRefundInfoList().getRefundInfo().iterator();
			while (iterator.hasNext()) {

				// Represents the refund attempt made to a receiver of a
				// PayRequest.
				RefundInfo refundInfo = iterator.next();

				// Status of the refund. It is one of the following values:
				// 
				// * REFUNDED - Refund successfully completed
				// * REFUNDED_PENDING - Refund awaiting transfer of funds;
				// for example, a refund paid by eCheck.
				// * NOT_PAID - Payment was never made; therefore, it cannot
				// be refunded.
				// * ALREADY_REVERSED_OR_REFUNDED - Request rejected because
				// the refund was already made, or the payment was reversed
				// prior to this request.
				// * NO_API_ACCESS_TO_RECEIVER - Request cannot be completed
				// because you do not have third-party access from the
				// receiver to make the refund.
				// * REFUND_NOT_ALLOWED - Refund is not allowed.
				// * INSUFFICIENT_BALANCE - Request rejected because the
				// receiver from which the refund is to be paid does not
				// have sufficient funds or the funding source cannot be
				// used to make a refund.
				// * AMOUNT_EXCEEDS_REFUNDABLE - Request rejected because
				// you attempted to refund more than the remaining amount of the
				// payment; call the PaymentDetails API operation to
				// determine the amount already refunded.
				// * PREVIOUS_REFUND_PENDING - Request rejected because a
				// refund is currently pending for this part of the payment
				// * NOT_PROCESSED - Request rejected because it cannot be
				// processed at this time
				// * REFUND_ERROR - Request rejected because of an internal
				// error
				// * PREVIOUS_REFUND_ERROR - Request rejected because
				// another part of this refund caused an internal error.
//				logger.info("Refund Status : "+ refundInfo.getRefundStatus());
				System.out.println("Refund Status : "+ refundInfo.getRefundStatus());
			}
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
//			logger.severe("API Error Message : "
//					+ refundResponse.getError().get(0).getMessage());
			System.out.println("API Error Message : "+ refundResponse.getError().get(0).getMessage());
		}
		return refundResponse;
	}
	
	/***
	 * EXECUTE PAYMENT THROUGH PAYKEY
	 * @param payKey
	 * @return
	 */
	public static ExecutePaymentResponse executePayment(String payKey) {

//		Logger logger = Logger.getLogger(this.getClass().toString());

		// ##ExecutePaymentRequest
		// The code for the language in which errors are returned, which must be
		// en_US.
		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(Constants.LOCALE_US);

		// ExecutePaymentRequest which takes,
		// 
		// * `Request Envelope` - Information common to each API operation, such
		// as the language in which an error message is returned.
		// * `Pay Key` - The pay key that identifies the payment for which you
		// want to set payment options. This is the pay key returned in the
		// PayResponse message. Action Type in PayRequest must be `CREATE` and must set `SenderEmail`
		ExecutePaymentRequest executePaymentRequest = new ExecutePaymentRequest(
				requestEnvelope, payKey);

		// The ID of the funding plan from which to make this payment.
		executePaymentRequest.setFundingPlanId("0");

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		AdaptivePaymentsService service = null;
		
		try {
			service = new AdaptivePaymentsService(
					"src/main/resources/sdk_config.properties");
		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
			e.printStackTrace();
		}
		ExecutePaymentResponse executePaymentResponse = null;
		try {
			ICredential credentials = new SignatureCredential(Constants.PAYPALL_USER_NAME, Constants.PAYPALL_PASSWORD, Constants.PAYPALL_SIGNATURE);
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			executePaymentResponse = service
					.executePayment(executePaymentRequest, credentials);
		} catch (Exception e) {
			e.printStackTrace();
//			logger.severe("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (executePaymentResponse.getResponseEnvelope().getAck()
				.getValue().equalsIgnoreCase("Success")) {

			// The status of the payment. Possible values are:
			// 
			// * CREATED - The payment request was received; funds will be
			// transferred once the payment is approved
			// * COMPLETED - The payment was successful
			// * INCOMPLETE - Some transfers succeeded and some failed for a
			// parallel payment
			// * ERROR - The payment failed and all attempted transfers
			// failed
			// or all completed transfers were successfully reversed
			// * REVERSALERROR - One or more transfers failed when
			// attempting
			// to reverse a payment
//			logger.info("Payment Execution Status : "
//					+ executePaymentResponse.getPaymentExecStatus());
			System.out.println("Payment Execution Status : "
					+ executePaymentResponse.getPaymentExecStatus());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
//			logger.severe("API Error Message : "
//					+ executePaymentResponse.getError().get(0).getMessage());
			System.out.println("API Error Message : "
					+ executePaymentResponse.getError().get(0).getMessage());
		}
		return executePaymentResponse;

	}
	
	/***
	 * GET PAYMENT DETAILS THROUGH PAYKEY 
	 * @param payKey
	 * @return
	 */
	public static Map<String,String> getPaymentDetails(String payKey) {
		Map<String,String> mapResponse = new HashMap<>();
//		Logger logger = Logger.getLogger(this.getClass().toString());

		// ##PaymentDetailsRequest
		// The code for the language in which errors are returned, which must be
		// en_US.
		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(Constants.LOCALE_US);

		// PaymentDetailsRequest which takes,
		// 
		// `Request Envelope` - Information common to each API operation, such
		// as the language in which an error message is returned.
		PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest(
				requestEnvelope);

		// You must specify either,
		// 
		// * `Pay Key` - The pay key that identifies the payment for which you
		// want to retrieve details. This is the pay key returned in the
		// PayResponse message.
		// * `Transaction ID` - The PayPal transaction ID associated with the
		// payment. The IPN message associated with the payment contains the
		// transaction ID.
		// `paymentDetailsRequest.setTransactionId(transactionId)`
		// * `Tracking ID` - The tracking ID that was specified for this payment
		// in the PayRequest message.
		// `paymentDetailsRequest.setTrackingId(trackingId)`
		paymentDetailsRequest.setPayKey(payKey);

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		AdaptivePaymentsService service = null;
		try {
			service = new AdaptivePaymentsService(Constants.PAYPAL_CONFIG_PROPERTIES_FILE);
//			service = new AdaptivePaymentsService(getSdkConf());
		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}
		PaymentDetailsResponse paymentDetailsResponse = null;
		try {

			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			paymentDetailsResponse = service
					.paymentDetails(paymentDetailsRequest);
		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (paymentDetailsResponse.getResponseEnvelope().getAck()
				.getValue().equalsIgnoreCase("Success")) {

			// The status of the payment. Possible values are:
			// 
			// * CREATED - The payment request was received; funds will be
			// transferred once the payment is approved
			// * COMPLETED - The payment was successful
			// * INCOMPLETE - Some transfers succeeded and some failed for a
			// parallel payment or, for a delayed chained payment, secondary
			// receivers have not been paid
			// * ERROR - The payment failed and all attempted transfers
			// failed
			// or all completed transfers were successfully reversed
			// * REVERSALERROR - One or more transfers failed when
			// attempting
			// to reverse a payment
			// * PROCESSING - The payment is in progress
			// * PENDING - The payment is awaiting processing
//			logger.info("Payment Status : "
//					+ paymentDetailsResponse.getStatus());
			System.out.println("Payment Status : "
					+ paymentDetailsResponse.getStatus());
			mapResponse.put("status", paymentDetailsResponse.getStatus());
			mapResponse.put("expiryDate", paymentDetailsResponse.getPayKeyExpirationDate());
			mapResponse.put("senderEmail", paymentDetailsResponse.getSenderEmail());
//			logger.info("Payment Key Expiration  : "
//					+ paymentDetailsResponse.getPayKeyExpirationDate());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
//			logger.severe("API Error Message : "
//					+ paymentDetailsResponse.getError().get(0).getMessage());
			System.out.println("API Error Message : "
					+ paymentDetailsResponse.getError().get(0).getMessage());
		}
		
		return mapResponse;
	}
	
	private static APIContext getAPIContext(){
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put(Constants.MODE, Constants.MODE_SANDBOX);

		APIContext apiContext = new APIContext();
		apiContext.setConfigurationMap(configurationMap);
		
		return apiContext;
	}
	
	public static PaypallAccountDTO getPaypalUserDetails(String authToken) {

		PaypallAccountDTO paypallAccountDTO = new PaypallAccountDTO();
		try {
				String accessToken = getAccessToken(authToken);
				UserinfoParameters params = new UserinfoParameters();
				params.setAccessToken(accessToken);
				
				Userinfo userInfo = Userinfo.getUserinfo(getAPIContext(),params);
				if (userInfo != null) {
					paypallAccountDTO.setAuthToken(authToken);
					paypallAccountDTO.setAccessToken(accessToken);
					paypallAccountDTO.setPaypallEmail(userInfo.getEmail());
					paypallAccountDTO.setAccountType(userInfo.getAccountType());
					paypallAccountDTO.setName(userInfo.getName() == null ? userInfo.getName() : userInfo.getName());
					paypallAccountDTO.setMiddleName(userInfo.getMiddleName());
				}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return paypallAccountDTO;
	}
	
	public static String getAccessToken(String authToken) {
		
		APIContext apiContext = getAPIContext();

		List<String> scopelist = new ArrayList<String>();
		scopelist.add(Constants.OPENID);
		scopelist.add(Constants.EMAIL);
		String redirectURI = Constants.PAYPALL_REDIRECT_URI;

		ClientCredentials clientCredentials = new ClientCredentials();
		clientCredentials.setClientID(Constants.PAYPALL_CLIENT_ID);

		String redirectUrl = Session.getRedirectURL(redirectURI, scopelist,
				apiContext, clientCredentials);
		System.out.println("redirect url == " + redirectUrl);

		CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
		param.setClientID(Constants.PAYPALL_CLIENT_ID);
		param.setClientSecret(Constants.PAYPALL_CLIENT_SECRET);
		param.setCode(authToken);
		String accessToken="";
		Tokeninfo info = null;
		try {
			info = Tokeninfo.createFromAuthorizationCode(apiContext, param);
			if (info != null) {
				accessToken = info.getAccessToken();
				System.out.println("access token == " + accessToken);
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	public DoReauthorizationResponseType doReauthorization() {

		Logger logger = Logger.getLogger(this.getClass().toString());

		// ## DoAuthorizationReq
		DoReauthorizationReq doReauthorizationReq = new DoReauthorizationReq();

		// `Amount` to reauthorize which takes mandatory params:
		// 
		// * `currencyCode`
		// * `amount`
		BasicAmountType amount = new BasicAmountType(CurrencyCodeType.USD,
				"3.00");

		// `DoReauthorizationRequest` which takes mandatory params:
		// 
		// * `Authorization Id` - Value of a previously authorized transaction
		// identification number returned by PayPal.
		// * `amount`
		DoReauthorizationRequestType doReauthorizationRequest = new DoReauthorizationRequestType(
				"9B2288061E685550E", amount);
		doReauthorizationReq
				.setDoReauthorizationRequest(doReauthorizationRequest);
		
		// ## Creating service wrapper object
		// Creating service wrapper object to make an API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = null;
		try {

			service = new PayPalAPIInterfaceServiceService(
					"src/main/resources/sdk_config.properties");
		} catch (IOException e) {
			logger.severe("Error Message : " + e.getMessage());
		}
		DoReauthorizationResponseType doReauthorizationResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			 doReauthorizationResponse = service
					.doReauthorization(doReauthorizationReq);
		} catch (Exception e) {
			logger.severe("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (doReauthorizationResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {
			
			// Authorization identification number
			logger.info("Authorization ID:"
					+ doReauthorizationResponse.getAuthorizationID());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = doReauthorizationResponse
					.getErrors();
			logger.severe("API Error Message : "
					+ errorList.get(0).getLongMessage());
		}
		return doReauthorizationResponse;
	}
	private static DoAuthorizationResponseType doAuthorization(String authorizationKey, String _amount) {

//		Logger logger = Logger.getLogger(this.getClass().toString());

		// ## DoAuthorizationReq
		DoAuthorizationReq doAuthorizationReq = new DoAuthorizationReq();

		// `Amount` which takes mandatory params:
		// 
		// * `currencyCode`
		// * `amount`
		BasicAmountType amount = new BasicAmountType(CurrencyCodeType.USD,
				_amount);

		// `DoAuthorizationRequest` which takes mandatory params:
		// 
		// * `Transaction ID` - Value of the order's transaction identification
		// number returned by PayPal.
		// * `Amount` - Amount to authorize.
		DoAuthorizationRequestType doAuthorizationRequest = new DoAuthorizationRequestType(
				authorizationKey, amount);
		doAuthorizationReq.setDoAuthorizationRequest(doAuthorizationRequest);
		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = null;
		try {

			service = new PayPalAPIInterfaceServiceService(
					"src/main/resources/sdk_config.properties");
		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}
		DoAuthorizationResponseType doAuthorizationResponse = null;
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			doAuthorizationResponse = service
					.doAuthorization(doAuthorizationReq);
		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (doAuthorizationResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {

			// Authorization identification number
//			logger.info("Transaction ID:"
//					+ doAuthorizationResponse.getTransactionID());
			System.out.println("Transaction ID:"
					+ doAuthorizationResponse.getTransactionID());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = doAuthorizationResponse.getErrors();
//			logger.severe("API Error Message : "
//					+ errorList.get(0).getLongMessage());
			System.out.println("API Error Message : "
					+ errorList.get(0).getLongMessage());
		}
		return doAuthorizationResponse;
	}
	
	
	public static DoCaptureResponseType doCapture(String authorizationId, String _amount) {

//		Logger logger = Logger.getLogger(this.getClass().toString());
//		String transactionId = doAuthorization(authorizationId, _amount).getTransactionID();
		// ## DoCaptureReq
		DoCaptureReq doCaptureReq = new DoCaptureReq();

		// `Amount` to capture which takes mandatory params:
		// 
		// * `currencyCode`
		// * `amount`
		BasicAmountType amount = new BasicAmountType(CurrencyCodeType.USD,	_amount);

		// `DoCaptureRequest` which takes mandatory params:
		// 
		// * `Authorization ID` - Authorization identification number of the
		// payment you want to capture. This is the transaction ID returned from
		// DoExpressCheckoutPayment, DoDirectPayment, or CheckOut. For
		// point-of-sale transactions, this is the transaction ID returned by
		// the CheckOut call when the payment action is Authorization.
		// * `amount` - Amount to capture
		// * `CompleteCode` - Indicates whether or not this is your last capture.
		// It is one of the following values:
		//  * Complete – This is the last capture you intend to make.
		//  * NotComplete – You intend to make additional captures.
		// `Note:
		// If Complete, any remaining amount of the original authorized
		// transaction is automatically voided and all remaining open
		// authorizations are voided.`
		DoCaptureRequestType doCaptureRequest = new DoCaptureRequestType(
				authorizationId, amount, CompleteCodeType.COMPLETE);
		
//		NVPCallerServices v;
		doCaptureReq.setDoCaptureRequest(doCaptureRequest);
//		APIProfile k;
		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		PayPalAPIInterfaceServiceService service = null;
		try {

			service = new PayPalAPIInterfaceServiceService(
					"src/main/resources/sdk_config.properties");
//			service = new PayPalAPIInterfaceServiceService(getSdkConf());
		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}
		DoCaptureResponseType doCaptureResponse = null;
		ICredential credentials = new SignatureCredential(Constants.PAYPALL_USER_NAME, Constants.PAYPALL_PASSWORD, Constants.PAYPALL_SIGNATURE);
		try {
			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			 doCaptureResponse = service.doCapture(doCaptureReq, credentials);
		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (doCaptureResponse.getAck().getValue()
				.equalsIgnoreCase("success")) {
			
			// Authorization identification number
//			logger.info("Authorization ID:"
//					+ doCaptureResponse.getDoCaptureResponseDetails()
//							.getAuthorizationID());
			System.out.println("Authorization ID:"
					+ doCaptureResponse.getDoCaptureResponseDetails()
							.getAuthorizationID());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			List<ErrorType> errorList = doCaptureResponse.getErrors();
//			logger.severe("API Error Message : "
//					+ errorList.get(0).getLongMessage());
			System.out.println("API Error Message : "
					+ errorList.get(0).getLongMessage());
		}
		return doCaptureResponse;
	}
	
	
	public static void captureAuthorizedPayment(String authId, String amount) throws PayPalException, com.paypal.sdk.exceptions.PayPalException{
		
//		final String DO_CAPTURE_METHOD = "DoCapture";
//		APIProfile profile;
//	    profile = ProfileFactory.createSignatureAPIProfile();
//	        profile.setAPIUsername(Constants.PAYPALL_USER_NAME);
//	        profile.setAPIPassword(Constants.PAYPALL_PASSWORD);
//	        profile.setSignature(Constants.PAYPALL_SIGNATURE);
//	        profile.setEnvironment("sandbox");
//	       // profile.setSubject("");
//	       // profile.setTimeout(timeout);
////	        curl https://svcs.sandbox.paypal.com/AdaptivePayments/PaymentDetails \
////	        	 -H "X-PAYPAL-SECURITY-USERID: {userId}" \
////	        	 -H "X-PAYPAL-SECURITY-PASSWORD: {password}" \
////	        	 -H "X-PAYPAL-SECURITY-SIGNATURE: {signature}" \
////	        	 -H "X-PAYPAL-APPLICATION-ID: {appId}" \
////	        	 -H "X-PAYPAL-REQUEST-DATA-FORMAT: NV" \
////	        	 -H "X-PAYPAL-RESPONSE-DATA-FORMAT: NV" \
//
//	    NVPEncoder encoder = new NVPEncoder();
//	    NVPDecoder decoder = new NVPDecoder();
//
//	    NVPCallerServices caller = new NVPCallerServices();
//	    caller.setAPIProfile(profile);
//
//	    encoder.add("METHOD", DO_CAPTURE_METHOD);
//	    encoder.add("AUTHORIZATIONID", authId);
//	    encoder.add("COMPLETETYPE", "Complete");
//	    encoder.add("AMT", amount);
//	    encoder.add("CURRENCYCODE", "USD");
////	    encoder.add("ENCODING", "UTF-8");
//	    
//	    String NVPRequest = encoder.encode();
//	    String NVPResponse = caller.call(NVPRequest);
//	    decoder.decode(NVPResponse);
//
//	    System.out.println("PayPal Response :: "+NVPResponse);
//	    System.out.println("ACK "+ decoder.get("ACK"));
	}
	
	private static HttpURLConnection makeCurl(String urlString, String httpMethod) throws IOException{
		URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(httpMethod);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", getMerchantAccessToken());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        
        return connection;
	}
	
	public static String getResponseAgainstAuthorizationKey(String authId, String payKey, String _amount)
			throws MalformedURLException, JSONException {

		String urlString = "https://api.sandbox.paypal.com/v1/payments/authorization/"+authId+"/capture";
//		String urlString = "https://api.sandbox.paypal.com/v1/payments/payment/"+payKey;
//		"https://svcs.sandbox.paypal.com/AdaptivePayments/PaymentDetails"
		
		 StringBuffer response = new StringBuffer();
        try {
        	HttpURLConnection connection = makeCurl(urlString, "GET");

            JSONObject amount   = new JSONObject();
            JSONObject parent = new JSONObject();

   		 	amount.put("currency", "USD");
   		 	amount.put("total", _amount);
   		 	parent.put("intent", "sale");
   		 	parent.put("amount", amount);
   		 	parent.put("is_final_capture", true);

            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(parent.toString());
            output.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
              connection.getInputStream()));
            String line;
            String paymentLink;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                JsonObject responseJSON = new JsonParser().parse(line).getAsJsonObject();
                if(responseJSON!=null){
                	System.out.println("ID == " +responseJSON.get("id"));
                	System.out.println("LINKS == " + responseJSON.get("links"));
                	System.out.println("PARENT-PAY-KEY == "+responseJSON.get("parent_payment"));
                	String _payKey= responseJSON.get("parent_payment").toString();
                	
//                	JSONObject payer   = new JSONObject();
//                	payer.put("payer_id","");
                	
                	
//                	if(responseJSON.get("links").isJsonArray()){
//                		JsonArray jsonArray = responseJSON.getAsJsonArray("links");
//                		for (int i = 0, size = jsonArray.size(); i < size; i++)
//                	    {
//                		JsonObject objectInArray = (JsonObject) jsonArray.get(i);
//                	      if(objectInArray.get("rel").toString().replaceAll("\"", "").equalsIgnoreCase("parent_payment")){
//                	    	 paymentLink = objectInArray.get("href").toString();
//                	    	 
//                	    	 
//                	    	 
//                	    	 
//                	    	 HttpURLConnection _connection = makeCurl(paymentLink, "GET");
//                	    	 DataOutputStream _output = new DataOutputStream(_connection.getOutputStream());
//                	            _output.writeBytes(parent.toString());
//                	            _output.close();
//
//                	            BufferedReader _reader = new BufferedReader(new InputStreamReader(
//                	              _connection.getInputStream()));
//                	            String _line;
//                	            while ((_line = _reader.readLine()) != null) {
//                	                System.out.println(_line);
//                	            }
//                	      }
//                	    }
//                	}
                	
                }
            }
            reader.close();
            
     } catch (Exception e) {
         System.out.println(e.getMessage());
     }

		/* return response */
		return response.toString();
	}

	private static List<Receiver> generateReceiverList(BigDecimal totalAmount, String primaryReceiver) {
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);
		String formattedAmountForSecondaryReceiver = df.format(totalAmount.doubleValue() * 0.20);
		System.out.println("Secondary Receiver Amount "+formattedAmountForSecondaryReceiver);
		
		List<Receiver> listReceivers = new ArrayList<Receiver>();
		
		Receiver receiver = new Receiver();
		receiver.setAmount(Double.valueOf(totalAmount.doubleValue()));
		receiver.setEmail(primaryReceiver);
		receiver.setPrimary(true);
		
		Receiver receiver2 = new Receiver();
		receiver2.setAmount(new BigDecimal(formattedAmountForSecondaryReceiver).doubleValue());
		receiver2.setEmail(Constants.PAYPAL_SECONDARY_RECEIVER);
		
		listReceivers.add(receiver);
		listReceivers.add(receiver2);

		return listReceivers;
	}

	private static Map<String, String> getSdkConf() {
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put(Constants.MODE, Constants.MODE_SANDBOX);
		sdkConfig.put("acct1.UserName", Constants.PAYPALL_USER_NAME);
		sdkConfig.put("acct1.Password", Constants.PAYPALL_PASSWORD);
		sdkConfig.put("acct1.Signature", Constants.PAYPALL_SIGNATURE);
		sdkConfig.put("acct1.AppId", Constants.PAYPALL_APP_ID);
//		sdkConfig.put("service.EndPoint","https://api.sandbox.paypal.com");
		return sdkConfig;
	}
	
	
	private static String getMerchantAccessToken(){
	Map<String, String> configurationMap = new HashMap<String, String>();
	configurationMap.put("service.EndPoint","https://api.sandbox.paypal.com");
	OAuthTokenCredential merchantTokenCredential = new OAuthTokenCredential(
			Constants.PAYPALL_CLIENT_ID, Constants.PAYPALL_CLIENT_SECRET, configurationMap);
	String accessToken="";
	try {
		accessToken = merchantTokenCredential.getAccessToken();
	} catch (PayPalRESTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return accessToken;
	}
	
	
	public static String getPaymentStatus(String payKey) {

//		Logger logger = Logger.getLogger(this.getClass().toString());

		// ##PaymentDetailsRequest
		// The code for the language in which errors are returned, which must be
		// en_US.
		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(Constants.LOCALE_US);

		// PaymentDetailsRequest which takes,
		// 
		// `Request Envelope` - Information common to each API operation, such
		// as the language in which an error message is returned.
		PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest(
				requestEnvelope);

		// You must specify either,
		// 
		// * `Pay Key` - The pay key that identifies the payment for which you
		// want to retrieve details. This is the pay key returned in the
		// PayResponse message.
		// * `Transaction ID` - The PayPal transaction ID associated with the
		// payment. The IPN message associated with the payment contains the
		// transaction ID.
		// `paymentDetailsRequest.setTransactionId(transactionId)`
		// * `Tracking ID` - The tracking ID that was specified for this payment
		// in the PayRequest message.
		// `paymentDetailsRequest.setTrackingId(trackingId)`
		
		paymentDetailsRequest.setPayKey(payKey);
//		paymentDetailsRequest.setTransactionId("");
		
		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		AdaptivePaymentsService service = null;
		try {
			service = new AdaptivePaymentsService(Constants.PAYPAL_CONFIG_PROPERTIES_FILE);
		} catch (IOException e) {
			System.out.println("Error Message : " + e.getMessage());
		}
		PaymentDetailsResponse paymentDetailsResponse = null;
		try {

			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			paymentDetailsResponse = service
					.paymentDetails(paymentDetailsRequest);
		} catch (Exception e) {
			System.out.println("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (paymentDetailsResponse.getResponseEnvelope().getAck()
				.getValue().equalsIgnoreCase("Success")) {

			// The status of the payment. Possible values are:
			// 
			// * CREATED - The payment request was received; funds will be
			// transferred once the payment is approved
			// * COMPLETED - The payment was successful
			// * INCOMPLETE - Some transfers succeeded and some failed for a
			// parallel payment or, for a delayed chained payment, secondary
			// receivers have not been paid
			// * ERROR - The payment failed and all attempted transfers
			// failed
			// or all completed transfers were successfully reversed
			// * REVERSALERROR - One or more transfers failed when
			// attempting
			// to reverse a payment
			// * PROCESSING - The payment is in progress
			// * PENDING - The payment is awaiting processing
			System.out.println("Payment Status : "+ paymentDetailsResponse.getStatus());
			System.out.println("Tracking ID : "+ paymentDetailsResponse.getTrackingId());
			System.out.println("Sender Email : "+paymentDetailsResponse.getSenderEmail());
//			paymentDetailsResponse.get
//			logger.info("Payment Key Expiration  : "
//					+ paymentDetailsResponse.getPayKeyExpirationDate());
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
			System.out.println("API Error Message : "+ paymentDetailsResponse.getError().get(0).getMessage());
		}
		return paymentDetailsResponse.getStatus();
	}
	
	
	public static String generatePayment(PaymentDTO paymentDTO){
		
		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage(Constants.LOCALE_US);
		
		ReceiverList listReceivers = new ReceiverList(generateReceiverList(paymentDTO.getAmountCharged(), paymentDTO.getOwnersPaypalEmail()));

		PayRequest payRequest = null;
		PayResponse payResponse= null;
		
		payRequest = new PayRequest(requestEnvelope, Constants.PAYPALL_ACTION_TYPE_PAY,
				Constants.PAYPAL_PAYMENT_RESPONSE_URL, Constants.CURRENCY_USD, listReceivers,
				Constants.PAYPAL_PAYMENT_RESPONSE_URL);
		
		payRequest.setReceiverList(listReceivers);
//		payRequest.setSenderEmail(senderEmail);
		
		payResponse = makeAPICall(payRequest);
		
		return payResponse.getPayKey();
	}
	
	private static PayResponse makeAPICall(PayRequest payRequest) {

//		Logger logger = Logger.getLogger(this.getClass().toString());

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
//		AdaptivePaymentsService service = new AdaptivePaymentsService(getSdkConf());
		AdaptivePaymentsService service = null;
		try {
			service = new AdaptivePaymentsService(Constants.PAYPAL_CONFIG_PROPERTIES_FILE);
		} catch (IOException e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}
		PayResponse payResponse = null;
		try {

			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			payResponse = service.pay(payRequest);
		} catch (Exception e) {
//			logger.severe("Error Message : " + e.getMessage());
			System.out.println("Error Message : " + e.getMessage());
		}
			
		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (payResponse.getResponseEnvelope().getAck().getValue()
				.equalsIgnoreCase("Success")) {

			// The pay key, which is a token you use in other Adaptive
			// Payment APIs (such as the Refund Method) to identify this
			// payment. The pay key is valid for 3 hours; the payment must
			// be approved while the pay key is valid.
//			logger.info("Pay Key : " + payResponse.getPayKey());
			System.out.println("Pay Key : " + payResponse.getPayKey());

			// Once you get success response, user has to redirect to PayPal
			// for the payment. Construct redirectURL as follows,
			// `redirectURL=https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey="
			// + payResponse.getPayKey();`
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else {
//			logger.severe("API Error Message : "
//					+ payResponse.getError().get(0).getMessage());
			System.out.println("API Error Message : "
					+ payResponse.getError().get(0).getMessage());
		}
		return payResponse;

	}
}
