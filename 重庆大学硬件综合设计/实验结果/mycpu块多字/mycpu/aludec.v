`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/10/23 15:27:24
// Design Name: 
// Module Name: aludec
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

`include "opcodedefines.vh"
`include "aludefines.vh"
module aludec(
    input wire [5:0] op_code,
	input wire [5:0] funct,
	output reg[4:0] alucontrol
    );
	always @(*) begin
		case (op_code)
		`OP_RELU:
				  case(funct)
				  	6'b0: alucontrol = `ALU_RELU;
					default: alucontrol = `ALU_DONOTHING;
				  endcase
		    `OP_R_TYPE:
		          case(funct)
				  	// arth
		              `OP_ADD: alucontrol = `ALU_ADD;
					  `OP_ADDU: alucontrol = `ALU_ADDU;
		              `OP_SUB: alucontrol = `ALU_SUB;
					  `OP_SUBU: alucontrol = `ALU_SUBU;
					  
					  `OP_SLT: alucontrol = `ALU_SLT;
					  `OP_SLTU: alucontrol = `ALU_SLTU;

					  `OP_MULT: alucontrol = `ALU_SIGNED_MULT;
					  `OP_MULTU: alucontrol = `ALU_UNSIGNED_MULT;
					  `OP_DIV: alucontrol = `ALU_SIGNED_DIV;
					  `OP_DIVU: alucontrol = `ALU_UNSIGNED_DIV;

					// logic
					  `OP_AND: alucontrol = `ALU_AND;
					  `OP_OR: alucontrol = `ALU_OR;
					  `OP_XOR: alucontrol = `ALU_XOR;
					  `OP_NOR: alucontrol = `ALU_NOR;

					// shift
					  `OP_SLL: alucontrol = `ALU_SLL_SA;
					  `OP_SRL: alucontrol = `ALU_SRL_SA;
					  `OP_SRA: alucontrol = `ALU_SRA_SA;
					  `OP_SLLV: alucontrol = `ALU_SLL;
					  `OP_SRLV: alucontrol = `ALU_SRL;
					  `OP_SRAV: alucontrol = `ALU_SRA;

					// move
					  `OP_MTHI: alucontrol = `ALU_MTHI;
					  `OP_MTLO: alucontrol = `ALU_MTLO;
		              default: alucontrol = `ALU_DONOTHING;
			      endcase
			// I TYPE
			`OP_LW, `OP_SW,`OP_LB,`OP_LH,`OP_SB,`OP_SH,`OP_LBU,`OP_LHU: alucontrol = `ALU_ADDU;
			`OP_ADDI: alucontrol = `ALU_ADD;
			`OP_ADDIU: alucontrol = `ALU_ADDU;
			`OP_SLTI: alucontrol = `ALU_SLT;
			`OP_SLTIU: alucontrol = `ALU_SLTU;
			`OP_BEQ,`OP_BNE,`OP_BGEZ,`OP_BGTZ,`OP_BLEZ,`OP_BLTZ: alucontrol = `ALU_SUBU; // 没有意义，beq不走exec阶段
			`OP_ANDI: alucontrol = `ALU_AND;
			`OP_ORI: alucontrol = `ALU_OR;
			`OP_XORI: alucontrol = `ALU_XOR;
			`OP_LUI: alucontrol = `ALU_LUI;

			default: alucontrol = `ALU_DONOTHING;
		endcase
	
	end
endmodule
