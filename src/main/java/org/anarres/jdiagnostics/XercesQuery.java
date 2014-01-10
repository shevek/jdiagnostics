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
public class XercesQuery extends AbstractQuery {

    /** The Xerces-J implementation jar. */
    private static final String XERCES_JARNAME = "xerces.jar";

    /** The new Xerces-J implementation jar. */
    private static final String XERCESIMPL_JARNAME = "xercesImpl.jar";

    /** The Xerces-J 2.x xmlParserAPIs jar. */
    private static final String PARSERAPIS_JARNAME = "xmlParserAPIs.jar";

    /** The Xerces-J 1.x version class. */
    private static final String XERCES1_VERSION_CLASS = "org.apache.xerces.framework.Version";

    /** The Xerces-J 1.x version field. */
    private static final String XERCES1_VERSION_FIELD = "fVersion";

    /** The Xerces-J 2.x version class. */
    private static final String XERCES2_VERSION_CLASS = "org.apache.xerces.impl.Version";

    /** The Xerces-J 2.x version field. */
    private static final String XERCES2_VERSION_FIELD = "fVersion";

    @Override
    public String getName() {
        return "xerces";
    }

    @Override
    public void call(Result result, String prefix) {
        new JarQuery(XERCES_JARNAME).call(result, prefix + "/xercesJar");
        new JarQuery(XERCESIMPL_JARNAME).call(result, prefix + "/xercesImplJar");
        new JarQuery(PARSERAPIS_JARNAME).call(result, prefix + "/parserApisJar");
        for (String packagePrefix : new String[]{"org.apache.xerces.", "com.sun.org.apache.xerces.internal."}) {
            new ClassFieldExistsGetQuery(null, packagePrefix + "framework.Version", XERCES1_VERSION_FIELD).call(result, prefix + packagePrefix + "xerces1/");
            new ClassFieldExistsGetQuery(null, packagePrefix + "impl.Version", XERCES2_VERSION_FIELD).call(result, prefix + packagePrefix + "xerces2/");
        }
    }

}
