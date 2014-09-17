package org.anarres.jdiagnostics;

import java.util.LinkedHashMap;

/**
 *
 * @author shevek
 */
public class ClassLoaderQuery extends AbstractQuery {

    private final String name;
    private final ClassLoader loader;

    public ClassLoaderQuery(String name, ClassLoader loader) {
        this.name = name;
        this.loader = loader;
    }

    @Override
    public String getName() {
        return "classLoader/" + name;
    }

    @Override
    protected Result newResult() {
        return new Result(new LinkedHashMap<String, Object>());
    }

    @Override
    public void call(Result result, String prefix) {
        ClassLoader l = loader;
        int i = 0;
        while (l != null) {
            result.put("loaderClass[" + i + "]", l.getClass());
            result.put("loader[" + i + "]", l);
            l = l.getParent();
            i++;
        }
    }

}
