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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 * @author shevek
 */
public class JarQuery extends AbstractQuery {

    /** A Properties block of known officially shipped .jar names/sizes.  */
    public static final Properties jarTable = new Properties();

    static {
        load(jarTable, "JarQuery.properties");
    }

    private static void load(Properties properties, String name) {
        try {
            InputStream is = JarQuery.class.getResourceAsStream(name);
            if (is == null)
                return;
            try {
                properties.load(is);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String jarName;

    public JarQuery(String jarName) {
        this.jarName = jarName;
    }

    @Override
    public String getName() {
        return "jar/" + jarName;
    }

    public void stat(Result result, String prefix, File file) {
        boolean exists = file.exists();
        result.put(prefix + "/exists", exists);
        if (!exists)
            return;
        {
            String key = file.length() + "_" + file.getName();
            String value = jarTable.getProperty(key);
            if (value != null)
                result.put(prefix + "/release", value);
        }
    }

    public static List<String> get_path(Result result, String prefix, String pathName) {
        String path = System.getProperty(pathName);
        // result.put(prefix + "/value", path);
        if (path == null)
            return null;
        List<String> out = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
        while (st.hasMoreTokens())
            out.add(st.nextToken());
        return out;
    }

    public void search_path(Result result, String prefix, String pathName) {
        prefix = prefix + "/" + pathName;
        List<String> path = get_path(result, prefix, pathName);
        if (path == null)
            return;
        for (String element : path) {
            if (element.indexOf(jarName) > -1)
                stat(result, prefix + "/-" + element, new File(element));
        }
    }

    public void search_dirs(Result result, String prefix, String pathName) {
        prefix = prefix + "/" + pathName;
        List<String> path = get_path(result, prefix, pathName);
        if (path == null)
            return;
        for (String element : path) {
            File file = new File(element, jarName);
            if (file.exists())
                stat(result, prefix, file);
        }
    }

    @Override
    public void call(Result result, String prefix) {
        search_path(result, prefix, "java.class.path");
        search_path(result, prefix, "sun.boot.class.path");
        search_dirs(result, prefix, "java.ext.dirs");
    }
}
