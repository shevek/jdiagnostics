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

/**
 *
 * @author shevek
 */
public class FieldGetQuery extends AbstractQuery {

    private final Object object;
    private final Field field;

    public FieldGetQuery(Object object, Field field) {
        this.object = object;
        this.field = field;
    }

    @Override
    public String getName() {
        return "field/" + field.getDeclaringClass().getName() + "." + field.getName();
    }

    @Override
    public void call(Result result, String prefix) {
        try {
            Object value = field.get(object);
            result.put(prefix + "value", value);
        } catch (IllegalArgumentException e) {
            thrown(result, prefix, e);
        } catch (IllegalAccessException e) {
            thrown(result, prefix, e);
        }
    }

}
