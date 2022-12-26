"C:\Program Files (x86)\Microsoft Visual Studio\2017\BuildTools\VC\Auxiliary\Build\vcvars64.bat"

Set JAVA_HOME=...\graalvm-ce-java11-22.3.0

Set GRAALVM_HOME=...\graalvm-ce-java11-22.3.0

Set GRAALVM_VERSION=22.3.0

Set GRAAL_VERSION=22.3.0

Set JAVA_VERSION=11

mvn -B --file "...\pom.xml" -Pnative package
