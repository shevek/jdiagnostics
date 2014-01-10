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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author shevek
 */
public class MethodCallQuery extends AbstractQuery {

    private final Object target;
    private final Method method;
    private final Object[] arguments;

    public MethodCallQuery(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return "method/" + method.getDeclaringClass().getName() + "." + method.getName();
    }

    public Object invoke(Result result, String prefix) {
        try {
            Object value = method.invoke(target, arguments);
            result.put(prefix + "value", value);
            return value;
        } catch (InvocationTargetException e) {
            thrown(result, prefix, e);
        } catch (IllegalArgumentException e) {
            thrown(result, prefix, e);
        } catch (IllegalAccessException e) {
            thrown(result, prefix, e);
        }
        return null;
    }

    @Override
    public void call(Result result, String prefix) {
        invoke(result, prefix);
    }

}
