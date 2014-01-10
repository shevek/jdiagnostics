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
public class FieldExistsQuery extends AbstractQuery {

    public final Class<?> type;
    public final String fieldName;

    public FieldExistsQuery(Class<?> type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    @Override
    public String getName() {
        return "fieldExists/" + type.getName() + "." + fieldName;
    }

    public Field findField(Result result, String prefix) {
        try {
            Field field = type.getDeclaredField(fieldName);
            result.put(prefix + "field", field);
            return field;
        } catch (NoSuchFieldException e) {
            thrown(result, prefix, e);
        } catch (SecurityException e) {
            thrown(result, prefix, e);
        }
        return null;
    }

    @Override
    public void call(Result result, String prefix) {
        Field field = findField(result, prefix);
        result.put(prefix + "exists", field != null);
    }

}
