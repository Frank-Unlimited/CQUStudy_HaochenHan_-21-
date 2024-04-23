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
	
	// 用于仿真
    output wire [31:0] num2_sim, num1_sim,
	output wire [31:0] RF_write_data,
	output wire [4:0] RF_write_adr
    );
	
	wire memtoreg,alusrc,regdst,regwrite,jump,branch,overflow;
	wire[2:0] alucontrol;

	
	controller c(
    .op(instr[31:26]),
    .funct(instr[5:0]),
    .ALUControl(alucontrol), 
    .RegDst(regdst), 
	.Branch(branch),  
	.MemtoReg(memtoreg), 
	.MemWrite(memwrite), 
	.ALUSrc(alusrc), 
	.RegWrite(regwrite), 
	.Jump(jump)
    );

	datapath d(
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
	.ALUResult(aluout), 
	.writedata(writedata), 
	.mem_read_data(readdata),
	.num2_sim(num2_sim),
	.num1_sim(num1_sim),
	.RF_write_data(RF_write_data),
	.RF_write_adr(RF_write_adr)
    );
	
endmodule
