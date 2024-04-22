`include "opcodedefines.vh"

module memreaddata(
    input wire[5:0] op,
    input wire[31:0] aluoutM,
    input wire[31:0] readdataM,
    output reg[31:0] readdata2M
);
    reg [7:0] membyte;
    reg [15:0] memhalf;

    wire [31:0] membyte_sign_ext;
    wire [31:0] memhalf_sign_ext;

    wire [31:0] membyte_unsign_ext;
    wire [31:0] memhalf_unsign_ext;

    always @(*) begin
        membyte = 0;
        memhalf = 0;
        readdata2M = 0;
        case(op)
            `OP_LW: begin
                readdata2M = readdataM;
                 // when aluoutM[1:0] != 00 then exception
            end
            `OP_LH: begin
                case(aluoutM[1:0])
                    2'b00: memhalf = readdataM[15:0];
                    2'b10: memhalf = readdataM[31:16];
                    default: memhalf = 0; // exception
                endcase 
                readdata2M = memhalf_sign_ext;
            end
            `OP_LHU: begin
                case(aluoutM[1:0])
                    2'b00: memhalf = readdataM[15:0];
                    2'b10: memhalf = readdataM[31:16];
                    default: memhalf = 0; // exception
                endcase 
                readdata2M = memhalf_unsign_ext;
            end
            `OP_LB: begin
                case(aluoutM[1:0])
                    2'b00: membyte = readdataM[7:0];
                    2'b01: membyte = readdataM[15:8];
                    2'b10: membyte = readdataM[23:16];
                    2'b11: membyte = readdataM[31:24];
                endcase
                readdata2M = membyte_sign_ext;
            end
            `OP_LBU: begin
                case(aluoutM[1:0])
                    2'b00: membyte = readdataM[7:0];
                    2'b01: membyte = readdataM[15:8];
                    2'b10: membyte = readdataM[23:16];
                    2'b11: membyte = readdataM[31:24];
                endcase
                readdata2M = membyte_unsign_ext;
            end
            default: readdata2M = readdataM;
        endcase 
    end

    assign membyte_sign_ext = {{24{membyte[7]}},membyte};
    assign membyte_unsign_ext = {24'b0,membyte};
    assign memhalf_sign_ext = {{16{memhalf[15]}},memhalf};
    assign memhalf_unsign_ext = {16'b0,memhalf};


endmodule