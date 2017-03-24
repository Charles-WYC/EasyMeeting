package em.services;

import em.models.Authentication;

public class AuthenticationService {
    private static String clientId = "7aea07ae5d5f4e3aa220873487a02cee";
    private static String clientSecret = "7aea07ae5d5f4e3aa220873487a02cee";
    
	private AuthenticationService(){}
	
	private static Authentication auth = new Authentication(clientId,clientSecret);
	
	public static Authentication getAuthenticationInstance(){
		if(auth.getToken()==null){
			auth = new Authentication(clientId,clientSecret);
		}
		return auth;
	}
	
	public static void renewAuthenticationInstance(){
		auth = new Authentication(clientId,clientSecret);
	}
}
