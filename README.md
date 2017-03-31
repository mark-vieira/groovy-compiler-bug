This repository hold an example for recreating a potential Groovy compiler bug. The issue is that for a given set of
source the compile bytecode differs. The test works by compiling a simple Groovy class using `@CompileStatic` and
comparing the compiled output against a reference copy. The test iterates multiple times, with some iterations passing
and other failing. This indicates that compiler output differs between runs.

    # To run the tests
    $ ./gradlew test