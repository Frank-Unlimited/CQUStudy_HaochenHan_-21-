`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/25 15:59:15
// Design Name: 
// Module Name: ALU
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


module ALU(
    input wire [31:0] num1,
    input wire [31:0] num2,
    input wire [2:0] op,    //操作码
    output reg [31:0] res,   //计算结果
    output wire overflow,
    output wire Zero,

    // 用于仿真
    output wire [31:0] num2_sim, num1_sim
    );
    assign num2_sim = num2, num1_sim = num1;
    
    always @(*) begin
        case(op)
            3'b000: res = num1 & num2;
            3'b001: res = num1 | num2;
            3'b111: res = num1 < num2? 32'h0000_0001:32'b0000_0000;
            3'b010: res = num1 + num2;
            3'b110: res = num1 - num2;
            default: res = 32'b0;
        endcase
    end

    assign Zero = (res == 32'b0);
    assign overflow = 1'b0;
endmodule
