package sshkt

import net.schmizz.sshj.connection.channel.direct.Session

class Host(val hostname: String, private val session: Session) {
  fun capture(vararg args: String) {

  }

  fun test(vararg args: String) {

  }

  fun execute(vararg args: String) {
    session.exec(args.joinToString())
  }
}