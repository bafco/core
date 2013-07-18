/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.weld.environment.osgi.tests.util;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ops4j.pax.exam.Option;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class Environment {

    private static final int REFRESH_TIME_FOR_STATE = 50;
    private static final int REFRESH_TIME_FOR_ENVIRONMENT = 500;
    private static final String ORG_JBOSS_WELD_OSGI = "org.jboss.weld.osgi";

    private Environment() {
    }

    public static final String PROJECT_VERSION = "1.2.0-SNAPSHOT";

    public static Option toMavenBundle(String groupId, String artifactName) {
        return mavenBundle(groupId, artifactName).version(PROJECT_VERSION);
    }

    public static Option toMavenBundle(String groupId, String artifactName, String version) {
        return mavenBundle(groupId, artifactName).version(version);
    }

    protected static List<Option> toDefaultOptions(Option... options) {
        List<Option> result = new ArrayList<Option>();
        result.add(toMavenBundle(ORG_JBOSS_WELD_OSGI, "weld-osgi-core-mandatory"));
        result.add(toMavenBundle(ORG_JBOSS_WELD_OSGI, "weld-osgi-core-api"));
        result.add(toMavenBundle(ORG_JBOSS_WELD_OSGI, "weld-osgi-core-spi"));
        result.add(toMavenBundle(ORG_JBOSS_WELD_OSGI, "weld-osgi-core-extension"));
        result.add(toMavenBundle(ORG_JBOSS_WELD_OSGI, "weld-osgi-core-integration"));
        result.add(toMavenBundle("org.jboss.weld.osgi.tests", "weld-osgi-tests-utils"));
        Collections.addAll(result, options);
        return result;
    }

    public static Option[] toCDIOSGiEnvironment(Option... options) {
        List<Option> result = toDefaultOptions(options);
        result.add(junitBundles());
        result.add(felix());
        return result.toArray(new Option[result.size()]);
    }

    public static Option[] toCDIKarafEnvironment(Option... options) {
        List<Option> result = toDefaultOptions(options);
        return result.toArray(new Option[result.size()]);
    }

    public static void waitForEnvironment(BundleContext context) throws InterruptedException {
        boolean ready = false;
        while(!ready) {
            ready = true;
            for(Bundle b : context.getBundles()) {
                if(b.getState() != Bundle.ACTIVE) {
                    ready = false;
                    break;
                }
            }
            Thread.sleep(REFRESH_TIME_FOR_ENVIRONMENT);
        }
        WeldOSGiWait.waitForContainersToStart(context, context.getBundles());
    }

    public static void waitForState(Bundle bundle, int state) throws InterruptedException {
        boolean ready = false;
        while(!ready) {
            if(bundle.getState() == state) {
                ready = true;
            }
            Thread.sleep(REFRESH_TIME_FOR_STATE);
        }
    }

    public static void waitForState(BundleContext context, String symbolicName, int state) throws InterruptedException {
        boolean ready = false;
        while(!ready) {
            for(Bundle bundle : context.getBundles()) {
                if(bundle.getSymbolicName().equals(symbolicName) && bundle.getState() == state) {
                    ready = true;
                }
            }
            Thread.sleep(REFRESH_TIME_FOR_STATE);
        }
    }

    public static String state(int state) {
        String result = "";
        switch (state) {
            case Bundle.ACTIVE:
                result = "ACTIVE";
                break;
            case Bundle.INSTALLED:
                result = "INSTALLED";
                break;
            case Bundle.RESOLVED:
                result = "RESOLVED";
                break;
            case Bundle.STARTING:
                result = "STARTING";
                break;
            case Bundle.STOPPING:
                result = "STOPPING";
                break;
            case Bundle.UNINSTALLED:
                result = "UNINSTALLED";
                break;
            default:
                result = "UNKNOWN";
        }
        return result;
    }

    public static String state(BundleContext context) {
        String result = "";
        for(Bundle b : context.getBundles()) {
            result += b.getSymbolicName() + "-" + b.getVersion() + ": " + state(b.getState()) + System.getProperty("line.separator");
        }
        return result;
    }

}
