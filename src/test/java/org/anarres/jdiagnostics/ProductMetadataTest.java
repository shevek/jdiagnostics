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
public class ProductMetadataTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMetadataTest.class);

    @Test
    public void testProductMetadata() throws IOException {
        ProductMetadata product = new ProductMetadata();
        LOG.info("Product is\n" + product);
        LOG.info("Product ch.qos is\n" + product.toString("ch.qos"));

        for (ProductMetadata.ModuleMetadata module : product.getModules()) {
            LOG.info("Module is " + module);
            LOG.info("Module summary is " + module.getSummary());
            LOG.info("Module build summary is " + module.getBuildSummary());
        }
    }
}
