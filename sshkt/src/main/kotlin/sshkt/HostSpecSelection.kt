package sshkt

internal object HostSpecSelection {
  private val HOST_SPECS: List<Class<out HostSpec>> = listOf(
      SimpleHostSpec::class.java,
      HostWithPortSpec::class.java,
      HostWithUsernameSpec::class.java,
      HostWithUsernameAndPortSpec::class.java)

  internal fun parse(hostString: String): HostSpec =
      HOST_SPECS
          .map { it.constructors[0] }
          .map { it.newInstance(hostString) as HostSpec }
          .firstOrNull { it.suitable() }
          ?: throw UnparseableHostStringException("Cannot parse host string $hostString")
}