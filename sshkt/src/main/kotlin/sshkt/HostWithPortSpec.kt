package sshkt

internal class HostWithPortSpec(hostString: String) : HostSpec(hostString) {
  override fun username() =
      null

  override fun port() =
      hostString.split(":")[1].toInt()

  override fun hostname() =
      hostString.split(":")[0]

  override fun suitable() =
      !hostString.contains(Regex("[@\\[\\]]"))
}