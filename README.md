XML-Parser
==========

A simple XML Parser written in Java, with some grammar written in xsd and dtd.

Usage
=====
Example: 
```shell
cd bin
java Parser sax ..\XML\RSS\0.92\testFile.xml
```

or

```shell
cd bin
java Parser dom ..\XML\RSS\0.92\testFile.xml
```

This command will validate the `testFile.xml` using the grammar specifed in `textFile.xml` (default xsd).
