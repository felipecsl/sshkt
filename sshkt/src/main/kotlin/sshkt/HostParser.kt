package sshkt

abstract class HostParser(val hostString: String) {
  abstract fun suitable(): Boolean
  abstract fun username(): String?
  abstract fun port(): Int
  abstract fun hostname(): String
}