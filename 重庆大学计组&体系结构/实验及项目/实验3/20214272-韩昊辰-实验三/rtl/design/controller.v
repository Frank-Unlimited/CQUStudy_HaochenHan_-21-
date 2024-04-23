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
    input wire [5:0] op,
    input wire [5:0] funct,
    output wire [2:0] ALUControl, 
    output wire RegDst, Branch, MemtoReg, MemWrite, ALUSrc, RegWrite, Jump
    );
    wire [1:0] ALUOp;
    main_decoder M_dec(
        .op(op),
        .ALUOp(ALUOp),
        .RegDst(RegDst),
        .Branch(Branch),
        .MemtoReg(MemtoReg), 
        .MemWrite(MemWrite), 
        .ALUSrc(ALUSrc), 
        .RegWrite(RegWrite),
        .Jump(Jump)
        );
    alu_decoder A_dec(
        .ALUOp(ALUOp),
        .funct(funct),
        .ALUControl(ALUControl)
    );
endmodule
