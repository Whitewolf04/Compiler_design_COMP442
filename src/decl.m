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


% Start of function/class bubbleSort
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 92	% set the stack pointer to the top position of the stack
bubbleSort	res 92
bubbleSort1size	res 4
bubbleSort1n	res 4
bubbleSort1i	res 4
bubbleSort1j	res 4
bubbleSort1temp	res 4
t0	res 4
t1	res 4
t2	res 4
t3	res 4
t4	res 4
t5	res 4
t6	res 4
t7	res 4
t8	res 4
t9	res 4
t10	res 4
t11	res 4
t12	res 4
t13	res 4
t14	res 4
t15	res 4
t16	res 4
t17	res 4
% Assigning size to n
	lw r1,-4(r13)
	sw -8(r13),r1
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -24(r13), r1
% Assigning t0 to i
	lw r1,-24(r13)
	sw -12(r13),r1
% Storing 0 into t1
	addi r1, r0, 0
	addi r1, r0, 0
	sw -28(r13), r1
% Assigning t1 to j
	lw r1,-28(r13)
	sw -16(r13),r1
% Storing 0 into t2
	addi r1, r0, 0
	addi r1, r0, 0
	sw -32(r13), r1
% Assigning t2 to temp
	lw r1,-32(r13)
	sw -20(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 1 into t3
	addi r1, r0, 0
	addi r1, r0, 1
	sw -36(r13), r1
% Subtracting n and t3
	lw r1,-8(r13)
	lw r2,-36(r13)
	sub r3,r1,r2
	sw -40(r13),r3	% Store result into t4
	lw r1,-12(r13)
	lw r2,-40(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% While statement
s1WHILE	addi r11,r0,0
% Storing 1 into t5
	addi r1, r0, 0
	addi r1, r0, 1
	sw -44(r13), r1
% Subtracting n and i
	lw r1,-8(r13)
	lw r2,-12(r13)
	sub r3,r1,r2
	sw -48(r13),r3	% Store result into t6
% Subtracting t6 and t5
	lw r1,-48(r13)
	lw r2,-44(r13)
	sub r3,r1,r2
	sw -52(r13),r3	% Store result into t7
	lw r1,-16(r13)
	lw r2,-52(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s1ENDWHILE
s1STARTWHILE	addi r0,r0,0
% If statement
s2IF	addi r0,r0,0
% Storing 1 into t8
	addi r1, r0, 0
	addi r1, r0, 1
	sw -56(r13), r1
% Adding j and t8
	lw r1,-16(r13)
	lw r2,-56(r13)
	add r3,r1,r2
	sw -60(r13),r3	% Store result into t9
	bnz r11,s2THEN
	j s2ELSE
s2THEN	align
% Assigning null to temp
	lw r1,99999999(r13)
	sw -20(r13),r1
% Storing 1 into t10
	addi r1, r0, 0
	addi r1, r0, 1
	sw -64(r13), r1
% Adding j and t10
	lw r1,-16(r13)
	lw r2,-64(r13)
	add r3,r1,r2
	sw -68(r13),r3	% Store result into t11
% Storing 1 into t12
	addi r1, r0, 0
	addi r1, r0, 1
	sw -72(r13), r1
% Adding j and t12
	lw r1,-16(r13)
	lw r2,-72(r13)
	add r3,r1,r2
	sw -76(r13),r3	% Store result into t13
	j s2ENDIF
s2ELSE	addi r0,r0,0
s2ENDIF	addi r0,r0,0
% Storing 1 into t14
	addi r1, r0, 0
	addi r1, r0, 1
	sw -80(r13), r1
% Adding j and t14
	lw r1,-16(r13)
	lw r2,-80(r13)
	add r3,r1,r2
	sw -84(r13),r3	% Store result into t15
% Assigning t15 to j
	lw r1,-84(r13)
	sw -16(r13),r1
	j s1WHILE
s1ENDWHILE	addi r0,r0,0
% Storing 1 into t16
	addi r1, r0, 0
	addi r1, r0, 1
	sw -88(r13), r1
% Adding i and t16
	lw r1,-12(r13)
	lw r2,-88(r13)
	add r3,r1,r2
	sw -92(r13),r3	% Store result into t17
% Assigning t17 to i
	lw r1,-92(r13)
	sw -12(r13),r1
	j s0WHILE
s0ENDWHILE	addi r0,r0,0


% Start of function/class printArray
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 24	% set the stack pointer to the top position of the stack
printArray	res 24
printArray1size	res 4
printArray1n	res 4
printArray1i	res 4
t18	res 4
t19	res 4
t20	res 4
% Assigning size to n
	lw r1,-4(r13)
	sw -8(r13),r1
% Storing 0 into t18
	addi r1, r0, 0
	addi r1, r0, 0
	sw -16(r13), r1
% Assigning t18 to i
	lw r1,-16(r13)
	sw -12(r13),r1
% While statement
s3WHILE	addi r11,r0,0
	lw r1,-12(r13)
	lw r2,-8(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s3ENDWHILE
s3STARTWHILE	addi r0,r0,0
% Storing 1 into t19
	addi r1, r0, 0
	addi r1, r0, 1
	sw -20(r13), r1
% Adding i and t19
	lw r1,-12(r13)
	lw r2,-20(r13)
	add r3,r1,r2
	sw -24(r13),r3	% Store result into t20
% Assigning t20 to i
	lw r1,-24(r13)
	sw -12(r13),r1
	j s3WHILE
s3ENDWHILE	addi r0,r0,0


% Start of function/class main
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 96	% set the stack pointer to the top position of the stack
main	res 96
main1arr	res 28
t21	res 4
t22	res 4
t23	res 4
t24	res 4
t25	res 4
t26	res 4
t27	res 4
t28	res 4
t29	res 4
t30	res 4
t31	res 4
t32	res 4
t33	res 4
t34	res 4
t35	res 4
t36	res 4
t37	res 4
% Storing 0 into t21
	addi r1, r0, 0
	addi r1, r0, 0
	sw -32(r13), r1
% Storing 64 into t22
	addi r1, r0, 0
	addi r1, r0, 64
	sw -36(r13), r1
% Storing 1 into t23
	addi r1, r0, 0
	addi r1, r0, 1
	sw -40(r13), r1
% Storing 34 into t24
	addi r1, r0, 0
	addi r1, r0, 34
	sw -44(r13), r1
% Storing 2 into t25
	addi r1, r0, 0
	addi r1, r0, 2
	sw -48(r13), r1
% Storing 25 into t26
	addi r1, r0, 0
	addi r1, r0, 25
	sw -52(r13), r1
% Storing 3 into t27
	addi r1, r0, 0
	addi r1, r0, 3
	sw -56(r13), r1
% Storing 12 into t28
	addi r1, r0, 0
	addi r1, r0, 12
	sw -60(r13), r1
% Storing 4 into t29
	addi r1, r0, 0
	addi r1, r0, 4
	sw -64(r13), r1
% Storing 22 into t30
	addi r1, r0, 0
	addi r1, r0, 22
	sw -68(r13), r1
% Storing 5 into t31
	addi r1, r0, 0
	addi r1, r0, 5
	sw -72(r13), r1
% Storing 11 into t32
	addi r1, r0, 0
	addi r1, r0, 11
	sw -76(r13), r1
% Storing 6 into t33
	addi r1, r0, 0
	addi r1, r0, 6
	sw -80(r13), r1
% Storing 90 into t34
	addi r1, r0, 0
	addi r1, r0, 90
	sw -84(r13), r1
% Storing 7 into t35
	addi r1, r0, 0
	addi r1, r0, 7
	sw -88(r13), r1
% Storing 7 into t36
	addi r1, r0, 0
	addi r1, r0, 7
	sw -92(r13), r1
% Storing 7 into t37
	addi r1, r0, 0
	addi r1, r0, 7
	sw -96(r13), r1
