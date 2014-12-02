.ORIG x3000
  ;  initialize CONST0 and CONST1
  AND R2, R2, 0
  ADD R3, R2, 1
  ;  set SFP and HP 
  LD R6, LAB_15
  LD R4, LAB_16
  BR LAB_14
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
LAB_14
  ; 
  ; BEGIN A virtual method table
  ; 
LAB_10
  ; 
  ; END A virtual method table
  ; 
LAB_13
  ; 
  ;  METHOD main
  ; 
  ;  save SFP R7 and initialize SP R5
  STR R7, R6, 2
  ADD R5, R6, 4
  ;  body 
  ; 
  ;  STRING CONST BEGIN 
  ; 
  BR LAB_24
LAB_23
.STRINGZ "Hello World!"
LAB_21
.FILL LAB_23
.FILL 14
LAB_22
.FILL LAB_21
LAB_24
  LD R0, LAB_22
  ADD R5, R5, 1
  STR R0, R5, -1
  ; 
  ;  STRING CONST END 
  ; 
  ADD R5, R5, -1
  LDR R0, R5, 0
  BRz LAB_26
  LDR R0, R0, 0
  BR LAB_19
LAB_26
  LEA R0, LAB_25
  BR LAB_19
LAB_25
.STRINGZ "null"
  BR LAB_19
LAB_20
.FILL LAB_1
LAB_19
  TRAP x22
  LD R1, LAB_20
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
  ; BEGIN Object virtual method table
  ; 
LAB_11
  ; 
  ; END Object virtual method table
  ; 
  ; 
  ; BEGIN String virtual method table
  ; 
LAB_12
  ; 
  ; END String virtual method table
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
  LEA R1, LAB_27
  ADD R0, R0, 0
  BRzp LAB_30
  LD R7, LAB_28
  STR R7, R1, 0
  ADD R1, R1, 1
  NOT R0, R0
  ADD R0, R0, 1
LAB_30
  AND R7, R7, 0
  LD R2, LAB_31
LAB_32
  ADD R0, R0, R2
  BRn LAB_33
  ADD R7, R7, 1
  BR LAB_32
  ; 
LAB_33
  ADD R7, R7, 0
  BRz LAB_56
  LD R2, LAB_29
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_56
  LD R2, LAB_34
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_35
  LD R2, LAB_36
LAB_37
  ADD R0, R0, R2
  BRn LAB_38
  ADD R7, R7, 1
  BR LAB_37
  ; 
LAB_38
  ADD R7, R7, 0
  BRz LAB_55
  LD R2, LAB_29
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_55
  LD R2, LAB_39
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_40
  LD R2, LAB_41
LAB_42
  ADD R0, R0, R2
  BRn LAB_43
  ADD R7, R7, 1
  BR LAB_42
  ; 
LAB_43
  ADD R7, R7, 0
  BRz LAB_54
  LD R2, LAB_29
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_54
  LD R2, LAB_44
  ADD R0, R0, R2
  ; 
  AND R7, R7, 0
LAB_45
  LD R2, LAB_46
LAB_47
  ADD R0, R0, R2
  BRn LAB_48
  ADD R7, R7, 1
  BR LAB_47
  ; 
LAB_48
  ADD R7, R7, 0
  BRz LAB_53
  LD R2, LAB_29
  ADD R7, R7, R2
  STR R7, R1, 0
  ADD R1, R1, 1
LAB_53
  ADD R0, R0, 10
  ; 
  LD R7, LAB_29
LAB_50
  ADD R7, R7, R0
  STR R7, R1, 0
  ADD R1, R1, 1
  AND R2, R2, 0
  STR R2, R1, 0
  ADD R5, R5, -1
  LDR R7, R5, 0
  LEA R0, LAB_27
  ADD R5, R5, 1
  STR R0, R5, -1
  RET
  ; 
  ; data
  ; 
LAB_27
  .BLKW 7
LAB_28
.FILL 45
LAB_29
.FILL 48
LAB_31
.FILL -10000
LAB_36
.FILL -1000
LAB_41
.FILL -100
LAB_46
.FILL -10
LAB_51
.FILL -1
LAB_34
.FILL 10000
LAB_39
.FILL 1000
LAB_44
.FILL 100
LAB_49
.FILL 10
LAB_52
.FILL 1
  ; 
  ;  print newline 
  ; 
LAB_1
  ADD R5, R5, 1
  STR R7, R5, -1
  LD R0, 1
  BR LAB_57
.FILL LAB_58
LAB_58
.STRINGZ "\n"
LAB_57
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
  BRp LAB_60
  ADD R4, R4, 0
  BRp LAB_61
LAB_62
  ADD R1, R5, 0
  NOT R1, R1
  ADD R1, R1, 1
  ADD R1, R4, R1
  BRp LAB_59
  BR LAB_61
LAB_60
  ADD R4, R4, 0
  BRn LAB_59
  BR LAB_62
LAB_61
  TRAP x25
LAB_59
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
LAB_63
  STR R2, R0, 0
  ADD R0, R0, 1
  ADD R1, R1, -1
  BRp LAB_63
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
  BRz LAB_65
  ;  if a is positive, jump
  BRp LAB_66
  ;  negate a 
  NOT R0, R0
  ADD R0, R0, 1
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_65
  ;  if b is positive, jump
  BRp LAB_67
  ;  a is neg, b is too
  ;  negate b
LAB_68
  NOT R1, R1
  ADD R1, R1, 1
  ;  go, multiply!
  BR LAB_64
LAB_67
  ;  a is negative, b is positive
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  ;  go, multiply!
  BR LAB_64
LAB_66
  ;  a is positive 
  ADD R1, R1, 0
  ;  if one is zero we're done
  BRz LAB_65
  ;  if b is positive, go multiply!
  BRp LAB_64
  ;  a is pos, b is neg
  ;  set flag to 1 for negative result
  ADD R2, R2, 1
  BR LAB_68
  ; 
  ;  multiply 
  ; 
LAB_64
  ADD R5, R5, 1
  STR R7, R5, -1
  ;  reset sum
  AND R7, R7, 0
LAB_69
  ADD R7, R7, R0
  ADD R1, R1, -1
  BRp LAB_69
  ;  adjust sign 
  ADD R2, R2, 0
  BRz LAB_70
  NOT R7, R7
  ADD R7, R7, 1
LAB_70
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
LAB_65
  ADD R5, R5, 1
  STR R2, R5, -1
  RET
  ; 
  ;  null pointer exception 
  ; 
  ;  prints error message and exits
LAB_7
  LEA R0, LAB_71
  TRAP x22
  TRAP x25
LAB_71
.STRINGZ "Null pointer exception
"
  ; 
  ;  index out of bounds exception 
  ; 
  ;  prints error message and exits
LAB_8
  LEA R0, LAB_72
  TRAP x22
  TRAP x25
LAB_72
.STRINGZ "Index out of bounds exception
"
  ; 
  ;  add two strings 
  ; 
  ;  expects args on top of stack, puts result on stack
LAB_6
  LDR R0, R5, -2
  BRnp LAB_76
  LEA R0, LAB_78
  STR R0, R5, -2
  BR LAB_77
LAB_76
  LDR R0, R5, -1
  BRnp LAB_77
  LEA R0, LAB_78
  STR R0, R5, -1
  BR LAB_77
LAB_78
.FILL LAB_79
.FILL 5
LAB_79
.STRINGZ "null"
LAB_77
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
  BR LAB_74
LAB_75
.FILL LAB_2
LAB_73
.FILL 3
  ;  copy string 
LAB_81
.FILL LAB_80
LAB_80
  ADD R5, R5, 1
  STR R7, R5, -1
  LDR R0, R0, 0
LAB_83
  LDR R7, R0, 0
  BRz LAB_82
  STR R7, R1, 0
  ADD R0, R0, 1
  ADD R1, R1, 1
  BR LAB_83
LAB_82
  ADD R5, R5, -1
  LDR R7, R5, 0
  RET
LAB_74
  ;  invoke new method 
  LD R1, LAB_73
  ADD R0, R1, R0
  ADD R5, R5, 1
  STR R0, R5, -1
  LD R1, LAB_75
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
  LD R7, LAB_81
  JSRR R7
  ;  copy second string from TMP0->0 to HP->0 
  ADD R5, R5, -1
  LDR R0, R5, 0
  LD R7, LAB_81
  JSRR R7
  ADD R5, R5, -1
  LDR R7, R5, 0
  ADD R5, R5, 1
  STR R4, R5, -1
  RET
.END
