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
  ADD R5, R6, 6
  ;  body 
  ; 
  ;  ASSIGN 
  ; 
  ;  rhs 
  ; 
  ;  INT CONST 40
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_22
  ;  value 
.FILL 40
LAB_22
  ; 
  ;  INT CONST END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER x lhs 
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
  ;  INT CONST 2
  ; 
  LD R0, 3
  ADD R5, R5, 1
  STR R0, R5, -1
  BR LAB_23
  ;  value 
.FILL 2
LAB_23
  ; 
  ;  INT CONST END
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER y lhs 
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
  ;  PLUS BEGIN
  ; 
  ;  lhs 
  ; 
  ;  IDENTIFIER x rhs 
  ; 
  ;  push value 
  LDR R0, R6, 4
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  IDENTIFIER END 
  ; 
  ;  rhs 
  ; 
  ;  IDENTIFIER y rhs 
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
  BR LAB_26
LAB_27
.FILL LAB_3
LAB_26
  LD R1, LAB_27
  JSRR R1
  ADD R5, R5, -1
  LDR R0, R5, 0
  BR LAB_24
LAB_25
.FILL LAB_1
LAB_24
  TRAP x22
  LD R1, LAB_25
  JSRR R1
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
  LEA R1, LAB_28
  ADD R0, R0, 0
  BRzp LAB_31
  LD R7, LAB_29
  STR R7, R1, 0
  ADD R1, R1, 1
  NOT R0, R0
  ADD R0, R0, 1
LAB_31
  AND R7, R7, 0
  LD R2, LAB_32
LAB_33
  ADD R0, R0, R2
  BRn LAB_34
  ADD R7, R7, 1
  BR LAB_33
  ; 
LAB_34
  ADD R7, R7, 0
  BRz LAB_57
  LD R2, LAB_30
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_57
  LD R2, LAB_35
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_36
  LD R2, LAB_37
LAB_38
  ADD R0, R0, R2
  BRn LAB_39
  ADD R7, R7, 1
  BR LAB_38
  ; 
LAB_39
  ADD R7, R7, 0
  BRz LAB_56
  LD R2, LAB_30
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_56
  LD R2, LAB_40
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_41
  LD R2, LAB_42
LAB_43
  ADD R0, R0, R2
  BRn LAB_44
  ADD R7, R7, 1
  BR LAB_43
  ; 
LAB_44
  ADD R7, R7, 0
  BRz LAB_55
  LD R2, LAB_30
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_55
  LD R2, LAB_45
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_46
  LD R2, LAB_47
LAB_48
  ADD R0, R0, R2
  BRn LAB_49
  ADD R7, R7, 1
  BR LAB_48
  ; 
LAB_49
  ADD R7, R7, 0
  BRz LAB_54
  LD R2, LAB_30
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_54
  ADD R0, R0, 10
  ; 
  LD R7, LAB_30
LAB_51
  ADD R7, R7, R0
  STR R7, R1, 0
  ADD R1, R1, 1
  AND R2, R2, 0
  STR R2, R1, 0
  ADD R5, R5, -1
  LDR R7, R5, 0
  LEA R0, LAB_28
  ADD R5, R5, 1
  STR R0, R5, -1
  RET
  ; 
  ; data
  ; 
LAB_28
  .BLKW 7
LAB_29
.FILL 45
LAB_30
.FILL 48
LAB_32
.FILL -10000
LAB_37
.FILL -1000
LAB_42
.FILL -100
LAB_47
.FILL -10
LAB_52
.FILL -1
LAB_35
.FILL 10000
LAB_40
.FILL 1000
LAB_45
.FILL 100
LAB_50
.FILL 10
LAB_53
.FILL 1
  ; 
  ;  print newline 
  ; 
LAB_1
  ADD R5, R5, 1
  STR R7, R5, -1
  LD R0, 1
  BR LAB_58
.FILL LAB_59
LAB_59
.STRINGZ "\n"
LAB_58
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
  BRp LAB_61
  ADD R4, R4, 0
  BRp LAB_62
LAB_63
  ADD R1, R5, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R1, R4, R1
  BRp LAB_60
  BR LAB_62
LAB_61
  ADD R4, R4, 0
  BRn LAB_60
  BR LAB_63
LAB_62
  TRAP x25
LAB_60
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
LAB_64
  STR R2, R0, 0
  ADD R0, R0, 1
  ADD R1, R1, -1
  BRp LAB_64
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
  BRz LAB_66
  ;  if a is positive, jump
  BRp LAB_67
  ;  negate a 
  NOT R0, R0
  ADD R0, R0, 1
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_66
  ;  if b is positive, jump
  BRp LAB_68
  ;  a is neg, b is too
  ;  negate b
LAB_69
  NOT R1, R1
  ADD R1, R1, 1
  ;  go, multiply!
  BR LAB_65
LAB_68
  ;  a is negative, b is positive
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  ;  go, multiply!
  BR LAB_65
LAB_67
  ;  a is positive 
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_66
  ;  if b is positive, go multiply!
  BRp LAB_65
  ;  a is pos, b is neg
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  BR LAB_69
  ; 
  ;  multiply 
  ; 
LAB_65
  ADD R5, R5, 1
  STR R7, R5, -1
  ;  reset sum
  AND R7, R7, 0
LAB_70
  ADD R7, R7, R0
  ADD R1, R1, -1
  BRp LAB_70
  ;  adjust sign 
  ADD R2, R2, 0
  BRz LAB_71
  NOT R7, R7
  ADD R7, R7, 1
LAB_71
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
LAB_66
  ADD R5, R5, 1
  STR R2, R5, -1
  RET
  ; 
  ;  null pointer exception 
  ; 
  ;  prints error message and exits
LAB_7
  LEA R0, LAB_72
  TRAP x22
  TRAP x25
LAB_72
.STRINGZ "Null pointer exception\n"
  ; 
  ;  index out of bounds exception 
  ; 
  ;  prints error message and exits
LAB_8
  LEA R0, LAB_73
  TRAP x22
  TRAP x25
LAB_73
.STRINGZ "Index out of bounds exception\n"
  ; 
  ;  add two strings 
  ; 
  ;  expects args on top of stack, puts result on stack
LAB_6
  LDR R0, R5, -2
  BRnp LAB_77
  LEA R0, LAB_79
  STR R0, R5, -2
  BR LAB_78
LAB_77
  LDR R0, R5, -1
  BRnp LAB_78
  LEA R0, LAB_79
  STR R0, R5, -1
  BR LAB_78
LAB_79
.FILL LAB_80
.FILL 5
LAB_80
.STRINGZ "null"
LAB_78
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
  BR LAB_75
LAB_76
.FILL LAB_2
LAB_74
.FILL 3
  ;  copy string 
LAB_82
.FILL LAB_81
LAB_81
  ADD R5, R5, 1
  STR R7, R5, -1
  LDR R0, R0, 0
LAB_84
  LDR R7, R0, 0
  BRz LAB_83
  STR R7, R1, 0
  ADD R0, R0, 1
  ADD R1, R1, 1
  BR LAB_84
LAB_83
  ADD R5, R5, -1
  LDR R7, R5, 0
  RET
LAB_75
  ;  invoke new method 
  LD R1, LAB_74
  ADD R0, R1, R0
  ADD R5, R5, 1
  STR R0, R5, -1
  LD R1, LAB_76
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
  LD R7, LAB_82
  JSRR R7
  ;  copy second string from TMP0->0 to HP->0 
  ADD R5, R5, -1
  LDR R0, R5, 0
  LD R7, LAB_82
  JSRR R7
  ADD R5, R5, -1
  LDR R7, R5, 0
  ADD R5, R5, 1
  STR R4, R5, -1
  RET
.END
