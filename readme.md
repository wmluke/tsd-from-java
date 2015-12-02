# tsd-from-java

A Java utility to generate Typescript type definitions from Java.

## Usage

```bash
$ java -jar tsd-from-java.jar -h

usage: tsd-from-java [-h] -p PACKAGES -m MODULE -o OUTPUT jar

Generate Typescript type definitions from Java

positional arguments:
  jar                    jar to scan

optional arguments:
  -h, --help             show this help message and exit
  -p PACKAGES, --packages PACKAGES
                         Comma separated list of java packages
  -m MODULE, --module MODULE
                         Typescript module
  -o OUTPUT, --output OUTPUT
                         Location of output file
```