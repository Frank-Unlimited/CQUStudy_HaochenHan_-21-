`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 11:28:41
// Design Name: 
// Module Name: alu_decoder
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


module alu_decoder(
    input wire [5:0] funct,
    input wire [1:0] ALUOp,
    output reg [2:0] ALUControl
    );
    always@(*) begin
        case(ALUOp)
            2'b00: begin  //lw
                ALUControl = 3'b010;
            end
            2'b01: begin  //beq
                ALUControl = 3'b110;
            end 
            2'b10: begin  //R-type
                case(funct)
                    6'b100000: begin    //Add
                        ALUControl = 3'b010;
                    end
                    6'b100010: begin    //subtract
                        ALUControl = 3'b110;
                    end
                    6'b100100: begin    //and
                        ALUControl = 3'b000;
                    end
                    6'b100101: begin    //or
                        ALUControl = 3'b001;
                    end
                    6'b101010: begin    //slt
                        ALUControl = 3'b111;
                    end
                    default: begin  //beq
                        ALUControl = 3'b000;
                    end 
                endcase
            end
            default: begin 
                ALUControl = 3'b000;
            end 
        endcase
    end
endmodule
