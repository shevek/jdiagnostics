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

import java.util.LinkedHashMap;

/**
 *
 * @author shevek
 */
public class ThrowableQuery extends AbstractQuery {

    private final Throwable throwable;

    public ThrowableQuery(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String getName() {
        return "throwable";
    }

    @Override
    protected Result newResult() {
        return new Result(new LinkedHashMap<String, Object>());
    }

    @Override
    public void call(Result result, String prefix) {
        if (throwable == null) {
            result.put("message", "<null throwable>");
            return;
        }

        new ClassQuery(throwable.getClass()).call(result, prefix + "throwable/");

        result.put("message", throwable.getMessage());
        int i = 0;
        for (StackTraceElement e : throwable.getStackTrace()) {
            result.put(prefix + "frame" + i + "/element", e);
            result.put(prefix + "frame" + i + "/class", new ClassExistsQuery(e.getClassName()).call());
            i++;
        }

        Object suppressed = new MethodExistsCallQuery(throwable, Throwable.class, "getSuppressed").invoke(result, prefix + "suppressed/");
        if (suppressed instanceof Throwable[]) {
            int j = 0;
            for (Throwable t : (Throwable[]) suppressed) {
                result.put(prefix + "suppressed" + j, new ThrowableQuery(t).call());
                j++;
            }
        }
    }

}
