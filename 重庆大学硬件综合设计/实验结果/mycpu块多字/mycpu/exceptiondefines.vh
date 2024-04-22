//Exception code
`define EXC_CODE_INT        5'h00     
`define EXC_CODE_ADEL      5'h04     
`define EXC_CODE_ADES       5'h05     
`define EXC_CODE_SYS        5'h08     
`define EXC_CODE_BP         5'h09     
`define EXC_CODE_RI         5'h0a     
`define EXC_CODE_OV         5'h0c     

//Exception type
`define EXC_TYPE_INT        32'h0000_0001  
`define EXC_TYPE_ADEL       32'h0000_0004  
`define EXC_TYPE_ADES       32'h0000_0005  
`define EXC_TYPE_SYS        32'h0000_0008  
`define EXC_TYPE_BP         32'h0000_0009  
`define EXC_TYPE_RI         32'h0000_000a  
`define EXC_TYPE_OV         32'h0000_000c  
`define EXC_TYPE_ERET       32'h0000_000e  
`define EXC_TYPE_NOEXC      32'h0000_0000