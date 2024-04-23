`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/18 09:54:22
// Design Name: 
// Module Name: datapath
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


module datapath(
    input wire clka, rst,
    input wire memtoreg,alusrc,regdst,regwrite,jump,branch,
    input [31:0] instr, mem_read_data,
    input wire [2:0] alucontrol,
    output wire overflow,
    output wire [31:0] pc, ALUResult, writedata,

    // 用于仿真
    output wire [31:0] num2_sim, num1_sim,
    output wire [31:0] RF_write_data,
    output wire [4:0] RF_write_adr
    );

wire [31:0] rd1, rd2;   // read_data
wire [31:0] SignImm;
wire [31:0] PCBranch;
wire [31:0] SignImm_sl2, Jump_adr;
wire [31:0] pc_add_4, alu_srcb, pc_next, pc_next_tmp, wd3;
wire [4:0] wa3;
wire pcsrc;

assign writedata = rd2;
assign RF_write_data = wd3, RF_write_adr = wa3;
// reg_file
regfile regfile(
	.clk(~clka),
	.we3(regwrite),
	.ra1(instr[25:21]), // base
    .ra2(instr[20:16]), // sw, load from rt
    .wa3(wa3), // lw, store to rt
	.wd3(wd3),
	.rd1(rd1),
    .rd2(rd2)
    );

// pc
D_flip_flop PC(
    .clk(clka),
    .rst(rst),
    .din(pc_next),
    .q(pc)
    );

// pc + 4
Add_4 pc_add_4_adder(
    .a(pc),
    .res(pc_add_4)
    );

// mux2 for reg_file_wd3
Mux2 mux_regfile_wd3(
    .a(mem_read_data),
    .b(ALUResult),
    .s(memtoreg),
    .y(wd3)
    );

// mux for reg_file_a3
Mux2 #(5) mux_regfile_a3(
    .a(instr[15:11]),
    .b(instr[20:16]),
    .s(regdst),
    .y(wa3)
    );

// sign_extend
sign_extend sign_extend(
    .a(instr[15:0]),
    .y(SignImm)
    );

// mux2 for alu_srcb
Mux2 mux_alu_srcB(
    .a(SignImm),
    .b(rd2),
    .s(alusrc),
    .y(alu_srcb)
    );

// ALU
ALU ALU(
    .num1(rd1),
    .num2(alu_srcb),
    .op(alucontrol),    // 操作码
    .res(ALUResult),   // 计算结果
    .overflow(),
    .Zero(zero),

    .num2_sim(num2_sim),
    .num1_sim(num1_sim)
    );

// 得到PCBranch
assign SignImm_sl2 = {SignImm[29:0],2'b00}; // shift left 2
assign PCBranch = SignImm_sl2 + pc_add_4;

// mux for pc_next_tmp (branch?)
assign pcsrc = zero & branch;
Mux2 mux_pc_next_tmp(
    .a(PCBranch),
    .b(pc_add_4),
    .s(pcsrc),
    .y(pc_next_tmp)
    );

// mux for pc_next (jump?)
assign Jump_adr[27:0] = {instr[25:0],2'b00}; // shift left 2
assign Jump_adr[31:28] = pc_add_4[31:28];
Mux2 mux_pc_next(
    .a(Jump_adr),
    .b(pc_next_tmp),
    .s(jump),
    .y(pc_next)
);


endmodule
