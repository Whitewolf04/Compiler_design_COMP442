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


% Start of function bubbleSort
bubbleSort	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 140	% set the stack pointer to the top position of the stack
	sw -136(r13),r15	% Put link onto stack frame
	sw 0(r13),r1	% Storing parameter arr into stack frame
	sw -28(r13),r2	% Storing parameter size into stack frame
% Assigning size to n
	lw r1,-28(r13)
	sw -32(r13),r1
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -48(r13), r1
% Assigning t0 to i
	lw r1,-48(r13)
	sw -36(r13),r1
% Storing 0 into t1
	addi r1, r0, 0
	addi r1, r0, 0
	sw -52(r13), r1
% Assigning t1 to j
	lw r1,-52(r13)
	sw -40(r13),r1
% Storing 0 into t2
	addi r1, r0, 0
	addi r1, r0, 0
	sw -56(r13), r1
% Assigning t2 to temp
	lw r1,-56(r13)
	sw -44(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 1 into t3
	addi r1, r0, 0
	addi r1, r0, 1
	sw -60(r13), r1
% Subtracting n and t3
	lw r1,-32(r13)
	lw r2,-60(r13)
	sub r3,r1,r2
	sw -64(r13),r3	% Store result into t4
% Comparision between i and t4
	lw r1,-36(r13)
	lw r2,-64(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% While statement
s1WHILE	addi r11,r0,0
% Storing 1 into t5
	addi r1, r0, 0
	addi r1, r0, 1
	sw -68(r13), r1
% Subtracting n and i
	lw r1,-32(r13)
	lw r2,-36(r13)
	sub r3,r1,r2
	sw -72(r13),r3	% Store result into t6
% Subtracting t6 and t5
	lw r1,-72(r13)
	lw r2,-68(r13)
	sub r3,r1,r2
	sw -76(r13),r3	% Store result into t7
% Comparision between j and t7
	lw r1,-40(r13)
	lw r2,-76(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s1ENDWHILE
s1STARTWHILE	addi r0,r0,0
% If statement
s2IF	addi r0,r0,0
% Offsetting array arr
	subi r9,r0,0
	lw r1,-40(r13)	% Loading index j
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Store the array offset in a temp variable t8
	lw r1,arrayOffsetBuf(r0)
	sw -80(r13),r1
% Storing 1 into t9
	addi r1, r0, 0
	addi r1, r0, 1
	sw -84(r13), r1
% Adding j and t9
	lw r1,-40(r13)
	lw r2,-84(r13)
	add r3,r1,r2
	sw -88(r13),r3	% Store result into t10
% Offsetting array arr
	subi r9,r0,0
	lw r1,-88(r13)	% Loading index t10
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Store the array offset in a temp variable t11
	lw r1,arrayOffsetBuf(r0)
	sw -92(r13),r1
% Comparision between t8 and t11
	lw r1,t8(r13)
	lw r2,t11(r13)
	sub r3,r1,r2
	cgti r11,r3,0
	bnz r11,s2THEN
	j s2ELSE
s2THEN	align
% Offsetting array arr
	subi r9,r0,0
	lw r1,-40(r13)	% Loading index j
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Store the array offset in a temp variable t12
	lw r1,arrayOffsetBuf(r0)
	sw -96(r13),r1
% Assigning t12 to temp
	lw r1,t12(r13)
	sw -44(r13),r1
% Storing 1 into t13
	addi r1, r0, 0
	addi r1, r0, 1
	sw -100(r13), r1
% Adding j and t13
	lw r1,-40(r13)
	lw r2,-100(r13)
	add r3,r1,r2
	sw -104(r13),r3	% Store result into t14
% Offsetting array arr
	subi r9,r0,0
	lw r1,-104(r13)	% Loading index t14
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Store the array offset in a temp variable t15
	lw r1,arrayOffsetBuf(r0)
	sw -108(r13),r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-40(r13)	% Loading index j
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t15 into arr
	lw r1,t15(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 1 into t16
	addi r1, r0, 0
	addi r1, r0, 1
	sw -112(r13), r1
% Adding j and t16
	lw r1,-40(r13)
	lw r2,-112(r13)
	add r3,r1,r2
	sw -116(r13),r3	% Store result into t17
% Offsetting array arr
	subi r9,r0,0
	lw r1,-116(r13)	% Loading index t17
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing temp into arr
	lw r1,-44(r13)
	sw arrayOffsetBuf(r13),r1
	j s2ENDIF
s2ELSE	addi r0,r0,0
s2ENDIF	addi r0,r0,0
% Storing 1 into t18
	addi r1, r0, 0
	addi r1, r0, 1
	sw -120(r13), r1
% Adding j and t18
	lw r1,-40(r13)
	lw r2,-120(r13)
	add r3,r1,r2
	sw -124(r13),r3	% Store result into t19
% Assigning t19 to j
	lw r1,-124(r13)
	sw -40(r13),r1
	j s1WHILE
s1ENDWHILE	addi r0,r0,0
% Storing 1 into t20
	addi r1, r0, 0
	addi r1, r0, 1
	sw -128(r13), r1
% Adding i and t20
	lw r1,-36(r13)
	lw r2,-128(r13)
	add r3,r1,r2
	sw -132(r13),r3	% Store result into t21
% Assigning t21 to i
	lw r1,-132(r13)
	sw -36(r13),r1
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
	lw r15,-136(r13)
	jr r15	% Jump back to the calling function


% Start of function printArray
printArray	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 60	% set the stack pointer to the top position of the stack
	sw -56(r13),r15	% Put link onto stack frame
	sw 0(r13),r1	% Storing parameter arr into stack frame
	sw -28(r13),r2	% Storing parameter size into stack frame
% Assigning size to n
	lw r1,-28(r13)
	sw -32(r13),r1
% Storing 0 into t22
	addi r1, r0, 0
	addi r1, r0, 0
	sw -40(r13), r1
% Assigning t22 to i
	lw r1,-40(r13)
	sw -36(r13),r1
% While statement
s3WHILE	addi r11,r0,0
% Comparision between i and n
	lw r1,-36(r13)
	lw r2,-32(r13)
	sub r3,r1,r2
	clti r11,r3,0
	bz r11,s3ENDWHILE
s3STARTWHILE	addi r0,r0,0
% Offsetting array arr
	subi r9,r0,0
	lw r1,-36(r13)	% Loading index i
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Store the array offset in a temp variable t23
	lw r1,arrayOffsetBuf(r0)
	sw -44(r13),r1
% Printing t23 to console
	lw r10,t23(r13)
	jl r15,putint
% Storing 1 into t24
	addi r1, r0, 0
	addi r1, r0, 1
	sw -48(r13), r1
% Adding i and t24
	lw r1,-36(r13)
	lw r2,-48(r13)
	add r3,r1,r2
	sw -52(r13),r3	% Store result into t25
% Assigning t25 to i
	lw r1,-52(r13)
	sw -36(r13),r1
	j s3WHILE
s3ENDWHILE	addi r0,r0,0
	lw r15,-56(r13)
	jr r15	% Jump back to the calling function


% Start of function main
main	nop
	entry
	align
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 100	% set the stack pointer to the top position of the stack
	sw -96(r13),r15	% Put link onto stack frame
% Storing 0 into t26
	addi r1, r0, 0
	addi r1, r0, 0
	sw -28(r13), r1
% Storing 64 into t27
	addi r1, r0, 0
	addi r1, r0, 64
	sw -32(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-28(r13)	% Loading index t26
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t27 into arr
	lw r1,-32(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 1 into t28
	addi r1, r0, 0
	addi r1, r0, 1
	sw -36(r13), r1
% Storing 34 into t29
	addi r1, r0, 0
	addi r1, r0, 34
	sw -40(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-36(r13)	% Loading index t28
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t29 into arr
	lw r1,-40(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 2 into t30
	addi r1, r0, 0
	addi r1, r0, 2
	sw -44(r13), r1
% Storing 25 into t31
	addi r1, r0, 0
	addi r1, r0, 25
	sw -48(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-44(r13)	% Loading index t30
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t31 into arr
	lw r1,-48(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 3 into t32
	addi r1, r0, 0
	addi r1, r0, 3
	sw -52(r13), r1
% Storing 12 into t33
	addi r1, r0, 0
	addi r1, r0, 12
	sw -56(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-52(r13)	% Loading index t32
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t33 into arr
	lw r1,-56(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 4 into t34
	addi r1, r0, 0
	addi r1, r0, 4
	sw -60(r13), r1
% Storing 22 into t35
	addi r1, r0, 0
	addi r1, r0, 22
	sw -64(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-60(r13)	% Loading index t34
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t35 into arr
	lw r1,-64(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 5 into t36
	addi r1, r0, 0
	addi r1, r0, 5
	sw -68(r13), r1
% Storing 11 into t37
	addi r1, r0, 0
	addi r1, r0, 11
	sw -72(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-68(r13)	% Loading index t36
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t37 into arr
	lw r1,-72(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 6 into t38
	addi r1, r0, 0
	addi r1, r0, 6
	sw -76(r13), r1
% Storing 90 into t39
	addi r1, r0, 0
	addi r1, r0, 90
	sw -80(r13), r1
% Offsetting array arr
	subi r9,r0,0
	lw r1,-76(r13)	% Loading index t38
	muli r2,r1,1	% Multiply with number of columns
	muli r2,r2,4	% Multiply with array type
	add r9,r9,r2
	sub r9,r0,r9
	sw arrayOffsetBuf(r0),r9
% Storing t39 into arr
	lw r1,-80(r13)
	sw arrayOffsetBuf(r13),r1
% Storing 7 into t40
	addi r1, r0, 0
	addi r1, r0, 7
	sw -84(r13), r1
	lw r1,0(r13)	% Load parameter arr
	lw r2,-84(r13)	% Load parameter t40
	jl r15,printArray
	addi r13,r13,60
	addi r14,r14,60
% Storing 7 into t41
	addi r1, r0, 0
	addi r1, r0, 7
	sw -88(r13), r1
	lw r1,0(r13)	% Load parameter arr
	lw r2,-88(r13)	% Load parameter t41
	jl r15,bubbleSort
	addi r13,r13,140
	addi r14,r14,140
% Storing 7 into t42
	addi r1, r0, 0
	addi r1, r0, 7
	sw -92(r13), r1
	lw r1,0(r13)	% Load parameter arr
	lw r2,-92(r13)	% Load parameter t42
	jl r15,printArray
	addi r13,r13,60
	addi r14,r14,60
	lw r15,-96(r13)
	jr r15	% Jump back to the calling function

% End of program, declaring variables
arrayOffsetBuf	res 4
t4	res 4
t6	res 4
t7	res 4
t8	res 4
t10	res 4
t11	res 4
t12	res 4
t14	res 4
t15	res 4
t17	res 4
t19	res 4
t21	res 4
t23	res 4
t25	res 4
