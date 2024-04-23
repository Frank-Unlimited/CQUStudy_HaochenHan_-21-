`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 10:10:45
// Design Name: 
// Module Name: pc_sim
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


module pc_sim();
reg clk,rst;
wire [31:0] pc;
wire en;
always #10 clk=~clk;
initial begin
    clk=0;
    rst=0;
    #1000
    rst=1;
    #50
    rst=0;
end
PC pc1(.clk(clk),.rst(rst),.pc(pc),.inst_ce(en));
endmodule
