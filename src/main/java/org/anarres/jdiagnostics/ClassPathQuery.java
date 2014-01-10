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

import java.io.File;
import java.util.LinkedHashMap;

/**
 *
 * @author shevek
 */
public class ClassPathQuery extends AbstractQuery {

    @Override
    public String getName() {
        return "classpath";
    }

    private void path(Result result, String prefix, String pathName) {
        Result out = new Result(new LinkedHashMap<String, Object>());
        int i = 0;
        for (String element : JarQuery.get_path(result, prefix, pathName)) {
            out.put(i + ": " + element, new FileQuery(new File(element)).call());
            i++;
        }
        result.put(prefix + pathName, out);
    }

    private void dir(Result result, String prefix, File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            result.put(prefix + "error", "Not a directory: " + dir);
            return;
        }
        Result out = new Result(new LinkedHashMap<String, Object>());
        int i = 0;
        for (File file : files) {
            out.put(i + ":" + file, new FileQuery(file).call());
            i++;
        }
        result.put(prefix + "path", out);
    }

    private void dirs(Result result, String prefix, String dirsName) {
        Result out = new Result(new LinkedHashMap<String, Object>());
        int i = 0;
        for (String element : JarQuery.get_path(result, prefix, dirsName)) {
            dir(out, prefix + i + ": " + element, new File(element));
            i++;
        }
        result.put(prefix + dirsName, out);
    }

    // foreach cp
    // size mdtm csum
    // search_path(result, prefix, "java.class.path");
    // search_path(result, prefix, "sun.boot.class.path");
    // search_dirs(result, prefix, "java.ext.dirs");
    @Override
    public void call(Result result, String prefix) {
        path(result, prefix, "java.class.path");
        path(result, prefix, "sun.boot.class.path");
        dirs(result, prefix, "java.ext.dirs");
    }

}
