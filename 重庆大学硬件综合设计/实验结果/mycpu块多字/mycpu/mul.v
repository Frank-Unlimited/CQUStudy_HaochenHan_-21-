module mul(
    input [31:0]        a,
    input [31:0]        b,
    input               sign,
    output [63:0]       result
);

wire sign_a = a[31];
wire sign_b = b[31];

wire [31:0] abs_a = sign_a ? ((~a)+1) : a;
wire [31:0] abs_b = sign_b ? ((~b)+1) : b;

wire [63:0] abs_result = abs_a * abs_b;

wire [63:0] signed_result = (sign_a ^ sign_b) ? ((~abs_result)+1) : abs_result;

assign result = sign ? signed_result : a * b;

endmodule
