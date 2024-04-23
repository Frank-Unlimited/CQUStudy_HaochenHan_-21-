`timescale 0.1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 17:26:17
// Design Name: 
// Module Name: sim
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


module sim();
reg clk,rst;
wire [2:0] ALUControl;
wire RegDst, Branch, MemtoReg, MemWrite, ALUSrc, RegWrite, Jump;
wire [6:0] seg;
wire [7:0] ans;
wire [31:0] instr_address;
wire inst_ce;
wire [31:0] opcode;

wire mclk;
clk_div g(clk,mclk);

PC g1(.clk(mclk),.rst(rst),.pc(instr_address),.inst_ce(inst_ce));

Inst_Rom g2(instr_address>>2,mclk,opcode);

IF_ID_Top g3(clk,rst,ALUControl,RegDst, Branch, MemtoReg, MemWrite, ALUSrc, RegWrite, Jump,seg,ans);

always #50 begin 
        clk = ~clk;  //100Mhz
    end

always@(opcode) $display("instruction:%h,ALUControl:%b,memtoreg:%b,memwrite:%b,alusrc:%b,regdst:%b,regwrite:%b,branch:%b,jump:%b",opcode,ALUControl, MemtoReg, MemWrite, ALUSrc, RegDst, RegWrite, Branch, Jump);
initial begin
    clk=0;
    rst=1;
    #100
    rst=0;
    #4000
    rst=1;
    #2000
    rst=0;
end

endmodule
