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
	wire [31:0] num2_sim,num1_sim;
	wire [31:0] mem_read_data_sim;
	wire [31:0] RF_write_data;
	wire [4:0] RF_write_adr;
	wire[31:0] writedata,dataadr;
	wire memwrite;
	wire [31:0] pc_sim, instr_sim, ALUResult_sim;
	wire [4:0] rs, rt, rd;
	top dut(
        .clka(clk),
        .rst(rst),
        .writedata(writedata),
        .dataadr(dataadr),
	    .memwrite(memwrite),
		.pc_sim(pc_sim), 
		.instr_sim(instr_sim), 
		.ALUResult_sim(ALUResult_sim),
		.rs(rs), 
		.rt(rt), 
		.rd(rd),
		.num2_sim(num2_sim),
		.mem_read_data_sim(mem_read_data_sim),
		.num1_sim(num1_sim),
		.RF_write_data(RF_write_data), 
		.RF_write_adr(RF_write_adr)
    );

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
