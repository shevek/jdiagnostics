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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 *
 * @author shevek
 */
public abstract class AbstractQuery implements Query {

    protected <T> T[] array(Class<T> type, T t0, T... t1) {
        T[] out = (T[]) Array.newInstance(type, t1.length + 1);
        out[0] = t0;
        System.arraycopy(t1, 0, out, 1, t1.length);
        return out;
    }

    protected <T extends AccessibleObject & Member> void access(T field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public abstract void call(Result result, String prefix);

    protected Result newResult() {
        return new Result();
    }

    @Override
    public Result call() {
        Result result = newResult();
        try {
            call(result, "");
        } catch (Throwable t) {
            thrown(result, "", t);
        }
        return result;
    }

    protected void thrown(Result result, String prefix, Throwable t) {
        result.put(prefix + "throwable", t);
        // t.printStackTrace();
    }

    @Override
    public String toString() {
        return getName();
    }

}
