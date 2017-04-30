package sshkt

internal abstract class HostSpec(val hostString: String) {
  internal abstract fun suitable(): Boolean
  internal abstract fun username(): String?
  internal abstract fun port(): Int
  internal abstract fun hostname(): String
}