package em.result;

public class EmResult {
	public static Result AccessTokenError = new HttpError("httpError", 401, "Invalid subscription key.", 
			"Error: --Access denied due to invalid subscription key.\n"
		  + "       --Make sure to provide a valid key for an active subscription.\n");

	public static Result TranslateRequestTimeout = new HttpError("httpError", 408, "Request time out.", 
			"Error: --Translate failed due to Request time out.\n"
		  + "       --Please check you request or try again.\n");
	
	public static Result TranslateAccessTokenInvalidError = new HttpError("httpError", 403, "Invalid access token.", 
			"Error: --Translate denied due to invalid access token.\n"
		  + "       --Make sure to provide a valid token.\n"
		  + "       --Valid time for each token is 10 minute.\n");
	
	public static Result TranslateBadRequestError = new HttpError("httpError", 400, "Error in request parameter.",
			"Error: --Translate denied due to bad request.\n"
		  + "       --A required parameter is missing, empty or null.\n"
		  + "       --Or the value passed to either a required or optional parameter is invalid.\n");
	
	public static Result TranslateRequestUnauthorizedError = new HttpError("httpError", 401, "Request is not authorized.",
			"Error: --Translate denied due to unauthorized request.\n");
	
	public static Result TranslateBadGatewayError = new HttpError("httpError", 502, "Service was unable to perform the recognition",
			"Error: --Translate denied due to service was unable to perform the recognition.\n");
			
	public static Result CreateProfileError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Create Profile failed due to Speaker Invalid.\n");
	
	public static Result HttpSuccessWithoutParameter = new HttpSuccess("httpSuccess", 200);
	
	public static Result DeleteProfileError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Delete Profile failed due to Speaker Invalid.\n");
	
	public static Result GetProfileError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Get Profile failed due to Speaker Invalid.\n");
	
	public static Result GetAllProfilesError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Get All Profiles failed due to Speaker Invalid.\n");
	
	public static Result CreateEnrollmentInternalServerError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Create Enrollment failed due to Speaker Invalid.\n");
	
	public static Result CreateEnrollmentBadRequest = new HttpError("httpError", 400, "Invalid Audio Format.",
			"Error: --Create Enrollment failed due to Invalid Audio Format.\n");
	
	public static Result GetOperationStatusError = new HttpError("httpError", 404, "No operation id found.",
			"Error: --Get Operation Status failed due to invalid GUID.\n");
	
	public static Result ResetEnrollmentInternalServerError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Reset Enrollment failed due to Speaker Invalid.\n");
	
	public static Result ResetEnrollmentBadRequestError = new HttpError("httpError", 400, "Bad Request",
			"Error: --Reset Enrollment failed due to Speaker Invalid.\n");
	
	public static Result NoIdentificationProfileIdError = new InternalError("internalError", "identificationProfileId missing",
			"Error: --Recognize Speaker failed due to identificationProfileId missing\n");
	
	public static Result SpeakerRecognitionInternalServerError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Speaker Recognition failed due to Speaker Invalid.\n");
	
	public static Result SpeakerRecognitionBadRequest = new HttpError("httpError", 400, "Invalid Audio Format.",
			"Error: --Speaker Recognition failed due to Invalid Audio Format.\n");
	
	public static Result UnknowError = new InternalError("UnknowError", "UnKnow",
			"Error: --Unknow Error\n");
	
	public static Result CloseFileError = new InternalError("closeFileError", "Error occur when close file",
			"Error: --Error occur when close file.\n");
	
	public static Result AnalyzeTextBadArgumentError = new HttpError("httpError", 400, "Bad Argument in request",
			"Error: --Analyze text failed due to Request body is invalid.\n");
	
	public static Result AnalyzeTextInvalidKeyError = new HttpError("httpError", 401, "Invalid subscription key",
			"Error: --Analyze text failed due to invalid subscription key.\n"
		  + "       --Make sure you are subscribed to an API you are trying to call and provide the right key.");
	
	public static Result AnalyzeTextOutOfQuotaError = new HttpError("httpError", 403, "Out of call volume quota",
			"Error: --Analyze text failed due to Out of call volume quota.\n"
		  + "       --Quota will be replenished in 2.12 days.");
	
	public static Result AnalyzeTextInvalidMediaTypeError = new HttpError("httpError", 415, "Invalid Media Type",
			"Error: --Analyze text failed due to Invalid Media Type.\n"
		  + "       --Content-Type should be application/json.\n");
	
	public static Result AnalyzeTextRateLimitError = new HttpError("httpError", 429, "Rate limit is exceeded",
			"Error: --Analyze text failed due to Rate limit is exceeded.\n"
		  + "       --Try again in 26 seconds.\n");
	
	public static Result AnalyzeTextInternalServerError = new HttpError("httpError", 500, "Internal Server Error",
			"Error: --Analyze Text failed due to Internal Server Error.\n");
	
	public static Result InitialResult = new HttpError("initialResult",0,"","");
	
	public static Result UnknowHttpError = new HttpError("httpError", 0, "Unknow Error", 
			"Error: --Unknow http error.\n");
}






