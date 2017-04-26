package sshkt

import net.schmizz.sshj.SSHClient

class SshKt(private val ssh: SSHClient = SSHClient()) {
  fun on(servers: List<String>, block: (Host) -> Unit) {
    block.invoke(Host(ssh.startSession()))
  }
}