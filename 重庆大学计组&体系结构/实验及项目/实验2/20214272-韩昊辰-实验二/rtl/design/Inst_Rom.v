`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 19:07:21
// Design Name: 
// Module Name: Inst_Rom
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module Inst_Rom(
    input wire [31:0]address,
    input wire clk,
    output wire [31:0]opcode
    );
blk_mem_gen_0 Mem (
  .clka(clk),    // input wire clka
  .addra(address),  // input wire [31 : 0] addra
  .douta(opcode)  // output wire [31 : 0] douta
);
endmodule

