`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 11:03:05
// Design Name: 
// Module Name: clk_div
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


module clk_div #(parameter N = 8)( //N-分频系数
    input clk_in,
    output reg clk_out=0
    );
    reg [31:0] cnt=0;
    always@(posedge clk_in) begin
        if(cnt==(N/2-1)) begin
            cnt <= 0;
            clk_out = ~clk_out;
        end
        else begin
            cnt <= cnt+1;
        end
    end
endmodule
