`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/25 20:09:53
// Design Name: 
// Module Name: ALU_sim
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


module ALU_sim();
    reg [7:0] num1;
    reg [2:0] op;
    wire [31:0] res;

    ALU hello(.num1(num1),.op(op),.res(res));

    initial begin
        op = 3'b000;
        num1 = 8'd2;

        #100
        op = 3'b001;
        num1 = 8'd255;

        #100
        op = 3'b010;
        num1 = 8'd254;

        #100
        op = 3'b011;
        num1 = 8'd170;

        #100
        op = 3'b100;
        num1 = 8'd240;

        #100
        op = 3'b101;
        num1 = 8'd129;
    end
endmodule
