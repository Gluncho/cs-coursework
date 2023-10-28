// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// while true:
//     state = 0
//     if KBD != 0:
//         state = -1
//     for SCREEN <= i <= end
//         RAM[i] = state

// initialize variables, 
// end=(address of KBD)
@KBD
D=A
@end
M=D
(INF_LOOP)
    @state
    M=0
    @KBD
    D=M
    @INIT_UPDATE_LOOP
    D;JEQ // if KBD is zero we can skip updating 'state' variable
    @state
    M=-1
(INIT_UPDATE_LOOP) // set i = SCREEN
    @SCREEN
    D=A
    @i
    M=D  
(UPDATE_LOOP)
    @i
    D=M
    @end
    D=M-D  // compute i - end
    @INF_LOOP
    D;JEQ  // if i-end >= 0, we're done filling, need to go to infinite loop
    @state
    D=M    // load state variable
    @i
    A=M
    M=D // ram[i] = state
    @i
    M=M+1  // increment i 
    @UPDATE_LOOP
    0;JMP 

