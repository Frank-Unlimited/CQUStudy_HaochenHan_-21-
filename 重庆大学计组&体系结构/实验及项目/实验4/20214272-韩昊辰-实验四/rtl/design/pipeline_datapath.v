`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/19 14:58:29
// Design Name: 
// Module Name: pipeline_datapath
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

module pipeline_datapath(
    input wire clka, rst,
    input wire memtoreg,alusrc,regdst,regwrite,jump,branch,
    input wire [31:0] instr, mem_read_data,
    input wire [2:0] alucontrol,
    input wire RegWriteM, RegWriteW, MemtoRegE, RegWriteE, MemtoRegM,
    output wire overflow,
    output wire [31:0] pc, ALUResult, writedataM, ALUResultM,//pc(F), ALUResult(E), writedatda(E)
    output wire [31:0] instrD,

    //用于debug
    output wire [31:0] alu_srca_sim, alu_srcb_sim, SignImmE_sim, WriteRegM_sim, WriteRegE_sim,
    output wire [1:0] ForwardAE_sim,
    output wire [4:0] rtE_sim, rsE_sim

    );
assign alu_srca_sim = alu_srca, alu_srcb_sim = alu_srcb, SignImmE_sim = SignImmE, WriteRegM_sim = wa3M;
assign ForwardAE_sim = ForwardAE;
assign rtE_sim = rtE, WriteRegE_sim = wa3, rsE_sim = rsE;
////////
wire StallF, StallD, FlushE;
wire [1:0] ForwardAE, ForwardBE;
wire ForwardAD, ForwardBD;
wire [4:0] wa3M;
wire [4:0] wa3W;

wire [4:0] rtE, rsE;
wire [31:0] rd1, rd2;   // read_data
wire [31:0] SignImm, writedata;
wire [31:0] PCBranch, PCBranchM;
wire [31:0] SignImm_sl2, Jump_adr;
wire [31:0] pc_add_4, alu_srcb, pc_next, pc_next_tmp, wd3;
wire [4:0] wa3;
wire pcsrc;
wire [31:0] pc_add_4D;
wire [4:0] rtD, rdD, rsD, rdE;
wire [31:0] SignImmE, pc_add_4E;
wire [31:0] alu_srca, alu_srcb_tmp, rd1E, rd2E, wd3W;
// M信号
wire zeroM;

// wire [4:0] wa3M;
// W信号
wire [31:0] ALUResultW, mem_read_dataW;

// F  //////////////////////////////////////////////////////////////////
// pc
D_flip_flop PC(
    .clk(clka),
    .rst(rst),
    .en(~StallF),
    .din(pc_next),
    .q(pc)
    );

// pc + 4
Add_4 pc_add_4_adder(
    .a(pc),
    .res(pc_add_4)
    );

// F-D
floprc  #(32) r11(.clk(clka), .rst(rst), .clear(pcsrc), .en(~StallD),  .din(instr), .q(instrD));
floprc  #(32) r12(.clk(clka), .rst(rst), .clear(0), .en(~StallD),  .din(pc_add_4), .q(pc_add_4D));

// D  //////////////////////////////////////////////////////////////////
wire [31:0] eq_input1, eq_input2;
// wire [4:0] rsD
assign rsD = instrD[25:21];
assign rtD = instrD[20:16];
assign rdD = instrD[15:11];
// reg_file
regfile regfile(
	.clk(~clka),
	.we3(regwrite),
	.ra1(instrD[25:21]), // base
    .ra2(instrD[20:16]), // sw, load from rt
    .wa3(wa3W), // lw, store to rt
	.wd3(wd3W),
	.rd1(rd1),
    .rd2(rd2)
    );

// mux2 for eq_input1 & eq_input2
wire EqualD;
Mux2 #(32) mux2_eq_input1(.a(ALUResultM), .b(rd1), .s(ForwardAD), .y(eq_input1));
Mux2 #(32) mux2_eq_input2(.a(ALUResultM), .b(rd2), .s(ForwardBD), .y(eq_input2));

assign EqualD = (eq_input1 == eq_input2);

// sign_extend
sign_extend sign_extend(
    .a(instrD[15:0]),
    .y(SignImm)
    );

// D-E
floprc  #(32) r21(.clk(clka), .rst(rst), .clear(FlushE), .en(1),  .din(rd1), .q(rd1E));
floprc  #(32) r22(.clk(clka), .rst(rst),.clear(FlushE), .en(1),  .din(rd2), .q(rd2E));
floprc  #(5) r23(.clk(clka), .rst(rst), .clear(FlushE), .en(1),.din(rtD), .q(rtE));
floprc  #(5) r24(.clk(clka), .rst(rst), .clear(FlushE), .en(1),.din(rdD), .q(rdE));
floprc  #(32) r25(.clk(clka), .rst(rst),.clear(FlushE), .en(1),  .din(SignImm), .q(SignImmE));
floprc  #(32) r26(.clk(clka), .rst(rst),.clear(FlushE), .en(1),  .din(pc_add_4D), .q(pc_add_4E));
floprc  #(5) r27(.clk(clka), .rst(rst), .clear(FlushE), .en(1),.din(rsD), .q(rsE));

// E  //////////////////////////////////////////////////////////////////
assign writedata = alu_srcb_tmp;

// mux3 for ForwardAE & ForwardBE
mux3 #(32) mux3_ALU_num1(.a(rd1E), .b(wd3W), .c(ALUResultM), .s(ForwardAE), .y(alu_srca));
mux3 #(32) mux3_ALU_num2_tmp(.a(rd2E), .b(wd3W), .c(ALUResultM), .s(ForwardBE), .y(alu_srcb_tmp));

// ALU
ALU alu(
    .num1(alu_srca),
    .num2(alu_srcb),
    .op(alucontrol),    // 操作码
    .res(ALUResult),   // 计算结果
    .overflow(),
    .Zero(zero)
    );

// mux2 for alu_srcb
Mux2 mux_alu_srcB(
    .a(SignImmE),
    .b(alu_srcb_tmp),
    .s(alusrc),
    .y(alu_srcb)
    );

// mux for reg_file_a3
Mux2 #(5) mux_regfile_a3(
    .a(rdE),
    .b(rtE),
    .s(regdst),
    .y(wa3)
    );

// 得到PCBranch(D)
assign SignImm_sl2 = {SignImm[29:0],2'b00}; // shift left 2
assign PCBranch = SignImm_sl2 + pc_add_4D;

// E-M
floprc  #(1) r31(.clk(clka), .rst(rst), .clear(0), .en(1), .din(zero), .q(zeroM));
floprc  #(32) r32(.clk(clka), .rst(rst), .clear(0), .en(1), .din(ALUResult), .q(ALUResultM));
floprc  #(32) r33(.clk(clka), .rst(rst), .clear(0), .en(1), .din(writedata), .q(writedataM));
floprc  #(5) r34(.clk(clka), .rst(rst), .clear(0), .en(1), .din(wa3), .q(wa3M));
floprc  #(32) r35(.clk(clka), .rst(rst), .clear(0), .en(1), .din(PCBranch), .q(PCBranchM));

// M  //////////////////////////////////////////////////////////////////

// M-W
floprc  #(32) r41(.clk(clka), .rst(rst), .clear(0), .en(1),  .din(ALUResultM), .q(ALUResultW));
floprc  #(32) r42(.clk(clka), .rst(rst), .clear(0), .en(1),  .din(mem_read_data), .q(mem_read_dataW));
floprc  #(32) r43(.clk(clka), .rst(rst), .clear(0), .en(1),  .din(wa3M), .q(wa3W));

// W  //////////////////////////////////////////////////////////////////
// mux2 for reg_file_wd3
Mux2 mux_regfile_wd3(
    .a(mem_read_dataW),
    .b(ALUResultW),
    .s(memtoreg),
    .y(wd3W)
    );

// mux for pc_next_tmp (branch?)
assign pcsrc = EqualD & branch;
Mux2 mux_pc_next_tmp(
    .a(PCBranch),
    .b(pc_add_4),
    .s(pcsrc),
    .y(pc_next_tmp)
    );

// mux for pc_next (jump?)
assign Jump_adr[27:0] = {instrD[25:0],2'b00}; // shift left 2
assign Jump_adr[31:28] = pc_add_4[31:28];
Mux2 mux_pc_next(
    .a(Jump_adr),
    .b(pc_next_tmp),
    .s(jump),
    .y(pc_next)
);

// HAZARD
Hazard_Unit hazard(
    .rsE(rsE), 
    .rtE(rtE), 
    .rsD(rsD), 
    .rtD(rtD),
    .WriteRegM(wa3M), 
    .WriteRegW(wa3W),
    .RegWriteM(RegWriteM), 
    .RegWriteW(RegWriteW),
    .ForwardAE(ForwardAE), 
    .ForwardBE(ForwardBE),
    .MemtoRegE(MemtoRegE),
    .StallF(StallF), 
    .StallD(StallD), 
    .FlushE(FlushE),
    .BranchD(branch), 
    .RegWriteE(RegWriteE), 
    .MemtoRegM(MemtoRegM),
    .ForwardAD(ForwardAD), 
    .ForwardBD(ForwardBD),
    .WriteRegE(wa3)
    );


endmodule
