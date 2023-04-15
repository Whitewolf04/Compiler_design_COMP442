% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

% Start of function man
man	nop
	subi r13,r13,24	% set the stack pointer to the top position of the stack
	sw -20(r13),r15	% Put link onto stack frame
% If statement
s0IF	addi r0,r0,0
% Comparision between x and y
	lw r1,0(r13)
	lw r2,-4(r13)
	sub r3,r1,r2
	ceqi r11,r3,0
	bnz r11,s0THEN
	j s0ELSE
s0THEN	align
% Storing 1 into t0
	addi r1, r0, 0
	addi r1, r0, 1
	sw -16(r13), r1
% Assigning t0 to z
	lw r1,-16(r13)
	sw -12(r13),r1
	j s0ENDIF
s0ELSE	addi r0,r0,0
s0ENDIF	addi r0,r0,0
	lw r15,-20(r13)
	jr r15	% Jump back to the calling function
	hlt

% End of program, declaring variables
