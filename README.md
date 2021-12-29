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
    NEWLINE                01 xx..
	READ                   10 xx..
	WRITE                  11 xx..
    READ STRING            12 xx..
    WRITE STRING           13 xx..
	LOAD                   20 xx..
	STORE                  21 xx..
	ADD                    30 xx..
	SUBTRACT               31 xx..
	DIVIDE                 32 xx..
	MULTIPY                33 xx..
	MODULUS                34 xx..
	EXPONENT               35 xx..
	BRANCH                 40 xx..
	BRANCH NEG             41 xx..
	BRANCH ZERO            42 xx..
	HALT                   43 xx..

xx.. is the address of the value to operate on. The math operations use the directed value on whatever is in the accumulator.