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
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author shevek
 */
public class EnvironmentQuery extends AbstractQuery {

    @Override
    public String getName() {
        return "environment";
    }

    @Override
    public void call(Result result, String prefix) {
        result.put(prefix + "locale", Locale.getDefault());
        result.put(prefix + "timzeone", TimeZone.getDefault());

        {
            URI uri = URI.create("http://www.google.com/");
            result.put(prefix + "proxy/http", new ProxyQuery(uri).call());
        }

        {
            URI uri = URI.create("https://www.google.com/");
            result.put(prefix + "proxy/https", new ProxyQuery(uri).call());
        }

    }

}
