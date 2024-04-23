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
    input wire [7:0] num1,  //操作数1
    input wire [2:0] op,    //操作码
    output reg [31:0] res   //计算结果
    );
    reg [31:0] num2;    //操作数2，恒为32'h01
    reg [31:0] num1_sign_extend;
    
    always @(op) begin
        num2 = 32'h0000_0001;
        num1_sign_extend = {24'h000000,num1[7:0]};
        case(op)
            3'b000: res = num1_sign_extend + num2;
            3'b001: res = num1_sign_extend - num2;
            3'b010: res = num1_sign_extend & num2;
            3'b011: res = num1_sign_extend | num2;
            3'b100: res = ~num1_sign_extend;
            3'b101: res = num1_sign_extend<num2? 32'h0000_0001:32'b0000_0000;
            default: res = 32'hxxxx_xxxx;    //高阻态
        endcase
    end
endmodule
