`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2017/11/02 15:12:22
// Design Name: 
// Module Name: datapath
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


module datapath(
	input wire clk,rst,
	input wire[5:0] ext_int,

	//fetch stage
	output wire[31:0] pcF,
	output wire inst_enF,
	input wire[31:0] instrF,
	//decode stage
	input wire pcsrcD,branchD,pcjrD,
	output wire[4:0] rtD,
	output wire[31:0] instrD,
	output wire stallD,flushD,
	input wire riD,
	input wire jumpD,
	input wire sign_extD,	
	output wire equalD,
	output wire[5:0] opD,functD,
	//execute stage
	input wire memtoregE,
	input wire alusrcE,regdstE,
	input wire regwriteE,
	input wire[4:0] alucontrolE,
	input wire rd31srcE,
	input wire hilotoregE,
	input wire cp0toregE,
	output wire flushE,
	output wire stallE,
	input wire cp0_wenE,
	//mem stage
	input wire memtoregM,
	input wire regwriteM,
	input wire pctoregM,
	input wire hilotoregM,
	output wire[31:0] mem_addrM,writedataM,
	input wire[31:0] readdatapreM,
	input wire[1:0] mfhiloM,
	input wire hilo_wenM,
	input wire breakM,syscallM,eretM,
	input wire cp0_wenM, cp0toregM,
	output wire stallM,
	output wire flushM,
	input wire memwriteM, memreadM,
	output wire mem_enM,
	output wire [3:0] mem_wenM,
	input wire is_in_delayslot_iM,
	//writeback stage
	input wire memtoregW,
	input wire regwriteW,
//	input wire hilotoregW,
	input wire pctoregW,
	output wire stallW,
	output wire flushW,
	
	//debug
    output [31:0] debug_wb_pc     ,
    output [3:0] debug_wb_rf_wen  ,
    output [4:0] debug_wb_rf_wnum ,
    output [31:0] debug_wb_rf_wdata,
	
	input wire i_stall, d_stall,
	output wire all_stall
    );
	
	//fetch stage
	wire stallF;
	wire pcerrorF;
	
	//FD
	wire [31:0] pcnextFD,pcnextbrFD,pcplus4F,pcbranchD;
	//decode stage
	wire [31:0] pcplus4D,pcD;
	wire [4:0] saD; //saD = instrD[10:6]
	wire forwardaD,forwardbD;
	wire [4:0] rsD,rdD;
//	wire [1:0] mfhi_loD;
	wire [31:0] signimmD,signimmshD;
	wire [31:0] srcaD,srca2D,srcbD,srcb2D;
	//execute stage
	wire [1:0] forwardaE,forwardbE;
	wire [4:0] rsE,rtE,rdE,saE;
	wire riE;
	wire overflowE;
	wire [31:0] instrE,pcE;
	wire div_stallE,mul_stallE;
	
	wire [4:0] writeregE;
	wire [31:0] signimmE;
//	wire [1:0] mfhi_loE;
	wire [31:0] srcaE,srca2E,srcbE,srcb2E,srcb3E;
	wire [63:0] aluoutE;
	//mem stage
	wire [63:0] hiloaluM;
	wire [31:0] hiloresultM;
	wire [31:0] instrM,pcM,aluoutM;
	wire overflowM;
//	wire [1:0] mfhiloM;
	wire [4:0] writeregM;
	wire [31:0] resultM;
	wire [63:0] aluoutHiloM;
	wire AdESM,AdELM,pcerrorM,all_flushM;
	wire riM;
	wire [31:0]except_typeM;
	wire [31:0]pcexcM;
	//writeback stage
	wire [4:0] writeregW;
	wire [31:0] aluoutW,readdataW,resultW;
	wire [31:0] hiloresultW,pcW;

	// debug
    assign debug_wb_pc = pcW;
    assign debug_wb_rf_wen = {4{regwriteW & ~stallW}};
    assign debug_wb_rf_wnum = writeregW;
    assign debug_wb_rf_wdata = resultW; 

	//hazard detection
	hazard h(
		//fetch stage
		stallF,
		//decode stage
		rsD,rtD,
		branchD,
		pcjrD,
		forwardaD,forwardbD,
		stallD,
		flushD,
		//execute stage
		rsE,rtE,rdE,
		writeregE,
		regwriteE,
		memtoregE,
		div_stallE,
		mul_stallE,
		hilotoregE,
		cp0toregE,
		forwardaE,forwardbE,
		flushE,
		stallE,
		//mem stage
		writeregM,
		regwriteM,
		memtoregM,
		all_flushM,
		stallM,
		flushM,
		//write back stage
		writeregW,
		regwriteW,
		stallW,
		flushW,
		i_stall, d_stall,
		all_stall
		);

	//next PC logic (operates in fetch an decode)
	mux2 #(32) pcbrmux(pcplus4F,pcbranchD,pcsrcD,pcnextbrFD);
	wire [31:0] pcnextjrFD, pcnextexceptionFD;
	mux2 #(32) pcjrmux(pcnextbrFD,srca2D,pcjrD,pcnextjrFD);
	mux2 #(32) pcexceptionmux(pcnextjrFD,pcexcM,all_flushM,pcnextexceptionFD);
	mux2 #(32) pcmux(pcnextexceptionFD,
		{pcplus4D[31:28],instrD[25:0],2'b00},
		jumpD,pcnextFD);
	assign inst_enF = ~all_flushM & ~pcerrorF; // 防止instram读出instr[pc后第三条]

	//regfile (operates in decode and writeback)
	
	regfile rf(clk,regwriteW,rsD,rtD,writeregW,resultW,srcaD,srcbD);

	//fetch stage logic
	pc #(32) pcreg(clk,rst,~stallF,pcnextFD,pcF);
	adder pcadd1(pcF,32'b100,pcplus4F);


	////////////////////////////////////// FD ////////////////////////////////////////////

	flopenrc #(32) r1D(clk,rst,~stallD,flushD,pcplus4F,pcplus4D);
	flopenrc #(32) r2D(clk,rst,~stallD,flushD,instrF,instrD);
	flopenrc #(32) r3D(clk,rst,~stallD,flushD,pcF,pcD);

	////////////////////////////////////// D ////////////////////////////////////////////

	signext se(instrD[15:0],sign_extD,signimmD);
	sl2 immsh(signimmD,signimmshD);
	adder pcadd2(pcplus4D,signimmshD,pcbranchD);
	// wire [31:0] srcaD1, srcaD2;
	// mux2 #(32) forwarda1mux(srcaD,aluoutM,forwardaD,srcaD1);
	// mux2 #(32) forwarda2mux(srcaD,pcplus8M,forwardaD,srcaD2);
	// mux2 #(32) srcadmux(srcaD1,srcaD2,pctoregM,srca2D);

	wire [31:0] pcplus8M;
	assign pcplus8M = pcM + 8;	// hilight : pcPlus8放在了M阶段
	wire [31:0] toForwardM;
	assign toForwardM = resultM;

	mux2 #(32) forwardamux(srcaD,toForwardM,forwardaD,srca2D);

	mux2 #(32) forwardbmux(srcbD,toForwardM,forwardbD,srcb2D);
	eqcmp comp(srca2D,srcb2D,opD,rtD,equalD);

	assign opD = instrD[31:26];
	assign functD = instrD[5:0];
	assign rsD = instrD[25:21];
	assign rtD = instrD[20:16];
	assign rdD = instrD[15:11];
	assign saD = instrD[10:6];

	////////////////////////////////////// DE ////////////////////////////////////////////
	flopenrc #(32) r1E(clk,rst,~stallE,flushE,srcaD,srcaE);
	flopenrc #(32) r2E(clk,rst,~stallE,flushE,srcbD,srcbE);
	flopenrc #(32) r3E(clk,rst,~stallE,flushE,signimmD,signimmE);
	flopenrc #(5) r4E(clk,rst,~stallE,flushE,rsD,rsE);
	flopenrc #(5) r5E(clk,rst,~stallE,flushE,rtD,rtE);
	flopenrc #(5) r6E(clk,rst,~stallE,flushE,rdD,rdE);
	flopenrc #(5) r7E(clk,rst,~stallE,flushE,saD,saE);
	flopenrc #(32) r8E(clk,rst,~stallE,flushE,instrD,instrE);
//	flopenrc #(2) r9E(clk,rst,~stallE,flushE,mfhi_loD,mfhi_loE);
	flopenrc #(32) r10E(clk,rst,~stallE,flushE,pcD,pcE);
	flopenrc #(1) r11E(clk,rst,~stallE,flushE,riD,riE);

	////////////////////////////////////// E ////////////////////////////////////////////
	mux3 #(32) forwardaemux(srcaE,resultW,toForwardM,forwardaE,srca2E);
	mux3 #(32) forwardbemux(srcbE,resultW,toForwardM,forwardbE,srcb2E);
	mux2 #(32) srcbmux(srcb2E,signimmE,alusrcE,srcb3E);

	alu alu(clk,rst,srca2E,srcb3E,alucontrolE,saE,hiloaluM,all_flushM,aluoutE,div_stallE,mul_stallE,overflowE,i_stall);
	
	wire [4:0] writereg2E;
	mux2 #(5) wrmux(rtE,rdE,regdstE,writereg2E);
	mux2 #(5) rd31mux(writereg2E,5'b11111,rd31srcE,writeregE); // jal

	//mem stage
	wire [31:0]hi_o, lo_o;
	hilo_reg hilo0(
        .clk(clk),
        .rst(rst),
        .we(hilo_wenM),
        .hilo_i(aluoutHiloM),
        .hi_o(hi_o),
		.lo_o(lo_o),
        .hilo(hiloaluM)
    );
	mux2 #(32)hilomux(hi_o,lo_o,mfhiloM[0],hiloresultM);
	assign mem_enM = (memreadM | memwriteM) & ~all_flushM; // reason same as instr
	memwen memwen(instrM[31:26],aluoutM,mem_wenM,AdESM);      //SW类可能设计的地址例外
	memlwexception memlwexception(instrM[31:26],aluoutM,AdELM);   //LW类可能设计的地址例外
 


	wire [31:0] readdataM; 
	wire [31:0] tempWritedataM;

	
	memreaddata memreaddata(instrM[31:26],aluoutM,readdatapreM,readdataM);
	memwritedata memwritedata(instrM[31:26],aluoutM,tempWritedataM,writedataM);


	//assign writedataM = tempWritedataM;
	assign mem_addrM = aluoutM;
	assign pcerrorF = (pcF[1:0] == 2'b00 ? 1'b0 : 1'b1);
	assign pcerrorM = (pcM[1:0] == 2'b00 ? 1'b0 : 1'b1);
	
	flopenrc #(32) r1M(clk,rst,~stallM,flushM,srcb2E,tempWritedataM);  // TODO:2E->3E
	flopenrc #(32) r2M(clk,rst,~stallM,flushM,aluoutE[31:0],aluoutM);
	flopenrc #(5) r3M(clk,rst,~stallM,flushM,writeregE,writeregM);
	flopenrc #(32) r4M(clk,rst,~stallM,flushM,instrE,instrM);
//	flopenrc #(2) r5M(clk,rst,~stallM,flushM,mfhi_loE,mfhiloM);
	flopenrc #(64) r6M(clk,rst,~stallM,flushM,aluoutE,aluoutHiloM);
	flopenrc #(32) r7M(clk,rst,~stallM,flushM,pcE,pcM);
	flopenrc #(1) r8M(clk,rst,~stallM,flushM,riE,riM);
	flopenrc #(1) r9M(clk,rst,~stallM,flushM,overflowE,overflowM);

	wire[31:0] temp1ResultM, temp2ResultM, temp3ResultM;
	mux2 #(32) resmux(aluoutM,readdataM,memtoregM,temp1ResultM);
	wire [31:0] pcplus8M;
	assign pcplus8M = pcM + 8;              //CONTROLLER CHUANJINLAI
	mux2 #(32) res2mux(temp1ResultM,hiloresultM,hilotoregM,temp2ResultM);
	mux2 #(32) res3mux(temp2ResultM,cp0_data_oM,cp0toregM,temp3ResultM);
	mux2 #(32) res4mux(temp3ResultM,pcplus8M,pctoregM,resultM);

	wire [31:0] cp0_statusM,cp0_causeM,cp0_epcM,badvaddrM,cp0_data_oM;
	exception exception0(
        .rst(rst),
        .ext_int(ext_int),
        .ri(riM), .brek(breakM), .syscall(syscallM), .overflow(overflowM), .AdES(AdESM), .AdEL(AdELM), .pcerror(pcerrorM), .eret(eretM),
        .cp0_status(cp0_statusM), .cp0_cause(cp0_causeM), .cp0_epc(cp0_epcM),
        .pcM(pcM),
        .alu_outM(aluoutM),
        .except_type(except_typeM),
        .flush_exception(all_flushM),
        .pc_exception(pcexcM),
        .badvaddrM(badvaddrM)
    );

	cp0_reg cp0(
       .clk(clk),
       .rst(rst),

       .int_i(ext_int),
       .we_i(cp0_wenM & ~stallM),
       .waddr_i(instrM[15:11]),
       .data_i(tempWritedataM),
       
       .raddr_i(instrM[15:11]),

       .excepttype_i(except_typeM),
       .current_inst_addr_i(pcM),
       .is_in_delayslot_i(is_in_delayslot_iM),
       .bad_addr_i(badvaddrM),

       .data_o(cp0_data_oM),
       .status_o(cp0_statusM),
       .cause_o(cp0_causeM),
       .epc_o(cp0_epcM)
    );

	//cp0_reg cp0(
    //    .clk(clk),
    //    .rst(rst),
    //    .int_i(ext_int),
    //    .we_i(cp0_wenM),
    //    .waddr_i(instrM[15:11]),
     //   .data_i(tempWritedataM),
     //   
    //    .raddr_i(instrM[15:11]),
//
    //    .excepttype_i(except_typeM),
    //    .current_inst_addr_i(pcM),
    //    .is_in_delayslot_i(is_in_delayslot_iM),
    //    .bad_addr_i(badvaddrM),
//
    //    .data_o(cp0_data_oM),
    //    .status_o(cp0_statusM),
    //    .cause_o(cp0_causeM),
    //    .epc_o(cp0_epcM)
    //);


	//writeback stage
	flopenrc #(32) r1W(clk,rst,~stallW,flushW,aluoutM,aluoutW);
	flopenrc #(32) r2W(clk,rst,~stallW,flushW,readdataM,readdataW);
	flopenrc #(5) r3W(clk,rst,~stallW,flushW,writeregM,writeregW);
	flopenrc #(32) r4W(clk,rst,~stallW,flushW,hiloresultM,hiloresultW);
	flopenrc #(32) r5W(clk,rst,~stallW,flushW,pcM,pcW);
	flopenrc #(32) r6W(clk,rst,~stallW,flushW,resultM,resultW);
	// wire[31:0] temp1ResultW, temp2ResultW;
	// mux2 #(32) resmux(aluoutW,readdataW,memtoregW,temp1ResultW);
	// wire [31:0] pcplus8W;
	// assign pcplus8W = pcW + 8;
	// mux2 #(32) res2mux(temp1ResultW,hiloresultW,hilotoregW,temp2ResultW);
	// mux2 #(32) res3mux(temp2ResultW,pcplus8W,pctoregW,resultW);
endmodule
