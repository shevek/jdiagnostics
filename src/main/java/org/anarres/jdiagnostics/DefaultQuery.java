/**
 * Copyright 2014 Shevek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * A composite of most other useful queries, and a good starting point.
 *
 * @see Query
 * @author shevek
 */
public class DefaultQuery extends CompositeQuery {

    public DefaultQuery() {
        add(new SystemPropertiesQuery());
        add(new SAXQuery());
        add(new DOMQuery());
        add(new JAXPQuery());
        add(new ClassPathQuery());
        add(new XSLTQuery());
        add(new EnvironmentQuery());
        add(new AntQuery());
        add(new XalanQuery());
        add(new XercesQuery());
        add(new TmpDirQuery());
        // add(new ProcessEnvironmentQuery());  // This can expose private data. We will exclude it by default.
        add(new ProductMetadataQuery());

        add(new ClassLoaderQuery("system", String.class.getClassLoader()));
        add(new ClassLoaderQuery("threadcontext", Thread.currentThread().getContextClassLoader()));
        add(new ClassLoaderQuery("jdiagnostics", getClass().getClassLoader()));
    }

    public DefaultQuery(Throwable t) {
        this();
        add(new ThrowableQuery(t));
    }

    public static void main(String[] args) {
        System.out.println(new DefaultQuery().call());
    }
}
