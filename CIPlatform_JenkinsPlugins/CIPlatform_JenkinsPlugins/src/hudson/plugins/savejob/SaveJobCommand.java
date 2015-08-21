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
package hudson.plugins.savejob;

import hudson.Extension;
import hudson.Util;
import hudson.cli.CLICommand;
import hudson.model.Failure;
import hudson.model.Item;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;

import org.kohsuke.args4j.Argument;

/**
 * Saves a job created by IBAP - to re-encrypt the passwords.
 * 
 * @author KrishnaKanth_BN
 */
@Extension
public class SaveJobCommand extends CLICommand {

	@Argument(metaVar = "JOB", usage = "Name of the job to be saved")
	public String job;

	public String getShortDescription() {
		return "Saves the given job to re-encrypt the passwords";
	}

	protected int run() throws Exception {

		Jenkins j = Jenkins.getInstance();
		if (j.hasPermission(Jenkins.ADMINISTER)) {
			Item item = j.getItemByFullName(this.job);
			if (item != null) {
				item.save();
			}
		}
		return 0;
	}

}
