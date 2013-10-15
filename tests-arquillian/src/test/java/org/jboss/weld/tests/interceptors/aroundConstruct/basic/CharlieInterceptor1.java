/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.interceptors.aroundConstruct.basic;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import junit.framework.Assert;

@Interceptor
@CharlieBinding
public class CharlieInterceptor1 {

    private static boolean invoked;

    public static boolean isInvoked() {
        return invoked;
    }

    public static void reset() {
        invoked = false;
    }

    @AroundConstruct
    public void aroundConstruct(InvocationContext ctx) {
        try {
            ctx.proceed();
            Assert.fail();
        } catch (CharlieException expected) {
            invoked = true;
            throw expected;
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
