% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

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
	subi r14, r14, 11	% set the stack pointer to the top position of the stack


% Start of class QUADRATIC
QUADRATIC	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 11	% set the stack pointer to the top position of the stack


% Start of function POLYNOMIAL::evaluate
POLYNOMIAL_evaluate_integer	nop
	subi r13,r13,16	% set the stack pointer to the top position of the stack
	sw -12(r13),r15	% Put link onto stack frame
	sw -8(r13),r12	% Store the object address onto the self
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
QUADRATIC_evaluate_integer	nop
	subi r13,r13,55	% set the stack pointer to the top position of the stack
	sw -51(r13),r15	% Put link onto stack frame
	sw -20(r13),r12	% Store the object address onto the self
	sw -4(r13),r1	% Storing parameter x into stack frame
% Assigning a to result
	lw r1,-12(r13)
	sw -8(r13),r1
% Multiplying result and x
	lw r1,-8(r13)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -31(r13),r3	% Store result into t1
% Load the calling object from class QUADRATIC
	lw r1,-20(r13)
	addi r1,r1,-4	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t1 and b
	lw r1,-31(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -35(r13),r3	% Store result into t2
% Assigning t2 to result
	lw r1,-35(r13)
	sw -8(r13),r1
% Multiplying result and x
	lw r1,-8(r13)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -39(r13),r3	% Store result into t3
% Load the calling object from class QUADRATIC
	lw r1,-20(r13)
	addi r1,r1,-8	% Get offset of member variable c in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t3 and c
	lw r1,-39(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -43(r13),r3	% Store result into t4
% Assigning t4 to result
	lw r1,-43(r13)
	sw -8(r13),r1
	lw r1,-8(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-51(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC_constructor_integer_integer_integer	nop
	subi r13,r13,27	% set the stack pointer to the top position of the stack
	sw -23(r13),r15	% Put link onto stack frame
	sw -12(r13),r12	% Store the object address onto the self
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -4(r13),r2	% Storing parameter B into stack frame
	sw -8(r13),r3	% Storing parameter C into stack frame
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-12(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning A to self.a
	lw r1,0(r13)
	sw memVarBuf,r1
	addi r1,r0,-4	% Get offset of member variable b in object
	lw r2,-12(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning B to self.b
	lw r1,-4(r13)
	sw memVarBuf,r1
	addi r1,r0,-8	% Get offset of member variable c in object
	lw r2,-12(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable c
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning C to self.c
	lw r1,-8(r13)
	sw memVarBuf,r1
	lw r15,-23(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC_constructor_integer	nop
	subi r13,r13,19	% set the stack pointer to the top position of the stack
	sw -15(r13),r15	% Put link onto stack frame
	sw -4(r13),r12	% Store the object address onto the self
	sw 0(r13),r1	% Storing parameter A into stack frame
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-4(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning A to self.a
	lw r1,0(r13)
	sw memVarBuf,r1
	lw r15,-15(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::constructor
LINEAR_constructor_integer_integer	nop
	subi r13,r13,20	% set the stack pointer to the top position of the stack
	sw -16(r13),r15	% Put link onto stack frame
	sw -8(r13),r12	% Store the object address onto the self
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -4(r13),r2	% Storing parameter B into stack frame
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-8(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning A to self.a
	lw r1,0(r13)
	sw memVarBuf,r1
	addi r1,r0,-4	% Get offset of member variable b in object
	lw r2,-8(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning B to self.b
	lw r1,-4(r13)
	sw memVarBuf,r1
	lw r15,-16(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::evaluate
LINEAR_evaluate_integer	nop
	subi r13,r13,36	% set the stack pointer to the top position of the stack
	sw -32(r13),r15	% Put link onto stack frame
	sw -12(r13),r12	% Store the object address onto the self
	sw -4(r13),r1	% Storing parameter x into stack frame
% Storing 0 into t6
	addi r1, r0, 0
	addi r1, r0, 0
	sw -20(r13), r1
% Assigning t6 to result
	lw r1,-20(r13)
	sw -8(r13),r1
	addi r1,r0,0
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-12(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Multiplying a and x
% Get value from memVarBuf
	addi r7,r0,memVarBuf	% Load buffer address onto r7
	lw r1,0(r7)
	lw r2,-4(r13)
	mul r3,r1,r2
	sw -24(r13),r3	% Store result into t7
% Load the calling object from class LINEAR
	lw r1,-12(r13)
	addi r1,r1,-4	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t7 and b
	lw r1,-24(r13)
	lw r2,0(r7)
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
count_integer	nop
	subi r13,r13,12	% set the stack pointer to the top position of the stack
	sw -8(r13),r15	% Put link onto stack frame
	sw -4(r13),r1	% Storing parameter x into stack frame
	lw r1,-4(r13)	% Get the return value x
	sw 0(r13),r1
	lw r15,-8(r13)
	jr r15	% Jump back to the calling function


% Start of function prog
prog	nop
	subi r13,r13,95	% set the stack pointer to the top position of the stack
	sw -79(r13),r15	% Put link onto stack frame
% Storing 2 into t9
	addi r1, r0, 0
	addi r1, r0, 2
	sw -27(r13), r1
% Changing the sign of t9
	lw r1,-27(r13)
	muli r1,r1,-1
	sw -27(r13),r1
% Storing 3 into t11
	addi r1, r0, 0
	addi r1, r0, 3
	sw -35(r13), r1
% Storing 2 into t12
	addi r1, r0, 0
	addi r1, r0, 2
	sw -39(r13), r1
% Changing the sign of t12
	lw r1,-39(r13)
	muli r1,r1,-1
	sw -39(r13),r1
% Storing 1 into t14
	addi r1, r0, 0
	addi r1, r0, 1
	sw -47(r13), r1
% Storing 0 into t15
	addi r1, r0, 0
	addi r1, r0, 0
	sw -51(r13), r1
% Storing 2 into t16
	addi r1, r0, 0
	addi r1, r0, 2
	sw -55(r13), r1
% Storing 1 into t17
	addi r1, r0, 0
	addi r1, r0, 1
	sw -59(r13), r1
% Changing the sign of t17
	lw r1,-59(r13)
	muli r1,r1,-1
	sw -59(r13),r1
% Assigning t17 to counter
	lw r1,-59(r13)
	sw -19(r13),r1
	addi r1,r0,0
	addi r1,r0,0	% Get offset of member variable a in object
	addi r2,r0,-8	% Get the object address
	add r2,r2,r1	% Get the member variable address
	addi r3,r0,memVarBuf	% Get the buffer address
	sw 0(r3),r2	% Store mem-var address in buffer
% While statement
s0WHILE	addi r11,r0,0
% Storing 10 into t19
	addi r1, r0, 0
	addi r1, r0, 10
	sw -67(r13), r1
% Comparision between counter and t19
	lw r1,-19(r13)
	lw r2,-67(r13)
	sub r3,r1,r2
	clei r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% Storing 1 into t20
	addi r1, r0, 0
	addi r1, r0, 1
	sw -71(r13), r1
% Adding counter and t20
	lw r1,-19(r13)
	lw r2,-71(r13)
	add r3,r1,r2
	sw -31(r13),r3	% Store result into t10
% Assigning t10 to counter
	lw r1,-31(r13)
	sw -19(r13),r1
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
	lw r15,-79(r13)
	jr r15	% Jump back to the calling function


% Start of function prog
prog	nop
	subi r13,r13,95	% set the stack pointer to the top position of the stack
	sw -79(r13),r15	% Put link onto stack frame
% Storing 1 into t22
	addi r1, r0, 0
	addi r1, r0, 1
	sw -83(r13), r1
% Storing 2 into t23
	addi r1, r0, 0
	addi r1, r0, 2
	sw -87(r13), r1
	lw r15,-79(r13)
	jr r15	% Jump back to the calling function
	hlt

% End of program, declaring variables
objectOffsetBuf	res 4
t1	res 4
t2	res 4
t3	res 4
t4	res 4
t5	res 4
memVarBuf	res 4
t7	res 4
t8	res 4
t10	res 4
t13	res 4
t18	res 4
t21	res 4
t10	res 4
t13	res 4
t18	res 4
t21	res 4
