`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/28 14:22:38
// Design Name: 
// Module Name: ALU_fun
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


module ALU_fun(
    input clk,
    input reset,
    input wire [7:0] ins,  //操作数1
    input wire [2:0] op,    //操作码
    output wire [6:0] seg,
    output wire [7:0] ans
    );
    //计算出结果
    wire [31:0] res;
    ALU culculate(.num1(ins),.op(op),.res(res));

    //输出
    display Dis(.clk(clk),.reset(reset),.s(res),.seg(seg),.ans(ans));

endmodule
