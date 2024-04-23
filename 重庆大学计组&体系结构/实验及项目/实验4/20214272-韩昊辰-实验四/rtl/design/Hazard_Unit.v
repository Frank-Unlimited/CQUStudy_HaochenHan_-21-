`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/20 13:50:54
// Design Name: 
// Module Name: Hazard_Unit
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


module Hazard_Unit(
    input wire [4:0] rsD, rtD, rsE, rtE, WriteRegM, WriteRegW, WriteRegE, 
    input wire RegWriteM, RegWriteW, MemtoRegE, 
    input wire BranchD, RegWriteE, MemtoRegM,
    output wire [1:0] ForwardAE, ForwardBE, 
    output wire StallF, StallD, FlushE, ForwardAD, ForwardBD

    );

// 判断当前输入ALU的地址是否和其他指令（正在M，W阶段）在此时执行的阶段要写入寄存器堆的地址相同。
// 如果相同，就需要将其他指令结果直接通过多路选择器输入到ALU中
assign ForwardAE = ((rsE != 5'b0) & (rsE == WriteRegM) & RegWriteM)? 2'b10 : 
                    ((rsE != 0) & (rsE == WriteRegW) & RegWriteW)? 2'b01 : 2'b00;


assign ForwardBE = ((rtE != 0) & (rtE == WriteRegM) & RegWriteM)? 2'b10 : 
                    ((rtE != 0) & (rtE == WriteRegW) & RegWriteW)? 2'b01 : 2'b00;

// lw流水线暂停
wire lwstall;
assign lwstall = (((rsD == rtE) | (rtD == rtE)) & MemtoRegE);

// 解决控制冒险
assign ForwardAD = ((rsD != 0) & (rsD == WriteRegM) & RegWriteM);
assign ForwardBD = ((rtD != 0) & (rtD == WriteRegM) & RegWriteM);

wire branchstall;
assign branchstall = (BranchD & RegWriteE & (WriteRegE == rsD | WriteRegE == rtD)) |
                        (BranchD & MemtoRegM & (WriteRegM == rsD | WriteRegM == rtD));

assign StallF = lwstall | branchstall;
assign StallD = lwstall | branchstall;
assign FlushE = lwstall | branchstall;
endmodule
