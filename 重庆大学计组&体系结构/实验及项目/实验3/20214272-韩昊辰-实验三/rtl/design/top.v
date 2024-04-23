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


module top(
	input wire clka,rst,
	output wire[31:0] writedata,
	output wire [31:0] dataadr,
	output wire memwrite,	// data_ram 写使能

	// 用于仿真
	output wire [31:0] pc_sim, instr_sim, ALUResult_sim,
	output wire [4:0] rs, rt, rd,
	output wire [31:0] num2_sim, mem_read_data_sim, num1_sim,
	output wire [31:0] RF_write_data, 
	output wire [4:0] RF_write_adr
);
	wire[31:0] pc,instr,mem_read_data,ALUResult;
	wire inst_ram_ena, data_ram_ena;
	wire [3:0] data_ram_wea;

assign dataadr = {{16{instr[15]}},instr[15:0]};
assign data_ram_wea = {4{memwrite}};
assign inst_ram_ena = 1'b1;	// 使能有效
assign data_ram_ena = 1'b1;

// 用于仿真
assign pc_sim = pc, instr_sim = instr, ALUResult_sim = ALUResult;
assign rs = instr[25:21], rt = instr[20:16], rd = instr[15:11];
assign mem_read_data_sim = mem_read_data;

// mips
mips mips(
	.clka(clka),
	.rst(rst),
	.pc(pc),
	.instr(instr),
	.memwrite(memwrite),
	.aluout(ALUResult),
	.writedata(writedata),
	.readdata(mem_read_data),

	.num2_sim(num2_sim),
	.num1_sim(num1_sim),
	.RF_write_data(RF_write_data), 
	.RF_write_adr(RF_write_adr)
);

// inst_ram
inst_ram inst_ram (
  .clka(~clka),    // input wire clka
  .ena(inst_ram_ena),      // input wire ena
  .wea(4'b0000),      // input wire [3 : 0] wea
  .addra(pc),  // input wire [31 : 0] addra
  .dina(32'b0),    // input wire [31 : 0] dina
  .douta(instr)  // output wire [31 : 0] douta
);


// data_ram
data_ram data_ram (
  .clka(~clka),    // input wire clka
  .ena(data_ram_ena),      // input wire ena
  .wea(data_ram_wea),      // input wire [3 : 0] wea
  .addra(ALUResult),  // input wire [31 : 0] addra
  .dina(writedata),    // input wire [31 : 0] dina
  .douta(mem_read_data)  // output wire [31 : 0] douta
);
endmodule
