`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/07 13:50:53
// Design Name: 
// Module Name: top
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


module EX4_top(
	input wire clka,rst,
	output wire[31:0] writedata,  // rd2E
	output wire [31:0] dataadr,
	output wire memwrite	// data_ram 写使能

    // // 用于仿真
    // output wire [31:0] pc_sim, instr_sim,
    // output wire [31:0] alu_srca_sim, alu_srcb_sim, SignImmE_sim, 
    // output wire [1:0] ForwardAE_sim,
    // output wire RegWriteM_sim,
    // output wire [31:0] WriteRegM_sim,
    // output wire [4:0] rtE_sim,
    // output wire regdst_sim,
    // output wire [31:0] WriteRegE_sim,
    // output [4:0] rsE_sim,
    // output wire alusrc_sim, ALUSrcD_sim,
    // output wire [5:0] sigsD_sim, sigsE_sim
);
	wire[31:0] pc,instr,mem_read_data,ALUResult,instrD;  // instr是F级
	wire inst_ram_ena, data_ram_ena;
	wire [3:0] data_ram_wea;

assign dataadr = ALUResult;
assign data_ram_wea = {4{memwrite}};
assign inst_ram_ena = 1'b1;	// 使能有效
assign data_ram_ena = 1'b1;

//用于仿真
assign pc_sim = pc, instr_sim = instr;

// mips
mips mips(
	.clka(clka),
	.rst(rst),
	.pc(pc),
	.instr(instr),
	.memwrite(memwrite),
	.aluout(ALUResult),
	.writedata(writedata),
	.readdata(mem_read_data)

    // /////
    // .alu_srca_sim(alu_srca_sim),
    // .alu_srcb_sim(alu_srcb_sim),
    // .SignImmE_sim(SignImmE_sim),
    // .ForwardAE_sim(ForwardAE_sim),
    // .RegWriteM_sim(RegWriteM_sim),
    // .WriteRegM_sim(WriteRegM_sim),
    // .rtE_sim(rtE_sim),
    // .regdst_sim(regdst_sim),
    // .WriteRegE_sim(WriteRegE_sim),
    // .rsE_sim(rsE_sim),
    // .alusrc_sim(alusrc_sim),
    // .ALUSrcD_sim(ALUSrcD_sim),
    // .sigsD_sim(sigsD_sim),
  	// .sigsE_sim(sigsE_sim)
);


// inst_ram
// inst_ram inst_ram (
//   .clka(~clka),    // input wire clka
//   .ena(inst_ram_ena),      // input wire ena
//   .wea(4'b0000),      // input wire [3 : 0] wea
//   .addra(pc),  // input wire [31 : 0] addra
//   .dina(32'b0),    // input wire [31 : 0] dina
//   .douta(instr)  // output wire [31 : 0] douta
// );
InstMem inst_mem (
  .clka(~clka),    // input wire clka
  .ena(inst_ram_ena),      // input wire ena
  .addra(pc),  // input wire [31 : 0] addra
  .douta(instr)  // output wire [31 : 0] douta
);


// data_ram
// data_ram data_ram (
//   .clka(~clka),    // input wire clka
//   .ena(data_ram_ena),      // input wire ena
//   .wea(data_ram_wea),      // input wire [3 : 0] wea
//   .addra(ALUResult),  // input wire [31 : 0] addra
//   .dina(writedata),    // input wire [31 : 0] dina
//   .douta(mem_read_data)  // output wire [31 : 0] douta
// );
DataMem data_mem (
  .clka(~clka),            // input wire clka
  .rsta(0),            // input wire rsta
  .ena(data_ram_ena),              // input wire ena
  .wea(data_ram_wea),              // input wire [3 : 0] wea
  .addra(ALUResult),          // input wire [31 : 0] addra
  .dina(writedata),            // input wire [31 : 0] dina
  .douta(mem_read_data),          // output wire [31 : 0] douta
  .rsta_busy()  // output wire rsta_busy
);
endmodule
