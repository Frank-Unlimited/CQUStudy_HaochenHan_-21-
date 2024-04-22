`include "opcodedefines.vh"

module memwen(
    input wire[5:0] op,
    input wire[31:0] aluoutM,
    output reg[3:0] mem_wenM,
    output reg AdESM
);
    always @(*) begin
        AdESM = 1'b0;
        case(op)
            `OP_SW: 
                case(aluoutM[1:0])
                    2'b00: mem_wenM = 4'b1111;
                    default: begin mem_wenM = 4'b0000; AdESM = 1'b1; end
                endcase
            `OP_SH: 
                case(aluoutM[1:0])
                    2'b00: mem_wenM = 4'b0011;
                    2'b10: mem_wenM = 4'b1100;
                    default: begin mem_wenM = 4'b0000; AdESM = 1'b1; end // exception
                endcase
            `OP_SB:
                case(aluoutM[1:0])
                    2'b00: mem_wenM = 4'b0001;
                    2'b01: mem_wenM = 4'b0010;
                    2'b10: mem_wenM = 4'b0100;
                    2'b11: mem_wenM = 4'b1000;
                endcase
            default: mem_wenM = 4'b0000;
        endcase 
    end


endmodule