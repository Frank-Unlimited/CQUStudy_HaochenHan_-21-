module d_cache (
    input wire clk, rst,
    //mips core
    input         cpu_data_req     ,    //是否是读写请求
    input         cpu_data_wr      ,    //当前请求是写请求
    input  [1 :0] cpu_data_size    ,
    input  [31:0] cpu_data_addr    ,    //cpu提供的地址
    input  [31:0] cpu_data_wdata   ,    //cpu写的数据
    output [31:0] cpu_data_rdata   ,    //Cache返回给mips的数据
    output        cpu_data_addr_ok ,    //Cache成功返回地址给mips
    output        cpu_data_data_ok ,    //Cache成功返回数据给mips

    //axi interface
    output         cache_data_req     , //cache发送的读写请求
    output         cache_data_wr      , //cache发送的写请求
    output  [1 :0] cache_data_size    ,
    output  [31:0] cache_data_addr    ,  //请求的地址
    output  [31:0] cache_data_wdata   ,  //写入的内存的数据
    input   [31:0] cache_data_rdata   ,     //内存返回给cache的数据
    input          cache_data_addr_ok ,    //从内存中返回，是否成功收到数据
    input          cache_data_data_ok      //从内存中返回，如果是读请求，表示从返回cache 的数据，如果是写请求，代表写入数据成功。


);
    //Cache配置
    parameter  INDEX_WIDTH  = 10, OFFSET_WIDTH = 2;  //指令index为10位，offset2位
    localparam TAG_WIDTH    = 32 - INDEX_WIDTH - OFFSET_WIDTH; //tag20位
    localparam CACHE_DEEPTH = 1 << INDEX_WIDTH;  //cash 的深度 1024
    
    //Cache存储单元
    reg                 cache_valid [CACHE_DEEPTH - 1 : 0]; //有效位
    reg                 cache_dirty [CACHE_DEEPTH - 1 : 0]; //表示是否是脏位
    reg [TAG_WIDTH-1:0] cache_tag   [CACHE_DEEPTH - 1 : 0]; //tag
  
    reg [31:0]          cache_block [CACHE_DEEPTH - 1 : 0];//一个字，32位的数据

    //访问地址分解
    wire [OFFSET_WIDTH-1:0] offset;
    wire [INDEX_WIDTH-1:0] index;
    wire [TAG_WIDTH-1:0] tag;
    
    assign offset = cpu_data_addr[OFFSET_WIDTH - 1 : 0];
    assign index = cpu_data_addr[INDEX_WIDTH + OFFSET_WIDTH - 1 : OFFSET_WIDTH];
    assign tag = cpu_data_addr[31 : INDEX_WIDTH + OFFSET_WIDTH];

    //访问Cache line
    wire c_valid;
    wire c_dirty; // 是否修改过（脏块）
    wire [TAG_WIDTH-1:0] c_tag;
    wire [31:0] c_block;

    assign c_valid = cache_valid[index];
    assign c_dirty = cache_dirty[index]; // 是否修改过（脏块）
    assign c_tag   = cache_tag  [index];
    assign c_block = cache_block[index];

    //判断是否命中
    wire hit, miss;
    assign hit = c_valid & (c_tag == tag);  //cache line的valid位为1，且tag与地址中tag相等
    assign miss = ~hit;

    //cpu请求是不是读或写请求
    wire read, write;
    assign write = cpu_data_wr;
    assign read = cpu_data_req & ~write; // 是读写请求并且不是写请求

    //cache当前位置是否dirty
    wire dirty, clean;
    assign dirty = c_dirty;
    assign clean = ~dirty;

    //状态转移变化
    parameter IDLE = 2'b00, RM = 2'b01, WM = 2'b11;
    reg [1:0] state;
    //现在是否处于读内存状态，会满1个时钟周期恢复，和别的信号一起用于判断是否是从读内存状态回到空闲状态
    reg in_RM;
    
    //状态转换
    always @(posedge clk) begin
        if(rst) begin
            state <= IDLE;
            in_RM <= 1'b0;
        end
        else begin
            case(state)
                IDLE:   begin            //当前位置命中则处于IDLE状态；如果没命中，判断是否为脏位，当前位置是脏块是则进入写内存状态，不是则进入读内存状态
                        state <= cpu_data_req & hit ? IDLE :
                                 cpu_data_req & miss & dirty  ? WM :
                                 cpu_data_req & miss & clean  ? RM : IDLE;
                        in_RM <= 1'b0;
                        end
                RM:     begin
                        state <= cache_data_data_ok ? IDLE : RM;          //读内存完后回到IDLE
                        in_RM <= 1'b1;
                        end
                WM:     state <= cache_data_data_ok ? RM : WM;              //写内存完后回到RM读内存状态
            endcase
        end
    end

    //读内存
    //isRM, addr_rcv, read_finish用于构成类sram信号
    wire read_req;      //1次完整的读事务，从发出读请求到结束
    reg addr_rcv;       //地址接收成功(addr_ok)后到结束  
    wire read_finish;   //数据接收成功(data_ok)，即读请求结束
    always @(posedge clk) begin
        addr_rcv <= rst ? 1'b0 :          //当处于读内存状态，有向内存的读写请求，并且内存成功收到地址时，置1
                    read_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                    read_finish ? 1'b0 : addr_rcv;
    end
    assign read_req = state==RM;
    assign read_finish = read_req & cache_data_data_ok;

    //写内存
    wire write_req;     
    reg waddr_rcv;      
    wire write_finish;   
    always @(posedge clk) begin
        //当处于写内存状态，有向内存的读写请求，并且内存成功收到地址时，置1
        waddr_rcv <= rst ? 1'b0 :
                     write_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                     write_finish ? 1'b0 : waddr_rcv;
    end
    assign write_req = state==WM;
    assign write_finish = write_req & cache_data_data_ok;

    //output to mips core
    assign cpu_data_rdata   = hit ? c_block : cache_data_rdata;    //cache成功返回地址和数据给mips
    assign cpu_data_addr_ok = cpu_data_req & hit | cache_data_req & cache_data_addr_ok & read_req;  //当cpu有读写请求并且命中，或者需要读内存并且内存收到地址时，向cpu报告成功取到了地址
    assign cpu_data_data_ok = cpu_data_req & hit | cache_data_data_ok & read_req; //当命中，或者读内存时内存读到数据，向cpu报告数据就位
    
    //output to axi interface
    assign cache_data_req   = read_req & ~addr_rcv | write_req & ~waddr_rcv;
    assign cache_data_wr    = write_req;   //对内存有写请求
    assign cache_data_size  = cpu_data_size;
    assign cache_data_addr  = cache_data_wr ? {c_tag, index, offset} : cpu_data_addr;  //有两种情况，写请求时，把当前地址（脏块地址）传入内存，读请求时，把mips传入的地址传入内存
    assign cache_data_wdata = c_block;     //写入的数据只能是脏块的数据


    //写入Cache
    //保存地址中的tag index，
    reg [TAG_WIDTH-1:0] tag_save;
    reg [INDEX_WIDTH-1:0] index_save;
    always @(posedge clk) begin
        tag_save   <= rst ? 0 :
                      cpu_data_req ? tag : tag_save;
        index_save <= rst ? 0 :
                      cpu_data_req ? index : index_save;
    end

    wire [31:0] write_cache_data;
    wire [3:0] write_mask;

    //根据地址低两位和size，生成写掩码（针对sb，sh等不是写完整1个字的指令）4位对应1个字（4字节）中每个字的写使能
    assign write_mask = cpu_data_size==2'b00 ?
                            (cpu_data_addr[1] ? (cpu_data_addr[0] ? 4'b1000 : 4'b0100):
                                                (cpu_data_addr[0] ? 4'b0010 : 4'b0001)) :
                            (cpu_data_size==2'b01 ? (cpu_data_addr[1] ? 4'b1100 : 4'b0011) : 4'b1111);

    //掩码的使用：位为1的代表需要更新的
    //位拓展：{8{1'b1}} -> 8'b11111111

    assign write_cache_data = cache_block[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};


    //当前是否处于空闲状态
    wire isIDLE = state==IDLE;
    integer t;
    always @(posedge clk) begin
        if(rst) begin
            for(t=0; t<CACHE_DEEPTH; t=t+1) begin   //刚开始将Cache置为无效
                cache_valid[t] <= 0;
                cache_dirty[t] <= 0;  //初始化dirty为0
            end
        end
        else begin
            if(read_finish) begin //读内存结束后，将数据放入cache
                cache_valid[index_save] <= 1'b1;             //将Cache line置为有效
                cache_dirty[index_save] <= 1'b0; // 从内存读取的数据是clean
                cache_tag  [index_save] <= tag_save;
                cache_block[index_save] <= cache_data_rdata; //写入Cache line
            end
            //写请求操作时，对cache中的数据进行修改
            //当写命中并且回到空闲状态时就是write & isIDLE & hit
            //当写缺失并且从读内存状态回到空闲状态write & isIDLE & in_RM
            //in_RM会晚1个时间周期恢复，in_RM和isIDLE合起来就可以判断是否是从读状态回到空闲状态
            else if(write & isIDLE & (hit | in_RM)) begin   
                cache_dirty[index] <= 1'b1; // 改了数据，所以修改dirty
                cache_block[index] <= write_cache_data;      //写入Cache line，使用index而不是index_save
            end
        end
    end
endmodule