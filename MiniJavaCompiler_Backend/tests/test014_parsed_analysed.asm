.ORIG x3000
  ;  initialize CONST0 and CONST1
  AND R2, R2, 0
  ADD R3, R2, 1
  ;  set SFP and HP 
  LD R6, LAB_15
  LD R4, LAB_16
  BR LAB_13
  ;  data for SFP and HP
LAB_15
.FILL 32768
LAB_16
.FILL 65023
  ;  arguments to main
  ;  string contents
LAB_17
.STRINGZ ""
  ;  strings
LAB_18
.FILL LAB_17
.FILL 1
  ;  args array
LAB_9
.FILL 1
.FILL LAB_18
  ; 
  ; Virtual Method Tables
  ; 
  ; 
  ; BEGIN A virtual method table
  ; 
LAB_10
.FILL LAB_19
LAB_19
  ; 
  ; DUMMY value for empty VMT
  ; 
.FILL 0
  ; 
  ; END A virtual method table
  ; 
  ; 
  ; BEGIN Object virtual method table
  ; 
LAB_11
.FILL LAB_20
LAB_20
  ; 
  ; DUMMY value for empty VMT
  ; 
.FILL 0
  ; 
  ; END Object virtual method table
  ; 
  ; 
  ; BEGIN String virtual method table
  ; 
LAB_12
.FILL LAB_21
LAB_21
  ; 
  ; DUMMY value for empty VMT
  ; 
.FILL 0
  ; 
  ; END String virtual method table
  ; 
LAB_13
  ; 
  ;  METHOD main
  ; 
  ;  save SFP R7 and initialize SP R5
  STR R7, R6, 2
  ADD R5, R6, 7
  ;  body 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  NEW ARRAY BEGIN 
  ; 
  BR LAB_23
  ;  address of allocation routine 
LAB_25
.FILL LAB_2
LAB_26
.FILL LAB_4
LAB_27
.FILL LAB_5
  ;  size of element type to allocate 
LAB_22
.FILL 1
LAB_23
  ;  compute size of array 
  ; 
  ;  INT CONST 100
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_28
  ;  value 
.FILL 100
LAB_28
  ; 
  ;  INT CONST END
  ; 
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_23
  ;  compute required space (size * sizeof elementtype)
  ADD R5, R5, 1
  STR R0, R5, -1
  ;  load size of elements
  LD R0, LAB_22
  ADD R5, R5, 1
  STR R0, R5, -1
  ;  multiply 
  LD R1, LAB_26
  JSRR R1
  ;  get size 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  increment with 2 to store size of array and array
  ADD R0, R0, 2
  ;  size must not be negative 
  BRn LAB_23
  ADD R5, R5, 1
  STR R4, R5, -1
  ADD R5, R5, 1
  STR R0, R5, -1
  ;  load address and size and invoke method 
  LD R1, LAB_25
  JSRR R1
  ;  top of stack is new address 
  LD R1, LAB_27
  JSRR R1
  ;  get size of array and store as first element
  ADD R5, R5, -1
  LDR R0, R5, 0
  STR R0, R4, 0
  ADD R0, R4, 2
  STR R0, R4, 1
  ADD R5, R5, 1
  STR R4, R5, -1
  ; 
  ;  NEW ARRAY END 
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER sieve lhs 
  ; 
  ;  push address 
  ADD R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 0
  ; 
  ADD R5, R5, 1
  STR R2, R5, -1
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  WHILE BEGIN 
  ; 
  ;  get the Block 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 0
  ; 
  ADD R5, R5, 1
  STR R2, R5, -1
  ;  lhs 
  ; 
  ;  ARRAY BEGIN 
  ; 
  BR LAB_30
LAB_31
  LD R0, 1
  JSRR R0
.FILL LAB_8
LAB_30
  ;  generate code for identifier 
  ; 
  ;  IDENTIFIER sieve rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  check for NPE
  LDR R0, R5, -1
  BRnp LAB_33
  LD R0, 1
  JSRR R0
.FILL LAB_7
LAB_33
  ;  look at top of stack without popping it 
  ;  generate code for index 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ; 
  ;  check index against array boundaries (0 and size, stored at first slot)
  ; 
  ;  look at top of stack without popping it 
  LDR R1, R5, -2
  LDR R0, R5, -1
  LDR R1, R1, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  must be negative (index - size)
  BRzp LAB_31
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_31
  ;  get offset 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  size must not be negative 
  BRn LAB_31
  ;  get address 
  ADD R5, R5, -1
  LDR R1, R5, 0
  LDR R1, R1, 1
  ADD R1, R1, R0
  ADD R5, R5, 1
  STR R1, R5, -1
  ; 
  ;  ARRAY END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 1
  ; 
  ADD R5, R5, 1
  STR R3, R5, -1
  ;  add integers 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R0, R0, R1
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  PLUS END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  LESS 
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 100
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_34
  ;  value 
.FILL 100
LAB_34
  ; 
  ;  INT CONST END
  ; 
  ;   compute lhs - rhs  
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if lhs < rhs, N flag is set
  BRn LAB_35
  ;  if not negative, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_36
LAB_35
  ;  if negative, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_36
  ; 
  ;  LESS END 
  ; 
  ; 
  ;  WHILE END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 2
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_37
  ;  value 
.FILL 2
LAB_37
  ; 
  ;  INT CONST END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  WHILE BEGIN 
  ; 
  ;  get the Block 
  ; 
  ;  IF 
  ; 
  ;  get the ifBlock 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 1
  ; 
  ADD R5, R5, 1
  STR R3, R5, -1
  ;  lhs 
  ; 
  ;  ARRAY BEGIN 
  ; 
  BR LAB_39
LAB_40
  LD R0, 1
  JSRR R0
.FILL LAB_8
LAB_39
  ;  generate code for identifier 
  ; 
  ;  IDENTIFIER sieve rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  check for NPE
  LDR R0, R5, -1
  BRnp LAB_42
  LD R0, 1
  JSRR R0
.FILL LAB_7
LAB_42
  ;  look at top of stack without popping it 
  ;  generate code for index 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ; 
  ;  check index against array boundaries (0 and size, stored at first slot)
  ; 
  ;  look at top of stack without popping it 
  LDR R1, R5, -2
  LDR R0, R5, -1
  LDR R1, R1, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  must be negative (index - size)
  BRzp LAB_40
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_40
  ;  get offset 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  size must not be negative 
  BRn LAB_40
  ;  get address 
  ADD R5, R5, -1
  LDR R1, R5, 0
  LDR R1, R1, 1
  ADD R1, R1, R0
  ADD R5, R5, 1
  STR R1, R5, -1
  ; 
  ;  ARRAY END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  add integers 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R0, R0, R1
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  PLUS END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER mark lhs 
  ; 
  ;  push address 
  ADD R0, R6, 6
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  WHILE BEGIN 
  ; 
  ;  get the Block 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 2
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_43
  ;  value 
.FILL 2
LAB_43
  ; 
  ;  INT CONST END
  ; 
  ;  lhs 
  ; 
  ;  ARRAY BEGIN 
  ; 
  BR LAB_45
LAB_46
  LD R0, 1
  JSRR R0
.FILL LAB_8
LAB_45
  ;  generate code for identifier 
  ; 
  ;  IDENTIFIER sieve rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  check for NPE
  LDR R0, R5, -1
  BRnp LAB_48
  LD R0, 1
  JSRR R0
.FILL LAB_7
LAB_48
  ;  look at top of stack without popping it 
  ;  generate code for index 
  ; 
  ;  IDENTIFIER mark rhs 
  ; 
  ;  push value 
  LDR R0, R6, 6
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ; 
  ;  check index against array boundaries (0 and size, stored at first slot)
  ; 
  ;  look at top of stack without popping it 
  LDR R1, R5, -2
  LDR R0, R5, -1
  LDR R1, R1, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  must be negative (index - size)
  BRzp LAB_46
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_46
  ;  get offset 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  size must not be negative 
  BRn LAB_46
  ;  get address 
  ADD R5, R5, -1
  LDR R1, R5, 0
  LDR R1, R1, 1
  ADD R1, R1, R0
  ADD R5, R5, 1
  STR R1, R5, -1
  ; 
  ;  ARRAY END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER mark rhs 
  ; 
  ;  push value 
  LDR R0, R6, 6
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  add integers 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R0, R0, R1
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  PLUS END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER mark lhs 
  ; 
  ;  push address 
  ADD R0, R6, 6
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  LESS 
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER mark rhs 
  ; 
  ;  push value 
  LDR R0, R6, 6
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 100
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_49
  ;  value 
.FILL 100
LAB_49
  ; 
  ;  INT CONST END
  ; 
  ;   compute lhs - rhs  
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if lhs < rhs, N flag is set
  BRn LAB_50
  ;  if not negative, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_51
LAB_50
  ;  if negative, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_51
  ; 
  ;  LESS END 
  ; 
  ; 
  ;  WHILE END
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  EQUAL 
  ; 
  ;  lhs 
  ; 
  ;  ARRAY BEGIN 
  ; 
  BR LAB_53
LAB_54
  LD R0, 1
  JSRR R0
.FILL LAB_8
LAB_53
  ;  generate code for identifier 
  ; 
  ;  IDENTIFIER sieve rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  check for NPE
  LDR R0, R5, -1
  BRnp LAB_56
  LD R0, 1
  JSRR R0
.FILL LAB_7
LAB_56
  ;  look at top of stack without popping it 
  ;  generate code for index 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ; 
  ;  check index against array boundaries (0 and size, stored at first slot)
  ; 
  ;  look at top of stack without popping it 
  LDR R1, R5, -2
  LDR R0, R5, -1
  LDR R1, R1, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  must be negative (index - size)
  BRzp LAB_54
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_54
  ;  get offset 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  size must not be negative 
  BRn LAB_54
  ;  get address 
  ADD R5, R5, -1
  LDR R1, R5, 0
  LDR R1, R1, 1
  ADD R1, R1, R0
  LDR R0, R1, 0
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  ARRAY END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 0
  ; 
  ADD R5, R5, 1
  STR R2, R5, -1
  ;  compute lhs - rhs -- this works also for bool 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if they are equal, the result is zero 
  BRz LAB_57
  ;  if not zero, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_58
LAB_57
  ;  if zero, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_58
  ; 
  ;  EQUAL END 
  ; 
  ; 
  ;  IF END 
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 1
  ; 
  ADD R5, R5, 1
  STR R3, R5, -1
  ;  add integers 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R0, R0, R1
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  PLUS END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  LESS 
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 100
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_59
  ;  value 
.FILL 100
LAB_59
  ; 
  ;  INT CONST END
  ; 
  ;   compute lhs - rhs  
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if lhs < rhs, N flag is set
  BRn LAB_60
  ;  if not negative, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_61
LAB_60
  ;  if negative, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_61
  ; 
  ;  LESS END 
  ; 
  ; 
  ;  WHILE END
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 0
  ; 
  ADD R5, R5, 1
  STR R2, R5, -1
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ; 
  ;  WHILE BEGIN 
  ; 
  ;  get the Block 
  ; 
  ;  IF 
  ; 
  ;  get the ifBlock 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  BR LAB_63
LAB_64
.FILL LAB_3
LAB_63
  LD R1, LAB_64
  JSRR R1
  ADD R5, R5, -1
  LDR R0, R5, 0
LAB_62
  TRAP x22
  ; 
  ;  STRING CONST BEGIN 
  ; 
  BR LAB_70
LAB_69
.STRINGZ " is prime."
LAB_67
.FILL LAB_69
.FILL 12
LAB_68
.FILL LAB_67
LAB_70
  LD R0, LAB_68
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  STRING CONST END 
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  BRz LAB_72
  LDR R0, R0, 0
  BR LAB_65
LAB_72
  LEA R0, LAB_71
  BR LAB_65
LAB_71
.STRINGZ "null"
  BR LAB_65
LAB_66
.FILL LAB_1
LAB_65
  TRAP x22
  LD R1, LAB_66
  JSRR R1
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  EQUAL 
  ; 
  ;  lhs 
  ; 
  ;  ARRAY BEGIN 
  ; 
  BR LAB_74
LAB_75
  LD R0, 1
  JSRR R0
.FILL LAB_8
LAB_74
  ;  generate code for identifier 
  ; 
  ;  IDENTIFIER sieve rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  check for NPE
  LDR R0, R5, -1
  BRnp LAB_77
  LD R0, 1
  JSRR R0
.FILL LAB_7
LAB_77
  ;  look at top of stack without popping it 
  ;  generate code for index 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ; 
  ;  check index against array boundaries (0 and size, stored at first slot)
  ; 
  ;  look at top of stack without popping it 
  LDR R1, R5, -2
  LDR R0, R5, -1
  LDR R1, R1, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  must be negative (index - size)
  BRzp LAB_75
  ;  look at top of stack without popping it 
  LDR R0, R5, -1
  ;  size must not be negative 
  BRn LAB_75
  ;  get offset 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  size must not be negative 
  BRn LAB_75
  ;  get address 
  ADD R5, R5, -1
  LDR R1, R5, 0
  LDR R1, R1, 1
  ADD R1, R1, R0
  LDR R0, R1, 0
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  ARRAY END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 1
  ; 
  ADD R5, R5, 1
  STR R3, R5, -1
  ;  compute lhs - rhs -- this works also for bool 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if they are equal, the result is zero 
  BRz LAB_78
  ;  if not zero, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_79
LAB_78
  ;  if zero, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_79
  ; 
  ;  EQUAL END 
  ; 
  ; 
  ;  IF END 
  ; 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 1
  ; 
  ADD R5, R5, 1
  STR R3, R5, -1
  ;  add integers 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R0, R0, R1
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  PLUS END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx lhs 
  ; 
  ;  push address 
  ADD R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  store 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  STR R0, R1, 0
  ; 
  ;  ASSIGN END
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ;  get the condition 
  ; 
  ;  LESS 
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER idx rhs 
  ; 
  ;  push value 
  LDR R0, R6, 5
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 100
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_80
  ;  value 
.FILL 100
LAB_80
  ; 
  ;  INT CONST END
  ; 
  ;   compute lhs - rhs  
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  NOT R1, R1
  ADD R1, R1, 1
  ADD R0, R0, R1
  ;  if lhs < rhs, N flag is set
  BRn LAB_81
  ;  if not negative, push 0 
  ADD R5, R5, 1
  STR R2, R5, -1
  BR LAB_82
LAB_81
  ;  if negative, push 1 
  ADD R5, R5, 1
  STR R3, R5, -1
LAB_82
  ; 
  ;  LESS END 
  ; 
  ; 
  ;  WHILE END
  ; 
  ; 
  ;  RETURN BEGIN
  ; 
  ;  get return PC from stack frame
  LDR R7, R6, 2
  RET
  ; 
  ;  RETURN END
  ; 
  ; 
  ;  METHOD END main
  ; 
  ; 
  ;  helper functions 
  ; 
  ; 
  ; 
  ; 
  ; 
  ;  translate top of stack to string, pushes result
  ; 
LAB_3
  ; 
  ; This algorithm takes the 2's complement representation of a signed
  ; integer, within the range -999 to +999, and converts it into an ASCII
  ; string consisting of a sign digit, followed by three decimal digits.
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ADD R5, R5, 1
  STR R7, R5, -1
  LEA R1, LAB_83
  ADD R0, R0, 0
  BRzp LAB_86
  LD R7, LAB_84
  STR R7, R1, 0
  ADD R1, R1, 1
  NOT R0, R0
  ADD R0, R0, 1
LAB_86
  AND R7, R7, 0
  LD R2, LAB_87
LAB_88
  ADD R0, R0, R2
  BRn LAB_89
  ADD R7, R7, 1
  BR LAB_88
  ; 
LAB_89
  ADD R7, R7, 0
  BRz LAB_112
  LD R2, LAB_85
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_112
  LD R2, LAB_90
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_91
  LD R2, LAB_92
LAB_93
  ADD R0, R0, R2
  BRn LAB_94
  ADD R7, R7, 1
  BR LAB_93
  ; 
LAB_94
  ADD R7, R7, 0
  BRz LAB_111
  LD R2, LAB_85
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_111
  LD R2, LAB_95
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_96
  LD R2, LAB_97
LAB_98
  ADD R0, R0, R2
  BRn LAB_99
  ADD R7, R7, 1
  BR LAB_98
  ; 
LAB_99
  ADD R7, R7, 0
  BRz LAB_110
  LD R2, LAB_85
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_110
  LD R2, LAB_100
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_101
  LD R2, LAB_102
LAB_103
  ADD R0, R0, R2
  BRn LAB_104
  ADD R7, R7, 1
  BR LAB_103
  ; 
LAB_104
  ADD R7, R7, 0
  BRz LAB_109
  LD R2, LAB_85
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_109
  ADD R0, R0, 10
  ; 
  LD R7, LAB_85
LAB_106
  ADD R7, R7, R0
  STR R7, R1, 0
  ADD R1, R1, 1
  AND R2, R2, 0
  STR R2, R1, 0
  ADD R5, R5, -1
  LDR R7, R5, 0
  LEA R0, LAB_83
  ADD R5, R5, 1
  STR R0, R5, -1
  RET
  ; 
  ; data
  ; 
LAB_83
  .BLKW 7
LAB_84
.FILL 45
LAB_85
.FILL 48
LAB_87
.FILL -10000
LAB_92
.FILL -1000
LAB_97
.FILL -100
LAB_102
.FILL -10
LAB_107
.FILL -1
LAB_90
.FILL 10000
LAB_95
.FILL 1000
LAB_100
.FILL 100
LAB_105
.FILL 10
LAB_108
.FILL 1
  ; 
  ;  print newline 
  ; 
LAB_1
  ADD R5, R5, 1
  STR R7, R5, -1
  LD R0, 1
  BR LAB_113
.FILL LAB_114
LAB_114
.STRINGZ "\n"
LAB_113
  TRAP x22
  ADD R5, R5, -1
  LDR R7, R5, 0
  RET
LAB_2
  ; 
  ;  create an object with size top of stack, result in HP
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  ; allocate object
  NOT R0, R0
  ADD R0, R0, 1
  ADD R4, R4, R0
  ADD R5, R5, 0
  BRp LAB_116
  ADD R4, R4, 0
  BRp LAB_117
LAB_118
  ADD R1, R5, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R1, R4, R1
  BRp LAB_115
  BR LAB_117
LAB_116
  ADD R4, R4, 0
  BRn LAB_115
  BR LAB_118
LAB_117
  TRAP x25
LAB_115
  ADD R5, R5, 1
  STR R4, R5, -1
  RET
  ; 
  ;  nullify 
  ; 
  ;  overwrites memory area a to b with 0s 
  ;  expects operands in top of stack 
  ;  assumes a<b!!! 
LAB_5
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R5, R5, 1
  STR R0, R5, -1
  NOT R0, R0
  ADD R0, R0, 1
  ADD R1, R1, R0
  ADD R5, R5, -1
  LDR R0, R5, 0
LAB_119
  STR R2, R0, 0
  ADD R0, R0, 1
  ADD R1, R1, -1
  BRp LAB_119
  RET
  ; 
  ;  multiplication routine 
  ; 
  ;  expects operands on top of stack 
LAB_4
  ;  while loop that multiplies a and b, R7 is sum 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ;  get a and b
  ;  check signs 
  ;  CONST0 is used to store the flag of the result
  ;  0 means positive (default), 1 negative
  ADD R0, R0, 0
  ;  if one is zero we're done
  BRz LAB_121
  ;  if a is positive, jump
  BRp LAB_122
  ;  negate a 
  NOT R0, R0
  ADD R0, R0, 1
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_121
  ;  if b is positive, jump
  BRp LAB_123
  ;  a is neg, b is too
  ;  negate b
LAB_124
  NOT R1, R1
  ADD R1, R1, 1
  ;  go, multiply!
  BR LAB_120
LAB_123
  ;  a is negative, b is positive
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  ;  go, multiply!
  BR LAB_120
LAB_122
  ;  a is positive 
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_121
  ;  if b is positive, go multiply!
  BRp LAB_120
  ;  a is pos, b is neg
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  BR LAB_124
  ; 
  ;  multiply 
  ; 
LAB_120
  ADD R5, R5, 1
  STR R7, R5, -1
  ;  reset sum
  AND R7, R7, 0
LAB_125
  ADD R7, R7, R0
  ADD R1, R1, -1
  BRp LAB_125
  ;  adjust sign 
  ADD R2, R2, 0
  BRz LAB_126
  NOT R7, R7
  ADD R7, R7, 1
LAB_126
  ;  reset CONST0 
  AND R2, R2, 0
  ;  transfer sum to TMP0, get RET from stack
  ADD R0, R7, 0
  ADD R5, R5, -1
  LDR R7, R5, 0
  ;  result in R0 
  ADD R5, R5, 1
  STR R0, R5, -1
  RET
  ;  one factor was 0
LAB_121
  ADD R5, R5, 1
  STR R2, R5, -1
  RET
  ; 
  ;  null pointer exception 
  ; 
  ;  prints error message and exits
LAB_7
  LEA R0, LAB_127
  TRAP x22
  TRAP x25
LAB_127
.STRINGZ "Null pointer exception\n"
  ; 
  ;  index out of bounds exception 
  ; 
  ;  prints error message and exits
LAB_8
  LEA R0, LAB_128
  TRAP x22
  TRAP x25
LAB_128
.STRINGZ "Index out of bounds exception\n"
  ; 
  ;  add two strings 
  ; 
  ;  expects args on top of stack, puts result on stack
LAB_6
  LDR R0, R5, -2
  BRnp LAB_132
  LEA R0, LAB_134
  STR R0, R5, -2
  BR LAB_133
LAB_132
  LDR R0, R5, -1
  BRnp LAB_133
  LEA R0, LAB_134
  STR R0, R5, -1
  BR LAB_133
LAB_134
.FILL LAB_135
.FILL 5
LAB_135
.STRINGZ "null"
LAB_133
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R5, R5, 1
  STR R7, R5, -1
  ;  compute combined length 
  LDR R7, R0, 1
  ADD R5, R5, 1
  STR R0, R5, -1
  ADD R0, R7, 0
  LDR R7, R1, 1
  ADD R5, R5, 1
  STR R1, R5, -1
  ADD R0, R0, R7
  BR LAB_130
LAB_131
.FILL LAB_2
LAB_129
.FILL 3
  ;  copy string 
LAB_137
.FILL LAB_136
LAB_136
  ADD R5, R5, 1
  STR R7, R5, -1
  LDR R0, R0, 0
LAB_139
  LDR R7, R0, 0
  BRz LAB_138
  STR R7, R1, 0
  ADD R0, R0, 1
  ADD R1, R1, 1
  BR LAB_139
LAB_138
  ADD R5, R5, -1
  LDR R7, R5, 0
  RET
LAB_130
  ;  invoke new method 
  LD R1, LAB_129
  ADD R0, R1, R0
  ADD R5, R5, 1
  STR R0, R5, -1
  LD R1, LAB_131
  JSRR R1
  ;  initialize object pointed to by HP 
  ;  initialize string 
  ADD R0, R4, 2
  STR R0, R4, 0
  ;  initialize length 
  ADD R5, R5, -1
  LDR R0, R5, 0
  STR R0, R4, 1
  ;  get two strings 
  ADD R5, R5, -2
  LDR R0, R5, 0
  LDR R1, R5, 1
  ADD R5, R5, 1
  STR R1, R5, -1
  ;  copy first string from TMP0->0 to HP->0 
  LDR R1, R4, 0
  LD R7, LAB_137
  JSRR R7
  ;  copy second string from TMP0->0 to HP->0 
  ADD R5, R5, -1
  LDR R0, R5, 0
  LD R7, LAB_137
  JSRR R7
  ADD R5, R5, -1
  LDR R7, R5, 0
  ADD R5, R5, 1
  STR R4, R5, -1
  RET
.END
