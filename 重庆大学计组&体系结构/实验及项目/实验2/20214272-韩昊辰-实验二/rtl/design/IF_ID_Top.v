`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/06 15:38:16
// Design Name: 
// Module Name: IF_ID_Top
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


module IF_ID_Top(   //取指，译码的顶层模块
    input wire clk,rst,
    output wire [2:0] ALUControl, // ALU控制信号
    output wire RegDst, Branch, MemtoReg, MemWrite, ALUSrc, RegWrite, Jump, //各个模块控制信号
    output wire [6:0] seg,   //七段数码管显示信号
    output wire [7:0] ans //数码管工作标识
    );
    
    //时钟分频模块
    wire mclk;  //1hz
    clk_div  mc_d (.clk_in(clk),.clk_out(mclk));

    //PC
    wire [31:0] inst_address;   //指令地址
    wire ena;   //指令存储器使能
    PC mpc(.clk(mclk),.rst(rst),.pc(inst_address),.inst_ce(ena));

    //Inst_Rom
    wire [31:0] opcode;
    Inst_Rom mI_Rom(.address(inst_address>>2),.clk(mclk),.opcode(opcode));

    //Controller
    controller mcontroller(
        .op(opcode[31:26]),
        .funct(opcode[5:0]),
        .RegDst(RegDst),
        .Branch(Branch),
        .MemRead(MemRead), 
        .MemtoReg(MemtoReg), 
        .MemWrite(MemWrite), 
        .ALUSrc(ALUSrc), 
        .RegWrite(RegWrite),
        .Jump(Jump),
        .ALUControl(ALUControl)
    );

    //show
    display show(.clk(clk),.reset(rst),.s(opcode),.seg(seg),.ans(ans));
endmodule
