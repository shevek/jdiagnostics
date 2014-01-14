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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author shevek
 */
public class ResourceQuery extends AbstractQuery {

    private final String resourceName;

    public ResourceQuery(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getName() {
        return "resource";
    }

    public List<URL> findResources(Result result, String prefix) {
        try {
            Enumeration<URL> resourcesUrls = getClass().getClassLoader().getResources(resourceName);
            List<URL> resources = Collections.list(resourcesUrls);
            result.put(prefix + "resources", "(" + resources.size() + ") " + resources);
            return resources;
        } catch (IOException t) {
            thrown(result, prefix, t);
        }
        return null;
    }

    @Override
    public void call(Result result, String prefix) {
        findResources(result, prefix);
    }
}
