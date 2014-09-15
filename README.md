# Introduction

JDiagnostics is an extensible, dependency-free library for building
support bundles. It is trivial to deploy in standalone applications,
web services and tools.

It can also be used as a self-diagnosing reflection library, for
example in a test case which needs to call a method on a class not
available at compile-time.

It speeds up development by reducing the number of cycles required
to discover and fix an issue. It is intended to replace ad-hoc print
statements, and encapsulates a library of knowledge about what breaks
Java applications, the knowledge and information required to detect
versioning issues, file checksums, classloader conflicts, missing
features, and so forth.

It is extensible: Any query may be constructed by implementing Query,
and any query-set may be constructed using CompositeQuery. Application
authors may write query-types which are specific to their application
and its compatibility requirements, and thus detect whether
(for example) it has been deployed into a container which has a
not-quite-new-enough version of the I/O framework.

# Comparison with ...

Ad-hoc logging statements require reading and parsing log files, and do
not encapsulate any library of knowledge about previous issues. Imagine
jdiagnostics as a refactored suite of logging statements which
represents the total of knowledge about what needs to be known.

JMX is a lightweight query protocol: It has much lower execution cost
than jdiagnostics and can return real-time information, but it has
a higher cost to write an accessor, and the information returned is
much less rich. It cannot debug versioning problems, exceptions or
classloader problems. Some deployments may create a JMX route which
calls jdiagnostics.

Hystrix is designed to alert the user to the presence of errors
and mitigate their effects on a larger system, but not necessarily
diagnose them. JDiagnostics will help diagnose the faulty component.

Zipkin/Brave is designed to trace behaviour of a large distributed
applications. It can help to find which component in a fault-tolerant
application is misbehaving, but will not necessarily diagnose it.

Basic reflective calls throw exceptions which abort processing
entirely and do not always explain what to do to fix the issue.
Most other reflection libraries have a focus on the success of the
call and the performance of the call, not on diagnosing why it
didn't work.

# Example Output

See [SAMPLE](SAMPLE) for a simple example output.

# How to use?

As a diagnostic bundle:
-----------------------

	LOG.info(new DefaultQuery().call());

Or:

	String report = String.valueOf(new DefaultQuery().call());

Any query may be used individually:

	LOG.info(new ThrowableQuery(t).call());

If you have an exception, DefaultQuery has a utility constructor:

	try {
		...
	} catch (MyException e) {
		try {
			report(new DefaultQuery(e).call());
		} finally {
			throw e;
		}
	}

If you have a web service, for example, spring-mvc:

	@Controller
	public class DebugController {
		@ResponseBody
		@RequestMapping("/jdiagnostics")
		public String  jdiagnostics() {
			return String.valueOf(new DefaultQuery().call());
		}
	}

Or, in Spring MVC, a HandlerExceptionResolver, etc.

For more advanced usage, you can build a CompositeQuery:

	DefaultQuery query = new DefaultQuery();
	query.add(new ThrowableQuery(e));
	query.add(new MySystemStatusQuery(...));
	String dump = String.valueOf(query.call());

As a reflection library:
------------------------

	Object object = ...	// Of an unknown class.
	Result result = new Result();
	Object ret = new MethodExistsCallQuery(object, object.getClass(), "methodName", 
		parameterTypes, arguments).invoke(result, "");
	if (ret != null)
		LOG.info("Call returned " + ret);
	else
		LOG.info("Call failed: " + result);	// Why it failed

Notes:
------

The string output of the routine is not designed for machine parsing,
and may change, but it is designed for human parsing, and readability
patches are welcomed.

# API Documentation

The [JavaDoc API](http://shevek.github.io/jdiagnostics/docs/javadoc/)
is available.

# Guidelines to contributors

I welcome and encourage contributions to detect common issues, versions
of software, and so forth. It would be good to build up this library
of knowledge about the JVM and its environs.

Do not depend on anything which might not be present in a target
environment. Use ClassMethodExistsCallQuery as much as you need.

While the software currently requires JDK1.5, if there are requests
for 1.4 (or a patch!) I will accept it.

# Original success story

I originally wrote this library because of fragility with XInclude
processing in Hadoop, which caused a JUnit test run to output over
350Mb of exceptions, and fail. After a couple of hours, I raised
this issue
	https://github.com/cobertura/cobertura/issues/122
and now I have to debug a Hive/JDO issue, which may involve writing
some more queries.

Nowadays it is increasingly used as the first-solution in test
suites both for class linkage issues and for its ability to do
self-diagnosing reflection.

There will no doubt be others, as this code is now standard for all
applications I am involved in, but whether this README gets updated
each time is less certain.

# Credits

This software was inspired by (amongst others) org.apache.env.Which
from commons-xml and ant -diagnostics.

