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

import java.net.URI;

/**
 *
 * @author shevek
 */
public class ProxyQuery extends AbstractQuery {

    private final URI uri;

    public ProxyQuery(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getName() {
        return "proxy/" + uri;
    }

    @Override
    public void call(Result result, String prefix) {
        Class<?> proxySelectorType = new ClassExistsQuery("java.net.ProxySelector").findClass(result, prefix);
        if (proxySelectorType == null)
            return;
        Object proxySelector = new MethodExistsCallQuery(null, proxySelectorType, "getDefault").invoke(result, prefix + "factory/");
        if (proxySelector == null)
            return;
        new MethodExistsCallQuery(proxySelector, proxySelectorType, "select", new Class<?>[]{URI.class}, new Object[]{uri}).call(result, prefix + uri.getScheme() + "/");
    }

}
