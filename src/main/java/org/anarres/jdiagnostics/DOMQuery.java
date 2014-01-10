/**
 * Copyright 2014 Shevek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

import java.lang.reflect.Method;

/**
 *
 * @author shevek
 */
public class DOMQuery extends AbstractQuery {

    private static final String DOM_LEVEL2_CLASS = "org.w3c.dom.Document";
    private static final String DOM_LEVEL2_METHOD = "createElementNS";  // String, String
    private static final String DOM_LEVEL2WD_CLASS = "org.w3c.dom.Node";
    private static final String DOM_LEVEL2WD_METHOD = "supported";  // String, String
    private static final String DOM_LEVEL2FD_CLASS = "org.w3c.dom.Node";
    private static final String DOM_LEVEL2FD_METHOD = "isSupported";  // String, String

    @Override
    public String getName() {
        return "dom";
    }

    @Override
    public void call(Result result, String prefix) {
        new JarQuery("dom.jar").call(result, prefix + "domJar/");
        Method m2 = new ClassMethodExistsQuery(DOM_LEVEL2_CLASS, DOM_LEVEL2_METHOD, String.class, String.class).findMethod(result, prefix + "v2/");
        if (m2 != null) {
            result.put(prefix + "version", ">=2.0");
            return;
        }
        Method m2wd = new ClassMethodExistsQuery(DOM_LEVEL2WD_CLASS, DOM_LEVEL2WD_METHOD, String.class, String.class).findMethod(result, prefix + "v2wd/");
        if (m2wd != null) {
            result.put(prefix + "version", "2.0 working draft");
            return;
        }
        Method m2fd = new ClassMethodExistsQuery(DOM_LEVEL2FD_CLASS, DOM_LEVEL2FD_METHOD, String.class, String.class).findMethod(result, prefix + "v2fd/");
        if (m2fd != null) {
            result.put(prefix + "version", "2.0 final draft");
            return;
        }
    }

}
