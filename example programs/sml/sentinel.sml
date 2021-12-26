// write a sentinel controlled loop to read 10 positive numbers, the sentinel must also be positive
// make sure to remove all other things from this file except the instructions, each instruction must be on a seperate line and -99999 should end each file
00 - 1099 // read the sentinel
01 - 1098 // read the users input
02 - 2098 // load users input to accumulator
03 - 4101 // if it is negative, let the user input the number again
04 - 3199 // subtract the sentinel from the users input
05 - 4210 // jump to the end of the program if the user typed in the sentinel
06 - 2098 // load the users input to the accumulator
07 - 3097 // add the users input to total
08 - 2197 // store the total
09 - 4001 // jump back to the start of the loop
10 - 1197 // display the total
11 - 0100 // display a new line
12 - 4300 // end the program