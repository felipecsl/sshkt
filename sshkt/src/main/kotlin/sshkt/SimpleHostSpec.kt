package sshkt

import net.schmizz.sshj.SSHClient

internal class SimpleHostSpec(hostString: String) : HostSpec(hostString) {
  override fun suitable() =
      !hostString.contains(Regex("[:@]"))

  override fun username() = null

  override fun port() = SSHClient.DEFAULT_PORT

  override fun hostname() = hostString
}