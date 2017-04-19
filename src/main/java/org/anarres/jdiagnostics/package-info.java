/**
 * Environment metadata for debugging and building support bundles.
 *
 * This package contains two primary utilities:
 * {@link ProductMetadata} and {@link DefaultQuery Query}.
 *
 * <h2>ProductMetadata</h2>
 * ProductMetadata is an interface to read metadata stored in the JAR files
 * of all your projects. You can implement a --version command-line flag as
 * <code>
 * System.out.println(new ProductMetadata());
 * </code>
 * You might want this to check, for example, that a customer is using a version
 * of your product built on a particular date, to discover the git hashref it
 * was build from, and so forth.
 * Of course it has a great many other accessors for querying available modules
 * and versions, and supports metadata from many sources, although our favourite
 * is nebula-plugins/gradle-info-plugin.
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
