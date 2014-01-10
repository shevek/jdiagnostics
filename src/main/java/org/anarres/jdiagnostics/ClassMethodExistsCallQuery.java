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

import java.util.Arrays;

/**
 *
 * @author shevek
 */
public class ClassMethodExistsCallQuery extends ClassExistsQuery {

    private final Object target;
    public final String methodName;
    private final Class<?>[] parameterTypes;
    private final Object[] arguments;

    public ClassMethodExistsCallQuery(Object target, String className, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        super(className);
        this.target = target;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    public ClassMethodExistsCallQuery(Object target, String className, String methodName) {
        this(target, className, methodName, new Class<?>[0], new Object[0]);
    }

    @Override
    public String getName() {
        return "classMethodExistsCall/" + className + "/" + methodName + " " + Arrays.toString(parameterTypes);
    }

    public Object invoke(Result result, String prefix) {
        Class<?> type = new ClassExistsQuery(className).findClass(result, prefix);
        if (type == null)
            return null;
        return new MethodExistsCallQuery(target, type, methodName, parameterTypes, arguments).invoke(result, prefix);
    }

    @Override
    public void call(Result result, String prefix) {
        invoke(result, prefix);
    }
}
