#include<bits/stdc++.h>
#define FOR(n) for(int i=0;i<n;i++)
#define INF 0x3f3f3f3f
using namespace std;

//��Կ���ɺ���
std::vector<std::string> Get_Key(int K, int n) {
    std::vector<std::string> Kis; // ���ڴ洢ÿ�ֵ���Կ
    std::string K_str = std::bitset<sizeof(int) * 32>(K).to_string(); // ����Կת��Ϊ�������ַ���
    int K_size = K_str.length() / n; // ����ÿ�ֵ���Կ����

    // ��ԭʼ��Կ�ֳɶ����
    for (int j = 0; j < n; ++j) {
        std::string block = K_str.substr(j * K_size, K_size);
        Kis.push_back(block);
    }

//	cout<<"���������Կ��"<<K<<endl;
//	cout<<"����Կ���У�"<<endl;
//	for(auto i:Kis){
//		cout<<i<<" ";
//	}
//	cout<<endl;

    return Kis;
}

// ���ַ���ת��Ϊ�����Ʊ���
std::string str2bin(const std::string& message) {
    std::string bin_M;
    for (char c : message) {
        bin_M += std::bitset<8>(c).to_string();
    }
    return bin_M;
}

// �������Ʊ���ת��Ϊ�ַ���
std::string bin2str(const std::string& bin_M) {
    std::string message;
    for (size_t i = 0; i < bin_M.length(); i += 8) {
        std::string bin_substr = bin_M.substr(i, 8);
        char c = static_cast<char>(std::bitset<8>(bin_substr).to_ulong());
        message += c;
    }
    return message;
}

// Feistel �ֺ��� F
std::string F(const std::string& R, string K) {
//    std::string R_hashed = std::to_string(std::stoll(R, nullptr, 2) ^ stoll(K,nullptr,2));
	std::string R_hashed = bitset<32>(std::stoll(R, nullptr, 2) ^ stoll(K,nullptr,2)).to_string();
    return R_hashed;
}

// Feistel ���ܺ���
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

// Feistel ���ܺ���
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
    int K = std::rand(); // ���������Կ
//    cout<<"���������Կ��"<<K<<endl;
    int rounds = 16; // ��������
    int duan = 4;   //�ֶμ���

	cout<<"���ģ�"<<M<<endl;
	string C_encode = "";
	for(int i=0;i<duan;i++){
		int n = M.length()/duan;
		string M_tmp = i==duan-1?M.substr(i*n) : M.substr(i*n,n);
		C_encode += feistel_encrypt(M_tmp, K, rounds);
	}
    cout << "���ܺ������: " << C_encode << std::endl;

    string M_decode = "";
    for(int i=0;i<duan;i++){
		int n = C_encode.length()/duan;
		string C_tmp = i==duan-1?C_encode.substr(i*n) : C_encode.substr(i*n,n);
		M_decode += feistel_decrypt(C_tmp, K, rounds);
	}
    std::cout << "���ܺ������: " << M << std::endl;

    return 0;
}

