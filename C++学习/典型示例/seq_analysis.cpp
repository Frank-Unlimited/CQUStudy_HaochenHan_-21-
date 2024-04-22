#include<bits/stdc++.h>
#define FOR(n) for(int i=0;i<n;i++)
#define INF 0x3f3f3f3f
using namespace std;

class ziMu {
	public:
		ziMu(char v, int n){
			this->val = v;
			this->num = n;
		}
		char val;
		int num;
};

bool Com(ziMu a,ziMu b){
	return a.num>=b.num;
}

void printPinDu(vector<ziMu>& v, int length){
	cout<<"单词：出现次数     频度"<<endl;
	for(auto i:v){
		cout<<i.val<<setw(10)<<i.num<<setw(20)<<(double)i.num/length<<endl;
	}
}

int main(){
	string str = "UZ QSO VUOHXMOPV GPOZPEVSG ZWSZ OPFPESX UDBMETSX AIZ VUEPHZ HMDZSHZO WSFP APPD TSVP QUZW YMXUZUHSX EPYEPOPDZSZUFPO MB ZWP FUPZ HMDJ UD TMOHMQ";
	vector<ziMu> pinDu;
 	vector<char> freq{'E','T','A','O','N','I','S','R','H','L','D','C','U','P','F','M','W','Y','B','G','V','K','Q','X','J','Z'};
	for(int i=0;i<26;i++){
		ziMu tmp((char)(i+'A'),0);
		pinDu.push_back(tmp);
	}
	for(auto i:str)
		pinDu[i-'A'].num++;
	sort(pinDu.begin(),pinDu.end(),Com);

	printPinDu(pinDu, str.length());

	map<char,char> m;
	m['P'] = 'E';
	m['Z'] = 'T';
	m['W'] = 'H';
	m['S'] = 'A';
	string ans = "";
	for(char i:str){
		if(m.count(i)) {
			ans += '[';
			ans += m[i];
			ans += ']';
		}
		else ans += i;
	}
	cout<<ans<<endl;

	ans = "IT WAS DISCLOSED YESTERDAY THAT SEVERAL INFORMAL BUT DIRECT CONTACTS HAVE BEEN MADE WITH POLITICAL REPRESENTATIVES OF THE VIET CONG IN MOSCO";
	string zimu = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	for(auto i:zimu) m[i] = i;
	for(int i=0;i<str.length();i++) m[str[i]] = ans[i];
	for(auto i:m) cout<<"   "<<i.first<<" : "<<i.second<<endl;

}
