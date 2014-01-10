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
public class ClassMethodExistsQuery extends ClassExistsQuery {

    public final String methodName;
    private final Class<?>[] parameterTypes;

    public ClassMethodExistsQuery(String className, String methodName, Class<?>... parameterTypes) {
        super(className);
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String getName() {
        return "classMethodExists/" + className + "/" + methodName + " " + Arrays.toString(parameterTypes);
    }

    public Method findMethod(Result result, String prefix) {
        Class<?> type = findClass(result, prefix + "class/");
        if (type == null)
            return null;
        return new MethodExistsQuery(type, methodName, parameterTypes).findMethod(result, prefix + "method/" + methodName + "/");
    }

    @Override
    public void call(Result result, String prefix) {
        Method method = findMethod(result, prefix);
        result.put(prefix + "exists", method != null);
    }

}
