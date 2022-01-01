package com.jeffreyorazulike.simpletron.core.utils

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.Register
import org.reflections.Reflections

/**
 * Returns the instances of the specified types in the package of the class set of classes,
 * make sure the classes being instantiated have a no-args constructor
 *
 * @param classes set of classes to search for the given type in their package
 * */
inline fun <reified T> classInstances(classes: Set<Class<*>>): List<T> {
    // retrieve all the required classes defined in the packages of the param [classes]
    return classes.asSequence().map { clazz ->
        Reflections(clazz.packageName).getSubTypesOf(T::class.java)
    }.reduce { acc, otherClasses ->
        // merge all into one set
        acc.union(otherClasses)
    }.map {
        // return instances of each of them
        it.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()
    }
}

/**
 * @param value the number to check for overflow
 * @param action the action to perform if no overflow occurred
 *
 * Checks to see if the given value overflowed the memory
 * */
inline fun CPU.overflow(
    value: Number,
    action: (Number) -> Unit = {}
): Boolean = with(controlUnit) {
    return if (value.toFloat() !in memory.minWord..memory.maxWord) {
        display.show("Memory Overflow${newline()}")
        true
    } else {
        action(value)
        false
    }
}

/**
 * Finds and returns the required register
 * */
inline fun <reified T : Register<*>> List<Register<*>>.findRegister(): T {
    return find { it.name == T::class.simpleName } as T
}

/**
 * Returns a platform dependent newline character
 * */
fun newline(): String = System.lineSeparator()