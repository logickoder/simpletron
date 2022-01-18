package com.logickoder.simpletron.core.contract

/**
 * A [Contract] executor
 *
 * @property executor the class that can be used to effectively handle the contract
 */
interface Contractor<T> {
    val executor: T
}