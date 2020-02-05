/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

import java.io.IOException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class DefaultQueryTest {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultQueryTest.class);

    @Test
    public void testQuery() {
        IllegalArgumentException e = new IllegalArgumentException("outer-message", new ClassNotFoundException("inner-message"));
        e.addSuppressed(new IllegalStateException("outer-suppressed", new IOException("inner-suppressed")));

        DefaultQuery query = new DefaultQuery();
        query.add(new ThrowableQuery(e));
        LOG.info(String.valueOf(query.call()));
    }

}
