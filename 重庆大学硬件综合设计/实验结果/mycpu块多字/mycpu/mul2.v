// module mul2(
//     input	wire clk,
// 	input wire rst,
	
// 	input wire                    signed_mul_i,
// 	input wire[31:0]              opdata1_i,
// 	input wire[31:0]		      opdata2_i,
// 	input wire                    start_i,
	
// 	output reg[63:0]              result_o,
// 	output reg			          ready_o,
// 	output reg                    mul_stall
// );
//     wire sign_a = opdata1_i[31];
//     wire sign_b = opdata2_i[31];

//     wire [31:0] abs_a = (sign_a && signed_mul_i) ? ((~opdata1_i)+1) : opdata1_i;
//     wire [31:0] abs_b = (sign_b && signed_mul_i) ? ((~opdata2_i)+1) : opdata2_i;
//     wire[15:0] a1,a2,b1,b2;
//     assign a1 = abs_a[31:16];
//     assign a2 = abs_a[15:0];
//     assign b1 = abs_b[31:16];
//     assign b2 = abs_b[15:0];
//     reg[1:0] state;
//     wire[63:0] g11,g12,g21,g22;
//     reg[31:0] t11,t12,t21,t22;
//     wire[63:0] tempresult;
//     assign tempresult = g11 + g12 + g21 + g22;
//     assign g11 = {t11, 32'b0};
//     assign g12 = {16'b0, t12, 16'b0};
//     assign g21 = {16'b0, t21, 16'b0};
//     assign g22 = {32'b0, t22};
//     always @(posedge clk) begin
//         if(rst) begin
//             state <= 0;
//             ready_o <= 0;
//             result_o <= 0;
//             mul_stall <= 0;
//             t11 <= 0;
//             t12 <= 0;
//             t21 <= 0;
//             t22 <= 0;
//         end else begin
//             case(state)
//                 2'b00: begin
//                     if(start_i == 1) begin
//                         state <= 1;
//                         mul_stall <= 1;
//                         result_o <= 0;
//                         ready_o <= 0;
//                         t11 <= a1 * b1;
//                         t12 <= a1 * b2;
//                         t21 <= a2 * b1;
//                         t22 <= a2 * b2;
//                     end
//                 end
//                 2'b01: begin
//                     state <= 2;
//                     mul_stall <= 0;
//                     ready_o <= 1;
//                     result_o <= (signed_mul_i && (sign_a ^ sign_b)) ? (~(tempresult) + 1) : tempresult;
//                 end
//                 2'b10: begin
//                     state <= 0;
//                     ready_o <= 0;
//                     mul_stall <= 0;
//                 end
//             endcase
//         end
//     end

// endmodule
module mul2(
    input	wire clk,
	input wire rst,
	
	input wire                    signed_mul_i,
	input wire[31:0]              opdata1_i,
	input wire[31:0]		      opdata2_i,
	input wire                    start_i,
	
	output reg[63:0]              result_o,
	output reg			          ready_o,
	output reg                    mul_stall
);
    wire sign_a = opdata1_i[31];
    wire sign_b = opdata2_i[31];

    wire [31:0] abs_a = (sign_a && signed_mul_i) ? ((~opdata1_i)+1) : opdata1_i;
    wire [31:0] abs_b = (sign_b && signed_mul_i) ? ((~opdata2_i)+1) : opdata2_i;
    wire[15:0] a1,a2,b1,b2;
    assign a1 = abs_a[31:16];
    assign a2 = abs_a[15:0];
    assign b1 = abs_b[31:16];
    assign b2 = abs_b[15:0];
    reg[1:0] state;
    wire[63:0] g11,g12,g21,g22;
    reg[31:0] t11,t12,t21,t22;
    wire[63:0] tempresult_1;
    reg[31:0] t11_1,t12_1,t21_1,t22_1;
    assign tempresult_1 = g11 + g12 + g21 + g22;
    assign g11 = {t11, 32'b0};
    assign g12 = {16'b0, t12, 16'b0};
    assign g21 = {16'b0, t21, 16'b0};
    assign g22 = {32'b0, t22};
    always @(posedge clk) begin
        if(rst) begin
            state <= 0;
            ready_o <= 0;
            result_o <= 0;
            mul_stall <= 0;
            t11_1 <= 0;
            t12_1 <= 0;
            t21_1 <= 0;
            t22_1 <= 0;
            t11 <= 0;
            t12 <= 0;
            t21 <= 0;
            t22 <= 0;
        end else begin
            case(state)
                2'b00: begin
                    if(start_i == 1) begin
                        state <= 1;
                        mul_stall <= 1;
                        result_o <= 0;
                        ready_o <= 0;
                        t11_1 <= a1 * b1;
                        t12_1 <= a1 * b2;
                        t21_1 <= a2 * b1;
                        t22_1 <= a2 * b2;
                    end
                end
                2'b01: begin
                    state <= 2;
                    mul_stall <= 1;
                    ready_o <= 0;
                    t11 <= t11_1;
                    t12 <= t12_1;
                    t21 <= t21_1;
                    t22 <= t22_1;
                end
                2'b10: begin
                    state <= 3;
                    mul_stall <= 0;
                    ready_o <= 1;
                    result_o <= (signed_mul_i && (sign_a ^ sign_b)) ? (~(tempresult_1) + 1) : tempresult_1;
                end
                2'b11: begin
                    state <= 0;
                    ready_o <= 0;
                    mul_stall <= 0;
                end
            endcase
        end
    end

endmodule