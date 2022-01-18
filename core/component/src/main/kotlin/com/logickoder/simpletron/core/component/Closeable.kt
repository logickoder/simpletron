package com.logickoder.simpletron.core.component

/**
 * A contract specifying that a component can be closed
 *
 * @property isClosed true if the component is closed
 * @property closedMessage A simple message showing the closed state of the component
 */
interface Closeable : AutoCloseable {

    val isClosed: Boolean

    val closedMessage get() = "${this::class.simpleName} is${if (!isClosed) " not " else ""}closed"
}