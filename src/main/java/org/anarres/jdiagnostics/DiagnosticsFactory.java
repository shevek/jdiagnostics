package org.anarres.jdiagnostics;

/**
 *
 * @author shevek
 */
public class DiagnosticsFactory {

    public static Query forException(ClassNotFoundException e) {
        CompositeQuery query = new CompositeQuery();
        query.add(new ClassExistsQuery(e.getMessage()));
        query.add(new ClassLoaderQuery("system", String.class.getClassLoader()));
        query.add(new ClassLoaderQuery("threadcontext", Thread.currentThread().getContextClassLoader()));
        query.add(new ClassLoaderQuery("jdiagnostics", DiagnosticsFactory.class.getClassLoader()));
        return query;
    }
}
