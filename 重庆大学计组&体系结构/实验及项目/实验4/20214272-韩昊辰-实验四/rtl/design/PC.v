`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 09:59:48
// Design Name: 
// Module Name: PC
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


module PC(
    input wire clk,rst,
    output reg [31:0] pc=0,
    output wire inst_ce
    );
    wire [31:0] tmp; //暂存加4后的pc

    assign inst_ce = (pc<1024)? 1:0;

    Add_4 pc_adder(
        .a(pc),
        .res(tmp)
    );

    always@(posedge clk) begin  
        if(rst) begin
            pc <= 0;
        end
        else begin
            pc <= tmp;
        end
    end
endmodule
