% Convention:
%	r14 is used for stack pointer
%	r13 is used for frame pointer

% Start of class POLYNOMIAL
POLYNOMIAL	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 0	% set the stack pointer to the top position of the stack


% Start of class LINEAR
LINEAR	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 16	% set the stack pointer to the top position of the stack


% Start of class QUADRATIC
QUADRATIC	nop
	addi r14, r0, topaddr	% initialize the stack pointer
	addi r13, r0, topaddr	% initialize the frame pointer
	subi r14, r14, 24	% set the stack pointer to the top position of the stack


% Start of function POLYNOMIAL::evaluate
POLYNOMIAL_evaluate_float	nop
	subi r13,r13,24	% set the stack pointer to the top position of the stack
	sw -20(r13),r15	% Put link onto stack frame
	sw -16(r13),r12	% Store the object address onto the self
	sw -8(r13),r1	% Storing parameter x into stack frame
% Storing 0 into t0
	addi r1, r0, 0
	addi r1, r0, 0
	sw -16(r13), r1
	lw r1,-16(r13)	% Get the return value t0
	sw 0(r13),r1
	lw r15,-20(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::evaluate
QUADRATIC_evaluate_float	nop
	subi r13,r13,92	% set the stack pointer to the top position of the stack
	sw -88(r13),r15	% Put link onto stack frame
	sw -24(r13),r12	% Store the object address onto the self
	sw -8(r13),r1	% Storing parameter x into stack frame
% Load the calling object from class QUADRATIC
	lw r1,-24(r13)
	addi r1,r1,0	% Get offset of member variable a in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Assigning a to result
	lw r1,0(r7)
	sw -16(r13),r1
% Multiplying result and x
	lw r1,-16(r13)
	lw r2,-8(r13)
	mul r3,r1,r2
	sw -48(r13),r3	% Store result into t1
% Load the calling object from class QUADRATIC
	lw r1,-24(r13)
	addi r1,r1,-8	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t1 and b
	lw r1,-48(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -56(r13),r3	% Store result into t2
% Assigning t2 to result
	lw r1,-56(r13)
	sw -16(r13),r1
% Multiplying result and x
	lw r1,-16(r13)
	lw r2,-8(r13)
	mul r3,r1,r2
	sw -64(r13),r3	% Store result into t3
% Load the calling object from class QUADRATIC
	lw r1,-24(r13)
	addi r1,r1,-16	% Get offset of member variable c in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t3 and c
	lw r1,-64(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -72(r13),r3	% Store result into t4
% Assigning t4 to result
	lw r1,-72(r13)
	sw -16(r13),r1
	lw r1,-16(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-88(r13)
	jr r15	% Jump back to the calling function


% Start of function QUADRATIC::constructor
QUADRATIC_constructor_float_float_float	nop
	subi r13,r13,52	% set the stack pointer to the top position of the stack
	sw -48(r13),r15	% Put link onto stack frame
	sw -24(r13),r12	% Store the object address onto the self
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -8(r13),r2	% Storing parameter B into stack frame
	sw -16(r13),r3	% Storing parameter C into stack frame
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-24(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning A to self.a
	lw r1,0(r13)
	sw memVarBuf,r1
	addi r1,r0,-8	% Get offset of member variable b in object
	lw r2,-24(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning B to self.b
	lw r1,-8(r13)
	sw memVarBuf,r1
	addi r1,r0,-16	% Get offset of member variable c in object
	lw r2,-24(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable c
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning C to self.c
	lw r1,-16(r13)
	sw memVarBuf,r1
	lw r15,-48(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::constructor
LINEAR_constructor_float_float	nop
	subi r13,r13,36	% set the stack pointer to the top position of the stack
	sw -32(r13),r15	% Put link onto stack frame
	sw -16(r13),r12	% Store the object address onto the self
	sw 0(r13),r1	% Storing parameter A into stack frame
	sw -8(r13),r2	% Storing parameter B into stack frame
	addi r1,r0,0	% Get offset of member variable a in object
	lw r2,-16(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable a
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning A to self.a
	lw r1,0(r13)
	sw memVarBuf,r1
	addi r1,r0,-8	% Get offset of member variable b in object
	lw r2,-16(r13)	% Get object address
	add r1,r1,r2	% Get address of member variable b
	addi r3,r0,memVarBuf	% Load buffer address
	sw 0(r3),r1	% Store mem-var address in buffer
% Assigning B to self.b
	lw r1,-8(r13)
	sw memVarBuf,r1
	lw r15,-32(r13)
	jr r15	% Jump back to the calling function


% Start of function LINEAR::evaluate
LINEAR_evaluate_float	nop
	subi r13,r13,68	% set the stack pointer to the top position of the stack
	sw -64(r13),r15	% Put link onto stack frame
	sw -24(r13),r12	% Store the object address onto the self
	sw -8(r13),r1	% Storing parameter x into stack frame
% Storing 0.0 into t6
	addi r1, r0, 0
	addi r1, r0, 0.0
	sw -40(r13), r1
% Assigning t6 to result
	lw r1,-40(r13)
	sw -16(r13),r1
% Load the calling object from class LINEAR
	lw r1,-24(r13)
	addi r1,r1,0	% Get offset of member variable a in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Multiplying a and x
	lw r1,0(r7)
	lw r2,-8(r13)
	mul r3,r1,r2
	sw -48(r13),r3	% Store result into t7
% Load the calling object from class LINEAR
	lw r1,-24(r13)
	addi r1,r1,-8	% Get offset of member variable b in object
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	sw 0(r7),r1	% Store offset in objectOffsetBuf
	addi r7,r0,objectOffsetBuf	% Load buffer address onto r7
	lw r7,0(r7)	% Load variable address onto r7
% Adding t7 and b
	lw r1,-48(r13)
	lw r2,0(r7)
	add r3,r1,r2
	sw -56(r13),r3	% Store result into t8
% Assigning t8 to result
	lw r1,-56(r13)
	sw -16(r13),r1
	lw r1,-16(r13)	% Get the return value result
	sw 0(r13),r1
	lw r15,-64(r13)
	jr r15	% Jump back to the calling function


% Start of function main
main	nop
	entry
	align
	addi r13,r0,topaddr	% initialize the frame pointer
% Storing 2 into t9
	addi r1, r0, 0
	addi r1, r0, 2
	sw -44(r13), r1
% Storing 3.5 into t10
	addi r1, r0, 0
	addi r1, r0, 3.5
	sw -48(r13), r1
% Calling member function LINEAR::constructor
	lw r1,-44(r13)	% Load parameter t9
	lw r2,-48(r13)	% Load parameter t10
	addi r12,r13,0	% Load object address onto r12
	jl r15,LINEAR_constructor_float_float
	addi r13,r13,36
% Storing 2.0 into t11
	addi r1, r0, 0
	addi r1, r0, 2.0
	sw -56(r13), r1
% Changing the sign of t11
	lw r1,-56(r13)
	muli r1,r1,-1
	sw -56(r13),r1
% Storing 1.0 into t13
	addi r1, r0, 0
	addi r1, r0, 1.0
	sw -72(r13), r1
% Storing 0.0 into t14
	addi r1, r0, 0
	addi r1, r0, 0.0
	sw -80(r13), r1
% Calling member function QUADRATIC::constructor
	lw r1,-56(r13)	% Load parameter t11
	lw r2,-72(r13)	% Load parameter t13
	lw r3,-80(r13)	% Load parameter t14
	addi r12,r13,-16	% Load object address onto r12
	jl r15,QUADRATIC_constructor_float_float_float
	addi r13,r13,52
% Storing 1 into t15
	addi r1, r0, 0
	addi r1, r0, 1
	sw -88(r13), r1
% Assigning t15 to counter
	lw r1,-88(r13)
	sw -40(r13),r1
% While statement
s0WHILE	addi r11,r0,0
% Storing 10 into t16
	addi r1, r0, 0
	addi r1, r0, 10
	sw -92(r13), r1
% Comparision between counter and t16
	lw r1,-40(r13)
	lw r2,-92(r13)
	sub r3,r1,r2
	clei r11,r3,0
	bz r11,s0ENDWHILE
s0STARTWHILE	addi r0,r0,0
% Printing counter to console
	lw r1,-40(r13)
	jl r15,putint
% Calling member function LINEAR::evaluate
	lw r1,-40(r13)	% Load parameter counter
	addi r12,r13,0	% Load object address onto r12
	jl r15,LINEAR_evaluate_float
	lw r8,0(r13)	% Load return value onto r8
	addi r13,r13,68
	addi r7,r0,funcReturnBuf	% Load buffer address
	sw 0(r7),r8	% Store return value in buffer
% Calling member function QUADRATIC::evaluate
	lw r1,-40(r13)	% Load parameter counter
	addi r12,r13,-16	% Load object address onto r12
	jl r15,QUADRATIC_evaluate_float
	lw r8,0(r13)	% Load return value onto r8
	addi r13,r13,92
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
memVarBuf	res 4
t7	res 8
t8	res 8
f1	res 16
f2	res 24
funcReturnBuf	res 4
t12	res 8
