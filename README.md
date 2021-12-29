# SIMPLETRON: CORE

Based on **Deitel's _How to Program_** books (Java, C++, C#) that contain a couple exercises to emulate the other aspects of the programming environment. 

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
    Example: 10099 // Read a word into the memory location 099

When providing instructions, you can separate the opcode and operand,
this will help best for when the amount of blocks the memory installed changes,
internally, it will still be resolved to the complete instruction

For Example: `30 99` will be resolved to `3099` on a 100 block memory, 
`30099` on a 1,000 block memory and `300099` on a 10,000 block memory.

This is best practice because it saves the stress of editing your machine code on 
different memory simpletron instances.

