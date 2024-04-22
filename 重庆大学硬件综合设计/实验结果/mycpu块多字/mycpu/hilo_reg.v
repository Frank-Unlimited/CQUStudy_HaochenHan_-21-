`timescale 1ns / 1ps

module hilo_reg(
                input wire        clk,rst,we,

                input wire [63:0] hilo_i,
                output wire [31:0] hi_o,lo_o,
                output reg [63:0] hilo
                );
   always @(posedge clk) begin
      if(rst)
         hilo <= 0;
      else if(we)
         hilo <= hilo_i;
      else
         hilo <= hilo;
   end

   assign hi_o = hilo[63:32];
   assign lo_o = hilo[31:0];
endmodule

