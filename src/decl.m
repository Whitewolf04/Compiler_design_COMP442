% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

% Start of function man
man	nop
	subi r13,r13,20	% set the stack pointer to the top position of the stack
	sw -16(r13),r15	% Put link onto stack frame
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
