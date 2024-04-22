#include<bits/stdc++.h>
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

int main(){
 ofstream ofs("Badminton Court.txt",ios::out|ios::trunc);
	for(int i=1;i<=7;i++){
		for(int j=1;j<=6;j++){
			for(int k=0;k<24;k++){
				ofs<<0<<" ";
			}
			ofs<<endl;
		}
		ofs<<endl<<endl;
	}
	ofs.close();
}
