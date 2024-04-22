#include<bits/stdc++.h>
#define FOR(n) for(int i=0;i<n;i++)
#define INF 0x3f3f3f3f
using namespace std;

//密钥生成函数
std::vector<std::string> Get_Key(int K, int n) {
    std::vector<std::string> Kis; // 用于存储每轮的密钥
    std::string K_str = std::bitset<sizeof(int) * 32>(K).to_string(); // 将密钥转换为二进制字符串
    int K_size = K_str.length() / n; // 计算每轮的密钥长度

    // 将原始密钥分成多个块
    for (int j = 0; j < n; ++j) {
        std::string block = K_str.substr(j * K_size, K_size);
        Kis.push_back(block);
    }

//	cout<<"生成随机密钥："<<K<<endl;
//	cout<<"子密钥序列："<<endl;
//	for(auto i:Kis){
//		cout<<i<<" ";
//	}
//	cout<<endl;

    return Kis;
}

// 将字符串转换为二进制编码
std::string str2bin(const std::string& message) {
    std::string bin_M;
    for (char c : message) {
        bin_M += std::bitset<8>(c).to_string();
    }
    return bin_M;
}

// 将二进制编码转换为字符串
std::string bin2str(const std::string& bin_M) {
    std::string message;
    for (size_t i = 0; i < bin_M.length(); i += 8) {
        std::string bin_substr = bin_M.substr(i, 8);
        char c = static_cast<char>(std::bitset<8>(bin_substr).to_ulong());
        message += c;
    }
    return message;
}

// Feistel 轮函数 F
std::string F(const std::string& R, string K) {
//    std::string R_hashed = std::to_string(std::stoll(R, nullptr, 2) ^ stoll(K,nullptr,2));
	std::string R_hashed = bitset<32>(std::stoll(R, nullptr, 2) ^ stoll(K,nullptr,2)).to_string();
    return R_hashed;
}

// Feistel 加密函数
std::string feistel_encrypt(const std::string& M, int K, int rounds) {
    std::string bin_M = str2bin(M);
    int half_size = bin_M.size() / 2;
    string L = bin_M.substr(0, half_size);
    string R = bin_M.substr(half_size);
    vector<string> Kis = Get_Key(K,rounds);

    for (int round = 0; round < rounds; ++round) {
//    	cout<<round<<endl;
        std::string new_R = R;
        R = std::bitset<32>(std::stoll(L, nullptr, 2) ^ std::stoll(F(R, Kis[round]), nullptr, 2)).to_string();
        L = new_R;
    }

    return L + R;
}

// Feistel 解密函数
std::string feistel_decrypt(const std::string& C, int K, int rounds) {
    std::string L = C.substr(0, C.size() / 2);
    std::string R = C.substr(C.size() / 2);
    vector<string> Kis = Get_Key(K,rounds);

    for (int round = 0; round < rounds; ++round) {
		std::string new_L = L;
		L = std::bitset<32>(std::stoll(R, nullptr, 2) ^ std::stoll(F(L, Kis[rounds-1-round]), nullptr, 2)).to_string();
        R = new_L;
    }

    return bin2str(L + R);
}

int main() {
    string M = "CQUINFORMATIONSECURITYEXP";

	srand((unsigned int)time(NULL));
    int K = std::rand(); // 生成随机密钥
//    cout<<"生成随机密钥："<<K<<endl;
    int rounds = 16; // 加密轮数
    int duan = 4;   //分段加密

	cout<<"明文："<<M<<endl;
	string C_encode = "";
	for(int i=0;i<duan;i++){
		int n = M.length()/duan;
		string M_tmp = i==duan-1?M.substr(i*n) : M.substr(i*n,n);
		C_encode += feistel_encrypt(M_tmp, K, rounds);
	}
    cout << "加密后的密文: " << C_encode << std::endl;

    string M_decode = "";
    for(int i=0;i<duan;i++){
		int n = C_encode.length()/duan;
		string C_tmp = i==duan-1?C_encode.substr(i*n) : C_encode.substr(i*n,n);
		M_decode += feistel_decrypt(C_tmp, K, rounds);
	}
    std::cout << "解密后的明文: " << M << std::endl;

    return 0;
}

