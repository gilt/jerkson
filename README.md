# Jerkson #

This fork of Jerkson contains patches not found in the original, abandoned, copy. See the [changelog](CHANGELOG.md) for more info.

## Install ##

- This version of Jerkson is hosted on
  [Maven Central](http://central.maven.org/maven2/com/gilt/jerkson_2.10/).
- From sbt:

  ```scala
  libraryDependencies += "com.gilt %% "jerkson" % "0.6.8-2"
  ```
- From Maven:

  ```xml
  <dependency>
    <groupId>com.gilt</groupId>
    <artifactId>jerkson_2.10</artifactId>
    <version>0.6.8-2</version>
  </dependency>
  ```

## Build ##

```sh
$ cd jerkson
$ sbt compile
$ sbt test
```

## Contributing ##

- Write your code, with tests
- Make sure the tests pass
- Update the CHANGELOG.md
- Make a PR!

## Releasing ##

Make sure you update:
- CHANGELOG.md
- Make a release in github
