`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/07 10:58:03
// Design Name: 
// Module Name: mips
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


module mips(
	input wire clk,rst,
	input wire[5:0] ext_int,
	//instr
    output wire inst_req,
    output wire inst_wr,
    output wire [1:0] inst_size,
    output wire [31:0] inst_addr,
    output wire [31:0] inst_wdata,
    input wire inst_addr_ok,
    input wire inst_data_ok,
    input wire [31:0] inst_rdata,
	input [3:0] cnt,

    //data
    output wire data_req,
    output wire data_wr,
    output wire [1:0] data_size,
    output wire [31:0] data_addr,
    output wire [31:0] data_wdata,
    input wire data_addr_ok,
    input wire data_data_ok,
    input wire [31:0] data_rdata,

	//debug
    output [31:0] debug_wb_pc     ,
    output [3:0] debug_wb_rf_wen  ,
    output [4:0] debug_wb_rf_wnum ,
    output [31:0] debug_wb_rf_wdata
	
    );
	wire[31:0] pcF;
	wire[31:0] instrF;
	wire[3:0] mem_wenM;
	wire[31:0] aluoutM,writedataM;
	wire[31:0] readdataM;
	wire stallF;
	wire [5:0] opD,functD;
	wire [4:0] rtD;
	wire [31:0] instrD;
	wire riD;
	wire sign_extD,pcjrD,stallD,flushD;
	wire regdstE,alusrcE,pcsrcD,memtoregE,hilotoregE,cp0toregE,memtoregM,memtoregW,
			regwriteE,regwriteM,regwriteW;
	wire breakM,syscallM,eretM;
	wire hilotoregM;
	wire [4:0] alucontrolE;
	wire cp0_wenE;
	wire stallE,flushE,equalD;
	wire[1:0] mfhiloM;
	wire hilo_wenM;
	wire hilotoregW;
	wire stallM;
	wire flushM;
	wire memwriteM,memreadM;
	wire mem_enM;
	// write back stage
	wire pctoregW;
	wire stallW;
	wire flushW;
	wire cp0_wenM,cp0toregM;
	wire is_in_delayslot_iM;
	
	 //datapath传出来的sram信号
    wire inst_sram_en           ;
    wire [31:0] inst_sram_addr  ;
	assign inst_sram_addr = pcF;
    wire [31:0] inst_sram_rdata ;
	assign instrF = inst_sram_rdata;
    wire i_stall          ;

    wire data_sram_en           ;
	assign data_sram_en = mem_enM;
    wire [31:0] data_sram_addr  ;
	assign data_sram_addr = aluoutM;
    wire [31:0] data_sram_rdata ;
	assign readdataM = data_sram_rdata;
    wire [3:0] data_sram_wen    ;
	assign data_sram_wen = mem_wenM;
    wire [31:0] data_sram_wdata ;
	assign data_sram_wdata = writedataM;
    wire d_stall          ;

	wire all_stall;

	controller c(
		clk,rst,

		//decode stage
		opD,functD,rtD,instrD,stallD,flushD,riD,sign_extD,
		pcsrcD,branchD,equalD,jumpD,pcjrD,
		
		
		//execute stage
		flushE,stallE,
		memtoregE,alusrcE,
		regdstE,regwriteE,	
		alucontrolE,
		rd31srcE,
		hilotoregE,
		cp0toregE,
		cp0_wenE,

		//mem stage
		stallM,flushM,
		memtoregM,
		regwriteM,
		pctoregM,
		hilotoregM,
		memwriteM,memreadM,
		mfhiloM,
		hilo_wenM,
		breakM,syscallM,eretM,
		cp0_wenM,cp0toregM,
		is_in_delayslot_iM,

		//write back stage
		memtoregW,regwriteW,
		stallW,flushW,
//		hilotoregW,
		pctoregW
		
		);



	datapath dp(
			clk,rst,ext_int,
			//fetch stage
			pcF,
			inst_sram_en,
			instrF,
			//decode stage
			pcsrcD,branchD,pcjrD,rtD,instrD,stallD,flushD,riD,
			jumpD,
			sign_extD,
			equalD,
			opD,functD,
			//execute stage
			memtoregE,
			alusrcE,regdstE,
			regwriteE,
			alucontrolE,
			rd31srcE,
			hilotoregE,
			cp0toregE,
			flushE,
			stallE,
			cp0_wenE,
			//mem stage
			memtoregM,
			regwriteM,
			pctoregM,
			hilotoregM,
			aluoutM,writedataM,
			readdataM,
			mfhiloM,
			hilo_wenM,
			breakM,syscallM,eretM,
			cp0_wenM,cp0toregM,
			stallM,
			flushM,
			memwriteM,memreadM,
			mem_enM,
			mem_wenM,
			is_in_delayslot_iM,
			//writeback stage
			memtoregW,
			regwriteW,
//			hilotoregW,
			pctoregW,
			stallW,
			flushW,
			//debug
    debug_wb_pc     ,
    debug_wb_rf_wen  ,
    debug_wb_rf_wnum ,
    debug_wb_rf_wdata,


	i_stall, d_stall,
	all_stall
			);


	i_sram_to_sram_like i_sram_to_sram_like(
		.clk(clk),
		.rst(rst),
		.inst_sram_en(inst_sram_en),
		.inst_sram_addr(inst_sram_addr),
		.inst_sram_rdata(inst_sram_rdata),
		.i_stall(i_stall),
		.cnt(cnt),

		.inst_req(inst_req),
		.inst_wr(inst_wr),
        .inst_size(inst_size),
        .inst_addr(inst_addr),   
        .inst_wdata(inst_wdata),
        .inst_addr_ok(inst_addr_ok),
        .inst_data_ok(inst_data_ok),
        .inst_rdata(inst_rdata),

		.all_stall(all_stall)
	);

	d_sram_to_sram_like d_sram_to_sram_like(
		.clk(clk),
		.rst(rst),

		//sram
        .data_sram_en(data_sram_en),
        .data_sram_addr(data_sram_addr),
        .data_sram_rdata(data_sram_rdata),
        .data_sram_wen(data_sram_wen),
        .data_sram_wdata(data_sram_wdata),
        .d_stall(d_stall),
        //sram like
        .data_req(data_req),    
        .data_wr(data_wr),
        .data_size(data_size),
        .data_addr(data_addr),   
        .data_wdata(data_wdata),
        .data_addr_ok(data_addr_ok),
        .data_data_ok(data_data_ok),
        .data_rdata(data_rdata),

        .all_stall(all_stall)


	);
	
endmodule
