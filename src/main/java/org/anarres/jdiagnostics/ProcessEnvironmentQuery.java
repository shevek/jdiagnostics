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

import java.util.Map;

/**
 *
 * @author shevek
 */
public class ProcessEnvironmentQuery extends AbstractQuery {

    private final String prefix;

    public ProcessEnvironmentQuery(String prefix) {
        this.prefix = prefix;
    }

    public ProcessEnvironmentQuery() {
        this("");
    }

    @Override
    public String getName() {
        return "processEnvironment";
    }

    @Override
    public void call(Result result, String prefix) {
        for (Map.Entry<String, String> e : System.getenv().entrySet()) {
            String key = e.getKey();
            if (key.startsWith(this.prefix))
                result.put(prefix + key, e.getValue());
        }
    }

}
