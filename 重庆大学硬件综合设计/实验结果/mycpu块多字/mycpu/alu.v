`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/02 14:52:16
// Design Name: 
// Module Name: alu
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

`include "aludefines.vh"

module alu(
	input wire clk, rst,
	input wire[31:0] a,b,
	input wire[4:0] alucontrol,
	input wire[4:0] sa,
	input wire[63:0] hilo,
	input wire all_flushM,

	output wire[63:0] y,
	output wire div_stall,
	output wire mul_stall,
	output wire overflow,
	input i_stall
    );
	reg carry;
	assign overflow = (alucontrol == `ALU_ADD || alucontrol == `ALU_SUB) & (carry ^ y_not_mul_div[31]);
	reg [31:0] y_not_mul_div;

	wire div_sign, div_valid, mul_sign, mul_valid;
	assign div_sign = (alucontrol == `ALU_SIGNED_DIV);
	assign div_valid = (alucontrol == `ALU_SIGNED_DIV || alucontrol == `ALU_UNSIGNED_DIV);
	assign mul_sign = (alucontrol == `ALU_SIGNED_MULT);
    assign mul_valid = (alucontrol == `ALU_SIGNED_MULT) | (alucontrol == `ALU_UNSIGNED_MULT);

	wire [63:0] y_div, y_mul;

	assign y = ({64{div_valid}} & y_div)
                    | ({64{mul_valid}} & y_mul)
                    | ({64{~mul_valid & ~div_valid}} & {32'b0, y_not_mul_div})
                    | ({64{(alucontrol == `ALU_MTHI)}} & {a, hilo[31:0]})
                    | ({64{(alucontrol == `ALU_MTLO)}} & {hilo[63:32], a});
	// mul mul(a,b,mul_sign,y_mul);
	wire div_ready;
	reg div_start;

	always @(*) begin
		if(alucontrol == `ALU_SIGNED_DIV || alucontrol == `ALU_UNSIGNED_DIV) begin
			if(div_ready == 1'b0) begin
				div_start = 1'b1;
			end
			else if(i_stall) begin
				div_start = 1'b1;
			end
			else if(div_ready == 1'b1) begin
				div_start = 1'b0;
			end
			else begin
				div_start = 1'b0;
			end
		end
		else begin
			div_start = 1'b0;
		end
	end


	wire mul_ready;
	reg mul_start;

	always @(*) begin
		if(alucontrol == `ALU_SIGNED_MULT || alucontrol == `ALU_UNSIGNED_MULT) begin
			if(mul_ready == 1'b0) begin
				mul_start = 1'b1;
			end
			else if(mul_ready == 1'b1) begin
				mul_start = 1'b0;
			end
			else begin
				mul_start = 1'b0;
			end
		end
		else begin
			mul_start = 1'b0;
		end
	end

	wire tempDiv_stall;
	assign div_stall = tempDiv_stall & ~all_flushM;  // flush时 F阶段不能stall
	div div(clk,rst,div_sign,a,b,div_start,1'b0,y_div,div_ready,tempDiv_stall);

	wire tempMul_stall;
	assign mul_stall = tempMul_stall & ~all_flushM;
	mul2 mul2(clk,rst,mul_sign,a,b,mul_start,y_mul,mul_ready,tempMul_stall);

	always @(*) begin
		carry = 0;
		case (alucontrol)
			
			// arth
			`ALU_ADD: {carry, y_not_mul_div} = {a[31], a} + {b[31], b};
			`ALU_SUB: {carry, y_not_mul_div} = {a[31], a} - {b[31], b};
			`ALU_SUBU: y_not_mul_div = a - b;
			`ALU_ADDU: y_not_mul_div = a + b;

			`ALU_SLT: y_not_mul_div = $signed(a) < $signed(b);
			`ALU_SLTU: y_not_mul_div = a < b;

			// Logic
			`ALU_AND: y_not_mul_div = a & b;
			`ALU_OR: y_not_mul_div = a | b;
			`ALU_XOR: y_not_mul_div = a ^ b;
			`ALU_NOR: y_not_mul_div = ~(a | b);
			`ALU_LUI: y_not_mul_div = {b[15:0], 16'b0};

			// Shift
			`ALU_SLL_SA: y_not_mul_div = b << sa;
			`ALU_SRL_SA: y_not_mul_div = b >> sa;
			`ALU_SRA_SA: y_not_mul_div = $signed(b) >>> sa;
			`ALU_SLL: y_not_mul_div = b << a[4:0];
			`ALU_SRL: y_not_mul_div = b >> a[4:0];
			`ALU_SRA: y_not_mul_div = $signed(b) >>> a[4:0];
			
			`ALU_RELU: begin
				if(a[31] == 1'b0) begin
					y_not_mul_div = a;
				end else begin
					y_not_mul_div = 32'b0;
				end
			end
			default: y_not_mul_div = 32'b0;
		endcase	
	end
	
	assign zero = (y == 64'b0);

endmodule
