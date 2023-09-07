# Compiler_design_COMP442
A compiler that can transform human-readable code into assembly code (specifically Moon code that can be run by the Moon machine that is designed by a professor at Concordia University)


To run the compiler:
1. Change directory into the _src_ directory
2. Run the _main.java_ code and enter the source code file (_.src_) that you want to run (the source code file must also be in the _src_ folder)
3. If all syntax are correct, an _out.m_ file should be outputted, which contains the assembly code for Moon machine
4. To run the assembly code, use command: ```moon out.m```


The code syntax is as follows:
<pre>
  function <i>functionName</i> (<i>parameterName</i>: <i>parameterType</i>, ...) => <i>returnType</i>
  {
      localvar <i>varName</i>: <i>varType</i>;
      <i>var1Name</i> = <i>var2Name</i>;
      <i>var2Name</i> = <i>constant</i>;

      while (<i>condition</i>) {
          <i>statement</i>
      };

      if (<i>condition</i>) then {
          <i>statement</i>
      } else {
          <i>statement</i>
      };
  }

  class <i>className1</i>
  {
      private attribute <i>attributeName</i>: <i>attributeType</i>;
      public attribute <i>attributeName</i>: <i>attributeType</i>;
      public constructor: (<i>param1Name</i>: <i>param1Type</i>, ...);
      public function <i>classFunc1Name</i>: (<i>param1Name</i>: <i>param1Type</i>, ...) => <i>returnType</i>;
      private function <i>classFunc2Name</i>: (<i>param1Name</i>: <i>param1Type</i>, <i>param2Name</i>: <i>param2Type</i>, ...) => <i>returnType</i>;
  }

  class <i>className2</i> isa <i>className1</i>
  {
      private attribute <i>attributeName</i>: <i>attributeType</i>;
      public attribute <i>attributeName</i>: <i>attributeType</i>;
      public constructor: (<i>param1Name</i>: <i>param1Type</i>, <i>param2Name</i>: <i>param2Type</i>);
      public function <i>classFunc1Name</i>: (<i>param1Name</i>: <i>param1Type</i>) => <i>returnType</i>;
      private function <i>classFunc2Name</i>: (<i>param1Name</i>: <i>param1Type</i>, <i>param2Name</i>: <i>param2Type</i>) => <i>returnType</i>;
  }

  function <i>classFunc1Name</i>::<i>className2</i> (<i>param1Name</i>: <i>param1Type</i>) => <i>returnType</i>
  {
      localvar <i>varName</i>: <i>varType</i>
  }

  function main()
  {
      localvar <i>object1</i>: <i>className2</i>(<i>attribute1</i>, <i>attribute2</i>)
      <i>object1</i>.<i>classFunc2Name</i>(<i>param1</i>, <i>param2</i>)
  }
</pre>

Code samples are available within the **src** folder with the extension _.src_
