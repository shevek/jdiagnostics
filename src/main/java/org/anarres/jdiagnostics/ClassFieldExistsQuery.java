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

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author shevek
 */
public class ClassFieldExistsQuery extends ClassExistsQuery {

    private final String[] fieldNames;

    public ClassFieldExistsQuery(String className, String fieldName0, String... fieldNames) {
        super(className);
        this.fieldNames = array(String.class, fieldName0, fieldNames);
    }

    @Override
    public String getName() {
        return "classFieldExists/" + className + "/" + Arrays.toString(fieldNames);
    }

    @Override
    public void call(Result result, String prefix) {
        Class<?> type = new ClassExistsQuery(className).findClass(result, prefix + "class/");
        if (type == null)
            return;
        for (String fieldName : fieldNames)
            new FieldExistsQuery(type, fieldName).call(result, prefix + "field/" + fieldName + "/");
    }

}
