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
package org.jboss.weld.environment.servlet.test.util;

import java.io.File;

import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

/**
 * @author Matus Abaffy
 */
public class GaeDeployments {
    public static final Asset APPENGINE_WEB = new StringAsset(
    		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
    		"<appengine-web-app xmlns=\"http://appengine.google.com/ns/1.0\">\n" +
    		"    <version>2</version>\n" +
    		"    <threadsafe>false</threadsafe>\n" +
    		"    <sessions-enabled>true</sessions-enabled>\n" +
    		"</appengine-web-app>");

    public static WebArchive addLibraries(WebArchive war) {
        PomEquippedResolveStage pers = Maven.resolver().loadPomFromFile("pom.xml");
        File[] servlet = pers.resolve("org.jboss.weld.servlet:weld-servlet-core").withTransitivity().asFile();
        File[] core = pers.resolve("org.jboss.weld:weld-core-impl").withTransitivity().asFile();
        File[] jsp = pers.resolve("javax.servlet.jsp:jsp-api").withTransitivity().asFile();

        return war.addAsLibraries(core).addAsLibraries(servlet).addAsLibraries(jsp);
    }
}
