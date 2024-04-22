module d_cache_2_group(
    input wire clk, rst,
    //mips core
     input         cpu_data_req     ,    //是否是读写请�??
    input         cpu_data_wr      ,    //当前请求是写请求
    input  [1 :0] cpu_data_size    ,
    input  [31:0] cpu_data_addr    ,
    input  [31:0] cpu_data_wdata   ,
    output [31:0] cpu_data_rdata   ,    //cache返回给mips的数�??
    output        cpu_data_addr_ok ,    //Cache成功返回地址给mips
    output        cpu_data_data_ok ,    //Cache成功返回数据给mips

    //axi interface
    output         cache_data_req     , //cache发�?�的读写请求
    output         cache_data_wr      , //cache发�?�的读请�??
    output  [1 :0] cache_data_size    ,
    output  [31:0] cache_data_addr    ,
    output  [31:0] cache_data_wdata   ,
    input   [31:0] cache_data_rdata   , //内存返回给cache的数�??
    input          cache_data_addr_ok , //从内存中返回，是否成功收到数�??
    input          cache_data_data_ok   //从内存中返回，如果是读请求，表示从返�?? cache 的数据，如果是写请求，代表写入数据成功�??

);
    //Cache配置
    parameter  INDEX_WIDTH  = 10, OFFSET_WIDTH = 2;
    localparam TAG_WIDTH    = 32 - INDEX_WIDTH - OFFSET_WIDTH;
    localparam CACHE_DEEPTH = 1 << INDEX_WIDTH;
    
    //Cache存储单元
    //两路，扩�??1�??
    //reg  cache_valid [CACHE_DEEPTH - 1 : 0][1:0];
    reg  cache_valid0 [CACHE_DEEPTH - 1 : 0];
    reg  cache_valid1 [CACHE_DEEPTH - 1 : 0];

    //reg  cache_dirty [CACHE_DEEPTH - 1 : 0][1:0]; //表示是否是脏�??
    reg  cache_dirty0 [CACHE_DEEPTH - 1 : 0];
    reg  cache_dirty1 [CACHE_DEEPTH - 1 : 0];

    //reg  cache_use   [CACHE_DEEPTH - 1 : 0][1:0]; //用来保存�??近是否使�??
    reg  cache_use0   [CACHE_DEEPTH - 1 : 0];
    reg  cache_use1   [CACHE_DEEPTH - 1 : 0];
    
    //reg [TAG_WIDTH-1:0] cache_tag   [CACHE_DEEPTH - 1 : 0][1:0];
    reg [TAG_WIDTH-1:0] cache_tag0   [CACHE_DEEPTH - 1 : 0];
    reg [TAG_WIDTH-1:0] cache_tag1   [CACHE_DEEPTH - 1 : 0];

    //reg [31:0]          cache_block [CACHE_DEEPTH - 1 : 0][1:0];
    reg [31:0]          cache_block0 [CACHE_DEEPTH - 1 : 0];
    reg [31:0]          cache_block1 [CACHE_DEEPTH - 1 : 0];

    //访问地址分解
    wire [OFFSET_WIDTH-1:0] offset;
    wire [INDEX_WIDTH-1:0] index;
    wire [TAG_WIDTH-1:0] tag;
    
    assign offset = cpu_data_addr[OFFSET_WIDTH - 1 : 0];
    assign index = cpu_data_addr[INDEX_WIDTH + OFFSET_WIDTH - 1 : OFFSET_WIDTH];
    assign tag = cpu_data_addr[31 : INDEX_WIDTH + OFFSET_WIDTH];

    //cpu请求是不是读或写请求
    wire read, write;
    assign write = cpu_data_wr;
    assign read = cpu_data_req & ~write; // 是读写请求并且不是写请求

    //�??个一个赋�??
     wire c_valid[1:0];
    //  assign c_valid[0] = cache_valid[index][0];            //0是第0组，1是第�?�?
    //  assign c_valid[1] = cache_valid[index][1];

    assign c_valid[0] = cache_valid0[index];
    assign c_valid[1] = cache_valid1[index];

    wire c_dirty[1:0]; // 是否修改�??(脏块)
    // assign c_dirty[0] = cache_dirty[index][0];
    // assign c_dirty[1] = cache_dirty[index][1];

    assign c_dirty[0] = cache_dirty0[index];
    assign c_dirty[1] = cache_dirty1[index];
     
    wire c_use[1:0]; //�??近是否使�??
    // assign c_use  [0] = cache_use  [index][0];
    // assign c_use  [1] = cache_use  [index][1];
    assign c_use  [0] = cache_use0  [index];
    assign c_use  [1] = cache_use1  [index];
    
    wire [TAG_WIDTH-1:0] c_tag[1:0];
    // assign c_tag  [0] = cache_tag  [index][0];
    // assign c_tag  [1] = cache_tag  [index][1];
   
    assign c_tag  [0] = cache_tag0  [index];
    assign c_tag  [1] = cache_tag1  [index];
    
    wire [31:0] c_block[1:0];
    // assign c_block[0] = cache_block[index][0];
    // assign c_block[1] = cache_block[index][1];

    assign c_block[0] = cache_block0[index];
    assign c_block[1] = cache_block1[index];

     //判断是否命中
    wire hit, miss;
    assign hit = c_valid[0] & (c_tag[0] == tag) | c_valid[1] & (c_tag[1] == tag);  //cache line中有1路valid位为1，且tag与地�??中tag相等
    assign miss = ~hit;



    //因此�?
    //根据是否�??近使用，判断替换哪一�??


    //判断本次访问选中的组�?
    //如果访问命中，则way代表命中的一�?
    //如果访问缺失，则way代表�?要替换的�?�?
    wire way;
    assign way = hit ? (c_valid[0] & (c_tag[0] == tag) ? 1'b0 : 1'b1) :   //   hit，hit的那�??路， c_use[0]==1 0路最近使用，则将way置为1表示1路去替换
                   c_use[0] ? 1'b1 : 1'b0; 


//没有命中，如�?0路最近使用，就将way视作1
//如果命中了，就是命中的那�?�?



    //cache当前位置是否dirty
    wire dirty, clean;
    assign dirty = c_dirty[way];
    assign clean = ~dirty;

    //状�?�转�??
    parameter IDLE = 2'b00, RM = 2'b01, WM = 2'b11;
    reg [1:0] state;
     //现在是否处于读内存状�??
    reg in_RM;
    always @(posedge clk) begin
        if(rst) begin
            state <= IDLE;
            in_RM <= 1'b0;
        end
        else begin
            case(state)
                IDLE:   begin   
                        state <= cpu_data_req & hit ? IDLE :
                                 cpu_data_req & miss & dirty  ? WM :
                                 cpu_data_req & miss & clean  ? RM : IDLE;
                        in_RM <= 1'b0;
                        end
           
                RM:     begin //读内存完后回到IDLE
                        state <= cache_data_data_ok ? IDLE : RM;
                        in_RM <= 1'b1;
                        end

                WM:     state <= cache_data_data_ok ? RM : WM;                  //写内存完后回到RM读内存状�??
            endcase
        end
    end

    //读内�??
    //isRM, addr_rcv, read_finish用于构�?�类sram信号
    wire read_req;      //1次完整的读事务，从发出读请求到结�??
    reg addr_rcv;       //地址接收成功(addr_ok)后到结束  
    wire read_finish;   //数据接收成功(data_ok)，即读请求结�??
    always @(posedge clk) begin
        //当处于读内存状�?�，有向内存的读写请求，并且内存成功收到地址时，�??1
        addr_rcv <= rst ? 1'b0 :
                    read_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                    read_finish ? 1'b0 : addr_rcv;
    end
    assign read_req = state==RM;
    assign read_finish = read_req & cache_data_data_ok;

    //写内�??
    wire write_req;     
    reg waddr_rcv;      
    wire write_finish;   
    always @(posedge clk) begin
         //当处于写内存状�?�，有向内存的读写请求，并且内存成功收到地址时，�??1
        waddr_rcv <= rst ? 1'b0 :
                     write_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                     write_finish ? 1'b0 : waddr_rcv;
    end
    assign write_req = state==WM;
    assign write_finish = write_req & cache_data_data_ok;

    //output to mips core
    assign cpu_data_rdata   = hit ? c_block[way] : cache_data_rdata;    //cache成功返回地址和数据给mips
    assign cpu_data_addr_ok = cpu_data_req & hit | cache_data_req & cache_data_addr_ok&read_req;
    assign cpu_data_data_ok = cpu_data_req & hit | cache_data_data_ok&read_req;

    //output to axi interface
    assign cache_data_req   = read_req & ~addr_rcv | write_req & ~waddr_rcv;     //对内存有写请�??
    assign cache_data_wr    = write_req;
    assign cache_data_size  = cpu_data_size;       
    assign cache_data_addr  = cache_data_wr ? {c_tag[way], index, offset} : cpu_data_addr;  //有两种情况，写请求时，把当前地址（脏块地�??）传入内存，读请求时，把mips传入的地�??传入内存
    assign cache_data_wdata = c_block[way];    //写入的数据只能是脏块的数�??

    //写入Cache
    //保存地址中的tag index，防止addr发生改变
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

    
    //根据地址低两位和size，生成写掩码（针对sb，sh等不是写完整1个字的指令）4位对�??1个字�??4字节）中每个字的写使�??
    assign write_mask = cpu_data_size==2'b00 ?
                            (cpu_data_addr[1] ? (cpu_data_addr[0] ? 4'b1000 : 4'b0100):
                                                (cpu_data_addr[0] ? 4'b0010 : 4'b0001)) :
                            (cpu_data_size==2'b01 ? (cpu_data_addr[1] ? 4'b1100 : 4'b0011) : 4'b1111);

    //掩码的使用：位为1的代表需要更新的
    //位拓展：{8{1'b1}} -> 8'b11111111
    
    //assign write_cache_data = cache_block[index][way] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              //cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};

    // assign write_cache_data = (!hit && c_use[0]) ? (cache_block1[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}})   :  (cache_block0[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}}) ;

    assign write_cache_data = (way ? cache_block1[index]: cache_block0[index] ) & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};



                              //如果way�?1�?

    //当前是否处于空闲状�??
    wire isIDLE = state==IDLE;

    integer t,y;
    always @(posedge clk) begin
        if(rst) begin
              for(t=0; t<CACHE_DEEPTH; t=t+1) begin   //刚开始将Cache置为无效
                //  for (y = 0; y<2; y=y+1) begin
                //      cache_valid[t][y] <= 0;
                //      cache_dirty[t][y] <= 0;  //初始化dirty�??0
                //      cache_use  [t][y] <= 0;  //�??近使用初始化�??0
                //  end

                 cache_valid0[t] <= 0;
                 cache_valid1[t] <= 0;

                 cache_dirty0[t] <= 0;
                 cache_dirty1[t] <= 0;

                 cache_use0[t] <= 0;
                 cache_use1[t] <= 0;
              end

//            cache_valid[0] <= '{default: '0};
//            cache_dirty[0] <= '{default: '0};
//            cache_use[0] <= '{default: '0};

//              cache_valid0 <= '{default: '0};
//              cache_valid1 <= '{default: '0};
//              cache_dirty0 <= '{default: '0};
//              cache_use0 <= '{default: '0};
//              cache_dirty1 <= '{default: '0};
//              cache_use1 <= '{default: '0};

//            cache_valid[1] <= '{default: '0};
//            cache_dirty[1] <= '{default: '0};
//            cache_use[1] <= '{default: '0};

            //   cache_valid0[1] <= '{default: '0};
            //   cache_valid1[1] <= '{default: '0};
            //   cache_dirty0[1] <= '{default: '0};
            //   cache_use0[1] <= '{default: '0};
            //   cache_dirty1[1] <= '{default: '0};
            //   cache_use1[1] <= '{default: '0};
        end

        else begin
            if(read_finish) begin //读内存结束后，将数据放入cache
                if (way) begin
                    cache_valid1[index_save] <= 1'b1;             //将Cache line置为有效
                    cache_dirty1[index_save] <= 1'b0;            // 从内存读取的数据是clean
                    cache_tag1  [index_save] <= tag_save;
                    cache_block1[index_save] <= cache_data_rdata; //写入Cache line
                end

                else begin
                    cache_valid0[index_save] <= 1'b1;             //将Cache line置为有效
                    cache_dirty0[index_save] <= 1'b0;            // 从内存读取的数据是clean
                    cache_tag0  [index_save] <= tag_save;
                    cache_block0[index_save] <= cache_data_rdata; //写入Cache line
                end
            end


            //写请求操作时，对cache中的数据进行修改
            //当写命中并且回到空闲状�?�时就是write & isIDLE & hit
            //当写缺失并且从读内存状�?�回到空闲状态write & isIDLE & in_RM
            //in_RM会晚1个时间周期恢复，in_RM和isIDLE合起来就可以判断是否是从读状态回到空闲状�??
            else if(write & isIDLE & (hit | in_RM)) begin   
                if (way) begin
                cache_dirty1[index] <= 1'b1; // 改了数据，所以修改dirty
                cache_block1[index] <= write_cache_data;      //写入Cache line，使用index而不是index_save
                end
                else begin
                cache_dirty0[index] <= 1'b1; // 改了数据，所以修改dirty
                cache_block0[index] <= write_cache_data;      //写入Cache line，使用index而不是index_save
                end
            end

            if(cpu_data_req & isIDLE & (hit | in_RM)) begin
                // 是读写请求指令，hit进入IDLE状�?? ，从读内存回到IDLE后，将最近使用情况更�??
                if (way) begin
                    cache_use1[index] <= 1'b1; // way路最近使用了
                    cache_use0[index] <= 1'b0; // 另一路最近未使用
                end
                else begin
                    cache_use0[index] <= 1'b1; // way路最近使用了
                    cache_use1[index] <= 1'b0; // 另一路最近未使用
                end
            end
        end
    end
endmodule




