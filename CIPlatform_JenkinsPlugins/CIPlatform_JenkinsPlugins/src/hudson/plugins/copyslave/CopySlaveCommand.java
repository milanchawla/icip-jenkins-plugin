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
package hudson.plugins.copyslave;

import hudson.Extension;
import hudson.Util;
import hudson.cli.CLICommand;
import hudson.model.Failure;

import hudson.model.Node;
import hudson.slaves.DumbSlave;
import jenkins.model.Jenkins;

import org.kohsuke.args4j.Argument;

/**
 * Copies the TG_MASTER_SLAVE with new name, FSRoot and number of executors.
 * 
 * @author KrishnaKanth_BN
 */
@Extension
public class CopySlaveCommand extends CLICommand {
	@Override
	public String getShortDescription() {
		return "Copy the master slave to create a dummy slave (only for administrators)";
	}

	@Argument(index = 0, metaVar = "NAME", usage = "Name of the slave to create")
	public String name;

	@Argument(index = 1, metaVar = "FSROOT", usage = "Root directory on slave machine where Jenkins downloads artefacts for build")
	public String fsroot;

	@Argument(index = 2, metaVar = "NUMEXEC", usage = "Number of executors")
	public String numexec;

	protected int run() throws Exception {
		Jenkins h = Jenkins.getInstance();

		int iNumExec = Integer.parseInt(numexec);
		if (h.hasPermission(Jenkins.ADMINISTER)) {
			Node src;
			Jenkins app = Jenkins.getInstance();
			
			src = app.getNode("TG_MASTER_SLAVE");
			if (src == null) {
				if (Util.fixEmpty("TG_MASTER_SLAVE") == null) {
					throw new Failure("No such master slave");
				}
				throw new Failure("No such master slave as TG_MASTER_SLAVE");
			}

			DumbSlave copiedSlave = new DumbSlave(name, src
					.getNodeDescription(), fsroot, numexec, src.getMode(), src.getLabelString(),
					((DumbSlave) src).getLauncher(), ((DumbSlave) src)
							.getRetentionStrategy(), src.getNodeProperties()
							.toList());

			app.addNode(copiedSlave);
		}

		return 0;

	}
}
