% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

% Utils:
% Write an int to stdout.
% Entry: -4(r10) -> int/float argument
putint	lw r1,-8(r10)
		addi r2,r0,0
putint1	lb r2,0(r1)
		ceqi r3,r2,0
		bnz r3,putint2
		putc r2
		addi r1,r1,1
		j putint1
putint2	jr r15

% Read a string from stdin
% Entry: -4(r10) -> buffer
getint	lw r1,-4(r10)
getint1	getc r2
	ceqi r3,r2,10
	bnz r3,getint2
	sb 0(r1),r2
	addi r1,r1,1
	j getint1
getint2	sb 0(r1),r0
	jr r15

% Convert string to integer. Skip leading blanks. Accept leading sign
% Entry: -4(r10) -> string
% Exit: result in r11
strint	addi r11,r0,0
	addi r4,r0,0
	lw r1,-4(r10)
	addi r2,r0,0
strint1	lb r2,0(r1)
cnei	r3,r2,32
	bnz r3,strint2
	addi r1,r1,1
	j strint1
strint2	cnei r3,r2,43
	bnz r3,strint3
	j strint4
strint3	cnei r3,r2,45
	bnz r3,strint5
	addi r4,r4,1
strint4	addi r1,r1,1
	lb r2,0(r1)
strint5	clti r3,r2,48
	bnz r3,strint6
	cgti r3,r2,57
	bnzr3,strint6
	subi r2,r2,48
	muli r11,r11,10
	add r11, r11, r2
	j strint4
strint6	ceqi r3,r4,0
	bnz r3,strint7
	sub r11,r0,r11
strint7	jr r15


% Start of class POLYNOMIAL
POLYNOMIAL	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 0	% set the stack pointer to the top position of the stack


% Start of class RANDOM
RANDOM	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 4	% set the stack pointer to the top position of the stack


% Start of class LINEAR
LINEAR	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 8	% set the stack pointer to the top position of the stack


% Start of class QUADRATIC
QUADRATIC	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 12	% set the stack pointer to the top position of the stack


% Start of function POLYNOMIAL::evaluate
POLYNOMIAL::evaluate	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 16	% set the stack pointer to the top position of the stack
	sw -12(r13),r15	% Put link onto stack frame
	sw -8(r13),r12	% Store the calling object into the self pointer
	sw -4(r13),r1	% Storing parameter x into stack frame
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -8(r13), r1
	lw r1,-8(r13)	% Get the return value t0
	sw 0(r13),r1
	lw r15,-12(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::evaluate
QUADRATIC::evaluate	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 48	% set the stack pointer to the top position of the stack
	sw -44(r13),r15	% Put link onto stack frame
	sw -12(r13),r12	% Store the calling object into the self pointer
	sw -4(r13),r1	% Storing parameter x into stack frame
% Load the calling object from class QUADRATIC
	addi r1,r0,0
	addi r1,r0,0	% Get offset of member variable a
	addi r1,r0,-12	% Get offset of member variable a in object
	sw objectOffsetBuf(r0),r1
% Assigning a to result
	lw r1,objectOffsetBuf(r13)
	sw -8(r13),r1
% Multiplying result and x
	lw r1,-8(r13)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -24(r13),r3	% Store result into t1
% Load the calling object from class QUADRATIC
	addi r1,r0,0
	addi r1,r0,-4	% Get offset of member variable b
	addi r1,r0,-12	% Get offset of member variable b in object
	sw objectOffsetBuf(r0),r1
% Adding t1 and b
	lw r1,-24(r13)
	lw r2,objectOffsetBuf(r13)
	add r3,r1,r2
	sw -28(r13),r3	% Store result into t2
% Assigning t2 to result
	lw r1,-28(r13)
	sw -8(r13),r1
% Multiplying result and x
	lw r1,-8(r13)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -32(r13),r3	% Store result into t3
% Load the calling object from class QUADRATIC
	addi r1,r0,0
	addi r1,r0,-8	% Get offset of member variable c
	addi r1,r0,-12	% Get offset of member variable c in object
	sw objectOffsetBuf(r0),r1
% Adding t3 and c
	lw r1,-32(r13)
	lw r2,objectOffsetBuf(r13)
	add r3,r1,r2
	sw -36(r13),r3	% Store result into t4
% Assigning t4 to result
	lw r1,-36(r13)
	sw -8(r13),r1
	lw r1,-8(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-44(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC::constructor	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 28	% set the stack pointer to the top position of the stack
	sw -24(r13),r15	% Put link onto stack frame
	sw -12(r13),r12	% Store the calling object into the self pointer
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -4(r13),r2	% Storing parameter B into stack frame
	sw -8(r13),r3	% Storing parameter C into stack frame
	lw r15,-24(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC::constructor	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 20	% set the stack pointer to the top position of the stack
	sw -16(r13),r15	% Put link onto stack frame
	sw -4(r13),r12	% Store the calling object into the self pointer
	sw 0(r13),r1	% Storing parameter A into stack frame
	lw r15,-16(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::constructor
LINEAR::constructor	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 20	% set the stack pointer to the top position of the stack
	sw -16(r13),r15	% Put link onto stack frame
	sw -8(r13),r12	% Store the calling object into the self pointer
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -4(r13),r2	% Storing parameter B into stack frame
	lw r15,-16(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::evaluate
LINEAR::evaluate	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 36	% set the stack pointer to the top position of the stack
	sw -32(r13),r15	% Put link onto stack frame
	sw -12(r13),r12	% Store the calling object into the self pointer
	sw -4(r13),r1	% Storing parameter x into stack frame
% Storing 0 into t6
	addi r1, r0, 0
	addi r1, r0, 0
	sw -20(r13), r1
% Assigning t6 to result
	lw r1,-20(r13)
	sw -8(r13),r1
% Load the calling object from class LINEAR
	addi r1,r0,0
	addi r1,r0,0	% Get offset of member variable a
	addi r1,r0,-12	% Get offset of member variable a in object
	sw objectOffsetBuf(r0),r1
% Multiplying a and x
	lw r1,objectOffsetBuf(r13)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -24(r13),r3	% Store result into t7
% Load the calling object from class LINEAR
	addi r1,r0,0
	addi r1,r0,-4	% Get offset of member variable b
	addi r1,r0,-12	% Get offset of member variable b in object
	sw objectOffsetBuf(r0),r1
% Adding t7 and b
	lw r1,-24(r13)
	lw r2,objectOffsetBuf(r13)
	add r3,r1,r2
	sw -28(r13),r3	% Store result into t8
% Assigning t8 to result
	lw r1,-28(r13)
	sw -8(r13),r1
	lw r1,-8(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-32(r13)
	jr r15	% Jump back to the calling function


% Start of function count
count	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 12	% set the stack pointer to the top position of the stack
	sw -8(r13),r15	% Put link onto stack frame
	sw -4(r13),r1	% Storing parameter x into stack frame
	lw r1,-4(r13)	% Get the return value x
	sw 0(r13),r1
	lw r15,-8(r13)
	jr r15	% Jump back to the calling function


% Start of function main
main	nop
	entry
	align
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 68	% set the stack pointer to the top position of the stack
	sw -64(r13),r15	% Put link onto stack frame
% Storing 2 into t9
	addi r1, r0, 0
	addi r1, r0, 2
	sw -28(r13), r1
% Storing 3 into t10
	addi r1, r0, 0
	addi r1, r0, 3
	sw -32(r13), r1
% Calling member function LINEAR::constructor
	lw r1,-28(r13)	% Load parameter t9
	lw r2,-32(r13)	% Load parameter t10
	subi r13,r13,20
	subi r14,r14,20
	lw r12,f1(r0)	% Load object address onto r12
	jl r15,LINEAR::constructor
	addi r13,r13,20
	addi r14,r14,20
% Storing 2 into t11
	addi r1, r0, 0
	addi r1, r0, 2
	sw -36(r13), r1
% Storing 1 into t13
	addi r1, r0, 0
	addi r1, r0, 1
	sw -44(r13), r1
% Storing 0 into t14
	addi r1, r0, 0
	addi r1, r0, 0
	sw -48(r13), r1
% Calling member function QUADRATIC::constructor
	lw r1,-36(r13)	% Load parameter t11
	lw r2,-44(r13)	% Load parameter t13
	lw r3,-48(r13)	% Load parameter t14
	subi r13,r13,28
	subi r14,r14,28
	lw r12,f2(r0)	% Load object address onto r12
	jl r15,QUADRATIC::constructor
	addi r13,r13,28
	addi r14,r14,28
% Storing 2 into t15
	addi r1, r0, 0
	addi r1, r0, 2
	sw -52(r13), r1
% Storing 1 into t16
	addi r1, r0, 0
	addi r1, r0, 1
	sw -56(r13), r1
% Assigning t16 to counter
	lw r1,-56(r13)
	sw -20(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 10 into t17
	addi r1, r0, 0
	addi r1, r0, 10
	sw -60(r13), r1
% Comparision between counter and t17
	lw r1,-20(r13)
	lw r2,-60(r13)
	sub r3,r1,r2
	clei r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% Printing counter to console
	lw r10,-20(r13)
	jl r15,putint
% Printing null to console
	lw r10,99999999(r13)
	jl r15,putint
% Printing null to console
	lw r10,99999999(r13)
	jl r15,putint
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
	lw r15,-64(r13)
	jr r15	% Jump back to the calling function

% End of program, declaring variables
objectOffsetBuf	res 4
t1	res 4
t2	res 4
t3	res 4
t4	res 4
t5	res 4
t7	res 4
t8	res 4
f1	res 8
f2	res 12
t12	res 4
