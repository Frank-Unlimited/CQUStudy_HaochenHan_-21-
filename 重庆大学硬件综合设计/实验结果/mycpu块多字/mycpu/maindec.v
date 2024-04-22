`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/10/23 15:21:30
// Design Name: 
// Module Name: maindec
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
`include "exceptiondefines.vh"

module maindec(
	input wire[5:0] op, funct,
	input wire [4:0] rt,
	input wire [31:0] instr,
	output reg ri,
	output wire memtoreg,memwrite,
	output reg memread,
	output wire sign_ext,
	output wire branch,alusrc,
	output wire regdst,regwrite,
	output wire jump,pcjr,
	output wire[1:0] mfhi_lo,
	output wire hilo_wen,
	output wire hilotoreg,
	output wire pctoreg,
	output wire rd31src,
	output wire brek,syscall,eret,
	output wire cp0_wen,cp0toreg
    );
	reg[6:0] controls;
	assign sign_ext = |(op[5:2] ^ 4'b0011);
	assign pcjr = (op == `OP_R_TYPE && (funct == `OP_JR || funct == `OP_JALR));
	assign pctoreg = (op == `OP_JAL) || (op == `OP_R_TYPE && funct == `OP_JALR) || (op == `OP_BRANCHS && (rt == `OP_BLTZAL || rt == `OP_BGEZAL));
	assign rd31src = (op == `OP_JAL || (op == `OP_BRANCHS && (rt == `OP_BLTZAL || rt == `OP_BGEZAL)));
	assign {regwrite,regdst,alusrc,branch,memwrite,memtoreg,jump} = controls;
	assign hilo_wen = ~(|( op ^ `OP_R_TYPE )) 
						& ( 	~(|(funct[5:2] ^ 4'b0110)) 			// div divu mult multu 	
							| 	( ~(|(funct[5:2] ^ 4'b0100)) & funct[0]) //mthi mtlo
						  );

	assign hilotoreg = (op == `OP_R_TYPE) && (funct == `OP_MFHI || funct == `OP_MFLO);
	wire mfhi;
	wire mflo;
	assign mfhi = !(op ^ `OP_R_TYPE) & !(funct ^ `OP_MFHI);
	assign mflo = !(op ^ `OP_R_TYPE) & !(funct ^ `OP_MFLO);
	assign brek = ~(|(op ^ `OP_R_TYPE)) & ~(|(funct ^ `OP_BREAK));
	assign syscall = ~(|(op ^ `OP_R_TYPE)) & ~(|(funct ^ `OP_SYSCALL));
	assign eret = ~(|(instr ^ {`OP_ERET_MFTC, `OP_ERET}));
	assign cp0_wen = ~(|(op ^ `OP_ERET_MFTC)) & ~(|(instr[25:21] ^ `OP_MTC0));
	assign cp0toreg = ~(|(op ^ `OP_ERET_MFTC)) & ~(|(instr[25:21] ^ `OP_MFC0));
	assign mfhi_lo = {mfhi, mflo};
	always @(*) begin
		ri = 1'b0;
		memread = 1'b0;
		case (op)
			`OP_RELU:
				case(funct)
					6'b0 : controls = 7'b1100000;
					default: begin ri = 1'b1; controls = 7'b0000000; end
				endcase
			`OP_R_TYPE:
				case(funct)
					`OP_JR,`OP_MULT,`OP_MULTU,`OP_DIV,`OP_DIVU,`OP_MTHI,`OP_MTLO,`OP_SYSCALL,`OP_BREAK: controls = 7'b0000000;
					`OP_JALR: controls = 7'b1100000;
					`OP_ADD,`OP_ADDU,`OP_SUB,`OP_SUBU,`OP_SLTU,`OP_SLT ,
					`OP_AND,`OP_NOR, `OP_OR, `OP_XOR,
					`OP_SLLV, `OP_SLL, `OP_SRAV, `OP_SRA, `OP_SRLV, `OP_SRL,
					`OP_MFHI, `OP_MFLO: controls = 7'b1100000;
					default: begin ri = 1'b1; controls = 7'b0000000; end//R-TYRE 只有算数运算指令才是7'B1100000 后续加乘除法和systemcall的时候要�??????
				endcase
			`OP_LW,`OP_LB,`OP_LBU,`OP_LH,`OP_LHU: begin memread = 1'b1; controls = 7'b1010010; end//LW
			`OP_SW,`OP_SB,`OP_SH:controls = 7'b0010100;//SW
			`OP_BEQ,`OP_BNE,`OP_BGTZ,`OP_BLEZ:controls = 7'b0001000;//BEQ
			`OP_BRANCHS:
				case (rt)
					`OP_BGEZ,`OP_BLTZ: controls = 7'b0001000;
					`OP_BGEZAL,`OP_BLTZAL: controls = 7'b1101000;
				endcase
			`OP_ADDI,`OP_SLTI,`OP_ADDIU,`OP_SLTIU,`OP_ANDI,`OP_ORI,`OP_XORI,`OP_LUI:controls = 7'b1010000;//ADDI
			
			`OP_J:controls = 7'b0000001;//J
			`OP_JAL:controls = 7'b1100001;
			`OP_ERET_MFTC:begin
				case(instr[25:21])
					`OP_MTC0: begin
						controls = 7'b0000000;
					end
					`OP_MFC0: begin
						controls = 7'b1000000;
					end
					default: begin
						ri  =  |(instr[25:0] ^ `OP_ERET);
						controls = 7'b0000000;
					end
				endcase
			end
			default: begin ri = 1'b1; controls = 7'b0000000; end//illegal op
		endcase
	end
endmodule
