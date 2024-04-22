`define OP_R_TYPE      6'b000000
//logic inst
`define OP_NOP			6'b000000
`define OP_AND 		6'b100100
`define OP_OR 			6'b100101
`define OP_XOR 		6'b100110
`define OP_NOR			6'b100111
`define OP_ANDI		6'b001100
`define OP_ORI			6'b001101
`define OP_XORI		6'b001110
`define OP_LUI			6'b001111
//shift inst
`define OP_SLL			6'b000000
`define OP_SLLV		6'b000100
`define OP_SRL 		6'b000010
`define OP_SRLV 		6'b000110
`define OP_SRA 		6'b000011
`define OP_SRAV 		6'b000111
//move inst
`define OP_MFHI  		6'b010000
`define OP_MTHI  		6'b010001
`define OP_MFLO  		6'b010010
`define OP_MTLO  		6'b010011
//��������
`define OP_SLT     6'b101010
`define OP_SLTU    6'b101011
`define OP_SLTI    6'b001010
`define OP_SLTIU   6'b001011   
`define OP_ADD     6'b100000
`define OP_ADDU    6'b100001
`define OP_SUB     6'b100010
`define OP_SUBU    6'b100011
`define OP_ADDI    6'b001000
`define OP_ADDIU   6'b001001

`define OP_MULT    6'b011000
`define OP_MULTU   6'b011001

`define OP_DIV  6'b011010
`define OP_DIVU  6'b011011
//jump
`define OP_J  6'b000010
`define OP_JAL  6'b000011
`define OP_JALR  6'b001001
`define OP_JR  6'b001000
//branch
`define OP_BEQ  6'b000100
`define OP_BGTZ  6'b000111
`define OP_BNE  6'b000101
`define OP_BLEZ  6'b000110
`define OP_BRANCHS 6'b000001   //
`define OP_BLTZ  5'b00000
`define OP_BLTZAL  5'b10000
`define OP_BGEZAL  5'b10001
`define OP_BGEZ  5'b00001
//load/store
`define OP_LB  6'b100000
`define OP_LBU  6'b100100
`define OP_LH  6'b100001
`define OP_LHU  6'b100101
`define OP_LL  6'b110000
`define OP_LW  6'b100011
`define OP_LWL  6'b100010
`define OP_LWR  6'b100110
`define OP_SB  6'b101000
`define OP_SC  6'b111000
`define OP_SH  6'b101001
`define OP_SW  6'b101011
`define OP_SWL  6'b101010
`define OP_SWR  6'b101110
//trap
`define OP_SYSCALL 6'b001100
`define OP_BREAK 6'b001101

`define OP_TEQ 6'b110100
`define OP_TEQI 5'b01100
`define OP_TGE 6'b110000
`define OP_TGEI 5'b01000
`define OP_TGEIU 5'b01001
`define OP_TGEU 6'b110001
`define OP_TLT 6'b110010
`define OP_TLTI 5'b01010
`define OP_TLTIU 5'b01011
`define OP_TLTU 6'b110011
`define OP_TNE 6'b110110
`define OP_TNEI 5'b01110
   
// `define OP_ERET 32'b01000010000000000000000000011000
`define OP_ERET 26'b10000000000000000000011000
`define OP_ERET_MFTC 6'b010000

`define OP_SYNC 6'b001111
`define OP_PREF 6'b110011

`define OP_MTC0 5'b00100
`define OP_MFC0 5'b00000
`define OP_RELU 6'b111111