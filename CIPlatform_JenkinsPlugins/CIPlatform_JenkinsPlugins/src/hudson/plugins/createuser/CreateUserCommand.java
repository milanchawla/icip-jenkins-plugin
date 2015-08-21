/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc.
 *               2011, Thomas Deruyter <tderuyte@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.createuser;

import hudson.Extension;
import hudson.cli.CLICommand;
import hudson.model.Computer;
import hudson.model.Hudson;
import hudson.model.Item;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.security.AuthorizationStrategy;
import hudson.security.GlobalMatrixAuthorizationStrategy;

import java.lang.reflect.Constructor;

import jenkins.model.Jenkins;

import org.kohsuke.args4j.Argument;

/**
 * Creates a new user by taking 4 command line parameters and assigns limited
 * permissions for the user at the global level
 * 
 * @author KrishnaKanth_BN
 */
@Extension
public class CreateUserCommand extends CLICommand {
	@Override
	public String getShortDescription() {
		return "Creates user by taking userid, password, full name and emailid (only for administrators)";
	}

	@Argument(index = 0, metaVar = "NAME", usage = "ID of the user to be created")
	public String name;

	@Argument(index = 1, metaVar = "PASSWORD", usage = "Password of the user")
	public String password;

	@Argument(index = 2, metaVar = "FULLNAME", usage = "Full name of the user")
	public String fullname;

	@Argument(index = 3, metaVar = "EMAIL", usage = "Email Address of the user")
	public String email;

	protected int run() throws Exception {
		TGSecurityRealm tgsec = new TGSecurityRealm(true);
		User user = tgsec.createAccount(name, password);
		user.setFullName(fullname);
		Jenkins jenkins = Jenkins.getInstance();

		if (jenkins.hasPermission(Jenkins.ADMINISTER)) {
			Class up = jenkins.pluginManager.uberClassLoader
					.loadClass("hudson.tasks.Mailer$UserProperty");
			Constructor c = up
					.getDeclaredConstructor(new Class[] { String.class });
			user.addProperty((UserProperty) c
					.newInstance(new Object[] { email }));
			user.save();

			AuthorizationStrategy as = Jenkins.getInstance()
					.getAuthorizationStrategy();
			if ((as instanceof GlobalMatrixAuthorizationStrategy)) {
				GlobalMatrixAuthorizationStrategy ma = (GlobalMatrixAuthorizationStrategy) as;
				
				 ma.add(Computer.DISCONNECT, user.getId());
				 ma.add(Computer.CONNECT, user.getId()); 
				 //ma.add(Item.BUILD, user.getId());
				// ma.add(Item.CANCEL, user.getId());
				// ma.add(Item.READ, user.getId()); 
				// ma.add(Item.WORKSPACE,user.getId());
				// ma.add(Hudson.READ, user.getId());
				 
				ma.add(Jenkins.READ, user.getId());
				jenkins.setAuthorizationStrategy(ma);
			}
		}
			return 0;

		
	}
}
