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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author shevek
 */
public class FileQuery extends AbstractQuery {

    private final File file;

    public FileQuery(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return "file/" + file;
    }

    @Override
    public void call(Result result, String prefix) {
        result.put(prefix + "path", file.getPath());
        try {
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.equals(file.getPath()))
                result.put(prefix + "canonicalPath", canonicalPath);
        } catch (IOException e) {
            thrown(result, prefix + "canonicalPath/", e);
        }
        result.put(prefix + "stat", "size=" + file.length()
                + ", exists=" + file.exists()
                + ", isFile=" + file.isFile() + ", isDirectory=" + file.isDirectory()
                + ", canRead=" + file.canRead() + ", canWrite=" + file.canWrite());
        if (file.isFile() && file.canRead()) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                InputStream in = new FileInputStream(file);
                try {
                    byte[] block = new byte[4096];
                    for (;;) {
                        int len = in.read(block);
                        if (len < 0)
                            break;
                        digest.update(block, 0, len);
                    }
                } catch (EOFException e) {
                } finally {
                    in.close();
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : digest.digest())
                    buf.append(Integer.toHexString(b & 0xff));
                result.put(prefix + "md5sum", buf.toString());
            } catch (IOException e) {
                thrown(result, prefix + "md5sum/", e);
            } catch (NoSuchAlgorithmException e) {
                thrown(result, prefix + "md5sum/", e);
            }
        }
    }

}
