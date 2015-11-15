/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

/**
 *
 * @author shevek
 */
public class ProductMetadataQuery extends AbstractQuery {

    @Override
    public String getName() {
        return "productmetadata";
    }

    @Override
    public void call(Result result, String prefix) {
        try {
            ProductMetadata metadata = new ProductMetadata();
            for (ProductMetadata.ModuleMetadata module : metadata.getModules()) {
                Result r = new Result();
                r.put("summary", module.getSummary());
                r.put("date", module.getBuildDate());
                r.put("jar", module.getJar());
                result.put(module.getName(), r);
            }
        } catch (Throwable t) {
            thrown(result, "", t);
        }
    }
}
