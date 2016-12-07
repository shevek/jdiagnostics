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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * A result.
 * Results are usually sorted by key (using TreeMap).
 * To preserve order of addition, call
 * <code>new Result(new LinkedHashMap&lt;String, Object&gt;())</code>.
 * In a query, override {@link AbstractQuery#newResult()}.
 *
 * @author shevek
 */
public class Result {

    private final Map<String, Object> data;

    public Result(Map<String, Object> data) {
        this.data = data;
    }

    public Result() {
        this(new TreeMap<String, Object>());
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    private void toIndent(StringBuilder buf, int indent) {
        for (int i = 0; i < indent; i++)
            buf.append("    ");
    }

    public void toString(StringBuilder buf, int indent) {
        for (Entry<String, Object> e : data.entrySet()) {
            toIndent(buf, indent);
            buf.append(e.getKey());
            if (e.getValue() instanceof Result) {
                Result result = (Result) e.getValue();
                buf.append("\n");
                result.toString(buf, indent + 1);
            } else if (e.getValue() instanceof Throwable) {
                Throwable t = (Throwable) e.getValue();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                pw.flush();
                buf.append(": ").append(sw).append("\n");
            } else {
                buf.append(": ").append(e.getValue()).append("\n");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf, 0);
        return buf.toString();
    }

}
