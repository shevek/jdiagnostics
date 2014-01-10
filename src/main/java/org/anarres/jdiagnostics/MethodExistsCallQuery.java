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

import java.lang.reflect.Method;

/**
 *
 * @author shevek
 */
public class MethodExistsCallQuery extends MethodExistsQuery {

    private final Object target;
    private final Object[] arguments;

    public MethodExistsCallQuery(Object target, Class<?> type, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        super(type, methodName, parameterTypes);
        this.target = target;
        this.arguments = arguments;
    }

    public MethodExistsCallQuery(Object target, Class<?> type, String methodName) {
        this(target, type, methodName, new Class<?>[0], new Object[0]);
    }

    public Object invoke(Result result, String prefix) {
        Method method = findMethod(result, prefix);
        if (method == null)
            return null;
        return new MethodCallQuery(target, method, arguments).invoke(result, prefix);
    }

    @Override
    public void call(Result result, String prefix) {
        invoke(result, prefix);
    }

}
