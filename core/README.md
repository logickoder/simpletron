# SIMPLETRON: CORE

[![](https://jitpack.io/v/jeffreyorazulike/simpletron-core.svg)](https://jitpack.io/#jeffreyorazulike/simpletron-core)

Based on **Deitel's _How to Program_** books (Java, C++, C#) that contain a couple exercises to emulate the other
aspects of the programming environment.

The core part of simpletron contains the building blocks of simpletron that makes it work

## CORE ISA

#### The default components are:

- A memory of 1,000 blocks
- CPU
- A commandline input and display
- A file input and display

#### The default registers are:
- Accumulator
- Operation Code
- Operand
- Instruction Counter
- Instruction Register

#### The default instructions are:
    NEWLINE                01xx..
	READ                   10xx..
	WRITE                  11xx..
    READ STRING            12xx..
    WRITE STRING           13xx..
	LOAD                   20xx..
	STORE                  21xx..
	ADD                    30xx..
	SUBTRACT               31xx..
	DIVIDE                 32xx..
	MULTIPY                33xx..
	MODULUS                34xx..
	EXPONENT               35xx..
	BRANCH                 40xx..
	BRANCH NEG             41xx..
	BRANCH ZERO            42xx..
	HALT                   43xx..

    xx.. is the address of the value to operate on. The math operations use the directed value on whatever is in the accumulator.
    Example: 1099 // Read a word into the memory location 099

## Contract
Contracts are the base of simpletron execution, contracts tell simpletron the sequence to follow during execution. 