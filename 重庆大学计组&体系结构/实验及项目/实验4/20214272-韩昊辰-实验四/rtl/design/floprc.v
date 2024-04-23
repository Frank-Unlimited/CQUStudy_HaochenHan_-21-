`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/19 15:01:02
// Design Name: 
// Module Name: floprc
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


module floprc #(parameter WIDTH = 8) (
    input wire clk, rst, clear, en,
    input wire [WIDTH-1:0] din,
    output reg [WIDTH-1:0] q
    );

    always @(posedge clk) begin
        if (rst) begin
            q <= 0;
        end
        else if (clear) begin
            q <= 0;
        end
        else if (en) begin
            q <= din;
        end
    end
endmodule
