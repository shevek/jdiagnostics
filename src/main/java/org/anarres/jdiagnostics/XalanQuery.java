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

/**
 *
 * @author shevek
 */
public class XalanQuery extends AbstractQuery {

    /** The Xalan-J implementation jar. */
    private static final String XALAN_JARNAME = "xalan.jar";

    /** The Xalan-J xml-apis jar. */
    private static final String XMLAPIS_JARNAME = "xml-apis.jar";

    /** The Xalan-J 1.x version class. */
    private static final String XALAN1_VERSION_CLASS = "org.apache.xalan.xslt.XSLProcessorVersion";

    /** The Xalan-J 2.0, 2.1 version class. */
    private static final String XALAN2_VERSION_CLASS = "org.apache.xalan.processor.XSLProcessorVersion";

    /** The Xalan-J 2.2+ version class. */
    private static final String XALAN2_2_VERSION_CLASS = "org.apache.xalan.Version";

    /** The Xalan-J 2.2+ version method. */
    private static final String XALAN2_2_VERSION_METHOD = "getVersion";

    @Override
    public String getName() {
        return "xalan";
    }

    @Override
    public void call(Result result, String prefix) {
        new JarQuery(XALAN_JARNAME).call(result, prefix + "xalanJar/");
        new JarQuery(XMLAPIS_JARNAME).call(result, prefix + "xmlApisJar/");

        for (String packagePrefix : new String[]{"org.apache.xalan.", "com.sun.org.apache.xalan.internal."}) {
            new ClassFieldExistsGetQuery(null, packagePrefix + "xslt.XSLProcessorVersion", "PRODUCT", "VERSION", "S_VERSION").call(result, prefix + packagePrefix + "xalan1/");
            new ClassFieldExistsGetQuery(null, packagePrefix + "processor.XSLProcessorVersion", "S_VERSION").call(result, prefix + packagePrefix + "xalan2/");
            new ClassMethodExistsCallQuery(null, packagePrefix + "Version", "getVersion").call(result, prefix + packagePrefix + "xalan2.2/");
        }
    }

}
