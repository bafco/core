/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.environment.servlet.test.el;

import static org.jboss.weld.environment.servlet.test.util.GaeDeployments.APPENGINE_WEB;

import java.io.File;

import javax.faces.FacesException;
import javax.faces.webapp.FacesServlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.weld.environment.servlet.test.util.GaeDeployments;
import org.junit.runner.RunWith;

/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
public class JsfTest extends JsfTestBase {
    @Deployment(testable = false)
    public static WebArchive deployment() {
        PomEquippedResolveStage pers = Maven.resolver().loadPomFromFile("pom.xml");
        File[] deps = pers
                .resolve(
                        "com.sun.faces:jsf-api:2.1.3", "com.sun.faces:jsf-impl:2.1.3",
        // "org.jboss.spec.javax.faces:jboss-jsf-api_2.2_spec", "javax.el:el-api:2.2",
                        "javax.enterprise:cdi-api", "org.jboss.spec.javax.annotation:jboss-annotations-api_1.2_spec")
                .withTransitivity().asFile();
        return GaeDeployments.addLibraries(JsfTestBase.deployment().addClasses(FacesServlet.class, FacesException.class)
                .addAsWebInfResource(APPENGINE_WEB, "appengine-web.xml")
                .addAsLibraries(deps));
    }
}
