package hudson.plugins.createuser;




import hudson.security.HudsonPrivateSecurityRealm;

public class TGSecurityRealm extends HudsonPrivateSecurityRealm {

	public TGSecurityRealm(boolean allowsSignup) {
		super(allowsSignup);
		// TODO Auto-generated constructor stub
	}
		
	/*public User createUser(String name, String password, String fullname, String email) {
		User user = User.get(name);
		user.addProperty(Details.fromPlainPassword(password));
		user.setFullName(fullname);
		try {
			Class up = Jenkins.getInstance().pluginManager.uberClassLoader
					.loadClass("hudson.tasks.Mailer$UserProperty");
			Constructor c = up
					.getDeclaredConstructor(new Class[] { String.class });
			user.addProperty((UserProperty) c
					.newInstance(new Object[] { email }));
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
		*/

}
