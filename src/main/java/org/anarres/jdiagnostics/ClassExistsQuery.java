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
public class ClassExistsQuery extends AbstractQuery {

    public final String className;

    public ClassExistsQuery(String className) {
        this.className = className;
    }

    @Override
    public String getName() {
        return "classExists/" + className;
    }

    public Class<?> findClass(Result result, String prefix, ClassLoader loader) {
        if (loader == null)
            return null;
        result.put(prefix + "className", className);
        try {
            Class<?> type = loader.loadClass(className);
            result.put(prefix + "classLoader", loader);
            new ClassQuery(type).call(result, prefix);
            return type;
        } catch (ClassNotFoundException e) {
            // Silent if not found.
            result.put(prefix + "className", className + " (not found)");
        } catch (Throwable t) {
            result.put(prefix + "classLoader", loader);
            thrown(result, prefix, t);
        }
        return null;
    }

    public Class<?> findClass(Result result, String prefix) {
        Class<?> out = null;
        Class<?> c;
        c = findClass(result, prefix + "bootClassLoader/", String.class.getClassLoader());
        if (c != null)
            out = c;
        ClassLoader appLoader = getClass().getClassLoader();
        if (appLoader != String.class.getClassLoader()) {
            c = findClass(result, prefix + "appClassLoader/", appLoader);
            if (c != null)
                out = c;
        }
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        if (contextLoader != String.class.getClassLoader() && contextLoader != appLoader) {
            c = findClass(result, prefix + "contextClassLoader/", Thread.currentThread().getContextClassLoader());
            if (c != null)
                out = c;
        }
        return out;
    }

    @Override
    public void call(Result result, String prefix) {
        // result.put("className", className);
        Class<?> c = findClass(result, prefix);
        // result.put(prefix + "exists", c != null);
    }

}
