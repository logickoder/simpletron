package com.jeffreyorazulike.simpletron.core.component

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 9:12 PM
 *
 * A contract specifying that a component can be closed
 *
 * @property isClosed true if the component is closed
 * @property closedMessage A simple message showing the closed state of the component
 */
interface Closeable : AutoCloseable {

    val isClosed: Boolean

    val closedMessage get() = "${this::class.simpleName} is${if (!isClosed) " not " else ""}closed"
}