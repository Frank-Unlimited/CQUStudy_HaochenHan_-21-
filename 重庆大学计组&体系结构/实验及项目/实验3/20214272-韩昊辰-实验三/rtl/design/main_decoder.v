`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 11:17:19
// Design Name: 
// Module Name: main_decoder
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


module main_decoder(
    input wire [5:0] op,    //opcode[31:26]
    output reg [1:0] ALUOp, 
    output reg RegDst, Branch, Jump, MemtoReg, MemWrite, ALUSrc, RegWrite
    );
    always@(*) begin
        case(op)
            6'b000000: begin    //R-type
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b1100000;
                ALUOp = 2'b10;
            end
            6'b100011: begin //lw
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b1010010;
                ALUOp = 2'b00;
            end
            6'b101011: begin //sw
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b0010100;
                ALUOp = 2'b00;
            end
            6'b000100: begin //beq
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b0001000;
                ALUOp = 2'b01;
            end
            6'b001000: begin //addi
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b1010000;
                ALUOp = 2'b00;
            end
            6'b000010: begin //j
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b00000001;
                ALUOp = 2'b00;
            end
            default: begin 
                {RegWrite,RegDst,ALUSrc,Branch,MemWrite,MemtoReg,Jump} = 7'b00000000;
                ALUOp = 2'b00;
            end
        endcase
    end

endmodule
