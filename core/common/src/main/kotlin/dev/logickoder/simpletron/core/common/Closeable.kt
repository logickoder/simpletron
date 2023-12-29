package dev.logickoder.simpletron.core.common

/**
 * A contract specifying that a component can be closed
 *
 * @property isClosed true if the component is closed
 */
interface Closeable : AutoCloseable {

    val isClosed: Boolean
}

/**
 * A simple message showing the closed state of the component
 */
val Closeable.status get() = "${this::class.simpleName} is ${if (!isClosed) "not " else ""}closed"