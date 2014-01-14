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

import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 *
 * @author shevek
 */
public class ClassQuery extends AbstractQuery {

    private final Class<?> type;

    public ClassQuery(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return "class/" + type.getName();
    }

    public static String toResource(Class<?> type) {
        String name = type.getName();
        name = name.replace('.', '/');
        return name + ".class";
    }

    @Override
    public void call(Result result, String prefix) {
        result.put(prefix + "className", type.getName());
        result.put(prefix + "class.classLoader", type.getClassLoader());

        LOCATION:
        {
            ProtectionDomain pd = type.getProtectionDomain();
            if (pd == null) {
                result.put(prefix + "codesource", "(no ProtectionDomain)");
                break LOCATION;
            }
            CodeSource cs = pd.getCodeSource();
            if (cs == null) {
                result.put(prefix + "codesource", "(no CodeSource)");
                break LOCATION;
            }
            result.put(prefix + "codesource", cs.getLocation());
        }

        String resourceName = toResource(type);
        new ResourceQuery(resourceName).call(result, prefix);
    }

}
