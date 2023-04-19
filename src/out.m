% Convention:
%	r13 is used for stack pointer
%	r9 is used for offset
%	r8 is used for return value
%	r7 is used for address buffer


% Start of class POLYNOMIAL
POLYNOMIAL	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 4	% set the stack pointer to the top position of the stack


% Start of class LINEAR
LINEAR	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 20	% set the stack pointer to the top position of the stack


% Start of class QUADRATIC
QUADRATIC	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 28	% set the stack pointer to the top position of the stack


% Start of function POLYNOMIAL::evaluate
POLYNOMIAL_evaluate_float	nop
	subi r13,r13,32	% set the stack pointer to the top position of the stack
	sw -28(r13),r15	% Put link onto stack frame
	sw -20(r13),r12	% Store the object address onto the self
	sw -12(r13),r1	% Storing parameter x into stack frame
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -24(r13), r1
	lw r1,-24(r13)	% Get the return value t0
	sw 0(r13),r1
	lw r15,-28(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::evaluate
QUADRATIC_evaluate_float	nop
	subi r13,r13,100	% set the stack pointer to the top position of the stack
	sw -96(r13),r15	% Put link onto stack frame
	sw -28(r13),r12	% Store the object address onto the self
	sw -12(r13),r1	% Storing parameter x into stack frame
% Load the calling object from class QUADRATIC
	lw r1,-28(r13)
	addi r1,r1,-4	% Get offset of member variable a in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Assigning a to result
	lw r1,0(r7)
	sw -20(r13),r1
% Multiplying result and x
	lw r1,-20(r13)
	lw r2,-12(r13)
	mul r3,r1,r2
	sw -56(r13),r3	% Store result into t1
% Load the calling object from class QUADRATIC
	lw r1,-28(r13)
	addi r1,r1,-12	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t1 and b
	lw r1,-56(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -64(r13),r3	% Store result into t2
% Assigning t2 to result
	lw r1,-64(r13)
	sw -20(r13),r1
% Multiplying result and x
	lw r1,-20(r13)
	lw r2,-12(r13)
	mul r3,r1,r2
	sw -72(r13),r3	% Store result into t3
% Load the calling object from class QUADRATIC
	lw r1,-28(r13)
	addi r1,r1,-20	% Get offset of member variable c in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t3 and c
	lw r1,-72(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -80(r13),r3	% Store result into t4
% Assigning t4 to result
	lw r1,-80(r13)
	sw -20(r13),r1
	lw r1,-20(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-96(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC_constructor_float_float_float	nop
	subi r13,r13,60	% set the stack pointer to the top position of the stack
	sw -56(r13),r15	% Put link onto stack frame
	sw -28(r13),r12	% Store the object address onto the self
	sw -4(r13),r1	% Storing parameter A into stack frame
	sw -12(r13),r2	% Storing parameter B into stack frame
	sw -20(r13),r3	% Storing parameter C into stack frame
	addi r1,r0,-4	% Get offset of member variable a in object
	lw r2,-28(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarAddressBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Get value from memVarAddressBuf
	addi r7,r0,memVarAddressBuf		% Load buffer address onto r7
	lw r7,0(r7)	% Load mem-var into r7
% Assigning A to self.a
	lw r1,-4(r13)
	sw 0(r7),r1
	addi r1,r0,-12	% Get offset of member variable b in object
	lw r2,-28(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarAddressBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Get value from memVarAddressBuf
	addi r7,r0,memVarAddressBuf		% Load buffer address onto r7
	lw r7,0(r7)	% Load mem-var into r7
% Assigning B to self.b
	lw r1,-12(r13)
	sw 0(r7),r1
	addi r1,r0,-20	% Get offset of member variable c in object
	lw r2,-28(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable c
	addi r3,r0,memVarAddressBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Get value from memVarAddressBuf
	addi r7,r0,memVarAddressBuf		% Load buffer address onto r7
	lw r7,0(r7)	% Load mem-var into r7
% Assigning C to self.c
	lw r1,-20(r13)
	sw 0(r7),r1
	lw r15,-56(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::constructor
LINEAR_constructor_float_float	nop
	subi r13,r13,44	% set the stack pointer to the top position of the stack
	sw -40(r13),r15	% Put link onto stack frame
	sw -20(r13),r12	% Store the object address onto the self
	sw -4(r13),r1	% Storing parameter A into stack frame
	sw -12(r13),r2	% Storing parameter B into stack frame
	addi r1,r0,-4	% Get offset of member variable a in object
	lw r2,-20(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarAddressBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Get value from memVarAddressBuf
	addi r7,r0,memVarAddressBuf		% Load buffer address onto r7
	lw r7,0(r7)	% Load mem-var into r7
% Assigning A to self.a
	lw r1,-4(r13)
	sw 0(r7),r1
	addi r1,r0,-12	% Get offset of member variable b in object
	lw r2,-20(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarAddressBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Get value from memVarAddressBuf
	addi r7,r0,memVarAddressBuf		% Load buffer address onto r7
	lw r7,0(r7)	% Load mem-var into r7
% Assigning B to self.b
	lw r1,-12(r13)
	sw 0(r7),r1
	lw r15,-40(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::evaluate
LINEAR_evaluate_float	nop
	subi r13,r13,76	% set the stack pointer to the top position of the stack
	sw -72(r13),r15	% Put link onto stack frame
	sw -28(r13),r12	% Store the object address onto the self
	sw -12(r13),r1	% Storing parameter x into stack frame
% Storing 0.0 into t6
	addi r1, r0, 0
	addi r1, r0, 0.0
	sw -48(r13), r1
% Assigning t6 to result
	lw r1,-48(r13)
	sw -20(r13),r1
% Load the calling object from class LINEAR
	lw r1,-28(r13)
	addi r1,r1,-4	% Get offset of member variable a in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Multiplying a and x
	lw r1,0(r7)
	lw r2,-12(r13)
	mul r3,r1,r2
	sw -56(r13),r3	% Store result into t7
% Load the calling object from class LINEAR
	lw r1,-28(r13)
	addi r1,r1,-12	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t7 and b
	lw r1,-56(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -64(r13),r3	% Store result into t8
% Assigning t8 to result
	lw r1,-64(r13)
	sw -20(r13),r1
	lw r1,-20(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-72(r13)
	jr r15	% Jump back to the calling function


% Start of function main
main	nop
	entry
	align
	addi r13,r0,topaddr	% initialize the frame pointer
% Storing 2 into t9
	addi r1, r0, 0
	addi r1, r0, 2
	sw -56(r13), r1
% Storing 3.5 into t10
	addi r1, r0, 0
	addi r1, r0, 3.5
	sw -60(r13), r1
% Calling member function LINEAR::constructor
	lw r1,-56(r13)	% Load parameter t9
	lw r2,-60(r13)	% Load parameter t10
	addi r12,r13,-4	% Load object address onto r12
	jl r15,LINEAR_constructor_float_float
	addi r13,r13,44
% Storing 2.0 into t11
	addi r1, r0, 0
	addi r1, r0, 2.0
	sw -68(r13), r1
% Changing the sign of t11
	lw r1,-68(r13)
	muli r1,r1,-1
	sw -68(r13),r1
% Storing 1.0 into t13
	addi r1, r0, 0
	addi r1, r0, 1.0
	sw -84(r13), r1
% Storing 0.0 into t14
	addi r1, r0, 0
	addi r1, r0, 0.0
	sw -92(r13), r1
% Calling member function QUADRATIC::constructor
	lw r1,-68(r13)	% Load parameter t11
	lw r2,-84(r13)	% Load parameter t13
	lw r3,-92(r13)	% Load parameter t14
	addi r12,r13,-24	% Load object address onto r12
	jl r15,QUADRATIC_constructor_float_float_float
	addi r13,r13,60
% Storing 1 into t15
	addi r1, r0, 0
	addi r1, r0, 1
	sw -100(r13), r1
% Assigning t15 to counter
	lw r1,-100(r13)
	sw -52(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 10 into t16
	addi r1, r0, 0
	addi r1, r0, 10
	sw -104(r13), r1
% Comparision between counter and t16
	lw r1,-52(r13)
	lw r2,-104(r13)
	sub r3,r1,r2
	clei r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% Printing counter to console
	lw r1,-52(r13)
	jl r15,putint
	addi r12,r0,32
	putc r12
% Calling member function LINEAR::evaluate
	lw r1,-52(r13)	% Load parameter counter
	addi r12,r13,-4	% Load object address onto r12
	jl r15,LINEAR_evaluate_float
	lw r8,0(r13)	% Load return value onto r8
	addi r13,r13,76
	addi r7,r0,funcReturnBuf	% Load buffer address
	sw 0(r7),r8	% Store return value in buffer
% Calling member function QUADRATIC::evaluate
	lw r1,-52(r13)	% Load parameter counter
	addi r12,r13,-24	% Load object address onto r12
	jl r15,QUADRATIC_evaluate_float
	lw r8,0(r13)	% Load return value onto r8
	addi r13,r13,100
	addi r7,r0,funcReturnBuf	% Load buffer address
	sw 0(r7),r8	% Store return value in buffer
	j s0WHILE
s0ENDWHILE	addi r0,r0,0
	hlt

% End of program, declaring variables
objectOffsetBuf	res 4
t1	res 8
t2	res 8
t3	res 8
t4	res 8
t5	res 8
memVarAddressBuf	res 4
t7	res 8
t8	res 8
f1	res 20
f2	res 28
funcReturnBuf	res 4
t12	res 8
