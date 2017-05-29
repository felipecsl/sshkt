# sshkt

A port of [sshkit](https://github.com/capistrano/sshkit) to Kotlin.

## Usage

```kotlin
SshKt() {
  on("1.example.com", "2.example.com") {
    within("/opt/sites/example.com") {
      asUser("deploy") {
        execute('node socket_server.js')
      }
    }
  }
}
```

## Running the tests 

```
cd sshkt
vagrant provision
vagrant up
cd .. && ./gradlew test
```