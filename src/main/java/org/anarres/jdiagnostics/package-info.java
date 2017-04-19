/**
 * Environment metadata for debugging and building support bundles.
 *
 * This package contains two primary utilities:
 * {@link ProductMetadata} and {@link DefaultQuery Query}.
 *
 * <h2>ProductMetadata</h2>
 * ProductMetadata is an interface to build metadata stored in the JAR files
 * of all your projects. You can implement a --version command-line flag as
 * <code>
 * System.out.println(new ProductMetadata());
 * </code>
 * Of course it has a great many other accessors for querying available modules
 * and versions.
 *
 * <h2>Query</h2>
 * Query and DefaultQuery are a failure-avoidant way to dump a great deal of
 * diagnostic information about the current environment, and detect many common
 * configuration errors. A DefaultQuery {@link Result} is an essential part of any
 * support bundle. You can also construct a DefaultQuery with a Throwable,
 * and the Result will contain a verbose stack trace of the Throwable with
 * all build and classpath metadata included.
 *
 * Basic usage is:
 * <code>
 * Query q = new {@link DefaultQuery}();
 * System.out.println(q.call().toString());
 * </code>
 * although most applications will want to write the result data to a file
 * or network rather than to the console.
 */
package org.anarres.jdiagnostics;
