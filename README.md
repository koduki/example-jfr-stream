# README

Examples of JFR event streaming API.

ref:
- [JFRとモニタリングの融合？ JEP 349: JFR Event Streamingを触ってみる](https://zenn.dev/koduki/articles/42c98a330bf25c)

# examples

Hello World code

```bash
$ java src/main/java/examples/Example01Sync.java
$ java src/main/java/examples/Example02Async.java
$ java src/main/java/examples/Example03CustomEvent.java
```

# example_app

Quarkus + Micrometer

```bash
$ ./mvnw compile quarkus:dev
```