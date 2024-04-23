`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 15:18:51
// Design Name: 
// Module Name: controller
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


module controller(
    input wire clka, rst,
    input wire [5:0] op,
    input wire [5:0] funct,
    output wire [2:0] ALUControl, 
    output wire RegDst, Branch, MemtoReg, MemWrite, ALUSrc, RegWrite, Jump,
    output wire RegWriteM, RegWriteW, MemtoRegE, RegWriteE, MemtoRegM,

    //
    output wire ALUSrcD_sim,
    output wire [5:0] sigsD_sim, sigsE_sim
    );

assign ALUSrcD_sim = ALUSrcD, sigsD_sim = sigsD, sigsE_sim = sigsE;
//
wire [1:0] ALUOp;
// D
wire RegDstD, BranchD, MemtoRegD, MemWriteD, ALUSrcD, RegWriteD;
wire [2:0] ALUControlD;
main_decoder M_dec(
    .op(op),
    .ALUOp(ALUOp),
    .RegDst(RegDstD),
    .Branch(BranchD),
    .MemtoReg(MemtoRegD), 
    .MemWrite(MemWriteD), 
    .ALUSrc(ALUSrcD), 
    .RegWrite(RegWriteD),
    .Jump(Jump)
    );
alu_decoder A_dec(
    .ALUOp(ALUOp),
    .funct(funct),
    .ALUControl(ALUControlD)
);
assign Branch = BranchD;

// D-E
wire [5:0] sigsD, sigsE;    // RegDstD, BranchD, MemtoRegD, MemWriteD, ALUSrcD, RegWriteD;
wire [2:0] ALUControlE;
assign sigsD = {RegDstD, BranchD, MemtoRegD,MemWriteD, ALUSrcD, RegWriteD};
floprc  #(6) rDE1(.clk(clka), .rst(rst), .clear(0), .en(1),  .din(sigsD), .q(sigsE));
floprc  #(3) rDE2(.clk(clka), .rst(rst), .clear(0), .en(1),  .din(ALUControlD), .q(ALUControlE));

// E
assign ALUControl = ALUControlE;
assign MemtoRegE = sigsE[3];
assign ALUSrc = sigsE[1];
assign RegDst = sigsE[5];
assign RegWriteE = sigsE[0];
// E-M
wire [3:0] sigsM;   // {BranchD, MemtoRegD, MemWriteD, RegWriteD;}
floprc  #(4) rEM1(.clk(clka), .rst(rst), .clear(0), .en(1),  .din({sigsE[4:2],sigsE[0]}), .q(sigsM));

// M
assign MemWrite = sigsM[1];
assign RegWriteM = sigsM[0];
assign MemtoRegM = sigsM[2];
// M-W
wire [1:0] sigsW;  // {MemtoRegD,RegWriteD}
floprc  #(2) rMW1(.clk(clka), .rst(rst), .clear(0), .en(1),  .din({sigsM[2],sigsM[0]}), .q(sigsW));

// W
assign RegWrite = sigsW[0];
assign RegWriteW = sigsW[0];
assign MemtoReg = sigsW[1];

endmodule
