module d_cache_wb_4way_FLRU (
    input wire clk, rst,
    //mips core  cpu<-->cache
    input         cpu_data_req     , // load或store请求信号
    input         cpu_data_wr      , // 写请求信�??
    input  [1 :0] cpu_data_size    , // 结合地址�??低两位，确定数据的有效字节（用于sb、sh等指令）
    input  [31:0] cpu_data_addr    , // 访存地址
    input  [31:0] cpu_data_wdata   , // 写入数据
    output [31:0] cpu_data_rdata   , // 读出数据
    output        cpu_data_addr_ok , // 由cache的ok间接传�?? mem确认addr已经收到
    output        cpu_data_data_ok , // 由cache的ok间接传�?? 读：mem确认cpu请求的数据已经传递给cpu；写：mem确认cpu请求写入的数据已经写入�??

    //axi interface   cache<-->mem
    output         cache_data_req     , // load或store请求信号
    output         cache_data_wr      , // 写请求信�??
    output  [1 :0] cache_data_size    ,
    output  [31:0] cache_data_addr    ,
    output  [31:0] cache_data_wdata   , // 写入内存的数�??
    input   [31:0] cache_data_rdata   , // 从内存读出的数据
    input          cache_data_addr_ok , // mem确认addr已经收到
    input          cache_data_data_ok   // 读：mem确认请求的数据已经传递给cache；写：mem确认请求写入的数据已经写�??
);
    //Cache配置
    parameter  INDEX_WIDTH  = 10, OFFSET_WIDTH = 2;
    localparam TAG_WIDTH    = 32 - INDEX_WIDTH - OFFSET_WIDTH;
    localparam CACHE_DEEPTH = 1 << INDEX_WIDTH;
    
    //Cache存储单元
    // 【m】四路组相联，cache set�??要扩大到四�??
    reg  cache_valid [CACHE_DEEPTH - 1 : 0][3:0];
    reg  cache_valid0 [CACHE_DEEPTH - 1 : 0];
    reg  cache_valid1 [CACHE_DEEPTH - 1 : 0];
    reg  cache_valid2 [CACHE_DEEPTH - 1 : 0];
    reg  cache_valid3 [CACHE_DEEPTH - 1 : 0];

    reg cache_dirty [CACHE_DEEPTH - 1 : 0][3:0]; // 增加脏位
    reg cache_dirty0 [CACHE_DEEPTH - 1 : 0];
    reg cache_dirty1 [CACHE_DEEPTH - 1 : 0];
    reg cache_dirty2 [CACHE_DEEPTH - 1 : 0];
    reg cache_dirty3 [CACHE_DEEPTH - 1 : 0];

    reg [TAG_WIDTH-1:0] cache_tag   [CACHE_DEEPTH - 1 : 0][3:0];
    reg [TAG_WIDTH-1:0] cache_tag0   [CACHE_DEEPTH - 1 : 0];
    reg [TAG_WIDTH-1:0] cache_tag1   [CACHE_DEEPTH - 1 : 0];
    reg [TAG_WIDTH-1:0] cache_tag2   [CACHE_DEEPTH - 1 : 0];
    reg [TAG_WIDTH-1:0] cache_tag3   [CACHE_DEEPTH - 1 : 0];


    // reg [31:0]          cache_block [CACHE_DEEPTH - 1 : 0][3:0];
    reg [31:0]          cache_block0 [CACHE_DEEPTH - 1 : 0];
    reg [31:0]          cache_block1 [CACHE_DEEPTH - 1 : 0];
    reg [31:0]          cache_block2 [CACHE_DEEPTH - 1 : 0];
    reg [31:0]          cache_block3 [CACHE_DEEPTH - 1 : 0];

    // 【new】伪LRU的查找树表，存储�??有set的查找树。四路组每个组需�??3bit存储节点
    reg [2:0]           tree_table  [CACHE_DEEPTH - 1 : 0];

    // 【new】对应cache set的查找树，tree[2]为根节点, tree[1]右子树，tree[0]左子�??
    wire [2:0] tree;

    //访问地址分解
    wire [OFFSET_WIDTH-1:0] offset;
    wire [INDEX_WIDTH-1:0] index;
    wire [TAG_WIDTH-1:0] tag;
    
    assign offset = cpu_data_addr[OFFSET_WIDTH - 1 : 0];
    assign index = cpu_data_addr[INDEX_WIDTH + OFFSET_WIDTH - 1 : OFFSET_WIDTH];
    assign tag = cpu_data_addr[31 : INDEX_WIDTH + OFFSET_WIDTH];

    //【m】访问Cache line
    wire c_valid[3:0];
    wire c_dirty[3:0]; // 脏位，即是否修改�??
    wire [TAG_WIDTH-1:0] c_tag[3:0];
    wire [31:0] c_block[3:0];

    assign tree = tree_table[index]; // 【n】当前cache set的查找树赋�??

    assign c_valid[0] = cache_valid0[index];
    assign c_valid[1] = cache_valid1[index];
    assign c_valid[2] = cache_valid2[index];
    assign c_valid[3] = cache_valid3[index];

    assign c_dirty[0] = cache_dirty0[index];
    assign c_dirty[1] = cache_dirty1[index];
    assign c_dirty[2] = cache_dirty2[index];
    assign c_dirty[3] = cache_dirty3[index];

    assign c_tag  [0] = cache_tag0  [index];
    assign c_tag  [1] = cache_tag1  [index];
    assign c_tag  [2] = cache_tag2  [index];
    assign c_tag  [3] = cache_tag3  [index];

    assign c_block[0] = cache_block0[index];
    assign c_block[1] = cache_block1[index];
    assign c_block[2] = cache_block2[index];
    assign c_block[3] = cache_block3[index];

    //【m】判断是否命�??
    wire hit, miss;
    assign hit = c_valid[0] & (c_tag[0] == tag) | 
                c_valid[1] & (c_tag[1] == tag) |
                c_valid[2] & (c_tag[2] == tag) |
                c_valid[3] & (c_tag[3] == tag);  // cache line某一路中的valid位为1，且tag与地�??中tag相等
    assign miss = ~hit;

    // 【m】后面的cache处理应访问哪�??�??
    wire[1:0] c_way;
    // 命中：�?�hit的那�??�??
    // 缺失：�?�查找树索引出的�??近未使用的那�??�??
    // 索引右子�??: tree[2]==0 -> c_way = {tree[2], tree[1]}
    // 索引左子�??: tree[2]==1 -> c_way = {tree[2], tree[0]}  
    assign c_way = hit ? (c_valid[0] & (c_tag[0] == tag) ? 2'b00 :
                          c_valid[1] & (c_tag[1] == tag) ? 2'b01 :
                          c_valid[2] & (c_tag[2] == tag) ? 2'b10 :
                          2'b11) : 
                   tree[2] ? {tree[2], tree[0]} : // 索引左子�??
                             {tree[2], tree[1]};  // 索引右子�??

    //读或�??
    wire read, write;
    assign write = cpu_data_wr;
    assign read = ~write;

    // 当前cache line是否dirty
    wire dirty, clean; 
    assign dirty = c_dirty[c_way]; // 选中的那�??路的脏位赋�??
    assign clean = ~dirty;

    //FSM 新的状�?�机
    parameter IDLE = 2'b00, RM = 2'b01, WM = 2'b11;
    reg [1:0] state;
    // 标识是否前一个状态是RM，用于确定写缺失时写内存的时�??
    reg from_RM;
    always @(posedge clk) begin
        if(rst) begin
            state <= IDLE;
            from_RM <= 1'b0;
        end
        else begin
            case(state)
                IDLE:begin
                    if (cpu_data_req) begin
                        if (hit) // 读写命中
                            state <= IDLE;
                        else if (miss & clean) // 读写缺失干净块，直接读内�??
                            state <= RM;
                        else if (miss & dirty) // 读写缺失脏块，先要写内存
                            state <= WM;
                    end
                    else begin
                        state <= IDLE;
                    end
                    from_RM <= 1'b0;
                end
                RM:begin
                    if (cache_data_data_ok) // 记得cache的状态转移依赖于mem传回的ok信息，�?�不能是cpu的ok信息
                        state <= IDLE;
                    else 
                        state <= RM;
                    from_RM <= 1'b1;
                end
                WM:begin
                    if (cache_data_data_ok)
                        state <= RM; // 每次写完脏块，都要再读出正确的数�??
                    else 
                        state <= WM;
                end
            endcase
        end
    end

    //读内�??
    //变量read_req, addr_rcv, read_finish用于构�?�类sram信号�??
    wire read_req;      //�??次完整的读事务，从发出读请求到结�?? 当前是否为RM状�??
    reg addr_rcv;       //地址接收成功(addr_ok)后到结束
    wire read_finish;   //数据接收成功(data_ok)，即读请求结�?? 处于RM状�?�，且已经得到读取的数据
    always @(posedge clk) begin
        addr_rcv <= rst ? 1'b0 :
                    read_req & cache_data_req & cache_data_addr_ok ? 1'b1 : // 【modified】更换read信号为read_req，因为在写回中读指令可能会包括WM过程；写指令也可能包括RM过程
                    read_finish ? 1'b0 : 
                    addr_rcv;
    end
    assign read_req = state==RM;
    assign read_finish = read_req & cache_data_data_ok; // 更改为read_req，原因同�??

    //写内�??
    wire write_req;     // 是否处于WM状�??
    reg waddr_rcv;      // 处于WM状�?�，且地�??已经得到mem确认
    wire write_finish;  // 处于WM状�?�，且data已经写入mem 
    always @(posedge clk) begin
        waddr_rcv <= rst ? 1'b0 :
                     write_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                     write_finish ? 1'b0 : waddr_rcv;
    end
    assign write_req = state==WM;
    assign write_finish = write_req & cache_data_data_ok;

    //output to mips core
    // 命中就读选中路的cache line；否则就读从mem取回的数�??
    assign cpu_data_rdata   = hit ? c_block[c_way] : cache_data_rdata;
    // 地址的确认：不论读写，分两种情况
    // 命中：即cpu有数据请求且hit真，直接确认
    // 缺失：即cache有数据请求cache_data_req(miss�??)并且还未收到数据，当在读mem�??(read_req�??)，cache得到了mem发来的ok时再更新cpu的ok
    assign cpu_data_addr_ok = (cpu_data_req & hit) | (cache_data_req & cache_data_addr_ok & read_req); // 【m？�??
    // 数据的确认：不论读写，分两种情况
    // 命中：直接确认数�??
    // 缺失：等到读mem时，并且mem确认数据已经写入或�?�返回时，即cache得到ok，再确认cpu的ok
    assign cpu_data_data_ok = (cpu_data_req & hit) | (cache_data_data_ok & read_req); // 【m�??

    //output to axi interface
    // 当是数据请求时，req要拉高；而当addr被mem确认，cache的req就要拉低�??
    assign cache_data_req   = read_req & ~addr_rcv | write_req & ~waddr_rcv;
    // 写请求信号的传�?�，只有处于WM状�?�才写�?��?�不是cpu_data_wr信号真就�??
    assign cache_data_wr    = write_req; // 【m�??
    // 确定数据的有效字�??
    // 读指令：有效字节�??4B
    // 写指令：根据sb、sh确定不同的有效字�??
    assign cache_data_size  = cpu_data_size;
    // 根据读内存和写内存传递不同的地址
    // 读内存：则读mem的地�??是当前cpu_data_addr（新地址�??
    // 【m】写内存：不管什么时候写内存，都以为�??把脏数据写回内存，则写mem的地�??是cache中存的脏数据的地�??（旧地址�??
    assign cache_data_addr  = cache_data_wr ? {c_tag[c_way], index, offset}:
                                                cpu_data_addr;
    // 对应上一个assign，要写回的数据必然是原来cache line的数�??
    assign cache_data_wdata = c_block[c_way];

    //写入Cache
    //保存地址中的tag, index，防止addr发生改变
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

    //根据地址低两位和size，生成写掩码（针对sb，sh等不是写完整�??个字的指令）�??4位对�??1个字�??4字节）中每个字的写使�??
    assign write_mask = cpu_data_size==2'b00 ?
                            (cpu_data_addr[1] ? (cpu_data_addr[0] ? 4'b1000 : 4'b0100):
                                                (cpu_data_addr[0] ? 4'b0010 : 4'b0001)) :
                            (cpu_data_size==2'b01 ? (cpu_data_addr[1] ? 4'b1100 : 4'b0011) : 4'b1111);

    //掩码的使用：位为1的代表需要更新的�??
    //位拓展：{8{1'b1}} -> 8'b11111111
    //new_data = old_data & ~mask | write_data & mask

    // if (c_way==2'b00) begin
    //     assign write_cache_data = cache_block0[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};
    // end


    // else if(c_way==2'b01)begin
    //     assign write_cache_data = cache_block1[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};
    // end

    // else if(c_way==2'b10)begin
    //     assign write_cache_data = cache_block2[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};
    // end

    // else if(c_way==2'b11)begin
    //     assign write_cache_data = cache_block3[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};
    // end


    assign write_cache_data = 
    (c_way == 2'b00) ?cache_block0[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} : 
    (c_way == 2'b01) ? cache_block1[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} : 
    (c_way == 2'b10) ? cache_block2[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} : 
    (c_way == 2'b11) ? cache_block3[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} : 32'b0;




    // assign write_cache_data = cache_block[index][c_way] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
    //                           cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};


    wire is_IDLE = state==IDLE; // 【n】标识可以更新cache使用情况�??
    integer t, w;
    always @(posedge clk) begin
        if(rst) begin
            // 双层for循环遍历每个set里的每一�??
            // for(t=0; t<CACHE_DEEPTH; t=t+1) begin
            //     for (w=0; w<4; w=w+1) begin
            //         cache_valid[t][w] <= 0; // 刚开始将Cache置为无效
            //         cache_dirty[t][w] <= 0; // 也全是干�??�??
            //     end
            //     //【n�?? tree初始化为000
            //     tree_table[t] <= 3'b000;
                
            //       cache_valid0[t]<= 0;
            //       cache_valid1[t]<= 0;
            //       cache_valid2[t]<= 0;
            //       cache_valid3[t]<= 0;

            //       cache_dirty0[t] <= 0;
            //       cache_dirty1[t] <= 0;
            //       cache_dirty2[t] <= 0;
            //       cache_dirty3[t] <= 0;


            // end
           cache_valid0 <= '{default: '0};
           cache_valid1 <= '{default: '0};
           cache_valid2 <= '{default: '0};
           cache_valid3 <= '{default: '0};

           cache_dirty0 <= '{default: '0};
           cache_dirty1 <= '{default: '0};
           cache_dirty2 <= '{default: '0};
           cache_dirty3 <= '{default: '0};

            tree_table <= '{default: '0};
        end


        else begin
            // 读缺失会写cache line
            if(read_finish) begin //读缺失，访存结束�??

                if (c_way==2'b00) begin
                    cache_valid0[index_save] <= 1'b1; // 将Cache line置为有效
                    cache_dirty0[index_save] <= 1'b0; // 刚从内存中读取的数据�??定是干净�??
                    cache_tag0  [index_save] <= tag_save; // 用当前指令的tag更新cache_tag
                    cache_block0[index_save] <= cache_data_rdata; // 读到的数据写入Cache line
                end

                else if (c_way==2'b01) begin
                    cache_valid1[index_save] <= 1'b1; // 将Cache line置为有效
                    cache_dirty1[index_save] <= 1'b0; // 刚从内存中读取的数据�??定是干净�??
                    cache_tag1  [index_save] <= tag_save; // 用当前指令的tag更新cache_tag
                    cache_block1[index_save] <= cache_data_rdata; // 读到的数据写入Cache line
                end

                else if (c_way==2'b10) begin
                    cache_valid2[index_save] <= 1'b1; // 将Cache line置为有效
                    cache_dirty2[index_save] <= 1'b0; // 刚从内存中读取的数据�??定是干净�??
                    cache_tag2  [index_save] <= tag_save; // 用当前指令的tag更新cache_tag
                    cache_block2[index_save] <= cache_data_rdata; // 读到的数据写入Cache line
                end

                else if (c_way==2'b11) begin
                    cache_valid3[index_save] <= 1'b1; // 将Cache line置为有效
                    cache_dirty3[index_save] <= 1'b0; // 刚从内存中读取的数据�??定是干净�??
                    cache_tag3  [index_save] <= tag_save; // 用当前指令的tag更新cache_tag
                    cache_block3[index_save] <= cache_data_rdata; // 读到的数据写入Cache line
                end
            end


            // 写指令会写cache line（不论命中还是缺失）
            // 写命中：直接更新
            // 写缺失：前一个状态必须是RM才可以更新，即必须读到了新的数据才能拿来更新cache line
            else if(write & is_IDLE & (hit | from_RM)) begin
                // cache_dirty[index][c_way] <= 1'b1; // 更改cache line �??要置为脏�??
                // cache_block[index][c_way] <= write_cache_data; // 写入Cache line，使用index而不是index_save【？�??
                if (c_way==2'b00) begin
                    cache_dirty0[index] <= 1'b1; // 更改cache line �??要置为脏�??
                    cache_block0[index] <= write_cache_data; // 写入Cache line，使用index而不是index_save【？�??
                end

                else if (c_way==2'b01) begin
                    cache_dirty1[index] <= 1'b1; // 更改cache line �??要置为脏�??
                    cache_block1[index] <= write_cache_data; // 写入Cache line，使用index而不是index_save【？�??
                end

                else if (c_way==2'b10) begin
                    cache_dirty2[index] <= 1'b1; // 更改cache line �??要置为脏�??
                    cache_block2[index] <= write_cache_data; // 写入Cache line，使用index而不是index_save【？�??
                end

                else if (c_way==2'b11) begin
                    cache_dirty3[index] <= 1'b1; // 更改cache line �??要置为脏�??
                    cache_block3[index] <= write_cache_data; // 写入Cache line，使用index而不是index_save【？�??
                end
                

            end


            // 【m】出现读指令和写指令，就要更新cache line的最近使用情�??
            if((read | write) & is_IDLE & (hit | from_RM)) begin
                if (c_way[1] == 1'b0)
                    {tree_table[index][2], tree_table[index][1]} <= ~c_way;

                else
                    {tree_table[index][2], tree_table[index][0]} <= ~c_way;

            end
        end
    end
endmodule