`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/07 10:58:03
// Design Name: 
// Module Name: mips
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


module mips(
	input wire clka,rst,
	output wire[31:0] pc,
	input wire[31:0] instr,
	output wire memwrite,
	output wire[31:0] aluout,writedata,
	input wire[31:0] readdata,

	/////
	output wire [31:0] alu_srca_sim, alu_srcb_sim, SignImmE_sim, WriteRegM_sim, WriteRegE_sim,
	output wire [1:0] ForwardAE_sim,
	output wire RegWriteM_sim, regdst_sim, alusrc_sim,
	output wire [4:0] rtE_sim, rsE_sim, ALUSrcD_sim,
	output wire [5:0] sigsD_sim, sigsE_sim
    );
	assign RegWriteM_sim = RegWriteM, regdst_sim = regdst, alusrc_sim = alusrc;
	////
	wire memtoreg,alusrc,regdst,regwrite,jump,branch,overflow;
	wire[2:0] alucontrol;
	wire RegWriteW, RegWriteM, MemtoRegE;
	wire [31:0] instrD;

	
	controller c(
	.clka(clka),
	.rst(rst),
    .op(instrD[31:26]),
    .funct(instrD[5:0]),
    .ALUControl(alucontrol), 
    .RegDst(regdst), 
	.Branch(branch),  
	.MemtoReg(memtoreg), 
	.MemWrite(memwrite), 
	.ALUSrc(alusrc), 
	.RegWrite(regwrite), 
	.Jump(jump),
	.RegWriteM(RegWriteM),
	.RegWriteW(RegWriteW),
	.MemtoRegE(MemtoRegE),
	.MemtoRegM(MemtoRegM),
	.RegWriteE(RegWriteE),

	.ALUSrcD_sim(ALUSrcD_sim),
	.sigsD_sim(sigsD_sim),
	.sigsE_sim(sigsE_sim)
    );

	pipeline_datapath d(
    .clka(clka), 
	.rst(rst),
    .memtoreg(memtoreg),
	.alusrc(alusrc),
	.regdst(regdst),
	.regwrite(regwrite),
	.jump(jump),
	.branch(branch),
    .alucontrol(alucontrol),
    .overflow(overflow),
    .pc(pc), 
	.instr(instr), 
	.ALUResult(),
	.ALUResultM(aluout), 
	.writedataM(writedata), 
	.mem_read_data(readdata),
	.RegWriteM(RegWriteM),
	.RegWriteW(RegWriteW),
	.MemtoRegE(MemtoRegE),
	.MemtoRegM(MemtoRegM),
	.RegWriteE(RegWriteE),
	.instrD(instrD),

	///////
	.alu_srca_sim(alu_srca_sim),
	.alu_srcb_sim(alu_srcb_sim),
	.SignImmE_sim(SignImmE_sim),
	.ForwardAE_sim(ForwardAE_sim),
	.WriteRegM_sim(WriteRegM_sim),
	.rtE_sim(rtE_sim),
	.WriteRegE_sim(WriteRegE_sim),
	.rsE_sim(rsE_sim)
    );


	
endmodule
