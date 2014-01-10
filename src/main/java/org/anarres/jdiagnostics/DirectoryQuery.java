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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author shevek
 */
public class DirectoryQuery extends AbstractQuery {

    private final File directory;
    private final boolean writable;

    public DirectoryQuery(File directory, boolean writable) {
        this.directory = directory;
        this.writable = writable;
    }

    @Override
    public String getName() {
        return "directory/" + directory + "[writable=" + writable + "]";
    }

    @Override
    public void call(Result result, String prefix) {
        new FileQuery(directory).call(result, prefix);
        if (!directory.isDirectory())
            return;
        if (!writable)
            return;
        if (!directory.canWrite())
            return;

        try {
            long now = System.currentTimeMillis();
            int outSize = 1024;
            byte[] buf = new byte[outSize];

            File file = File.createTempFile("jdiagnostics", "testfile", directory);
            try {
                OutputStream out = new FileOutputStream(file);
                try {
                    out.write(buf);
                } finally {
                    out.close();
                }

                result.put(prefix + "drift", file.lastModified() - now);

                long inSize = 0L;
                InputStream in = new FileInputStream(file);
                try {
                    for (;;) {
                        int read = in.read(buf);
                        if (read <= 0)
                            break;
                        inSize += read;
                    }
                } finally {
                    in.close();
                }

                if (inSize != outSize)
                    result.put(prefix + "sizemismatch", "outSize=" + outSize + ", inSize=" + inSize);

            } finally {
                if (!file.delete())
                    result.put(prefix + "deletefailed", "true");
            }
        } catch (IOException e) {
            thrown(result, prefix, e);
        }

    }
}
