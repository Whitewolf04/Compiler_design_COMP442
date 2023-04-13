% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

% Start of function main
main	nop
	entry
	align
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r13, r13, 32	% set the stack pointer to the top position of the stack
% Storing 1 into t0
	addi r1, r0, 0
	addi r1, r0, 1
	sw -8(r13), r1
% Assigning t0 to x
	lw r1,-8(r13)
	sw 0(r13),r1
% Storing 2 into t1
	addi r1, r0, 0
	addi r1, r0, 2
	sw -12(r13), r1
% Assigning t1 to y
	lw r1,-12(r13)
	sw -4(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 10 into t2
	addi r1, r0, 0
	addi r1, r0, 10
	sw -16(r13), r1
% Comparision between x and t2
	lw r1,0(r13)
	lw r2,-16(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% Storing 2 into t3
	addi r1, r0, 0
	addi r1, r0, 2
	sw -20(r13), r1
% Multiplying x and t3
	lw r1,0(r13)
	lw r2,-20(r13)
	mul r3,r1,r2
	sw -24(r13),r3	% Store result into t4
% Assigning t4 to x
	lw r1,-24(r13)
	sw 0(r13),r1
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
% Printing x to console
	lw r1,0(r13)
	jl r15,putint
	hlt

% End of program, declaring variables
t4	res 4
