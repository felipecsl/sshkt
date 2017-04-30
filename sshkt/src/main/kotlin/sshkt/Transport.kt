package sshkt

interface Transport {
  fun capture(vararg args: String): String
  fun execute(vararg args: String): Int
  fun test(vararg args: String): Boolean
}