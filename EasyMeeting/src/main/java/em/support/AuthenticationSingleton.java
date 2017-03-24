package em.support;

public class AuthenticationSingleton {
	private AuthenticationSingleton(){}
	
	private static Authentication auth = new Authentication("7aea07ae5d5f4e3aa220873487a02cee","7aea07ae5d5f4e3aa220873487a02cee");
	
	public static Authentication getAuthenticationInstance(){
		if(auth.getToken()==null){
			auth = new Authentication("7aea07ae5d5f4e3aa220873487a02cee","7aea07ae5d5f4e3aa220873487a02cee");
		}
		return auth;
	}
}
