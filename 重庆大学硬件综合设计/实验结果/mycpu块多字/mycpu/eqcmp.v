`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/23 22:57:01
// Design Name: 
// Module Name: eqcmp
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


module eqcmp(
	input wire [31:0] a,b,
	input wire [5:0]op,
	input wire [4:0]rt,
	output wire y
    );

	assign y = (op == `OP_BEQ) ? (a == b) :
			   (op == `OP_BNE) ? (a != b) :
			   (op == `OP_BRANCHS && (rt == `OP_BGEZ || rt == `OP_BGEZAL)) ? (a[31] == 0) :
			   (op == `OP_BGTZ) ? ($signed(a) > 0) :
			   (op == `OP_BLEZ) ? ($signed(a) <= 0) :
			   (op == `OP_BRANCHS && (rt == `OP_BLTZ || rt == `OP_BLTZAL)) ? ($signed(a) < 0) : 0;
endmodule
