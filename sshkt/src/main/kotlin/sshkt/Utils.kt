package sshkt

import java.util.*

internal fun <T> linkedListOf(elements: List<T>): LinkedList<T> = LinkedList(elements)

internal fun <T> linkedListOf(): LinkedList<T> = LinkedList()