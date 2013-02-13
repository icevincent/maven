/*******************************************************************************
 * Copyright 2013 Universidad Polit�cnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.support.directives.checks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.universAAL.support.directives.api.APIFixableCheck;
import org.universAAL.support.directives.api.AbstractCheckMojo;
import org.universAAL.support.directives.util.PomFixer;
import org.universAAL.support.directives.util.PomWriter;

/**
 * @author amedrano
 *
 */
public class ModulesCheckFix implements APIFixableCheck, PomFixer {
	
	/**
	 * Message content when check fails
	 */
	private static final String MODULES_NOT_CONFIGURED_ROOT = System
			.getProperty("line.separator")
			+ "\n"
			+ "Modules List Directive Fail :\n"
			+ "It seems the POM does not list all the modules it should. "
			+ System.getProperty("line.separator") + "\n";

	/**
	 * List of Dependencies to be fixed
	 */
	private ArrayList<String> toBeFixed = new ArrayList<String>();
	
	/** {@inheritDoc} */
	public boolean check(MavenProject mavenproject, Log log)
			throws MojoExecutionException, MojoFailureException {
		
		if (!passCheck(mavenproject, log)) {
			String err = getErrorMessge(mavenproject);
			throw new MojoFailureException(err);
		}
		return true;
	}

	private String getErrorMessge(MavenProject mavenProject) {
		if (AbstractCheckMojo.isRootProject(mavenProject)) {
			String err = MODULES_NOT_CONFIGURED_ROOT;
			for (String mod : toBeFixed) {
				err += "\n" + mod 
						+ ", version should be listed as a module." ;
			}
			return err;
		}
		return "";
	}

	/** 
	 * {@inheritDoc}
	 */
	public void fix(MavenProject mavenProject2, Log log) throws MojoFailureException {
		// TODO Auto-generated method stub
		try {
			new PomWriter(this, mavenProject2).fix();
		} catch (Exception e) {
			log.error("unable to Write POM.");
		}
	}

	/**
	 * check whether there are any versions defined or dependencyManagement points to correct versions
	 * @param mavenProject2
	 * @param log TODO
	 * @return
	 */
	private boolean passCheck(MavenProject mavenProject2, Log log) {
		if (mavenProject2.getPackaging().equals("pom")) {
			return passRootCheck(mavenProject2, log);
		}
		else {
			return true;
		}
	}

	private boolean passRootCheck(MavenProject mavenProject2, Log log) {
		List<String> listed = (List<String>) mavenProject2.getModules();
		
		//gather the existent modules
		File dir = mavenProject2.getBasedir().getParentFile();
		for (File f : dir.listFiles()) {
			String rel = "../" + f.getName();
			if (f.isDirectory()
					&& !listed.contains(rel)
					&& !rel.endsWith(mavenProject2.getBasedir().getName())) {
				toBeFixed.add(rel);
				log.debug("Found not listed module : " + rel);
			}
		}
		return toBeFixed.isEmpty();
	}
	
	public void fix(Model model) {
		for (String mod: toBeFixed) {
			model.addModule(mod);
		}
	}
}