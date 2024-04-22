`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/02 14:25:38
// Design Name: 
// Module Name: adder
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


module i_burst(
	input wire clk, rst,
	// axi
    output  reg [255:0] cache_inst_rdata   ,
    output  reg        cache_inst_addr_ok ,
    output  reg        cache_inst_data_ok ,
	output  reg [3:0]  cnt,

	// mips
	input   [31:0] cache_inst_rdata_iburst   ,
    input          cache_inst_addr_ok_iburst ,
    input          cache_inst_data_ok_iburst 
    );
	assign cache_inst_addr_ok = cache_inst_addr_ok_iburst;
	assign cache_inst_rdata = 0;
	reg [31:0] cache_inst_rdata1, cache_inst_rdata2, cache_inst_rdata3, cache_inst_rdata4, cache_inst_rdata5, cache_inst_rdata6, cache_inst_rdata7, cache_inst_rdata8;

	
	always @(posedge clk) begin
		if(rst) begin
			cnt <= 0;
		end
		else if(cnt == 0) begin
			if(cache_inst_data_ok_iburst) begin
				cnt <= 1;
				cache_inst_rdata1 <= cache_inst_rdata_iburst;
			end else begin
				cache_inst_data_ok <= 0;
			end
		end
		else if(cnt == 1) begin
			cnt <= 2;
			cache_inst_rdata2 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 2) begin
			cnt <= 3;
			cache_inst_rdata3 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 3) begin
			cnt <= 4;
			cache_inst_rdata4 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 4) begin
			cnt <= 5;
			cache_inst_rdata5 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 5) begin
			cnt <= 6;
			cache_inst_rdata6 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 6) begin
			cnt <= 7;
			cache_inst_rdata7 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 7) begin
			cnt <= 8;
			cache_inst_rdata8 <= cache_inst_rdata_iburst;
		end
		else if(cnt == 8) begin
			cnt <= 0;
			cache_inst_rdata[31:0]    <= cache_inst_rdata1;
			cache_inst_rdata[63:32]   <= cache_inst_rdata2;
			cache_inst_rdata[95:64]   <= cache_inst_rdata3;
			cache_inst_rdata[127:96]  <= cache_inst_rdata4;
			cache_inst_rdata[159:128] <= cache_inst_rdata5;
			cache_inst_rdata[191:160] <= cache_inst_rdata6;
			cache_inst_rdata[223:192] <= cache_inst_rdata7;
			cache_inst_rdata[255:224] <= cache_inst_rdata8;
			cache_inst_data_ok <= 1;
		end
	end
		
	
endmodule
