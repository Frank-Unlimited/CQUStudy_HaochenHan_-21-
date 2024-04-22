`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/22 10:23:13
// Design Name: 
// Module Name: hazard
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


module hazard(
	//fetch stage
	output wire stallF,
	//decode stage
	input wire[4:0] rsD,rtD,
	input wire branchD,pcjrD,
	output wire forwardaD,forwardbD,
	output wire stallD,
	output wire flushD,
	//execute stage
	input wire[4:0] rsE,rtE,rdE,
	input wire[4:0] writeregE,
	input wire regwriteE,
	input wire memtoregE,
	input wire div_stallE,
	input wire mul_stallE,
	input wire hilotoregE,
	input wire cp0toregE,
	output reg[1:0] forwardaE,forwardbE,
	output wire flushE,
	output wire stallE,
	//mem stage
	input wire[4:0] writeregM,
	input wire regwriteM,
	input wire memtoregM,
	input wire all_flushM,
	output wire stallM,
	output wire flushM,
	//write back stage
	input wire[4:0] writeregW,
	input wire regwriteW,
	output wire stallW,
	output wire flushW,

	input wire i_stall, d_stall,
	output wire all_stall
    );

	wire lwstallD,branchstallD,jrstallD,hilostallD;

	//forwarding sources to D stage (branch equality)
	assign forwardaD = (rsD != 0 & rsD == writeregM & regwriteM);
	assign forwardbD = (rtD != 0 & rtD == writeregM & regwriteM);
	
	//forwarding sources to E stage (ALU)

	always @(*) begin
		forwardaE = 2'b00;
		forwardbE = 2'b00;
		if(rsE != 0) begin
			/* code */
			if(rsE == writeregM & regwriteM) begin
				/* code */
				forwardaE = 2'b10;
			end else if(rsE == writeregW & regwriteW) begin
				/* code */
				forwardaE = 2'b01;
			end
		end
		if(rtE != 0) begin
			/* code */
			if(rtE == writeregM & regwriteM) begin
				/* code */
				forwardbE = 2'b10;
			end else if(rtE == writeregW & regwriteW) begin
				/* code */
				forwardbE = 2'b01;
			end
		end
	end

	//stalls
	assign lwstallD = memtoregE & (rtE == rsD | rtE == rtD);

	assign hilostallD = hilotoregE & (rdE == rsD | rdE == rtD);

	assign cp0stallD = cp0toregE & (rtE == rsD | rtE == rtD);

	assign branchstallD = branchD &
				(regwriteE & 
				(writeregE == rsD | writeregE == rtD) |
				memtoregM &
				(writeregM == rsD | writeregM == rtD));
	// assign branchstallD = branchD &
	// 			(regwriteE & 
	// 			(writeregE == rsD | writeregE == rtD) |
	// 			0);
	assign jrstallD = pcjrD &
				(regwriteE & 
				(writeregE == rsD | writeregE == rtD) |
				memtoregM &
				(writeregM == rsD | writeregM == rtD));
	// assign jrstallD = pcjrD &
	// 			(regwriteE & 
	// 			(writeregE == rsD | writeregE == rtD) |
	// 			0);
	wire other_stall;
	assign other_stall = (lwstallD | branchstallD | jrstallD | hilostallD | cp0stallD) & ~all_flushM;
	assign all_stall = i_stall | d_stall | div_stallE | mul_stallE;

	assign stallF = all_stall | other_stall;
	assign stallD = all_stall | other_stall;
	assign stallE = all_stall;
	assign stallM = all_stall;
	assign stallW = all_stall;
		//stalling D stalls all previous stages
	assign flushD = all_flushM;
	assign flushE = other_stall & ~all_stall | all_flushM;
	assign flushM = all_flushM;
	assign flushW = all_flushM;

		//stalling D flushes next stage
	// Note: not necessary to stall D stage on store
  	//       if source comes from load;
  	//       instead, another bypass network could
  	//       be added from W to M
endmodule
