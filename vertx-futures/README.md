# maven-slf4j-main-starter
Maven application project starter using java 23, maven wrapper, slf4j, assertj

## Getting Started

```bash
npx --yes degit daggerok/maven-slf4j-main-starter my-app && cd $_
```

## Test Build Run

```bash
./mvnw clean ; ./mvnw ; java -jar target/*.jar
```

## Setup Development Environment

Java 23

```bash
brew reinstall zulu # zulu@23

declare -f use # output:
function use() {
	function usage() {
		echo "Usage:\n\tuse jdk 1.8\nor:\n\tuse graalvm 21"
		return
	}
	if [[ $# -eq 0 ]] ; then
		usage
		return -1
	fi
	if [[ $# -eq 2 ]] ; then
		USE_WHAT=${1:-jdk}
		if [[ "jdk" != "$USE_WHAT" && "graalvm" != "$USE_WHAT" ]] ; then
			usage
			return -2
		fi
		export JAVA_VERSION=${2:-21}
	else
		export JAVA_VERSION=${1:-21}
	fi
	if [[ "graalvm" == "$USE_WHAT" ]] ; then
		export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-$JAVA_VERSION.jdk/Contents/Home
	else
		export JAVA_HOME=$(/usr/libexec/java_home -v $JAVA_VERSION)
	fi
	export PATH=$JAVA_HOME/bin:$PATH
	return 0
}
```

## RTFM
- https://medium.com/@aribooluwatoba/avoiding-nested-callback-hell-in-reactive-operations-using-vert-x-7af7ac99754e
