`include "opcodedefines.vh"

module memwritedata(
    input wire[5:0] op,
    input wire[31:0] aluoutM, tempWritedataM,
    output reg[31:0] writedataM
);
    always @(*) begin
        case(op)
            `OP_SW: writedataM = tempWritedataM; // when aluoutM[1:0] != 00 then exception
            `OP_SH: 
                case(aluoutM[1:0])
                    2'b00: writedataM = {16'b0,tempWritedataM[15:0]};
                    2'b10: writedataM = {tempWritedataM[15:0],16'b0};
                    default: writedataM = tempWritedataM;
                endcase
            `OP_SB:
                case(aluoutM[1:0])
                    2'b00: writedataM = {24'b0,tempWritedataM[7:0]};
                    2'b01: writedataM = {16'b0,tempWritedataM[7:0],8'b0};
                    2'b10: writedataM = {8'b0,tempWritedataM[7:0],16'b0};
                    2'b11: writedataM = {tempWritedataM[7:0],24'b0};
                endcase
            default: writedataM = tempWritedataM;
        endcase 
    end


endmodule