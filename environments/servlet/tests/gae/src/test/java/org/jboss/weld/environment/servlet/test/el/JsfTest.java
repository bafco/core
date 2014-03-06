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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.environment.servlet.test.util.GaeDeployments;
import org.junit.runner.RunWith;

/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
public class JsfTest extends JsfTestBase {
    @Deployment(testable = false)
    public static WebArchive deployment() {
        return GaeDeployments.addLibraries(JsfTestBase.deployment().addAsWebInfResource(APPENGINE_WEB, "appengine-web.xml"));
    }
}
