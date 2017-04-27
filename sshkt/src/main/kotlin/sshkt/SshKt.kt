package sshkt

import net.schmizz.sshj.SSHClient

class SshKt(private val ssh: SSHClient = SSHClient()) {
  fun on(servers: List<String>, block: (Host) -> Unit) {
    servers.forEach { host ->
      ssh.connect(host)
      try {
        ssh.authPublickey("root")
        ssh.startSession().use { session ->
          block.invoke(Host(host, session))
        }
      } finally {
        ssh.disconnect()
      }
    }
  }
}