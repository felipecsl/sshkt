package sshkt

internal class HostWithUsernameAndPortParser(hostString: String) : HostParser(hostString) {
  override fun username() =
      hostString.split(Regex("[:@]"))[0]

  override fun port() =
      hostString.split(Regex("[:@]"))[2].toInt()

  override fun hostname() =
      hostString.split(Regex("[:@]"))[1]

  override fun suitable() =
      hostString.contains(Regex("@.*:\\d+"))
}