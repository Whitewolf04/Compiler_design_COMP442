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


% Start of function/class POLYNOMIAL
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 0	% set the stack pointer to the top position of the stack
POLYNOMIAL	res 0


% Start of function/class RANDOM
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 4	% set the stack pointer to the top position of the stack
RANDOM	res 4


% Start of function/class LINEAR
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 8	% set the stack pointer to the top position of the stack
LINEAR	res 8


% Start of function/class QUADRATIC
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 12	% set the stack pointer to the top position of the stack
QUADRATIC	res 12


% Start of function/class POLYNOMIAL::evaluate
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 16	% set the stack pointer to the top position of the stack
	sw -12(r13),r15	% Put link onto stack frame
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -8(r13), r1
POLYNOMIAL::evaluate	res 16


% Start of function/class QUADRATIC::evaluate
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 36	% set the stack pointer to the top position of the stack
	sw -32(r13),r15	% Put link onto stack frame
% Assigning a to result
	lw r1,0(r13)
	sw -4(r13),r1
% Multiplying result and x
	lw r1,-4(r13)
	lw r2,0(r13)
	mul r3,r1,r2
	sw -12(r13),r3	% Store result into t1
% Adding t1 and b
	lw r1,-12(r13)
	lw r2,-4(r13)
	add r3,r1,r2
	sw -16(r13),r3	% Store result into t2
% Assigning t2 to result
	lw r1,-16(r13)
	sw -4(r13),r1
% Multiplying result and x
	lw r1,-4(r13)
	lw r2,0(r13)
	mul r3,r1,r2
	sw -20(r13),r3	% Store result into t3
% Adding t3 and c
	lw r1,-20(r13)
	lw r2,-8(r13)
	add r3,r1,r2
	sw -24(r13),r3	% Store result into t4
% Assigning t4 to result
	lw r1,-24(r13)
	sw -4(r13),r1
QUADRATIC::evaluate	res 36


% Start of function/class QUADRATIC::constructor
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 16	% set the stack pointer to the top position of the stack
	sw -12(r13),r15	% Put link onto stack frame
QUADRATIC::constructor	res 16


% Start of function/class QUADRATIC::constructor
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 8	% set the stack pointer to the top position of the stack
	sw -4(r13),r15	% Put link onto stack frame
QUADRATIC::constructor	res 8


% Start of function/class LINEAR::constructor
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 12	% set the stack pointer to the top position of the stack
	sw -8(r13),r15	% Put link onto stack frame
LINEAR::constructor	res 12


% Start of function/class LINEAR::evaluate
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 28	% set the stack pointer to the top position of the stack
	sw -24(r13),r15	% Put link onto stack frame
% Storing 0 into t6
	addi r1, r0, 0
	addi r1, r0, 0
	sw -12(r13), r1
% Assigning t6 to result
	lw r1,-12(r13)
	sw -4(r13),r1
% Multiplying a and x
	lw r1,0(r13)
	lw r2,0(r13)
	mul r3,r1,r2
	sw -16(r13),r3	% Store result into t7
% Adding t7 and b
	lw r1,-16(r13)
	lw r2,-4(r13)
	add r3,r1,r2
	sw -20(r13),r3	% Store result into t8
% Assigning t8 to result
	lw r1,-20(r13)
	sw -4(r13),r1
LINEAR::evaluate	res 28


% Start of function/class count
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 12	% set the stack pointer to the top position of the stack
	sw -8(r13),r15	% Put link onto stack frame
count	res 12


% Start of function/class main
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
	lw r10,null(r13)
	jl r15,putint
% Printing null to console
	lw r10,null(r13)
	jl r15,putint
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
main	res 68
