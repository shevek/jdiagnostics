# Introduction

You have a large complex application. Something breaks. What do
you print?

Your code is deployed in a server farm or a customer site. You need
a support bundle. What do you grab?

See SAMPLE for a simple example output.

# How to use?

Basic version:

	LOG.info(new DefaultQuery().call());

Or:

	String dump = String.valueOf(new DefaultQuery().call());

If you have an exception:

	try {
		...
	} catch (MyException e) {
		try {
			submit(new DefaultQuery(e).call());
		} finally {
			throw e;
		}
	}

For more advanced information:

	DefaultQuery query = new DefaultQuery();
	query.add(new ThrowableQuery(e));
	query.add(new MySystemStatusQuery(...));
	String dump = String.valueOf(query.call());

The string output of the routine is not designed for machine parsing.

# Guidelines to contributors

I welcome and encourage contributions to detect common issues,
versions of software, and so forth.

Do not depend on anything which might not be present in a target
environment. Use ClassMethodExistsCallQuery as much as you need.

# Success stories

I originally wrote this library because of fragility with XInclude
processing in Hadoop, which caused a JUnit test run to output over
350Mb of exceptions, and fail. After a couple of hours, I raised
this issue
	https://github.com/cobertura/cobertura/issues/122
and now I have to debug a Hive/JDO issue, which may involve writing
some more queries.

# Credits

This software was inspired by (amongst others) org.apache.env.Which
from commons-xml and ant -diagnostics.

