package com.logickoder.simpletron.core.contract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:33 PM
 *
 * A [Contract] executor
 *
 * @property executor the class that can be used to effectively handle the contract
 */
interface Contractor<T> {
    val executor: T
}