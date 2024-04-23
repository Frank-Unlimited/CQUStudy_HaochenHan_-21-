`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/07 13:54:42
// Design Name: 
// Module Name: testbench
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


module testbench();
	reg clk;
	reg rst;

	wire[31:0] writedata,dataadr;
	wire memwrite;
	// wire [31:0] pc, instr;
	// wire [31:0] alu_srca_sim, alu_srcb_sim, SignImmE_sim, WriteRegM_sim,WriteRegE_sim;
	// wire [1:0] ForwardAE_sim;
	// wire RegWriteM_sim,regdst_sim, alusrc_sim , ALUSrcD_sim ;
	// wire [4:0] rtE_sim,rsE_sim;
	// wire [5:0] sigsD_sim, sigsE_sim;
	EX4_top dut(clk,rst,writedata,dataadr,memwrite);//,  pc, instr, alu_srca_sim, alu_srcb_sim, SignImmE_sim, ForwardAE_sim,RegWriteM_sim, WriteRegM_sim,rtE_sim,regdst_sim,WriteRegE_sim,rsE_sim, alusrc_sim, ALUSrcD_sim, sigsD_sim, sigsE_sim

	initial begin 
		rst <= 1;
		#200;
		rst <= 0;
	end

	always begin
		clk <= 1;
		#10;
		clk <= 0;
		#10;
	
	end

	always @(negedge clk) begin
		if(memwrite) begin
			/* code */
			if(dataadr === 84 & writedata === 7) begin
				/* code */
				$display("Simulation succeeded");
				$stop;
			end else if(dataadr !== 80) begin
				/* code */
				$display("Simulation Failed");
				$stop;
			end
		end
	end
endmodule
