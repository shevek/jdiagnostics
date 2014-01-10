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
import java.util.Arrays;

/**
 *
 * @author shevek
 */
public class MethodExistsQuery extends AbstractQuery {

    private final Class<?> type;
    private final String methodName;
    private final Class<?>[] parameterTypes;

    public MethodExistsQuery(Class<?> type, String methodName, Class<?>... parameterTypes) {
        this.type = type;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String getName() {
        return "methodName/" + type.getName() + "." + methodName + " " + Arrays.toString(parameterTypes);
    }

    public Method findMethod(Result result, String prefix) {
        // result.put(prefix + "search", getName());
        try {
            Method method = type.getDeclaredMethod(methodName, parameterTypes);
            result.put(prefix + "method", method);
            return method;
        } catch (NoSuchMethodException e) {
            thrown(result, prefix, e);
        } catch (SecurityException e) {
            thrown(result, prefix, e);
        }
        return null;
    }

    @Override
    public void call(Result result, String prefix) {
        Method method = findMethod(result, prefix);
        result.put(prefix + "exists", method != null);
    }

}
