`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/25 17:52:23
// Design Name: 
// Module Name: adder_sim
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


module adder_sim();
    reg clk,rst; 
    reg [3:0] stop,refresh;     
    reg validin;
    reg [31:0] cin_a,cin_b;       
    reg c_in;
    reg out_allow;     
    wire validout;
    wire [31:0] sum_out;
    wire c_out;  

    pipeline_adder_4steps adder(.clk(clk),.rst(rst),.stop(stop),.refresh(refresh),
                .validin(validin), .cin_a(cin_a),.cin_b(cin_b),.c_in(c_in),
                .out_allow(out_allow),.validout(validout),.sum_out(sum_out),.c_out(c_out));
    
    always #20 clk=~clk;    //40单位 = 1T
    initial begin
        clk=1;
        rst=1;
        stop=0;
        refresh=0;
        validin=1;
        cin_a=0;
        cin_b=0;
        c_in=0;
        out_allow=1;
        #10 rst=0;
        #400 stop = 4'b0010;//第10周期，暂停第二级，持续两个周期
        #80 stop = 4'b0000;
        #320 refresh = 4'b0100; //第20周期刷新流水线第三级
        #40 refresh = 4'b0000;
    end

    always #40 cin_a = {$random} % 100000; 
    always #40 cin_b = {$random} % 100000;
endmodule
