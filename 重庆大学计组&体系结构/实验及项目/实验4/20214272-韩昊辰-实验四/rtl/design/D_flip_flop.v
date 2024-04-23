`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/18 13:27:07
// Design Name: 
// Module Name: D_flip_flop
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


module D_flip_flop(
    input wire clk, rst, en,
    input wire [31:0] din,
    output reg [31:0] q
    );

    always@(posedge clk) begin
        if(rst)
            q <= 32'b0;
        else if(en)
            q <= din;
    end
endmodule
