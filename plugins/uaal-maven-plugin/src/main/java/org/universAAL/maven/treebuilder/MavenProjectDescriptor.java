/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science 

See the NOTICE file distributed with this work for additional
information regarding copyright ownership

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.universAAL.maven.treebuilder;

import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * Descriptor of maven project related to the dependency resolving process. It
 * aggregates maven project, list of remote repositories used for resolving and
 * indication if dependencies should be resolved transitively or not.
 * 
 * @author rotgier
 * 
 */
public class MavenProjectDescriptor {

    MavenProject project;
    List remoteRepositories;
    boolean transitive = true;

    public MavenProjectDescriptor(final MavenProject project) {
	this.project = project;
    }

    public MavenProjectDescriptor(final MavenProject project,
	    final List remoteRepositories, final boolean transitive) {
	this.project = project;
	this.remoteRepositories = remoteRepositories;
	this.transitive = transitive;
    }

}
