/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 *
 * @author shevek
 */
public class DefaultQueryTest {

    private static final Log LOG = LogFactory.getLog(DefaultQueryTest.class);

    @Test
    public void testQuery() {
        DefaultQuery query = new DefaultQuery();
        query.add(new ThrowableQuery(new Exception()));
        LOG.info(query.call());
    }

}
