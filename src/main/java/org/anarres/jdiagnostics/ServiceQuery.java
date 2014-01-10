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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author shevek
 */
public class ServiceQuery extends AbstractQuery {

    public static final String PREFIX = "META-INF/services/";
    private final String serviceName;

    public ServiceQuery(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceQuery(Class<?> serviceClass) {
        this(serviceClass.getName());
    }

    @Override
    public String getName() {
        return "service/" + serviceName;
    }

    private Result cat(URL url) {
        Result result = new Result(new LinkedHashMap<String, Object>());
        try {
            InputStream in = url.openStream();
            try {
                int i = 0;
                Reader ir = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(ir);
                for (;;) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    result.put(i + ": " + line, new ClassExistsQuery(line).call());
                    i++;
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            thrown(result, "", e);
        }
        return result;
    }

    @Override
    protected Result newResult() {
        return new Result(new LinkedHashMap<String, Object>());
    }

    @Override
    public void call(Result result, String prefix) {
        List<URL> urls = new ResourceQuery(PREFIX + serviceName).findResources(result, prefix);
        if (urls == null)
            return;
        for (URL url : urls) {
            result.put(prefix + url, cat(url));
        }
    }

}
