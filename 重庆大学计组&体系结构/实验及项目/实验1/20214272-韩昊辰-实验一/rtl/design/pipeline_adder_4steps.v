`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/25 17:23:32
// Design Name: 
// Module Name: pipeline_adder_4steps
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


module pipeline_adder_4steps(
    input clk,
    input rst,  //复位
    input [3:0] stop,   //暂停标志，0000不暂停,0001暂停第一级，0010暂停第二级，0100暂停第三级，1000暂停第四级
    input [3:0] refresh,    //刷新标志，0000不刷新,0001刷新第一级，0010刷新第二级，0100刷新第三级，1000刷新第四级
    input validin,  //输入是否有效
    input [31:0] cin_a,
    input [31:0] cin_b,
    input c_in,
    input out_allow,    //是否允许输出
    output validout, //输出是否有效
    output [31:0] sum_out,
    output c_out
    );

    //流水线之间的临时寄存器，存储未计算的a,b值
    reg [23:0] temp_a_t1,temp_b_t1; //第一、二级间
    reg [15:0] temp_a_t2,temp_b_t2; //第二、三级间
    reg [7:0]  temp_a_t3,temp_b_t3; //第三、四级间

    //各级流水线输出寄存器
    reg [7:0]  sum_out_t1; //第一级 
    reg [15:0] sum_out_t2; //第二级
    reg [23:0] sum_out_t3; //第三级
    reg [31:0] sum_out_t4; //第四级

    //各级流水线进位输出寄存器
    reg c_out_t1, c_out_t2, c_out_t3, c_out_t4;

    //第一级
    reg pipe1_valid;    //当前流水线数据是否有效
    wire pipe1_allowin;       //【可接收数据】标志
    wire pipe1_ready_go;      //【可刷新下一级】
    wire pipe1_to_pipe2_valid;//【可进入下一级】标志(用于传递数据有效性)
    
    assign pipe1_ready_go = !stop[0];  //如果没有暂停，那么就可以Go
    // 如果pipe1中的值已经无效，或者这一轮一定会传给下一个，那么就可以进行接收
    assign pipe1_allowin = !pipe1_valid || pipe1_ready_go && pipe2_allowin;
    // 如果pipe1有效，并且pipe1可以进行传输，那么pipe1_to_pipe2_valid可以进行。
    assign pipe1_to_pipe2_valid = pipe1_valid && pipe1_ready_go;

    always@(posedge clk) begin
        //赋值pipe1_valid
        if(rst) pipe1_valid <= 1'b0;    //如果需要复位,那么pipe1_valid变为0,表示pipe1中的值不再有效
        else if(refresh[0]) pipe1_valid <= 1'b0;    //如果需要刷新,那么pipe1_valid变为0,表示pipe1中的值不再有效
        else if(pipe1_allowin)  pipe1_valid <= validin; //既不复位也不刷新,当pipe1允许输入,若当前输入有效，pipe1_valid为1，值有效，否则0，值无效

        //如果输入有效，pipe1能接收，则进行低位运算并保留高位
        if(validin && pipe1_allowin) begin
            {c_out_t1,sum_out_t1} <= cin_a[7:0] + cin_b[7:0] + c_in;
            temp_a_t1 <= cin_a[31:8];
            temp_b_t1 <= cin_b[31:8];
        end
    end

    //第二级
    reg pipe2_valid;
    wire pipe2_allowin;       
    wire pipe2_ready_go;      
    wire pipe2_to_pipe3_valid;
 
    assign pipe2_ready_go = !stop[1]; 
    assign pipe2_allowin = !pipe2_valid || pipe2_ready_go && pipe3_allowin;
    assign pipe2_to_pipe3_valid = pipe2_valid && pipe2_ready_go;
    always@(posedge clk)begin
      if(rst)begin
         pipe2_valid <= 1'b0;
      end
      else if(refresh[1])begin
         pipe2_valid <= 1'b0;
      end
      else if(pipe2_allowin)begin
         pipe2_valid <=  pipe1_to_pipe2_valid;
      end
      if(pipe1_to_pipe2_valid && pipe2_allowin) begin
        {c_out_t2,sum_out_t2} <= {{1'b0,temp_a_t1[7:0]} + {1'b0,temp_b_t1[7:0]} + c_out_t1, sum_out_t1};
        temp_a_t2<=temp_a_t1[23:8];
        temp_b_t2<=temp_b_t1[23:8];
      end
    end
    
    
    //第三级
    reg pipe3_valid;
    wire pipe3_allowin;       
    wire pipe3_ready_go;      
    wire pipe3_to_pipe4_valid;
 
    assign pipe3_ready_go = !stop[2]; 
    assign pipe3_allowin = !pipe3_valid || pipe3_ready_go && pipe4_allowin;
    assign pipe3_to_pipe4_valid = pipe3_valid && pipe3_ready_go;
 
    always@(posedge clk)begin
      if(rst)begin
         pipe3_valid <= 1'b0;
      end
      else if(refresh[2])begin
         pipe3_valid <= 1'b0;
      end
      else if(pipe3_allowin)begin
         pipe3_valid <=  pipe2_to_pipe3_valid;
      end
      if(pipe2_to_pipe3_valid && pipe3_allowin)begin
        {c_out_t3,sum_out_t3} <= {{1'b0,temp_a_t2[7:0]} + {1'b0,temp_b_t2[7:0]} + c_out_t2, sum_out_t2};
        temp_a_t3<=temp_a_t2[15:8];
        temp_b_t3<=temp_b_t2[15:8];
       end
    end
    
    //第四级
    reg pipe4_valid;
    wire pipe4_allowin;       
    wire pipe4_ready_go;      
 
    assign pipe4_ready_go = !stop[3]; 
    assign pipe4_allowin = !pipe4_valid || pipe4_ready_go && out_allow;
 
    always@(posedge clk)begin
      if(rst)begin
         pipe4_valid <= 1'b0;
      end
      else if(refresh[3])begin
         pipe4_valid <= 1'b0;
      end
      else if(pipe4_allowin)begin
         pipe4_valid <=  pipe3_to_pipe4_valid;
      end
      if(pipe3_to_pipe4_valid && pipe4_allowin)begin
        {c_out_t4,sum_out_t4} <= {{1'b0,temp_a_t3[7:0]} + {1'b0,temp_b_t3[7:0]} + c_out_t3, sum_out_t3};
      end
    end

    //最终输出
    assign validout = pipe4_valid && pipe4_ready_go;
    assign sum_out = sum_out_t4;
    assign c_out = c_out_t4;
endmodule
