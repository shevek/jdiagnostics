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
public class SAXQuery extends AbstractQuery {

    private static final String SAX_VERSION1_CLASS = "org.xml.sax.Parser";
    private static final String SAX_VERSION1_METHOD = "parse";  // String
    private static final String SAX_VERSION2BETA_CLASS = "org.xml.sax.XMLReader";
    private static final String SAX_VERSION2BETA_METHOD = "parse";  // String
    private static final String SAX_VERSION2_CLASS = "org.xml.sax.helpers.AttributesImpl";
    private final String SAX_VERSION2_METHOD = "setAttributes";  // Attributes

    @Override
    public String getName() {
        return "sax";
    }

    public void version(Result result, String prefix) {

        new JarQuery("sax.jar").call(result, prefix + "saxJar/");

        Class<?> c2 = new ClassExistsQuery("org.xml.sax.Attributes").findClass(result, prefix + "v2arg/");
        if (c2 != null) {
            Method m2 = new ClassMethodExistsQuery(SAX_VERSION2_CLASS, SAX_VERSION2_METHOD, c2).findMethod(result, prefix + "v2/");
            if (m2 != null) {
                result.put(prefix + "version", ">=2.0");
                return;
            }
        }

        Method m2b = new ClassMethodExistsQuery(SAX_VERSION2BETA_CLASS, SAX_VERSION2BETA_METHOD, String.class).findMethod(result, prefix + "v2beta/");
        if (m2b != null) {
            result.put(prefix + "saxversion", ">=2.0 beta");
            return;
        }

        Method m1 = new ClassMethodExistsQuery(SAX_VERSION1_CLASS, SAX_VERSION1_METHOD, String.class).findMethod(result, prefix + "v1/");
        if (m1 != null) {
            result.put(prefix + "saxversion", ">=1.0");
            return;
        }

    }

    public void factory(Result result, String prefix) {
        Object factory = new ClassMethodExistsCallQuery(null, "javax.xml.parsers.SAXParserFactory", "newInstance").invoke(result, prefix);
        if (factory == null)
            return;
        Object instance = new MethodExistsCallQuery(factory, factory.getClass(), "newSAXParser").invoke(result, prefix + "instance/");
        if (instance == null)
            return;
        new ClassQuery(instance.getClass()).call(result, prefix + "type/");
    }

    @Override
    public void call(Result result, String prefix) {
        version(result, prefix + "version/");
        factory(result, prefix + "factory/");
    }

}
