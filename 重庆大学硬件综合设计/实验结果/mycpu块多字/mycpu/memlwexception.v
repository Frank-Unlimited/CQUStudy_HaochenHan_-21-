`include "opcodedefines.vh"

module memlwexception(
    input wire[5:0] op,
    input wire[31:0] aluoutM,
    output reg AdELM
);
    always @(*) begin
        AdELM = 1'b0;
        case(op)
            `OP_LW: begin
                case(aluoutM[1:0])
                    2'b00: /* ok */;
                    default: AdELM = 1'b1;
                endcase 
            end
            `OP_LH: begin
                case(aluoutM[1:0])
                    2'b00: /* ok */;
                    2'b10: /* ok */;
                    default: AdELM = 1'b1;
                endcase
            end
            `OP_LHU: begin
                case(aluoutM[1:0])
                    2'b00: /* ok */;
                    2'b10: /* ok */;
                    default: AdELM = 1'b1;
                endcase
            end
            default: /* ok */;
        endcase 
    end

endmodule