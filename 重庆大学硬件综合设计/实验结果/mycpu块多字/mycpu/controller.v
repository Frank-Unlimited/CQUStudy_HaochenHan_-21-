`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/10/23 15:21:30
// Design Name: 
// Module Name: controller
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


module controller(
	input wire clk,rst,

	// fetch stage
	

	//decode stage
	input wire[5:0] opD,functD,
	input wire[4:0] rtD,
	input wire[31:0] instrD,
	input wire stallD,flushD,
	output wire riD,
	output wire sign_extD,
	output wire pcsrcD,branchD,equalD,jumpD,pcjrD,
	
	//execute stage
	input wire flushE, stallE,
	output wire memtoregE,alusrcE,
	output wire regdstE,regwriteE,	
	output wire[4:0] alucontrolE,
	output wire rd31srcE,
	output wire hilotoregE,
	output wire cp0toregE,
	output wire cp0_wenE,
	//mem stage
	input wire stallM,
	input wire flushM,
	output wire memtoregM,
				regwriteM,
				pctoregM,
				hilotoregM,
	output wire memwriteM,memreadM,
	output wire[1:0] mfhiloM,
	output wire hilo_wenM,
	output wire breakM,syscallM,eretM,
	output wire cp0_wenM,cp0toregM,
	output wire is_in_delayslot_iM,
	//write back stage
	output wire memtoregW,regwriteW,
	input wire stallW,
	output wire flushW,
//	output wire hilotoregW,
	output wire pctoregW
    );
	wire is_in_delayslot_iF;
	//decode stage
	wire memtoregD,memwriteD,alusrcD,
		regdstD,regwriteD,hilotoregD;
	wire[4:0] alucontrolD;
	wire[1:0] mfhi_loD;
	wire hilo_wenD;
	wire pctoregD;
	wire rd31srcD;
	wire breakD,syscallD,eretD;
	wire cp0_wenD,cp0toregD;
	wire is_in_delayslot_iD;
	//execute stage
	wire memwriteE;
	wire[1:0] mfhi_loE;
	wire hilo_wenE;
	wire pctoregE;
	wire breakE,syscallE,eretE;
	
	wire memreadE;
	wire is_in_delayslot_iE;
	// wire hilotoregE;
	
	//mem stage
//	wire hilotoregM;
	

	maindec md(
		opD,functD,rtD,instrD,riD,
		memtoregD,memwriteD,memreadD,
		sign_extD,
		branchD,alusrcD,
		regdstD,regwriteD,
		jumpD,pcjrD,
		mfhi_loD,
		hilo_wenD,
		hilotoregD,
		pctoregD,
		rd31srcD,
		breakD,syscallD,eretD,
		cp0_wenD,
		cp0toregD
		);
	aludec ad(opD,functD,alucontrolD);

	assign pcsrcD = branchD & equalD;
	assign is_in_delayslot_iF = branchD | jumpD | pcjrD;



	//pipeline registers

	flopenrc #(1) regD(
		clk,
		rst,
		~stallD,
		flushD,
		is_in_delayslot_iF,
		is_in_delayslot_iD
	);

	flopenrc #(23) regE(
		clk,
		rst,
		~stallE,
		flushE,
		{memtoregD,memwriteD,memreadD,alusrcD,regdstD,regwriteD,alucontrolD,mfhi_loD,hilo_wenD,hilotoregD,pctoregD,rd31srcD,breakD,syscallD,eretD,cp0_wenD,cp0toregD,is_in_delayslot_iD},
		{memtoregE,memwriteE,memreadE,alusrcE,regdstE,regwriteE,alucontrolE,mfhi_loE,hilo_wenE,hilotoregE,pctoregE,rd31srcE,breakE,syscallE,eretE,cp0_wenE,cp0toregE,is_in_delayslot_iE}
		);
		
	flopenrc #(20) regM(
		clk,rst,
		~stallM,
		flushM,
		{memtoregE,memwriteE,memreadE,regwriteE,mfhi_loE,hilo_wenE,hilotoregE,pctoregE,breakE,syscallE,eretE,cp0_wenE,cp0toregE,is_in_delayslot_iE},
		{memtoregM,memwriteM,memreadM,regwriteM,mfhiloM,hilo_wenM,hilotoregM,pctoregM,breakM,syscallM,eretM,cp0_wenM,cp0toregM,is_in_delayslot_iM}
		);
	flopenrc #(13) regW(
		clk,rst,
		~stallW,
		flushW,
		{memtoregM,regwriteM,hilotoregM,pctoregM},
		{memtoregW,regwriteW,hilotoregW,pctoregW}
		);
endmodule
