`include "exceptiondefines.vh"

module exception(
   input rst,
   input [5:0] ext_int,
   input ri, brek, syscall, overflow, AdES, AdEL, pcerror, eret,
   input [31:0] cp0_status, cp0_cause, cp0_epc,
   input [31:0] pcM,
   input [31:0] alu_outM,

   output [31:0] except_type,
   output flush_exception,
   output [31:0] pc_exception,
   output [31:0] badvaddrM
);

   //INTERUPT
   wire intt;
   //             //IE             //EXL            
   assign intt =   cp0_status[0] && ~cp0_status[1] && (
                     //IM                 //IP
                  ( |(cp0_status[9:8] & cp0_cause[9:8]) ) ||        //soft interupt
                  ( |(cp0_status[15:10] & ext_int) )           //hard interupt
   );

   assign except_type =    (intt)                   ? `EXC_TYPE_INT :
                           (AdEL | pcerror) ? `EXC_TYPE_ADEL :
                           (ri)                    ? `EXC_TYPE_RI :
                           (syscall)               ? `EXC_TYPE_SYS :
                           (brek)                 ? `EXC_TYPE_BP :
                           (AdES)           ? `EXC_TYPE_ADES :
                           (overflow)              ? `EXC_TYPE_OV :
                           (eret)                 ? `EXC_TYPE_ERET :
                                                     `EXC_TYPE_NOEXC;
   //interupt pc address
   assign pc_exception =      (except_type == `EXC_TYPE_NOEXC) ? `ZeroWord:
                           (except_type == `EXC_TYPE_ERET)? cp0_epc :       //存放上一次发生例外指令的 PC
                           32'hbfc0_0380;

   assign flush_exception =   (except_type == `EXC_TYPE_NOEXC) ? 1'b0:
                           1'b1;
   assign badvaddrM =      (pcerror) ? pcM : alu_outM;            //记录最新地址相关例外的出错地址

endmodule
