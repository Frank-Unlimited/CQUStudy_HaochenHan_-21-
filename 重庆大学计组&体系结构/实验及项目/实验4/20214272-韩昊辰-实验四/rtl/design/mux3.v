`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/20 13:46:00
// Design Name: 
// Module Name: mux3
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


module mux3 #(parameter WIDTH = 8)(
    input wire [WIDTH-1:0] a,b,c,
    input wire [1:0] s,
    output wire [WIDTH-1:0] y
    );

    assign y = (s == 2'b00)? a:
                (s == 2'b01)? b:
                (s == 2'b10)? c : a;
endmodule
