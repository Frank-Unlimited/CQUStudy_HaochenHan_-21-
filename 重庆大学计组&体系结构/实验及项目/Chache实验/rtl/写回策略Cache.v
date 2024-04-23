module d_cache (
    input wire clk, rst,
    //mips core
    input         cpu_data_req     ,    //�Ƿ��Ƕ�д����
    input         cpu_data_wr      ,    //��ǰ������д����
    input  [1 :0] cpu_data_size    ,
    input  [31:0] cpu_data_addr    ,    //cpu�ṩ�ĵ�ַ
    input  [31:0] cpu_data_wdata   ,    //cpuд������
    output [31:0] cpu_data_rdata   ,    //Cache���ظ�mips������
    output        cpu_data_addr_ok ,    //Cache�ɹ����ص�ַ��mips
    output        cpu_data_data_ok ,    //Cache�ɹ��������ݸ�mips

    //axi interface
    output         cache_data_req     , //cache���͵Ķ�д����
    output         cache_data_wr      , //cache���͵�д����
    output  [1 :0] cache_data_size    ,
    output  [31:0] cache_data_addr    ,  //����ĵ�ַ
    output  [31:0] cache_data_wdata   ,  //д����ڴ������
    input   [31:0] cache_data_rdata   ,     //�ڴ淵�ظ�cache������
    input          cache_data_addr_ok ,    //���ڴ��з��أ��Ƿ�ɹ��յ�����
    input          cache_data_data_ok      //���ڴ��з��أ�����Ƕ����󣬱�ʾ�ӷ���cache �����ݣ������д���󣬴���д�����ݳɹ���


);
    //Cache����
    parameter  INDEX_WIDTH  = 10, OFFSET_WIDTH = 2;  //ָ��indexΪ10λ��offset2λ
    localparam TAG_WIDTH    = 32 - INDEX_WIDTH - OFFSET_WIDTH; //tag20λ
    localparam CACHE_DEEPTH = 1 << INDEX_WIDTH;  //cash ����� 1024
    
    //Cache�洢��Ԫ
    reg                 cache_valid [CACHE_DEEPTH - 1 : 0]; //��Чλ
    reg                 cache_dirty [CACHE_DEEPTH - 1 : 0]; //��ʾ�Ƿ�����λ
    reg [TAG_WIDTH-1:0] cache_tag   [CACHE_DEEPTH - 1 : 0]; //tag
  
    reg [31:0]          cache_block [CACHE_DEEPTH - 1 : 0];//һ���֣�32λ������

    //���ʵ�ַ�ֽ�
    wire [OFFSET_WIDTH-1:0] offset;
    wire [INDEX_WIDTH-1:0] index;
    wire [TAG_WIDTH-1:0] tag;
    
    assign offset = cpu_data_addr[OFFSET_WIDTH - 1 : 0];
    assign index = cpu_data_addr[INDEX_WIDTH + OFFSET_WIDTH - 1 : OFFSET_WIDTH];
    assign tag = cpu_data_addr[31 : INDEX_WIDTH + OFFSET_WIDTH];

    //����Cache line
    wire c_valid;
    wire c_dirty; // �Ƿ��޸Ĺ�����飩
    wire [TAG_WIDTH-1:0] c_tag;
    wire [31:0] c_block;

    assign c_valid = cache_valid[index];
    assign c_dirty = cache_dirty[index]; // �Ƿ��޸Ĺ�����飩
    assign c_tag   = cache_tag  [index];
    assign c_block = cache_block[index];

    //�ж��Ƿ�����
    wire hit, miss;
    assign hit = c_valid & (c_tag == tag);  //cache line��validλΪ1����tag���ַ��tag���
    assign miss = ~hit;

    //cpu�����ǲ��Ƕ���д����
    wire read, write;
    assign write = cpu_data_wr;
    assign read = cpu_data_req & ~write; // �Ƕ�д�����Ҳ���д����

    //cache��ǰλ���Ƿ�dirty
    wire dirty, clean;
    assign dirty = c_dirty;
    assign clean = ~dirty;

    //״̬ת�Ʊ仯
    parameter IDLE = 2'b00, RM = 2'b01, WM = 2'b11;
    reg [1:0] state;
    //�����Ƿ��ڶ��ڴ�״̬������1��ʱ�����ڻָ����ͱ���ź�һ�������ж��Ƿ��ǴӶ��ڴ�״̬�ص�����״̬
    reg in_RM;
    
    //״̬ת��
    always @(posedge clk) begin
        if(rst) begin
            state <= IDLE;
            in_RM <= 1'b0;
        end
        else begin
            case(state)
                IDLE:   begin            //��ǰλ����������IDLE״̬�����û���У��ж��Ƿ�Ϊ��λ����ǰλ��������������д�ڴ�״̬�������������ڴ�״̬
                        state <= cpu_data_req & hit ? IDLE :
                                 cpu_data_req & miss & dirty  ? WM :
                                 cpu_data_req & miss & clean  ? RM : IDLE;
                        in_RM <= 1'b0;
                        end
                RM:     begin
                        state <= cache_data_data_ok ? IDLE : RM;          //���ڴ����ص�IDLE
                        in_RM <= 1'b1;
                        end
                WM:     state <= cache_data_data_ok ? RM : WM;              //д�ڴ����ص�RM���ڴ�״̬
            endcase
        end
    end

    //���ڴ�
    //isRM, addr_rcv, read_finish���ڹ�����sram�ź�
    wire read_req;      //1�������Ķ����񣬴ӷ��������󵽽���
    reg addr_rcv;       //��ַ���ճɹ�(addr_ok)�󵽽���  
    wire read_finish;   //���ݽ��ճɹ�(data_ok)�������������
    always @(posedge clk) begin
        addr_rcv <= rst ? 1'b0 :          //�����ڶ��ڴ�״̬�������ڴ�Ķ�д���󣬲����ڴ�ɹ��յ���ַʱ����1
                    read_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                    read_finish ? 1'b0 : addr_rcv;
    end
    assign read_req = state==RM;
    assign read_finish = read_req & cache_data_data_ok;

    //д�ڴ�
    wire write_req;     
    reg waddr_rcv;      
    wire write_finish;   
    always @(posedge clk) begin
        //������д�ڴ�״̬�������ڴ�Ķ�д���󣬲����ڴ�ɹ��յ���ַʱ����1
        waddr_rcv <= rst ? 1'b0 :
                     write_req & cache_data_req & cache_data_addr_ok ? 1'b1 :
                     write_finish ? 1'b0 : waddr_rcv;
    end
    assign write_req = state==WM;
    assign write_finish = write_req & cache_data_data_ok;

    //output to mips core
    assign cpu_data_rdata   = hit ? c_block : cache_data_rdata;    //cache�ɹ����ص�ַ�����ݸ�mips
    assign cpu_data_addr_ok = cpu_data_req & hit | cache_data_req & cache_data_addr_ok & read_req;  //��cpu�ж�д���������У�������Ҫ���ڴ沢���ڴ��յ���ַʱ����cpu����ɹ�ȡ���˵�ַ
    assign cpu_data_data_ok = cpu_data_req & hit | cache_data_data_ok & read_req; //�����У����߶��ڴ�ʱ�ڴ�������ݣ���cpu�������ݾ�λ
    
    //output to axi interface
    assign cache_data_req   = read_req & ~addr_rcv | write_req & ~waddr_rcv;
    assign cache_data_wr    = write_req;   //���ڴ���д����
    assign cache_data_size  = cpu_data_size;
    assign cache_data_addr  = cache_data_wr ? {c_tag, index, offset} : cpu_data_addr;  //�����������д����ʱ���ѵ�ǰ��ַ������ַ�������ڴ棬������ʱ����mips����ĵ�ַ�����ڴ�
    assign cache_data_wdata = c_block;     //д�������ֻ������������


    //д��Cache
    //�����ַ�е�tag index��
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

    //���ݵ�ַ����λ��size������д���루���sb��sh�Ȳ���д����1���ֵ�ָ�4λ��Ӧ1���֣�4�ֽڣ���ÿ���ֵ�дʹ��
    assign write_mask = cpu_data_size==2'b00 ?
                            (cpu_data_addr[1] ? (cpu_data_addr[0] ? 4'b1000 : 4'b0100):
                                                (cpu_data_addr[0] ? 4'b0010 : 4'b0001)) :
                            (cpu_data_size==2'b01 ? (cpu_data_addr[1] ? 4'b1100 : 4'b0011) : 4'b1111);

    //�����ʹ�ã�λΪ1�Ĵ�����Ҫ���µ�
    //λ��չ��{8{1'b1}} -> 8'b11111111

    assign write_cache_data = cache_block[index] & ~{{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}} | 
                              cpu_data_wdata & {{8{write_mask[3]}}, {8{write_mask[2]}}, {8{write_mask[1]}}, {8{write_mask[0]}}};


    //��ǰ�Ƿ��ڿ���״̬
    wire isIDLE = state==IDLE;
    integer t;
    always @(posedge clk) begin
        if(rst) begin
            for(t=0; t<CACHE_DEEPTH; t=t+1) begin   //�տ�ʼ��Cache��Ϊ��Ч
                cache_valid[t] <= 0;
                cache_dirty[t] <= 0;  //��ʼ��dirtyΪ0
            end
        end
        else begin
            if(read_finish) begin //���ڴ�����󣬽����ݷ���cache
                cache_valid[index_save] <= 1'b1;             //��Cache line��Ϊ��Ч
                cache_dirty[index_save] <= 1'b0; // ���ڴ��ȡ��������clean
                cache_tag  [index_save] <= tag_save;
                cache_block[index_save] <= cache_data_rdata; //д��Cache line
            end
            //д�������ʱ����cache�е����ݽ����޸�
            //��д���в��һص�����״̬ʱ����write & isIDLE & hit
            //��дȱʧ���ҴӶ��ڴ�״̬�ص�����״̬write & isIDLE & in_RM
            //in_RM����1��ʱ�����ڻָ���in_RM��isIDLE�������Ϳ����ж��Ƿ��ǴӶ�״̬�ص�����״̬
            else if(write & isIDLE & (hit | in_RM)) begin   
                cache_dirty[index] <= 1'b1; // �������ݣ������޸�dirty
                cache_block[index] <= write_cache_data;      //д��Cache line��ʹ��index������index_save
            end
        end
    end
endmodule